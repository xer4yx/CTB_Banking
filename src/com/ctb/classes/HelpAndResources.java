package com.ctb.classes;

class HelpAndResources {
    private String helpID;
    private String h_rType;
    private String h_rDescription;
    private String feedback;
    protected String generateHelpID(final String h_rType) {
        if (h_rType == "AI")
        {
            return "HAI" + to_string(time(nullptr)) + to_string(rand());
        }
        else if (h_rType == "Help")
        {
            return "HLP" + to_string(time(nullptr)) + to_string(rand());
        }
        else
        {
            return "HSR" + to_string(time(nullptr)) + to_string(rand());
        }
    }

    protected void chatBot(final String message, final String username) {
        String feedback; // Declare the feedback variable once.

        // Checks if user's message indicates they've forgotten their password.
        if (regex_search(message, regex("(forgot)(.*)(password)", regex_constants::icase)))
        {
            feedback = "It seems like you've forgotten your password. Don't worry, you can reset it by clicking the 'Forgot Password?' button and following the instructions provided.";
            cout << feedback;
        }

        // Responds to requests for a guide or tutorial.
        if (regex_search(message, regex("(guide)", regex_constants::icase)))
        {
            feedback = "Looking for a guide? We have comprehensive documentation and tutorials available to help you navigate through our system.";
            cout << feedback;
        }

        // Directs users to a transaction guide if they mention "transact".
        if (regex_search(message, regex("(transact)", regex_constants::icase)))
        {
            feedback = "You're interested in making a transaction? At the moment, we don't have a specific answer for this. Please refer to our transaction guide for more details.";
            cout << feedback;
        }

        // Provides information on how to contact customer service.
        if (regex_search(message, regex("(contact)", regex_constants::icase)))
        {
            feedback = "Need to get in touch? Our customer service team is always ready to help. You can reach us through our Contact Us page.";
            cout << feedback;
        }

        // Assists users with inquiries about their credit limit.
        if (regex_search(message, regex("(credit)(.*)(limit)", regex_constants::icase)))
        {
            feedback = "Inquiring about your credit limit? You can check this information in your account settings under the 'Credit Limit' section.";
            cout << feedback;
        }

        saveHelpAndResources(username, "AI", message, feedback);
    }

    protected static void saveHelpAndResources(final String username, final String h_rType,
                              final String h_rDescription, final String feedback) {}

}
