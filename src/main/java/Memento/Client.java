package Memento;

public class Client {
    public static void main(String[] args){
        Originator originator = new Originator();
        originator.setState("born");
        Memento bornMemento = originator.createMemento();
        CareTaker careTaker = new CareTaker();
        careTaker.saveMemento(bornMemento);

        originator.setState("grow");
        Memento growMemento = originator.createMemento();

        originator.restoreMemento(careTaker.retrieveMemento());

        System.out.println("state = " + originator.getState());
    }
}
