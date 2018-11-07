package lock;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子地更新对象的某个字段，该字段需要volatile修饰
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/7
 */
public class AtomicFieldUpdaterTest {

    private static AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater(FieldObject.class , "age");

    public static void main(String[] args){
        FieldObject fieldObject = new FieldObject("test" , 1);
        int oldValue = updater.getAndAdd(fieldObject,5);
        System.out.println("oldValue = " + oldValue);
        System.out.println("newValue = " + updater.get(fieldObject));
    }

    static class FieldObject{
        private String userName;
        public volatile int age;

        public FieldObject(String userName, int age) {
            this.userName = userName;
            this.age = age;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "FieldObject{" +
                    "userName='" + userName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
