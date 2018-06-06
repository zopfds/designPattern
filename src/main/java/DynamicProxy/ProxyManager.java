package DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk默认动态代理的实现
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/6
 */
public class ProxyManager implements InvocationHandler{

    private Target target;

    public Object newProxyInstance(Target target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader() , target.getClass().getInterfaces() , this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;

        System.out.println("method = " + method.getName() + ", before method invoke!");

        try{
            ret = method.invoke(target , args);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("error -- >> " + method.getName());
            throw e;
        }

        return ret;
    }
}
