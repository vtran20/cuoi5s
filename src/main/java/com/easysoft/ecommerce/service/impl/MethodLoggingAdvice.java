package com.easysoft.ecommerce.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLoggingAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodLoggingAdvice.class);

    @Around("execution(* com.easysoft.ecommerce.service.impl.*.*(..)) || execution(* com.easysoft.ecommerce.dao.impl.*.*(..))")
    public Object debugCall(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        Class<?> clazz;
        String methodName;
        try {
            if (LOGGER.isDebugEnabled()) {
                clazz = pjp.getTarget().getClass();
                methodName = clazz.getSimpleName() + "::"
                        + pjp.getSignature().getName();
                String arguments;
                arguments = convertToString(pjp.getArgs());
                LOGGER.debug("BEGIN: " + methodName + arguments);
                result = pjp.proceed();
                if (((MethodSignature) pjp.getSignature()).getReturnType() == Void.TYPE) {
                    LOGGER.debug("END: " + methodName + "(..)");
                } else {
                    LOGGER.debug("END: " + methodName + "(..)=" + result);
                }
            } else {
                result = pjp.proceed();
            }
        } catch (Exception e) {
            clazz = pjp.getTarget().getClass();
            methodName = clazz.getSimpleName() + "::"
                    + pjp.getSignature().getName();
            LOGGER.warn("END WITH EXCEPTION: " + methodName, e);
            throw e;
        }
        return result;
    }

    private static String convertToString(Object[] arguments) {
        StringBuffer result;
        if (arguments.length == 0) {
            result = new StringBuffer(4);
            result.append("(void)");
        } else {
            result = null;
            for (Object object : arguments) {
                if (result == null) {
                    result = new StringBuffer(100);
                    result.append('(').append(object);
                } else {
                    result.append(',').append(object);
                }
            }
            result.append(')');
        }
        return result.toString();
    }

}
