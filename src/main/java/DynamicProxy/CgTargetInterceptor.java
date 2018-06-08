package DynamicProxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/7
 */
public class CgTargetInterceptor implements MethodInterceptor{
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("CgTargetInterceptor,before Interceptor! " + method.getName());
        Object object = methodProxy.invokeSuper(o , objects);
        System.out.println("CgTargetInterceptor,after Interceptor! " + method.getName());
        return object;
    }
}
