package springTransaction;

import springTransaction.dao.StockOrder;

/**
 * 库存订单服务
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
public interface StockOrderService {

    /**
     * 新增库存订单
     * @return
     */
    boolean addStockOrder(StockOrder stockOrder);
}
