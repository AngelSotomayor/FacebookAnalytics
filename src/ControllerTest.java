import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class ControllerTest {
	Controller smallController;
	Controller mediumController;
	Controller facebookController;

	@Before
	public void setUp() throws Exception {
		smallController = new Controller("small_graph.txt");
		mediumController = new Controller("medium_graph.txt");
		facebookController = new Controller("facebook_combined.txt");
	}

	@Test
	public void testSmallInput() throws UserNotFoundException {
		Person p = smallController.getPerson(0);
		
		Set<Person> friends = new HashSet<Person>();
		friends.add(smallController.getPerson(1));
		friends.add(smallController.getPerson(3));
		
		assertEquals(friends, p.getFriends());
	}
	
	@Test
	public void smallCorrectFriendsSet() throws UserNotFoundException {
		Person p = smallController.getPerson(2);
		
		Set<Person> friends = new HashSet<Person>();
		friends.add(smallController.getPerson(1));
		friends.add(smallController.getPerson(3));
		friends.add(smallController.getPerson(5));
		
		assertEquals(friends, p.getFriends());
	}
	
	@Test
	public void facebookCorrectNumberOfEdges() throws UserNotFoundException {
		Person p = facebookController.getPerson(0);
		
		assertEquals(347, p.numberOfFriends());
	}
	
	@Test
	public void facebookContainsFriend() throws UserNotFoundException {
		Person p0 = facebookController.getPerson(0);
		Person p1 = facebookController.getPerson(1);
		
		assertTrue(p1.getFriends().contains(p0));
	}

}
