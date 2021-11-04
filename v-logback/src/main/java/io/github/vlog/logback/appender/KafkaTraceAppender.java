package io.github.vlog.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.github.vlog.core.MessageAppenderFactory;
import io.github.vlog.core.constant.LogMessageConstant;
import io.github.vlog.core.dto.BaseLogMessage;
import io.github.vlog.core.kafka.KafkaProducerClient;
import io.github.vlog.logback.LogMessageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * className：KafkaAppender
 * description：KafkaAppender 如果使用kafka作为队列用这个KafkaAppender输出
 */

@Data
@Slf4j
public class KafkaTraceAppender extends AppenderBase<ILoggingEvent> {

    private KafkaProducerClient kafkaClient;

    private String appName;
    private String kafkaHosts;
    private String runModel;
    private String expand;

    @Override
    protected void append(ILoggingEvent event) {
        final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(appName, event);
        MessageAppenderFactory.push(logMessage, kafkaClient, "plume.log.ack");
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

