package com.tulasi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "Name cannot be null or empty")
	private String name;
    @NotBlank
	private String department;
	@Min(value = 1000, message = "Salary must be at least 1000")
	private Double  salary;

	@Column(unique = true)
	private String email;

	private String phno;

}
