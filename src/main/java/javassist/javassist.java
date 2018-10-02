package javassist;

/**
 * Javassist是一个开源的分析、编辑和创建Java字节码的类库。
 * 它已加入了开放源代码JBoss 应用服务器项目，通过使用Javassist对字节码操作为JBoss实现动态"AOP"框架。
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/9/26
 */
public class javassist {

    public static void main(String[] args){
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get("javassist.javassistTest");

            CtMethod ctMethod = ctClass.getDeclaredMethod("print");

            ctMethod.setBody("System.out.println(\"this method is changed dynamically!\");");

            ctClass.toClass();

            javassistTest javassistTest = new javassistTest();

            javassistTest.print();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

class javassistTest{
    public void print(){
        System.out.println("before replace!");
    }
}
