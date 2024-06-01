package com.semester.tinder.controller;

import com.semester.tinder.dto.firebase.StatusEnum;
import com.semester.tinder.dto.request.Follower.LikeUserRequest;
import com.semester.tinder.dto.request.Follower.StatusFollower;
import com.semester.tinder.dto.request.RabbitMQ.Notification;
import com.semester.tinder.dto.request.Test.Message;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.Follower;
import com.semester.tinder.entity.User;
import com.semester.tinder.repository.IFollowerRepo;
import com.semester.tinder.repository.IUserRepo;
import com.semester.tinder.service.RappidMQ.RabbitMQService;
import com.semester.tinder.service.message.MessageFirebaseService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Controller
public class TestController {

    @Autowired
    private IUserRepo _iUserRepo;

    @Autowired
    private IFollowerRepo _iFollowerRepo;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    // simpMessagingTemplate -> 1 phần module của spring message cung cấp methods gửu mess đến đích cụ thể
    // bao gồm việc gửu tn đến ng dùng cụ thể qua kênh socket
    // Gửi tin nhắn đến các đích đến (destinations) như /topic hoặc /queue
    //sử dụng một tiền tố đích đến người dùng được cấu hình, thường là /user

    @Autowired
    public MessageFirebaseService _messageFireBaseService;

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receivePublicMessage(@Payload Message message){
//        System.out.println(message.toString());
//        return message;
//    }

    // all tin nhắn gửu đi sẽ được gửu vào đường dẫn này "@MessageMapping("/private-message")"
    // sau đó "simpMessagingTemplate.convertAndSendToUser" sẽ chuyển hoá tin nhắn về cho một đối tợng cụ thể
    //  Đờng dẫn sẽ là  /user/ReceiverId/private
//    @MessageMapping("/private-message")
//    public Message recMessage(@Payload Message message) throws ExecutionException, InterruptedException {
//        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getMatchId()) ,"/private",message);//  user/David/private

        // save message in fire base

//        System.out.println(message.toString());
//        System.out.println(message.getReceiverId());
//         // _messageFireBaseService.createSocketMessage(message);
//          return message;
//    }
// Phương thức convertAndSendToUser của simpMessagingTemplate được sử dụng để gửi tin nhắn đến một người dùng cụ thể thông qua WebSocket.

    // simpMessagingTemplate.convertAndSendToUser(userId, destination, payload);

    // khi ta gọi "convertAndSendToUser" spring sẽ xác định kết nối WebSocket của người dùng dựa
    // trên "userId" và gửu tin nhắn đến địa chỉ đã chỉ định.


    //http://localhost:8080/ws
    // ->  /app/chat/{roomId}
    // -> /topic/{roomId}
//    @MessageMapping("/chat/{roomId}")
//    @SendTo("/topic/{roomId}")
//    public Message chat(@DestinationVariable String roomId,@Payload Message message) {
//
//        // xử lý async await :
//       message.setTimeSent( new Date());
//       message.setStatus( StatusEnum.SENT );

//        CompletableFuture.runAsync( ()  -> {
//            try {
//                _messageFireBaseService.createSocketMessage(message);
//            }catch (Exception e){
//                throw new RuntimeException(e.getMessage());
//            }
//
//        } );
//        System.out.println(roomId);
//
//        System.out.println(message.toString());
//
//        return message;
//    }


//    @MessageMapping("/sent/{roomId}")
//    @SendTo("/instance/topic/123")
@MessageMapping("/message/{roomId}")
@SendTo("/chatroom/public/{roomId}")
//@Async
    public Message message1(@DestinationVariable String roomId, Message message) {
        System.out.println(message.toString());
        System.out.println(roomId);

        // save message in fire base:
            CompletableFuture.runAsync( ()  -> {
                Notification no = new Notification();
            try {

                message.setTimeSent( new Date() );
                message.setStatus( StatusEnum.SENT );


                _messageFireBaseService.createSocketMessage(message);

                // 2. save thong bao vao firebase.
                no.setId( UUID.randomUUID().toString() );
                no.setSenderId(message.getSenderId());
                no.setCodeNotify(2);
                no.setTitle("Thông báo có tin nhắn mới từ " + message.getNameSender()); // bo sung doan nay vao react guu du lieu len!
                no.setRead(false);
                no.setContent(message.getMessage());
                no.setReceiverId(message.getReceiverId());
               // no.setUrlAttached("localhost:3000/123/123");
                no.setTimeNotify( new Date());
                _messageFireBaseService.createNotify(no);
                // 1. guu thong bao cho node js:
                  rabbitMQService.sendMessage("node_channel", no);

            }catch (Exception e){
                no.setCodeNotify(2);
                no.setTitle("Có lỗi xảy ra:");
                no.setContent("Tin nhắn gửu đi đã bị lỗi ");
                no.setReceiverId(message.getSenderId());
                no.setTimeNotify( new Date() );

                rabbitMQService.sendMessage("node_channel", no);
            }

        } );    

        return message;
    }

// quy chuẩn thah một cái form gửu thông báo .




}