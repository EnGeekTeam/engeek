package vn.giki.rest.entity;

public class Game2 {
	private String word;
	private String synonym;
	private String choice1;
	private String choice2;
	private String choice3;
	private long lastReview;
	public Game2(String word, String synonym, String choice1, String choice2, String choice3, long lastReview) {
		super();
		this.word = word;
		this.synonym = synonym;
		this.choice1 = choice1;
		this.choice2 = choice2;
		this.choice3 = choice3;
		this.lastReview = lastReview;
	}
	public Game2() {
		super();
	}
	
	public long getLastReview() {
		return lastReview;
	}
	public void setLastReview(long lastReview) {
		this.lastReview = lastReview;
	}
	public String getSynonym() {
		return synonym;
	}
	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getChoice1() {
		return choice1;
	}
	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}
	public String getChoice2() {
		return choice2;
	}
	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}
	public String getChoice3() {
		return choice3;
	}
	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}
	
	

}
