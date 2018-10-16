package thread;

public class SuspendTest {

    public static  void main(String[] args){
        Object monitor = new Object();
        Object innerMonitor = new Object();
        synchronized (monitor){
            System.out.println("main Thread enter monitor!");
            new Thread(new InnerThread(monitor ,innerMonitor ,  Thread.currentThread())).start();
            try {
                Thread.currentThread().sleep(30 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (innerMonitor) {
                System.out.println("main Thread enter inner monitor!");
            }
        }

    }

    private static class InnerThread implements Runnable{
        private Object monitor;
        private Thread mainThread;
        private Object innerMonitor;

        public InnerThread(Object m ,Object innerMonitor , Thread mainThread){
            this.monitor = m;
            this.mainThread = mainThread;
            this.innerMonitor = innerMonitor;
        }

        @Override
        public void run() {
            System.out.println("Inner Thread start!");
            synchronized (innerMonitor){
                System.out.println("Inner Thread enter Inner monitor!");
                synchronized (monitor) {
                    System.out.println("Inner Thread enter monitor!");
                }
            }
            System.out.println("Inner Thread end!");
        }
    }
}
