package jvm;

/**
 * 字符串常量intern
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/15
 */
public class JvmTest {
    public static void main(String[] args){
        String s1 = new String("1");
        String s2 = "1";
        String s3 = s1.intern();
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);

        String s4 = new String("1") + new String("1");
        String s5 = "11";
        String s6 = s4.intern();

        System.out.println(s4 == s5);
        System.out.println(s5 == s6);
    }
}
