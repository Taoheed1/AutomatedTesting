package param;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DemoAddUser {
	@FindBy(name = "username")
	private WebElement username;

	@FindBy(name = "password")
	private WebElement password;

	public void insertDeets(String user, String pass) {
		username.sendKeys(user);
		password.sendKeys(pass);
		password.submit();
	}

}
