package dk.jbfp.staveapp.login;

import android.os.AsyncTask;

import java.util.List;

import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.UserRepository;

public class LoginPresenter {
    private UserRepository users;
    private LoginView view;

    public LoginPresenter(UserRepository users) {
        this.users = users;
    }

    public void setView(final LoginView view) {
        this.view = view;
        new GetAllUsersAsyncTask().execute();
    }

    public void onNewUserClicked() {
        this.view.navigateToRegisterActivity();
    }

    public void onUserClicked(final User user) {
        this.view.navigateToStepsActivity(user);
    }

    public void onResume() {
        new GetAllUsersAsyncTask().execute();
    }

    private class GetAllUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            return users.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            view.showUsers(users);
        }
    }
}
