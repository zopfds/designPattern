package springioc;

import java.net.URL;

/**
 * 资源读取
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/31
 */
public class ResourceLoader {

    public ResourceUrl getResource(String location){
        URL url = this.getClass().getClassLoader().getResource(location);
        return new ResourceUrl(url);
    }
}
