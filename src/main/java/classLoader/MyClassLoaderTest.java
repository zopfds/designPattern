package classLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoaderTest {
    public static void main(String[] args){
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class<?> helloClass = myClassLoader.loadClass("C:\\Users\\74077\\Desktop\\MyHelloTest");

            System.out.println(helloClass.getClassLoader());
            Object hello = helloClass.getDeclaredConstructor().newInstance();
            System.out.println(hello);
            Method method = helloClass.getMethod("welcome");
            System.out.println("welcome method result = " + method.invoke(hello));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
