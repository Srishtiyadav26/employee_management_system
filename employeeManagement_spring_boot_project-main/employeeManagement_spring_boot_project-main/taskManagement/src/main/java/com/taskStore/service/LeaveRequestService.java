package com.taskStore.service;

import java.util.List;

import com.taskStore.entity.Employee;
import com.taskStore.entity.LeaveRequest;


public interface LeaveRequestService {
    void submitLeave(LeaveRequest leaveRequest);
    List<LeaveRequest> getAllLeaveRequests();
    List<LeaveRequest> getLeaveRequestsByEmployee(Employee employee);
    void updateLeaveStatus(Long leaveId, String status);
}
