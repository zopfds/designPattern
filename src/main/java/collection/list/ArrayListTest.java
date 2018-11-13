package collection.list;

import java.util.ArrayList;
import java.util.List;

/**
 * arraylist测试类，测试
 *
 * add,
 *
 * grow,
 *
 * remove,
 *
 * get,
 *
 * size
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/8
 */
public class ArrayListTest {

    public static void main(String[] args){
        List<Integer> test = new ArrayList<>();

        for(int i = 0 ; i < 10 ; i ++){
            test.add(i);
        }

        test.add(10);

        test.get(3);

        test.size();

        test.remove(Integer.valueOf(1));
    }
}
