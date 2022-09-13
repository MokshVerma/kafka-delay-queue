package org.example.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


    @Value("${producer.kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${producer.kafka.security.protocol}")
    private String securityProtocol;

    @Value("${producer.kafka.sasl.mechanism}")
    private String saslMechanism;

    @Value("${producer.kafka.user}")
    private String kafkaUser;

    @Value("${producer.kafka.password}")
    private String kafkaPassword;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put("sasl.jaas.config", ScramLoginModule.class.getName() + " required username=\"" + kafkaUser + "\" password=\"" + kafkaPassword + "\";");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put("security.protocol", securityProtocol);
//        props.put("sasl.mechanism", saslMechanism);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1500);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1048576);
//        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
//        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        return props;
    }

    @Bean
    public <K, V> ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public <K, V> KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean("KafkaProducer")
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer();
    }

}
