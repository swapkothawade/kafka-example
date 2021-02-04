package com.sdk.producer.controller;

import com.sdk.producer.domain.BookEvent;
import com.sdk.producer.service.MessageConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LibraryEventConsumer {
    private MessageConsumerService consumerService;

    @Autowired
    public LibraryEventConsumer(MessageConsumerService consumerService) {
        this.consumerService = consumerService;
    }


    @GetMapping("/event")
    public ResponseEntity<?> events(){
        List<BookEvent> events = consumerService.getEvents();

        return ResponseEntity.ok(events);
    }


}
