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
		if (people == null) { return; }
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
	
	/**
	 * 
	 * @return Number of friends Person has
	 */
	public int numberOfFriends() {
		return this.friends.size();
	}
	
	@Override
	public String toString() {
		return Integer.toString(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
