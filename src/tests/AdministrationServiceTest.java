package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import admin.AdministrationService;
import arenaShop.InventoryManager;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;

public class AdministrationServiceTest {

	private InventoryManager<SalableProduct> inventory;
	private File tempSeedFile = new File("tempSeed.txt");

	private static class SalableProductFactory implements SalableProductFactoryInterface<SalableProduct> {
		@Override
		public SalableProduct createProduct(String name, String description, double price, int quantity,
				boolean available, int id, String purchasedAt, String category) {
			return new SalableProduct(name, description, price, quantity, available, id, purchasedAt, category);
		}
	}

	@BeforeEach
	public void setUp() throws IOException {
		inventory = new InventoryManager<>(new SalableProductFactory());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("tempSeed.txt"))) {
			writer.write("102,Potion,Heals 50 HP,12.5,5,true,BunnyArena,health\n");
			writer.write("103,Hi-Potion,Heals 150 HP,35.0,3,true,BunnyArena,health\n");
		}
	}


	@AfterEach
	public void deleteTempSeedFile() {
		tempSeedFile.delete();
	}

	@Test
	public void testGetInventoryInJSONReturnsValidJsonString() throws Exception {
		String json = AdministrationService.getInventoryInJSON(inventory);

		assertNotNull(json);
		assertTrue(json.contains("Potion"));
		assertTrue(json.contains("\"id\" : 102"));
	}

	@Test
	public void testUpdateJSONFileUpdatesInventory() throws IOException {
		AdministrationService.updateJSONFile(inventory, tempSeedFile.getAbsolutePath());

		ArrayList<SalableProduct> allProducts = inventory.getAllInventory();

		System.out.println(allProducts);

		assertEquals(2, allProducts.size());
		assertEquals("Potion", allProducts.get(0).getName());

	}

	@Test
	public void testGetNewIdReturnsNextHighestId() {
		int newId = AdministrationService.getNewId(tempSeedFile.getAbsolutePath());

		assertEquals(104, newId);
	}

}