package nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;

/**
 * 多线程阻塞io服务器
 * 阶段1 主线程处理客户端链接，阶段2 对每一个到来的io请求开一个线程处理
 * 后续阶段2可开启线程池来优化处理
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/23
 */
public class MultiThreadBioServer {
    public static void main(String[] args){
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(1234));

        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        try{
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(){
                    @Override
                    public void run() {
                        InputStream is = null;
                        try{
                           is = socket.getInputStream();
                           System.out.println("Receive message!");
                       }catch(Exception e){
                        e.printStackTrace();
                       }finally {
                           if(is != null){
                               try {
                                   is.close();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                    }
                }.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
