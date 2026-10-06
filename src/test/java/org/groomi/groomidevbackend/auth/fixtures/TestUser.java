package org.groomi.groomidevbackend.auth.fixtures;

import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.dashboard.UserDashboard;

public class TestUser {
    public static UserDashboard hasAllDashboardInformation(){
        return new UserDashboard(
                "Jimmie",
                "Smith",
                "masterbranchmistress@gmail.com"
        );
    }
}
