package com.tulasi.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.tulasi.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

}
