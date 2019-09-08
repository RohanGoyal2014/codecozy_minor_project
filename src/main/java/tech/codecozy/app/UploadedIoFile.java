package tech.codecozy.app;

import java.io.InputStream;

public class UploadedIoFile {
	
	private String name;
	private InputStream content;
	
	public UploadedIoFile(String name, InputStream content) {
		super();
		this.name = name;
		this.content = content;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}

	
	
}
