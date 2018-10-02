package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * nio模型filechannel
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/17
 */
public class FileChannel {

    /**
     * 文件映射
     */
    public static void FileChannelMap(){
        int length = 0x00000034;
        try {
            java.nio.channels.FileChannel fc = new RandomAccessFile("/Users/jianbopan/Desktop/还款计划.txt","rw").getChannel();

            MappedByteBuffer mappedByteBuffer = fc.map(java.nio.channels.FileChannel.MapMode.READ_WRITE , 0 , length);

            for(int i = 0 ; i < length ; i ++){
                mappedByteBuffer.put((byte) (65 + i));
            }

            System.out.println("Finish writing!");

            for(int i = length / 2 ; i < length / 2 + 6 ; i++){
                System.out.println((char) mappedByteBuffer.get(i));
            }

            fc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void transFertest(){
        try {
            java.nio.channels.FileChannel fromChannel = new RandomAccessFile("/Users/jianbopan/Desktop/还款计划.txt","rw").getChannel();
            java.nio.channels.FileChannel toChannel = new RandomAccessFile("/Users/jianbopan/Desktop/target1.txt","rw").getChannel();
            long size = fromChannel.size();
//            toChannel.transferFrom(fromChannel , 0 , size);
            fromChannel.transferTo(0 , size , toChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ie){
            ie.printStackTrace();
        }
    }

    public static void RandomAccess(){
        try {
            RandomAccessFile file = new RandomAccessFile("/Users/jianbopan/Desktop/还款计划.txt","rw");

            java.nio.channels.FileChannel inChannel = file.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(48);

            int bytesRead = inChannel.read(buf);

            while(bytesRead != -1){
                buf.flip();

                while(buf.hasRemaining()){
                    System.out.println((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ie){
            ie.printStackTrace();
        }
    }

    public static void main(String[] args){
//        FileChannelMap();

//        System.out.println((int) 'A');

        transFertest();
    }

}
