package Decorator;

public class ConcreteComponent implements Component{
    @Override
    public void sampleOperation() {
        System.out.println("I am concreteComponent!");
    }
}
