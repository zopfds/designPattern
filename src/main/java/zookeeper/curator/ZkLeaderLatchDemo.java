package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * zk选举利用LeaderLatch
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/14
 */
public class ZkLeaderLatchDemo {
        protected static String PATH = "/francis/leader";
        private static final int CLIENT_QTY = 10;


        public static void main(String[] args) throws Exception {
            List<CuratorFramework> clients = new ArrayList<>();
            List<LeaderLatch> examples = new ArrayList<>();
            TestingServer server=new TestingServer();
            try {
                for (int i = 0; i < CLIENT_QTY; i++) {
                    CuratorFramework client
                            = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(20000, 3));
                    clients.add(client);
                    LeaderLatch latch = new LeaderLatch(client, PATH, "Client #" + i);
                    latch.addListener(new LeaderLatchListener() {

                        @Override
                        public void isLeader() {
                            System.out.println(latch.getId() + " , I am Leader");
                        }

                        @Override
                        public void notLeader() {
                            System.out.println(latch.getId() + ",I am not Leader");
                        }
                    });
                    examples.add(latch);
                    client.start();
                    latch.start();
                }
                Thread.sleep(10000);
                LeaderLatch currentLeader = null;
                for (LeaderLatch latch : examples) {
                    if (latch.hasLeadership()) {
                        currentLeader = latch;
                    }
                }
                System.out.println("current leader is " + currentLeader.getId());
                System.out.println("release the leader " + currentLeader.getId());
                currentLeader.close();

                Thread.sleep(5000);

                for (LeaderLatch latch : examples) {
                    if (latch.hasLeadership()) {
                        currentLeader = latch;
                    }
                }
                System.out.println("current leader is " + currentLeader.getId());
                System.out.println("release the leader " + currentLeader.getId());
            } finally {
                for (LeaderLatch latch : examples) {
                    if (null != latch.getState())
                        CloseableUtils.closeQuietly(latch);
                }
                for (CuratorFramework client : clients) {
                    CloseableUtils.closeQuietly(client);
                }
            }
        }
}
