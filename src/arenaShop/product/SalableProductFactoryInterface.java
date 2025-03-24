package arenaShop.product;

public interface SalableProductFactoryInterface<T extends SalableProduct> {

	T createProduct(String name, String description, double price, int quantity, boolean available, int id,
			String purchasedAt, String category);

}
