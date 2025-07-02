package com.taskStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskStore.entity.Employee;
import com.taskStore.repository.empListRepository;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
	private empListRepository employeeRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public void saveEmployee(Employee employee) {
		this.employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		Employee employee = null;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			throw new RuntimeException(" Employee not found for id :: " + id);
		}
		return employee;
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
	}

	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.employeeRepository.findAll(pageable);
	}

	

	@Override
	public Optional<Employee> findByEmail(String email) {
    return employeeRepository.findByEmail(email); // Return the Optional<Employee> from the repository
	}

	@Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }
	
    public void createEmployee(Employee employee) {
        // Encrypt the password
        String rawPassword = employee.getPassword(); // Admin gives this
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        employee.setPassword(encryptedPassword); // Set encrypted password

        employeeRepository.save(employee);
    }

	@Override
	public boolean validateLogin(String email, String rawPassword) {
		Optional<Employee> optionalEmployee = findByEmail(email);  // Call the service's findByEmail method

		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			String encryptedPasswordFromDB = employee.getPassword();

			return passwordEncoder.matches(rawPassword, encryptedPasswordFromDB);  // Validate the password
		}

		return false;  // Employee not found or invalid login
	}
	public Employee getEmployeeById(Long employeeId) {
    return employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("Employee not found"));
	}
}
