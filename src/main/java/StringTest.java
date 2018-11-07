public class StringTest {

    public static void main(String[] args){
        String test = "test";
        String testa = test;
        changeString(test);
        changeString(testa);
        System.out.println(test);
        System.out.println(testa);

        TestObject testObject = new TestObject("test");
        changeObject(testObject);
        System.out.println(testObject.getTest());
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
