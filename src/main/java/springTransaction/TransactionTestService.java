package springTransaction;

/**
 *
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/22
 */
public interface TransactionTestService {

    /**
     * 第一类更新丢失
     */
    void firstLost();

    /**
     * 第一类更新丢失交互方法
     */
    void firstLostFirst();

    /**
     * 第二类更新丢失
     */
    void secondLost();

    /**
     * 嵌套事务测试
     */
    void nestTest();
}
