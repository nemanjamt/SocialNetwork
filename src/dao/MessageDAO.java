package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Gender;
import beans.Message;
import beans.Role;
import beans.User;

public class MessageDAO {
	
	private Map<Long, Message> messages;
	
	public MessageDAO() {
		
		messages = new HashMap<Long, Message>();
		
	}
	
	public MessageDAO(String contextPath) {
		this();
		loadMessages(contextPath);
	}
	
	
	public Message findOne(Long id) {
		return messages.get(id);
	}
	
	public void add(Message m) {
		m.setId((long) messages.size());
		messages.put(m.getId(), m);
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
