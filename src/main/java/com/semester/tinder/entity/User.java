package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.semester.tinder.repository.IRoleRepo;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "user")
public class User implements UserDetails {
    @Id
    private int id;

    private String fullname;

    private String email;

    private String phone_number;

    private Boolean isBlock = false;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private double latitude;

    private double longitude;

    private String password;

    private String images;

    private String gender;

    @JsonIgnore
    @OneToMany(mappedBy = "implementer")
    private List<Follower> people_i_follow;

    @JsonIgnore
    @OneToMany(mappedBy = "affected_person")
    private List<Follower> my_followers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ta sẽ tạo @ManyToMany cho một cột role nghĩa là: private List<Role> roles
        // -> đưa roles vào "SimpleGrantedAuthority()" thì ta sẽ có 1 user sẽ có nhiều roles
        return List.of( new SimpleGrantedAuthority( role.getRole_name() ));
    }
    // collection là mảng


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


