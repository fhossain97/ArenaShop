package arenaShop;

public class User {

	private String name;
	private String accessLevel;

	public User(String name, String accessLevel) {
		this.name = name;
		this.accessLevel = accessLevel;

	}

	/**
	 * Retrieves the name of a user
	 * 
	 * @return name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the name of a user
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the accessLevel of a user (admin or non-admin)
	 * 
	 * @return accessLevel
	 */

	public String getAccessLevel() {
		return accessLevel;
	}

	/**
	 * Sets the accessLevel of a user to admin or non-admin (game user)
	 * 
	 * @param accessLevel
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

}

class GameUser extends User {

	public GameUser(String name, String accessLevel) {
		super(name, accessLevel);

	}

}

class AdminUser extends User {

	public AdminUser(String name, String accessLevel) {
		super(name, accessLevel);
	}

}
