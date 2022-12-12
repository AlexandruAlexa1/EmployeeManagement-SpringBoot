package com.aa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.aa.domain.Address;
import com.aa.domain.Employee;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {
	
	private String baseUrl = "http://localhost";

	@LocalServerPort
	private int port;

	private Employee employee;
	
	private static RestTemplate restTemplate;
	
	@BeforeAll
	static void init() {
		restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	void beforeEach() {
		baseUrl = baseUrl + ":" + port + "/api/v1/employees";
		
		Address address = new Address("City", "State", "Country", "Postal Code", "0000000000");
		employee = new Employee("First Name", "Last Name", "employee@yahoo.com", address);
	}
	
	@Test
	void listAll() {
		ResponseEntity<Page<Employee>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, 
				new ParameterizedTypeReference<Page<Employee>>() {});
		
		Page<Employee> body = response.getBody();
		List<Employee> listEmployees = body.getContent();
		
		assertNotNull(listEmployees);
	}
	
	@Test
	void get() {
		Integer id = 1;
		
		Employee employee = restTemplate.getForObject(baseUrl + "/" + id, Employee.class);
		
		assertNotNull(employee);
	}
	
	@Test
	void save() {
		Employee savedEmployee = restTemplate.postForObject(baseUrl, employee, Employee.class);
		
		assertNotNull(savedEmployee);
		assertThat(savedEmployee.getId()).isGreaterThan(0);
		assertEquals(employee.getFirstName(), savedEmployee.getFirstName());
	}
	
	@Test
	void update() {
		Integer id = 1;
		String firstName = "First Name";
		
		employee.setId(id);
		employee.setFirstName(firstName);
		
		restTemplate.put(baseUrl, employee);
		
		Employee findedEmployee = restTemplate.getForObject(baseUrl + "/" + id, Employee.class);
		
		assertNotNull(findedEmployee);
		assertEquals(firstName, findedEmployee.getFirstName());
	}
	
	@Test
	void delete() {
		Integer id = 1;
		
		restTemplate.delete(baseUrl + "/" + id);
	}
}












