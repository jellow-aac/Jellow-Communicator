package com.dsource.idc.jellowboard.makemyboard;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.dsource.idc.jellowboard.Nomenclature;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowboard.makemyboard.utility.ExpressiveIconManager;
import com.dsource.idc.jellowboard.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowboard.makemyboard.adapters.HomeAdapter;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.JellowTTSService;
import com.dsource.idc.jellowboard.utility.LanguageHelper;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.dsource.idc.jellowboard.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowboard.verbiage_model.MiscellaneousIcons;
import com.dsource.idc.jellowboard.verbiage_model.VerbiageDatabaseHelper;

import java.util.ArrayList;

import static com.dsource.idc.jellowboard.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowboard.makemyboard.MyBoards.BOARD_ID;

public class Home extends AppCompatActivity {

    private static final int SEARCH = 1221;
    RecyclerView mRecycler;
    ImageView home,back,keyboardButton;
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
    private RecyclerView.OnScrollListener scrollListener;
    private ViewGroup.LayoutParams defaultRecyclerParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_board_layout);
        findViewById(R.id.save_button).setVisibility(View.GONE);

        mContext=this;
        verbiageDatabase=new VerbiageDatabaseHelper(this);

        database=new BoardDatabase(this);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Home"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        try{
            if(getIntent().getExtras()!=null)
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }
        defaultRecyclerParams = findViewById(R.id.recycler_view).getLayoutParams();

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
                adapter.expIconPos = expIconPos;
                adapter.notifyDataSetChanged();
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

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage=verbiageDatabase.getVerbiageById(jellowIcon.IconDrawable);
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
                speakSpeech(getString(R.string.home));
                if(Level!=0) {
                    ActivateView(home,false);
                    ActivateView(back,false);
                    LevelOneParent = -1;
                    LevelTwoParent = -1;
                    adapter.selectedPosition = -1;
                    adapter.expIconPos = -1;
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
                speakSpeech("Back");onBackPressed();
            }
        });
        keyboardButton=findViewById(R.id.keyboard);
        keyboardButton.setAlpha(.5f);

    }


    private void changeGridSize()
    {
        adapter=new HomeAdapter(displayList,this,currentBoard.getGridSize());
        //Parameters for centering the recycler view in the layout.
        RelativeLayout.LayoutParams centeredRecyclerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        centeredRecyclerParams.addRule(RelativeLayout.ABOVE,findViewById(R.id.relativeLayoutNavigation).getId());
        centeredRecyclerParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        centeredRecyclerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);

        int GridSize  = currentBoard.getGridSize();
        if(GridSize<4)
        {

            switch (GridSize)
            {
                case 1:mRecycler.setLayoutParams(centeredRecyclerParams);break;
                case 2:mRecycler.setLayoutParams(centeredRecyclerParams);break;
                case 3:mRecycler.setLayoutParams(defaultRecyclerParams);break;
            }
            mRecycler.setLayoutManager(new CustomGridLayoutManager(this,currentBoard.getGridSize(),3));
        }
        else
        {
            mRecycler.setLayoutParams(defaultRecyclerParams);
            mRecycler.setLayoutManager(new CustomGridLayoutManager(this,3,9));
        }
        adapter.setOnDoubleTapListner(new HomeAdapter.onDoubleTapListener() {
            @Override
            public void onItemDoubleTap(View view, int position) {
                adapter.notifyDataSetChanged();
                notifyItemClicked(position);
            }
        });
        adapter.setOnItemSelectListner(new HomeAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {
                adapter.expIconPos =-1;
                adapter.selectedPosition = position;
                adapter.notifyDataSetChanged();
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
                adapter.expIconPos =-1;
                adapter.selectedPosition = -1;
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
                selectedIconVerbiage=null;
            }
            else Toast.makeText(Home.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){
            //ActivateView(home,true);
            ActivateView(back,true);
            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    adapter.expIconPos =-1;
                    adapter.selectedPosition = -1;
                    displayList = temp;
                    Level++;
                    updateList();
                    LevelTwoParent=position;
                    selectedIconVerbiage=null;
                } else Toast.makeText(Home.this, "No sub category", Toast.LENGTH_SHORT).show();

            }
        }
        else if(Level==2)
        {
            Toast.makeText(Home.this,"No sub category",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        expIconManager.resetSelection();
        adapter.selectedPosition = -1;
        adapter.expIconPos = -1;
        selectedIconVerbiage = null;
        if(Level==2)
        {
            if(LevelOneParent!=-1) {
                displayList = modelManager.getLevelTwoFromModel(LevelOneParent);
                adapter.selectedPosition = LevelOneParent;
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
     * {@link com.dsource.idc.jellowboard.utility.JellowTTSService} Text-to-speech Engine.
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
                database.updateBoardIntoDatabase(currentBoard);
                changeGridSize();
            }
        });
        dialog.show();
        dialog.setCancelable(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:startActivity(new Intent(this,MyBoards.class));finish(); break;
            case R.id.grid_size:
                showGridDialog();break;
            case R.id.search:Toast.makeText(this,"Not implemented yet", Toast.LENGTH_SHORT).show();//searchInBoard();break;
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
                        Animation wiggle = AnimationUtils.loadAnimation(RepositionIcons.this,R.anim.jiggle_determinate);
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
    public void setSearchHighlight(final int pos) {
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


                Animation wiggle = AnimationUtils.loadAnimation(Home.this,R.anim.jiggle_determinate);
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
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }

        SessionManager session = new SessionManager(this);
        if(!session.getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    session.getToastMessage(), Toast.LENGTH_SHORT).show();
            session.setToastMessage("");
        }
    }


}
