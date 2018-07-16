package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.Nomenclature;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ExpressiveIconManager;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.verbiage_model.MiscellaneousIcons;
import com.dsource.idc.jellowintl.verbiage_model.VerbiageDatabaseHelper;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.MyBoards.BOARD_ID;

public class BoardHome extends AppCompatActivity {

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
            Log.d("ExpIcons","ID: "+expIconVerbiage.get(i).Title +" L: "+expIconVerbiage.get(i).L+" LL: "+expIconVerbiage.get(i).LL);
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
        adapter=new HomeAdapter(displayList,this);
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
                resetButtons();
            }
        });

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
        if(selectedIconVerbiage!=null)
        speakSpeech(selectedIconVerbiage.Speech_Label);
        else Log.d("VerbiageNotFound","True "+jellowIcon);
        resetButtons();
    }

    private void resetButtons() {
        expIconManager.resetSelection();
    }

    private void initViews(){
        mRecycler=findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));
        home=findViewById(R.id.ivhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Level!=0) {
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

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {
            ActivateView(home,true);
            ActivateView(back,true);
            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
            }
            //else Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
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
                } //else Toast.makeText(BoardHome.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
           // Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

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
        Log.d("SpeakSpeech","Speech Called");
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
                initViews();
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


}
