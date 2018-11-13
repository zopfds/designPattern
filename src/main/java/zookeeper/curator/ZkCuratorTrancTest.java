package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/9
 */
public class ZkCuratorTrancTest {

    private static CuratorFramework client;

    private static ThreadPoolExecutor pool;

    static{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build();

        pool = new ThreadPoolExecutor(2 ,
                4 ,
                5000,
                TimeUnit.MILLISECONDS ,
                new ArrayBlockingQueue<Runnable>(20 , true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void main(String[] args){

    }
}
