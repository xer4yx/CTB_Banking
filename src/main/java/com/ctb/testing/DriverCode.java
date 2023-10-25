package com.ctb.testing;

import com.ctb.classes.Profile;
import com.ctb.classes.User;

public class DriverCode {
    public static void main(String[] args){
        Profile userProfile = new Profile();
        User user = new User();

        user.setUsername("Xerayx");

        userProfile.setEmail("angelbicombz626@gmail.com");
        userProfile.setPhoneNumber("09288650313");
        userProfile.set2FAStatus(true);

        userProfile.displayProfile(user.getUsername());
    }
}

