package com.dsource.idc.jellowintl.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.activities.adapters.MainActivityAdapter;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.package_updater_module.UpdatePackageCheckUtils;
import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;
import com.dsource.idc.jellowintl.utility.LevelUiUtils;
import com.dsource.idc.jellowintl.utility.UserEventCollector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class MainActivity extends LevelBaseActivity{
    private final int REQ_HOME = 0;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = GlobalConstants.SHORT_SPEECH, mFlgYes = GlobalConstants.SHORT_SPEECH,
            mFlgMore = GlobalConstants.SHORT_SPEECH, mFlgDntLike = GlobalConstants.SHORT_SPEECH,
            mFlgNo = GlobalConstants.SHORT_SPEECH, mFlgLess = GlobalConstants.SHORT_SPEECH;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = GlobalConstants.NO_EXPR;
    /*Image views which are visible on the layout such as six expressive buttons, below navigation
      buttons and speak button when keyboard is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack;

    /*Recycler view which will populate category icons.*/
    public RecyclerView mRecyclerView;
    /*This variable indicates index of category icon selected in level one*/
    private int mLevelOneItemPos = GlobalConstants.NOT_SELECTED;
    /*This variable indicates index of category icon in adapter in level 1. This variable is
     different than mLevelOneItemPos. */
    private int mSelectedItemAdapterPos = GlobalConstants.NOT_SELECTED;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount = -1;
    /* This flag is set to true, when user press the category icon and reset when user press the home
     button. When user presses expressive button and mShouldReadFullSpeech = true, it means that user
     is already selected a category icon and user intend to speak full sentence verbiage for a
     selected icon.*/
    private boolean mShouldReadFullSpeech = false;
    /* This flag indicates keyboard is open or not, true indicates is not open.*/
    private boolean mSearched = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mExprBtnTxt, mNavigationBtnTxt, mIconCode;
    private String txtHome;

    /*Firebase event Collector class instance.*/
    private UserEventCollector mUec;

    private Icon[] level1IconObjects;

    private ImageView[] expressiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        setupActionBarTitle(View.GONE, getString(R.string.action_bar_title));
        setNavigationUiConditionally();
        adjustTopMarginForNavigationUi();
        mUec = new UserEventCollector();
        loadArraysFromResources();
        // Set the capacity of mRecyclerItemsViewList list to total number of category icons to be
        // populated on the screen.
        mRecyclerItemsViewList = new ArrayList<>(level1IconObjects.length);
        while (mRecyclerItemsViewList.size() < level1IconObjects.length)
            mRecyclerItemsViewList.add(null);
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        txtHome = getString(R.string.action_bar_title);
        initializeLayoutViews();
        initializeViewListeners();
        //This method is invoked when the activity is launched from the SearchActivity
        try {
            String s = getIntent().getExtras().getString(getString(R.string.from_search));
            if (s != null)
                if (s.equals(getString(R.string.search_tag))) {
                    mSearched = true;
                    highlightSearchedItem();
                }
        }
        catch (NullPointerException e)
        {
            //Not from Search Activity
        }
        new UpdatePackageCheckUtils().checkLanguagePackageUpdateAvailable(this);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    /**
     * This function is responsible for the Highlighting of the searched item from the
     * search activity
     * @Author Ayaz Alam
     * **/
    RecyclerView.OnScrollListener scrollListener;
    private void highlightSearchedItem() {
        //Referring to the Intent that invoked the activity
        final int iconIndex = getIntent().getExtras().getInt(getString(R.string.search_parent_0));
        int gridCode= getSession().getGridSize();
        int gridSize;
        if(gridCode == GlobalConstants.NINE_ICONS_PER_SCREEN)
            gridSize=8;
        else gridSize=2;
        //Scroll to the position if the icon is not present in first grid
        if(iconIndex>gridSize)
        {
            scrollListener =null;
            scrollListener = getListener(iconIndex);
            mRecyclerView.addOnScrollListener(scrollListener);
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,iconIndex);

        }
        else
            {
            setSearchHighlight(iconIndex);
            }

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
                {
                    //Wait until the scrolling is complete
                }
                else if(newState==RecyclerView.SCROLL_STATE_IDLE) //Try highlighting if scrolling is done
                    setSearchHighlight(index);
            }
        };
        return scrollListener;
    }

    /**
     * This function is responsible for highlighting the view
     * @param pos
     *
     *
     * */
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
                    //Try re-scrolling if the view is not present in the current recycler view child
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,pos );
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
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
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authenticateUserIfNot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(MainActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();

        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(this, getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }

        if(getIntent().hasExtra(getString(R.string.goto_home)))
            gotoHome(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring("LevelOneActivity");
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available int this version.</p>
    **/
    private void initializeLayoutViews() {
        mIvLike = findViewById(R.id.ivlike);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvMore = findViewById(R.id.ivadd);
        mIvLess = findViewById(R.id.ivminus);
        mIvYes = findViewById(R.id.ivyes);
        mIvNo = findViewById(R.id.ivno);
        mIvHome = findViewById(R.id.ivhome);
        mIvBack = findViewById(R.id.ivback);
        //on this screen initially back button is disabled.
        mIvBack.setAlpha(GlobalConstants.DISABLE_ALPHA);
        mIvBack.setEnabled(false);
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

        expressiveBtn = new ImageView[]{ mIvLike, mIvYes, mIvMore, mIvDontLike, mIvNo, mIvLess };
    }

    private void loadRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        // Initiate 3 columns in Recycler View.
        switch (getSession().getGridSize()){
            case GlobalConstants.ONE_ICON_PER_SCREEN:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            case GlobalConstants.TWO_ICONS_PER_SCREEN:
            case GlobalConstants.FOUR_ICONS_PER_SCREEN:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case GlobalConstants.THREE_ICONS_PER_SCREEN:
            case GlobalConstants.NINE_ICONS_PER_SCREEN:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
        }
        mRecyclerView.setAdapter(new MainActivityAdapter(this, level1IconObjects));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.requestFocus();
    }

    /**
     * <p>This function will initialize the action listeners to the views which are populated on
     * on this activity.</p>
     **/
    private void initializeViewListeners() {
        initRecyclerViewListeners();
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
                    LevelUiUtils.setBorderToCategoryIcon(MainActivity.this, view, true,
                            mActionBtnClickCount, mFlgImage);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                LevelUiUtils.setBorderToCategoryIcon(MainActivity.this, view, false,
                        mActionBtnClickCount, mFlgImage);
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), null);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "home" button.
     * {@link MainActivity} navigation home button when clicked it clears, every state of view as
     * like app is launched as fresh.</p>
     **/
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUec.createSendFbEventFromTappedView(26, "", "");
                gotoHome(false);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "keyboard" button.
     * {@link MainActivity} navigation keyboard button either enable or disable the keyboard layout.
     * This button enable the back button when keyboard is open.</p>
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase event
                singleEvent("Navigation","Keyboard");
                new DialogKeyboardUtterance().show(MainActivity.this);
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
     * a) press expressive like button once
     * b) press expressive like button twice
     * c) press category icon first then press expressive like button once
     * d) press category icon first then press expressive like button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except like button
     * b) set pressed icon to like button
     * c) produce speech using output for like
     * d) set border to category icon of color associated with like button</p>
     **/
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
                    // if value of mFlgLike is 1 then should speak "really like".
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
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null){
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    }
                    // if value of mFlgLike is 1 then speak associated really like expression
                    // verbiage for selected category icon.
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getLL());
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(14,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"LL","");
                    // if value of mFlgLike is 0 then Speak associated like expression
                    // verbiage to selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getL());
                        mFlgLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(13,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"L0","");
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "don't like" button.
     * Expressive don't like button is works in four ways:
     * a) press expressive don't like button once
     * b) press expressive don't like button twice
     * c) press category icon first then press expressive don't like button once
     * d) press category icon first then press expressive don't like button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except don't like button
     * b) set pressed icon to don't like button
     * c) produce speech using output for don't like
     * d) set border to category icon of color associated with don't like button</p>
     **/
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
                // if value of mFlgDntLike is 1 then should speak "really don't like".
                    if (mFlgDntLike == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[7]);
                        mFlgDntLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(7, "", "");
                    // if value of mFlgDntLike is 0, then should speak "don't like".
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
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                            mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                            mActionBtnClickCount, mFlgImage);
                    // if value of mFlgDntLike is 1 then speak associated really don't like expression
                    // verbiage for selected category icon.
                    if (mFlgDntLike == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getDD());
                        mFlgDntLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(20,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"DD","");
                    // if value of mFlgDntLike is 0 then Speak associated don't like expression
                    // verbiage to selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getD());
                        mFlgDntLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(19,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"D0","");
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "yes" button.
     * Expressive yes button is works in four ways:
     * a) press expressive yes button once
     * b) press expressive yes button twice
     * c) press category icon first then press expressive yes button once
     * d) press category icon first then press expressive yes button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except yes button
     * b) set pressed icon to yes button
     * c) produce speech using output for yes
     * d) set border to category icon of color associated with yes button</p>
     **/
    private void initYesBtnListener() {
        mIvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the yes button all expressive button speech flag (except yes)
                // are set to reset.
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                mFlgImage = GlobalConstants.YES;
                // Set yes button icon to pressed using mFlgImage.
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
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgYes is 1 then speak associated really yes expression
                    // verbiage for selected category icon.
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getYY());
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(16,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"YY","");
                    // if value of mFlgYes is 0 then speak associated yes expression
                    // verbiage for selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getY());
                        mFlgYes = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(15,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"Y0","");
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "no" button.
     * Expressive no button is works in four ways:
     * a) press expressive no button once
     * b) press expressive no button twice
     * c) press category icon first then press expressive no button once
     * d) press category icon first then press expressive no button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except no button
     * b) set pressed icon to no button
     * c) produce speech using output for no
     * d) set border to category icon of color associated with no button</p>
     **/
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
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        // if value of mFlgNo is 1, then should speak "really no".
                        speak(mExprBtnTxt[9]);
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(9, "", "");
                    } else {
                        // if value of mFlgNo is 0, then should speak "no".
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
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgNo is 1 then speak associated really no expression
                    // verbiage for selected category icon.
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getNN());
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(22,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"NN","");
                    // if value of mFlgNo is 0 then Speak associated no expression
                    // verbiage to selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getN());
                        mFlgNo = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(21,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"N0","");
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "more" button.
     * Expressive more button is works in four ways:
     * a) press expressive more button once
     * b) press expressive more button twice
     * c) press category icon first then press expressive more button once
     * d) press category icon first then press expressive more button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except more button
     * b) reset all expressive buttons
     * c) produce speech using output for more
     * d) set border to category icon of color associated with more button</p>
     **/
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
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgMore is 1, then should speak "really more" expression
                    // verbiage associated to selected category icon.
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getMM());
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(18,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"MM","");
                    // if value of mFlgMore is 0, then should speak "more" expression
                    // verbiage associated to selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getM());
                        mFlgMore = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(17,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"M0","");
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "less" button.
     * Expressive less button is works in four ways:
     * a) press expressive less button once
     * b) press expressive less button twice
     * c) press category icon first then press expressive less button once
     * d) press category icon first then press expressive less button twice
     * This function execute task as follows:
     * a) reset the all expressive button speech flag except less button
     * b) reset all expressive buttons
     * c) produce speech using output for less
     * d) set border to category icon of color associated with less button</p>
     **/
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
                        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this,
                                mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true,
                                mActionBtnClickCount, mFlgImage);
                    // if value of mFlgLess is 1 then speak associated really less expression
                    // verbiage for selected category icon.
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        speak(level1IconObjects[mLevelOneItemPos].getSS());
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(24,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"SS","");
                    // if value of mFlgLess is 0 then Speak associated less expression
                    // verbiage to selected category icon.
                    } else {
                        speak(level1IconObjects[mLevelOneItemPos].getS());
                        mFlgLess = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(23,
                level1IconObjects[mLevelOneItemPos].getEvent_Tag()+"_"+
                            mIconCode[mLevelOneItemPos]+"S0","");
                    }
                }
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
     *             d) Set the border to selected category icon
     *             e) If same category icon clicked twice that category will open up.
     *             f) Checks if, level two icons data set is available or not.</p>
     * */
    public void tappedCategoryItemEvent(final View view, int position) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
        mIvHome.setImageResource(R.drawable.home);
        // reset all expressive button.
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        // reset every populated category icon before setting the border to selected icon.
        LevelUiUtils.resetRecyclerAllItems(MainActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mActionBtnClickCount = 0;
        // set border to selected category icon
        LevelUiUtils.setBorderToCategoryIcon(MainActivity.this, view, true,
                mActionBtnClickCount, mFlgImage);
        // set true to speak verbiage associated with category icon
        mShouldReadFullSpeech = true;
        String title = level1IconObjects[position].getDisplay_Label().replace("…","");
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            // below condition is true when user tap same category icon twice.
            // i.e. user intends to open a sub-category of selected category icon.
            if (mLevelOneItemPos == position){
                // create event bundle for firebase
                Bundle bundle = new Bundle();
                bundle.putString("Icon", "Opened " + level1IconObjects[position].
                        getDisplay_Label().replace("…",""));
                bundleEvent("Grid", bundle);

                // send current position in recycler view of selected category icon and bread
                // crumb path as extra intent data to LevelTwoActivity.
                title = getString(R.string.home)+"/ "+title;
                Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
                intent.putExtra(getString(R.string.level_one_intent_pos_tag), position);
                intent.putExtra(getString(R.string.intent_menu_path_tag), title + "/");
                startActivityForResult(intent, REQ_HOME);
            }else {
                if (!mSearched)
                    speak(level1IconObjects[position].getSpeech_Label());
                else
                    speakWithDelay(level1IconObjects[position].getSpeech_Label());
                mSearched = false;
                // create event bundle for firebase
                mUec.createSendFbEventFromTappedView(12,
                        level1IconObjects[position].getDisplay_Label().replace("…",""), "");
            }
        }else{
            showAccessibleDialog(position, title, view);
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            // create event bundle for firebase
            mUec.createSendFbEventFromTappedView(12, level1IconObjects[position].
                    getDisplay_Label().replace("…",""), "");
        }
        mLevelOneItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
    }

    private void showAccessibleDialog(final int position, final String title, final View disabledView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        final Button enterCategory = mView.findViewById(R.id.enterCategory);
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

        ivBack.setEnabled(false);
        ivBack.setAlpha(GlobalConstants.DISABLE_ALPHA);
        ivBack.setOnClickListener(null);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUec.sendEventIfAny("");
                mIvKeyboard.performClick();
                dialog.dismiss();
            }
        });

        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create event bundle for firebase
                Bundle bundle = new Bundle();
                bundle.putString("Icon", "Opened " + level1IconObjects[position].getDisplay_Label()
                        .replace("…",""));
                bundleEvent("Grid", bundle);

                Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
                intent.putExtra("mLevelOneItemPos", position);
                intent.putExtra("selectedMenuItemPath", title + "/");
                startActivityForResult(intent, REQ_HOME);
                dialog.dismiss();
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
                //clear all selection
                clearSelectionAfterAccessibilityDialogClose();
                //dismiss dialog
                dialog.dismiss();
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
        resetRecyclerMenuItemsAndFlags();
        //Send pending grid event
        mUec.sendEventIfAny("");
        LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
        mShouldReadFullSpeech = false;
        mFlgImage = GlobalConstants.NO_EXPR;
    }

    /**
     * <p>When home button is pressed it is needed to make app state as it is started fresh.
     * This function will reset every category icons, expressive button tapped.
     * @param isUserRedirected is set when user presses home from {@link LevelTwoActivity},
     * {@link LevelThreeActivity}, {@link SequenceActivity} and resets when user presses home
     * {@link MainActivity}.</p>
     **/
    private void gotoHome(boolean isUserRedirected) {
        setupActionBarTitle(txtHome);
        // reset all expressive button flags.
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
        // reset expressive buttons
        mIvLike.setImageResource(R.drawable.like);
        mIvDontLike.setImageResource(R.drawable.dontlike);
        mIvYes.setImageResource(R.drawable.yes);
        mIvNo.setImageResource(R.drawable.no);
        mIvMore.setImageResource(R.drawable.more);
        mIvLess.setImageResource(R.drawable.less);
        // reset category items
        resetRecyclerMenuItemsAndFlags();
        // clear verbiage speak (mShouldReadFullSpeech), border color flag (mFlgImage)
        mShouldReadFullSpeech = false;
        mFlgImage = GlobalConstants.NO_EXPR;

        mIvHome.setImageResource(R.drawable.home_pressed);
        //Firebase event
        singleEvent("Navigation","Home");
        if(!isUserRedirected) {
            speak(mNavigationBtnTxt[0]);
        }
    }

    /**
     * <p>This function will:
     *     a) Read verbiage lines into  model.
     *     b) Read speech text from arrays for category icons.
     *     c) Read speech text from arrays for expressive buttons.
     *     d) Read speech text from arrays for navigation buttons.</p>
     * */
    private void loadArraysFromResources() {
        //Retrieve icon names from json file for Level 1
        mIconCode = IconFactory.getL1IconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );

        level1IconObjects = TextFactory.getIconObjects(mIconCode);
        loadRecyclerView();

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

    /**
     * <p>This function will reset :
     *     a) Reset expressive button pressed if any. To set home button to pressed
     *        6 value is sent to resetExpressiveButtons(6).
     *     b) Reset category icons pressed if any.
     *     c) Setting mLevelOneItemPos = GlobalConstants.NOT_SELECTED means, no category icon is selected.</p>
     **/
    private void resetRecyclerMenuItemsAndFlags() {
        mIvHome.setImageResource(R.drawable.home_pressed);
        mLevelOneItemPos = GlobalConstants.NOT_SELECTED;
        LevelUiUtils.resetRecyclerAllItems(MainActivity.this, mRecyclerView,
                mActionBtnClickCount, mFlgImage);
        mActionBtnClickCount = 0;
    }


    private void authenticateUserIfNot() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            if (isConnectedToNetwork((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                performManualLogin(mAuth);
            }else{
                showSignInSnackBar();
            }
        }
    }

    private void performManualLogin(FirebaseAuth mAuth) {
        mAuth
            .signInAnonymously()
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference ref = db.getReference(BuildConfig.DB_TYPE+"/users/" +
                                getSession().getUserId());
                        ref.child("versionCode").setValue(BuildConfig.VERSION_CODE);
                    }
                }
            });
    }

    private void showSignInSnackBar() {
        Snackbar.make(findViewById(android.R.id.content),R.string.checkConnectivity,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        authenticateUserIfNot();
                    }
                }).show();
    }

    public void hideCustomKeyboardDialog() {
        mIvKeyboard.setImageResource(R.drawable.keyboard);
        mIvBack.setImageResource(R.drawable.back);
    }
}