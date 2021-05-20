package cn.schoolwow.dns.interceptor;

import cn.schoolwow.dns.config.ConfigurationFile;
import cn.schoolwow.quickserver.interceptor.HandlerInterceptor;
import cn.schoolwow.quickserver.interceptor.Interceptor;
import cn.schoolwow.quickserver.request.HttpRequest;
import cn.schoolwow.quickserver.response.HttpResponse;
import cn.schoolwow.quickserver.response.HttpStatus;
import cn.schoolwow.quickserver.session.HttpSession;
import org.aeonbits.owner.ConfigCache;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Interceptor(patterns = "/**")
@Component
public class MDCInterceptor implements HandlerInterceptor {
    ConfigurationFile configurationFile = ConfigCache.getOrCreate(ConfigurationFile.class);

    @Override
    public boolean preHandle(HttpRequest httpRequest, HttpResponse httpResponse, HttpSession httpSession) throws IOException {
        MDC.put("requestId",UUID.randomUUID().toString());
        if(!configurationFile.username().isEmpty()&&!configurationFile.password().isEmpty()){
            if (httpRequest.hasHeader("Authorization")) {
                String authorization = httpRequest.getHeader("Authorization");
                authorization = authorization.substring(authorization.indexOf("Basic ") + 6);
                String expectAuthorization = new String(Base64.getEncoder().encode((configurationFile.username() + ":" + configurationFile.password()).getBytes()));
                if (authorization.equals(expectAuthorization)) {
                    return true;
                }
            }
            httpResponse.status(HttpStatus.UNAUTHORIZED);
            httpResponse.setHeader("WWW-Authenticate","Basic realm=QuickSign3 Basic Auth");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpRequest httpRequest, HttpResponse httpResponse, HttpSession httpSession) throws IOException {

    }

    @Override
    public void beforeResponse(HttpRequest httpRequest, HttpResponse httpResponse, HttpSession httpSession) throws IOException {
        httpResponse.setHeader("X-Request-Id",MDC.get("requestId"));
        MDC.remove("requestId");
    }
}
