package io.github.luons.vlog.core;

import lombok.Data;

@Data
public class TraceMessage {

    private Long runTime;

    private String traceId;

    private String messageType;

    // private String position;

    // private AtomicInteger positionNum = new AtomicInteger((0));

}
