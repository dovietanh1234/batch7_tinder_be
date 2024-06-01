package com.semester.tinder.service;

import com.semester.tinder.dto.ReqRes;
import com.semester.tinder.dto.request.Profile.DetailUser;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.Profile;
import com.semester.tinder.entity.Role;
import com.semester.tinder.entity.User;
import com.semester.tinder.repository.IProfileRepo;
import com.semester.tinder.repository.IRoleRepo;
import com.semester.tinder.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private IUserRepo _iUserRepo; // related to jpa user

    @Autowired
    private JWTUtils _jwtUtils; // related to token

    @Autowired
    private PasswordEncoder _passwordEncoder; // related to passwordEncoder

    @Autowired
    private IProfileRepo _iProfileRepo;

    @Autowired
    private AuthenticationManager _authenticationManager; // related to quan ly phan quyen!
    //dc  cau hinh trong file "SecurityConfig" -> xu ly qua trinh xac thuc
    //no dinh nghia ra mot authenticate ma co the dc goi de xac thuc Authenticate object dc cung cap
    //

    @Autowired
    private IRoleRepo _iRoleRepo;

    public ReqRes signup( ReqRes registrationRequest ){
        ReqRes resp = new ReqRes();
        try {
            User user = new User();
            user.setFullname(registrationRequest.getName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(_passwordEncoder.encode(registrationRequest.getPassword()));
            user.setIsBlock(false);
           Optional<Role> role = _iRoleRepo.findById(1);
            role.ifPresent(user::setRole);
            User new_u = _iUserRepo.save( user );

                resp.setEmail( new_u.getEmail() );
                resp.setRole( new_u.getRole().getRole_name() );
                resp.setOurUser(new_u);
                resp.setMessage("user saved successfully");
                resp.setStatusCode(200);

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;

    }

    public ReqRes signin(ReqRes signinRequest){
            ReqRes response = new ReqRes();
            try {
                _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
                //AuthenticationManager là một interface cốt lõi trong
                // Spring Security, nó định nghĩa một phương thức authenticate
                // mà có thể được gọi để xác thực Authentication object được cung cấp
                var n_user = _iUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
                System.out.println("USER IS: " + n_user);

                 var jwt = _jwtUtils.generateToken(n_user);
                 var refreshToken = _jwtUtils.generateRefreshToken( new HashMap<>(), n_user);

                 response.setStatusCode(200);
                 response.setToken(jwt);
                 response.setRefreshToken( refreshToken );
                 response.setExpirationTime("24hour");
                 response.setMessage("successfully signed in");

            }catch (Exception e){
                response.setStatusCode(500);
                response.setError(e.getMessage());
            }
            return response;
    }

    public ReqRes signSocial(ReqRes registrationRequest){
        ReqRes response = new ReqRes();

        try{

         Optional<User> U = _iUserRepo.findByEmail(registrationRequest.getEmail());
            User user_n = null;
         if(U.isEmpty()){
             User user = new User();
             user.setFullname(registrationRequest.getName());
             user.setEmail(registrationRequest.getEmail());
             user.setPassword(_passwordEncoder.encode("12345678"));
             user.setIsBlock(false);
             Optional<Role> role = _iRoleRepo.findById(1);
             role.ifPresent(user::setRole);
             user_n =  _iUserRepo.save( user );
             //response.setIsHasProfile( false );
         } else{
             user_n = U.get();
            // response.setIsHasProfile( true );
         }

         Optional<Profile> p = _iProfileRepo.findByUser( user_n );
         if( p.isEmpty() ){
             response.setIsHasProfile( false );
         }else{
             response.setIsHasProfile( true );
         }

         _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), "12345678"));
         var refreshToken = _jwtUtils.generateRefreshToken( new HashMap<>(), user_n);

         //response.setId_user( U.get().getId() );
         response.setStatusCode(200);
         response.setToken(_jwtUtils.generateToken(user_n));
         response.setRefreshToken( refreshToken );
         response.setExpirationTime("24hour");
         response.setId_user(user_n.getId());
         response.setMessage("successfully signed in");

        }catch (Exception e){
            response.setStatusCode(400);
            response.setError(e.getMessage());
        }

        return response;
    }



    public ReqRes refreshToken( ReqRes refreshTokenRequest ){
        ReqRes response = new ReqRes();
        String ourEmail = _jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Optional<User> new_u = _iUserRepo.findByEmail( ourEmail );
        if(new_u.isPresent()){
            if( _jwtUtils.isTokenValid( refreshTokenRequest.getToken(), new_u.get()) ){
                var jwt = _jwtUtils.generateToken(new_u.get());
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken( refreshTokenRequest.getToken() );
                response.setExpirationTime("24hour");
                response.setMessage("successfully refresh token in");
            }
            return response;
        }
        response.setStatusCode(500);
        return response;
    }

    public ApiResponse<DetailUser> sendTokenGetDetail(String Token ){

        String EmailU = _jwtUtils.extractUsername(Token);
        Optional<User> u = _iUserRepo.findByEmail( EmailU );

        ApiResponse<DetailUser> result = new ApiResponse<>();
        if( u.isEmpty() ){
            result.setMessage("ERROR");
            result.setCode(404);
        }

        DetailUser dt = new DetailUser();
        dt.setEmail( EmailU );
        dt.setId( u.get().getId() );
        dt.setFull_name( u.get().getFullname() );
        dt.setPhone_number( u.get().getPhone_number() );
        dt.setImages( u.get().getImages() );

        result.setMessage("SUCCESSFULLY");
        result.setCode(200);
        result.setResult(dt);
        return result;


    }

}



/*
* if( role.isPresent() ){
*    user.setRole( role.get() )
* }
* Nó tương đương với:
* role.ifPresent(user::setRole)
*
*
*
* */