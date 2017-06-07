package vn.giki.rest.entity;

import java.util.Date;

public class User {
	private Date created;
	private String facebookId;
	private String googleId;
	private String token;
	private String avatarUrl;
	private Date expiredDate;
	private String gender;
	private int hint;
	private int invitedFriends;
	private String name;
	private  int paymentStatus;
	private Date paymentTime;
	private int type;
	private String tokenClient;
	private String email;
	
	//
	private int id, scoreGame1, scoreGame2, scoreGame3, scoreTotal;
	
	
	public User(Date created, String facebookId, String googleId, String token, String avatarUrl, Date expiredDate,
			String gender, int hint, int invitedFriends, String name, int paymentStatus, Date paymentTime, int type,
			String tokenClient, String email) {
		super();
		this.created = created;
		this.facebookId = facebookId;
		this.googleId = googleId;
		this.token = token;
		this.avatarUrl = avatarUrl;
		this.expiredDate = expiredDate;
		this.gender = gender;
		this.hint = hint;
		this.invitedFriends = invitedFriends;
		this.name = name;
		this.paymentStatus = paymentStatus;
		this.paymentTime = paymentTime;
		this.type = type;
		this.tokenClient = tokenClient;
		this.email = email;
	}
	
	
	public User() {
		super();
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getHint() {
		return hint;
	}
	public void setHint(int hint) {
		this.hint = hint;
	}
	public int getInvitedFriends() {
		return invitedFriends;
	}
	public void setInvitedFriends(int invitedFriends) {
		this.invitedFriends = invitedFriends;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Date getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTokenClient() {
		return tokenClient;
	}
	public void setTokenClient(String tokenClient) {
		this.tokenClient = tokenClient;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getScoreGame1() {
		return scoreGame1;
	}
	public void setScoreGame1(int scoreGame1) {
		this.scoreGame1 = scoreGame1;
	}
	public int getScoreGame2() {
		return scoreGame2;
	}
	public void setScoreGame2(int scoreGame2) {
		this.scoreGame2 = scoreGame2;
	}
	public int getScoreGame3() {
		return scoreGame3;
	}
	public void setScoreGame3(int scoreGame3) {
		this.scoreGame3 = scoreGame3;
	}
	public int getScoreTotal() {
		return scoreTotal;
	}
	public void setScoreTotal(int scoreTotal) {
		this.scoreTotal = scoreTotal;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
}
