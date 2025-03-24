package arenaShop.product;

public class SalableProductFactory implements SalableProductFactoryInterface<SalableProduct> {
	/**
	 * Specifying the type to override the generic type
	 */
	@Override
	public SalableProduct createProduct(String name, String description, double price, int quantity, boolean available,
			int id, String purchasedAt, String category) {
		return new SalableProduct(name, description, price, quantity, available, id, purchasedAt, category);
	}

}
