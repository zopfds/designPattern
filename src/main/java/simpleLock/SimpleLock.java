package simpleLock;

/**
 * 简单锁实现
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/12
 */
public class SimpleLock {

    private boolean isLocked = false;

    private Thread lockThread = null;

    /**
     * 加锁
     * @throws InterruptedException
     */
    public synchronized void lock() throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
        lockThread = Thread.currentThread();
    }

    /**
     * 解锁
     */
    public synchronized void unLock(){
        if(lockThread != Thread.currentThread()){
            throw new IllegalMonitorStateException("Calling thread has not hold the monitor!");
        }
        isLocked = false;
        lockThread = null;
        notify();
    }
}
