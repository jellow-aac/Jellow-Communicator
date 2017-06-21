package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsource.idc.jellow.Models.LayerThreeEnglishSpeech;
import com.dsource.idc.jellow.Models.LayerThreeHindiSpeech;
import com.dsource.idc.jellow.Utility.IndexSorter;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.UserDataMeasure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class Layer3Activity extends AppCompatActivity {
    private final boolean DISABLE_ACTION_BTNS = true;
    private int mCk = 0, mCy = 0, mCm = 0, mCd = 0, mCn = 0, mCl = 0;

    private int image_flag = -1, flag_keyboard = 0, mActionBtnClickCount, more_count = 0;
    private ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    private EditText et;
    private KeyListener originalKeyListener;
    private TextToSpeech mTts;
    private RecyclerView mRecyclerView;
    private LinearLayout mMenuItemLinearLayout;
    private String[] myMusic;
    private int[] mColor;
    private UserDataMeasure userDataMeasure;
    private int mLevelOneItemPos, mLevelTwoItemPos, mLevelThreeItemPos = -1, mSelectedItemAdapterPos = -1;
    private boolean mShouldReadFullSpeech = false;
    private ArrayList<View> mRecyclerItemsViewList;
    private SessionManager mSession;
    private Integer[] count = new Integer[100];
    private int[] sort = new int[100];
    private int count_flag = 0;
    private DataBaseHelper myDbHelper;
    private String[] side = new String[100];
    private String[] below = new String[100];
    private String[][][][] tempSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial1);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        getSupportActionBar().setElevation(0);
        myDbHelper = new DataBaseHelper(this);
        mColor = getResources().getIntArray(R.array.arrActionBtnColors);
        side = getResources().getStringArray(R.array.arrActionSpeech);
        below = getResources().getStringArray(R.array.arrNavigationSpeech);
        mSession = new SessionManager(this);
        more_count = 0;
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        if(mSession.getLanguage() == 0)
            tempSpeech = new LayerThreeEnglishSpeech().getLayerThreeSpeechEnglish();
        else
            tempSpeech = new LayerThreeHindiSpeech().getlayerThreeSpeechHindi();
        try {
            myDbHelper.createDataBase();
            myDbHelper.openDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch (SQLException sqle) {
            throw sqle;
        }

        mRecyclerItemsViewList = new ArrayList<>(100);
        while(mRecyclerItemsViewList.size() <= 100) mRecyclerItemsViewList.add(null);

        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTts.setEngineByPackageName("com.google.android.tts");
                    new BackgroundSpeechOperationsAsync().execute("");
                }
            }
        });
        mTts.setSpeechRate((float) mSession.getSpeed() / 50);
        mTts.setPitch((float) mSession.getPitch() / 50);
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
                            resetActionButtons(-1);
                            resetRecyclerAllItems();
                            mActionBtnClickCount = 0;
                            setMenuImageBorder(v, true);
                            mShouldReadFullSpeech = true;
                            mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
                            mLevelThreeItemPos = mRecyclerView.getChildLayoutPosition(view);
                            if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)))
                                mTts.speak(myMusic[mLevelThreeItemPos], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                mTts.speak(myMusic[sort[mLevelThreeItemPos]], TextToSpeech.QUEUE_FLUSH, null);

                            incrementTouchCountOfItem(mLevelThreeItemPos);
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
                            }
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
            count = new Integer[tempSpeech[mLevelOneItemPos][mLevelTwoItemPos].length];
            for (int j = 0; j < tempSpeech[mLevelOneItemPos][mLevelTwoItemPos].length; j++) {
                count[j] = Integer.parseInt(st.nextToken());
            }
            IndexSorter<Integer> is = new IndexSorter<Integer>(count);
            is.sort();
            Integer[] indexes = new Integer[tempSpeech[mLevelOneItemPos][mLevelTwoItemPos].length];
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                g++;
            }
            for (int j = 0; j < count.length; j++)
                sort[j] = indexes[j];
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
            mRecyclerView.setAdapter(new Layer_three_Adapter(this, mLevelOneItemPos, mLevelTwoItemPos, sort));
        } else if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) || (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4))) {
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
            mRecyclerView.setAdapter(new Layer_three_Adapter(this, mLevelOneItemPos, mLevelTwoItemPos, sort));
        } else {
            count_flag = 0;
            myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setImageResource(R.drawable.homepressed);
                mTts.speak(below[0], TextToSpeech.QUEUE_FLUSH, null);
                more_count = 0;
                mShouldReadFullSpeech = false;
                image_flag = -1;
                Intent intent = new Intent(Layer3Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                } else {
                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.backpressed);
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
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
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
                    // Hithe soft keyboard.
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
                back.setImageResource(R.drawable.backpressed);
                if (flag_keyboard == 1) {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    changeTheActionButtons(!DISABLE_ACTION_BTNS);
                } else if (more_count > 0) {
                    more_count -= 1;
                    myMusic_function(mLevelOneItemPos, mLevelTwoItemPos);
                    mRecyclerView.setAdapter(new Layer_three_Adapter(Layer3Activity.this, mLevelOneItemPos, mLevelTwoItemPos, sort));
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
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCk == 1) {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][1], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][1], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 0;

                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][0], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][0], TextToSpeech.QUEUE_FLUSH, null);
                        mCk = 1;
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
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][7], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][7], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 0;
                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][6], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][6], TextToSpeech.QUEUE_FLUSH, null);
                        mCd = 1;
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
                        mTts.speak(side[3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        mTts.speak(side[2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mCy == 1) {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][3], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][3], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 0;
                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][2], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][2], TextToSpeech.QUEUE_FLUSH, null);
                        mCy = 1;
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
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][9], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][9], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 0;
                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][8], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][8], TextToSpeech.QUEUE_FLUSH, null);
                        mCn = 1;
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
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][5], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][5], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 0;
                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][4], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][4], TextToSpeech.QUEUE_FLUSH, null);
                        mCm = 1;
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
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][11], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][11], TextToSpeech.QUEUE_FLUSH, null);
                        mCl = 0;
                    } else {
                        if (count_flag == 1)
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][sort[mLevelThreeItemPos]][10], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            mTts.speak(tempSpeech[mLevelOneItemPos][mLevelTwoItemPos][mLevelThreeItemPos][10], TextToSpeech.QUEUE_FLUSH, null);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    private class BackgroundSpeechOperationsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                mTts.setLanguage(new Locale("eng", "IND"));
            } catch (Exception e) {
                Thread.interrupted();
            }
            return "Executed";
        }
    }
}


