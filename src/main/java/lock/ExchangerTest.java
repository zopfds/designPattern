package lock;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程间通讯的
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/7
 */
public class ExchangerTest {

    private static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args){
        ExecutorService service = Executors.newFixedThreadPool(2);

        service.execute(() ->{
            try {
                String message = exchanger.exchange("fuck you!");

                System.out.println(Thread.currentThread().getName() + " receive message from other: " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.execute(() ->{
            try {
                String message = exchanger.exchange("fuck you , too !");

                System.out.println(Thread.currentThread().getName() + " receive message from other: " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
