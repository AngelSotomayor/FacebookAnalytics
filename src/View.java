import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;


/**
 * 
 * @author Shayan Patel
 *
 */
public class View {
	
	// prints out the input options of the user
	public static void showPrompt() {
		System.out.println("> Enter 1 to find how well a user's clustering coefficient");
		System.out.println("> Enter 2 to get friend recommendations for a user based on" +
							" triadic closure \n(sorted in increasing order of the number of traidic closures of a recommendation)");
		System.out.println("> Enter 3 to get friend recommendations for a user based on " +
						   "triadic closure (sorted in increasing order of the centrality of each recommendation). ");
		System.out.println("> Enter 4 to find out which friends of a user serve as local bridges");
		System.out.println("> Enter Q or q to quit");
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("> Please input the path of the file that holds the facebook graph information");
		System.out.print(">> ");
		String filepath = null;
		Controller c = null;
		
		while (filepath == null) {
			filepath = in.nextLine().trim();
			if (filepath.equals("Q") || filepath.equals("q")) {
				return;
			}
			try {
				c = new Controller(filepath);
			} 
			catch (IOException e) {
				System.out.println("> Error: You inputted an invalid file path. Please try again or enter Q to quit");
				System.out.print(">> ");
				filepath = null;
			}
		}
		
		System.out.println("\nWelcome to your friendly Facebook Analytics");
		showPrompt();
		
		while (true) {
			System.out.print(">> ");
			String input = in.nextLine();
			if (input.equals("Q") || input.equals("q")) {
				break;
			}
			else if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4")) {
				System.out.println("> Error: You entered invalid input. Please try again");
				continue;
			}
			
			System.out.println("> Please enter the user id");
			System.out.print(">> ");
			int user;
			try {
				user = Integer.parseInt(in.nextLine().trim());
			}
			catch (NumberFormatException e) {
				System.out.println("> Error: Please input a valid number representing the user id");
				continue;
			}
			
			if (input.equals("1")) {
				try {
					System.out.println("> The clustering coefficient of user with id " +
										user + " is " + c.getClusteringCoefficient(user));
				} 
				catch (UserNotFoundException e) {
					System.out.println("> Error: Sorry, there is no user with id " + user);
				}
			}
			else if (input.equals("2")) {
				try {
					SortedMap<Double, Set<Person>> results = c.getTriadicRecommendations(user);
					int rec = 1;
					for (Entry<Double, Set<Person>> e : results.entrySet()) {
						Set<Person> set = e.getValue();
						for (Person x : set) {
							System.out.println("> Recommendation " + rec + " -> user with id " + x.getId());
							rec++;
						}
					}
				} 
				catch (UserNotFoundException e) {
					System.out.println("> Error: Sorry, there is no user with id " + user);
				}
				
			}
			else if (input.equals("3")) {
				try {
					SortedMap<Double, Set<Person>> results = c.getCentralityRecommendations(user);
					int rec = 1;
					for (Entry<Double, Set<Person>> e : results.entrySet()) {
						Set<Person> set = e.getValue();
						for (Person x : set) {
							System.out.println("> Recommendation " + rec + " -> user with id " + x.getId());
							rec++;
						}
					}
				} 
				catch (UserNotFoundException e) {
					System.out.println("> Error: Sorry, there is no user with id " + user);
				}
			}
			else if (input.equals("4")) {
				Set<Person> friends;
				try {
					friends = c.getLocalBridges(user);
					if (friends.size() == 0) {
						System.out.println("> User " + user + " has no friends who are local bridges");
						continue;
					}
					for (Person p : friends) {
						System.out.println("> " + p.getId() + " is a local bridge");
					}
				} catch (UserNotFoundException e) {
					System.out.println("> Error: Sorry, there is no user with id " + user);
				}
				
			}
			else {
				System.out.println("> Error: You entered invalid input. Please try again");
			}
			
			System.out.println();
			showPrompt();
		}
		
		in.close();
		
	}

}
