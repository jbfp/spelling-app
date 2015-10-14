package dk.jbfp.staveapp.login;

import java.util.List;

import dk.jbfp.staveapp.User;

public interface LoginView {
    void showUsers(List<User> users);
    void navigateToRegisterActivity();
    void navigateToStartActivity(int userId, String[] words);
}
