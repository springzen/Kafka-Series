package com.springzen.kafka.tutorial1;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.ExecutionException;

@Slf4j
public class ProducerDemoWithKeys {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ProducerDemoWithCallback producerDemoWithCallback = new ProducerDemoWithCallback();

        for (int i = 0; i < 10; i++) {
            // create a producer record
            final String topic = "first_topic";
            final String value = "hello world @ " + i;
            final String key = "id_" + i;
            log.info("Key {}", key);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producerDemoWithCallback.send(record).get(); // force it to be synchronous - do not do this in production
        }


        /*
         * test on CLI
         * kafka-console-consumer.sh --bootstrap-server $KAFKA -topic first_topic --group my-third-app-group
         */
    }
}
