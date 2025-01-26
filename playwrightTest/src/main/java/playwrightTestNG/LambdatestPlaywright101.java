package playwrightTestNG;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.SelectOption;

public class LambdatestPlaywright101 {
	
	Playwright playwright;
	Browser browser;
	String caps;
	String cdpUrl;
	Page page;
    SoftAssert softAssert = new SoftAssert();
	@BeforeMethod
	@Parameters({"browserName", "browserVersion", "platform"})
	public void setup(String browsername, String browserVersion, String platform) throws UnsupportedEncodingException {
		JsonObject capabilities = new JsonObject();
		JsonObject ltOptions = new JsonObject();

		String user = "abhishek_yelne";
		String accessKey = "8uEOzmj44bEFX5SWISEjw3W3GXlh5ghLZES9nlLKxEFESEwPCt";

		capabilities.addProperty("browsername", browsername); // Browsers allowed: `Chrome`, `MicrosoftEdge`,
															// `pw-chromium`, `pw-firefox` and `pw-webkit`
		capabilities.addProperty("browserVersion", browserVersion);
		ltOptions.addProperty("platform", platform);
		ltOptions.addProperty("name", "Playwright 101 Test Firefox macOS Monterey");
		ltOptions.addProperty("build", "Playwright 101 Exam Assignment");
		ltOptions.addProperty("user", user);
		ltOptions.addProperty("accessKey", accessKey);
		ltOptions.addProperty("visual", true);
		ltOptions.addProperty("video", true);
		ltOptions.addProperty("network", true);
		capabilities.add("LT:Options", ltOptions);
		
		playwright = Playwright.create();
		switch(browsername) {
		case "pw-firefox":
			BrowserType chromium = playwright.firefox();
			caps = URLEncoder.encode(capabilities.toString(), "utf-8");
			cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + caps;
			browser = chromium.connect(cdpUrl);
			break;
		case "pw-chromium":
			BrowserType firefox = playwright.chromium();
			caps = URLEncoder.encode(capabilities.toString(), "utf-8");
			cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + caps;
			browser = firefox.connect(cdpUrl);
			break;
		}
		page = browser.newPage();
	}

	//fill your code
	@Test(priority=1)
	public void test_scenario1() throws Exception {
		
		page.navigate("https://www.lambdatest.com/selenium-playground");
        page.click("text=Simple Form Demo");

        softAssert.assertTrue(page.url().contains("simple-form-demo"), "The page URL does not contain 'simple-form-demo'");
        
        String message = "Welcome to LambdaTest";
        
        //using id locator
        page.fill("#user-message", message);

        //using xpath locator
        page.click("//button[@id='showInput']");

        String displayedMessage = page.innerText("#message");
        
        softAssert.assertEquals(displayedMessage, message, "The message displayed does not match with the one that was entered");
        System.out.println(displayedMessage);
        softAssert.assertAll();
	}

	@Test(priority=2)
	public void test_scenario2() throws Exception {
		
		page.navigate("https://www.lambdatest.com/selenium-playground");

        page.waitForSelector("text=Drag & Drop Sliders");
        page.click("text=Drag & Drop Sliders");

        Locator slider = page.locator("//input[@value=15]");
        BoundingBox initialBBox = slider.boundingBox();

        // Dragging the slider to 95
        slider.hover();
        page.mouse().move(initialBBox.x + initialBBox.width / 2, initialBBox.y + initialBBox.height / 2); // Hover at the center of the slider
        page.mouse().down();
        page.mouse().move(initialBBox.x + 465, initialBBox.y);  // Adjust to the right
        page.mouse().up();

        Locator outputElement = page.locator("#rangeSuccess");

        String outputText = outputElement.textContent();
        
    	softAssert.assertEquals(outputText, "95", "Slider value does not equal 95");
    	softAssert.assertAll();
	}

	@Test(priority=3)
	public void test_scenario3() throws Exception {
		
		page.navigate("https://www.lambdatest.com/selenium-playground");

        page.locator("text=Input Form Submit").click();

        Locator submitBtn = page.locator("//button[text()='Submit']");
        submitBtn.click();

        String validationMsg = page.locator("//input[@name='name']").getAttribute("validationMessage");
        String expectedErrorMsg = "Please fill out this field.";
        softAssert.assertEquals(validationMsg, expectedErrorMsg, "Error message not as expected");

        page.fill("#name", "Abhishek Y");
        
        page.fill("//input[@placeholder='Email']", "abhisheky@gmail.com");

        page.fill("//input[@placeholder='Password']", "pass!341");

        page.fill("//input[@placeholder='Company']", "Persistent Systems");

        page.fill("#websitename", "www.persistent.com");

        page.selectOption("select[name='country']", new SelectOption().setLabel("United States"));

        page.fill("#inputCity", "New York City");
        
        page.fill("#inputAddress1", "Plot 25, near RJ Road");
        
        page.fill("#inputAddress2", "Greater County, New York City");
        
        page.fill("//input[@placeholder='State']", "New York");
        
        page.fill("//input[@name='zip']", "213067");
        
        submitBtn.click();

        softAssert.assertTrue(page.locator("text=Thanks for contacting us, we will get back to you shortly.").isVisible(),
                "Success message is not visible");

        softAssert.assertAll();
	}

	@AfterMethod
	public void quitDriver() {
		browser.close();
		playwright.close();
	}
	
}
