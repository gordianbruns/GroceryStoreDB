package edu.depauw.csc480.projectv4.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WORKER")
public class Worker {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "WId")
	private Long wId;
	
	@Basic(optional = false)
	@Column(name = "FName", length = 20)
	private String fName;
	
	@Basic(optional = false)
	@Column(name = "LName", length = 20)
	private String lName;
	
	@Basic(optional = false)
	@Column(name = "email", length = 30)
	private String email;
	
	@Basic(optional = false)
	@Column(name = "birthdate")
	private LocalDate birthDate;
	
	@Basic(optional = false)
	@Column(name = "salary")
	private double salary;
	
	@Basic(optional = true)
	@Column(name = "phone", length = 15)
	private String phone;
	
	@OneToMany(mappedBy = "worker")
	private Collection<RestockOrder> restockOrders;
	
	protected Worker() {
		// No-argument constructor for JPA
	}
	
	public Worker(String fName, String lName, String email, LocalDate birthDate,
			double salary) {
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.birthDate = birthDate;
		this.salary = salary;
	}
	
	public Worker(String fName, String lName, String email, LocalDate birthDate,
			double salary, String phone) {
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.birthDate = birthDate;
		this.salary = salary;
		this.phone = phone;
	}
	
	public Long getWId() {
		return wId;
	}
	
	public String getFName() {
		return fName;
	}
	
	public String getLName() {
		return lName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public double getSalary() {
		return salary;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public Collection<RestockOrder> getRestockOrders() {
		return restockOrders;
	}

}
