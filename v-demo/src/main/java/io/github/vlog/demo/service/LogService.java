package io.github.vlog.demo.service;

import com.alibaba.ttl.threadpool.TtlExecutors;
import io.github.vlog.core.LogMessage;
import io.github.vlog.logback.annotation.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class LogService {

    private static Logger logger = LoggerFactory.getLogger(LogService.class);

    private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(
            new ThreadPoolExecutor((2), (8), (0L), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()));

    // @Trace
    public void test1(String data) {
        logger.info("LogService test1 ==>>{}", data);
    }

    @Trace
    public void test2(String data, Long time, Integer v2, Double d, Date d4) {
        logger.info("LogService test2 ==>>{}", data);
    }


    @Trace
    public void testLog(String data) {
        logger.error("I am service! 下面调用EasyLogDubboService远程服务！");
        logger.info("远程调用成功！");

        executorService.execute(() -> {
            logger.info("子线程日志展示");
        });
        try {
            LogMessage lo = null;
            lo.setMethod("");
        } catch (Exception e) {
            logger.error("异常日志展示");
        }
        logger.warn("警告日志展示！");
    }
}
