package io.github.vlog.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.vlog.core.constant.LogMessageConstant;
import io.github.vlog.core.disruptor.LogMessageProducer;
import io.github.vlog.core.disruptor.LogRingBuffer;
import io.github.vlog.core.dto.BaseLogMessage;
import io.github.vlog.core.dto.RunLogMessage;
import io.github.vlog.core.exception.LogQueueConnectException;
import io.github.vlog.core.util.GfJsonUtil;
import io.github.vlog.core.util.ThreadPoolUtil;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MessageAppenderFactory {

    private static Boolean logOutPut = true;

    private static int exceptionCount = 0;

    /**
     * 异常超过maxExceptionCount不在输送日志到队列
     */

    private final static int maxExceptionCount = 10;
    /**
     * 当下游异常的时候，状态缓存时间
     */
    private final static Cache<String, Boolean> cache = CacheBuilder.newBuilder().expireAfterWrite((30), TimeUnit.SECONDS).build();

    /**
     * 设置阻塞队列为5000，防止OOM
     */
    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.getPool((4), (8), (5000));

    public static void push(BaseLogMessage baseLogMessage) {
        LogMessageProducer producer = new LogMessageProducer(LogRingBuffer.ringBuffer);
        producer.send(baseLogMessage);
    }

    public static void push(BaseLogMessage baseLogMessage, AbstractClient client) {
        final String redisKey =
                baseLogMessage instanceof RunLogMessage ? LogMessageConstant.LOG_KEY : LogMessageConstant.LOG_KEY_TRACE;
        threadPoolExecutor.execute(() -> {
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
     * @param baseLogMessage
     * @param client
     * @param logOutPutKey
     */
    public static void push(BaseLogMessage baseLogMessage, AbstractClient client, String logOutPutKey) {
        final String redisKey =
                baseLogMessage instanceof RunLogMessage ? LogMessageConstant.LOG_KEY : LogMessageConstant.LOG_KEY_TRACE;
        threadPoolExecutor.execute(() -> {
            logOutPut = cache.getIfPresent(logOutPutKey);
            if (logOutPut == null || logOutPut) {
                try {
                    client.pushMessage(redisKey, GfJsonUtil.toJSONString(baseLogMessage));
                    // 写入成功重置异常数量
                    logOutPut = true;
                    exceptionCount = 0;
                    cache.put(logOutPutKey, true);
                } catch (LogQueueConnectException e) {
                    exceptionCount++;
                    if (exceptionCount > maxExceptionCount) {
                        cache.put(logOutPutKey, false);
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
