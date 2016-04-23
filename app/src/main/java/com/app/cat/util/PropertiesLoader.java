package com.app.cat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Class to load property files.
 *
 * @author Andreas Sekulski
 */
public class PropertiesLoader {

    /**
     * Load all given properties from an given file input stream. Stream will be closed if operation
     * successfully.
     *
     * @param file Open input file.
     * @param properties Properties which should be get from property file.
     * @return Map which conatins all properties if exists otherwise value will be null from key.
     * @throws IOException Throws an IOException if file is not an property file.
     */
    public static Map<String, String> loadProperty(InputStream file, List<String> properties) throws IOException {

        Map<String, String> props = new HashMap<String, String>();
        Properties prop = new Properties();

        try {
            // load a properties file
            prop.load(file);

            for(String property : properties) {
                // Store all properties to map.
                props.put(property, prop.getProperty(property));
            }
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return props;
    }

}
