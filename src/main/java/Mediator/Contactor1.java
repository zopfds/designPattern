package Mediator;

public class Contactor1 extends Contactor{


    public Contactor1(String name) {
        super(name);
    }

    public Contactor1(String name, Mediator mediator) {
        super(name, mediator);
    }
}
