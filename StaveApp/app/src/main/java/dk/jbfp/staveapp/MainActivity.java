package dk.jbfp.staveapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String[] Alphabet = {
            "A", "B","C","D","E","F","G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Æ", "Ø", "Å"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final NumberPicker numberPicker1 = (NumberPicker) findViewById(R.id.NumberPicker1);
        final NumberPicker numberPicker2 = (NumberPicker) findViewById(R.id.NumberPicker2);

        configureNumberPickers(numberPicker1);
        configureNumberPickers(numberPicker2);

        final Button svarButton = (Button) findViewById(R.id.SvarButton);
        svarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String left = Alphabet[numberPicker1.getValue()];
                String right = Alphabet[numberPicker2.getValue()];
                String answer = left + right;

                Toast toast;

                if (answer.equalsIgnoreCase("KO")) {
                    toast = Toast.makeText(v.getContext(), "Godt arbejde!", Toast.LENGTH_LONG);
                } else {
                    toast = Toast.makeText(v.getContext(), "Prøv igen!", Toast.LENGTH_LONG);
                }

                toast.show();
            }
        });

        final Drawable playIcon = ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_48dp);
        final Drawable stopIcon = ContextCompat.getDrawable(this, R.drawable.ic_stop_black_48dp);
        final Button lytButton = (Button) findViewById(R.id.LytButton);

        lytButton.setOnClickListener(new View.OnClickListener() {
            MediaPlayer mp;

            @Override
            public void onClick(final View v) {
                lytButton.setEnabled(false);

                if (mp == null) {
                    start(v.getContext());
                } else {
                    stop();
                }

                lytButton.setEnabled(true);
            }

            private void start(Context context) {
                mp = MediaPlayer.create(context, R.raw.word_ko);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer _) {
                        stop();
                    }
                });
                mp.start();
                lytButton.setCompoundDrawablesWithIntrinsicBounds(null, stopIcon, null, null);
            }

            private void stop() {
                mp.stop();
                mp.release();
                mp = null;
                lytButton.setCompoundDrawablesWithIntrinsicBounds(null, playIcon, null, null);
            }
        });
    }

    private void configureNumberPickers(final NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(Alphabet.length - 1);
        numberPicker.setDisplayedValues(Alphabet);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }
}
