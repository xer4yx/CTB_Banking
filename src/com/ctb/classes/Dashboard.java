package com.ctb.classes;

public class Dashboard {
    private String dashboardID;
    private String dashboardContent;

    /*----------------------Setter Methods----------------------*/
    public void setDashboardID(String dashboardID) {this.dashboardID = dashboardID;}
    public void setDashboardContent(String dashboardContent) {this.dashboardContent = dashboardContent;}

    /*----------------------Getter Methods----------------------*/
    public String getDashboardID() {return dashboardID;}
    public String getDashboardContent() {return dashboardContent;}

    /*----------------------Class Methods----------------------*/
    public void displayDashboard(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                if (isadmin(username))
                {
                ::system("cls");
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║            Administrator            ║    " << endl;
                    cout << "╚═════════════════════════════════════╝    " << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Manage Users                    ║     " << endl;
                    cout << "║  2. Help & Resources                ║     " << endl;
                    cout << "║  3. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
                else if (iscustomerservice(username))
                {
                ::system("cls");
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║          Customer Service           ║    " << endl;
                    cout << "╚═════════════════════════════════════╝    " << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Messages                        ║     " << endl;
                    cout << "║  2. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
                else
                {
                ::system("cls");
                    SetConsoleOutputCP(CP_UTF8);
                    cout << " " << endl;
                    cout << "╭─────────────────────────────────────╮" << endl;
                    cout << "│         CENTRAL TRUST BANK          │" << endl;
                    cout << "╰─────────────────────────────────────╯" << endl;
                    cout << " " << endl;
                    cout << " Welcome " << user.name << "!" << endl;
                    cout << "  " << endl;
                    cout << " Current Balance: $" << getCurrentBalance(username) << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Transaction Center              ║     " << endl;
                    cout << "║  2. User Profile                    ║     " << endl;
                    cout << "║  3. Data Analytics Dashboard        ║     " << endl;
                    cout << "║  4. Help & Resources                ║     " << endl;
                    cout << "║  5. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
            }
        }
    }
}
