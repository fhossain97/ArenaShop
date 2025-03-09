package arenaShop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.parser.ParseException;
import arenaShop.product.SalableProduct;

public class StoreFront {

	/**
	 * * Allows the game user to remove a product from their shopping cart or return
	 * a previous product purchased
	 * 
	 * @param inventory    List of products
	 * @param product      Product that game user is returning or removing from cart
	 * @param command      Internal command that executes the correct logic in this
	 *                     method
	 * @param shoppingCart List of products in the game user's shopping cart
	 */

	// TODO - needs validation logic to check if product being returned exceeds the
	// original inventory quantity of that specific product

	public static void cancelProduct(InventoryManager inventory, SalableProduct product, String command,
			ShoppingCart shoppingCart) {

		ArrayList<SalableProduct> products = inventory.getAvailableInventory();

		for (int item = 0; item < products.size(); item++) {

			if (products.get(item).getId() == product.getId() && product.getPurchasedAt().equals("BunnyArena")) {

				if (command.equals("return")) {
					System.out.printf("Processing your return for: %s \n", product.getName());

					inventory.updateInventory(products, "decrease", item);

					System.out.println("Return has been processed.");
					return;
				} else {
					System.out.printf("Removing %s from your shopping cart . . . \n", product.getName());

					inventory.updateInventory(products, "decrease", item);

					shoppingCart.removeProductFromCart(product);

					System.out.printf("%s has been removed from your shopping cart. \n", product.getName());

				}

			} else {
				System.out.printf(
						"Product was not purchased from the Bunny Arena store. Please return product at: %s \n",
						product.getPurchasedAt());
			}
		}

	}

	/**
	 * Allows the game user to purchase a product which then updates the current
	 * inventory and the user's shopping cart
	 * 
	 * @param inventory    List of products
	 * @param product      Selected product by game user
	 * @param shoppingCart List of products in the game user's shopping cart
	 */

	public static void purchaseProduct(InventoryManager inventory, SalableProduct product, ShoppingCart shoppingCart) {

		ArrayList<SalableProduct> products = inventory.getAllInventory();

		for (int item = 0; item < products.size(); item++) {

			if (products.get(item).getId() == product.getId() && products.get(item).isAvailable()) {
				if (products.get(item).getQuantity() > 0) {
					System.out.printf("%s is available - adding to your shopping cart . . . \n", product.getName());
					shoppingCart.addProductToCart(product);

					inventory.updateInventory(products, "decrease", item);

					System.out.printf("%s has been added to your cart. \n", product.getName());
				} else {
					System.out.printf("%s is out of stock! Please select another product. \n", product.getName());
				}
			}
		}

	}

	/**
	 * Displays menu options for the game user (view, purchase, return, and clear
	 * the shopping cart)
	 */

	public static void displayMenuOptions() {
		System.out.println("1: View all inventory products");
		System.out.println("2: Purchase a product");
		System.out.println("3: Return a product");
		System.out.println("4: View shopping cart");
		System.out.println("5: Clear shopping cart");
		System.out.println("5: Exit the Bunny Arena");

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

		ArrayList<SalableProduct> products = inventory.getAllInventory();

		String productId = scnr.nextLine();
		SalableProduct selectedProduct = null;

		for (int prod = 0; prod < products.size(); prod++) {

			if (products.get(prod).getId() == Integer.parseInt(productId)) {
				selectedProduct = products.get(prod);
			}
		}

		if (selectedProduct == null) {
			System.out.println("Product not found!");
		}
		return selectedProduct;

	}

	// TODO public void sortByPrice() { }

	public static void main(String[] args) throws ParseException, IOException {

		Scanner scnr = new Scanner(System.in);
		InventoryManager inventory = new InventoryManager();
		ShoppingCart shoppingCart = new ShoppingCart();

		boolean isEmpty = shoppingCart.validateShoppingCart();

		System.out.println(
				"Welcome to the Bunny Arena Store! We are your one stop shop for all armor, health, and weapons. Enter a number for the corresponding option below to get started. \n");

		boolean isMakingSelection = true;

		while (isMakingSelection) {
			displayMenuOptions();

			String selectedOption = scnr.nextLine();

			switch (selectedOption) {
			case "1":
				inventory.getAvailableInventory();
				System.out.println();
				break;

			case "2":

				System.out.println("Which product would you like to purchase? Please enter a product id. \n");
				inventory.getAvailableInventory();

				SalableProduct newProduct = retrieveProductInformation(scnr, inventory);
				purchaseProduct(inventory, newProduct, shoppingCart);

				break;

			case "3":
				System.out.println(
						"Thank you for trying our products. Let's go ahead and process your return. Please enter your product id - a list has been provided for your reference.");
				SalableProduct previousProduct = retrieveProductInformation(scnr, inventory);
				cancelProduct(inventory, previousProduct, "increase", shoppingCart);

				break;

			case "4":

				if (isEmpty) {
					System.out.println(
							"No products have been added to your shopping cart! Please add products to your cart.");

					break;
				}
				System.out.println("-****- Shopping Cart -****-");

				for (int cartItem = 0; cartItem < shoppingCart.viewCart().size(); cartItem++) {
					System.out.println(shoppingCart.viewCart().get(cartItem).toString());
				}

				break;

			case "5":
				if (isEmpty) {
					break;
				}

				System.out.println("Would you like to remove all products from your shopping cart? Type Y or N");

				String confirmation = scnr.nextLine().toLowerCase();

				switch (confirmation) {
				case "y":
					System.out.println("Clearing out shopping cart . . .");
					shoppingCart.clearCart(inventory.getAllInventory(), inventory);
					System.out.println("Shopping cart has been cleared.");
					break;
				case "n":
					break;
				default:
					System.out.println("Invalid character.");
					break;

				}

			case "6":
				System.out.println("Thank you for shopping at the Bunny Arena.");
				break;

			default:
				System.out.println("Invalid option chosen. Please try again.");
				break;

			}

			System.out.println();

			System.out.println("Would you like to select another option? Type Y to continue or N to exit.");
			String newOption = scnr.nextLine().toLowerCase();

			if (newOption.equals("n")) {
				System.out.println("Thank you for coming. We hope to see you again soon! Goodbye.");
				isMakingSelection = false;
			}

		}
		scnr.close();

		shoppingCart.clearCart(inventory.getAllInventory(), inventory);

	}

}
