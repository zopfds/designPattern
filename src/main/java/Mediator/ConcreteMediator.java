package Mediator;

import java.util.ArrayList;
import java.util.List;

public class ConcreteMediator extends Mediator{

    private List<Contactor> targetContactor = new ArrayList<>();

    public List<Contactor> getTargetContactor() {
        return targetContactor;
    }

    public void addContactor(Contactor targetContactor) {
        this.targetContactor.add(targetContactor);
    }

    @Override
    public void contact(Contactor sourceContactor) {
        System.out.println("Mediator send message from " + sourceContactor.getName() + " to " + targetContactor.get(0).getName());
        targetContactor.get(0).getMessage(sourceContactor.getContent());
    }
}
