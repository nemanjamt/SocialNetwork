package beans;

public class PostRequest {

	private String username;
	private String imagePath;
	private String text;
	private Long date;
	public PostRequest(String username, String imagePath, String text, Long date) {
		super();
		this.username = username;
		this.imagePath = imagePath;
		this.text = text;
		this.date = date;
	}
	public PostRequest() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "PostRequest [username=" + username + ", imagePath=" + imagePath + ", text=" + text + ", date=" + date
				+ "]";
	}
	
}
