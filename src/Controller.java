import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class Controller {
	
	private Reader r;
	private Map<Integer, Person> graph;

	
	public Controller(String filepath) throws IOException {
		graph = new HashMap<Integer, Person>();
		this.r = new Reader(filepath, this.graph);
	}

	/**
	 * 
	 * @param id
	 * @return Person based on id
	 * @throws UserNotFoundException 
	 */
	public Person getPerson(int id) throws UserNotFoundException {
		Person temp = this.graph.get(id);
		if (temp == null) {
			System.out.println("The input user id does not exist in the graph");
			throw new UserNotFoundException ();
		}
		return temp;
	}
	
	public SortedMap<Double, List<Person>> getFriendRecommendations(int id) throws UserNotFoundException {
		Map<Person, Double> personMap = new HashMap<Person, Double>();
		Person p = this.getPerson(id);
		Set<Person> friends = p.getFriends();
		int numberOfFriends = p.numberOfFriends();
		
		for (Person friend : friends) {
			// Get all friends from p is not friends with
			Set<Person> fr = friend.getFriends();
			fr.removeAll(friends);
			
			// For all Person that are not friends with P
			// But are friends with friends of P
			// Add +1 to personMap
			for (Person f : fr) {
				if (f.equals(p)) { continue; }
				
				if (!personMap.containsKey(f)) {
					personMap.put(f, 1.0);
				}
				else {
					Double value = personMap.get(f);
					personMap.put(f, value + 1);
				}
			}
		}
		
		// Normalize Double values based on numberOfFriends
		Set<Person> keySet = personMap.keySet();
		for (Person key : keySet) {
			Double value = personMap.get(key);
			personMap.put(key, value / numberOfFriends);
		}
		
		SortedMap<Double, List<Person>> recommendations = this.convertFriendRecommendations(personMap);
		
		return recommendations;
	}
	
	private SortedMap<Double, List<Person>> convertFriendRecommendations(Map<Person, Double> personMap) {
		SortedMap<Double, List<Person>> recommendations = new TreeMap<Double, List<Person>>();
		Set<Person> keySet = personMap.keySet();
		for (Person key : keySet) {
			Double value = personMap.get(key);
			if (!recommendations.containsKey(value)) {
				List<Person> l = new ArrayList<Person>();
				l.add(key);
				recommendations.put(value, l);
			}
			else {
				List<Person> l = recommendations.get(value);
				l.add(key);
			}
		}
		
		return recommendations;
	}

}
