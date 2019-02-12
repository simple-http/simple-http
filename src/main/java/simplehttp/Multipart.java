package simplehttp;

import java.io.File;

public class Multipart implements HttpPost {
	
	private final String name;
	private final File file;

	public Multipart(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void accept(HttpRequestVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public MessageContent getContent() {
		return null;
	}

	@Override
	public Headers getHeaders() {
		return null;
	}
}
