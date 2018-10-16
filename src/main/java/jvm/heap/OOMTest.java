package jvm.heap;

import java.util.ArrayList;
import java.util.List;

public class OOMTest {
    public static void main(String[] args){
        List<InnerObject> objects = new ArrayList<>();
        while(true){
            objects.add(new InnerObject());
        }
    }

    private static class InnerObject{

    }
}
