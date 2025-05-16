package org.example.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class JsonParserUtil {

    public JsonObject jsonObject;

    // Constructor to load the JSON file
    public JsonParserUtil(String jsonFilePath) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(jsonFilePath);
            if (resource == null) {
                System.out.println("Unable to find resource: " + jsonFilePath);
            } else {
                System.out.println("Resource found at: " + resource.getPath());
            }

            File file = new File(classLoader.getResource(jsonFilePath).getFile());

            // FileReader fileReader = new FileReader(resourcePath.toFile());
            FileReader fileReader = new FileReader(file);
            this.jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load " + jsonFilePath + " file");
        }
    }

    // Method to get the direct field
    public JsonObject getField(String fieldName) {
        return jsonObject.has(fieldName) ? jsonObject.getAsJsonObject(fieldName) : null;
    }

    // Method set the locator on the Object
    protected JsonObject setLocatorOnObject(String fieldName, String value) {
        JsonObject field = getField(fieldName);
        if (field != null && field.has("primary")) {
            field.remove("primary");
            field.addProperty("primary", value);
        }
        if (jsonObject.has(fieldName)) {
            jsonObject.remove(fieldName);
            jsonObject.add(fieldName, field);
        }
        System.out.println("Updated jsonObject: " + jsonObject);
        return jsonObject;
    }


    public String getPrimaryLocator(String fieldName) {
        JsonObject field = getField(fieldName);
        return (field != null && field.has("primary")) ? field.get("primary").getAsString() : null;
    }

    public String getfallbacksLocator(String fieldName, int index) {
        String key = "fallbacks";
        JsonObject field = getField(fieldName);
        if (field != null && field.has(key)) {
            try {
                if( field.get(key).isJsonArray()) {
                    return field.getAsJsonArray(key).get(index).getAsString();
                } else {
                    return field.get(key).getAsString();
                }
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
        return null;
    }
}