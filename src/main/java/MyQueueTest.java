import com.alibaba.fastjson.JSON;
//import com.sun.jmx.remote.internal.ArrayQueue;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 数组模拟实现循环队列
 */
public class MyQueueTest<T> implements Serializable{
//    private T[] data;
//    private int capacity;
//    private int start;
//    private int end;
//
//    public T[] getData() {
//        return data;
//    }
//
//    /**
//     * 构造方法
//     * @param capacity
//     */
//    public MyQueueTest(int capacity) {
//        //这里为什么+1,为了区分队列中是初始状态还是满了的状态,实际队列大小会+1
//        this.capacity = capacity + 1;
//        this.start = 0;
//        this.end = 0;
//        this.data = (T[])new Object[capacity + 1];
//    }
//
//    public void add(T t){
//        int tail = (end + 1) % capacity;
//        if(tail == start){
//            throw new IndexOutOfBoundsException("queue full");
//        }
//        data[end] = t;
//        end = (end + 1) % capacity;
//    }
//
//    public T remove(){
//        if(end == start){
//            throw new IndexOutOfBoundsException();
//        }
//        T result = data[start];
//        data[start] = null;
//        start = (start + 1) % capacity;
//        return result;
//    }
//
//    /**
//     * 扩充大小，该实现在数量级较大的数据扩容时更快
//     *
//     * jdk与本实现区别
//     * 1.jdk逐个复制,寻址时间较长
//     * 2.该实现直接调用system.arraycopy直接操作内存，速度更快
//     *
//     */
//    private void resize(int newCapacity){
//        if(this.data.length == 0){
//            throw new IndexOutOfBoundsException("empty!");
//        }
//        int size = size();
//        if(newCapacity < size){
//            throw new IndexOutOfBoundsException("newCapacity smaller than the last");
//        }
//        T[] newData = (T[])new Object[newCapacity + 1];
//        //判断当前队列是满了还是未满
//        boolean full = (end + 1) % capacity == start;
//        //获取元素真实下标，若end为0，则可能有队列为空，或者队列满两种情况
//        int tail = (end - 1) > 0 ? end - 1 : full ? (end - 1) + capacity : 0;
//        if(tail < start){
//            System.arraycopy(data , start , newData , 0 ,tail - start + 1);
//            if(tail != 0) {
//                System.arraycopy(data, 0, newData, tail - start, tail);
//            }
//        }else if(tail > start){
//            System.arraycopy(data , start , newData , 0 , tail - start + 1);
//        }
//        start = 0;
//        end = size;
//        data = newData;
//    }
//
//    /**
//     * 获取容量大小
//     * @return
//     */
//    private int size(){
//        int diff = end - start;
//        if(diff < 0){
//            return capacity + diff;
//        }
//        return diff;
//    }
//
//    /***
//     *
//     * @param args
//     */
//    public static void main(String[] args) throws InterruptedException {
//        MyQueueTest<Integer> test = new MyQueueTest<>(100000);
//        ArrayQueue<Integer> jdkTest = new ArrayQueue<>(100000);
//
//        for(int i = 0 ; i < 100000 ; i ++){
//            test.add(i);
//            jdkTest.add(i);
//        }
//
//        long startTime = 0;
//        for(int i = 0 ; i < 100000 ; i ++){
//            jdkTest.remove(0);
//            jdkTest.add(100000 + i);
//            long thisStartTime = System.currentTimeMillis();
//            jdkTest.resize(100001 + i);
//            startTime += System.currentTimeMillis() - thisStartTime;
//        }
//
//        System.out.println(JSON.toJSONString(jdkTest));
//
//        long myStartTime = 0;
//        for(int i = 0 ; i < 100000 ; i ++){
//            test.remove();
//            test.add(100000 + i);
//            long thisStartTime = System.currentTimeMillis();
//            test.resize(100001 + i);
//            myStartTime += System.currentTimeMillis() - thisStartTime;
//        }
//
//        System.out.println(JSON.toJSONString(test));
//
//        System.out.println("jdk consume time :" + startTime);
//        System.out.println("my consume time :" + myStartTime);
//    }
//
//    @Override
//    public String toString() {
//        return "MyQueueTest{" +
//                "data=" + Arrays.toString(data) +
//                ", capacity=" + capacity +
//                ", start=" + start +
//                ", end=" + end +
//                '}';
//    }
}
