package springTransaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import springTransaction.StockOrderService;
import springTransaction.StockService;
import springTransaction.TStockService;
import springTransaction.dao.StockOrder;

/**
 * 库存服务
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/21
 */
@Service
public class TStockServiceImpl implements TStockService{

    private static final Logger logger = LoggerFactory.getLogger(TStockServiceImpl.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private StockOrderService stockOrderService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED , rollbackFor = Exception.class)
    public boolean consumeStock(long stockId, String userName) {

        try{
            boolean result = stockService.updateUsedStock(stockId);

            if(!result){
                logger.error("【TStockServiceImpl.consumeStock】扣件库存表库存失败! stockId = {} , userName = {}" , stockId , userName);
                throw new Exception("扣件库存表失败");
            }

            result = stockOrderService.addStockOrder(new StockOrder(stockId , userName));

            if(!result){
                logger.error("【TStockServiceImpl.consumeStock】扣件库存表库存失败! stockId = {} , userName = {}" , stockId , userName);
                throw new Exception("扣件库存表失败");
            }

            return true;

        }catch(Exception e){

            logger.error("【TStockServiceImpl.consumeStock】消耗库存失败! stockId = {} , userName = {}" , stockId , userName , e);

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return false;
    }


}
