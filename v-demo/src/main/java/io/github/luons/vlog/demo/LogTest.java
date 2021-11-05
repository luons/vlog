package io.github.luons.vlog.demo;

import io.github.luons.vlog.core.TraceId;
import io.github.luons.vlog.core.util.IdWorker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTest {

    private static final IdWorker worker = new IdWorker(1, 1, 1);

    public static void main(String[] args) {

        log.info("begin to start write log");
        String traceId = TraceId.logTraceID.get();
        // 设置 traceId
        if (traceId == null || traceId.isEmpty()) {
            TraceId.logTraceID.set(String.valueOf(worker.nextId()));
            log.info(traceId);
        }
        traceId = TraceId.logTraceID.get();
        if (traceId == null || traceId.isEmpty()) {
            System.out.println("设置traceId 失败！！");
        }
        log.info("{\"name\": 123}");
        System.out.println("end to start write log");
    }
}
