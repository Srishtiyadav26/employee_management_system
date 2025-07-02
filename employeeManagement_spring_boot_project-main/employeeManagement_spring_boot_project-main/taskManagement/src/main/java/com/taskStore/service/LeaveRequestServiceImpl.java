package com.taskStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskStore.entity.Employee;
import com.taskStore.entity.LeaveRequest;
import com.taskStore.repository.LeaveRequestRepository;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Override
    public void submitLeave(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAllWithEmployees();  // This will fetch leave requests with employee details
    }



   @Override
    public List<LeaveRequest> getLeaveRequestsByEmployee(Employee employee) {
        return leaveRequestRepository.findByEmployee(employee);  // Custom query to fetch leave requests by employee
    }

    @Override
    public void updateLeaveStatus(Long leaveId, String status) {
        Optional<LeaveRequest> optionalLeave = leaveRequestRepository.findById(leaveId);
        if (optionalLeave.isPresent()) {
            LeaveRequest leave = optionalLeave.get();
            leave.setStatus(status);
            leaveRequestRepository.save(leave);
        } else {
            throw new RuntimeException("Leave Request not found with ID: " + leaveId);
        }
    }
}