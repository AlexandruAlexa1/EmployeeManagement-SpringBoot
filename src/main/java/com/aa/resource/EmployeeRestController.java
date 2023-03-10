package com.aa.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aa.domain.Employee;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.EmployeeNotFoundException;
import com.aa.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeRestController {
	
	@Autowired
	private EmployeeService service;

	@GetMapping
	public ResponseEntity<List<Employee>> listAll() {
		List<Employee> listEmployees = service.findAll();
		
		if (listEmployees.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return new ResponseEntity<>(listEmployees, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee> get(@PathVariable("id") Integer id) throws EmployeeNotFoundException {
		return new ResponseEntity<>(service.get(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Employee> save(@RequestBody @Valid Employee employee) throws DuplicateEmailException {
		Employee savedEmployee = service.save(employee);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmployee.getId()).toUri();
		
		return ResponseEntity.created(location).body(savedEmployee);
	}
	
	@PutMapping
	public ResponseEntity<Employee> update(@RequestBody @Valid Employee employee) throws DuplicateEmailException {
		return new ResponseEntity<>(service.save(employee), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws EmployeeNotFoundException {
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}










