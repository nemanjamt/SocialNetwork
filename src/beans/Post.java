package beans;

import java.util.ArrayList;
import java.util.List;

public  class Post {

	private List<PostComment> comments;
	private String usernameCreator;
	private Long id;
	private boolean deleted;
	private String pictureName;
	private String postText;
	private Long date;//publishedDate
	public Post() {
		comments = new ArrayList<PostComment>();
		
	}
	public Post(List<PostComment> comments, String usernameCreator, Long id, boolean deleted, String name,
			String postText, Long date) {
		super();
		this.comments = comments;
		this.usernameCreator = usernameCreator;
		this.id = id;
		this.deleted = deleted;
		this.pictureName = name;
		this.postText = postText;
		this.date = date;
	}
	public Post(String usernameCreator, Long id, boolean deleted, String name, String postText, Long date) {
		super();
		this.usernameCreator = usernameCreator;
		this.id = id;
		this.deleted = deleted;
		this.pictureName = name;
		this.postText = postText;
		this.date = date;
	}
	public List<PostComment> getComments() {
		return comments;
	}
	public void setComments(List<PostComment> comments) {
		this.comments = comments;
	}
	public String getUsernameCreator() {
		return usernameCreator;
	}
	public void setUsernameCreator(String usernameCreator) {
		this.usernameCreator = usernameCreator;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getPictureName() {
		return pictureName;
	}
	public void setPictureName(String name) {
		this.pictureName = name;
	}
	public String getPostText() {
		return postText;
	}
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public void setPostText(String postText) {
		this.postText = postText;
	}
	@Override
	public String toString() {
		return "Post [comments=" + comments + ", usernameCreator=" + usernameCreator + ", id=" + id + ", deleted="
				+ deleted + ", pictureName=" + pictureName + ", postText=" + postText + ", date=" + date + "]";
	}
	
	
	
	
	
	
}
