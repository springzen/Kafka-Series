package com.springzen.kafka.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;


public class SecretsLoader {
    public static TwitterSecrets loadTwitterAPiSecrets(String twitterSecretsFile) {
        try (InputStream input = new FileInputStream(twitterSecretsFile)) {

            /*
                - get your credentials from https://developer.twitter.com/
                -- https://developer.twitter.com/en/apps
                - sample twitter secrets properties file
                consumerKey=<your_consumer_key>
                consumerSecret=<your_consumer_secret>
                token=<your_token>
                secret=<your_secret>

             */


            Properties appProps = new Properties();
            appProps.load(input);

            final String consumerKey = appProps.getProperty("consumerKey");
            Objects.requireNonNull(consumerKey);

            final String consumerSecret = appProps.getProperty("consumerSecret");
            Objects.requireNonNull(consumerSecret);

            final String token = appProps.getProperty("token");
            Objects.requireNonNull(token);

            final String secret = appProps.getProperty("secret");
            Objects.requireNonNull(secret);

            return TwitterSecrets.builder()
                    .consumerKey(consumerKey)
                    .consumerSecret(consumerSecret)
                    .token(token)
                    .secret(secret)
                    .build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        final String userHome = System.getProperty("user.home");
        final TwitterSecrets twitterSecrets = loadTwitterAPiSecrets(Paths.get(userHome, "/work/dev/twitter/ts.properties").toString());
        System.out.println(twitterSecrets);
    }
}
