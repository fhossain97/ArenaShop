package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import arenaShop.InventoryManager;
import arenaShop.product.SalableProduct;
import arenaShop.product.SalableProductFactoryInterface;

public class InventoryManagerTest {

	private InventoryManager<SalableProduct> inventory;
	@TempDir
	Path tempDir;

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
	}

	@Test
	void testFileExistsAndPermissions() throws IOException {
		File testFile = tempDir.resolve("sample.json").toFile();
		assertTrue(testFile.createNewFile());

		assertTrue(inventory.fileExists(testFile.getAbsolutePath()));
		assertTrue(inventory.fileIsReadable(testFile.getAbsolutePath()));
		assertTrue(inventory.fileIsWritable(testFile.getAbsolutePath()));
	}

	@Test
	public void testGenerateInventoryParsesFileCorrectly()
			throws IOException, ParseException, org.json.simple.parser.ParseException {
		File testFile = tempDir.resolve("test_inventory.json").toFile();
		try (FileWriter writer = new FileWriter(testFile)) {
			writer.write("""
					[
					  {
					    "quantity": 2,
					    "price": 150.87,
					    "name": "Gladiator's Helm",
					    "available": true,
					    "purchasedAt": "BunnyArena",
					    "description": "A sturdy helmet for protecting warriors in the arena",
					    "id": 1,
					    "category": "armor"
					  }
					]
					""");
		}

		inventory.generateInventory(testFile.getAbsolutePath());

		ArrayList<SalableProduct> items = inventory.getAllInventory();
		assertEquals(1, items.size());
		SalableProduct p = items.get(0);

		assertEquals("Gladiator's Helm", p.getName());
		assertEquals(true, p.isAvailable());
		assertEquals("armor", p.getCategory());
	}

	@Test
	public void testWriteToInventoryJSONFileCreatesValidFile() {

		List<JSONObject> jsonInventory = new ArrayList<>();

		HashMap<String, Object> productMap = new HashMap<>();
		productMap.put("quantity", 1);
		productMap.put("price", 0);
		productMap.put("name", "Farhana's Sleep");
		productMap.put("available", false);
		productMap.put("purchasedAt", "BunnyArena");
		productMap.put("description", "Sleep is a luxury");
		productMap.put("id", 99);
		productMap.put("category", "health");

		JSONObject productJson = new JSONObject(productMap);
		jsonInventory.add(productJson);

		inventory.writeToInventoryJSONFile(jsonInventory);
		File writtenFile = new File("Inventory.json");

		assertTrue(writtenFile.exists());
		assertTrue(writtenFile.length() > 0);
	}

}