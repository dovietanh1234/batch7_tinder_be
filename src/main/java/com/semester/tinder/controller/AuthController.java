package com.semester.tinder.controller;

import com.semester.tinder.dto.ReqRes;
import com.semester.tinder.dto.request.Profile.DetailUser;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(authService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.signin(signInRequest));
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/signin/social")
    public ResponseEntity<ReqRes> social(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok( authService.signSocial(refreshTokenRequest) );
    }

    @PostMapping("/token/getId")
    //@PreAuthorize("hasAnyAuthority('ROLE_USER') OR hasAnyAuthority('ROLE_ADMIN') OR hasAnyAuthority('ROLE_STAFF')")
    public ResponseEntity<ApiResponse<DetailUser>> sendTokenGetId( @RequestBody ReqRes refreshTokenRequest ){
            return ResponseEntity.ok( authService.sendTokenGetDetail(refreshTokenRequest.getToken()) );
    }

}
