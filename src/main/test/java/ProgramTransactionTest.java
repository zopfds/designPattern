import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class ProgramTransactionTest {

    private static final Logger logger = LoggerFactory.getLogger(ProgramTransactionTest.class);

    @Autowired
    private DruidDataSource dataSource;

    @Test
    public void submitTest(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try{
            connection.prepareStatement("INSERT INTO t_stock(total_stock , used_stock) VALUE (20 , 0);").execute();
            ResultSet resultSet = connection.prepareStatement("SELECT * from t_stock where id = 1;").executeQuery();
            while(resultSet.next()){
                logger.info("resultSet = {}" , resultSet.getString(2));
            }
            dataSourceTransactionManager.commit(transactionStatus);

        }catch(Exception e){
            logger.error("【获取事务失败】系统异常!" , e);
        }
    }
}
