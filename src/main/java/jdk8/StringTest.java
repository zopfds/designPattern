package jdk8;

import java.util.ArrayList;
import java.util.List;

public class StringTest {
    public static void main(String[] args){
        String test = String.join(",","test" , "abc","hah");
        System.out.println(test);
        List<String> testList = new ArrayList<>();
        testList.add("test");
        testList.add("fuck");
        System.out.println(String.join(",",testList));
    }
}
