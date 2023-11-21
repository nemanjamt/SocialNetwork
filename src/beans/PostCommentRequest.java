package beans;

public class PostCommentRequest {

	private String content;
	private String username;
	private  Long postId;
	
	public PostCommentRequest() {
		super();
	}
	public PostCommentRequest(String content, String username, Long postId) {
		super();
		this.content = content;
		this.username = username;
		this.postId = postId;
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
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	@Override
	public String toString() {
		return "PostCommentRequest [content=" + content + ", username=" + username + ", postId=" + postId + "]";
	}
	
}
