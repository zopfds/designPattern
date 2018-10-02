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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/23
 */
public class MultiThreadReactor {
    public static void main(String[] args){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(3030));
            ssc.register(selector , SelectionKey.OP_READ);

            while(true){
                if(selector.selectNow() < 0){
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isAcceptable()){
                        ServerSocketChannel acceptChannel = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel socketChannel = acceptChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("Accept from client:" + socketChannel.getRemoteAddress());
                        SelectionKey readKey = socketChannel.register(selector , SelectionKey.OP_READ);
                        readKey.attach(new Processor());
                    }else if(selectionKey.isReadable()){
                        Processor processor = (Processor) selectionKey.attachment();
                        processor.process(selectionKey);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class Processor{
        private static final ExecutorService service = Executors.newFixedThreadPool(16);

        public void process(SelectionKey selectionKey){
            service.submit(() -> {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                int count = socketChannel.read(buffer);
                if (count < 0) {
                    socketChannel.close();
                    selectionKey.cancel();
                    System.out.println("Read ended" + socketChannel);
                    return null;
                } else if(count == 0) {
                    return null;
                }
               System.out.println("Read message" + socketChannel + new String(buffer.array()));
                return null;
            });
        }
    }
}
