package dk.jbfp.staveapp.register;

public interface RegisterView {
    void setSaveButtonEnabled(boolean enabled);
    void takePhoto();
    void setPhoto(byte[] photo);
    void returnToLoginActivity();
}
