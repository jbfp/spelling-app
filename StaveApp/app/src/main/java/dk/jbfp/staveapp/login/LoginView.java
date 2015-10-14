package dk.jbfp.staveapp.login;

import java.util.List;

import dk.jbfp.staveapp.User;

public interface LoginView {
    void showUsers(List<User> users);
    void navigateToRegisterActivity();
    void navigateToStepsActivity(int userId, String[] words);
}
