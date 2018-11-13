//package lock.testlock;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
///**
// * 简单锁的实现 - 简单利用CAS操作设置标志位，利用标志位来实现锁的进入和推出
// *
// *
// *  优点：
// *  1.实现简单
// *
// *  缺点:
// *  1.当前线程锁获取失败则会一直自旋获取，浪费cpu资源
// *  2.当锁竞争比较激烈时，存在大量等待线程自旋，浪费cpu资源
// *
// *
// *  可能存在疑问，多线程加锁的可见性问题如何保证的，主要是由 volatile 语义保证的，
// *  volatile语义将会在修饰变量的前后加入内存屏障，并且保证当前线程读取或写入的变量操作直接生效于内存,从而解决不同线程间的可见性问题
// *
// * @author jianbo.pan@mljr.com
// * @version ${VERSION}
// * @create 2018/10/19
// */
//public class SpinLock {
//
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
//    public void lock(){
//        while(!compareAndSet(unlock_status,lock_status)){
//            System.out.println(Thread.currentThread().getName() + " attemp to acquire lock failed!");
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
//        SpinLock spinLock = new SpinLock();
//
//        //起两个线程不断的获得和释放锁
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    spinLock.lock();
//                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
//                    spinLock.unLock();
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
//                    spinLock.lock();
//                    System.out.println(Thread.currentThread().getName() + " acquire lock!");
//                    spinLock.unLock();
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
