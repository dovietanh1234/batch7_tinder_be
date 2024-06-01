package com.semester.tinder.repository;

import com.semester.tinder.entity.Follower;
import com.semester.tinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFollowerRepo extends JpaRepository<Follower, Integer> {

    @Query("SELECT f from follower as f where f.implementer.id = :id1 AND f.affected_person.id = :id2")
    Optional<Follower> findIWasLikedYet(
            @Param("id1") int id1,
            @Param("id2") int id2 );

    @Query("  select f.affected_person.id from follower as f where f.implementer.id = :idUser AND f.status = 'Matching' ")
    List<Integer> findUsers1(@Param("idUser") int idUser);

    @Query(" select f.implementer.id from follower as f where f.affected_person.id = :idUser AND f.status = 'Matching'")
    List<Integer> findUsers2(@Param("idUser") int idUser);

    @Query("select f from follower as f where f.implementer.id = :idUser1 AND f.affected_person.id = :idUser2 AND f.status = 'Matching' OR f.implementer.id = :idUser2 AND f.affected_person.id = :idUser1 AND f.status = 'Matching' ")
    Optional<Follower> findUserFollow(@Param("idUser1") int idUser1, @Param("idUser2") int idUser2);

    @Query("select f from follower as f where f.implementer.id = :idUser1 AND f.affected_person.id = :idUser2 OR f.implementer.id = :idUser2 AND f.affected_person.id = :idUser1")
    Optional<Follower> findUserIsExist(@Param("idUser1") int idUser1, @Param("idUser2") int idUser2);

    @Query("select f.implementer.id from follower as f where f.affected_person.id = :idU AND f.status = 'Like'")
    List<Integer> findLiked(@Param("idU") int idU);

}
