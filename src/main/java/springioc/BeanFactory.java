package springioc;

/**
 * bean工厂
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/27
 */
public interface BeanFactory {

    /**
     * 根据Bean名称获取Bean
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;

    /**
     * 注册beanDefinition
     * @param name
     * @param beanDefinition
     * @throws Exception
     */
    void registerBeanDefinition(String name , BeanDefinition beanDefinition) throws Exception;
}
