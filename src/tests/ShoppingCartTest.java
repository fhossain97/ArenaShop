package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import arenaShop.InventoryManager;
import arenaShop.ShoppingCart;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;

public class ShoppingCartTest {

	private ShoppingCart<SalableProduct> shoppingCart;
	private SalableProduct product;
	

	@BeforeEach
	void setUp() {
		shoppingCart = new ShoppingCart<>();
		product = new SalableProduct("Phoenix Down", "A mystical feather that revives fallen warriors", 85.25, 10, true,
				101, "BunnyArena", "health");
	}

	private static class SalableProductFactory implements SalableProductFactoryInterface<SalableProduct> {
		@Override
		public SalableProduct createProduct(String name, String description, double price, int quantity,
				boolean available, int id, String purchasedAt, String category) {
			return new SalableProduct(name, description, price, quantity, available, id, purchasedAt, category);
		}
	}

	@Test
	public void testAddProductToCart() {
		shoppingCart.addProductToCart(product);

		assertEquals(1, shoppingCart.viewCart().size());
		assertTrue(shoppingCart.viewCart().contains(product));
	}

	@Test
	public void testRemoveProductFromCart() {
		shoppingCart.addProductToCart(product);
		shoppingCart.removeProductFromCart(product);

		assertEquals(0, shoppingCart.viewCart().size());
		assertFalse(shoppingCart.viewCart().contains(product));
	}

	@Test
	public void testValidateShoppingCartWhenEmpty() {
		assertFalse(shoppingCart.validateShoppingCart());
	}

	@Test
	public void testValidateShoppingCartWhenNotEmpty() {
		shoppingCart.addProductToCart(product);

		assertTrue(shoppingCart.validateShoppingCart());
	}

	@Test
	public void testClearCart() {
		InventoryManager<SalableProduct> inventoryManager = new InventoryManager<>(new SalableProductFactory());

		shoppingCart.addProductToCart(product);

		shoppingCart.clearCart(shoppingCart.viewCart(), inventoryManager);

		assertTrue(shoppingCart.viewCart().isEmpty());
	}
}