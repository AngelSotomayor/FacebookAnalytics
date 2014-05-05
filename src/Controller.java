import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author Fabio Fleitas & Shayan Patel
 *
 */
public class Controller {
	
	private Reader r;
	private Map<Integer, Person> graph;
	private Map<Person, Double> centrality; 
	private boolean centralityCalculated = false;

	
	public Controller(String filepath) throws IOException {
		graph = new HashMap<Integer, Person>();
		this.r = new Reader(filepath, this.graph);
		this.centrality = new HashMap<Person, Double>();
		for (Entry<Integer, Person> entry : this.graph.entrySet()) {
			Person p = entry.getValue();
			this.centrality.put(p, new Double(0.0));
		}
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
	
	/**
	 * 
	 * @param id
	 * @return clustering coefficient of the user with the inputted user id
	 * @throws UserNotFoundException 
	 */
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
	
	/**
	 * Calculates the betweenness centrality of all the nodes in the graph
	 * All data stored in global map called centrality
	 * Implementation based on: http://www.inf.uni-konstanz.de/algo/publications/b-fabc-01.pdf
	 */
	private void calculateCentrality() {
		if (! this.centralityCalculated) {
			for (Entry<Integer, Person> entry : this.graph.entrySet()) {
				Person source = entry.getValue();
				Stack<Person> stack = new Stack<Person>();
				Map<Person, List<Person>> list = new HashMap<Person, List<Person>>();
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
					stack.push(x);
					for (Person n : x.getFriends()) {
						if(d.get(n) < 0.0) {
							q.offer(n);
							d.put(n, new Double (1 + d.get(x)));
						}
						
						if(d.get(n) == (d.get(x) + 1)) {
							double y = sigma.get(n) + sigma.get(x);
							sigma.put(n, y);
							
							List<Person> get = list.get(n);
							if (get == null) {
								get = new ArrayList<Person>();
							}
							get.add(x);
							list.put(n, get);
						}
					}
				}
				
				Map<Person, Double> delta = new HashMap<Person, Double>();
				
				for (Entry<Integer, Person> entry3 : this.graph.entrySet()) {
					delta.put(entry3.getValue(), new Double(0));
				}
				
				while (! stack.isEmpty()) {
					Person w = stack.pop();
					List<Person> get = list.get(w);
					if (get == null) {
						get = new ArrayList<Person>();
					}
					
					for (Person v : get) {
						double deltaV = delta.get(v);
						deltaV = (deltaV) + ((sigma.get(v) / sigma.get(w)) * (1 + delta.get(w)));
						delta.put(v, deltaV);
						if (! w.equals(source)) {
							double centralityw = this.centrality.get(w);
							centralityw += delta.get(w);
							this.centrality.put(w, centralityw);
						}
					}
				}
			}
			
			this.centralityCalculated = true;
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return Set of Person that are local bridges to Person id
	 * @throws UserNotFoundException
	 */
	public Set<Person> getLocalBridges(int id) throws UserNotFoundException {
		Set<Person> localBridges = new HashSet<Person>();
		Person p = this.getPerson(id);
		Set<Person> friends = p.getFriends();
		Set<Person> allFriendsOfFriends = new HashSet<Person>(); // set of all friends of friends
		
		for (Person friend : friends) {
			allFriendsOfFriends.addAll(friend.getFriends());
		}
		
		for (Person friend : friends) {
			if (!allFriendsOfFriends.contains(friend)) {
				localBridges.add(friend);
			}
		}
		
		return localBridges;
	}
	
	/**
	 * Recommends friends based on the centrality.
	 * The pool of potential recommendations comes from friends of friends.
	 * If that Person has a high centrality, then it will be a high recommendation.
	 * 
	 * @param id
	 * @return SortedMap from least recommended to most recommended
	 * @throws UserNotFoundException
	 */
	public SortedMap<Double, Set<Person>> getCentralityRecommendations(int id) throws UserNotFoundException {
		calculateCentrality();
		SortedMap<Double, Set<Person>> recommendations = new TreeMap<Double, Set<Person>>(new FriendRecommendationComparator());
		Person p = this.getPerson(id);
		Set<Person> friends = p.getFriends();
		
		for (Person friend : friends) {
			// Get all friends from p is not friends with
			Set<Person> fr = friend.getFriends();
			fr.removeAll(friends);

			for (Person f : fr) {
				if (f.equals(p)) { continue; }
				
				Double cent = centrality.get(f);
				if (!recommendations.containsKey(cent)) {
					Set<Person> set = new HashSet<Person>();
					set.add(f);
					recommendations.put(cent, set);
				}
				else {
					Set<Person> set = recommendations.get(cent);
					set.add(f);
				}
			}
		}
		
		return recommendations;
	}
	
	/**
	 * Recommends friends based on triadic closures. 
	 * The more triadic closures a recommendation would close, the higher the recommendation.
	 * So, if a lot of your friends are friends with a person you are not,
	 * this method will highly recommend that person as a friend.
	 * 
	 * @param id
	 * @return SortedMap from least recommended to most recommended
	 * @throws UserNotFoundException
	 */
	public SortedMap<Double, Set<Person>> getTriadicRecommendations(int id) throws UserNotFoundException {
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
		
		SortedMap<Double, Set<Person>> recommendations = this.convertFriendRecommendations(personMap);
		
		return recommendations;
	}
	
	/**
	 * Converts a inverts input map
	 * 
	 * @param personMap
	 * @return SortedMap of triadic recommendations
	 */
	private SortedMap<Double, Set<Person>> convertFriendRecommendations(Map<Person, Double> personMap) {
		SortedMap<Double, Set<Person>> recommendations = new TreeMap<Double, Set<Person>>(new FriendRecommendationComparator());
		Set<Person> keySet = personMap.keySet();
		for (Person key : keySet) {
			Double value = personMap.get(key);
			if (!recommendations.containsKey(value)) {
				Set<Person> l = new HashSet<Person>();
				l.add(key);
				recommendations.put(value, l);
			}
			else {
				Set<Person> s = recommendations.get(value);
				s.add(key);
			}
		}
		
		return recommendations;
	}
	
	/**
	 * Used for TreeMaps when creating a SortedMap
	 * Compares Doubles
	 * 
	 * @author Fabio Fleitas
	 *
	 */
	class FriendRecommendationComparator implements Comparator<Double> {

		@Override
		public int compare(Double o1, Double o2) {
			return o1.compareTo(o2);
		}
		
	}

}
