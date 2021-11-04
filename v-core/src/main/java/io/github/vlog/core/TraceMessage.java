package io.github.vlog.core;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class TraceMessage {

    private Long runTime;

    private String traceId;

    private String messageType;

    // private String position;

    // private AtomicInteger positionNum = new AtomicInteger((0));

}
