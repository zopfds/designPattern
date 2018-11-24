
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * Jedis工具类<br />针对一台缓存服务器
 *
 * @author liupoyang
 * @since 2014-01-08
 */
public class JedisUtil implements ApplicationContextAware{
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    /**
     * Redis主机
     */
    private static String redisServerHost;
    /**
     * Redis端口
     */
    private static int redisServerPort;
    /**
     * Redis密码
     */
    private static String redisServerPassword;
    /**
     * Redis超时
     */
    private static int redisServerTimeout;
    /**
     * 缓存Key前缀
     */
    private static String keyPrefix;
    /**
     * 缓存开关
     */
    private static boolean redisSwitch;
    /**
     * 最大连接数
     */
    public static final int CONFIG_MAX_ACTIVE = 50;
    /**
     * 最大空闲连接数，-1 表示无限制
     */
    public static final int CONFIG_MAX_IDLE = -1;
    /**
     * 取一个连接的最长阻塞时间, milliseconds. 此处设置为10秒
     */
    public static final int CONFIG_MAX_WAITE_MILLISECOND = 10 * 1000;
    /**
     * 连接池达到最大连接数后取连接的行为:此处为阻塞等待，直到有连接返回，或达到最大阻塞时间返回失败
     */
    public static final byte CONFIG_EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;

    /**
     * Jedis工具类实例对象
     */
    private static JedisUtil instance;
    /**
     * Jedis连接池配置对象
     */
    private static JedisPoolConfig config;
    /**
     * Jedis连接池对象
     */
    private JedisPool pool;


    private static ApplicationContext applicationContext;

    public void setRedisServerHost(String redisServerHost) {
        this.redisServerHost = redisServerHost;
    }
    public void setRedisServerPort(int redisServerPort) {
        this.redisServerPort = redisServerPort;
    }
    public void setRedisServerPassword(String redisServerPassword) {
        this.redisServerPassword = redisServerPassword;
    }
    public void setRedisServerTimeout(int redisServerTimeout) {
        this.redisServerTimeout = redisServerTimeout;
    }
    public String getKeyPrefix() {
        return keyPrefix;
    }
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
    public void setRedisSwitch(boolean redisSwitch) {
        this.redisSwitch = redisSwitch;
    }

    /**
     * 私有构造器，初始化连接池
     */
    @SuppressWarnings("DM_BOXED_PRIMITIVE_FOR_PARSING")
    private JedisUtil() {
        config = new JedisPoolConfig();
        config.setMaxTotal(CONFIG_MAX_ACTIVE); //config.setMaxActive(CONFIG_MAX_ACTIVE);
        config.setMaxIdle(CONFIG_MAX_IDLE);
        config.setMaxWaitMillis(CONFIG_MAX_WAITE_MILLISECOND);//config.setMaxWait(CONFIG_MAX_WAITE_MILLISECOND);
        config.setBlockWhenExhausted(false);//config.setWhenExhaustedAction(CONFIG_EXHAUSTED_ACTION);
    }

    /**
     * 获取连接池单例对象
     * @return JedisUtil
     */
    public static JedisUtil getInstance() {
        if (instance == null) {
			/*instance = (JedisUtil) Launcher.getCtxProducer().getBean("redisConfig");*/
            instance = (JedisUtil) applicationContext.getBean("redisConfig");
        }
        return instance;
    }

    /**
     * 从连接池中获取Jedis
     * @return
     */
    public Jedis getJedis() {
//        if(pool == null){
//            pool = new JedisPool(config, redisServerHost, redisServerPort, redisServerTimeout, redisServerPassword);
//        }

        return JedisPoolHolder.getPoolInstance().getResource();
    }

    /**
     * 利用静态内部类的线程安全的单例模式
     * (由jvm确保类的加在是单线程的，线程安全的)
     */
    private static final class JedisPoolHolder{
        private static final JedisPool poolInstance = new JedisPool(config, redisServerHost, redisServerPort, redisServerTimeout, redisServerPassword);

        public static JedisPool getPoolInstance(){
            return poolInstance;
        }
    }

    /**
     * 释放Jedis回连接池
     * @param jedis
     */
    public void releaseJedis(Jedis jedis) {
        if (jedis != null) {
//            if(pool == null){
//                pool = new JedisPool(config, redisServerHost,redisServerPort,redisServerTimeout, redisServerPassword);
//            }
            JedisPoolHolder.getPoolInstance().returnResource(jedis);
        }
    }

    /**
     * 设置缓存对象，并指定缓存时间
     * @param key 键
     * @param object 要缓存的对象
     * @param seconds 要缓存的时间
     */
    public void set(String key, Object object, int seconds){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    jedis.set(key.getBytes(), SerializationUtils.serialize(object));
                    jedis.expire(key.getBytes(), seconds);
                }
            }catch(Exception e){
                logger.error("#设置缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
    }

    /**
     * 设置指定key的值
     * @param key 键
     * @param value
     */
    public void set(String key, String value){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    jedis.set(key, value);
                }
            }catch(Exception e){
                logger.error("#设置指定key的值", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
    }

    /**
     * 设置指定key的值
     * @param key 键
     * @param value
     */
    public Long setnx(String key, String value){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    return jedis.setnx(key, value);
                }
            }catch(Exception e){
                logger.error("#设置指定key的值", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return 0L;
    }

    /**
     * 获取缓存对象
     * @param key 键
     * @return Object
     */
    public Object get(String key){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();

                if (jedis != null)
                    return SerializationUtils.deserialize(jedis.get(key.getBytes()));

            }catch(Exception e){
                logger.error("#获取缓存对象时发生错误", e);
                return null;
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
        return null;
    }

    /**
     * 获取缓存
     * @param key 键
     * @return Object
     */
    public String getString(String key){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();

                if (jedis != null)
                    return jedis.get(key);

            }catch(Exception e){
                logger.error("#获取缓存", e);
                return null;
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
        return null;
    }

    /**
     * 清除缓存对象
     * @param key 键
     * @return Object
     */
    public void del(String key){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    jedis.del(key.getBytes());
                }
            }catch(Exception e){
                logger.error("#清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
    }

    /**
     * 批量清除缓存对象
     * @param key 键
     * @return Object
     */
    public void delBatch(String key){

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Set<String> keys = jedis.keys(key);
                    if(CollectionUtils.isEmpty(keys)){
                        return;
                    }
                    jedis.del(keys.toArray(new String[keys.size()]));
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
    }

    /**
     * 从哈希集中读取全部的域和值
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key){

        Map<String, String> result = new HashMap<String, String>();

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    result = jedis.hgetAll(key);
                }
            }catch(Exception e){
                logger.error("#从哈希集中读取全部的域和值", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return result;

    }

    /**
     * 获取hash里面指定字段的值
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(String key, String...fields){

        List<String> result = new ArrayList<String>();

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    result = jedis.hmget(key, fields);
                }
            }catch(Exception e){
                logger.error("#获取hash里面指定字段的值", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return result;
    }

    /**
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long hincrby(String key, String field, long value){

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.hincrBy(key, field, value);
                    return (result == null) ? 0 : result;
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return 0;
    }

    /**
     * 设置一个key的过期的秒数
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds){
        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null)
                    jedis.expire(key, seconds);
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }
    }

    /**
     * 为有序集key的成员member的score值加上增量increment。
     * @param key
     * @param score
     * @param member
     * @return
     */
    public boolean zadd(String key, double score, String member){

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.zadd(key, score, member);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;
    }

    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    return jedis.zrangeWithScores(key, start, end);
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return null;
    }

    /**
     * 返回的成员在排序设置的范围
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    return jedis.zrevrangeWithScores(key, start, end);
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return null;
    }

    /**
     * 增量的一名成员在排序设置的评分
     * @param key
     * @param score
     * @param member
     * @return
     */
    public double zincrby(String key, double score, String member) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Double result = jedis.zincrby(key, score, member);
                    return result == null ? 0 : result;
                }
            }catch(Exception e){
                logger.error("#批量清除缓存对象时发生错误", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return 0;
    }

    /**
     * 添加一个或者多个元素到集合(set)里
     * @param key
     * @param members
     * @return
     */
    public boolean sadd(String key, String... members){

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.sadd(key, members);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#添加一个或者多个元素到集合(set)里", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;

    }

    /**
     * 从集合里删除一个或多个key
     * @param key
     * @param members
     * @return
     */
    public boolean srem(String key, String... members) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.srem(key, members);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#从集合里删除一个或多个key", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;
    }

    /**
     * 设置hash里面一个字段的值
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, String field, String value) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.hset(key, field, value);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#从集合里删除一个或多个key", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;

    }

    /**
     * 批量设置hash里面一个字段的值
     * @param key
     * @param hash
     * @return
     */
    public boolean hmset(final String key, final Map<String, String> hash) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    String result = jedis.hmset(key, hash);
                    return result != null && "OK".equals(result);
                }
            }catch(Exception e){
                logger.error("#从集合里删除一个或多个key", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;
    }

    /**
     * 读取哈希域的的值
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null)
                    return jedis.hget(key, field);
            }catch(Exception e){
                logger.error("#从集合里删除一个或多个key", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return null;

    }

    public boolean hdel(String key, String... fields) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.hdel(key, fields);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#删除一个或多个哈希域", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;

    }

    /**
     * 从队列的左边入队一个或多个元素
     * @param key
     * @param strings
     * @return
     */
    public boolean lpush(final String key, final String... strings) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Long result = jedis.lpush(key, strings);
                    return result != null && result > 0;
                }
            }catch(Exception e){
                logger.error("#从队列的左边入队一个或多个元素", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;
    }

    /**
     * 从列表中获取指定返回的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(final String key, final long start,
                               final long end) {

        List<String> result = new ArrayList<String>();

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    result = jedis.lrange(key, start, end);
                    return result;
                }
            }catch(Exception e){
                logger.error("#从列表中获取指定返回的元素", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return result;

    }

    /**
     * 将key对应的数字加decrement。
     * @param key
     * @return
     */
    public Long incr(String key) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    return jedis.incr(key);
                }
            }catch(Exception e){
                logger.error("#将key对应的数字加decrement", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return 0L;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean exists(String key) {

        if(redisSwitch){

            Jedis jedis = null;

            try{
                jedis = this.getJedis();
                if (jedis != null){
                    Boolean result = jedis.exists(key);
                    return result != null && result;
                }
            }catch(Exception e){
                logger.error("#判断key是否存在", e);
            }finally {
                if (jedis != null)
                    JedisUtil.getInstance().releaseJedis(jedis);
            }
        }

        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
