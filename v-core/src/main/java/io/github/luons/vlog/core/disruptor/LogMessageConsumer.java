package io.github.luons.vlog.core.disruptor;

import com.lmax.disruptor.WorkHandler;
import io.github.luons.vlog.core.constant.LogMessageConstant;
import io.github.luons.vlog.core.AbstractClient;
import io.github.luons.vlog.core.dto.BaseLogMessage;
import io.github.luons.vlog.core.dto.RunLogMessage;
import io.github.luons.vlog.core.util.GfJsonUtil;

/**
 * className：LogMessageConsumer description： 日志消费
 */
public class LogMessageConsumer implements WorkHandler<LogEvent> {

    private String name;

    public LogMessageConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(LogEvent event) throws Exception {
        BaseLogMessage baseLogMessage = event.getBaseLogMessage();
        final String logKey =
                baseLogMessage instanceof RunLogMessage ? LogMessageConstant.LOG_KEY : LogMessageConstant.LOG_KEY_TRACE;
        AbstractClient.getClient().pushMessage(logKey, GfJsonUtil.toJSONString(baseLogMessage));
    }
}
