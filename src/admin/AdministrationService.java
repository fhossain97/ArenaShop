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
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import arenaShop.InventoryManager;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactory;
import arenaShop.product.SalableProductFactoryInterface;

public class AdministrationService {
	
	// TODO - need to retest and update logic

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * Starts the thread execution
	 * 
	 * @param port Port to connect the server and client on
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
			if (input.equals(".")) {
				System.out.println("Shutting server down . . .");
				out.println("QUIT");
				break;
			} else {
				processCommand(input);
			}
		}
		System.out.println("Server shut down");
	}

	/**
	 * Converts a product object to JSON
	 * 
	 * @param product Product object being converted to JSON
	 * @return JSONObject(map) JSON version of an object
	 */

	public static synchronized JSONObject toJSON(SalableProduct product) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", product.getId());
		map.put("name", product.getName());
		map.put("price", product.getPrice());
		map.put("quantity", product.getQuantity());
		map.put("category", product.getCategory());
		map.put("available", product.isAvailable());
		map.put("purchasedAt", product.getPurchasedAt());
		map.put("description", product.getDescription());
		return new JSONObject(map);
	}

	/**
	 * Outputs JSON of inventory
	 * 
	 * @param inventory List of all products
	 * @return jsonInventory JSON list of all products
	 */

	public static synchronized List<JSONObject> getInventoryInJSON(InventoryManager<SalableProduct> inventory) {
		ArrayList<SalableProduct> products = inventory.getAllInventory();
		List<JSONObject> jsonInventory = new ArrayList<>();
		for (SalableProduct product : products) {
			jsonInventory.add(toJSON(product));
		}

		return jsonInventory;
	}

	/**
	 * Updates the JSON file
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
	 * @return
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
	 * Adds a new product to the seedFile and then updates the JSON file
	 * 
	 * @param scnr      Scanner
	 * @param seedFile  File containing seedData
	 * @param inventory List of products
	 */

	public static synchronized void addNewProduct(Scanner scnr, String seedFile,
			InventoryManager<SalableProduct> inventory) {
		try (scnr) {
			System.out.print("Enter product name: ");
			String name = scnr.nextLine();

			System.out.print("Enter description: ");
			String description = scnr.nextLine();

			System.out.print("Enter quantity: ");
			int quantity = Integer.parseInt(scnr.nextLine());

			System.out.print("Enter price: ");
			double price = Double.parseDouble(scnr.nextLine());

			System.out.print("Enter category: ");
			String category = scnr.nextLine();

			System.out.print("Enter purchasedAt: ");
			String purchasedAt = scnr.nextLine();

			int newId = getNewId(seedFile);
			boolean available = quantity > 0;

			String newProduct = String.format("%d,%s,%s,%.2f,%d,%b,%s,%s", newId, name, description, price, quantity,
					available, purchasedAt, category);

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedData.txt", true))) {
				writer.newLine();
				writer.write(newProduct);
			}

			System.out.println("Product added successfully!");
			updateJSONFile(inventory, seedFile);

		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	/**
	 * Removes a product from the seedFile and then updates the JSON
	 * 
	 * @param scnr
	 * @param seedFile
	 * @param inventory
	 */

	public static synchronized void removeProduct(Scanner scnr, String seedFile,
			InventoryManager<SalableProduct> inventory) {

		try (scnr) {
			System.out.print("Enter the ID of the product to remove: ");
			int idToRemove = Integer.parseInt(scnr.nextLine());

			List<String> updatedLines = new ArrayList<>();
			boolean found = false;

			try (BufferedReader reader = new BufferedReader(new FileReader(seedFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					int currentId = Integer.parseInt(parts[0]);

					if (currentId == idToRemove) {
						found = true;
					} else {
						updatedLines.add(line);
					}
				}
			}

			if (!found) {
				System.out.println("Product not found.");
				return;
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(seedFile))) {
				for (String updatedLine : updatedLines) {
					writer.write(updatedLine);
					writer.newLine();
				}
			}

			System.out.println("Product removed successfully!");
			updateJSONFile(inventory, seedFile);

		} catch (IOException e) {
			System.err.println("Error modifying the file: " + e.getMessage());
		}
	}

	/**
	 * Updates a specified property of an existing product
	 * 
	 * @param id
	 * @param seedFile
	 * @param inventory
	 * @param scnr
	 */

	public static synchronized void updatePropertyOfProduct(int id, String seedFile,
			InventoryManager<SalableProduct> inventory, Scanner scnr) {

		try (scnr) {
			List<String> updatedLines = new ArrayList<>();
			boolean found = false;

			try (BufferedReader reader = new BufferedReader(new FileReader(seedFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					int currentId = Integer.parseInt(parts[0]);

					if (currentId == id) {
						found = true;

						System.out.println("What property would you like to update?");
						System.out.println("1. Name");
						System.out.println("2. Description");
						System.out.println("3. Price");
						System.out.println("4. Quantity");
						System.out.println("5. Category");
						System.out.println("6. Purchased At");

						int choice = Integer.parseInt(scnr.nextLine());

						String newName = parts[1];
						String newDescription = parts[2];
						String newPrice = parts[3];
						String newQuantity = parts[4];
						String newCategory = parts[7];
						String newAvailability = parts[5];
						String newPurchasedAt = parts[6];

						switch (choice) {
						case 1:
							System.out.print("Enter new name: ");
							newName = scnr.nextLine();
							break;
						case 2:
							System.out.print("Enter new description: ");
							newDescription = scnr.nextLine();
							break;
						case 3:
							System.out.print("Enter new price: ");
							newPrice = scnr.nextLine();
							break;
						case 4:
							System.out.print("Enter new quantity: ");
							newQuantity = scnr.nextLine();
							newAvailability = String.valueOf((Integer.parseInt(newQuantity)) > 0);
							break;
						case 5:
							System.out.print("Enter new category: ");
							newCategory = scnr.nextLine();
							break;
						case 6:
							System.out.print("Enter new purchasedAt: ");
							newPurchasedAt = scnr.nextLine();
							break;
						default:
							System.out.println("Invalid choice.");
							return;
						}

						String updatedProduct = String.format("%d,%s,%s,%s,%s,%s,%s,%s", id, newName, newDescription,
								newPrice, newQuantity, newAvailability, newPurchasedAt, newCategory);

						updatedLines.add(updatedProduct);
					} else {
						updatedLines.add(line);
					}
				}
			}

			if (!found) {
				System.out.println("Product not found.");
				return;
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(seedFile))) {
				for (String updatedLine : updatedLines) {
					writer.write(updatedLine);
					writer.newLine();
				}
			}

			System.out.println("Product updated successfully!");
			updateJSONFile(inventory, seedFile);

		} catch (IOException e) {
			System.err.println("Error updating the file: " + e.getMessage());
		}
	}

	/**
	 * Add a list of new products to the current inventory
	 * 
	 * @param fileName
	 * @param seedfile
	 * @param inventory
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
					String newProduct = String.format("%d,%s,%s,%.2f,%d,%b,%s,%s", ((Integer) product.get("id")),
							product.get("name"), product.get("description"), (Double) product.get("price"),
							((Long) product.get("quantity")).intValue(), (Boolean) product.get("available"),
							product.get("purchasedAt"), product.get("category"));

					writer.newLine();
					writer.write(newProduct);
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

		Scanner scnr = new Scanner(System.in);
		String seedFile = "seedData.txt";
		String newProductJSONFile = "NewSalableProducts.json";

		SalableProductFactoryInterface<SalableProduct> productFactory = new SalableProductFactory();
		InventoryManager<SalableProduct> inventory = new InventoryManager<SalableProduct>(productFactory);
		switch (command) {
		case "R":

			out.println("**---** All inventory **---**\n");
			out.println(getInventoryInJSON(inventory));
			break;
		case "U":
			out.println("Enter update option (1: Add, 2: Update, 3: Remove, 4: Add Multiple):");
			String updateOption = in.readLine();
			handleUpdateOption(updateOption, scnr, seedFile, inventory, newProductJSONFile);
			break;
		default:
			out.println("Invalid command. Please enter 'R' to retrieve or 'U' to update inventory.");
			break;
		}
	}

	/**
	 * Additional options
	 * 
	 * @param option
	 * @param scnr
	 * @param seedFile
	 * @param inventory
	 * @param newProductJSONFile
	 * @throws IOException
	 */

	private void handleUpdateOption(String option, Scanner scnr, String seedFile,
			InventoryManager<SalableProduct> inventory, String newProductJSONFile) throws IOException {
		switch (option) {
		case "1":
			addNewProduct(scnr, seedFile, inventory);
			break;

		case "2":
			out.println("**---** All inventory **---**\n");
			getInventoryInJSON(inventory);
			out.println("\n Enter the ID of the product you would like to update: ");

			int selectedId = Integer.parseInt(scnr.nextLine());
			updatePropertyOfProduct(selectedId, seedFile, inventory, scnr);
			break;

		case "3":
			removeProduct(scnr, seedFile, inventory);
			break;

		case "4":
			addProducts(newProductJSONFile, seedFile, inventory);

			break;
		default:

			out.println("Invalid option chosen. Please try again.");
			break;
		}

	}

	/**
	 * Cleanup logic
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
