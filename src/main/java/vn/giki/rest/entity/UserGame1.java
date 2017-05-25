package vn.giki.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class UserGame1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private User user;
	private Word word1, word2, word3, word4;
	private int timeRemain;
	private int score;

	public UserGame1(User user, Word word1, Word word2, Word word3, Word word4, int timeRemain, int score) {
		super();
		this.user = user;
		this.word1 = word1;
		this.word2 = word2;
		this.word3 = word3;
		this.word4 = word4;
		this.timeRemain = timeRemain;
		this.score = score;
	}
	
	

	public UserGame1() {
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
	public Word getWord1() {
		return word1;
	}

	public void setWord1(Word word1) {
		this.word1 = word1;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Word getWord2() {
		return word2;
	}

	public void setWord2(Word word2) {
		this.word2 = word2;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Word getWord3() {
		return word3;
	}

	public void setWord3(Word word3) {
		this.word3 = word3;
	}

	@OneToOne(fetch = FetchType.LAZY)
	public Word getWord4() {
		return word4;
	}

	public void setWord4(Word word4) {
		this.word4 = word4;
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
