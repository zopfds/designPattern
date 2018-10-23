package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.server.DataTree;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 模拟测试创建zk链接，通过实现WATCHER当收到链接建立事件后打印日志
 *
 * 注意ZK构造方法并非构造出来后链接就建立，必须等到链接建立的事件通知
 *
 *
 */
public class ZkSessionTest implements Watcher{
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,new ZkSessionTest());
        System.out.println(zooKeeper.getState());

        try{
            connectedSemaphore.await();
        }catch(InterruptedException e){
            System.out.println("zkSession estlabished!");
        }

        /**
         * 使用同步接口创建两种类型的临时节点
         */
        try {
            String path1 = zooKeeper.create("/zk-Test-ephemeral","".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

            System.out.println("successed created znode: " + path1);

            String path2 = zooKeeper.create("/zk-Test-ephemeral","".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println("successed created znode: " + path2);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**
         * 以下为异步方式创建节点
         */
        zooKeeper.create("/zk-Test-ephemeral","".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE  , CreateMode.EPHEMERAL , new IStringCallBack() , "I am context!");

        zooKeeper.create("/zk-Test-ephemeral","".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE  , CreateMode.EPHEMERAL , new IStringCallBack() , "I am context!");

        zooKeeper.create("/zk-Test-ephemeral","".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE  , CreateMode.EPHEMERAL_SEQUENTIAL , new IStringCallBack() , "I am context!");


        try {
            Thread.currentThread().sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Received watched event:" + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            connectedSemaphore.countDown();
        }
    }

    /**
     * 异步创建节点需要回调的类
     */
    private static class IStringCallBack implements AsyncCallback.StringCallback{

        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            //接口调用成功
            if(rc == 0){
                System.out.println("Create path success , result:[" + rc + "," + path + "," + ctx + " , real path name:" + name);
                //客户端和服务器链接已断开
            }else if(rc == -4){
                System.out.println("Connection loss ,  result:[" + rc + "," + path + "," + ctx + " , real path name:" + name);
                //节点已经存在
            }else if(rc == -110){
                System.out.println("Node already exist ,  result:[" + rc + "," + path + "," + ctx + " , real path name:" + name);
                //会话已过期
            }else if(rc == -112){
                System.out.println("Session expired ,  result:[" + rc + "," + path + "," + ctx + " , real path name:" + name);
            }
        }
    }
}
