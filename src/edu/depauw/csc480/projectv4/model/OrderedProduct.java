package edu.depauw.csc480.projectv4.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERED_PRODUCT")
public class OrderedProduct {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "OPId")
	private Long opId;
	
	@Basic(optional = false)
	@Column(name = "quantity")
	private int quantity;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "RestockOrderId")
	private RestockOrder restockOrder;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ProductId")
	private Product product;
	
	protected OrderedProduct() {
		// No-argument constructor for JPA
	}
	
	public OrderedProduct(int quantity, RestockOrder restockOrder, Product product) {
		this.quantity = quantity;
		this.restockOrder = restockOrder;
		this.product = product;
	}
	
	public Long getOpId() {
		return opId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public RestockOrder getRestockOrder() {
		return restockOrder;
	}
	
	public Product getProduct() {
		return product;
	}

}
