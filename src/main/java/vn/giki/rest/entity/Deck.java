package vn.giki.rest.entity;

public class Deck {
	private String id;
	private String meaning;
	private String memo;
	private String name;
	private int numberOfWord;
	private String picturePath;
	private boolean isPremium;
	private String packageId;
	public Deck(String id, String meaning, String memo, String name, int numberOfWord, String picturePath,
			boolean isPremium, String packageId) {
		super();
		this.id = id;
		this.meaning = meaning;
		this.memo = memo;
		this.name = name;
		this.numberOfWord = numberOfWord;
		this.picturePath = picturePath;
		this.isPremium = isPremium;
		this.packageId = packageId;
	}
	public Deck() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOfWord() {
		return numberOfWord;
	}
	public void setNumberOfWord(int numberOfWord) {
		this.numberOfWord = numberOfWord;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public boolean isPremium() {
		return isPremium;
	}
	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
}
