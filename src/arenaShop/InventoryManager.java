package arenaShop;

import java.util.ArrayList;

public class InventoryManager {

	private ArrayList<SalableProduct> inventory;

	public ArrayList<SalableProduct> getInventory() {
		return inventory;
	}

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
