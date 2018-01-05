import nl.haarlem.ebase.unittest.Navigator;
import nl.haarlem.ebase.unittest.Utils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebElement;

import java.util.Properties;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EbaseUnitTestExampleTest {

    private static Navigator navigator = new Navigator();
    private static Properties properties = null;

    @BeforeClass
    public static void init(){
        navigator.init("http://localhost:3050/ebasetest/elementTest.eb");
    }

    @AfterClass
    public static void cleanup(){
       // navigator.quit();
    }

    @Test
    public void t1_formStarts(){
        assertEquals("Ebase",navigator.getFormTitle());
    }

    @Test
    public void t2_selectYes() {
        navigator.selectRadioButtonValue("Yes or No?","Y");
        assertEquals(true,navigator.inputIsVisible("Demo"));
    }

    @Test
    public void t3_setValue(){
        navigator.setInputValueByFieldLabel("Demo","Hello");

    }

    @Test
    public void t4_clickButton(){
        navigator.clickButtonByValue("Next page");
        assertEquals(true,navigator.pageContainsText("Page 2"));
    }
}