package arenaShop;

import java.util.ArrayList;

public class InventoryManager {

	private ArrayList<SalableProduct> inventory;

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
	 * @param inventory
	 */

	public void setInventory(ArrayList<SalableProduct> inventory) {
		this.inventory = inventory;
	}

	public InventoryManager(ArrayList<SalableProduct> inventory) {
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
