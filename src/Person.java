import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Fabio Fleitas & Shayan Patel
 *
 */
public class Person {
	private int id;
	private Set<Person> friends;
	
	/**
	 * Initialize Person from id
	 * @param id
	 */
	public Person(int id) {
		this.id = id;
		this.friends = new HashSet<Person>();
	}
	
	/**
	 * Adds person to each other's friend set
	 * @param p
	 */
	public void addFriend(Person p) {
		if (p == null || friends.contains(p)) { return; }
		
		friends.add(p);
		p.addFriend(this);
	}
	
	/**
	 * Sets people as friends
	 * @param people
	 */
	public void setFriends(Set<Person> people) {
		this.friends = people;
	}
	
	/**
	 * 
	 * @return Person's id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @return Person's friends
	 */
	public Set<Person> getFriends() {
		return this.friends;
	}
	
	public int numberOfFriends() {
		return this.friends.size();
	}
}
