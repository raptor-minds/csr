package com.blockchain.csr.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class RequestLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);
    private final ObjectMapper objectMapper;

    public RequestLogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.blockchain.csr.controller..*.*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        try {
            String argsJson = objectMapper.writeValueAsString(args);
            logger.info("[请求日志] 类名: {}, 方法: {}, 参数: {}", className, methodName, argsJson);
        } catch (JsonProcessingException e) {
            logger.warn("参数序列化失败: {}", e.getMessage());
        }

        return joinPoint.proceed();
    }
}