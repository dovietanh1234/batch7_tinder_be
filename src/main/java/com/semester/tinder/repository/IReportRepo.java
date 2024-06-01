package com.semester.tinder.repository;

import com.semester.tinder.entity.Report;
import com.semester.tinder.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReportRepo extends JpaRepository<Report, Integer> {
    Optional<Report> findById(int id);

    @Query(" SELECT r FROM report as r where r.user_report.id = :id")
    List<Report> findAllMyReport(@Param("id") int id);

    Page<Report> findAll(Pageable pageable);

}