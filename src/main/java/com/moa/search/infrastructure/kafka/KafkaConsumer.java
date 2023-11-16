package com.moa.search.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "logstash-test", groupId = "test-group")
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
    @KafkaListener(topics = "local-test", groupId = "your-consumer-group")
    public void listen(String message) {
        System.out.println("Received message in group your-consumer-group: " + message);
        // 여기에서 추가적인 처리를 할 수 있습니다.
    }

}
