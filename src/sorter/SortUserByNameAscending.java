package sorter;

import java.util.Comparator;

import beans.User;

public class SortUserByNameAscending implements Comparator<User>{

	@Override
	public int compare(User o1, User o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}
