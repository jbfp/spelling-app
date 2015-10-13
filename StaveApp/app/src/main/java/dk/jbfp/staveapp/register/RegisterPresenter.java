package dk.jbfp.staveapp.register;

import android.graphics.Bitmap;

import dk.jbfp.staveapp.UserRepository;

public class RegisterPresenter {
    private UserRepository users;
    private RegisterView view;

    public RegisterPresenter(UserRepository users) {
        this.users = users;
    }

    public void setView(RegisterView view) {
        this.view = view;
    }

    public void onTakePhotoButtonClick() {
        this.view.takePhoto();
    }

    public void onPhotoTaken(Bitmap photo) {
        this.view.setPhoto(photo);
    }

    public void save(String name, byte[] photo) {
        this.users.addUser(name, photo);
        this.view.returnToLoginActivity();
    }
}
