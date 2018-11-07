package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 简单Nio客户端查询服务端
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/26
 */
public class TimeClientServer{

    private Selector selector;

    private volatile boolean stop = false;

    private SocketChannel socketChannel;

    private int count = 0;

    /**
     * 创建客户端连接
     * @param port
     */
    public void createClient(int port){
        try {
            //开启socketchannel
            socketChannel = SocketChannel.open();

            socketChannel.connect(new InetSocketAddress(port));
            //配置为非阻塞模式
            socketChannel.configureBlocking(false);
            //开启selector
            selector = Selector.open();
            //将感兴趣的事件注册到selector上
            socketChannel.register(selector , SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            System.out.println("time client start to send message to server!");

            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.stop = stop;
    }

    public void start() {
        try{
            while(!stop){
                //多路复用选择器
                selector.select();
                //选择key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //迭代器
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handle(selectionKey);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理事件
     * @param key
     */
    private void handle(SelectionKey key) throws IOException {

        if(key.isValid()){
            //如果是可连接的
            if(key.isConnectable()){
                //获取socketchannel
                socketChannel = (SocketChannel)key.channel();
                //如果key是可写入的
            }else if(key.isWritable()){
                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                String message = String.valueOf(count++);
                if(count == 10){
                    message = "get time";
                }
                writeBuffer.put(message.getBytes("utf-8"));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
                System.out.println("client send message :" + message);
                //如果是可读的
            }else if(key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readCount = socketChannel.read(readBuffer);
                readBuffer.flip();
                byte[] readByte = new byte[readCount];
                readBuffer.get(readByte);
                System.out.println("receive message from server:" + new String(readByte , "utf-8"));
                socketChannel.register(selector , SelectionKey.OP_WRITE);
            }
        }
    }

    public static void main(String[] args){
        TimeClientServer timeClientServer = new TimeClientServer();
        timeClientServer.createClient(8081);
    }

}
