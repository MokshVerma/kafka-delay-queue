package org.example.kafka;

import org.example.task.ConsumerWaitingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class KafkaDelay {

    @Autowired
    @Qualifier("consumerConfigs")
    Map<String, String> kafkaConsumerProperties;

    @Autowired
    @Qualifier("KafkaProducer")
    KafkaProducer kafkaProducer;

    @Autowired
    Properties properties;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000, initialDelay = 1000)
    public void start() {
        String delayTopic = "delayTopic";
        String targetTopic = "targetTopic";
        List<Future<?>> futures = new ArrayList<>();
        try {
            futures.add(executorService.submit(new ConsumerWaitingTask(kafkaConsumerProperties, kafkaProducer, Arrays.asList(delayTopic.split(",")), targetTopic, 30 * 1000L)));
            System.out.println("Task Submitted!");

//            for (Future<?> future : futures) {
//                future.get();
//            }

            System.out.println("Performing further tasks.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
