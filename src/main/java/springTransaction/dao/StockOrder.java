package springTransaction.dao;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
public class StockOrder implements Serializable{

    private long id;

    private long stockId;

    private String userName;

    public StockOrder(long stockId, String userName) {
        this.stockId = stockId;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "StockOrder{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
