package flyweight;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class FlyweightClient {

    public static void main(String[] args){
        SecurityController securityController = SecurityController.getInstance();

        securityController.login("张三");
        boolean f1 = securityController.checkPermission("张三","其他","查看");
        boolean f2 = securityController.checkPermission("张三","其他","修改");

        System.out.println("f1 = " + f1);
        System.out.println("f2 = " + f2);
    }
}
