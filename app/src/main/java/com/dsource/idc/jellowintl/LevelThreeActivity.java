package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_DoubleClick;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.models.LevelThreeVerbiageModel;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowintl.utility.IndexSorter;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class LevelThreeActivity extends AppCompatActivity {
    private final boolean DISABLE_EXPR_BTNS = true;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = 0, mFlgYes = 0, mFlgMore = 0, mFlgDntLike = 0, mFlgNo = 0,
            mFlgLess = 0;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = -1;
    /* This flag indicates keyboard is open or not, 0 indicates is not open.*/
    private int mFlgKeyboard = 0;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount;
    /*Image views which are visible on the layout such as six expressive buttons, mNavigationBtnTxt navigation
      buttons and speak button when custom keyboard input layout is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack, mIvTTs;
    /*Input text view to speak custom text.*/
    private EditText mEtTTs;
    private KeyListener originalKeyListener;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private RecyclerView mRecyclerView;
    /*This variable indicates index of category icon selected in level one, two and three respectively*/
    private int mLevelOneItemPos, mLevelTwoItemPos, mLevelThreeItemPos = -1;
    /*This variable indicates index of category icon in adapter in level 1. This variable is
     different than mLevelOneItemPos. */
    private int mSelectedItemAdapterPos = -1;
    /* This flag is set to true, when user press the category icon and reset when user press the home
     button. When user presses expressive button and mShouldReadFullSpeech = true, it means that user
     is already selected a category icon and user intend to speak full sentence verbiage for a
     selected icon.*/
    private boolean mShouldReadFullSpeech = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*This variable stores current tap mArrIconTapCount to every category icon populated in recycler view.*/
    private Integer[] mArrIconTapCount = new Integer[100];
    /*This variable stores array of index of category icons sorted by tap count*/
    private int[] mArrSort = new int[100];
    private int count_flag = 0;
    private DataBaseHelper myDbHelper;
    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mSpeechTxt, mExprBtnTxt, mNavigationBtnTxt;
    /*Below list stores the verbiage that are spoken when category icon + expression buttons
    pressed in conjunction*/
	ArrayList <ArrayList <String>> mNewVerbTxt;
    private String actionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag)));
        // when layout is loaded on activity, using the tag attribute of a parent view in layout
        // the device size is identified. If device size is large (10' tablets) enable the
        // hardware acceleration. As seen in testing device, scrolling recycler items on 10' tab
        // have jagged views (lags in scrolling) hence hardware acceleration is enabled.
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // set app locale which is set in settings by user.
        myDbHelper = new DataBaseHelper(this);
        mLevelOneItemPos = getIntent().getExtras().getInt(getString(R.string.level_one_intent_pos_tag));
        mLevelTwoItemPos = getIntent().getExtras().getInt(getString(R.string.level_2_item_pos_tag));
        loadArraysFromResources();

        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            Crashlytics.logException(e);
        }

        // Set the capacity of mRecyclerItemsViewList list to total number of category icons to be
        // populated on the screen.
        mRecyclerItemsViewList = new ArrayList<>(100);
        while(mRecyclerItemsViewList.size() <= 100) mRecyclerItemsViewList.add(null);
        initializeLayoutViews();
        initializeRecyclerViewAdapter();
        initializeViewListeners();
        /**
       * If the intent is fired from the {@link SearchActivity} then disable the back button
       * because back button will close the app (as Search Activity clears the back stack)
       * and the highlight function will highlight the required icon.
       * @Author Ayaz Alam
       * */
      if(getIntent().getExtras().getString(getString(R.string.from_search))!=null) {
            if (getIntent().getExtras().getString(getString(R.string.from_search))
                    .equals(getString(R.string.search_tag))) {
                highlightSearchedItem();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager session = new SessionManager(this);
        if(!isAnalyticsActive()){
            resetAnalytics(this, session.getCaregiverNumber().substring(1));
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        // Start measuring user app screen timer .
        startMeasuring();
        if(!session.getToastMessage().isEmpty()) {
            Toast.makeText(this, session.getToastMessage(), Toast.LENGTH_SHORT).show();
            session.setToastMessage("");
        }
    }

    /**
     * This function is responsible for the Highlighting of the searched item from the
     * search activity
     * @Author Ayaz Alam
     * **/
    RecyclerView.OnScrollListener scrollListener;
    private void highlightSearchedItem() {

        //Getting the sorted index of the icon from intent extra of the invoked activity
        final int sortedIndex=getSortedIndex(getIntent().getExtras().getInt(getString(R.string.search_parent_2)));
        //To get gridSize
                int gridSize;
                if(new SessionManager(this).getGridSize()==1)
                    gridSize=8;
                else gridSize=2;
                //Scroll to the position if the icon is not present in first grid
                if(sortedIndex>gridSize) {
                    scrollListener =null;
                    scrollListener = getListener(sortedIndex);
                    mRecyclerView.addOnScrollListener(scrollListener);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,sortedIndex);
                }
                else
                    setSearchHighlight(sortedIndex);
    }
    /**
     * This functions returns a scroll scrollListener which triggers the setHighlight function
     * when the scrolling is done
     * @Author Ayaz Alam
     * */
    private RecyclerView.OnScrollListener getListener(final int index) {
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                ;//Wait untill scrolling
                else if(newState==RecyclerView.SCROLL_STATE_IDLE)
                    setSearchHighlight(index);//Try highlighting the view after scrolling
                }};
        return scrollListener;
    }

    /**
     * This function is responsible for highlighting the view
     * @param pos is the postion of view to be highlighted
     * */
    ViewTreeObserver.OnGlobalLayoutListener populationDoneListener;
    public void setSearchHighlight(final int pos)
    {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        populationDoneListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View searchedView = mRecyclerItemsViewList.get(pos);
                if(searchedView==null) {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,pos );
                    return;
                    }
                GradientDrawable gd = (GradientDrawable) searchedView.findViewById(R.id.borderView).getBackground();
                gd.setColor(ContextCompat.getColor(getApplicationContext(), R.color.search_highlight));
                Log.d("Ayaz", "Step 4: Background is set and removing the scrollListener");
                mRecyclerView.removeOnScrollListener(scrollListener);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
            }
        };
        //Adding the scrollListener to the mRecycler to listen onPopulated callBack
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        SessionManager session = new SessionManager(this);
        long sessionTime = validatePushId(session.getSessionCreatedAt());
        session.setSessionCreatedAt(sessionTime);
        // Stop measuring user app screen timer .
        stopMeasuring("LevelThreeActivity");
    }

    @Override
    protected void onDestroy() {
        mRecyclerView = null;
        mRecyclerItemsViewList = null;
        mSpeechTxt = mExprBtnTxt = mNavigationBtnTxt = null;
        mArrSort = null; mArrIconTapCount = null;
        myDbHelper = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.feedback:
                AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                boolean isAccessibilityEnabled = am.isEnabled();
                boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
                if(isAccessibilityEnabled && isExploreByTouchEnabled) {
                    startActivity(new Intent(this, FeedbackActivityTalkback.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available in this version.</p>
     * */
    private void initializeLayoutViews() {
        mIvLike = findViewById(R.id.ivlike);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvMore = findViewById(R.id.ivadd);
        mIvLess = findViewById(R.id.ivminus);
        mIvYes = findViewById(R.id.ivyes);
        mIvNo = findViewById(R.id.ivno);
        mIvHome = findViewById(R.id.ivhome);
        mIvBack = findViewById(R.id.ivback);
        mIvKeyboard = findViewById(R.id.keyboard);
        mEtTTs = findViewById(R.id.et);
        //Initially custom input text is invisible
        mEtTTs.setVisibility(View.INVISIBLE);
        mIvTTs = findViewById(R.id.ttsbutton);
        //Initially custom input text speak button is invisible
        mIvTTs.setVisibility(View.INVISIBLE);

        ViewCompat.setAccessibilityDelegate(mIvLike, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvYes, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvMore, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvDontLike, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvNo, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvBack, new TalkbackHints_SingleClick());

        originalKeyListener = mEtTTs.getKeyListener();
        // Set it to null - this will make to the field non-editable
        mEtTTs.setKeyListener(null);

        mRecyclerView = findViewById(R.id.recycler_view);
        // Initiate 3 columns in Recycler View.
        //This code is to decide the speed of the Scrolling
        // which grid size is 3 then scrolling is fast as compared to the 9 .
        SessionManager sessionManager=new SessionManager(this);
        int iconCount=sessionManager.getGridSize();
        if(iconCount!=0)
        mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 3,iconCount));
        else //Default speed is 3 icons speed
            mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 3,3));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
    }

    /**
     * <p>This function will initialize the adapter for recycler view.
     * As per the category icon selected in the level one {@link MainActivity} and
     * level two {@link LevelTwoActivity}, category icons are populated in this level.
     * Also, if any category uses preferences then in that category icons are arranged
     * using preferences. If a category not uses preferences then in that category icons are
     * populated directly. "Most tapped category icon will have highest preference value",
     * in this fashion preferences are defined for category icons.
     * In level three, all category icon preferences are stored in SQLite database.
     * Preferences are stored in the from comma separated values string.
     *  e.g. "5,4,9,2,5"
     * This preference string is retrieved from database using category icon selected in level
     * one {@link MainActivity} and level two {@link LevelTwoActivity}.
     * Then each value in preference string is converted into individual tokens and
     * followed by storing it into arrays. This array is known as Tap count array. Value
     * in 0th index of tap count array is tap count (number of times user tapped) for 0th element
     * in adapter array/speech array.
     *  e.g mArrIconTapCount = [5,4,9,2,5]
     * Preferences are applied to a category during adapter setup only. To apply preferences to a
     * category below steps to be followed sequentially:
     *   1. Retrieve stored preferences from database as "savedString" for selected category.
     *   2. Convert savedString into tokens and then store individual tokens "savedString"
     *      into preference array.
     *   3. From preference array create index array. Index array is a integer array, it
     *      has index of most-preferred/most-tapped element first,
     *      then followed by second most-preferred/most-tapped element index and so on.
     *      e.g. Consider preference array as
     *      [5, 4, 9 , 2, 5] then its index array is [2, 0, 4, 1, 3].
     *   4. Then speech and below text array elements are rearranged using index array. In this
     *      step, Speech and below text array elements are arranged in such a way that most
     *      used icon will appear first in the category.</p>
     * */
    private void initializeRecyclerViewAdapter() {
        // Retrieve preference string stored in SQlite db for given category using category icons
        // selected in level one and level two. Database will return preference string if exist
        // otherwise it will return value "false".
        String savedString = myDbHelper.getLevel(mLevelOneItemPos, mLevelTwoItemPos);

        if(savedString.equals("false") &&
                checkNewIfNewCategory(mLevelOneItemPos+"," + mLevelTwoItemPos)) {
            savedString = "";
            for (int i = 0; i < 100; ++i)
                savedString = savedString.concat("0,");
        }

        // savedString is equal to "false" then load level three category icons without any sort/preferences
        if (!savedString.equals("false")) {
            count_flag = 1;
            // convert savedString into individual tokens.
            StringTokenizer st = new StringTokenizer(savedString, ",");

            // create tap count array (mArrIconTapCount) of user taps using individual tokens
            mArrIconTapCount = new Integer[mNewVerbTxt.size()];
            for (int j = 0; j < mNewVerbTxt.size(); j++) {
                mArrIconTapCount[j] = Integer.parseInt(st.nextToken());
            }

            //create sort array using tap count array (mArrIconTapCount)
            IndexSorter<Integer> is = new IndexSorter<Integer>(mArrIconTapCount);
            is.sort();
            Integer[] indexes = new Integer[mNewVerbTxt.size()];

            //get index array for given tap count array(mArrIconTapCount)
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                g++;
            }
            for (int j = 0; j < mArrIconTapCount.length; j++)
                mArrSort[j] = indexes[j];

            // load speech array directly.
            retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);
            //setup adapter.
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mLevelOneItemPos,
                    mLevelTwoItemPos, mArrSort));

        // saved string is == "false" then load level three category icons without sort/preferences.
        // Categories given below are in if condition respectively:
        // Fun -> TV, fun -> Music,
        // Time & weather -> (Time, Day, month, Weather, Season) and
        // Learning -> Money, should load always without sorting
        } else if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                        mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4))
                || (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9)) {
              retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);

            // create sort array to sequentially arrange icons without any preferences
            for (int i=0; i< mNewVerbTxt.size();i++)
                mArrSort[i] = i;
            //setup adapter
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mLevelOneItemPos,
                    mLevelTwoItemPos, mArrSort));
        } else {
            count_flag = 0;
            retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);
        }
    }

    private boolean checkNewIfNewCategory(String newCatIndices) {
        switch(newCatIndices){
            case"1,9":
            case"6,0":
            case"6,1":
            case"6,2":
            case"6,3":
            case"6,4":
            case"6,5":
            case"6,6":
            case"6,7":
            case"6,8":
            case"6,9":
            case"6,10":
            case"6,11":
            case"6,12":
            case"6,13":
            case"6,14":
            case"6,15":
             return true;
            default:
                return false;
        }
    }

    /**
     * <p>This function will initialize the action listeners to the views which are populated on
     * on this activity.</p>
     * */
    private void initializeViewListeners() {
        initRecyclerViewListeners();
        initBackBtnListener();
        initHomeBtnListener();
        initKeyboardBtnListener();
        initLikeBtnListener();
        initDontLikeBtnListener();
        initYesBtnListener();
        initNoBtnListener();
        initMoreBtnListener();
        initLessBtnListener();
        initTTsBtnListener();
        initTTsEditTxtListener();
    }

    /**
     * <p>This function initializes {@link RecyclerTouchListener} and
     * {@link RecyclerView.OnChildAttachStateChangeListener} for recycler view.
     * {@link RecyclerTouchListener} is a custom defined Touch event scrollListener class.
     * {@link RecyclerView.OnChildAttachStateChangeListener} is defined to manage view state of
     * recycler child view. It is useful to retain current state of child, when recycler view is scrolled
     * and recycler child views are recycled for memory usage optimization.</p>
     * */
    private void initRecyclerViewListeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                LinearLayout menuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
                menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tappedCategoryItemEvent(view, v, position);
                    }
                });
            }
            @Override public void onLongClick(View view, int position) {}
        }));

        // When user scrolls into category, the child views are attached and detached from
        // recycler view. Also, the child views in recycler view have scrolled
        // off-screen are kept for reuse. Many times the reused view is assigned to
        // on-screen view. In our app this behaviour be can seen as follows:
        // When user scrolls into category, the child views are attached and detached from
        // recycler view. Also, the child views in recycler view have scrolled
        // off-screen are kept for reuse. Many times the reused view is assigned to
        // on-screen view. In our app this behaviour be can seen as follows:
        // Select "Dog" category icon in "Learning -> Animals & birds" then scrolled it off-screen
        // you will see selection border is appeared to another on-screen child of recycler view.
        // To overcome this situation a global list of state to every child of recycler view
        // is maintained.
        // When view is detached from recycler view it is removed from global list (mRecyclerItemsViewList)
        // and when child is attached to recycler view it is added to global list.
        // If a category icon is selected and it is scrolled off-screen, other on-screen view reusing
        // same view will get selection border so its border is removed first before removing from
        // global list.
        // When recycler view is scrolled, every newly attached child is checked if it is selected
        // previously or not. If the child is selected and it is reattached to recycler view then
        // set its border.
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), view);
                if(mRecyclerItemsViewList.contains(view) && mSelectedItemAdapterPos > -1 &&
                        mRecyclerView.getChildLayoutPosition(view) == mSelectedItemAdapterPos)
                    setMenuImageBorder(view, true);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                setMenuImageBorder(view, false);
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), null);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "back" button.
     * When pressed navigation back button:
     *  a) If user is using custom keyboard input text then custom keyboard input text layout
     *     is closed.
     *  b) If user is not using custom keyboard input text then current level is closed returning
     *  successful closure (RESULT_OK) of a screen. User will returned back to {@link LevelTwoActivity}.
     * </p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mNavigationBtnTxt[1]);
                mIvTTs.setImageResource(R.drawable.ic_search_list_speaker);
                mIvBack.setImageResource(R.drawable.back_pressed);
                //when mFlgKeyboardOpened is set to 1, it means user is using custom keyboard input
                // text and system keyboard is visible.
                if (mFlgKeyboard == 1) {
                    // As user is using custom keyboard input text and then back button is pressed,
                    // user intent to close custom keyboard input text so below steps will follow:
                    // a) set keyboard button to unpressed state.
                    // b) set back button to pressed state.
                    // c) hide custom keyboard input text view.
                    // d) show category icons.
                    // e) set flag mFlgKeyboardOpened = false, as user not using custom keyboard input
                    //    anymore.
                    // e) retain expressive button state as they were before custom keyboard input text
                    //    open.
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mIvBack.setImageResource(R.drawable.back_pressed);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    // after closing keyboard, then enable all expressive buttons
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    if(mLevelThreeItemPos != -1)
                        retainExpressiveButtonStates();
                //when back button is pressed and if mFlgKeyboard == 0, then custom keyboard
                // input text is not open and user intends to close current screen. Then simply
                // close activity and after setting result code. The result code is useful
                // to identify for returning activity that user is returned by pressing "back" button.
                } else {
                    mIvBack.setImageResource(R.drawable.back_pressed);
                    String str = getIntent().getExtras().getString(getString(R.string.from_search));
                    if(str != null && !str.isEmpty() && str.equals(getString(R.string.search_tag))){
                        Intent intent = new Intent(LevelThreeActivity.this, LevelTwoActivity.class);
                        intent.putExtra(getString(R.string.level_one_intent_pos_tag), mLevelOneItemPos);
                        intent.putExtra("search_and_back", true);
                        intent.putExtra(getString(R.string.intent_menu_path_tag), getIntent().
                                getExtras().getString(getString(R.string.intent_menu_path_tag)));
                        startActivity(intent);
                        finish();
                    }else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                //Firebase event
                singleEvent("Navigation","Back");
                showActionBarTitle(true);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation home button.
     * When user press this button user navigated to {@link MainActivity} with
     *  every state of views is like app launched as fresh. Action bar title is set
     *  to "home"</p>
     * */
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speakSpeech(mNavigationBtnTxt[0]);
                    }
                }).start();
                //When home is tapped in this activity it will close all other activities and
                // user is redirected/navigated to MainActivity and gotoHome() method is called.
                // As Firebase home event is defined in gotoHome() function of mainActivity.
                mIvHome.setImageResource(R.drawable.home_pressed);
                mIvKeyboard.setImageResource(R.drawable.keyboard);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(getString(R.string.goto_home), true);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_CANCELED);
                        finishAffinity();
                    }
                },100);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "keyboard" button.
     * {@link LevelThreeActivity} navigation keyboard button either enable or disable
     * the custom keyboard input text layout.
     * When custom keyboard input text layout is enabled using keyboard button, is visible to user
     * and action bar title set to "keyboard".
     * When custom keyboard input text layout is disabled using keyboard button, the state of the
     * {@link LevelThreeActivity} retrieved as it was before opening custom keyboard
     * input text layout.
     * */
    private void initKeyboardBtnListener() {
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strKeyboard = getString(R.string.keyboard);
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                //Firebase event
                singleEvent("Navigation","Keyboard");
                mIvTTs.setImageResource(R.drawable.ic_search_list_speaker);
                //when mFlgKeyboardOpened is set to 1, it means user is using custom keyboard input
                // text and system keyboard is visible.
                if (mFlgKeyboard == 1) {
                    // As user is using custom keyboard input text and then press the keyboard button,
                    // user intent to close custom keyboard input text so below steps will follow:
                    // a) set keyboard button to unpressed state.
                    // b) hide custom keyboard input text.
                    // c) show category icons
                    // d) hide custom keyboard input text
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    if(mLevelThreeItemPos != -1)
                        retainExpressiveButtonStates();
                    showActionBarTitle(true);
                //when mFlgKeyboardOpened is set to 0, it means user intent to use custom
                //keyboard input text so below steps will follow:
                } else {
                    // a) keyboard button to pressed state
                    // c) show custom keyboard input text and speak button view
                    // b) set back button unpressed state
                    // d) hide category icons
                    // e) disable expressive buttons
                    mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                    mEtTTs.setVisibility(View.VISIBLE);
                    // Focus to the field.
                    mEtTTs.setKeyListener(originalKeyListener);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    // when user intend to use custom keyboard input text system keyboard should
                    // only appear when user taps on custom keyboard input view. Setting
                    // InputMethodManager to InputMethodManager.HIDE_NOT_ALWAYS does this task.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    mFlgKeyboard = 1;
                    showActionBarTitle(false);
                    getSupportActionBar().setTitle(strKeyboard);
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "like" button.
     * Expressive like button is works in four ways:
     *  a) press expressive like button once
     *  b) press expressive like button twice
     *  c) press category icon first then press expressive like button once
     *  d) press category icon first then press expressive like button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except like button
     *  b) set pressed icon to like button
     *  c) produce speech using output for like
     *  d) set border to category icon of color associated with like button</p>
     * */
    private void initLikeBtnListener() {
        mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the like button all expressive button speech flag (except like)
                // are set to reset.
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 0, indicates like expressive button is pressed
                mFlgImage = 0;
                // Set like button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLike is 1, then should speak "really like".
                    if (mFlgLike == 1) {
                        speakSpeech(mExprBtnTxt[1]);
                        mFlgLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                    // if value of mFlgLike is 0, then should speak "like".
                    } else {
                        speakSpeech(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated like
                // expression verbiage for selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);

                    // if value of mFlgLike is 1 then speak associated really like expression
                    // verbiage for selected category icon.
                    if (mFlgLike == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(1));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(1));

                        //reset mFlgLike to speak "like" expression
                        mFlgLike = 0;
                    // if value of mFlgLike is 0 then Speak associated like expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(0));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(0));
                        //reset mFlgLike to speak "really like" expression
                        mFlgLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "don't like" button.
     * Expressive don't like button is works in four ways:
     *  a) press expressive don't like button once
     *  b) press expressive don't like button twice
     *  c) press category icon first then press expressive don't like button once
     *  d) press category icon first then press expressive don't like button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except don't like button
     *  b) set pressed icon to don't like button
     *  c) produce speech using output for don't like
     *  d) set border to category icon of color associated with don't like button</p>
     * */
    private void initDontLikeBtnListener() {
        mIvDontLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the don't like button all expressive button speech flag (except don't like)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 1, indicates dont like expressive button is pressed
                mFlgImage = 1;
                // Set don't like button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgDntLike is 1, then should speak "really dont like".
                    if (mFlgDntLike == 1) {
                        speakSpeech(mExprBtnTxt[7]);
                        mFlgDntLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                    // if value of mFlgDntLike is 0, then should speak " dont like".
                    } else {
                        speakSpeech(mExprBtnTxt[6]);
                        mFlgDntLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                    // if value of mShouldReadFullSpeech is true, then speak associated don't like
                    // expression verbiage for selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set don't like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);

                    // if value of mFlgDntLike is 1 then speak associated really don't like expression
                    // verbiage for selected category icon.
                    if (mFlgDntLike == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(7));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(7));
                        //reset mFlgDntLike to speak "dont like" expression
                        mFlgDntLike = 0;
                    // if value of mFlgDntLike is 0 then Speak associated don't like expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(6));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(6));
                        //reset mFlgDntLike to speak "really don't like" expression
                        mFlgDntLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "yes" button.
     * Expressive yes button is works in four ways:
     *  a) press expressive yes button once
     *  b) press expressive yes button twice
     *  c) press category icon first then press expressive yes button once
     *  d) press category icon first then press expressive yes button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except yes button
     *  b) set pressed icon to yes button
     *  c) produce speech using output for yes
     *  d) set border to category icon of color associated with yes button</p>
     * */
    private void initYesBtnListener() {
        mIvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the yes button all expressive button speech flag (except yes)
                // are set to reset.
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 2, indicates yes expressive button is pressed
                mFlgImage = 2;
                // Set like button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgYes is 1, then should speak "really yes".
                    if (mFlgYes == 1) {
                        speakSpeech(mExprBtnTxt[3]);
                        mFlgYes = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                    // if value of mFlgYes is 0, then should speak "yes".
                    } else {
                        speakSpeech(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated yes
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set yes button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgYes is 1 then speak associated really yes expression
                    // verbiage for selected category icon.
                    if (mFlgYes == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(3));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(3));
                        //reset mFlgYes to speak "yes" expression
                        mFlgYes = 0;
                    // if value of mFlgYes is 0 then speak associated really yes expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(2));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(2));
                        //reset mFlgYes to speak "really yes" expression
                        mFlgYes = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "no" button.
     * Expressive no button is works in four ways:
     *  a) press expressive no button once
     *  b) press expressive no button twice
     *  c) press category icon first then press expressive no button once
     *  d) press category icon first then press expressive no button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except no button
     *  b) set pressed icon to no button
     *  c) produce speech using output for no
     *  d) set border to category icon of color associated with no button</p>
     * */
    private void initNoBtnListener() {
        mIvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the no button all expressive button speech flag (except no)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgLess = 0;
                //Value of mFlgImage = 3, indicates no expressive button is pressed
                mFlgImage = 3;
                // Sets no button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgNo is 1, then should speak "really no".
                    if (mFlgNo == 1) {
                        speakSpeech(mExprBtnTxt[9]);
                        mFlgNo = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                    // if value of mFlgNo is 0, then should speak "no".
                    } else {
                        speakSpeech(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated no
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set no button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgNo is 1, then should speak "really no" expression
                    // verbiage associated for selected category icon.
                    if (mFlgNo == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(9));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(9));
                        //reset mFlgNo to speak "no" expression
                        mFlgNo = 0;
                    // if value of mFlgNo is 0 then Speak associated no expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(8));
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(8));
                        //reset mFlgLike to speak "really no" expression
                        mFlgNo = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to expressive more button.
     * When expressive more button is pressed:
     *  a. Single time, speech output is 'more'
     *  b. twice, speech output is 'some more'
     * When expressive more button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'more' expression.
     *  b. twice, speech output is full sentence with 'some more' expression.
     *  Also, it set image flag to 4. This flag is used when border is applied to
     *  a category icon.</p>
     * */
    private void initMoreBtnListener() {
        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the more button all expressive button speech flag (except more)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 4, indicates more expressive button is pressed
                mFlgImage = 4;
                // Sets more button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgMore is 1, then should speak "really more".
                    if (mFlgMore == 1) {
                        speakSpeech(mExprBtnTxt[5]);
                        mFlgMore = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                    // if value of mFlgMore is 0, then should speak "more".
                    } else {
                        speakSpeech(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated more
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgMore is 1, then should speak "really more" expression
                    // verbiage associated to selected category icon.
                    if (mFlgMore == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(5));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(5));
                        //reset mFlgMore to speak "more" expression
                        mFlgMore = 0;
                    // if value of mFlgMore is 0, then should speak "more" expression
                    // verbiage associated to selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(4));
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(4));
                        //reset mFlgMore to speak "really more" expression
                        mFlgMore = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "less" button.
     * Expressive less button is works in four ways:
     *  a) press expressive less button once
     *  b) press expressive less button twice
     *  c) press category icon first then press expressive less button once
     *  d) press category icon first then press expressive less button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except less button
     *  b) reset all expressive buttons
     *  c) produce speech using output for less
     *  d) set border to category icon of color associated with less button</p>
     * */
    private void initLessBtnListener() {
        mIvLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the less button all expressive button speech flag (except less)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = 0;
                //Value of mFlgImage = 5, indicates less expressive button is pressed
                mFlgImage = 5;
                // Sets less button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLess is 1, then should speak "really less".
                    if (mFlgLess == 1) {
                        speakSpeech(mExprBtnTxt[11]);
                        mFlgLess = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                    // if value of mFlgLess is 0, then should speak "less".
                    } else {
                        speakSpeech(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated less
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set less button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgLess is 1 then speak associated really less expression
                    // verbiage for selected category icon.
                    if (mFlgLess == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(11));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(11));
                        //reset mFlgLess to speak "less" expression
                        mFlgLess = 0;
                    // if value of mFlgLess is 0 then Speak associated less expression
                    // verbiage to selected category icon.
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                        singleEvent("ExpressiveGridIcon",
                                mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(10));

                        speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(10));
                        //reset mFlgLess to speak "really less" expression
                        mFlgLess = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view, is synthesized by the service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mEtTTs.getText().toString());
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", mEtTTs.getText().toString());
                bundleEvent("Keyboard", bundle);
                //singleEvent("Keyboard", mEtTTs.getText().toString());
                //if expressive buttons always disabled during custom text speech output
                mIvLike.setEnabled(false);
                mIvDontLike.setEnabled(false);
                mIvMore.setEnabled(false);
                mIvLess.setEnabled(false);
                mIvYes.setEnabled(false);
                mIvNo.setEnabled(false);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to EditText which is used by user to
     * input custom strings.</p>
     * */
    private void initTTsEditTxtListener() {
        mEtTTs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If custom keyboard input text loses focus...
                if (!hasFocus) {
                    // Hide system keyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtTTs.getWindowToken(), 0);
                    // Make it non-editable again.
                    mEtTTs.setKeyListener(null);
                }
            }
        });
    }

    /**
     * <p>This function is called when user taps a category icon. It will change the state of
     * category icon pressed. Also, it set the flag for app speak full verbiage sentence.
     * @param view is parent view in selected RecyclerView.
     * @param v is parent relative layout of category icon tapped.
     * @param position is position of a tapped category icon in the RecyclerView.
     * This function
     *             a) Clear every expressive button flags
     *             b) Reset expressive button icons
     *             c) Reset category icon views
     *             d) Set the border to selected category icon
     *             e) Increment preference mArrIconTapCount of tapped category icon.</p>
     * */
    public void tappedCategoryItemEvent(View view, View v, int position) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
        // Clear the last expressive button selection. Set each expressive button to unpressed.
        resetExpressiveButtons(-1);
        // reset every populated category icon before setting the border to selected icon.
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        // set border to selected category icon
        setMenuImageBorder(v, true);
        // set true to speak verbiage associated with category icon
        mShouldReadFullSpeech = true;
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        mLevelThreeItemPos = mRecyclerView.getChildLayoutPosition(view);
        // Below categories which does not have preferences enabled, their speech text directly
        // retained from speech array otherwise speech text is retained using preference sort array
        // and then text is sent to synthesis.
        // Categories not using preference sort in below if are stated respectively:
        // Fun -> (Tv and Music)
        // Time ans weather -> (Time, Day, Month, Weather, Season)
        // Learning -> (Money) .
        if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                        mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9))
            speakSpeech(mSpeechTxt[mLevelThreeItemPos]);
        else
            speakSpeech(mSpeechTxt[mArrSort[mLevelThreeItemPos]]);

        // increment category item touch count
        incrementTouchCountOfItem(mLevelThreeItemPos);
        // retain state of expressive button when particular type category icon pressed
        retainExpressiveButtonStates();
        Bundle bundle = new Bundle();
        bundle.putString("Icon", mSpeechTxt[position]);
        bundleEvent("Grid",bundle);

        mIvBack.setImageResource(R.drawable.back);
    }

    /**
     * <p>This function will display/ hide action bar title.
     * If {@param showTitle} is set then title is displayed otherwise not.</p>
     * */
    private void showActionBarTitle(boolean showTitle){
        if (showTitle)
            getSupportActionBar().setTitle(actionBarTitleTxt);
        else{
            actionBarTitleTxt = getSupportActionBar().getTitle().toString();
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>This function will send speech output request to
     * {@link com.dsource.idc.jellowintl.utility.JellowTTSService} Text-to-speech Engine.
     * The string in {@param speechText} is speech output request string.</p>
     * */
    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText.toLowerCase());
        sendBroadcast(intent);
    }

    /**
     * <p>This function will:
     *     a) Read speech text from arrays for expressive buttons.
     *     b) Read speech text from arrays for navigation buttons.
     *     c) Read verbiage lines into {@link LevelThreeVerbiageModel} model.</p>
     * */
    private void loadArraysFromResources() {
        mExprBtnTxt = getResources().getStringArray(R.array.arrActionSpeech);
        mNavigationBtnTxt = getResources().getStringArray(R.array.arrNavigationSpeech);
        String verbString = getString(R.string.levelThreeVerbiage1) +
                getString(R.string.levelThreeVerbiage2) +
                getString(R.string.levelThreeVerbiage3)+
                getString(R.string.levelThreeVerbiage4) +
                getString(R.string.levelThreeVerbiage5) +
                getString(R.string.levelThreeVerbiage6) +
                getString(R.string.levelThreeVerbiage7) +
                getString(R.string.levelThreeVerbiage8) +
                getString(R.string.levelThreeVerbiage9) +
                getString(R.string.levelThreeVerbiage10) +
                getString(R.string.levelThreeVerbiage11) +
                getString(R.string.levelThreeVerbiage12) +
                getString(R.string.levelThreeVerbiage13) +
                getString(R.string.levelThreeVerbiage14) +
                getString(R.string.levelThreeVerbiage15) +
                getString(R.string.levelThreeVerbiage16) +
                getString(R.string.levelThreeVerbiage17) +
                getString(R.string.levelThreeVerbiage18) +
                getString(R.string.levelThreeVerbiage19) +
                getString(R.string.levelThreeVerbiage20) +
                getString(R.string.levelThreeVerbiage21) +
                getString(R.string.levelThreeVerbiage22) +
                getString(R.string.levelThreeVerbiage23) +
                getString(R.string.levelThreeVerbiage24) +
                getString(R.string.levelThreeVerbiage25);
        LevelThreeVerbiageModel mLevelThreeVerbiageModel = new Gson().
                fromJson(verbString, LevelThreeVerbiageModel.class);
        mNewVerbTxt = mLevelThreeVerbiageModel.getVerbiageModel()
                                         .get(mLevelOneItemPos).get(mLevelTwoItemPos);
    }

    /**
     * <p>In level three, several category icons have associated multiple expressive buttons are
     * disabled/enabled.
     * This function will provide these enabled/disabled states for expressive buttons for a
     * selected category icon in level three.</p>
     * */
    private void retainExpressiveButtonStates() {
        //if category Greet and feel -> feelings is selected
        if(mLevelOneItemPos == 0 && mLevelTwoItemPos == 1) {
            int tmp = mArrSort[mLevelThreeItemPos];
            // if in feeling category icon having index (default position in recycler view)
            // "tmp" = 0 (category icon is Happy) then disable no expressive button and keep all
            // other expressive button enabled.
            if (tmp == 0) {
                mIvNo.setAlpha(0.5f);
                mIvNo.setEnabled(false);
                mIvYes.setAlpha(1f);
                mIvYes.setEnabled(true);
                // if in feeling category icon having index (default position in recycler view)
                // "tmp" = 1 (Sad), 2 (Angry), 3 (Afraid), 5 (Irritated)
                //         6 (Confused), 7 (Ashamed), 8 (Disappointed),
                //         9 (Bored), 10 (Worried), 11 (Stressed),
                //         12 (Tired), 15 (Sick) & 16 (Hurt)
                // then disable yes expressive button and keep all other expressive button enabled.
            } else if (tmp == 1 || tmp == 2 || tmp == 3 || tmp == 5 || tmp == 6 || tmp == 7 ||
                    tmp == 8 || tmp == 9 || tmp == 10 || tmp == 11 || tmp == 12 || tmp == 15 ||
                    tmp == 16) {
                mIvYes.setAlpha(0.5f);
                mIvYes.setEnabled(false);
                mIvNo.setAlpha(1f);
                mIvNo.setEnabled(true);
                // if in feeling category icon having index (default position in recycler view)
                // other than "tmp" enable all expressive buttons
            } else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);

        //If in Places -> Hospital -> I am hurt
        //      Places -> Hospital -> I feel sick
        // all expressive buttons are disabled as they are simple expressions
        // otherwise all expressive buttons are enabled.
        }else if(mLevelOneItemPos == 6 && (mLevelTwoItemPos == 11)){
            if (mArrSort[mLevelThreeItemPos] == 0 || mArrSort[mLevelThreeItemPos] == 1)
                changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        //If in feeling -> greetings,
        //      feeling -> requests
        //      feeling -> questions
        //      Daily Activities -> Habits
        // all expressive buttons are disabled as they are simple expressions
        }else if((mLevelOneItemPos == 0 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 2 ||
                mLevelTwoItemPos == 3)) ||
                (mLevelOneItemPos == 1 && mLevelTwoItemPos == 9))
            changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
        // Daily activities -> clothes and more
        else if(mLevelOneItemPos == 1 && mLevelTwoItemPos == 3){
            int tmp = mArrSort[mLevelThreeItemPos];
            String lang = new SessionManager(this).getLanguage();
            // if any one of category icon from "My clothes are tight", "My clothes are loose",
            // "Help me remove clothes" and "Help put on clothes" is selected then all expressive
            // buttons are disabled.
            // Number of icons in category "daily activities" -> "clothes and  more" for app
            // language Hindi, English (India) is different than app language English (US, UK).
            // So category icons "My clothes are tight", "My clothes are loose", "Help me remove clothes"
            // and "Help put on clothes" have index (default position in recycler view)
            // 34, 35, 36, 37 in Hindi, English (India) and
            // have index (default position in recycler view) 39, 40, 41, 42 in English (US, UK).
            // So to disable expressive buttons for above category icon it required to check the
            // user language and index (default position in recycler view) of category icon.
            if ((!lang.equals("en-rUS") && !lang.equals("en-rGB") &&
                    (tmp == 34 || tmp == 35 || tmp == 36 || tmp == 37))
                    ||
                    ((lang.equals("en-rUS") || lang.equals("en-rGB")) &&
                            (tmp == 39 || tmp == 40 || tmp == 41 || tmp == 42) ))
                changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        //if in time and weather -> time
        //  time and weather -> day
        //  time and weather -> month,
        //  time and weather -> weather
        // in each above category first category icon will have expressive buttons disabled.
        } else if (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) {
            if (mLevelThreeItemPos == 0)
                changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        }else
            changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
    }

    /**
     * <p>This function enable or disable the expressive buttons using {@param setDisable}.
     * {@param setDisable}, if setDisable = true, buttons are disabled otherwise
     * enabled.</p>
     * */
    private void changeTheExpressiveButtons(boolean setDisable) {
        if(setDisable) {
            mIvLike.setAlpha(0.5f);
            mIvDontLike.setAlpha(0.5f);
            mIvYes.setAlpha(0.5f);
            mIvNo.setAlpha(0.5f);
            mIvMore.setAlpha(0.5f);
            mIvLess.setAlpha(0.5f);
            mIvLike.setEnabled(false);
            mIvDontLike.setEnabled(false);
            mIvYes.setEnabled(false);
            mIvNo.setEnabled(false);
            mIvMore.setEnabled(false);
            mIvLess.setEnabled(false);
        }else{
            mIvLike.setAlpha(1f);
            mIvDontLike.setAlpha(1f);
            mIvYes.setAlpha(1f);
            mIvNo.setAlpha(1f);
            mIvMore.setAlpha(1f);
            mIvLess.setAlpha(1f);
            mIvLike.setEnabled(true);
            mIvDontLike.setEnabled(true);
            mIvYes.setEnabled(true);
            mIvNo.setEnabled(true);
            mIvMore.setEnabled(true);
            mIvLess.setEnabled(true);
        }
    }

    /**
     * <p>This function set the border to category icons. This function first extracts the view to
     * which border should applied then apply the border.
     * {@param recyclerChildView} is a parent view extracted from recycler view when category icon is tapped.
     * {@param setBorder} is set if category icon tapped and a color border should appear on the view other wise
     *  transparent color is set to border.
     * </p>
     * */
    private void setMenuImageBorder(View recyclerChildView, boolean setBorder) {
        GradientDrawable gd = (GradientDrawable) recyclerChildView.findViewById(R.id.borderView).getBackground();
        if(setBorder){
            // mActionBtnClickCount = 0, brown color border is set.
            if (mActionBtnClickCount > 0) {
                // mFlgImage define color of border.
                switch (mFlgImage) {
                    case 0: gd.setColor(ContextCompat.getColor(this,R.color.colorLike)); break;
                    case 1: gd.setColor(ContextCompat.getColor(this,R.color.colorDontLike)); break;
                    case 2: gd.setColor(ContextCompat.getColor(this,R.color.colorYes)); break;
                    case 3: gd.setColor(ContextCompat.getColor(this,R.color.colorNo)); break;
                    case 4: gd.setColor(ContextCompat.getColor(this,R.color.colorMore)); break;
                    case 5: gd.setColor(ContextCompat.getColor(this,R.color.colorLess)); break;
                }
            } else
                gd.setColor(ContextCompat.getColor(this,R.color.colorSelect));
        } else
            gd.setColor(ContextCompat.getColor(this,android.R.color.transparent));
    }

    /**
     * <p>This function first reset if any expressive button is pressed. Then set an
     * expressive button to pressed. {@param image_flag} identifies which expressive button
     * is pressed.
     * If user had previously pressed any expressive button (i.e. state of any expressive
     * button is pressed) and then user presses some other expressive button, it is needed to
     * clear previously pressed expressive button state and home button state (if pressed).
     * {@param image_flag} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.
     *  To set home button to pressed state image_flag value must be 6</p>
     * */
    private void resetExpressiveButtons(int image_flag) {
        // clear previously selected any expressive button or home button
        mIvLike.setImageResource(R.drawable.like);
        mIvDontLike.setImageResource(R.drawable.dontlike);
        mIvYes.setImageResource(R.drawable.yes);
        mIvNo.setImageResource(R.drawable.no);
        mIvMore.setImageResource(R.drawable.more);
        mIvLess.setImageResource(R.drawable.less);
        mIvHome.setImageResource(R.drawable.home);
        // set expressive button or home button to pressed state
        switch (image_flag){
            case 0: mIvLike.setImageResource(R.drawable.like_pressed); break;
            case 1: mIvDontLike.setImageResource(R.drawable.dontlike_pressed); break;
            case 2: mIvYes.setImageResource(R.drawable.yes_pressed); break;
            case 3: mIvNo.setImageResource(R.drawable.no_pressed); break;
            case 4: mIvMore.setImageResource(R.drawable.more_pressed); break;
            case 5: mIvLess.setImageResource(R.drawable.less_pressed); break;
            case 6: mIvHome.setImageResource(R.drawable.home_pressed); break;
            default: break;
        }
    }

    /**
     * <p>This function reset the border for all category icons that are populated
     * in recycler view.</p>
     * */
    private void resetRecyclerAllItems() {
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
        }
    }

    /**
     * <p>This function increment the touch mArrIconTapCount for the selected category icon.
     * In {@link LevelThreeActivity}, several categories has enabled preferences.
     * This function will store these preferences into local SQLite database.</p>
     * */
    private void incrementTouchCountOfItem(int levelThreeItemPos) {
        if (count_flag == 1) {
            // increment tap count of selected category icon
            mArrIconTapCount[mArrSort[levelThreeItemPos]] =
                    mArrIconTapCount[mArrSort[levelThreeItemPos]] + 1;
            // build preference string for current category
            StringBuilder str = new StringBuilder();
            for(int i = 0; i< mArrIconTapCount.length; ++i)
                str.append(mArrIconTapCount[i]).append(",");

            // To make preference string uniform length, "0" is appended to the end of string
            // till total number of tokens in string reaches to 100.
            for(int i = mArrIconTapCount.length; i < 100; ++i)
                str.append("0,");

            // store preference string of category into database.
            myDbHelper.setLevel(mLevelOneItemPos, mLevelTwoItemPos, str.toString());
        }
    }

    /**
     * <p>This function retrieve speech text array from arrays
     * resource using category icon index selected in {@link MainActivity} and
     * {@link LevelTwoActivity}.
     * @param levelOneItemPos is a index (position of category icon in recycler view)
     * of category icon selected in {@link MainActivity}.
     * @param levelTwoItemPos is a index (position of category icon in recycler view)
     * of category icon selected in {@link LevelTwoActivity}.</p>
     * */
    private void retrieveSpeechArrays(int levelOneItemPos, int levelTwoItemPos) {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadySpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActSleepSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActTherapySpeechText);
                    break;
                case 9:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActHabitsSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutlerySpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 3){
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunInDGamesSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunSportsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunTvSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunMusicSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunActivitiesSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningBooksSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningColorsSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningShapesSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningStationarySpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjSpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjSpeechText);
                    break;
                case 8:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningTransportSpeechText);
                    break;
                case 9:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningMoneySpeechText);
                    break;
            }
        } else if (levelOneItemPos == 6) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesMyHouseSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesSchoolSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesMallSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesMuseumSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesRestaurantSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesTheatreSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesPlaygroundSpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesParkSpeechText);
                    break;
                case 8:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesStoreSpeechText);
                    break;
                case 9:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesFriendHouseSpeechText);
                    break;
                case 10:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesRelativeHouseSpeechText);
                    break;
                case 11:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesHospitalSpeechText);
                    break;
                case 12:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesClinicSpeechText);
                    break;
                case 13:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesLibrarySpeechText);
                    break;
                case 14:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesZooSpeechText);
                    break;
                case 15:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreePlacesWorshipSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaDaySpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysSpeechText);
                    break;
            }
        }
    }


    /**<p> This function will returns the index of the item searched item in the sorted list
     * it takes default index of the searched item and returns the actual sorted index of the element.
     * @Author AyazAlam</p>
     *
     **/

    private int getSortedIndex(int index)
    {
        for(int i=0;i<mArrSort.length;i++)
        {
            if(index==mArrSort[i])
                return i;
        }
        return -1;

    }
}
