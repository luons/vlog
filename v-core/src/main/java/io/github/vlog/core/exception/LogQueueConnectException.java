package io.github.vlog.core.exception;

/**
 * 连接日志异常
 */
public class LogQueueConnectException extends Exception {

    public LogQueueConnectException() {
        super();
    }

    public LogQueueConnectException(String message) {
        super(message);
    }

    public LogQueueConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogQueueConnectException(Throwable cause) {
        super(cause);
    }
}
