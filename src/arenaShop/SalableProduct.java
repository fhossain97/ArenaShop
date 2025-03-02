package arenaShop;

public class SalableProduct {

	private String name;
	private String description;
	private double price;
	private int quantity;
	private boolean available;

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
		// TODO - may not need
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

	public SalableProduct(String name, String description, double price, int quantity, boolean available) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.available = available;
	}

	/**
	 * Overrides the toString() method to output product information
	 */

	@Override
	public String toString() {

		return "Product information: {name: " + name + ", description: " + description + " price: $"
				+ String.format("%, .2f", price) + "}";

	}

}

class AddOn extends SalableProduct {

	public AddOn(String name, String description, double price, int quantity, boolean available) {
		super(name, description, price, quantity, available);
		// TODO - TO BE COMPLETED WITH MORE CONTEXT LATER
	}

}