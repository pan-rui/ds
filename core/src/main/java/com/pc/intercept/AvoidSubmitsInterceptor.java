package com.pc.intercept;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pc.annotation.AvoidSubmits;
import com.pc.annotation.impl.TokenProcessor;
import com.pc.base.BaseResult;
import com.pc.base.Constants;
import com.pc.base.ReturnCode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hynpublic on 2015/1/5.
 */
public class AvoidSubmitsInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
    public AvoidSubmitsInterceptor(){}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            AvoidSubmits avoidSubmits = ((HandlerMethod) handler).getMethod().getAnnotation(AvoidSubmits.class);
            TokenProcessor tokenProcessor= TokenProcessor.getInstance();
            if(avoidSubmits!=null&&avoidSubmits.saveToken())
                tokenProcessor.saveToken(request);
            if(avoidSubmits!=null&&avoidSubmits.removeToken()){
                boolean flag= TokenProcessor.isTokenValid(request);
                if(!flag){
                	returnMsg(response,new BaseResult(ReturnCode.REPEAT_SUBMIT_ERROR));
                }
                return flag;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
/*        request.setAttribute("productss",queryService.queryAllProduct());
        request.setAttribute("sends", JSON.parseArray(Constants.getSystemStringValue("DeliveryType")));
        request.setAttribute("linkss",queryService.queryAllLinks());*/
        super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
    }
    
    private void returnMsg(HttpServletResponse response, BaseResult result)
            throws IOException {
        response.setContentType(Constants.APPLICATION_JSON);
        response.setStatus(200);
        response.getOutputStream()
                .write(JSON.toJSONBytes(result, SerializerFeature.WriteEnumUsingToString));
        response.getOutputStream().close();
    }
}
