package spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/10/15
 */
public class SpiTest {
    public static void main(String[] args){

        ServiceLoader<TestService> d = ServiceLoader.load(TestService.class);
        Iterator<TestService> iterator = d.iterator();
        while(iterator.hasNext()){
            TestService testService = iterator.next();
            testService.println("test");
        }
    }
}
