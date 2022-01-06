package beans;

import java.time.LocalDate;

public class FriendshipRequest {
	
	private FriendshipRequestState state;
	private LocalDate date;
	private String sender;
	private String receiver;
	private Long id;
	
	
	public FriendshipRequest() {
		super();
	}
	public FriendshipRequest(FriendshipRequestState state, LocalDate date, String sender, String receiver) {
		super();
		this.state = state;
		this.date = date;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public FriendshipRequestState getState() {
		return state;
	}
	public void setState(FriendshipRequestState state) {
		this.state = state;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "FriendshipRequest [state=" + state + ", date=" + date + ", sender=" + sender + ", receiver=" + receiver
				+ ", id=" + id + "]";
	}
	
	
	
}
