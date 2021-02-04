package com.sdk.producer.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import  org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class BookEventSerializer  implements Serializer<BookEvent> {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, BookEvent bookEvent) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(bookEvent).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public byte[] serialize(String topic, Headers headers, BookEvent data) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
