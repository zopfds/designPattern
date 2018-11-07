package lock.concurrentHolder;

import org.springframework.util.StringUtils;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟ArrayBlockingQueue队列,底层用atomicInteger和ReentrantLock实现
 *
 *
 * 典型的生产者消费者模型
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/7
 */
public class SelfArrayBlockingQueue<T> {

    /**
     * 队列元素数组
     */
    private Object[] items;

    /**
     * 数组元素大小
     */
    private int capacity;

    /**
     * 当前数量
     */
    private int count = 0;

    /**
     * 锁
     */
    private ReentrantLock lock;

    /**
     * 因为队列为环形队列，所以需要put当前的位置，相当于队列的结尾
     */
    private int putIndex;

    /**
     * 因为队列为环形队列，所以需要take当前的位置，相当于队列的开头
     */
    private int takeIndex;

    /**
     * 非空信号
     */
    private Condition notEmpty;

    /**
     * 没满信号
     */
    private Condition notFull;

    /**
     * 构造方法
     * @param capacity
     */
    public SelfArrayBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.items = new Object[capacity];
        this.lock = new ReentrantLock();
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
    }

    public boolean put(T t) throws InterruptedException {
        try {
            //先加锁
            lock.lockInterruptibly();
            //若当前数组数量等于数组
            while (count == capacity) {
                notFull.await();
            }

            //被唤醒,队列元素数量小于capacity,队列元素入队
            items[putIndex] = t;

            if(++putIndex == capacity){
                putIndex = 0;
            }
            //增加count，队列中元素的总数
            count++;
            //通知阻塞在非空信号上的线程
            notEmpty.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        try{
            //先加锁
            lock.lockInterruptibly();

            //若当前队列数量为0，在非空信号上等待
            while(count == 0){
                notEmpty.await();
            }
            //先获取结果
            T item = (T)items[takeIndex];
            //若takeIndex到了数组结果，则重新从头开始
            if(++takeIndex == capacity){
                takeIndex = 0;
            }
            //总数-1
            count--;
            //通知阻塞在非满信号上的线程
            notFull.signal();
            return item;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args){
        SelfArrayBlockingQueue<Integer> arrayBlockingQueue = new SelfArrayBlockingQueue<Integer>(5);

        for(int i = 0 ; i < 10 ; i ++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        arrayBlockingQueue.put(1);
                        System.out.println("produce thread put to queue: " + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for(int i = 0 ; i < 2 ; i ++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer a = arrayBlockingQueue.take();
                        System.out.println("consume thread take from queue: " + a);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        Scanner scanner = new Scanner(System.in);
        String message;
        while(!"stop".equals(message = scanner.nextLine())) {
            if (!StringUtils.isEmpty(message) && "p".equals(message)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Integer a = arrayBlockingQueue.take();
                            System.out.println("consume thread take from queue: " + a);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
