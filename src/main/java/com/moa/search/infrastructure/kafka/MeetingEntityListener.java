package com.moa.search.infrastructure.kafka;

import com.moa.search.domain.Meeting;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MeetingEntityListener {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostPersist
    public void onPostPersist(Meeting meeting) {
        // Kafka로 전송할 메시지 생성
        String message = createMessage(meeting);
        // Kafka 토픽으로 메시지 전송
        kafkaTemplate.send("search", message);
    }

    private String createMessage(Meeting meeting) {
        // 메시지 생성 로직 구현
        return "New Meeting: " + meeting.getMeetingTitle()+ ", " + meeting.getId(); // 예시 메시지
    }

}
