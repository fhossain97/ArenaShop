package arenaShop;

import java.util.ArrayList;

public class ShoppingCart {
	private ArrayList<SalableProduct> cart;

	public ShoppingCart() {
		// TODO - populate with products from a JSON list
		this.cart = new ArrayList<>();
	}

	/**
	 * Retrieves all products available in a game user's shopping cart
	 * 
	 * @return cart
	 */

	public ArrayList<SalableProduct> getCart() {
		return cart;
	}

	/**
	 * Updates the game user's cart as products are added for purchase
	 * 
	 * @param product
	 */

	public void setCart(SalableProduct product) {
		this.cart.add(product);
	}

	public void viewCart() {
		// TODO - may or may not need method
	}

	/**
	 * Adds a product to a game user's shopping cart
	 * 
	 * @param product
	 */

	public void addProductToCart(SalableProduct product) {
		setCart(product);
	}

	/**
	 * Removes a product from a game user's shopping cart
	 * 
	 * @param product
	 */
	public void removeProductFromCart(SalableProduct product) {

		for (int item = 0; item < cart.size(); item++) {
			if (cart.get(item).getName().equals(product.getName())) {
				cart.remove(item);
			}
		}

	}

	/**
	 * Removes all the products of a game user's shopping cart
	 */

	public void clearCart() {

		cart.clear();
	}

	public void checkoutCart() {
		// TODO - may or may not need method

	}

}
