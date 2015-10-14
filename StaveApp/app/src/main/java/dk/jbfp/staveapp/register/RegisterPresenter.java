package dk.jbfp.staveapp.register;

import android.graphics.Bitmap;

import java.util.Random;

import dk.jbfp.staveapp.UserRepository;

public class RegisterPresenter {
    private static final Random random = new Random();

    private UserRepository users;
    private RegisterView view;

    public RegisterPresenter(UserRepository users) {
        this.users = users;
    }

    public void setView(RegisterView view) {
        this.view = view;
    }

    public void onNameChanged(String name) {
        this.view.setSaveButtonEnabled(name.length() > 0);
    }

    public void onTakePhotoButtonClick() {
        this.view.takePhoto();
    }

    public void onPhotoTaken(Bitmap photo) {
        this.view.setPhoto(photo);
    }

    public void save(String name, byte[] photo) {
        if (name == null) {
            name = "";
        }

        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }

        int seed = random.nextInt();
        this.users.addUser(name, seed, photo);
        this.view.returnToLoginActivity();
    }
}