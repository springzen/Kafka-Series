package com.springzen.kafka.tutorial1;

import com.springzen.kafka.common.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Slf4j
public class ProducerDemoWithCallback {

    private Properties properties;

    public static void main(String[] args) {


        ProducerDemoWithCallback producerDemoWithCallback = new ProducerDemoWithCallback();

        for (int i = 0; i < 10; i++) {
            // create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world @ " + i);
            producerDemoWithCallback.send(record);
        }


        /*
         * test on CLI
         * kafka-console-consumer.sh --bootstrap-server $KAFKA -topic first_topic --group my-third-app-group
         */
    }

    public ProducerDemoWithCallback() {
        this.properties = defaultProperties();
    }

    private Properties defaultProperties() {
        //
        // create producer properties
        // https://kafka.apache.org/documentation/#producerconfigs
        //
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.LOCAL_BOOTSTRAP_SERVERS);
        properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }

    public ProducerDemoWithCallback withProperties(Properties properties) {
        Objects.requireNonNull(properties);
        this.properties = properties;

        return this;
    }

    public Future<RecordMetadata> send(ProducerRecord<String, String> record) {
    /*
        - moved to closeable
     */
        try (
                // create the producer
                KafkaProducer<String, String> producer = new KafkaProducer<>(this.properties)
        ) {
            // send data - this is asynchronous
            final Callback callback = (metadata, exception) -> {
                // executed on on send - success of failure
                if (exception == null) {
                    log.info("Topic {}", metadata.topic());
                    log.info("Partition {}", metadata.partition());
                    log.info("Offset {}", metadata.offset());
                    log.info("Timestamp {}", metadata.timestamp());
                } else {
                    log.warn(exception.getMessage(), exception);
                }
            };

            return producer.send(record, callback);

            /*
                if try with resources is not used
                producer.flush();
                producer.close();
             */
        }
    }
}
