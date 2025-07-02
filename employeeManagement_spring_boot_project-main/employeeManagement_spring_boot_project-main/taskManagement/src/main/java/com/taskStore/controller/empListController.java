package com.taskStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskStore.entity.Admin;
import com.taskStore.entity.Employee;
import com.taskStore.repository.AdminRepository;
import com.taskStore.service.EmployeeServiceImpl;

import jakarta.servlet.http.HttpSession;



@Controller
public class empListController {
	
	@Autowired
	private EmployeeServiceImpl  employeeService;

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;  // Inject PasswordEncoder

	@Autowired
    private EmployeeServiceImpl employeeServ;

	/* @Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService; */

	
	@GetMapping("/admin_login")
	public String adminLogin() {
		return "adminLogin";
	}

	
	@PostMapping("/adminLogin")
	public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
		Admin admin = adminRepo.findByUsernameAndPassword(username, password);
		if (admin != null) {
			return "adminDashboard";
		} 
		else {
			model.addAttribute("error", "Invalid credentials");
			return "adminLogin";
		}
	}
	//geeting all employee detail
	@GetMapping("/employees")
	public String viewHomePage(Model model) {
		return findPaginated(1, "name", "asc", model);		
		//return "employees";
	}
	
	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}
	
	
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		// Encrypt the password before saving
		String encryptedPassword = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(encryptedPassword);  // Set the encrypted password
		
		// Save employee to the database
		employeeService.saveEmployee(employee);
		
		return "redirect:/employees";  // Redirect to the list of employees (or another appropriate page)
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		// get employee from the service
		Employee employee = employeeService.getEmployeeById(id);
		
		// set employee as a model attribute to pre-populate the form
		model.addAttribute("employee", employee);
		return "update_employee";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value = "id") long id) {
		
		// call delete employee method 
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/employees";
	}
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		//model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listEmployees", listEmployees);
		return "employees";
	}

	


	
	@GetMapping("/employee/dashboard")
	public String showDashboard(){
		return "employee-dashboard";
	}
	/* @GetMapping("/employee/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");

        List<TaskList> allTasks = taskService.getTasksByEmployee(employee);
        List<TaskList> completed = taskService.getCompletedTasks(employee);
        List<TaskList> pending = taskService.getPendingOrInProgressTasks(employee);

        model.addAttribute("employeeName", employee.getName());
        model.addAttribute("totalTasks", allTasks.size());
        model.addAttribute("completedCount", completed.size());
        model.addAttribute("pendingCount", pending.size());

        return "employeeDashboard";
    }

    @GetMapping("/employee/tasks/completed")
    public String viewCompletedTasks(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        List<TaskList> completed = taskService.getCompletedTasks(employee);

        model.addAttribute("tasks", completed);
        model.addAttribute("viewType", "Completed");
        return "employeeTaskList"; // Create this view
    }

    @GetMapping("/employee/tasks/pending")
    public String viewPendingTasks(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("loggedInEmployee");
        List<TaskList> pending = taskService.getPendingOrInProgressTasks(employee);

        model.addAttribute("tasks", pending);
        model.addAttribute("viewType", "Pending/In Progress");
        return "employeeTaskList"; // Create this view
    }

	
 */	@GetMapping("/employee/self-service")
	public String showSelfServicePortal(HttpSession session, Model model) {
		Employee employee = (Employee) session.getAttribute("employee");

		if (employee == null) {
			System.out.println("No employee found in session. Redirecting...");
			return "redirect:/employee/dashboard"; // redirect to login if not logged in
		}

		model.addAttribute("employee", employee);
		//System.out.println("Session employee: " + session.getAttribute("employee"));

		return "selfServicePortal";
	}
	@PostMapping("employee/profile/update")
    public String updateProfile(@ModelAttribute("employee") Employee employee,
                                HttpSession session) {

        // Get the employee from session
        Employee emp = (Employee) session.getAttribute("employee");

        if (emp == null) {
            // Session expired or not set, redirect to login
            return "redirect:/employee/dashboard";
        }

        // Update editable fields
        emp.setPhoneNo(employee.getPhoneNo());
        emp.setGender(employee.getGender());
        emp.setMaritalStatus(employee.getMaritalStatus());

        emp.setCity(employee.getCity());
        emp.setState(employee.getState());
        emp.setZipCode(employee.getZipCode());
        emp.setCountry(employee.getCountry());
        emp.setAlternateEmail(employee.getAlternateEmail());

        emp.setEmergencyContactName(employee.getEmergencyContactName());
        emp.setEmergencyContactRelation(employee.getEmergencyContactRelation());
        emp.setEmergencyContactPhone(employee.getEmergencyContactPhone());
        emp.setEmergencyContactEmail(employee.getEmergencyContactEmail());

        // Save updated data in DB
        employeeServ.save(emp);

        // Update session with new employee object
        session.setAttribute("employee", emp);

        return "selfServicePortal";
    }
}