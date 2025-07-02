package com.taskStore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskStore.entity.Employee;
import com.taskStore.entity.TaskList;
import com.taskStore.repository.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private EmployeeService employeeService;
    
    // Save task to the database
    public TaskList saveTask(TaskList task) {
        return taskRepo.save(task); // Auto-generation of taskId will be handled by the database
    }

    // Fetch all tasks
    public List<TaskList> getAllTasks() {
        return taskRepo.findAll(); // Assuming you want to display all tasks
    }
	
    public TaskList getTaskById(Integer taskId) {
        return taskRepo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public TaskList assignTaskToEmployee(Integer taskId, Long id) {
        TaskList task = getTaskById(taskId);
        Employee employee = employeeService.getEmployeeById(id);  // Fetch employee by ID
        task.setEmployee(employee);  // Assign employee to the task
        return taskRepo.save(task);  // Save the updated task
    }

    public List<TaskList> getTasksByEmployeeId(Long employeeId) {
        //return taskRepo.findByEmployeeId(employeeId);
        List<TaskList> tasks = taskRepo.findByEmployeeId(employeeId);
        System.out.println("Fetched tasks for employeeId " + employeeId + ": " + tasks);
        return tasks;
    }
    
    public void deleteTaskById(Integer taskId) {
        taskRepo.deleteById(taskId);
    }

    /* public List<TaskList> getTasksByEmployee(Employee employee) {
        return taskRepo.findByEmployee(employee);
    } */

    /* public List<TaskList> getCompletedTasks(Employee employee) {
        return taskRepo.findByEmployeeAndStatus(employee, "Completed");
    }

    public List<TaskList> getPendingOrInProgressTasks(Employee employee) {
        return taskRepo.findByEmployeeAndStatusIn(employee, Arrays.asList("Pending", "In Progress"));
    } */
} 
