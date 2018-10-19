package lock.testlock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 简单锁的实现 yield - 简单利用CAS操作设置标志位，利用标志位来实现锁的进入和推出
 *
 * 主要为了解决 SpinLock 中当CAS失败的时候自旋消耗cpu的问题，
 * yield能够让出cpu资源
 *
 * 优点：
 * 1.实现简单
 *
 * 缺点:
 * 1.同 SpinLock 的问题，虽然 yield 会释放cpu资源，让其他线程有争抢的机会，但是该线程依然可能会被继续调度(cpu 主要按优先级调度)
 * 2.利用率低，加入有100个线程争抢，0号线程获得锁，1号线程获取锁失败，yield让出cpu,cpu按照2，3...99号的顺序调度，
 * 则占用cpu的有效时间只有 1/100 ，导致cpu利用率过低
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/19
 */
public class SpinYieldLock {

    /* 锁状态 */
    private volatile int status;

    /* 已锁标志位 */
    private static final int lock_status = 1;

    /* 未锁标志位 */
    private static final int unlock_status = 0;

    /* 虚拟机unsafe类，主要用于CAS操作 */
    private static Unsafe unsafe;

    /* 偏移量 */
    private static final long valueOffset;


    static {
        try {
            unsafe = getUnsafe();
            //初始化status位于类中的偏移量
            valueOffset = unsafe.objectFieldOffset
                    (SpinLock.class.getDeclaredField("status"));
        } catch (Throwable ex) {
            throw new Error(ex);
        }
    }

    /**
     * 加锁
     *
     * cas操作设置标志位获取锁,若当前线程未获取锁将会让出cpu资源（注意该线程可能会被继续调度）
     */
    public void lock(){
        while(!compareAndSet(unlock_status,lock_status)){
            System.out.println(Thread.currentThread().getName() + " attemp to acquire lock failed!");
            Thread.currentThread().yield();
        }
    }

    /**
     * CAS指令为CPU支持的原子指令集，compare and swap 比较并且交换的原子操作
     *
     * @param a
     * @param b
     * @return
     */
    private final boolean compareAndSet(int a, int b) {
        return unsafe.compareAndSwapInt(status , valueOffset , a , b);
    }

    /**
     * 解锁
     *
     * cas操作设置标志位释放锁
     */
    public void unLock(){
        compareAndSet(lock_status , unlock_status);
        System.out.println(Thread.currentThread().getName() + " release lock!");
    }

    /**
     * 调用反射获取虚拟机unsafe
     * @return
     * @throws Throwable
     */
    private static Unsafe getUnsafe() throws Throwable {
        Class<?> unsafeClass = Unsafe.class;
        for (Field f : unsafeClass.getDeclaredFields()) {
            if ("theUnsafe".equals(f.getName())) {
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            }

        }
        throw new IllegalAccessException("no declared field: theUnsafe");
    }

    public static void main(String[] args){
        SpinLock spinLock = new SpinLock();

        //起两个线程不断的获得和释放锁
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    spinLock.lock();
                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
                    spinLock.unLock();
                }
            }
        });

        thread1.setDaemon(false);

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    spinLock.lock();
                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
                    spinLock.unLock();
                }
            }
        });

        thread2.setDaemon(false);

        thread1.start();
        thread2.start();
    }
}
