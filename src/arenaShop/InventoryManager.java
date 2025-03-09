package arenaShop;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import arenaShop.product.SalableProduct;

public class InventoryManager {

	private ArrayList<SalableProduct> inventory;

	/**
	 * Reads the JSON seed file and populates the inventory with products
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */

	public InventoryManager() throws ParseException, IOException {
		this.inventory = new ArrayList<>();

		String fileName = "Products.json";

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

			SalableProduct product = new SalableProduct(name, description, price, quantity, available, id, purchasedAt,
					category);
			this.inventory.add(product);

			Collections.sort(this.inventory);
		}
	}

	/**
	 * Retrieves the current inventory of products (both in-stock and out-of-stock
	 * products)
	 * 
	 * @return inventory
	 */

	public ArrayList<SalableProduct> getAllInventory() {
		return inventory;
	}

	/**
	 * Updates the inventory as products are purchased, specifically the quantity of
	 * a product and its current availability
	 * 
	 * @param inventory List of all products
	 * @return inventory
	 */

	public void setInventory(ArrayList<SalableProduct> inventory) {
		this.inventory = inventory;
	}

	/**
	 * Allows game users to view available products for purchase
	 * 
	 * @return availableInventory
	 */

	public ArrayList<SalableProduct> getAvailableInventory() {

		System.out.println("-****- All available products -****-");

		ArrayList<SalableProduct> availableInventory = new ArrayList<>();

		ArrayList<SalableProduct> inventory = getAllInventory();

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

	public void updateInventory(ArrayList<SalableProduct> products, String command, int item) {

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
