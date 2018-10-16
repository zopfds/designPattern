package thread;

class Target
{
    private int count;

    public synchronized void increase()
    {
        System.out.println(Thread.currentThread().getName() + "--be called");
        if(count == 2)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        count++;
        System.out.println(Thread.currentThread().getName() + ":" + count);
        notify();
    }

    public synchronized void decrease()
    {
        System.out.println(Thread.currentThread().getName() + "--be called");
        if(count == 0)
        {
            try
            {
                //等待，由于Decrease线程调用的该方法,
                //所以Decrease线程进入对象t(main函数中实例化的)的等待池，并且释放对象t的锁
                wait();//Object类的方法
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        count--;
        System.out.println(Thread.currentThread().getName() + ":" + count);

        //唤醒线程Increase，Increase线程从等待池到锁池
        notify();
    }
}
class Increase extends Thread
{
    private Target t;

    public Increase(Target t)
    {
        this.t = t;
    }
    @Override
    public void run()
    {
        for(int i = 0 ;i < 30; i++)
        {
            try
            {
                Thread.sleep((long)(Math.random()*500));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            t.increase();
        }

    }

}
class Decrease extends Thread
{

    private Target t;
    public Decrease(Target t)
    {
        this.t = t;
    }

    @Override
    public void run()
    {
        for(int i = 0 ; i < 30 ; i++)
        {
            try
            {
                //随机睡眠0~500毫秒
                //sleep方法的调用，不会释放对象t的锁
                Thread.sleep((long)(Math.random()*500));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            t.decrease();

        }

    }

}

public class ThreadWaitTest
{
    public static void main(String[] args)
    {
        Target t = new Target();

        Thread t1 = new Increase(t);
        t1.setName("Increase");
        Thread t2 = new Decrease(t);
        t2.setName("Decrease");

        t1.start();
        t2.start();
    }
}
