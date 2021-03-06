package dk.jbfp.staveapp.register;

import android.os.AsyncTask;

import java.util.Random;

import dk.jbfp.staveapp.UserRepository;

public class RegisterPresenter {
    private static final Random random = new Random();

    private UserRepository users;
    private RegisterView view;

    private String name;
    private byte[] photo;

    public RegisterPresenter(UserRepository users) {
        this.users = users;
    }

    public void setView(RegisterView view) {
        this.view = view;
    }

    public void onNameChanged(String name) {
        this.name = name;
        this.view.setSaveButtonEnabled(this.name.length() > 0);
    }

    public void onTakePhotoButtonClick() {
        this.view.takePhoto();
    }

    public void onPhotoTaken(byte[] photo) {
        this.photo = photo;
        this.view.setPhoto(this.photo);
    }

    public void save() {
        final int seed = random.nextInt();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                users.addUser(name, seed, photo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                view.returnToLoginActivity();
            }
        }.execute();
    }
}
