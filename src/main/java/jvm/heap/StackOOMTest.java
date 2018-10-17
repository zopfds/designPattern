package jvm.heap;

/**
 * 虚拟机栈和本地方法栈溢出
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/16
 */
public class StackOOMTest {

    private int stackLength = 1;

    public void stackLeak(){
        stackLength++;
        stackLeak();
    }

    public void dontStop(){
        while(true){

        }
    }

    public void stackLeakByThread(){
        while(true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            }).start();
        }
    }

    public static void main(String[] args){
        StackOOMTest stackOOMTest = new StackOOMTest();
//        stackOOMTest.stackLeak();
        stackOOMTest.stackLeakByThread();
    }
}
