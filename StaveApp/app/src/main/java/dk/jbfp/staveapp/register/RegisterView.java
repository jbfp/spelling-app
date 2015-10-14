package dk.jbfp.staveapp.register;

import android.graphics.Bitmap;

public interface RegisterView {
    void setSaveButtonEnabled(boolean enabled);
    void takePhoto();
    void setPhoto(Bitmap photo);
    void returnToLoginActivity();
}
