package DynamicProxy;

/**
 * 被代理类的实际操作
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/6
 */
public class TargetImpl implements Target{
    @Override
    public void doSomething() {
        System.out.println("I am targetImpl , I can do something!");
    }
}
