package springTransaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springTransaction.StockService;
import springTransaction.TransactionTestService1;

import javax.annotation.Resource;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/22
 */
public class TransactionTestService1Impl implements TransactionTestService1{

    private static final Logger logger = LoggerFactory.getLogger(TransactionTestServiceImpl.class);

    @Autowired
    private StockService stockService;

    private static Object firstNotifyObject = new Object();

    private static Object secondNotifyObject = new Object();

    @Resource(name = "tradeTransactionManager")
    private DataSourceTransactionManager transactionManager;

    @Override
    public void firstLostFirst() {

    }

    @Override
    public void secondLostSecond() {

    }
}
