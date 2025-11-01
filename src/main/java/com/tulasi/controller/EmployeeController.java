package com.tulasi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tulasi.entity.Employee;
import com.tulasi.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	
	private EmployeeService service;
	
	public EmployeeController(EmployeeService service) {
		this.service=service;
	}

	@PostMapping("/add")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee emp) {
		Employee employee = service.addEmployee(emp);
		if (employee == null) {
			//sonar fix
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		return new ResponseEntity<>(employee, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> findemployeeById(@PathVariable("id") Integer id) {
		Employee emp = service.findEmployeeById(id);
		return new ResponseEntity<>(emp, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Employee>> findAllEmployees() {
		List<Employee> allEmployees = service.getAllEmployees();

		return new ResponseEntity<>(allEmployees, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Integer id, @Valid @RequestBody Employee emp) {
		Employee updateEmployee = service.updateEmployee(id, emp);

		return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Integer id) {
		service.deleteEmployee(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
