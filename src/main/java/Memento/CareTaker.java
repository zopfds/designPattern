package Memento;

public class CareTaker {

    private Memento memento;

    public void saveMemento(Memento memento){
        this.memento = memento;
    }

    public Memento retrieveMemento(){
        return this.memento;
    }

}
