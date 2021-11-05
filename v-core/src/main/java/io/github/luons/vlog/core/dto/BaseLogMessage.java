package io.github.luons.vlog.core.dto;

import lombok.Data;

@Data
public class BaseLogMessage {
    /**
     * 服务IP
     */
    private String serverName;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 方法名
     */
    private String method;

}
