package flyweight;

/**
 * 享元接口,通过这个接口可用于接受外部状态
 *（当系统中存在大量小粒度重复对象的时候，可利用享元模式减少重复对象的占用内存）
 *
 * 例子，用户权限控制实体，张三对菜单拦有编辑权限
 * 对菜单栏有编辑权限属于内部状态，通常不可变，
 * 张三属于外部状态一般可变
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public interface Flyweight {

    /**
     * 判断对象的内部状态是否匹配
     * @param
     */
    boolean match(String operation , String permit);

}
