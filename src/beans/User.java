package beans;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
	
	private String username;
	private String password;
	private String email;
	private String name;
	private String lastName;
	private long birthDate;
	private Gender gender;
	private Role role;
	private String profilePicture;
	private boolean privateAccount;
	private boolean blocked;
	List<String> friends;
//	private List<FriendshipRequest> friendshipRequests;
//	private List<User> friends;
//	private List<Post> posts;
//	private List<Message> messages;
	
	
	public User() {
		super();
		friends = new ArrayList<String>();
	}
	
	public User(String username, String password, String email, String name, String lastName, long birthDate,
			Gender gender, Role role, String profilePicture, boolean privateAccount, boolean blocked) {
		this();
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.role = role;
		this.profilePicture = profilePicture;
		this.privateAccount = privateAccount;
		this.blocked = blocked;
	}
	
	
	
	
	

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public boolean isPrivateAccount() {
		return privateAccount;
	}
	public void setPrivateAccount(boolean privateAccount) {
		this.privateAccount = privateAccount;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", name=" + name
				+ ", lastName=" + lastName + ", birthDate=" + birthDate + ", gender=" + gender + ", role=" + role
				+ ", profilePicture=" + profilePicture + ", privateAccount=" + privateAccount + ", blocked=" + blocked
				+ "]";
	}
	

	
	
	
	
	
	
}
