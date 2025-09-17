package org.silver9.s9_java_exchange_adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShovelService implements Shovel {

    // kubectl run --rm -it --restart=Never --image=confluentinc/cp-kafka asilvennoinen-kafkacat -- bash
    // kafka-topics --bootstrap-server kafka-service:9092 --create --topic binance-topic --partitions 1 --replication-factor 1 --config min.insync.replicas=1
    // kafka-console-producer --bootstrap-server kafka-service:9092 --topic binance-topic
    // kafka-console-consumer --bootstrap-server kafka-service:9092 --topic binance-topic --from-beginning --group test-group
    // kafka-consumer-groups --bootstrap-server kafka-service:9092 --list
    //
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Not a KafkaStreams producer, but a plaint one using application.yaml config

    @Override
    public void onNextMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("binance-topic", message);
        future.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                System.err.println("Error sending message to Kafka: " + throwable.getMessage());
            } else {
                //System.out.println("Sent message to Kafka offset: " + sendResult.getRecordMetadata().offset()
                //        + ", partition: " + sendResult.getRecordMetadata().partition()
                //        + ", timestamp: " + sendResult.getRecordMetadata().timestamp());
            }
        });
    }
}
