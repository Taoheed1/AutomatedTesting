
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.PageFactory;

import param.Constants;
import param.DemoAddUser;
import param.DemoLogin;

@RunWith(Parameterized.class)
public class ExcelParamTest {

	@Before
	public void setup() {
//		System.setProperty("webdriver.chrome.driver", Constants.DRIVER);
//		driver = new ChromeDriver();

		System.setProperty(Constants.PHANTOM, Constants.DRIVER);
		driver = new PhantomJSDriver();

	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Parameters
	public static Collection<Object[]> data() throws IOException {
		FileInputStream file = new FileInputStream(Constants.EXCEL);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		Object[][] ob = new Object[sheet.getPhysicalNumberOfRows()][4];

		// Reading
		for (int rowNum = 1; rowNum < sheet.getPhysicalNumberOfRows() - 1; rowNum++) {
			ob[rowNum - 1][0] = sheet.getRow(rowNum).getCell(0).getStringCellValue();
			ob[rowNum - 1][1] = sheet.getRow(rowNum).getCell(1).getStringCellValue();
			ob[rowNum - 1][2] = sheet.getRow(rowNum).getCell(2).getStringCellValue();
			ob[rowNum - 1][3] = rowNum;
		}
		return Arrays.asList(ob);
	}

	private String username;
	private String password;
	private String expected;
	private int rowNum;
	private WebDriver driver;

	public ExcelParamTest(String username, String password, String expected, int rowNum) {
		this.username = username;
		this.password = password;
		this.expected = expected;
		this.rowNum = rowNum;
	}

	@Test
	public void login() throws IOException {

		driver.get(Constants.DEMOADD);
		DemoAddUser excelAdd = PageFactory.initElements(driver, DemoAddUser.class);
		excelAdd.insertDeets(username, password);

		driver.get(Constants.DEMOLOGIN);
		DemoLogin excelLogin = PageFactory.initElements(driver, DemoLogin.class);
		excelLogin.insertDeets(username, password);

		// WebElement success = driver
		// .findElement(By.xpath("/html/body/table/tbody/tr/td[1]/big/blockquote/blockquote/font/center/b"));
		// System.out.println(success.getText());

		///////////////////////////////////////////////////////////
		FileInputStream file = new FileInputStream(Constants.EXCEL);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		XSSFRow row = sheet.getRow(rowNum);
		XSSFCell cell = row.getCell(3);
		if (cell == null) {
			cell = row.createCell(3);
		}
		cell.setCellValue(excelLogin.returnMessage());

		try {
			assertEquals("Login failed", "**Successful Login**", excelLogin.returnMessage());
			// assertEquals("error message", expected value, actual value);
			cell = row.getCell(4);
			cell.setCellValue("Pass");
			// write pass to excel sheet
		} catch (AssertionError e) {
			// write fail to excel sheet
			cell = row.getCell(4);
			// checks if the cell is empty and thus does not exist
			if (cell == null) {
				// the cell value in excel is set to fail
				cell = row.createCell(4);
			}
			cell.setCellValue("Fail");
			Assert.fail();
		} finally {
			// grab actual result and insert it into spreadsheet
			FileOutputStream fileOut = new FileOutputStream(Constants.EXCEL);
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();

			file.close();
		}

	}
}
