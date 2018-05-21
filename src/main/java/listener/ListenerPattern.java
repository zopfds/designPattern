package listener;

public class ListenerPattern {

    public static void main(String[] args){
        EventSource es = new EventSource();

        Event e1 = new ActualEvent1("event1");
        Event e2 = new ActualEvent2("event2");

        ActualListener1 ae1 = new ActualListener1();
        ActualListener2 ae2 = new ActualListener2();

        es.registerListener(ae1);
        es.registerListener(ae2);

        es.notifyListenerEvent(e1);
        es.notifyListenerEvent(e2);

    }

}