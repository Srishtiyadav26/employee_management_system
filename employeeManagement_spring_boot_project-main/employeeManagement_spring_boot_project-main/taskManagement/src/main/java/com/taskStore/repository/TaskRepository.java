package com.taskStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskStore.entity.TaskList;

public interface TaskRepository extends JpaRepository<TaskList, Integer> {
    List<TaskList> findByEmployeeId(Long employeeId);
}
