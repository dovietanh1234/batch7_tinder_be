package com.semester.tinder.service.RappidMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String queueName, Object message) {

        try{
            String messageJson = objectMapper.writeValueAsString(message);

            rabbitTemplate.convertAndSend(queueName, messageJson);
            System.out.println("SENT MESSAGE SUCCESS");

        }catch ( JsonProcessingException ex ){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
