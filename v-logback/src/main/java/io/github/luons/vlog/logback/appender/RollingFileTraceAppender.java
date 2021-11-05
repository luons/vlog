package io.github.luons.vlog.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import io.github.luons.vlog.core.dto.BaseLogMessage;
import io.github.luons.vlog.core.util.GfJsonUtil;
import io.github.luons.vlog.logback.LogMessageUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class RollingFileTraceAppender extends RollingFileAppender<ILoggingEvent> {

    private static final Logger log = LoggerFactory.getLogger(("-LOG_WATCH-"));

    private String appName;

    // private String level;
    private String packageName;


    @Override
    protected void append(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        if (loggerName == null || loggerName.isEmpty() || !loggerName.contains(packageName)) {
            return;
        }
        final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(appName, event);
        log.info(GfJsonUtil.toJSONString(logMessage));
    }

    @Override
    public void start() {
        super.start();
    }
}
