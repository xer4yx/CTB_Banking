package com.ctb.testing;

import com.ctb.classes.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.logging.Logger;

import static com.ctb.classes.BankSystem.users;

public class LoadDataTest {
    private String dataFilePath;
    private Logger logger;
    protected void loadDataFromFile() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(dataFilePath));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                data.append(line);
            }
            file.close();

            JSONObject j = new JSONObject(new JSONTokener(data.toString()));

            for (int i = 0; i < j.length(); i++) {
                JSONObject item = j.getJSONObject(String.valueOf(i));

                User user = new User();
                user.setUserID(item.optString("id", ""));
                user.setName(item.optString("name", ""));
                user.setUsername(item.optString("username", ""));
                user.setAdmin(item.optBoolean("isadmin", false));
                user.setCustomerService(item.optBoolean("iscustomerservice", false));
                user.setPassword(item.optString("password", ""));
                user.setProductType(item.optString("producttype", ""));
                user.setBalance(item.optDouble("balance", 0.0));

                if (item.has("transactionhistory")) {
                    JSONArray transactionArray = item.getJSONArray("transactionhistory");
                    for (int n = 0; i < transactionArray.length(); n++) {
                        JSONObject transactionItem = transactionArray.getJSONObject(n);

                        Transaction transaction = new Transaction();
                        transaction.setTransactionID(transactionItem.optString("transactionID", ""));
                        transaction.setTransactionType(transactionItem.optString("transactionType", ""));
                        transaction.setAmount(transactionItem.optDouble("amount", 0.0));
                        transaction.setTimeStamp(transactionItem.optLong("timestamp", 0));
                        transaction.setDescription(transactionItem.optString("description", ""));

                        user.userTransaction.add(transaction);
                    }
                }

                if (item.has("sessions")) {
                    JSONArray sessionArray = item.getJSONArray("sessions");
                    for (int k = 0; k < sessionArray.length(); k++) {
                        JSONObject sessionItem = sessionArray.getJSONObject(k);

                        Session session = new Session();
                        session.setSessionID(sessionItem.optString("sessionID", ""));
                        session.setUsername(sessionItem.optString("username", ""));
                        session.setTimeStamp(sessionItem.optLong("timestamp", 0));

                        user.userSessions.add(session);
                    }
                }

                if (item.has("productapplications")) {
                    JSONArray productApplicationArray = item.getJSONArray("productapplications");
                    for (int l = 0; l < productApplicationArray.length(); l++) {
                        JSONObject productApplicationItem = productApplicationArray.getJSONObject(l);

                        ProductApplication productApplication = new ProductApplication();
                        productApplication.setProductType(productApplicationItem.optString("producttype", ""));
                        productApplication.setProductID(productApplicationItem.optString("productID", ""));

                        user.userProductApplications.add(productApplication);
                    }
                }

                if (item.has("helpandresources")) {
                    JSONArray helpandResourcesArray = item.getJSONArray("helpandresources");
                    for (int m = 0; m < helpandResourcesArray.length(); m++) {
                        JSONObject helpandResourcesItem = helpandResourcesArray.getJSONObject(m);

                        HelpAndResources helpAndResource = new HelpAndResources();
                        helpAndResource.setHelpID(helpandResourcesItem.optString("Help ID", ""));
                        helpAndResource.setH_rType(helpandResourcesItem.optString("Type", ""));
                        helpAndResource.setH_rDescription(helpandResourcesItem.optString("Description", ""));

                        user.userHelpAndResources.add(helpAndResource);
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
}
