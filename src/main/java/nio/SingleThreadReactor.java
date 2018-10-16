package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
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
 * @create 2018/7/23
 */
public class SingleThreadReactor {
    public static void main(String[] args){
        ByteBuffer readBuffer = ByteBuffer.allocate(50);
        try {
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(1234));
            ssc.register(selector , SelectionKey.OP_ACCEPT);

            while(true){

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> its= selectionKeys.iterator();

                while(its.hasNext()){
                    SelectionKey key = its.next();
                    its.remove();

                    if(key.isAcceptable()){
                        ServerSocketChannel acceptChannel = (ServerSocketChannel)key.channel();
                        SocketChannel sc = acceptChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector , SelectionKey.OP_READ);
                    }else if(key.isReadable()){
                        SocketChannel readChannel = (SocketChannel)key.channel();
                        int count = readChannel.read(readBuffer);
                        if(count <= 0){
                            readChannel.close();
                            key.cancel();
                            readBuffer.flip();
                            System.out.println("readFromClient:" + new String(readBuffer.array()));
                        }
                    }
                    selectionKeys.remove(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
