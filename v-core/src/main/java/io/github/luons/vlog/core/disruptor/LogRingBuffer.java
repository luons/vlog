package io.github.luons.vlog.core.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;

import java.util.concurrent.Executors;

public class LogRingBuffer {

    public static RingBuffer<LogEvent> ringBuffer = getRingBuffer();

    private static RingBuffer<LogEvent> getRingBuffer() {

        RingBuffer<LogEvent> ringBuffer = RingBuffer.createMultiProducer(LogEvent::new, (512), new BlockingWaitStrategy());
        // 2. 通过 ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3. 创建含有4个消费者的数组:
        LogMessageConsumer[] consumers = new LogMessageConsumer[4];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new LogMessageConsumer("C" + i);
        }
        // 4. 构建多消费者工作池
        WorkerPool<LogEvent> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new EventExceptionHandler(), consumers);
        // 5. 设置多个消费者的sequence序号 用于单独统计消费进度, 并且设置到 ringBuffer 中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        // 6. 启动workerPool
        workerPool.start(Executors.newFixedThreadPool((4)));
        return ringBuffer;
    }
}
