package tech.codecozy.app;

import java.util.ArrayList;

public class Submission {

	private long id;
	private long problemId;
	private long time;
	private String link;
	private String email;
	private ArrayList<Long> tcId;
	private ArrayList<String> verdict;
	private int score;

	public Submission(long id, long problemId, long time, String link, String email, ArrayList<Long> tcId,
			ArrayList<String> verdict) {
		super();
		this.id = id;
		this.problemId = problemId;
		this.time = time;
		this.link = link;
		this.email = email;
		this.tcId = tcId;
		this.verdict = verdict;
		this.score = computeScore();
	}
	
	private int computeScore() {
		if(verdict == null) return 0;
		int c=0;
		for(String v:verdict) {
			if(v.equals("AC")) ++c; 
		}
		return c*20;
		
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public ArrayList<String> getVerdict() {
		return verdict;
	}

	public void setVerdict(ArrayList<String> verdict) {
		this.verdict = verdict;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProblemId() {
		return problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Long> getTcId() {
		return tcId;
	}

	public void setTcId(ArrayList<Long> tcId) {
		this.tcId = tcId;
	}
	
	
}
