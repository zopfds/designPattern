package nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopyTest {

    /**
     * 用老的IO流复制文件
     *
     * 旧io流已经用nio覆写过
     * @param sourceFile
     * @param targetFile
     * @return
     */
    public static long copyFileWithIO(File sourceFile , File targetFile) throws IOException , FileNotFoundException {
        if(!targetFile.exists()){
            targetFile.createNewFile();
        }

        long startTime = System.currentTimeMillis();

        InputStream is = null;
        OutputStream os = null;

        try {
            is = new BufferedInputStream(new FileInputStream(sourceFile));

            os = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] buffer = new byte[1024 * 1024];

            while(is.read(buffer) != -1){
                os.write(buffer);
            }

            return System.currentTimeMillis() - startTime;

        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * nio复制文件
     * @param sourceFile
     * @param targetFile
     * @return
     */
    public static long copyFileWithNio(File sourceFile , File targetFile) throws IOException {
        if(!targetFile.exists()){
            targetFile.createNewFile();
        }

        long startTime = System.currentTimeMillis();

        RandomAccessFile irf = new RandomAccessFile(sourceFile , "r");
        RandomAccessFile orf = new RandomAccessFile(targetFile,"rw");

        FileChannel ifc = irf.getChannel();
        FileChannel ofc = orf.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        while(ifc.read(buffer) > 0) {
            buffer.flip();
            ofc.write(buffer);
            buffer.clear();
        }

        ifc.close();
        ofc.close();

        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args){
        File sourceFile = new File("E:\\test.MP4");

        File ioTargetFile = new File("C:\\Users\\74077\\Desktop\\io.MP4");
        File nioTargetFile = new File("C:\\Users\\74077\\Desktop\\nio.MP4");

        try {
            System.out.println("io copy consume time： " + copyFileWithIO(sourceFile , ioTargetFile));

            System.out.println("nio copy consume time： " + copyFileWithNio(sourceFile , nioTargetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
