package com.springzen.kafka.common;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

@Slf4j
public class CloseableUtil {
    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
