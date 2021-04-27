package edu.depauw.csc480.projectv4.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RESTOCK_ORDER")
public class RestockOrder {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ROId")
	private Long roId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "VendorId")
	private Vendor vendor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "WorkerId")
	private Worker worker;
	
	@OneToMany(mappedBy = "restockOrder")
	private Collection<OrderedProduct> orderedProducts;
	
	protected RestockOrder() {
		// No-argument constructor for JPA
	}
	
	public RestockOrder(Vendor vendor, Worker worker) {
		this.vendor = vendor;
		this.worker = worker;
	}
	
	public Long getRoId() {
		return roId;
	}
	
	public Vendor getVendor() {
		return vendor;
	}
	
	public Worker getWorker() {
		return worker;
	}
	
	public Collection<OrderedProduct> getOrderedProducts() {
		return orderedProducts;
	}

}
