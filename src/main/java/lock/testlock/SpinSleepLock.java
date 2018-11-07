//package lock.testlock;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
///**
// * 简单锁的实现 sleep - 简单利用CAS操作设置标志位，利用标志位来实现锁的进入和推出
// *
// * 主要为了解决 SpinYieldLock 中 cpu 利用率过低的问题，
// * 线程 sleep 能让线程处于睡眠状态，从而减少 cpu 自旋操作
// *
// * 优点：
// * 1.通常用于实现上层锁。
// *
// * 缺点:
// * 1.该方式不适合用于操作系统级别的锁，因为作为一个底层锁，其sleep时间很难设置。
// * sleep的时间取决于同步代码块的执行时间，sleep时间如果太短了，会导致线程切换频繁（极端情况和yield方式一样）；
// * sleep时间如果设置的过长，会导致线程不能及时获得锁。因此没法设置一个通用的sleep值。
// * 就算sleep的值由调用者指定也不能完全解决问题：有的时候调用锁的人也不知道同步块代码会执行多久。
// *
// *
// * @author jianbo.pan@mljr.com
// * @version ${VERSION}
// * @create 2018/10/19
// */
//
//public class SpinSleepLock {
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
//    public void lock() throws InterruptedException{
//        while(!compareAndSet(unlock_status,lock_status)){
//            System.out.println(Thread.currentThread().getName() + " attemp to acquire lock failed!");
//            Thread.currentThread().sleep(10);
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
