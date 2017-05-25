package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class UserProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String gender;
	private String avatarUrl;
	private int hint;
	private int type;
	private int paymentStatus;
	private Date paymentTime;
	private Date expiredDate;
	private int invitedFriends;

	
	
	public UserProfile() {
		super();
	}

	public UserProfile(String name, String gender, String avatarUrl, int hint, int type, int paymentStatus,
			Date paymentTime, Date expiredDate, int invitedFriends) {
		super();
		this.name = name;
		this.gender = gender;
		this.avatarUrl = avatarUrl;
		this.hint = hint;
		this.type = type;
		this.paymentStatus = paymentStatus;
		this.paymentTime = paymentTime;
		this.expiredDate = expiredDate;
		this.invitedFriends = invitedFriends;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "avatarUrl")
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Column(name = "hint")
	public int getHint() {
		return hint;
	}

	public void setHint(int hint) {
		this.hint = hint;
	}

	@Column(name = "type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "paymentStatus")
	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "paymentTime")
	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiredDate")
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	@Column(name = "invitedFriends")
	public int getInvitedFriends() {
		return invitedFriends;
	}

	public void setInvitedFriends(int invitedFriends) {
		this.invitedFriends = invitedFriends;
	}

}
