package com.dsource.idc.jellowintl.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
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
import com.dsource.idc.jellowintl.activities.adapters.LevelTwoAdapter;
import com.dsource.idc.jellowintl.activities.adapters.PeopleAdapter;
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
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utility.UserEventCollector;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.factories.IconFactory.getIconCode;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class LevelTwoActivity extends LevelBaseActivity{
    private final int REQ_HOME = 0;
    private final int CATEGORY_ICON_PEOPLE = 5;
    private final int CATEGORY_ICON_HELP = 8;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = GlobalConstants.SHORT_SPEECH, mFlgYes = GlobalConstants.SHORT_SPEECH,
            mFlgMore = GlobalConstants.SHORT_SPEECH, mFlgDntLike = GlobalConstants.SHORT_SPEECH,
            mFlgNo = GlobalConstants.SHORT_SPEECH, mFlgLess = GlobalConstants.SHORT_SPEECH;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = -1;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount;
    /*Image views which are visible on the layout such as six expressive buttons, below navigation
      buttons and speak button when keyboard is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack;
    /*Input text view to speak custom text.*/
    /*Recycler view which will populate category icons.*/
    private RecyclerView mRecyclerView;

    /*This variable indicates index of category icon selected in level one and two respectively.*/
    private int mLevelOneItemPos, mLevelTwoItemPos = GlobalConstants.NOT_SELECTED;
    /*This variable indicates index of category icon in adapter in level 2. This variable is
     different than mLevelTwoItemPos. */
    private int mSelectedItemAdapterPos = GlobalConstants.NOT_SELECTED;
    /*This flag is sets to true, when category icon is pressed followed by an expressive button;
     in this case the full sentence associated with selected expressive button is spoken.
     In case flag is false only expressive button is spoken out.*/
    private boolean mShouldReadFullSpeech = false, mSearched = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*This variable store current action bar title.*/
    private String mActionBarTitle;

    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mSpeechText, mDisplayText, mExprBtnTxt,
            mNavigationBtnTxt, mIconCode;
    /*Below array stores tap count and index sort array respectively. This variables are used
     only, when in level one people or places category is
      selected.*/
    private Integer[] mArrPeopleTapCount, mArrSort;

    private String txtActionBarTitle, mSpeak, mEnterCat;

    /*Firebase event Collector class instance.*/
    private UserEventCollector mUec;

    private Icon[] level2IconObjects;

    private ImageView[] expressiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        // Get index of category icons (position in recycler view) selected in level one.
        mLevelOneItemPos = getIntent().getExtras().getInt(getString(R.string.level_one_intent_pos_tag));
        // Get and set title of category icons selected in level one.
        txtActionBarTitle = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag));
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
        mUec = new UserEventCollector();
        // The below string has value "" in english (all regions) and "है।" in Hindi (India).
        // It is used when user select category "Help" -> "About me".
        // To complete full sentence verbiage in Hindi below text is used.
        loadArraysFromResources();
        initializeArrayListOfRecycler();
        initializeLayoutViews();
        initializeRecyclerViewAdapter();
        initializeViewListeners();
        mSpeak = getString(R.string.speak);
        mEnterCat = getString(R.string.enter_category);
        /*Event recorded: Event{appId='com.dsource.idc.jellowintl.debug'*/ /*GridExpressiveIcon*/
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
    }

    //A scrollListener field to listen when recycler view have done populating the data
    RecyclerView.OnScrollListener scrollListener;
    private void highlightSearchedItem() {
        //Referring to the Intent that invoked the activity
            final int p1 = getIntent().getExtras().getInt(getString(R.string.level_one_intent_pos_tag));
            final int s = getIntent().getExtras().getInt(getString(R.string.search_parent_1));
            final int gridCode=new SessionManager(this).getGridSize();
            //Deciding the gridSize according to the gridCode
            int gridSize;
            if(gridCode==GlobalConstants.NINE_ICONS_PER_SCREEN)
            gridSize=8;
            else gridSize=2;
            //Case with sorting is people which needs sorting.
            if(p1==CATEGORY_ICON_PEOPLE)
            {
                final int sortedIndex=getSortedIndex(s);
                if(sortedIndex>gridSize) {
                    scrollListener =null;
                    scrollListener = getListener(sortedIndex);
                    mRecyclerView.addOnScrollListener(scrollListener);
                    mRecyclerView.smoothScrollToPosition(sortedIndex);
                }
                else
                    setSearchHighlight(sortedIndex);

            }
            else // if not person and place then there's no need of scrolling
            {
                if(s>gridSize) {
                    scrollListener =null;
                    scrollListener = getListener(s);
                    mRecyclerView.addOnScrollListener(scrollListener);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,s);
                }
                else
                    setSearchHighlight(s);
            }
    }//End of highlightSearch

    private RecyclerView.OnScrollListener getListener(final int index) {
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {//Wait untill the scrolling is done
                }
                else if(newState==RecyclerView.SCROLL_STATE_IDLE) //Try highlighting when the scrolling is done
                    setSearchHighlight(index);
            }
        };
        return scrollListener;
    }

    //This scrollListener fields listens the callback from recycler finished laying out the views
    ViewTreeObserver.OnGlobalLayoutListener populationDoneListener;
    public void setSearchHighlight(final int pos)
    {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        populationDoneListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete and the
                //dimensions of recyclerView and any child views are known.
                View searchedView = mRecyclerItemsViewList.get(pos);
                if(searchedView==null) {
                    //if the searched view is null then try scrolling again
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,pos );
                    return;
                }
                //When view is found remove the scrollListener and manually tap the icon.
                tappedCategoryItemEvent(searchedView, pos);
                mRecyclerView.removeOnScrollListener(scrollListener);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    searchedView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
              }
        };
        //Adding the scrollListener to the recycler view
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LevelTwoActivity.class.getSimpleName());
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
        stopMeasuring("LevelTwoActivity");
    }

    @Override
    public void onDestroy() {
        mRecyclerView = null;
        //mLayerTwoSpeech = null;
        mRecyclerItemsViewList = null;
        mExprBtnTxt = null;
        mArrSort = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If user pressed home button in level three, it indicate that user be redirected to level
        // one (MainActivity). When this activity receives RESULT_CANCELED as resultCode it
        // understands HomeActivity request is generated, so it closes itself.
        if(requestCode == REQ_HOME && resultCode == RESULT_CANCELED)
            finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void clearSelectionAfterAccessibilityDialogClose() {
        LevelUiUtils.enableAllExpressiveIcon(expressiveBtn);
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        LevelUiUtils.resetRecyclerAllItems(LevelTwoActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mActionBtnClickCount = -1;
        mShouldReadFullSpeech = false;
        mFlgImage = -1;
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
        mIvBack.setAlpha(GlobalConstants.ENABLE_ALPHA);
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
        /**
         * if GridSize is 3 then scroll faster than GridSize 9
         * */
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
     * <p>This function will initialize the adapter for recycler view. In this level two
     * types of adapter are used:
     *      a) {@link PeopleAdapter}
     *      b) {@link LevelTwoAdapter}
     * As per the category icon selected in the {@link MainActivity}, an adapter is selected and hence
     * category icons are populated in this level.</p>
     * */
    private void initializeRecyclerViewAdapter() {
        if(mLevelOneItemPos != CATEGORY_ICON_PEOPLE) {
            mRecyclerView.setAdapter(new LevelTwoAdapter(this, level2IconObjects));
        }else{
            mRecyclerView.setAdapter(new PeopleAdapter(this, level2IconObjects, mArrSort));
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
                    LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this, view, true,
                            mActionBtnClickCount, mFlgImage);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this, view, false,
                        mActionBtnClickCount, mFlgImage);
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
     *  successful closure (RESULT_OK) of a screen. User will returned back to {@link MainActivity}.
     * </p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Firebase event
                singleEvent("Navigation","Back");
                speak(mNavigationBtnTxt[1]);
                mUec.createSendFbEventFromTappedView(27, "", "");
                mIvBack.setImageResource(R.drawable.back_pressed);
                String str = getIntent().getExtras().getString(getString(R.string.from_search));
                boolean close =getIntent().getExtras().getBoolean("search_and_back");
                if((str != null && !str.isEmpty() && str.equals(getString(R.string.search_tag)))
                        || close){
                    startActivity(new Intent(LevelTwoActivity.this, MainActivity.class));
                }else {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation home button.
     * When user press this button user navigated to {@link MainActivity} with
     *  every state of views is like app launched as fresh. Action bar title is set
     *  to 'home'</p>
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
                if(getIntent().getExtras().getString(getString(R.string.from_search))!=null) {
                    if (getIntent().getExtras().getString(getString(R.string.from_search))
                            .equals(getString(R.string.search_tag))) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(getString(R.string.goto_home), true);
                        startActivity(intent);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finishAffinity();
                            }
                        },300);
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(getString(R.string.goto_home), true);
                    startActivity(intent);
                    setResult(RESULT_CANCELED);
                    finishAffinity();
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "keyboard" button.
     * {@link LevelThreeActivity} navigation keyboard button either enable or disable
     * the custom keyboard input text layout.
     * When custom keyboard input text layout is enabled using keyboard button, it is visible to user
     * and action bar title set to "keyboard".
     * When custom keyboard input text layout is disabled using keyboard button, the state of the
     * {@link LevelThreeActivity} retrieved as it was before opening custom keyboard
     * input text layout.
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase event
                singleEvent("Navigation", "Keyboard");
                new DialogKeyboardUtterance().show(LevelTwoActivity.this);
                speak(mNavigationBtnTxt[2]);
                mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                mIvBack.setImageResource(R.drawable.back);
                mIvHome.setImageResource(R.drawable.home);
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
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                //Value of mFlgImage = 0, indicates like expressive button is pressed
                mFlgImage = GlobalConstants.LIKE;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set like button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgLike is 1, then should speak "really like" expression
                    // verbiage associated to selected category icon.
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getLL());
                            mUec.createSendFbEventFromTappedView(14,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"LL","");
                        // If Help -> About me category icon is selected,
                        // "really like" expression will speak child's name
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getLL());
                            }else{
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getL()
                                        .replace("_", getSession().getName());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(14,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"LL","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getLL());
                            mUec.createSendFbEventFromTappedView(14,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"LL","");
                        }
                        //reset mFlgLike to speak "like" expression
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgLike is 0 then Speak associated like expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getL());
                            mUec.createSendFbEventFromTappedView(13,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"L0","");
                            // If Help -> About me category icon is selected,
                            // "really like" expression will speak child's name
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getL());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getL()
                                        .replace("_", getSession().getName());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(13,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"L0","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getL());
                            mUec.createSendFbEventFromTappedView(13,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"L0","");
                        }
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
                //Value of mFlgImage = 1, indicates don't like expressive button is pressed
                mFlgImage = GlobalConstants.DONT_LIKE;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set don't like button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set don't like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);

                    // if value of mFlgDntLike is 1, then should speak "really don't like" expression
                    // verbiage associated to selected category icon.
                    if (mFlgDntLike == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getDD());
                            mUec.createSendFbEventFromTappedView(20,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"DD","");
                        // If Help -> About me category icon is selected,
                        // "really don't like" expression will speak child's caregiver's name
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getDD());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getDD()
                                        .replace("_", getSession().getCaregiverName());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(20,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"DD","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getDD());
                            mUec.createSendFbEventFromTappedView(20,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"DD","");
                        }
                        //reset mFlgDntLike to speak "don't like" expression
                        mFlgDntLike = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgDntLike is 0 then Speak associated don't like expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To get correct speech text sort
                        // is applied.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE){
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getD());
                            mUec.createSendFbEventFromTappedView(19,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"D0","");
                        // If Help -> About me category icon is selected,
                        // "really don't like" expression will speak child's caregiver name
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getDD());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getDD()
                                        .replace("_", getSession().getCaregiverName());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(19,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                    +"_"+ mIconCode[mLevelTwoItemPos]+"D0","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getD());
                            mUec.createSendFbEventFromTappedView(19,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"D0","");
                        }
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
                //Value of mFlgImage = 2, indicates yes expressive button is pressed
                mFlgImage = GlobalConstants.YES;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set yes button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                } else  {
                    ++mActionBtnClickCount;
                    // Set yes button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgYes is 1, then should speak "really yes" expression
                    // verbiage associated to selected category icon.
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getYY());
                            mUec.createSendFbEventFromTappedView(16,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"YY","");
                        // If Help -> About me category icon is selected,
                        // "really yes" expression will speak caregivers email id
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getYY());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getYY();
                                speechTxt = speechTxt.replace("_", getSession().getEmailId()
                                        .replaceAll(".", ",$0 ").replace(".", "dot"));
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(16,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"YY","");
                        }else{
                            speak(level2IconObjects[mLevelTwoItemPos].getYY());
                            mUec.createSendFbEventFromTappedView(16,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"YY","");
                        }
                        //reset mFlgYes to speak "yes" expression
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgYes is 0 then Speak associated yes expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getY());
                            mUec.createSendFbEventFromTappedView(15,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"Y0","");
                        // If Help -> About me category icon is selected,
                        // "yes" expression will speak caregivers email id
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getY());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getY();
                                speechTxt = speechTxt.replace("_", getSession().getEmailId()
                                        .replaceAll(".", ",$0 ").replace(".", "dot"));
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(15,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"Y0","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getY());
                            mUec.createSendFbEventFromTappedView(15,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"Y0","");
                        }
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
                //Value of mFlgImage = 3, indicates no expressive button is pressed
                mFlgImage = GlobalConstants.NO;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set no button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                // if value of mShouldReadFullSpeech is true, then speak associated no
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set no button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgNo is 1, then should speak "really no" expression
                    // verbiage associated to selected category icon.
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getNN());
                            mUec.createSendFbEventFromTappedView(22,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"NN","");
                            // If Help -> About me category icon is selected,
                            // "really no" expression will speak child's address
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getNN());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getNN()
                                        .replace("_", getSession().getAddress());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(22,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"NN","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getNN());
                            mUec.createSendFbEventFromTappedView(22,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"NN","");
                        }
                        //reset mFlgNo to speak "no" expression
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgNo is 0 then Speak associated like expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getN());
                            mUec.createSendFbEventFromTappedView(21,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"N0","");
                            // If Help -> About me category icon is selected,
                            // "really no" expression will speak child's address
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getN());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getNN()
                                        .replace("_", getSession().getAddress());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(21,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"N0","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getN());
                            mUec.createSendFbEventFromTappedView(21,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"N0","");
                        }
                        //reset mFlgLike to speak "really no" expression
                        mFlgNo = GlobalConstants.LONG_SPEECH;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "more" button.
     * Expressive more button is works in four ways:
     *  a) press expressive more button once
     *  b) press expressive more button twice
     *  c) press category icon first then press expressive more button once
     *  d) press category icon first then press expressive more button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except more button
     *  b) reset all expressive buttons
     *  c) produce speech using output for more
     *  d) set border to category icon of color associated with more button</p>
     * */
    private void initMoreBtnListener() {
        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the more button all expressive button speech flag (except more)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                //Value of mFlgImage = 4, indicates more expressive button is pressed
                mFlgImage = GlobalConstants.MORE;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set more button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                // if value of mShouldReadFullSpeech is true, then speak associated more
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set more button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgmore is 1, then should speak "really more" expression
                    // verbiage associated to selected category icon.
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getMM());
                            mUec.createSendFbEventFromTappedView(18,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"MM","");
                        // If Help -> About me category icon is selected,
                        // "really more" expression will speak caregivers number
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getMM());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getMM();
                                speechTxt = speechTxt.replace("_", getContactWithSpacesBetweenDigits());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(18,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"MM","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getMM());
                            mUec.createSendFbEventFromTappedView(18,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"MM","");
                        }
                        //reset mFlgMore to speak "more" expression
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgMore is 0 then Speak associated more expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getM());
                            mUec.createSendFbEventFromTappedView(17,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"M0","");
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1) {
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getM());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getMM();
                                speechTxt = speechTxt.replace("_", getContactWithSpacesBetweenDigits());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(17,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"M0","");
                        }else {
                            speak(level2IconObjects[mLevelTwoItemPos].getM());
                            mUec.createSendFbEventFromTappedView(17,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"M0","");
                        }
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
                // When user press the like button all expressive button speech flag (except less)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = GlobalConstants.SHORT_SPEECH;
                //Value of mFlgImage = 5, indicates less expressive button is pressed
                mFlgImage = GlobalConstants.LESS;
                // If selected category icon is Help -> About me, then showDialog
                // expressive icons of About me category.
                // and set less button icon to pressed using mFlgImage.
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
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
                        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);

                    // if value of mFlgLess is 1, then should speak "really less" expression
                    // verbiage associated to selected category icon.
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getSS());
                            mUec.createSendFbEventFromTappedView(24,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"SS","");
                        // If Help -> About me category icon is selected,
                        // "really less" expression will speak child's blood group
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getSS());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getS()
                                        .replace("_", getBloodGroup());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(24,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"SS","");
                        } else {
                            speak(level2IconObjects[mLevelTwoItemPos].getSS());
                            mUec.createSendFbEventFromTappedView(24,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"SS","");
                        }
                        //reset mFlgLess to speak "less" expression
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                    // if value of mFlgLess is 0 then Speak associated less expression
                    // verbiage to selected category icon.
                    } else {
                        // People and places will have preferences. To speak the correct speech text
                        // preference sort array is used.
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
                            speak(level2IconObjects[mArrSort[mLevelTwoItemPos]].getS());
                            mUec.createSendFbEventFromTappedView(23,
                    level2IconObjects[mArrSort[mLevelTwoItemPos]].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"S0","");
                        // If Help -> About me category icon is selected,
                        // "really less" expression will speak child's blood group
                        }else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            if(isNoTTSLanguage()) {
                                speakInQueue(level2IconObjects[mLevelTwoItemPos].getS());
                            }else {
                                String speechTxt = level2IconObjects[mLevelTwoItemPos].getS()
                                        .replace("_", getBloodGroup());
                                speak(speechTxt);
                            }
                            mUec.createSendFbEventFromTappedView(23,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"S0","");
                        } else {
                            speak(level2IconObjects[mLevelTwoItemPos].getS());
                            mUec.createSendFbEventFromTappedView(23,
                    level2IconObjects[mLevelTwoItemPos].getEvent_Tag()
                                +"_"+ mIconCode[mLevelTwoItemPos]+"S0","");
                        }
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
     * @param position is position of a tapped category icon in the RecyclerView.
     * This function
     *             a) Clear every expressive button flags
     *             b) Reset expressive button icons
     *             c) Reset category icon views
     *             d) Set the border to newly selected category icon
     *             e) If same category icon clicked twice that category will open up.
     *             f) Set action bar title.
     *             g) Increment preference count of tapped category icon.</p>
     * */
    public void tappedCategoryItemEvent(View view, int position) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
        // Clear the last expressive button selection. Set each expressive button to unpressed.
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        // reset every populated category icon before setting the border to selected icon.
        LevelUiUtils.resetRecyclerAllItems(LevelTwoActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mActionBtnClickCount = 0;
        // set border to selected category icon
        LevelUiUtils.setBorderToCategoryIcon(LevelTwoActivity.this, view, true,
                mActionBtnClickCount, mFlgImage);
        // set true to speak verbiage associated with category icon
        mShouldReadFullSpeech = true;
        String title = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag))+ " ";
        // if user is in people or places category and user pressed any of the category icon then
        // create bundle for firebase event.
        // bundle has values category icon position (index), "level two"
        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
            if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
                showAccessibleDialog(position, view);
                view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                mUec.accessibilityPopupOpenedEvent(mSpeechText[position]);
            }else {
                if (!mSearched)
                    speak(mSpeechText[position]);
                else
                    speakWithDelay(mSpeechText[position]);
                mSearched = false;
                mUec.createSendFbEventFromTappedView(12, mDisplayText[position], "");
            }
        // In below if category icon selected in level one is neither people nor help.
        // Also, mLevelTwoItemPos == position is true it means user taps twice on same category icon.
        // If above both conditions are true then open category icon selected in level three.
        }else if(mLevelTwoItemPos == position && mLevelOneItemPos != CATEGORY_ICON_HELP){
            if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
                showAccessibleDialog(position, view);
                view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
		    }else {
                // set intent to open level three category
                Intent intent = new Intent(LevelTwoActivity.this, LevelThreeActivity.class);
                int CATEGORY_ICON_DAILY_ACT = 1;
                if (mLevelOneItemPos == CATEGORY_ICON_DAILY_ACT && LevelUiUtils.isSequencePosition(position))
                    intent = new Intent(LevelTwoActivity.this, SequenceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Icon", "Opened " + mDisplayText[position].replace("…", ""));
                bundleEvent("Grid", bundle);
                //intent to open new activity have extra data such position of level one category icon,
                // level two category icon and action bar title (bread crumb)
                intent.putExtra(getString(R.string.level_one_intent_pos_tag), mLevelOneItemPos);
                intent.putExtra(getString(R.string.level_2_item_pos_tag), mLevelTwoItemPos);
                intent.putExtra(getString(R.string.intent_menu_path_tag), mActionBarTitle + "/");
                startActivityForResult(intent, REQ_HOME);
            }
        }else {
            //If user tapped the Help -> Emergency category icon and
            // user enabled the calling from app and
            // if user have sim device and ready to call then
            // skip speaking speech or else speak speech to every category icons
            if(mLevelOneItemPos == CATEGORY_ICON_HELP && position == 0 &&
                    getSession().isCallingEnabled() &&
                    isDeviceReadyToCall((TelephonyManager)getSystemService
                            (Context.TELEPHONY_SERVICE))){}
            else {
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE)) &&
                        mLevelOneItemPos == CATEGORY_ICON_HELP){
                    showAccessibleDialog(position, view);
                    view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(mDisplayText[position]);
                }else if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
                    showAccessibleDialog(position, view);
                    view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.createSendFbEventFromTappedView(12, mDisplayText[position]
                            .replace("…",""), "");
                }else {
                    if (!mSearched)
                        speak(mSpeechText[position]);
                    else
                        speakWithDelay(mSpeechText[position]);
                    mSearched = false;
                    mUec.createSendFbEventFromTappedView(12, mDisplayText[position]
                            .replace("…",""), "");
                }
            }
        }
        mLevelTwoItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);

        // set title to breadcrumb (or actionbar) of activity.
        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE ||
                mLevelOneItemPos == CATEGORY_ICON_HELP)
            title += mDisplayText[mLevelTwoItemPos];
        else
            title += mDisplayText[mLevelTwoItemPos].substring
                    (0, mDisplayText[mLevelTwoItemPos].length()-1);
        mActionBarTitle = title;

        // increment preference count an item if people or place category is selected in level one
        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE)
            LevelUiUtils.incrementTouchCountOfItem(mArrPeopleTapCount, mArrSort,
                    mLevelTwoItemPos, mLevelOneItemPos, -1,
                    LanguageFactory.getCurrentLanguageCode(this), getAppDatabase());

        // Help -> About me category have different set of expressive buttons. If user selected
        // About me category then all expressive button icons are changed.
        // sending -1 value to setExpressiveButtonToAboutMe(-1), will only change expressive
        // button icons to About me icons.
        if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 1)
            setExpressiveButtonToAboutMe(-1);
        if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 0 &&
                getSession().isCallingEnabled()) {
            stopSpeaking();
            LevelUiUtils.startCall(LevelTwoActivity.this,
                    "tel:" + getSession().getCaregiverNumber());
        }
        LevelUiUtils.setExpressiveIconConditionally(expressiveBtn, level2IconObjects[mLevelTwoItemPos]);
        mIvBack.setImageResource(R.drawable.back);
    }

    /**
     * <p>This function will set capacity of ArrayList holding RecyclerView items to the array size.
     * Array is selected based on category icon opened in {@link MainActivity}</p>
     * */
    private void initializeArrayListOfRecycler() {
        int size = mDisplayText.length;
        // Set the capacity of mRecyclerItemsViewList list to total number of category icons to be
        // populated on the screen.
        mRecyclerItemsViewList = new ArrayList<>(size);
        while(mRecyclerItemsViewList.size() <= size) mRecyclerItemsViewList.add(null);
    }

    /**
     * <p>This function will:
     *     a) Read speech text from arrays for navigation buttons.
     *     b) Read speech text from arrays for expressive buttons.
     *     c) Read verbiage lines into {@link Icon} model.
     *     d) Read speech and adapter text from arrays for category icons
     *        for People/Places if in {@link MainActivity } mArrIcon/places
     *        category is selected.
     *     e) Read speech and adapter text from arrays for category icons
     *        other than People/Places.</p>
     * */
    private void loadArraysFromResources() {

        // fill speech and adapter text arrays

        mIconCode = IconFactory.getL2IconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this),
                getLevel2IconCode(mLevelOneItemPos)
        );

        level2IconObjects = TextFactory.getIconObjects(mIconCode);
        mDisplayText = TextFactory.getDisplayText(level2IconObjects);
        mSpeechText = TextFactory.getSpeechText(level2IconObjects);

        if(mLevelOneItemPos == 5){
            useSortToLoadArray(mSpeechText, mDisplayText, mIconCode);
        }

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
        mNavigationBtnTxt = TextFactory.getTitle(miscellaneousIconObjects);
    }

    private void showAccessibleDialog(final int position, final View disabledView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LevelTwoActivity.this);
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
                if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE ||
                        mLevelOneItemPos == CATEGORY_ICON_HELP){
                    // clear pending Firebase events.
                    mUec.clearPendingEvent();
                }
                mIvBack.performClick();
                dialog.dismiss();
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE ||
                        mLevelOneItemPos == CATEGORY_ICON_HELP){
                    // clear pending Firebase events.
                    mUec.clearPendingEvent();
                }
                mIvHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE ||
                        mLevelOneItemPos == CATEGORY_ICON_HELP){
                    // clear pending Firebase events.
                    mUec.clearPendingEvent();
                }
                clearSelectionAfterAccessibilityDialogClose();
                mIvKeyboard.performClick();
                dialog.dismiss();
            }
        });

        //If user opened the People or Help category.
        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_HELP){
            if(mLevelOneItemPos == CATEGORY_ICON_HELP && position == 0){
                enterCategory.setText(mDisplayText[position]);
            }else {
                enterCategory.setText(mSpeak);
            }
            if(mLevelOneItemPos == CATEGORY_ICON_HELP && position == 1){
                //change icon images of dialog expressive button to about me expressive button icons
                ivLike.setImageResource(R.drawable.mynameis);
                ivDisLike.setImageResource(R.drawable.caregiver);
                ivYes.setImageResource(R.drawable.email);
                ivNo.setImageResource(R.drawable.address);
                ivAdd.setImageResource(R.drawable.contact);
                ivMinus.setImageResource(R.drawable.bloodgroup);
                ivLike.setContentDescription(getString(R.string.child_s_name_dialog_btn));
                ivDisLike.setContentDescription(getString(R.string.caregiverName_dialog_btn));
                ivYes.setContentDescription(getString(R.string.caregiver_s_email_address_dialog_btn));
                ivNo.setContentDescription(getString(R.string.homeAddress_dialog_btn));
                ivAdd.setContentDescription(getString(R.string.caregiver_s_contact_number_dialog_btn));
                ivMinus.setContentDescription(getString(R.string.bloodGroup_dialog_btn));
            }else if(mLevelOneItemPos == CATEGORY_ICON_HELP){
                LevelUiUtils.setExpressiveIconConditionally(expressiveBtns, level2IconObjects[position]);
            }
            enterCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speak(mSpeechText[position]);
                    //Send People and Help categories events directly to Firebase.
                    mUec.createSendFbEventFromTappedView(12, mDisplayText[position].replace("…", ""), "");
                }
            });
        }else {
            enterCategory.setText(mEnterCat);
            enterCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", "Opened " + mDisplayText[position].replace("…", ""));
                    bundleEvent("Grid", bundle);
                    Intent intent = new Intent(LevelTwoActivity.this, LevelThreeActivity.class);
                    if (mLevelOneItemPos == 1 && LevelUiUtils.isSequencePosition(position))
                        intent = new Intent(LevelTwoActivity.this, SequenceActivity.class);
                    intent.putExtra(getString(R.string.level_one_intent_pos_tag), mLevelOneItemPos);
                    intent.putExtra(getString(R.string.level_2_item_pos_tag), position);
                    intent.putExtra(getString(R.string.intent_menu_path_tag), mActionBarTitle + "/");
                    startActivityForResult(intent, REQ_HOME);
                    dialog.dismiss();
                }
            });
        }

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
                dialog.dismiss();
                //clear all selection
                clearSelectionAfterAccessibilityDialogClose();
                if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE ||
                        mLevelOneItemPos == CATEGORY_ICON_HELP){
                    //Firebase event
                    singleEvent("Navigation","Back");
                }
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

    /**
     * <p>This function will find and return the blood group of user
     * @return blood group of user.</p>
     * */
    private String getBloodGroup() {
        switch(getSession().getBlood()){
            case  1: return getString(R.string.aPos);
            case  2: return getString(R.string.aNeg);
            case  3: return getString(R.string.bPos);
            case  4: return getString(R.string.bNeg);
            case  5: return getString(R.string.abPos);
            case  6: return getString(R.string.abNeg);
            case  7: return getString(R.string.oPos);
            case  8: return getString(R.string.oNeg);
            default: return "";
        }
    }

    /**
     * <p>This function add spaces between each in character digit in caregiver number.
     * Also, it replaces '+' symbol with 'plus' text.
     * This form of caregiver number used to speak mobile number via Speech Engine in
     * Help -> About Me category icon.
     * @return The modified caregiver number.</p>
     * */
    private String getContactWithSpacesBetweenDigits() {
        String contact = getSession().getCaregiverNumber();
        contact =
                (SessionManager.ES_ES+SessionManager.FR_FR). contains(getSession().getLanguage()) ?

                        contact.replaceAll("[0-9]{2}", "$0, ").
                                replace("+", "plus ")    :

                        contact.replaceAll(".", "$0, ").
                                replace("+", "plus");
        return contact;
    }

    /**
     * <p>This function first set the all expressive button to About me expressive buttons.
     * Then set an about me expressive button to pressed using {@param image_flag}.
     * image_flag identifies which expressive button is pressed.
     * If user pressed had previously pressed any expressive button (i.e. state of any expressive
     * button is pressed) then user is pressed some other expressive button, it is needed to
     * clear previously pressed expressive button state and home button state (if pressed).
     * {@param image_flag} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.
     *  To set home button to pressed state image_flag value must be 6</p>
     * */
    private void setExpressiveButtonToAboutMe(int image_flag){
        //change icon images of expressive button to about me expressive button icons
        mIvLike.setImageResource(R.drawable.mynameis);
        mIvDontLike.setImageResource(R.drawable.caregiver);
        mIvYes.setImageResource(R.drawable.email);
        mIvNo.setImageResource(R.drawable.address);
        mIvMore.setImageResource(R.drawable.contact);
        mIvLess.setImageResource(R.drawable.bloodgroup);
        // set expressive button to pressed state
        switch (image_flag){
            case GlobalConstants.LIKE: mIvLike.setImageResource(R.drawable.mynameis_pressed); break;
            case GlobalConstants.YES: mIvYes.setImageResource(R.drawable.email_pressed); break;
            case GlobalConstants.MORE: mIvMore.setImageResource(R.drawable.contact_pressed); break;
            case GlobalConstants.DONT_LIKE: mIvDontLike.setImageResource(R.drawable.caregiver_pressed); break;
            case GlobalConstants.NO: mIvNo.setImageResource(R.drawable.address_pressed); break;
            case GlobalConstants.LESS: mIvLess.setImageResource(R.drawable.blooedgroup_pressed); break;
            default: break;
        }
    }

    /**
     * <p>This function will create sort index array for people/places category.
     * If any category from people or places is not selected in level one {@link MainActivity}
     * then this function is not used.
     * As per the category icon selected in the level one {@link MainActivity}
     * category icons are populated in this level using preferences.
     * "Most tapped category icon will have highest preference value",
     * in this fashion preferences are defined for category icons.
     * In level two, all category icon preferences are stored in session
     * preferences (shared preferences). Preferences are stored in the
     * from comma separated values string.
     *  e.g. "5,4,9,2,5"
     * This preference string is retrieved using {@link SessionManager} class.
     * Then each value in preference string is converted into individual tokens and
     * followed by storing it into arrays. This array is known as Tap count array. Value
     * in 0th index of tap count array is tap count (number of times user tapped) for 0th element
     * in adapter array/speech array.
     *  e.g mArrIconTapCount = [5,4,9,2,5]
     * Preferences are applied to a category during adapter setup only. To apply preferences to a
     * category below steps to be followed sequentially:
     *   1. Retrieves users stored preferences from {@link SessionManager} as "savedString".
     *   2. Convert savedString into tokens and then store preferences "savedString"
     *      into preference array.
     *   3. From preference array create index array. Index array is a integer array, it
     *      has index of most-preferred/most-tapped element first,
     *      then followed by second most-preferred/most-tapped element index and so on.
     *      e.g. Consider preference array as
     *      [5, 4, 9 , 2, 5] then its index array is [2, 0, 4, 1, 3].
     *   4. Then speech and below text array elements are rearranged using index array. In this
     *      step, Speech and below text array elements are arranged in such a way that most
     *      used icon will appear first in the category.
     * @param arrSpeechTxt, raw speech text array,  used to sort the speech elements.
     * @param arrAdapterTxt, raw below text array,  used to sort the below text elements.</p>
     * @param level2Icons */
    private void useSortToLoadArray(String[] arrSpeechTxt, String[] arrAdapterTxt, String[] level2Icons) {
        // Get preference string from shared preferences into "savedString" variable
        String savedString = PreferencesHelper.getPrefString(getAppDatabase(), getIconCode(
                LanguageFactory.getCurrentLanguageCode(this), mLevelOneItemPos, -1));

        // Extra 0's are concat ed at the end of "savedString" variable. This
        // will make length of array and length of savedString to equal to load category.
        if(!savedString.isEmpty() && savedString.split(",").length != arrAdapterTxt.length){
            while (savedString.split(",").length != arrAdapterTxt.length)
                savedString = savedString.concat("0,");
        }

        // Create temporary array of user taps on category icons
        Integer[]  mArrIconTapCount = new Integer[arrAdapterTxt.length];
        mArrSort = new Integer[arrAdapterTxt.length];

        //initialize mArrIconTapCount[i] = 0 so that every category icon have same preference.
        //mArrSort array is global index sort array.
        for (int i = 0; i < arrAdapterTxt.length; ++i) {
            mArrIconTapCount[i] = 0;
            mArrSort[i] = i;
        }

        // Each value in savedString is now converted into individual tokens.
        if(!savedString.equals("")){
            StringTokenizer st = new StringTokenizer(savedString, ",");

            // create tap count array (mArrIconTapCount) of user taps using individual these tokens
            for (int i = 0; i < arrAdapterTxt.length; ++i)
                mArrIconTapCount[i] = Integer.parseInt(st.nextToken());
        }

        //mArrPeopleTapCount is global variable to activity;
        // It holds tap count of each sub-category icon within People or places categories.
        mArrPeopleTapCount = new Integer[arrAdapterTxt.length];
        for(int i = 0; i< arrAdapterTxt.length ; ++i)
            mArrPeopleTapCount[i] = mArrIconTapCount[i];

        //create index sort array using tap count array (mArrIconTapCount)
        IndexSorter<Integer> is = new IndexSorter<Integer>(mArrIconTapCount);
        is.sort();

        //get index sort array for given tap count array(mArrIconTapCount)
        mArrSort =  new Integer[level2IconObjects.length];
        int j = -1;
        for (Integer i : is.getIndexes()) {
            mArrSort[++j] = i;
        }

        mSpeechText = new String[mArrIconTapCount.length];
        mDisplayText = new String[mArrIconTapCount.length];
        mIconCode = new String[mArrIconTapCount.length];
        int idx;
        // arrange speech and adapter icons using preferences.
        for (int i = 0; i < mArrIconTapCount.length; ++i) {
            idx = mArrSort[i];
            mSpeechText[i] = arrSpeechTxt[idx];
            mDisplayText[i] = arrAdapterTxt[idx];
            mIconCode[i] = level2Icons[i];
        }
    }

    /**<p> This function will returns the index of the item searched item in the sorted list
     * it takes default index of the searched item and returns the actual sorted index of the element.
     * .</p>**/
    private int getSortedIndex(int index)
    {
        for(int i=0;i<mArrSort.length;i++)
        {
            if(index==mArrSort[i])
                return i;
        }
        return -1;

    }

    private String getLevel2IconCode(int level1Position) {
        if (level1Position + 1 <= 9) {
            return "0" + (level1Position + 1);
        } else {
            return Integer.toString(level1Position + 1);
        }
    }

    public void hideCustomKeyboardDialog() {
        mIvKeyboard.setImageResource(R.drawable.keyboard);
        mIvBack.setImageResource(R.drawable.back);
    }
}