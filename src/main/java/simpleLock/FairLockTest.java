package simpleLock;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/12
 */
public class FairLockTest {

    public static void main(String[] args){
        FairLock fairLock = new FairLock();
        new Thread(new PrintThread(1 , fairLock)).start();
        new Thread(new PrintThread(2 , fairLock)).start();

    }

    private static class PrintThread implements Runnable{

        private int i;

        private FairLock fairLock;

        public PrintThread(int i, FairLock fairLock) {
            this.i = i;
            this.fairLock = fairLock;
        }

        @Override
        public void run() {
            try {
                fairLock.lock();
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println(i);
            try {
                if(i > 1) {
                    Thread.currentThread().sleep(i * 1000L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fairLock.unLock();
        }
    }
}
