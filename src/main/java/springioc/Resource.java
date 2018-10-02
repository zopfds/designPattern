package springioc;

import java.io.InputStream;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/31
 */
public interface Resource {

    /**
     * 获取输入流
     */
    InputStream getInputstream() throws Exception;
}
