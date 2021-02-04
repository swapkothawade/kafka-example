package com.sdk.producer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.ByteArraySerializer;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookEvent extends ByteArraySerializer {
    private Integer bookeventid;
    private Book book;
}
