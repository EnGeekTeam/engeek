package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String password;
	private String facebookId;
	private String googleId;
	private String token;
	private String tokenClient;
	private Date created;
	private UserProfile userProfile;


	public User(String userName, String password, String facebookId, String googleId, String token, String tokenClient,
			Date created, UserProfile userProfile) {
		super();
		this.userName = userName;
		this.password = password;
		this.facebookId = facebookId;
		this.googleId = googleId;
		this.token = token;
		this.tokenClient = tokenClient;
		this.created = created;
		this.userProfile = userProfile;
	}



	public User() {
		super();
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "userName")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonIgnore
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "facebookId")
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Column(name = "googleId")
	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Embedded
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@JsonIgnore
	@Column(name = "token")
	@Type(type="text")
	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}


	@Column(name = "tokenClient")
	public String getTokenClient() {
		return tokenClient;
	}



	public void setTokenClient(String tokenClient) {
		this.tokenClient = tokenClient;
	}
	
	
	

}
