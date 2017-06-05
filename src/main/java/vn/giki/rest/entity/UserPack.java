package vn.giki.rest.entity;

import java.util.Date;

public class UserPack {
	private Date createAt;
	private int status;
	private String packageId;
	private int userId;
	public UserPack(Date createAt, int status, String packageId, int userId) {
		super();
		this.createAt = createAt;
		this.status = status;
		this.packageId = packageId;
		this.userId = userId;
	}
	public UserPack() {
		super();
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
