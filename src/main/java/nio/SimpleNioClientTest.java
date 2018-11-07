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
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/25
 */
public class SimpleNioClientTest {
    static class NioClient{

        private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        private ByteBuffer writeBuffer = ByteBuffer.allocate(2014);

        public void createServer(int port){
            try {
                //客户端开启channel
                SocketChannel socketChannel = SocketChannel.open();
                //设置channel为非阻塞
                socketChannel.configureBlocking(false);
                //开启selector
                Selector selector = Selector.open();
                //连接到对应的
                socketChannel.connect(new InetSocketAddress("localhost" , port));
                //连接到对应的
                socketChannel.register(selector , SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                //select选择
                select(selector);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * selector 选择事件响应
         * @param selector
         * @throws IOException
         */
        public void select(Selector selector) throws IOException {
            while(true){
                if(selector.select() <= 0){
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handle(selectionKey);
                }
            }
        }

        /**
         * 处理响应
         * @param selectionKey
         */
        public void handle(SelectionKey selectionKey) throws IOException {

            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            socketChannel.configureBlocking(false);
            //连接
            if(selectionKey.isConnectable()){
                if(socketChannel.isConnectionPending()){
                    socketChannel.finishConnect();
                    socketChannel.register(selectionKey.selector() , SelectionKey.OP_WRITE);
                }
                //如果
            }else if(selectionKey.isWritable()){
                writeBuffer.clear();
                writeBuffer.put("fuck".getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }else if(selectionKey.isReadable()){
                readBuffer.clear();
                socketChannel.read(readBuffer);
                readBuffer.flip();
                System.out.println(new String(readBuffer.array()));
            }
        }
    }

    public static void main(String[] args){
        NioClient test = new NioClient();
        test.createServer(8091);
    }
}
