package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * java 原生nio简单例子
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/25
 */
public class SimpleNioTest {

    /**
     * nio服务器
     */
    static class NioServer{

        private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        /**
         * 创建nio channel ,绑定selector
         * @param port
         */
        public void createServer(int port){
            try {
                //开启serverSocketChannel
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                //channel绑定端口地址
                serverSocketChannel.bind(new InetSocketAddress(port));
                //channel配置为非阻塞模式
                serverSocketChannel.configureBlocking(false);
                //开启selector响应事件
                Selector selector = Selector.open();
                //注册
                serverSocketChannel.register(selector , SelectionKey.OP_ACCEPT);

                select(selector);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 响应select事件
         * @param selector
         */
        public void select(Selector selector) throws IOException {
            while(true){
                System.out.println("block in the selector select!");
                //监听channel
                selector.select();
                //获取有事件的key
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = keys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    //先移除该key
                    iterator.remove();
                    handleKey(selectionKey);
                }
            }
        }

        /**
         * 处理selector事件
         * @param selectionKey
         */
        public void handleKey(SelectionKey selectionKey) throws IOException {

            ServerSocketChannel serverSocketChannel = null;
            SocketChannel socketChannel = null;
            if(selectionKey.isAcceptable()){
                serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selectionKey.selector() , SelectionKey.OP_READ);
            }else if(selectionKey.isReadable()){
                socketChannel = (SocketChannel) selectionKey.channel();
                //先清楚readBuffer
                readBuffer.clear();
                socketChannel.read(readBuffer);
                System.out.println(new String(readBuffer.array()));
                socketChannel.register(selectionKey.selector() , SelectionKey.OP_WRITE);
            }else if(selectionKey.isWritable()){
                socketChannel = (SocketChannel) selectionKey.channel();
                writeBuffer.clear();
                writeBuffer.put("hello".getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
                socketChannel.register(selectionKey.selector() , SelectionKey.OP_READ);
            }
        }
    }

    public static void main(String[] args){
        NioServer nioServer = new NioServer();
        nioServer.createServer(8091);
    }
}
