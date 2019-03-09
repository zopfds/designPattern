package thread.circlePrint;

/**
 * 两个线程循环交替打印1-100，无锁实现方案，依赖于java happens-before规则
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/12
 */
public class CirclePrint {
    private static int num;
    private static volatile boolean flag;
    private static int maxNum;

    private static class SecondThread implements Runnable{

        @Override
        public void run() {
            while(true){
                if(flag && num <= maxNum) {
                    System.out.println(num++);
                    flag = false;
                }else{
                    Thread.yield();
                }
            }
        }
    }

    private static class FirstThread implements Runnable{

        @Override
        public void run() {
            while(true){
                if(!flag && num <= maxNum) {
                    System.out.println(num++);
                    flag = true;
                }else{
                    Thread.yield();
                }
            }
        }
    }


    public static void main(String[] args){
        CirclePrint circlePrint = new CirclePrint();
        circlePrint.num = 1;
        circlePrint.flag = true;
        circlePrint.maxNum = 100;

        Thread firstThread = new Thread(new FirstThread());
        Thread secondThread = new Thread(new SecondThread());
        firstThread.setDaemon(false);
        secondThread.setDaemon(false);
        firstThread.start();
        secondThread.start();
    }
}
