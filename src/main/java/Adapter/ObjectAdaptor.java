package Adapter;

public class ObjectAdaptor implements Target{

    private ObjectAdaptee objectAdaptee;

    public ObjectAdaptor(ObjectAdaptee objectAdaptee){
        this.objectAdaptee = objectAdaptee;
    }

    @Override
    public void request() {
        System.out.println("I am ObjectAdaptor,I can make objectAdaptee work!");
        objectAdaptee.specificRequest();
    }
}
