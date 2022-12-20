package com.aa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aa.domain.Address;
import com.aa.domain.Employee;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.EmployeeNotFoundException;
import com.aa.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@InjectMocks
	private EmployeeService service;
	
	@Mock
	private EmployeeRepository repo;

	private Employee employee_1;
	private Employee employee_2;
	
	@BeforeEach
	void beforeEach() {
		Address address = new Address("City", "State", "Country", "Postal Code", "0000000000");
		
		employee_1 = new Employee("Employee_1", "Employee_1", "employee1@yahoo.com", address);
		employee_2 = new Employee("Employee_2", "Employee_2", "employee2@yahoo.com", address);
	}
	
	@Test
	void findAll() {
		when(repo.findAll()).thenReturn(List.of(employee_1, employee_2));

		List<Employee> listEmployees = service.findAll();
		
		assertNotNull(listEmployees);
		assertEquals(2, listEmployees.size());
	}
	
	@Test
	void get() throws EmployeeNotFoundException {
		when(repo.findById(anyInt())).thenReturn(Optional.of(employee_1));
		
		Employee findedEmployee = service.get(1);
		
		assertNotNull(findedEmployee);
		assertEquals(employee_1.getFirstName(), findedEmployee.getFirstName());
	}
	
	@Test
	void getForException() {
		when(repo.findById(1)).thenReturn(Optional.of(employee_1));
		
		assertThrows(Exception.class, () -> {
			service.get(10);
		});
	}
	
	@Test
	void save() throws DuplicateEmailException {
		when(repo.save(any(Employee.class))).thenReturn(employee_1);
		
		Employee savedEmployee = service.save(employee_1);
		
		assertNotNull(savedEmployee);
		assertThat(savedEmployee.getId()).isGreaterThan(0);
		assertEquals(employee_1.getAddress(), savedEmployee.getAddress());
	}
	
	@Test 
	void delete() throws EmployeeNotFoundException {
		Integer id = 2;
		
		when(repo.existsById(anyInt())).thenReturn(true);
		
		doNothing().when(repo).deleteById(anyInt());
		
		service.delete(id);
		
		verify(repo, times(1)).deleteById(id);
	}
}