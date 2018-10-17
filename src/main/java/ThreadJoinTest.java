/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/11
 */
public class ThreadJoinTest {

    public static void main(String[] args){
        JoinThread thread1 = new JoinThread("joinThread1" , 90);
        JoinThread thread2 = new JoinThread("joinThread2" , 100);

        thread1.start();

        try{
            thread1.join();
            System.out.println(thread1.getName() + " : " + thread1.getState());

            Thread.currentThread().sleep(20 * 1000L);

            System.out.println(Thread.currentThread().getName() + " : " + Thread.currentThread().getState());
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("");

    }

    private static class JoinThread extends Thread{

        private String name;

        private int count;

        public JoinThread(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public void run() {
            System.out.println("JoinThread start! name = " + name);
            try{
                Thread.currentThread().sleep(10 * 1000L);
                for(int i = 0 ; i < count ; i ++){

                }
                System.out.println(this.getName() + " : " + this.getState());
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("JoinThread end! name = " + name);
        }
    }
}
