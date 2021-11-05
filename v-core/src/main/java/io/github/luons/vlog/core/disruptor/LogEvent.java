package io.github.luons.vlog.core.disruptor;

import io.github.luons.vlog.core.dto.BaseLogMessage;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogEvent implements Serializable {
    /**
     * 日志基础信息
     */
    private BaseLogMessage baseLogMessage;

    public LogEvent() {

    }

}
