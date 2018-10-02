package DynamicProxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

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

        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/jianbopan/Desktop");


        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CgTarget.class);
        enhancer.setCallback(new CgTargetInterceptor());
        CgTarget cgTarget = (CgTarget)enhancer.create();
        cgTarget.doSomething();
    }


}
