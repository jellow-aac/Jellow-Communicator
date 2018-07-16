package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.verbiage_model.VerbiageDatabaseHelper;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.MyBoards.BOARD_ID;

public class BoardHome extends AppCompatActivity  implements View.OnClickListener{

    RecyclerView mRecycler;
    ImageView like, yes,more,dontLike,dontWant,less,home,back,keyboardButton, speakButton;
    ClickHolder likeHolder, yesHolder,moreHolder,dontLikeHolder,dontWantHolder,lessHolder,homeHolder,backHolder,keyboardButtonHolder, speakButtonHolder;
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
    private View selectedIconBackground;
    private ArrayList<View> mRecyclerItemsViewList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        mContext=this;
        verbiageDatabase=new VerbiageDatabaseHelper(this,new DataBaseHelper(this).getWritableDatabase());

        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
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
        adapter.setOnDoubleTapListner(new HomeAdapter.onDoubleTapListener() {
            @Override
            public void onItemDoubleTap(View view, int position) {
                notifyItemClicked(position);
                resetButtons();
            }
        });
        adapter.setOnItemSelectListner(new HomeAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelected(View view, int position) {

                selectedIconBackground=view;
                setSelectedIconBackground(-1);
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
        likeHolder.reset();
        yesHolder.reset();
        moreHolder.reset();
        dontWantHolder.reset();
        dontLikeHolder.reset();
        lessHolder.reset();
    }

    private void initViews(){


        mRecycler=findViewById(R.id.recycler_view);
        mRecyclerItemsViewList=new ArrayList<>();
        mRecycler.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));
        like=findViewById(R.id.ivlike);
        like.setOnClickListener(this);
        yes =findViewById(R.id.ivyes);
        yes.setOnClickListener(this);
        more=findViewById(R.id.ivadd);
        more.setOnClickListener(this);
        dontLike=findViewById(R.id.ivdislike);
        dontLike.setOnClickListener(this);
        dontWant=findViewById(R.id.ivno);
        dontWant.setOnClickListener(this);
        less=findViewById(R.id.ivminus);
        less.setOnClickListener(this);

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
        back.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        keyboardButton=findViewById(R.id.keyboard);
        keyboardButton.setOnClickListener(this);
        speakButton =findViewById(R.id.ttsbutton);
        speakButton.setOnClickListener(this);
        edittext=findViewById(R.id.et);
        edittext.setVisibility(View.GONE);
        speakButton.setVisibility(View.GONE);

        likeHolder=new ClickHolder(like,getResources().getDrawable(R.drawable.like_pressed),getResources().getDrawable(R.drawable.like));
        yesHolder =new ClickHolder(yes,getResources().getDrawable(R.drawable.yes_pressed),getResources().getDrawable(R.drawable.yes));
        moreHolder=new ClickHolder(more,getResources().getDrawable(R.drawable.more_pressed),getResources().getDrawable(R.drawable.more));
        dontLikeHolder=new ClickHolder(dontLike,getResources().getDrawable(R.drawable.dontlike_pressed),getResources().getDrawable(R.drawable.dontlike));
        dontWantHolder=new ClickHolder(dontWant,getResources().getDrawable(R.drawable.no_pressed),getResources().getDrawable(R.drawable.no));
        lessHolder=new ClickHolder(less,getResources().getDrawable(R.drawable.less_pressed),getResources().getDrawable(R.drawable.less));

        mRecycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
               /* mRecyclerItemsViewList.set(mRecycler.getChildLayoutPosition(view), view);
                if(mRecyclerItemsViewList.contains(view) && selectedIconBackground!=null &&
                        mRecycler.getChildAt(mRecycler.getChildLayoutPosition(view)) == selectedIconBackground)
                    setSelectedIconBackground(-1);*/
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                /*setSelectedIconBackground(-2);
                mRecyclerItemsViewList.set(mRecycler.getChildLayoutPosition(view), null);*/
            }
        });


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
            dialog.setText("Are you sure you yes to exit ?");
            dialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
                @Override
                public void onPositiveClickListener() {
                    finish();
                }
            });
            dialog.show();
        }

    }

    private void setSelectedIconBackground(int i)
    {
        /*//When view is found remove the scrollListener and highlight the background
        GradientDrawable gd = (GradientDrawable) selectedIconBackground.findViewById(R.id.borderView).getBackground();
        switch (i)
        {

            case -1:gd.setColor(ContextCompat.getColor(this,R.color.colorSelect)); break;
            case 0: gd.setColor(ContextCompat.getColor(this,R.color.colorLike)); break;
            case 1: gd.setColor(ContextCompat.getColor(this,R.color.colorDontLike)); break;
            case 2: gd.setColor(ContextCompat.getColor(this,R.color.colorYes)); break;
            case 3: gd.setColor(ContextCompat.getColor(this,R.color.colorNo)); break;
            case 4: gd.setColor(ContextCompat.getColor(this,R.color.colorMore)); break;
            case 5: gd.setColor(ContextCompat.getColor(this,R.color.colorLess)); break;

        }*/
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

    @Override
    public void onClick(View v) {
        //resetButtons();
        if(v==like){
            if(selectedIconVerbiage!=null) {
                if(likeHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.L);
                else speakSpeech(selectedIconVerbiage.LL);
            } else {

            }

            likeHolder.Clicked();
        }
        else if(v== yes){
            if(selectedIconVerbiage!=null) {
                if(yesHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.Y);
                else speakSpeech(selectedIconVerbiage.YY);
            } else {

            }
            yesHolder.Clicked();
        }
        else if(v==more){
            if(selectedIconVerbiage!=null) {
                if(moreHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.M);
                else speakSpeech(selectedIconVerbiage.MM);
            } else {

            }
            moreHolder.Clicked();

        }
        else if(v==dontLike){
            if(selectedIconVerbiage!=null) {
                if(dontLikeHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.D);
                else speakSpeech(selectedIconVerbiage.DD);
            } else {

            }

            dontLikeHolder.Clicked();
        }
        else if(v==dontWant){
            if(selectedIconVerbiage!=null) {
                if(dontWantHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.N);
                else speakSpeech(selectedIconVerbiage.NN);
            } else {

            }
            dontWantHolder.Clicked();
        }
        else if(v==less){
            if(selectedIconVerbiage!=null) {
                if(lessHolder.clickedTime==0)
                    speakSpeech(selectedIconVerbiage.S);
                else speakSpeech(selectedIconVerbiage.SS);
            } else {

            }
            lessHolder.Clicked();
        }
    }

    private class ClickHolder
    {

        ImageView button;
        int clickedTime=0;
        Drawable selectedBackground,notSelectedBackground;

        public ClickHolder(ImageView button,Drawable selected,Drawable notSelected) {
            this.button = button;
            this.selectedBackground=selected;
            this.notSelectedBackground=notSelected;
        }

        public void Clicked()
        {
            setSelectionBackGround(true);
            if(clickedTime==1)
            clickedTime=0;
            else clickedTime++;
        }

        private void setSelectionBackGround(boolean selected)
        {
            if(selected)
                button.setImageDrawable(selectedBackground);
            else button.setImageDrawable(notSelectedBackground);

        }

        public void reset()
        {
            setSelectionBackGround(false);
            clickedTime=0;
        }
    }

}
