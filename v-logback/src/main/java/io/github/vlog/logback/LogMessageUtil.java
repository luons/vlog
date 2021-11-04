package io.github.vlog.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import io.github.vlog.core.LogMessageThreadLocal;
import io.github.vlog.core.TraceId;
import io.github.vlog.core.TraceMessage;
import io.github.vlog.core.constant.LogMessageConstant;
import io.github.vlog.core.dto.BaseLogMessage;
import io.github.vlog.core.dto.RunLogMessage;
import io.github.vlog.core.util.DateUtil;
import io.github.vlog.core.util.LogExceptionStackTrace;
import io.github.vlog.core.util.TraceLogMessageFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

public class LogMessageUtil {

    private static String isExpandRunLog(ILoggingEvent logEvent) {
        String traceId = null;
        if (LogMessageConstant.EXPAND.equals(LogMessageConstant.SLEUTH_EXPAND)) {
            if (!logEvent.getMDCPropertyMap().isEmpty()) {
                traceId = logEvent.getMDCPropertyMap().get(LogMessageConstant.TRACE_ID);
                TraceId.logTraceID.set(traceId);
            }
        }
        return traceId;
    }

    public static BaseLogMessage getLogMessage(final String appName, final ILoggingEvent iLoggingEvent) {
        String traceId = isExpandRunLog(iLoggingEvent);
        TraceMessage traceMessage = LogMessageThreadLocal.logMessageThreadLocal.get();
        String formattedMessage = getMessage(iLoggingEvent);
        if (formattedMessage.startsWith(LogMessageConstant.TRACE_PRE)) {
            if (!LogMessageConstant.EXPAND.equals(LogMessageConstant.DEFAULT_EXPAND) && traceId != null) {
                traceMessage.setTraceId(traceId);
            }
            return TraceLogMessageFactory.getTraceLogMessage(traceMessage, appName, iLoggingEvent.getTimeStamp());
        }
        RunLogMessage logMessage =
                TraceLogMessageFactory.getLogMessage(appName, formattedMessage, iLoggingEvent.getTimeStamp());
        logMessage.setClassName(iLoggingEvent.getLoggerName());

        if (LogMessageConstant.RUN_MODEL == 1) {
            logMessage.setMethod(iLoggingEvent.getThreadName());
        } else {
            StackTraceElement attackTraceElement = iLoggingEvent.getCallerData()[0];
            String method = attackTraceElement.getMethodName();
            String line = String.valueOf(attackTraceElement.getLineNumber());
            logMessage.setMethod(method + "(" + attackTraceElement.getFileName() + ":" + line + ")");
            logMessage.setDateTime(DateUtil.parseDateToStr(new Date(iLoggingEvent.getTimeStamp()), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        }
        logMessage.setLogLevel(iLoggingEvent.getLevel().toString());
        return logMessage;
    }

    private static String getMessage(ILoggingEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{LogExceptionStackTrace.erroStackTrace(throwableProxy.getThrowable()).toString()};
                return packageMessage(logEvent.getMessage(), args);
            } else {
                Object[] args = logEvent.getArgumentArray();
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof Throwable) {
                            args[i] = LogExceptionStackTrace.erroStackTrace(args[i]);
                        }
                    }
                    return packageMessage(logEvent.getMessage(), args);
                }
            }
        }
        return logEvent.getFormattedMessage();
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.indexOf(LogMessageConstant.DELIM_STR) > 0) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return TraceLogMessageFactory.packageMessage(message, args);
    }
}
