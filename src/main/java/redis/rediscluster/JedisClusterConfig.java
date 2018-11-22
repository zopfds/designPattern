package redis.rediscluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.Set;
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

    private static final String REDIS_CACHE_NODES = "127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005";

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

    public static void main(String[] args){
        JedisCluster jedisCluster = JedisClusterConfig.getJedisCluster();
        jedisCluster.set("testCluster" , "testCLuster");
        System.out.println(jedisCluster.get("testCluster"));
        jedisCluster.set("fuck" , "testCLuster");
        System.out.println(jedisCluster.get("fuck"));
    }

}
