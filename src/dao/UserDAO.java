package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Gender;
import beans.Role;
import beans.SearchUserParam;
import beans.User;
import sorter.SortUserByBirthDateAscending;
import sorter.SortUserByBirthDateDescending;
import sorter.SortUserByLastNameAscending;
import sorter.SortUserByLastNameDescending;
import sorter.SortUserByNameAscending;
import sorter.SortUserByNameDescending;

public class UserDAO {
	
	
private Map<String, User> users = new HashMap<>();
	
	
	public UserDAO() {
		this("./WebContent/files");
	}
	
	public UserDAO(String contextPath) {
		loadUsers(contextPath);
	}
	
	/**
	 * Vraca korisnika za prosledjeno korisnicko ime i šifru. Vraca null ako korisnik ne postoji
	 * @param username
	 * @param password
	 * @return
	 */
	public User find(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}
	
	public User findById(String id) {
		return users.get(id);
	}
	
	public List<User> searchUser(SearchUserParam params){
		
		List<User> result = new ArrayList<User>();
		
		for(User u: users.values()) {
			if(u.getName().toLowerCase().contains(params.getName()) && u.getLastName().toLowerCase().contains(params.getLastName()) && u.getBirthDate() >= params.getStartBirthDate() && u.getBirthDate() <= params.getEndBirthDate()) {
				result.add(u);
			}
				
		}
		
		
		return result;
	}
	
	public List<User> sortUser(List<User> users, List<String> sortParams, String orderBy){
		for(String param: sortParams) {
			switch(param) {
			case "name":{
				if(orderBy.equals("asc")){
					Collections.sort(users, new SortUserByNameAscending());
				}else {
					Collections.sort(users, new SortUserByNameDescending());
				}
				break;
			}
			case "lastName":{
				if(orderBy.equals("asc")){
					Collections.sort(users, new SortUserByLastNameAscending());
				}else {
					Collections.sort(users, new SortUserByLastNameDescending());
				}
				break;
			}
			case "birthDate":{
				if(orderBy.equals("asc")){
					Collections.sort(users, new SortUserByBirthDateAscending());
				}else {
					Collections.sort(users, new SortUserByBirthDateDescending());
				}
				break;
			}
			default:
				break;
			}
		}
		return users;
	}
	
	
	
	public Collection<User> findAll() {
		return users.values();
	}
	
	public void add(User u) {
		users.put(u.getUsername(), u);
	}
	
	public void addFriendship(String user, String user2) {
		User u = users.get(user);
		User u2 = users.get(user2);
		if( u == null || u2 == null)
			return;
		if(isFriend(user, user2))
			return;
		u.getFriends().add(user2);
		u2.getFriends().add(user);
	}
	
	public void removeFriendship(String user, String user2) {
		if(!isFriend(user, user2))
			return;
		User u = users.get(user);
		User u2 = users.get(user2);
		u.getFriends().remove(user2);
		u2.getFriends().remove(user);
	}
	
	public boolean updateUser(User u) {
		User user = users.get(u.getUsername());
		if (user == null) {return false;}
		user.setBirthDate(u.getBirthDate());
		user.setBlocked(u.isBlocked());
		user.setProfilePicture(u.getProfilePicture());
		user.setRole(u.getRole());
		user.setPrivateAccount(u.isPrivateAccount());
		user.setEmail(u.getEmail());
		user.setName(u.getName());
		user.setLastName(u.getLastName());
		user.setGender(u.getGender());
		user.setPassword(u.getPassword());
		saveUsers("./WebContent/");
		return true;
	}
	
	
	public boolean isFriend(String first, String second) {
		User u = users.get(first);
		User u2 = users.get(second);
		if( u == null || u2 == null)
			return false;
		for(String s : u.getFriends()) {
			if(s.equals(second))
				return true;
		}
		return false;
	}
	

	
	/**
	 * Ucitava korisnike iz WebContent/users.txt fajla i dodaje ih u mapu {@link #users}.
	 * Kljuè je korisnièko ime korisnika.
	 * @param contextPath Putanja do aplikacije u Tomcatu
	 */
	private void loadUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				//String s = u.getUsername() +";"+u.getPassword()+";"+u.getEmail()+ ";"+ u.getName()+";"+
				//u.getLastName()+";"+ u.getProfilePicture()+";"+u.getBirthDate()+";"+
				//u.getRole()+";"+u.getGender()+";" + u.isBlocked() + ";" +u.isPrivateAccount();
				while (st.hasMoreTokens()) {
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String mail = st.nextToken().trim();
					String name = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					String profilePicture = st.nextToken().trim();
					long date = Long.parseLong(st.nextToken().trim());
					Role role = Role.valueOf(st.nextToken().trim());
					Gender gender = Gender.valueOf(st.nextToken().trim());
					boolean isBlocked = Boolean.parseBoolean(st.nextToken().trim());
					boolean isPrivate = Boolean.parseBoolean(st.nextToken().trim());
					User u = new User(username, password, mail, name, lastName, date, gender, role, profilePicture, isPrivate, isBlocked);
					
					users.put(username, u);
				}
				loadFriendship(contextPath);
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	private void loadFriendship(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/friendships.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String first = st.nextToken().trim();
					String second = st.nextToken().trim();
					User u = users.get(first);
					User u2 = users.get(second);
					if(u == null || u2 == null)
						continue;
					u.getFriends().add(second);
//					u2.getFriends().add(first);
				}
				
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	
	
	public void saveFriendships(String contextPath) {

		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/friendships.txt");
			out = new BufferedWriter(new FileWriter(file));

			
			for(User u: users.values()) {
				for(String s : u.getFriends()) {
					out.write(createLineFriendship(u, s));
					out.newLine();
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	
	
	public void saveUsers(String contextPath) {
		
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/users.txt");
			out = new BufferedWriter(new FileWriter(file));

			
			for(User u: users.values()) {
				
				out.write(createLine(u));
				out.newLine();
			}
			saveFriendships(contextPath);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	
	private String createLine(User u) {
		String s = u.getUsername() +";"+u.getPassword()+";"+u.getEmail()+ ";"+ u.getName()+";"+
				u.getLastName()+";"+ u.getProfilePicture()+";"+u.getBirthDate()+";"+
				u.getRole()+";"+u.getGender()+";" + u.isBlocked() + ";" +u.isPrivateAccount();
		return s;
	}
	
	private String createLineFriendship(User u, String user ) {
		String s = u.getUsername() + ";" + user;
		return s;
	}

	
}


