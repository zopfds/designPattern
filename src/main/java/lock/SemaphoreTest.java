package lock;

import java.util.concurrent.Semaphore;

/**
 * 信号量测试，信号量适用于控制流量的场景
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/7
 */
public class SemaphoreTest {

    private static Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 同学准备获取笔!");
                    try {
                        semaphore.acquire();

                        System.out.println(Thread.currentThread().getName() + " 同学获取到笔!");

                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                    System.out.println(Thread.currentThread().getName() + " 同学填写完了表格！");
                }
            }).start();
        }
    }
}
