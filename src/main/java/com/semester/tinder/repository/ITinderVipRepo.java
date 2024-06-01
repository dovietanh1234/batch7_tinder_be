package com.semester.tinder.repository;

import com.semester.tinder.entity.TinderVip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITinderVipRepo extends JpaRepository<TinderVip, Integer> {
    Optional<TinderVip> findById(int id);
}
