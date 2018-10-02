package springTransaction;

/**
 * 业务秒杀库存接口
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/21
 */
public interface TStockService {

    /**
     * 消耗库存
     * @return
     */
    boolean consumeStock(long stockId , String userName);
}
