package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class HelpAndResources {
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private String helpID;
    private String h_rType;
    private String h_rDescription;
    private String feedback;

    /*----------------------Setter Methods----------------------*/
    public void setHelpID(String helpID) {
        this.helpID = helpID;
    }

    public void setH_rType(String h_rType) {
        this.h_rType = h_rType;
    }

    public void setH_rDescription(String h_rDescription) {
        this.h_rDescription = h_rDescription;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /*----------------------Getter Methods----------------------*/
    public String getHelpID() {
        return helpID;
    }

    public String getH_rType() {
        return h_rType;
    }

    public String getH_rDescription() {
        return h_rDescription;
    }

    public String getFeedback() {
        return feedback;
    }

    /*----------------------Class Methods----------------------*/
    protected static String generateHelpID(final String h_rType) { // TODO: Refactor method String -> long
        long time = calendar.getTimeInMillis();
        int randomNumber = rand.nextInt();
        String timeString = Long.toString(time);
        String randomNumberString = Integer.toString(randomNumber);
        if (Objects.equals(h_rType, "AI")) {
            return "HAI" + timeString + randomNumberString;
        } else if (Objects.equals(h_rType, "Help")) {
            return "HLP" + timeString + randomNumberString;
        } else {
            return "HSR" + timeString + randomNumberString;
        }
    }

    protected static void chatBot(final String message, final String username) {
        Map<String, String> keywordToFeedback = new HashMap<>();
        keywordToFeedback.put("forgot password", "It seems like you've forgotten your password. Don't worry, you can " +
                "reset it by clicking the 'Forgot Password?' button and following the instructions provided.");

        keywordToFeedback.put("guide",
                "Looking for a guide? We have comprehensive documentation and tutorials available " +
                        "to help you navigate through our system.");

        keywordToFeedback.put("transact", "You're interested in making a transaction? At the moment, we don't have a " +
                "specific answer for this. Please refer to our transaction guide for more details.");

        keywordToFeedback.put("contact", "Need to get in touch? Our customer service team is always ready to help. " +
                "You can reach us through our Contact Us page.");

        keywordToFeedback.put("credit limit",
                "Inquiring about your credit limit? You can check this information in your " +
                        "account settings under the 'Credit Limit' section.");

        for (Map.Entry<String, String> entry : keywordToFeedback.entrySet()) {
            if (message.toLowerCase().contains(entry.getKey())) {
                String feedback = entry.getValue();
                saveHelpAndResources(username, "AI", message, feedback);
                System.out.print(feedback);
                break;
            }
        }
    }

    public static int generateUniqueHrId() {
        return new Random().nextInt(100) + 1;
    }

    protected static void saveHelpAndResources(final String username, final String h_rType, final String h_rDescription,
            final String feedback) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "INSERT INTO help_resources (hr_id, user_id, hr_type, hr_description, feedback) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Generate a unique hr_id
            long hrId = generateUniqueHrId();

            // Get the user_id of the user
            long userId = User.getUserId(username);
            if (userId == -1) {
                SecuritySystem.auditLog(false);
                return;
            }

            pstmt.setLong(1, hrId);
            pstmt.setLong(2, userId);
            pstmt.setString(3, h_rType);
            pstmt.setString(4, h_rDescription);
            pstmt.setString(5, feedback);
            pstmt.executeUpdate();

            SecuritySystem.auditLog(true);
        } catch (SQLException e) {
            e.printStackTrace();
            SecuritySystem.auditLog(false);
        }
    }
}