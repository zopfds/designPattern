package thread;

//import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MultiThreadPrintTest {

//    private static Unsafe U;
//    private volatile int i = 0;
//    private static long I_VALUE;
//    private volatile boolean lock = false;
//    private static long LOCK_VALUE;
//
//    static {
//        try {
//            Field f = Unsafe.class.getDeclaredField("theUnsafe");
//            f.setAccessible(true);
//            U = (Unsafe) f.get(null);
//            I_VALUE = U.objectFieldOffset
//                    (MultiThreadPrintTest.class.getDeclaredField("i"));
//            LOCK_VALUE = U.objectFieldOffset
//                    (MultiThreadPrintTest.class.getDeclaredField("lock"));
//        } catch (ReflectiveOperationException e) {
//            throw new Error(e);
//        }
//    }
//
//    private void incr(){
//        U.getAndAddInt(this, I_VALUE, 1);
//    }
//
//    private boolean lock(){
//        return U.compareAndSwapObject(this, LOCK_VALUE, false , true);
//    }
//
//    private boolean unLock(){
//        return U.compareAndSwapObject(this, LOCK_VALUE, true , false);
//    }
//
//    private int getI(){
//        return U.getInt(this , I_VALUE);
//    }
//
//    private static final class MultiThreadAddTest implements Runnable{
//
//        private MultiThreadPrintTest multiThreadPrintTest;
//
//        public MultiThreadAddTest(MultiThreadPrintTest multiThreadPrintTest) {
//            this.multiThreadPrintTest = multiThreadPrintTest;
//        }
//
//        @Override
//        public void run() {
//            for(int j = 1 ; j <= 100 ; j++) {
//                while (!multiThreadPrintTest.lock()) {
//                    Thread.yield();
//                }
//                multiThreadPrintTest.incr();
//                while (!multiThreadPrintTest.unLock()) {
//                }
//            }
//        }
//    }
//
//
//    public static void main(String[] args) throws ClassNotFoundException {
//        MultiThreadPrintTest multiThreadPrintTest = new MultiThreadPrintTest();
//        for(int j = 0 ; j < 10 ; j ++ ){
//            new Thread(new MultiThreadAddTest(multiThreadPrintTest)).start();
//        }
//        try {
//            Thread.currentThread().sleep(1000 * 10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(multiThreadPrintTest.getI());
//    }
}
