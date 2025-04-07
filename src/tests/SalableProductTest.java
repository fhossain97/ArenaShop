package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import arenaShop.product.Armor;
import arenaShop.product.Health;
import arenaShop.product.SalableProduct;
import arenaShop.product.Weapon;

public class SalableProductTest {

	private SalableProduct product;

	@BeforeEach
	void setUp() {
		product = new SalableProduct("Phoenix Down", "A mystical feather that revives fallen warriors", 85.25, 10, true,
				101, "BunnyArena", "health");
	}

	@Test
	void testGetName() {
		assertEquals("Phoenix Down", product.getName());
	}

	@Test
	void testSetName() {
		product.setName("Mega Phoenix");
		assertEquals("Mega Phoenix", product.getName());
	}

	@Test
	void testGetDescription() {
		assertEquals("A mystical feather that revives fallen warriors", product.getDescription());
	}

	@Test
	void testSetDescription() {
		product.setDescription("Restores entire party from KO");
		assertEquals("Restores entire party from KO", product.getDescription());
	}

	@Test
	void testGetPrice() {
		assertEquals(85.25, product.getPrice());
	}

	@Test
	void testSetPrice() {
		product.setPrice(150.00);
		assertEquals(150.00, product.getPrice());
	}

	@Test
	void testGetQuantity() {
		assertEquals(10, product.getQuantity());
	}

	@Test
	void testSetQuantity() {
		product.setQuantity(3);
		assertEquals(3, product.getQuantity());
	}

	@Test
	void testIsAvailableTrue() {
		product.setAvailable();
		assertTrue(product.isAvailable());
	}

	@Test
	void testIsAvailableFalse() {
		product.setQuantity(0);
		product.setAvailable();
		assertFalse(product.isAvailable());
	}

	@Test
	void testGetPurchasedAt() {
		assertEquals("BunnyArena", product.getPurchasedAt());
	}

	@Test
	void testGetCategory() {
		assertEquals("health", product.getCategory());
	}

	@Test
	void testGetId() {
		assertEquals(101, product.getId());
	}

	@Test
	void testArmorClass() {
		SalableProduct product = new Armor("Gladiator's Helm", "A sturdy helmet for protecting warriors in the arena",
				150.87, 2, true, 1, "BunnyArena", "armor");

		assertEquals("Gladiator's Helm", product.getName());
		assertEquals("armor", product.getCategory());
		assertTrue(product instanceof Armor);
	}

	@Test
	void testWeaponClass() {
		SalableProduct product = new Weapon("Champion's Sword", "A finely crafted blade designed for arena combat",
				300.05, 5, true, 2, "BunnyArena", "weapon");

		assertEquals("Champion's Sword", product.getName());
		assertEquals("weapon", product.getCategory());
		assertTrue(product instanceof Weapon);
	}

	@Test
	void testHealthClass() {
		SalableProduct product = new Health("Arena Potion", "A potion that restores health after intense battles",
				50.04, 20, true, 3, "BunnyArena", "health");

		assertEquals("Arena Potion", product.getName());
		assertEquals("health", product.getCategory());
		assertTrue(product instanceof Health);

	}
}