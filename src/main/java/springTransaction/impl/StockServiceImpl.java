package springTransaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springTransaction.StockService;
import springTransaction.dao.Stock;
import springTransaction.mapper.TStockMapper;

import javax.annotation.Resource;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
@Service
public class StockServiceImpl implements StockService{

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Resource
    private TStockMapper tStockMapper;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    public boolean updateUsedStock(long id) {
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
//        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
//        TransactionStatus status = transactionManager.getTransaction(def);
        try{
            Stock stock = tStockMapper.getById(2);

            logger.info("【StockServiceImpl.updateUsedStock】更新库存获取库存！stock = {}" , stock);

            boolean updateResult = tStockMapper.updateUsedStock(id) > 0;

            logger.info("【StockServiceImpl.updateUsedStock】更新库存结果！id = {} , updateResult = {}"
                    , id , updateResult);

//            transactionManager.rollback(status);

        }catch(Exception e){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("【StockServiceImpl.updateUsedStock】更新库存失败，回滚! id = {}" , id ,e);
        }

        return false;
    }

    @Override
    public Stock getById(long id) {
        return tStockMapper.getById(id);
    }
}
