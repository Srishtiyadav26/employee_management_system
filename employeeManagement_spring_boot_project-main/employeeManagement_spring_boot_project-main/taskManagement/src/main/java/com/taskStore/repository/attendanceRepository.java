package com.taskStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskStore.entity.Attendance;

public interface attendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByOrderByLoginTimeDesc();
}
