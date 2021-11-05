package io.github.luons.vlog.demo;

import io.github.luons.vlog.logback.aspect.AbstractTraceAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;

// 自动全局扫描
//@Aspect
//@ComponentScan
public class AspectConfig extends AbstractTraceAspect {

    @Around("within(io.github.vlog..*))")
    public Object around(JoinPoint joinPoint) throws Throwable {
        return aroundExecute(joinPoint);
    }
}
