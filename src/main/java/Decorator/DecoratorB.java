package Decorator;

public class DecoratorB extends Decorator{
    public DecoratorB(Component component) {
        super(component);
    }

    @Override
    public void sampleOperation() {
        System.out.println("I am DecoratorB,do sth before sampleOperation!");
        super.sampleOperation();
    }
}
