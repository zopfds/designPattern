package thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 多人赛跑例子
 */
public class RunTest {

    private CountDownLatch countDownLatch;

    private ConcurrentHashMap<Long , Long> timeMap;

    private Long startTime;

    private long runCount;

    public RunTest(int runCount , Long startTime){
        this.runCount = runCount;
        this.countDownLatch = new CountDownLatch(runCount);
        timeMap = new ConcurrentHashMap<>(runCount);
        this.startTime = startTime;
    }

    public static void main(String[] args){
        RunTest runTest = new RunTest(10 , System.currentTimeMillis());
        runTest.run();
    }

    public void run(){
        for(long i = 0 ; i < runCount ; i ++){
            new Thread(new Runner(i)).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timeMap.entrySet().stream().forEach(entry -> System.out.println("Runner : " + entry.getKey() + " , consumeTime :" + entry.getValue() + "ms!"));
    }

     class Runner implements Runnable{

        private Long id;

        Runner(Long id){
            this.id = id;
        }

        @Override
        public void run() {

            try {
                Thread.currentThread().sleep((long)(Math.random()*5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();

            timeMap.put(id , endTime - startTime);

            countDownLatch.countDown();
        }
    }
}
