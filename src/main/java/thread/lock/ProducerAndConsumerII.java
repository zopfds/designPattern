package thread.lock;


import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ProducerAndConsumerII {

    /**
     * 货物队列
     */
    private LinkedList<Integer> queue = new LinkedList<>();

    /**
     * 队列最大值
     */
    private final int MAX_SIZE;

    /**
     * 是否已经被通知了
     */
    private boolean isNotify;

    /**
     * 货物满信号量
     */
    private final Object fullSign = new Object();

    /**
     * 货物清空信号量
     */
    private final Object emptySign = new Object();

    public ProducerAndConsumerII(int limit) {
        MAX_SIZE = limit;
    }

    /**
     * 向队列中生产货物
     * @param o
     */
    public void offer(Integer o){
        synchronized (queue){
            while(queue.size() >= MAX_SIZE){
                System.out.println(Thread.currentThread().getName() + " 队列已满，当前生产线程等待！ size = " + queue.size());
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addFirst(o);
            queue.notifyAll();
        }
    }

    public Integer poll(){
        synchronized (queue){
            while(queue.size() == 0){
                System.out.println(Thread.currentThread().getName() + " 队列已空，当前消费线程等待！ size = " + queue.size());
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Integer result = queue.removeLast();
            queue.notifyAll();
            return result;
        }
    }

    public static void main(String[] args){
        final ProducerAndConsumerII test = new ProducerAndConsumerII(10);
        IntStream.range(0 , 10).forEach(a -> {new Thread(() -> {
            IntStream.range(0, 10).forEach(b -> {
                System.out.println(Thread.currentThread().getName() + " 开始往队列中插入数据 data = " + a);
                test.offer(a);
                System.out.println(Thread.currentThread().getName() + " 结束往队列中插入数据 data = " + a);
            });
        }).start();
        });

        IntStream.range(0 , 2).forEach(a -> {new Thread(() -> {
            IntStream.range(0, 50).forEach(b -> {
                System.out.println(Thread.currentThread().getName() + " 开始往队列中取数据");
                Integer data = test.poll();
                System.out.println(Thread.currentThread().getName() + " 结束往队列中插入数据 data = " + data);
            });
        }).start();
        });

        while(true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("当前队列中货物数量 size =" + test.queue.size());
        }
    }
}
