package Heap;

import java.util.Arrays;
import java.util.Comparator;

public class Heap<E> {

    private E[] elementData;

    private int size;

    private Comparator<? super E> comparator;

    public Heap(E[] elementData, int size, Comparator<? super E> comparator) {
        this.elementData = elementData;
        this.size = size;
        this.comparator = comparator;
    }

    public void siftDown(int k){
        int half = size >>> 1;
        while(k < half) {
            int min = k;
            if (comparator.compare(elementData[(k << 1) + 1], elementData[k]) < 0) {
                min = (k << 1) + 1;
            }

            if ((k << 1) + 2 < size && comparator.compare(elementData[(k << 1) + 2], elementData[min]) < 0) {
                min = (k << 1) + 2;
            }

            if(min == k){
                break;
            }

            E tmp = elementData[min];
            elementData[min] = elementData[k];
            elementData[k] = tmp;
            k = min;
        }

    }

    public void prioritySiftDown(int k , E e){
        int half = size >>> 1;
        while(k < half){
            int child = (k << 1) + 1;
            E childE = elementData[child];
            if(child + 1 < size && comparator.compare(elementData[child + 1] , childE) < 0){
                child = child + 1;
                childE = elementData[child + 1];
            }

            if(comparator.compare(e,childE) <= 0){
                break;
            }

            elementData[k] = childE;
            k = child;
        }
        elementData[k] = e;
    }

    public void siftUp(int k){
        E e = elementData[k];
        while(k > 0){
            int parent = k >>> 1;
            if(comparator.compare(elementData[parent],e) < 0){
                break;
            }

            elementData[k] = elementData[parent];
            k = parent;
        }
        elementData[k] = e;
    }

    public static void main(String[] args){
        Heap heap = new Heap<Integer>(new Integer[]{5, 4, 3, 2, 1}, 5, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        heap.prioritySiftDown(2 , 2);
        heap.siftDown(1);
        heap.siftDown(0);
        Arrays.stream(heap.elementData).forEach(i -> System.out.println(i));
    }
}
