package Iterator;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/8
 */
public interface Iterator {

    /**
     * 是否存在下一个元素
     * @return
     */
    boolean hasNext();

    /**
     * 下一个元素
     * @return
     */
    Object next();

}
