package admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import arenaShop.InventoryManager;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactory;
import arenaShop.product.SalableProductFactoryInterface;

/**
 * Server side that processes client commands
 */

public class AdministrationService {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Starts the thread execution
	 * 
	 * @param port Port that the client and server run on
	 * @throws IOException
	 */

	public void start(int port) throws IOException {
		System.out.println("Waiting for client connection . . .");
		serverSocket = new ServerSocket(port);
		clientSocket = serverSocket.accept();
		System.out.println("Received client connection on port: " + clientSocket.getLocalPort());
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		String input;

		while ((input = in.readLine()) != null) {
			processCommand(input);

		}
		System.out.println("Server has been shut down");
	}

	/**
	 * Converts objects to JSON strings
	 * 
	 * @param inventory List of all products
	 * @return JSON list of all products
	 * @throws JsonProcessingException
	 */

	public static synchronized String getInventoryInJSON(InventoryManager<SalableProduct> inventory)
			throws JsonProcessingException {

		ObjectMapper objMapper = new ObjectMapper();

		return objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(inventory.getAllInventory());
	}

	/**
	 * Converts seedData.txt lines to JSON data
	 * 
	 * @param inventory List of products
	 * @param seedFile  File containing seedData
	 * @throws IOException
	 */

	public static synchronized void updateJSONFile(InventoryManager<SalableProduct> inventory, String seedFile)
			throws IOException {
		List<JSONObject> products = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(seedFile))) {
			String line;
			HashMap<String, Object> productMap = new HashMap<>();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				productMap.put("id", Integer.parseInt(values[0]));
				productMap.put("name", values[1]);
				productMap.put("description", values[2]);
				productMap.put("price", Double.parseDouble(values[3]));
				productMap.put("quantity", Integer.parseInt(values[4]));
				productMap.put("available", Boolean.parseBoolean(values[5]));
				productMap.put("purchasedAt", values[6]);
				productMap.put("category", values[7]);
				products.add(new JSONObject(productMap));
			}
		}
		inventory.writeToInventoryJSONFile(products);
	}

	/**
	 * Generates a new ID for a product
	 * 
	 * @param seedFile File containing seedData
	 * @return new id
	 */

	public static synchronized int getNewId(String seedFile) {
		int highestId = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(seedFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				int currentId = Integer.parseInt(values[0]);
				if (currentId > highestId) {
					highestId = currentId;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading " + seedFile + ": " + e.getMessage());
		}
		return highestId + 1;
	}

	/**
	 * Add a list of new products to the current inventory
	 * 
	 * @param fileName Name of the file containing new products to add
	 * @param seedfile Name of the seed file
	 * @param inventory List of products
	 */

	public static synchronized void addProducts(String fileName, String seedfile,
			InventoryManager<SalableProduct> inventory) {
		JSONParser parser = new JSONParser();

		try {
			FileReader reader = new FileReader(fileName);
			JSONArray products = (JSONArray) parser.parse(reader);
			reader.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedData.txt", true))) {
				for (Object obj : products) {
					JSONObject product = (JSONObject) obj;
					String newProduct = String.format("%d,%s,%s,%.2f,%d,%b,%s,%s", ((Long) product.get("id")),
							product.get("name"), product.get("description"), (Double) product.get("price"),
							((Long) product.get("quantity")).intValue(), (Boolean) product.get("available"),
							product.get("purchasedAt"), product.get("category"));

					writer.write(newProduct);
					writer.newLine();
				}
			}

			System.out.println(products.size() + " products added successfully to seedData.txt!");
			updateJSONFile(inventory, seedfile);

		} catch (FileNotFoundException e) {
			System.err.println("JSON file not found: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error reading/writing file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error processing JSON: " + e.getMessage());
		}
	}

	/**
	 * Processes the commands from the user
	 * 
	 * @param command
	 * @throws IOException
	 */

	private void processCommand(String command) throws IOException {

		String seedFile = "seedData.txt";
		String newProductJSONFile = "NewSalableProducts.json";

		SalableProductFactoryInterface<SalableProduct> productFactory = new SalableProductFactory();
		InventoryManager<SalableProduct> inventory = new InventoryManager<SalableProduct>(productFactory);
		switch (command) {
		case "R":
			out.println("**---** All inventory **---**\n");
			out.println(getInventoryInJSON(inventory));
			out.println("End");
			out.flush();
			break;
		case "U":
			addProducts(newProductJSONFile, seedFile, inventory);
			out.println("Inventory updated with additional products");
			out.println("End");
			out.flush();
			break;
		default:
			out.println("Invalid command. Please enter 'R' to retrieve or 'U' to update inventory.");
			break;
		}
	}

	/**
	 * Closes the reader, writer, and client/server sockets
	 * 
	 * @throws IOException
	 */

	public void cleanup() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException {
		AdministrationService server = new AdministrationService();
		server.start(8080);
		server.cleanup();
	}

}
