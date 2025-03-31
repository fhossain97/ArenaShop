package admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AdministrationApp {
	
	// TODO - need to retest and update logic

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	/**
	 * Starts the thread execution
	 * 
	 * @param ip   Local IP address
	 * @param port Port to connect the server and client on
	 * @throws UnknownHostException
	 * @throws IOException
	 */

	public void start(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	/**
	 * Sends a message to the server
	 * @param msg Outputs the user's selected choice
	 * @return in.readLine()
	 * @throws IOException
	 */

	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		return in.readLine();
	}
	
	/**
	 * Closes all readers, writer, and sockets
	 * @throws IOException
	 */

	public void cleanup() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}
	
	/**
	 * Display options for user
	 */

	public static void displayMenuOptions() {
		System.out.println("R: View all inventory products");
		System.out.println("U: Update inventory");
	}
	
	/**
	 * Further display options
	 */

	public static void displayUpdateOptions() {
		System.out.println("1: Add a new product");
		System.out.println("2: Update an existing product");
		System.out.println("3: Remove a product");
		System.out.println("4: Add multiple products");
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		AdministrationApp adminApp = new AdministrationApp();
		adminApp.start("127.0.0.1", 8080);

		BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
		String userCommand;

		while (true) {
			displayMenuOptions();
			userCommand = userInputReader.readLine().toUpperCase();

			if (userCommand.equals("U")) {
				displayUpdateOptions();
				String updateCommand = userInputReader.readLine();

				String response = adminApp.sendMessage(updateCommand);
				System.out.println("Server Response: " + response);

				switch (updateCommand) {
				case "1":
					System.out.println("Enter product details (e.g., name, price): ");
					String productDetails = userInputReader.readLine();
					response = adminApp.sendMessage("ADD_PRODUCT:" + productDetails);
					System.out.println("Server Response: " + response);
					break;

				case "2":
					System.out.println("Enter product ID to update: ");
					String productId = userInputReader.readLine();
					System.out.println("Enter new product details: ");
					String updatedDetails = userInputReader.readLine();
					response = adminApp.sendMessage("UPDATE_PRODUCT:" + productId + ":" + updatedDetails);
					System.out.println("Server Response: " + response);
					break;

				case "3":
					System.out.println("Enter product ID to remove: ");
					String removeProductId = userInputReader.readLine();
					response = adminApp.sendMessage("REMOVE_PRODUCT:" + removeProductId);
					System.out.println("Server Response: " + response);
					break;

				case "4":
					System.out.println("Enter product details for multiple products (comma separated): ");
					String multipleProductsDetails = userInputReader.readLine();
					response = adminApp.sendMessage("ADD_MULTIPLE_PRODUCTS:" + multipleProductsDetails);
					System.out.println("Server Response: " + response);
					break;

				default:
					System.out.println("Invalid selection. Please select a valid option.");
					break;
				}
			} else if (userCommand.equals("R")) {
				String response = adminApp.sendMessage("R");
				System.out.println("Server Response: " + response);
			} else {
				System.out.println("Invalid command. Please enter 'R' to view or 'U' to update.");
			}

			System.out.println("Would you like to continue? (Y/N)");
			String continueCommand = userInputReader.readLine().toUpperCase();
			if (continueCommand.equals("N")) {
				break;
			}
		}
	
		adminApp.cleanup();
	}
}