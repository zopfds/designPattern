package Decorator;

public class Client {

    public static void main(String[] args){
        Component beDecorateComponent = new ConcreteComponent();

        Component decoratorComponent = new DecoratorA(beDecorateComponent);
        decoratorComponent.sampleOperation();

        decoratorComponent = new DecoratorB(beDecorateComponent);
        decoratorComponent.sampleOperation();
    }
}
