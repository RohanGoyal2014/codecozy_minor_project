package tech.codecozy.app;

public class TestCase {
	
	private long tcID;
	private long problemID;
	private String inputPath;
	private String outputPath;
	
	public TestCase(long problemID, String inputPath, String outputPath) {
		super();
		this.problemID = problemID;
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public TestCase(long tcID, long problemID, String inputPath, String outputPath) {
		super();
		this.tcID = tcID;
		this.problemID = problemID;
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}



	public long getTcID() {
		return tcID;
	}



	public void setTcID(long tcID) {
		this.tcID = tcID;
	}



	public long getProblemID() {
		return problemID;
	}

	public void setProblemID(long problemID) {
		this.problemID = problemID;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	

}
