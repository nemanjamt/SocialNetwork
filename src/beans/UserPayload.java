package beans;

public class UserPayload {

	private String username;
	private String password;
	private String jwt;
	public UserPayload(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public UserPayload() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
	public String getJwt() {
		return this.jwt;
	}
	
	
}
