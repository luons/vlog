package io.github.vlog.core.disruptor;

import com.lmax.disruptor.ExceptionHandler;

public class EventExceptionHandler implements ExceptionHandler<LogEvent> {

    @Override
    public void handleEventException(Throwable ex, long sequence, LogEvent event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
