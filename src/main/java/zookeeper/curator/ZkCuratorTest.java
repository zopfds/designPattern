package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * zkCurator客户端测试类
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/9
 */
public class ZkCuratorTest {

    private static CuratorFramework client = null;

    static{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build();
    }

    public static void main(String [] args){

        client.start();

        String path = "/testCurator/1";

        try {
//            client.create().creatingParentContainersIfNeeded()
//                    .withMode(CreateMode.PERSISTENT)
//                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                    .forPath(path,"abc".getBytes());
//
//            byte[] readBuffer = client.getData().forPath(path);
//
//            System.out.println("test create and set node data to zk success! data:" + new String(readBuffer));

            Stat stat = new Stat();

            client.getData().storingStatIn(stat).forPath(path);

            System.out.println("try to get version from path , version : " + stat.getVersion());




//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        client.setData().withVersion(stat.getVersion()).forPath(path,"thread1".getBytes());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        client.setData().withVersion(stat.getVersion()).forPath(path,"thread2".getBytes());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//            Thread.currentThread().sleep(10 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
