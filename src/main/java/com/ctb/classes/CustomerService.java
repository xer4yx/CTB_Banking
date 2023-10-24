package com.ctb.classes;

class CustomerService {
    protected static void displayHelpHistory(final String username) {
        bool helpFound = false;

        for (const User &user : users)
        {
            if (user.username == username)
            {
                cout << "╔═════════════════════════════════════════════════════════════╗    " << endl;
                cout << "║                        Help History                         ║    " << endl;
                cout << "╚═════════════════════════════════════════════════════════════╝    " << endl;
                cout << "───────────────────────────────────────────────────────────────" << endl;
                for (const HelpandResources &resources : user.helpandresources)
                {

                    cout << "Help ID: " << resources.helpID << endl;
                    cout << "Type: " << resources.helpandresourcesType << endl;
                    cout << "Description: " << resources.helpandresourcesDescription << endl;
                    if (resources.feedback != "")
                    {
                        cout << "Feedback: " << resources.feedback << endl;
                    }
                    else
                    {
                        cout << "Feedback: No feedback yet." << endl;
                    }
                    cout << "───────────────────────────────────────────────────────────────" << endl;

                    helpFound = true;
                }
            }
        }

        if (!helpFound)
        {
            cout << "No Help history is available for the user " << username << "." << endl;
        }
    }

    protected static void displayAllHR() {
        cout << " " << endl;
        cout << "╭───────────────────────────────────────────╮" << endl;
        cout << "│             Help & Resources              │" << endl;
        cout << "╰───────────────────────────────────────────╯" << endl;
        cout << "────────────────────────────────────────────" << endl;
        const string desiredType = "Help"; // Change this to the type you want to display
        bool helpFound = false;            // Initialize a boolean flag to check if any Help is found

        for (const User &user : users)
        {
            for (const HelpandResources &resources : user.helpandresources)
            {
                if (resources.helpandresourcesType == desiredType)
                {
                    cout << "Help ID: " << resources.helpID << endl;
                    cout << "Type: " << resources.helpandresourcesType << endl;
                    cout << "Description: " << resources.helpandresourcesDescription << endl;
                    cout << "Feedback: " << resources.feedback << endl;
                    cout << "────────────────────────────────────────────" << endl;
                    cout << " " << endl;

                    helpFound = true; // Set the flag to true if Help is found
                }
            }
        }

        if (!helpFound)
        {
            cout << "No Help is available." << endl;
        }
    }

    protected static void replyToHelp() {
        string helpID;
        cout << "\nEnter the help ID of the help and resources to reply to: ";
        cin >> helpID;
        cin.ignore();

        string feedback;
        cout << "Enter your feedback: ";
        getline(cin, feedback);

        for (User &user : users)
        {
            for (HelpandResources &resources : user.helpandresources)
            {
                if (resources.helpID == helpID)
                {
                    resources.feedback = feedback;
                    cout << "Feedback saved successfully." << endl;
                    saveDataToFile();
                    return;
                }
            }
        }

        cout << "Help and resources with ID '" << helpID << "' not found." << endl;
    }
}
