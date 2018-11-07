import java.text.MessageFormat;
import java.util.Arrays;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/9
 */
public class MessageFormatTest {

    public static void main(String[] args){
//        String testString  = "$name";
//
//        String replaceString = "\\$name";
//
//        System.out.println(testString.replaceAll(replaceString,"test"));

        String[] test = new String[10];
        changeArray(test);
        Arrays.stream(test).forEach(i -> System.out.println(i));

    }

    public static void changeArray(String[] test){
        test[1] = "aaa";
    }
}
