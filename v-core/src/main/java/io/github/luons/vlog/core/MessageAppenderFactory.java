package io.github.luons.vlog.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.luons.vlog.core.constant.LogMessageConstant;
import io.github.luons.vlog.core.disruptor.LogMessageProducer;
import io.github.luons.vlog.core.disruptor.LogRingBuffer;
import io.github.luons.vlog.core.dto.BaseLogMessage;
import io.github.luons.vlog.core.dto.RunLogMessage;
import io.github.luons.vlog.core.util.GfJsonUtil;
import io.github.luons.vlog.core.util.ThreadPoolUtil;
import io.github.luons.vlog.core.exception.LogQueueConnectException;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MessageAppenderFactory {

    /**
     * 异常超过maxExceptionCount不在输送日志到队列
     */

    private final static int MAX_EXCEPTION_COUNT = 10;
    /**
     * 当下游异常的时候，状态缓存时间
     */
    private final static Cache<String, Boolean> CACHE = CacheBuilder.newBuilder().expireAfterWrite((30), TimeUnit.SECONDS).build();
    private static Boolean logOutPutFlag = true;
    private static int exceptionCount = 0;
    /**
     * 设置阻塞队列为5000，防止OOM
     */
    private static final ThreadPoolExecutor POOL_EXECUTOR = ThreadPoolUtil.getPool((4), (8), (5000));

    public static void push(BaseLogMessage baseLogMessage) {
        LogMessageProducer producer = new LogMessageProducer(LogRingBuffer.ringBuffer);
        producer.send(baseLogMessage);
    }

    public static void push(BaseLogMessage baseLogMessage, AbstractClient client) {
        final String redisKey =
                baseLogMessage instanceof RunLogMessage ? LogMessageConstant.LOG_KEY : LogMessageConstant.LOG_KEY_TRACE;
        POOL_EXECUTOR.execute(() -> {
            try {
                client.pushMessage(redisKey, GfJsonUtil.toJSONString(baseLogMessage));
            } catch (LogQueueConnectException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 带队列宕机保护的push方法
     *
     * @param baseLogMessage baseLogMessage
     * @param logKey         logKey
     * @param client         client
     * @param logOutPutKey   logOutPutKey
     */
    public static void push(BaseLogMessage baseLogMessage, String logKey, AbstractClient client, String logOutPutKey) {
        POOL_EXECUTOR.execute(() -> {
            logOutPutFlag = CACHE.getIfPresent(logOutPutKey);
            if (logOutPutFlag == null || logOutPutFlag) {
                try {
                    client.pushMessage(logKey, GfJsonUtil.toJSONString(baseLogMessage));
                    logOutPutFlag = true;
                    exceptionCount = 0;
                    CACHE.put(logOutPutKey, true);
                } catch (LogQueueConnectException e) {
                    exceptionCount++;
                    if (exceptionCount > MAX_EXCEPTION_COUNT) {
                        CACHE.put(logOutPutKey, false);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 带队列宕机保护的push方法
     *
     * @param logMessage   logMessage
     * @param logKey       logKey
     * @param client       client
     * @param logOutPutKey logOutPutKey
     */
    public static void push(String logMessage, String logKey, AbstractClient client, String logOutPutKey) {
        POOL_EXECUTOR.execute(() -> {
            logOutPutFlag = CACHE.getIfPresent(logOutPutKey);
            if (logOutPutFlag == null || logOutPutFlag) {
                try {
                    client.pushMessage(logKey, logMessage);
                    logOutPutFlag = true;
                    exceptionCount = 0;
                    CACHE.put(logOutPutKey, true);
                } catch (LogQueueConnectException e) {
                    exceptionCount++;
                    if (exceptionCount > MAX_EXCEPTION_COUNT) {
                        CACHE.put(logOutPutKey, false);
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
