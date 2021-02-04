package intg.com.sdk.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdk.producer.domain.Book;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = com.sdk.producer.LibraryEventProducerApplication.class)
@EmbeddedKafka(topics = {"library-inventory-dev"},partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventProducerControllerIntgTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<Integer,String> consumer;
    @BeforeEach
    void setUp(){
        Map<String,Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("test","true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory(configs,new IntegerDeserializer(),new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @Test
     void testEventProducerController() throws JsonProcessingException {
        HttpEntity<Book> bookHttpEntity = new HttpEntity<>(getBook());

        ResponseEntity<?> response = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,bookHttpEntity, Book.class);

       ConsumerRecord<Integer,String> record =KafkaTestUtils.getSingleRecord(consumer,"library-inventory-dev");
       //System.out.print(" Value" + record.value());


        String expectedValue ="{\"bookeventid\":1028,\"book\":{\"bookid\":\"121\",\"title\":\"Mindset\",\"author\":\"Coral S. DWECK\"}}";
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(expectedValue,record.value());
    }

   @AfterEach
    void tearDown(){
        consumer.close();
    }

    private Book  getBook() {
        return Book.builder()
                .bookid("121")
                .title("Mindset")
                .author("Coral S. DWECK").build();
    }

}
