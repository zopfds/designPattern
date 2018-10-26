package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * nio实现简单的时间服务器
 */
public class TimeServer implements Runnable{

    /**
     * 多路复用器
     */
    private Selector selector;

    /**
     * serverSocketChannel
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * 是否停止标志
     */
    private volatile boolean stop;

    public void stop(){
        this.stop = true;
    }

    /**
     * 根据端口号创建server
     * @param port
     */
    public void createServer(int port){
        try {
            //开启serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //serverSocketChannel绑定地址和端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //将channel模式设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //开启多路选择器
            selector = Selector.open();
            //创建后马上注册接受客户端链接的事件到多路复用选择器上
            serverSocketChannel.register(selector , SelectionKey.OP_ACCEPT);

            System.out.println("sercer create successed! start to accept client connection!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(!stop){
                //如果没有事件，select操作会阻塞
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                //遍历selectionkey
                while(iterator.hasNext()){
                    //先将selectionkey保存下来，并且从迭代器中移除，然后再处理key,防止重复处理key值
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handle(selectionKey);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭多路复用选择器，具体的channel和pipe等资源会再selector关闭后释放
            if(selector != null){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 具体业务操作
     * @param key
     */
    private void handle(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                System.out.println("start to accept client connection!");
                //接受客户端链接并开启socketchannel来管理客户端来的数据，并且将多路复用器绑定到socketChannel上
                //下面语句执行后，客户端和服务器端即完成了tcp链接
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector , SelectionKey.OP_READ);

                //读请求必定是上一步注册到多路复用器上的，从bytebuffer中读取对应的数据
            }else if(key.isReadable()){

                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                SocketChannel socketChannel = (SocketChannel)key.channel();
                //获取读取的字节数
                int readBytes = socketChannel.read(readBuffer);

                if(readBytes > 0){
                    //反转readBuffer,从写模式转换到读模式
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes , "utf-8");
                    System.out.println("the time server receive the data from client:" + body);
                    //若为获取当前时间的操作，则返回时间
                    handleBody(socketChannel , body);
                    //若读取字节数小于0，关闭链路
                }else if(readBytes < 0){
                    key.cancel();
                    socketChannel.close();
                    //若读取字节数等于0，忽略
                }else{

                }
            }
        }
    }

    /**
     * 向socketchannel中写入时间
     * @param socketChannel
     * @param body
     */
    private void handleBody(SocketChannel socketChannel, String body) throws IOException {
        if(body != null){
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            //若客户端字符串为查询时间，则返回当前时间
            if(body.equals("get time")) {
                writeBuffer.put(new Date().toString().getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }else{
                writeBuffer.put(body.getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }
        }
    }

    public static void main(String[] args){
        TimeServer timeServer = new TimeServer();
        timeServer.createServer(8081);
        Thread serverThread = new Thread(timeServer);
        serverThread.setDaemon(false);
        serverThread.start();
    }
}
