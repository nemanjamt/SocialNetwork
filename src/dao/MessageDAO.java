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
import java.util.concurrent.ConcurrentHashMap;

import beans.Gender;
import beans.Message;
import beans.Role;
import beans.User;

public class MessageDAO {
	
	private static MessageDAO dao;
	private Map<Long, Message> messages;
	private String contextPath = "./WebContent/files";
	private MessageDAO() {
		
		messages = new ConcurrentHashMap<Long, Message>();
		loadMessages(contextPath);
	}
	
	public static MessageDAO getInstance() {
		if(dao == null) {}
			dao = new MessageDAO();
		return dao;
	}
	
	
	
	
	public Message findOne(Long id) {
		return messages.get(id);
	}
	
	public void add(Message m) {
		m.setId((long) messages.size());
		this.messages.put(m.getId(), m);
		saveMessages(this.contextPath);
	}
	
	public int getSize() {
		return this.messages.size();
	}
	
	public List<Message> findByTwoUsers(String receiver, String sender){
		messages = new ConcurrentHashMap<Long, Message>();
		loadMessages(contextPath);
		
		List<Message> mess = new ArrayList<>();
		messages.values().stream().filter(m -> (m.getReceiverUsername().equals(receiver) && m.getSenderUsername().equals(sender))|| (m.getReceiverUsername().equals(sender) && m.getSenderUsername().equals(receiver))).forEach(m -> mess.add(m));
		
		return mess;
	}
	
	private void loadMessages(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/messages.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				//		String s = m.getId() + ";" + m.getReceiverUsername() + ";" + m.getSenderUsername() +";"+m.getContext()+ ";" + m.getDate();

				while (st.hasMoreTokens()) {
					
					Long id = Long.parseLong(st.nextToken().trim());
					String receiver = st.nextToken().trim();
					String sender = st.nextToken().trim();
					String context = st.nextToken().trim();
					Long date = Long.valueOf(st.nextToken().trim());
					Message m = new Message(receiver,sender,context,date);
					m.setId(id);
					messages.put(id, m);
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
	
	public void saveMessages(String contextPath) {
		
		BufferedWriter out = null;
		try {
			File file = new File(contextPath + "/messages.txt");
			out = new BufferedWriter(new FileWriter(file));

			
			for(Message m: messages.values()) {
				
				out.write(createLine(m));
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
	
	private String createLine(Message m) {
		String s = m.getId() + ";" + m.getReceiverUsername() + ";" + m.getSenderUsername() +";"+m.getContext()+ ";" + m.getDate();
		
		return s;
		
	}

	@Override
	public String toString() {
		return "MessageDAO [messages=" + messages + "]";
	}
	
	
	
	
	
	
	

}
