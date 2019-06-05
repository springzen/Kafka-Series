package com.springzen.kafka.tutorial1;

import com.springzen.kafka.common.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
public class ConsumerDemoWithThreads {


    private ConsumerDemoWithThreads() {
    }

    private void consume() {
        final String groupId = "my-sixth-app-group";
        final String topic = "first_topic";
        CountDownLatch latch = new CountDownLatch(1);

        ConsumerRunnable consumerRunnable = new ConsumerRunnable(
                KafkaConstants.LOCAL_BOOTSTRAP_SERVERS,
                groupId,
                topic,
                latch
        );

        Thread myThread = new Thread(consumerRunnable);
        myThread.start();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown intercepted");
            consumerRunnable.shutdown();

            try {
                latch.await();
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
            }
            log.info("Application existed.");
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Application was interrupted", e);
        } finally {
            log.info("Application is closing.");
        }

    }

    public class ConsumerRunnable implements Runnable {

        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;

        ConsumerRunnable(String bootstrapServers,
                         String groupId,
                         String topic,
                         CountDownLatch latch) {
            this.latch = latch;

            Properties properties = new Properties();
            properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(GROUP_ID_CONFIG, groupId);
            // options: earliest / latest / none
            properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");


            // create consumer
            consumer = new KafkaConsumer<>(properties);

            // subscribe consumer to topic(s)
            consumer.subscribe(Collections.singleton(topic));
        }

        @Override
        public void run() {

            try {
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
            } catch (WakeupException e) {
                log.info("WakeUp called... exiting");
            } finally {
                close(consumer);

                // latch is released
                latch.countDown();
            }

        }

        void shutdown() {
            // wakeup method interrupts consumer.poll()
            // throw WakeUpException
            consumer.wakeup();
        }

        private void close(Closeable closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        }
    }


    public static void main(String[] args) {
        ConsumerDemoWithThreads consumerDemo = new ConsumerDemoWithThreads();
        consumerDemo.consume();

    }
}
