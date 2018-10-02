package springioc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 资源url
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/31
 */
public class ResourceUrl implements Resource{

    /**
     * 类库URL
     */
    private final URL url;

    public ResourceUrl(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputstream() throws Exception {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }
}
