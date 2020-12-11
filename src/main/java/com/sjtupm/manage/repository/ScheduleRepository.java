package com.sjtupm.manage.repository;

import com.sjtupm.manage.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByDocterNameEquals(String docterName);
}
