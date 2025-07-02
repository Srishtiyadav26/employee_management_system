package com.taskStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.taskStore.entity.Employee;
//import com.taskStore.entity.empList;

public interface EmployeeService {
    List<Employee> getAllEmployees();
	void saveEmployee(Employee employee);
	Employee getEmployeeById(long id);
	void deleteEmployeeById(long id);
	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	Optional<Employee> findByEmail(String email);
    void save(Employee employee);
	boolean validateLogin(String email, String rawPassword); 
}
