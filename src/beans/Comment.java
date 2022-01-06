package beans;

import java.time.LocalDateTime;

public class Comment {
	private String content;
	private LocalDateTime editDate;
	private boolean edited;
	private LocalDateTime publishedDate;
	private Long id;
	private String usernameCreator;
	private boolean deleted;
	private Long postId;
	public Comment() {
		super();
	}
	
	
	
	public Comment(String content, LocalDateTime editDate, boolean edited, LocalDateTime publishedDate, 
			String usernameCreator, boolean deleted, Long postId) {
		super();
		this.content = content;
		this.editDate = editDate;
		this.edited = edited;
		this.publishedDate = publishedDate;
	
		this.usernameCreator = usernameCreator;
		this.deleted = deleted;
		this.postId = postId;
	}



	
	
	
	public boolean isDeleted() {
		return deleted;
	}

	
	

	public Long getPostId() {
		return postId;
	}


	public void setPostId(Long postId) {
		this.postId = postId;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getEditDate() {
		return editDate;
	}
	public void setEditDate(LocalDateTime editDate) {
		this.editDate = editDate;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public LocalDateTime getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(LocalDateTime publishedDate) {
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


	@Override
	public String toString() {
		return "Comment [content=" + content + ", editDate=" + editDate + ", edited=" + edited + ", publishedDate="
				+ publishedDate + ", id=" + id + ", usernameCreator=" + usernameCreator + ", deleted=" + deleted
				+ ", postId=" + postId + "]";
	}
	
	
	
	
	

}
