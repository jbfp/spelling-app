package dk.jbfp.staveapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String[] Alphabet = {
            "A", "B","C","D","E","F","G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Æ", "Ø", "Å"
    };

    private static final int[] Sounds = {
            R.raw.a_hard, R.raw.b, R.raw.s, R.raw.d, R.raw.e_hard, R.raw.f, R.raw.g, R.raw.h,
            R.raw.i_hard, R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.n, R.raw.o_hard, R.raw.p,
            R.raw.k, R.raw.r, R.raw.s, R.raw.t, R.raw.u_hard, R.raw.v, R.raw.v, R.raw.k,
            R.raw.y_hard, R.raw.s, R.raw.ae_hard, R.raw.oe_hard, R.raw.aa_hard
    };

    private NumberPicker[] numberPickers;
    private boolean isPlayingCombinedSounds;

    private String[] words;
    private int wordIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = new String[] { "ko", "by", "bo" }; // TODO: From intent.
        wordIndex = 0;
        int wordLength = words[wordIndex].length();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.NumberPickerLayout);
        layout.removeAllViews();
        numberPickers = new NumberPicker[wordLength];

        for (int i = 0; i < wordLength; i++) {
            NumberPicker numberPicker = new NumberPicker(getApplicationContext());
            numberPicker.setId(View.generateViewId());
            configureNumberPickers(numberPicker);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, numberPickers[i - 1].getId());
            }

            numberPickers[i] = numberPicker;
            layout.addView(numberPicker, lp);
        }

        final Button svarButton = (Button) findViewById(R.id.SvarButton);
        svarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Button self = (Button) v;
                self.setEnabled(false);

                String answer = "";
                List<Integer> soundIds = new ArrayList<>();

                for (int i = 0; i < numberPickers.length; i++) {
                    answer += Alphabet[numberPickers[i].getValue()];
                    soundIds.add(Sounds[numberPickers[i].getValue()]);
                }

                soundIds.add(R.raw.word_ko);

                final Toast toast;

                if (answer.equalsIgnoreCase(words[wordIndex])) {
                    soundIds.add(R.raw.yay_008);
                    toast = Toast.makeText(v.getContext(), "Godt arbejde!", Toast.LENGTH_LONG);
                } else {
                    soundIds.add(R.raw.doh_06);
                    soundIds.add(R.raw.doh_09);
                    toast = Toast.makeText(v.getContext(), "Prøv igen!", Toast.LENGTH_LONG);
                }

                playChain(v.getContext(), soundIds, new Callback() {
                    @Override
                    public void execute() {
                        self.setEnabled(true);
                        toast.show();
                    }
                });
            }
        });

        final Drawable playIcon = ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_48dp);
        final Drawable stopIcon = ContextCompat.getDrawable(this, R.drawable.ic_stop_black_48dp);
        final ImageButton lytButton = (ImageButton) findViewById(R.id.LytButton);

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
                lytButton.setImageDrawable(stopIcon);
            }

            private void stop() {
                mp.stop();
                mp.release();
                mp = null;
                lytButton.setImageDrawable(playIcon);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lytButton.callOnClick();
            }
        }, 1500);
    }

    private void configureNumberPickers(final NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(Alphabet.length - 1);
        numberPicker.setDisplayedValues(Alphabet);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayingCombinedSounds == false) {
                    playCombinedSounds(v.getContext(), 0);
                }
            }
        });

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(final NumberPicker picker, final int oldVal, final int newVal) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (picker.getValue() == newVal) {
                            if (isPlayingCombinedSounds == false) {
                                playCombinedSounds(picker.getContext(), 0);
                            }
                        }
                    }
                }, 1500);
            }
        });

        // Text color.
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);

            if (child instanceof EditText) {
                // Set text colors.
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(Color.BLACK);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // Set divider color.
                try {
                    Field selectionDividerField = numberPicker.getClass()
                            .getDeclaredField("mSelectionDivider");
                    selectionDividerField.setAccessible(true);
                    ((Drawable) selectionDividerField.get(numberPicker)).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                // Set text color.
                ((EditText) child).setTextColor(Color.BLACK);
                numberPicker.invalidate();
            }
        }
    }

    private void playCombinedSounds(final Context context, final int index) {
        if (index >= numberPickers.length) {
            isPlayingCombinedSounds = false;
        } else {
            isPlayingCombinedSounds = true;
            NumberPicker numberPicker = numberPickers[index];
            int soundId = Sounds[numberPicker.getValue()];
            MediaPlayer.create(context, soundId)
                    .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.stop();
                                    mp.release();
                                    playCombinedSounds(context, index + 1);
                                }
                            });

                            mp.start();
                        }
                    });
        }
    }

    private void playChain(final Context context, final List<Integer> soundIds,
                           final Callback callback) {
        playChain(context, soundIds, 0, callback);
    }

    private void playChain(final Context context, final List<Integer> soundIds, final int index,
                           final Callback callback) {
        if (index < soundIds.size()) {
            MediaPlayer.create(context, soundIds.get(index))
                    .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.stop();
                                    mp.release();
                                    playChain(context, soundIds, index + 1, callback);
                                }
                            });

                            mp.start();
                        }
                    });
        } else {
            callback.execute();
        }
    }
}
