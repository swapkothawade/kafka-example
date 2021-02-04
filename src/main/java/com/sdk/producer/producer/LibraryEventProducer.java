package com.sdk.producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.producer.domain.BookEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;



@Component
@Slf4j
public class LibraryEventProducer {

    @Autowired
    private KafkaTemplate<Integer,String> kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;


    public String sendMessage(BookEvent bookEvent) throws JsonProcessingException {
        Integer eventId= bookEvent.getBookeventid();
        String message = mapper.writeValueAsString(bookEvent);
        ListenableFuture<SendResult<Integer, String>> result =  kafkaTemplate.sendDefault(eventId,message);

        result.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onFailure(Throwable throwable) {
                    failureHandler(eventId, message,throwable);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                    successHandler(eventId, message,result);
            }
        });
        return "Success";
    }

    private void successHandler(Integer key, String message ,SendResult<Integer,String> messageResult) {
        log.info(" Message Successfully persisted in partition {} with  id {} and with value {} in topic {}",
                messageResult.getRecordMetadata().partition(),
                messageResult.getProducerRecord().key(),
                messageResult.getProducerRecord().value(),
                messageResult.getRecordMetadata().topic());
    }

    private void failureHandler(Integer key, String message, Throwable t)  {
        log.error("Message failed with exception {} ", t.getMessage());
        try {
            throw t;
        } catch (Throwable throwable) {
            log.error("Error while processing  onFailure {} ", t.getMessage());
            throwable.printStackTrace();
        }
    }
}
