package com.sdk.producer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.producer.domain.Book;
import com.sdk.producer.domain.BookEvent;
import com.sdk.producer.producer.LibraryEventProducer;
import com.sdk.producer.service.MessageProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventProducerController.class)
@AutoConfigureMockMvc
public class LibraryEventProducerControllerUnitTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private LibraryEventProducer producer;

    @MockBean    private MessageProducerService producerService;
    ObjectMapper mapper = new ObjectMapper();
    @Test
    void postLibraryEvent() throws Exception {

        String bookRequest = mapper.writeValueAsString(getBook());
        mockMvc.perform(post("/v1/libraryevent")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        when(producer.sendMessage(isA(BookEvent.class))).thenReturn("Success");

    }
    private Book getBook() {
        return Book.builder()
                .bookid("121")
                .title("Mindset")
                .author("Coral S. DWECK").build();
    }
}
