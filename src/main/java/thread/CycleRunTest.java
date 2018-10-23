package thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

public class CycleRunTest {

    private ConcurrentHashMap<Long , Long> startTimeMap;

    private ConcurrentHashMap<Long , Long> endTimeMap;

    private CyclicBarrier cyclicBarrier;

    private int userCount;

    CycleRunTest(int userCount){
        this.userCount = userCount;
        startTimeMap = new ConcurrentHashMap<>(userCount);
        endTimeMap = new ConcurrentHashMap<>(userCount);
        cyclicBarrier = new CyclicBarrier(userCount);
    }

    public static void main(String[] args){
        CycleRunTest cycleRunTest = new CycleRunTest(5);

        cycleRunTest.run();

        try {
            Thread.currentThread().sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Stream.iterate(0 , i -> i + 1).limit(userCount).forEach(i -> {
            new Thread(new Runner((long)i)).start();
        });


        while(cyclicBarrier.getNumberWaiting() > 0) {
            try {
                Thread.currentThread().sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        startTimeMap.entrySet().stream().forEach(entry -> System.out.println("runner : " +
                entry.getKey() +
                " , startTime : " +
                entry.getValue() +
                " , endTime = "  +
                endTimeMap.get(entry.getKey()) +
                " , consumeTime :"+
                String.valueOf(endTimeMap.get(entry.getKey()) - entry.getValue())));

    }

    class Runner implements Runnable{

        private Long id;

        Runner(Long id){
            this.id = id;
        }

        @Override
        public void run() {

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            startTimeMap.put(id , System.currentTimeMillis());

            try {
                Thread.currentThread().sleep((long)(Math.random()*5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            endTimeMap.put(id , System.currentTimeMillis());

            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
