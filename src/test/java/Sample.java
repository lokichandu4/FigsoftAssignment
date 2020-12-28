import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Sample {

    public WebDriver driver;
    private static final long ELEMENT_INTERACTION_TIMEOUT_SECS = 120;
    public String amazonurl = "https://www.amazon.in";
    public String flipkarturl = "https://www.flipkart.com";
    String exePath = ".\\chromedriver_win32\\chromedriver.exe";
    String amazonproductname = "Apple iPhone XR (64GB) - Yellow";
    String flipkartproductname = "Apple iPhone XR (Yellow, 64 GB)";
    private By amazonSearcBox = By.cssSelector("[id='twotabsearchtextbox']");
    private By amazonSearchButton = By.cssSelector("input[type='submit'][value='Go']");
    private By amazonPhoneList = By.cssSelector("[class='a-link-normal a-text-normal']");
    private By amazonPhonePrice = By.cssSelector("[data-a-color='price']");
    private By alertclose = By.cssSelector("[class='_2AkmmA _29YdH8']");
    private By flipkartSearcBox = By.cssSelector("input[name='q']");
    private By flipkartSearchButton = By.cssSelector("button[type='submit']");
    private By flipkartPhoneList = By.cssSelector("a[href^='/apple-iphone-xr']");
    private By flipkartPhonePrice = By.cssSelector("[class^='_1vC4OE']");
    public String amazon_iPhoneXR64GBPrice;
    public String flipkart_iPhoneXR64GBPrice;
    int amazonprice,flipkartprice;

    public WebDriver getDriver() {
        return driver;
    }

    private ExpectedCondition<Boolean> readyStateExpectation = (WebDriver driver) -> {
        try {
            assert driver != null;
            return ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState")
                    .toString()
                    .equals("complete");
        } catch (Exception ignored) {
            return false;
        }
    };

    public void waitForPageLoaded(By... expectedElements) {
        new WebDriverWait(getDriver(), ELEMENT_INTERACTION_TIMEOUT_SECS).until(readyStateExpectation);
        for (By expectedElement : expectedElements) {
            waitUntilElementDisplayed(expectedElement);
        }

    }

    private boolean waitUntilElementDisplayed(By selector) {
        try {
            return getElement(selector).isDisplayed();
        }
        catch(NoSuchElementException e){
            return false;
        }
    }
    @BeforeClass
    public void launch(){
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--enable-automation",
                "--password-store=basic",
                "--use-mock-keychain",
                "--disable-background-networking",
                "--disable-background-timer-throttling",
                "--disable-client-side-phishing-detection",
                "--disable-default-apps",
                "--disable-extensions",
                "--disable-hang-monitor",
                "--disable-popup-blocking",
                "--disable-prompt-on-repost",
                "--disable-sync",
                "--disable-translate",
                "--metrics-recording-only",
                "--no-first-run",
                "--safebrowsing-disable-auto-update",
                //  "--headless",
                "--mute-audio",
                "--disable-gpu",
                "--hide-scrollbars",
                "--false-useAutomationExtension");
        System.setProperty("webdriver.chrome.driver", exePath);
        driver = new ChromeDriver(options);
        getDriver().get(amazonurl);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    public WebElement getElement(By selector) {
        try {
            return getDriver().findElement(selector);
        } catch (NoSuchElementException e) {
            Assert.fail("Unable to locate element using selector: " + selector.toString());
            throw e;
        }
    }
    @Test
    public void test() {
        waitForPageLoaded(amazonSearcBox);
        getElement(amazonSearcBox).sendKeys(amazonproductname);
        getElement(amazonSearchButton).click();
        waitForPageLoaded(amazonSearcBox);
        List<WebElement> amazonphonelist = getDriver().findElements(amazonPhoneList);
        List<WebElement> amazonphoneprice = getDriver().findElements(amazonPhonePrice);
        if(amazonphonelist.size() == amazonphoneprice.size()) {
            for (int i=0;i<amazonphonelist.size();i++) {
                String phonename = amazonphonelist.get(i).getText();
                String price = amazonphoneprice.get(i).getText().replaceAll("[^0-9]","");
                if(phonename.contains(amazonproductname)) {
                    amazon_iPhoneXR64GBPrice=price;
                    //System.out.println("Amazon "+phonename + " : " + amazon_iPhoneXR64GBPrice);
                    break;
                }
            }
        }
        getDriver().get(flipkarturl);
        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        waitForPageLoaded(flipkartSearcBox);
        getElement(alertclose).isDisplayed();
        getElement(alertclose).click();
        getElement(flipkartSearcBox).sendKeys(flipkartproductname);
        getElement(flipkartSearchButton).click();
        waitForPageLoaded(flipkartSearcBox);
        List<WebElement> flipkartphonelist = getDriver().findElements(flipkartPhoneList);
        List<WebElement> flipkartphoneprice = getDriver().findElements(flipkartPhonePrice);
        if(flipkartphonelist.size() == flipkartphoneprice.size()) {
            for (int i=0;i<flipkartphonelist.size();i++) {
                String phonename = flipkartphonelist.get(i).getText();
                String price = flipkartphoneprice.get(i).getText().replaceAll("[^0-9]","");
                if(phonename.contains(flipkartproductname)) {
                    flipkart_iPhoneXR64GBPrice=price;
                    //System.out.println("Flipkart"+ phonename + " : " + flipkart_iPhoneXR64GBPrice);
                    break;
                }
            }
        }
        amazonprice = Integer.parseInt(amazon_iPhoneXR64GBPrice);
        flipkartprice = Integer.parseInt(flipkart_iPhoneXR64GBPrice);
        if(amazonprice>flipkartprice){
            System.out.println("Apple iPhone XR (64GB) - Yellow is lower in Flipkart");
        }else{
            System.out.println("Apple iPhone XR (64GB) - Yellow is lower in Amazon");
        }
    }
    @AfterMethod
    public void teardown(){
        driver.close();
    }



}
