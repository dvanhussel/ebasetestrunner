
import com.gargoylesoftware.htmlunit.BrowserVersion;
import net.vanhussel.ebase.unittest.Navigator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Properties;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EbaseUnitTestExampleTest {

    private static Navigator navigator = new Navigator();
    private static Properties properties = null;

    @BeforeClass
    public static void init(){
        navigator.init("http://localhost:3050/ebasetest/demoForm.eb", BrowserVersion.BEST_SUPPORTED);
    }

    @AfterClass
    public static void cleanup(){
       navigator.quit();
    }

    @Test
    public void t1_formStarts(){
        assertEquals("Ebase",navigator.getFormTitle());
    }

    @Test
    public void t2_selectYes() throws InterruptedException {
        navigator.selectRadioButtonValue("Yes or No?","Y");
        assertEquals(true,navigator.inputIsVisible("Demo"));
    }

    @Test
    public void t3_setValue(){
        navigator.setInputValueByFieldLabel("Demo","Hello");
    }

    @Test
    public void t4_selectDropdownValue(){
        navigator.selectDropdownValueByFieldLabel("Dropdown","2");
        assertEquals(true,navigator.pageContainsText("2 is selected"));
    }

    @Test
    public void t5_textareaValue(){
      navigator.setTextareaValueByFieldLabel("Textarea","Textarea");
    }

    @Test
    public void t6_clickButton(){
        navigator.clickButtonByValue("Next page");
        assertEquals(true,navigator.pageContainsText("Page 2"));
    }
}