package io.github.vlog.core;

import lombok.Data;

/**
 * className：LogMessage
 * appName 应用名称用来区分日志属于哪个应用
 * serverName 应用运行所属IP地址
 * traceId 应用traceId，配置了拦截器才能自动生成
 * logType 日志类型，区分运行日志还是链路日志
 */
@Data
public class LogMessage {

    private String appName;

    private String serverName;

    private Long dtTime;

    private String traceId;

    private Object content;

    private String logLevel;

    private String className;

    private String method;

    private String logType;

    private String dateTime;

}
