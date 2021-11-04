package io.github.vlog.core.dto;

import lombok.Data;

@Data
public class RunLogMessage extends BaseLogMessage {

    /**
     * 事件时间
     */
    private Long dtTime;
    /**
     * 是否是Json 0：字符串；1：Json；2：JsonArray
     */
    private int isJson = 0;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 日志级别
     */
    private String logLevel;
    /**
     * 日志类名
     */
    private String className;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 时间
     */
    private String dateTime;

}
