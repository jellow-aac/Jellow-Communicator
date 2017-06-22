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

import com.dsource.idc.jellow.Models.SequenceActivitySpeech;
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
            layer_1_speech = new SequenceActivitySpeech().getSequenceActivitySpeechEnglish();
            side = side_english;
            below = below_english;
            bt = bt_english;
            strNext = "NEXT";   strPrevious = "PREVIOUS";
        }else{
            daily_activities_text = daily_activities_text_hindi[mLevelTwoItemPos];
            daily_activities_text1 = daily_activities_text_hindi1[mLevelTwoItemPos];
            heading = heading_hindi;
            layer_1_speech = new SequenceActivitySpeech().getSequenceActivitySpeechHindi();
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
}