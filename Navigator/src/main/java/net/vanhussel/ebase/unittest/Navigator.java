package net.vanhussel.ebase.unittest;


import org.apache.xpath.operations.Bool;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Navigator {
    private static WebDriver driver = null;
    private static Properties properties = null;
    WebDriverWait wait = null;

    /**
     * Initializes the Selium webdriver to use Firefox
     */
    private void initializeDriver(Boolean headless){
        properties = Utils.readPropertiesFromFile("application.properties");
        System.setProperty("webdriver.gecko.driver", properties.getProperty("geckodriver"));

        if(headless){
            driver = new HtmlUnitDriver(true);
        } else {
            driver = new FirefoxDriver();
        }

        wait = new WebDriverWait(driver, 10);

        driver.manage().timeouts().implicitlyWait(Long.parseLong(properties.getProperty("timeout")), TimeUnit.SECONDS);
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
        this.initializeDriver(true);
    }

    /**
     * This wil initialize the Selenium webdriver and load the form with this URL
     * @param url
     */
    public void init (String url) {
        this.initializeDriver(true);
        this.loadForm(url);
    }

    /**
     * This wil initialize the Selenium webdriver and load the form with this URL
     * @param url
     */
    public void init (String url, Boolean headless) {
        this.initializeDriver(headless);
        this.loadForm(url);
    }

    /**
     * Sets a field of the radiobutton type to the supplied value. The field is found by its fieldlabel.
     * @param fieldLabel
     * @param value
     */
    public void selectRadioButtonValue(String fieldLabel,String value)  {
        //Get all radio buttons that have this label as title
        List<WebElement> radioButtons = driver.findElements(By.cssSelector("input[title='"+fieldLabel+"']"));
        //Get the radiobutton that has de value that should be selected
        WebElement radioButtonToSelect = radioButtons.stream()
                .filter(el->el.getAttribute("value").equals(value))
                .collect(Collectors.toList())
                .get(0);

        //select radioButton
        radioButtonToSelect.click();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        element.click();
        element.clear();
        element.sendKeys(value);
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
        element.click();
        element.clear();
        element.sendKeys(value);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the dropdown to provided value.
     * @param fieldLabel
     * @param value
     */
    public void selectDropdownValueByFieldLabel(String fieldLabel,String value){
        Select element = new Select(this.getSelectByFieldLabel(fieldLabel));
        element.selectByValue(value);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebElement getInputElementByFieldLabel(String fieldLabel){
        return this.getInputByFieldLabel(fieldLabel);
    }

    /**
     * Click the Ebase button that has this text as value.
     * @param value
     */
    public void clickButtonByValue(String value){
        try{

            //driver.findElement(By.xpath("/html/body")).click();


            System.out.println("Button click requested for button: "+value);
            WebElement element = driver.findElement(By.cssSelector("input[type='submit'][value='"+value+"']"));
            System.out.println("Button found: "+element.getAttribute("value"));

            element.click();
            System.out.println("Button clicked");
        } catch(Exception ex){
            ex.printStackTrace();
        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Click the Ebase button that has this text as value and waits the specified amount of milliseconds
     * @param value
     * @param wait
     */
    public void clickButtonByValue(String value,int wait){
        WebElement element = driver.findElement(By.cssSelector("input[type='submit'][value='"+value+"']"));
        element.click();
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
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

    public void waitForJStoLoad() {

        JavascriptExecutor js = (JavascriptExecutor)driver;

        for (int i=0; i<25; i++){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {}
            //To check page ready state.
            if (js.executeScript("return document.readyState").toString().equals("complete")){
                System.out.println("Js is ready");
                break;
            }
        }

    }



}
