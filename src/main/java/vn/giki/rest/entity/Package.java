package vn.giki.rest.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value = { "decks" })
public class Package implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private int orders;
	private int numberOfRoot;
	private String picturePath;
	private Date createdAt;
	private String description;
	private Set<Deck> decks;

	public Package(String name, int orders, String picturePath, Date createdAt, String description, Set<Deck> decks) {
		super();
		this.name = name;
		this.orders = orders;
		this.picturePath = picturePath;
		this.createdAt = createdAt;
		this.description = description;
		this.decks = decks;
		this.numberOfRoot = decks != null ? decks.size() : 0;
	}

	public Package() {
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

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public int getNumberOfRoot() {
		return numberOfRoot;
	}

	public void setNumberOfRoot(int numberOfRoot) {
		this.numberOfRoot = numberOfRoot;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id")
	public Set<Deck> getDecks() {
		return decks;
	}

	public void setDecks(Set<Deck> decks) {
		this.decks = decks;
	}

}
