package net.vanhussel.ebase.unittest;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Navigator {
    private static WebDriver driver = null;
    private static Properties properties = null;
    WebDriverWait wait = null;
    private static BrowserVersion browserVersion = null;

    /**
     * Initializes the Selium webdriver
     *
     * If no browserversion is supplied the gecko webdriver is used (en should be configured in the application properties).
     * @param browserVersion
     */
    private void initializeDriver(BrowserVersion browserVersion){
        properties = Utils.readPropertiesFromFile("application.properties");
        System.setProperty("webdriver.gecko.driver", properties.getProperty("geckodriver"));

        if(browserVersion != null){
            this.browserVersion = browserVersion;
            driver = new HtmlUnitDriver(this.browserVersion,true);
        } else {
            driver = new FirefoxDriver();
        }

        wait = new WebDriverWait(driver,Long.parseLong(properties.getProperty("timeout"), 10) );

        driver.manage().timeouts().implicitlyWait(Long.parseLong(properties.getProperty("timeout")), TimeUnit.SECONDS);

        //Disable console logger to prevent Webdriver to log all CSS-errors
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
    }

    /**
     * Sets the time in seconds that Selenium will implicitly wait for the searched HTML element to appear on the page
     * @param seconds
     */
    public void setTimeout(Integer seconds){
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /**
     * This will initialize the Selenium webdriver without loading a form
     */
    public void init(){
        this.initializeDriver(null);
    }

    /**
     * This wil initialize the Selenium webdriver and load the form with this URL
     * @param url
     */
    public void init (String url) {
        this.initializeDriver(null);
        this.loadForm(url);
    }

    /**
     * This wil initialize the Selenium webdriver and load the form with this URL
     * @param url
     */
    public void init (String url, BrowserVersion browserVersion) {
        this.initializeDriver(browserVersion);
        this.loadForm(url);
    }

    /**
     * Gets the radiobutton option for this field with this value
     * @param fieldLabel
     * @param value
     * @return
     */
    private WebElement getRadioButtonByFieldLabelAndValue(String fieldLabel,String value){
        //Get all radio buttons that have this label as title
        List<WebElement> radioButtons = driver.findElements(By.cssSelector("input[title='"+fieldLabel+"']"));
        //Get the radiobutton that has de value that should be selected
        WebElement radioButton = radioButtons.stream()
                .filter(el->el.getAttribute("value").equals(value))
                .collect(Collectors.toList())
                .get(0);

       return  radioButton;
    }

    /**
     * Sets a field of the radiobutton type to the supplied value. The field is found by its fieldlabel.
     * @param fieldLabel
     * @param value
     */
    public void selectRadioButtonValue(String fieldLabel,String value)  {
        getRadioButtonByFieldLabelAndValue(fieldLabel,value).click();
        //((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", radioButtonToSelect);
        //Wait for AJAX calls and reset value
        Utils.wait(1000);
        getRadioButtonByFieldLabelAndValue(fieldLabel,value).click();

        //Also set value with JavaScript, otherwise the value won't set correctly when headless mode is used.
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", getRadioButtonByFieldLabelAndValue(fieldLabel,value));
    }

    /**
     * Gets the Selenium WebElement by its Ebase fieldlabel.
     * @param fieldLabel
     * @return
     */
    private WebElement getInputByFieldLabel(String fieldLabel){
        return  wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[title='"+fieldLabel+"']")));
    }

    /**
     * Gets the Selenium WebElement by its Ebase fieldlabel.
     * @param fieldLabel
     * @return
     */
    private WebElement getTextareaByFieldLabel(String fieldLabel){
        return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("textarea[title='"+fieldLabel+"']")));
    }


    /**
     * Gets the Selenium WebElement by its Ebase fieldlabel.
     * @param fieldLabel
     * @return
     */
    private WebElement getSelectByFieldLabel(String fieldLabel){
        return driver.findElement(By.cssSelector("select[title='"+fieldLabel+"']"));
    }

    /**
     * Checks if this input (found by its fieldlabel) is visible.
     * @param fieldLabel
     * @return
     */
    public boolean inputIsVisible(String fieldLabel){
        return this.getInputByFieldLabel(fieldLabel).isDisplayed();
    }

    /**
     * Clears the input of its current value and set it to the provided value.
     * @param fieldLabel
     * @param value
     */
    public void setInputValueByFieldLabel(String fieldLabel,String value) {
        WebElement element = this.getInputByFieldLabel(fieldLabel);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '"+value+"';",element);
    }

    /**
     * Gets the current value of this form input
     * @param fieldLabel
     * @return
     */
    public String getInputValueByFieldLabel(String fieldLabel){
        WebElement element = this.getInputByFieldLabel(fieldLabel);
        return this.getInputByFieldLabel(fieldLabel).getAttribute("value");
    }

    /**
     * Clears the textarea of its current value and set it to the provided value.
     * @param fieldLabel
     * @param value
     */
    public void setTextareaValueByFieldLabel(String fieldLabel,String value){
        WebElement element = this.getTextareaByFieldLabel(fieldLabel);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '"+value+"';",element);
    }

    /**
     * Sets the dropdown to provided value.
     * @param fieldLabel
     * @param value
     */
    public void selectDropdownValueByFieldLabel(String fieldLabel,String value){
        Select element = new Select(this.getSelectByFieldLabel(fieldLabel));
        element.selectByValue(value);

        //wait for possible AJAX trigger that can detach the element from the DOM, then get new reference to same element and reset value
        Utils.wait(1500);
        element = new Select(this.getSelectByFieldLabel(fieldLabel));
        element.selectByValue(value);

    }


    /**
     * Click the Ebase button that has this text as value.
     * @param value
     */
    public void clickButtonByValue(String value){
        try{
            WebElement element = driver.findElement(By.cssSelector("input[type='submit'][value='"+value+"']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();",element);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Cheks if this text is displayed anywhere on the page
     * @param text
     * @return
     */
    public boolean pageContainsText(String text){
        return driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]")).size() > 0;
    }

    /**
     * Load the form by its complete URL (i.e https://www.haarlem.ni/ufs/kenteken_activeren.eb).
     * @param url
     */
    public void loadForm(String url){
        driver.get(url);
    }

    /**
     * Close the driver sesison and start a new one and load the form by its complete URL (i.e https://www.haarlem.ni/ufs/kenteken_activeren.eb).
     * @param url
     * @param newSession
     */
    public void loadForm(String url,boolean newSession){
        if(newSession){
            driver.quit();
            driver = null;
            init(url,this.browserVersion);
        }
        driver.get(url);
    }

    /**
     * End the Selenum webdriver session
     */
    public void quit(){
        driver.quit();
    }

    /**
     * Get the HTML title
     * @return
     */
    public String getFormTitle(){
        return driver.getTitle();
    }
}
