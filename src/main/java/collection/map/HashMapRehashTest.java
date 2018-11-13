package collection.map;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.HashMap;

/**
 * 测试hashmap多线程插入，扩容导致死循环
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/9
 */
public class HashMapRehashTest {

    public static void main(String[] args){
        HashMap<Integer,String> map = new HashMap<>(2);
        map.put(5, "C");

        new Thread("Thread1") {
            public void run() {
                map.put(7, "B");
                System.out.println(map);
            };
        }.start();
        new Thread("Thread2") {
            public void run() {
                map.put(3, "A");
                        System.out.println(map);
            };
        }.start();
    }
}
