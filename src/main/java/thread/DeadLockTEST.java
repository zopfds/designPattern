package thread;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2019/2/21
 */
public class DeadLockTEST {
    private static Object a = new Object();
    private static Object b = new Object();

    public static void main(String[] args){
        new Thread(() -> {synchronized (a){
            System.out.println("ThreadA enter synchronized block a!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (b){
                System.out.println("ThreadA enter synchronized block b!");
            }
        }
        }).start();

        new Thread(() -> {synchronized (b){
            System.out.println("ThreadB enter synchronized block b!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (a){
                System.out.println("ThreadB enter synchronized block a!");
            }
        }
        }).start();
    }
}
