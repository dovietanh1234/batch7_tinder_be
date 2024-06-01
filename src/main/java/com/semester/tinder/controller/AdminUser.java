package com.semester.tinder.controller;

import com.semester.tinder.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUser {

    // normalize API:
    @GetMapping("/user/product")
    public ApiResponse<String> getDataUser(){

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(200);
        apiResponse.setMessage("OK");
        apiResponse.setResult("get data user success");

        return apiResponse;
    }

    @GetMapping("/staff/product")
    public ResponseEntity<?> getDataStaff(){
        return ResponseEntity.ok("get data of staff");
    }

    @GetMapping("/admin/product")
    public ResponseEntity<?> getDataAdmin(){
        return ResponseEntity.ok("get data of admin");
    }



}
