package vn.giki.rest.entity;

public class ProgressInfo {
	private long totalTime;
	private int totalScore;
	private int newWord;
	private String nameLevel;
	public static final int maxScore = 3000000;
	public ProgressInfo(long totalTime, int totalScore, int newWord, String nameLevel) {
		super();
		this.totalTime = totalTime;
		this.totalScore = totalScore;
		this.newWord = newWord;
		this.nameLevel = nameLevel;
	}
	
	public String getNameLevel() {
		return nameLevel;
	}

	public void setNameLevel(String nameLevel) {
		this.nameLevel = nameLevel;
	}

	public ProgressInfo() {
		super();
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getNewWord() {
		return newWord;
	}
	public void setNewWord(int newWord) {
		this.newWord = newWord;
	}
	
	
}
