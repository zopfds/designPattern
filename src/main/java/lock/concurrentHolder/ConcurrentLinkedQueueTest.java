package lock.concurrentHolder;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * concurrentlinkedqueue的并发debug测试
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/5
 */
public class ConcurrentLinkedQueueTest {

    public static void main(String[] args){
        multipartOffer();

//        offerPollOffer();
        try {
            Thread.currentThread().sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个线程的poll操作会
     */
    private static void multipartOffer(){
        ConcurrentLinkedQueue<Integer> test = new ConcurrentLinkedQueue<Integer>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.offer(Integer.valueOf(1));
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.offer(Integer.valueOf(2));
            }
        }).start();
    }

    private static void offerPollOffer(){
        ConcurrentLinkedQueue<Integer> test = new ConcurrentLinkedQueue<Integer>();

        test.offer(Integer.valueOf(1));
        test.offer(Integer.valueOf(2));
        test.offer(Integer.valueOf(3));

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.offer(Integer.valueOf(4));
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(test.poll());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.offer(Integer.valueOf(2));
            }
        }).start();
    }
}
