package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value = { "words" })
public class Deck implements Serializable {
	private static final long serialVersionUID = -2012744927026503313L;
	private String id;
	private String name;
	private String meaning;
	private String memo;
	private boolean isPremium;
	private String picturePath;
	private int numberOfWord;
	private Set<Word> words;

	public Deck() {
	}

	public Deck(String name, String meaning, String memo, boolean isPremium, String picturePath, Set<Word> words) {
		super();
		this.name = name;
		this.meaning = meaning;
		this.memo = memo;
		this.isPremium = isPremium;
		this.picturePath = picturePath;
		this.words = words;
		this.numberOfWord = words == null ? 0 : words.size();
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	@Transient
	public int getNumberOfWord() {
		return numberOfWord;
	}

	public void setNumberOfWord(int numberOfWord) {
		this.numberOfWord = numberOfWord;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "deck_id")
	public Set<Word> getWords() {
		return words;
	}

	public void setWords(Set<Word> words) {
		this.words = words;
	}

}
