package com.ctb.testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TestCase {
    private String dataFilePath;

    protected void loadDataFromFile() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(dataFilePath));
            String data = "";
            String line;
            while ((line = file.readLine()) != null) {
                data += line;
            }
            file.close();

            JSONObject j = new JSONObject(new JSONTokener(data));

            for (int i = 0; i < j.length(); i++) {
                JSONObject item = j.getJSONObject(String.valueOf(i));

                User user = new User();
                user.userID = item.optString("id", "");
                user.name = item.optString("name", "");
                user.username = item.optString("username", "");
                user.isadmin = item.optBoolean("isadmin", false);
                user.iscustomerservice = item.optBoolean("iscustomerservice", false);
                user.password = item.optString("password", "");
                user.producttype = item.optString("producttype", "");
                user.balance = item.optDouble("balance", 0.0);

                if (item.has("transactionhistory")) {
                    JSONArray transactionArray = item.getJSONArray("transactionhistory");
                    for (int j = 0; j < transactionArray.length(); j++) {
                        JSONObject transactionItem = transactionArray.getJSONObject(j);

                        Transaction transaction = new Transaction();
                        transaction.transactionID = transactionItem.optString("transactionID", "");
                        transaction.transactionType = transactionItem.optString("transactionType", "");
                        transaction.amount = transactionItem.optDouble("amount", 0.0);
                        transaction.timestamp = transactionItem.optLong("timestamp", 0);
                        transaction.description = transactionItem.optString("description", "");

                        user.transactionhistory.add(transaction);
                    }
                }

                if (item.has("sessions")) {
                    JSONArray sessionArray = item.getJSONArray("sessions");
                    for (int j = 0; j < sessionArray.length(); j++) {
                        JSONObject sessionItem = sessionArray.getJSONObject(j);

                        Session session = new Session();
                        session.sessionID = sessionItem.optString("sessionID", "");
                        session.username = sessionItem.optString("username", "");
                        session.timestamp = sessionItem.optLong("timestamp", 0);

                        user.sessions.add(session);
                    }
                }

                if (item.has("productapplications")) {
                    JSONArray productApplicationArray = item.getJSONArray("productapplications");
                    for (int j = 0; j < productApplicationArray.length(); j++) {
                        JSONObject productApplicationItem = productApplicationArray.getJSONObject(j);

                        ProductApplication productApplication = new ProductApplication();
                        productApplication.producttype = productApplicationItem.optString("producttype", "");
                        productApplication.productID = productApplicationItem.optString("productID", "");

                        user.productapplications.add(productApplication);
                    }
                }

                if (item.has("helpandresources")) {
                    JSONArray helpandResourcesArray = item.getJSONArray("helpandresources");
                    for (int j = 0; j < helpandResourcesArray.length(); j++) {
                        JSONObject helpandResourcesItem = helpandResourcesArray.getJSONObject(j);

                        HelpAndResource helpAndResource = new HelpAndResource();
                        helpAndResource.id = helpandResourcesItem.optString("id", "");
                        helpAndResource.title = helpandResourcesItem.optString("title", "");
                        helpAndResource.url = helpandResourcesItem.optString("url", "");

                        user.helpandresources.add(helpAndResource);
                    }
                }

                users.add(user);
            }
        } catch (IOException e) {
            logger.severe("Failed to read from file: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Failed to parse JSON data: " + e.getMessage());
        }
    }

    protected void saveDataToFile() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (User user : users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.userID);
                userJson.put("name", user.name);
                userJson.put("username", user.username);
                userJson.put("isadmin", user.isadmin);
                userJson.put("iscustomerservice", user.iscustomerservice);
                userJson.put("password", user.password);
                userJson.put("producttype", user.producttype);
                userJson.put("balance", user.balance);

                JSONArray transactionHistoryArray = new JSONArray();
                for (Transaction transaction : user.transactionhistory) {
                    JSONObject transactionJson = new JSONObject();
                    transactionJson.put("transactionID", transaction.transactionID);
                    transactionJson.put("transactionType", transaction.transactionType);
                    transactionJson.put("amount", transaction.amount);
                    transactionJson.put("timestamp", transaction.timestamp);
                    transactionJson.put("description", transaction.description);

                    transactionHistoryArray.put(transactionJson);
                }
                userJson.put("transactionhistory", transactionHistoryArray);

                JSONArray sessionsArray = new JSONArray();
                for (Session session : user.sessions) {
                    JSONObject sessionJson = new JSONObject();
                    sessionJson.put("sessionID", session.sessionID);
                    sessionJson.put("username", session.username);
                    sessionJson.put("timestamp", session.timestamp);

                    sessionsArray.put(sessionJson);
                }
                userJson.put("sessions", sessionsArray);

                JSONArray productApplicationsArray = new JSONArray();
                for (ProductApplication productApplication : user.productapplications) {
                    JSONObject productApplicationJson = new JSONObject();
                    productApplicationJson.put("producttype", productApplication.producttype);
                    productApplicationJson.put("productID", productApplication.productID);

                    productApplicationsArray.put(productApplicationJson);
                }
                userJson.put("productapplications", productApplicationsArray);

                JSONArray helpandResourcesArray = new JSONArray();
                for (HelpAndResource helpAndResource : user.helpandresources) {
                    JSONObject helpAndResourceJson = new JSONObject();
                    helpAndResourceJson.put("id", helpAndResource.id);
                    helpAndResourceJson.put("title", helpAndResource.title);
                    helpAndResourceJson.put("url", helpAndResource.url);

                    helpandResourcesArray.put(helpAndResourceJson);
                }
                userJson.put("helpandresources", helpandResourcesArray);

                jsonArray.put(userJson);
            }

            String jsonData = jsonArray.toString(4);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(jsonData);
            }
        } catch (IOException e) {
            logger.severe("Failed to write to file: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Failed to save data to file: " + e.getMessage());
        }
    }
\end{code}

    Please note that these code snippets assume the use of a logger, so make sure to initialize and configure one
}
