package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserWord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private User user;
	private Word word;
	private int totalNumberOfReview;
	private int totalNumberOfWrong;
	private Date createdAt;
	private Date interactedTime;
	private int ranking;

	public UserWord(User user, Word word, int totalNumberOfReview, int totalNumberOfWrong, Date createdAt,
			Date interactedTime, int ranking) {
		super();
		this.user = user;
		this.word = word;
		this.totalNumberOfReview = totalNumberOfReview;
		this.totalNumberOfWrong = totalNumberOfWrong;
		this.createdAt = createdAt;
		this.interactedTime = interactedTime;
		this.ranking = ranking;
	}
	
	

	public UserWord() {
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

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Column(name = "totalNumberOfReview")
	public int getTotalNumberOfReview() {
		return totalNumberOfReview;
	}

	public void setTotalNumberOfReview(int totalNumberOfReview) {
		this.totalNumberOfReview = totalNumberOfReview;
	}

	@Column(name = "totalNumberOfWrong")
	public int getTotalNumberOfWrong() {
		return totalNumberOfWrong;
	}

	public void setTotalNumberOfWrong(int totalNumberOfWrong) {
		this.totalNumberOfWrong = totalNumberOfWrong;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt")
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "interactedTime")
	public Date getInteractedTime() {
		return interactedTime;
	}

	public void setInteractedTime(Date interactedTime) {
		this.interactedTime = interactedTime;
	}

	@Column(name = "ranking")
	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

}
