package com.pc.annotation;

import com.pc.core.Page;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2015/10/30.
 */
@Target({ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapBind {
    String value() default "page.params";
    Class<?> clazz() default Page.class;
}
