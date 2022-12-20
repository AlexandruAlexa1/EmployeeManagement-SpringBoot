package com.aa.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.domain.Employee;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.EmployeeNotFoundException;
import com.aa.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repo;
	
	public List<Employee> findAll() {
		return repo.findAll();
	}
	
	public Employee get(Integer id) throws EmployeeNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EmployeeNotFoundException("Could not find any Employee with ID: " + id);
		}
	}
	
	public Employee save(Employee employee) throws DuplicateEmailException {
		boolean isEditMode = (employee.getId() != null);
		
		if (isEditMode) {
			Employee employeeInDB = repo.findById(employee.getId()).get();
			
			checkEmail(employeeInDB, employee);
		} else {
			checkDuplicateEmail(employee.getEmail());
		}
		
		return repo.save(employee);
	}
	
	private void checkEmail(Employee employeeInDB, Employee employee) throws DuplicateEmailException {
		boolean isTheSameEmail = (employeeInDB.getEmail().contentEquals(employee.getEmail()));
		
		if (isTheSameEmail) {
			employee.setEmail(employeeInDB.getEmail());
		} else {
			checkDuplicateEmail(employee.getEmail());
		}
	}

	private void checkDuplicateEmail(String email) throws DuplicateEmailException {
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
