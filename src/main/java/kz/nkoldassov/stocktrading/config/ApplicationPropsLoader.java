package kz.nkoldassov.stocktrading.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropsLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ApplicationPropsLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("yBgM18iZ :: Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("8rdz5zQh :: Failed to load properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}