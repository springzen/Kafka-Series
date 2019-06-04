package com.springzen.kafka.tutorial1;

import com.springzen.kafka.common.KafkaConstants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class ProducerDemo {

    public static void main(String[] args) {

        //
        // create producer properties
        // https://kafka.apache.org/documentation/#producerconfigs
        //
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.LOCAL_BOOTSTRAP_SERVERS);
        properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*
            - moved to closeable to send the message using Closeable
         */
        try (
                // create the producer
                KafkaProducer<String, String> producer = new KafkaProducer<>(properties)
        ) {

            // create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world");

            // send data - this is asynchronous
            producer.send(record);

            /*
                if try with resources is not used
                producer.flush();
                producer.close();
             */
        }

        /*
         * test on CLI
         * kafka-console-consumer.sh --bootstrap-server $KAFKA -topic first_topic --group my-third-app-group
         */
    }
}
