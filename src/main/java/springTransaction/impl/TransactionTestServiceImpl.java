package springTransaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springTransaction.StockOrderService;
import springTransaction.StockService;
import springTransaction.TransactionTestService;
import springTransaction.dao.StockOrder;
import springTransaction.mapper.TStockMapper;

import javax.annotation.Resource;
import java.util.concurrent.Semaphore;

/**
 * 事务测试服务
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/22
 */
@Service
public class TransactionTestServiceImpl implements TransactionTestService{

    private static final Logger logger = LoggerFactory.getLogger(TransactionTestServiceImpl.class);

    @Autowired
    private StockService stockService;

    private static Semaphore firstNotifyObject = new Semaphore(1);

    @Autowired
    private TStockMapper tStockMapper;

    @Resource(name = "tradeTransactionManager")
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private StockOrderService stockOrderService;

    @Override
    public void firstLost() {
            //获取默认的事务管理definition
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                logger.info("【事务1】开始事务!");

                synchronized (firstNotifyObject) {
                    firstNotifyObject.notify();
                    logger.info("【事务1】通知事务2执行开始!");
                }

                boolean result = stockService.updateUsedStock(2);

                logger.info("【事务1】更新完成！");

                synchronized (firstNotifyObject) {
                    firstNotifyObject.wait();
                }

                transactionManager.commit(status);

                logger.info("【事务1】提交成功!");

                synchronized (firstNotifyObject) {
                    firstNotifyObject.notify();
                }

            } catch (Exception e) {
                logger.error("【事务1】异常回滚! ", e);
            }
    }

    @Override
    public void firstLostFirst() {
            //获取默认的事务管理definition
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                logger.info("【事务2】开始事务!");

                this.firstLost();

                synchronized (firstNotifyObject) {
                    logger.info("【事务2】开始事务，等待事务1通知!");
                    firstNotifyObject.wait();
                }

                logger.info("【事务2】开始事务，收到事务1通知!");

                boolean result = stockService.updateUsedStock(2);

                logger.info("【事务2】更新完成！");

                Thread.currentThread().sleep(200);

                synchronized (firstNotifyObject) {
                    firstNotifyObject.notify();
                }

                synchronized (firstNotifyObject) {
                    firstNotifyObject.wait();
                }

                transactionManager.rollback(status);

                logger.info("【事务2】回滚完成!");

            } catch (Exception e) {
                logger.error("【事务2】异常回滚! ", e);
            }
    }

    @Override
    public void secondLost() {

    }

    @Override
    public void nestTest() {
        //获取默认的事务管理definition
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try{
            boolean result = tStockMapper.updateUsedStock(2) > 0;

            logger.info("【嵌套测试】外部事务更新库存1! result = {}" , result);

            stockOrderService.addStockOrder(new StockOrder(2 , "test"));

            result = stockService.updateUsedStock(2L);

            logger.info("【嵌套测试】那部事务更新库存! result = {}" , result);

            result = tStockMapper.updateUsedStock(2) > 0;

            logger.info("【嵌套测试】外部事务更新库存2! result = {}" , result);

            transactionManager.commit(status);
        }catch(Exception e){
            logger.error("【嵌套测试】系统异常! ",e);
        }
    }


}
