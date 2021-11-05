package io.github.luons.vlog.logback.aspect;

import io.github.luons.vlog.core.LogMessageThreadLocal;
import io.github.luons.vlog.core.TraceId;
import io.github.luons.vlog.core.TraceMessage;
import io.github.luons.vlog.core.constant.LogMessageConstant;
import io.github.luons.vlog.core.util.GfJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

@Slf4j
public abstract class AbstractTraceAspect {

    public Object aroundExecute(JoinPoint joinPoint) throws Throwable {
        TraceMessage traceMessage = LogMessageThreadLocal.logMessageThreadLocal.get();
        String traceId = TraceId.logTraceID.get();
        if (traceMessage == null || traceId == null) {
            traceMessage = new TraceMessage();
            // traceMessage.getPositionNum().set(0);
        }
        long startTime = System.currentTimeMillis();
        traceMessage.setTraceId(traceId);
        traceMessage.setMessageType(joinPoint.getSignature().toString());
        // traceMessage.setPosition(LogMessageConstant.TRACE_START);
        // traceMessage.getPositionNum().incrementAndGet();
        LogMessageThreadLocal.logMessageThreadLocal.set(traceMessage);
        // log.info(LogMessageConstant.TRACE_PRE + GfJsonUtil.toJSONString(traceMessage));
        Object proceed = ((ProceedingJoinPoint) joinPoint).proceed();
        traceMessage.setMessageType(joinPoint.getSignature().toString());
        // traceMessage.setPosition(LogMessageConstant.TRACE_END);
        // traceMessage.getPositionNum().incrementAndGet();
        traceMessage.setRunTime(System.currentTimeMillis() - startTime);
        log.info(LogMessageConstant.TRACE_PRE + GfJsonUtil.toJSONString(traceMessage));
        return proceed;
    }
}
