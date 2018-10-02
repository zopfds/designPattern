package springTransaction;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/22
 */
public interface TransactionTestService1 {

    /**
     * 第一类更新丢失的交互线程
     */
    void firstLostFirst();

    /**
     * 第二类更新丢失的交互线程
     */
    void secondLostSecond();
}
