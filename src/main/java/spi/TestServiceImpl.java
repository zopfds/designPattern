package spi;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/15
 */
public class TestServiceImpl implements TestService{
    @Override
    public void println(String test) {
        if(test != null) {
            System.out.println("TestServiceImpl" + test);
        }
    }
}
