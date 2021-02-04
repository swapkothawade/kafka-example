package com.sdk.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdk.producer.domain.Book;
import com.sdk.producer.domain.BookEvent;
import com.sdk.producer.producer.LibraryEventProducer;
import com.sdk.producer.service.MessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.net.URI;
import java.util.Random;

@RestController
public class LibraryEventProducerController {
    @Autowired
    private MessageProducerService producerService;
    @Autowired
    private LibraryEventProducer producer;
    @Autowired

    @GetMapping
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("Welcome to Kafka");
    }

    /**
     * Without Kafka Template
     * @return
     */
    @PostMapping("/libraryevent")
    public ResponseEntity<?> event(){
        BookEvent event = getBookEvent();
        producerService.sendEvent(event);
        return ResponseEntity.created(URI.create("http://localhost:8080/libraryevent/" + event.getBookeventid())).body("Event Send to Kafaka");
    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<?> bookevent( @Valid @RequestBody  Book book){
        Random rand = new Random();
        Integer eventid = rand.nextInt(10000);
       BookEvent bookEvent = BookEvent.builder().bookeventid(eventid).book(book).build();
        try {
            producer.sendMessage(bookEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Event not created because of " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookEvent);
    }


    private BookEvent getBookEvent() {
       Book book = new Book("123","The Monk who sold his Ferari","Robin Sharma");
        Random rand = new Random();
        Integer eventid = rand.nextInt(10000);
        return new BookEvent(eventid,book);
    }
}
