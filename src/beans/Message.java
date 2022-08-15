package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message {

	private String receiverUsername;
	private String senderUsername;
	private String context;
	private Long date;
	private Long id;
	
	public Message() {
		super();
	}
	
	public Message(String receiverUsername, String senderUsername, String context, Long date) {
		super();
		this.receiverUsername = receiverUsername;
		this.senderUsername = senderUsername;
		this.context = context;
		this.date = date;
	}
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiverUsername() {
		return receiverUsername;
	}
	public void setReceiverUsername(String receiverUsername) {
		this.receiverUsername = receiverUsername;
	}
	public String getSenderUsername() {
		return senderUsername;
	}
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message [receiverUsername=" + receiverUsername + ", senderUsername=" + senderUsername + ", context="
				+ context + ", date=" + date + ", id=" + id + "]";
	}
	
	
	
}
