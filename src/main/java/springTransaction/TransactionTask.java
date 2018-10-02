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
public class TransactionTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(TransactionTask.class);

    private Object notifyObject;

    private DataSourceTransactionManager transactionManager;

    private StockService stockService;

    public TransactionTask(Object notifyObject, DataSourceTransactionManager transactionManager, StockService stockService) {
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
            logger.info("【提交事务】开始事务!");

            stockService.updateUsedStock(2);

            transactionManager.commit(status);

            Stock stock = stockService.getById(2);

            logger.info("【提交事务】提交成功! stock = {}" , stock);

            synchronized (notifyObject) {
                notifyObject.notify();
            }

        }catch(Exception e){
            logger.error("【提交事务】提交异常!",e);
        }
    }
}
