package sorter;

import java.util.Comparator;

import beans.User;

public class SortUserByLastNameDescending implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		// TODO Auto-generated method stub
		return o1.getLastName().compareTo(o2.getLastName())*-1;
	}

}
