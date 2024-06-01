package com.semester.tinder.repository;

import com.semester.tinder.entity.HistoryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryOrderRepo extends JpaRepository<HistoryOrder, Integer> {
}
