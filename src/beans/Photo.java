package beans;

import java.util.List;

public class Photo extends Post {

	private String path;

	
	public Photo() {
		super();
	}
	
	public Photo(String path,  String usernameCreator, boolean deleted)
	{
		super(usernameCreator, deleted);
		this.path = path;
	}
	
	public Photo(String path, List<Comment> comments, String usernameCreator, boolean deleted)
	{
		super(comments, usernameCreator, deleted);
		this.path = path;
	}
	
	public Photo(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Photo [path=" + path + "=" + super.toString()+ "]";
	}
	
	
	
	
}
