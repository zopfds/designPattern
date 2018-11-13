package jvm;

/**
 * 测试局部变量表对gc的影响
 *
 *
 * 如果没有下列int a = 0赋值语句
 * gc日志如下:
 *
 * [0.028s][info][gc] Using G1
 * [0.216s][info][gc] GC(0) Pause Full (System.gc()) 71M->67M(226M) 6.862ms
 *
 * 虽然到system.gc语句已经超出作用域,但因为局部变量表任然有指向该对象placeHolder的引用,
 * 所以其任然属于gcRoots能遍历到的对象,所以不能回收.
 *
 * 当有int a = 0赋值语句
 * gc日志如下:
 *
 * [0.026s][info][gc] Using G1
 * [0.228s][info][gc] GC(0) Pause Full (System.gc()) 71M->2M(9M) 12.380ms
 *
 * 因为局部变量表的slot是复用的,其slot数量的最大值在编译器已经确定,而slot是复用的,所以当赋值
 * 以后局部变量表就不存在对数组的引用了,因此能gc回收
 *
 */
public class LocalVariableTest {

    public static void main(String[] args){
        {
            byte[] placeHolder = new byte[64 * 1024 * 1024];
        }
        int a = 0;
        System.gc();
    }
}
