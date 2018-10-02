package springTransaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springTransaction.dao.Stock;
import springTransaction.mapper.TStockMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class TestTransaction {

    private static final Logger logger = LoggerFactory.getLogger(TestTransaction.class);

    @Resource
    private TStockMapper tStockMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private TStockService tStockService;

    @Test
    public void updateTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        long stockId = 1L;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch finishedCountDownLatch = new CountDownLatch(100);

        for(int i = 0 ; i < 200 ; i++){
            executorService.submit(new StockTask(tStockService , i , 1 , "test"+i , countDownLatch ,finishedCountDownLatch));
        }

        logger.info("【consumeTaskTest】开始扣减库存! startTime = {}" , System.currentTimeMillis());

        countDownLatch.countDown();

        try {
            finishedCountDownLatch.await();
            logger.info("【consumeTaskTest】扣减库存结束！startTime = {} , endTime = {} , consume");
            Thread.currentThread().sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
