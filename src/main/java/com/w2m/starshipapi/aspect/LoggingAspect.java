package com.w2m.starshipapi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.w2m.starshipapi.service.StarshipService.getStarshipById(..)) && args(id)")
    public void logBeforeGetStarshipById(JoinPoint joinPoint, Long id) {
        if (id < 0) {
            logger.warn("Attempt to get a starship with a negative ID: " + id);
        }
    }
}
