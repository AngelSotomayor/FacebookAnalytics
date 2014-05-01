import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.SortedMap;
import java.util.TreeMap;



public class Controller {
	
	private Reader r;
	private Map<Integer, Person> graph;
	private Map<Person, Double> centrality; 

	
	public Controller(String filepath) throws IOException {
		graph = new HashMap<Integer, Person>();
		this.r = new Reader(filepath, this.graph);
		this.centrality = new HashMap<Person, Double>();
		for (Entry<Integer, Person> entry : this.graph.entrySet()) {
			Person p = entry.getValue();
			this.centrality.put(p, new Double(0.0));
		}
		calculateCentrality();
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
	
	public double getClusteringCoefficient(int id) throws UserNotFoundException {
		Person user = this.graph.get(id);
		if (user == null) {
			System.out.println("The input user id does not exist in the graph");
			throw new UserNotFoundException ();
		}
		Set<Person> friends = user.getFriends();
		int denom = (friends.size() * (friends.size() - 1)) / 2;
		
		if (denom == 0) {
			return 0;
		}
		
		double num = 0;
		for (Person p : friends) {
			Set<Person> pfriends = p.getFriends();
			for (Person p2 : pfriends) {
				if (friends.contains(p2) && (! p.equals(p2))) {
					num++;
				}
			}
		}
		num /= 2;

		return (num / denom);
	}
	
	private void calculateCentrality() {
		for (Entry<Integer, Person> entry : this.graph.entrySet()) {
			Person source = entry.getValue();
			Stack<Person> stack = new Stack<Person>();
			List<Person> list = new ArrayList<Person>();
			Map<Person, Double> sigma = new HashMap<Person, Double>();
			Map<Person, Double> d = new HashMap<Person, Double>();
			Queue<Person> q = new ArrayDeque<Person>();
			
			for (Entry<Integer, Person> entry2 : this.graph.entrySet()) {
				Person p = entry2.getValue();
				if(p.equals(source)) {
					sigma.put(p, new Double(1));
					d.put(p, new Double(0));
				}
				else {
					sigma.put(p, new Double(0));
					d.put(p, new Double(-1));
				}
			}
			
			q.offer(source);
			
			while (! q.isEmpty()) {
				Person x = q.poll();
			}
		}
		
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
		SortedMap<Double, List<Person>> recommendations = new TreeMap<Double, List<Person>>(new FriendRecommendationComparator());
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
	
	class FriendRecommendationComparator implements Comparator<Double> {

		@Override
		public int compare(Double o1, Double o2) {
			return o1.compareTo(o2);
		}
		
	}

}
