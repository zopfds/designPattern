package annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/23
 */
@SubComponent(value = "test")
public class GetAnnotationType {
    public static void main(String[] args){
        Class testClass = GetAnnotationType.class;
        Annotation[] annotations = testClass.getAnnotations();

        Arrays.stream(annotations).forEach(annotation -> {
            System.out.println(annotation.toString());
            System.out.println(annotation.annotationType());
            System.out.println(annotation.getClass().getName());
            System.out.println(annotation.getClass().getTypeName());
            System.out.println(annotation.getClass().toString());
        });

//        TestComponent testComponent = (TestComponent) testClass.getAnnotation(TestComponent.class);
//
//        System.out.println(testComponent.value());

        SubComponent subComponent = (SubComponent) testClass.getAnnotation(SubComponent.class);

        System.out.println(subComponent.value());
    }
}
