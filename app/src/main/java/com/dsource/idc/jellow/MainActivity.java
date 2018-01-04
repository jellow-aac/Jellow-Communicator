package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellow.Models.LevelOneVerbiageModel;
import com.dsource.idc.jellow.Utility.Analytics;
import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.dsource.idc.jellow.Utility.Analytics.bundleEvent;
import static com.dsource.idc.jellow.Utility.Analytics.reportLog;
import static com.dsource.idc.jellow.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellow.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellow.Utility.Analytics.stopMeasuring;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_HOME = 0;
    private final boolean DISABLE_ACTION_BTNS = true;

    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    private int image_flag = -1, flag_keyboard = 0;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    private EditText et;
    private TextView actionBarTitle;
    private KeyListener originalKeyListener;
    public RecyclerView mRecyclerView;
    private LinearLayout mMenuItemLinearLayout;
    private int mLevelOneItemPos = -1, mSelectedItemAdapterPos = -1, mActionBtnClickCount = -1;
    private boolean mShouldReadFullSpeech = false;
    private ArrayList<View> mRecyclerItemsViewList;
    private Analytics mAnalytics;
    private ArrayList<ArrayList<String>> mLayerOneSpeech;
    private String[] myMusic, side, below;
    private String actionBarTitleTxt;
    //private SpeakOnKeyboardDialog mKeyboardDialog;
    private boolean mKeyChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable( getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getString(R.string.action_bar_title));

        new ChangeAppLocale(this).setLocale();
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();
        mRecyclerItemsViewList = new ArrayList<>(myMusic.length);
        while (mRecyclerItemsViewList.size() < myMusic.length)  mRecyclerItemsViewList.add(null);
        like = findViewById(R.id.ivlike);
        like.setContentDescription(side[0]);
        dislike = findViewById(R.id.ivdislike);
        dislike.setContentDescription(side[6]);
        add = findViewById(R.id.ivadd);
        add.setContentDescription(side[4]);
        minus = findViewById(R.id.ivminus);
        minus.setContentDescription(side[10]);
        yes = findViewById(R.id.ivyes);
        yes.setContentDescription(side[2]);
        no = findViewById(R.id.ivno);
        no.setContentDescription(side[8]);
        home = findViewById(R.id.ivhome);
        home.setContentDescription(below[0]);
        back = findViewById(R.id.ivback);
        back.setContentDescription(below[1]);
        back.setAlpha(.5f);
        back.setEnabled(false);
        keyboard = findViewById(R.id.keyboard);
        keyboard.setContentDescription(below[2]);
        et = findViewById(R.id.et);
        et.setContentDescription(getString(R.string.string_to_speak));
        et.setVisibility(View.INVISIBLE);

        ttsButton = findViewById(R.id.ttsbutton);
        ttsButton.setContentDescription(getString(R.string.speak_written_text));
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(new MainActivityAdapter(this));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.requestFocus();
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                mMenuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
                mMenuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tappedGridItemEvent(view, v, position);
                    }
                });
            }
            @Override   public void onLongClick(View view, int position) {}
        }));

        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), view);
                if(mRecyclerItemsViewList.contains(view) && mSelectedItemAdapterPos > -1 && mRecyclerView.getChildLayoutPosition(view) == mSelectedItemAdapterPos)
                    setMenuImageBorder(view, true);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                setMenuImageBorder(view, false);
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), null);
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                if (flag_keyboard == 1){
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    home.setImageResource(R.drawable.home);
                    ttsButton.setImageResource(R.drawable.speaker_button);
                    speakSpeech(below[1]);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    back.setEnabled(false);
                    back.setAlpha(.5f);
                    showActionBarTitle(true);
                    //mKeyboardDialog.dismissDialog();
                    singleEvent("Navigation","Back");
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHome(true);
                singleEvent("Navigation","Home");
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(below[2]);
                ttsButton.setImageResource(R.drawable.speaker_button);
                if (flag_keyboard  == 1){
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    back.setAlpha(.5f);
                    back.setEnabled(false);
                    showActionBarTitle(true);
                }else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.back_button);
                    home.setImageResource(R.drawable.home);
                    et.setVisibility(View.VISIBLE);

                    et.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheActionButtons(DISABLE_ACTION_BTNS);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    //mKeyboardDialog = new SpeakOnKeyboardDialog(MainActivity.this);
                    //mKeyboardDialog.showDialog();
                    back.setAlpha(1f);
                    back.setEnabled(true);
                    flag_keyboard = 1;
                    showActionBarTitle(false);
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                speakSpeech(et.getText().toString());
                if(!et.getText().toString().equals("")) ttsButton.setImageResource(R.drawable.speaker_pressed);
                reportLog(getLocalClassName()+", TtsSpeak", Log.INFO);
                singleEvent("Keyboard",et.getText().toString());
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
                image_flag = 0;
                resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCk == 1) {
                        speakSpeech(side[1]);
                        mCk = 0;
                        singleEvent("ExpressiveIcon","ReallyLike");
                    } else {
                        speakSpeech(side[0]);
                        mCk = 1;
                        singleEvent("ExpressiveIcon","Like");
                    }
                } else {
                    reportLog(getLocalClassName()+", like: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null){
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    }
                    if (mCk == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(1));
                        mCk = 0;
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(1));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(0));
                        mCk = 1;
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(0));
                    }
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
                        speakSpeech(side[7]);
                        mCd = 0;
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                    } else {
                        speakSpeech(side[6]);
                        mCd = 1;
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                } else {
                    reportLog(getLocalClassName()+", dislike: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCd == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(7));
                        mCd = 0;
                        singleEvent("ExpressiveIcon","ReallyLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(7));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(6));
                        mCd = 1;
                        singleEvent("ExpressiveIcon","Don'tLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(6));
                    }
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
                        speakSpeech(side[3]);
                        mCy = 0;
                        singleEvent("ExpressiveIcon","ReallyYes");
                    } else {
                        speakSpeech(side[2]);
                        mCy = 1;
                        singleEvent("ExpressiveIcon","Yes");
                    }
                } else {
                    reportLog(getLocalClassName()+", yes: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(3));
                        mCy = 0;
                        singleEvent("ExpressiveIcon","ReallyYes");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(3));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(2));
                        mCy = 1;
                        singleEvent("ExpressiveIcon","Yes");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(2));
                    }
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
                        speakSpeech(side[9]);
                        mCn = 0;
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        speakSpeech(side[8]);
                        mCn = 1;
                        singleEvent("ExpressiveIcon","No");
                    }
                } else {
                    reportLog(getLocalClassName()+", no: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCn == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(9));
                        mCn = 0;
                        singleEvent("ExpressiveIcon","ReallyNo");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(9));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(8));
                        mCn = 1;
                        singleEvent("ExpressiveIcon","No");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(8));
                    }
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
                        speakSpeech(side[5]);
                        mCm = 0;
                        singleEvent("ExpressiveIcon","ReallyMore");
                    } else {
                        speakSpeech(side[4]);
                        mCm = 1;
                        singleEvent("ExpressiveIcon","More");
                    }
                } else {
                    reportLog(getLocalClassName()+", add: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCm == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(5));
                        mCm = 0;
                        singleEvent("ExpressiveIcon","ReallyMore");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(5));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(4));
                        mCm = 1;
                        singleEvent("ExpressiveIcon","More");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(4));
                    }
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
                        speakSpeech(side[11]);
                        mCl = 0;
                        singleEvent("ExpressiveIcon","ReallyLess");
                    } else {
                        speakSpeech(side[10]);
                        mCl = 1;
                        singleEvent("ExpressiveIcon","Less");
                    }
                } else {
                    reportLog(getLocalClassName()+", minus: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCl == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(11));
                        mCl = 0;
                        singleEvent("ExpressiveIcon","ReallyLess");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(11));
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(10));
                        mCl = 1;
                        singleEvent("ExpressiveIcon","Less");

                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.get(mLevelOneItemPos).get(10));
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("com.dsource.idc.jellow.STOP_SERVICE"));
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); break;
            case R.id.settings: startActivity(new Intent(getApplication(), SettingActivity.class)); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_HOME && resultCode == RESULT_CANCELED)
            gotoHome(false);
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
        stopMeasuring("LevelOneActivity");
        new ChangeAppLocale(this).setLocale();
    }

    public void tappedGridItemEvent(final View view, View v, int position) {
        mCk = mCy = mCm = mCd = mCn = mCl = 0;
        resetActionButtons(-1);
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        setMenuImageBorder(v, true);
        mShouldReadFullSpeech = true;
        String title = getActionBarTitle(position);
        getSupportActionBar().setTitle(title);
        if (mLevelOneItemPos == position) {
            Bundle bundle = new Bundle();
            bundle.putString("Icon",myMusic[position]);
            bundle.putString("Level","LevelOne");
            bundleEvent("Grid",bundle);
            Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
            intent.putExtra("mLevelOneItemPos", position);
            intent.putExtra("selectedMenuItemPath", title + "/");
            startActivityForResult(intent,REQ_HOME);
        }else {
            speakSpeech(myMusic[position]);
        }
        mLevelOneItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        reportLog(getLocalClassName()+" "+mLevelOneItemPos, Log.INFO);
    }

    private void gotoHome(boolean isHomePressed) {
        getSupportActionBar().setTitle(getString(R.string.action_bar_title));
        mCk = mCy = mCm = mCd = mCn = mCl = 0;
        like.setImageResource(R.drawable.ilikewithoutoutline);
        dislike.setImageResource(R.drawable.idontlikewithout);
        yes.setImageResource(R.drawable.iwantwithout);
        no.setImageResource(R.drawable.idontwantwithout);
        add.setImageResource(R.drawable.morewithout);
        minus.setImageResource(R.drawable.lesswithout);
        resetRecyclerMenuItemsAndFlags(6);
        mShouldReadFullSpeech = false;
        image_flag = -1;
        if (flag_keyboard  == 1){
            keyboard.setImageResource(R.drawable.keyboard_button);
            back.setImageResource(R.drawable.back_button);
            et.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ttsButton.setVisibility(View.INVISIBLE);
            flag_keyboard = 0;
            changeTheActionButtons(!DISABLE_ACTION_BTNS);
            back.setAlpha(.5f);
            back.setEnabled(false);
        }
        if(isHomePressed) {
            speakSpeech(below[0]);
            singleEvent("Navigation","Home");
            home.setImageResource(R.drawable.homepressed);
        }else
            home.setImageResource(R.drawable.home);
    }

    private void showActionBarTitle(boolean showTitle){
        if (showTitle)
            getSupportActionBar().setTitle(actionBarTitleTxt);
        else{
            actionBarTitleTxt = getSupportActionBar().getTitle().toString();
            getSupportActionBar().setTitle("");
        }
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private String getActionBarTitle(int position) {
            String[] tempTextArr = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        return tempTextArr[position];
    }

    private void loadArraysFromResources() {
        LevelOneVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.levelOneVerbiage), LevelOneVerbiageModel.class);
        mLayerOneSpeech = verbiageModel.getVerbiageModel();
        //To log custom events.

        reportLog("Activity created", Log.INFO);
        myMusic = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        side = getResources().getStringArray(R.array.arrActionSpeech);
        below = getResources().getStringArray(R.array.arrNavigationSpeech);
    }

    private void resetRecyclerMenuItemsAndFlags(int setPressedIcon) {
        resetActionButtons(setPressedIcon);
        mLevelOneItemPos = -1;
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
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

    private void setMenuImageBorder(View recyclerChildView, boolean setBorder) {
        View borderView = recyclerChildView.findViewById(R.id.borderView);
        if(new SessionManager(this).getGridSize() == 0) {
            if (setBorder) {
                if (mActionBtnClickCount > 0) {
                    switch (image_flag) {
                        case 0:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_yellow_1by3));
                            break;
                        case 1:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_blue_1by3));
                            break;
                        case 2:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_green_1by3));
                            break;
                        case 3:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_red_1by3));
                            break;
                        case 4:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_light_1by3));
                            break;
                        case 5:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_dark_1by3));
                            break;
                    }
                } else
                    borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_brown_1by3));
            } else
                borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_1by3));
        }else{
            if (setBorder) {
                if (mActionBtnClickCount > 0) {
                    switch (image_flag) {
                        case 0:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_yellow_3by3));
                            break;
                        case 1:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_blue_3by3));
                            break;
                        case 2:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_green_3by3));
                            break;
                        case 3:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_red_3by3));
                            break;
                        case 4:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_light_3by3));
                            break;
                        case 5:
                            borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_grey_dark_3by3));
                            break;
                    }
                } else
                    borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_brown_3by3));
            } else
                borderView.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_drawable_nocolor_3by3));
        }
        borderView.invalidate();
    }

    private void resetActionButtons(int image_flag) {
        like.setImageResource(R.drawable.ilikewithoutoutline);
        dislike.setImageResource(R.drawable.idontlikewithout);
        yes.setImageResource(R.drawable.iwantwithout);
        no.setImageResource(R.drawable.idontwantwithout);
        add.setImageResource(R.drawable.morewithout);
        minus.setImageResource(R.drawable.lesswithout);
        home.setImageResource(R.drawable.home);
        switch (image_flag){
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

    private void resetRecyclerAllItems() {
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
        }
    }
}