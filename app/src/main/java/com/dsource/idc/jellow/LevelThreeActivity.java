package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
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

import com.dsource.idc.jellow.Models.LevelThreeVerbiageModel;
import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellow.Utility.IndexSorter;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class LevelThreeActivity extends AppCompatActivity {
    private final boolean DISABLE_ACTION_BTNS = true;
    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;

    private int image_flag = -1, flag_keyboard = 0, mActionBtnClickCount, more_count = 0;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    private EditText et;
    private KeyListener originalKeyListener;
    private RecyclerView mRecyclerView;
    private LinearLayout mMenuItemLinearLayout;
    private String[] myMusic;
    private int[] mColor;
    private int mLevelOneItemPos, mLevelTwoItemPos, mLevelThreeItemPos = -1, mSelectedItemAdapterPos = -1;
    private boolean mShouldReadFullSpeech = false;
    private ArrayList<View> mRecyclerItemsViewList;
    private Integer[] count = new Integer[100];
    private int[] sort = new int[100];
    private int count_flag = 0;
    private DataBaseHelper myDbHelper;
    private String[] side, below;
    private ArrayList<ArrayList<ArrayList <ArrayList <String>>>> mVerbTxt;
    private UserDataMeasure mUserDataMeasure;
    private String actionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        myDbHelper = new DataBaseHelper(this);
        mUserDataMeasure = new UserDataMeasure(this);
        mUserDataMeasure.recordScreen(getLocalClassName());
        new ChangeAppLocale(this).setLocale();
        more_count = 0;
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();

        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            mUserDataMeasure.reportException(e);
        }

        mRecyclerItemsViewList = new ArrayList<>(100);
        while(mRecyclerItemsViewList.size() <= 100) mRecyclerItemsViewList.add(null);
        like = (ImageView) findViewById(R.id.ivlike);
        like.setContentDescription(side[0]);
        dislike = (ImageView) findViewById(R.id.ivdislike);
        dislike.setContentDescription(side[6]);
        add = (ImageView) findViewById(R.id.ivadd);
        add.setContentDescription(side[4]);
        minus = (ImageView) findViewById(R.id.ivminus);
        minus.setContentDescription(side[10]);
        yes = (ImageView) findViewById(R.id.ivyes);
        yes.setContentDescription(side[2]);
        no = (ImageView) findViewById(R.id.ivno);
        no.setContentDescription(side[8]);
        home = (ImageView) findViewById(R.id.ivhome);
        home.setContentDescription(below[0]);
        back = (ImageView) findViewById(R.id.ivback);
        back.setContentDescription(below[1]);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        keyboard.setContentDescription(below[2]);
        et = (EditText) findViewById(R.id.et);
        et.setContentDescription(getString(R.string.string_to_speak));
        et.setVisibility(View.INVISIBLE);
        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setContentDescription(getString(R.string.speak_written_text));
        ttsButton.setVisibility(View.INVISIBLE);
        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make to the field non-editable
        et.setKeyListener(null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(final View view, final int position) {
                    mMenuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
                    mMenuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tappedGridItemEvent(view, v, position);
                        }
                    });
                }
                @Override public void onLongClick(View view, int position) {}
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

        String savedString = myDbHelper.getlevel(mLevelOneItemPos, mLevelTwoItemPos);
        if (!savedString.equals("false")) {
            count_flag = 1;
            StringTokenizer st = new StringTokenizer(savedString, ",");
            count = new Integer[mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).size()];
            for (int j = 0; j < mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).size(); j++) {
                count[j] = Integer.parseInt(st.nextToken());
            }
            IndexSorter<Integer> is = new IndexSorter<Integer>(count);
            is.sort();
            Integer[] indexes = new Integer[mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).size()];
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                g++;
            }
            for (int j = 0; j < count.length; j++)
                sort[j] = indexes[j];
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
            mRecyclerView.setAdapter(new LayerThreeAdapter(this, mLevelOneItemPos, mLevelTwoItemPos, sort));
        } else if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9)) {
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
            mRecyclerView.setAdapter(new LayerThreeAdapter(this, mLevelOneItemPos, mLevelTwoItemPos, sort));
        } else {
            count_flag = 0;
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
        }

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(below[1]);
                ttsButton.setImageResource(R.drawable.speaker_button);
                back.setImageResource(R.drawable.backpressed);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    if(mLevelThreeItemPos != -1) retainExpressiveButtonStates();
                    else changeTheActionButtons(!DISABLE_ACTION_BTNS);
                } else if (more_count > 0) {
                    more_count -= 1;
                    myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
                    mRecyclerView.setAdapter(new LayerThreeAdapter(LevelThreeActivity.this, mLevelOneItemPos, mLevelTwoItemPos, sort));
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
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    if(mLevelThreeItemPos != -1) retainExpressiveButtonStates();
                    else changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    showActionBarTitle(true);
                } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    et.setVisibility(View.VISIBLE);
                    et.setKeyListener(originalKeyListener);
                    // Focus to the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheActionButtons(DISABLE_ACTION_BTNS);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
                    mUserDataMeasure.reportLog(getLocalClassName()+", TtsSpeak", Log.INFO);
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
                    // Hithe soft keyboard.
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyLike"));
                    } else {
                        speakSpeech(side[0]);
                        mCk = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("like"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", like: "+
                        mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCk == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(1));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(1));
                        mCk = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyLikeVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(0));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(0));
                        mCk = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("LikeVerbiage"));
                    }
                }
                back.setImageResource(R.drawable.back_button);
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyDislike"));
                    } else {
                        speakSpeech(side[6]);
                        mCd = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("Dislike"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", dislike: "+
                            mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCd == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(7));
                         else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(7));
                        mCd = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyDislikeVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(6));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(6));
                        mCd = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("DislikeVerbiage"));
                    }
                }
                back.setImageResource(R.drawable.back_button);
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyYes"));
                    } else {
                        speakSpeech(side[2]);
                        mCy = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("Yes"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", yes: "+
                            mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(3));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(3));
                        mCy = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyYesVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(2));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(2));
                        mCy = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("YesVerbiage"));
                    }
                }
                back.setImageResource(R.drawable.back_button);
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyNo"));
                    } else {
                        speakSpeech(side[8]);
                        mCn = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("No"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", no: "+
                            mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCn == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(9));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(9));
                        mCn = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyNoVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(8));
                         else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(8));
                        mCn = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("NoVerbiage"));
                    }
                }
                back.setImageResource(R.drawable.back_button);
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyAdd"));
                    } else {
                        speakSpeech(side[4]);
                        mCm = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("Add"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", add: "+
                            mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCm == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(5));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(5));
                        mCm = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyAddVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(4));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(4));
                        mCm = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("AddVerbiage"));
                    }
                }
                back.setImageResource(R.drawable.back_button);
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
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyMinus"));
                    } else {
                        speakSpeech(side[10]);
                        mCl = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("Minus"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", minus: "+
                            mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCl == 1) {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(11));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(11));
                        mCl = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyMinusVerbiage"));
                    } else {
                        if (count_flag == 1)
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(sort[mLevelThreeItemPos]).get(10));
                        else
                            speakSpeech(mVerbTxt.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(mLevelThreeItemPos).get(10));
                        mCl = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("MinusVerbiage"));
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
    protected void onResume() {
        super.onResume();
        new ChangeAppLocale(this).setLocale();
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
    protected void onPause() {
        super.onPause();
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView = null;
        mVerbTxt = null;
        mRecyclerItemsViewList = null;
        mMenuItemLinearLayout = null;
        mUserDataMeasure = null;
        myMusic = side = below = null;
        sort = null; count = null; mColor = null;
        myDbHelper = null;
    }

    public void tappedGridItemEvent(View view, View v, int position) {
        mCk = mCy = mCm = mCd = mCn = mCl = 0;
        resetActionButtons(-1);
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        setMenuImageBorder(v, true);
        mShouldReadFullSpeech = true;
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        mLevelThreeItemPos = mRecyclerView.getChildLayoutPosition(view);
        if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9))
            speakSpeech(myMusic[mLevelThreeItemPos]);
        else
            speakSpeech(myMusic[sort[mLevelThreeItemPos]]);

        incrementTouchCountOfItem(mLevelThreeItemPos);
        retainExpressiveButtonStates();
        mUserDataMeasure.recordGridItem("Tapped ".concat(myMusic[position]));
        mUserDataMeasure.reportLog(getLocalClassName()+", "+
                mLevelOneItemPos+", "+ mLevelTwoItemPos +", "+ mLevelThreeItemPos, Log.INFO);
        back.setImageResource(R.drawable.back_button);
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

    private void loadArraysFromResources() {
        mColor = getResources().getIntArray(R.array.arrActionBtnColors);
        side = getResources().getStringArray(R.array.arrActionSpeech);
        below = getResources().getStringArray(R.array.arrNavigationSpeech);
        String str = getResources().getString(R.string.levelThreeVerbiage);
        LevelThreeVerbiageModel mLevelThreeVerbiageModel = new Gson().fromJson(str, LevelThreeVerbiageModel.class);
        mVerbTxt = mLevelThreeVerbiageModel.getVerbiageModel();
    }

    private void retainExpressiveButtonStates() {
        if(mLevelOneItemPos == 0 && mLevelTwoItemPos == 1){
            int tmp = sort[mLevelThreeItemPos];
            if(tmp == 0){
                no.setAlpha(0.5f);
                no.setEnabled(false);
                yes.setAlpha(1f);
                yes.setEnabled(true);
            } else if (tmp == 1 || tmp == 2 || tmp == 3 || tmp == 5 || tmp == 6 || tmp == 7 || tmp == 8 || tmp == 9 || tmp == 10 || tmp == 11 || tmp == 12 || tmp == 15 || tmp == 16) {
                yes.setAlpha(0.5f);
                yes.setEnabled(false);
                no.setAlpha(1f);
                no.setEnabled(true);
            } else
                changeTheActionButtons(!DISABLE_ACTION_BTNS);
        }else if(mLevelOneItemPos == 0 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3))
            changeTheActionButtons(DISABLE_ACTION_BTNS);
        else if(mLevelOneItemPos == 1 && mLevelTwoItemPos == 3){
            int tmp = sort[mLevelThreeItemPos];
            if (tmp == 34 || tmp == 35 || tmp == 36 || tmp == 37)
                changeTheActionButtons(DISABLE_ACTION_BTNS);
            else
                changeTheActionButtons(!DISABLE_ACTION_BTNS);
        } else if (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) {
            if (mLevelThreeItemPos == 0)
                changeTheActionButtons(DISABLE_ACTION_BTNS);
            else
                changeTheActionButtons(!DISABLE_ACTION_BTNS);
        }else
            changeTheActionButtons(!DISABLE_ACTION_BTNS);
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

    private void incrementTouchCountOfItem(int levelThreeItemPos) {
        if (count_flag == 1) {
            count[sort[levelThreeItemPos]] = count[sort[levelThreeItemPos]] + 1;
            StringBuilder str = new StringBuilder();
            for(int i=0; i< count.length; ++i)
                str.append(count[i]).append(",");
            myDbHelper.setlevel(mLevelOneItemPos, mLevelTwoItemPos, str.toString());
        }
    }

    private void myMusic_function(int levelOneItemPos, int levelTwoItemPos) {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsSpeechText);   break;
                case 2:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechText);   break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActBrushingSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActToiletSpeechText);   break;
                case 2:
                    myMusic =  getResources().getStringArray(R.array.arrLevelThreeDailyActBathingSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccSpeechText);   break;
                case 4:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadySpeechText);    break;
                case 5:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActSleepSpeechText);   break;
                case 6:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActTherapySpeechText);   break;
                case 7:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActMorningScheSpeechText);   break;
                case 8:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeDailyActBedTimeScheSpeechText);   break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerSpeechText);   break;
                case 2:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksSpeechText);   break;
                case 4:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsSpeechText);    break;
                case 5:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksSpeechText);   break;
                case 6:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutlerySpeechText);   break;
                case 7:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonSpeechText);   break;
            }
        } else if (levelOneItemPos == 3){
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunInDGamesSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesSpeechText);   break;
                case 2:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunSportsSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunTvSpeechText);   break;
                case 4:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunMusicSpeechText);    break;
                case 5:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeFunActivitiesSpeechText);   break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsSpeechText);   break;
                case 2:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningBooksSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningColorsSpeechText);   break;
                case 4:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningShapesSpeechText);    break;
                case 5:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningStationarySpeechText);   break;
                case 6:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjSpeechText);   break;
                case 7:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjSpeechText);   break;
                case 8:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningTransportSpeechText);   break;
                case 9:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeLearningMoneySpeechText);   break;
            }
        } else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos){
                case 0:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeSpeechText);   break;
                case 1:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaDaySpeechText);   break;
                case 2:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthSpeechText);   break;
                case 3:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherSpeechText);   break;
                case 4:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsSpeechText);    break;
                case 5:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestSpeechText);   break;
                case 6:
                    myMusic = getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysSpeechText);   break;
            }
        }
    }
}