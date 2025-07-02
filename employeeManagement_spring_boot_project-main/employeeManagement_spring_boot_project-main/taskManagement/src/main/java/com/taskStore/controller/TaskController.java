package com.taskStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskStore.entity.Employee;
import com.taskStore.entity.TaskList;
import com.taskStore.service.TaskService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TaskController {

    @Autowired
    private TaskService service;

    /* @Autowired
    private TaskRepository taskRepository; */

    @GetMapping("/home1")
	public String home1() {
		return "home1";

	}

    // Show the task form to add a new task
    @GetMapping("/new_Task")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new TaskList());  // Initialize a new task object
        return "taskRegister"; // This is the name of the HTML form for creating a task
    }

    // Show the task list
    @GetMapping("/task_List")
    public String viewTaskList(Model model) {
        // Fetch the list of tasks from the service
        model.addAttribute("tasks", service.getAllTasks());
        return "taskList"; // Name of the Thymeleaf HTML page
    }

    // Save a new task
    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute TaskList task) {
        service.saveTask(task);  // Call the service method to save the task
        return "redirect:/task_List";  // Redirect to the task list page after saving
    }

	@GetMapping("/updateTask/{taskId}")
    public String showUpdateForm(@PathVariable Integer taskId, Model model) {
        // Fetch task by ID using service
        TaskList task = service.getTaskById(taskId);
        model.addAttribute("task", task);  // Pass the task to the view
        return "taskEdit";  // The view where the task will be updated
    }

    // Handle the form submission and update the task
    @PostMapping("/editTask")
    public String editTask(@ModelAttribute TaskList task) {
    service.saveTask(task);  // Save or update the task
    return "redirect:/task_List";  // Redirect to the task list after updating
    }

    @GetMapping("/addEmpList/{taskId}")
    public String showAssignEmployeeForm(@PathVariable Integer taskId, Model model) {
        model.addAttribute("taskId", taskId); // send taskId to the form
        return "assignTask"; // this will be the new HTML page
    }

    // Handle task assignment
    @PostMapping("/assignTask/{taskId}")
    public String assignTask(@PathVariable Integer taskId, Long employeeId) {
        service.assignTaskToEmployee(taskId, employeeId);
        return "redirect:/empList/" + employeeId;  // Redirect back to task list after assigning
    }

    // Show tasks assigned to a specific employee
    @GetMapping("/empList/{employeeId}")
    public String viewEmployeeTasks(@PathVariable Long employeeId, Model model) {
    // Fetch employee tasks using the employeeId
        List<TaskList> tasks = service.getTasksByEmployeeId(employeeId);  // Fetch tasks assigned to the employee
        
        model.addAttribute("tasks", tasks);  // Add the list of tasks to the model
        return "empList";  // Return the view (empList.html) to show employee's tasks
    }

    @PostMapping("/employee/updateTaskStatus/{taskId}")
    public String updateTaskStatus(@PathVariable Integer taskId, @RequestParam String status, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/employee/dashboard"; // Redirect if not logged in
        }

        TaskList task = service.getTaskById(taskId);
        
        if (task.getEmployee().getId() != employee.getId()) {
            return "redirect:/employee/tasks"; // Prevent other employees from updating
        }

        task.setStatus(status); // Update status
        service.saveTask(task); // Save task back to database

        return "redirect:/employee/tasks"; // Redirect to the tasks page
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) {
        service.deleteTaskById(taskId);
        return "redirect:/task_List"; // Redirect back to task list page
    }
    
    @GetMapping("/emp_list")
    public String employeeList(Model model){
        List<TaskList> tasks = service.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "empList";
    }

    @GetMapping("/employee/tasks")
    public String showEmployeeTasks(HttpSession session, Model model) {
        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            System.out.println("No employee found in session. Redirecting...");
            return "redirect:/employee/dashboard";  // Redirect to login if not logged in
        }

        // Log the employee info to make sure it's in the session
        System.out.println("Employee found in session: " + employee.getName());

        // Fetch the tasks assigned to the employee
        List<TaskList> tasks = service.getTasksByEmployeeId(employee.getId());

        // Log the tasks to debug
        System.out.println("Fetched tasks: " + tasks);

        model.addAttribute("employee", employee);
        model.addAttribute("tasks", tasks);  // Pass tasks to the view

        return "employeeTasks"; // Return the Thymeleaf template for the tasks page
    }
    

}
