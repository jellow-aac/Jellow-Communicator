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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsource.idc.jellow.Utility.IndexSorter;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;

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
    private String[] peopleSpeechTextEnglish, placesSpeechTextEnglish,peopleSpeechTextHindi, placesSpeechTextHindi,
            people_adapter_hindi, places_adapter_hindi, places_adapter, people_adapter, side_english, side_hindi, below_english, below_hindi;
    private String[][][] layer_2_speech = new String[100][100][100];
    private String[] myMusic, actionBarText;
    private String[] side = new String[100];
    private String[] below = new String[100];
    private String[] temp = new String[100];
    private Integer[] image_temp = new Integer[100];
    private String[] new_people_adapter = new String[100];
    private Integer[] people = new Integer[100];
    private Integer[] count_people = new Integer[100];
    private Integer[] new_people = new Integer[100];
    private Integer[] new_people_count = new Integer[100];
    private Integer[] sort = new Integer[100];
    private String[] new_places_adapter = new String[100];
    private Integer[] count_places = new Integer[15];
    private Integer[] new_places = new Integer[100];
    private Integer[] new_places_count = new Integer[15];
    private Integer[] sort_places = new Integer[15];
    private String mBloodGroup;
    private String end = " है।";

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
        if (mSession.getLanguage() == LANG_ENG){
            layer_2_speech = layer_2_speech_english;
            side = side_english;
            below = below_english;
        }else{
            layer_2_speech = layer_2_speech_hindi;
            side = side_hindi;
            below = below_hindi;
        }
        {
            int size = -1;
            switch (mLevelOneItemPos) {
                case 0:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextHindi).length;
                    break;
                case 1:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextHindi).length;
                    break;
                case 2:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextHindi).length;
                    break;
                case 3:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextHindi).length;
                    break;
                case 4:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextHindi).length;
                    break;
                case 5:
                    if(mSession.getLanguage() == LANG_ENG) size = peopleSpeechTextEnglish.length;
                    else  size = peopleSpeechTextHindi.length;
                    break;
                case 6:
                    if(mSession.getLanguage() == LANG_ENG) size = placesSpeechTextEnglish.length;
                    else  size = placesSpeechTextHindi.length;
                    break;
                case 7:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextHindi).length;
                    break;
                case 8:
                    if(mSession.getLanguage() == LANG_ENG) size = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextEnglish).length;
                    else  size = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextHindi).length;
                    break;
            }
            mRecyclerItemsViewList = new ArrayList<>(size);
            while(mRecyclerItemsViewList.size() <= size) mRecyclerItemsViewList.add(null);
        }

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
        if (mLevelOneItemPos != MENU_ITEM_PEOPLE || mLevelOneItemPos != MENU_ITEM_PLACES) {
            mRecyclerView.setAdapter(new LayerImageAdapter(this, mLevelOneItemPos));
        }

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
                            mTts.speak(myMusic[position], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelTwoItemPos == position && mLevelOneItemPos != MENU_ITEM_HELP){
                            Intent intent = new Intent(Main2LAyer.this, Layer3Activity.class);
                            if(mSession.getLanguage() == LANG_HINDI)
                                intent = new Intent(Main2LAyer.this, Layer_3_Hindi_Activity.class);
                            if(mLevelOneItemPos == MENU_ITEM_DAILY_ACT && ( mLevelTwoItemPos == 0 ||  mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8 ))
                                intent = new Intent(Main2LAyer.this, Sequence_Activity.class);
                            intent.putExtra("mLevelOneItemPos", mLevelOneItemPos);
                            intent.putExtra("mLevelTwoItemPos", mLevelTwoItemPos);
                            intent.putExtra("selectedMenuItemPath", mActionBarTitle);
                            startActivity(intent);
                        }else if(mLevelOneItemPos == MENU_ITEM_PEOPLE || mLevelOneItemPos == MENU_ITEM_PLACES){
                            mTts.speak(myMusic[sort[mLevelTwoItemPos]], TextToSpeech.QUEUE_FLUSH, null);
                        }else {
                            mTts.speak(myMusic[position], TextToSpeech.QUEUE_FLUSH, null);
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
                        userDataMeasure.recordGridItem(String.valueOf(mLevelOneItemPos));
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

        if (mSession.getLanguage() == LANG_ENG){
            people = people_english;
            end =  "";
        }else {
            people = people_hindi;
            end = " है।";
        }

        if (mLevelOneItemPos == MENU_ITEM_PEOPLE) {
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
            Integer[] indexes = new Integer[layer_2_speech[mLevelOneItemPos].length];
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                ++g;
            }
            for (int j = 0; j < new_people_count.length; ++j)
                sort[j] = indexes[j];
            for (int j = 0; j < new_people_count.length; ++j) {
                new_people[j] = people[sort[j]];
                if (mSession.getLanguage() == LANG_ENG)
                    new_people_adapter[j] = people_adapter[sort[j]];
                else
                    new_people_adapter[j] = people_adapter_hindi[sort[j]];
            }
            temp = Arrays.copyOfRange(new_people_adapter, 0, new_people_count.length);
            image_temp = Arrays.copyOfRange(new_people, 0, new_people_count.length);
            mRecyclerView.setAdapter(new Adapter_ppl_places(this, temp, image_temp));
            myMusic = new String[temp.length];
            if(mSession.getLanguage() == LANG_ENG) myMusic = sortTtsSpeechArray(peopleSpeechTextEnglish, MENU_ITEM_PEOPLE);
            else myMusic = sortTtsSpeechArray(peopleSpeechTextHindi, MENU_ITEM_PEOPLE);
        }else if (mLevelOneItemPos == MENU_ITEM_PLACES) {
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
                if (mSession.getLanguage() == LANG_ENG)
                    new_places_adapter[j] = places_adapter[sort_places[j]];
                else
                    new_places_adapter[j] = places_adapter_hindi[sort_places[j]];
            }
            if (mLevelOneItemPos == MENU_ITEM_PLACES) {
                temp = Arrays.copyOfRange(new_places_adapter, 0, new_places_count.length);
                image_temp = Arrays.copyOfRange(new_places, 0, new_places_count.length);
                mRecyclerView.setAdapter(new Adapter_ppl_places(this, temp, image_temp));
                myMusic = new String[temp.length];
                if(mSession.getLanguage() == LANG_ENG) myMusic = sortTtsSpeechArray(placesSpeechTextEnglish, MENU_ITEM_PLACES);
                else myMusic = sortTtsSpeechArray(placesSpeechTextHindi, MENU_ITEM_PLACES);
            }
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setImageResource(R.drawable.homepressed);
                mTts.speak(below[0], TextToSpeech.QUEUE_FLUSH, null);
                finish();
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(below[2], TextToSpeech.QUEUE_FLUSH, null);
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

        ttsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mTts.setSpeechRate((float) mSession.getSpeed() / 50);
                        mTts.setPitch((float) mSession.getPitch() / 50);
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

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mTts.speak(below[1], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[1], TextToSpeech.QUEUE_FLUSH, null);         //if pressing like for second time then says like very much
                        mCk = 0;
                    } else {
                        mTts.speak(side[0], TextToSpeech.QUEUE_FLUSH, null);         //if pressing like for first time says i like iin lang specified for mSession
                        mCk = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCk == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][1], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][1], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][1]+ mSession.getName()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;
                    } else {
                        if(mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][0], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][0], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                             mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][0]+ mSession.getName()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][0], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        mTts.speak(side[6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCd == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][7], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][7], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][7]+ mSession.getFather_name()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][6], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][6], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][6]+ mSession.getFather_name()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][6], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTts.speak(side[2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                } else  {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][3], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][3], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                             mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][3]+ mSession.getEmailId()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][2], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][2], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][2]+ mSession.getEmailId()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][2], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        mTts.speak(side[8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCn == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][9], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][9], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][9]+ mSession.getAddress()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][8], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][8], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][8]+ mSession.getAddress()+end,TextToSpeech.QUEUE_FLUSH,null) ;
                        else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][8], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        mTts.speak(side[4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCm == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][5], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][5], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0) {
                            mTts.setLanguage(new Locale("eng", "IND"));
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][5] + mSession.getFather_no().replaceAll("\\B", " ") + end, TextToSpeech.QUEUE_FLUSH, null);
                            mTts.setLanguage(new Locale("hin", "IND"));
                        } else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][4], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][4], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            mTts.setLanguage(new Locale("eng", "IND"));
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][5] + mSession.getFather_no().replaceAll("\\B", " ") + end, TextToSpeech.QUEUE_FLUSH, null);
                            mTts.setLanguage(new Locale("hin", "IND"));
                        } else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][4], TextToSpeech.QUEUE_FLUSH, null);
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
                        mTts.speak(side[11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        mTts.speak(side[10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCl == 1) {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][11], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][11], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            switch(mSession.getBlood()){
                                case  0: mBloodGroup ="A positive"; break;
                                case  1: mBloodGroup ="A negative"; break;
                                case  2: mBloodGroup ="B positive"; break;
                                case  3: mBloodGroup ="B negative"; break;
                                case  4: mBloodGroup ="A B positive"; break;
                                case  5: mBloodGroup ="A B negative"; break;
                                case  6: mBloodGroup ="O positive"; break;
                                case  7: mBloodGroup ="O negative"; break;
                            }
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][10] + mBloodGroup + end, TextToSpeech.QUEUE_FLUSH, null);
                        } else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        if (mLevelOneItemPos == 5)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort[mLevelTwoItemPos]][10], TextToSpeech.QUEUE_FLUSH, null);
                        else if (mLevelOneItemPos == 6)
                            mTts.speak(layer_2_speech[mLevelOneItemPos][sort_places[mLevelTwoItemPos]][10], TextToSpeech.QUEUE_FLUSH, null);
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 0){
                            switch(mSession.getBlood()){
                                case  0: mBloodGroup ="A positive"; break;
                                case  1: mBloodGroup ="A negative"; break;
                                case  2: mBloodGroup ="B positive"; break;
                                case  3: mBloodGroup ="B negative"; break;
                                case  4: mBloodGroup ="A B positive"; break;
                                case  5: mBloodGroup ="A B negative"; break;
                                case  6: mBloodGroup ="O positive"; break;
                                case  7: mBloodGroup ="O negative"; break;
                            }
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][10] + mBloodGroup + end, TextToSpeech.QUEUE_FLUSH, null);
                        } else
                            mTts.speak(layer_2_speech[mLevelOneItemPos][mLevelTwoItemPos][10], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 1;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int LANG_HINDI = 1;
        super.onCreateOptionsMenu(menu);
        if (mSession.getLanguage() == LANG_HINDI){
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_main, menu);
        }else{
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_1, menu);
        }
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

    private void loadArraysFromResources() {
        mColor = getResources().getIntArray(R.array.arrActionBtnColors);
        /*greetSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextEnglish);
        dailySpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextEnglish);
        eatSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextEnglish);
        funSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextEnglish);
        learningSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextEnglish);*/
        peopleSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechTextEnglish);
        placesSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechTextEnglish);
        /*timeWeatherSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextEnglish);
        helpSpeechTextEnglish = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextEnglish);
        greetSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextHindi);
        dailySpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextHindi);
        eatSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextHindi);
        funSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextHindi);
        learningSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextHindi);*/
        peopleSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechTextHindi);
        placesSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechTextHindi);
        /*timeWeatherSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextHindi);
        helpSpeechTextHindi = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextHindi);*/

        places_adapter = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterTextEnglish);
        people_adapter = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterTextEnglish);
        people_adapter_hindi = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterTextHindi);
        places_adapter_hindi = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterTextHindi);

        side_english = getResources().getStringArray(R.array.arrSideEnglish);
        side_hindi = getResources().getStringArray(R.array.arrSideHindi);
        below_english = getResources().getStringArray(R.array.arrBelowEnglish);
        below_hindi = getResources().getStringArray(R.array.arrBelowHindi);
    }

    private void resetRecyclerMenuItemsAndFlags() {
        resetActionButtons(6);
        mLevelTwoItemPos = -1;
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
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
        }
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
        if (levelOneItemPos == 0){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterTextHindi);
            }
        } else if (levelOneItemPos == 1) {
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterTextHindi);
            }
        } else if (levelOneItemPos == 2){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoEatAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoEatSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoEatAdapterTextHindi);
            }
        } else if (levelOneItemPos == 3){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoFunAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoFunSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoEatAdapterTextHindi);
            }
        } else if (levelOneItemPos == 4){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoLearningAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoLearningAdapterTextHindi);
            }
        }else if (levelOneItemPos == MENU_ITEM_PEOPLE){
            if (mSession.getLanguage() == LANG_ENG)
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterTextEnglish);
            else
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterTextHindi);

        } else if (levelOneItemPos == MENU_ITEM_PLACES){
            if (mSession.getLanguage() == LANG_ENG)
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterTextEnglish);
            else
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterTextHindi);
        }else if (levelOneItemPos == 7){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterTextHindi);
            }
        } else if (levelOneItemPos == 8){
            if (mSession.getLanguage() == LANG_ENG) {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextEnglish);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoHelpAdapterTextEnglish);
            }else {
                myMusic = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechTextHindi);
                actionBarText = getResources().getStringArray(R.array.arrLevelTwoHelpAdapterTextHindi);
            }
        }
    }

    private class ArrayIndexComparator implements Comparator<Integer>{
        private final Integer[] array;
        public ArrayIndexComparator(Integer[] array)
        {
            this.array = array;
        }
        public Integer[] createIndexArray(){
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
                mTts.setLanguage(new Locale("hin", "IND"));
            } catch (Exception e) {
                Thread.interrupted();
            }
            return "Executed";
        }
    }

    private final Integer[] people_english = {
            R.drawable.level2_people_mom, R.drawable.level2_people_dad,
            R.drawable.level2_people_brother, R.drawable.level2_people_sister,
            R.drawable.level2_people_grandfather,R.drawable.level2_people_grandmother, R.drawable.level2_people_uncle, R.drawable.level2_people_aunt,
            R.drawable.level2_people_cousin, R.drawable.level2_people_baby,
            R.drawable.level2_people_friend, R.drawable.level2_people_teacher,
            R.drawable.level2_people_doctor,R.drawable.level2_people_nurse, R.drawable.level2_people_caregiver1,
            R.drawable.level2_people_stranger, R.drawable.level2_people_aboutme
    };

    private final Integer[] people_hindi = {
            R.drawable.level2_people_mom, R.drawable.level2_people_dad,
            R.drawable.level2_people_brother, R.drawable.level2_people_sister,R.drawable.level2_people_badepapa,R.drawable.level2_people_badimom,
            R.drawable.level2_people_grandfather,R.drawable.level2_people_grandmother,
            R.drawable.level2_people_nanaji, R.drawable.level2_people_nanima,R.drawable.level2_people_uncle, R.drawable.level2_people_aunt,
            R.drawable.level2_people_mama, R.drawable.level2_people_mami, R.drawable.level2_people_bua, R.drawable.level2_people_fufa,
            R.drawable.level2_people_mausi, R.drawable.level2_people_mausa,
            R.drawable.level2_people_baby,
            R.drawable.level2_people_friend, R.drawable.level2_people_teacher,
            R.drawable.level2_people_doctor,  R.drawable.level2_people_nurse, R.drawable.level2_people_caregiver1,
            R.drawable.level2_people_stranger, R.drawable.level2_people_aboutme
    };

    private final Integer[] places = {
            R.drawable.level2_places_home, R.drawable.level2_places_school1,
            R.drawable.level2_places_mall, R.drawable.level2_places_museum1,
            R.drawable.level2_places_restraunt, R.drawable.level2_places_theatre,
            R.drawable.level2_places_playground, R.drawable.level2_places_park,
            R.drawable.level2_places_store, R.drawable.level2_places_friend_house,
            R.drawable.level2_places_relative_house, R.drawable.level2_places_hospital,
            R.drawable.level2_places_clinic, R.drawable.level2_places_library, R.drawable.level2_places_terr
    };

    private String[][][] layer_2_speech_english = {{{
            "I like to greet others",
            "I really like to greet others",
            "I want to greet others",
            "I really want to greet others",
            "I want to greet some more",
            "I really want to greet some more",
            "I don’t like to greet others",
            "I really don’t like to use greet others",
            "I don’t want to greet others",
            "I really don’t want to greet others",
            "I don’t want to greet any more",
            "I really don’t want to greet any more",

    },{"I like to talk about my feelings",
            "I really like to talk about my feelings",
            "I want to talk about my feelings",
            "I really want to talk about my feelings",
            "I want to talk some more about my feelings",
            "I really want to talk some more about my feelings",
            "I don’t like to talk about my feelings",
            "I really don’t like to talk about my feelings",
            "I don’t want to talk about my feelings",
            "I really don’t want to talk about my feelings",
            "I don’t want to talk more about my feelings",
            "I really don’t want to talk any more about my feelings"

    },{"I like to request",
            "I really like to request",
            "I want to request something",
            "I really want to request something",
            "I want to request something more",
            "I really want to request some more",
            "I don’t like to request",
            "I really don’t like to request",
            "I don’t want to request anything",
            "I really don’t want to request anything",
            "I don’t want to request any more",
            "I really don’t want to request any more"

    },{"I like to ask questions",
            "I really like to ask questions",
            "I want to ask a question",
            "I really want to ask a question",
            "I want to ask more questions",
            "I really want to ask some more questions",
            "I don’t like to ask questions",
            "I really don’t like to ask questions",
            "I don’t want to ask a question",
            "I really don’t want to ask a question",
            "I don’t want to ask more questions",
            "I really don’t want to ask any more questions"


    }},{{"I like to brush my teeth",
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

    },{"I like to go to the toilet",
            "I really like to go to the toilet",
            "I want to go to the toilet",
            "I really want to go to the toilet",
            "I want to go to the toilet again",
            "I really want to go to the toilet again",
            "I don’t like to go to the toilet",
            "I really don’t like to go to the toilet",
            "I don’t want to go to the toilet",
            "I really don’t want to go to the toilet",
            "I don’t want to go to the toilet again",
            "I really don’t want to go to the toilet again"

    },{"I like to bathe",
            "I really like to bathe ",
            "I want to bathe ",
            "I really want to bathe ",
            "I want to bathe again",
            "I really want to bathe again",
            "I don’t like to bathe ",
            "I really don’t like to bathe ",
            "I don’t want to bathe ",
            "I really don’t want to bathe ",
            "I don’t want to bathe again",
            "I really don’t want to bathe again"

    },{"I like clothes and accessories",
            "I really like clothes and accessories",
            "I want some clothes and accessories",
            "I really want some clothes and accessories",
            "I want more clothes and accessories",
            "I really want some more clothes and accessories",
            "I don’t like clothes and accessories",
            "I really don’t like clothes and accessories",
            "I don’t want clothes and accessories",
            "I really don’t want any clothes and accessories",
            "I don’t want more clothes and accessories",
            "I really don’t want any more clothes and accessories"

    },{"I like to get ready",
            "I really like to get ready",
            "I want to get ready",
            "I really want to get ready",
            "I need more time to get ready",
            "I really need some more time to get ready",
            "I don’t like to get ready",
            "I really don’t like to get ready",
            "I don’t want to get ready",
            "I really don’t want to get ready",
            "I don’t need more time to get ready",
            "I really don’t need any more time to get ready"

    },{"I like to sleep",
            "I really like to sleep",
            "I want to sleep",
            "I really want to sleep",
            "I want to sleep some more",
            "I really want to sleep for some more time",
            "I don’t like to sleep",
            "I really don’t like to sleep",
            "I don’t want to sleep",
            "I really don’t want to sleep",
            "I don’t want to sleep any more",
            "I really don’t want to sleep any more now"

    },{"I like to go for therapy",
            "I really like to go for therapy",
            "I want to go for therapy",
            "I really want to go for therapy",
            "I want to go for therapy again",
            "I really want to go for therapy again",
            "I don’t like to go for therapy",
            "I really don’t like to go for therapy",
            "I don’t want to go for therapy",
            "I really don’t want to go for therapy",
            "I don’t want to go for therapy again",
            "I really don’t want to go for therapy again"

    },{"I like to follow my morning routine",
            "I really like to follow my morning routine",
            "I want to know my morning routine",
            "I really want to know my morning routine",
            "I want to know more about my morning routine",
            "I really want to know some more about my morning routine",
            "I don’t like to follow my morning routine",
            "I really don’t like to follow my morning routine",
            "I don’t want to know my morning routine",
            "I really don’t want to know my morning routine",
            "I don’t want to know more about my morning routine",
            "I really don’t want to know any more about my morning routine"

    },{"I like to follow my bedtime routine",
            "I really like to follow my bedtime routine",
            "I want to know my bedtime routine",
            "I really want to know my bedtime routine",
            "I want to know more about my bedtime routine",
            "I really want to know some more about my bedtime routine",
            "I don’t like to follow my bedtime routine",
            "I really don’t like to follow my bedtime routine",
            "I don’t want to know my bedtime routine",
            "I really don’t want to know my bedtime routine",
            "I don’t want to know more about my bedtime routine",
            "I really don’t want to know any more about my bedtime routine"


    }},{{"I like to have brekfust",
            "I really like to have brekfust",
            "I want to have brekfust",
            "I really want to have brekfust",
            "I want to have more brekfust",
            "I really want to have some more brekfust",
            "I don’t like to have brekfust",
            "I really don’t like to have brekfust",
            "I don’t want to have brekfust",
            "I really don’t want to have brekfust",
            "I don’t want any more brekfust",
            "I really don’t want any more brekfust"

    },{"I like to have food",
            "I really like to have food",
            "I want to have some food",
            "I really want to have some food",
            "I want more food",
            "I really want some more food",
            "I don’t like to have food",
            "I really don’t like to have food",
            "I don’t want to have food",
            "I really don’t want any food",
            "I don’t want more food",
            "I really don’t want any more food"

    },{"I like sweets",
            "I really like sweets",
            "I want to have some sweets",
            "I really want to have some sweets",
            "I want more sweets",
            "I really want some more sweets",
            "I don’t like sweets",
            "I really don’t like sweets",
            "I don’t want to have sweets",
            "I really don’t want to have sweets",
            "I don’t want more sweets",
            "I really don’t want any more sweets"

    },{"I like स्नैक्स",
            "I really like स्नैक्स",
            "I want to have some स्नैक्स",
            "I really want to have some स्नैक्स",
            "I want more स्नैक्स ",
            "I really want some more स्नैक्स",
            "I don’t like स्नैक्स",
            "I really don’t like स्नैक्स",
            "I don’t want to have स्नैक्स",
            "I really don’t want to have स्नैक्स",
            "I don’t want more स्नैक्स",
            "I really don’t want any more स्नैक्स",

    },{"I like fruits",
            "I really like fruits",
            "I want to have some fruits",
            "I really want to have some fruits",
            "I want more fruits ",
            "I really want some more fruits",
            "I don’t like fruits",
            "I really don’t like fruits",
            "I don’t want to have fruits",
            "I really don’t want to have fruits",
            "I don’t want more fruits",
            "I really don’t want any more fruits"

    },{"I am thirsty",
            "I am really very thirsty",
            "I want to drink something",
            "I really want to drink something",
            "I am very thirsty",
            "I am really very thirsty",
            "I am not thirsty",
            "I am really not thirsty",
            "I don’t want to drink anything",
            "I really don’t want to drink anything",
            "I am not thirsty at all",
            "I am really not thirsty any more"

    },{"I like to use cutlery",
            "I really like to use cutlery",
            "I want some cutlery",
            "I really want some cutlery",
            "I want some more cutlery ",
            "I really want some more cutlery",
            "I don’t like to use cutlery",
            "I really don’t like to use cutlery",
            "I don’t want cutlery",
            "I really don’t want any cutlery",
            "I don’t want more cutlery ",
            "I really don’t want any more cutlery"

    },{"I like to use add-ons",
            "I really like to use add-ons",
            "I want to use some add-ons",
            "I really want to use some add-ons",
            "I want more add-ons",
            "I really want some more add-ons",
            "I don’t like to use add-ons",
            "I really don’t like to use add-ons",
            "I don’t want to use add-ons",
            "I really don’t want to use add-ons",
            "I don’t want more add-ons",
            "I really don’t want any more add-ons"


    }},{{"I like to play indoor games",
            "I really like to play indoor games",
            "I want to play some indoor games",
            "I really want to play some indoor games",
            "I want to play some more indoor games",
            "I really want to play indoor games some more",
            "I don’t like to play indoor games",
            "I really don’t like to play indoor games",
            "I don’t want to play indoor games",
            "I really don’t want to play indoor games",
            "I don’t want to play any more indoor games",
            "I really don’t want to play indoor games any more"

    },{"I like to play outdoor games",
            "I really like to play outdoor games",
            "I want to play some outdoor games",
            "I really want to play some outdoor games",
            "I want to play some more outdoor games",
            "I really want to play outdoor games some more",
            "I don’t like to play outdoor games",
            "I really don’t like to play outdoor games",
            "I don’t want to play outdoor games",
            "I really don’t want to play outdoor games",
            "I don’t want to play any more outdoor games",
            "I really don’t want to play outdoor games any more"

    },{"I like sports",
            "I really like sports",
            "I want to play sports",
            "I really want to play sports",
            "I want to play some more sports",
            "I really want to play sports some more",
            "I don’t like sports",
            "I really don’t like sports",
            "I don’t want to play sports",
            "I really don’t want to play sports",
            "I don’t want to play any more sports",
            "I really don’t want to play sports any more",

    },{"I like to watch TV",
            "I really like to watch TV",
            "I want to watch TV",
            "I really want to watch TV",
            "I want to watch some more TV",
            "I really want to watch TV some more",
            "I don’t like to watch TV",
            "I really don’t like to watch TV",
            "I don’t want to watch TV",
            "I really don’t want to watch TV",
            "I don’t want to watch any more TV",
            "I really don’t want to watch TV any more"

    },{"I like to listen to music",
            "I really like to listen to music",
            "I want to listen to some music",
            "I really want to listen to some music",
            "I want to listen to some more music",
            "I really want to listen to music some more",
            "I don’t like to listen to music",
            "I really don’t like to listen to music",
            "I don’t want to listen to music",
            "I really don’t want to listen to music",
            "I don’t want to listen to any more music",
            "I really don’t want to listen to music any more"

    },{"I like to do different activities",
            "I really like to do different activities",
            "I want to do different activities",
            "I really want to do different activities",
            "I want to do more activities",
            "I really want to do some more activities",
            "I don’t like to do different activities",
            "I really don’t like to do different activities",
            "I don’t want to do different activities",
            "I really don’t want to do different activities",
            "I don’t want to do more activities",
            "I really don’t want to do anymore activities"


    }},{{"I like animals and birds",
            "I really like animals and birds",
            "I want to learn about animals and birds",
            "I really want to learn about animals and birds",
            "I want to learn more about animals and birds",
            "I really want to learn some more about animals and birds",
            "I don’t like animals and birds",
            "I really don’t like animals and birds",
            "I don’t want to learn about animals and birds",
            "I really don’t want to learn about animals and birds",
            "I don’t want to learn more about animals and birds",
            "I really don’t want to learn any more about animals and birds"

    },{"I like my body",
            "I really like my body",
            "I want to learn about my body",
            "I really want to learn about my body",
            "I want to learn more about my body",
            "I really want to learn some more about my body",
            "I don’t like my body",
            "I really don’t like my body",
            "I don’t want to learn about my body",
            "I really don’t want to learn about my body",
            "I don’t want to learn more about my body",
            "I really don’t want to learn any more about my body",

    },{"I like books",
            "I really like books",
            "I want to read books",
            "I really want to read books",
            "I want to read more books",
            "I really want to read some more books",
            "I don’t like books",
            "I really don’t like books",
            "I don’t want to read books",
            "I really don’t want to read books",
            "I don’t want to read more books",
            "I really don’t want to read any more books",

    },{"I like colours",
            "I really like colours",
            "I want to learn about colours",
            "I really want to learn about colours",
            "I want to learn more about colours",
            "I really want to learn some more about colours",
            "I don’t like colours",
            "I really don’t like colours",
            "I don’t want to learn about colours",
            "I really don’t want to learn about colours",
            "I don't want to learn more about colours",
            "I really don’t want to learn any more about colours",

    },{"I like shapes",
            "I really like shapes",
            "I want to learn about shapes",
            "I really want to learn about shapes",
            "I want to learn more about shapes",
            "I really want to learn some more about shapes",
            "I don’t like shapes",
            "I really don’t like shapes",
            "I don’t want to learn about shapes",
            "I really don’t want to learn about shapes",
            "I don't want to learn more about shapes",
            "I really don’t want to learn any more about shapes",

    },{"I like to use stationery",
            "I really like to use stationery",
            "I want some stationery",
            "I really want some stationery",
            "I want more stationery",
            "I really want some more stationery",
            "I don’t like to use stationery",
            "I really don’t like to use stationery",
            "I don’t want stationery",
            "I really don’t want any stationery",
            "I don’t want more stationery",
            "I really don’t want any more stationery",

    },{"I like to use school objects",
            "I really like to use school objects",
            "I want to learn about school objects",
            "I really want to learn about school objects",
            "I want to learn more about school objects",
            "I really want to learn some more about school objects",
            "I don’t like to use school objects",
            "I really don’t like to use school objects",
            "I don’t want to learn about school objects",
            "I really don’t want to learn about school objects",
            "I don’t want to learn more about school objects",
            "I really don’t want to learn any more about school objects"

    },{"I like to use different home objects",
            "I really like to use different home objects",
            "I want to learn about home objects",
            "I really want to learn about home objects",
            "I want to learn more about home objects",
            "I really want to learn some more about home objects",
            "I don’t like to use different home objects",
            "I really don’t like to use different home objects",
            "I don’t want to learn about home objects",
            "I really don’t want to learn about home objects",
            "I don't want to learn more about home objects",
            "I really don’t want to learn any more about home objects"

    },{"I like to use different tran sport modes",
            "I really like to use different tran sport modes",
            "I want to learn about tran sport modes",
            "I really want to learn about tran sport modes",
            "I want to learn more about tran sport modes",
            "I really want to learn some more about tran sport modes",
            "I don’t like to use different tran sport modes",
            "I really don’t like to use different tran sport modes",
            "I don’t want to learn about tran sport modes",
            "I really don’t want to learn about tran sport modes",
            "I don't want to learn more about tran sport modes",
            "I really don’t want to learn any more about tran sport modes",


    }},{{"I love my mother",
            "I really love my mother",
            "I want my mother",
            "I really want my mother",
            "I want to spend more time with my mother",
            "I really want to spend much more time with my mother",
            "I don’t like my mother",
            "I really don’t like my mother",
            "I don’t want my mother",
            "I really don’t want my mother",
            "I don’t want to spend more time with my mother",
            "I really don’t want to spend any more time with my mother"

    },{"I love my father",
            "I really love my father",
            "I want my father",
            "I really want my father",
            "I want to spend more time with my father",
            "I really want to spend much more time with my father",
            "I don’t like my father",
            "I really don’t like my father",
            "I don’t want my father",
            "I really don’t want my father",
            "I don’t want to spend more time with my father",
            "I really don’t want to spend any more time with my father"

    },{"I love my brother",
            "I really love my brother",
            "I want my brother",
            "I really want my brother",
            "I want to spend more time with my brother",
            "I really want to spend much more time with my brother",
            "I don’t like my brother",
            "I really don’t like my brother",
            "I don’t want my brother",
            "I really don’t want my brother",
            "I don’t want to spend more time with my brother",
            "I really don’t want to spend any more time with my brother",

    },{"I love my sister",
            "I really love my sister",
            "I want my sister",
            "I really want my sister",
            "I want to spend more time with my sister",
            "I really want to spend much more time with my sister",
            "I don’t like my sister",
            "I really don’t like my sister",
            "I don’t want my sister",
            "I really don’t want my sister",
            "I don’t want to spend more time with my sister",
            "I really don’t want to spend any more time with my sister"

    },{"I love my grandfather",
            "I really love my grandfather",
            "I want my grandfather",
            "I really want my grandfather",
            "I want to spend more time with my grandfather",
            "I really want to spend much more time with my grandfather",
            "I don’t like my grandfather",
            "I really don’t like my grandfather",
            "I don’t want my grandfather",
            "I really don’t want my grandfather",
            "I don’t want to spend more time with my grandfather",
            "I really don’t want to spend any more time with my grandfather"

    },{"I love my grandmother",
            "I really love my grandmother",
            "I want my grandmother",
            "I really want my grandmother",
            "I want to spend more time with my grandmother",
            "I really want to spend much more time with my grandmother",
            "I don’t like my grandmother",
            "I really don’t like my grandmother",
            "I don’t want my grandmother",
            "I really don’t want my grandmother",
            "I don’t want to spend more time with my grandmother",
            "I really don’t want to spend any more time with my grandmother"

    },{"I love my uncle",
            "I really love my uncle",
            "I want my uncle",
            "I really want my uncle",
            "I want to spend more time with my uncle",
            "I really want to spend much more time with my uncle",
            "I don’t like my uncle",
            "I really don’t like my uncle",
            "I don’t want my uncle",
            "I really don’t want my uncle",
            "I don’t want to spend more time with my uncle",
            "I really don’t want to spend any more time with my uncle"

    },{"I love my aunt",
            "I really love my aunt",
            "I want my aunt",
            "I really want my aunt",
            "I want to spend more time with my aunt",
            "I really want to spend much more time with my aunt",
            "I don’t like my aunt",
            "I really don’t like my aunt",
            "I don’t want my aunt",
            "I really don’t want my aunt",
            "I don’t want to spend more time with my aunt",
            "I really don’t want to spend any more time with my aunt"

    },{"I love my cousin",
            "I really love my cousin",
            "I want my cousin",
            "I really want my cousin",
            "I want to spend more time with my cousin",
            "I really want to spend much more time with my cousin",
            "I don’t like my cousin",
            "I really don’t like my cousin",
            "I don’t want my cousin",
            "I really don’t want my cousin",
            "I don’t want to spend more time with my cousin",
            "I really don’t want to spend any more time with my cousin"

    },{"I love babies",
            "I really love babies",
            "I want to hold the baby",
            "I really want to hold the baby",
            "I want to spend more time with the baby",
            "I really want to spend much more time with the baby",
            "I don’t like babies",
            "I really don’t like babies",
            "I don’t want to hold the baby",
            "I really don’t want to hold the baby",
            "I don’t want to spend more time with the baby",
            "I really don’t want to spend any more time with the baby"

    },{"I love my friends",
            "I really love my friends",
            "I want my friends",
            "I really want my friends",
            "I want to spend more time with my friends",
            "I really want to spend much more time with my friends",
            "I don’t like my friends",
            "I really don’t like my friends",
            "I don’t want my friends",
            "I really don’t want my friends",
            "I don’t want to spend more time with my friends",
            "I really don’t want to spend any more time with my friends",

    },{"I like my teacher",
            "I really like my teacher",
            "I want my teacher",
            "I really want my teacher",
            "I want to spend more time with my teacher",
            "I really want to spend much more time with my teacher",
            "I don’t like my teacher",
            "I really don’t like my teacher",
            "I don’t want my teacher",
            "I really don’t want my teacher",
            "I don’t want to spend more time with my teacher",
            "I really don’t want to spend any more time with my teacher"

    },{"I like the doctor",
            "I really like the doctor",
            "I want to go to the doctor",
            "I really want to go to the doctor",
            "I want to spend more time with the doctor",
            "I really want to spend much more time with the doctor",
            "I don’t like the doctor",
            "I really don’t like the doctor",
            "I don’t want to go to the doctor",
            "I really don’t want to go to the doctor",
            "I don’t want to spend more time with the doctor",
            "I really don’t want to spend any more time with the doctor"

    },{"I like the नरस",
            "I really like the नरस",
            "I want to go to the नरस",
            "I really want to go to the नरस",
            "I want to spend more time with the नरस",
            "I really want to spend much more time with the नरस",
            "I don’t like the नरस",
            "I really don’t like the नरस",
            "I don’t want to go to the नरस",
            "I really don’t want to go to the नरस",
            "I don’t want to spend more time with the नरस",
            "I really don’t want to spend any more time with the नरस"

    },{"I like my caregiver",
            "I really like my caregiver",
            "I want my caregiver",
            "I really want my caregiver",
            "I want to spend more time with my caregiver",
            "I really want to spend much more time with my caregiver",
            "I don’t like my caregiver",
            "I really don’t like my caregiver",
            "I don’t want my caregiver",
            "I really don’t want my caregiver",
            "I don’t want to spend more time with my caregiver",
            "I really don’t want to spend any more time with my caregiver"

    },{"I like that stranger",
            "I really like that stranger",
            "I want to talk to that stranger",
            "I really want to talk to that stranger",
            "I want to spend more time with that stranger",
            "I really want to spend much more time with that stranger",
            "I don’t like that stranger",
            "I really don’t like that stranger",
            "I don’t want to talk to that stranger",
            "I really don’t want to talk to that stranger",
            "I don’t want to spend more time with that stranger",
            "I really don’t want to spend any more time with that stranger"

    },{"I like myself",
            "I really like myself",
            "I want to tell you about myself",
            "I really want to tell you about myself",
            "I want to tell you some more about myself",
            "I really want to tell you much more about myself",
            "I don’t like myself",
            "I really don’t like myself",
            "I don’t want to tell you about myself",
            "I really don’t want to tell you about myself",
            "I don’t want to tell you more about myself",
            "I really don’t want to tell you any more about myself"


    }},{{"I like my house",
            "I really like my house",
            "I want to go to my house",
            "I really want to go to my house",
            "I want to go to my house again",
            "I really want to go to my house again",
            "I don’t like my house",
            "I really don’t like my house",
            "I don’t want to go to my house",
            "I really don’t want to go to my house",
            "I don’t want to go to my house again",
            "I really don’t want to go to my house again"

    },{"I like my school",
            "I really like my school",
            "I want to go to school",
            "I really want to go to school",
            "I want to go to school again",
            "I really want to go to school again",
            "I don’t like my school",
            "I really don’t like my school",
            "I don’t want to go to school",
            "I really don’t want to go to school",
            "I don’t want to go to school again",
            "I really don’t want to go to school again"

    },{"I like to go to the mall",
            "I really like to go to the mall",
            "I want to go to the mall",
            "I really want to go to the mall",
            "I want to go again to the mall",
            "I really want to go to the mall again",
            "I don’t like to go to the mall",
            "I really don’t like to go to the mall",
            "I don’t want to go to the mall",
            "I really don’t want to go to the mall",
            "I don’t want to go to the mall again",
            "I really don’t want to go to the mall again"

    },{"I like to go to the museum",
            "I really like to go to the museum",
            "I want to go to the museum",
            "I really want to go to the museum",
            "I want to go to the museum again",
            "I really want to go to the museum again",
            "I don’t like to go to the museum",
            "I really don’t like to go to the museum",
            "I don’t want to go to the museum",
            "I really don’t want to go to the museum",
            "I don’t want to go to the museum again",
            "I really don’t want to go to the museum again"

    },{"I like to go to a hotel",
            "I really like to go to a hotel",
            "I want to go to a hotel",
            "I really want to go to a hotel",
            "I want to go to a hotel again",
            "I really want to go to a hotel again",
            "I don’t like to go to a hotel",
            "I really don’t like to go to a hotel",
            "I don’t want to go to a hotel",
            "I really don’t want to go to a hotel",
            "I don’t want to go to a hotel again",
            "I really don’t want to go to a hotel again"

    },{"I like to go to the theatre",
            "I really like to go to the theatre",
            "I want to go to the theatre",
            "I really want to go to the theatre",
            "I want to go to the theatre again",
            "I really want to go to the theatre again",
            "I don’t like to go to the theatre",
            "I really don’t like to go to the theatre",
            "I don’t want to go to the theatre",
            "I really don’t want to go to the theatre",
            "I don’t want to go to the theatre again",
            "I really don’t want to go to the theatre again"

    },{"I like to go to the playground",
            "I really like to go to the playground",
            "I want to go to the playground",
            "I really want to go to the playground",
            "I want to go to the playground again",
            "I really want to go to the playground again",
            "I don’t like to go to the playground",
            "I really don’t like to go to the playground",
            "I don’t want to go to the playground",
            "I really don’t want to go to the playground",
            "I don’t want to go to the playground again",
            "I really don’t want to go to the playground again"

    },{"I like to go to the park",
            "I really like to go to the park",
            "I want to go to the park",
            "I really want to go to the park",
            "I want to go to the park again",
            "I really want to go to the park again",
            "I don’t like to go to the park",
            "I really don’t like to go to the park",
            "I don’t want to go to the park",
            "I really don’t want to go to the park",
            "I don’t want to go to the park again",
            "I really don’t want to go to the park again"

    },{"I like to go to the store",
            "I really like to go to the store",
            "I want to go to the store",
            "I really want to go to the store",
            "I want to go to the store again",
            "I really want to go to the store again",
            "I don’t like to go to the store",
            "I really don’t like to go to the store",
            "I don’t want to go to the store",
            "I really don’t want to go to the store",
            "I don’t want to go to the store again",
            "I really don’t want to go to the store again"

    },{"I like to go to my friend’s house",
            "I really like to go to my friend’s house",
            "I want to go to my friend’s house",
            "I really want to go to my friend’s house",
            "I want to go to my friend’s house again",
            "I really want to go to my friend’s house again",
            "I don’t like to go to my friend’s house",
            "I really don’t like to go to my friend’s house",
            "I don’t want to go to my friend’s house",
            "I really don’t want to go to my friend’s house",
            "I don’t want to go to my friend’s house again",
            "I really don’t want to go to my friend’s house again"

    },{"I like to go to my relative’s house",
            "I really like to go to my relative’s house",
            "I want to go to my relative’s house",
            "I really want to go to my relative’s house",
            "I want to go to my relative’s house again",
            "I really want to go to my relative’s house again",
            "I don’t like to go to my relative’s house",
            "I really don’t like to go to my relative’s house",
            "I don’t want to go to my relative’s house",
            "I really don’t want to go to my relative’s house",
            "I don’t want to go to my relative’s house again",
            "I really don’t want to go to my relative’s house again"

    },{"I like to go to the hospital",
            "I really like to go to the hospital",
            "I want to go to the hospital",
            "I really want to go to the hospital",
            "I want to go to the hospital again",
            "I really want to go to the hospital again",
            "I don’t like to go to the hospital",
            "I really don’t like to go to the hospital",
            "I don’t want to go to the hospital",
            "I really don’t want to go to the hospital",
            "I don’t want to go to the hospital again",
            "I really don’t want to go to the hospital again"

    },{"I like to go to the clinic",
            "I really like to go to the clinic",
            "I want to go to the clinic",
            "I really want to go to the clinic",
            "I want to go to the clinic again",
            "I really want to go to the clinic again",
            "I don’t like to go to the clinic",
            "I really don’t like to go to the clinic",
            "I don’t want to go to the clinic",
            "I really don’t want to go to the clinic",
            "I don’t want to go to the clinic again",
            "I really don’t want to go to the clinic again"

    },{"I like to go to the library",
            "I really like to go to the library",
            "I want to go to the library",
            "I really want to go to the library",
            "I want to go to the library again",
            "I really want to go to the library again",
            "I don’t like to go to the library",
            "I really don’t like to go to the library",
            "I don’t want to go to the library",
            "I really don’t want to go to the library",
            "I don’t want to go to the library again",
            "I really don’t want to go to the library again"

    },{"I like to go to the terrace",
            "I really like to go to the terrace",
            "I want to go to the terrace",
            "I really want to go to the terrace",
            "I want to go to the terrace again",
            "I really want to go to the terrace again",
            "I don’t like to go to the terrace",
            "I really don’t like to go to the terrace",
            "I don’t want to go to the terrace",
            "I really don’t want to go to the terrace",
            "I don’t want to go to the terrace again",
            "I really don’t want to go to the terrace again"


    }},{{"I like to be on time",
            "I really like to be on time",
            "I need some time",
            "I really need some time",
            "I need some more time",
            "I really need some more time",
            "I don’t like to be on time",
            "I really don’t like to be on time",
            "I don’t need any time",
            "I really don’t need any time",
            "I don’t need any more time",
            "I really don’t need any more time"

    },{"I like to know about the days of the week",
            "I really like to know about the days of the week",
            "I want to know about the days of the week",
            "I really want to know about the days of the week",
            "I want to know more about the days of the week",
            "I really want to know some more about the days of the week",
            "I don’t like to know about the days of the week",
            "I really don’t like to know about the days of the week",
            "I don’t want to know about the days of the week",
            "I really don’t want to know about the days of the week",
            "I don’t want to know more about the days of the week",
            "I really don’t want to know any more about the days of the week"

    },{"I like to know about the months of the year",
            "I really like to know about the months of the year",
            "I want to know about the months of the year",
            "I really want to know about the months of the year",
            "I want to know more about the months of the year",
            "I really want to know some more about the months of the year",
            "I don’t like to know about the months of the year",
            "I really don’t like to know about the months of the year",
            "I don’t want to know about the months of the year",
            "I really don’t want to know about the months of the year",
            "I don’t want to know more about the months of the year",
            "I really don’t want to know any more about the months of the year"

    },{"I like to know about the weather",
            "I really like to know about the weather",
            "I want to know about the weather",
            "I really want to know about the weather",
            "I want to know more about the weather",
            "I really want to know some more about the weather",
            "I don’t like to know about the weather",
            "I really don’t like to know about the weather",
            "I don’t want to know about the weather",
            "I really don’t want to know about the weather",
            "I don’t want to know more about the weather",
            "I really don’t want to know any more about the weather"

    },{"I like to know about the seasons",
            "I really like to know about the seasons",
            "I want to know about the seasons",
            "I really want to know about the seasons",
            "I want to know more about the seasons",
            "I really want to know some more about the seasons",
            "I don’t like to know about the seasons",
            "I really don’t like to know about the seasons",
            "I don’t want to know about the seasons",
            "I really don’t want to know about the seasons",
            "I don’t want to know more about the seasons",
            "I really don’t want to know any more about the seasons"

    },{"I like to know about festivals and holidays",
            "I really like to know about festivals and holidays",
            "I want to know about festivals and holidays",
            "I really want to know about festivals and holidays",
            "I want to know some more about festivals and holidays",
            "I really want to know some more about festivals and holidays",
            "I don’t like to know about festivals and holidays",
            "I really don’t like to know about festivals and holidays",
            "I don’t want to know about festivals and holidays",
            "I really don’t want to know about festivals and holidays",
            "I don’t want to know more about festivals and holidays",
            "I really don’t want to know any more about festivals and holidays"

    },{"I like to celebrate birthdays",
            "I really like to celebrate birthdays",
            "I want to celebrate birthdays",
            "I really want to celebrate birthdays",
            "I want to celebrate more birthdays",
            "I really want to celebrate some more birthdays",
            "I don’t like to celebrate birthdays",
            "I really don’t like to celebrate birthdays",
            "I don’t want to celebrate birthdays",
            "I really don’t want to celebrate birthdays",
            "I don’t want to celebrate more birthdays",
            "I really don’t want to celebrate any more birthdays"

    }}, {{"My name is ",
            "My name is ",
            "My caregiver's email address is ",
            "My caregiver's email address is ",
            "My caregiver's contact number is ",
            "My caregiver's contact number is ",
            "My caregiver’s name is ",
            "My caregiver’s name is ",
            "My home address is ",
            "My home address is ",
            "My blood group is ",
            "My blood group is "},
            {},{},{},{},
            {"I like medicines",
                    "I really like medicines",
                    "I want a medicine",
                    "I really want a medicine",
                    "I want more medicines",
                    "I want many more medicines",
                    "I don’t like medicines",
                    "I really don’t like medicines",
                    "I don’t want a medicine",
                    "I really don’t want a medicine",
                    "I don’t want more medicines",
                    "I don’t want any more medicines"
            },{"I like to use bandages",
            "I really like to use bandages",
            "I want a bandage",
            "I really want a bandage",
            "I want more bandages",
            "I want many more bandages",
            "I don’t like to use bandages",
            "I really don’t like to use bandages",
            "I don’t want a bandage",
            "I really don’t want a bandage",
            "I don’t want more bandages",
            "I don’t want any more bandages"
    },{"I like water",
            "I really like water",
            "I want water",
            "I really want water",
            "I want more water",
            "I want a lot more water",
            "I don’t like water",
            "I really don’t like water",
            "I don’t want water",
            "I really don’t want water",
            "I don’t want more water",
            "I don’t want any more water"

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

    }}};

    String[][][] layer_2_speech_hindi = {{
            {"मुझे शुभकामनाएं देना अच्छा लगता हैं",
                    "मुझे सच में शुभकामनाएं देना अच्छा लगता हैं",
                    "मुझे शुभकामनाएं देनी हैं",
                    "मुझे सच में शुभकामनाएं देनी हैं",
                    "मुझे और शुभकामनाएं देनी हैं",
                    "मुझे सच में थोड़ी और शुभकामनाएं देनी हैं",
                    "मुझे शुभकामनाएं देना अच्छा नहीं लगता हैं",
                    "मुझे सच में शुभकामनाएं देना अच्छा नहीं लगता हैं",
                    "मुझे शुभकामनाएं नहीं देनी हैं",
                    "मुझे सच में शुभकामनाएं नहीं देनी हैं",
                    "मुझे और शुभकामनाएं नहीं देनी हैं",
                    "मुझे सच में और शुभकामनाएं नहीं देनी हैं"

            },{"मुझे अपनी भावनाओं के बारे में बात करना अच्छा लगता हैं",
            "मुझे सच में अपनी भावनाओं के बारे में बात करना अच्छा लगता हैं",
            "मुझे अपनी भावनाओं के बारे में बात करनी हैं",
            "मुझे सच में अपनी भावनाओं के बारे में बात करनी हैं",
            "मुझे अपनी भावनाओं के बारे में थोड़ी और बात करनी हैं",
            "मुझे सच में अपनी भावनाओं के बारे में थोड़ी और बात करनी हैं",
            "मुझे अपनी भावनाओं के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी भावनाओं के बारे में बात करना अच्छा नहीं लगता हैं",
            "मुझे अपनी भावनाओं के बारे में बात नहीं करनी हैं",
            "मुझे सच में अपनी भावनाओं के बारे में बात नहीं करनी हैं",
            "मुझे अपनी भावनाओं के बारे में और बात नहीं करनी हैं",
            "मुझे सच में अपनी भावनाओं के बारे में कोई भी बात नहीं करनी हैं"

    },{"मुझे बिनती करना अच्छा लगता हैं",
            "मुझे सच में बिनती करना अच्छा लगता हैं",
            "मुझे कुछ बिनती करनी हैं",
            "मुझे सच में कुछ बिनती करनी हैं",
            "मुझे एक और बिनती करनी हैं",
            "मुझे सच में एक और बिनती करनी हैं",
            "मुझे बिनती करना अच्छा नहीं लगता हैं",
            "मुझे सच में बिनती करना अच्छा नहीं लगता हैं",
            "मुझे कोई भी बिनती नहीं करनी हैं",
            "मुझे सच में कोई भी बिनती नहीं करनी हैं",
            "मुझे और बिनती नहीं करनी हैं",
            "मुझे सच में कोई भी बिनती नहीं करनी हैं"

    },{"मुझे सवाल पूछना अच्छा लगता हैं",
            "मुझे सच में सवाल पूछना अच्छा लगता हैं",
            "मुझे एक सवाल पूछना हैं",
            "मुझे सच में एक सवाल पूछना हैं",
            "मुझे कुछ और सवाल पूछने हैं",
            "मुझे सच में कई और सवाल पूछने हैं",
            "मुझे सवाल पूछना अच्छा नहीं लगता हैं",
            "मुझे सच में सवाल पूछना अच्छा नहीं लगता हैं",
            "मुझे एक भी सवाल नहीं पूछना हैं",
            "मुझे सच में एक भी सवाल नहीं पूछना हैं",
            "मुझे और सवाल नहीं पूछने हैं",
            "मुझे सच में कोई भी सवाल नहीं पूछना हैं"

    }},{{"मुझे अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने दांत साफ़ करने हैं",
            "मुझे सच में अपने दांत साफ़ करने हैं",
            "मुझे अपने दांत फिर से साफ़ करने हैं",
            "मुझे सच में अपने दांत फिर से साफ़ करने हैं",
            "मुझे अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत साफ़ नहीं करने हैं",
            "मुझे अपने दांत फिर से साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत फिर से साफ़ नहीं करने हैं"

    },{"मुझे शौचालय जाना अच्छा लगता हैं",
            "मुझे सच में शौचालय जाना अच्छा लगता हैं",
            "मुझे शौचालय जाना हैं",
            "मुझे सच में शौचालय जाना हैं",
            "मुझे फिर से शौचालय जाना हैं",
            "मुझे सच में फिर से शौचालय जाना  हैं",
            "मुझे शौचालय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में शौचालय जाना अच्छा नहीं लगता हैं",
            "मुझे शौचालय नहीं जाना हैं",
            "मुझे सच में शौचालय नहीं जाना हैं",
            "मुझे फिर से शौचालय नहीं जाना हैं",
            "मुझे सच में फिर से शौचालय नहीं जाना हैं",

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
            "मुझे सच में फिर से नहाना नहीं हैं",

    },{"मुझे कपड़े और सहायक चीज़ें अच्छी लगती हैं",
            "मुझे सच में कपड़े और सहायक चीज़ें अच्छी लगती हैं",
            "मुझे कुछ कपड़े और सहायक चीज़ें चाहिए",
            "मुझे सच में कुछ कपड़े और सहायक चीज़ें चाहिए",
            "मुझे कुछ और कपड़े और सहायक चीज़ें चाहिए",
            "मुझे सच में कुछ और कपड़े और सहायक चीज़ें चाहिए",
            "मुझे कपड़े और सहायक चीज़ें अच्छी नहीं लगती हैं",
            "मुझे सच में कपड़े और सहायक चीज़ें अच्छी नहीं लगती हैं",
            "मुझे कपड़े और सहायक चीज़ें नहीं चाहिए",
            "मुझे सच में कपड़े और सहायक चीज़ें नहीं चाहिए",
            "मुझे और कपड़े और सहायक चीज़ें नहीं चाहिए",
            "मुझे सच में और कपड़े और सहायक चीज़ें नहीं चाहिए"

    },{"मुझे तैयार होना अच्छा लगता हैं",
            "मुझे सच में तैयार होना अच्छा लगता हैं",
            "मुझे तैयार होना हैं",
            "मुझे सच में तैयार होना हैं",
            "मुझे तैयार होने के लिए और समय चाहिए",
            "मुझे सच में तैयार होने के लिए और समय चाहिए",
            "मुझे तैयार होना अच्छा नहीं लगता हैं",
            "मुझे सच में तैयार होना अच्छा नहीं लगता हैं",
            "मुझे तैयार नहीं होना हैं",
            "मुझे सच में तैयार नहीं होना हैं",
            "मुझे तैयार होने के लिए और समय नहीं चाहिए",
            "मुझे सच में तैयार होने के लिए कुछ और समय नहीं चाहिए"

    },{"मुझे सोना अच्छा लगता हैं",
            "मुझे सच में सोना अच्छा लगता हैं",
            "मुझे सोना हैं",
            "मुझे सच में सोना हैं",
            "मुझे और सोना हैं",
            "मुझे सच में थोड़ा और समय सोना हैं",
            "मुझे सोना अच्छा नहीं लगता हैं",
            "मुझे सच में सोना अच्छा नहीं लगता हैं",
            "मुझे सोना नहीं हैं",
            "मुझे सच में सोना नहीं हैं",
            "मुझे और समय सोना नहीं हैं",
            "मुझे सच में और समय सोना नहीं हैं"

    },{"मुझे उपचार करवाना अच्छा लगता हैं",
            "मुझे सच में उपचार करवाना अच्छा लगता हैं",
            "मुझे उपचार के लिए जाना  हैं",
            "मुझे सच में उपचार के लिए जाना  हैं",
            "मुझे फिर से उपचार के लिए जाना  हैं",
            "मुझे सच में फिर से उपचार के लिए जाना  हैं",
            "मुझे उपचार करवाना अच्छा नहीं लगता हैं",
            "मुझे सच में उपचार करवाना अच्छा नहीं लगता हैं",
            "मुझे उपचार के लिए नहीं  जाना हैं",
            "मुझे सच में उपचार के लिए नहीं जाना  हैं",
            "मुझे फिर से उपचार के लिए नहीं जाना  हैं",
            "मुझे सच में फिर से उपचार के लिए नहीं जाना हैं"

    },{"मुझे अपने सुबह के नियमित कार्य करना अच्छा लगता है",
            "मुझे सच में अपने सुबह के नियमित कार्य करना अच्छा लगता हैं",
            "मुझे अपने सुबह के नियमित कार्यों के बारे में जानना  हैं",
            "मुझे सच में अपने सुबह के नियमित कार्यों के बारे में जानना  हैं",
            "मुझे अपने सुबह के नियमित कार्यों के बारे में थोड़ा और  जानना  हैं",
            "मुझे सच में अपने सुबह के नियमित कार्यों के बारे में थोड़ा और  जानना  हैं",
            "मुझे अपने सुबह के नियमित कार्य करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने सुबह के नियमित कार्य करना अच्छा नहीं लगता हैं",
            "मुझे अपने सुबह के नियमित कार्यों के बारे में नहीं जानना  हैं",
            "मुझे सच में अपने सुबह के नियमित कार्यों के बारे में नहीं जानना  हैं",
            "मुझे अपने सुबह के नियमित कार्यों के बारे में और  नहीं जानना  हैं",
            "मुझे सच में अपने सुबह के नियमित कार्यों के बारे में कुछ भी नहीं जानना  हैं"

    },{"मुझे अपने रात के नियमित कार्य करना अच्छा लगता हैं",
            "मुझे सच में अपने रात के नियमित कार्य करना अच्छा लगता हैं",
            "मुझे अपने रात के नियमित कार्यों के बारे में जानना हैं",
            "मुझे सच में अपने रात के नियमित कार्यों के बारे में जानना  हैं",
            "मुझे अपने रात के नियमित कार्यों के बारे में थोड़ा और जानना हैं",
            "मुझे सच में अपने रात के नियमित कार्यों के बारे में थोड़ा और जानना हैं",
            "मुझे अपने रात के नियमित कार्य करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने रात के नियमित कार्य करना अच्छा नहीं लगता हैं",
            "मुझे अपने रात के नियमित कार्यों के बारे में नहीं जानना हैं",
            "मुझे सच में अपने रात के नियमित कार्यों के बारे में नहीं जानना हैं",
            "मुझे अपने रात के नियमित कार्यों के बारे में और नहीं जानना हैं",
            "मुझे सच में अपने रात के नियमित कार्यों के बारे में कुछ भी नहीं जानना हैं"

    }},{{"मुझे सुबह का नाश्ता करना अच्छा लगता हैं",
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

    },{"मुझे भोजन करना अच्छा लगता हैं",
            "मुझे सच में भोजन करना अच्छा लगता हैं",
            "मुझे भोजन चाहिए",
            "मुझे सच में भोजन चाहिए",
            "मुझे और भोजन चाहिए",
            "मुझे सच में और भोजन चाहिए",
            "मुझे भोजन करना अच्छा नहीं लगता हैं",
            "मुझे सच में भोजन करना अच्छा नहीं लगता हैं",
            "मुझे भोजन नहीं चाहिए",
            "मुझे सच में भोजन नहीं चाहिए",
            "मुझे और भोजन नहीं चाहिए",
            "मुझे सच में और भोजन नहीं चाहिए"

    },{"मुझे मिठाइयाँ अच्छी लगती हैं",
            "मुझे सच में मिठाइयाँ अच्छी लगती हैं",
            "मुझे कुछ मिठाइयाँ खानी हैं",
            "मुझे सच में कुछ मिठाइयाँ खानी हैं",
            "मुझे और मिठाइयाँ खानी हैं",
            "मुझे सच में और मिठाइयाँ खानी हैं",
            "मुझे मिठाइयाँ अच्छी नहीं लगती हैं",
            "मुझे सच में मिठाइयाँ अच्छी नहीं लगती हैं",
            "मुझे मिठाइयाँ नहीं खानी हैं",
            "मुझे सच में मिठाइयाँ नहीं खानी हैं",
            "मुझे और मिठाइयाँ नहीं खानी हैं",
            "मुझे सच में कुछ भी मिठाइयाँ नहीं खानी हैं"

    },{"मुझे स्नैक्स खाना अच्छा लगता हैं",
            "मुझे सच में स्नैक्स खाना अच्छा लगता हैं",
            "मुझे कुछ स्नैक्स चाहिए",
            "मुझे सच में कुछ स्नैक्स चाहिए",
            "मुझे और स्नैक्स चाहिए",
            "मुझे सच में थोड़े और स्नैक्स चाहिए",
            "मुझे स्नैक्स खाना अच्छा नहीं लगता हैं",
            "मुझे सच में स्नैक्स खाना अच्छा नहीं लगता हैं",
            "मुझे स्नैक्स नहीं चाहिए",
            "मुझे सच में स्नैक्स नहीं चाहिए",
            "मुझे और स्नैक्स नहीं चाहिए",
            "मुझे सच में और स्नैक्स नहीं चाहिए"

    },{"मुझे फल अच्छे लगते हैं",
            "मुझे सच में फल अच्छे लगते हैं",
            "मुझे  कुछ फल चाहिए",
            "मुझे सच में कुछ फल चाहिए",
            "मुझे और फल चाहिए",
            "मुझे  सच में थोड़े और  फल चाहिए",
            "मुझे फल अच्छे नहीं लगते हैं",
            "मुझे सच में फल अच्छे नहीं लगते हैं",
            "मुझे  फल नहीं चाहिए",
            "मुझे  सच में फल नहीं चाहिए",
            "मुझे और फल नहीं चाहिए",
            "मुझे सच में कुछ भी फल नहीं चाहिए"

    },{"मुझे प्यास लगी हैं",
            "मुझे सच में प्यास लगी हैं",
            "मुझे कुछ पीना हैं",
            "मुझे सच में कुछ पीना हैं",
            "मुझे बहुत प्यास लगी हैं",
            "मुझे सच में बहुत प्यास लगी हैं",
            "मुझे प्यास नहीं लगी हैं",
            "मुझे सच में प्यास नहीं लगी हैं",
            "मुझे कुछ भी पीना नहीं हैं",
            "मुझे सच में कुछ भी पीना नहीं हैं",
            "मुझे बिल्कुल प्यास नहीं लगी हैं",
            "मुझे सच में बिल्कुल प्यास नहीं लगी हैं"

    },{"मुझे कटलरी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कटलरी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे थोड़ी कटलरी चाहिए",
            "मुझे सच में थोड़ी कटलरी चाहिए",
            "मुझे और कटलरी चाहिए",
            "मुझे सच में थोड़ी और कटलरी चाहिए",
            "मुझे कटलरी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कटलरी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कटलरी नहीं चाहिए",
            "मुझे सच में कटलरी नहीं चाहिए",
            "मुझे और कटलरी नहीं चाहिए",
            "मुझे सच में और कटलरी नहीं चाहिए"

    },{"मुझे अॅड-ऑन्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में अॅड-ऑन्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे कुछ अॅड-ऑन्स चाहिए",
            "मुझे सच में कुछ अॅड-ऑन्स चाहिए",
            "मुझे और अॅड-ऑन्स चाहिए",
            "मुझे सच में कुछ और अॅड-ऑन्स चाहिए",
            "मुझे अॅड-ऑन्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में अॅड-ऑन्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कुछ भी अॅड-ऑन्स नहीं चाहिए",
            "मुझे सच में कुछ भी अॅड-ऑन्स नहीं चाहिए",
            "मुझे और अॅड-ऑन्स नहीं चाहिए",
            "मुझे सच में और अॅड-ऑन्स नहीं चाहिए"

    }},{{"मुझे घर के खेल खेलना अच्छा लगता हैं",
            "मुझे सच में घर के खेल खेलना अच्छा लगता हैं",
            "मुझे कुछ घर के खेल खेलने हैं",
            "मुझे सच में कुछ घर के खेल खेलने हैं",
            "मुझे कुछ और घर के खेल खेलने हैं",
            "मुझे सच में कुछ और समय के लिए घर के खेल खेलने हैं",
            "मुझे घर के खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में घर के खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे घर के खेल नहीं खेलने हैं",
            "मुझे सच में घर के खेल नहीं खेलने हैं",
            "मुझे कोई भी घर के खेल नहीं खेलने हैं",
            "मुझे सच में और समय के लिए घर के खेल नहीं खेलने हैं"

    },{"मुझे बाहरी खेल खेलना अच्छा लगता हैं",
            "मुझे सच में बाहरी खेल खेलना अच्छा लगता हैं",
            "मुझे कुछ बाहरी खेल खेलने हैं",
            "मुझे सच में कुछ बाहरी खेल खेलने हैं",
            "मुझे कुछ और बाहरी खेल खेलने हैं",
            "मुझे सच में कुछ और समय के लिए बाहरी खेल खेलने हैं",
            "मुझे बाहरी खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में बाहरी खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे बाहरी खेल नहीं खेलने हैं",
            "मुझे सच में बाहरी खेल नहीं खेलने हैं",
            "मुझे कोई भी बाहरी खेल नहीं खेलने हैं",
            "मुझे सच में और समय के लिए बाहरी खेल नहीं खेलने हैं"

    },{"मुझे खेलना-कूदना अच्छा लगता हैं",
            "मुझे सच में खेलना-कूदना अच्छा लगता हैं",
            "मुझे खेलना-कूदना हैं",
            "मुझे सच में खेलना-कूदना हैं",
            "मुझे और खेलना-कूदना हैं",
            "मुझे सच में और समय के लिए खेलना-कूदना हैं",
            "मुझे खेलना-कूदना अच्छा नहीं लगता हैं",
            "मुझे सच में खेलना-कूदना अच्छा नहीं लगता हैं",
            "मुझे खेलना-कूदना नहीं हैं",
            "मुझे सच में खेलना-कूदना नहीं हैं",
            "मुझे और खेलना-कूदना नहीं हैं",
            "मुझे सच में और समय के लिए खेलना-कूदना नहीं हैं"

    },{"मुझे टीवी देखना अच्छा लगता हैं",
            "मुझे सच में टीवी देखना अच्छा लगता हैं",
            "मुझे टीवी देखना हैं",
            "मुझे सच में टीवी देखना हैं",
            "मुझे कुछ और समय के लिए टीवी देखना हैं",
            "मुझे सच में कुछ और समय के लिए टीवी देखना हैं",
            "मुझे टीवी देखना अच्छा नहीं लगता हैं",
            "मुझे सच में टीवी देखना अच्छा नहीं लगता हैं",
            "मुझे टीवी नहीं देखना हैं",
            "मुझे सच में टीवी नहीं देखना हैं",
            "मुझे और समय के लिए टीवी नहीं देखना हैं",
            "मुझे सच में टीवी बिल्कुल नहीं देखना हैं"

    },{"मुझे संगीत सुनना अच्छा लगता हैं",
            "मुझे सच में संगीत सुनना अच्छा लगता हैं",
            "मुझे कुछ संगीत सुनना हैं",
            "मुझे सच में कुछ संगीत सुनना हैं",
            "मुझे कुछ और संगीत सुनना हैं",
            "मुझे सच में कुछ और समय के लिए संगीत सुनना हैं",
            "मुझे संगीत सुनना अच्छा नहीं लगता हैं",
            "मुझे सच में संगीत सुनना अच्छा नहीं लगता हैं",
            "मुझे संगीत नहीं सुनना हैं",
            "मुझे सच में संगीत नहीं सुनना हैं",
            "मुझे और समय के लिए संगीत नहीं सुनना हैं",
            "मुझे सच में संगीत बिल्कुल नहीं सुनना हैं"

    },{"मुझे अलग-अलग कार्य करना अच्छा लगता हैं",
            "मुझे सच में अलग-अलग कार्य करना अच्छा लगता हैं",
            "मुझे अलग-अलग कार्य करने हैं",
            "मुझे सच में अलग-अलग कार्य करने हैं",
            "मुझे और कार्य करने हैं",
            "मुझे सच में कुछ और कार्य करने हैं",
            "मुझे अलग-अलग कार्य करना अच्छा नहीं लगता हैं",
            "मुझे सच में अलग-अलग कार्य करना अच्छा नहीं लगता हैं",
            "मुझे अलग-अलग कार्य नहीं करने हैं",
            "मुझे सच में अलग-अलग कार्य नहीं करने हैं",
            "मुझे और कार्य नहीं करने हैं",
            "मुझे सच में कोई भी कार्य नहीं करने हैं"

    }},{{"मुझे पशु-पक्षी अच्छे लगते हैं",
            "मुझे सच में पशु-पक्षी अच्छे लगते हैं",
            "मुझे पशु-पक्षीयों के बारे में जानना हैं",
            "मुझे सच में पशु-पक्षीयों के बारे में जानना हैं",
            "मुझे पशु-पक्षीयों के बारे में और जानना हैं",
            "मुझे सच में पशु-पक्षीयों के बारे में थोड़ा और जानना हैं",
            "मुझे पशु-पक्षी अच्छे नहीं लगते हैं",
            "मुझे सच में पशु-पक्षी अच्छे नहीं लगते हैं",
            "मुझे पशु-पक्षीयों के बारे में नहीं जानना हैं",
            "मुझे सच में पशु-पक्षीयों के बारे में नहीं जानना हैं",
            "मुझे पशु-पक्षीयों के बारे में और नहीं जानना  हैं",
            "मुझे सच में पशु-पक्षीयों के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे अपना शरीर अच्छा लगता हैं",
            "मुझे सच में अपना शरीर अच्छा लगता हैं",
            "मुझे अपने शरीर के बारे में जानना हैं",
            "मुझे सच में अपने शरीर के बारे में जानना हैं",
            "मुझे अपने शरीर के बारे में कुछ और जानना हैं",
            "मुझे सच में अपने शरीर के बारे में थोड़ा और जानना हैं",
            "मुझे अपना शरीर अच्छा नहीं लगता हैं",
            "मुझे सच में अपना शरीर अच्छा नहीं लगता हैं",
            "मुझे अपने शरीर के बारे में नहीं जानना हैं",
            "मुझे सच में अपने शरीर के बारे में नहीं जानना हैं",
            "मुझे अपने शरीर के बारे में और नहीं जानना हैं",
            "मुझे सच में अपने शरीर के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे किताबें अच्छी लगती हैं",
            "मुझे सच में किताबें अच्छी लगती हैं",
            "मुझे किताबें पढ़नी हैं",
            "मुझे सच में किताबें पढ़नी हैं",
            "मुझे और किताबें पढ़नी हैं",
            "मुझे सच में कुछ और किताबें पढ़नी हैं",
            "मुझे किताबें अच्छी नहीं लगती हैं",
            "मुझे सच में किताबें अच्छी नहीं लगती हैं",
            "मुझे किताबें नहीं पढ़नी हैं",
            "मुझे सच में किताबें नहीं पढ़नी हैं",
            "मुझे और किताबें नहीं पढ़नी हैं",
            "मुझे सच में और किताबें नहीं पढ़नी हैं"

    },{"मुझे रंग अच्छे लगते हैं",
            "मुझे सच में रंग अच्छे लगते हैं",
            "मुझे रंगों के बारे में जानना हैं",
            "मुझे सच में रंगों के बारे में जानना हैं",
            "मुझे रंगों के बारे में और जानना हैं",
            "मुझे सच में रंगों के बारे में कुछ और जानना हैं",
            "मुझे रंग अच्छे नहीं लगते हैं",
            "मुझे सच में रंग अच्छे नहीं लगते हैं",
            "मुझे रंगों के बारे में नहीं जानना हैं",
            "मुझे सच में रंगों के बारे में नहीं जानना हैं",
            "मुझे रंगों के बारे में और नहीं जानना हैं",
            "मुझे सच में रंगों के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे आकार अच्छे लगते हैं",
            "मुझे सच में आकार अच्छे लगते हैं",
            "मुझे आकारों के बारे में जानना हैं",
            "मुझे सच में आकारों के बारे में जानना हैं",
            "मुझे आकारों के बारे में और जानना हैं",
            "मुझे सच में आकारों के बारे में कुछ और जानना हैं",
            "मुझे आकार अच्छे नहीं लगते हैं",
            "मुझे सच में आकार अच्छे नहीं लगते हैं",
            "मुझे आकारों के बारे में नहीं जानना हैं",
            "मुझे सच में आकारों के बारे में नहीं जानना हैं",
            "मुझे आकारों के बारे में और नहीं जानना हैं",
            "मुझे सच में आकारों के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे स्टेशनरी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में स्टेशनरी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे कुछ स्टेशनरी चाहिए",
            "मुझे सच में कुछ स्टेशनरी चाहिए",
            "मुझे और स्टेशनरी चाहिए",
            "मुझे सच में थोड़ी और स्टेशनरी चाहिए",
            "मुझे स्टेशनरी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्टेशनरी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे स्टेशनरी नहीं चाहिए",
            "मुझे सच में कोई भी स्टेशनरी नहीं चाहिए",
            "मुझे और स्टेशनरी नहीं चाहिए",
            "मुझे सच में कुछ भी स्टेशनरी नहीं चाहिए"

    },{"मुझे पाठशाला की वस्तुओं का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में पाठशाला की वस्तुओं का इस्तमाल करना अच्छा लगता हैं",
            "मुझे पाठशाला की वस्तुओं के बारे में जानना हैं",
            "मुझे सच में पाठशाला की वस्तुओं के बारे में जानना हैं",
            "मुझे पाठशाला की वस्तुओं के बारे में और जानना हैं",
            "मुझे सच में पाठशाला की वस्तुओं के बारे में कुछ और जानना हैं",
            "मुझे पाठशाला की वस्तुओं का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में पाठशाला की वस्तुओं का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे पाठशाला की वस्तुओं के बारे में नहीं जानना हैं",
            "मुझे सच में पाठशाला की वस्तुओं के बारे में नहीं जानना हैं",
            "मुझे पाठशाला की वस्तुओं के बारे में और नहीं जानना हैं",
            "मुझे सच में पाठशाला की वस्तुओं के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे अलग-अलग तरह के घरेलु वस्तुओं का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में अलग-अलग तरह के घरेलु वस्तुओं का इस्तमाल करना अच्छा लगता हैं",
            "मुझे घरेलु वस्तुओं के बारे में जानना हैं",
            "मुझे सच में घरेलु वस्तुओं के बारे में जानना हैं",
            "मुझे घरेलु वस्तुओं के बारे में और जानना हैं",
            "मुझे सच में घरेलु वस्तुओं के बारे में कुछ और जानना हैं",
            "मुझे अलग-अलग तरह के घरेलु वस्तुओं का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में अलग-अलग तरह के घरेलु वस्तुओं का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे घरेलु वस्तुओं के बारे में नहीं जानना हैं",
            "मुझे सच में घरेलु वस्तुओं के बारे में नहीं जानना हैं",
            "मुझे घरेलु वस्तुओं के बारे में और नहीं जानना हैं",
            "मुझे सच में घरेलु वस्तुओं के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे यात्रा करने के अलग-अलग साधनों का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में यात्रा करने के अलग-अलग साधनों का इस्तमाल करना अच्छा लगता हैं",
            "मुझे यात्रा करने के साधनों के बारे में जानना हैं",
            "मुझे सच में यात्रा करने के साधनों के बारे में जानना हैं",
            "मुझे यात्रा करने के साधनों के बारे में और जानना हैं",
            "मुझे सच में यात्रा करने के साधनों के बारे में कुछ और जानना हैं",
            "मुझे यात्रा करने के अलग-अलग साधनों का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में यात्रा करने के अलग-अलग साधनों का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे यात्रा करने के साधनों के बारे में नहीं जानना हैं",
            "मुझे सच में यात्रा करने के साधनों के बारे में नहीं जानना हैं",
            "मुझे यात्रा करने के साधनों के बारे में और नहीं जानना हैं",
            "मुझे सच में यात्रा करने के साधनों के बारे में कुछ भी नहीं जानना हैं"

    }},{{"मुझे अपनी माँ से प्यार हैं",
            "मुझे सच में अपनी माँ से प्यार हैं",
            "मुझे अपनी माँ चाहिए",
            "मुझे सच में अपनी माँ चाहिए",
            "मुझे अपनी माँ के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी माँ के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी माँ  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी माँ  अच्छी नहीं लगती हैं",
            "मुझे अपनी माँ नहीं चाहिए",
            "मुझे सच में अपनी माँ नहीं चाहिए",
            "मुझे अपनी माँ के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी माँ के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने पिताजी से प्यार हैं",
            "मुझे सच में अपने पिताजी से प्यार हैं",
            "मुझे अपने पिताजी चाहिए",
            "मुझे सच में अपने पिताजी चाहिए",
            "मुझे अपने पिताजी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने पिताजी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने पिताजी  अच्छे नहीं लगते हैं",
            "मुझे सच में अपने पिताजी अच्छे नहीं लगते हैं",
            "मुझे अपने पिताजी नहीं चाहिए",
            "मुझे सच में अपने पिताजी नहीं चाहिए",
            "मुझे अपने पिताजी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने पिताजी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने भाई से प्यार हैं",
            "मुझे सच में अपने भाई से प्यार हैं",
            "मुझे अपना भाई चाहिए",
            "मुझे सच में अपना भाई चाहिए",
            "मुझे अपने भाई के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने भाई के साथ कुछ और समय बिताना हैं",
            "मुझे अपना भाई  अच्छा नहीं लगता हैं",
            "मुझे सच में अपना भाई अच्छा नहीं लगता हैं",
            "मुझे अपना भाई नहीं चाहिए",
            "मुझे सच में अपना भाई नहीं चाहिए",
            "मुझे अपने भाई के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने भाई के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी बहन से प्यार हैं",
            "मुझे सच में अपनी बहन से प्यार हैं",
            "मुझे अपनी बहन चाहिए",
            "मुझे सच में अपनी बहन चाहिए",
            "मुझे अपनी बहन के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी बहन के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी बहन  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी बहन अच्छी नहीं लगती हैं",
            "मुझे अपनी बहन नहीं चाहिए",
            "मुझे सच में अपनी बहन नहीं चाहिए",
            "मुझे अपनी बहन के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी बहन के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने बड़े पापा से प्यार हैं",
            "मुझे सच में अपने बड़े पापा से प्यार हैं",
            "मुझे अपने बड़े पापा चाहिए",
            "मुझे सच में अपने बड़े पापा चाहिए",
            "मुझे अपने बड़े पापा के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने बड़े पापा के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने बड़े पापा अच्छे नहीं लगते हैं",
            "मुझे सच में अपने बड़े पापा अच्छे नहीं लगते हैं",
            "मुझे अपने बड़े पापा नहीं चाहिए",
            "मुझे सच में अपने बड़े पापा नहीं चाहिए",
            "मुझे अपने बड़े पापा के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने बड़े पापा के साथ कुछ और समय नहीं बिताना हैं"

    }, {"मुझे अपनी बड़ी मम्मी से प्यार हैं",
            "मुझे सच अपनी बड़ी मम्मी से प्यार हैं",
            "मुझे अपनी बड़ी मम्मी चाहिए",
            "मुझे सच में अपनी बड़ी मम्मी चाहिए",
            "मुझे अपनी बड़ी मम्मी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी बड़ी मम्मी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी बड़ी मम्मी अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी बड़ी मम्मी अच्छी नहीं लगती हैं",
            "मुझे अपनी बड़ी मम्मी नहीं चाहिए",
            "मुझे सच में अपनी बड़ी मम्मी नहीं चाहिए",
            "मुझे अपनी बड़ी मम्मी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी बड़ी मम्मी के साथ कुछ और समय नहीं बिताना हैं"

    }, {"मुझे अपने दादाजी से प्यार हैं",
            "मुझे सच में अपने दादाजी से प्यार हैं",
            "मुझे अपने दादाजी चाहिए",
            "मुझे सच में अपने दादाजी चाहिए",
            "मुझे अपने दादाजी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने दादाजी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने दादाजी  अच्छे नहीं लगते हैं",
            "मुझे सच में अपने दादाजी  अच्छे नहीं लगते हैं",
            "मुझे अपने दादाजी नहीं चाहिए",
            "मुझे सच में अपने दादाजी नहीं चाहिए",
            "मुझे अपने दादाजी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने दादाजी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी दादी मां से प्यार हैं",
            "मुझे सच में अपनी दादी मां से प्यार हैं",
            "मुझे अपनी दादी मां चाहिए",
            "मुझे सच में अपनी दादी मां चाहिए",
            "मुझे अपनी दादी मां के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी दादी मां के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी दादी मां  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी दादी मां अच्छी नहीं लगती हैं",
            "मुझे अपनी दादी मां नहीं चाहिए",
            "मुझे सच में अपनी दादी मां नहीं चाहिए",
            "मुझे अपनी दादी मां के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी दादी मां के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने नानाजी से प्यार हैं",
            "मुझे सच में अपने नानाजी से प्यार हैं",
            "मुझे अपने नानाजी चाहिए",
            "मुझे सच में अपने नानाजी चाहिए",
            "मुझे अपने नानाजी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने नानाजी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने नानाजी  अच्छे नहीं लगते हैं",
            "मुझे सच में अपने नानाजी  अच्छे नहीं लगते हैं",
            "मुझे अपने नानाजी नहीं चाहिए",
            "मुझे सच में अपने नानाजी नहीं चाहिए",
            "मुझे अपने नानाजी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने नानाजी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी नानी मां से प्यार हैं",
            "मुझे सच में अपनी नानी मां से प्यार हैं",
            "मुझे अपनी नानी मां चाहिए",
            "मुझे सच में अपनी नानी मां चाहिए",
            "मुझे अपनी नानी मां के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी नानी मां के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी नानी मां  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी नानी मां अच्छी नहीं लगती हैं",
            "मुझे अपनी नानी मां नहीं चाहिए",
            "मुझे सच में अपनी नानी मां नहीं चाहिए",
            "मुझे अपनी नानी मां के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी नानी मां के साथ कुछ और समय नहीं बिताना हैं"

    }, {"मुझे अपने चाचा से प्यार हैं",
            "मुझे सच में अपने चाचा से प्यार हैं",
            "मुझे अपने चाचा चाहिए",
            "मुझे सच में अपने चाचा चाहिए",
            "मुझे अपने चाचा के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने चाचा के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने चाचा  अच्छे नहीं लगते हैं",
            "मुझे सच में अपने चाचा अच्छे नहीं लगते हैं",
            "मुझे अपने चाचा नहीं चाहिए",
            "मुझे सच में अपने चाचा नहीं चाहिए",
            "मुझे अपने चाचा के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने चाचा के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी चाची से प्यार हैं",
            "मुझे सच में अपनी चाची से प्यार हैं",
            "मुझे अपनी चाची चाहिए",
            "मुझे सच में अपनी चाची चाहिए",
            "मुझे अपनी चाची के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी चाची के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी चाची  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी चाची अच्छी नहीं लगती हैं",
            "मुझे अपनी चाची नहीं चाहिए",
            "मुझे सच में अपनी चाची नहीं चाहिए",
            "मुझे अपनी चाची के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी चाची के साथ कुछ और समय नहीं बिताना हैं"

    }, {"मुझे अपने मामा से प्यार हैं",
            "मुझे सच में अपने मामा से प्यार हैं",
            "मुझे अपने मामा चाहिए",
            "मुझे सच में अपने मामा चाहिए",
            "मुझे अपने मामा के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने मामा के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने मामा  अच्छे  नहीं लगते हैं",
            "मुझे सच में अपने मामा  अच्छे  नहीं लगते हैं",
            "मुझे अपने मामा  नहीं चाहिए",
            "मुझे सच में अपने मामा नहीं चाहिए",
            "मुझे अपने मामा  के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने मामा  के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी मामी से प्यार हैं",
            "मुझे सच में अपनी मामी से प्यार हैं",
            "मुझे अपनी मामी चाहिए",
            "मुझे सच में अपनी मामी चाहिए",
            "मुझे अपनी मामी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी मामी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी मामी  अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी मामी अच्छी नहीं लगती हैं",
            "मुझे अपनी मामी नहीं चाहिए",
            "मुझे सच में अपनी मामी नहीं चाहिए",
            "मुझे अपनी मामी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी मामी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी बुआ से प्यार हैं",
            "मुझे सच अपनी बुआ से प्यार हैं",
            "मुझे अपनी बुआ चाहिए",
            "मुझे सच में अपनी बुआ चाहिए",
            "मुझे अपनी बुआ के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी बुआ के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी बुआ अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी बुआ अच्छी नहीं लगती हैं",
            "मुझे अपनी बुआ नहीं चाहिए",
            "मुझे सच में अपनी बुआ नहीं चाहिए",
            "मुझे अपनी बुआ के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी बुआ के साथ कुछ और समय नहीं बिताना हैं",

    },{"मुझे अपने फ़ुफ़ा से प्यार हैं",
            "मुझे सच में अपने फ़ुफ़ा से प्यार हैं",
            "मुझे अपने फ़ुफ़ा चाहिए",
            "मुझे सच में अपने फ़ुफ़ा चाहिए",
            "मुझे अपने फ़ुफ़ा के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने फ़ुफ़ा के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने फ़ुफ़ा अच्छे  नहीं लगते हैं",
            "मुझे सच में अपने फ़ुफ़ा अच्छे  नहीं लगते हैं",
            "मुझे अपने फ़ुफ़ा नहीं चाहिए",
            "मुझे सच में अपने फ़ुफ़ा नहीं चाहिए",
            "मुझे अपने फ़ुफ़ा के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने फ़ुफ़ा के साथ कुछ और समय नहीं बिताना हैं",

    },{"मुझे अपनी मौसी से प्यार हैं",
            "मुझे सच अपनी मौसी से प्यार हैं",
            "मुझे अपनी मौसी चाहिए",
            "मुझे सच में अपनी मौसी चाहिए",
            "मुझे अपनी मौसी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी मौसी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी मौसी अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी मौसी अच्छी नहीं लगती हैं",
            "मुझे अपनी मौसी नहीं चाहिए",
            "मुझे सच में अपनी मौसी नहीं चाहिए",
            "मुझे अपनी मौसी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी मौसी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने मौसा से प्यार हैं",
            "मुझे सच में अपने मौसा से प्यार हैं",
            "मुझे अपने मौसा चाहिए",
            "मुझे सच में अपने मौसा चाहिए",
            "मुझे अपने मौसा के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने मौसा के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने मौसा अच्छे  नहीं लगते हैं",
            "मुझे सच में अपने मौसा अच्छे  नहीं लगते हैं",
            "मुझे अपने मौसा नहीं चाहिए",
            "मुझे सच में अपने मौसा नहीं चाहिए",
            "मुझे अपने मौसा के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने मौसा के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे नन्हें बच्चें प्यारे लगते हैं",
            "मुझे सच में नन्हें बच्चें प्यारे लगते हैं",
            "मुझे नन्हें बच्चे को पकड़ना हैं",
            "मुझे सच में नन्हें बच्चे को पकड़ना हैं",
            "मुझे नन्हें बच्चे के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में नन्हें बच्चे के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे नन्हें बच्चें अच्छे नहीं लगते हैं",
            "मुझे सच में नन्हें बच्चें अच्छे नहीं लगते हैं",
            "मुझे नन्हें बच्चे को नहीं पकड़ना हैं",
            "मुझे सच में नन्हें बच्चे को नहीं पकड़ना हैं",
            "मुझे नन्हें बच्चे के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में नन्हें बच्चे के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने दोस्तों से प्यार हैं",
            "मुझे सच में अपने दोस्तों से प्यार हैं",
            "मुझे अपने दोस्त चाहिए",
            "मुझे सच में अपने दोस्त चाहिए",
            "मुझे अपने दोस्तों के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने दोस्तों के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने दोस्त अच्छे नहीं लगते हैं",
            "मुझे सच अपने दोस्त अच्छे नहीं लगते हैं",
            "मुझे अपने दोस्त नहीं चाहिए",
            "मुझे सच में अपने दोस्त नहीं चाहिए",
            "मुझे अपने दोस्तों के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने दोस्तों के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने शिक्षक अच्छे लगते हैं",
            "मुझे सच में अपने शिक्षक अच्छे लगते हैं",
            "मुझे अपने शिक्षक चाहिए",
            "मुझे सच में अपने शिक्षक चाहिए",
            "मुझे अपने शिक्षक के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपने शिक्षक के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपने शिक्षक अच्छे नहीं लगते हैं",
            "मुझे सच में अपने शिक्षक अच्छे नहीं लगते हैं",
            "मुझे अपने शिक्षक नहीं चाहिए",
            "मुझे सच में अपने शिक्षक नहीं चाहिए",
            "मुझे अपने शिक्षक के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपने शिक्षक के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे यह डॉक्टर अच्छे लगते हैं",
            "मुझे सच में यह डॉक्टर अच्छे लगते हैं",
            "मुझे डॉक्टर के पास जाना हैं",
            "मुझे सच में डॉक्टर के पास जाना हैं",
            "मुझे डॉक्टर के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में डॉक्टर के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे यह डॉक्टर अच्छे नहीं लगते हैं",
            "मुझे सच में यह डॉक्टर अच्छे नहीं लगते हैं",
            "मुझे डॉक्टर के पास नहीं जाना हैं",
            "मुझे सच में डॉक्टर के पास नहीं जाना हैं",
            "मुझे डॉक्टर के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में डॉक्टर के साथ कुछ और समय नहीं बिताना हैं"

    }, {"मुझे यह नरस अच्छी लगती हैं",
            "मुझे सच में यह नरस अच्छी लगती हैं",
            "मुझे नरस के पास जाना हैं",
            "मुझे सच में नरस के पास जाना हैं",
            "मुझे नरस के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में नरस के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे यह नरस अच्छी नहीं लगती हैं",
            "मुझे सच में यह नरस अच्छी नहीं लगती हैं",
            "मुझे नरस के पास नहीं जाना हैं",
            "मुझे सच में नरस के पास नहीं जाना हैं",
            "मुझे नरस के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में नरस के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपनी देख-रेख करनेवाली मौसी अच्छी लगती हैं",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी अच्छी लगती हैं",
            "मुझे अपनी देख-रेख करनेवाली मौसी चाहिए",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी चाहिए",
            "मुझे अपनी देख-रेख करनेवाली मौसी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे अपनी देख-रेख करनेवाली मौसी अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी अच्छी नहीं लगती हैं",
            "मुझे अपनी देख-रेख करनेवाली मौसी नहीं चाहिए",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी नहीं चाहिए",
            "मुझे अपनी देख-रेख करनेवाली मौसी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में अपनी देख-रेख करनेवाली मौसी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे वह अज नबी अच्छा लगता हैं",
            "मुझे सच में वह अज नबी अच्छा लगता हैं",
            "मुझे उस अज नबी से बात करनी हैं",
            "मुझे सच में उस अज नबी से बात करनी हैं",
            "मुझे उस अज नबी के साथ ज़्यादा समय बिताना हैं",
            "मुझे सच में उस अज नबी के साथ बहुत ज़्यादा समय बिताना हैं",
            "मुझे वह अज नबी अच्छा नहीं लगता हैं",
            "मुझे सच में वह अज नबी अच्छा नहीं लगता हैं",
            "मुझे उस अज नबी से बात नहीं करनी हूँ",
            "मुझे सच में उस अज नबी से बात नहीं करनी हैं",
            "मुझे उस अज नबी के साथ ज़्यादा समय नहीं बिताना हैं",
            "मुझे सच में उस अज नबी के साथ कुछ और समय नहीं बिताना हैं"

    },{"मुझे अपने आपसे प्यार हैं",
            "मुझे सच में अपने आपसे प्यार हैं",
            "मुझे आपको अपने बारे में बताना हैं",
            "मुझे सच में आपको अपने बारे में बताना हैं",
            "मुझे आपको अपने बारे में कुछ और बताना हैं",
            "मुझे सच में आपको अपने बारे में बहुत ज़्यादा बताना हैं",
            "मुझे अपने आपसे प्यार नहीं हैं",
            "मुझे सच में अपने आपसे प्यार नहीं  हैं",
            "मुझे आपको अपने बारे में नहीं बताना हैं",
            "मुझे सच में आपको अपने बारे में नहीं बताना हैं",
            "मुझे आपको अपने बारे में और नहीं बताना हैं",
            "मुझे सच में आपको अपने बारे में कुछ भी नहीं बताना हैं"

    }},{{"मुझे मेरा घर अच्छा लगता हैं",
            "मुझे सच में मेरा घर अच्छा लगता हैं",
            "मुझे अपने घर जाना हैं",
            "मुझे सच में अपने घर जाना हैं",
            "मुझे फिर से अपने घर जाना हैं",
            "मुझे सच में फिर से अपने घर जाना हैं",
            "मुझे मेरा घर अच्छा नहीं लगता हैं",
            "मुझे सच में मेरा घर अच्छा नहीं लगता हैं",
            "मुझे अपने घर नहीं जाना हैं",
            "मुझे सच में अपने घर नहीं जाना हैं",
            "मुझे फिर से अपने घर नहीं जाना हैं",
            "मुझे सच में फिर से अपने घर नहीं जाना हैं"

    },{"मुझे मेरी पाठशाला अच्छी लगती हैं",
            "मुझे सच में मेरी पाठशाला अच्छी लगती हैं",
            "मुझे पाठशाला जाना हैं",
            "मुझे सच में पाठशाला जाना हैं",
            "मुझे फिर से पाठशाला जाना हैं",
            "मुझे सच में फिर से पाठशाला जाना हैं",
            "मुझे मेरी पाठशाला अच्छी नहीं लगती हैं",
            "मुझे सच में मेरी पाठशाला अच्छी नहीं लगती हैं",
            "मुझे पाठशाला नहीं जाना हैं",
            "मुझे सच में पाठशाला नहीं जाना हैं",
            "मुझे फिर से पाठशाला नहीं जाना हैं",
            "मुझे सच में फिर से पाठशाला नहीं जाना हैं"

    },{"मुझे मॉल जाना अच्छा लगता हैं",
            "मुझे सच में मॉल जाना अच्छा लगता हैं",
            "मुझे मॉल जाना हैं",
            "मुझे सच में मॉल जाना हैं",
            "मुझे फिर से मॉल जाना हैं",
            "मुझे सच में फिर से मॉल जाना हैं",
            "मुझे मॉल जाना अच्छा नहीं लगता हैं",
            "मुझे सच में मॉल जाना अच्छा नहीं लगता हैं",
            "मुझे मॉल नहीं जाना हैं",
            "मुझे सच में मॉल नहीं जाना हैं",
            "मुझे फिर से मॉल नहीं जाना हैं",
            "मुझे सच में फिर से मॉल नहीं जाना हैं"

    },{"मुझे संग्रहालय जाना अच्छा लगता हैं",
            "मुझे सच में संग्रहालय जाना अच्छा लगता हैं",
            "मुझे संग्रहालय जाना हैं",
            "मुझे सच में संग्रहालय जाना हैं",
            "मुझे फिर से संग्रहालय जाना हैं",
            "मुझे सच में फिर से संग्रहालय जाना हैं",
            "मुझे संग्रहालय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में संग्रहालय जाना अच्छा नहीं लगता हैं",
            "मुझे संग्रहालय नहीं जाना हैं",
            "मुझे सच में संग्रहालय नहीं जाना हैं",
            "मुझे फिर से संग्रहालय नहीं जाना हैं",
            "मुझे सच में फिर से संग्रहालय नहीं जाना हैं"

    },{"मुझे होटल जाना अच्छा लगता हैं",
            "मुझे सच में होटल जाना अच्छा लगता हैं",
            "मुझे होटल जाना हैं",
            "मुझे सच में होटल जाना हैं",
            "मुझे फिर से होटल जाना हैं",
            "मुझे सच में फिर से होटल जाना हैं",
            "मुझे होटल जाना अच्छा नहीं लगता हैं",
            "मुझे सच में होटल जाना अच्छा नहीं लगता हैं",
            "मुझे होटल नहीं जाना हैं",
            "मुझे सच में होटल नहीं जाना हैं",
            "मुझे फिर से होटल नहीं जाना हैं",
            "मुझे सच में फिर से होटल नहीं जाना हैं"

    },{"मुझे थिएटर जाना अच्छा लगता हैं",
            "मुझे सच में थिएटर जाना अच्छा लगता हैं",
            "मुझे थिएटर जाना हैं",
            "मुझे सच में थिएटर जाना हैं",
            "मुझे फिर से थिएटर जाना हैं",
            "मुझे सच में फिर से थिएटर जाना हैं",
            "मुझे थिएटर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में थिएटर जाना अच्छा नहीं लगता हैं",
            "मुझे थिएटर नहीं जाना हैं",
            "मुझे सच में थिएटर नहीं जाना हैं",
            "मुझे फिर से थिएटर नहीं जाना हैं",
            "मुझे सच में फिर से थिएटर नहीं जाना हैं"

    },{"मुझे खेल के मैदान पर जाना अच्छा लगता हैं",
            "मुझे सच में खेल के मैदान पर जाना अच्छा लगता हैं",
            "मुझे खेल के मैदान पर जाना हैं",
            "मुझे सच में खेल के मैदान पर जाना हैं",
            "मुझे फिर से खेल के मैदान पर जाना हैं",
            "मुझे सच में फिर से खेल के मैदान पर जाना हैं",
            "मुझे खेल के मैदान पर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में खेल के मैदान पर जाना अच्छा नहीं लगता हैं",
            "मुझे खेल के मैदान पर नहीं जाना हैं",
            "मुझे सच में खेल के मैदान पर नहीं जाना हैं",
            "मुझे फिर से खेल के मैदान पर नहीं जाना हैं",
            "मुझे सच में फिर से खेल के मैदान पर नहीं जाना हैं"

    },{"मुझे बगीचे में जाना अच्छा लगता हैं",
            "मुझे सच में बगीचे में जाना अच्छा लगता हैं",
            "मुझे बगीचे में जाना हैं",
            "मुझे सच में बगीचे में जाना हैं",
            "मुझे फिर से बगीचे में जाना हैं",
            "मुझे सच में फिर से बगीचे में जाना हैं",
            "मुझे बगीचे में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में बगीचे में जाना अच्छा नहीं लगता हैं",
            "मुझे बगीचे में नहीं जाना हैं",
            "मुझे सच में बगीचे में नहीं जाना हैं",
            "मुझे फिर से बगीचे में नहीं जाना हैं",
            "मुझे सच में फिर से बगीचे में नहीं जाना हैं"

    },{"मुझे दुकान में जाना अच्छा लगता हैं",
            "मुझे सच में दुकान में जाना अच्छा लगता हैं",
            "मुझे दुकान में जाना हैं",
            "मुझे सच में दुकान में जाना हैं",
            "मुझे फिर से दुकान में जाना हैं",
            "मुझे सच में फिर से दुकान में जाना हैं",
            "मुझे दुकान में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में दुकान में जाना अच्छा नहीं लगता हैं",
            "मुझे दुकान में नहीं जाना हैं",
            "मुझे सच में दुकान में नहीं जाना हैं",
            "मुझे फिर से दुकान में नहीं जाना हैं",
            "मुझे सच में फिर से दुकान में नहीं जाना हैं"

    },{"मुझे अपने दोस्तों के घर जाना अच्छा लगता हैं",
            "मुझे सच में अपने दोस्तों के घर जाना अच्छा लगता हैं",
            "मुझे अपने दोस्त के घर जाना हैं",
            "मुझे सच में अपने दोस्त के घर जाना हैं",
            "मुझे फिर से अपने दोस्त के घर जाना हैं",
            "मुझे सच में फिर से अपने दोस्त के घर जाना हैं",
            "मुझे अपने दोस्तों के घर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दोस्तों के घर जाना अच्छा नहीं लगता हैं",
            "मुझे अपने दोस्त के घर नहीं जाना हैं",
            "मुझे सच में अपने दोस्त के घर नहीं जाना हैं",
            "मुझे फिर से अपने दोस्त के घर नहीं जाना हैं",
            "मुझे सच में फिर से अपने दोस्त के घर नहीं जाना हैं"

    },{"मुझे अपने रिश्तेदारों के घर जाना अच्छा लगता हैं",
            "मुझे सच में अपने रिश्तेदारों के घर जाना अच्छा लगता हैं",
            "मुझे अपने रिश्तेदार के घर जाना हैं",
            "मुझे सच में अपने रिश्तेदार के घर जाना हैं",
            "मुझे फिर से अपने रिश्तेदार के घर जाना हैं",
            "मुझे सच में फिर से अपने रिश्तेदार के घर जाना हैं",
            "मुझे अपने रिश्तेदारों के घर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने रिश्तेदारों के घर जाना अच्छा नहीं लगता हैं",
            "मुझे अपने रिश्तेदार के घर नहीं जाना हैं",
            "मुझे सच में अपने रिश्तेदार के घर नहीं जाना हैं",
            "मुझे फिर से अपने रिश्तेदार के घर नहीं जाना हैं",
            "मुझे सच में फिर से अपने रिश्तेदार के घर नहीं जाना हैं"

    },{"मुझे अस्पताल जाना अच्छा लगता हैं",
            "मुझे सच में अस्पताल जाना अच्छा लगता हैं",
            "मुझे अस्पताल जाना हैं",
            "मुझे सच में अस्पताल जाना हैं",
            "मुझे फिर से अस्पताल जाना हैं",
            "मुझे सच में फिर से अस्पताल जाना हैं",
            "मुझे अस्पताल जाना अच्छा नहीं लगता हैं",
            "मुझे सच में अस्पताल जाना अच्छा नहीं लगता हैं",
            "मुझे अस्पताल नहीं जाना हैं",
            "मुझे सच में अस्पताल नहीं जाना हैं",
            "मुझे फिर से अस्पताल नहीं जाना हैं",
            "मुझे सच में फिर से अस्पताल नहीं जाना हैं"

    },{"मुझे क्लिनिक जाना अच्छा लगता हैं",
            "मुझे सच में क्लिनिक जाना अच्छा लगता हैं",
            "मुझे क्लिनिक जाना हैं",
            "मुझे सच में क्लिनिक जाना हैं",
            "मुझे फिर से क्लिनिक जाना हैं",
            "मुझे सच में फिर से क्लिनिक जाना हैं",
            "मुझे क्लिनिक जाना अच्छा नहीं लगता हैं",
            "मुझे सच में क्लिनिक जाना अच्छा नहीं लगता हैं",
            "मुझे क्लिनिक नहीं जाना हैं",
            "मुझे सच में क्लिनिक नहीं जाना हैं",
            "मुझे फिर से क्लिनिक नहीं जाना हैं",
            "मुझे सच में फिर से क्लिनिक नहीं जाना हैं"

    },{"मुझे वाचना लय जाना अच्छा लगता हैं",
            "मुझे सच में वाचना लय जाना अच्छा लगता हैं",
            "मुझे वाचना लय जाना हैं",
            "मुझे सच में वाचना लय जाना हैं",
            "मुझे फिर से वाचना लय जाना हैं",
            "मुझे सच में फिर से वाचना लय जाना हैं",
            "मुझे वाचना लय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में वाचना लय जाना अच्छा नहीं लगता हैं",
            "मुझे वाचना लय नहीं जाना हैं",
            "मुझे सच में वाचना लय नहीं जाना हैं",
            "मुझे फिर से वाचना लय नहीं जाना हैं",
            "मुझे सच में फिर से वाचना लय नहीं जाना हैं"

    },{"मुझे छत पर जाना अच्छा लगता हैं",
            "मुझे सच में छत पर जाना अच्छा लगता हैं",
            "मुझे छत पर जाना हैं",
            "मुझे सच में छत पर जाना हैं",
            "मुझे फिर से छत पर जाना हैं",
            "मुझे सच में फिर से छत पर जाना हैं",
            "मुझे छत पर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में छत पर जाना अच्छा नहीं लगता हैं",
            "मुझे छत पर नहीं जाना हैं",
            "मुझे सच में छत पर नहीं जाना हैं",
            "मुझे फिर से छत पर नहीं जाना हैं",
            "मुझे सच में फिर से छत पर नहीं जाना हैं"

    }},{{"मुझे समय पर रहना अच्छा लगता हैं",
            "मुझे सच में समय पर रहना अच्छा लगता हैं",
            "मुझे कुछ समय चाहिए",
            "मुझे सच में कुछ समय चाहिए",
            "मुझे कुछ और समय चाहिए",
            "मुझे सच में कुछ और समय चाहिए",
            "मुझे समय पर रहना अच्छा नहीं लगता हैं",
            "मुझे सच में समय पर रहना अच्छा नहीं लगता हैं",
            "मुझे और समय नहीं चाहिए",
            "मुझे सच में और समय नहीं चाहिए",
            "मुझे थोड़ा और समय नहीं चाहिए",
            "मुझे सच में थोड़ा और समय नहीं चाहिए"

    },{"मुझे सप्ताह के दिनों के बारे में जानना अच्छा लगता है",
            "मुझे सच में सप्ताह के दिनों के बारे में जानना अच्छा लगता हैं",
            "मुझे सप्ताह के दिनों के बारे में जानना हैं",
            "मुझे सच में सप्ताह के दिनों के बारे में जानना हैं",
            "मुझे सप्ताह के दिनों के बारे में कुछ और जानना हैं",
            "मुझे सच में सप्ताह के दिनों के बारे में कुछ और जानना हैं",
            "मुझे सप्ताह के दिनों के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे सच में सप्ताह के दिनों के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे सप्ताह के दिनों के बारे में नहीं जानना हैं",
            "मुझे सच में सप्ताह के दिनों के बारे में नहीं जानना हैं",
            "मुझे सप्ताह के दिनों के बारे में और नहीं जानना हैं",
            "मुझे सच में सप्ताह के दिनों के बारे में कुछ भी नहीं जानना हैं",

    },{"मुझे साल के महीनों के बारे में जानना अच्छा लगता हैं",
            "मुझे सच में साल के महीनों के बारे में जानना अच्छा लगता हैं",
            "मुझे साल के महीनों के बारे में जानना हैं",
            "मुझे सच में साल के महीनों के बारे में जानना  हैं",
            "मुझे साल के महीनों के बारे में कुछ और जानना हैं",
            "मुझे सच में साल के महीनों के बारे में कुछ और जानना हैं",
            "मुझे साल के महीनों के बारे में  जानना अच्छा नहीं लगता हैं",
            "मुझे सच में साल के महीनों के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे साल के महीनों के बारे में नहीं जानना हैं",
            "मुझे सच में साल के महीनों के बारे में नहीं जानना हैं",
            "मुझे साल के महीनों के बारे में और नहीं जानना हैं",
            "मुझे सच में साल के महीनों के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे मौसम के बारे में जानना अच्छा लगता हैं",
            "मुझे सच में मौसम के बारे में जानना अच्छा लगता हैं",
            "मुझे मौसम के बारे में जानना हैं",
            "मुझे सच में मौसम के बारे में जानना हैं",
            "मुझे मौसम के बारे में कुछ और जानना हैं",
            "मुझे सच में मौसम के बारे में कुछ और जानना हैं",
            "मुझे मौसम के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे सच में मौसम के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे मौसम के बारे में नहीं जानना हैं",
            "मुझे सच में मौसम के बारे में नहीं जानना हैं",
            "मुझे मौसम के बारे में और नहीं जानना हैं",
            "मुझे सच में मौसम के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे ऋतुओं के बारे में जानना अच्छा लगता हैं",
            "मुझे सच में ऋतुओं के बारे में जानना अच्छा लगता हैं",
            "मुझे ऋतुओं के बारे में जानना हैं",
            "मुझे सच में ऋतुओं के बारे में जानना हैं",
            "मुझे ऋतुओं के बारे में कुछ और जानना हैं",
            "मुझे सच में ऋतुओं के बारे में कुछ और जानना हैं",
            "मुझे ऋतुओं के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे सच में ऋतुओं के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे ऋतुओं के बारे में नहीं जानना हैं",
            "मुझे सच में ऋतुओं के बारे में नहीं जानना हैं",
            "मुझे ऋतुओं के बारे में और नहीं जानना हैं",
            "मुझे सच में ऋतुओं के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे त्योहारों और छुट्टियों के बारे में जानना अच्छा लगता हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में जानना अच्छा लगता हैं",
            "मुझे त्योहारों और छुट्टियों के बारे में जानना हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में जानना हैं",
            "मुझे त्योहारों और छुट्टियों के बारे में कुछ और जानना हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में कुछ और जानना हैं",
            "मुझे त्योहारों और छुट्टियों के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में जानना अच्छा नहीं लगता हैं",
            "मुझे त्योहारों और छुट्टियों के बारे में नहीं जानना हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में नहीं जानना हैं",
            "मुझे त्योहारों और छुट्टियों के बारे में और नहीं जानना हैं",
            "मुझे सच में त्योहारों और छुट्टियों के बारे में कुछ भी नहीं जानना हैं"

    },{"मुझे जन्मदिन मनाने अच्छे लगते हैं",
            "मुझे सच में जन्मदिन मनाने अच्छे लगते हैं",
            "मुझे जन्मदिन मनाने हैं",
            "मुझे सच में जन्मदिन मनाने हैं",
            "मुझे कुछ और जन्मदिन मनाने हैं",
            "मुझे सच में कुछ और जन्मदिन मनाने हैं",
            "मुझे जन्मदिन मनाने अच्छे नहीं लगते हैं",
            "मुझे सच में जन्मदिन मनाने अच्छे नहीं लगते हैं",
            "मुझे जन्मदिन नहीं मनाने हैं",
            "मुझे सच में जन्मदिन नहीं मनाने हैं",
            "मुझे और जन्मदिन नहीं मनाने हैं",
            "मुझे सच में कोई भी जन्मदिन नहीं मनाने हैं"

    }}, {{"मेरा नाम ",
            "मेरा नाम ",
            "मेरा ई- मेल ",
            "मेरा ई- मेल ",
            "मेरे केयर गिवर का संपर्क नंबर ",
            "मेरे केयर गिवर का संपर्क नंबर ",
            "मेरे केयर गिवर का नाम ",
            "मेरे केयर गिवर का नाम ",
            "मेरे घर का पता ",
            "मेरे घर का पता ",
            "मेरा रक्त वर्ग ",
            "मेरा रक्त वर्ग "},
            {},{},{},{},
            {"मुझे दवाई लेना अच्छा लगता हैं",
                    "मुझे सच में दवाई लेना अच्छा लगता हैं",
                    "मुझे दवाई चाहिए",
                    "मुझे सच में दवाई चाहिए",
                    "मुझे और दवाईयाँ चाहिए",
                    "मुझे सच में कुछ और दवाईयाँ चाहिए",
                    "मुझे दवाई लेना अच्छा नहीं लगता हैं",
                    "मुझे सच में दवाई लेना अच्छा नहीं लगता हैं",
                    "मुझे दवाई नहीं चाहिए",
                    "मुझे सच में दवाई नहीं चाहिए",
                    "मुझे और दवाईयाँ नहीं चाहिए",
                    "मुझे सच में कुछ और दवाईयाँ नहीं चाहिए"

            },{"मुझे बैंडेज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में बैंडेज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक बैंडेज चाहिए",
            "मुझे सच में एक बैंडेज चाहिए",
            "मुझे और बैंडेज चाहिए",
            "मुझे सच में कुछ और बैंडेज चाहिए",
            "मुझे बैंडेज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में बैंडेज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे बैंडेज नहीं चाहिए",
            "मुझे सच में बैंडेज नहीं चाहिए",
            "मुझे और बैंडेज नहीं चाहिए",
            "मुझे सच में बैंडेज बिल्कुल नहीं चाहिए",

    },{"मुझे पानी पीना अच्छा लगता हैं",
            "मुझे सच में पानी पीना अच्छा लगता हैं",
            "मुझे थोड़ा पानी चाहिए",
            "मुझे सच में थोड़ा पानी चाहिए",
            "मुझे और पानी चाहिए",
            "मुझे थोड़ा और पानी चाहिए",
            "मुझे पानी पीना अच्छा नहीं लगता हैं",
            "मुझे सच में पानी पीना अच्छा नहीं लगता हैं",
            "मुझे पानी नहीं चाहिए",
            "मुझे सच में पानी नहीं चाहिए",
            "मुझे और पानी नहीं चाहिए",
            "मुझे सच में थोड़ा भी पानी नहीं चाहिए"
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
    }}};
}