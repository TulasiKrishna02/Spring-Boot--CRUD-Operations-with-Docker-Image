package com.tulasi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import com.tulasi.entity.Employee;
import com.tulasi.exception.EmployeeNotFoundException;
import com.tulasi.repo.EmployeeRepo;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
	@Mock
	private EmployeeRepo repo;
	
	// Inject mocks into the implementation, not the interface
	@InjectMocks
	private EmployeeServiceImpl service;
	
	private Employee e;
	@BeforeEach
	void setup() {
		 e = new Employee(1, "Tulasi", "HR", 6000.0, "tulasi@example.com", "9876543210");
		
	}
	@Test
	void testAddEmployee() {
		when(repo.save(any(Employee.class))).thenReturn(e);
		
		Employee saved = service.addEmployee(e);
		assertNotNull(saved);
		assertEquals("Tulasi", saved.getName());
		verify(repo,times(1)).save(any(Employee.class));
	}
	@Test
	void testAddEmployee_DataIntegrityViolation() {
	    // Arrange
	    when(repo.save(any(Employee.class)))
	        .thenThrow(new DataIntegrityViolationException("email already exists"));

	    // Act + Assert
	    DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {
	        service.addEmployee(e);
	    });

	    assertEquals("Duplicate Employee data(email already exists)", ex.getMessage());
	    verify(repo, times(1)).save(any(Employee.class));
	}

	
	
	
	@Test
	void testUpdateEmployee_sucess() {
		when(repo.findById(1)).thenReturn(Optional.of(e));
		when(repo.save(any(Employee.class))).thenReturn(e);
		
		Employee updated = service.updateEmployee(1, e);
		
		assertEquals(6000.0, updated.getSalary());
		verify(repo,times(1)).findById(1);
		verify(repo,times(1)).save(any(Employee.class));
	}
	@Test
	void testUpdateEmployee_failure() {
		when(repo.findById(1)).thenReturn(Optional.empty());
		int id = 1;
		EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, ()->{
			service.updateEmployee(1, e);
		});
		
		assertEquals(String.format("Employee not found with the id %d",id), ex.getMessage());
		verify(repo,times(1)).findById(1);
		verify(repo,never()).save(any(Employee.class));
		
	}
	@Test
	void testFindEmployeeById_Success(){
		when(repo.findById(1)).thenReturn(Optional.of(e));
		
		Employee result = service.findEmployeeById(1);
		
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Tulasi", result.getName());
		verify(repo,times(1)).findById(1);
	}
	
	@Test
	void testFindEmployeeById_failure() {
		when(repo.findById(1)).thenReturn(Optional.empty());
		
		EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,()->{
			service.findEmployeeById(1);
		});
		
		assertTrue(ex.getMessage().contains("Employee not found with id:1"));
		verify(repo,times(1)).findById(1);
	}
	
	@Test
	void testGetAllEmployees_sucess() {
		List<Employee> list = Arrays.asList(new Employee(1, "Tulasi", "HR", 6000.0, "tulasi@gmail.com", "9999999999"),
				 new Employee(2, "Krishna", "Dev", 8000.0, "krishna@gmail.com", "8888888888"));
			
		
		when(repo.findAll()).thenReturn(list);
		
		List<Employee> result = service.getAllEmployees();
		
		assertEquals(2, result.size());
		verify(repo,times(1)).findAll();
		
		
	}
	@Test
	void testGetAllEmployees_Failure() {
		when(repo.findAll()).thenReturn(Collections.emptyList());
		
		EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,()->{
			service.getAllEmployees();
		});
		assertEquals("No employees found", ex.getMessage());
		verify(repo,times(1)).findAll();
	}
	
	@Test
	void testGetAllEmployees_failure_databaseError() {
	    when(repo.findAll()).thenThrow(new DataAccessResourceFailureException("Database connection failed"));

	    DataAccessResourceFailureException ex = assertThrows(DataAccessResourceFailureException.class, () -> {
	        service.getAllEmployees();
	    });

	    assertTrue(ex.getMessage().contains("Database connection failed"));
	    verify(repo, times(1)).findAll();
	}

	@Test
	void testDeleteEmployee_sucess() {
		when(repo.findById(1)).thenReturn(Optional.of(e));
		
		service.deleteEmployee(1);
		
		verify(repo,times(1)).findById(1);
		verify(repo,times(1)).delete(e);
	}
	@Test
	void testDeleteEmployee_failure() {
		when(repo.findById(1)).thenReturn(Optional.empty());
		
		EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,()->{
			service.deleteEmployee(1);
		});
		
		assertEquals("Employee not found with id1",ex.getMessage());
		verify(repo,times(1)).findById(1);
		verify(repo,never()).delete(any(Employee.class));
	}

	

}
