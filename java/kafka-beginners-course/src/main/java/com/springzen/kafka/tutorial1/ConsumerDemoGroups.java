package com.springzen.kafka.tutorial1;

import com.springzen.kafka.common.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
public class ConsumerDemoGroups {

    public void consume() {

    }

    public static void main(String[] args) {

        //
        // create producer properties
        // https://kafka.apache.org/documentation/#producerconfigs
        //
        final String groupId = "my-fifth-app-group";

        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.LOCAL_BOOTSTRAP_SERVERS);
        properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(GROUP_ID_CONFIG, groupId);
        // options: earliest / latest / none
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");


        final String topic = "first_topic";

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe consumer to topic(s)
        consumer.subscribe(Collections.singleton(topic));

        // poll for new data
        while (true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {}", record.key());
                log.info("Value: {}", record.value());
                log.info("Partition: {}", record.partition());
                log.info("Offset: {}", record.offset());
            }
        }

    }
}
