package cn.schoolwow.dns.config;

import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.quickserver.controller.ResponseBodyAdvice;
import cn.schoolwow.quickserver.request.HttpRequest;
import cn.schoolwow.quickserver.response.HttpResponse;
import cn.schoolwow.quickserver.session.HttpSession;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ResponseBodyConfig implements ResponseBodyAdvice {
    @Override
    public boolean support(Method method) {
        return method.getDeclaringClass().getName().startsWith("cn.schoolwow");
    }

    @Override
    public Object beforeBodyWrite(Object result, Method method, HttpRequest httpRequest, HttpResponse httpResponse, HttpSession httpSession) {
        if(null==result){
            return null;
        }
        if(result instanceof CustomResponse){
            return result;
        }
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(result);
        return customResponse;
    }
}
