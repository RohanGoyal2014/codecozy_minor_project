package tech.codecozy.app;

public class Problem {
	
	private long id;
	private String name;
	private String link;
	private long contestId;
	
	public Problem(long id, String name, String link, long contestId) {
		super();
		this.id = id;
		this.name = name;
		this.link = link;
		this.contestId = contestId;
	}

	public Problem(String name, String link, long contestId) {
		super();
		this.name = name;
		this.link = link;
		this.contestId = contestId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public long getContestId() {
		return contestId;
	}
	public void setContestId(long contestId) {
		this.contestId = contestId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

}
