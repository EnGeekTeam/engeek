package vn.giki.rest.entity;

public class Badge {
	private String name;
	private String picURL;
	public Badge(String name, String picURL) {
		super();
		this.name = name;
		this.picURL = picURL;
	}
	public Badge() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	
	
}
