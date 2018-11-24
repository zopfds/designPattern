import com.sun.jmx.remote.internal.ArrayQueue;

/**
 * 数组模拟实现循环队列
 */
public class MyQueueTest<T>{
    private T[] data;
    private int capacity;
    private volatile int start;
    private volatile int end;

    /**
     * 构造方法
     * @param capacity
     */
    public MyQueueTest(int capacity) {
        //这里为什么+1,为了区分队列中是初始状态还是满了的状态,实际队列大小会+1
        this.capacity = capacity + 1;
        this.start = 0;
        this.end = 0;
        this.data = (T[])new Object[capacity + 1];
    }

    public void add(T t){
        int tail = (end + 1) % capacity;
        if(tail == start){
            throw new IndexOutOfBoundsException("queue full");
        }
        data[end] = t;
        end = (end + 1) % capacity;
    }

    public T remove(){
        if(end == start){
            throw new IndexOutOfBoundsException();
        }
        T result = data[start];
        data[start] = null;
        start = (start + 1) % capacity;
        return result;
    }

    /**
     * 扩充大小
     * 与jdk实现的区别，jdk逐个复制
     */
    private void resize(int newCapacity){
        int size = size();
        if(newCapacity < size){
            throw new IndexOutOfBoundsException("newCapacity smaller than the last");
        }
        T[] newData = (T[])new Object[newCapacity + 1];
        //判断当前队列是满了还是未满
        boolean full = (end + 1) % capacity == start;
        //获取元素真实下标，若end为0，则可能有队列为空，或者队列满两种情况
        int tail = (end - 1) > 0 ? end - 1 : full ? (end - 1) + capacity : 0;
        if(tail < start){
            System.arraycopy(data , start , newData , 0 ,tail - start + 1);
            if(tail != 0) {
                System.arraycopy(data, 0, newData, tail - start, tail);
            }
        }else if(tail > start){
            System.arraycopy(data , start , newData , 0 , tail - start + 1);
        }
        start = 0;
        end = size;
        data = newData;
    }

    /**
     * 获取容量大小
     * @return
     */
    private int size(){
        int diff = end - start;
        if(diff < 0){
            return capacity + diff;
        }
        return diff;
    }

    /***
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        MyQueueTest<Integer> test = new MyQueueTest<>(1000);
        ArrayQueue<Integer> jdkTest = new ArrayQueue<>(1000);

        for(int i = 0 ; i < 1000 ; i ++){
            test.add(i);
            jdkTest.add(i);
        }

        long startTime = 0;
        for(int i = 0 ; i < 100 ; i ++){
            jdkTest.remove();
            jdkTest.add(1000 + i);
            long thisStartTime = System.currentTimeMillis();
            jdkTest.resize(1001 + i);
            startTime += System.currentTimeMillis() - thisStartTime;
        }

        long myStartTime = 0;
        for(int i = 0 ; i < 100 ; i ++){
            test.remove();
            test.add(1000 + i);
            long thisStartTime = System.currentTimeMillis();
            test.resize(1001 + i);
            myStartTime += System.currentTimeMillis() - thisStartTime;
        }

        System.out.println("jdk consume time :" + startTime);
        System.out.println("my consume time :" + startTime);
    }
}
