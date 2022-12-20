package com.aa.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.aa.domain.Address;
import com.aa.domain.Employee;
import com.aa.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class EmployeeRestControllerTest {

	private static final String URI = "/api/v1/employees";

	@MockBean
	private EmployeeService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Employee employee_1;
	private Employee employee_2;
	
	@BeforeEach
	void beforeEach() {
		Address address = new Address("City", "State", "Country", "Postal Code", "0000000000");
		
		employee_1 = new Employee("Employee_1", "Employee_1", "employee1@yahoo.com", address);
		employee_2 = new Employee("Employee_2", "Employee_2", "employee2@yahoo.com", address);
	}
	
	@Test
	void listAll() throws Exception {
		when(service.findAll()).thenReturn(List.of(employee_1, employee_2));
		
		mockMvc.perform(get(URI))
			.andExpect(status().isOk());
	}
	
	@Test
	void getEmployee() throws Exception {
		Integer id = 1;
		
		when(service.get(anyInt())).thenReturn(employee_1);
		
		mockMvc.perform(get(URI + "/" + id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(employee_1.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee_1.getLastName())));
	}
	
	@Test
	void add() throws JsonProcessingException, Exception {
		when(service.save(any(Employee.class))).thenReturn(employee_1);
		
		mockMvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee_1)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName", is(employee_1.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee_1.getLastName())));
	}
	
	@Test
	void update() throws JsonProcessingException, Exception {
		employee_1.setId(1);
		
		when(service.save(any(Employee.class))).thenReturn(employee_1);
		
		mockMvc.perform(put(URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee_1)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(employee_1.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee_1.getLastName())));
	}
	
	@Test
	void deleteUser() throws Exception {
		doNothing().when(service).delete(anyInt());
		
		this.mockMvc.perform(delete(URI + "/{id}", 1))
			.andExpect(status().isNoContent());
	}
}
