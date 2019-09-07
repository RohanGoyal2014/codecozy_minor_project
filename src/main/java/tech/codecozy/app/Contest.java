package tech.codecozy.app;

import java.util.Date;

public class Contest {
	
	private long id;
	private String name;
	private long start;
	private long end;
	private Date startDateTime;
	private Date endDateTime;
	
	public Contest(long id, String name, long start, long end) {
		super();
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;
	}

	public Contest(long id, String name, long start, long end, Date startDateTime, Date endDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	
	
	

}
