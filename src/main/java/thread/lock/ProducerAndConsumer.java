package thread.lock;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 生产者和消费者(问题:notify信号丢失)
 * 第一版，本质是生产者和消费者两者线程间的协调
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/17
 */
public class ProducerAndConsumer {

    private static volatile ConcurrentLinkedDeque<String> queue = new ConcurrentLinkedDeque<String>();

    private static int maxSize = 10;

    private static int maxProducer = 20;

    private static int currentProduce = 0;

    private static Object fullSign = new Object();

    private static Object emptySign = new Object();

    /**
     * 消费者
     * @return
     */
    public static synchronized String consume(){
        while(true) {
            String elem = queue.pollFirst();
            if(elem == null){
                try {
                    emptySign.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("consumer consume object:" + elem);

            }
        }
    }


    /**
     * 生产者
     */
    public static void producer(){
        while(true && currentProduce < maxProducer){
            if(queue.size() == maxSize){
                synchronized (fullSign){
                    try {
                        System.out.println("queue full , producer thread wait!");
                        fullSign.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                String elem = String.valueOf(++currentProduce);
                queue.addFirst(elem);
                System.out.println("producer produce object:" + elem);
                if(queue.size() == 1){
                    synchronized (emptySign){
                        emptySign.notify();
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        ProducerAndConsumer producerAndConsumer = new ProducerAndConsumer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                producerAndConsumer.consume();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                producerAndConsumer.producer();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                producerAndConsumer.consume();
            }
        }).start();
    }
}
