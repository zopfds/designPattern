package Adapter;

public class Client {

    public static void main(String []args){
        //类适配器模式
        Target target = new Adaptor();
        target.request();

        //对象适配器模式
        Target target1 = new ObjectAdaptor(new ObjectAdaptee());
        target1.request();
    }
}
