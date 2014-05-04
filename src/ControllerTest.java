import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

	

public class ControllerTest {
	Controller smallController;
	Controller mediumController;
	Controller wellController;
	Controller facebookController;
	Controller clusteringController;

	@Before
	public void setUp() throws Exception {
		smallController = new Controller("small_graph.txt");
		mediumController = new Controller("medium_graph.txt");
		wellController = new Controller("well_connected_graph.txt");
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
		SortedMap<Double, Set<Person>> map = new TreeMap<Double, Set<Person>>();
		Set<Person> l0 = new HashSet<Person>();
		l0.add(smallController.getPerson(4));
		map.put(0.5, l0);
	
		Set<Person> l1 = new HashSet<Person>();
		l1.add(smallController.getPerson(2));
		map.put(1.0, l1);
		
		assertEquals(map, smallController.getTriadicRecommendations(0));
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
	
//	@Test
//	public void calculateCentrality() {
//		clusteringController.calculateCentrality();
//		for (Entry<Person, Double> e : clusteringController.centrality.entrySet()) {
//			System.out.println (e.getKey() + " -> " + e.getValue());
//		}

	public void friendRecommendationsMediumGraphUser1() throws UserNotFoundException {
		SortedMap<Double, Set<Person>> map = new TreeMap<Double, Set<Person>>();
		Set<Person> l0 = new HashSet<Person>();
		l0.add(mediumController.getPerson(8));
		l0.add(mediumController.getPerson(4));
		map.put(0.25, l0);
	
		Set<Person> l1 = new HashSet<Person>();
		l1.add(mediumController.getPerson(5));
		map.put(0.5, l1);
		
		assertEquals(map, mediumController.getTriadicRecommendations(1));
	}
	
	@Test
	public void localBridgesSmallGraph3() throws UserNotFoundException {
		Set<Person> localBridges = new HashSet<Person>();
		localBridges.add(smallController.getPerson(0));
		localBridges.add(smallController.getPerson(2));
		
		assertEquals(localBridges, smallController.getLocalBridges(3));
	}
	
	@Test
	public void localBridgesMediumGraph3() throws UserNotFoundException {
		Set<Person> localBridges = new HashSet<Person>();
		localBridges.add(smallController.getPerson(0));
		localBridges.add(smallController.getPerson(2));
		
		assertEquals(localBridges, smallController.getLocalBridges(3));
	}
	
	@Test
	public void localBridgesWellConnected5() throws UserNotFoundException {
		Set<Person> localBridges = new HashSet<Person>();
		localBridges.add(wellController.getPerson(4));
		
		assertEquals(localBridges, wellController.getLocalBridges(5));
	}
	
	@Test
	public void localBridgesWellConnected() throws UserNotFoundException {
		Set<Person> localBridges = new HashSet<Person>();
		localBridges.add(wellController.getPerson(3));
		localBridges.add(wellController.getPerson(7));
		
		assertEquals(localBridges, wellController.getLocalBridges(2));
	}
	
//	@Test
//	public void friendRecommendationsFacebookGraphUser0() throws UserNotFoundException {	
//		System.out.println(facebookController.getTriadicRecommendations(0));
//	}
	
	
	/** commented because all fields and methods tested are private in Controller */
//	@Test
//	public void centralityTest() {
//		this.clusteringController.calculateCentrality();
//		assertEquals(new Double(0), this.clusteringController.centrality.get(new Person(2)));
//		assertEquals(new Double(0), this.clusteringController.centrality.get(new Person(3)));
//		assertEquals(new Double(0), this.clusteringController.centrality.get(new Person(4)));
//		assertEquals(new Double(1), this.clusteringController.centrality.get(new Person(5)));
//		assertEquals(new Double(7), this.clusteringController.centrality.get(new Person(1)));
//	}
	


}
