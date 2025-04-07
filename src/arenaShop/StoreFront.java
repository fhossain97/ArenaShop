package arenaShop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import org.json.simple.parser.ParseException;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;
import arenaShop.product.SalableProductFactory;

/**
 * Main class that handles the game play
 */

public class StoreFront {

	/**
	 * * Allows the game user to remove a product from their shopping cart or return
	 * a previous product purchased
	 * 
	 * @param inventory    List of products
	 * @param product      Product that game user is returning or removing from cart
	 * @param shoppingCart List of products in the game user's shopping cart
	 */

	public static void returnProduct(InventoryManager<SalableProduct> inventory, SalableProduct product,
			ShoppingCart<SalableProduct> shoppingCart) {

		ArrayList<SalableProduct> products = inventory.getAvailableInventory();

		for (int item = 0; item < products.size(); item++) {

			if (products.get(item).getId() == product.getId() && product.getPurchasedAt().equals("BunnyArena")) {

				System.out.printf("Processing your return for: %s \n", product.getName());

				inventory.updateInventory(products, "increase", item);
				shoppingCart.removeProductFromCart(product);

				System.out.println("Return has been processed.");
				return;

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

	public static void purchaseProduct(InventoryManager<SalableProduct> inventory, SalableProduct product,
			ShoppingCart<SalableProduct> shoppingCart) {

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
		System.out.println("6: Exit the Bunny Arena");

	}

	/**
	 * Validates game user's product against inventory and returns product
	 * information
	 * 
	 * @param scnr      Scanner
	 * @param inventory List of products
	 * @return selectedProduct
	 */

	public static SalableProduct retrieveProductInformation(Scanner scnr, InventoryManager<SalableProduct> inventory) {

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

	/**
	 * Sorts the inventory in ascending or descending order
	 * 
	 * @param scnr       Scanner
	 * @param comparator Comparing products with one another
	 * @return comparator
	 */

	public static Comparator<SalableProduct> sortByAscOrDesc(Scanner scnr, Comparator<SalableProduct> comparator) {
		System.out.println("\nChoose the order type of the products.");
		System.out.println("1: Ascending");
		System.out.println("2: Descending");

		int userSelection = scnr.nextInt();

		if (userSelection == 2) {
			comparator = comparator.reversed();
		}

		return comparator;
	}

	/**
	 * Sorts the inventory by ascending or descending along with by name or price
	 * 
	 * @param inventory List of products
	 * @param scnr      Scanner
	 * @return products
	 */

	public static ArrayList<SalableProduct> sortByNameOrPrice(InventoryManager<SalableProduct> inventory,
			Scanner scnr) {
		System.out.println("\nSort the products by their name or price. Please select an option.");
		System.out.println("1: Name");
		System.out.println("2: Price");

		ArrayList<SalableProduct> products = inventory.getAllInventory();
		ArrayList<SalableProduct> availableProds = new ArrayList<>();

		for (SalableProduct prod : products) {
			if (prod.isAvailable()) {
				availableProds.add(prod);
			}
		}

		int userSelection = scnr.nextInt();

		Comparator<SalableProduct> comparator = null;

		if (userSelection == 1) {
			comparator = Comparator.comparing(SalableProduct::getName);
		} else if (userSelection == 2) {
			comparator = Comparator.comparing(SalableProduct::getPrice);

		} else {
			System.out.println("Invalid number entered. Please try again.");
			return products;
		}

		comparator = sortByAscOrDesc(scnr, comparator);

		availableProds.sort(comparator);

		for (SalableProduct prod : availableProds) {
			System.out.println(prod);
		}

		return availableProds;
	}

	/**
	 * Outputs the sorted inventory
	 * 
	 * @param inventory List of products
	 * @param scnr      Scanner
	 * @return products
	 */

	public static ArrayList<SalableProduct> sortedProducts(InventoryManager<SalableProduct> inventory, Scanner scnr) {
		inventory.getAvailableInventory();
		ArrayList<SalableProduct> products = sortByNameOrPrice(inventory, scnr);
		return products;
	}

	public static void main(String[] args) throws ParseException, IOException {

		Scanner scnr = new Scanner(System.in);
		SalableProductFactoryInterface<SalableProduct> productFactory = new SalableProductFactory();
		InventoryManager<SalableProduct> inventory = new InventoryManager<SalableProduct>(productFactory);
		ShoppingCart<SalableProduct> shoppingCart = new ShoppingCart<SalableProduct>();

		boolean isEmpty = shoppingCart.validateShoppingCart();

		boolean isMakingSelection = true;

		ArrayList<SalableProduct> sortedProds = new ArrayList<>();

		System.out.println(
				"Welcome to the Bunny Arena Store! We are your one stop shop for all armor, health, and weapons. Enter a number for the corresponding option below to get started. \n");

		displayMenuOptions();
		while (isMakingSelection) {

			String selectedOption = scnr.nextLine();

			switch (selectedOption) {
			case "1":
				sortedProds = sortedProducts(inventory, scnr);
				break;

			case "2":

				System.out.println("Which product would you like to purchase? Please enter a product id. \n");
				if (sortedProds.size() > 0) {
					System.out.println(sortedProds);
				} else {
					inventory.getAvailableInventory();
				}
				SalableProduct newProduct = retrieveProductInformation(scnr, inventory);
				purchaseProduct(inventory, newProduct, shoppingCart);

				break;

			case "3":
				System.out.println(
						"Thank you for trying our products. Let's go ahead and process your return. Please enter your product id - a list has been provided for your reference.");
				System.out.println(shoppingCart.viewCart());
				SalableProduct previousProduct = retrieveProductInformation(scnr, inventory);
				returnProduct(inventory, previousProduct, shoppingCart);

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
			} else if (newOption.equals("y")) {
				displayMenuOptions();
			}

		}
		scnr.close();

		shoppingCart.clearCart(inventory.getAllInventory(), inventory);

	}

}
