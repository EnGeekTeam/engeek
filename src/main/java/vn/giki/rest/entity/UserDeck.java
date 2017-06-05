package vn.giki.rest.entity;

import java.util.Date;

public class UserDeck {
	private Date createAt;
	private Date interactedTime;
	private int status;
	private String deckId;
	private int userId;
	public UserDeck(Date createAt, Date interactedTime, int status, String deckId, int userId) {
		super();
		this.createAt = createAt;
		this.interactedTime = interactedTime;
		this.status = status;
		this.deckId = deckId;
		this.userId = userId;
	}
	public UserDeck() {
		super();
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getInteractedTime() {
		return interactedTime;
	}
	public void setInteractedTime(Date interactedTime) {
		this.interactedTime = interactedTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDeckId() {
		return deckId;
	}
	public void setDeckId(String deckId) {
		this.deckId = deckId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
