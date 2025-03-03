package arenaShop;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

public class StoreFront {

	/**
	 * Allows the game user to purchase a product which then updates the current
	 * inventory and the user's shopping cart
	 * 
	 * @param inventory List of products
	 * @param product   Selected product by game user
	 */

	public static void purchaseProduct(InventoryManager inventory, SalableProduct product) {

		ShoppingCart shoppingCart = new ShoppingCart();

		for (int item = 0; item < inventory.getInventory().size(); item++) {

			if (inventory.getInventory().get(item).getId() == (product.getId())) {
				if (inventory.getInventory().get(item).isAvailable()
						&& inventory.getInventory().get(item).getQuantity() > 0) {
					System.out.printf("%s is available - adding to your shopping cart . . . \n", product.getName());
					shoppingCart.addProductToCart(product);
					int currentQuantity = inventory.getInventory().get(item).getQuantity();

					inventory.getInventory().get(item).setQuantity(currentQuantity - 1);

					int newQuantity = inventory.getInventory().get(item).getQuantity();

					if (newQuantity <= 0) {
						inventory.getInventory().get(item).setAvailable();

					}

					System.out.printf("%s has been added to your cart. \n", product.getName());
				} else {
					System.out.printf("%s is out of stock! Please select another product. \n", product.getName());
				}
			}
		}

	}

	/**
	 * Allows the game user to return products which then updates the current
	 * inventory
	 * 
	 * @param inventory List of products
	 * @param product   Selected product by game user
	 * 
	 */

	public static void returnProduct(InventoryManager inventory, SalableProduct product) {

		System.out.println(inventory.getInventory().get(0).getId());

		for (int item = 0; item < inventory.getInventory().size(); item++) {

			if (inventory.getInventory().get(item).getId() == product.getId()) {

				System.out.printf("Processing your return for: %s \n", product.getName());

				int currentQuantity = inventory.getInventory().get(item).getQuantity();

				inventory.getInventory().get(item).setQuantity(currentQuantity + 1);

				if (inventory.getInventory().get(item).getQuantity() > 0) {
					inventory.getInventory().get(item).setAvailable();

				}

				System.out.println("Return has been processed.");
				return;
			}
		}

		System.out.println("Product was not purchased from our Arena store. Please recheck your reciept.");

	}

	/**
	 * Allows game users to view all available products for purchase
	 * 
	 * @param inventory List of products
	 */

	public static void viewProducts(InventoryManager inventory) {

		System.out.println("-****- All available products -****-");

		for (int item = 0; item < inventory.getInventory().size(); item++) {

			if (inventory.getInventory().get(item).isAvailable()) {
				System.out.println(inventory.getInventory().get(item).toString());
			}
		}

	}

	/**
	 * Displays menu options for the game user (view, purchase, return)
	 */

	public static void displayMenuOptions() {
		System.out.println("1: View all inventory products");
		System.out.println("2: Purchase a product");
		System.out.println("3: Return a product");

	}

	/**
	 * Validates game user's product against inventory and returns product
	 * information
	 * 
	 * @param scnr      Scanner
	 * @param inventory List of products
	 * @return selectedProduct
	 */

	public static SalableProduct retrieveProductInformation(Scanner scnr, InventoryManager inventory) {

		viewProducts(inventory);
		String productId = scnr.nextLine();
		SalableProduct selectedProduct = inventory.getInventory().get(0);

		for (int prod = 0; prod < inventory.getInventory().size(); prod++) {

			if (inventory.getInventory().get(prod).getId() == Integer.parseInt(productId)) {
				selectedProduct = inventory.getInventory().get(prod);
			}
		}
		return selectedProduct;

	}

	// TODO public void sortByNameOrPrice() { }

	public static void main(String[] args) throws ParseException, IOException {

		Scanner scnr = new Scanner(System.in);
		InventoryManager inventory = new InventoryManager();

		System.out.println(
				"Welcome to the Bunny Arena Store! We are your one stop shop for all armor, health, and weapons. Enter a number for the corresponding option below to get started.");

		boolean isMakingSelection = true;

		while (isMakingSelection) {
			displayMenuOptions();

			String selectedOption = scnr.nextLine();

			switch (selectedOption) {
			case "1":
				viewProducts(inventory);
				System.out.println();
				break;

			case "2":
				System.out.println(
						"Which product would you like to purchase? Please enter a product id from below list.");

				SalableProduct newProduct = retrieveProductInformation(scnr, inventory);
				purchaseProduct(inventory, newProduct);

				break;

			case "3":
				System.out.println(
						"Thank you for trying our products. Let's go ahead and process your return. Please enter your product id - a list has been provided for your reference.");
				SalableProduct previousProduct = retrieveProductInformation(scnr, inventory);
				returnProduct(inventory, previousProduct);
				break;

			default:
				System.out.println("Invalid option chosen. Please try again.");
				break;

			}

			System.out.println("Would you like to select another option? Type Y to continue or N to exit.");
			String newOption = scnr.nextLine().toLowerCase();

			if (newOption.equals("n")) {
				System.out.println("Thank you for coming. We hope to see you again soon! Goodbye.");
				isMakingSelection = false;
			}

		}
		scnr.close();

		// TODO - reset shopping cart for the next user

	}

}
