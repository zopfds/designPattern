package circleRef;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring 框架 构造器注入循环引用 会报错
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/9/26
 */
public class Circletest {

    public static void main(String[] args){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:springCircleRef.xml");

        FirstBean firstBean = (FirstBean) classPathXmlApplicationContext.getBean("firstBean");

        System.out.println("className = " + firstBean.getClass().getName());
    }
}
