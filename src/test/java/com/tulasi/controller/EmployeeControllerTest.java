package com.tulasi.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulasi.entity.Employee;
import com.tulasi.exception.EmployeeNotFoundException;
import com.tulasi.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	
	@MockitoBean
	private EmployeeService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testAddEmployee() throws Exception {
		Employee e = new Employee();
		e.setId(1);
		e.setName("tulasi");
		e.setSalary(5000.00);
		when(service.addEmployee(any(Employee.class))).thenReturn(e);

		mockMvc.perform(
				post("/api/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(e)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("tulasi"))
				.andExpect(jsonPath("$.salary").value(5000.00));

		verify(service, times(1)).addEmployee(any(Employee.class));

	}

	@Test
	void testAddEmployeeFailure() throws Exception {
		Employee e = new Employee();
		e.setId(1);
		e.setName("tulasi");
		e.setSalary(5000.00);
		when(service.addEmployee(any(Employee.class))).thenReturn(null);

		mockMvc.perform(
				post("/api/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(e)))
				.andExpect(status().isBadRequest());
		verify(service, times(1)).addEmployee(any(Employee.class));
	}

	@Test
	void testupdateEmployee() throws Exception {
		Employee e = new Employee();
		e.setId(1);
		e.setName("tulasi");
		e.setDepartment("HR");
		e.setSalary(6000.00);
		e.setEmail("tulasi@example.com");
		e.setPhno("9876543210");
		when(service.updateEmployee(eq(1), any(Employee.class))).thenReturn(e);

		mockMvc.perform(
				put("/api/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(e)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("tulasi"))
				.andExpect(jsonPath("$.salary").value(6000.0));

		verify(service, times(1)).updateEmployee(eq(1), any(Employee.class));
	}

	// employee not found
	@Test
	void testUpdateEmployeeNotFound() throws Exception {
		Employee e = new Employee(1, "tulasi", "HR", 6000.00, "tulasi@example.com", "9876543210");

		when(service.updateEmployee(eq(99), any(Employee.class)))
				.thenThrow(new EmployeeNotFoundException("Employee not found"));

		MvcResult result = mockMvc.perform(
				put("/api/99").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(e)))
				.andExpect(status().isNotFound()).andReturn();

	    // Assert response message content
	    String response = result.getResponse().getContentAsString();
	    assertTrue(response.contains("Employee not found"));
	}

	// invalid pathvariable type
	@Test
	void testUpdateEmployeeInvalidType() throws Exception {
		mockMvc.perform(put("/api/abc").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new Employee(1, "tulasi", "HR", 5000.00, "tulasi@gmail.com", "5847968574"))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateEmployeeNullFields() throws Exception {
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("tulasi");
		emp.setSalary(6000.00);
		emp.setEmail("tulasi@example.com");
		emp.setPhno("9876543210");

		mockMvc.perform(
				put("/api/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emp)))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testfindEmployeeById() throws Exception {

		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("tulasi");
		emp.setSalary(6000.00);
		emp.setEmail("tulasi@example.com");
		emp.setPhno("9876543210");

		when(service.findEmployeeById(1)).thenReturn(emp);

		mockMvc.perform(get("/api/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("tulasi")).andExpect(jsonPath("$.salary").value(6000.0));

		verify(service, times(1)).findEmployeeById(1);

	}

	@Test
	void testfindEmployeeById_failure() throws Exception {

		when(service.findEmployeeById(99)).thenThrow(new EmployeeNotFoundException("Employee not found"));

		mockMvc.perform(get("/api/99").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Employee not found"));

		verify(service, times(1)).findEmployeeById(99);
	}

	@Test
	void testFindAllEmployees_success() throws Exception {
		List<Employee> employees = List.of(new Employee(1, "tulasi", "HR", 6000.00, "tulasi@example.com", "9876543210"),
				new Employee(2, "krishna", "IT", 7000.00, "krishna@example.com", "9876543211"));

		when(service.getAllEmployees()).thenReturn(employees);

		mockMvc.perform(get("/api").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("tulasi")).andExpect(jsonPath("$[1].department").value("IT"));

		verify(service, times(1)).getAllEmployees();
	}

	@Test
	void testFindAllEmployees_Failure() throws Exception {
		when(service.getAllEmployees()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("[]"));
	}

	@Test
	void testDeleteEmployee_sucess() throws Exception {
		Integer empId = 1;

		doNothing().when(service).deleteEmployee(empId);

		mockMvc.perform(delete("/api/{id}", empId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		verify(service, times(1)).deleteEmployee(empId);

	}

	@Test
	void testDeleteEmployee_NotFound() throws Exception {
		Integer empId = 99;

		doThrow(new EmployeeNotFoundException("Employee not found")).when(service).deleteEmployee(empId);

		mockMvc.perform(delete("/api/{id}", empId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("Employee not found"));

		verify(service, times(1)).deleteEmployee(empId);

	}

}
