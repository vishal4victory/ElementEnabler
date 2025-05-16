package org.example.utils;

import com.github.javafaker.Faker;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateUserPayload {

    public static JSONObject generatePayload() {
        Faker faker = new Faker();
        JSONObject payload = new JSONObject();
        payload.put("name", faker.name().fullName());
        payload.put("username", faker.name().username());
        payload.put("email", faker.internet().emailAddress());
        payload.put("phone", faker.phoneNumber().cellPhone());
        payload.put("address", faker.address().fullAddress());
        payload.put("password", faker.internet().password());
        return payload;
    }

    public static void writeToFile(JSONObject payload) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "payload-" + timestamp + ".json";

        // File path
        String filePath = "src/main/resources/testdata/" + fileName;

        // Ensure directory exists
        File dir = new File("src/main/resources/testdata");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Write JSON to file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(payload.toString(4));  // Pretty-print with 4-space indent
            System.out.println("JSON file created at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JSONObject payload = generatePayload();
        writeToFile(payload);
    }
}
