package com.semester.tinder.repository;

import com.semester.tinder.entity.VipUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVipUsersRepo extends JpaRepository<VipUsers, Integer> {

    @Query("select v from vipUsers as v where v.user.id = :id1 ")
    Optional<VipUsers> findIsExist(@Param("id1") int id1);

}
