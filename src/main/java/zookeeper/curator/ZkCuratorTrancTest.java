package zookeeper.curator;

import com.alibaba.fastjson.JSON;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

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
        try {
            client.start();
            Stat stat = client.checkExists().forPath("/path");
            System.out.println("path is exist : " + JSON.toJSONString(stat));
            String path = "/pathtest";
            client.inTransaction()
                    .create().withMode(CreateMode.EPHEMERAL).forPath(path,"data".getBytes())
                    .and()
                    .setData().withVersion(0).forPath(path,"data2".getBytes())
                    .and()
                    .commit();

            byte[] buffer = client.getData().forPath(path);

            System.out.println("after transaction \"path\" : " + new String(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
