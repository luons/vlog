package io.github.luons.vlog.core.disruptor;

import com.lmax.disruptor.RingBuffer;
import io.github.luons.vlog.core.dto.BaseLogMessage;

/**
 * className：LogMessageProducer description： 日志生产
 */
public class LogMessageProducer {

    private RingBuffer<LogEvent> ringBuffer;

    public LogMessageProducer(RingBuffer<LogEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void send(BaseLogMessage data) {
        long next = ringBuffer.next();
        try {
            LogEvent event = ringBuffer.get(next);
            event.setBaseLogMessage(data);
        } finally {
            ringBuffer.publish(next);
        }
    }
}
