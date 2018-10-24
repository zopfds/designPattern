package jvm.reference;

/**
 * 强引用自救
 *
 * 当对象从GCRoot不可通过引用链到达的时候，执行GC不不会马上回收该对象，而会将其打上标记放入QUEUE队列，
 * 由一个低优先级的后台线程去执行该对象的FINALIZE方法，当该对象执行过FINALIZE方法或在执行FINALIZE期间再次出现GC，该对象会被直接收回
 * 相反如果该对象在此期间又重新被引用则不会被回收
 *
 * FINALIZE方法已经不推荐使用
 */
public class StrongReferenceRescue {
    private static StrongReferenceRescue HOOK = null;

    public void isAlive(){
        System.out.println("I am fucking alive!");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finialize method called!");
        HOOK = this;
    }

    public static void main(String[] args) throws Throwable{
        HOOK = new StrongReferenceRescue();

        //对象第一次尝试拯救自己
        HOOK = null;
        System.gc();

        //因后台执行FINALIZE线程处于低优先级，所以线程睡眠500毫秒，让其有机会执行对象的FINALIZE方法
        Thread.sleep(500);
        if(HOOK != null){
            HOOK.isAlive();
        }else{
            System.out.println("no,I am dead!");
        }

        //对象第二次尝试拯救自己
        HOOK = null;
        System.gc();

        //因后台执行FINALIZE线程处于低优先级，所以线程睡眠500毫秒，让其有机会执行对象的FINALIZE方法
        Thread.sleep(500);
        //因该对象已经执行过FINALIZE方法，所以不会再执行，于是这次被回收
        if(HOOK != null){
            HOOK.isAlive();
        }else{
            System.out.println("no,I am dead!");
        }

    }
}
