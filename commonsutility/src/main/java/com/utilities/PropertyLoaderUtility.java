/*
 *  @author
 *  Nidhi Chourasia created on 2018
 *
 */
package com.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class PropertyLoaderUtility {

    private static final String ENVIRONMENT = "env";
    private static final String PREFIX = "/application";
    private static final String SUFFIX = ".properties";
    private static PropertyLoaderUtility propertyLoaderUtility = new PropertyLoaderUtility();
    private static Map<String, String> propertiesMap;

    public static PropertyLoaderUtility getInstance() {
        return propertyLoaderUtility;
    }

    public String getPropertyValue(String propertyKey) {
        if (isNull(propertiesMap)) {
            propertiesMap = loadProperties();
        }
        return propertiesMap.get(propertyKey);
    }

    private Map<String, String> loadProperties() {
        propertiesMap = new HashMap<>();
        String env = System.getenv(ENVIRONMENT);
        if (nonNull(env)) {
            env = "-" + env;
            propertiesMap.putAll(load(PREFIX + env + SUFFIX));
        }
        return propertiesMap;
    }

    private Map<? extends String, ? extends String> load(String file) {
        Properties properties = new Properties();
        InputStream inputStream = PropertyLoaderUtility.class.getResourceAsStream(file);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Unable to load properties" + e);
        }
        return (Map) properties;
    }


    public static void main(String[] args) {
        PropertyLoaderUtility instance = PropertyLoaderUtility.getInstance();
        String url = instance.getPropertyValue("url");
        System.out.println("property read from propertyr file for propertyKey 'url' : " + url);
    }


}
