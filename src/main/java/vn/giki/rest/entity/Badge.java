package vn.giki.rest.entity;

public class Badge {
	private String type;
	private String name;
	private String picURL;
	private int get_level;
	public Badge(String type, String name, String picURL) {
		super();
		this.type = type;
		this.name = name;
		this.picURL = picURL;
	}
	public Badge() {
		super();
	}
	
	public Badge(String type, int get_level) {
		super();
		this.type = type;
		this.get_level = get_level;
	}
	public int getGet_level() {
		return get_level;
	}
	public void setGet_level(int get_level) {
		this.get_level = get_level;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
