package com.pc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2014-12-27 14:21)
 * @version: \$Rev: 2351 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-05-16 18:07:26 +0800 (周二, 16 5月 2017) $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface OperationLog {
    String[] value() default {};        //操作内容
    String[] table() default {};        //操作库表
}
