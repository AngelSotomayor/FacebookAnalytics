import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author Fabio Fleitas & Shayan Patel
 *
 */
public class Reader {
	private HashMap<Integer, Person> people;

	public Reader(String path) {
		people = new HashMap<Integer, Person>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			while (br.ready()) {
				String line = br.readLine();
				String[] id = line.split(" ");

				int id0 = Integer.parseInt(id[0]);
				int id1 = Integer.parseInt(id[1]);

				Person p0 = people.get(id0);
				Person p1 = people.get(id1);

				if (p0 == null) { 
					p0 = new Person(id0); 
					people.put(id0, p0);
				}
				if (p1 == null) { 
					p1 = new Person(id1);
					people.put(id1, p1);
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
	
	/**
	 * 
	 * @param id
	 * @return Person based on id
	 */
	public Person getPerson(int id) {
		return this.people.get(id);
	}
}