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
