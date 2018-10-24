package jvm.gclog;


/**
 * 日志测试，分析GC日志
 * vm 参数：-verbose:gc -Xms20M -Xmx20m -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
 */
public class LongLiveOldTest {

    public static final int _1MB = 1024 * 1024;

    /**
     * 长期存活的对象将从新生代转入老年代
     */
    public static void testTenuringThreshold(){
        byte[] allocation1 , allocation2 , allocation3;
        allocation1 = new byte[_1MB / 4];
        allocation2 = new byte[4 * _1MB];
        /**
         * 如果 新生代回收算法为 SerialNew 或 ParNew 与 SerialOld 组合
         *
         * 下面语句将会触发gc,因为新生代大小为10m,Eden区大小为8m,fromspace为1m,tospace为1m
         * 因Eden区大小不满足下面allocation3对象的分配，所以触发gc,又因为tospace大小不满足 allocation1 和 allocation2的大小
         * 所以 allocation1 和 allocation2 将为晋升为老年代对象
         *
         *
         * 如果采用G1垃圾收集器
         */
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }


    public static void main(String[] args){
        testTenuringThreshold();
    }
}
