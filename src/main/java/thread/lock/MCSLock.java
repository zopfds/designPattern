package thread.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * mcs锁,每个节点轮询自己节点的isBlock标志，看是否获取锁继续执行,解锁后需要改变后继节点的isBlock标志以唤醒后继节点
 *
 * 等待线程节点成链表组织
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2019/2/25
 */
public class MCSLock {
    //等待线程封装节点
    private static class MCSNode{
        //volatile关键字必须的，保证不同线程间的可见性
        volatile MCSNode next;
        //是否阻塞，用于标志当前节点是否为头节点，false则为头节点
        volatile boolean isBlock = true;
    }

    //标志等待队列的末尾
    volatile MCSNode tail;
    //提供线程安全的 MCSLock CAS 设置 tail的更新操作
    private static final AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(MCSLock.class , MCSNode.class , "tail");

    /**
     * 加锁操作
     *
     * 1.原子更新并将节点链入队列尾部，并且如果当前不为队列第一个节点则自旋阻塞
     * 2.否则当前阻塞标志为false,防止阻塞
     * @param currentNode
     */
    public void lock(MCSNode currentNode){
        //原子更新链表尾部，多线程进入会在 AtomicReferenceFieldUpdater 内自旋CAS操作
        MCSNode t = (MCSNode)updater.getAndSet(this , currentNode);         //lock1
        //当前节点不是第一个节点
        if(t != null){
            //将前驱节点的下一个节点指向当前节点，也就是链到队列尾部
            t.next = currentNode;                                               //lock2
            //当前节点是否阻塞
            while(currentNode.isBlock){
            }
            //否则将当前阻塞标志为false
        }else{
            currentNode.isBlock = false;
        }
    }


    /**
     * 解锁操作
     *
     * 1.如果 isBlock 为 true 则说明头节点不是当前节点，解锁无效直接返回
     * 2.获取当前节点的后继节点
     * 3.如果后继节点为空则当前节点可能为头节点，尝试更新队列尾为null，更新成功则返回
     * 4.如果后继节点不为空
     *
     * @param currentNode
     */
    public void unLock(MCSNode currentNode){
        if(currentNode.isBlock){
            return;
        }

        while(true){
            MCSNode next = currentNode.next;
            if(next == null){
                //这里是防止当前队列只有一个当前节点，走到这里的时候，有第二个线程加锁走到lock1行 , 而lock2行还没走到
                if(updater.compareAndSet(this , currentNode , null)){
                    currentNode.next = null; // help gc
                    return;
                }
                //如果上面设置失败，说明当前节点后面新插入了节点，重新循环获取下一个节点
            }else{
                next.isBlock = false;
                currentNode.next = null;  //help gc
                return;
            }
        }
    }

    private static volatile int i = 0;
    private static final MCSLock lock = new MCSLock();

    private static final class UpdateThread implements Runnable{
        @Override
        public void run() {
            MCSNode node = new MCSNode();
            lock.lock(node);
            System.out.println(Thread.currentThread().getName() + " get lock and incr i = " + i++);
            lock.unLock(node);
        }
    }

    public static void main(String[] args){
        for(int i = 0 ; i < 100 ; i ++){
            new Thread(new UpdateThread()).start();
        }
        try {
            Thread.currentThread().sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
