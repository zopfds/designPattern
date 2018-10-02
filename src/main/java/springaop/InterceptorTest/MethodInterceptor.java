package springaop.InterceptorTest;

import org.aopalliance.intercept.*;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/3
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation invocation);
}
