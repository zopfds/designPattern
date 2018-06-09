package Mediator;

public class Client {
    public static void main(String[] args){
        Contactor contactor1 = new Contactor1("Contactor1");
        Contactor contactor2 = new Contactor2("Contactor2");

        ConcreteMediator mediator = new ConcreteMediator();

        contactor1.setContent("fuck you!");
        contactor2.setContent("fuck me?");

        mediator.addContactor(contactor2);
        contactor1.setMediator(mediator);
        contactor1.sendMessage();
    }
}
