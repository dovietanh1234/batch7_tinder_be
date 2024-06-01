package com.semester.tinder.controller.TestRabbitMQ;

import com.semester.tinder.dto.request.RabbitMQ.Notification;
import com.semester.tinder.dto.request.Test.OrderTest;
import com.semester.tinder.service.RappidMQ.RabbitMQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class MessController {

    private final RabbitMQService rabbitMQService;

//    @GetMapping("/send")
//    public ResponseEntity<String> createOrder(){
//
//        OrderTest order = new OrderTest(2, 120000, 1, "Viet");
//
//        rabbitMQService.sendMessage("node_channel", order);
//
//        return ResponseEntity.ok("sent successfully");
//    }

    @GetMapping("/send2")
    public ResponseEntity<String> createOrder2(){

        Notification no = new Notification();

        no.setId( UUID.randomUUID().toString() );
        no.setCodeNotify(1);
        no.setTitle("thông báo có người matching ");
        no.setRead(false);
        no.setContent("bạn đã matching với con mèo ...");
        no.setReceiverId(12);
        no.setUrlAttached("localhost:3000/123/123");
        no.setTimeNotify( new Date());

        rabbitMQService.sendMessage("node_channel", no);

        return ResponseEntity.ok("sent successfully");
    }



}
