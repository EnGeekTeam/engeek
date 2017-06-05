package vn.giki.rest.entity;

import java.util.Date;

public class UserWord {
	private Date createAt;
	private Date interactedTime;
	private int ranking;
	private int totalNumberOfReview;
	private int totalNumberOfWrong;
	private int userId;
	private String wordId;
	public UserWord(Date createAt, Date interactedTime, int ranking, int totalNumberOfReview, int totalNumberOfWrong,
			int userId, String wordId) {
		super();
		this.createAt = createAt;
		this.interactedTime = interactedTime;
		this.ranking = ranking;
		this.totalNumberOfReview = totalNumberOfReview;
		this.totalNumberOfWrong = totalNumberOfWrong;
		this.userId = userId;
		this.wordId = wordId;
	}
	public UserWord() {
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
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public int getTotalNumberOfReview() {
		return totalNumberOfReview;
	}
	public void setTotalNumberOfReview(int totalNumberOfReview) {
		this.totalNumberOfReview = totalNumberOfReview;
	}
	public int getTotalNumberOfWrong() {
		return totalNumberOfWrong;
	}
	public void setTotalNumberOfWrong(int totalNumberOfWrong) {
		this.totalNumberOfWrong = totalNumberOfWrong;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getWordId() {
		return wordId;
	}
	public void setWordId(String wordId) {
		this.wordId = wordId;
	}
	
	
}
