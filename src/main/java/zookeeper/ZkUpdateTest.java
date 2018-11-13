package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zk更细节点测试
 */
public class ZkUpdateTest implements Watcher{

    //链接建立的锁
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    //zookeeper
    private static ZooKeeper zk;

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeDataChanged == event.getType()){
                System.out.println("detected node data change : " + event.getPath() + " , state = " + event.getState());
                try {
                    zk.exists(event.getPath() , true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(Event.EventType.NodeCreated == event.getType()) {
                System.out.println("detected node create : " + event.getPath() + " , state = " + event.getState());
                try {
                    zk.exists(event.getPath() , true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(Event.EventType.NodeDeleted == event.getType()){
                System.out.println("detected node delete : " + event.getPath() + " , state = " + event.getState());
                try {
                    zk.exists(event.getPath() , true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String path = "/watf2";

        zk = new ZooKeeper("127.0.0.1:2181" , 5000 , new ZkUpdateTest());

        countDownLatch.await();

        zk.exists(path , true);

        zk.create(path , "fuck".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
//
//        zk.getData(path , true , null);
//
//        Stat stat = zk.setData(path , "456".getBytes() , -1);
//
//        System.out.println(stat.getCzxid() + " , " + stat.getMzxid() + " , " + stat.getVersion());
//
//        Stat stat2 = zk.setData(path , "123".getBytes() , stat.getVersion());
//
//        System.out.println(stat2.getCzxid() + " , " + stat2.getMzxid() + " , " + stat2.getVersion());

        zk.create(path + "/c1" , "13".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);

        zk.setData(path + "/c1" , "fuck".getBytes() , -1);

        zk.delete(path + "/c1" , -1);

        Thread.currentThread().sleep(20 * 1000);
    }
}
