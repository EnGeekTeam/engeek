package vn.giki.rest.entity;

import java.util.Date;

public class Package {
	private String id;
	private String description;
	private String name;
	private int numberOfRoot;
	private int orders;
	private String picturePath;
	private Date createdAt;
	public Package(String id, String description, String name, int numberOfRoot, int orders, String picturePath,
			Date createdAt) {
		super();
		this.id = id;
		this.description = description;
		this.name = name;
		this.numberOfRoot = numberOfRoot;
		this.orders = orders;
		this.picturePath = picturePath;
		this.createdAt = createdAt;
	}
	public Package() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOfRoot() {
		return numberOfRoot;
	}
	public void setNumberOfRoot(int numberOfRoot) {
		this.numberOfRoot = numberOfRoot;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
