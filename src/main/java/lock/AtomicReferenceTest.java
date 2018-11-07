package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子更新某个对象的引用
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/7
 */
public class AtomicReferenceTest {

    private static AtomicReference<ReferenceTest> reference = new AtomicReference<>();

    static class ReferenceTest{
        private String name;

        public ReferenceTest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ReferenceTest{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args){
        ReferenceTest referenceTest = new ReferenceTest("test");
        ReferenceTest fuckTest = new ReferenceTest("fuck");

        reference.set(referenceTest);

        ReferenceTest old = reference.getAndSet(fuckTest);

        System.out.println("old reference : " + old.toString());

        ReferenceTest newRefer  = reference.get();

        System.out.println("new reference : " + newRefer.toString());
    }
}
