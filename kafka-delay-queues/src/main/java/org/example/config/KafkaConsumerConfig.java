package org.example.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${consumer.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${consumer.kafka.security.protocol}")
    private String securityProtocol;

    @Value("${consumer.kafka.security.mechanism}")
    private String saslMechanism;

    @Value("${consumer.kafka.security.username}")
    private String username;

    @Value("${consumer.kafka.security.password}")
    private String password;


    @Bean("consumerConfigs")
    public Map consumerConfigs() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "connect-cluster");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
//        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
//        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
//                "%s required username=\"%s\" " + "password=\"%s\";", PlainLoginModule.class.getName(), username, password
//        ));
//        props.put("auto.offset.reset", "latest");
//        props.put("enable.auto.commit", "false");
//        props.put("client.id", "mirchi_notifications");

        return props;
    }


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                "%s required username=\"%s\" " + "password=\"%s\";", PlainLoginModule.class.getName(), username, password
        ));
        props.put("auto.offset.reset", "latest");
        props.put("enable.auto.commit", "false");
        props.put("client.id", "mirchi_notifications");
        //props.put("listener.type", "batch");
        //props.put("max.poll.records", 20);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("KafkaConsumer")
    public <K, V> KafkaConsumer<K, V> kafkaConsumer() {
        return new KafkaConsumer<>(consumerConfigs());
    }


}
