package org.example.newsfeed.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j(topic = "LoggingAspectAop")
@Aspect
public class LoggingAspectAop {

    // 모든 컨트롤러에서 작동시키기 위한 포인트컷
    @Pointcut("execution(* com.sparta.areadevelopment.controller..*(..))")
    private void controller() {
    }

    // 모든 컨트롤러에서 실행
    @Before("controller()")
    public void logBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        log.info("Request URL : ", request.getRequestURL().toString());
        log.info("HTTP Method : ", request.getMethod());
    }
}

