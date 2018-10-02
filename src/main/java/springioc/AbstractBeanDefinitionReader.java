package springioc;

import java.util.Map;

/**
 * 抽象的bean读取类
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/30
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    /**
     * bean注册map
     */
    private Map<String,BeanDefinition> registry;

    /**
     * 资源读取器
     */
    private ResourceLoader resourceLoader;

    public void setRegistry(Map<String, BeanDefinition> registry) {
        this.registry = registry;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected AbstractBeanDefinitionReader(Map<String, BeanDefinition> registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }
}
