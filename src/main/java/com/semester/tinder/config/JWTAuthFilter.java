package com.semester.tinder.config;

import com.semester.tinder.service.JWTUtils;
import com.semester.tinder.service.OurUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    // có 2 cửa mà 1 request cần đi qua đó là: filter và security
// OncePerRequestFilter -> cấu hình cho cửa filter
// cấu hình cho cửa security

    @Autowired
    private JWTUtils _jwtUtils; // create token and verify token

    @Autowired
    private OurUserDetailService ourUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;

        final String userEmail;
        if ( authHeader == null || authHeader.isBlank() ) {
             filterChain.doFilter(request, response);
             return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = _jwtUtils.extractUsername(jwtToken);
        if( userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = ourUserDetailService.loadUserByUsername(userEmail);

            if ( _jwtUtils.isTokenValid(jwtToken, userDetails) ){

                //b1 tao một bối cảnh mới cho spring security
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            // tạo một securityContext rỗng mới -> huu ich khi ta muon thiet lap một bối cảnh bảo mật mới.
                // sau khi đi qua cửa filter rồi
                // ta tạo một bối cảnh bảo mật mới cho cửa spring security ( chi tiêt ve client đã đc xác thực )

                //b2 tạo một token mới cho spring security( thẻ ra vào cty ở cửa spring security )
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                ); // nguoi dung la "userDetails", password la "null", quyen han la "userDetails.getAuthorities()"
                // đưa vào đây đối tượng: userDetails, null, và các quyền của nó

                //b3 thiết lập chi tiết xác thực cho token
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // tạo ra một đối tượng "WebAuthenticationDetails" từ yêu cầu hiện tại
                // chứa địa chỉ IP và session.

                //b4
                securityContext.setAuthentication(token);
                // set token cho Spring Security. securityContext chứa thông tin xác thực người dùng

                //b5
                // sau đó đưa một bối cảnh securityContext -> vào cho cổng security.
                SecurityContextHolder.setContext(securityContext);
// securityContext thiết lập cho SecurityContextHolder
                //SecurityContextHolder -> class giúp cung cấp truy câp den spring security hien tai
                //Va bay gio "SecurityContext" hiẹn tại sẽ chứa thông tin xác thực của người dùng.

            }

        }

        filterChain.doFilter(request, response);
        }

    //doFilterInternal -> thực hiện logic cho cửa filter của mình
}
