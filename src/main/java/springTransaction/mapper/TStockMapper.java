package springTransaction.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import springTransaction.dao.Stock;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/17
 */
@Repository
public interface TStockMapper {

    List<Stock> getList();

    Stock getById(@Param("id") long id);

    int updateUsedStock(@Param("id") long id);

}
