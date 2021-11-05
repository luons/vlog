package io.github.luons.vlog.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.github.luons.vlog.core.MessageAppenderFactory;
import io.github.luons.vlog.core.constant.LogMessageConstant;
import io.github.luons.vlog.core.dto.BaseLogMessage;
import io.github.luons.vlog.core.kafka.KafkaProducerClient;
import io.github.luons.vlog.logback.LogMessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * className：KafkaAppender description：KafkaAppender 如果使用kafka作为队列用这个KafkaAppender输出
 */

@Data
@Slf4j
public class KafkaTraceAppender extends AppenderBase<ILoggingEvent> {

    private KafkaProducerClient kafkaClient;

    private String kafkaHosts;
    private String kafkaTopic;
    private String appName;
    private String pattenLogger;
    private String runModel;
    private String expand;

    @Override
    protected void append(ILoggingEvent event) {
        if (!StringUtils.isEmpty(pattenLogger) && event.getLoggerName().indexOf(pattenLogger) > 0) {
            MessageAppenderFactory.push(event.getMessage(), kafkaTopic, kafkaClient, ("vlog.log.ack"));
            return;
        }
        final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(appName, event);
        MessageAppenderFactory.push(logMessage, kafkaTopic, kafkaClient, ("vlog.log.ack"));
    }

    @Override
    public void start() {
        super.start();
        if (this.runModel != null) {
            LogMessageConstant.RUN_MODEL = Integer.parseInt(this.runModel);
        }
        if (kafkaClient == null) {
            kafkaClient = KafkaProducerClient.getInstance(this.kafkaHosts);
        }
        if (expand != null && LogMessageConstant.EXPANDS.contains(expand)) {
            LogMessageConstant.EXPAND = expand;
        }
    }
}

