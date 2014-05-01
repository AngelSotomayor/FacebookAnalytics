import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Fabio Fleitas & Shayan Patel
 *
 */
public class Reader {

	public Reader(String path, Map<Integer, Person> graph) {
		graph = new HashMap<Integer, Person>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			while (br.ready()) {
				String line = br.readLine();
				String[] id = line.split(" ");

				int id0 = Integer.parseInt(id[0]);
				int id1 = Integer.parseInt(id[1]);

				Person p0 = graph.get(id0);
				Person p1 = graph.get(id1);

				if (p0 == null) { 
					p0 = new Person(id0); 
					graph.put(id0, p0);
				}
				if (p1 == null) { 
					p1 = new Person(id1);
					graph.put(id1, p1);
				}

				p0.addFriend(p1);
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}
	
}