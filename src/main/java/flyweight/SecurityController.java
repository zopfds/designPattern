package flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户权限的控制服务，使用享元工厂，实现面向业务的功能
 *
 * 比如本例中的获取用户权限
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class SecurityController {

    private static SecurityController securityController = new SecurityController();

    private SecurityController(){
    }

    public static SecurityController getInstance(){
        return securityController;
    }

    /**
     * 用户与权限集合的对应关系map
     */
    private Map<String , List<Flyweight>> map = new HashMap<>(16);

    /**
     * 登录方法，将用户权限放到map中缓存
     * @param user
     * @return
     */
    public void login(String user){
        List<Flyweight> list = getByUser(user);
        map.put(user , list);
    }

    /**
     * 检查某个用户有没有该项权限
     * @param user
     * @param operation
     * @param permit
     * @return
     */
    public boolean checkPermission(String user , String operation , String permit){
        List<Flyweight> permissionList = map.get(user);
        if(permissionList == null || permissionList.size() == 0){
            System.out.println("该用户没有权限! user = " + user + " , operation = " + operation + " , permit = " + permit);
        }

        for(Flyweight f : permissionList) {
            System.out.println("permission = " + f);
            if(f.match(operation , permit)){
                return true;
            }
        }

        return false;
    }

    /**
     * 根据用户获取用户的享元对象集合
     * @param user
     * @return
     */
    private List<Flyweight> getByUser(String user){
        List<Flyweight> result = new ArrayList<>();
        TestDB.colDB.stream().forEach(s -> {
            String[] ss = s.split(",");
            if(ss[0].equals(user)){
                //获取某项权限的享元对象
                Flyweight flyweight = FlyweightFactory.getInstance().getFlyweight(String.join("," , ss[1] , ss[2]));
                result.add(flyweight);
            }
        });
        return result;
    }
}
