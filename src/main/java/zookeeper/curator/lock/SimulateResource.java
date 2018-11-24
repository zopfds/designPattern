package zookeeper.curator.lock;

/**
 * 模拟共享资源的访问
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/20
 */
public class SimulateResource {
    private volatile int i = 0;

    public void incr() throws InterruptedException{
        System.out.println("before get resource , i = " + i);
        System.out.println("after incr and get resource , i = " + ++i);
    }

    public int get(){
        return i;
    }
}
