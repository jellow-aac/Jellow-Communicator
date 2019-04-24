package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.HomeAdapter;
import com.dsource.idc.jellowintl.makemyboard.models.Board;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ExpressiveIconManager;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.Nomenclature;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.MiscellaneousIcons;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.VerbiageDatabaseHelper;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.LanguageHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class HomeActivity extends SpeechEngineBaseActivity {

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
    private EditText editText;
    private ImageView speakerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNotchDevice())
            setContentView(R.layout.activity_levelx_layout_notch);
        else
            setContentView(R.layout.activity_levelx_layout);
        findViewById(R.id.save_button).setVisibility(View.GONE);

        mContext=this;
        verbiageDatabase=new VerbiageDatabaseHelper(this);

        database=new BoardDatabase(this);

        if(getSupportActionBar()!=null) {
            enableNavigationBack();
            setActivityTitle("HomeActivity");
            //TODO Check color to keep or remove.
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        }
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
        else if(time==0)verbiage=expIconVerbiage.get(expIconPos).L;else verbiage=expIconVerbiage.get(expIconPos).LL;

        if(!verbiage.equals("NA"))
        speak(verbiage);
    }

    private void updateList() {
        changeGridSize();
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        home.setImageDrawable(getResources().getDrawable(R.drawable.home));
    }

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage=verbiageDatabase.getVerbiageById(jellowIcon.IconDrawable);
        expIconManager.setAccordingVerbiage(selectedIconVerbiage);
        if(selectedIconVerbiage!=null)
        speak(selectedIconVerbiage.Speech_Label);
    }

    private void initViews(){
        editText = findViewById(R.id.et);
        editText.setVisibility(View.GONE);
        speakerButton = findViewById(R.id.ttsbutton);
        speakerButton.setVisibility(View.GONE);


        mRecycler=findViewById(R.id.recycler_view);
        changeGridSize();
        home=findViewById(R.id.ivhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(getString(R.string.home));
                ActivateView(home,true);
                ActivateView(back,false);
                LevelOneParent = -1;
                LevelTwoParent = -1;
                adapter.selectedPosition = -1;
                adapter.expIconPos = -1;
                displayList = modelManager.getLevelOneFromModel();
                Level = 0;
                updateList();
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
            }
        });
        back=findViewById(R.id.ivback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak("Back");onBackPressed();
            }
        });
        keyboardButton=findViewById(R.id.keyboard);
        keyboardButton.setAlpha(.5f);

    }


    private void changeGridSize()
    {
        adapter=new HomeAdapter(displayList,this,currentBoard.getGridSize());
        switch (currentBoard.getGridSize()){
            case 1:
                mRecycler.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            case 2:
                mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case 4: mRecycler.setLayoutManager(new GridLayoutManager(this,2));
                break;
            case 3:
            default :
                mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                break;
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
                home.setImageDrawable(getResources().getDrawable(R.drawable.home));
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
            else Toast.makeText(HomeActivity.this,"No sub category",Toast.LENGTH_SHORT).show();
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
                } else Toast.makeText(HomeActivity.this, "No sub category", Toast.LENGTH_SHORT).show();

            }
        }
        else if(Level==2)
        {
            Toast.makeText(HomeActivity.this,"No sub category",Toast.LENGTH_SHORT).show();
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
            ActivateView(back,false);
        }
        else if(Level==0)
        {
            ActivateView(back,false);
            home.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
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
            case android.R.id.home:startActivity(new Intent(this, MyBoardsActivity.class));finish(); break;
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
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
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

    private void highlightIcon(final ArrayList<Integer> iconPos) {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        int pos;
        if(iconPos.get(1)==-1)
        {//Level One
             pos = iconPos.get(0);
            if(Level!=0) {
                Level = 0;
                LevelOneParent = -1;
                LevelTwoParent = -1;
                displayList.clear();
                displayList = modelManager.getLevelOneFromModel();
                updateList();
            }

        }
        else if(iconPos.get(2)==-1)
        {       pos = iconPos.get(1);
                Level = 1;
                LevelOneParent = iconPos.get(0);
                LevelTwoParent = -1;
                displayList.clear();
                displayList = modelManager.getLevelTwoFromModel(iconPos.get(0));
                updateList();
        }
        else {
            // LevelThree
                Level = 2;
                displayList.clear();
                displayList = modelManager.getLevelThreeFromModel(iconPos.get(0), iconPos.get(1));
                updateList();
                pos = iconPos.get(2);
                LevelOneParent = iconPos.get(0);
                LevelTwoParent = iconPos.get(1);

        }
        ActivateView(back,Level>0);
        if(Level==0) home.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
        adapter.highlightedIcon = pos;
        if(currentBoard.getGridSize()>pos)
            adapter.notifyDataSetChanged();
        else {
            mRecycler.addOnScrollListener(getListener(pos));
            smoothScroller.setTargetPosition(pos);
            mRecycler.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    private RecyclerView.OnScrollListener getListener(final int index) {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    if(itemDisplayed(index)) mRecycler.removeOnScrollListener(scrollListener);
                    else {smoothScroller.setTargetPosition(index); mRecycler.getLayoutManager().startSmoothScroll(smoothScroller);}
                }
            }};

        return scrollListener;
    }
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((GridLayoutManager)mRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((GridLayoutManager)mRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if(lastVisiblePos==(index-1))
            return true;
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(HomeActivity.class.getSimpleName());
        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }
    }


}
