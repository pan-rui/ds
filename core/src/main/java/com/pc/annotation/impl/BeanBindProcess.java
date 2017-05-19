package com.pc.annotation.impl;

import com.pc.annotation.BeanBind;
import com.pc.core.Constants;
import com.pc.util.DateUtil;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/10/16.
 */
public class BeanBindProcess implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(BeanBind.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        BeanBind beanBind = methodParameter.getParameterAnnotation(BeanBind.class);
        HashMap<String,String> pathVar= (HashMap<String, String>) ((ServletWebRequest) nativeWebRequest).getRequest().getAttribute("org.springframework.web.servlet.View.pathVariables");
        String poName = beanBind.value().equalsIgnoreCase("bean")?pathVar!=null?Constants.getClassName(pathVar.get("tableName")):Constants.getClassName(nativeWebRequest.getParameter("tableName")):beanBind.value();
        Object obj = Class.forName("com.pc.pojo." + StringUtils.capitalize(poName)).newInstance();
        Iterator<String> itNames = nativeWebRequest.getParameterNames();
        while (itNames.hasNext()) {
            String name = itNames.next();
            if (name.startsWith(beanBind.value() + ".")) {
                String fieldName = name.substring(name.indexOf(".") + 1);
                Object value = nativeWebRequest.getParameter(name);
                Constructor fieldConstructor = Constants.ReflectUtil.getField(obj, fieldName).getType().getConstructor(String.class);
                if(!StringUtils.isEmpty(value))
                if (value.toString().matches("^\\d{4}[\\-/]\\d{1,2}[\\-/]\\d{1,2}.*"))
                    value = DateUtil.convertStringToDate(value.toString(), null);
                else
                    value = fieldConstructor.newInstance(value);
                Constants.ReflectUtil.setFieldValue(obj, fieldName, value);
            }
        }
        return obj;
    }
}
