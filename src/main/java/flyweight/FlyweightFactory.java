package flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂，用于缓存享元（用hashmap缓存），该实例通常为单例
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class FlyweightFactory {

    private static FlyweightFactory factory = new FlyweightFactory();

    private FlyweightFactory() {
    }

    public static FlyweightFactory getInstance(){
        return factory;
    }

    private Map<String,Flyweight> fsMap = new HashMap<>(16);

    /**
     * 获取享元
     * @param key
     * @return
     */
    public Flyweight getFlyweight(String key){
        Flyweight flyweight = fsMap.get(key);
        if(flyweight == null){
            flyweight = new AuthorizationFlyweight(key);
            fsMap.put(key , flyweight);
        }
        return flyweight;
    }
}
