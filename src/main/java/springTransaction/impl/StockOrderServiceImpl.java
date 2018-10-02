package springTransaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import springTransaction.StockOrderService;
import springTransaction.dao.StockOrder;
import springTransaction.mapper.TStockOrderMapper;

import javax.annotation.Resource;

/**
 * 库存订单服务实现
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
@Service
public class StockOrderServiceImpl implements StockOrderService{

    private static final Logger logger = LoggerFactory.getLogger(StockOrderServiceImpl.class);

    @Resource
    private TStockOrderMapper tStockOrderMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY , isolation = Isolation.READ_UNCOMMITTED , rollbackFor = Exception.class)
    public boolean addStockOrder(StockOrder stockOrder) {
        try {
            boolean result = tStockOrderMapper.addOrder(stockOrder) > 0;

            throw new Exception("test");
        }catch(Exception e){
            logger.error("【新增订单】新增订单异常! stockOrder = {}"  , stockOrder , e);

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return false;
    }


}
