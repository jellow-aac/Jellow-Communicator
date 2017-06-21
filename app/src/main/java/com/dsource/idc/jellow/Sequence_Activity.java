package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.SessionManager;

import java.util.Locale;

/**
 * Created by ekalpa on 6/22/2016.
 */
public class Sequence_Activity extends AppCompatActivity {
    final int LANG_ENG = 0;
    private final boolean DISABLE_ACTION_BTNS = true;
    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    private int image_flag = -1, flag_keyboard = 0, mLevelTwoItemPos;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton;
    private EditText et;
    private KeyListener originalKeyListener;
    private TextToSpeech mTtts;
    private int[] mColor;
    private TextView tt1, bt1, bt2, bt3;
    private CircularImageView image1, image2, image3;
    private ImageView arrow1, arrow2, back;
    private LinearLayout linear;
    private Button forward, backward;

    private String[] daily_activities_text1 = new String[100];
    private String[] daily_activities_text = new String[100];
    private String[] heading = new String[100];
    private String[][][] layer_1_speech = new String[100][100][100];

    private int count = 0;
    private SessionManager mSession;

    private int sr, bw;
    private String strNext, strPrevious;

    private String[] side = new String[100];
    private String[] below = new String[100];
    private String[] bt = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setElevation(0);
        mColor = getResources().getIntArray(R.array.arrActionBtnColors);
        Typeface fontMuktaRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Regular.ttf");
        Typeface fontMuktaBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Bold.ttf");
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        if(mLevelTwoItemPos == 7)   mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)  mLevelTwoItemPos = 4;
        mSession = new SessionManager(this);
        {
            String strSrBw = mSession.getShadowRadiusAndBorderWidth();
            sr = Integer.valueOf(strSrBw.split(",")[0]);
            bw = Integer.valueOf(strSrBw.split(",")[1]);
        }

        if (mSession.getLanguage() == LANG_ENG) {
            daily_activities_text = daily_activities_text_english[mLevelTwoItemPos];
            daily_activities_text1 = daily_activities_text_english1[mLevelTwoItemPos];
            heading = heading_english;
            layer_1_speech = layer_1_speech_english;
            side = side_english;
            below = below_english;
            bt = bt_english;
            strNext = "NEXT";   strPrevious = "PREVIOUS";
        }else{
            daily_activities_text = daily_activities_text_hindi[mLevelTwoItemPos];
            daily_activities_text1 = daily_activities_text_hindi1[mLevelTwoItemPos];
            heading = heading_hindi;
            layer_1_speech = layer_1_speech_hindi;
            side = side_hindi;
            below = below_hindi;
            bt = bt_hindi;
            strNext = "आगे";    strPrevious = " पीछे";
        }

        mTtts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTtts.setEngineByPackageName("com.google.android.tts");
                    new BackgroundSpeechOperationsAsync().execute("");
                }
            }
        });

        mTtts.setSpeechRate((float) mSession.getSpeed() / 50);
        mTtts.setPitch((float) mSession.getPitch() / 50);

        like = (ImageView) findViewById(R.id.ivlike);
        dislike = (ImageView) findViewById(R.id.ivdislike);
        add = (ImageView) findViewById(R.id.ivadd);
        minus = (ImageView) findViewById(R.id.ivminus);
        yes = (ImageView) findViewById(R.id.ivyes);
        no = (ImageView) findViewById(R.id.ivno);
        home = (ImageView) findViewById(R.id.ivhome);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        et = (EditText) findViewById(R.id.et);
        linear = (LinearLayout) findViewById(R.id.linear);
        back = (ImageView) findViewById(R.id.ivback);
        forward = (Button) findViewById(R.id.forward);
        backward = (Button) findViewById(R.id.backward);
        tt1 = (TextView) findViewById(R.id.tt1);
        bt1 = (TextView) findViewById(R.id.bt1);
        bt2 = (TextView) findViewById(R.id.bt2);
        bt3 = (TextView) findViewById(R.id.bt3);

        forward.setText(bt[1]);
        backward.setText(bt[0]);
        backward.setEnabled(false);
        backward.setAlpha(.5f);
        et.setVisibility(View.INVISIBLE);

        tt1.setTypeface(fontMuktaBold);
        tt1.setTextColor(Color.rgb(64, 64, 64));
        tt1.setText(/*getSmallCapsString(*/heading[mLevelTwoItemPos].toLowerCase())/*)*/;

        bt1.setTypeface(fontMuktaRegular);
        bt1.setTextColor(Color.rgb(64, 64, 64));

        bt2.setTypeface(fontMuktaRegular);
        bt2.setTextColor(Color.rgb(64, 64, 64));

        bt3.setTypeface(fontMuktaRegular);
        bt3.setTextColor(Color.rgb(64, 64, 64));

        image1 = (CircularImageView) findViewById(R.id.image1);
        image2 = (CircularImageView) findViewById(R.id.image2);
        image3 = (CircularImageView) findViewById(R.id.image3);

        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow2 = (ImageView) findViewById(R.id.arrow2);

        like.setVisibility(View.INVISIBLE);
        dislike.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);
        minus.setVisibility(View.INVISIBLE);
        yes.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);

        bt1.setText(daily_activities_text[0]);
        bt2.setText(daily_activities_text[1]);
        bt3.setText(daily_activities_text[2]);
        image1.setImageResource(daily_activities[mLevelTwoItemPos][0]);
        image2.setImageResource(daily_activities[mLevelTwoItemPos][1]);
        image3.setImageResource(daily_activities[mLevelTwoItemPos][2]);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTtts.speak(strNext,TextToSpeech.QUEUE_FLUSH, null);
                backward.setEnabled(true);
                backward.setAlpha(1f);
                count = count + 3;

                like.setVisibility(View.INVISIBLE);
                dislike.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);

                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);

                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);

                image_flag = 0;

                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);

                if (daily_activities[mLevelTwoItemPos].length == count + 3) {
                    forward.setAlpha(.5f);
                    forward.setEnabled(false);
                }
                if (daily_activities[mLevelTwoItemPos].length < count + 3) {
                    if (mLevelTwoItemPos == 0) {
                        image1.setImageResource(daily_activities[mLevelTwoItemPos][count]);
                        image2.setImageResource(daily_activities[mLevelTwoItemPos][count + 1]);
                        arrow2.setVisibility(View.INVISIBLE);
                        image3.setVisibility(View.INVISIBLE);
                        bt3.setVisibility(View.INVISIBLE);
                        bt1.setText(daily_activities_text[count]);
                        bt2.setText(daily_activities_text[count + 1]);
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 4 || mLevelTwoItemPos == 3) {
                        image1.setImageResource(daily_activities[mLevelTwoItemPos][count]);
                        bt1.setText(daily_activities_text[count]);
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
                    bt1.setText(daily_activities_text[count]);
                    bt2.setText(daily_activities_text[count + 1]);
                    bt3.setText(daily_activities_text[count + 2]);
                    image1.setImageResource(daily_activities[mLevelTwoItemPos][count]);
                    image2.setImageResource(daily_activities[mLevelTwoItemPos][count + 1]);
                    image3.setImageResource(daily_activities[mLevelTwoItemPos][count + 2]);
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTtts.speak(strPrevious,TextToSpeech.QUEUE_FLUSH, null);
                count = count - 3;
                forward.setAlpha(1f);
                forward.setEnabled(true);

                like.setVisibility(View.INVISIBLE);
                dislike.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);

                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);

                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);

                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);

                image_flag = 0;

                arrow1.setVisibility(View.VISIBLE);
                arrow2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image1.setVisibility(View.VISIBLE);
                bt3.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.VISIBLE);
                bt1.setVisibility(View.VISIBLE);
                bt1.setText(daily_activities_text[count]);
                bt2.setText(daily_activities_text[count + 1]);
                bt3.setText(daily_activities_text[count + 2]);
                image1.setImageResource(daily_activities[mLevelTwoItemPos][count]);
                image2.setImageResource(daily_activities[mLevelTwoItemPos][count + 1]);
                image3.setImageResource(daily_activities[mLevelTwoItemPos][count + 2]);

                if (count == 0) {
                    backward.setEnabled(false);
                    backward.setAlpha(.5f);
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_flag == 1) {
                    like.setVisibility(View.INVISIBLE);
                    dislike.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    yes.setVisibility(View.INVISIBLE);
                    no.setVisibility(View.INVISIBLE);
                    image1.setBorderColor(-1);
                    image1.setShadowColor(0);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    image_flag = 1;
                    if (count + image_flag == daily_activities[mLevelTwoItemPos].length) {
                        like.setVisibility(View.INVISIBLE);
                        dislike.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        minus.setVisibility(View.INVISIBLE);
                        yes.setVisibility(View.INVISIBLE);
                        no.setVisibility(View.INVISIBLE);
                    } else {
                        like.setVisibility(View.VISIBLE);
                        dislike.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        minus.setVisibility(View.VISIBLE);
                        yes.setVisibility(View.VISIBLE);
                        no.setVisibility(View.VISIBLE);
                    }
                    mTtts.speak(daily_activities_text1[count], TextToSpeech.QUEUE_FLUSH, null);
                    image1.setBorderColor(-1283893945);
                    image1.setShadowColor(-1283893945);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(bw);

                    image2.setBorderColor(-1);
                    image2.setShadowColor(0);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(0);

                    image3.setBorderColor(-1);
                    image3.setShadowColor(0);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(0);

                    like.setImageResource(R.drawable.ilikewithoutoutline);
                    dislike.setImageResource(R.drawable.idontlikewithout);
                    yes.setImageResource(R.drawable.iwantwithout);
                    no.setImageResource(R.drawable.idontwantwithout);
                    add.setImageResource(R.drawable.morewithout);
                    minus.setImageResource(R.drawable.lesswithout);
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_flag == 2) {
                    like.setVisibility(View.INVISIBLE);
                    dislike.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    yes.setVisibility(View.INVISIBLE);
                    no.setVisibility(View.INVISIBLE);
                    image2.setBorderColor(-1);
                    image2.setShadowColor(0);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    image_flag = 2;
                    if (count + image_flag == daily_activities[mLevelTwoItemPos].length) {
                        like.setVisibility(View.INVISIBLE);
                        dislike.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        minus.setVisibility(View.INVISIBLE);
                        yes.setVisibility(View.INVISIBLE);
                        no.setVisibility(View.INVISIBLE);
                    } else {
                        like.setVisibility(View.VISIBLE);
                        dislike.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        minus.setVisibility(View.VISIBLE);
                        yes.setVisibility(View.VISIBLE);
                        no.setVisibility(View.VISIBLE);
                    }
                    mTtts.speak(daily_activities_text1[count + 1], TextToSpeech.QUEUE_FLUSH, null);
                    image2.setBorderColor(-1283893945);
                    image2.setShadowColor(-1283893945);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(bw);

                    image1.setBorderColor(-1);
                    image1.setShadowColor(0);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(0);

                    image3.setBorderColor(-1);
                    image3.setShadowColor(0);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(0);

                    like.setImageResource(R.drawable.ilikewithoutoutline);
                    dislike.setImageResource(R.drawable.idontlikewithout);
                    yes.setImageResource(R.drawable.iwantwithout);
                    no.setImageResource(R.drawable.idontwantwithout);
                    add.setImageResource(R.drawable.morewithout);
                    minus.setImageResource(R.drawable.lesswithout);
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_flag == 3) {
                    like.setVisibility(View.INVISIBLE);
                    dislike.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    minus.setVisibility(View.INVISIBLE);
                    yes.setVisibility(View.INVISIBLE);
                    no.setVisibility(View.INVISIBLE);
                    image3.setBorderColor(-1);
                    image3.setShadowColor(0);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    image_flag = 3;
                    if (count + image_flag == daily_activities[mLevelTwoItemPos].length) {
                        like.setVisibility(View.INVISIBLE);
                        dislike.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        minus.setVisibility(View.INVISIBLE);
                        yes.setVisibility(View.INVISIBLE);
                        no.setVisibility(View.INVISIBLE);
                    } else {
                        like.setVisibility(View.VISIBLE);
                        dislike.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        minus.setVisibility(View.VISIBLE);
                        yes.setVisibility(View.VISIBLE);
                        no.setVisibility(View.VISIBLE);
                    }
                    mTtts.speak(daily_activities_text1[count + 2], TextToSpeech.QUEUE_FLUSH, null);
                    image3.setBorderColor(-1283893945);
                    image3.setShadowColor(-1283893945);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(bw);

                    image2.setBorderColor(-1);
                    image2.setShadowColor(0);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(0);

                    image1.setBorderColor(-1);
                    image1.setShadowColor(0);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(0);

                    like.setImageResource(R.drawable.ilikewithoutoutline);
                    dislike.setImageResource(R.drawable.idontlikewithout);
                    yes.setImageResource(R.drawable.iwantwithout);
                    no.setImageResource(R.drawable.idontwantwithout);
                    add.setImageResource(R.drawable.morewithout);
                    minus.setImageResource(R.drawable.lesswithout);
                }
            }
        });

        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);

        back.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mTtts.speak(below[1], TextToSpeech.QUEUE_FLUSH, null);
                        if (flag_keyboard == 1) {
                            keyboard.setImageResource(R.drawable.keyboard_button);
                            back.setImageResource(R.drawable.back_button);
                            et.setVisibility(View.INVISIBLE);
                            linear.setVisibility(View.VISIBLE);
                            ttsButton.setVisibility(View.INVISIBLE);
                            flag_keyboard = 0;
                            changeTheActionButtons(!DISABLE_ACTION_BTNS);
                            forward.setVisibility(View.VISIBLE);
                            backward.setVisibility(View.VISIBLE);
                        } else {
                            count = 0;
                            image_flag = 0;
                            back.setImageResource(R.drawable.backpressed);
                            finish();
                        }
                    }
                });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                image_flag = 0;
                home.setImageResource(R.drawable.homepressed);
                mTtts.speak(below[0], TextToSpeech.QUEUE_FLUSH, null);
                startActivity(new Intent(Sequence_Activity.this, MainActivity.class));
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTtts.speak(below[2], TextToSpeech.QUEUE_FLUSH, null);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    linear.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    backward.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.VISIBLE);
                    et.setKeyListener(originalKeyListener);
                    // Focus the field.
                    linear.setVisibility(View.INVISIBLE);
                    changeTheActionButtons(DISABLE_ACTION_BTNS);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    backward.setVisibility(View.INVISIBLE);
                    forward.setVisibility(View.INVISIBLE);
                    flag_keyboard = 1;
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mTtts.setSpeechRate((float) mSession.getSpeed() / 50);
                    mTtts.setPitch((float) mSession.getPitch() / 50);
                    String tmpStr = et.getText().toString();
                    mTtts.speak(tmpStr, TextToSpeech.QUEUE_FLUSH, null);

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

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCy = mCm = mCd = mCn = mCl = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);
                like.setImageResource(R.drawable.ilikewithoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);

                if (image_flag == 0) {
                    if (mCk == 1) {
                        mTtts.speak(side[1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;
                    } else {
                        mTtts.speak(side[0], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[0]);
                        image1.setShadowColor(mColor[0]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[0]);
                        image2.setShadowColor(mColor[0]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[0]);
                        image3.setShadowColor(mColor[0]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCk == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][0], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 1;
                    }
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCm = mCd = mCn = mCl = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);

                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithoutline);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                if (image_flag == 0) {
                    if (mCy == 1) {
                        mTtts.speak(side[3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTtts.speak(side[2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[2]);
                        image1.setShadowColor(mColor[2]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[2]);
                        image2.setShadowColor(mColor[2]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[2]);
                        image3.setShadowColor(mColor[2]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCy == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCd = mCn = mCl = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithoutline);
                minus.setImageResource(R.drawable.lesswithout);

                if (image_flag == 0) {
                    if (mCm == 1) {
                        mTtts.speak(side[5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        mTtts.speak(side[4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[4]);
                        image1.setShadowColor(mColor[4]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[4]);
                        image2.setShadowColor(mColor[4]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[4]);
                        image3.setShadowColor(mColor[4]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCm == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
                    }
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCn = mCl = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithoutline);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);

                if (image_flag == 0) {
                    if (mCd == 1) {
                        mTtts.speak(side[7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        mTtts.speak(side[6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[1]);
                        image1.setShadowColor(mColor[1]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[1]);
                        image2.setShadowColor(mColor[1]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[1]);
                        image3.setShadowColor(mColor[1]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCd == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
                    }
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCl = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithoutline);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);

                if (image_flag == 0) {
                    if (mCn == 1) {
                        mTtts.speak(side[9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        mTtts.speak(side[8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[3]);
                        image1.setShadowColor(mColor[3]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[3]);
                        image2.setShadowColor(mColor[3]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[3]);
                        image3.setShadowColor(mColor[3]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCn == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCn = 0;
                image1.setBorderColor(-1);
                image1.setShadowColor(0);
                image1.setShadowRadius(sr);
                image1.setBorderWidth(0);
                image2.setBorderColor(-1);
                image2.setShadowColor(0);
                image2.setShadowRadius(sr);
                image2.setBorderWidth(0);
                image3.setBorderColor(-1);
                image3.setShadowColor(0);
                image3.setShadowRadius(sr);
                image3.setBorderWidth(0);
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithoutline);

                if (image_flag == 0) {
                    if (mCl == 1) {
                        mTtts.speak(side[11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        mTtts.speak(side[10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                } else {
                    if (image_flag == 1) {
                        image1.setBorderColor(mColor[5]);
                        image1.setShadowColor(mColor[5]);
                        image1.setShadowRadius(sr);
                        image1.setBorderWidth(bw);
                    } else if (image_flag == 2) {
                        image2.setBorderColor(mColor[5]);
                        image2.setShadowColor(mColor[5]);
                        image2.setShadowRadius(sr);
                        image2.setBorderWidth(bw);
                    } else if (image_flag == 3) {
                        image3.setBorderColor(mColor[5]);
                        image3.setShadowColor(mColor[5]);
                        image3.setShadowRadius(sr);
                        image3.setBorderWidth(bw);
                    }
                    if (mCl == 1) {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        mTtts.speak(layer_1_speech[mLevelTwoItemPos][count + image_flag - 1][10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                }
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
            case R.id.settings:
                startActivity(new Intent(this, Setting.class));
                break;
            case R.id.info:
                startActivity(new Intent(this, About_Jellow.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, Profile_form.class));
                break;
            case R.id.feedback:
                startActivity(new Intent(this, Feedback.class));
                break;
            case R.id.usage:
                startActivity(new Intent(this, Tutorial.class));
                break;
            case R.id.reset:
                startActivity(new Intent(this, Reset__preferences.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, Keyboard_Input.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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

    /*private final char[] smallCaps = new char[]
            {
                    '\uf761', //A
                    '\uf762',
                    '\uf763',
                    '\uf764',
                    '\uf765',
                    '\uf766',
                    '\uf767',
                    '\uf768',
                    '\uf769',
                    '\uf76A',
                    '\uf76B',
                    '\uf76C',
                    '\uf76D',
                    '\uf76E',
                    '\uf76F',
                    '\uf770',
                    '\uf771',
                    '\uf772',
                    '\uf773',
                    '\uf774',
                    '\uf775',
                    '\uf776',
                    '\uf777',
                    '\uf778',
                    '\uf779',
                    '\uf77A'   //Z
            };

    private final String getSmallCapsString(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = smallCaps[chars[i] - 'a'];
            }
        }
        return String.valueOf(chars);
    }*/

    private class BackgroundSpeechOperationsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                mTtts.setLanguage(new Locale("hin", "IND"));
            } catch (Exception e) {
                Thread.interrupted();
            }
            return "Executed";
        }
    }

    private Integer[][] daily_activities = {{R.drawable.rinsemouth, R.drawable.rinsetoothbrush,
            R.drawable.puttoothpaste, R.drawable.brushfrontteeth,
            R.drawable.brushbackteeth, R.drawable.brushtongue,
            R.drawable.rinsemouth, R.drawable.iamalldone}, {R.drawable.pullpantsdown, R.drawable.sitonthetoilet,
            R.drawable.washyourbottom, R.drawable.flushtoilet,
            R.drawable.pullpantsup, R.drawable.washhands,
            R.drawable.iamalldone}, {R.drawable.removeclothes, R.drawable.turnonwater,
            R.drawable.getinshower, R.drawable.wetbody,
            R.drawable.putsoap, R.drawable.putshampoo,
            R.drawable.putfacewash, R.drawable.washhair, R.drawable.washbody,
            R.drawable.closetap, R.drawable.dryhair,
            R.drawable.dryface, R.drawable.drybody,
            R.drawable.putonclothes, R.drawable.iamalldone}, { R.drawable.wakeup, R.drawable.washface,
            R.drawable.gototoilet, R.drawable.brushteeth,
            R.drawable.removeclothes, R.drawable.haveabath,
            R.drawable.getdressed, R.drawable.combhair, R.drawable.eatbreakfast,
            R.drawable.packlunch, R.drawable.packbagpack,
            R.drawable.takeschoolbag, R.drawable.haveagreatday}, {R.drawable.eatdinner, R.drawable.wearnightsuit,
            R.drawable.brushteeth, R.drawable.readstory,
            R.drawable.saygoodnight, R.drawable.sayprayres,
            R.drawable.sweetdreams}
    };

    private final String[] side_hindi = {"अच्छा लगता हैं", "सच में अच्छा लगता हैं", "हाँ", "सच में हाँ", "ज़्यादा", "सच में ज़्यादा", "अच्छा नहीं लगता हैं", "सच में अच्छा नहीं लगता हैं", "नहीं", "सच में नहीं", "कम", "सच में कम"};
    private final String[] side_english = {"like", "really like", "yes", "really yes", "more", "really more", "don’t like", "really don’t like", "no", "really no", "less", "really less"};

    private final String[] below_hindi = {"होम", "वापस", "कीबोर्ड"};
    private final String[] below_english = {"Home", "back", "keyboard"};

    private final String[] bt_hindi = {"<< पीछे", "आगे >>"};
    private final String[] bt_english = {"<< PREVIOUS", "NEXT >>"};

    private final String[][] daily_activities_text_english1 =
            {{"Rinse mouth", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush back teeth", "Brush tongue", "Rinse mouth", "All done"}, {"Pull pants down", "Sit on toilet", "Wash bottom", "Flushh toilet", "Pull pants up", "Wash hands", "All done"}, {"Remove clothes", "Turn on water", "Get in the shower", "Wet body", "Put soap", "shampoo हैर", "Put face wash", "Wash हैर", "Wash body", "Turn off water", "Dry हैर", "Dry face", "Dry body", "Put on clothes", "All done"}, {"Wake up", "Wash face", "Go to toilet", "Brush teeth", "Remove clothes", "Have a बाथ", "Get dressed", "Comb हैर", "Eat brekfust", "Pack lunch box", "Pack school bag", "Go to school", "Have a great day!"}, {"Eat dinner", "wear night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"}};

    private final String[][] daily_activities_text_hindi1 = {{"मुँह पानी से धोना", "टूथ ब्रश पानी से धोना", "ब्रश पर टूथपेस्ट लगाना", "सामने के दांत साफ़ करना", "पीछे के दांत साफ़ करना", "जीभ साफ़ करना", "मुँह पानी से धोना", "मैंने ख़त्म कर दिया"}, {"पैन्ट नीचे खींचना", "शौचालय में बैठना", "पिछला हिस्सा धोना", "टॉयलेट फ़्लश करना", "पैन्ट ऊपर खींचना", "हाथ धोना", "मैंने ख़त्म कर दिया"}, {"कपड़े निकालना", "पानी चालू करना", "शॉवर लेना", "शरीर को भिगोना", "साबुन लगाना", "बालों को शाम्पू लगाना ", "फेस वॉश लगाना", "बाल धोना", "शरीर धोना", "पानी बंद करना", "बाल सुखाना", "चेहरा पोंछना", "शरीर पोंछना", "कपड़े पैहनना", "मैंने ख़त्म कर दिया"}, {"उठना", "चेहरा धोना", "शौचालय जाना", "दांत साफ़ करना", "कपड़े उतारना", "नहाना", "कपड़े पैहनना", "कंघी करना", "सुबह का नाश्ता खाना", "खाने का डिब्बा भरना", "पाठशाला की bag भरना", "पाठशाला जाना", "आपका दिन अच्छा रहे"}, {"रात का भोजन", "रात के कपड़े पैहनना", "दांत साफ़ करना", "कहानियाँ पढ़ना", "शुभ रात्रि बोलना", "प्रार्थना करना", "प्यारे सपने देखो"}};

    private final String[][] daily_activities_text_hindi = {
            {"१. मुँह पानी से धोना", "२. टूथब्रश पानी से धोना", "३. टूथपेस्ट लगाना", "४. सामने के दांत साफ़", "५. पीछे के दांत साफ़", "६. जीभ साफ़", "७. मुँह पानी से धोना", "८. मैंने ख़त्म कर दिया"},
            {"१. पैंट नीचे खींचना", "२. शौचालय में बैठना", "३. पिछला हिस्सा धोना", "४. टॉयलेट फ़्लश करना", "५. पैंट ऊपर खींचना", "६. हाथ धोना", "७. मैंने ख़त्म कर दिया"},
            {"१. कपड़े निकालना", "२. पानी चालू करना", "३. शावर लेना", "४. शरीर को भिगोना", "५. साबुन लगाना", "६. शैम्पू लगाना ", "७. फेस वॉश लगाना", "८. बाल धोना", "९. शरीर धोना", "१०. पानी बंद करना", "११. बाल सुखाना ", "१२. चेहरा पोंछना", "१३. शरीर पोंछना", "१४. कपड़े पैहनना", "१५. मैंने ख़त्म कर दिया"},
            {"१. उठना", "२ . चेहरा धोना", "३. शौचालय जाना", "४. दांत साफ़ करना", "५. कपड़े उतारना", "६. नहाना", "७. कपड़े पैहनना", "८. कंघी करना", "९. नाश्ता खाना", "१०. डिब्बा भरना", "११. पाठशाला की बॅग भरना", "१२. पाठशाला जाना", "१३. आपका दिन अच्छा रहे"},
            {"१. रात का भोजन", "२. रात के कपड़े पैहनना", "३. दांत साफ़ करना", "४. कहानियाँ पढ़ना", "५. शुभ रात्रि बोलना", "६. प्रार्थना करना", "७. प्यारे सपने देखो"}
    };

    private final String[][] daily_activities_text_english = {
            {"1. Rinse mouth", "2. Rinse toothbrush", "3. Put toothpaste", "4. Brush front teeth", "5. Brush back teeth", "6. Brush tongue", "7. Rinse mouth", "8. All done"},
            {"1. Pull pants down", "2. Sit on toilet", "3. Wash bottom", "4. Flush toilet", "5. Pull pants up", "6. Wash hands", "7. All done"},
            {"1. Remove clothes", "2. Turn on water", "3. Get in shower", "4. Wet body", "5. Put soap", "6. Shampoo hair", "7. Put face wash", "8. Wash hair", "9. Wash body", "10. Turn off water", "11. Dry hair", "12. Dry face", "13. Dry body", "14. Put on clothes", "15. All done"},
            {"1. Wake up", "2. Wash face", "3. Go to toilet", "4. Brush teeth", "5. Remove clothes", "6. Have a bath", "7. Get dressed", "8. Comb Hair", "9. Eat breakfast", "10. Pack lunchbox", "11. Pack school bag", "12. Go to school", "13. Have a great day"},
            {"1. Eat dinner", "2. Wear night dress", "3. Brush teeth", "4. Read story", "5. Say goodnight", "6. Say prayers", "7. Sweet dreams!"}
    };

    private final String[] heading_english = {"brushing", "toilet", "bathing", "\tmorning routine", "\tbedtime routine"};

    private final String[] heading_hindi = {"ब्रश करना", "शौचालय ", "नहाना", "सुबह के कार्य", "रात के कार्य"};

    private final String[][][] layer_1_speech_english = {{{"I like to rinse my mouth",
            "I really like to rinse my mouth",
            "I want to rinse my mouth",
            "I really want to rinse my mouth",
            "I want to rinse my mouth some more",
            "I really want to rinse my mouth some more",
            "I don’t like to rinse my mouth",
            "I really don’t like to rinse my mouth",
            "I don’t want to rinse my mouth",
            "I really don’t want to rinse my mouth",
            "I don’t want to rinse my mouth more",
            "I really don’t want to rinse my mouth any more",

    },{"I like to rinse my toothbrush",
            "I really like to rinse my toothbrush",
            "I want to rinse my toothbrush",
            "I really want to rinse my toothbrush",
            "I want to rinse my toothbrush some more",
            "I really want to rinse my toothbrush some more",
            "I don’t like to rinse my toothbrush",
            "I really don’t like to rinse my toothbrush",
            "I don’t want to rinse my toothbrush",
            "I really don’t want to rinse my toothbrush",
            "I don’t want to rinse my toothbrush more",
            "I really don’t want to rinse my toothbrush any more"

    },{"I like to put toothpaste on my brush",
            "I really like to put toothpaste on my brush",
            "I want to put toothpaste on my brush",
            "I really want to put toothpaste on my brush",
            "I want to put some more toothpaste on my brush",
            "I really want to put some more toothpaste on my brush",
            "I don’t like to put toothpaste on my brush",
            "I really don’t like to put toothpaste on my brush",
            "I don’t want to put toothpaste on my brush",
            "I really don’t want to put toothpaste on my brush",
            "I don’t want to put more toothpaste on my brush",
            "I really don’t want to put any more toothpaste on my brush"

    },{"I like to brush my front teeth",
            "I really like to brush my front teeth",
            "I want to brush my front teeth",
            "I really want to brush my front teeth",
            "I want to brush my front teeth some more",
            "I really want to brush my front teeth some more",
            "I don’t like to brush my front teeth",
            "I really don’t like to brush my front teeth",
            "I don’t want to brush my front teeth",
            "I really don’t want to brush my front teeth",
            "I don’t want to brush my front teeth more",
            "I really don’t want to brush my front teeth any more"

    },{"I like to brush my back teeth",
            "I really like to brush my back teeth",
            "I want to brush my back teeth",
            "I really want to brush my back teeth",
            "I want to brush my back teeth some more",
            "I really want to brush my back teeth some more",
            "I don’t like to brush my back teeth",
            "I really don’t like to brush my back teeth",
            "I don’t want to brush my back teeth",
            "I really don’t want to brush my back teeth",
            "I don’t want to brush my back teeth more",
            "I really don’t want to brush my back teeth any more"

    },{"I like to brush my tongue",
            "I really like to brush my tongue",
            "I want to brush my tongue",
            "I really want to brush my tongue",
            "I want to brush my tongue some more",
            "I really want to brush my tongue some more",
            "I don’t like to brush my tongue",
            "I really don’t like to brush my tongue",
            "I don’t want to brush my tongue",
            "I really don’t want to brush my tongue",
            "I don’t want to brush my tongue more",
            "I really don’t want to brush my tongue any more",

    },{"I like to rinse my mouth",
            "I really like to rinse my mouth",
            "I want to rinse my mouth",
            "I really want to rinse my mouth",
            "I want to rinse my mouth some more",
            "I really want to rinse my mouth some more",
            "I don’t like to rinse my mouth",
            "I really don’t like to rinse my mouth",
            "I don’t want to rinse my mouth",
            "I really don’t want to rinse my mouth",
            "I don’t want to rinse my mouth more",
            "I really don’t want to rinse my mouth any more"

    }},{{
            "I have to pull my pants down",
            "I really have to pull my pants down",
            "I want to pull my pants down",
            "I really want to pull my pants down",
            "I want to pull my pants down again",
            "I really want to pull my pants down again",
            "I don’t have to pull my pants down",
            "I really don’t have to pull my pants down",
            "I don’t want to pull my pants down",
            "I really don’t want to pull my pants down",
            "I don’t want to pull my pants down again",
            "I really don’t want to pull my pants down again"

    },{"I have to sit on the toilet",
            "I really have to sit on the toilet",
            "I want to sit on the toilet",
            "I really want to sit on the toilet",
            "I want to sit on the toilet again",
            "I really want to sit on the toilet again",
            "I don’t have to sit on the toilet",
            "I really don’t have to sit on the toilet",
            "I don’t want to sit on the toilet",
            "I really don’t want to sit on the toilet",
            "I don’t want to sit on the toilet again",
            "I really don’t want to sit on the toilet again"

    },{"I have to wash my bottom",
            "I really have to wash my bottom",
            "I want to wash my bottom",
            "I really want to wash my bottom",
            "I want to wash my bottom again",
            "I really want to wash my bottom again",
            "I don’t have to wash my bottom",
            "I really don’t have to wash my bottom",
            "I don’t want to wash my bottom",
            "I really don’t want to wash my bottom",
            "I don’t want to wash my bottom again",
            "I really don’t want to wash my bottom again"

    },{"I have to flushh the toilet",
            "I really have to flushh the toilet",
            "I want to flushh the toilet",
            "I really want to flushh the toilet",
            "I want to flushh the toilet again",
            "I really want to flushh the toilet again",
            "I don’t have to flushh the toilet",
            "I really don’t have to flushh the toilet",
            "I don’t want to flushh the toilet",
            "I really don’t want to flushh the toilet",
            "I don’t want to flushh the toilet again",
            "I really don’t want to flushh the toilet again"

    },{"I have to pull my pants up",
            "I really have to pull my pants up",
            "I want to pull my pants up",
            "I really want to pull my pants up",
            "I want to pull my pants up again",
            "I really want to pull my pants up again",
            "I don’t have to pull my pants up",
            "I really don’t have to pull my pants up",
            "I don’t want to pull my pants up",
            "I really don’t want to pull my pants up",
            "I don’t want to pull my pants up again",
            "I really don’t want to pull my pants up again",

    },{"I have to wash my hands",
            "I really have to wash my hands",
            "I want to wash my hands",
            "I really want to wash my hands",
            "I want to wash my hands again",
            "I really want to wash my hands again",
            "I don’t have to wash my hands",
            "I really don’t have to wash my hands",
            "I don’t want to wash my hands",
            "I really don’t want to wash my hands",
            "I don’t want to wash my hands again",
            "I really don’t want to wash my hands again"
    }},{{"I have to remove my clothes",
            "I really have to remove my clothes",
            "I want to remove my clothes",
            "I really want to remove my clothes",
            "I want to remove my clothes again",
            "I really want to remove my clothes again",
            "I don’t have to remove my clothes",
            "I really don’t have to remove my clothes",
            "I don’t want to remove my clothes",
            "I really don’t want to remove my clothes",
            "I don’t want to remove my clothes again",
            "I really don’t want to remove my clothes again"

    },{"I have to turn on the water",
            "I really have to turn on the water",
            "I want to turn on the water",
            "I really want to turn on the water",
            "I want to turn on the water again",
            "I really want to turn on the water again",
            "I don’t have to turn on the water",
            "I really don’t have to turn on the water",
            "I don’t want to turn on the water",
            "I really don’t want to turn on the water",
            "I don’t want to turn on the water again",
            "I really don’t want to turn on the water again"

    },{"I have to get in the shower",
            "I really have to get in the shower",
            "I want to get in the shower",
            "I really want to get in the shower",
            "I want to get in the shower again",
            "I really want to get in the shower again",
            "I don’t have to get in the shower",
            "I really don’t have to get in the shower",
            "I don’t want to get in the shower",
            "I really don’t want to get in the shower",
            "I don’t want to get in the shower again",
            "I really don’t want to get in the shower again"

    },{"I like to wet my body",
            "I really like to wet my body",
            "I want to wet my body",
            "I really want to wet my body",
            "I want to wet my body again",
            "I really want to wet my body again",
            "I don’t like to wet my body",
            "I really don’t like to wet my body",
            "I don’t want to wet my body",
            "I really don’t want to wet my body",
            "I don’t want to wet my body again",
            "I really don’t want to wet my body again"

    },{"I like to put soap on my body",
            "I really like to put soap on my body",
            "I want to put soap on my body",
            "I really want to put soap on my body",
            "I want to put some more soap on my body",
            "I really want to put some more soap on my body",
            "I don’t like to put soap on my body",
            "I really don’t like to put soap on my body",
            "I don’t want to put soap on my body",
            "I really don’t want to put soap on my body",
            "I don’t want to put more soap on my body",
            "I really don’t want to put any more soap on my body"

    },{"I like to shampoo my हैर",
            "I really like to shampoo my हैर",
            "I want to shampoo my हैर",
            "I really want to shampoo my हैर",
            "I want to shampoo my हैर again",
            "I really want to shampoo my हैर again",
            "I don’t like to shampoo my हैर",
            "I really don’t like to shampoo my हैर",
            "I don’t want to shampoo my हैर",
            "I really don’t want to shampoo my हैर",
            "I don’t want to shampoo my हैर again",
            "I really don’t want to shampoo my हैर again"

    },{"I like to use face wash",
            "I really like to use face wash",
            "I want to use some face wash",
            "I really want to use some face wash",
            "I want to use face wash again",
            "I really want to use face wash again",
            "I don’t like to use face wash",
            "I really don’t like to use face wash",
            "I don’t want to use face wash",
            "I really don’t want to use face wash",
            "I don’t want to use face wash again",
            "I really don’t want to use face wash again"

    },{"I like to wash my हैर",
            "I really like to wash my हैर",
            "I want to wash my हैर",
            "I really want to wash my हैर",
            "I want to wash my हैर again",
            "I really want to wash my हैर again",
            "I don’t like to wash my हैर",
            "I really don’t like to wash my हैर",
            "I don’t want to wash my हैर",
            "I really don’t want to wash my हैर",
            "I don’t want to wash my हैर again",
            "I really don’t want to wash my हैर again"

    },{"I like to wash my body",
            "I really like to wash my body",
            "I want to wash my body",
            "I really want to wash my body",
            "I want to wash my body again",
            "I really want to wash my body again",
            "I don’t like to wash my body",
            "I really don’t like to wash my body",
            "I don’t want to wash my body",
            "I really don’t want to wash my body",
            "I don’t want to wash my body again",
            "I really don’t want to wash my body again"

    },{"I have to turn off the water",
            "I really have to turn off the water",
            "I want to turn off the water",
            "I really want to turn off the water",
            "I want to turn off the water again",
            "I really want to turn off the water again",
            "I don’t have to turn off the water",
            "I really don’t have to turn off the water",
            "I don’t want to turn off the water",
            "I really don’t want to turn off the water",
            "I don’t want to turn off the water again",
            "I really don’t want to turn off the water again"

    },{"I like to dry my हैर",
            "I really like to dry my हैर",
            "I want to dry my हैर",
            "I really want to dry my हैर",
            "I want to dry my हैर again",
            "I really want to dry my हैर again",
            "I don’t like to dry my हैर",
            "I really don’t like to dry my हैर",
            "I don’t want to dry my हैर",
            "I really don’t want to dry my हैर",
            "I don’t want to dry my हैर again",
            "I really don’t want to dry my हैर again"

    },{"I like to dry my face",
            "I really like to dry my face",
            "I want to dry my face",
            "I really want to dry my face",
            "I want to dry my face again",
            "I really want to dry my face again",
            "I don’t like to dry my face",
            "I really don’t like to dry my face",
            "I don’t want to dry my face",
            "I really don’t want to dry my face",
            "I don’t want to dry my face again",
            "I really don’t want to dry my face again",

    },{"I like to dry my body",
            "I really like to dry my body",
            "I want to dry my body",
            "I really want to dry my body",
            "I want to dry my body again",
            "I really want to dry my body again",
            "I don’t like to dry my body",
            "I really don’t like to dry my body",
            "I don’t want to dry my body",
            "I really don’t want to dry my body",
            "I don’t want to dry my body again",
            "I really don’t want to dry my body again"

    },{"I have to put my clothes on",
            "I really have to put my clothes on",
            "I want to put my clothes on",
            "I really want to put my clothes on",
            "I want to put my clothes on again",
            "I really want to put my clothes on again",
            "I don’t have to put my clothes on",
            "I really don’t have to put my clothes on",
            "I don’t want to put my clothes on",
            "I really don’t want to put my clothes on",
            "I don’t want to put my clothes on again",
            "I really don’t want to put my clothes on again"
    }},{{"I like to wake up early in the morning",
            "I really like to wake up early in the morning",
            "I want to wake up early in the morning",
            "I really want to wake up early in the morning",
            "I want to wake up early in the morning again",
            "I really want to wake up early in the morning again",
            "I don’t like to wake up early in the morning",
            "I really don’t like to wake up early in the morning",
            "I don’t want to wake up early in the morning",
            "I really don’t want to wake up early in the morning",
            "I don’t want to wake up early in the morning again",
            "I really don’t want to wake up early in the morning again"

    },{"I like to wash my face",
            "I really like to wash my face",
            "I want to wash my face",
            "I really want to wash my face",
            "I want to wash my face again",
            "I really want to wash my face again",
            "I don’t like to wash my face",
            "I really don’t like to wash my face",
            "I don’t want to wash my face",
            "I really don’t want to wash my face",
            "I don’t want to wash my face again",
            "I really don’t want to wash my face again"

    },{"I like to go to the toilet",
            "I really like to go to the toilet ",
            "I want to go to the toilet ",
            "I really want to go to the toilet ",
            "I want to go to the toilet again",
            "I really want to go to the toilet again",
            "I don’t like to go to the toilet ",
            "I really don’t like to go to the toilet ",
            "I don’t want to go to the toilet ",
            "I really don’t want to go to the toilet ",
            "I don’t want to go to the toilet again",
            "I really don’t want to go to the toilet again"

    },{"I like to brush my teeth",
            "I really like to brush my teeth",
            "I want to brush my teeth",
            "I really want to brush my teeth",
            "I want to brush my teeth again",
            "I really want to brush my teeth again",
            "I don’t like to brush my teeth",
            "I really don’t like to brush my teeth",
            "I don’t want to brush my teeth",
            "I really don’t want to brush my teeth",
            "I don’t want to brush my teeth again",
            "I really don’t want to brush my teeth again"

    },{"I have to remove my clothes",
            "I really have to remove my clothes",
            "I want to remove my clothes",
            "I really want to remove my clothes",
            "I want to remove my clothes again",
            "I really want to remove my clothes again",
            "I don’t have to remove my clothes",
            "I really don’t have to remove my clothes",
            "I don’t want to remove my clothes",
            "I really don’t want to remove my clothes",
            "I don’t want to remove my clothes again",
            "I really don’t want to remove my clothes again"

    },{"I like to bathe",
            "I really like to bathe",
            "I want to bathe",
            "I really want to bathe",
            "I want to bathe again",
            "I really want to bathe again",
            "I don’t like to bathe",
            "I really don’t like to bathe",
            "I don’t want to bathe",
            "I really don’t want to bathe",
            "I don’t want to bathe again",
            "I really don’t want to bathe again"

    },{"I like to get dressed",
            "I really like to get dressed",
            "I want to get dressed",
            "I really want to get dressed",
            "I want to get dressed again",
            "I really want to get dressed again",
            "I don’t like to get dressed",
            "I really don’t like to get dressed",
            "I don’t want to get dressed",
            "I really don’t want to get dressed",
            "I don’t want to get dressed again",
            "I really don’t want to get dressed again"

    },{"I like to comb my हैर",
            "I really like to comb my हैर",
            "I want to comb my हैर",
            "I really want to comb my हैर",
            "I want to comb my हैर again",
            "I really want to comb my हैर again",
            "I don’t like to comb my हैर",
            "I really don’t like to comb my हैर",
            "I don’t want to comb my हैर",
            "I really don’t want to comb my हैर",
            "I don’t want to comb my हैर again",
            "I really don’t want to comb my हैर again"

    },{"I like to eat brekfust",
            "I really like to eat brekfust",
            "I want to eat brekfust",
            "I really want to eat brekfust",
            "I want to eat more brekfust",
            "I really want to eat some more brekfust",
            "I don’t like to eat brekfust",
            "I really don’t like to eat brekfust",
            "I don’t want to eat brekfust",
            "I really don’t want to eat brekfust",
            "I don’t want to eat more brekfust",
            "I really don’t want to eat any more brekfust"

    },{"I like to pack my lunch box",
            "I really like to pack my lunch box",
            "I want to pack my lunch box",
            "I really want to pack my lunch box",
            "I want to pack my lunch box again",
            "I really want to pack my lunch box again",
            "I don’t like to pack my lunch box",
            "I really don’t like to pack my lunch box",
            "I don’t want to pack my lunch box",
            "I really don’t want to pack my lunch box",
            "I don’t want to pack my lunch box again",
            "I really don’t want to pack my lunch box again"

    },{"I like to pack my school bag",
            "I really like to pack my school bag",
            "I want to pack my school bag",
            "I really want to pack my school bag",
            "I want to pack my school bag again",
            "I really want to pack my school bag again",
            "I don’t like to pack my school bag",
            "I really don’t like to pack my school bag",
            "I don’t want to pack my school bag",
            "I really don’t want to pack my school bag",
            "I don’t want to pack my school bag again",
            "I really don’t want to pack my school bag again"

    },{"I like to go to school",
            "I really like to go to school",
            "I want to go to school",
            "I really want to go to school",
            "I want to go to school again",
            "I really want to go to school again",
            "I don’t like to go to school",
            "I really don’t like to go to school",
            "I don’t want to go to school",
            "I really don’t want to go to school",
            "I don’t want to go to school again",
            "I really don’t want to go to school again"

    }},{{"I like to eat dinner",
            "I really like to eat dinner",
            "I want to eat dinner",
            "I really want to eat dinner",
            "I want to eat more dinner",
            "I really want to eat some more dinner",
            "I don’t like to eat dinner",
            "I really don’t like to eat dinner",
            "I don’t want to eat dinner",
            "I really don’t want to eat dinner",
            "I don’t want to eat more dinner",
            "I really don’t want to eat any more dinner"

    },{"I like to Wear my night dress",
            "I really like to Wear my night dress",
            "I want to Wear my night dress",
            "I really want to Wear my night dress",
            "I want to Wear my night dress again",
            "I really want to Wear my night dress again",
            "I don’t like to Wear my night dress",
            "I really don’t like to Wear my night dress",
            "I don’t want to Wear my night dress",
            "I really don’t want to Wear my night dress",
            "I don’t want to Wear my night dress again",
            "I really don’t want to Wear my night dress again"

    },{"I like to brush my teeth",
            "I really like to brush my teeth",
            "I want to brush my teeth",
            "I really want to brush my teeth",
            "I want to brush my teeth again",
            "I really want to brush my teeth again",
            "I don’t like to brush my teeth",
            "I really don’t like to brush my teeth",
            "I don’t want to brush my teeth",
            "I really don’t want to brush my teeth",
            "I don’t want to brush my teeth again",
            "I really don’t want to brush my teeth again"

    },{"I like to read stories",
            "I really like to read stories",
            "I want to read a story",
            "I really want to read a story",
            "I want to read another story",
            "I really want to read another story",
            "I don’t like to read stories",
            "I really don’t like to read stories",
            "I don’t want to read a story",
            "I really don’t want to read a story",
            "I don’t want to read another story",
            "I really don’t want to read another story"

    },{"I like to say goodnight to every1",
            "I really like to say goodnight to every1",
            "I want to say goodnight to every1",
            "I really want to say goodnight to every1",
            "I want to say goodnight to every1 again",
            "I really want to say goodnight to every1 again",
            "I don’t like to say goodnight to every1",
            "I really don’t like to say goodnight to every1",
            "I don’t want to say goodnight to every1",
            "I really don’t want to say goodnight to every1",
            "I don’t want to say goodnight to every1 again",
            "I really don’t want to say goodnight to every1 again"

    },{"I like to say my prayers",
            "I really like to say my prayers",
            "I want to say my prayers",
            "I really want to say my prayers",
            "I want to say my prayers again",
            "I really want to say my prayers again",
            "I don’t like to say my prayers",
            "I really don’t like to say my prayers",
            "I don’t want to say my prayers",
            "I really don’t want to say my prayers",
            "I don’t want to say my prayers again",
            "I really don’t want to say my prayers again"
    }}};


    String[][][] layer_1_speech_hindi = {{{"मुझे अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे अपना मुँह पानी से धोना हैं",
            "मुझे सच में अपना मुँह पानी से धोना हैं",
            "मुझे अपना मुँह थोड़ा और धोना हैं",
            "मुझे सच में अपना मुँह थोड़ा और धोना हैं",
            "मुझे अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना मुँह पानी से धोना नहीं हैं",
            "मुझे सच में अपना मुँह पानी से धोना नहीं हैं",
            "मुझे अपना मुँह पानी से और नहीं धोना हैं",
            "मुझे सच में अपना मुँह पानी से और नहीं धोना हैं"

    },{"मुझे अपना टूथ ब्रश पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से धोना अच्छा लगता हैं",
            "मुझे अपना टूथ ब्रश पानी से धोना हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से धोना हैं",
            "मुझे अपना टूथ ब्रश पानी से और धोना हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से और धोना हैं",
            "मुझे अपना टूथ ब्रश पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना टूथ ब्रश पानी से धोना नहीं हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से धोना नहीं हैं",
            "मुझे अपना टूथ ब्रश पानी से और नहीं धोना हैं",
            "मुझे सच में अपना टूथ ब्रश पानी से और नहीं धोना हैं"

    },{"मुझे अपने ब्रश पर टूथपेस्ट लगाना अच्छा लगता हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगाना अच्छा लगता हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट लगाना हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगाना हैं",
            "मुझे अपने ब्रश पर थोड़ा टूथपेस्ट लगाना हैं",
            "मुझे सच में अपने ब्रश पर बहुत टूथपेस्ट लगाना हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगाना अच्छा नहीं लगता हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट नहीं लगाना हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट नहीं लगाना हैं",
            "मुझे अपने ब्रश पर और टूथपेस्ट नहीं लगाना हैं",
            "मुझे सच में अपने ब्रश पर और टूथपेस्ट नहीं लगाना हैं"

    },{"मुझे अपने सामने के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने सामने के दांत साफ़ करने हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करने हैं",
            "मुझे अपने सामने के दांत थोड़े और साफ़ करने हैं",
            "मुझे सच में अपने सामने के दांत थोड़े और साफ़ करने हैं",
            "मुझे अपने सामने के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने सामने के दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने सामने के दांत साफ़ नहीं करने हैं",
            "मुझे अपने सामने के दांत और साफ़ नहीं करने हैं",
            "मुझे सच में अपने सामने के दांत और साफ़ नहीं करने हैं"

    },{"मुझे अपने पीछे के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने पीछे के दांत साफ़ करने हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करने हैं",
            "मुझे अपने पीछे के दांत थोड़े और साफ़ करने हैं",
            "मुझे सच में अपने पीछे के दांत थोड़े और साफ़ करने हैं",
            "मुझे अपने पीछे के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने पीछे के दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ नहीं करने हैं",
            "मुझे अपने पीछे के दांत और साफ़ नहीं करने हैं",
            "मुझे सच में अपने पीछे के दांत और साफ़ नहीं करने हैं"

    },{"मुझे अपनी जीभ साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपनी जीभ साफ़ करना अच्छा लगता हैं",
            "मुझे अपनी जीभ साफ़ करनी हैं",
            "मुझे सच में अपनी जीभ साफ़ करनी हैं",
            "मुझे अपनी जीभ थोड़ी और साफ़ करनी हैं",
            "मुझे सच में अपनी जीभ थोड़ी और साफ़ करनी हैं",
            "मुझे अपनी जीभ साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी जीभ साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपनी जीभ साफ़ नहीं करनी हैं",
            "मुझे सच में अपनी जीभ साफ़ नहीं करनी हैं",
            "मुझे अपनी जीभ और साफ़ नहीं करनी हैं",
            "मुझे सच में अपनी जीभ और साफ़ नहीं करनी हैं"

    },{"मुझे अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे अपना मुँह पानी से धोना हैं",
            "मुझे सच में अपना मुँह पानी से धोना हैं",
            "मुझे अपना मुँह थोड़ा और धोना हैं",
            "मुझे सच में अपना मुँह थोड़ा और धोना हैं",
            "मुझे अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना मुँह पानी से धोना नहीं हैं",
            "मुझे सच में अपना मुँह पानी से धोना नहीं हैं",
            "मुझे अपना मुँह पानी से और नहीं धोना हैं",
            "मुझे सच में अपना मुँह पानी से और नहीं धोना हैं"

    }},{{"मुझे अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे सच में अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे सच में अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे फिर से अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे सच में फिर से अपनी पैन्ट नीचे खींचनी हैं",
            "मुझे अपनी पैन्ट नीचे नहीं खींचनी हैं",
            "मुझे सच में अपनी पैन्ट नीचे नहीं खींचनी हैं",
            "मुझे अपनी पैन्ट नीचे नहीं खींचनी हैं",
            "मुझे सच में अपनी पैन्ट नीचे नहीं खींचनी हैं",
            "मुझे फिर से अपनी पैन्ट नीचे नहीं खींचनी हैं",
            "मुझे सच में फिर से अपनी पैन्ट नीचे नहीं खींचनी हैं"

    },{"मुझे शौचालय में बैठना हैं",
            "मुझे सच में शौचालय में बैठना हैं",
            "मुझे शौचालय में बैठना हैं",
            "मुझे सच में शौचालय में बैठना हैं",
            "मुझे फिर से शौचालय में बैठना हैं",
            "मुझे सच में फिर से शौचालय में बैठना हैं",
            "मुझे शौचालय में नहीं बैठना हैं",
            "मुझे सच में शौचालय में नहीं बैठना हैं",
            "मुझे शौचालय में नहीं बैठना हैं",
            "मुझे सच में शौचालय में नहीं बैठना हैं",
            "मुझे फिर से शौचालय में नहीं बैठना हैं",
            "मुझे सच में फिर से शौचालय में नहीं बैठना हैं"

    },{"मुझे मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा धोना हैं",
            "मुझे मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा धोना हैं",
            "मुझे फिर से मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में फिर से मेरा पिछला हिस्सा धोना हैं",
            "मुझे मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे फिर से मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में फिर से मेरा पिछला हिस्सा नहीं धोना हैं"

    },{"मुझे टॉयलेट फ़्लश करना हैं",
            "मुझे सच में टॉयलेट फ़्लश करना हैं",
            "मुझे टॉयलेट फ़्लश करना हैं",
            "मुझे सच में टॉयलेट फ़्लश करना हैं",
            "मुझे फिर से टॉयलेट फ़्लश करना हैं",
            "मुझे सच में फिर से टॉयलेट फ़्लश करना हैं",
            "मुझे टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे फिर से टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में फिर से टॉयलेट फ़्लश नहीं करना हैं"

    },{"मुझे अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे सच में अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे सच में अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे फिर से अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे सच में फिर से अपनी पैन्ट ऊपर खींचनी हैं",
            "मुझे अपनी पैन्ट ऊपर नहीं खींचनी हैं",
            "मुझे सच में अपनी पैन्ट ऊपर नहीं खींचनी हैं",
            "मुझे अपनी पैन्ट ऊपर नहीं खींचनी हैं",
            "मुझे सच में अपनी पैन्ट ऊपर नहीं खींचनी हैं",
            "मुझे फिर से अपनी पैन्ट ऊपर नहीं खींचनी हैं",
            "मुझे सच में फिर से अपनी पैन्ट ऊपर नहीं खींचनी हैं"

    },{"मुझे अपने हाथ धोने हैं",
            "मुझे सच में अपने हाथ धोने हैं",
            "मुझे अपने हाथ धोने हैं",
            "मुझे सच में अपने हाथ धोने हैं",
            "मुझे फिर से अपने हाथ धोने हैं",
            "मुझे सच में फिर से अपने हाथ धोने हैं",
            "मुझे अपने हाथ नहीं धोने हैं",
            "मुझे सच में अपने हाथ नहीं धोने हैं",
            "मुझे अपने हाथ नहीं धोने हैं",
            "मुझे सच में अपने हाथ नहीं धोने हैं",
            "मुझे फिर से अपने हाथ नहीं धोने हैं",
            "मुझे सच में फिर से अपने हाथ नहीं धोने हैं"

    }},{{"मुझे अपने कपड़े निकालने हैं",
            "मुझे सच में अपने कपड़े निकालने हैं",
            "मुझे अपने कपड़े निकालने हैं",
            "मुझे सच में अपने कपड़े निकालने हैं",
            "मुझे फिर से अपने कपड़े निकालने हैं",
            "मुझे सच में फिर से अपने कपड़े निकालने हैं",
            "मुझे अपने कपड़े नहीं निकालने हैं",
            "मुझे सच में अपने कपड़े नहीं निकालने हैं",
            "मुझे अपने कपड़े नहीं निकालने हैं",
            "मुझे सच में अपने कपड़े नहीं निकालने हैं",
            "मुझे फिर से अपने कपड़े नहीं निकालने हैं",
            "मुझे सच में फिर से अपने कपड़े नहीं निकालने हैं"

    },{"मुझे पानी चालू करना हैं",
            "मुझे सच में पानी चालू करना हैं",
            "मुझे पानी चालू करना हैं",
            "मुझे सच में पानी चालू करना हैं",
            "मुझे फिर से पानी चालू करना हैं",
            "मुझे सच में फिर से पानी चालू करना हैं",
            "मुझे पानी चालू नहीं करना हैं",
            "मुझे सच में पानी चालू नहीं करना हैं",
            "मुझे पानी चालू नहीं करना हैं",
            "मुझे सच में पानी चालू नहीं करना हैं",
            "मुझे फिर से पानी चालू नहीं करना हैं",
            "मुझे सच में फिर से पानी चालू नहीं करना हैं"

    },{"मुझे शॉवर लेना हैं",
            "मुझे सच में शॉवर लेना हैं",
            "मुझे शॉवर लेना हैं",
            "मुझे सच में शॉवर लेना हैं",
            "मुझे फिर से शॉवर लेना हैं",
            "मुझे सच में फिर से शॉवर लेना हैं",
            "मुझे शॉवर नहीं लेना हैं",
            "मुझे सच में शॉवर नहीं लेना हैं",
            "मुझे शॉवर नहीं लेना हैं",
            "मुझे सच में शॉवर नहीं लेना हैं",
            "मुझे फिर से शॉवर नहीं लेना हैं",
            "मुझे सच में फिर से शॉवर नहीं लेना हैं"

    },{"मुझे अपने शरीर को भिगोना अच्छा लगता हैं",
            "मुझे सच में अपने शरीर को भिगोना अच्छा लगता हैं",
            "मुझे अपने शरीर को भिगोना हैं",
            "मुझे सच में अपने शरीर को भिगोना हैं",
            "मुझे फिर से अपने शरीर को भिगोना हैं",
            "मुझे सच में फिर से अपने शरीर को भिगोना हैं",
            "मुझे अपने शरीर को भिगोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शरीर को भिगोना अच्छा नहीं लगता हैं",
            "मुझे अपने शरीर को नहीं भिगोना हैं",
            "मुझे सच में अपने शरीर को नहीं भिगोना हैं",
            "मुझे फिर से अपने शरीर को नहीं भिगोना हैं",
            "मुझे सच में फिर से अपने शरीर को नहीं भिगोना हैं"

    },{"मुझे अपने शरीर पर साबुन लगाना अच्छा लगता हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना अच्छा लगता हैं",
            "मुझे अपने शरीर पर साबुन लगाना हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना हैं",
            "मुझे अपने शरीर पर थोड़ा और साबुन लगाना हैं",
            "मुझे सच में अपने शरीर पर थोड़ा और साबुन लगाना हैं",
            "मुझे अपने शरीर पर साबुन लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना अच्छा नहीं लगता हैं",
            "मुझे अपने शरीर पर साबुन नहीं लगाना हैं",
            "मुझे सच में अपने शरीर पर साबुन नहीं लगाना हैं",
            "मुझे अपने शरीर पर और साबुन नहीं लगाना हैं",
            "मुझे सच में अपने शरीर पर और साबुन नहीं लगाना हैं"

    },{"मुझे अपने बालों को शाम्पू लगाना अच्छा लगता हैं",
            "मुझे सच में अपने बालों को शाम्पू लगाना अच्छा लगता हैं",
            "मुझे अपने बालों को शाम्पू लगाना हैं",
            "मुझे सच में अपने बालों को शाम्पू लगाना हैं",
            "मुझे फिर से अपने बालों को शाम्पू लगाना हैं",
            "मुझे सच में फिर से अपने बालों को शाम्पू लगाना हैं",
            "मुझे अपने बालों को शाम्पू लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बालों को शाम्पू लगाना अच्छा नहीं लगता हैं",
            "मुझे अपने बालों को शाम्पू नहीं लगाना हैं",
            "मुझे सच में अपने बालों को शाम्पू नहीं लगाना हैं",
            "मुझे फिर से अपने बालों को शाम्पू नहीं लगाना हैं",
            "मुझे सच में फिर से अपने बालों को शाम्पू नहीं लगाना हैं"

    },{"मुझे फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल नहीं करना हैं"

    },{"मुझे अपने बाल धोना अच्छा लगता हैं",
            "मुझे सच में अपने बाल धोना अच्छा लगता हैं",
            "मुझे अपने बाल धोने हैं",
            "मुझे सच में अपने बाल धोने हैं",
            "मुझे फिर से अपने बाल धोने हैं",
            "मुझे सच में फिर से अपने बाल धोने हैं",
            "मुझे अपने बाल धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बाल धोना अच्छा नहीं लगता हैं",
            "मुझे अपने बाल नहीं धोने हैं",
            "मुझे सच में अपने बाल नहीं धोने हैं",
            "मुझे फिर से अपने बाल नहीं धोने हैं",
            "मुझे सच में फिर से अपने बाल नहीं धोने हैं"

    },{"मुझे अपना शरीर धोना अच्छा लगता हैं",
            "मुझे सच में अपना शरीर धोना अच्छा लगता हैं",
            "मुझे अपना शरीर धोना हैं",
            "मुझे सच में अपना शरीर धोना हैं",
            "मुझे फिर से अपना शरीर धोना हैं",
            "मुझे सच में फिर से अपना शरीर धोना हैं",
            "मुझे अपना शरीर धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना शरीर धोना अच्छा नहीं लगता हैं",
            "मुझे अपना शरीर नहीं धोना हैं",
            "मुझे सच में अपना शरीर नहीं धोना हैं",
            "मुझे फिर से अपना शरीर नहीं धोना हैं",
            "मुझे सच में फिर से अपना शरीर नहीं धोना हैं"

    },{"मुझे पानी बंद करना हैं",
            "मुझे सच में पानी बंद करना हैं",
            "मुझे पानी बंद करना हैं",
            "मुझे सच में पानी बंद करना हैं",
            "मुझे फिर से पानी बंद करना हैं",
            "मुझे सच में फिर से पानी बंद करना हैं",
            "मुझे पानी बंद नहीं करना हैं",
            "मुझे सच में पानी बंद नहीं करना हैं",
            "मुझे पानी बंद नहीं करना हैं",
            "मुझे सच में पानी बंद नहीं करना हैं",
            "मुझे फिर से पानी बंद नहीं करना हैं",
            "मुझे सच में फिर से पानी बंद नहीं करना हैं"

    },{"मुझे अपने बाल सुखाना अच्छा लगता हैं",
            "मुझे सच में अपने बाल सुखाना अच्छा लगता हैं",
            "मुझे अपने बाल सुखाने हैं",
            "मुझे सच में अपने बाल सुखाने हैं",
            "मुझे फिर से अपने बाल सुखाने हैं",
            "मुझे सच में फिर से अपने बाल सुखाने हैं",
            "मुझे अपने बाल सुखाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बाल सुखाना अच्छा नहीं लगता हैं",
            "मुझे अपने बाल नहीं सुखाने हैं",
            "मुझे सच में अपने बाल नहीं सुखाने हैं",
            "मुझे फिर से अपने बाल नहीं सुखाने हैं",
            "मुझे सच में फिर से अपने बाल नहीं सुखाने हैं"

    },{"मुझे अपना चेहरा पोंछना अच्छा लगता हैं",
            "मुझे सच में अपना चेहरा पोंछना अच्छा लगता हैं",
            "मुझे अपना चेहरा पोंछना हैं",
            "मुझे सच में अपना चेहरा पोंछना हैं",
            "मुझे फिर से अपना चेहरा पोंछना हैं",
            "मुझे सच में फिर से अपना चेहरा पोंछना हैं",
            "मुझे अपना चेहरा पोंछना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना चेहरा पोंछना अच्छा नहीं लगता हैं",
            "मुझे अपना चेहरा नहीं पोंछना हैं",
            "मुझे सच में अपना चेहरा नहीं पोंछना हैं",
            "मुझे फिर से अपना चेहरा नहीं पोंछना हैं",
            "मुझे सच में फिर से अपना चेहरा नहीं पोंछना हैं"

    },{"मुझे अपना शरीर पोंछना अच्छा लगता हैं",
            "मुझे सच में अपना शरीर पोंछना अच्छा लगता हैं",
            "मुझे अपना शरीर पोंछना हैं",
            "मुझे सच में अपना शरीर पोंछना हैं",
            "मुझे फिर से अपना शरीर पोंछना हैं",
            "मुझे सच में फिर से अपना शरीर पोंछना हैं",
            "मुझे अपना शरीर पोंछना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना शरीर पोंछना अच्छा नहीं लगता हैं",
            "मुझे अपना शरीर नहीं पोंछना हैं",
            "मुझे सच में अपना शरीर नहीं पोंछना हैं",
            "मुझे फिर से अपना शरीर नहीं पोंछना हैं",
            "मुझे सच में फिर से अपना शरीर नहीं पोंछना हैं"

    },{"मुझे अपने कपड़े पहनने हैं",
            "मुझे सच में अपने कपड़े पहनने हैं",
            "मुझे अपने कपड़े पहनने हैं",
            "मुझे सच में अपने कपड़े पहनने हैं",
            "मुझे अपने कपड़े फिर से पहनने हैं",
            "मुझे सच में अपने कपड़े फिर से पहनने हैं",
            "मुझे अपने कपड़े नहीं पहनने हैं",
            "मुझे सच में अपने कपड़े नहीं पहनने हैं",
            "मुझे अपने कपड़े नहीं पहनने हैं",
            "मुझे सच में अपने कपड़े नहीं पहनने हैं",
            "मुझे अपने कपड़े फिर से नहीं पहनने हैं",
            "मुझे सच में अपने कपड़े फिर से नहीं पहनने हैं"

    }},{{"मुझे सुबह जल्दी उठना अच्छा लगता हैं",
            "मुझे सच में सुबह जल्दी उठना अच्छा लगता हैं",
            "मुझे सुबह जल्दी उठना हैं",
            "मुझे सच में सुबह जल्दी उठना हैं",
            "मुझे फिर से सुबह जल्दी उठना हैं",
            "मुझे सच में फिर से सुबह जल्दी उठना हैं",
            "मुझे सुबह जल्दी उठना अच्छा नहीं लगता हैं",
            "मुझे सच में सुबह जल्दी उठना अच्छा नहीं लगता हैं",
            "मुझे सुबह जल्दी नहीं उठना हैं",
            "मुझे सच में सुबह जल्दी नहीं उठना हैं",
            "मुझे फिर से सुबह जल्दी नहीं उठना हैं",
            "मुझे सच में फिर से सुबह जल्दी नहीं उठना हैं",

    },{"मुझे अपना चेहरा धोना अच्छा लगता हैं",
            "मुझे सच में अपना चेहरा धोना अच्छा लगता हैं",
            "मुझे अपना चेहरा धोना हैं",
            "मुझे सच में अपना चेहरा धोना हैं",
            "मुझे फिर से अपना चेहरा धोना हैं",
            "मुझे सच में फिर से अपना चेहरा धोना हैं",
            "मुझे अपना चेहरा धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना चेहरा धोना अच्छा नहीं लगता हैं",
            "मुझे अपना चेहरा नहीं धोना हैं",
            "मुझे सच में अपना चेहरा नहीं धोना हैं",
            "मुझे फिर से अपना चेहरा नहीं धोना हैं",
            "मुझे सच में फिर से अपना चेहरा नहीं धोना हैं"

    },{"मुझे शौचालय जाना अच्छा लगता हैं",
            "मुझे सच में शौचालय जाना अच्छा लगता हैं",
            "मुझे शौचा लय जाना हैं",
            "मुझे सच में शौचालय जाना हैं",
            "मुझे फिर से शौचालय जाना हैं",
            "मुझे सच में फिर से शौचालय जाना हैं",
            "मुझे शौचालय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में शौचालय जाना अच्छा नहीं लगता हैं",
            "मुझे शौचालय नहीं जाना हैं",
            "मुझे सच में शौचालय नहीं जाना हैं",
            "मुझे फिर से शौचालय नहीं जाना हैं",
            "मुझे सच में फिर से शौचालय नहीं जाना हैं"

    },{"मुझे अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने दांत साफ़ करने हैं",
            "मुझे सच में अपने दांत साफ़ करने हैं",
            "मुझे फिर से अपने दांत साफ़ करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ करने हैं",
            "मुझे अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत साफ़ नहीं करने हैं",
            "मुझे फिर से अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ नहीं करने हैं"

    },{"मुझे अपने कपड़े उतारना अच्छा लगता हैं",
            "मुझे सच में अपने कपड़े उतारना अच्छा लगता हैं",
            "मुझे अपने कपड़े उतारने हैं",
            "मुझे सच में अपने कपड़े उतारने हैं",
            "मुझे फिर से अपने कपड़े उतारने हैं",
            "मुझे सच में फिर से अपने कपड़े उतारने हैं",
            "मुझे अपने कपड़े उतारना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने कपड़े उतारना अच्छा नहीं लगता हैं",
            "मुझे अपने कपड़े नहीं उतारने हैं",
            "मुझे सच में अपने कपड़े नहीं उतारने हैं",
            "मुझे फिर से अपने कपड़े नहीं उतारने हैं",
            "मुझे सच में फिर से अपने कपड़े नहीं उतारने हैं"

    },{"मुझे नहाना अच्छा लगता हैं",
            "मुझे सच में नहाना अच्छा लगता हैं",
            "मुझे नहाना हैं",
            "मुझे सच में नहाना हैं",
            "मुझे फिर से नहाना हैं",
            "मुझे सच में फिर से नहाना हैं",
            "मुझे नहाना अच्छा नहीं लगता हैं",
            "मुझे सच में नहाना अच्छा नहीं लगता हैं",
            "मुझे नहाना नहीं हैं",
            "मुझे सच में नहाना नहीं हैं",
            "मुझे फिर से नहाना नहीं हैं",
            "मुझे सच में फिर से नहाना नहीं हैं"

    },{"मुझे कपड़े पहनने हैं",
            "मुझे सच में कपड़े पहनने हैं",
            "मुझे कपड़े पहनने हैं",
            "मुझे सच में कपड़े पहनने हैं",
            "मुझे फिर से कपड़े पहनने हैं",
            "मुझे सच में फिर से कपड़े पहनने हैं",
            "मुझे कपड़े नहीं पहनने हैं",
            "मुझे सच में कपड़े नहीं पहनने हैं",
            "मुझे कपड़े नहीं पहनने हैं",
            "मुझे सच में कपड़े नहीं पहनने हैं",
            "मुझे फिर से कपड़े नहीं पहनने हैं",
            "मुझे सच में फिर से कपड़े नहीं पहनने हैं"

    },{"मुझे अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे अपने बालों को कंघी करनी हैं",
            "मुझे सच में अपने बालों को कंघी करनी हैं",
            "मुझे फिर से अपने बालों को कंघी करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी करनी हैं",
            "मुझे अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में अपने बालों को कंघी नहीं करनी हैं",
            "मुझे फिर से अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी नहीं करनी हैं"

    },{"मुझे सुबह का नाश्ता करना अच्छा लगता हैं",
            "मुझे सच में सुबह का नाश्ता करना अच्छा लगता हैं",
            "मुझे सुबह का नाश्ता चाहिए",
            "मुझे सच में सुबह का नाश्ता चाहिए",
            "मुझे थोड़ा और नाश्ता चाहिए",
            "मुझे सच में थोड़ा और नाश्ता चाहिए",
            "मुझे सुबह का नाश्ता करना अच्छा नहीं लगता हैं",
            "मुझे सच में सुबह का नाश्ता करना अच्छा नहीं लगता हैं",
            "मुझे सुबह का नाश्ता नहीं चाहिए",
            "मुझे सच में सुबह का नाश्ता नहीं चाहिए",
            "मुझे और नाश्ता नहीं चाहिए",
            "मुझे सच में कुछ भी नाश्ता नहीं चाहिए"

    },{"मुझे अपना खाने का डिब्बा भरना अच्छा लगता हैं",
            "मुझे सच में अपना खाने का डिब्बा भरना अच्छा लगता हैं",
            "मुझे अपने खाने का डिब्बा भरना हैं",
            "मुझे सच में अपने खाने का डिब्बा भरना हैं",
            "मुझे फिर से अपने खाने का डिब्बा भरना हैं",
            "मुझे सच में फिर से अपने खाने का डिब्बा भरना हैं",
            "मुझे अपना खाने का डिब्बा भरना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना खाने का डिब्बा भरना अच्छा नहीं लगता हैं",
            "मुझे अपने खाने का डिब्बा नहीं भरना हैं",
            "मुझे सच में अपने खाने का डिब्बा नहीं भरना हैं",
            "मुझे फिर से अपने खाने का डिब्बा नहीं भरना हैं",
            "मुझे सच में फिर से अपने खाने का डिब्बा नहीं भरना हैं"

    },{"मुझे अपनी पाठशाला की bag भरना अच्छा लगता हैं",
            "मुझे सच में अपनी पाठशाला की bag भरना अच्छा लगता हैं",
            "मुझे अपनी पाठशाला की bag भरनी हैं",
            "मुझे सच में अपनी पाठशाला की bag भरनी हैं",
            "मुझे फिर से अपनी पाठशाला की bag भरनी हैं",
            "मुझे सच में फिर से अपनी पाठशाला की bag भरनी हैं",
            "मुझे अपनी पाठशाला की bag भरना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठशाला की bag भरना अच्छा नहीं लगता हैं",
            "मुझे अपनी पाठशाला की bag नहीं भरनी हैं",
            "मुझे सच में अपनी पाठशाला की bag नहीं भरनी हैं",
            "मुझे फिर से अपनी पाठशाला की bag नहीं भरनी हैं",
            "मुझे सच में फिर से अपनी पाठशाला की bag नहीं भरनी हैं"

    },{"मुझे पाठशाला जाना अच्छा लगता हैं",
            "मुझे सच में पाठशाला जाना अच्छा लगता हैं",
            "मुझे पाठशाला जाना हैं",
            "मुझे सच में पाठशाला जाना हैं",
            "मुझे फिर से पाठशाला जाना हैं",
            "मुझे सच में फिर से पाठशाला जाना हैं",
            "मुझे पाठशाला जाना अच्छा नहीं लगता हैं",
            "मुझे सच में पाठशाला जाना अच्छा नहीं लगता हैं",
            "मुझे पाठशाला नहीं जाना हैं",
            "मुझे सच में पाठशाला नहीं जाना हैं",
            "मुझे फिर से पाठशाला नहीं जाना हैं",
            "मुझे सच में फिर से पाठशाला नहीं जाना हैं"

    }},{{"मुझे रात का भोजन करना अच्छा लगता हैं",
            "मुझे सच में रात का भोजन करना अच्छा लगता हैं",
            "मुझे रात का भोजन चाहिए",
            "मुझे सच में रात का भोजन चाहिए",
            "मुझे और भोजन चाहिए",
            "मुझे सच में और भोजन चाहिए",
            "मुझे रात का भोजन करना अच्छा नहीं लगता हैं",
            "मुझे सच में रात का भोजन करना अच्छा नहीं लगता हैं",
            "मुझे रात  का भोजन नहीं चाहिए",
            "मुझे सच में रात  का भोजन नहीं चाहिए",
            "मुझे और भोजन नहीं चाहिए",
            "मुझे सच में और भोजन नहीं चाहिए"

    },{"मुझे रात के कपड़े पैहनना अच्छा लगता हैं",
            "मुझे सच में रात के कपड़े पैहनना अच्छा लगता हैं",
            "मुझे रात के कपड़े पहनने हैं",
            "मुझे सच में रात के कपड़े पहनने हैं",
            "मुझे फिर से रात के कपड़े पहनने हैं",
            "मुझे सच में फिर से रात के कपड़े पहनने हैं",
            "मुझे रात के कपड़े पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में रात के कपड़े पैहनना अच्छा नहीं लगता हैं",
            "मुझे रात के कपड़े नहीं पहनने हैं",
            "मुझे सच में रात के कपड़े नहीं पहनने हैं",
            "मुझे फिर से रात के कपड़े नहीं पहनने हैं",
            "मुझे सच में फिर से रात के कपड़े नहीं पहनने हैं"

    },{"मुझे अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने दांत साफ़ करने हैं",
            "मुझे सच में अपने दांत साफ़ करने हैं",
            "मुझे फिर से अपने दांत साफ़ करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ करने हैं",
            "मुझे अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत साफ़ नहीं करने हैं",
            "मुझे फिर से अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ नहीं करने हैं"

    },{"मुझे कहानियाँ पढ़ना अच्छा लगता हैं",
            "मुझे सच में कहानियाँ पढ़ना अच्छा लगता हैं",
            "मुझे एक कहानी पढ़नी हैं",
            "मुझे सच में एक कहानी पढ़नी हैं",
            "मुझे और एक कहानी पढ़नी हैं",
            "मुझे सच में और एक कहानी पढ़नी हैं",
            "मुझे कहानियाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में कहानियाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे कहानी नहीं पढ़नी हैं",
            "मुझे सच में कहानी नहीं पढ़नी हैं",
            "मुझे और एक कहानी नहीं पढ़नी हैं",
            "मुझे सच में और एक कहानी नहीं पढ़नी हैं"

    },{"मुझे सबको शुभ रात्रि बोलना अच्छा लगता हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना अच्छा लगता हैं",
            "मुझे सबको शुभ रात्रि बोलना हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना हैं",
            "मुझे फिर से सबको शुभ रात्रि बोलना हैं",
            "मुझे सच में फिर से सबको शुभ रात्रि बोलना हैं",
            "मुझे सबको शुभ रात्रि बोलना अच्छा नहीं लगता हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना अच्छा नहीं लगता हैं",
            "मुझे सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे सच में सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे फिर से सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे सच में फिर से सबको शुभ रात्रि नहीं बोलना हैं"

    },{"मुझे प्रार्थना करना अच्छा लगता हैं",
            "मुझे सच में प्रार्थना करना अच्छा लगता हैं",
            "मुझे प्रार्थना करनी हैं",
            "मुझे सच में प्रार्थना करनी हैं",
            "मुझे फिर से प्रार्थना करनी हैं",
            "मुझे सच में फिर से प्रार्थना करनी हैं",
            "मुझे प्रार्थना करना अच्छा नहीं लगता हैं",
            "मुझे सच में प्रार्थना करना अच्छा नहीं लगता हैं",
            "मुझे प्रार्थना नहीं करनी हैं",
            "मुझे सच में प्रार्थना नहीं करनी हैं",
            "मुझे फिर से प्रार्थना नहीं करनी हैं",
            "मुझे सच में फिर से प्रार्थना नहीं करनी हैं",
    }}};
}
