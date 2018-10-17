package jvm.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量池溢出测试
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/16
 */
public class RunTimeConstantPoolOOMTest{
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        int i = 0;
        while(true) {
            list.add(String.valueOf(i++).intern());
        }
    }
}
