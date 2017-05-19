package com.pc.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2015/10/16.
 */
@Target({ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanBind {
    //value应为PO的类名
    String value();
}
