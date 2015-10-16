package dk.jbfp.staveapp.login;

import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.UserRepository;

public class LoginPresenter {
    private UserRepository users;
    private LoginView view;

    public LoginPresenter(UserRepository users) {
        this.users = users;
    }

    public void setView(LoginView view) {
        this.view = view;
        this.view.showUsers(this.users.getAllUsers());
    }

    public void onNewUserClicked() {
        this.view.navigateToRegisterActivity();
    }

    public void onUserClicked(User user) {
        this.view.navigateToStepsActivity(user);
    }

    public void onResume() {
        this.view.showUsers(this.users.getAllUsers());
    }
}
