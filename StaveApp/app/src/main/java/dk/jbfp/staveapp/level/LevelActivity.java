package dk.jbfp.staveapp.level;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import dk.jbfp.staveapp.Callback;
import dk.jbfp.staveapp.R;

public class LevelActivity extends Activity implements LevelView {
    public static final String USER_ID_KEY = "dk.jbfp.staveapp.USER_ID";
    public static final String WORDS_KEY = "dk.jbfp.staveapp.WORDS";

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

    private static final Map<String, Integer> wordSounds;

    static {
        ArrayMap<String, Integer> map = new ArrayMap<>();
        map.put("bi", R.raw.word_bi);
        map.put("bo", R.raw.word_bo);
        map.put("by", R.raw.word_by);
        map.put("bæ", R.raw.word_bae);
        map.put("da", R.raw.word_da);
        map.put("dø", R.raw.word_doe);
        map.put("en", R.raw.word_en);
        map.put("fe", R.raw.word_fe);
        map.put("fy", R.raw.word_fy);
        map.put("fæ", R.raw.word_fae);
        map.put("få", R.raw.word_faa);
        map.put("gø", R.raw.word_goe);
        map.put("gå", R.raw.word_gaa);
        map.put("ha", R.raw.word_ha);
        map.put("hi", R.raw.word_hi);
        map.put("ho", R.raw.word_ho);
        map.put("hæ", R.raw.word_hae);
        map.put("hø", R.raw.word_hoe);
        map.put("is", R.raw.word_is);
        map.put("ja", R.raw.word_ja);
        map.put("jo", R.raw.word_jo);
        map.put("ko", R.raw.word_ko);
        map.put("kø", R.raw.word_koe);
        map.put("le", R.raw.word_le);
        map.put("lo", R.raw.word_lo);
        map.put("lå", R.raw.word_laa);
        map.put("må", R.raw.word_maa);
        map.put("ni", R.raw.word_ni);
        map.put("nu", R.raw.word_nu);
        map.put("ny", R.raw.word_ny);
        map.put("nå", R.raw.word_naa);
        map.put("på", R.raw.word_paa);
        map.put("ro", R.raw.word_ro);
        map.put("rå", R.raw.word_raa);
        map.put("se", R.raw.word_se);
        map.put("si", R.raw.word_si);
        map.put("so", R.raw.word_so);
        map.put("sy", R.raw.word_sy);
        map.put("sø", R.raw.word_soe);
        map.put("te", R.raw.word_te);
        map.put("ti", R.raw.word_ti);
        map.put("to", R.raw.word_to);
        map.put("tø", R.raw.word_toe);
        map.put("uf", R.raw.word_uf);
        map.put("vi", R.raw.word_vi);
        map.put("æg", R.raw.word_aeg);
        map.put("øl", R.raw.word_oel);
        map.put("ål", R.raw.word_aal);
        map.put("Bo", R.raw.word_bo);
        map.put("Ib", R.raw.word_ib);
        map.put("Ea", R.raw.word_ea);
        wordSounds = Collections.unmodifiableMap(map);
    }

    private NumberPicker[] numberPickers;
    private boolean isPlayingCombinedSounds;
    private boolean cancelAnswerSound;

    private MediaPlayer lytButtonMediaPlayer;
    private Drawable playIcon;
    private Drawable stopIcon;
    private int currentWordSoundId;

    private LevelPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        this.playIcon = ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_48dp);
        this.stopIcon = ContextCompat.getDrawable(this, R.drawable.ic_stop_black_48dp);

        Intent intent = getIntent();
        String[] words = intent.getStringArrayExtra(WORDS_KEY);

        this.presenter = new LevelPresenter(words);
        this.presenter.setView(this);
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
                    presenter.onAnswerChanged();
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
                                presenter.onAnswerChanged();
                            }
                        }
                    }
                }, 1000);
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

    @Override
    public void onNextWord(Word next) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.NumberPickerLayout);
        layout.removeAllViews();

        String word = next.getWord();
        int wordLength = word.length();
        numberPickers = new NumberPicker[wordLength];

        for (int i = 0; i < wordLength; i++) {
            NumberPicker numberPicker = new NumberPicker(this);
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

        currentWordSoundId = wordSounds.get(word);
    }

    @Override
    public void onCompleted() {
        playSound(this, R.raw.yay_009, null);
    }

    @Override
    public void addWord(Word word) {
        LinearLayout wordsLinearLayout = (LinearLayout) findViewById(R.id.WordsLinearLayout);
        TextView wordTextView = new TextView(this);
        wordTextView.setText(word.getAnswer());

        if (word.getStatus() == Word.WordStatus.Incorrect) {
            wordTextView.setTextColor(Color.RED);
        }

        wordsLinearLayout.addView(wordTextView);
    }

    @Override
    public void clearList() {
        LinearLayout wordsLinearLayout = (LinearLayout) findViewById(R.id.WordsLinearLayout);
        wordsLinearLayout.removeAllViews();
    }

    @Override
    public void setLevel(int level, int total) {
        TextView levelTextView = (TextView) findViewById(R.id.LevelTextView);
        levelTextView.setText(level + "/" + total);
    }

    @Override
    public void playAnswerSound() {
        playCombinedSounds(this, 0);
    }

    private void playCombinedSounds(final Context context, final int index) {
        if (cancelAnswerSound) {
            cancelAnswerSound = false;
            return;
        }

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

    public void onSvarButtonClick(View view) throws Exception {
        StringBuilder answer = new StringBuilder();

        for (int i = 0; i < numberPickers.length; i++) {
            answer.append(Alphabet[numberPickers[i].getValue()]);
        }

        this.cancelAnswerSound = true;
        presenter.onAnswerClicked(answer.toString());
    }

    @Override
    public void playWordSound(int delay) {
        final ImageButton lytButton = (ImageButton) findViewById(R.id.LytButton);
        lytButtonMediaPlayer = MediaPlayer.create(lytButton.getContext(), currentWordSoundId);
        lytButtonMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer _) {
                stopWordSound();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lytButtonMediaPlayer != null) {
                    lytButtonMediaPlayer.start();
                    lytButton.setImageDrawable(stopIcon);
                }
            }
        }, delay);
    }

    @Override
    public void stopWordSound() {
        final ImageButton lytButton = (ImageButton) findViewById(R.id.LytButton);

        if (lytButtonMediaPlayer != null) {
            lytButtonMediaPlayer.stop();
            lytButtonMediaPlayer.release();
            lytButtonMediaPlayer = null;
        }

        lytButton.setImageDrawable(playIcon);
    }

    public void onLytButtonClick(View view) {
        if (lytButtonMediaPlayer == null) {
            this.presenter.onPlayClicked();
        } else {
            this.presenter.onStopClicked();
        }
    }

    private static void playSound(final Context context, final int resId, final Callback onCompleted) {
        MediaPlayer mp = MediaPlayer.create(context, resId);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();

                if (onCompleted != null) {
                    onCompleted.execute();
                }
            }
        });

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}
