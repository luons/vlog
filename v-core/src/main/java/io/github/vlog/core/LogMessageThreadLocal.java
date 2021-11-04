package io.github.vlog.core;


import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * className：LogMessageThreadLocal description：LogMessageThreadLocal 用来存储trace相关信息
 */
public class LogMessageThreadLocal {
    public static TransmittableThreadLocal<TraceMessage> logMessageThreadLocal = new TransmittableThreadLocal<>();
}
