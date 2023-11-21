package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.FriendshipRequest;
import beans.FriendshipRequestState;
import beans.Message;
import beans.User;

public class FriendshipRequestDAO {
	
	private Map<Long, FriendshipRequest> friendshipsRequests;
	private FriendshipDAO friendshipDAO;
	private String contextPath;
	private static FriendshipRequestDAO dao;
	private FriendshipRequestDAO() {
		
		friendshipsRequests = new HashMap<Long, FriendshipRequest>();
	}
	
	private FriendshipRequestDAO(String contextPath, FriendshipDAO dao) {
		this();
		loadRequests(contextPath);
		this.friendshipDAO = dao;
		this.contextPath = contextPath;
	
	}
	public static FriendshipRequestDAO getInstance(String contextPath, FriendshipDAO friendshipDAO) {
		if(dao == null) {
			dao = new FriendshipRequestDAO(contextPath, friendshipDAO);
		}
		return dao;
	}
	
	public FriendshipRequest findOne(Long id) {
		return friendshipsRequests.get(id);
	}
	
	public FriendshipRequest addOne(FriendshipRequest f) {
		if(checkFriendRequestExists(f.getSender(), f.getReceiver())) 
			return null;
		if(friendshipDAO.checkFriendshipExist(f.getReceiver(), f.getSender()))
			return null;
		FriendshipRequest req = new FriendshipRequest();
		req.setDate(System.currentTimeMillis());
		long id = (long) friendshipsRequests.size()+1;
		while(friendshipsRequests.containsKey(id)) {
			++id;
		}
		req.setId(id);
		req.setReceiver(f.getReceiver());
		req.setSender(f.getSender());
		req.setState(FriendshipRequestState.WAITING);
		friendshipsRequests.put(req.getId(), req);
		saveRequests(this.contextPath);
		return f;
	}
	
	public boolean checkFriendRequestExists(String firstUser, String secondUser) {
		for(FriendshipRequest f : friendshipsRequests.values()){
			if((f.getReceiver().equals(firstUser) && f.getSender().equals(secondUser)) || (f.getReceiver().equals(secondUser) && f.getSender().equals(firstUser))) {
				return true;
			}
		}
		return false;
	}
	
	public FriendshipRequest checkFriendRequest(String firstUser, String secondUser) {
		for(FriendshipRequest f : friendshipsRequests.values()){
			if((f.getReceiver().equals(firstUser) && f.getSender().equals(secondUser)) || (f.getReceiver().equals(secondUser) && f.getSender().equals(firstUser))) {
				return f;
			}
		}
		return null;
	}
	
	public void loadRequests(String contextPath) {
		
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/friendshiprequests.txt");
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
					String sender = st.nextToken().trim();
					String receiver = st.nextToken().trim();
					Long date = Long.valueOf(st.nextToken().trim());
					FriendshipRequestState state = FriendshipRequestState.valueOf(st.nextToken().trim());
					FriendshipRequest req = new FriendshipRequest(state, date, sender, receiver);
					req.setId(id);
					friendshipsRequests.put(id, req);
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
	public void changeStatus(Long id, FriendshipRequestState state) {
		FriendshipRequest req = friendshipsRequests.get(id);
		if(req == null)
			return;
		req.setState(state);
		
		if(state.equals(FriendshipRequestState.ACCEPTED)) {
			friendshipDAO.addOne(req);
		}
		
		//brise se iz liste zahtjeva
		if(req.getState() != FriendshipRequestState.WAITING)
			friendshipsRequests.remove(id);
		saveRequests(this.contextPath);
		
		
	}
	public void saveRequests(String contextPath) {
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/friendshiprequests.txt");
			out = new BufferedWriter(new FileWriter(file));
			for(FriendshipRequest f: friendshipsRequests.values()) {
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
	
	public List<FriendshipRequest> findByUser(String username){
		List<FriendshipRequest> req = new ArrayList<FriendshipRequest>();
		friendshipsRequests.values().stream().filter(f -> f.getReceiver().equals(username)).forEach(f -> req.add(f));
		return req;
	}
	
	private String createLine(FriendshipRequest f) {
		String s = f.getId() + ";" + f.getSender() + ";" + f.getReceiver() + ";" + f.getDate() + ";" + f.getState();
		return s;
	}

	@Override
	public String toString() {
		return "FriendshipRequestDAO [friendshipsRequests=" + friendshipsRequests + "]";
	}
	
	
	

}
