import java.io.IOException;
import java.util.Scanner;


public class View {
	
	public static void showPrompt() {
		System.out.println("Welcome to your friendly facebook analytics");
		System.out.println("Enter 1 to get friend recommendations for a user");
		System.out.println("Enter 2 to ...");
		System.out.println("Enter Q to quit");
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
			if (input.equals("1")) {
				
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
