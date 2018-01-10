package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.Models.SeqActivityVerbiageModel;
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.Utility.Analytics.reportLog;
import static com.dsource.idc.jellowintl.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;


/**
 * Created by ekalpa on 6/22/2016.
 */
public class SequenceActivity extends AppCompatActivity {
    private final boolean DISABLE_ACTION_BTNS = true;
    private final int MODE_PICTURE_ONLY = 1;
    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    private int image_flag = -1, flag_keyboard = 0, mLevelTwoItemPos, count = 0;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton;
    private EditText et;
    private KeyListener originalKeyListener;
    private TextView tt1, bt1, bt2, bt3;
    private ImageView image1, image2, image3;
    private ImageView arrow1, arrow2, back;
    private RelativeLayout relativeLayout;
    private Button forward, backward;
    private String path;
    String[] mDailyActivitiesIcons;
    private String strNext, strPrevious, actionBarTitleTxt;
    private String[] mDailyActivitiesSpeechText, mDailyActivitiesBelowText, heading, side, below, bt;
    private ArrayList<ArrayList<ArrayList<String>>> mSeqActSpeech;
    private SessionManager mSession;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        Typeface fontMuktaRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Regular.ttf");
        Typeface fontMuktaBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Bold.ttf");
        mSession = new SessionManager(this);
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        new ChangeAppLocale(this).setLocale();
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        if(mLevelTwoItemPos == 7)   mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)  mLevelTwoItemPos = 4;


        loadArraysFromResources();
        initializeViews();
        forward.setText(bt[1]);
        backward.setText(bt[0]);
        backward.setEnabled(false);
        backward.setAlpha(.5f);
        et.setVisibility(View.INVISIBLE);

        File en_dir = SequenceActivity.this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);

        path = en_dir.getAbsolutePath()+"/drawables";

        tt1.setTypeface(fontMuktaBold);
        tt1.setAllCaps(true);
        tt1.setTextColor(Color.rgb(64, 64, 64));
        tt1.setText(heading[mLevelTwoItemPos].toLowerCase());

        bt1.setTypeface(fontMuktaRegular);
        bt1.setTextColor(Color.rgb(64, 64, 64));

        bt2.setTypeface(fontMuktaRegular);
        bt2.setTextColor(Color.rgb(64, 64, 64));

        bt3.setTypeface(fontMuktaRegular);
        bt3.setTextColor(Color.rgb(64, 64, 64));

        hideActionBtn(true);
        bt1.setText(mDailyActivitiesBelowText[0]);
        bt2.setText(mDailyActivitiesBelowText[1]);
        bt3.setText(mDailyActivitiesBelowText[2]);

        GlideApp.with(this)
                .load(path+"/"+mDailyActivitiesIcons[0]+".png")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(image1);

        GlideApp.with(this)
                .load(path+"/"+mDailyActivitiesIcons[1]+".png")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(image2);

        GlideApp.with(this)
                .load(path+"/"+mDailyActivitiesIcons[2]+".png")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(image3);




        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(strNext);
                backward.setEnabled(true);
                backward.setAlpha(1f);
                count = count + 3;
                hideActionBtn(true);
                resetActionBtnImageIcons();
                image_flag = 0;
                if (mDailyActivitiesIcons.length == count + 3) {
                    forward.setAlpha(.5f);
                    forward.setEnabled(false);
                }
                if (mDailyActivitiesIcons.length < count + 3) {
                    if (mLevelTwoItemPos == 0) {
                        GlideApp.with(SequenceActivity.this)
                                .load(path+"/"+mDailyActivitiesIcons[count]+".png")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false)
                                .centerCrop()
                                .dontAnimate()
                                .into(image1);
                        GlideApp.with(SequenceActivity.this)
                                .load(path+"/"+mDailyActivitiesIcons[count+1]+".png")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false)
                                .centerCrop()
                                .dontAnimate()
                                .into(image2);

                        arrow2.setVisibility(View.INVISIBLE);
                        image3.setVisibility(View.INVISIBLE);
                        bt3.setVisibility(View.INVISIBLE);
                        bt1.setText(mDailyActivitiesBelowText[count]);
                        bt2.setText(mDailyActivitiesBelowText[count + 1]);
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 4 || mLevelTwoItemPos == 3) {
                        GlideApp.with(SequenceActivity.this)
                                .load(path+"/"+mDailyActivitiesIcons[count]+".png")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false)
                                .centerCrop()
                                .dontAnimate()
                                .into(image1);
                        bt1.setText(mDailyActivitiesBelowText[count]);
                        image2.setVisibility(View.INVISIBLE);
                        image3.setVisibility(View.INVISIBLE);
                        arrow1.setVisibility(View.INVISIBLE);
                        arrow2.setVisibility(View.INVISIBLE);
                        bt2.setVisibility(View.INVISIBLE);
                        bt3.setVisibility(View.INVISIBLE);
                    }
                    forward.setAlpha(.5f);
                    forward.setEnabled(false);
                } else {
                    bt1.setText(mDailyActivitiesBelowText[count]);
                    bt2.setText(mDailyActivitiesBelowText[count + 1]);
                    bt3.setText(mDailyActivitiesBelowText[count + 2]);
                    GlideApp.with(SequenceActivity.this)
                            .load(path+"/"+mDailyActivitiesIcons[count]+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(image1);

                    GlideApp.with(SequenceActivity.this)
                            .load(path+"/"+mDailyActivitiesIcons[count+1]+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(image2);

                    GlideApp.with(SequenceActivity.this)
                            .load(path+"/"+mDailyActivitiesIcons[count+2]+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(image3);
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(strPrevious);
                forward.setEnabled(true);
                forward.setAlpha(1f);
                count = count - 3;
                hideActionBtn(true);
                resetActionBtnImageIcons();
                image_flag = 0;
                arrow1.setVisibility(View.VISIBLE);
                arrow2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image1.setVisibility(View.VISIBLE);
                if(mSession.getPictureViewMode() != MODE_PICTURE_ONLY) {
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setVisibility(View.VISIBLE);
                    bt3.setVisibility(View.VISIBLE);
                }
                bt1.setText(mDailyActivitiesBelowText[count]);
                bt2.setText(mDailyActivitiesBelowText[count + 1]);
                bt3.setText(mDailyActivitiesBelowText[count + 2]);
                GlideApp.with(SequenceActivity.this)
                        .load(path+"/"+mDailyActivitiesIcons[count]+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .dontAnimate()
                        .into(image1);

                GlideApp.with(SequenceActivity.this)
                        .load(path+"/"+mDailyActivitiesIcons[count+1]+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .dontAnimate()
                        .into(image2);

                GlideApp.with(SequenceActivity.this)
                        .load(path+"/"+mDailyActivitiesIcons[count+2]+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .dontAnimate()
                        .into(image3);
                if (count == 0) {
                    backward.setEnabled(false);
                    backward.setAlpha(.5f);
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = mCl = 0;
                if (image_flag == 1) {
                    hideActionBtn(true);setBorderToView(findViewById(R.id.borderView1), -1);
                    image_flag = 0;
                } else {
                    singleEvent("SequenceActivity",mDailyActivitiesSpeechText[count]);
                    reportLog(getLocalClassName()+", firstIcon: "+ mDailyActivitiesSpeechText[count], Log.INFO);
                    image_flag = 1;
                    if (count + image_flag == mDailyActivitiesIcons.length)
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count]);
                    resetActionBtnImageIcons();
                    setBorderToView(findViewById(R.id.borderView1), 6);
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = mCl = 0;
                if (image_flag == 2) {
                    hideActionBtn(true);setBorderToView(findViewById(R.id.borderView2), -1);
                    image_flag = 0;
                } else {
                    singleEvent("SequenceActivity",mDailyActivitiesSpeechText[count+1]);
                    reportLog(getLocalClassName()+", secondIcon: "+ mDailyActivitiesSpeechText[count + 1], Log.INFO);
                    image_flag = 2;
                    if (count + image_flag == mDailyActivitiesIcons.length)
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count + 1]);
                    resetActionBtnImageIcons();setBorderToView(findViewById(R.id.borderView2), 6);
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = mCl = 0;
                if (image_flag == 3) {
                    hideActionBtn(true);
                    setBorderToView(findViewById(R.id.borderView3), -1);
                    image_flag = 0;
                } else {
                    singleEvent("SequenceActivity",mDailyActivitiesSpeechText[count+2]);
                    reportLog(getLocalClassName()+", thirdIcon: "+ mDailyActivitiesSpeechText[count + 2], Log.INFO);
                    image_flag = 3;
                    if (count + image_flag == mDailyActivitiesIcons.length)
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count + 2]);
                    resetActionBtnImageIcons();
                    setBorderToView(findViewById(R.id.borderView3), 6);
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        ttsButton = findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(below[1]);
                ttsButton.setImageResource(R.drawable.speaker_button);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);
                } else {
                    back.setImageResource(R.drawable.backpressed);
                    setResult(RESULT_OK);
                    finish();
                }
                showActionBarTitle(true);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setImageResource(R.drawable.homepressed);
                keyboard.setImageResource(R.drawable.keyboard_button);
                speakSpeech(below[0]);
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(below[2]);
                ttsButton.setImageResource(R.drawable.speaker_button);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    et.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    backward.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    showActionBarTitle(true);
                } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    et.setVisibility(View.VISIBLE);
                    et.setKeyListener(originalKeyListener);
                    // Focus the field.
                    relativeLayout.setVisibility(View.INVISIBLE);
                    changeTheActionButtons(DISABLE_ACTION_BTNS);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    backward.setVisibility(View.INVISIBLE);
                    forward.setVisibility(View.INVISIBLE);
                    flag_keyboard = 1;
                    showActionBarTitle(false);
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    speakSpeech(et.getText().toString());
                    if(!et.getText().toString().equals("")) ttsButton.setImageResource(R.drawable.speaker_pressed);
                    reportLog(getLocalClassName()+", TtsSpeak: ", Log.INFO);
                    hideActionBtn(true);
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

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCy = mCm = mCd = mCn = mCl = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCk == 1) {
                        speakSpeech(side[1]);
                        mCk = 0;
                        singleEvent("ExpressiveIcon","ReallyLike");
                    } else {
                        speakSpeech(side[0]);
                        mCk = 1;
                        singleEvent("ExpressiveIcon","like");
                    }
                } else {
                    setBorderToIcon(0);
                    if (mCk == 1) {
                        reportLog(getLocalClassName()+", like: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(1), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(1));
                        mCk = 0;
                    } else {
                        reportLog(getLocalClassName()+", like: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(0), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(0));
                        mCk = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCn = mCl = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCd == 1) {
                        speakSpeech(side[7]);
                        mCd = 0;
                        singleEvent("ExpressiveIcon","ReallyDislike");
                    } else {
                        speakSpeech(side[6]);
                        mCd = 1;
                        singleEvent("ExpressiveIcon","Dislike");
                    }
                } else {
                    setBorderToIcon(1);
                    if (mCd == 1) {
                       reportLog(getLocalClassName()+", dislike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(7), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(7));
                        mCd = 0;
                    } else {
                       reportLog(getLocalClassName()+", dislike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(6), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(6));
                        mCd = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCm = mCd = mCn = mCl = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCy == 1) {
                        speakSpeech(side[3]);
                        mCy = 0;
                        singleEvent("ExpressiveIcon","ReallyYes");
                    } else {
                        speakSpeech(side[2]);
                        mCy = 1;
                        singleEvent("ExpressiveIcon","Yes");
                    }
                } else {
                    setBorderToIcon(2);
                    if (mCy == 1) {
                        reportLog(getLocalClassName()+", yes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(3), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(3));
                        mCy = 0;
                    } else {
                        reportLog(getLocalClassName()+", yes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(2), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(2));
                        mCy = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCl = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCn == 1) {
                        speakSpeech(side[9]);
                        mCn = 0;
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        speakSpeech(side[8]);
                        mCn = 1;
                        singleEvent("ExpressiveIcon","No");
                    }
                } else {
                    setBorderToIcon(3);
                    if (mCn == 1) {
                        reportLog(getLocalClassName()+", no: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(9), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(9));
                        mCn = 0;
                    } else {
                        reportLog(getLocalClassName()+", no: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(8), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(8));
                        mCn = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCd = mCn = mCl = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCm == 1) {
                        speakSpeech(side[5]);
                        mCm = 0;
                        singleEvent("ExpressiveIcon","ReallyMore");
                    } else {
                        speakSpeech(side[4]);
                        mCm = 1;
                        singleEvent("ExpressiveIcon","More");
                    }
                } else {
                    setBorderToIcon(4);
                    if (mCm == 1) {
                        reportLog(getLocalClassName()+", add: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(5), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(5));
                        mCm = 0;
                    } else {
                        reportLog(getLocalClassName()+", add: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(4), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(4));
                        mCm = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = 0;
                resetActionBtnImageIcons();
                if (image_flag == 0) {
                    if (mCl == 1) {
                        speakSpeech(side[11]);
                        mCl = 0;
                        singleEvent("ExpressiveIcon","ReallyLess");
                    } else {
                        speakSpeech(side[10]);
                        mCl = 1;
                        singleEvent("ExpressiveIcon","Less");
                    }
                } else {
                    setBorderToIcon(5);
                    if (mCl == 1) {
                        reportLog(getLocalClassName()+", minus: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(11), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(11));
                        mCl = 0;
                    } else {
                        reportLog(getLocalClassName()+", minus: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(10), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(10));
                        mCl = 1;
                    }
                }
                back.setImageResource(R.drawable.back_button);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); break;
            case R.id.settings: startActivity(new Intent(this, SettingActivity.class)); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
         startMeasuring();
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasuring("SequenceActivity");
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mSeqActSpeech = null;
        mDailyActivitiesIcons = null;
        relativeLayout = null;
        mDailyActivitiesSpeechText = null;
        mDailyActivitiesBelowText = null;
        heading = null;
        side = null;
        below = null;
        bt = null;
        
    }

    private void showActionBarTitle(boolean showTitle){
        if (showTitle)
            getSupportActionBar().setTitle(actionBarTitleTxt);
        else{
            actionBarTitleTxt = getSupportActionBar().getTitle().toString();
            getSupportActionBar().setTitle("");
        }
    }

    private void initializeViews(){
        like = findViewById(R.id.ivlike);
        dislike = findViewById(R.id.ivdislike);
        add = findViewById(R.id.ivadd);
        minus = findViewById(R.id.ivminus);
        yes = findViewById(R.id.ivyes);
        no = findViewById(R.id.ivno);
        home = findViewById(R.id.ivhome);
        keyboard = findViewById(R.id.keyboard);
        et = findViewById(R.id.et);
        relativeLayout = findViewById(R.id.relativeLayout);
        back = findViewById(R.id.ivback);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        tt1 = findViewById(R.id.tt1);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        arrow1 = findViewById(R.id.arrow1);
        arrow2 = findViewById(R.id.arrow2);
        final int MODE_PICTURE_ONLY = 1;
        if(mSession.getPictureViewMode() == MODE_PICTURE_ONLY){
            bt1.setVisibility(View.INVISIBLE);
            bt2.setVisibility(View.INVISIBLE);
            bt3.setVisibility(View.INVISIBLE);
        }
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void loadArraysFromResources() {
        side = getResources().getStringArray(R.array.arrActionSpeech);
        below = getResources().getStringArray(R.array.arrNavigationSpeech);
        bt = getResources().getStringArray(R.array.arrSeqActivityNavigationText);
        heading = getResources().getStringArray(R.array.arrSeqActivityHeadingText);
        strPrevious = bt[0].substring(2, bt[0].length());   strNext = bt[1].substring(0, bt[1].length()-2);

        SeqActivityVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.sequenceActVerbiage), SeqActivityVerbiageModel.class);
        mSeqActSpeech = verbiageModel.getVerbiageModel();

        switch(mLevelTwoItemPos){
            case 0:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityBrushingBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityBrushingSpeechText);
                mDailyActivitiesIcons = getResources().getStringArray(R.array.arrSeqActivityBrushingIcon);
                break;
            case 1:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityToiletBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityToiletSpeechText);
                mDailyActivitiesIcons = getResources().getStringArray(R.array.arrSeqActivityToiletIcon);
                break;
            case 2:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityBathingBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityBathingSpeechText);
                mDailyActivitiesIcons = getResources().getStringArray(R.array.arrSeqActivityBathingIcon);
                break;
            case 3:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityMorningRoutineBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityMorningRoutineSpeechText);
                mDailyActivitiesIcons = getResources().getStringArray(R.array.arrSeqActivityMorningRoutineIcon);
                break;
            case 4:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityBedtimeRoutineBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityBedtimeRoutineSpeechText);
                mDailyActivitiesIcons = getResources().getStringArray(R.array.arrSeqActivityBedtimeRoutineIcon);
                break;
        }
    }

    private void hideActionBtn(boolean hideBtn) {
        if(hideBtn) {
            like.setVisibility(View.INVISIBLE);
            dislike.setVisibility(View.INVISIBLE);
            add.setVisibility(View.INVISIBLE);
            minus.setVisibility(View.INVISIBLE);
            yes.setVisibility(View.INVISIBLE);
            no.setVisibility(View.INVISIBLE);
        }else{
            like.setVisibility(View.VISIBLE);
            dislike.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            minus.setVisibility(View.VISIBLE);
            yes.setVisibility(View.VISIBLE);
            no.setVisibility(View.VISIBLE);
        }
    }

    private void changeTheActionButtons(boolean setDisable) {
        if(setDisable) {
            like.setAlpha(0.5f);
            dislike.setAlpha(0.5f);
            yes.setAlpha(0.5f);
            no.setAlpha(0.5f);
            add.setAlpha(0.5f);
            minus.setAlpha(0.5f);
            like.setEnabled(false);
            dislike.setEnabled(false);
            yes.setEnabled(false);
            no.setEnabled(false);
            add.setEnabled(false);
            minus.setEnabled(false);
        }else{
            like.setAlpha(1f);
            dislike.setAlpha(1f);
            yes.setAlpha(1f);
            no.setAlpha(1f);
            add.setAlpha(1f);
            minus.setAlpha(1f);
            like.setEnabled(true);
            dislike.setEnabled(true);
            yes.setEnabled(true);
            no.setEnabled(true);
            add.setEnabled(true);
            minus.setEnabled(true);
        }
    }

    private void resetActionBtnImageIcons() {
        findViewById(R.id.borderView1).setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_1by3));
        findViewById(R.id.borderView2).setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_1by3));
        findViewById(R.id.borderView3).setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_1by3));
        like.setImageResource(R.drawable.ilikewithoutoutline);
        dislike.setImageResource(R.drawable.idontlikewithout);
        yes.setImageResource(R.drawable.iwantwithout);
        no.setImageResource(R.drawable.idontwantwithout);
        add.setImageResource(R.drawable.morewithout);
        minus.setImageResource(R.drawable.lesswithout);
    }

    private void setBorderToIcon(int actionBtnIdx) {
        if (image_flag == 1) {
            setBorderToView(findViewById(R.id.borderView1), actionBtnIdx);
        } else if (image_flag == 2) {
            setBorderToView(findViewById(R.id.borderView2), actionBtnIdx);
        } else if (image_flag == 3) {
            setBorderToView(findViewById(R.id.borderView3), actionBtnIdx);
        }
        switch (actionBtnIdx){
            case 0: like.setImageResource(R.drawable.ilikewithoutline); break;
            case 1: dislike.setImageResource(R.drawable.idontlikewithoutline); break;
            case 2: yes.setImageResource(R.drawable.iwantwithoutline); break;
            case 3: no.setImageResource(R.drawable.idontwantwithoutline); break;
            case 4: add.setImageResource(R.drawable.morewithoutline); break;
            case 5: minus.setImageResource(R.drawable.lesswithoutline); break;
            case 6: home.setImageResource(R.drawable.homepressed); break;
            default: break;
        }
    }

    private void setBorderToView(View borderView, int actionBtnIdx) {
        switch(actionBtnIdx){
            case 0: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_yellow_1by3));
                break;
            case 1: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_blue_1by3));
                break;
            case 2: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_green_1by3));
                break;
            case 3: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_red_1by3));
                break;
            case 4: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_light_1by3));
                break;
            case 5: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_dark_1by3));
                break;
            case 6: borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_brown_1by3));
                break;
            default:    borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_1by3));
        }
        borderView.invalidate();
    }
}