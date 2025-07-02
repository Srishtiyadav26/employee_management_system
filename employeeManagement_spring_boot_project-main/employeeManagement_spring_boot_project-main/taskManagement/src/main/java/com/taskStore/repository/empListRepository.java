package com.taskStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskStore.entity.Employee;

@Repository
public interface empListRepository extends JpaRepository<Employee,Long>{
    Optional<Employee> findByEmail(String email);
}
