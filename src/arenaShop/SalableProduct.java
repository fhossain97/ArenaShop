package arenaShop;

public class SalableProduct {

	private String name;
	private String description;
	private double price;
	private int quantity;
	private boolean available;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public SalableProduct(String name, String description, double price, int quantity, boolean available) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.available = available;
	}

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