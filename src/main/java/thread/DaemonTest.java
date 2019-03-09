package thread;

import java.util.HashMap;

public class DaemonTest {
    public static void main(String[] args){
        InnerClass test = new InnerClass();
        test.setDaemon(false);
        test.start();
        System.out.println("main thread end!");
    }

    private static class InnerClass extends Thread{
        @Override
        public void run() {
            for(int i = 0 ; i < 10 ; i ++ ){
                System.out.println(i);
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
