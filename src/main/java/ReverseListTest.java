import java.util.Comparator;
import java.util.TreeMap;

/**
 * 反转列表的两种实现
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/26
 */
public class ReverseListTest {

    /**
     * 头插法反转链表
     * @param first
     * @return
     */
    public static ListNode insertFromHead(ListNode first){
        ListNode result = first;
        first = first.next;
        result.next = null;
        while(first != null){
            ListNode next = first.next;
            first.next = result;
            result = first;
            first = next;
        }
        return result;
    }

    /**
     * 递归法反转链表
     * @return
     */
    public static ListNode reverse(ListNode first , ListNode next){
        if(next == null){
            return first;
        }

        ListNode nextN = next.next;
        next.next = first;
        return reverse(next , nextN);
    }

    /**
     * 递归反转链表
     * @param first
     * @return
     */
    public static ListNode reverse(ListNode first){
        if(first == null || first.next == null){
            return first;
        }

        ListNode result = reverse(first.next);

        first.next.next = first;
        first.next = null;
        return result;
    }


    public static void printlnList(ListNode first){
        ListNode printNode = first;
        while(printNode != null){
            System.out.print(printNode.value + " -> ");
            printNode = printNode.next;
        }
    }

    public static void main(String[] args){
        ListNode<Integer> firstNode = new ListNode<>(5,null);
        ListNode<Integer> secondNode = new ListNode<>(4,firstNode);
        ListNode<Integer> thirdNode = new ListNode<>(3,secondNode);
        ListNode<Integer> FourthNode = new ListNode<>(2,thirdNode);
        ListNode<Integer> fifthNode = new ListNode<>(1,FourthNode);

//        fifthNode = insertFromHead(fifthNode);
//        printlnList(fifthNode);
        fifthNode = reverse(fifthNode);
        printlnList(fifthNode);

    }


    private static class ListNode<T>{
        T value;
        ListNode next;

        public ListNode(T value, ListNode next) {
            this.value = value;
            this.next = next;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
