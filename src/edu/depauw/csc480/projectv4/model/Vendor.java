package edu.depauw.csc480.projectv4.model;

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
@Table(name = "VENDOR")
public class Vendor {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "VId")
	private Long vId;
	
	@Basic(optional = false)
	@Column(name = "name", length = 30)
	private String name;
	
	@OneToMany(mappedBy = "vendor")
	private Collection<RestockOrder> restockOrders;
	
	protected Vendor() {
		// No-argument constructor for JPA
	}
	
	public Vendor(String name) {
		this.name = name;
	}
	
	public Long getVId() {
		return vId;
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<RestockOrder> getRestockOrders() {
		return restockOrders;
	}

}
