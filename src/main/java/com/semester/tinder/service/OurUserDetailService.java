package com.semester.tinder.service;

import com.semester.tinder.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailService implements UserDetailsService {

    @Autowired
    private IUserRepo _iUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // nếu có nhiều bảng thì phải check trong nay!
        return _iUserRepo.findByEmail(username).orElseThrow();
    }
//loadUserByUsername de Spring Security sử dụng để tải thông tin người dùng khi thực hiện xác thực. Nghia la no se thong qua
    // cai nay de no lay thong tin cua nguoi dung return ve 1 UserDetail ( 1 cách thức tim user trong DB )






}
