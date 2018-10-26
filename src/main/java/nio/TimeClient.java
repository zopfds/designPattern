package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * nio时间客户端
 */
public class TimeClient implements Runnable{

    //域名
    private String host;
    //端口号
    private int port;
    //多路复用器
    private Selector selector;
    //socket channel
    private SocketChannel socketChannel;
    //服务器停止标志位
    private volatile boolean stop;

    /**
     * 创建服务器
     * @param host
     * @param port
     */
    public void createServer(String host, int port){
        this.host = host;
        this.port = port;
        try{
            //开启selector多路复用器
            selector = Selector.open();
            //设置socketchannel
            socketChannel = SocketChannel.open();
            //配置socketchannel为非阻塞
            socketChannel.configureBlocking(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(!stop){
            try{
                //设置最长事件等待时间
                selector.select(10000);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();

                    iterator.remove();

                    try{
                        //处理事件
                        handleInput(key);
                    }catch(Exception e) {
                        e.printStackTrace();
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理事件
     * @param key
     */
    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    //向通道注册读事件
                    sc.register(selector , SelectionKey.OP_READ);
                    doWrite(socketChannel);
                }else{
                    System.out.println("connect to server failed!");
                }
            }else if(key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //从channel中读取数据不保证全部读完
                int readBytes = sc.read(readBuffer);
                //读取字节数大于0
                if(readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    System.out.println(new String(bytes , "utf-8"));
                    sc.register(selector, SelectionKey.OP_READ);
                    //小于0则关闭channel
                }else if(readBytes < 0){
                    key.cancel();
                    sc.close();
                    //等于0则忽略
                }else{

                }

            }
        }
    }

    /**
     * 创建链接
     */
    public void doConnect() throws IOException {
        //链接成功向selector注册读事件
        //connct成功代表tcp三次握手成功，建立了tcp链接
        if(socketChannel.connect(new InetSocketAddress(host , port))){
            socketChannel.register(selector , SelectionKey.OP_READ);
            doWrite(socketChannel);
            //若未链接成功则继续注册链接事件
        }else{
            socketChannel.register(selector , SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 向channel中写入数据
     */
    public void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] req = "get time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        //socketchannel不保证一次全部写入writeBuffer中所有数据！
        if(!writeBuffer.hasRemaining()){
            System.out.println("send to server success!");
        }
    }

    /**
     * 写入string
     */
    public void writeFromString(String message) throws IOException {
        byte[] req = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        //socketchannel不保证一次全部写入writeBuffer中所有数据！
        if(!writeBuffer.hasRemaining()){
            System.out.println("send to server success! message :" + message);
        }
    }

    public static void main(String[] args){
        TimeClient client = new TimeClient();
        client.createServer("localhost" , 8081);
        Thread clientThread = new Thread(client);
        clientThread.setDaemon(false);
        clientThread.start();

        String scanString = "";
        while(!scanString.equals("stop client")){
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            try {
                client.writeFromString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
