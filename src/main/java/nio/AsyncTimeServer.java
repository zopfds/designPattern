package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * java nio2 时间服务器简单例子
 */
public class AsyncTimeServer {

    private static class AsyncTimeServerHandler implements Runnable{

        //端口号
        private int port;
        //
        public CountDownLatch latch;
        //异步serversocketchannel
        private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

        /**
         * 构造方法
         * @param port
         */
        public AsyncTimeServerHandler(int port){
            this.port = port;
            try{
                asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
                System.out.println("AsyncTime server started at port : " + port);
            }catch(IOException e){
                e.printStackTrace();
            }
        }



        @Override
        public void run() {
            latch = new CountDownLatch(1);
            doAccept();
            System.out.println("AsyncTime server started to accpet client channel!");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 绑定接受事件处理器到CHANNEL
         */
        public void doAccept(){
            asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
        }
    }

    /**
     * 链接事件回调处理器
     */
    static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel , AsyncTimeServerHandler>{

        /**
         * 完成则分配BYTEBUFFER,将读取事件回调处理器绑定到channel上
         * @param result
         * @param attachment
         */
        @Override
        public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
            //链接到客户端后需要再次注册链接事件回调处理器以接受新的客户端的链接
            attachment.asynchronousServerSocketChannel.accept(attachment , this);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //将byteBuffer作为attachement，注册读取事件处理器到channel上
            result.read(byteBuffer , byteBuffer , new ReadCompletionHandler(result));
        }

        /**
         * 链接失败则让COUNTDOWNLATCH COUNTDOWN 服务器退出
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            attachment.latch.countDown();
        }
    }

    /**
     * 读取事件回调处理器
     */
    static class ReadCompletionHandler implements CompletionHandler<Integer , ByteBuffer>{

        private AsynchronousSocketChannel channel;

        public ReadCompletionHandler(AsynchronousSocketChannel channel){
            if(this.channel == null){
                this.channel = channel;
            }
        }

        /**
         * 读取事件响应方法
         * @param result
         * @param attachment
         */
        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            try{
                String req = new String(bytes , "utf-8");
                System.out.println("Async Time Serve receive data : " + req);
                String response = "get time".equalsIgnoreCase(req) ? new Date(System.currentTimeMillis()).toString() : req;
                doWrite(response);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        /**
         * 写入响应到channel中
         * @param response
         */
        private void doWrite(String response) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer =  ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(attachment.hasRemaining()){
                        channel.write(writeBuffer , writeBuffer , this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("Async Time Server write data failed! close channel!");
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

    }

    public static void main(String[] args){
        int port = 8090;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        Thread serverThread = new Thread(timeServer);
        serverThread.setDaemon(false);
        serverThread.start();
    }
}
