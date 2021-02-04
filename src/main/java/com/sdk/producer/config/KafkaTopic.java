package com.sdk.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("dev")
public class KafkaTopic {
    @Bean
    public NewTopic eventTopic(){
        return TopicBuilder.name("library-inventory-dev")
                .partitions(3)
                .replicas(3)
                .build();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
