package springaop.InterceptorTest;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/3
 */
public class MethodBeforeInterceptor implements MethodInterceptor{
    private String name;

    public MethodBeforeInterceptor(String name) {
        this.name = name;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {
        System.out.println("MethodBeforeInterceptor name = " + name);
        Object result = invocation.proceed();
        return result;
    }
}
