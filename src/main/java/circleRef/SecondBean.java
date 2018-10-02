package circleRef;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/9/26
 */
public class SecondBean {

    private FirstBean firstBean;

    public SecondBean(FirstBean firstBean) {
        this.firstBean = firstBean;
    }
}
