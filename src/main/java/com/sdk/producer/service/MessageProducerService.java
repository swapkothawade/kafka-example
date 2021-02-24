package com.sdk.producer.service;

import com.sdk.producer.domain.Book;
import com.sdk.producer.domain.BookEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import org.springframework.stereotype.Service;


import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

@Service
public class MessageProducerService {

    private Producer<Integer, BookEvent> bookEventProducer;

    public MessageProducerService(){
        this.bookEventProducer = producer();
    }

    public void sendEvent(BookEvent event){
        ProducerRecord<Integer,BookEvent> record = new ProducerRecord<Integer,BookEvent>("library-inventory",event.getBookeventid(),event);
        Future<RecordMetadata> eventMeta = bookEventProducer.send(record);
    }


    public Producer<Integer, BookEvent> producer(){
        Producer<Integer, BookEvent> producer = new KafkaProducer<>(getProperties());
        return producer;
    }

    private Properties getProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
       // props.put("transactional.id", "my-transactional-id");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "com.sdk.producer.domain.BookEventSerializer");
       // Producer<String, String> producer = new KafkaProducer<>(props);
        return props;
    }


    public BookEvent getBookEventMessage(Book book) {
        Random rand = new Random();
        Integer eventid = rand.nextInt(10000);
        BookEvent event =BookEvent.builder().bookeventid(eventid).book(book).build();
        return new BookEvent(eventid,book);
    }
}
