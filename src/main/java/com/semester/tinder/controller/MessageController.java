package com.semester.tinder.controller;

import com.semester.tinder.dto.firebase.GetMessagedto;
import com.semester.tinder.dto.firebase.Message;
import com.semester.tinder.dto.firebase.MessageFormCreate;
import com.semester.tinder.dto.request.RabbitMQ.Notification;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.service.message.MessageFirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
@RestController
@RequestMapping("/auth/service")
public class MessageController {

    @Autowired
    public MessageFirebaseService _messageFireBaseService;

//    @PostMapping("/create-message")
//    public ApiResponse<String> createMessage(@ModelAttribute MessageFormCreate message) {
//
//        ApiResponse<String> result = new ApiResponse<>();
//
//        String Image = "";
//        // HANDLE IMAGE:
//    if( message.getMultipartFile() != null ){
//        Image = _messageFireBaseService.uploadFile( message.getMultipartFile() );
//        if(Image == null){
//
//            result.setMessage("error handle image");
//            result.setCode(400);
//
//            return result;
//        }
//
//    }
//
//    Message message1 = new Message();
//
//    message1.setContent(message.getContent());
//    message1.setImage(Image);
//    message1.setSender_id(message.getSender_id());
//    message1.setReceive_id(message.getReceive_id());
//    message1.setMatches_id(message.getMatches_id());
//
//        result.setMessage("create success");
//        result.setCode(200);
//        result.setResult(_messageFireBaseService.createMessage(message1));
//        return result;
//
//    }

    // new tin nhắn mới t5
    @PostMapping("/get-message")
    public ApiResponse<List<com.semester.tinder.dto.request.Test.Message>> getList(@RequestBody GetMessagedto getMessagedto) throws ExecutionException, InterruptedException {

        ApiResponse<List<com.semester.tinder.dto.request.Test.Message>> result = new ApiResponse<>();

        result.setMessage("get data done");
        result.setCode(200);
        result.setResult(_messageFireBaseService.getMessages(getMessagedto));

        return result;
    }

    // new tin nhắn mới t5
    @DeleteMapping("/delete/message")
    public ApiResponse<String> deleteL(@RequestParam String documentId){
        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("create success");
        result.setCode(200);
        result.setResult(_messageFireBaseService.deleteMessage(documentId));

        return result;
    }

    @DeleteMapping("/delete/notify")
    public ApiResponse<String> deleteNotify(@RequestParam String documentId){
        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("create success");
        result.setCode(200);
        result.setResult(_messageFireBaseService.deleteNotify(documentId));

        return result;
    }

    // get list dữ liệu notify
@GetMapping("/notification")
    public ApiResponse<List<Notification>> ListNotify(@RequestParam int id ) throws ExecutionException, InterruptedException {

    ApiResponse<List<Notification>> result = new ApiResponse<>();
    result.setMessage("get list successfully");
    result.setCode(200);
    result.setResult( _messageFireBaseService.getListNotify(id) );
        return result;
}

    @GetMapping("/update/status/message")
    public ApiResponse<String> updateMassageStatus( @RequestParam int matchId ){

        ApiResponse<String> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("update message status");
        result.setResult( _messageFireBaseService.updateMessageStatus(matchId) );

        return result;
    }

    @GetMapping("/update/status/notify")
    public ApiResponse<String> updateNotifyStatus( @RequestParam int receiverId ){

        ApiResponse<String> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("update message status");
        result.setResult( _messageFireBaseService.updateNotifyStatus(receiverId) );

        return result;
    }

}



