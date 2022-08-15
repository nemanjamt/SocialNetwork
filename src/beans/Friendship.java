package beans;

public class Friendship {

	private String firstUser;
	private String secondUser;
	private Long id;
	private boolean deleted;
	
	
	public Friendship(String firstUser, String secondUser, Long id, boolean deleted) {
		super();
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		this.id = id;
		this.deleted = deleted;
	}
	
	
	public Friendship() {
		super();
	}


	public String getFirstUser() {
		return firstUser;
	}
	public void setFirstUser(String firstUser) {
		this.firstUser = firstUser;
	}
	public String getSecondUser() {
		return secondUser;
	}
	public void setSecondUser(String secondUser) {
		this.secondUser = secondUser;
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


	@Override
	public String toString() {
		return "Friendship [firstUser=" + firstUser + ", secondUser=" + secondUser + ", id=" + id + "]";
	}
	
	
}
