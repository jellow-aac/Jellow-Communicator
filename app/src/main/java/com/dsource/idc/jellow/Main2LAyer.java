package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsource.idc.jellow.Models.LevelTwoVerbiageModel;
import com.dsource.idc.jellow.Utility.IndexSorter;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;

public class Main2LAyer extends AppCompatActivity {
    private final int LANG_ENG = 0, LANG_HINDI = 1, MENU_ITEM_DAILY_ACT = 1, MENU_ITEM_PEOPLE = 5, MENU_ITEM_PLACES = 6, MENU_ITEM_HELP = 8;
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
    private TextToSpeech mTts;
    private UserDataMeasure userDataMeasure;
    private String mActionBarTitle;
    private SessionManager mSession;

    private int[] mColor;
    private String[] mLevelTwoSpeechText, mLevelTwoAdapterText,side, below;
    private ArrayList<ArrayList<ArrayList<String>>> mLayerTwoSpeech;
    private String[] myMusic, actionBarText;
    private String[] temp = new String[100];
    private Integer[] image_temp = new Integer[100];

    private String[] new_people_adapter = new String[100];
    private Integer[] people = new Integer[100];
    private Integer[] count_people = new Integer[100];
    private Integer[] new_people = new Integer[100];
    private Integer[] new_people_count = new Integer[100];
    private Integer[] sort = new Integer[100];

    private Integer[] places = new Integer[100];
    private String[] new_places_adapter = new String[100];
    private Integer[] count_places = new Integer[15];
    private Integer[] new_places = new Integer[100];
    private Integer[] new_places_count = new Integer[15];
    private Integer[] sort_places = new Integer[15];
    private String mBloodGroup;
    private String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial1);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        loadArraysFromResources();
        userDataMeasure = new UserDataMeasure(this);
        userDataMeasure.recordScreen(this.getLocalClassName());
        mSession = new SessionManager(this);
        initializeArrayListOfRecycler();

        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myMusic_function(mLevelOneItemPos);
                    mTts.setEngineByPackageName("com.google.android.tts");
                    new BackgroundSpeechOperationsAsync().execute("");
                }
            }
        });
        mTts.setSpeechRate((float) mSession.getSpeed()/50);
        mTts.setPitch((float) mSession.getPitch()/50);

        myMusic = new String[100];
        like = (ImageView) findViewById(R.id.ivlike);
        dislike = (ImageView) findViewById(R.id.ivdislike);
        add = (ImageView) findViewById(R.id.ivadd);
        minus = (ImageView) findViewById(R.id.ivminus);
        yes = (ImageView) findViewById(R.id.ivyes);
        no = (ImageView) findViewById(R.id.ivno);
        home = (ImageView) findViewById(R.id.ivhome);
        back = (ImageView) findViewById(R.id.ivback);
        back.setAlpha(1f);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        et = (EditText) findViewById(R.id.et);
        et.setVisibility(View.INVISIBLE);

        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make the field non-editable
        et.setKeyListener(null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 3));
        if (mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES) {}
        else mRecyclerView.setAdapter(new LayerImageAdapter(this, mLevelOneItemPos));

        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                mMenuItemLinearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_icon1);
                mMenuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetActionButtons(-1);
                        resetRecyclerAllItems();
                        mActionBtnClickCount = 0;
                        setMenuImageBorder(v, true);
                        mShouldReadFullSpeech = true;
                        String title = getIntent().getExtras().getString("selectedMenuItemPath");
                        if (mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES)
                            speakSpeech(myMusic[position]);
                        else if(mLevelTwoItemPos == position && mLevelOneItemPos != MENU_ITEM_HELP){
                            Intent intent = new Intent(Main2LAyer.this, Layer3Activity.class);
                            if(mLevelOneItemPos == MENU_ITEM_DAILY_ACT && ( mLevelTwoItemPos == 0 ||  mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8 ))
                                intent = new Intent(Main2LAyer.this, Sequence_Activity.class);
                            intent.putExtra("mLevelOneItemPos", mLevelOneItemPos);
                            intent.putExtra("mLevelTwoItemPos", mLevelTwoItemPos);
                            intent.putExtra("selectedMenuItemPath", mActionBarTitle);
                            startActivity(intent);
                        }else if(mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES){
                            speakSpeech(myMusic[sort[mLevelTwoItemPos]]);
                        }else {
                            speakSpeech(myMusic[position]);
                        }
                        mLevelTwoItemPos = mRecyclerView.getChildLayoutPosition(view);
                        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
                        if(mLevelOneItemPos == MENU_ITEM_PEOPLE)
                            title += " / " + actionBarText[sort[mLevelTwoItemPos]];
                        else if( mLevelOneItemPos == MENU_ITEM_PLACES)
                            title += " / " + actionBarText[sort_places[mLevelTwoItemPos]];
                        else if( mLevelOneItemPos == MENU_ITEM_HELP)
                            title += " / " + actionBarText[mLevelTwoItemPos];
                        else
                            title += " / " + actionBarText[mLevelTwoItemPos].substring(0, actionBarText[mLevelTwoItemPos].length()-1);
                        mActionBarTitle = title;
                        getSupportActionBar().setTitle(mActionBarTitle);
                        if(mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES)
                            incrementTouchCountOfItem(mLevelTwoItemPos);

                        if(mLevelOneItemPos == MENU_ITEM_HELP && mLevelTwoItemPos == 0)
                            setActionButtonToAboutMe(-1);
                        if(mLevelOneItemPos == MENU_ITEM_HELP &&
                                ((mLevelTwoItemPos == 1) || (mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) ||(mLevelTwoItemPos == 4)))
                            changeTheActionButtons(DISABLE_ACTION_BTNS);
                        else
                            changeTheActionButtons(!DISABLE_ACTION_BTNS);
                        userDataMeasure.reportLog(getLocalClassName()+", "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
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
            people = loadAdapterMenuIconsWithoutSort(getResources().obtainTypedArray(R.array.arrLevelTwoPeopleIcon));
            count_people = new Integer[people.length];
            new_people_count = new Integer[people.length];
            new_people = new Integer[people.length];
            sort = new Integer[people.length];
            for (int j = 0; j < people.length; j++) {
                count_people[j] = 0;
                sort[j] = j;
            }

            String savedString = "";
            if (mSession.getLanguage() == LANG_ENG)
                savedString = mSession.getPeoplePreferences();
            else savedString = mSession.getPeoplePreferences();
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
            mRecyclerView.setAdapter(new Adapter_ppl_places(this, temp, image_temp));
            myMusic = new String[temp.length];
            myMusic = sortTtsSpeechArray(mLevelTwoSpeechText, MENU_ITEM_PEOPLE);
        }else if (mLevelOneItemPos == MENU_ITEM_PLACES) {
            places = loadAdapterMenuIconsWithoutSort(getResources().obtainTypedArray(R.array.arrLevelTwoPlacesIcon));
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
                mRecyclerView.setAdapter(new Adapter_ppl_places(this, temp, image_temp));
                myMusic = new String[temp.length];
                myMusic = sortTtsSpeechArray(mLevelTwoSpeechText, MENU_ITEM_PLACES);
            }
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setImageResource(R.drawable.homepressed);
                speakSpeech(below[0]);
                finish();
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
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                    back.setAlpha(.5f);
                    back.setEnabled(false);
                    flag_keyboard = 0;
                } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.backpressed);
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
                    back.setAlpha(1f);
                    back.setEnabled(true);
                    flag_keyboard = 1;
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mTts.setSpeechRate((float) mSession.getSpeed() / 50);
                mTts.setPitch((float) mSession.getPitch() / 50);
                speakSpeech(et.getText().toString());
                userDataMeasure.reportLog(getLocalClassName()+", TtsSpeak", Log.INFO);
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

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(below[1]);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                } else {
                    back.setImageResource(R.drawable.backpressed);
                    finish();
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCy = mCm = mCd = mCn = mCl = 0;
                image_flag = 0;
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCk == 1) {
                        speakSpeech(side[1]);                               //if pressing like for second time then says like very much
                        mCk = 0;
                    } else {
                        speakSpeech(side[0]);                               //if pressing like for first time says i like iin lang specified for mSession
                        mCk = 1;
                    }
                } else {
                    userDataMeasure.reportLog(getLocalClassName()+", like: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCk == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(1));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(1));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1) + mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1));
                        mCk = 0;
                    } else {
                        if(mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(0));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(0));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0)+ mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0));
                        mCk = 1;
                    }
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = 0; mCy = 0; mCm = 0; mCn = 0; mCl = 0;
                image_flag = 1;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos==0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCd == 1) {
                        speakSpeech(side[7]);
                        mCd = 0;
                    } else {
                        speakSpeech(side[6]);
                        mCd = 1;
                    }
                } else {
                    userDataMeasure.reportLog(getLocalClassName()+", dislike: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCd == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(7));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(7));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7));
                        mCd = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(6));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(6));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6));
                        mCd = 1;
                    }
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = 0; mCm = 0; mCd = 0; mCn = 0; mCl = 0;
                image_flag = 2;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCy == 1) {
                        speakSpeech(side[3]);
                        mCy = 0;
                    } else {
                        speakSpeech(side[2]);
                        mCy = 1;
                    }
                } else  {
                    userDataMeasure.reportLog(getLocalClassName()+", yes: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(3));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(3));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3));
                        mCy = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(2));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(2));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2));
                        mCy = 1;
                    }
                }
                }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = 0; mCy = 0; mCm = 0; mCd = 0; mCl = 0;
                image_flag = 3;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos==0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCn == 1) {
                        speakSpeech(side[9]);
                        mCn = 0;
                    } else {
                        speakSpeech(side[8]);
                        mCn = 1;
                    }
                } else {
                    userDataMeasure.reportLog(getLocalClassName()+", no: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCn == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(9));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(9));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9));
                        mCn = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(8));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(8));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8));
                        mCn = 1;
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = 0; mCy = 0; mCd = 0; mCn = 0; mCl = 0;
                image_flag = 4;
                resetActionButtons(image_flag);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCm == 1) {
                        speakSpeech(side[5]);
                        mCm = 0;
                    } else {
                        speakSpeech(side[4]);
                        mCm = 1;
                    }
                } else {
                    userDataMeasure.reportLog(getLocalClassName()+", add: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCm == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(5));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(5));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0) {
                            /*mTts.setLanguage(new Locale("eng", "IND"));*/
                            mTts.setLanguage(Locale.US);
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5)+ mSession.getFather_no().replaceAll("\\B", " ")+end);
                            mTts.setLanguage(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5));
                        mCm = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(4));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(4));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            //mTts.setLanguage(new Locale("eng", "IND"));
                            mTts.setLanguage(Locale.US);
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5) + mSession.getFather_no().replaceAll("\\B", " ")+end);
                            mTts.setLanguage(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(4));
                        mCm = 1;
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCk = 0; mCy = 0; mCm = 0; mCd = 0; mCn = 0;
                image_flag = 5;
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                    setActionButtonToAboutMe(image_flag);
                else
                    resetActionButtons(image_flag);
                if (!mShouldReadFullSpeech) {
                    if (mCl == 1) {
                        speakSpeech(side[11]);
                        mCl = 0;
                    } else {
                        speakSpeech(side[10]);
                        mCl = 1;
                    }
                } else {
                    userDataMeasure.reportLog(getLocalClassName()+",minus: "+mLevelOneItemPos+", "+ mLevelTwoItemPos , Log.INFO);
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCl == 1) {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(11));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(11));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            setBloodGroup();
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10) + mBloodGroup+end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(11));
                        mCl = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort[mLevelTwoItemPos]).get(10));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(sort_places[mLevelTwoItemPos]).get(10));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            setBloodGroup();
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10)+ mBloodGroup+end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10));
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
                startActivity(new Intent(Main2LAyer.this, Setting.class));
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    private void speakSpeech(String speechText){
        mTts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null);
    }

    private Integer[] loadAdapterMenuIconsWithoutSort(TypedArray typeIconArray) {
        Integer[] tempIconArr = new Integer[typeIconArray.length()];

        for (int j = 0; j < typeIconArray.length(); j++) {
            tempIconArr[j] = typeIconArray.getResourceId(j, -1);
        }
        return tempIconArr;
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
        CircularImageView circularImageView = (CircularImageView) recyclerChildView.findViewById(R.id.icon1);
        String strSrBw = new SessionManager(this).getShadowRadiusAndBorderWidth();
        int sr, bw;
        sr = Integer.valueOf(strSrBw.split(",")[0]);
        bw = Integer.valueOf(strSrBw.split(",")[1]);
        if (setBorder){
            if(mActionBtnClickCount > 0)
                circularImageView.setBorderColor(mColor[image_flag]);
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
        like.setImageResource(R.drawable.mynameis_unpressed);
        dislike.setImageResource(R.drawable.caregiver_unpressed);
        yes.setImageResource(R.drawable.email_unpressed);
        no.setImageResource(R.drawable.address_unpressed);
        add.setImageResource(R.drawable.contact_unpressed);
        minus.setImageResource(R.drawable.bloodgroup_unpressed);
        switch (image_flag){
            case 0: like.setImageResource(R.drawable.mynameis); break;
            case 1: dislike.setImageResource(R.drawable.caregiver); break;
            case 2: yes.setImageResource(R.drawable.email); break;
            case 3: no.setImageResource(R.drawable.address); break;
            case 4: add.setImageResource(R.drawable.contact); break;
            case 5: minus.setImageResource(R.drawable.bloodgroup); break;
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

    private class BackgroundSpeechOperationsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                mTts.setLanguage(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));
            } catch (Exception e) {
                new UserDataMeasure(Main2LAyer.this).reportException(e);
                new UserDataMeasure(Main2LAyer.this).reportLog("Failed to set language.", Log.ERROR);
            }
            return "";
        }
    }
}