package com.sdk.producer.service;

import com.sdk.producer.domain.BookEvent;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


@Service
public class MessageConsumerService {
    private KafkaConsumer<Integer, BookEvent> messageConsumer;
    public MessageConsumerService() {
        this.messageConsumer = getConsumer();
    }

    public List<BookEvent> getEvents(){
        messageConsumer.subscribe(Collections.singleton("library-inventory"));
        ConsumerRecords<Integer, BookEvent> record = messageConsumer.poll(100);
        List<BookEvent> eventlist = new ArrayList<>() ;

        record.forEach(record1-> eventlist.add(record1.value()));
        return eventlist;
    }

    private KafkaConsumer<Integer, BookEvent> getConsumer(){
        KafkaConsumer<Integer, BookEvent> consumer = new KafkaConsumer<>(getProperties());
        return consumer;
    }

    private Properties getProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        // props.put("transactional.id", "my-transactional-id");
        props.setProperty("group.id", "test");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "com.sdk.producer.domain.BookEventDeSerializer");
         return props;
    }
}
