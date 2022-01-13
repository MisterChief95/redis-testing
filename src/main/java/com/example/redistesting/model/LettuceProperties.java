package com.example.redistesting.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class LettuceProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LettuceProperties.class);

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("Init Lettuce Properties. Host: {}, Port: {}", host, port);
    }
}
