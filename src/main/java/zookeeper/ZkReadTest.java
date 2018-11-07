package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 读取节点的子节点信息(包含同步和异步模式)
 */
public class ZkReadTest implements Watcher{

    private static ZooKeeper zk;

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/testAsyncRead2";

        zk = new ZooKeeper("127.0.0.1:2181" , 5000 , new ZkReadTest());

        connectedSemaphore.await();

        zk.create(path , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);

        zk.create(path + "/c1" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

//        List<String> childrenList = zk.getChildren("/testRead",true);
//        System.out.println(childrenList);

        zk.getChildren(path , true , new IChildrenCallback() , null);

        zk.create(path + "/c2" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

        Thread.currentThread().sleep(10 * 1000);
    }


    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                connectedSemaphore.countDown();
            }else if(event.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("receive childrenChanged event,start to get newest data from zk : " + zk.getChildren(event.getPath() , true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class IChildrenCallback implements AsyncCallback.Children2Callback{

        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            System.out.println("get children znode result:[ response code:" + rc +
                    " , param path:" + path +
                    " , ctx:" + ctx +
                    ", children list:" + children +
                    ", stat: " + stat);
        }
    }
}
