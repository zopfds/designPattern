package thread.synchronizedTest;


/**
 * synchronized 关键字是加在object上的，当线程等待该monitor时线程进入block状态,进入锁池
 *
 * 本实例展示synchronized是可重入的
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/12
 */
public class ReenterTest {

    public synchronized void InnerSync(){
        System.out.println("enter inner synchronized!");

        System.out.println("enter inner synchronized!");
    }

    public static void main(String[] args){
        ReenterTest test = new ReenterTest();
        new Thread(new SynchronizedThread(test)).start();
    }

    private static class SynchronizedThread implements Runnable{

        private ReenterTest reenterTest;

        public SynchronizedThread(ReenterTest reenterTest) {
            this.reenterTest = reenterTest;
        }

        @Override
        public void run() {
            synchronized (reenterTest){
                System.out.println("enter outer synchronized");
                    reenterTest.InnerSync();
                System.out.println("exit outer synchronized");
            }
        }
    }
}
