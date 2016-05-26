/*
 * This program is an Voice over IP client for Android devices.
 * Copyright (C) 2016 Andreas Sekulski, Dimitry Kotlovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
