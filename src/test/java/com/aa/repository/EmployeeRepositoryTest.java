package com.aa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.aa.domain.Address;
import com.aa.domain.Employee;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class EmployeeRepositoryTest {
	
	@Autowired
	private EmployeeRepository repo;
	
	@Test
	void save() {
		Address address = new Address("City", "State", "Country", "Postal Code", "0000000000");
		Employee employee = new Employee("First Name", "Last Name", "email@yahoo.com", address);
		
		Employee savedEmployee = repo.save(employee);
		
		assertNotNull(savedEmployee);
		assertThat(savedEmployee.getId()).isGreaterThan(0);
	}

	@Test
	void findAll() {
		List<Employee> listEmployees = repo.findAll();
		
		assertNotNull(listEmployees);
		assertThat(listEmployees.size()).isGreaterThan(0);
		
		listEmployees.forEach(employee -> System.out.println(employee));
	}
	
	@Test
	void get() {
		Integer id = 1;
		
		Optional<Employee> findedEmployee = repo.findById(id);
		
		assertNotNull(findedEmployee);
	}
	
	@Test
	void update() {
		Integer id = 1;
		String firstName = "AA";
		
		Employee findedEmployee = repo.findById(id).get();
		findedEmployee.setFirstName(firstName);
		
		Employee updatedEmployee = repo.save(findedEmployee);
		
		assertEquals(firstName, updatedEmployee.getFirstName());
	}
	
	@Test
	void delete() {
		Integer id = 1;
		
		repo.deleteById(id);
		
		Optional<Employee> findedEmployee = repo.findById(id);
		
		assertThat(!findedEmployee.isPresent());
	}
	
	@Test
	void findByEmail() {
		String email = "employee1@yahoo.com";
		
		Employee findedEmployee = repo.findByEmail(email);
		
		assertNotNull(findedEmployee);
		assertEquals(email, findedEmployee.getEmail());
	}
}



