package circleRef;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/9/26
 */
public class FirstBean {

    private SecondBean secondBean;

    public FirstBean(SecondBean secondBean) {
        this.secondBean = secondBean;
    }
}
