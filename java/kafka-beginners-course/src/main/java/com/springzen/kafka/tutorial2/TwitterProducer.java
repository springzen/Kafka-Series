package com.springzen.kafka.tutorial2;

import com.google.common.collect.Lists;
import com.springzen.kafka.common.CloseableUtil;
import com.springzen.kafka.common.KafkaConstants;
import com.springzen.kafka.common.SecretsLoader;
import com.springzen.kafka.common.TwitterSecrets;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.springzen.kafka.common.CloseableUtil.close;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Slf4j
public class TwitterProducer {
    public TwitterProducer() {
    }

    public void run() {

        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);

        // create twitter client
        final Client twitterClient = createTwitterClient(msgQueue);
        twitterClient.connect();

        // create Kafka producer
        KafkaProducer<String, String> producer = createKafkaProducer();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown intercepted");

            close(producer);
            closeTwitterClient(twitterClient);

            log.info("Application existed.");
        }));

        // loop to sent tweets to Kafka
        // on a different thread, or multiple different threads....
        while (!twitterClient.isDone()) {

            /*
             * String msg = msgQueue.take();
             **/
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
                twitterClient.stop();
            }

            if (msg != null) {
                log.info(msg);
                producer.send(new ProducerRecord<>("twitter_tweets", null, msg), (metadata, exception) -> {
                    if (exception != null) {
                        log.warn(exception.getMessage(), exception);
                    }
                });
            }
        }

        log.info("Application existed.");
    }

    private KafkaProducer<String, String> createKafkaProducer() {

        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.LOCAL_BOOTSTRAP_SERVERS);
        properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

    public static void main(String[] args) {
        new TwitterProducer().run();
    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue) {


        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms

        /*
         * if following people
         *
         * List<Long> followings = Lists.newArrayList(1234L, 566788L);
         * hosebirdEndpoint.followings(followings);
         **/

        /*
         * topic to follow
         **/
        List<String> terms = Lists.newArrayList("bitcoin");

        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        final String userHome = System.getProperty("user.home");
        final TwitterSecrets twitterSecrets = SecretsLoader.loadTwitterAPiSecrets(Paths.get(userHome, "/work/dev/twitter/ts.properties").toString());

        Authentication hosebirdAuth = new OAuth1(twitterSecrets.getConsumerKey(), twitterSecrets.getConsumerSecret(), twitterSecrets.getToken(), twitterSecrets.getSecret());

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }

    private void closeTwitterClient(Client client) {
        try {
            client.stop();
        } finally {
            log.info("Twitter stopped.");
        }
    }
}
