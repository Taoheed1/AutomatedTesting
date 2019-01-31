package param;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DemoLogin {

	@FindBy(name = "username")
	private WebElement username;

	@FindBy(name = "password")
	private WebElement password;
	
	@FindBy(xpath="/html/body/table/tbody/tr/td[1]/big/blockquote/blockquote/font/center/b")
	private WebElement message;

	public void insertDeets(String user, String pass) {
		username.sendKeys(user);
		password.sendKeys(pass);
		password.submit();
	}
	
	public String returnMessage() {
		return message.getText();
	}

}
