package com.taskStore.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskStore.entity.Attendance;
import com.taskStore.entity.Employee;
import com.taskStore.repository.attendanceRepository;
import com.taskStore.service.EmployeeServiceImpl;

import jakarta.servlet.http.HttpSession;


@Controller
public class attendanceController {

    

	@Autowired
	private attendanceRepository attendanceRepo;

    
    @Autowired
    private EmployeeServiceImpl employeeServ;

    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @GetMapping("/")
	public String home() {
		return "home";
	}

    @GetMapping("/emp_login")
    public String empLogin() {
        return "login";
    }

    @PostMapping("/employeeLogin")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<Employee> optionalEmployee = employeeServ.findByEmail(email);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            if (passwordEncoder.matches(password, employee.getPassword())) {
                session.setAttribute("employee", employee);
                session.setMaxInactiveInterval(1800); // 30 minutes

                // <-- Save login time */
                Attendance attendance = new Attendance();
                attendance.setEmployeeId(employee.getId());
                attendance.setEmployeeName(employee.getName());
                attendance.setLoginTime(LocalDateTime.now());
                attendanceRepo.save(attendance);

                System.out.println("Employee logged in: " + employee.getEmail());

                return "redirect:/employee/dashboard";
            }
        }

        model.addAttribute("error", "Invalid credentials");
        return "employeeLogin";
    }

    @GetMapping("/admin/attendance")
    public String viewAttendance(Model model) {
        List<Attendance> attendanceList = attendanceRepo.findAllByOrderByLoginTimeDesc();
        model.addAttribute("attendanceList", attendanceList);
        return "attendance-list";
    }
}