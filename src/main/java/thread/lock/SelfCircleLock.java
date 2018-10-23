package thread.lock;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/17
 */
public class SelfCircleLock {

    private int maxCircleTime;

    private static ThreadLocal<Integer> circleTime = new ThreadLocal<>();

    public void selfCircleLock(){
        int time = circleTime.get();
        while(time < maxCircleTime){
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            circleTime.set(++time);
        }
    }

    public static void main(String[] args){

    }
}
