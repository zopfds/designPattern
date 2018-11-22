package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * zk curator客户端
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class ZkClientUtil {

    private static volatile String host;

    private static CuratorFramework client;

    private static ThreadPoolExecutor pool;

    public static void setHost(String host) {
        ZkClientUtil.host = host;
    }

    private ZkClientUtil(CuratorFramework client , ThreadPoolExecutor pool) {
        this.client = client;
        this.pool = pool;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public static CuratorFramework getInstance(){
        return ZkClientUtilHolder.instance.getClient();
    }

    /**
     * 内部类实现线程安全类
     */
    private static final class ZkClientUtilHolder{
        private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        public static ZkClientUtil instance = new ZkClientUtil(client = CuratorFrameworkFactory.builder()
                .connectString(host)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build(),
                new ThreadPoolExecutor(2 ,
                        4 ,
                        5000,
                        TimeUnit.MILLISECONDS ,
                        new ArrayBlockingQueue<Runnable>(20 , true),
                        new ThreadPoolExecutor.CallerRunsPolicy()));
    }
}
