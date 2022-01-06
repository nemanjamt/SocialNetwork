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
	private UserDAO userDAO;
	public FriendshipRequestDAO() {
		
		friendshipsRequests = new HashMap<Long, FriendshipRequest>();
	}
	
	public FriendshipRequestDAO(String contextPath, UserDAO userDAO) {
		this();
		loadRequests(contextPath);
		this.userDAO = userDAO;
	}
	
	public FriendshipRequest findOne(Long id) {
		return friendshipsRequests.get(id);
	}
	
	public void addOne(FriendshipRequest f) {
		f.setId((long) friendshipsRequests.size());
		friendshipsRequests.put((long) friendshipsRequests.size(), f);
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
//				String s = f.getId() + ";" + f.getSender() + ";" + f.getReceiver() + ";" + f.getDate() + ";" + f.getState();

				while (st.hasMoreTokens()) {
					
					Long id = Long.parseLong(st.nextToken().trim());
					String sender = st.nextToken().trim();
					String receiver = st.nextToken().trim();
					LocalDate date = LocalDate.parse(st.nextToken().trim());
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
		if(state.equals(FriendshipRequestState.WAITING))
			return;
		
		if(state.equals(FriendshipRequestState.ACCEPTED)) {
			String user = req.getReceiver();
			String user2 = req.getSender();
			userDAO.addFriendship(user, user2);
		}
		
		
		
		friendshipsRequests.remove(id);//u svakom slucaju zahtjev se brise
		//samo je pitanje da li ce se stvoriti prijateljtstvo ili nece
		
		
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
		for(FriendshipRequest f: friendshipsRequests.values()) {
			if(f.getReceiver().equals(username))
				req.add(f);
		}
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
