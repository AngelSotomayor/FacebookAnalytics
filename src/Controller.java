import java.util.HashMap;
import java.util.Map;


public class Controller {
	
	private Reader r;
	private Map<Integer, Person> graph;
	private int userId;
	
	public Controller(String filepath, int userId) {
		graph = new HashMap<Integer, Person>();
		this.r = new Reader(filepath, this.graph);
		this.userId = userId;
	}

	/**
	 * 
	 * @param id
	 * @return Person based on id
	 */
	public Person getPerson(int id) {
		return this.graph.get(id);
	}
}
