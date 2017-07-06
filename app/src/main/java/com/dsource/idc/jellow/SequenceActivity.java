package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellow.Models.SeqActivityVerbiageModel;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by ekalpa on 6/22/2016.
 */
public class SequenceActivity extends AppCompatActivity {
    private final boolean DISABLE_ACTION_BTNS = true;
    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    private int image_flag = -1, flag_keyboard = 0, mLevelTwoItemPos,
            sr, bw, count = 0;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton;
    private EditText et;
    private KeyListener originalKeyListener;
    private int[] mColor;
    private TextView tt1, bt1, bt2, bt3;
    private CircularImageView image1, image2, image3;
    private ImageView arrow1, arrow2, back;
    private LinearLayout linear;
    private Button forward, backward;

    TypedArray mDailyActivitiesIcons;
    private String strNext, strPrevious;
    private String[] mDailyActivitiesSpeechText, mDailyActivitiesBelowText, heading, side, below, bt;
    private ArrayList<ArrayList<ArrayList<String>>> mSeqActSpeech;
    private SessionManager mSession;
    private UserDataMeasure mUserDataMeasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        Typeface fontMuktaRegular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Regular.ttf");
        Typeface fontMuktaBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Mukta-Bold.ttf");
        mSession = new SessionManager(this);
        mUserDataMeasure = new UserDataMeasure(this);
        mUserDataMeasure.recordScreen(getLocalClassName());
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        if(mLevelTwoItemPos == 7)   mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)  mLevelTwoItemPos = 4;
        {
            String strSrBw = mSession.getShadowRadiusAndBorderWidth();
            sr = Integer.valueOf(strSrBw.split(",")[0]);
            bw = Integer.valueOf(strSrBw.split(",")[1]);
        }
        loadArraysFromResources();
        initializeViews();
        forward.setText(bt[1]);
        backward.setText(bt[0]);
        backward.setEnabled(false);
        backward.setAlpha(.5f);
        et.setVisibility(View.INVISIBLE);

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
        image1.setImageDrawable(mDailyActivitiesIcons.getDrawable(0));
        image2.setImageDrawable(mDailyActivitiesIcons.getDrawable(1));
        image3.setImageDrawable(mDailyActivitiesIcons.getDrawable(2));

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
                if (mDailyActivitiesIcons.length() == count + 3) {
                    forward.setAlpha(.5f);
                    forward.setEnabled(false);
                }
                if (mDailyActivitiesIcons.length() < count + 3) {
                    if (mLevelTwoItemPos == 0) {
                        image1.setImageDrawable(mDailyActivitiesIcons.getDrawable(count));
                        image2.setImageDrawable(mDailyActivitiesIcons.getDrawable(count+1));
                        arrow2.setVisibility(View.INVISIBLE);
                        image3.setVisibility(View.INVISIBLE);
                        bt3.setVisibility(View.INVISIBLE);
                        bt1.setText(mDailyActivitiesBelowText[count]);
                        bt2.setText(mDailyActivitiesBelowText[count + 1]);
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 4 || mLevelTwoItemPos == 3) {
                        image1.setImageDrawable(mDailyActivitiesIcons.getDrawable(count));
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
                    image1.setImageDrawable(mDailyActivitiesIcons.getDrawable(count));
                    image2.setImageDrawable(mDailyActivitiesIcons.getDrawable(count+1));
                    image3.setImageDrawable(mDailyActivitiesIcons.getDrawable(count+2));
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
                bt3.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.VISIBLE);
                bt1.setVisibility(View.VISIBLE);
                bt1.setText(mDailyActivitiesBelowText[count]);
                bt2.setText(mDailyActivitiesBelowText[count + 1]);
                bt3.setText(mDailyActivitiesBelowText[count + 2]);
                image1.setImageDrawable(mDailyActivitiesIcons.getDrawable(count));
                image2.setImageDrawable(mDailyActivitiesIcons.getDrawable(count+1));
                image3.setImageDrawable(mDailyActivitiesIcons.getDrawable(count+2));

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
                    hideActionBtn(true);
                    image1.setBorderColor(-1);
                    image1.setShadowColor(0);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", firstIcon: "+ mDailyActivitiesSpeechText[count], Log.INFO);
                    image_flag = 1;
                    if (count + image_flag == mDailyActivitiesIcons.length())
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count]);
                    resetActionBtnImageIcons();
                    image1.setBorderColor(-1283893945);
                    image1.setShadowColor(-1283893945);
                    image1.setShadowRadius(sr);
                    image1.setBorderWidth(bw);
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_flag == 2) {
                    hideActionBtn(true);
                    image2.setBorderColor(-1);
                    image2.setShadowColor(0);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", secondIcon: "+ mDailyActivitiesSpeechText[count + 1], Log.INFO);
                    image_flag = 2;
                    if (count + image_flag == mDailyActivitiesIcons.length())
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count + 1]);
                    resetActionBtnImageIcons();
                    image2.setBorderColor(-1283893945);
                    image2.setShadowColor(-1283893945);
                    image2.setShadowRadius(sr);
                    image2.setBorderWidth(bw);
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_flag == 3) {
                    hideActionBtn(true);
                    image3.setBorderColor(-1);
                    image3.setShadowColor(0);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(0);
                    image_flag = 0;
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", thirdIcon: "+ mDailyActivitiesSpeechText[count + 2], Log.INFO);
                    image_flag = 3;
                    if (count + image_flag == mDailyActivitiesIcons.length())
                        hideActionBtn(true);
                    else
                        hideActionBtn(false);
                    speakSpeech(mDailyActivitiesSpeechText[count + 2]);
                    resetActionBtnImageIcons();
                    image3.setBorderColor(-1283893945);
                    image3.setShadowColor(-1283893945);
                    image3.setShadowRadius(sr);
                    image3.setBorderWidth(bw);
                }
            }
        });

        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(below[1]);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    linear.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    image_flag = flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);
                } else {
                    back.setImageResource(R.drawable.backpressed);
                    finish();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setImageResource(R.drawable.homepressed);
                keyboard.setImageResource(R.drawable.keyboard_button);
                speakSpeech(below[0]);
                Intent intent = new Intent(SequenceActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(below[2]);
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
                    speakSpeech(et.getText().toString());
                    mUserDataMeasure.reportLog(getLocalClassName()+", TtsSpeak: ", Log.INFO);
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
                    } else {
                        speakSpeech(side[0]);
                        mCk = 1;
                    }
                } else {
                    setBorderToIcon(0);
                    if (mCk == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", like: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(1), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(1));
                        mCk = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", like: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(0), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(0));
                        mCk = 1;
                    }
                }
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
                    } else {
                        speakSpeech(side[2]);
                        mCy = 1;
                    }
                } else {
                    setBorderToIcon(2);
                    if (mCy == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", yes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(3), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(3));
                        mCy = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", yes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(2), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(2));
                        mCy = 1;
                    }
                }
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
                    } else {
                        speakSpeech(side[4]);
                        mCm = 1;
                    }
                } else {
                    setBorderToIcon(4);
                    if (mCm == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", add: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(5), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(5));
                        mCm = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", add: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(4), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(4));
                        mCm = 1;
                    }
                }
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
                    } else {
                        speakSpeech(side[6]);
                        mCd = 1;
                    }
                } else {
                    setBorderToIcon(1);
                    if (mCd == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", dislike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(7), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(7));
                        mCd = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", dislike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(6), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(6));
                        mCd = 1;
                    }
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = mCy = mCm = mCd = mCl = 0;
                if (image_flag == 0) {
                    if (mCn == 1) {
                        speakSpeech(side[9]);
                        mCn = 0;
                    } else {
                        speakSpeech(side[8]);
                        mCn = 1;
                    }
                } else {
                    setBorderToIcon(3);
                    if (mCn == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", no: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(9), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(9));
                        mCn = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", no: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(8), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(8));
                        mCn = 1;
                    }
                }
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
                    } else {
                        speakSpeech(side[10]);
                        mCl = 1;
                    }
                } else {
                    setBorderToIcon(5);
                    if (mCl == 1) {
                        mUserDataMeasure.reportLog(getLocalClassName()+", minus: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(11), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(11));
                        mCl = 0;
                    } else {
                        mUserDataMeasure.reportLog(getLocalClassName()+", minus: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(10), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + image_flag - 1).get(10));
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
            case R.id.profile: startActivity(new Intent(this, ProfileForm.class)); break;
            case R.id.info: startActivity(new Intent(this, AboutJellow.class)); break;
            case R.id.usage: startActivity(new Intent(this, Tutorial.class)); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInput.class)); break;
            case R.id.feedback: startActivity(new Intent(this, Feedback.class)); break;
            case R.id.settings: startActivity(new Intent(this, Setting.class)); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferences.class)); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initializeViews() {
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
        image1 = (CircularImageView) findViewById(R.id.image1);
        image2 = (CircularImageView) findViewById(R.id.image2);
        image3 = (CircularImageView) findViewById(R.id.image3);

        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow2 = (ImageView) findViewById(R.id.arrow2);
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void loadArraysFromResources() {
        mColor = getResources().getIntArray(R.array.arrActionBtnColors);
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
                mDailyActivitiesIcons = getResources().obtainTypedArray(R.array.arrSeqActivityBrushingIcon);
                break;
            case 1:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityToiletBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityToiletSpeechText);
                mDailyActivitiesIcons = getResources().obtainTypedArray(R.array.arrSeqActivityToiletIcon);
                break;
            case 2:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityBathingBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityBathingSpeechText);
                mDailyActivitiesIcons = getResources().obtainTypedArray(R.array.arrSeqActivityBathingIcon);
                break;
            case 3:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityMorningRoutineBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityMorningRoutineSpeechText);
                mDailyActivitiesIcons = getResources().obtainTypedArray(R.array.arrSeqActivityMorningRoutineIcon);
                break;
            case 4:
                mDailyActivitiesBelowText = getResources().getStringArray(R.array.arrSeqActivityBedtimeRoutineBelowText);
                mDailyActivitiesSpeechText = getResources().getStringArray(R.array.arrSeqActivityBedtimeRoutineSpeechText);
                mDailyActivitiesIcons = getResources().obtainTypedArray(R.array.arrSeqActivityBedtimeRoutineIcon);
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
        minus.setImageResource(R.drawable.lesswithout);
    }

    private void setBorderToIcon(int actionBtnIdx) {
        if (image_flag == 1) {
            image1.setBorderColor(mColor[actionBtnIdx]);
            image1.setShadowColor(mColor[actionBtnIdx]);
            image1.setShadowRadius(sr);
            image1.setBorderWidth(bw);
        } else if (image_flag == 2) {
            image2.setBorderColor(mColor[actionBtnIdx]);
            image2.setShadowColor(mColor[actionBtnIdx]);
            image2.setShadowRadius(sr);
            image2.setBorderWidth(bw);
        } else if (image_flag == 3) {
            image3.setBorderColor(mColor[actionBtnIdx]);
            image3.setShadowColor(mColor[actionBtnIdx]);
            image3.setShadowRadius(sr);
            image3.setBorderWidth(bw);
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
}