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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserGame2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private User user;
	private Word word;
	private boolean isCorrected;
	private Date timeReview;
	private int timeRemain;
	private int score;

	public UserGame2(User user, Word word, boolean isCorrected, Date timeReview, int timeRemain, int score) {
		super();
		this.user = user;
		this.word = word;
		this.isCorrected = isCorrected;
		this.timeReview = timeReview;
		this.timeRemain = timeRemain;
		this.score = score;
	}
	
	

	public UserGame2() {
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

	@OneToOne(fetch = FetchType.LAZY)
	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Column(name = "isCorrected")
	public boolean isCorrected() {
		return isCorrected;
	}

	public void setCorrected(boolean isCorrected) {
		this.isCorrected = isCorrected;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timeReview")
	public Date getTimeReview() {
		return timeReview;
	}

	public void setTimeReview(Date timeReview) {
		this.timeReview = timeReview;
	}

	@Column(name = "timeRemain")
	public int getTimeRemain() {
		return timeRemain;
	}

	public void setTimeRemain(int timeRemain) {
		this.timeRemain = timeRemain;
	}

	@Column(name = "score")
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
