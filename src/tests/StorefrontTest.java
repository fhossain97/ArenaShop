package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import arenaShop.InventoryManager;
import arenaShop.ShoppingCart;
import arenaShop.StoreFront;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;

public class StorefrontTest {

	private InventoryManager<SalableProduct> inventory;
	private ShoppingCart<SalableProduct> shoppingCart;
	private SalableProduct product;

	private static class SalableProductFactory implements SalableProductFactoryInterface<SalableProduct> {
		@Override
		public SalableProduct createProduct(String name, String description, double price, int quantity,
				boolean available, int id, String purchasedAt, String category) {
			return new SalableProduct(name, description, price, quantity, available, id, purchasedAt, category);
		}
	}

	@BeforeEach
	void setUp() {
		inventory = new InventoryManager<>(new SalableProductFactory());
		shoppingCart = new ShoppingCart<>();
		;

		product = inventory.getAvailableInventory().get(0);
		shoppingCart.addProductToCart(product);

	}

	@Test
	public void testReturnProduct() {
		List<SalableProduct> products = inventory.getAvailableInventory();
		assertTrue(products.contains(product));

		StoreFront.returnProduct(inventory, product, shoppingCart);

		products = inventory.getAvailableInventory();
		assertTrue(products.contains(product));
	}

	@Test
	public void testRemoveProductFromCart() {

		System.out.println(shoppingCart);

		assertTrue(shoppingCart.viewCart().contains(product));

		StoreFront.returnProduct(inventory, product, shoppingCart);

		assertFalse(shoppingCart.viewCart().contains(product));
	}

	@Test
	public void testPurchaseProduct() {

		StoreFront.purchaseProduct(inventory, inventory.getAvailableInventory().get(0), shoppingCart);

		assertTrue(shoppingCart.viewCart().contains(product));

		ArrayList<SalableProduct> products = inventory.getAllInventory();
		assertEquals("Gladiator's Helm", products.get(0).getName());
	}

	@Test
	public void testPurchaseProductWhenOutOfStock() {
		product.setQuantity(0);

		StoreFront.purchaseProduct(inventory, product, shoppingCart);

		assertEquals(0, product.getQuantity());
	}

	@Test
	public void testRetrieveProductInformation() {
		Scanner scnr = new Scanner("1\n");
		SalableProduct retrievedProduct = StoreFront.retrieveProductInformation(scnr, inventory);
		assertNotNull(retrievedProduct);
		assertEquals(product, retrievedProduct);
	}

	@Test
	public void testSortByAscOrDescAscending() {
		Comparator<SalableProduct> comparator = Comparator.comparing(SalableProduct::getName);

		Scanner scnr = new Scanner("1\n");

		Comparator<SalableProduct> result = StoreFront.sortByAscOrDesc(scnr, comparator);

		assertEquals(comparator, result);
	}

}
