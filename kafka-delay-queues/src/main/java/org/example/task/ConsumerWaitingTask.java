package org.example.task;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.example.kafka.KafkaProducer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ConsumerWaitingTask implements Runnable {

    Map<String, String> kafkaConsumerProperties;

    KafkaProducer kafkaProducer;

    List<String> delayTopics;

    String targetTopic;

    Long sleepTime;


    @Override
    public void run() {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(kafkaConsumerProperties);
        List<TopicPartition> topicPartitions = getTopicPartition(kafkaConsumer);
        kafkaConsumer.assign(topicPartitions);
        kafkaConsumer.seekToBeginning(topicPartitions);
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
            if (consumerRecords.isEmpty()) {
                continue;
            }

            kafkaConsumer.pause(topicPartitions);
            System.out.println("SLEEP START!");
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("SLEEP END!");

            kafkaConsumer.resume(topicPartitions);
            while (!consumerRecords.isEmpty()) {
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    kafkaProducer.sendMessage(targetTopic, consumerRecord.value());
                }
                consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            }
            break;
        }
    }

    private <K, V> List<TopicPartition> getTopicPartition(KafkaConsumer<K, V> kafkaConsumer) {
        List<PartitionInfo> list = kafkaConsumer.partitionsFor(delayTopics.get(0));
        List<TopicPartition> result = new ArrayList<>();
        for (PartitionInfo partitionInfo : list) {
            result.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
        }
        return result;
    }
}
