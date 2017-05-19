package com.pc.annotation;

import java.lang.annotation.*;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-03-27 20:32)
 * @version: \$Rev: 1158 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-04-18 15:53:47 +0800 (周二, 18 4月 2017) $
 */
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptResponse {
    String value() default "application/json;charset=UTF-8";
}
