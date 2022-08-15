package beans;

public class PhotoCommentRequest {

	private String content;
	private String username;
	private  Long photoId;
	public PhotoCommentRequest(String content, String username, Long photoId) {
		super();
		this.content = content;
		this.username = username;
		this.photoId = photoId;
	}
	public PhotoCommentRequest() {
		super();
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	@Override
	public String toString() {
		return "PhotoCommentRequest [content=" + content + ", username=" + username + ", photoId=" + photoId + "]";
	}
	
}
