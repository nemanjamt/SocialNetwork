package beans;



public class PostComment {
	private String content;
	private Long editDate;
	private boolean edited;
	private Long publishedDate;
	private Long id;
	private String usernameCreator;
	private boolean deleted;
	private Long postId;
	
	
	
	public PostComment() {
		super();
	}
	public PostComment(String content, Long editDate, boolean edited, Long publishedDate, Long id,
			String usernameCreator, boolean deleted, Long postId) {
		super();
		this.content = content;
		this.editDate = editDate;
		this.edited = edited;
		this.publishedDate = publishedDate;
		this.id = id;
		this.usernameCreator = usernameCreator;
		this.deleted = deleted;
		this.postId = postId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getEditDate() {
		return editDate;
	}
	public void setEditDate(Long editDate) {
		this.editDate = editDate;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public Long getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsernameCreator() {
		return usernameCreator;
	}
	public void setUsernameCreator(String usernameCreator) {
		this.usernameCreator = usernameCreator;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	@Override
	public String toString() {
		return "PostComment [content=" + content + ", editDate=" + editDate + ", edited=" + edited + ", publishedDate="
				+ publishedDate + ", id=" + id + ", usernameCreator=" + usernameCreator + ", deleted=" + deleted
				+ ", postId=" + postId + "]";
	}
	
	
	
	
	
	
	
	

}
