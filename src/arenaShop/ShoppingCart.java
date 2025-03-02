package arenaShop;

import java.util.ArrayList;

public class ShoppingCart {
	private ArrayList<SalableProduct> cart;

	public ShoppingCart() {
		// TODO - populate with products from a JSON list
		this.cart = new ArrayList<>();
	}

	public ArrayList<SalableProduct> getCart() {
		return cart;
	}

	public void setCart(SalableProduct product) {

		this.cart.add(product);
	}

	public void viewCart() {

	}

	public void addProductToCart(SalableProduct product) {
		setCart(product);

	}

	public void removeProductFromCart(SalableProduct product) {

		for (int item = 0; item < cart.size(); item++) {
			if (cart.get(item).getName().equals(product.getName())) {
				cart.remove(item);
			}
		}

	}

	public void clearCart() {

		cart.clear();
	}

	public void checkoutCart() {

	}

}
