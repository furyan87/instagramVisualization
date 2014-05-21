package de.prochnow.instaScraper.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.prochnow.instaScraper.InstaScraper;


public class PropertyReader {

    private static final Properties prop = new Properties();

    private static final String PROP_FILE_NAME = "config.properties";


    public static String getProperty(final String propertyString) {

        InputStream input = null;

        String result = "";

        try {

            input = InstaScraper.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
            if(input==null){
                System.out.println("Sorry, unable to find " + PROP_FILE_NAME);
                return result;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            result = prop.getProperty(propertyString);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

    }
}
