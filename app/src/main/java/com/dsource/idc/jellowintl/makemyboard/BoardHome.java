package com.dsource.idc.jellowintl.makemyboard;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.Nomenclature;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.verbiage_model.VerbiageDatabaseHelper;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.makemyboard.MyBoards.BOARD_ID;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class BoardHome extends AppCompatActivity {

    RecyclerView mRecycler;
    ImageView like,want,more,dontLike,dontWant,less,home,back,keyboardButton, speakButton;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        mContext=this;
        verbiageDatabase=new VerbiageDatabaseHelper(this,new DataBaseHelper(this).getWritableDatabase());

        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
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


    }

    private void updateList() {
        adapter=new HomeAdapter(displayList,this);
        adapter.setOnItemClickListner(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                notifyItemClicked(position);
            }
        });
        adapter.setOnItemSelectListner(new HomeAdapter.OnItemSelectListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setBackground(getResources().getDrawable(R.drawable.shape_droptarget));
                prepareSpeech(displayList.get(position));
            }
        });
        mRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage=verbiageDatabase.getVerbiageById(Nomenclature.getIconName(jellowIcon,mContext));
        if(selectedIconVerbiage!=null)
        speakSpeech(selectedIconVerbiage.Speech_Label);
        else Log.d("VirbiageNotFound","True "+jellowIcon);
        resetButtons();
    }

    private void resetButtons() {

    }

    private void initViews(){
        mRecycler=findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));
        like=findViewById(R.id.ivlike);
        want=findViewById(R.id.ivyes);
        more=findViewById(R.id.ivadd);
        dontLike=findViewById(R.id.ivdislike);
        dontWant=findViewById(R.id.ivno);
        less=findViewById(R.id.ivminus);
        home=findViewById(R.id.ivhome);
        back=findViewById(R.id.ivback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        keyboardButton=findViewById(R.id.keyboard);
        speakButton =findViewById(R.id.ttsbutton);
        edittext=findViewById(R.id.et);
        edittext.setVisibility(View.GONE);
        speakButton.setVisibility(View.GONE);

    }

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {
            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
            }
            else Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList();
                    LevelTwoParent=position;
                } else Toast.makeText(BoardHome.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
            Toast.makeText(BoardHome.this,"No sub category",Toast.LENGTH_SHORT).show();
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
        }
        else if(Level==0)
        {
            CustomDialog dialog=new CustomDialog(this);
            dialog.setText("Are you sure you want to exit ?");
            dialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
                @Override
                public void onPositiveClickListener() {
                    finish();
                }
            });
            dialog.show();
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

    private class ClickHolder
    {
        View button;
        int clickedTime=0;

        public ClickHolder(View button) {
            this.button = button;
        }

        public void Clicked()
        {
            if(clickedTime==2)
            clickedTime=0;
            else clickedTime++;
        }
        public void reset()
        {
            clickedTime=0;
        }

    }

}
