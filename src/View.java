import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;


public class View {
	
	public static void showPrompt() {
		System.out.println("Welcome to your friendly facebook analytics");
		System.out.println("Enter 1 to find how well a user's clustering coefficient");
		System.out.println("Enter 2 to get friend recommendations for a user based on" +
							" triadic closure \n(sorted by the number of traidic closures of a recommendation)");
		System.out.println("Enter 3 to get friend recommendations for a user based on " +
						   "triadic closure \n (sorted by the centrality of each recommendation)");
		System.out.println("Enter Q to quit\n");
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the path of the file that holds the facebook graph information");
		String filepath = null;
		Controller c = null;
		
		while (filepath == null) {
			filepath = in.nextLine().trim();
			if (filepath.equals("Q")) {
				return;
			}
			try {
				c = new Controller(filepath);
			} 
			catch (IOException e) {
				System.out.println("You inputted an invalid file path. Please try again or enter Q to quit");
				filepath = null;
			}
		}
		
		while (true) {
			showPrompt();
			String input = in.nextLine();
			System.out.println("Please enter the user id");
			int user = Integer.parseInt(in.nextLine().trim());
			if (input.equals("1")) {
				try {
					System.out.println("The clustering coefficient of user with id " +
										user + " is " + c.getClusteringCoefficient(user));
				} 
				catch (UserNotFoundException e) {
					System.out.println("Sorry, there is no user with id " + user);
				}
			}
			else if (input.equals("2")) {
				try {
					SortedMap<Double, Set<Person>> results = c.getTriadicRecommendations(user);
					int rec = 1;
					for (Entry<Double, Set<Person>> e : results.entrySet()) {
						Set<Person> set = e.getValue();
						for (Person x : set) {
							System.out.println("Recommendation " + rec + " -> user with id " + x.getId());
							rec++;
						}
					}
				} 
				catch (UserNotFoundException e) {
					System.out.println("Sorry, there is no user with id " + user);
				}
				
			}
			else if (input.equals("3")) {
				try {
					SortedMap<Double, Set<Person>> results = c.getCentralityRecommendations(user);
					int rec = 1;
					for (Entry<Double, Set<Person>> e : results.entrySet()) {
						Set<Person> set = e.getValue();
						for (Person x : set) {
							System.out.println("Recommendation " + rec + " -> user with id " + x.getId());
							rec++;
						}
					}
				} 
				catch (UserNotFoundException e) {
					System.out.println("Sorry, there is no user with id " + user);
				}
			}
			else if (input.equals("Q")) {
				break;
			}
			else {
				System.out.println("You entered invalid input. Please try again");
			}
		}
		
		in.close();
		
	}

}
