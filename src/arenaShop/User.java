package arenaShop;

public class User {

	private String name;
	private String accessLevel;

	public User(String name, String accessLevel) {
		this.name = name;
		this.accessLevel = accessLevel;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

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
