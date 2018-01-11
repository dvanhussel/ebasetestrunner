package net.vanhussel.ebase.unittest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    /**
     * Reads the properties file
     */
    public static Properties readPropertiesFromFile(String propertiesFile) {
        Properties properties = new Properties();
        try {
            InputStream is = Navigator.class.getClassLoader().getResourceAsStream(propertiesFile);
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Suspends the execution for given amount of milliseconds
     * @param milliseconds
     */
    public static void wait(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
