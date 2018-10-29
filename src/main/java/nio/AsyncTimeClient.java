package nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * 异步时间客户端
 */
public class AsyncTimeClient {

    /**
     * 异步事件客户端处理器
     */
    private static class AsyncTimeClientHandler implements CompletionHandler<Void , AsyncTimeClientHandler> , Runnable{

        private AsynchronousSocketChannel channel;

        private String host;

        private int port;

        private CountDownLatch latch;

        private volatile Boolean allWrite = false;

        private volatile String writeString = "get time";

        /**
         * 异步事件客户端处理器构造方法
         * @param host
         * @param port
         */
        public AsyncTimeClientHandler(String host , int port){
            this.host = host;
            this.port = port;

            try {
                channel = AsynchronousSocketChannel.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * 链接完成处理器
         * @param result
         * @param attachment
         */
        @Override
        public void completed(Void result, AsyncTimeClientHandler attachment) {
            byte[] req = writeString.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            writeString = null;
            channel.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            //若没写完则继续写
                            if(attachment.hasRemaining()){
                                channel.write(attachment , attachment , this);
                                //否则读取服务器响应
                            }else{
                                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                                channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer result, ByteBuffer attachment) {
                                        attachment.flip();
                                        byte[] readBytes = new byte[attachment.remaining()];
                                        attachment.get(readBytes);
                                        String response;
                                        try {
                                            response = new String(readBytes , "utf-8");
                                            System.out.println("response from server:" + response);
                                            latch.countDown();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer attachment) {
                                        try {
                                            channel.close();
                                            latch.countDown();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();

                        }
                    });
        }

        @Override
        public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }

        @Override
        public void run() {
            latch = new CountDownLatch(1);
            //链接到服务器
            channel.connect(new InetSocketAddress(host , port));

            System.out.println("client start to connect to server!");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        AsyncTimeClientHandler asyncTimeClientHandler = new AsyncTimeClientHandler("localhost" , 8090);
        Thread clientThread  = new Thread(asyncTimeClientHandler);
        clientThread.setDaemon(false);
        clientThread.start();
    }
}
