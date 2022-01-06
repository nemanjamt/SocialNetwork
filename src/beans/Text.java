package beans;

import java.util.List;

public class Text extends Post {

	private String context;
	
	public Text() {
		super();
	}
	
	public Text(String context,  String usernameCreator, boolean deleted)
	{
		super(usernameCreator, deleted);
		this.context = context;
	}
	
	public Text(String context, List<Comment> comments, String usernameCreator, boolean deleted)
	{
		super(comments, usernameCreator, deleted);
		this.context = context;
	}
	
	public Text(String context) {
		super();
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "Text [context=" + context +"=" + super.toString()+ "]";
	}
	
	
}
