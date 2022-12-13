package com.aa.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.aa.domain.Employee;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.EmployeeNotFoundException;
import com.aa.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repo;
	
	public Page<Employee> findAll(int pageNum, int pageSize) {
		return repo.findAll(PageRequest.of(pageNum, pageSize));
	}
	
	public Employee get(Integer id) throws EmployeeNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EmployeeNotFoundException("Could not find any Employee with ID: " + id);
		}
	}
	
	public Employee save(Employee employee) throws DuplicateEmailException {
		checkDuplicateEmail(employee);
		
		return repo.save(employee);
	}
	
	private void checkDuplicateEmail(Employee employeeInForm) throws DuplicateEmailException {
		String email = employeeInForm.getEmail();
		Employee employeeInDB = repo.findByEmail(email);
		
		if (employeeInDB != null) {
			throw new DuplicateEmailException("This E-mail: " + email + " already exist. Please choose another E-mail.");
		}
	}

	public void delete(Integer id) throws EmployeeNotFoundException {
		if (!repo.existsById(id)) {
			throw new EmployeeNotFoundException("Could not find any Employee with ID: " + id);
		}
		
		repo.deleteById(id);
	}
}
