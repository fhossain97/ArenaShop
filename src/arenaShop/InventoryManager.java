package arenaShop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;

public class InventoryManager<T extends SalableProduct> {

	private ArrayList<T> inventory;
	private final SalableProductFactoryInterface<T> productFactory;

	/**
	 * Checks if a file can be read (permissions)
	 * 
	 * @param fileName Name of the file
	 * @return fileReadable
	 */

	public boolean fileIsReadable(String fileName) {

		File file = new File(fileName);
		return file.canRead();

	}

	/**
	 * Checks if a file can be written to (permissions)
	 * 
	 * @param fileName Name of the file
	 * @return fileWritable
	 */

	public boolean fileIsWritable(String fileName) {
		File file = new File(fileName);
		return file.canWrite();

	}

	/**
	 * Checks if a file exists
	 * 
	 * @param fileName Name of the file
	 * @return fileExists
	 */

	public boolean fileExists(String fileName) {
		File file = new File(fileName);

		return file.exists();

	}

	/**
	 * Initializes the store inventory using the Inventory.json file
	 * 
	 * @param fileName Name of the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */

	public void generateInventory(String fileName) throws FileNotFoundException, IOException, ParseException {
		this.inventory = new ArrayList<T>();
		try {

			JSONParser parser = new JSONParser();
			JSONArray jsonArr = (JSONArray) parser.parse(new FileReader(fileName));

			for (Object obj : jsonArr) {
				JSONObject jsonObj = (JSONObject) obj;

				String name = (String) jsonObj.get("name");
				double price = Double.parseDouble(jsonObj.get("price").toString());
				int quantity = Integer.parseInt(jsonObj.get("quantity").toString());
				String description = (String) jsonObj.get("description");
				boolean available = (boolean) jsonObj.get("available");
				int id = Integer.parseInt(jsonObj.get("id").toString());
				String purchasedAt = (String) jsonObj.get("purchasedAt");
				String category = (String) jsonObj.get("category");

				T product = productFactory.createProduct(name, description, price, quantity, available, id, purchasedAt,
						category);
				this.inventory.add(product);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.printf("%s does not exist. \n", fileName);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.printf("Unable to parse file: %s. \n", fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Unable to read %s. \n", fileName);
		}

	}

	/**
	 * Generates a JSON file with seed data of the inventory in the event the
	 * Inventory.json file does not exist.
	 * 
	 * @param fileName Name of the file
	 * @throws IOException
	 */

	public void generateJSONFile(String fileName) throws IOException {
		File inventory = new File(fileName);
		boolean fileCreated = inventory.createNewFile();

		if (fileCreated && fileIsWritable(fileName)) {
			String seedData = "seedData.txt";
			List<JSONObject> products = new ArrayList<>();

			try (BufferedReader br = new BufferedReader(new FileReader(seedData))) {
				String line;
				HashMap<String, Object> productList = new HashMap<>();

				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");

					productList.put("id", Integer.parseInt(values[0]));
					productList.put("name", values[1]);
					productList.put("description", values[2]);
					productList.put("price", Double.parseDouble(values[3]));
					productList.put("quantity", Integer.parseInt(values[4]));
					productList.put("available", Boolean.parseBoolean(values[5]));
					productList.put("purchasedAt", values[6]);
					productList.put("category", values[7]);

					JSONObject productJson = new JSONObject(productList);
					products.add(productJson);

				}

				try (FileWriter file = new FileWriter("Inventory.json")) {
					file.write("[");

					for (int i = 0; i < products.size(); i++) {
						JSONObject prod = products.get(i);
						file.write(prod.toJSONString());

						if (i < products.size() - 1) {
							file.write(",");
						}
					}

					file.write("]");

					file.flush();
					System.out.println("Inventory.json created successfully.");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.printf("Unable to read %s. \n", fileName);
				}

			}
		} else {
			System.out.println("Inventory.json file NOT created.");
		}
	}

	/**
	 * Utilizes the JSON file to populate the inventory with products
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */

	public InventoryManager(SalableProductFactoryInterface<T> productFactory) {
		this.productFactory = productFactory;
		this.inventory = new ArrayList<>();
		String fileName = "Inventory.json";

		try {
			if (fileIsReadable(fileName) && fileExists(fileName)) {
				generateInventory(fileName);
			}

			else {

				generateJSONFile(fileName);
				if (fileExists(fileName)) {
					generateInventory(fileName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error found when attempting to retrieve the inventory list.");
		}
	}

	/**
	 * Retrieves the current inventory of products (both in-stock and out-of-stock
	 * products)
	 * 
	 * @return inventory List of all products
	 * 
	 * 
	 */

	public ArrayList<T> getAllInventory() {
		return inventory;
	}

	/**
	 * Updates the inventory as products are purchased, specifically the quantity of
	 * a product and its current availability
	 * 
	 * @param inventory List of all products
	 * @return inventory
	 */

	public void setInventory(ArrayList<T> inventory) {
		this.inventory = inventory;
	}

	/**
	 * Allows game users to view available products for purchase
	 * 
	 * @return availableInventory
	 */

	public ArrayList<T> getAvailableInventory() {

		System.out.println("-****- All available products -****-");

		ArrayList<T> availableInventory = new ArrayList<>();

		ArrayList<T> inventory = getAllInventory();

		for (int item = 0; item < inventory.size(); item++) {

			if (inventory.get(item).isAvailable()) {
				availableInventory.add(inventory.get(item));
				System.out.println(inventory.get(item).toString());
			}
		}

		return availableInventory;
	}

	/**
	 * Updates the inventory by removing or adding the correct quantity of a
	 * specified product
	 * 
	 * @param products All products in the inventory
	 * @param command  Internal command that executes the correct logic in this
	 *                 method
	 * @param item     Order of a product within the inventory
	 */

	public void updateInventory(ArrayList<T> products, String command, int item) {

		int currentQuantity = products.get(item).getQuantity();

		if (command.equals("increase")) {

			products.get(item).setQuantity(currentQuantity - 1);

			if (products.get(item).getQuantity() <= 0) {
				products.get(item).setAvailable();

			}

		} else {

			products.get(item).setQuantity(currentQuantity + 1);

			if (products.get(item).getQuantity() > 0) {
				products.get(item).setAvailable();

			}

		}
	}

}
