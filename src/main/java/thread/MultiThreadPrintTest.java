package thread;

public class MultiThreadPrintTest {

    private static final jdk.internal.misc.Unsafe U = jdk.internal.misc.Unsafe.getUnsafe();
    private static volatile int i = 0;
    private static final long I_VALUE;
    private static volatile boolean lock = false;
    private static final long LOCK_VALUE;

    static {
        try {
            I_VALUE = U.objectFieldOffset
                    (MultiThreadPrintTest.class.getDeclaredField("i"));
            LOCK_VALUE = U.objectFieldOffset
                    (MultiThreadPrintTest.class.getDeclaredField("lock"));
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    private void incr(){
        U.getAndAddInt(this, I_VALUE, 1);
    }

    private boolean lock(){
        return U.compareAndSetBoolean(this, LOCK_VALUE, false , true);
    }

    private boolean unLock(){
        return U.compareAndSetBoolean(this, LOCK_VALUE, true , false);
    }

    private static final class MultiThreadAddTest implements Runnable{

        private MultiThreadPrintTest multiThreadPrintTest;

        public MultiThreadAddTest(MultiThreadPrintTest multiThreadPrintTest) {
            this.multiThreadPrintTest = multiThreadPrintTest;
        }

        @Override
        public void run() {
            for(int j = 1 ; j <= 100 ; j++) {
                while (!multiThreadPrintTest.lock()) {
                    Thread.yield();
                }
                multiThreadPrintTest.incr();
                while (!multiThreadPrintTest.unLock()) {
                }
            }
        }
    }


    public static void main(String[] args){
        MultiThreadPrintTest multiThreadPrintTest = new MultiThreadPrintTest();
        for(int j = 0 ; j < 10 ; j ++ ){
            new Thread(new MultiThreadAddTest(multiThreadPrintTest)).start();
        }
        try {
            Thread.currentThread().sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(i);
    }
}
