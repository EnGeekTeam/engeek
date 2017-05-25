package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Word implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String meaning;
	private String wordLink;
	private String phonetics;
	private String audioPath;
	private String example;
	private String sysnonym;
	private String antonym;
	private int absoluteFrequency;
	private String description;
	private Date createdAt;

	public Word(String name, String meaning, String wordLink, String phonetics, String audioPath, String example,
			String sysnonym, String antonym, int absoluteFrequency, String description, Date createdAt) {
		this.name = name;
		this.meaning = meaning;
		this.wordLink = wordLink;
		this.phonetics = phonetics;
		this.audioPath = audioPath;
		this.example = example;
		this.sysnonym = sysnonym;
		this.antonym = antonym;
		this.absoluteFrequency = absoluteFrequency;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Word() {
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

	public String getWordLink() {
		return wordLink;
	}

	public void setWordLink(String wordLink) {
		this.wordLink = wordLink;
	}

	public String getPhonetics() {
		return phonetics;
	}

	public void setPhonetics(String phonetics) {
		this.phonetics = phonetics;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getSysnonym() {
		return sysnonym;
	}

	public void setSysnonym(String sysnonym) {
		this.sysnonym = sysnonym;
	}

	public String getAntonym() {
		return antonym;
	}

	public void setAntonym(String antonym) {
		this.antonym = antonym;
	}

	public int getAbsoluteFrequency() {
		return absoluteFrequency;
	}

	public void setAbsoluteFrequency(int absoluteFrequency) {
		this.absoluteFrequency = absoluteFrequency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
