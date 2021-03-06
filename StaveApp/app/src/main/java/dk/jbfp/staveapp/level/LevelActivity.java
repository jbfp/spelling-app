package dk.jbfp.staveapp.level;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import dk.jbfp.staveapp.Callback;
import dk.jbfp.staveapp.R;
import dk.jbfp.staveapp.data.Database;

public class LevelActivity extends Activity implements LevelView, OnEditorActionListener {
    public static final String STEP_ID_KEY = "dk.jbfp.staveapp.STEP_ID";
    public static final String STEP_KEY = "dk.jbfp.staveapp.STEP";
    public static final String WORDS_KEY = "dk.jbfp.staveapp.WORDS";
    public static final String GAME_RESULT_KEY = "dk.jbfp.staveapp.GAME_RESULT";

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

    private EditText answerEditText;
    private boolean isPlayingCombinedSounds;
    private boolean cancelAnswerSound;

    private MediaPlayer lytButtonMediaPlayer;
    private Drawable playIcon;
    private Drawable stopIcon;
    private int currentWordSoundId;

    private long stepId;
    private LevelPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        this.answerEditText = (EditText) findViewById(R.id.answer);
        this.answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    return;
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isPlayingCombinedSounds == false) {
                            presenter.onAnswerChanged();
                        }
                    }
                }, 1000);
            }
        });
        this.answerEditText.setOnEditorActionListener(this);

        this.playIcon = ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_48dp);
        this.stopIcon = ContextCompat.getDrawable(this, R.drawable.ic_stop_black_48dp);

        Intent intent = getIntent();
        this.stepId = intent.getLongExtra(STEP_ID_KEY, -1);
        int step = intent.getIntExtra(STEP_KEY, -1);
        String[] words = intent.getStringArrayExtra(WORDS_KEY);

        this.presenter = new LevelPresenter(step, words, new Database(this));
        this.presenter.setView(this);
    }

    @Override
    public void onNextWord(Word next) {
        String word = next.getWord();
        this.answerEditText.setText("");
        this.answerEditText.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(word.length())
        });
        this.answerEditText.requestFocus();
        currentWordSoundId = wordSounds.get(word);
    }

    @Override
    public void onCompleted(final boolean perfect) {
        int soundId;

        if (perfect) {
            soundId = R.raw.yay_011;
        } else {
            soundId = R.raw.yay_010;
        }

        playSound(this, soundId, new Callback() {
            @Override
            public void execute() {
                Intent intent = new Intent();
                intent.putExtra(STEP_ID_KEY, stepId);
                intent.putExtra(GAME_RESULT_KEY, perfect);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void showWords(List<Word> words) {
        LinearLayout wordsLinearLayout = (LinearLayout) findViewById(R.id.WordsLinearLayout);
        wordsLinearLayout.removeAllViews();

        for (Word word: words) {
            TextView wordTextView = new TextView(this);
            wordTextView.setText(word.getAnswer());

            if (word.getStatus() == Word.WordStatus.Incorrect) {
                wordTextView.setTextColor(Color.RED);
            }

            wordsLinearLayout.addView(wordTextView);
        }
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

    @Override
    public void setStep(int step) {
        getActionBar().setTitle("Niveau " + step);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void playCombinedSounds(final Context context, final int index) {
        if (cancelAnswerSound) {
            cancelAnswerSound = false;
            isPlayingCombinedSounds = false;
            return;
        }

        Editable value = answerEditText.getText();

        if (index >= value.length()) {
            isPlayingCombinedSounds = false;
        } else {
            isPlayingCombinedSounds = true;
            int letterIndex = Character.toUpperCase(value.charAt(index)) - 65;
            int soundId = Sounds[letterIndex];
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
        this.cancelAnswerSound = true;
        presenter.onAnswerClicked(answerEditText.getText().toString());
    }

    @Override
    public synchronized void playWordSound(int delay) {
        if (this.isPlayingCombinedSounds) {
            this.cancelAnswerSound = true;
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                intent.putExtra(STEP_ID_KEY, this.stepId);
                setResult(RESULT_CANCELED, intent);
                this.finish();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            try {
                onSvarButtonClick(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
