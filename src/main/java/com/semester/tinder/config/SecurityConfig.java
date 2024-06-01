package com.semester.tinder.config;

import com.google.cloud.storage.Cors;
import com.semester.tinder.service.OurUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// @EnableMethodSecurity -> enable cho check phương thức ở mỗi hàm trong controller

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private OurUserDetailService ourUserDetailService; // tao doi tuong cho UserDetailService

    @Autowired
    private JWTAuthFilter jwtAuthFilter; // cau hinh filter va spring security

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/public/**", "/ws/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/staff/**").hasAnyAuthority("ROLE_STAFF")
                        .requestMatchers("/staff&admin/**").hasAnyAuthority("ROLE_STAFF", "ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore( // xu ly xac thuc
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){ // tim kiem thong tin nguoi dung dua tren thong tin nguoi dung cung cap, sd passwordEncoder() de so sanh password
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    // mục đích thăng này dùng để:
    /*
    * Authentication Provider: được sử dụng bởi ProviderManager, chịu trách nhiệm xác minh thông tin xác thực của
    * người dùng hoặc ứng dụng. Ví dụ, khi một người dùng đăng nhập vào hệ thống, Authentication Provider sẽ kiểm
    * tra thông tin đăng nhập của người dùng và trả về kết quả xác thực.
Mặc định, DaoAuthenticationProvider sẽ là Authentication Provider được sử dụng. DaoAuthenticationProvider
*  sẽ dùng UserDetailsService và PasswordEncoder để xác thực người dùng.


    * */


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
//AuthenticationManager -> de quan ly viec Authentication cua user
    // no dung de: xu ly qua trinh xác thực nó sử dụng nhan vao gia tri cua client va xu ly return true: object or false: exception

    /*
    * Một instance mà nó thực hiện AuthenticationManager có thể làm 1 trong 3
    * việc sau khi nó implement method authenticate(): 1. Trả về 1 object
    *  Authentication (thường thì với trường hợp attribute authenticated=true)
    *  nếu nó kiểm tra được các giá trị input là hợp lệ. 2. Trả về 1 exception
    *  (AuthenticationException) nếu các giá trị input không hợp lệ 3. Trả về null nếu nó không thể xử lý
    * */

    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",  config);
        return source;

    }



}

// muốn dùng thk nào mà muô @Autowire thi ta se phai dung cai @Bean