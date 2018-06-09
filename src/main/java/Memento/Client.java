package Memento;

public class Client {
    public static void main(String[] args){
        Originator originator = new Originator();
        originator.setState("born");
        CareTaker careTaker = new CareTaker();
        careTaker.saveMemento(originator.createMemento());

        originator.setState("grow");
        originator.restoreMemento(careTaker.retrieveMemento());

        System.out.println("state = " + originator.getState());
    }
}
