package listener;

import java.io.IOException;

public interface Listener {

    /**
     * 监听者做事
     */
    void listenAndDoSth(Event e) throws Exception;
}
