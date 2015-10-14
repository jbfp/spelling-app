package dk.jbfp.staveapp.register;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.jbfp.staveapp.R;
import dk.jbfp.staveapp.data.Database;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private static final int CAMERA_REQUEST_CODE = 1;

    private EditText nameEditText;
    private ImageButton photoImageButton;
    private Button saveButton;

    private RegisterPresenter presenter;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.nameEditText = (EditText) findViewById(R.id.name_edit_text);
        this.photoImageButton = (ImageButton) findViewById(R.id.photo_image_button);
        this.saveButton = (Button) findViewById(R.id.save_button);

        this.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onNameChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        this.presenter = new RegisterPresenter(new Database(this));
        this.presenter.setView(this);
    }

    public void onTakePhotoButtonClick(View view) {
        this.presenter.onTakePhotoButtonClick();
    }

    @Override
    public void setSaveButtonEnabled(boolean enabled) {
        this.saveButton.setEnabled(enabled);
    }

    @Override
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";

        File photo = new File(getExternalFilesDir(null),  imageFileName);
        this.photoUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, this.photoUri);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void setPhoto(Bitmap photo) {
        this.photoImageButton.setImageBitmap(photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    ContentResolver contentResolver = getContentResolver();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, this.photoUri);
                    this.presenter.onPhotoTaken(bitmap);
                    this.photoUri = null;
                } catch (Exception e) { }
            }
        }
    }

    public void onSaveButtonClick(View view) {
        String name = this.nameEditText.getText().toString();
        BitmapDrawable drawable = (BitmapDrawable) this.photoImageButton.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] photo = stream.toByteArray();

        this.presenter.save(name, photo);
    }

    @Override
    public void returnToLoginActivity() {
        this.finish();
    }
}
