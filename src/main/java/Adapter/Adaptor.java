package Adapter;

public class Adaptor extends Adaptee implements Target{
    @Override
    public void request() {
        System.out.println("I am Adaptor , I can make Adaptee work!");
        super.specificRequest();
    }
}
