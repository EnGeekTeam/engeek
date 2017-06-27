package vn.giki.rest.entity;

public class Game1 {
	private int numberFalse;
	private int numberReview;
	private String antonym;
	private String word;
	private long lastReview;
	public Game1(int numberFalse, int numberReview, String antonym, String word, long lastReview) {
		super();
		this.numberFalse = numberFalse;
		this.numberReview = numberReview;
		this.antonym = antonym;
		this.word = word;
		this.lastReview = lastReview;
	}
	public Game1() {
		super();
	}
	public int getNumberFalse() {
		return numberFalse;
	}
	public void setNumberFalse(int numberFalse) {
		this.numberFalse = numberFalse;
	}
	public int getNumberReview() {
		return numberReview;
	}
	public void setNumberReview(int numberReview) {
		this.numberReview = numberReview;
	}
	public String getAntonym() {
		return antonym;
	}
	public void setAntonym(String antonym) {
		this.antonym = antonym;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getLastReview() {
		return lastReview;
	}
	public void setLastReview(long lastReview) {
		this.lastReview = lastReview;
	}
	
	
}
