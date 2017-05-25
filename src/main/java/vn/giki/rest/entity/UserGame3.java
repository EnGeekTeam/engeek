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
public class UserGame3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private User user;
	private Word word;
	private String text;
	private int lastCorrectChar;
	private Date timeReview;
	private int score;
	private boolean isCorrected;
	
	

	public UserGame3() {
		super();
	}

	public UserGame3(User user, Word words, String text, int lastCorrectChar, Date timeReview, int score,
			boolean isCorrected) {
		super();
		this.user = user;
		this.word = words;
		this.text = text;
		this.lastCorrectChar = lastCorrectChar;
		this.timeReview = timeReview;
		this.score = score;
		this.isCorrected = isCorrected;
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

	public void setWord(Word words) {
		this.word = words;
	}

	@Column(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "lastCorrectChar")
	public int getLastCorrectChar() {
		return lastCorrectChar;
	}

	public void setLastCorrectChar(int lastCorrectChar) {
		this.lastCorrectChar = lastCorrectChar;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timeReview")
	public Date getTimeReview() {
		return timeReview;
	}

	public void setTimeReview(Date timeReview) {
		this.timeReview = timeReview;
	}

	@Column(name = "score")
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Column(name = "isCorrected")
	public boolean isCorrected() {
		return isCorrected;
	}

	public void setCorrected(boolean isCorrected) {
		this.isCorrected = isCorrected;
	}

}
