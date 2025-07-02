package com.taskStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taskStore.entity.Employee;
import com.taskStore.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    @Query("SELECT l FROM LeaveRequest l JOIN FETCH l.employee")
    List<LeaveRequest> findAllWithEmployees(); 
    List<LeaveRequest> findByEmployee(Employee employee);
}
