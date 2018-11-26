package annotation;

import java.lang.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestComponent
public @interface SubComponent{
    String value() default "subTest";
}
