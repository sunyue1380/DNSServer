package cn.schoolwow.dns.config;

import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.quickserver.controller.ExceptionHandler;
import cn.schoolwow.quickserver.request.HttpRequest;
import cn.schoolwow.quickserver.response.HttpResponse;
import cn.schoolwow.quickserver.response.HttpStatus;
import cn.schoolwow.quickserver.session.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExceptionHandlerConfig implements ExceptionHandler {
    @Override
    public Object beforeBodyWrite(Throwable throwable, HttpRequest httpRequest, HttpResponse httpResponse, HttpSession httpSession) {
        httpResponse.status(HttpStatus.OK);
        CustomResponse customResponse = new CustomResponse();
        customResponse.setStatus(1);
        customResponse.setMsg(throwable.getMessage());
        if (!(throwable instanceof IOException)) {
            throwable.printStackTrace();
        }
        return customResponse;
    }
}
