import java.util.HashMap;
import java.util.Map;


public class Controller {
	
	private Reader r;
	private Map<Integer, Person> graph;

	
	public Controller(String filepath) throws UserNotFoundException {
		graph = new HashMap<Integer, Person>();
		this.r = new Reader(filepath, this.graph);
	}

	/**
	 * 
	 * @param id
	 * @return Person based on id
	 * @throws UserNotFoundException 
	 */
	public Person getNode(int id) throws UserNotFoundException {
		Person temp = this.graph.get(id);
		if (temp == null) {
			System.out.println("The input user id does not exist in the graph");
			throw new UserNotFoundException ();
		}
		return temp;
	}
	

}
