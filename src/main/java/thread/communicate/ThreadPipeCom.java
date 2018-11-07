package thread.communicate;


import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 线程间通信的方式
 *
 * 1.共享变量方式
 * 2.通过pipe流
 * 3.
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/24
 */
public class ThreadPipeCom {

    public static void main(String[] args){
        PipedInputStream pipedInputStream = new PipedInputStream();
        try {
            final PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0 ; i < 100 ; i ++)
                        try {
                            pipedOutputStream.write(String.valueOf(i).getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] buffer = new byte[128];
                        while(true) {
                            int length = pipedInputStream.read(buffer);
                            System.out.println(new String(buffer , 0 , length));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
