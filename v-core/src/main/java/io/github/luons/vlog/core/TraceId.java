package io.github.luons.vlog.core;


import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 用来存储traceID相关信息
 */
public class TraceId {
    public static TransmittableThreadLocal<String> logTraceID = new TransmittableThreadLocal<>();
}
