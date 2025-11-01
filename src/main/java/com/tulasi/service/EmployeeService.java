package com.tulasi.service;

import java.util.List;

import com.tulasi.entity.Employee;

public interface EmployeeService {
	
	public Employee addEmployee(Employee emp);
	public Employee findEmployeeById(Integer id);
	public List<Employee> getAllEmployees();
	public Employee updateEmployee(Integer id,Employee emp);
	public void deleteEmployee(Integer id);

}
