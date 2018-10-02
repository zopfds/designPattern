package springTransaction;

import springTransaction.dao.Stock;

/**
 * 库存服务
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
public interface StockService {

    /**
     * 更新已用的库存
     * @param id
     * @return
     */
    boolean updateUsedStock(long id);


    /**
     * 获取库存id
     * @param id
     * @return
     */
    Stock getById(long id);
}
