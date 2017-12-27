package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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

import com.dsource.idc.jellow.Models.LevelTwoVerbiageModel;
import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellow.Utility.IndexSorter;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import static com.dsource.idc.jellow.Utility.UserDataMeasure.startMeasuring;
import static com.dsource.idc.jellow.Utility.UserDataMeasure.stopMeasuring;

public class LevelTwoActivity extends AppCompatActivity {
    private final int REQ_HOME = 0, MENU_ITEM_DAILY_ACT = 1, MENU_ITEM_PEOPLE = 5, MENU_ITEM_PLACES = 6, MENU_ITEM_HELP = 8;
    private final boolean DISABLE_ACTION_BTNS = true;

    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;
    private int image_flag = -1, flag_keyboard = 0, mActionBtnClickCount;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    private EditText et;
    private KeyListener originalKeyListener;
    private RecyclerView mRecyclerView;
    private LinearLayout mMenuItemLinearLayout;
    private int mLevelOneItemPos, mLevelTwoItemPos = -1, mSelectedItemAdapterPos = -1;
    private boolean mShouldReadFullSpeech = false;
    private ArrayList<View> mRecyclerItemsViewList;
    private UserDataMeasure mUserDataMeasure;
    private String mActionBarTitle;
    private SessionManager mSession;

    private int[] mColor;
    private String[] mLevelTwoSpeechText, mLevelTwoAdapterText,side, below;
    private ArrayList<ArrayList<ArrayList<String>>> mLayerTwoSpeech;
    private String[] myMusic, actionBarText;
    private String[] temp = new String[100];
    private String[] image_temp = new String[100];

    private String[] new_people_adapter = new String[100];
    private String[] people = new String[100];
    private Integer[] count_people = new Integer[100];
    private String[] new_people = new String[100];
    private Integer[] new_people_count = new Integer[100];
    private Integer[] sort = new Integer[100];

    private String[] places = new String[100];
    private String[] new_places_adapter = new String[100];
    private Integer[] count_places = new Integer[15];
    private String[] new_places = new String[100];
    private Integer[] new_places_count = new Integer[15];
    private Integer[] sort_places = new Integer[15];
    private String mBloodGroup, end, actionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();
        mUserDataMeasure = new UserDataMeasure(this);
        mUserDataMeasure.recordScreen(this.getLocalClassName());
        new ChangeAppLocale(this).setLocale();
        mSession = new SessionManager(this);
        initializeArrayListOfRecycler();
        myMusic_function(mLevelOneItemPos);

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
        back.setAlpha(1f);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        keyboard.setContentDescription(below[2]);
        et = (EditText) findViewById(R.id.et);
        et.setContentDescription(getString(R.string.string_to_speak));
        et.setVisibility(View.INVISIBLE);

        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setContentDescription(getString(R.string.speak_written_text));
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);


        startMeasuring();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 3));
        if (mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES) {}
        else mRecyclerView.setAdapter(new LevelTwoAdapter(this, mLevelOneItemPos));

        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                mMenuItemLinearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_icon1);
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

        end = getString(R.string.endString);
        if (mLevelOneItemPos == MENU_ITEM_PEOPLE) {
            people = getResources().getStringArray(R.array.arrLevelTwoPeopleIcon);
            count_people = new Integer[people.length];
            new_people_count = new Integer[people.length];
            new_people = new String[people.length];
            sort = new Integer[people.length];
            for (int j = 0; j < people.length; j++) {
                count_people[j] = 0;
                sort[j] = j;
            }

            String savedString = "";
            {
                final int LANG_ENG = 0;
                if (mSession.getLanguage().contains("en"))
                    savedString = mSession.getPeoplePreferences();
                else savedString = mSession.getPeoplePreferences();
            }
            if (!savedString.equals("")) {
                StringTokenizer st = new StringTokenizer(savedString, ",");
                for (int j = 0; j < people.length; ++j)
                    count_people[j] = Integer.parseInt(st.nextToken());
            }

            new_people_count = count_people;
            IndexSorter<Integer> is = new IndexSorter<Integer>(new_people_count);
            is.sort();
            Integer[] indexes = new Integer[mLayerTwoSpeech.get(mLevelOneItemPos).size()];
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                ++g;
            }
            for (int j = 0; j < new_people_count.length; ++j)
                sort[j] = indexes[j];
            for (int j = 0; j < new_people_count.length; ++j) {
                new_people[j] = people[sort[j]];
                new_people_adapter[j] = mLevelTwoAdapterText[sort[j]];
            }
            temp = Arrays.copyOfRange(new_people_adapter, 0, new_people_count.length);
            image_temp = Arrays.copyOfRange(new_people, 0, new_people_count.length);
            mRecyclerView.setAdapter(new PeoplePlacesAdapter(this, temp, image_temp));
            myMusic = new String[temp.length];
            myMusic = sortTtsSpeechArray(mLevelTwoSpeechText, MENU_ITEM_PEOPLE);
        }else if (mLevelOneItemPos == MENU_ITEM_PLACES) {
            places = getResources().getStringArray(R.array.arrLevelTwoPlacesIcon);
            for (int j = 0; j < count_places.length; j++) {
                count_places[j] = 0;
                sort_places[j] = j;
            }

            String savedString = mSession.getPlacesPreferences();
            if(!savedString.equals("")){
                StringTokenizer st = new StringTokenizer(savedString, ",");
                for (int j = 0; j < places.length; ++j)
                    count_places[j] = Integer.parseInt(st.nextToken());
            }
            new_places_count = count_places;
            ArrayIndexComparator comparator_places = new ArrayIndexComparator(new_places_count);
            Integer[] indexes_places = comparator_places.createIndexArray();
            Arrays.sort(indexes_places, comparator_places);
            for (int j = 0; j < new_places_count.length; ++j)
                sort_places[j] = indexes_places[j];
            for (int j = 0; j < new_places_count.length; ++j) {
                new_places[j] = places[sort_places[j]];
                new_places_adapter[j] = mLevelTwoAdapterText[sort_places[j]];
            }
            if (mLevelOneItemPos == MENU_ITEM_PLACES) {
                temp = Arrays.copyOfRange(new_places_adapter, 0, new_places_count.length);
                image_temp = Arrays.copyOfRange(new_places, 0, new_places_count.length);
                mRecyclerView.setAdapter(new PeoplePlacesAdapter(this, temp, image_temp));
                myMusic = new String[temp.length];
                myMusic = sortTtsSpeechArray(mLevelTwoSpeechText, MENU_ITEM_PLACES);
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(below[1]);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 1) {
                        setActionButtonToAboutMe(image_flag);
                        changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    }else if(mLevelOneItemPos == MENU_ITEM_HELP &&
                            ((mLevelTwoItemPos == 0) ||(mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) || (mLevelTwoItemPos == 4) ||(mLevelTwoItemPos == 5) ||(mLevelTwoItemPos == 12) ||(mLevelTwoItemPos == 13) ||(mLevelTwoItemPos == 14)))
                        changeTheActionButtons(DISABLE_ACTION_BTNS);
                    else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 10)
                        badTouchDisableActionButtons();
                    else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 15)
                        safetyDisableActionButtons();
                    else
                        changeTheActionButtons(!DISABLE_ACTION_BTNS);
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
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    if (mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 1){
                        setActionButtonToAboutMe(image_flag);
                        changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    }else if(mLevelOneItemPos == MENU_ITEM_HELP &&
                            ((mLevelTwoItemPos == 0) ||(mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) || (mLevelTwoItemPos == 4) ||(mLevelTwoItemPos == 5) ||(mLevelTwoItemPos == 12) ||(mLevelTwoItemPos == 13) ||(mLevelTwoItemPos == 14)))
                        changeTheActionButtons(DISABLE_ACTION_BTNS);
                    else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 10)
                        badTouchDisableActionButtons();
                    else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 15)
                        safetyDisableActionButtons();
                    else
                        changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    flag_keyboard = 0;
                    showActionBarTitle(true);
                    } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
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
                    back.setImageResource(R.drawable.back_button);
                    back.setAlpha(1f);
                    back.setEnabled(true);
                    flag_keyboard = 1;
                    showActionBarTitle(false);
                }
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
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCk == 1) {
                        speakSpeech(side[1]);                               //if pressing like for second time then says like very much
                        mCk = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyLike"));
                    } else {
                        speakSpeech(side[0]);                               //if pressing like for first time says i like iin lang specified for mSession
                        mCk = 1;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("like"));
                    }
                } else {
                    mUserDataMeasure.reportLog(getLocalClassName()+", like: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCk == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(1));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(1));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1) + mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1));
                        mCk = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyLikeVerbiage"));
                    } else {
                        if(mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(0));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(0));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0)+ mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0));
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
                mCk = 0; mCy = 0; mCm = 0; mCn = 0; mCl = 0;
                image_flag = 1;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
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
                    mUserDataMeasure.reportLog(getLocalClassName()+", dislike: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCd == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(7));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(7));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7));
                        mCd = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyDislikeVerbiage"));
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(6));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(6));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6));
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
                mCk = 0; mCm = 0; mCd = 0; mCn = 0; mCl = 0;
                image_flag = 2;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
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
                } else  {
                    mUserDataMeasure.reportLog(getLocalClassName()+", yes: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(3));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(3));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3));
                        mCy = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyYesVerbiage"));
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(2));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(2));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2));
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
                mCk = 0; mCy = 0; mCm = 0; mCd = 0; mCl = 0;
                image_flag = 3;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
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
                    mUserDataMeasure.reportLog(getLocalClassName()+", no: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCn == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(9));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(9));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9));
                        mCn = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyNoVerbiage"));
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(8));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(8));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8));
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
                mCk = 0; mCy = 0; mCd = 0; mCn = 0; mCl = 0;
                image_flag = 4;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
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
                    mUserDataMeasure.reportLog(getLocalClassName()+", add: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCm == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(5));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(5));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5)+ mSession.getFather_no().replaceAll("\\B", " ")+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5));
                        mCm = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyAddVerbiage"));
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(4));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(4));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5) + mSession.getFather_no().replaceAll("\\B", " ")+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(4));
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
                mCk = 0; mCy = 0; mCm = 0; mCd = 0; mCn = 0;
                image_flag = 5;
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setActionButtonToAboutMe(image_flag);
                else
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
                    mUserDataMeasure.reportLog(getLocalClassName()+",minus: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCl == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(11));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(11));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            setBloodGroup();
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10) + mBloodGroup+end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(11));
                        mCl = 0;
                        mUserDataMeasure.recordGridItem("Tapped ".concat("ReallyMinusVerbiage"));
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(10));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(10));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            setBloodGroup();
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10)+ mBloodGroup+end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          if(requestCode == REQ_HOME && resultCode == RESULT_CANCELED)
            finish();
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
        mRecyclerView = null;
        mLayerTwoSpeech = null;
        mRecyclerItemsViewList = null;
        mMenuItemLinearLayout = null;
        mUserDataMeasure = null;
        mLevelTwoSpeechText = null;
        mLevelTwoAdapterText = null;
        side = null; below = null;
        temp = null;
        image_temp = null;
        new_people_adapter = null;
        people  = null;
        count_people = null;
        new_people = null;
        new_people_count = null;
        sort = null;

        places = null;
        new_places_adapter = null;
        count_places = null;
        new_places = null;
        new_places_count = null;
        sort_places = null;

        stopMeasuring("LevelTwoActivity");
        super.onDestroy();

    }

    public void tappedGridItemEvent(View view, View v, int position) {
        mCk = mCy = mCm = mCd = mCn = mCl = 0;
        resetActionButtons(-1);
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        setMenuImageBorder(v, true);
        mShouldReadFullSpeech = true;
        String title = getIntent().getExtras().getString("selectedMenuItemPath")+ " ";
        if (mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES) {
            speakSpeech(myMusic[position]);
            mUserDataMeasure.recordGridItem("Tapped ".concat(myMusic[position]));
        }else if(mLevelTwoItemPos == position && mLevelOneItemPos != MENU_ITEM_HELP){
            Intent intent = new Intent(LevelTwoActivity.this, LevelThreeActivity.class);
            if(mLevelOneItemPos == MENU_ITEM_DAILY_ACT && ( mLevelTwoItemPos == 0 ||  mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8 ))
                intent = new Intent(LevelTwoActivity.this, SequenceActivity.class);
            mUserDataMeasure.recordGridItem("Opened ".concat(myMusic[position]));
            intent.putExtra("mLevelOneItemPos", mLevelOneItemPos);
            intent.putExtra("mLevelTwoItemPos", mLevelTwoItemPos);
            intent.putExtra("selectedMenuItemPath", mActionBarTitle+ "/");
            startActivityForResult(intent, REQ_HOME);
        }else if(mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES){
            speakSpeech(myMusic[sort[mLevelTwoItemPos]]);
            mUserDataMeasure.recordGridItem("Tapped ".concat(myMusic[position]));
        }else {
            speakSpeech(myMusic[position]);
            mUserDataMeasure.recordGridItem("Tapped ".concat(myMusic[position]));
        }
        mLevelTwoItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        if(mLevelOneItemPos == MENU_ITEM_PEOPLE)
            title += actionBarText[sort[mLevelTwoItemPos]];
        else if( mLevelOneItemPos == MENU_ITEM_PLACES)
            title += actionBarText[sort_places[mLevelTwoItemPos]];
        else if( mLevelOneItemPos == MENU_ITEM_HELP)
            title += actionBarText[mLevelTwoItemPos];
        else
            title += actionBarText[mLevelTwoItemPos].substring(0, actionBarText[mLevelTwoItemPos].length()-1);
        mActionBarTitle = title;
        getSupportActionBar().setTitle(mActionBarTitle);
        if(mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES)
            incrementTouchCountOfItem(mLevelTwoItemPos);

        if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 1)
            setActionButtonToAboutMe(-1);
        if(mLevelOneItemPos == MENU_ITEM_HELP &&
                ((mLevelTwoItemPos == 0) ||(mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) || (mLevelTwoItemPos == 4) ||(mLevelTwoItemPos == 5) ||(mLevelTwoItemPos == 12) ||(mLevelTwoItemPos == 13) ||(mLevelTwoItemPos == 14)))
            changeTheActionButtons(DISABLE_ACTION_BTNS);
        else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 10)
            badTouchDisableActionButtons();
        else if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 15)
            safetyDisableActionButtons();
        else
            changeTheActionButtons(!DISABLE_ACTION_BTNS);
        mUserDataMeasure.reportLog(getLocalClassName()+", "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
        back.setImageResource(R.drawable.back_button);
    }

    private void initializeArrayListOfRecycler() {
        int size = -1;
        switch (mLevelOneItemPos) {
            case 0: size = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText).length; break;
            case 1: size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText).length; break;
            case 2: size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText).length; break;
            case 3: size = getResources().getStringArray(R.array.arrLevelTwoFunSpeechText).length; break;
            case 4: size = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText).length; break;
            case 5: size = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText).length; break;
            case 6: size = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText).length; break;
            case 7: size = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText).length; break;
            case 8: size = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText).length; break;
        }
        mRecyclerItemsViewList = new ArrayList<>(size);
        while(mRecyclerItemsViewList.size() <= size) mRecyclerItemsViewList.add(null);
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

        LevelTwoVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.levelTwoVerbiage), LevelTwoVerbiageModel.class);
        mLayerTwoSpeech = verbiageModel.getVerbiageModel();

        if (mLevelOneItemPos == MENU_ITEM_PEOPLE) {
            mLevelTwoSpeechText = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText);
            mLevelTwoAdapterText = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText);
        }else if (mLevelOneItemPos == MENU_ITEM_PLACES) {
            mLevelTwoSpeechText = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText);
            mLevelTwoAdapterText = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText);
        }
    }


    private void setBloodGroup() {
        switch(mSession.getBlood()){
            case  0: mBloodGroup = getString(R.string.aPos); break;
            case  1: mBloodGroup = getString(R.string.aNeg); break;
            case  2: mBloodGroup = getString(R.string.bPos); break;
            case  3: mBloodGroup = getString(R.string.bNeg); break;
            case  4: mBloodGroup = getString(R.string.abPos); break;
            case  5: mBloodGroup = getString(R.string.abNeg); break;
            case  6: mBloodGroup = getString(R.string.oPos); break;
            case  7: mBloodGroup = getString(R.string.oNeg); break;
        }
    }

    private void safetyDisableActionButtons() {
        like.setAlpha(1f);
        dislike.setAlpha(1f);
        yes.setAlpha(1f);
        no.setAlpha(0.5f);
        add.setAlpha(1f);
        minus.setAlpha(1f);
        like.setEnabled(true);
        dislike.setEnabled(true);
        yes.setEnabled(true);
        no.setEnabled(false);
        add.setEnabled(true);
        minus.setEnabled(true);
    }

    private void badTouchDisableActionButtons() {
        dislike.setAlpha(1f);
        no.setAlpha(1f);
        minus.setAlpha(1f);
        dislike.setEnabled(true);
        no.setEnabled(true);
        minus.setEnabled(true);
        like.setAlpha(0.5f);
        yes.setAlpha(0.5f);
        add.setAlpha(0.5f);
        like.setEnabled(false);
        yes.setEnabled(false);
        add.setEnabled(false);
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
        if(mSession.getGridSize() == 0) {
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
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i)
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
    }

    private void incrementTouchCountOfItem(int levelTwoItemPosition) {
        StringBuilder stringBuilder = new StringBuilder();
        if(mLevelOneItemPos == MENU_ITEM_PEOPLE) {
            count_people[sort[levelTwoItemPosition]] += 1;
            for (Integer countPeople : count_people) stringBuilder.append(countPeople).append(",");
        }else {
            count_places[sort_places[levelTwoItemPosition]] += 1;
            for (Integer countPlace : count_places) stringBuilder.append(countPlace).append(",");
        }
        if(mLevelOneItemPos == MENU_ITEM_PEOPLE)
            mSession.setPeoplePreferences(stringBuilder.toString());
        else if(mLevelOneItemPos == MENU_ITEM_PLACES)
            mSession.setPlacesPreferences(stringBuilder.toString());
    }

    private void setActionButtonToAboutMe(int image_flag){
        like.setImageResource(R.drawable.mynameis);
        dislike.setImageResource(R.drawable.caregiver);
        yes.setImageResource(R.drawable.email);
        no.setImageResource(R.drawable.address);
        add.setImageResource(R.drawable.contact);
        minus.setImageResource(R.drawable.bloodgroup);
        switch (image_flag){
            case 0: like.setImageResource(R.drawable.mynameis_pressed); break;
            case 1: dislike.setImageResource(R.drawable.caregiver_pressed); break;
            case 2: yes.setImageResource(R.drawable.email_pressed); break;
            case 3: no.setImageResource(R.drawable.address_pressed); break;
            case 4: add.setImageResource(R.drawable.contact_pressed); break;
            case 5: minus.setImageResource(R.drawable.blooedgroup_pressed); break;
            case 6: home.setImageResource(R.drawable.homepressed); break;
            default: break;
        }
    }

    private String[] sortTtsSpeechArray(String[] speechTextArray, int levelOnePos) {
        String[] temp = new String[speechTextArray.length];
        if(levelOnePos == MENU_ITEM_PEOPLE) {
            for (int i = 0; i < speechTextArray.length; ++i)
                temp[i] = speechTextArray[sort[i]];
        }else{
            for (int i = 0; i < speechTextArray.length; ++i)
                temp[i] = speechTextArray[sort_places[i]];
        }
        return temp;
    }

    public void myMusic_function(int levelOneItemPos){
        switch (levelOneItemPos){
            case 0:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterText);
                break;
            case 1:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterText);
                break;
            case 2:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoEatAdapterText);
                break;
            case 3:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoFunSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoFunAdapterText);
                break;
            case 4:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoLearningAdapterText);
                break;
            case 5:
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText); break;
            case 6:
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText); break;
            case 7:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterText);
                break;
            case 8:
                myMusic = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoHelpAdapterText);
                break;
        }
    }

    private class ArrayIndexComparator implements Comparator<Integer>{
        private final Integer[] array;
        ArrayIndexComparator(Integer[] array)
        {
            this.array = array;
        }
        Integer[] createIndexArray(){
            Integer[] indexes = new Integer[array.length];
            for (int i = 0; i < array.length; i++){
                indexes[i] = i; // Autoboxing
            }
            return indexes;
        }

        @Override
        public int compare(Integer index1, Integer index2){
            // Autounbox from Integer to int to use as array indexes
            return array[index2].compareTo(array[index1]);
        }
    }
}