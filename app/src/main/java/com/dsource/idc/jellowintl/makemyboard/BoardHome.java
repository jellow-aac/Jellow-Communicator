package com.dsource.idc.jellowintl.makemyboard;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.Nomenclature;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ExpressiveIconManager;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.verbiage_model.MiscellaneousIcons;
import com.dsource.idc.jellowintl.verbiage_model.VerbiageDatabaseHelper;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.makemyboard.MyBoards.BOARD_ID;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;

public class BoardHome extends AppCompatActivity {

    private static final int SEARCH = 1221;
    RecyclerView mRecycler;
    ImageView home,back,keyboardButton, speakButton;
    EditText edittext;
    ModelManager modelManager;
    ArrayList<JellowIcon> displayList;
    int Level=0;
    private String boardId;
    private BoardDatabase database;
    private Board currentBoard;
    private HomeAdapter adapter;
    private int LevelOneParent=-1;
    private int LevelTwoParent=-1;
    private Context mContext;
    private JellowVerbiageModel selectedIconVerbiage;
    private VerbiageDatabaseHelper verbiageDatabase;
    private ExpressiveIconManager expIconManager;
    private ArrayList<MiscellaneousIcons> expIconVerbiage;
    private int expIconPosition;
    private View selectedView;
    private RecyclerView.OnScrollListener scrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        mContext=this;
        verbiageDatabase=new VerbiageDatabaseHelper(this,new DataBaseHelper(this).getWritableDatabase());

        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Home"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }

        currentBoard=database.getBoardById(boardId);
        getSupportActionBar().setTitle("Home");
        modelManager =new ModelManager(this,currentBoard.getBoardIconModel());
        displayList= modelManager.getLevelOneFromModel();
        initViews();
        updateList();
        expIconManager=new ExpressiveIconManager(this,findViewById(R.id.parent));
        expIconManager.setClickListener(new ExpressiveIconManager.expIconClickListener() {
            @Override
            public void expressiveIconClicked(int expIconPos, int time) {
                Log.d("ExpIcons","Callback");
                speakVerbiage(expIconPos,time);
            }
        });
        loadExpressiveIconVerbiage();
        //ActivateView(home,false);
        ActivateView(back,false);
    }

    private void loadExpressiveIconVerbiage() {
        expIconVerbiage=new ArrayList<>();
        for(int i=0;i<6;i++) {
            expIconVerbiage.add(verbiageDatabase.getMiscellaneousIconsById(Nomenclature.getNameForExpressiveIcons(this, i)));
           }
    }

    private void speakVerbiage(int expIconPos, int time) {
        String verbiage="";
        expIconPosition=expIconPos;
        if(selectedIconVerbiage!=null)
        switch (expIconPos)
        {
            case 0:if(time==0)verbiage=selectedIconVerbiage.L;else verbiage=selectedIconVerbiage.LL;break;
            case 1:if(time==0)verbiage=selectedIconVerbiage.Y;else verbiage=selectedIconVerbiage.YY;break;
            case 2:if(time==0)verbiage=selectedIconVerbiage.M;else verbiage=selectedIconVerbiage.MM;break;
            case 3:if(time==0)verbiage=selectedIconVerbiage.D;else verbiage=selectedIconVerbiage.DD;break;
            case 4:if(time==0)verbiage=selectedIconVerbiage.N;else verbiage=selectedIconVerbiage.NN;break;
            case 5:if(time==0)verbiage=selectedIconVerbiage.S;else verbiage=selectedIconVerbiage.SS;break;

        }
        else
        {
            if(time==0)verbiage=expIconVerbiage.get(expIconPos).L;else verbiage=expIconVerbiage.get(expIconPos).LL;
        }
        if(!verbiage.equals("NA"))
        speakSpeech(verbiage);
    }

    private void updateList() {
        changeGridSize();

        mRecycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {


            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void highlightSelection(int colorCode) {
       /* GradientDrawable gd = (GradientDrawable) selectedView.findViewById(R.id.borderView).getBackground();

                switch (colorCode) {
                    case -2:gd.setColor(ContextCompat.getColor(this,R.color.transparent)); break;
                    case -1:gd.setColor(ContextCompat.getColor(this,R.color.colorSelect)); break;
                    case 0: gd.setColor(ContextCompat.getColor(this,R.color.colorLike)); break;
                    case 1: gd.setColor(ContextCompat.getColor(this,R.color.colorDontLike)); break;
                    case 2: gd.setColor(ContextCompat.getColor(this,R.color.colorYes)); break;
                    case 3: gd.setColor(ContextCompat.getColor(this,R.color.colorNo)); break;
                    case 4: gd.setColor(ContextCompat.getColor(this,R.color.colorMore)); break;
                    case 5: gd.setColor(ContextCompat.getColor(this,R.color.colorLess)); break;
                }

*/
    }


    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage=verbiageDatabase.getVerbiageById(Nomenclature.getIconName(jellowIcon,mContext));
        expIconManager.setAccordingVerbiage(selectedIconVerbiage);
        if(selectedIconVerbiage!=null)
        speakSpeech(selectedIconVerbiage.Speech_Label);
    }

    private void initViews(){
        mRecycler=findViewById(R.id.recycler_view);
        changeGridSize();
        home=findViewById(R.id.ivhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Level!=0) {
                    ActivateView(home,false);
                    ActivateView(back,false);
                    LevelOneParent = -1;
                    LevelTwoParent = -1;
                    displayList = modelManager.getLevelOneFromModel();
                    Level = 0;
                    updateList();
                }
            }
        });
        back=findViewById(R.id.ivback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        keyboardButton=findViewById(R.id.keyboard);
        keyboardButton.setAlpha(.5f);
        speakButton =findViewById(R.id.ttsbutton);
        edittext=findViewById(R.id.et);
        edittext.setVisibility(View.GONE);
        speakButton.setVisibility(View.GONE);

    }

    private void changeGridSize()
    {
        adapter=new HomeAdapter(displayList,this,currentBoard.getGridSize());
        if(currentBoard.getGridSize()<4)
        mRecycler.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));
        else
        {
            switch (currentBoard.getGridSize())
            {
                case 4:mRecycler.setLayoutManager(new GridLayoutManager(this,2));break;//2X2
                case 5:break;//2X3
                case 6:mRecycler.setLayoutManager(new GridLayoutManager(this,3));break;//3X3
            }
        }
        adapter.setOnDoubleTapListner(new HomeAdapter.onDoubleTapListener() {
            @Override
            public void onItemDoubleTap(View view, int position) {
                selectedIconVerbiage=null;
                notifyItemClicked(position);
            }
        });
        adapter.setOnItemSelectListner(new HomeAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {
                selectedView=view;
                highlightSelection(-1);
                prepareSpeech(displayList.get(position));

            }
        });

        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {

            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                ActivateView(home,true);
                ActivateView(back,true);
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
            }
            else Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){
            //ActivateView(home,true);
            ActivateView(back,true);
            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList();
                    LevelTwoParent=position;
                } else Toast.makeText(BoardHome.this, "No sub category", Toast.LENGTH_SHORT).show();

            }
        }
        else if(Level==2)
        {
            Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        expIconManager.resetSelection();
        selectedIconVerbiage = null;
        if(Level==2)
        {
            if(LevelOneParent!=-1) {
                displayList = modelManager.getLevelTwoFromModel(LevelOneParent);
                LevelTwoParent=-1;
                updateList();
                Level--;
            }

        }
        else if(Level==1)
        {
            displayList= modelManager.getLevelOneFromModel();
            LevelOneParent=-1;
            updateList();
            Level--;
           // ActivateView(home,false);
            ActivateView(back,false);
        }
        else if(Level==0)
        {
            //ActivateView(home,false);
            ActivateView(back,false);
            super.onBackPressed();
        }

    }

    private void ActivateView(ImageView view, boolean activate) {
        if(activate)
        {
            view.setAlpha(1f);
            view.setClickable(true);
        }
        else
        {
            view.setAlpha(.5f);
            view.setClickable(false);
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }
    /**
     * <p>This function will send speech output request to
     * {@link com.dsource.idc.jellowintl.utility.JellowTTSService} Text-to-speech Engine.
     * The string in {@param speechText} is speech output request string.</p>
     * */

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText.toLowerCase());
        mContext.sendBroadcast(intent);
    }

    private void showGridDialog() {
        CustomDialog dialog=new CustomDialog(this,CustomDialog.GRID_SIZE);
        dialog.setGridSelectListener(new CustomDialog.GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                database.updateBoardIntoDatabase(new DataBaseHelper(BoardHome.this).getWritableDatabase(),currentBoard);
                changeGridSize();
            }
        });
        dialog.show();
        dialog.setCancelable(true);

        //TODO Add some codes to resize the icons
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish(); break;
            case R.id.grid_size:
                showGridDialog();break;
            case R.id.search:searchInBoard();break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.board_home_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    private void searchInBoard() {
        Intent searchIntent = new Intent(this,BoardSearch.class);
        searchIntent.putExtra(BoardSearch.SEARCH_MODE,BoardSearch.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID,currentBoard.getBoardID());
        startActivityForResult(searchIntent,SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==SEARCH)
        {
            if(resultCode==RESULT_OK)
            {
                JellowIcon icon  = (JellowIcon)data.getSerializableExtra(getString(R.string.search_result));
                ArrayList<Integer> iconPos = modelManager.getIconPositionInModel(icon);
                if(!(iconPos.size()<1))
                    highlightIcon(iconPos);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    ViewTreeObserver.OnGlobalLayoutListener tempListener;
    private void highlightIcon(final ArrayList<Integer> iconPos) {
        if(iconPos.get(1)==-1)
        {//Level One
            Level = 0;
            displayList.clear();
            displayList =modelManager.getLevelOneFromModel();
            updateList();
            // mRecyclerView.addOnScrollListener(getListener(iconPos.get(0)));
            mRecycler.getLayoutManager().smoothScrollToPosition(mRecycler,null,iconPos.get(0));
           /*  tempListener= new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    View v = mRecyclerView.getChildAt(iconPos.get(0));
                    if (v != null) {
                        Animation wiggle = AnimationUtils.loadAnimation(EditBoard.this,R.anim.jiggle_determinate);
                        v.startAnimation(wiggle);
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(tempListener);
                    }
                    else
                    {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(tempListener);

                    }
                }
            };
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(tempListener);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
*/


        }
        else if(iconPos.get(2)==-1)
        {
            //Level Two
            Level = 1;
            displayList.clear();
            displayList =modelManager.getLevelTwoFromModel(iconPos.get(0));
            updateList();
            mRecycler.getLayoutManager().smoothScrollToPosition(mRecycler,null,iconPos.get(1));

        }
        else {
            // LevelThree
            Level = 2;
            displayList.clear();
            displayList =modelManager.getLevelThreeFromModel(iconPos.get(0),iconPos.get(1));
            updateList();
            mRecycler.getLayoutManager().smoothScrollToPosition(mRecycler,null,iconPos.get(2));

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
        mRecycler.getAdapter().notifyDataSetChanged();
        populationDoneListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View searchedView = mRecycler.getChildAt(pos);
                if(searchedView==null) {
                    mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                    mRecycler.getLayoutManager().smoothScrollToPosition(mRecycler,null,pos );
                    return;
                }


                Animation wiggle = AnimationUtils.loadAnimation(BoardHome.this,R.anim.jiggle_determinate);
                searchedView.startAnimation(wiggle);
                /*
                GradientDrawable gd = (GradientDrawable) searchedView.findViewById(R.id.borderView).getBackground();
                gd.setColor(ContextCompat.getColor(getApplicationContext(), R.color.search_highlight));*/
                mRecycler.removeOnScrollListener(scrollListener);
                mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
            }
        };
        //Adding the scrollListener to the mRecycler to listen onPopulated callBack
        mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            throw new Error("unableToResume");
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        // Start measuring user app screen timer .
        startMeasuring();
        SessionManager session = new SessionManager(this);
        if(!session.getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    session.getToastMessage(), Toast.LENGTH_SHORT).show();
            session.setToastMessage("");
        }
    }


}
