package lock.testlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    public static void main(String[] args){
        ReentrantLock lock = new ReentrantLock();
        Condition wakeCondition = lock.newCondition();
        for(int i = 0 ; i < 10 ; i ++){
            new Thread(() -> {
                lock.lock();
                try {
                    wakeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }).start();
        }

        try {
            Thread.currentThread().sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
