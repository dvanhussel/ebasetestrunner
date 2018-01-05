package nl.haarlem.ebase.unittest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Navigator {
    private static WebDriver driver = null;
    private static Properties properties = null;

    /**
     * Initializes the Selium webdriver to use Firefox
     */
    private void initializeDriver(){
        properties = Utils.readPropertiesFromFile("application.properties");
        System.setProperty("webdriver.gecko.driver", properties.getProperty("geckodriver"));
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Long.parseLong(properties.getProperty("timeout")), TimeUnit.SECONDS);
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
        this.initializeDriver();
    }

    /**
     * This wil initialize the Selenium webdriver and load the form with this URL
     * @param url
     */
    public void init (String url) {
        this.initializeDriver();
        this.loadForm(url);
    }

    /**
     * Sets a field of the radiobutton type to the supplied value. The field is found by its fieldlabel.
     * @param fieldLabel
     * @param value
     */
    public void selectRadioButtonValue(String fieldLabel,String value){
        //Get all radio buttons that have this label as title
        List<WebElement> radioButtons = driver.findElements(By.cssSelector("input[title='"+fieldLabel+"']"));
        //Get the radiobutton that has de value that should be selected
        WebElement radioButtonToSelect = radioButtons.stream()
                .filter(el->el.getAttribute("value").equals(value))
                .collect(Collectors.toList())
                .get(0);

        //select radioButton
        radioButtonToSelect.click();
    }

    /**
     * Gets the Selenium WebElement by its Ebase fieldlabel.
     * @param fieldLabel
     * @return
     */
    private WebElement getInputByFieldLabel(String fieldLabel){
        return driver.findElement(By.cssSelector("input[title='"+fieldLabel+"']"));
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
    public void setInputValueByFieldLabel(String fieldLabel,String value){
        WebElement element = this.getInputByFieldLabel(fieldLabel);
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Click the Ebase button that has this text as value.
     * @param value
     */
    public void clickButtonByValue(String value){
        WebElement element = driver.findElement(By.cssSelector("input[type='submit'][value='"+value+"']"));
        element.click();
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
