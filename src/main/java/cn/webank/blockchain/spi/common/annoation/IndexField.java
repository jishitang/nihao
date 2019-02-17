package cn.webank.blockchain.spi.common.annoation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface IndexField {

    String name() default "";

    String nullAs() default "";

    /**
     * start from 0
     */
    int index();
}
