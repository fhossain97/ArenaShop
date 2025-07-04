package arenaShop.product;

/**
 * Creates a salable product for the inventory
 */

public class SalableProduct  {
	

	public String name;
	public int id;
	public String description;
	public double price;
	public int quantity;
	public boolean available;
	public String purchasedAt;
	public String category;

	/**
	 * Retrieves the name of store the product was purchased from
	 * 
	 * @return purchasedAt
	 */

	public String getPurchasedAt() {
		return purchasedAt;
	}

	/**
	 * Retrieves the name of a product
	 * 
	 * @return name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the name of a product
	 * 
	 * @param name Name of product
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the category the product belongs to
	 * 
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Retrieves the description of a product
	 * 
	 * @return description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for a product
	 * 
	 * @param description Description of product
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retrieves the price of a product
	 * 
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price of a product
	 * 
	 * @param price Price of product
	 */

	public void setPrice(double price) {

		this.price = price;
	}

	/**
	 * Retrieves the available number of products for a specific product type
	 * 
	 * @return quantity
	 */

	public int getQuantity() {
		return quantity;
	}

	/**
	 * Updates the quantity of a product based on purchases and returns
	 * 
	 * @param quantity Available items of the product
	 */

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Retrieves the current availability of a product
	 * 
	 * @return available
	 */

	public boolean isAvailable() {
		return available;
	}

	/**
	 * Sets the availability of a product to true or false depending on the quantity
	 * of a product
	 */

	public void setAvailable() {
		this.available = getQuantity() > 0;

	}

	/**
	 * Retrieves product id
	 * 
	 * @return id
	 */

	public int getId() {
		return id;
	}

	public SalableProduct(String name, String description, double price, int quantity, boolean available, int id,
			String purchasedAt, String category) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.available = available;
		this.id = id;
		this.purchasedAt = purchasedAt;
		this.category = category;
	}

	/**
	 * Overrides the toString() method to output product information
	 */

	@Override
	public String toString() {

		return "{id: " + id + ", name: " + name + ", description: " + description + ", price: $"
				+ String.format("%,.2f", price) + ", category: " + category + ", remainingStock: " + quantity + "}";

	}



}
