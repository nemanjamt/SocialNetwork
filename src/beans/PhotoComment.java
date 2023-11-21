package beans;

import java.time.LocalDateTime;

public class PhotoComment {

	private String content;
	private LocalDateTime editDate;
	private boolean edited;
	private LocalDateTime publishedDate;
	private Long id;
	private String usernameCreator;
	private boolean deleted;
	private Long photoId;
	private Boolean isEdited = Boolean.FALSE;
	public PhotoComment(String content, LocalDateTime editDate, boolean edited, LocalDateTime publishedDate, Long id,
			String usernameCreator, boolean deleted, Long photoId) {
		super();
		this.content = content;
		this.editDate = editDate;
		this.edited = edited;
		this.publishedDate = publishedDate;
		this.id = id;
		this.usernameCreator = usernameCreator;
		this.deleted = deleted;
		this.photoId = photoId;
		
	}
	
	public Boolean getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(Boolean isEdited) {
		this.isEdited = isEdited;
	}

	public PhotoComment() {
		super();
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
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	@Override
	public String toString() {
		return "PhotoComment [content=" + content + ", editDate=" + editDate + ", edited=" + edited + ", publishedDate="
				+ publishedDate + ", id=" + id + ", usernameCreator=" + usernameCreator + ", deleted=" + deleted
				+ ", photoId=" + photoId + "]";
	}
	
	
}
