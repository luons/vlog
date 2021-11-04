package io.github.vlog.logback.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnMissingBean(value = AbstractTraceAspect.class, ignored = TraceAspect.class)
public class TraceAspect extends AbstractTraceAspect {

    @Around("@annotation(io.github.vlog.logback.annotation.Trace))")
    public Object around(JoinPoint joinPoint) throws Throwable {
        return aroundExecute(joinPoint);
    }
}
