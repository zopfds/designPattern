package redis.rediscluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.IdGenerator;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import zookeeper.curator.lock.SimulateResource;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * jedis集群测试
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/21
 */
public class JedisClusterConfig {

    private static final Logger logger = LoggerFactory.getLogger(JedisClusterConfig.class);

    private static final IdGenerator idGenerator = new SimpleIdGenerator();

    private static final String REDIS_CACHE_NODES = "127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005";

    private static final ThreadLocal<String> flowId = new ThreadLocal<>();

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    private static final String delKeyLuaScript = "local times = redis.call('get',KEYS[1])\n" +
            "if times == ARGV[1] then\n" +
            "    redis.call('del',KEYS[1])\n" +
            "    return 1\n" +
            "else\n" +
            "    return 0\n" +
            "end";

    private static final class JedisClusterHolder{
        private static final JedisCluster jedisCluster = getJedisCluster();
        private static final JedisCluster getJedisCluster(){
            String[] serverArray = REDIS_CACHE_NODES.split(",");
            Set<HostAndPort> nodes =  Arrays.stream(serverArray).map(nodeString -> {
                String[] ipPortPair = nodeString.split(":");
                return new HostAndPort(ipPortPair[0] , Integer.valueOf(ipPortPair[1]));
            }).collect(Collectors.toSet());
            return new JedisCluster(nodes, 5000);
        }
    }

    public static JedisCluster getJedisCluster(){
        return JedisClusterHolder.jedisCluster;
    }

    public void set(String key , String value) throws IllegalArgumentException{
        if(StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not null!");
        getJedisCluster().set(key , value);
        logger.info("JedisCluster set key successed! key = {}  , value = {}" , key , value);
    }

    public String get(String key) throws IllegalArgumentException{
        if(StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not null!");
        String result = getJedisCluster().get(key);
        logger.info("JedisCluster get key successed! key = {}  , value = {}" , key , result);
        return result;
    }

    /**
     * redis实现的分布式锁
     * @param key
     * @return
     */
    public static boolean getDistributeLock(String key){
        while(true) {
            if (StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not null!");
            flowId.set(idGenerator.generateId().toString().replaceAll("-", ""));
            startTime.set(System.currentTimeMillis());
            String result = getJedisCluster().set(key, flowId.get(), "NX", "EX", 5000);
            logger.info("lock successed! key = {} , value = {} , startTime = {}", key, flowId.get(), startTime.get());
            if ("OK".equals(result)) {
                return true;
            }
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 利用lua脚本释放分布式锁
     * @param key
     * @return
     */
    public static boolean releaseDistributeLock(String key){
        if(StringUtils.isEmpty(key)) throw new IllegalArgumentException("key must not null!");
        String value = flowId.get();
        try {
            Long result = (Long) getJedisCluster().eval(delKeyLuaScript , Arrays.asList(key) , Arrays.asList(value));
            Long endTime = System.currentTimeMillis();
            logger.info("unlock result! key = {} , value = {} , startTime = {} , endTime = {} , consumeTime = {}"
                    , key , flowId.get() , startTime.get() , endTime , endTime - startTime.get());
            if(result == 1){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            logger.error("jedisCluster del key failed! key = {} , value = {}" , key , value);
        }
        return false;
    }



    public static void main(String[] args){
//        boolean result = JedisClusterConfig.getDistributeLock("testLock");
//        System.out.println("get distributelock result = " + result);
//        boolean releaseResult = JedisClusterConfig.releaseDistributeLock("testLock");
//        System.out.println("release lock result = "  + releaseResult);

        final SimulateResource resource = new SimulateResource();
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        String key = "distributeLockTest";

        Long startTime = System.currentTimeMillis();

        logger.info("begin get Lock , key = {} , startTime = {}" ,key ,  startTime );
        for(int i = 0 ; i < 10 ; i ++){
            new Thread(() -> {
                for(int j = 0 ; j < 100 ; j ++) {
                    getDistributeLock(key);
                    try {
                        resource.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    releaseDistributeLock(key);
                }
                countDownLatch.countDown();
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        logger.info("end get lock , key = {} , endTime = {} , startTime = {} , totalTime = {} , finalCount = {}" ,
                key , startTime , endTime , endTime - startTime , resource.get());

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
