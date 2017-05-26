package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Locale;

import static com.dsource.idc.jellow.R.id.reset;
import static com.dsource.idc.jellow.Reset__preferences.flag;

public class MainActivity extends AppCompatActivity {
    int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    int image_flag = -1, flag_keyboard = 0, mActionBtnClickCount;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    private EditText et;
    private KeyListener originalKeyListener;
    private RecyclerView mRecyclerView;
    private CircularImageView mMenuItemImage;
    private LinearLayout mMenuItemLinearLayout;
    private int mSelectedItemPosition;
    private View mSelectedItemView;
    private boolean mShouldReadFullSpeech = false;
    public TextToSpeech mTts;
    private ImageAdapter mImageAdapter;
    private Integer[] mColor = {-5317, -12627531 , -7617718 , -2937298 , -648053365 , -1761607680 };

    String[][] layer_1_speech_hindi = {{

            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करना अच्छा लगता हैं ",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करना अच्छा लगता हैं",
            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करनी हैं",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करनी हैं",
            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में और ज़्यादा बात करनी हैं",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में और ज़्यादा बात करनी हैं",
            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात नहीं करनी हैं",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में बात नहीं करनी हैं",
            "मुझे लोगों को नमस्कार करना और अपनी भावनाओं के बारे में कुछ भी बात नहीं करनी हैं",
            "मुझे सच में लोगों को नमस्कार करना और अपनी भावनाओं के बारे में कुछ भी बात नहीं करनी हैं"
    }, {"मुझे अपने रोज़़ के काम करना अच्छा लगता हैं",
            "मुझे सच में अपने रोज़ के काम करना अच्छा लगता हैं",
            "मुझे अपने रोज़ के काम करने हैं",
            "मुझे सच में अपने रोज़ के काम करने हैं",
            "मुझे अपने रोज़ के काम पर ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने रोज़ के काम पर और ज़्यादा समय बिताना हैं",
            "मुझे अपने रोज़ के काम करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने रोज़ के काम करना अच्छा नहीं लगता हैं",
            "मुझे अपने रोज़ के काम नहीं करने हैं",
            "मुझे सच में अपने रोज़ के काम नहीं करने हैं",
            "मुझे अपने रोज़ के काम पर ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने रोज़ के काम पर कुछ भी समय नहीं बिताना हैं"
    }, {"मुझे खाना अच्छा लगता हैं",
            "मुझे सच में खाना अच्छा लगता हैं",
            "मुझे खाना हैं",
            "मुझे सच में खाना हैं",
            "मुझे थोड़ा और खाना हैं",
            "मुझे सच में थोड़ा और खाना हैं",
            "मुझे खाना अच्छा नहीं लगता हैं",
            "मुझे सच में खाना अच्छा नहीं लगता हैं",
            "मुझे खाना नहीं हैं",
            "मुझे सच में खाना नहीं हैं",
            "मुझे कुछ भी खाना नहीं हैं",
            "मुझे सच में कुछ भी खाना नहीं हैं"
    }, {"मुझे मज़े करना अच्छा लगता हैं",
            "मुझे सच में मज़े करना अच्छा लगता हैं",
            "मुझे मज़े करने हैं",
            "मुझे सच में मज़े करने हैं",
            "मुझे और मज़े करने हैं",
            "मुझे सच में और ज़्यादा मज़े करने हैं",
            "मुझे मज़े करना अच्छा नहीं लगता हैं",
            "मुझे सच में मज़े करना अच्छा नहीं लगता हैं",
            "मुझे मज़े नहीं करने हैं",
            "मुझे सच में मज़े नहीं करने हैं",
            "मुझे और मज़े नहीं करने हैं",
            "मुझे सच में और मज़े नहीं करने हैं"
    }, {"मुझे सीखना अच्छा लगता हैं",
            "मुझे सच में सीखना अच्छा लगता हैं",
            "मुझे सीखना हैं",
            "मुझे सच में सीखना हैं",
            "मुझे और सीखना हैं",
            "मुझे थोड़ा और सीखना हैं",
            "मुझे सीखना अच्छा नहीं लगता हैं",
            "मुझे सच में सीखना अच्छा नहीं लगता हैं",
            "मुझे सीखना नहीं हैं",
            "मुझे सच में सीखना नहीं हैं",
            "मुझे और सीखना नहीं हैं",
            "मुझे सच में और सीखना  नहीं हैं"
    }, {"मुझे लोगों से बात करना अच्छा लगता हैं",
            "मुझे सच में लोगों से बात करना अच्छा लगता हैं",
            "मुझे लोगों से मिलना हैं",
            "मुझे सच में लोगों से मिलना हैं",
            "मुझे कुछ और लोगों से मिलना हैं",
            "मुझे सच में कुछ और लोगों से मिलना हैं",
            "मुझे लोगों से बात करना अच्छा नहीं लगता हैं",
            "मुझे सच में लोगों से बात करना अच्छा नहीं लगता हैं",
            "मुझे लोगों से नहीं मिलना हैं",
            "मुझे सच में लोगों से नहीं मिलना हैं",
            "मुझे किसी से भी नहीं मिलना हैं",
            "मुझे सच में किसी से भी नहीं मिलना हैं"
    }, {"मुझे जगहों को देखना अच्छा लगता हैं",
            "मुझे सच में जगहों को देखना अच्छा लगता हैं",
            "मुझे अलग-अलग जगहों को देखना हैं",
            "मुझे सच में अलग-अलग जगहों को देखना हैं",
            "मुझे कुछ और जगहों को देखना हैं",
            "मुझे सच में कुछ और जगहों को देखना हैं",
            "मुझे जगहों को देखना अच्छा नहीं लगता हैं",
            "मुझे सच में जगहों को देखना अच्छा नहीं लगता हैं",
            "मुझे अलग-अलग जगहों को नहीं देखना हैं",
            "मुझे सच में अलग-अलग जगहों को नहीं देखना हैं",
            "मुझे और जगहों को नहीं देखना हैं",
            "मुझे सच में और जगहों को नहीं देखना हैं"
    }, {"मुझे समय और मौसम के बारे में बात करना अच्छा लगता हैं",
            "मुझे सच में समय और मौसम के बारे में बात करना अच्छा लगता हैं",
            "मुझे समय और मौसम के बारे में बात करनी  हैं",
            "मुझे सच में समय और मौसम के बारे में बात करनी हैं",
            "मुझे समय और मौसम के बारे में और ज़्यादा बात करनी  हैं",
            "मुझे सच में समय और मौसम के बारे में और ज़्यादा बात करनी हैं",
            "मुझे समय और मौसम के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे सच में समय और मौसम के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे समय और मौसम के बारे में बात नहीं करनी हैं",
            "मुझे  सच में समय और मौसम के बारे में बात नहीं करनी हैं",
            "मुझे समय और मौसम के बारे में कुछ भी बात नहीं करनी हैं",
            "मुझे सच में समय और मौसम के बारे में कुछ भी बात नहीं करनी हैं"
    }, {"मुझे मदद करना अच्छा लगता हैं",
            "मुझे सच में मदद करना अच्छा लगता हैं",
            "मुझे मदद की ज़रूरत हैं",
            "मुझे सच में मदद की ज़रूरत हैं",
            "मुझे और मदद की ज़रूरत हैं",
            "मुझे  सच में और ज़्यादा मदद की ज़रूरत है",
            "मुझे मदद करना अच्छा नहीं लगता हैं",
            "मुझे सच में मदद करना अच्छा नहीं लगता हैं",
            "मुझे मदद की ज़रूरत नहीं हैं",
            "मुझे सच में मदद की ज़रूरत नहीं  हैं",
            "मुझे और मदद की ज़रूरत नहीं हैं",
            "मुझे  सच में और मदद की ज़रूरत नहीं हैं"
    }};

    String[][] layer_1_speech_english = {{

            "I like to greet others and talk about my feelings",
            "I really like to greet others and talk about my feelings",
            "I want to greet others and talk about my feelings",
            "I really want to greet others and talk about my feelings",
            "I want to greet others and talk about my feelings some more",
            "I really want to greet others and talk about my feelings some more",
            "I don’t like to greet others and talk about my feelings",
            "I really don’t like to greet others and talk about my feelings",
            "I don’t want to greet others and talk about my feelings",
            "I really don’t want to greet others and talk about my feelings",
            "I don’t want to greet others and talk about my feelings anymore",
            "I really don’t want to greet others and talk about my feelings any more",

    },{"I like to do my daily activities",
            "I really like to do my daily activities",
            "I want to do my daily activities",
            "I really want to do my daily activities",
            "I want to spend more time doing my daily activities",
            "I really want to spend some more time doing my daily activities",
            "I don’t like to do my daily activities",
            "I really don’t like to do my daily activities",
            "I don’t want to do my daily activities",
            "I really don’t want to do my daily activities",
            "I don’t want to spend more time doing my daily activities",
            "I really don’t want to spend any more time doing my daily activities"
    },{"I like to eat",
            "I really like to eat",
            "I want to eat",
            "I really want to eat",
            "I want to eat some more",
            "I really want to eat some more",
            "I don’t like to eat",
            "I really don’t like to eat",
            "I don’t want to eat",
            "I really don’t want to eat",
            "I don’t want to eat any more",
            "I really don’t want to eat any more",
    },{"I like to have fun",
            "I really like to have fun",
            "I want to have fun",
            "I really want to have fun",
            "I want to have more fun",
            "I want to have some more fun",
            "I don’t like to have fun",
            "I really don’t like to have fun",
            "I don’t want to have fun",
            "I really don’t want to have fun",
            "I don’t want to have more fun",
            "I really don’t want to have any more fun"
    },{"I like to learn",
            "I really like to learn",
            "I want to learn",
            "I really want to learn",
            "I want to learn more",
            "I really want to learn some more",
            "I don’t like to learn",
            "I really don’t like to learn",
            "I don’t want to learn",
            "I really don’t want to learn",
            "I don’t want to learn any more",
            "I really don’t want to learn any more"
    },  {"I like to talk to people",
            "I really like to talk to people",
            "I want to meet people",
            "I really want to meet people",
            "I want to meet some more people",
            "I really want to meet some more people",
            "I don’t like to talk to people",
            "I really don’t like to talk to people",
            "I don’t want to meet people",
            "I really don’t want to meet people",
            "I don’t want to meet any more people",
            "I really don’t want to meet any more people"
    },{"I like to visit places",
            "I really like to visit places",
            "I want to visit different places",
            "I really want to visit different places",
            "I want to visit some more places",
            "I really want to visit some more places",
            "I don’t like to visit places",
            "I really don’t like to visit places",
            "I don’t want to visit different places",
            "I really don’t want to visit different places",
            "I don’t want to visit any more places",
            "I really don’t want to visit any more places"
    },{"I like to talk about time and weather",
            "I really like to talk about time and weather",
            "I want to talk about time and weather",
            "I really want to talk about time and weather",
            "I want to talk some more about time and weather",
            "I really want to talk some more about time and weather",
            "I don’t like to talk about time and weather",
            "I really don’t like to talk about time and weather",
            "I don’t want to talk about time and weather",
            "I really don’t want to talk about time and weather",
            "I don’t want to talk any more about time and weather",
            "I really don’t want to talk any more about time and weather"
    },{"I like to help",
            "I really like to help",
            "I need help",
            "I really need help",
            "I need more help",
            "I really need some more help",
            "I don’t like to help",
            "I really don’t like to help",
            "I don’t need help",
            "I really don’t need help",
            "I don’t need more help",
            "I really don’t need any more help"
    }};

    private SessionManager session;

    String[][] layer_1_speech = new String[100][100];
    String[] myMusic = new String[100];
    String[] side = new String[100];
    String[] below = new String[100];

    float dpHeight;
    float dpWidth;
    int sr, bw;

    final String[] belowText_hindi ={"शुभकामना और भावना", "रोज़ के काम", "खाना", "मज़े", "सीखना", "लोग", "जगह", "समय और मौसम", "मदद"};
    final String[] belowText_english ={"greet and feel", "daily activities", "eating", "fun", "learning", "people", "places", "time and Weather", "help"};
    final String[] english1 ={"Greet and Feel", "Daily Activities", "Eating", "Fun", "Learning", "People", "Places", "Time and Weather", "Help"};

    final String[] side_hindi ={"अच्छा लगता हैं", "सच में अच्छा लगता हैं", "हाँ", "सच में हाँ", "ज़्यादा", "सच में ज़्यादा", "अच्छा नहीं लगता हैं", "सच में अच्छा नहीं लगता हैं", "नहीं", "सच में नहीं", "कम", "सच में कम"};
    final String[] side_english ={"like", "really like", "yes", "really yes", "more", "really more", "don’t like", "really don’t like", "no", "really no", "less", "really less"};

    final String[] below_hindi ={"होम", "वापस", "कीबोर्ड"};
    final String[] below_english ={"Home", "back", "keyboard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial1);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable( getResources().getDrawable(R.drawable.yellow_bg));

        new CalculateSrBwAsync().execute("");
        session = new SessionManager(getApplicationContext());

        if(session.getLanguage()==1){
            getSupportActionBar().setTitle("होम");
        }else if (session.getLanguage()==0){
            getSupportActionBar().setTitle("Home");
        }

        mTts =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTts.setEngineByPackageName("com.google.android.mTts");
                    new BackgroundSpeechOperationsAsync().execute("");
                }
            }
        });

        mTts.setSpeechRate((float) session.getSpeed()/50);
        mTts.setPitch((float) session.getPitch()/50);

        like = (ImageView) findViewById(R.id.ivlike);
        dislike = (ImageView) findViewById(R.id.ivdislike);
        add = (ImageView) findViewById(R.id.ivadd);
        minus = (ImageView) findViewById(R.id.ivminus);
        yes = (ImageView) findViewById(R.id.ivyes);
        no = (ImageView) findViewById(R.id.ivno);
        home = (ImageView) findViewById(R.id.ivhome);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        et = (EditText) findViewById(R.id.et);
        et.setVisibility(View.INVISIBLE);
        back = (ImageView) findViewById(R.id.ivback);

        ttsButton = (ImageView)findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 3));
        //mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, -50, false));

        mImageAdapter = new ImageAdapter(this);
        mRecyclerView.setAdapter(new ImageAdapter(this));

        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                mMenuItemLinearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_icon1);
                mMenuItemImage = (CircularImageView) view.findViewById(R.id.icon1);
                mMenuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetActionButtons(-1);
                        resetPreviousSelectedItem();
                        mMenuItemImage.setBorderColor(-1283893945);
                        mMenuItemImage.setShadowColor(-1283893945);
                        mMenuItemImage.setShadowRadius(sr);
                        mMenuItemImage.setBorderWidth(bw);
                        mShouldReadFullSpeech = true;
                        mSelectedItemPosition = position;
                        mSelectedItemView = mMenuItemImage;
                        mActionBtnClickCount = 0;
                        /*Intent intent = new Intent(MainActivity.this, Main2LAyer.class);
                        intent.putExtra("id", position);
                        startActivity(intent);*/
                        getSupportActionBar().setTitle(english1[position]);
                        mTts.speak(myMusic[position], TextToSpeech.QUEUE_FLUSH, null);
                    }

                    private void resetPreviousSelectedItem() {
                        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
                            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
                        }
                    }
                });
            }
            @Override   public void onLongClick(View view, int position) {}
        }));

        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                CircularImageView circularImageView = (CircularImageView) view.findViewById(R.id.icon1);
                if(mSelectedItemView != null && circularImageView.equals(mSelectedItemView)){
                    setMenuImageBorder(view, true);
                }else{
                    setMenuImageBorder(view, false);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                setMenuImageBorder(view, false);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                mImageAdapter.notifyDataSetChanged();
                mSelectedItemPosition = -1;
                flag = 0;
                mShouldReadFullSpeech = false;
                image_flag = -1;
                if (flag_keyboard  == 1)
                {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    like.setEnabled(true);
                    dislike.setEnabled(true);
                    add.setEnabled(true);
                    minus.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                    like.setAlpha(1f);
                    dislike.setAlpha(1f);
                    add.setAlpha(1f);
                    minus.setAlpha(1f);
                    yes.setAlpha(1f);
                    no.setAlpha(1f);
                    back.setAlpha(.5f);
                    back.setEnabled(false);
                }

                mTts.speak(below[0], TextToSpeech.QUEUE_FLUSH, null);
                home.setImageResource(R.drawable.home);
                mRecyclerView.setAdapter(new ImageAdapter(MainActivity.this));
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(below[2], TextToSpeech.QUEUE_FLUSH, null);
                if (flag_keyboard  == 1)
                {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    like.setEnabled(true);
                    dislike.setEnabled(true);
                    add.setEnabled(true);
                    minus.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                    like.setAlpha(1f);
                    dislike.setAlpha(1f);
                    add.setAlpha(1f);
                    minus.setAlpha(1f);
                    yes.setAlpha(1f);
                    no.setAlpha(1f);
                    back.setAlpha(.5f);
                    back.setEnabled(false);
                }else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.VISIBLE);

                    et.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    like.setAlpha(0.5f);
                    dislike.setAlpha(0.5f);
                    add.setAlpha(0.5f);
                    minus.setAlpha(0.5f);
                    yes.setAlpha(0.5f);
                    no.setAlpha(0.5f);
                    like.setEnabled(false);
                    dislike.setEnabled(false);
                    add.setEnabled(false);
                    minus.setEnabled(false);
                    yes.setEnabled(false);
                    no.setEnabled(false);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    back.setAlpha(1f);
                    back.setEnabled(true);
                    flag_keyboard = 1;
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mTts.setSpeechRate((float) session.getSpeed()/50);
                mTts.setPitch((float) session.getPitch()/50);
                String s1 = et.getText().toString();
                mTts.speak(s1, TextToSpeech.QUEUE_FLUSH, null);

                like.setEnabled(false);
                dislike.setEnabled(false);
                add.setEnabled(false);
                minus.setEnabled(false);
                yes.setEnabled(false);
                no.setEnabled(false);
            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...
                if (!hasFocus) {
                    // Hide soft keyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    // Make it non-editable again.
                    et.setKeyListener(null);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                if (flag_keyboard == 1)
                {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    mTts.speak(below[1], TextToSpeech.QUEUE_FLUSH, null);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    like.setEnabled(true);
                    dislike.setEnabled(true);
                    add.setEnabled(true);
                    minus.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                    like.setAlpha(1f);
                    dislike.setAlpha(1f);
                    add.setAlpha(1f);
                    minus.setAlpha(1f);
                    yes.setAlpha(1f);
                    no.setAlpha(1f);
                    back.setEnabled(false);
                    back.setAlpha(.5f);
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCy = mCm = mCd = mCn = mCl = 0;
                image_flag = 0;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCk == 1) {
                        mTts.speak(side[1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;
                    } else {
                        mTts.speak(side[0], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[0]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCk == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][0], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCn = mCl = 0;
                image_flag = 1;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCd == 1) {
                        mTts.speak(side[7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        mTts.speak(side[6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[1]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCd == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCm = mCd = mCn = mCl = 0;
                image_flag = 2;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCy == 1) {
                        mTts.speak(side[3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTts.speak(side[2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[2]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCy == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCl = 0;
                image_flag = 3;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCn == 1) {
                        mTts.speak(side[9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        mTts.speak(side[8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[3]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCn == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCd = mCn = mCl = 0;
                image_flag = 4;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCm == 1) {
                        mTts.speak(side[5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        mTts.speak(side[4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[4]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCm == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = 0;
                image_flag = 5;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCl == 1) {
                        mTts.speak(side[11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        mTts.speak(side[10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                } else {
                    ((CircularImageView)mSelectedItemView).setBorderColor(mColor[5]);
                    ((CircularImageView)mSelectedItemView).setShadowRadius(sr);
                    ((CircularImageView)mSelectedItemView).setBorderWidth(bw);
                    if (mCl == 1) {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        mTts.speak(layer_1_speech[mSelectedItemPosition][10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                    ++mActionBtnClickCount;
                }
            }
        });
    }

    private void setMenuImageBorder(View recyclerChildView, boolean setBorder) {
        CircularImageView circularImageView = (CircularImageView) recyclerChildView.findViewById(R.id.icon1);
        if (setBorder){
            if(mActionBtnClickCount > 0)
                ((CircularImageView)mSelectedItemView).setBorderColor(mColor[image_flag]);
            else {
                circularImageView.setBorderColor(-1283893945);
                circularImageView.setShadowColor(-1283893945);
            }
            circularImageView.setShadowRadius(sr);
            circularImageView.setBorderWidth(bw);
        }else {
            circularImageView.setBorderColor(-1);
            circularImageView.setShadowColor(0);
            circularImageView.setShadowRadius(sr);
            circularImageView.setBorderWidth(0);
        }
    }

    private void resetActionButtons(int image_flag) {
        like.setImageResource(R.drawable.ilikewithoutoutline);
        dislike.setImageResource(R.drawable.idontlikewithout);
        yes.setImageResource(R.drawable.iwantwithout);
        no.setImageResource(R.drawable.idontwantwithout);
        add.setImageResource(R.drawable.morewithout);
        minus.setImageResource(R.drawable.lesswithout);
        switch (image_flag){
            case 0: like.setImageResource(R.drawable.ilikewithoutline); break;
            case 1: dislike.setImageResource(R.drawable.idontlikewithoutline); break;
            case 2: yes.setImageResource(R.drawable.iwantwithoutline); break;
            case 3: no.setImageResource(R.drawable.idontwantwithoutline); break;
            case 4: add.setImageResource(R.drawable.morewithoutline); break;
            case 5: minus.setImageResource(R.drawable.lesswithoutline); break;
            default: break;
        }
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (session.getLanguage()==1){
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_main, menu);
        }
        if (session.getLanguage()==0) {
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_1, menu);
        }
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
                break;
            case R.id.info:
                Intent i = new Intent(MainActivity.this, About_Jellow.class);
                startActivity(i);
                break;
            case R.id.profile:
                Intent intent1 = new Intent(MainActivity.this, Profile_form.class);
                startActivity(intent1);
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(MainActivity.this, Feedback.class);
                startActivity(intent2);
                break;
            case R.id.usage:
                Intent intent3 = new Intent(MainActivity.this, Tutorial.class);
                startActivity(intent3);
                break;
            case reset:
                Intent intent4 = new Intent(MainActivity.this, Reset__preferences.class);
                startActivity(intent4);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(MainActivity.this, Keyboard_Input.class);
                startActivity(intent6);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class BackgroundSpeechOperationsAsync extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            if (session.getLanguage()==0 ){
                mTts.setLanguage(new Locale("hin", "IND"));
                layer_1_speech = layer_1_speech_english;
                myMusic = belowText_english;
                side = side_english;
                below = below_english;
            }
            if (session.getLanguage()==1){
                mTts.setLanguage(new Locale("hin", "IND"));
                layer_1_speech = layer_1_speech_hindi;
                myMusic = belowText_hindi;
                side = side_hindi;
                below = below_hindi;
                System.out.println("sdasdada" +session.getLanguage());
            }
        } catch (Exception e) {
            Thread.interrupted();
        }
        return "Executed";
    }
}

    private class CalculateSrBwAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                if (dpHeight >= 720) {
                    sr = 0;
                    bw = 15;
                }else{
                    sr = 0;
                    bw = 7;
                }
            } catch (Exception e) {
                Thread.interrupted();
            }
            return "Executed";
        }
    }
}