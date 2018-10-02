package springTransaction.mapper;

import org.springframework.stereotype.Repository;
import springTransaction.dao.StockOrder;

import java.util.List;

/**
 * 库存订单
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
@Repository
public interface TStockOrderMapper {

    /**
     * 插入库存订单mapper
     * @param stockOrder
     * @return
     */
    int addOrder(StockOrder stockOrder);

}
