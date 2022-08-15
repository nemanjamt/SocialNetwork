package beans;

public class PhotoRequest {

	private String content;
	private String text;
	private String username;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PhotoRequest(String content) {
		super();
		this.content = content;
	}

	public PhotoRequest() {
		super();
	}

	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "PhotoRequest [content=" + content + "]";
	}
}
