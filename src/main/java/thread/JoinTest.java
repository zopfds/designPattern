package thread;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/12
 */
public class JoinTest {

    public static void main(String[] args){
        Thread y = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("join threas started");
                try {
                    Thread.currentThread().sleep(20 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("join threas end");
            }
        });

        try {
            y.start();
            y.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main end");
    }
}
