package arenaShop;

import java.util.ArrayList;

public class StoreFront {

	public void purchaseProduct(ArrayList<SalableProduct> inventory, SalableProduct product) {

		ShoppingCart shoppingCart = new ShoppingCart();

		for (int item = 0; item < inventory.size(); item++) {

			if (inventory.get(item).getName().equals(product.getName())) {
				if (inventory.get(item).isAvailable() && inventory.get(item).getQuantity() > 0) {
					System.out.printf("%s is available - adding to your shopping cart . . . \n", product.getName());
					shoppingCart.addProductToCart(product);
					int currentQuantity = inventory.get(item).getQuantity();

					inventory.get(item).setQuantity(currentQuantity - 1);

					int newQuantity = inventory.get(item).getQuantity();

					if (newQuantity <= 0) {
						inventory.get(item).setAvailable(false);

					}

					System.out.printf("%s has been added to your cart. \n", product.getName());
				} else {
					System.out.printf("%s is out of stock! Please select another product. \n", product.getName());
				}
			}
		}

	}

	public void returnProduct(ArrayList<SalableProduct> inventory, SalableProduct product) {
		InventoryManager stock = new InventoryManager(inventory);

		for (int item = 0; item < stock.getInventory().size(); item++) {

			if (stock.getInventory().get(item).getName().equals(product.getName())) {

				System.out.printf("Processing your return for: %s \n", product.getName());

				int currentQuantity = inventory.get(item).getQuantity();

				stock.getInventory().get(item).setQuantity(currentQuantity + 1);

				if (stock.getInventory().get(item).getQuantity() > 0) {
					stock.getInventory().get(item).setAvailable(true);

				}

				System.out.println("Return has been processed.");

			} else {
				System.out.println("Product was not purchased from our Arena store. Please recheck your reciept.");
			}
		}

	}

	public void viewProducts(ArrayList<SalableProduct> inventory) {

		System.out.println("All available products");

		for (int item = 0; item < inventory.size(); item++) {

			if (inventory.get(item).isAvailable()) {
				System.out.println(inventory.get(item).toString());
			}
		}

	}

	// TODO public void sortByNameOrPrice() { }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
