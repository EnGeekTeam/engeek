package vn.giki.rest.entity;

public class BadgeInfo {

	private int currentStreak;
	private long totalTime;
	private int invitedFriend;
	private int learnedWord;
	public BadgeInfo(int currentStreak, long totalTime,
			int invitedFriend, int learnedWord) {
		super();
		this.currentStreak = currentStreak;
		this.totalTime = totalTime;
		this.invitedFriend = invitedFriend;
		this.learnedWord = learnedWord;
	}
	public int getCurrentStreak() {
		return currentStreak;
	}
	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public int getInvitedFriend() {
		return invitedFriend;
	}
	public void setInvitedFriend(int invitedFriend) {
		this.invitedFriend = invitedFriend;
	}
	public int getLearnedWord() {
		return learnedWord;
	}
	public void setLearnedWord(int learnedWord) {
		this.learnedWord = learnedWord;
	}
	public BadgeInfo() {
		super();
	}
	

}
