import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

	

public class ControllerTest {
	Controller smallController;
	Controller mediumController;
	Controller facebookController;
	Controller clusteringController;

	@Before
	public void setUp() throws Exception {
		smallController = new Controller("small_graph.txt");
		mediumController = new Controller("medium_graph.txt");
		facebookController = new Controller("facebook_combined.txt");
		clusteringController = new Controller("clustering_coefficient_graph");
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
	
	@Test
	public void friendRecommendationsSmallGraphUser0() throws UserNotFoundException {
		Map<Double, List<Person>> map = new HashMap<Double, List<Person>>();
		List<Person> l0 = new ArrayList<Person>();
		l0.add(smallController.getPerson(4));
		map.put(0.5, l0);
	
		List<Person> l1 = new ArrayList<Person>();
		l1.add(smallController.getPerson(2));
		map.put(1.0, l1);
		
		assertEquals(map, smallController.getFriendRecommendations(0));
	}
	
	@Test
	public void clusterCoefficientTest() {
		try {
			assertEquals(0.3333333333, clusteringController.getClusteringCoefficient(1), 0.000001);
			assertEquals(0.6666666666, clusteringController.getClusteringCoefficient(5), 0.000001);
			assertEquals(0, clusteringController.getClusteringCoefficient(2), 0.000001);
			assertEquals(1, clusteringController.getClusteringCoefficient(3), 0.000001);
			assertEquals(1, clusteringController.getClusteringCoefficient(4), 0.000001);
		} 
		catch (UserNotFoundException e) {
			assertTrue(false);
		}
	}

}
