import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import param.Constants;
import param.DemoSiteExcel;

public class ExcelParamTest {

	WebDriver driver;

	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", Constants.DRIVER);
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void someTest() throws IOException {
		FileInputStream file = new FileInputStream(Constants.EXCEL);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		driver.manage().window().maximize();
		DemoSiteExcel excel = PageFactory.initElements(driver, DemoSiteExcel.class);

		for (int rowNum = 1; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
			String user = sheet.getRow(rowNum).getCell(0).getStringCellValue();
			String pass = sheet.getRow(rowNum).getCell(1).getStringCellValue();
			
			driver.get(Constants.DEMOADD);
			excel.insertDeets(user, pass);
			driver.get(Constants.DEMOLOGIN);
			excel.insertDeets(user, pass);
			WebElement success = driver
					.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/big/blockquote/blockquote/font/center/b"));

				XSSFRow row = sheet.getRow(rowNum);
				XSSFCell cell = row.getCell(3);
				if (cell == null) {
					cell = row.createCell(3);
				}
				cell.setCellValue(success.getText());
				assertEquals("Login failed", row.getCell(3) , row.getCell(2));

			}

		
		FileOutputStream fileOut = new FileOutputStream(Constants.EXCEL);

		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();

		file.close();
		

	}
}
