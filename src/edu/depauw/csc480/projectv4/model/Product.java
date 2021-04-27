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
@Table(name = "PRODUCT")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "PId")
	private Long pId;
	
	@Basic(optional = false)
	@Column(name = "name", length = 20)
	private String name;
	
	@Basic(optional = false)
	@Column(name = "company", length = 20)
	private String companyName;
	
	@Basic(optional = false)
	@Column(name = "price")
	private double price;
	
	@Basic(optional = true)
	@Column(name = "description", length = 100)
	private String description;
	
	@Basic(optional = false)
	@Column(name = "stock")
	private int stock;
	
	@OneToMany(mappedBy = "product")
	private Collection<OrderedProduct> orderedProducts;
	
	protected Product() {
		// No-argument constructor for JPA
	}
	
	public Product(String name, String company, double price, int stock) {
		this.name = name;
		this.companyName = company;
		this.price = price;
		this.stock = stock;
	}
	
	public Product(String name, String company, double price, int stock,
			String description) {
		this.name = name;
		this.companyName = company;
		this.price = price;
		this.stock = stock;
		this.description = description;
	}
	
	public Long getPId() {
		return pId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public Collection<OrderedProduct> getOrderedProducts() {
		return orderedProducts;
	}
	
}
