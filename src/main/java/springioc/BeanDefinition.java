package springioc;

/**
 * bean的定义数据接口类型
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/27
 */
public class BeanDefinition {

    /**
     * bean对象
     */
    private Object bean;

    /**
     * 类名称
     */
    private String className;

    /**
     * bean类的全限定名
     */
    private Class beanClass;

    /**
     * 类的属性合集
     */
    private PropertyValues propertyValues;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {

        try{
            this.beanClass = Class.forName(className);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        this.className = className;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
