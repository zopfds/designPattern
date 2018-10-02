package springTransaction.dao;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
public class Stock implements Serializable{

    /**
     * id
     */
    private long id;

    private Integer total_stock;

    private Integer used_stock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(Integer total_stock) {
        this.total_stock = total_stock;
    }

    public Integer getUsed_stock() {
        return used_stock;
    }

    public void setUsed_stock(Integer used_stock) {
        this.used_stock = used_stock;
    }



    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", total_stock=" + total_stock +
                ", used_stock=" + used_stock +
                '}';
    }
}
