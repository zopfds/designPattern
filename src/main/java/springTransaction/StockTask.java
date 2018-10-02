package springTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
public class StockTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(StockTask.class);

    private TStockService tStockService;

    private int threadId;

    private long stockId;

    private String userName;

    private CountDownLatch countDownLatch;

    private CountDownLatch finishedCountDownLatch;

    public StockTask(TStockService tStockService, int threadId, long stockId , String userName, CountDownLatch countDownLatch , CountDownLatch finishedCountDownLatch) {
        this.tStockService = tStockService;
        this.threadId = threadId;
        this.stockId = stockId;
        this.userName = userName;
        this.countDownLatch = countDownLatch;
        this.finishedCountDownLatch = finishedCountDownLatch;
    }

    @Override
    public void run() {

        try{
            countDownLatch.await();

            logger.info("【库存任务】更新库存开始！threadId = {} , stockId = {} , userName = {}" , threadId , stockId , userName);

            boolean result = tStockService.consumeStock(stockId , userName);

            logger.info("【库存任务】更新库存开始！threadId = {} , stockId = {} , userName = {} , result = {}"
                    , threadId , stockId , userName , result);
        }catch(Exception e){
            logger.error("【【库存任务】更新库存异常！threadId = {} , stockId = {} , userName = {} , result = {}"
                    , threadId , stockId , userName, e);
        }finally{
            finishedCountDownLatch.countDown();
        }
    }
}
