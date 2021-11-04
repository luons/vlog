package io.github.vlog.core.dto;

import lombok.Data;

@Data
public class TraceLogMessage extends BaseLogMessage {

    /**
     * 执行的毫秒时间
     */
    private Long time;
    /**
     * 执行的位置 开始 or 结束
     */
    private String position;
    /**
     * 执行位置数
     */
    private Integer positionNum;

}
