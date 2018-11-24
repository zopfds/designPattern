import com.alibaba.fastjson.JSON;
import com.google.common.hash.BloomFilter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.*;

public class StringTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        String test = "test";
//        String testa = test;
//        changeString(test);
//        changeString(testa);
//        System.out.println(test);
//        System.out.println(testa);
//
//        TestObject testObject = new TestObject("test");
//        changeObject(testObject);
//        System.out.println(testObject.getTest());

        HashMap<String, String> map = new HashMap<>();

        map.put(null,null);

        map.put(null,"test");

        System.out.println(JSON.toJSONString(map));

        Hashtable<String,String> hashtable = new Hashtable<>();

        hashtable.put("test" , "test");

        hashtable.put("test" , "abc");

        ExecutorService es = Executors.newFixedThreadPool(3);

        Future<String> future = es.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("create thread by callable!");
                return "result";
            }
        });

        System.out.println("get result from callable : " + future.get());


        System.out.println(JSON.toJSONString(hashtable));
    }

    private static void changeString(String a){
        a = new String("test");
    }

    private static void changeObject(TestObject testObject){
        testObject.setTest("fuck");
    }

    private static final class TestObject{
        private String test;

        public void setTest(String test){
            this.test = test;
        }

        public String getTest() {
            return test;
        }

        public TestObject(String test) {
            this.test = test;
        }
    }

}
