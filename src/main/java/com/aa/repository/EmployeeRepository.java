package com.aa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aa.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
