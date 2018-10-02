package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/18
 */
public class AsynchronousFileChannelTest {

    public static void main(String[] args){
        Path path = Paths.get("/Users/jianbopan/Desktop/1.txt");

        try {
            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.READ);

            AsynchronousFileChannel writeChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

            ByteBuffer byteBuffer = ByteBuffer.allocate(50);
            ByteBuffer writeBuffer = ByteBuffer.allocate(10);
            writeBuffer.put("fuck".getBytes());
            long position = 0;

            writeBuffer.flip();
            Future<Integer> operation = fileChannel.read(byteBuffer , position);
            Future<Integer> writeOperation = writeChannel.write(writeBuffer , 0);

            while(!operation.isDone()){

            }

            byteBuffer.flip();

            while(byteBuffer.hasRemaining()) {
                System.out.print((char)byteBuffer.get());
            }

            while(!writeOperation.isDone()){

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
