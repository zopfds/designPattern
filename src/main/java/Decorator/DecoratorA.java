package Decorator;

public class DecoratorA extends Decorator{

    public DecoratorA(Component component) {
        super(component);
    }

    @Override
    public void sampleOperation() {
        System.out.println("I am DecoratorA,do sth before sampleOperation!");
        super.sampleOperation();
    }
}
