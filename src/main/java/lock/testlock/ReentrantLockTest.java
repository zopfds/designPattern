package lock.testlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁测试
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/2
 */
public class ReentrantLockTest {
    public static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args){
        new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    Thread.currentThread().sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    reentrantLock.unlock();
                }
            }
        }).start();

        try {
            Thread.currentThread().sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
