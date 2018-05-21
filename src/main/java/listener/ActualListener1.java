package listener;

public class ActualListener1 implements Listener{

    @Override
    public void listenAndDoSth(Event e) {
       System.out.println("ActualListener1 say " + e.getWorld() + "!");
    }
}
