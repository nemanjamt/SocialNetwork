package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Friendship;
import beans.FriendshipRequest;
import beans.FriendshipRequestState;

public class FriendshipDAO {

	private Map<Long, Friendship> friendships;
	private String contextPath;
	public FriendshipDAO() {
		this.friendships = new HashMap<>();
		
	}
	
	public FriendshipDAO(String contextPath) {
		this();
		this.contextPath = contextPath;
		load(contextPath);
	}
	
	public Friendship addOne(FriendshipRequest req) {
		Friendship f = new Friendship();
		f.setFirstUser(req.getReceiver());
		f.setSecondUser(req.getSender());
		f.setDeleted(false);
		f.setId((long) friendships.size()+1);
		friendships.put(f.getId(), f);
		save(this.contextPath);
		return f;
	}
	
	public boolean checkFriendship(String firstUser, String secondUser) {
		
		for(Friendship f : friendships.values()){
			if((f.getFirstUser().equals(firstUser) && f.getSecondUser().equals(secondUser)) || (f.getFirstUser().equals(secondUser) && f.getSecondUser().equals(firstUser))
					&& (!f.isDeleted())) {
				return true;
			}
		}
		return false;
	}
	
	public List<Friendship> getAllByUser(String user){
		List<Friendship> userFriendships = new ArrayList<Friendship>();
		friendships.values().stream().filter(f -> (!f.isDeleted()) && (f.getFirstUser().equals(user) || f.getSecondUser().equals(user))).forEach(f-> userFriendships.add(f));
		return userFriendships;
	}
	
	public Friendship getById(Long id) {
		if(friendships.get(id) == null)
			return null;
		if(friendships.get(id).isDeleted())
			return null;
		return friendships.get(id);
	}
	
	public boolean deleteFriendship(Long id) {
		if(friendships.get(id) == null)
			return false;
		if(friendships.get(id).isDeleted())
			return false;
		friendships.get(id).setDeleted(true);
		save(this.contextPath);
		return true;
	}
	
	public void load(String contextPath) {
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
					
					Long id = Long.parseLong(st.nextToken().trim());
					String firstUser = st.nextToken().trim();
					String secondUser = st.nextToken().trim();
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					Friendship f = new Friendship(firstUser, secondUser, id, deleted);
					friendships.put(f.getId(), f);
					System.out.println(f);
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
	
	public void save(String contextPath) {
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/friendships.txt");
			out = new BufferedWriter(new FileWriter(file));

			

			
			for(Friendship f: friendships.values()) {
				out.write(createLine(f));
				out.newLine();
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
	
	public String createLine(Friendship f) {
		return f.getId()+";"+f.getFirstUser()+";"+f.getSecondUser()+";"+f.isDeleted();
	}
}
