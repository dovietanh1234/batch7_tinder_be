package com.semester.tinder.repository;

import com.semester.tinder.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepo extends JpaRepository<Role, Integer> {
}
