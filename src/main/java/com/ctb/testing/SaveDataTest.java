package com.ctb.testing;

import com.ctb.classes.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveDataTest {

    protected static void saveDataToFile() {
        String dataFilePath = "dataFilePath.txt";
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(dataFilePath));
            JSONArray jsonArray = new JSONArray();

            for (User user : BankSystem.users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getUserID());
                userJson.put("name", user.getName());
                userJson.put("username", User.getUsername());
                userJson.put("password", user.getPassword());
                userJson.put("isadmin", user.isAdmin());
                userJson.put("iscustomerservice", user.isCustomerService());
                userJson.put("producttype", user.getProductType());
                userJson.put("balance", user.getBalance());

                JSONArray profilesJsonArray = new JSONArray();
                for (Profile profile : user.userProfile) {
                    JSONObject profileJson = new JSONObject();
                    profileJson.put("email", profile.getEmail());
                    profileJson.put("phone", profile.getPhoneNumber());
                    profileJson.put("isTwoFactorEnabled", profile.get2FAStatus());
                    profilesJsonArray.put(profileJson);
                }
                userJson.put("profiles", profilesJsonArray);

                JSONArray transactionHistoryJsonArray = setUserTransactions(user);
                userJson.put("transactionhistory", transactionHistoryJsonArray);

                JSONArray sessionsJsonArray = new JSONArray();
                for (Session session : user.userSessions) {
                    JSONObject sessionJson = new JSONObject();
                    sessionJson.put("sessionID", session.getSessionID());
                    sessionJson.put("username", User.getUsername());
                    sessionJson.put("timestamp", session.getTimeStamp());
                    sessionsJsonArray.put(sessionJson);
                }
                userJson.put("sessions", sessionsJsonArray);

                JSONArray productApplicationsJsonArray = setUserProductApplications(user);
                userJson.put("productapplications", productApplicationsJsonArray);

                JSONArray helpAndResourcesJsonArray = setUserHelpAndResources(user);
                userJson.put("helpandresources", helpAndResourcesJsonArray);

                jsonArray.put(userJson);
            }

            bufferedWriter.write(jsonArray.toString(4));
            bufferedWriter.close();
        } catch (JSONException e) {
            System.out.println("JSON error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    private static JSONArray setUserHelpAndResources(User user) {
        JSONArray helpAndResourcesJsonArray = new JSONArray();
        for (HelpAndResources resources : user.userHelpAndResources) {
            JSONObject helpAndResourcesJson = new JSONObject();
            helpAndResourcesJson.put("helpandresourcesID", resources.getHelpID());
            helpAndResourcesJson.put("helpandresourcesType", resources.getH_rType());
            helpAndResourcesJson.put("helpandresourcesDescription", resources.getH_rDescription());
            helpAndResourcesJsonArray.put(helpAndResourcesJson);
        }
        return helpAndResourcesJsonArray;
    }

    private static JSONArray setUserProductApplications(User user) {
        JSONArray productApplicationsJsonArray = new JSONArray();
        for (ProductApplication productApplication : user.userProductApplications) {
            JSONObject productApplicationJson = new JSONObject();
            productApplicationJson.put("producttype", productApplication.getProductType());
            productApplicationJson.put("productID", productApplication.getProductID());
            productApplicationsJsonArray.put(productApplicationJson);
        }
        return productApplicationsJsonArray;
    }

    private static JSONArray setUserTransactions(User user) {
        JSONArray transactionHistoryJsonArray = new JSONArray();
        for (Transaction transaction : user.userTransaction) {
            JSONObject transactionJson = new JSONObject();
            transactionJson.put("transactionID", transaction.getTransactionID());
            transactionJson.put("transactionType", transaction.getTransactionType());
            transactionJson.put("amount", transaction.getAmount());
            transactionJson.put("timestamp", transaction.getTimeStamp());
            transactionJson.put("description", transaction.getDescription());
            transactionHistoryJsonArray.put(transactionJson);
        }
        return transactionHistoryJsonArray;
    }
}