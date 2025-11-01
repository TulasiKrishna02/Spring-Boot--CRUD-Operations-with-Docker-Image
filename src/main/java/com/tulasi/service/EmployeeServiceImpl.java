package com.tulasi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.tulasi.entity.Employee;
import com.tulasi.exception.EmployeeNotFoundException;
import com.tulasi.repo.EmployeeRepo;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepo repo;

	public EmployeeServiceImpl(EmployeeRepo repo) {
		this.repo = repo;
	}

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Override
	public Employee addEmployee(Employee emp) {
		logger.info("Attempting to add new employee: {}", emp.getName());
		try {
			Employee e = new Employee();
			e.setName(emp.getName());
			e.setDepartment(emp.getDepartment());
			e.setSalary(emp.getSalary());
			e.setEmail(emp.getEmail());
			e.setPhno(emp.getPhno());
			Employee saved = repo.save(e);
			logger.info("Employee added sucessfully with ID: {}", saved.getId());
			return saved;
		} catch (DataIntegrityViolationException e) {
			logger.error("Duplicate employee email detected: {}", emp.getEmail());
			throw new DataIntegrityViolationException("Duplicate Employee data(email already exists)");
		}
	}

	@Override
	public Employee findEmployeeById(Integer id) {
		logger.debug("fetching employee with ID: {}", id);
		return repo.findById(id).orElseThrow(() -> {
			logger.warn("Employee not found with ID: {}", id);
			return new EmployeeNotFoundException("Employee not found with id:" + id);
		});
	}

	@Override
	public List<Employee> getAllEmployees() {
		logger.info("fetching all employees");
		List<Employee> list = repo.findAll();
		if (list.isEmpty()) {
			logger.warn("No employees found in the database");
			throw new EmployeeNotFoundException("No employees found");
		}
		logger.debug("Total employees fetched: {}",list.size());
		return list;
	}

	@Override
	public Employee updateEmployee(Integer id, Employee emp) {
		logger.info("updating employee with id: {}", id);
		return repo.findById(id).map(employee -> {
			employee.setName(emp.getName());
			employee.setDepartment(emp.getDepartment());
			employee.setSalary(emp.getSalary());
			employee.setEmail(emp.getEmail());
			employee.setPhno(emp.getPhno());
			Employee updated = repo.save(employee);
			logger.info("Employee updated successfully : {}", updated.getId());
			return updated;
		}).orElseThrow(() -> {
			logger.warn("Employee not found with Id: {}", id);
			return new EmployeeNotFoundException("Employee not found with the id " + id);
		});
	}

	@Override
	public void deleteEmployee(Integer id) {
		logger.info("Deleting employee with Id: {}", id);
		Employee employee = repo.findById(id).orElseThrow(() -> {
			logger.warn("Employee not found with Id: {}", id);
			return new EmployeeNotFoundException("Employee not found with id" + id);
		});

		repo.delete(employee);
		logger.info("Employee deleted sucessfully with ID: {}", id);

	}

}
