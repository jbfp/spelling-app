package dk.jbfp.staveapp.register;

import android.graphics.Bitmap;

public interface RegisterView {
    void takePhoto();
    void setPhoto(Bitmap photo);
    void returnToLoginActivity();
}
