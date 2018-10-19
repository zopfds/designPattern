package simpleLock;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 公平锁
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/12
 */
public class FairLock {

    private boolean isLocked = false;
    private Thread lockingThread = null;
    private LinkedList<Object> waitingQueue = new LinkedList<Object>();
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private ThreadLocal<Boolean> isFirst = new ThreadLocal<Boolean>();

    /**
     * 锁
     * @throws InterruptedException
     */
    public void lock() throws InterruptedException {
        Object lockObject = new Object();
        Object currentObject = null;
        boolean first = true;

        while (true) {
            synchronized (waitingQueue) {
                if (first) {
                    waitingQueue.addLast(lockObject);
                    first = false;
                }
                currentObject = waitingQueue.getFirst();
            }

            System.out.println("thread enter queue success!");

            Thread.currentThread().sleep(atomicInteger.getAndIncrement() * 3000L);

            if (currentObject != lockObject) {
                synchronized (lockObject) {
                    while (isLocked) {
                        System.out.println(Thread.currentThread().getName() + " : wait on obejct : " + lockObject.toString());
                        lockObject.wait();
                    }
                }
            } else {
                synchronized (this) {
                    lockingThread = Thread.currentThread();
                    isLocked = true;
                    return;
                }
            }
        }
    }

    /**
     * 解锁
     */
    public synchronized void unLock(){
        if(lockingThread != Thread.currentThread()){
            throw new IllegalMonitorStateException("Current Thread has not hold the lock!");
        }

        waitingQueue.removeFirst();
        isLocked = false;
        if(waitingQueue.size() > 0){
            synchronized(waitingQueue.get(0)) {
                System.out.println(Thread.currentThread().getName() + " : notify thread in queue! notify object : " + waitingQueue.get(0).toString());
                waitingQueue.get(0).notify();
            }
        }
    }
}
