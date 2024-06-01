package com.semester.tinder.repository;

import com.semester.tinder.entity.Explore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IExploreRepo extends JpaRepository<Explore, Integer> {
    @Transactional // tao mot giao dich bao quanh de giu ket noi mo -> resultSet ms co the tieu thu
    @Procedure(name = "ExploreUsers")
    List<Explore> exploreUsers(@Param("userId") int userId,
                                                                @Param("latitude1") double latitude,
                                                                @Param("longitude1") double longitude,
                                                                @Param("distance_preference1") int distance_preference,
                                                                @Param("looking_for1") String looking_for,
                                                                @Param("min_age_preference1") int min_age_preference,
                                                                @Param("max_age_preference1") int max_age_preference,
                                                                @Param("OFFSET_VAL") int offset
    );

}
