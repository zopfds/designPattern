package springaop.InterceptorTest;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/3
 */
public class AfterReturningInterceptor implements MethodInterceptor{

    private String name;

    public AfterReturningInterceptor(String name) {
        this.name = name;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {
        Object result = invocation.proceed();
        System.out.println("AfterReturningInterceptor name = " + name);
        return result;
    }
}
