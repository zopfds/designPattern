package heartbeat;


import com.alibaba.fastjson.JSON;
import org.apache.tools.ant.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 心跳检测服务器
 */
public class HeartBeatServer {

    private static final Logger logger  = LoggerFactory.getLogger(HeartBeatServer.class);

    private static final int port = 20991;

    private static volatile boolean acceptClient = false;

    private static final long expireTime = 30 * 1000L;

    private static final int schedulTime = 10;

    /**
     * 心跳服务检测服务数据
     */
    private static ConcurrentHashMap<String,HeartBeatData> clientServerList = new ConcurrentHashMap<>();

    /**
     * 过期服务处理线程
     */
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 心跳服务处理线程池
     */
    private static ExecutorService dealBeatServerPool = Executors.newFixedThreadPool(5);

    private static class ExpireThread implements Runnable{

        @Override
        public void run() {
            try{
                clientServerList.forEach((key , heartBeatData) -> {
                    synchronized (heartBeatData) {
                        if (heartBeatData.getStatus() != ClientStatusEnum.DOWN.getStatusCode() && isStale(heartBeatData.getLastUpdateTime(), new Date())) {
                            logger.info("[HeartBeatServer.ExpireThread] update client to down! HeartBeatData = {} , time = {}" , heartBeatData , System.currentTimeMillis());
                            heartBeatData.setStatus(ClientStatusEnum.DOWN.getStatusCode());
                        }
                    }
                });
            }catch(Exception e){
                logger.error("[HeartBeatServer.ExpireThread] expire thread run error! time = {}" , System.currentTimeMillis());
            }
        }
    }

    /**
     * 客户端接收线程
     */
    private static class AcceptClientThread implements Runnable{

        @Override
        public void run() {

            while(acceptClient) {

                logger.info("[HeartBeatServer.AcceptClientThread] start accpeted ! time = {}" , System.currentTimeMillis());

                BufferedReader is = null;

                ServerSocket serverSocket = null;

                try {
                    serverSocket = new ServerSocket(port);

                    Socket socket = serverSocket.accept();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String data = "";
                    StringBuffer finalData = new StringBuffer();
                    while((data = is.readLine()) != null){
                        finalData.append(data);
                    }

                    if(finalData != null && !finalData.equals("")){
                        dealBeatServerPool.submit(new HeartBeatDealThread(finalData.toString()));
                    }

                } catch (Exception e) {
                    logger.error("[HeartBeatServer.AcceptClientThread] error accept client data!", e);
                } finally {
                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            logger.error("[HeartBeatServer.AcceptClientThread] close reader failed!", e);
                        }
                    }

                    if(serverSocket != null){
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            logger.error("[HeartBeatServer.AcceptClientThread] close socket failed!", e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 心跳处理线程
     */
    private static class HeartBeatDealThread implements Runnable{

        private final String dataString;

        public HeartBeatDealThread(String dataString) {
            this.dataString = dataString;
        }

        @Override
        public void run() {

            logger.info("-------------[HeartBeatServer.HeartBeatDealThread] thread started! ----------");

            try{
                HeartBeatData heartBeatData = JSON.parseObject(dataString , HeartBeatData.class);

                if(heartBeatData == null || isBlank(heartBeatData.getClientId())){
                    logger.warn("[HeartBeatServer.HeartBeatDealThread] parse data is null or illeagal! dataString = {}" , dataString);
                    return;
                }

                Date createOrUpdateTime = new Date();
                heartBeatData.setCreateTime(createOrUpdateTime);
                heartBeatData.setLastUpdateTime(createOrUpdateTime);

                if(!register(heartBeatData)){

                    HeartBeatData oldData = clientServerList.get(heartBeatData.getClientId());

                    if(oldData == null){
                        logger.warn("[HeartBeatServer.HeartBeatDealThread] update heart beat time error! dataString = {}" , dataString);
                        return;
                    }

                    if(!checkPass(oldData , heartBeatData)){
                        logger.warn("[HeartBeatServer.HeartBeatDealThread] check same client failed! dataString = {} , oldData = {}" , dataString , oldData);
                        return;
                    }

                    if(!checkAndUpdate(oldData , heartBeatData)){
                        logger.warn("[HeartBeatServer.HeartBeatDealThread] update time older than the old time! dataString = {} , oldData = {}" , dataString , oldData);
                        return;
                    }
                }

            }catch(Exception e){
                logger.error("[HeartBeatServer.HeartBeatDealThread] error deal client data! dataString = {}" , dataString , e);
            }

            logger.info("-------------[HeartBeatServer.HeartBeatDealThread] thread ended! ----------");
        }
    }

    /**
     * 注册客户端
     * @param heartBeatData
     */
    private static boolean register(HeartBeatData heartBeatData){
        return clientServerList.putIfAbsent(heartBeatData.getClientId() , heartBeatData) == null;
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    private static final boolean isBlank(String s){
        return s == null ? true : "".equals(s);
    }

    /**
     * 设置时间和相关属性
     * @param oldData
     * @param newData
     */
    private static final void setProperties(HeartBeatData oldData , HeartBeatData newData){
        if(oldData != null && newData != null){
            oldData.setIp(newData.getIp());
            oldData.setClientName(newData.getClientName());
            oldData.setLastUpdateTime(newData.getLastUpdateTime());

            if(isStale(oldData.getLastUpdateTime() , new Date())){
                logger.info("[HeartBeatServer.setProperties] new update time is stale, will update status to down! oldData = {}" , oldData);
                oldData.setStatus(ClientStatusEnum.DOWN.getStatusCode());
            }else{
                logger.info("[HeartBeatServer.setProperties] new update time is new, will update status to up! oldData = {}" , oldData);
                oldData.setStatus(ClientStatusEnum.UP.getStatusCode());
            }
        }
    }

    /**
     * 检查更新时间是否比更新前时间更小
     * @param oldData
     * @param heartBeatData
     * @return
     */
    private static final boolean checkAndUpdate(HeartBeatData oldData, HeartBeatData heartBeatData) {
        synchronized (oldData){
            if(heartBeatData.getLastUpdateTime().after(oldData.getLastUpdateTime())){
                setProperties(oldData , heartBeatData);
                logger.info("[HeartBeatServer.checkAndUpdateTime]update client time success! oldData = {} , heartBeatData = {}" , oldData , heartBeatData);
                return true;
            }else{
                logger.info("[HeartBeatServer.checkAndUpdateTime]update time is older than the old time , will not update! oldData = {} , heartBeatData = {}" , oldData , heartBeatData);
                return false;
            }
        }
    }

    /**
     * 检测是否为注册的客户端
     * @return
     */
    private static final boolean checkPass(HeartBeatData oldData , HeartBeatData newData){
        return oldData.getClientId().equals(newData.getClientId()) && oldData.getHeartBeatData().equals(newData.getHeartBeatData());
    }

    /**
     * 判断是否超时
     * @return
     */
    private static final boolean isStale(Date oldTime , Date newTime){
        return newTime.getTime() - oldTime.getTime() > expireTime;
    }

    public final void start(){
        this.acceptClient = true;
        new Thread(new AcceptClientThread()).start();
        scheduledExecutorService.scheduleAtFixedRate(new ExpireThread(), 0 , schedulTime , TimeUnit.SECONDS);
    }

    public static void main(String[] args){
        HeartBeatServer heartBeatServer = new HeartBeatServer();
        heartBeatServer.start();
        try {
            Thread.currentThread().sleep(30 * 60 * 1000L);
        } catch (InterruptedException e) {
            logger.error("[HeartBeatServer.main] main thread sleep error!",e);
        }
    }

}
