package arenaShop.product;

public class Weapon extends SalableProduct {

	public Weapon(String name, String description, double price, int quantity, boolean available, int id, String purchasedAt, String category) {
		super(name, description, price, quantity, available, id, purchasedAt, category);
	}

}