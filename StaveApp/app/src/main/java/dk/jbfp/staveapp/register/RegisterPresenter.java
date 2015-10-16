package dk.jbfp.staveapp.register;

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
        int seed = random.nextInt();
        this.users.addUser(name, seed, photo);
        this.view.returnToLoginActivity();
    }
}
