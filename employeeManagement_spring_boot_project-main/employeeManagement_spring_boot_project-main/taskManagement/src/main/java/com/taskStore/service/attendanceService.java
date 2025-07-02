package com.taskStore.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskStore.entity.Attendance;
import com.taskStore.entity.Employee;
import com.taskStore.repository.attendanceRepository;

@Service
public class attendanceService {

    @Autowired
    private attendanceRepository attendanceRepository;

    public void recordLogin(Employee employee) {
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employee.getId());
        attendance.setEmployeeName(employee.getName());
        attendance.setLoginTime(LocalDateTime.now());
        attendanceRepository.save(attendance);
    }
}

    

