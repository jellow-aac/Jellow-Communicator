package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.HomeAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.iActivity.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ExpressiveIconManager;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.Nomenclature;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class HomeActivity extends SpeechEngineBaseActivity {

    private static final int SEARCH = 1221;
    private RecyclerView rvRecycler;
    private ImageView ivHome, ivBack;
    private ModelManager modelManager;
    private ArrayList<JellowIcon> displayList;
    private int Level=0;
    private String boardId;
    private BoardDatabase database;
    private BoardModel currentBoard;
    private HomeAdapter adapter;
    private int LevelOneParent=-1;
    private int LevelTwoParent=-1;
    private Context mContext;
    private Icon selectedIconVerbiage;
    private TextDatabase verbiageDatabase;
    private ExpressiveIconManager expIconManager;
    private ArrayList<ExpressiveIcon> expIconVerbiage;
    private RecyclerView.OnScrollListener scrollListener;
    private EditText etSpeechTextInput;
    private ImageView ivSpeakerButton;
    private ImageView ivKeyboard;
    private boolean isKeyboardVisible=false;
    private SessionManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNotchDevice())
            setContentView(R.layout.activity_levelx_layout_notch);
        else
            setContentView(R.layout.activity_levelx_layout);

        manager = new SessionManager(this);
        mContext=this;

        database=new BoardDatabase(getAppDatabase());
        try{
            if(getIntent().getExtras()!=null)
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }

        currentBoard=database.getBoardById(boardId);
        verbiageDatabase=new TextDatabase(this,currentBoard.getLanguage(), getAppDatabase());
        modelManager =new ModelManager(getAppDatabase(),currentBoard.getIconModel());
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
        //ActivateView(ivHome,false);
        ActivateView(ivBack,false);


        if(getSupportActionBar()!=null) {
            enableNavigationBack();
            getSupportActionBar().setTitle(currentBoard.getBoardName());
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        }

        manageKeyboard();
    }

    private void loadExpressiveIconVerbiage() {
        expIconVerbiage=new ArrayList<>();
        for(int i=0;i<6;i++)
            expIconVerbiage.add(verbiageDatabase.getExpressiveIconsById(Nomenclature.getNameForExpressiveIcons(i,currentBoard.getLanguage())));

    }

    private void speakVerbiage(int expIconPos, int time) {
        String verbiage="";
        if(selectedIconVerbiage!=null)
        switch (expIconPos)
        {
            case 0:if(time==0)verbiage=selectedIconVerbiage.getL();else verbiage=selectedIconVerbiage.getLL();break;
            case 1:if(time==0)verbiage=selectedIconVerbiage.getY();else verbiage=selectedIconVerbiage.getYY();break;
            case 2:if(time==0)verbiage=selectedIconVerbiage.getM();else verbiage=selectedIconVerbiage.getMM();break;
            case 3:if(time==0)verbiage=selectedIconVerbiage.getD();else verbiage=selectedIconVerbiage.getDD();break;
            case 4:if(time==0)verbiage=selectedIconVerbiage.getN();else verbiage=selectedIconVerbiage.getNN();break;
            case 5:if(time==0)verbiage=selectedIconVerbiage.getS();else verbiage=selectedIconVerbiage.getSS();break;

        }
        else if(time==0)verbiage=expIconVerbiage.get(expIconPos).getL();else verbiage=expIconVerbiage.get(expIconPos).getLL();

        if(!verbiage.equals("NA"))
        speak(verbiage);
    }

    private void manageKeyboard(){
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(getResources().getString(R.string.keyboard));
                isKeyboardVisible = !isKeyboardVisible;
                enableKeyboardView();
            }
        });
    }

    private void enableKeyboardView(){
        if(isKeyboardVisible) {
            ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_pressed));
            rvRecycler.setVisibility(View.GONE);
            etSpeechTextInput.setVisibility(View.VISIBLE);
            etSpeechTextInput.requestFocus();
            ivSpeakerButton.setVisibility(View.VISIBLE);
            expIconManager.resetExpressiveButtons(true);
            ActivateView(ivBack,true);
        }
        else{
            ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
            rvRecycler.setVisibility(View.VISIBLE);
            etSpeechTextInput.setVisibility(View.GONE);
            ivSpeakerButton.setVisibility(View.GONE);
            expIconManager.resetExpressiveButtons(false);
        }
        invalidateOptionsMenu();
    }

    private void updateList() {
        changeGridSize();
        rvRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
    }

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage=verbiageDatabase.getVerbiageById(jellowIcon.getVerbiageId());
        expIconManager.setAccordingVerbiage(selectedIconVerbiage);
        if(selectedIconVerbiage!=null)
        speak(selectedIconVerbiage.getSpeech_Label());
    }

    private void initViews(){
        etSpeechTextInput = findViewById(R.id.et);
        etSpeechTextInput.setVisibility(View.GONE);
        ivSpeakerButton = findViewById(R.id.ttsbutton);
        ivSpeakerButton.setVisibility(View.GONE);
        ivKeyboard = findViewById(R.id.keyboard);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSpeechTextInput.getText().toString().equals("")) {
                    if(currentBoard.getLanguage().equals(SessionManager.HI_IN))
                        Toast.makeText(mContext,"कृपया क्षेत्र भरें।",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(mContext,"Please input something in field",Toast.LENGTH_LONG).show();
                    etSpeechTextInput.requestFocus();
                }else speak(etSpeechTextInput.getText().toString());
            }
        });


        rvRecycler =findViewById(R.id.recycler_view);
        changeGridSize();
        ivHome =findViewById(R.id.ivhome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isKeyboardVisible = false;
                enableKeyboardView();
                speak(getString(R.string.home));
                ActivateView(ivHome,true);
                ActivateView(ivBack,false);
                LevelOneParent = -1;
                LevelTwoParent = -1;
                adapter.selectedPosition = -1;
                adapter.expIconPos = -1;
                displayList = modelManager.getLevelOneFromModel();
                Level = 0;
                updateList();
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
            }
        });
        ivBack =findViewById(R.id.ivback);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(getResources().getString(R.string.back));
                if(isKeyboardVisible){
                    isKeyboardVisible = false;
                    enableKeyboardView();
                    if(Level==0) ActivateView(ivBack,false);
                }else onBackPressed();
            }
        });
    }


    private void changeGridSize()
    {
        adapter=new HomeAdapter(displayList,this,currentBoard.getGridSize(),currentBoard.getLanguage());
        switch (currentBoard.getGridSize()){
            case 0:
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            case 1:
            case 3:
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            default :
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                break;
        }

        adapter.setOnItemSelectListener(new onRecyclerItemClick() {
            @Override
            public void onClick(int pos) {
                adapter.expIconPos =-1;
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
                adapter.selectedPosition = pos;
                adapter.notifyDataSetChanged();
                prepareSpeech(displayList.get(pos));
            }

            @Override
            public void onDoubleTap(int pos) {
                adapter.notifyDataSetChanged();
                notifyItemClicked(pos);
            }
        });

        rvRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {

            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                ActivateView(ivHome,true);
                ActivateView(ivBack,true);
                adapter.expIconPos =-1;
                adapter.selectedPosition = -1;
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
                selectedIconVerbiage=null;
            }
            else {
                speak(displayList.get(position).getIconTitle());
            }
        }
        else if(Level==1){
            //ActivateView(ivHome,true);
            ActivateView(ivBack,true);
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
                } else{
                    speak(displayList.get(position).getIconTitle());
                }

            }
        }
        else if(Level==2)
        {
            speak(displayList.get(position).getIconTitle());
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
            ActivateView(ivBack,false);
        }
        else if(Level==0)
        {
            ActivateView(ivBack,false);
            ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
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
        super.attachBaseContext(BoardLanguageHelper.getInstance().changeLanguage(newBase));
    }

    private void showGridDialog() {
        new CustomDialog(this,currentBoard.getLanguage(), new GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                database.updateBoardIntoDatabase(currentBoard);
                changeGridSize();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:startActivity(new Intent(this, BoardListActivity.class));finishAffinity();
            if(manager!=null) manager.setCurrentBoardLanguage(""); break;
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
        if(!isKeyboardVisible)
            getMenuInflater().inflate(R.menu.board_home_menu, menu);
        return true;
    }
    private void searchInBoard() {
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID,currentBoard.getBoardId());
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
                selectedIconVerbiage = null;

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
        ActivateView(ivBack,Level>0);
        if(Level==0) ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
        adapter.highlightedIcon = pos;
        if(currentBoard.getGridSize()>pos)
            adapter.notifyDataSetChanged();
        else {
            rvRecycler.addOnScrollListener(getListener(pos));
            smoothScroller.setTargetPosition(pos);
            rvRecycler.getLayoutManager().startSmoothScroll(smoothScroller);
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
                    if(itemDisplayed(index)) rvRecycler.removeOnScrollListener(scrollListener);
                    else {smoothScroller.setTargetPosition(index); rvRecycler.getLayoutManager().startSmoothScroll(smoothScroller);}
                }
            }};

        return scrollListener;
    }
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((GridLayoutManager) rvRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((GridLayoutManager) rvRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if(lastVisiblePos==(index-1))
            return true;
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!getSpeechEngineLanguage().equals(currentBoard.getLanguage())) {
            if (getSession().getCurrentBoardLanguage() == null || getSession().getCurrentBoardLanguage().equals(""))
                setSpeechEngineLanguage(SessionManager.ENG_IN);
            else
                initiateSpeechEngineWithLanguage(getSession().getCurrentBoardLanguage());
        }
        setVisibleAct(HomeActivity.class.getSimpleName());
        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }
    }


}
