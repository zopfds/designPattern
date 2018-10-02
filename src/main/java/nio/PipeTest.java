package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/18
 */
public class PipeTest {

    public static void main(String[] args){

        final String sendData = "String from pipe!";

        try {
            Pipe pipe = Pipe.open();

            Pipe.SinkChannel sinkChannel = pipe.sink();

            ByteBuffer byteBuffer = ByteBuffer.allocate(48);

            byteBuffer.clear();

            byteBuffer.put(sendData.getBytes());

            byteBuffer.flip();

            while(byteBuffer.hasRemaining()){
                sinkChannel.write(byteBuffer);
            }

            Pipe.SourceChannel sourceChannel = pipe.source();

            ByteBuffer readBuffer = ByteBuffer.allocate(5);

            int length = sourceChannel.read(readBuffer);

            while(length == 5){

                readBuffer.flip();

                while(readBuffer.hasRemaining()) {
                    System.out.print((char)readBuffer.get());
                }

                readBuffer.clear();

                length = sourceChannel.read(readBuffer);
            }

            readBuffer.flip();

            while(readBuffer.hasRemaining()) {
                System.out.print((char)readBuffer.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
