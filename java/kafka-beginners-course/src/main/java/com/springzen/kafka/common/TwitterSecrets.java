package com.springzen.kafka.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TwitterSecrets {
    private String consumerKey;
    private String consumerSecret;
    private String token;
    private String secret;
}
