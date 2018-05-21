package listener;

import java.util.Vector;

public class EventSource {

    private Vector<Listener> listeners = new Vector<>();

    public void registerListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    /**
     * 接受外部事件
     */
    public void notifyListenerEvent(Event event){
        listeners.forEach(listener -> {
            try {
                listener.listenAndDoSth(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
