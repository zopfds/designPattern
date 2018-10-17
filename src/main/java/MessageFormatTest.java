import java.text.MessageFormat;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/9
 */
public class MessageFormatTest {

    public static void main(String[] args){
        String testString  = "$name";

        String replaceString = "\\$name";

        System.out.println(testString.replaceAll(replaceString,"test"));
    }
}
