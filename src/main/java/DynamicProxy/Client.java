package DynamicProxy;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/6
 */
public class Client {

    public static void main(String[] args){

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles" , true);

        ProxyManager proxyManager = new ProxyManager();

        Target target = (Target) proxyManager.newProxyInstance(new TargetImpl());

        target.doSomething();
    }
}
