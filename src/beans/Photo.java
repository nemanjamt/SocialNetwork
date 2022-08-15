package beans;

import java.util.ArrayList;
import java.util.List;

public class Photo {

	private Long id;
	private String path;
	private String usernameCreate;
	private boolean deleted;
	private Long date;//published date
	private List<PhotoComment> comments;
	private String text;
	
	
	public Photo() {
		this.comments = new ArrayList<>();
	}
	public Photo(Long id, String path, String usernameCreate, boolean deleted, Long date, String text) {
		this();
		this.id = id;
		this.path = path;
		this.usernameCreate = usernameCreate;
		this.deleted = deleted;
		this.date = date;
		this.text = text;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUsernameCreate() {
		return usernameCreate;
	}
	public void setUsernameCreate(String usernameCreate) {
		this.usernameCreate = usernameCreate;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public List<PhotoComment> getComments() {
		return comments;
	}
	public void setComments(List<PhotoComment> comments) {
		this.comments = comments;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "Photo [id=" + id + ", path=" + path + ", usernameCreate=" + usernameCreate + ", deleted=" + deleted
				+ ", date=" + date + ", comments=" + comments + ", text=" + text + "]";
	}
	
	
}
