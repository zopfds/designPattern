package springTransaction;

import com.alibaba.druid.support.json.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springTransaction.dao.Stock;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/22
 */
public class TransactionRollbackTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(TransactionRollbackTask.class);

    private Object notifyObject;

    private DataSourceTransactionManager transactionManager;

    private StockService stockService;

    public TransactionRollbackTask(Object notifyObject, DataSourceTransactionManager transactionManager, StockService stockService) {
        this.notifyObject = notifyObject;
        this.transactionManager = transactionManager;
        this.stockService = stockService;
    }

    @Override
    public void run() {
        //获取默认的事务管理definition
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try{
            logger.info("【回滚事务】开始事务!");

            new Thread(new TransactionTask(this.notifyObject , this.transactionManager , this.stockService)).start();

            synchronized (notifyObject) {
                logger.info("【回滚事务】开始事务，等待事务1通知!");
                notifyObject.wait();
            }

            logger.info("【回滚事务】开始事务，收到事务1通知!");

            stockService.updateUsedStock(2);

            Thread.currentThread().sleep(200);

            transactionManager.rollback(status);

            Stock stock = stockService.getById(2);

            logger.info("【回滚事务】回滚完成! stock = {}" , stock);

        }catch(Exception e){
            logger.error("【回滚线程】执行异常! " , e);
        }
    }
}
