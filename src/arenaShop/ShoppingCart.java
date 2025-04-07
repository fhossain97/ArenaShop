package arenaShop;

import java.util.ArrayList;

import arenaShop.product.SalableProduct;

/**
 * Modifies the shopping cart of a game user
 * @param <T> Extends the SalableProduct class
 */

public class ShoppingCart<T extends SalableProduct> {
	private ArrayList<T> cart;

	public ShoppingCart() {
		this.cart = new ArrayList<T>();
	}

	/**
	 * Retrieves all products available in a game user's shopping cart
	 * 
	 * @return cart
	 */

	public ArrayList<T> viewCart() {
		return cart;
	}

	/**
	 * Updates the game user's cart as products are added for purchase
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */

	public void setCart(T product) {
		this.cart.add(product);
	}

	/**
	 * Adds a product to a game user's shopping cart
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */

	public void addProductToCart(T product) {
		setCart(product);
	}

	/**
	 * Removes a product from a game user's shopping cart
	 * 
	 * @param product Product that game user is returning or removing from cart
	 */
	public void removeProductFromCart(T product) {

		for (int item = 0; item < cart.size(); item++) {
			if (cart.get(item).getId() == product.getId()) {
				cart.remove(item);
				break;
			}
		}

	}

	/**
	 * Checks if the shopping cart has products
	 * 
	 * @return hasItems
	 */

	public boolean validateShoppingCart() {
		return viewCart().size() > 0;

	}

	/**
	 * Removes all the products in a game user's shopping cart and updates the
	 * inventory
	 */

	public void clearCart(ArrayList<T> products, InventoryManager<T> inventory) {

		for (int prod = 0; prod < products.size(); prod++) {
			inventory.updateInventory(products, "increase", prod);
		}
		cart.clear();
	}

}
