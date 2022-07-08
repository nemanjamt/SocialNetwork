package beans;

import java.time.LocalDate;

public class SearchUserParam {

	private String name;
	private String lastName;
	private long startBirthDate;
	private long endBirthDate;
	public SearchUserParam() {
		super();
		this.name="";
		this.lastName = "";
		this.startBirthDate=-1;
		this.endBirthDate=1231231231231231231l;
	}
	
	
	
	
	
	public SearchUserParam(String name, String lastName, long startBirthDate, long endBirthDate) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.startBirthDate = startBirthDate;
		this.endBirthDate = endBirthDate;
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


	public long getStartBirthDate() {
		return startBirthDate;
	}


	public void setStartBirthDate(long startBirthDate) {
		this.startBirthDate = startBirthDate;
	}


	public long getEndBirthDate() {
		return endBirthDate;
	}


	public void setEndBirthDate(long endBirthDate) {
		this.endBirthDate = endBirthDate;
	}


	@Override
	public String toString() {
		return "SearchUserParam [name=" + name + ", lastName=" + lastName + ", startBirthDate=" + startBirthDate
				+ ", endBirthDate=" + endBirthDate + "]";
	}
	
}
