package base;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.InputStream;
import java.util.Properties;

public class Base {

    private static Properties prop = new Properties();



    static {
        try (InputStream input = Base.class.getClassLoader().getResourceAsStream("config.properties")){
           prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load the properties file : "+e.getMessage());
        }
    }

    public static String getPropValue(String key){
        return prop.getProperty(key);
    }

    // --- Allure Attachments for richer logs ---
    public static void logInfo(String message) {
        Allure.addAttachment("DBValidation log", "text/plain", message);
    }

    public static void logError(Throwable error)
    {
        Allure.addAttachment("DBValidation log", "text/plain", error.getMessage());
    }


}
