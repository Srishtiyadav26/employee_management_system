package com.taskStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskStore.entity.Employee;
import com.taskStore.entity.LeaveRequest;
import com.taskStore.service.LeaveRequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LeaveRequestController {
    // inside EmployeeController.java (or wherever you handle employee-related routes)

    @Autowired
    private LeaveRequestService leaveRequestService; // inject Service

    @GetMapping("/employee/leave")
    public String showLeaveForm(Model model) {
        model.addAttribute("leaveRequests", new LeaveRequest()); // important!
        return "employeeLeave"; // employeeLeave.html (your page)
    }


    @PostMapping("/submitLeave")
    public String submitLeave(@ModelAttribute LeaveRequest leaveRequests, HttpSession session) {
        // Fetch the logged-in employee from the session
        Employee employee = (Employee) session.getAttribute("employee");

        if (employee != null) {
            // Set the employee to the leave request
            leaveRequests.setEmployee(employee);
            leaveRequestService.submitLeave(leaveRequests); // Save the leave request
        } else {
            // Handle the case where employee is not logged in
            return "redirect:/employee/dashboard"; // Redirect to login page if session expired or no employee in session
        }

        return "redirect:/employee/dashboard"; // Redirect to employee dashboard after submitting leave
    }


    @GetMapping("/admin/leaveRequests")
    public String showAllLeaveRequests(Model model) {
        model.addAttribute("leaveRequests", leaveRequestService.getAllLeaveRequests());
        return "adminLeave"; // your HTML page name
    }

    
    @PostMapping("/admin/updateLeaveStatus")
    public String updateLeaveStatus(@RequestParam("id") Long leaveId,
                                    @RequestParam("status") String status) {
        leaveRequestService.updateLeaveStatus(leaveId, status);
        return "redirect:/admin/leaveRequests";
    }

    @GetMapping("/leaveStatus")
    public String showLeaveStatus(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("employee");
        System.out.println("Session employee: " + employee);  // Add this

        if (employee != null) {
            List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployee(employee);
            model.addAttribute("leaveRequests", leaveRequests);
            return "leaveStatus";
        } else {
            System.out.println("Redirecting because employee not in session.");
            return "redirect:/employee/dashboard";
        }
    }
}
