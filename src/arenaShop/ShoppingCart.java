package arenaShop;

import java.util.ArrayList;

import arenaShop.product.SalableProduct;

public class ShoppingCart {
	private ArrayList<SalableProduct> cart;

	public ShoppingCart() {
		this.cart = new ArrayList<>();
	}

	/**
	 * Retrieves all products available in a game user's shopping cart
	 * 
	 * @return cart
	 */

	public ArrayList<SalableProduct> viewCart() {
		return cart;
	}

	/**
	 * Updates the game user's cart as products are added for purchase
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */

	public void setCart(SalableProduct product) {
		this.cart.add(product);
	}

	/**
	 * Adds a product to a game user's shopping cart
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */

	public void addProductToCart(SalableProduct product) {
		setCart(product);
	}

	/**
	 * Removes a product from a game user's shopping cart
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */
	public void removeProductFromCart(SalableProduct product) {

		for (int item = 0; item < cart.size(); item++) {
			if (cart.get(item).getId() == product.getId()) {
				cart.remove(item);
			}
		}

	}

	/**
	 * Checks if the shopping cart has products
	 * 
	 * @return hasItems
	 */

	public boolean validateShoppingCart() {
		boolean hasItems = false;

		if (viewCart().size() <= 0) {
			return hasItems;

		}

		hasItems = true;
		return hasItems;

	}

	/**
	 * Removes all the products in a game user's shopping cart and updates the
	 * inventory
	 */

	public void clearCart(ArrayList<SalableProduct> products, InventoryManager inventory) {

		for (int prod = 0; prod < products.size(); prod++) {
			inventory.updateInventory(products, "increase", prod);
		}
		cart.clear();
	}

}
