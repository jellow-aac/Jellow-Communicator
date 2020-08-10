package com.dsource.idc.jellowintl.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.Presentor.PreferencesHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.activities.adapters.LevelThreeAdapter;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;
import com.dsource.idc.jellowintl.utility.IndexSorter;
import com.dsource.idc.jellowintl.utility.LevelUiUtils;
import com.dsource.idc.jellowintl.utility.UserEventCollector;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.factories.IconFactory.getIconCode;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class LevelThreeActivity extends LevelBaseActivity{

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = GlobalConstants.SHORT_SPEECH, mFlgYes = GlobalConstants.SHORT_SPEECH,
            mFlgMore = GlobalConstants.SHORT_SPEECH, mFlgDntLike = GlobalConstants.SHORT_SPEECH,
            mFlgNo = GlobalConstants.SHORT_SPEECH, mFlgLess = GlobalConstants.SHORT_SPEECH;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = GlobalConstants.NO_EXPR;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount;
    /*Image views which are visible on the layout such as six expressive buttons, mNavigationBtnTxt navigation
      buttons and speak button when custom keyboard input layout is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private RecyclerView mRecyclerView;
    /*This variable indicates index of category icon selected in level one, two and three respectively*/
    private int mLevelOneItemPos, mLevelTwoItemPos, mLevelThreeItemPos = GlobalConstants.NOT_SELECTED;
    /*This variable indicates index of category icon in adapter in level 1. This variable is
     different than mLevelOneItemPos. */
    private int mSelectedItemAdapterPos = GlobalConstants.NOT_SELECTED;
    /* This flag is set to true, when user press the category icon and reset when user press the home
     button. When user presses expressive button and mShouldReadFullSpeech = true, it means that user
     is already selected a category icon and user intend to speak full sentence verbiage for a
     selected icon.*/
    private boolean mShouldReadFullSpeech = false, mSearched = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*This variable stores current tap mArrIconTapCount to every category icon populated in recycler view.*/
    /*This variable stores array of index of category icons sorted by tap count*/
    private Integer[] mArrIconTapCount, mArrSort;
    private int count_flag = 0;
    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mSpeechText,mDisplayText, mExprBtnTxt, mNavigationBtnTxt, mIconCode;
    private String txtActionBarTitle, txtKeyboard, mSpeak;

    /*Firebase event Collector class instance.*/
    private UserEventCollector mUec;

    Icon[] level3IconObjects;

    private ImageView[] expressiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        txtActionBarTitle = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag));
        txtKeyboard = getString(R.string.keyboard);
        setupActionBarTitle(View.GONE, txtActionBarTitle);
        setNavigationUiConditionally();
        adjustTopMarginForNavigationUi();

        // when layout is loaded on activity, using the tag attribute of a parent view in layout
        // the device size is identified. If device size is large (10' tablets) enable the
        // hardware acceleration. As seen in testing device, scrolling recycler items on 10' tab
        // have jagged views (lags in scrolling) hence hardware acceleration is enabled.
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // set app locale which is set in settings by user.
        mUec = new UserEventCollector();
        mLevelOneItemPos = getIntent().getExtras().getInt(getString(R.string.level_one_intent_pos_tag));
        mLevelTwoItemPos = getIntent().getExtras().getInt(getString(R.string.level_2_item_pos_tag));
        loadArraysFromResources();

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
                 mSearched = true;
                highlightSearchedItem();
            }
        }
        mSpeak = getString(R.string.speak);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LevelThreeActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer .
        startMeasuring();
        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(this, getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
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
                if(getSession().getGridSize()== GlobalConstants.NINE_ICONS_PER_SCREEN)
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
                //When view is found remove the scrollListener and manually tap the icon.
                tappedCategoryItemEvent(searchedView);
                mRecyclerView.removeOnScrollListener(scrollListener);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    searchedView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
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
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);
        // Stop measuring user app screen timer .
        stopMeasuring("LevelThreeActivity");
    }

    @Override
    public void onDestroy() {
        mRecyclerView = null;
        mRecyclerItemsViewList = null;
        mSpeechText = mDisplayText = mExprBtnTxt = mNavigationBtnTxt = null;
        mArrSort = null; mArrIconTapCount = null;
        super.onDestroy();
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

        ViewCompat.setAccessibilityDelegate(mIvLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvMore, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvDontLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvBack, new TalkbackHints_SingleClick());

        mRecyclerView = findViewById(R.id.recycler_view);
        // Initiate 3 columns in Recycler View.
        //This code is to decide the speed of the Scrolling
        // which grid size is 3 then scrolling is fast as compared to the 9.
        switch (getSession().getGridSize()){
            case GlobalConstants.ONE_ICON_PER_SCREEN:
                mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 1,3));
                break;
            case GlobalConstants.TWO_ICONS_PER_SCREEN:
            case GlobalConstants.FOUR_ICONS_PER_SCREEN:
                mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 2,3));
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case GlobalConstants.THREE_ICONS_PER_SCREEN:
                mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 3,3));
                break;
            case GlobalConstants.NINE_ICONS_PER_SCREEN:
                mRecyclerView.setLayoutManager(new CustomGridLayoutManager
                        (this, 3, getSession().getGridSize()));
                break;
        }
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        expressiveBtn = new ImageView[]{ mIvLike, mIvYes, mIvMore, mIvDontLike, mIvNo, mIvLess };
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

        // load speech array directly.
        retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);


        // Set the capacity of mRecyclerItemsViewList list to total number of category icons to be
        // populated on the screen.
        mRecyclerItemsViewList = new ArrayList<>(level3IconObjects.length);
        while(mRecyclerItemsViewList.size() <= level3IconObjects.length) mRecyclerItemsViewList.add(null);


        String savedString = PreferencesHelper.getPrefString(getAppDatabase(),
                getIconCode(LanguageFactory.getCurrentLanguageCode(this ),
                mLevelOneItemPos, mLevelTwoItemPos));

        // Extra 0's are concat ed at the end of "savedString" variable. This
        // will make length of array and length of savedString to equal to load category.
        if(savedString.isEmpty() && savedString.split(",").length != level3IconObjects.length){
            while (savedString.split(",").length != level3IconObjects.length)
                savedString = savedString.concat("0,");
        }
        mArrSort = new Integer[level3IconObjects.length];
        // savedString is equal to "false" then load level three category icons without any sort/preferences
        if (isCategoryWithPreference()) {
            count_flag = 1;
            mArrIconTapCount = new Integer[level3IconObjects.length];
            // convert savedString into individual tokens.
            StringTokenizer st = new StringTokenizer(savedString, ",");

            // create tap count array (mArrIconTapCount) of user taps using individual tokens
            mArrIconTapCount = new Integer[level3IconObjects.length];
            for (int j = 0; j < level3IconObjects.length; j++) {
                mArrIconTapCount[j] = Integer.parseInt(st.nextToken());
            }

            //create sort array using tap count array (mArrIconTapCount)
            IndexSorter<Integer> is = new IndexSorter<Integer>(mArrIconTapCount);
            is.sort();
            Integer[] indexes = new Integer[level3IconObjects.length];

            //get index array for given tap count array(mArrIconTapCount)
            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                g++;
            }
            for (int j = 0; j < mArrIconTapCount.length; j++)
                mArrSort[j] = indexes[j];

            //setup adapter.
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mIconCode,
                    level3IconObjects, mArrSort));

        // saved string is == "false" then load level three category icons without sort/preferences.
        // Categories given below are in if condition respectively:
        // Fun -> TV, fun -> Music,
        // Time & weather -> (Time, Day, month, Weather, Season) and
        // Learning -> Money, should load always without sorting
        } else if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                        mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4))
                || (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9)) {

            // create sort array to sequentially arrange icons without any preferences
            for (int i=0; i< level3IconObjects.length;i++)
                mArrSort[i] = i;
            //setup adapter
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mIconCode,
                    level3IconObjects, mArrSort));
        } else {
            count_flag = 0;
        }
    }

    public boolean isCategoryWithPreference() {
        String iconCode = IconFactory.getIconCode(
                LanguageFactory.getCurrentLanguageCode(this), mLevelOneItemPos, mLevelTwoItemPos);
        
        return ("01010000GG,01020000GG,01030000GG,01040000GG,"+
                "02040000GG,02050000GG,02060000GG,02070000GG,02100000GG"+
                "03010000GG,03020000GG,03030000GG,03040000GG,"+
                "03050000GG,03060000GG,03070000GG,03080000GG,"+
                "04010000GG,04020000GG,04030000GG,04060000GG,"+
                "05010000GG,05020000GG,05030000GG,05040000GG,"+
                "05050000GG,05060000GG,05070000GG,05080000GG,"+
                "05090000GG,05100000GG,07010000GG,07020000GG,"+
                "07030000GG,07040000GG,07050000GG,07060000GG,"+
                "07070000GG,07080000GG,07090000GG,07100000GG,"+
                "07110000GG,07120000GG,07130000GG,07140000GG,"+
                "07150000GG,07160000GG,08060000GG,08070000GG,")
                .contains(iconCode.substring(2));
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
    }

    /**
     * <p>This function initializes
     * {@link RecyclerView.OnChildAttachStateChangeListener} for recycler view.
     * {@link RecyclerView.OnChildAttachStateChangeListener} is defined to manage view state of
     * recycler child view. It is useful to retain current state of child, when recycler view is scrolled
     * and recycler child views are recycled for memory usage optimization.</p>
     * */
    private void initRecyclerViewListeners() {
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
                if(mRecyclerItemsViewList.contains(view) && mSelectedItemAdapterPos > GlobalConstants.NOT_SELECTED &&
                        mRecyclerView.getChildLayoutPosition(view) == mSelectedItemAdapterPos)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this, view,
                                true, mActionBtnClickCount, mFlgImage);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this, view,
                        false, mActionBtnClickCount, mFlgImage);
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
                speak(mNavigationBtnTxt[1]);
                mIvBack.setImageResource(R.drawable.back_pressed);
                mUec.createSendFbEventFromTappedView(27, "", "");
                mIvBack.setImageResource(R.drawable.back_pressed);
                String str = getIntent().getExtras().getString(getString(R.string.from_search));
                if(str != null && !str.isEmpty() && str.equals(getString(R.string.search_tag))){
                    Intent intent = new Intent(LevelThreeActivity.this, LevelTwoActivity.class);
                    intent.putExtra(getString(R.string.level_one_intent_pos_tag), mLevelOneItemPos);
                    intent.putExtra("search_and_back", true);
                    {
                        String path = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag));
                        path = path.split("/")[0]+ "/"+path.split("/")[1]+"/";
                        intent.putExtra(getString(R.string.intent_menu_path_tag), path);
                    }
                    startActivity(intent);
                }else {
                    setResult(RESULT_OK);
                }
                finish();
                //Firebase event
                singleEvent("Navigation","Back");
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
                mUec.createSendFbEventFromTappedView(26, "", "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speak(mNavigationBtnTxt[0]);
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
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase event
                singleEvent("Navigation", "Keyboard");
                new DialogKeyboardUtterance().show(LevelThreeActivity.this);
                speak(mNavigationBtnTxt[2]);
                mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                mIvBack.setImageResource(R.drawable.back);
                mIvHome.setImageResource(R.drawable.home);
            }
        });
    }

    private int getTagPos(){
        String noPrefLevelPos = "3-3,3-4,4-9,7-0,7-1,7-2,7-3,7-4";
        return noPrefLevelPos.contains(mLevelOneItemPos+"-"+mLevelTwoItemPos) ?
                mLevelThreeItemPos : mArrSort[mLevelThreeItemPos];
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
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.LIKE;
                // Set like button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLike is 1, then should speak "really like".
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[1]);
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(1, "", "");
                    // if value of mFlgLike is 0, then should speak "like".
                    } else {
                        speak(mExprBtnTxt[0]);
                        mFlgLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(0, "", "");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated like
                // expression verbiage for selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);

                    // if value of mFlgLike is 1 then speak associated really like expression
                    // verbiage for selected category icon.
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(14,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"LL","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getLL());

                        //reset mFlgLike to speak "like" expression
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgLike is 0 then Speak associated like expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(13,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"L0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getL());
                        //reset mFlgLike to speak "really like" expression
                        mFlgLike = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.DONT_LIKE;
                // Set don't like button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgDntLike is 1, then should speak "really dont like".
                    if (mFlgDntLike == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[7]);
                        mFlgDntLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(7, "", "");
                    // if value of mFlgDntLike is 0, then should speak " dont like".
                    } else {
                        speak(mExprBtnTxt[6]);
                        mFlgDntLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(6, "", "");
                    }
                    // if value of mShouldReadFullSpeech is true, then speak associated don't like
                    // expression verbiage for selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set don't like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);

                    // if value of mFlgDntLike is 1 then speak associated really don't like expression
                    // verbiage for selected category icon.
                    if (mFlgDntLike == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(20,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"DD","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getDD());
                        //reset mFlgDntLike to speak "dont like" expression
                        mFlgDntLike = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgDntLike is 0 then Speak associated don't like expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(19,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"D0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getD());
                        //reset mFlgDntLike to speak "really don't like" expression
                        mFlgDntLike = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.YES;
                // Set like button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgYes is 1, then should speak "really yes".
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[3]);
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(3, "", "");
                    // if value of mFlgYes is 0, then should speak "yes".
                    } else {
                        speak(mExprBtnTxt[2]);
                        mFlgYes = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(2, "", "");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated yes
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set yes button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);
                    // if value of mFlgYes is 1 then speak associated really yes expression
                    // verbiage for selected category icon.
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(16,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"YY","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getYY());
                        //reset mFlgYes to speak "yes" expression
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgYes is 0 then speak associated really yes expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(15,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"Y0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getY());
                        //reset mFlgYes to speak "really yes" expression
                        mFlgYes = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.NO;
                // Sets no button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgNo is 1, then should speak "really no".
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[9]);
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(9, "", "");
                    // if value of mFlgNo is 0, then should speak "no".
                    } else {
                        speak(mExprBtnTxt[8]);
                        mFlgNo = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(8, "", "");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated no
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set no button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);
                    // if value of mFlgNo is 1, then should speak "really no" expression
                    // verbiage associated for selected category icon.
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(22,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"NN","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getNN());
                        //reset mFlgNo to speak "no" expression
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgNo is 0 then Speak associated no expression
                    // verbiage for selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(21,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"N0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getN());
                        //reset mFlgLike to speak "really no" expression
                        mFlgNo = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.MORE;
                // Sets more button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgMore is 1, then should speak "really more".
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[5]);
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(5, "", "");
                    // if value of mFlgMore is 0, then should speak "more".
                    } else {
                        speak(mExprBtnTxt[4]);
                        mFlgMore = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(4, "", "");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated more
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);
                    // if value of mFlgMore is 1, then should speak "really more" expression
                    // verbiage associated to selected category icon.
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(18,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"MM","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getMM());
                        //reset mFlgMore to speak "more" expression
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgMore is 0, then should speak "more" expression
                    // verbiage associated to selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(17,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"M0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getM());
                        //reset mFlgMore to speak "really more" expression
                        mFlgMore = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.LESS;
                // Sets less button icon to pressed using mFlgImage.
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLess is 1, then should speak "really less".
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[11]);
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(11, "", "");
                    // if value of mFlgLess is 0, then should speak "less".
                    } else {
                        speak(mExprBtnTxt[10]);
                        mFlgLess = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(10, "", "");
                    }
                // if value of mShouldReadFullSpeech is true, then speak associated less
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set less button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos),
                                true, mActionBtnClickCount, mFlgImage);
                    // if value of mFlgLess is 1 then speak associated really less expression
                    // verbiage for selected category icon.
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(24,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"SS","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getSS());
                        //reset mFlgLess to speak "less" expression
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgLess is 0 then Speak associated less expression
                    // verbiage to selected category icon.
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(23,
                level3IconObjects[getTagPos()].getEvent_Tag()
                            +"_"+ mIconCode[getTagPos()]+"S0","");

                        speak(level3IconObjects[mArrSort[mLevelThreeItemPos]].getS());
                        //reset mFlgLess to speak "really less" expression
                        mFlgLess = GlobalConstants.LONG_SPEECH;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function is called when user taps a category icon. It will change the state of
     * category icon pressed. Also, it set the flag for app speak full verbiage sentence.
     * @param view is parent view in selected RecyclerView.
     * This function
     *             a) Clear every expressive button flags
     *             b) Reset expressive button icons
     *             c) Reset category icon views
     *             d) Set the border to selected category icon
     *             e) Increment preference mArrIconTapCount of tapped category icon.</p>
     * */
    public void tappedCategoryItemEvent(View view) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
        // Clear the last expressive button selection. Set each expressive button to unpressed.
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        // reset every populated category icon before setting the border to selected icon.
        LevelUiUtils.resetRecyclerAllItems(LevelThreeActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mActionBtnClickCount = 0;
        // set border to selected category icon
        LevelUiUtils.setBorderToCategoryIcon(LevelThreeActivity.this, view,true,
                mActionBtnClickCount, mFlgImage);
        // set true to speak verbiage associated with category icon
        mShouldReadFullSpeech = true;
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        mLevelThreeItemPos = mRecyclerView.getChildLayoutPosition(view);

        if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            mUec.accessibilityPopupOpenedEvent(mSpeechText[getTagPos()]);
            showAccessibleDialog(view);
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }else {
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
                    (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9)) {
                if (!mSearched)
                    speak(mSpeechText[mLevelThreeItemPos]);
                else
                    speakWithDelay(mSpeechText[mLevelThreeItemPos]);
            } else {
                if (!mSearched)
                    speak(mSpeechText[mArrSort[mLevelThreeItemPos]]);
                else
                    speakWithDelay(mSpeechText[mArrSort[mLevelThreeItemPos]]);
            }
            mSearched = false;
            mUec.createSendFbEventFromTappedView(12, mDisplayText[getTagPos()], "");
        }
        // increment category item touch count
        if (count_flag == 1)
            LevelUiUtils.incrementTouchCountOfItem(mArrIconTapCount, mArrSort,
                mLevelThreeItemPos, mLevelOneItemPos, mLevelTwoItemPos,
                    LanguageFactory.getCurrentLanguageCode(this), getAppDatabase());
        // retain state of expressive button when particular type category icon pressed
        LevelUiUtils.setExpressiveIconConditionally(expressiveBtn, level3IconObjects[mArrSort[mLevelThreeItemPos]]);
        mIvBack.setImageResource(R.drawable.back);
    }

    /**
     * <p>This function will:
     *     a) Read speech text from arrays for expressive buttons.
     *     b) Read speech text from arrays for navigation buttons.</p>
     * */
    private void loadArraysFromResources() {

        //Retrieve expressive icons text
        String[] expressiveIcons = IconFactory.getExpressiveIconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );
        //Extract expressive icon text from expressiveIconObjects
        ExpressiveIcon[] expressiveIconObjects = TextFactory.getExpressiveIconObjects(
                expressiveIcons);
        mExprBtnTxt = TextFactory.getExpressiveSpeechText(expressiveIconObjects);


        //Retrieve Navigation icon text
        String[] miscellaneousIcons = IconFactory.getMiscellaneousIconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );
        MiscellaneousIcon[] miscellaneousIconObjects = TextFactory.getMiscellaneousIconObjects(
                miscellaneousIcons
        );
        //Extract Navigation icon text from miscellaneousIconObjects
        mNavigationBtnTxt = TextFactory.getTitle(miscellaneousIconObjects);
    }

    private void showAccessibleDialog(final View disabledView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LevelThreeActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Button enterCategory = mView.findViewById(R.id.enterCategory);
        final Button closeDialog = mView.findViewById(R.id.btnClose);
        ImageView ivLike = mView.findViewById(R.id.ivlike);
        ImageView ivYes = mView.findViewById(R.id.ivyes);
        ImageView ivAdd = mView.findViewById(R.id.ivadd);
        ImageView ivDisLike = mView.findViewById(R.id.ivdislike);
        ImageView ivNo = mView.findViewById(R.id.ivno);
        ImageView ivMinus = mView.findViewById(R.id.ivminus);
        ImageView ivBack = mView.findViewById(R.id.back);
        ImageView ivHome = mView.findViewById(R.id.home);
        ImageView ivKeyboard = mView.findViewById(R.id.keyboard);
        ViewCompat.setAccessibilityDelegate(ivLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivAdd, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivDisLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivMinus, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivBack, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(enterCategory, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(closeDialog, new TalkbackHints_SingleClick());
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        enterCategory.setText(mSpeak);
        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(mSpeechText[getTagPos()]);
                mUec.createSendFbEventFromTappedView(12, mDisplayText[getTagPos()]
                        .replace("…",""), "");
            }
        });

        enterCategory.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);
                if(event.getEventType() != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    mView.findViewById(R.id.txTitleHidden).
                            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all selection in current level
                clearSelectionAfterAccessibilityDialogClose();
                disabledView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                //dismiss dialog
                dialog.dismiss();
                //Firebase event
                singleEvent("Navigation","Back");
            }
        });

        final ImageView[] expressiveBtns = {ivLike, ivYes, ivAdd, ivDisLike, ivNo, ivMinus};

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvLike.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LIKE);
            }
        });
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvYes.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.YES);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvMore.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.MORE);
            }
        });
        ivDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvDontLike.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.DONT_LIKE);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNo.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.NO);
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvLess.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LESS);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvBack.performClick();
                dialog.dismiss();
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvKeyboard.performClick();
                dialog.dismiss();
            }
        });
        LevelUiUtils.setExpressiveIconConditionally(expressiveBtns, level3IconObjects
                [mArrSort[mLevelThreeItemPos]]);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearSelectionAfterAccessibilityDialogClose();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                disabledView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2; //style id
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isNotchDevice()) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void clearSelectionAfterAccessibilityDialogClose() {
        LevelUiUtils.enableAllExpressiveIcon(expressiveBtn);
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        mLevelThreeItemPos= GlobalConstants.NOT_SELECTED;
        LevelUiUtils.resetRecyclerAllItems(LevelThreeActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mShouldReadFullSpeech = false;
        mFlgImage = GlobalConstants.NO_EXPR;
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

        //Retrieve icon names from json file for Level 1
        mIconCode = IconFactory.getL3IconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this),
                getLevel2_3IconCode(levelOneItemPos),
                getLevel2_3IconCode(levelTwoItemPos)
        );

        level3IconObjects = TextFactory.getIconObjects(mIconCode);

        mDisplayText = TextFactory.getDisplayText(level3IconObjects);
        mSpeechText = TextFactory.getSpeechText(level3IconObjects);
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

    private String getLevel2_3IconCode(int level2_3Position){
        if(level2_3Position+1 <= 9){
            return "0" + (level2_3Position + 1);
        } else {
            return Integer.toString(level2_3Position+1);
        }
    }

    public void hideCustomKeyboardDialog() {
        mIvKeyboard.setImageResource(R.drawable.keyboard);
        mIvBack.setImageResource(R.drawable.back);
    }
}