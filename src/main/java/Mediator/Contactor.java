package Mediator;

public abstract class Contactor {

    private String content;

    private String name;

    private Mediator mediator;

    public Contactor(String name){
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getMessage(String message){
        System.out.println("Contactor name = " + this.getName() + " get message = " + message);
    }

    public void sendMessage(){
        this.mediator.contact(this);
    }

    public Contactor(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public Mediator getMediator() {
        return mediator;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public String toString() {
        return "Contactor{" +
                "content='" + content + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
