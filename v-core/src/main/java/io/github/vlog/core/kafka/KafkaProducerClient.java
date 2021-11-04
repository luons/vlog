package io.github.vlog.core.kafka;

import io.github.vlog.core.AbstractClient;
import io.github.vlog.core.exception.LogQueueConnectException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * className：KafkaProducerClient description：kafka Producer instance
 */
public class KafkaProducerClient extends AbstractClient {
    private static KafkaProducerClient instance;
    private KafkaProducerPool kafkaProducerPool;

    private KafkaProducerClient(String hosts) {
        this.kafkaProducerPool = new KafkaProducerPool(hosts);
    }

    public static KafkaProducerClient getInstance(String hosts) {
        if (instance == null) {
            synchronized (KafkaProducerClient.class) {
                if (instance == null) {
                    instance = new KafkaProducerClient(hosts);
                }
            }
        }
        return instance;
    }

    @Override
    public void pushMessage(String topic, String message) throws LogQueueConnectException {
        KafkaProducer kafkaProducer = null;
        try {
            kafkaProducer = kafkaProducerPool.getResource();
            kafkaProducer.send(new ProducerRecord<String, String>(topic, message));
        } catch (Exception e) {
            throw new LogQueueConnectException("kafka 写入失败！", e);
        } finally {
            if (kafkaProducer != null) {
                kafkaProducerPool.returnResource(kafkaProducer);
            }
        }

    }
}
