package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/17
 */
public class SelectorTest {

    private static class NioServer{

        private static volatile int port;

        private static Selector selector;

        private static ByteBuffer send = ByteBuffer.allocate(1024);
        private static ByteBuffer receive = ByteBuffer.allocate(1024);

        //静态内部类
        private static class NioServerBuilder{
            static NioServer instance = new NioServer(port);
        }

        //获取实例,单例
        public static NioServer getInstance(int port){
            NioServer.port = port;
            return NioServerBuilder.instance;
        }

        //私有构造方法
        private NioServer(int port){

            port = port;

            try {
                //监听channel
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                //必须配置为非阻塞
                serverSocketChannel.configureBlocking(false);
                //检测与通道关联的serverSocket
                ServerSocket serverSocket = serverSocketChannel.socket();
                //serverSocket绑定端口
                serverSocket.bind(new InetSocketAddress(port));
                //赋值
                selector = Selector.open();

                serverSocketChannel.register(selector , SelectionKey.OP_ACCEPT);

                System.out.println("Server Start at port:" + port);

                send.put("data from server".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void wakeUp(){
            selector.wakeup();
        }

        //持续监听
        private static void listen() throws IOException {
            while(true){

                System.out.println("Selector block!");

                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();

                    iterator.remove();
                    dealKey(selectionKey);
                }
            }
        }

        //处理key值
        private static void dealKey(SelectionKey selectionKey) throws IOException {

            ServerSocketChannel server = null;
            SocketChannel client = null;
            if(selectionKey.isAcceptable()){
                server = (ServerSocketChannel) selectionKey.channel();
                client = server.accept();
                client.configureBlocking(false);
                client.register(selector , SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }else if(selectionKey.isReadable()){
                client = (SocketChannel) selectionKey.channel();
                receive.clear();
                client.read(receive);
                System.out.println(new String(receive.array()));
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }else if(selectionKey.isWritable()){
                send.flip();
                client = (SocketChannel) selectionKey.channel();
                client.write(send);
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private static class Client{
        SocketChannel sc = null;
        Selector selector = null;

        // 发送接收缓冲区
        ByteBuffer send = ByteBuffer.wrap("data come from client".getBytes());
        ByteBuffer receive = ByteBuffer.allocate(1024);

        public void work(int port) throws IOException
        {

            try {
                sc = SocketChannel.open();
                selector = selector.open();
                // 注册为非阻塞通道
                sc.configureBlocking(false);
                sc.connect(new InetSocketAddress("localhost", 8080));
                sc.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                // 选择
                if (selector.select() == 0) {
                    continue;
                }

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    // 必须由程序员手动操作
                    it.remove();
                    sc = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        if (sc.isConnectionPending()) {
                            // 结束连接，以完成整个连接过程
                            sc.finishConnect();
                            System.out.println("connect completely");

                            try {
                                sc.write(send);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (key.isReadable()) {
                        try {
                            receive.clear();
                            sc.read(receive);
                            System.out.println(new String(receive.array()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (key.isWritable()) {
                        receive.flip();
                        try {
                            send.flip();
                            sc.write(send);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }// end while

            }// end while(true)

        }// end work()

    }

    public static void main(String[] args){
        NioServer nioServer = NioServer.getInstance(8080);
        try {
            new Thread(){
                @Override
                public void run() {
                    try {
                        nioServer.listen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            Thread.currentThread().sleep(2000);

            nioServer.wakeUp();

//            Client client1 = new Client();
//            Client client2 = new Client();
//
//            client1.work(8080);
//            client2.work(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
