package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * zookeeper监听节点变更测试类
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class ZkWatcherTest {
    private static final String PATH = "/testWatcher";

    public static void main(String[] args) throws Exception {
        ZkClientUtil.setHost("127.0.0.1:2181");
        CuratorFramework curatorFramework = ZkClientUtil.getInstance();
        curatorFramework.start();

        PathChildrenCache cache = new PathChildrenCache(curatorFramework , PATH , true);
        cache.start();

        PathChildrenCacheListener listener = (client, event) -> {
          System.out.println("事件类型：" + event.getType());
          if(event.getData() != null){
              System.out.println("节点数据 : " + event.getData());
          }
        };

        cache.getListenable().addListener(listener);
        curatorFramework.create().creatingParentsIfNeeded().forPath(PATH + "/test01" , "01".getBytes());
        Thread.sleep(10);
        curatorFramework.create().creatingParentsIfNeeded().forPath(PATH + "/test02" , "02".getBytes());
        Thread.sleep(10);
        curatorFramework.setData().forPath(PATH + "/test01" , "01_v2".getBytes());
        cache.getCurrentData().forEach(d -> System.out.println("getCurrentData:" + d.getPath() + " = " + new String(d.getData())));
        curatorFramework.delete().forPath(PATH + "/test01");
        Thread.sleep(10);
        curatorFramework.delete().forPath(PATH + "/test02");
        Thread.sleep(1000 * 5);
        cache.close();
        curatorFramework.close();
        System.out.println("finished!");
    }
}
