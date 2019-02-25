package thread.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ticket lock
 *
 * 缺点: 每个进程／线程均要读写一个变量
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2019/2/25
 */
public class TicketLock {

    private AtomicInteger ticketNum = new AtomicInteger();
    private AtomicInteger serviceNum = new AtomicInteger();

    /**
     * 加锁
     * @return
     */
    public int lock(){
        int ticket = ticketNum.getAndIncrement();
        while(serviceNum.get() != ticket){
            Thread.currentThread().yield();
        }
        return ticket;
    }

    public void unLock(int ticket){
        serviceNum.compareAndSet(ticket , ticket + 1);
    }

    public static void main(String[] args){
    }
}
