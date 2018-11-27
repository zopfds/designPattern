/**
 * 尾递归实现斐波那契数列
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/27
 */
public class FibonaciTest {

    /**
     * 常规简单递归，对于某些count - 2以下的数字会重复递归计算
     * @param count
     * @return
     */
    public int fibonaci(int count){
        if(count <= 2){
            return 1;
        }else{
            return fibonaci(count - 1) + fibonaci(count - 2);
        }
    }

    /**
     * 尾递归实现斐波那契数列
     * @param count
     * @param a
     * @param b
     * @return
     */
    public int fibonaci2(int count , int a , int b){
        if(count <= 2){
            return b;
        }

        return fibonaci2(count - 1 , b , a + b);
    }

    public static void main(String[] args){
        FibonaciTest test = new FibonaciTest();

        for(int i = 0 ;i < 30 ; i ++) {
            System.out.println(test.fibonaci(i));
            System.out.println(test.fibonaci2(i, 1, 1));
        }
    }
}
