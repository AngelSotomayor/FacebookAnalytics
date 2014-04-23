import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class ReaderTest {
	
	@Test
	public void testSmallInput() {
		Reader r = new Reader("small_graph.txt");
		Person p = r.getPerson(0);
		
		Set<Person> friends = new HashSet<Person>();
		friends.add(r.getPerson(1));
		friends.add(r.getPerson(3));
		
		assertEquals(friends, p.getFriends());
	}
	
	@Test
	public void smallCorrectFriendsSet() {
		Reader r = new Reader("small_graph.txt");
		Person p = r.getPerson(2);
		
		Set<Person> friends = new HashSet<Person>();
		friends.add(r.getPerson(1));
		friends.add(r.getPerson(3));
		friends.add(r.getPerson(5));
		
		assertEquals(friends, p.getFriends());
	}
	
	@Test
	public void facebookCorrectNumberOfEdges() {
		Reader r = new Reader("facebook_combined.txt");
		Person p = r.getPerson(0);
		
		assertEquals(347, p.numberOfFriends());
	}
	
	@Test
	public void facebookContainsFriend() {
		Reader r = new Reader("facebook_combined.txt");
		Person p0 = r.getPerson(0);
		Person p1 = r.getPerson(1);
		
		assertTrue(p1.getFriends().contains(p0));
	}
	
}
