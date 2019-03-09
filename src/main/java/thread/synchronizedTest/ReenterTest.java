package thread.synchronizedTest;



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
