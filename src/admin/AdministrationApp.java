package admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client side for the admin user
 */

public class AdministrationApp {

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Starts the thread execution
	 * 
	 * @param ip   Local IP address
	 * @param port Port that the client and server run on
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
	 * 
	 * @param msg Message sent to the server
	 * @return response
	 * @throws IOException
	 */

	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = in.readLine()) != null) {
			if (line.equals("End")) {
				break;
			}
			response.append(line).append("\n");
		}
		return response.toString().trim();
	}

	/**
	 * Closes the reader, writer, and client socket
	 * 
	 * @throws IOException
	 */

	public void cleanup() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		AdministrationApp adminApp = new AdministrationApp();
		adminApp.start("127.0.0.1", 8080);

		BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
		String response = "";

		while (true) {
			System.out.println("R: View all inventory products");
			System.out.println("U: Update inventory");
			String userCommand = userInputReader.readLine().toUpperCase();

			if (userCommand.equals("U")) {
				response = adminApp.sendMessage("U");
			} else if (userCommand.equals("R")) {
				response = adminApp.sendMessage("R");
			} else {
				System.out.println("Invalid command. Please enter 'R' to view or 'U' to update.");
			}
			System.out.println("Server Response: " + response);

			System.out.println("Would you like to continue? (Y/N)");

			if (userCommand.equals("N")) {
				break;
			}
		}

		adminApp.cleanup();
	}
}