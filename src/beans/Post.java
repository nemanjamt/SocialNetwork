package beans;

import java.util.ArrayList;
import java.util.List;

public abstract class Post {

	private List<Comment> comments;
	private String usernameCreator;
	private Long id;
	private boolean deleted;
	
	public Post() {
		comments = new ArrayList<Comment>();
		
	}
	public Post(String usernameCreator, boolean deleted) {
		this();
		this.deleted = deleted;
		this.usernameCreator = usernameCreator;
	}
	
	public Post(List<Comment> comments, String usernameCreator, boolean deleted) {
		this();
		this.comments = comments;
		this.usernameCreator = usernameCreator;
		this.deleted = deleted;
	}
	
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
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
	@Override
	public String toString() {
		return "Post [comments=" + comments + ", usernameCreator=" + usernameCreator + ", id=" + id + ", deleted="
				+ deleted + "]";
	}
	
	
	
	
}
