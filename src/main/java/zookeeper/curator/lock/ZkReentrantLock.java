package zookeeper.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.test.TestingServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * zk实现可重入的分布式锁
 * 其实现其实跟jdk自带的ReentrantLock类似
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/20
 */
public class ZkReentrantLock {

    /**
     * curator实现的可重入锁
     */
    private InterProcessLock lock;

    /**
     * 共享资源的访问
     */
    private final SimulateResource simulateResource;

    /**
     * 客户端名称
     */
    private final String clientName;

    /**
     * 构造方法
     * @param curatorFramWork
     * @param lockPath
     * @param simulateResource
     * @param clientName
     */
    public ZkReentrantLock(CuratorFramework curatorFramWork ,String lockPath , SimulateResource simulateResource, String clientName) {
        this.simulateResource = simulateResource;
        this.clientName = clientName;
        //互斥锁的实现
        this.lock = new InterProcessMutex(curatorFramWork , lockPath);
    }

    /**
     * 对共享资源+1，并发操作
     * @param time
     * @param timeUnit
     * @throws Exception
     */
    public void incr(long time , TimeUnit timeUnit) throws Exception{
        //获取锁
        if(!lock.acquire(time , timeUnit)){
            throw new IllegalStateException(clientName + " can not acquire the lock!");
        }

        try{
            System.out.println(clientName + " get the lock!");
            //调用+1操作
            simulateResource.incr();
            System.out.println(clientName + " get the lock!");
        }finally{
            System.out.println(clientName + " release the lock!");
            lock.release();
        }
    }

    /**
     * 服务器数量
     */
    private static final int SERVER_COUNT = 5;

    /**
     * 重复次数
     */
    private static final int REPEAT_TIME = 5 * 10;

    /**
     * zk路径
     */
    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception {
        final SimulateResource resource = new SimulateResource();
        ExecutorService service = Executors.newFixedThreadPool(SERVER_COUNT);
        final TestingServer testingServer = new TestingServer();

    }

}
