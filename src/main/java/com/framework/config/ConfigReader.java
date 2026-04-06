package com.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads configuration from src/test/resources/config.properties.
 * Singleton pattern — call ConfigReader.get("key").
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
            log.info("Config loaded from: {}", path);
        } catch (IOException e) {
            log.error("Failed to load config.properties", e);
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    private ConfigReader() {}

    /**
     * Returns the value for the given property key.
     *
     * @param key property name
     * @return property value, or throws if not found
     */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found in config.properties: " + key);
        }
        return value.trim();
    }

    /**
     * Returns the value for the given key, or defaultValue if missing.
     *
     * @param key          property name
     * @param defaultValue fallback value
     * @return property value or default
     */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue).trim();
    }

    /**
     * Returns an int property value.
     *
     * @param key property name
     * @return integer value
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
