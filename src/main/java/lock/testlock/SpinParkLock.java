//package lock.testlock;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * 简单锁的实现 park 并且 加入队列 - 简单利用CAS操作设置标志位，利用标志位来实现锁的进入和退出
// * 线程加锁失败后
// *
// * 对于锁冲突不严重的情况，用自旋锁会更适合，试想每个线程获得锁后很短的一段时间内就释放锁，
// * 竞争锁的线程只要经历几次自旋运算后就能获得锁，那就没必要等待该线程了，因为等待线程意味着需要进入到内核态进行上下文切换，
// * 而上下文切换是有成本的并且还不低，如果锁很快就释放了，那上下文切换的开销将超过自旋。
// *
// * @author jianbo.pan@mljr.com
// * @version ${VERSION}
// * @create 2018/10/19
// */
//public class SpinParkLock {
//    /* 锁状态 */
//    private volatile int status;
//
//    /* 已锁标志位 */
//    private static final int lock_status = 1;
//
//    /* 未锁标志位 */
//    private static final int unlock_status = 0;
//
//    /* 虚拟机unsafe类，主要用于CAS操作 */
//    private static Unsafe unsafe;
//
//    /* 偏移量 */
//    private static final long valueOffset;
//
//    /* 线程阻塞队列 */
//    private static LinkedBlockingQueue<Thread> linkedBlockingQueue = new LinkedBlockingQueue();
//
//
//    static {
//        try {
//            unsafe = getUnsafe();
//            //初始化status位于类中的偏移量
//            valueOffset = unsafe.objectFieldOffset
//                    (SpinLock.class.getDeclaredField("status"));
//        } catch (Throwable ex) {
//            throw new Error(ex);
//        }
//    }
//
//    /**
//     * 加锁
//     *
//     * cas操作设置标志位获取锁,若当前线程未获取锁将会一直重试设置标记位
//     */
//    public void lock() throws InterruptedException{
//        while(!compareAndSet(unlock_status,lock_status)){
//            System.out.println(Thread.currentThread().getName() + " attemp to acquire lock failed!");
//            linkedBlockingQueue.add(Thread.currentThread());
//        }
//    }
//
//    /**
//     * CAS指令为CPU支持的原子指令集，compare and swap 比较并且交换的原子操作
//     *
//     * @param a
//     * @param b
//     * @return
//     */
//    private final boolean compareAndSet(int a, int b) {
//        return unsafe.compareAndSwapInt(status , valueOffset , a , b);
//    }
//
//    /**
//     * 解锁
//     *
//     * cas操作设置标志位释放锁
//     */
//    public void unLock(){
//        compareAndSet(lock_status , unlock_status);
//        System.out.println(Thread.currentThread().getName() + " release lock!");
//    }
//
//    /**
//     * 调用反射获取虚拟机unsafe
//     * @return
//     * @throws Throwable
//     */
//    private static Unsafe getUnsafe() throws Throwable {
//        Class<?> unsafeClass = Unsafe.class;
//        for (Field f : unsafeClass.getDeclaredFields()) {
//            if ("theUnsafe".equals(f.getName())) {
//                f.setAccessible(true);
//                return (Unsafe) f.get(null);
//            }
//
//        }
//        throw new IllegalAccessException("no declared field: theUnsafe");
//    }
//
//    public static void main(String[] args){
//        SpinParkLock spinParkLock = new SpinParkLock();
//
//        //起两个线程不断的获得和释放锁
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        spinParkLock.lock();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
//                    spinParkLock.unLock();
//                }
//            }
//        });
//
//        thread1.setDaemon(false);
//
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        spinParkLock.lock();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
//                    spinParkLock.unLock();
//                }
//            }
//        });
//
//        thread2.setDaemon(false);
//
//        thread1.start();
//        thread2.start();
//    }
//}
