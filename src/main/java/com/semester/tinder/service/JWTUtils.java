package com.semester.tinder.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey Key;
    private static final long EXPIRATION_TIME = 86400000; // 24H

    public JWTUtils() {
        String secretKey = "12345678998765432345676543sdfghjkwertyugfdsacvbnmjhgfdwertyuikjh";
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){

        Map<String, Object> Claims = new HashMap<>();
        Claims.put("authorities", userDetails.getAuthorities());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(Claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
    /*
    * tại sao phải dùng "HashMap<String, Object> claims" trong param
    * claims( tập hợp các thông tin ta muốn đưa vào )
    * và ".claims(claims)" -> trong nó sẽ chứa rất nhiều thông tin theo cặp key-value ...
    * */

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject); // tham chieu den 1 method cua mot class
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction){
        return claimsFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }
    // trích xuất thông tin từ token JWT in this case:
    // T co the la bat ky kieu du lieu nao method "extractClaims" co the return ve kieu du lieu do
    // Kiểu dữ liệu cụ thể của T sẽ được xác định bởi hàm claimsFunction được truyền vào.

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }


    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }


/*
* Claims::getExpiration và Claims::getSubject -> method tham chieu trong java
*
*
* <T> được sử dụng để chỉ định một kiểu dữ liệu chung trong định nghĩa của một lớp, interface, method
*
*
* <T> T, bạn đang nói với trình biên dịch Java rằng T là một kiểu dữ liệu chung.
* <T> ở đầu phương thức cho trình biên dịch biết rằng T không phải là một kiểu
*  dữ liệu cụ thể mà là một kiểu dữ liệu chung sẽ được xác định khi phương thức được gọi.
*
*
* Cú pháp :: trong Java được gọi là Method Reference
*
* */

}
