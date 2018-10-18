package jvm.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆上分配对象导致堆内存溢出
 */
public class HeapOOMTest{
    public static void main(String[] args){
        List<InnerObject> objects = new ArrayList<>();
        while(true){
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            objects.add(new InnerObject());
        }
    }

    private static class InnerObject{

    }
}
