//package pers.fjl.server.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import pers.fjl.common.entity.RequestLog;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 切面处理
// *
// * @author fangjiale 2021年01月26日
// */
//@Aspect
//@Slf4j
//@Component
//public class LogAspect {
//
//    //    @Pointcut("execution(* pers.fjl.server.*.*(..))")
//    @Pointcut("within(pers.fjl.server..*)")
//    public void log() {
//        System.out.println("pointcut");
//    }
//
//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes!=null){
//            HttpServletRequest request = attributes.getRequest();
//            String url = request.getRequestURL().toString();
//            String ip = request.getRemoteAddr();
//            String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
//            Object[] args = joinPoint.getArgs();
//            RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
//            log.info("Request:{}", requestLog);
//        }
//    }
//
//    @After("log()")
//    public void doAfter() {
////        System.out.println("after");
////        log.info("---------doAfter-----");
//    }
//
//    @AfterReturning(returning = "result", pointcut = "log()")
//    public void doAfterReturn(Object result) {
//        log.info("Result:{}", result);
//    }
//
//}
