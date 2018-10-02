package springaop.InterceptorTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/3
 */
public class MethodInvocationTest implements MethodInvocation{

    private static List<MethodInterceptor> interceptorList = new ArrayList<>();

    private static volatile int count = 0;

    private Class targetClass;

    private String methodName;

    private Object[] args;

    public MethodInvocationTest(Class targetClass, String methodName, Object[] args) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.args = args;
    }

    static{
        interceptorList.add(new MethodBeforeInterceptor("test1"));
        interceptorList.add(new AfterReturningInterceptor("test1"));
        interceptorList.add(new AfterReturningInterceptor("test2"));
        interceptorList.add(new MethodBeforeInterceptor("test2"));
    }

    @Override
    public Object proceed() {
        if(count == interceptorList.size()){
            try {
                return targetClass.getMethod(methodName).invoke(targetClass.newInstance() , args);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        return interceptorList.get(count++).invoke(this);
    }

    public static void main(String[] args){
        MethodInvocationTest methodInvocationTest = new MethodInvocationTest(InvokeClassTest.class,"speak" , new Object[0]);
        methodInvocationTest.proceed();
    }

    static class InvokeClassTest{
        public void speak(){
            System.out.println("I am a fucker!");
        }
    }
}
