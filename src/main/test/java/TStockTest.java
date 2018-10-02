import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springTransaction.*;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class TStockTest {

    private static final Logger logger = LoggerFactory.getLogger(TStockTest.class);

    @Autowired
    private TStockService tStockService;

    @Autowired
    private TransactionTestService transactionTestService;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private StockService stockService;

    @Test
    public void consumeTaskTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        long stockId = 1L;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch finishedCountDownLatch = new CountDownLatch(200);

        for(int i = 0 ; i < 200 ; i++){
            executorService.submit(new StockTask(tStockService , i , 1 , "test"+i , countDownLatch , finishedCountDownLatch));
        }

        long startTime = System.currentTimeMillis();
        logger.info("【consumeTaskTest】开始扣减库存! startTime = {}" , System.currentTimeMillis());
        countDownLatch.countDown();

        try {
            finishedCountDownLatch.await();
            long endTime = System.currentTimeMillis();
            logger.info("【consumeTaskTest】扣减库存结束！startTime = {} , endTime = {} , consumeTime = {}" , startTime , endTime , endTime - startTime);

            Thread.currentThread().sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void firstLostTest(){
        Object notifyObject = new Object();
        TransactionTask commitTask =  new TransactionTask(notifyObject , dataSourceTransactionManager , stockService);
        TransactionRollbackTask rollBackTask = new TransactionRollbackTask(notifyObject , dataSourceTransactionManager , stockService);

        new Thread(rollBackTask).start();

        try {
            Thread.currentThread().sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nestTest(){
        transactionTestService.nestTest();
    }
}
