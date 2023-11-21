package sorter;

import java.util.Comparator;

import beans.User;

public class SortUserByBirthDateAscending implements Comparator<User> {

	@Override
	public int compare(User o1, User o2) {
		// TODO Auto-generated method stub
		return Long.compare(o1.getBirthDate(), o2.getBirthDate())*-1;
	}
}
