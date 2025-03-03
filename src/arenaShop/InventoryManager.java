package arenaShop;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InventoryManager {

	private ArrayList<SalableProduct> inventory;
	
	/**
	 * Reads the JSON seed file and populates the inventory with products
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

			SalableProduct product = new SalableProduct(name, description, price, quantity, available, id);
			this.inventory.add(product);
		}
	}

	/**
	 * Retrieves the current inventory of products (both in-stock and out-of-stock
	 * products)
	 * 
	 * @return inventory
	 */

	public ArrayList<SalableProduct> getInventory() {
		return inventory;
	}

	/**
	 * Updates the inventory as products are purchased, specifically the quantity of
	 * a product and its current availability
	 * 
	 * @param inventory List of all products
	 */

	public void setInventory(ArrayList<SalableProduct> inventory) {
		this.inventory = inventory;
	}

	// TODO
//	public void getCurrentInventory() {
//		
//	}
//	
//	public void updateInventory() {
//		
//	}
//	
//	public void sortByNameOrPrice() {
//		
//	}

}
