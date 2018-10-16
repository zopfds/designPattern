package heartbeat;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 心跳检测客户端S
 */
public class HeartBeatClient {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatClient.class);

    private static final String serverAddress = "192.168.0.104";

    private static final int port = 20991;

    private static class SendThread implements Runnable{

        private HeartBeatData heartBeatData;

        private long sleepTime;

        private volatile boolean start;

        public SendThread(HeartBeatData heartBeatData, long sleepTime) {
            this.heartBeatData = heartBeatData;
            this.sleepTime = sleepTime;
            this.start = false;
        }

        @Override
        public void run() {

            while(start){

                logger.info("[HeartBeatClient.SendThread] will send data to server! heartBeatData = {} , currentTime = {}" , heartBeatData , System.currentTimeMillis());

                BufferedWriter writer = null;

                try{
                    Socket socket = new Socket(serverAddress , port);

                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    writer.write(JSON.toJSONString(heartBeatData));

                    writer.flush();

                }catch(Exception e){
                    logger.error("[HeartBeatClient.SendThread] send error! heartBeatData = {}" , heartBeatData , e);
                }finally {
                    try {
                        if(writer != null) {
                            writer.close();
                        }
                    } catch (IOException e) {
                        logger.error("[HeartBeatClient.SendThread] writer close error! heartBeatData = {}" , heartBeatData , e);
                    }
                }

                try {
                    Thread.currentThread().sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.error("[HeartBeatClient.SendThread] writer close error! heartBeatData = {}" , heartBeatData , e);
                }
            }
        }

        public void setStart(){
            this.start = true;
        }

        public void stop(){
            this.start = false;
        }
    }

    public void startSendThread(HeartBeatData heartBeatData , long sleepTime){
        SendThread sendThread = new SendThread(heartBeatData , sleepTime);
        sendThread.setStart();
        new Thread(sendThread).start();
    }

    public static void main(String[] args){
        HeartBeatClient heartBeatClient = new HeartBeatClient();

        HeartBeatData heartBeatData = new HeartBeatData("testId" , "testIp" , "testClient" , "testData");
        heartBeatClient.startSendThread(heartBeatData , 20 * 1000L);

        try {
            Thread.currentThread().sleep(50 * 60 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
