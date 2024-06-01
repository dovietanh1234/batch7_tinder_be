package com.semester.tinder.repository;

import com.semester.tinder.entity.CheckCountLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckCountLikesRepo extends JpaRepository<CheckCountLikes, Integer> {
}
