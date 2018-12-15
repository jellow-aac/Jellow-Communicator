package com.dsource.idc.jellowboard.makemyboard.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.AddEditIconAndCategory;
import com.dsource.idc.jellowboard.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowboard.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowboard.makemyboard.models.ListItem;
import com.dsource.idc.jellowboard.makemyboard.SimpleListAdapter;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.dsource.idc.jellowboard.verbiage_model.JellowVerbiageModel;
import com.rey.material.app.Dialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowboard.makemyboard.MyBoards.LIBRARY_REQUEST;

public class VerbiageEditor implements View.OnClickListener {

    private Context context;
    private boolean isVisible=false;
    private TextView saveButton;
    private RelativeLayout expandExpressive;
    private Dialog dialog;
    private EditText titleText;
    private TextView cancelSaveBoard;
    private ImageView editBoardIconButton;
    private ImageView iconImage;
    private ListView listView;
    private VerbiageEditorInterface dialogInterface;
    private boolean expressiveIconIsExpanded;
    private LinearLayout expList;
    private RelativeLayout hideShowLayout;
    private ArrayList<RelativeLayout> expListLayouts;
    private ArrayList<String> verbiageList;
    private ArrayList<String> defaultVerbiage;
    private JellowIcon thisIcon=null;
    private boolean flag=false;

    public VerbiageEditor(Context context, VerbiageEditorInterface dialogInterface, JellowIcon thisIcon) {
        this.dialogInterface=dialogInterface;
        this.context = context;
        this.thisIcon = thisIcon;
        initViews();
    }

    private void initViews()
    {
        View dialogContainerView;
        int resFile = R.layout.verbiage_edit_dialog;
        dialogContainerView = LayoutInflater.from(context).inflate(resFile, null);
        dialog = new Dialog(context,R.style.MyDialogBox);
        dialog.applyStyle(R.style.MyDialogBox);
        dialog.backgroundColor(context.getResources().getColor(R.color.transparent));
        dialog.setCancelable(false);
        dialog.setContentView(dialogContainerView);
        //Views related to the Dialogs
        titleText =dialogContainerView.findViewById(R.id.board_name);
        saveButton=dialogContainerView.findViewById(R.id.save_baord);
        cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        iconImage =dialogContainerView.findViewById(R.id.board_icon);
        iconImage.setBackground(context.getResources().getDrawable(R.drawable.icon_back_grey));
        listView=dialogContainerView.findViewById(R.id.camera_list);
        expandExpressive=dialogContainerView.findViewById(R.id.expressive_verbiage);
        expList = dialogContainerView.findViewById(R.id.exp_verbiage_list);
        hideShowLayout = dialogContainerView.findViewById(R.id.expressive_verbiage_show);

        if(thisIcon!=null)
        setIconImage();
        //Click Listeners

        saveButton.setOnClickListener(this);
        editBoardIconButton.setOnClickListener(this);
        cancelSaveBoard.setOnClickListener(this);
        hideShowLayout.setOnClickListener(this);
        verbiageRelatedViews();
    }

    private void setIconImage() {

        if(thisIcon.parent0==-1)//Is a custom Icon
        {
            SessionManager mSession = new SessionManager(context);
            File en_dir = context.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .into(iconImage);
            iconImage.setBackground(context.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else
        {
            SessionManager mSession = new SessionManager(context);
            File en_dir = context.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(iconImage);
        }

    }

    private void verbiageRelatedViews()
    {
        expandExpressive.setOnClickListener(this);
        defaultVerbiage = new ArrayList<>();
        verbiageList = new ArrayList<>();
        //Feeding default verbiage heads
        defaultVerbiage.add("I like ");
        defaultVerbiage.add("I want ");
        defaultVerbiage.add("I want more ");
        defaultVerbiage.add("I don't like ");
        defaultVerbiage.add("I don't want ");
        defaultVerbiage.add("I don't want more ");
        //Loading expressive icons
        @SuppressLint("Recycle")
        TypedArray iconImages = context.getResources().obtainTypedArray(R.array.expressive_icon_unpressed);

        expListLayouts = new ArrayList<>();
        //Populating the list item of verbiage
        for(int i = 0 ; i<6;i++)
        {
            @SuppressLint("InflateParams")
            View view  = LayoutInflater.from(context).inflate(R.layout.vebiage_list_item,null);
            view.setOnClickListener(this);
            expListLayouts.add((RelativeLayout)view);
            view.findViewById(R.id.add_remove).setOnClickListener(this);
            ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(iconImages.getDrawable(i));
            expList.addView(view);
        }

    }

    @SuppressLint("ResourceType")
    public void initAddEditDialog(String text) {
        //List on the dialog.
        listView.setVisibility(View.GONE);
        titleText.setText(text);
        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=context.getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Photos",mArray.getDrawable(0)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(context,list);
        listView.setAdapter(adapter);
        dialogInterface.initPhotoResultListener(new VerbiageEditorReverseInterface() {
            @Override
            public void onPhotoResult(Bitmap bitmap, int code, String fileName) {
                if(code!=LIBRARY_REQUEST)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(context)
                            .asBitmap()
                            .load(stream.toByteArray())
                            .apply(RequestOptions.
                                    circleCropTransform()).into(iconImage);
                }
                else
                {
                    SessionManager mSession = new SessionManager(context);
                    File en_dir = context.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                    String path = en_dir.getAbsolutePath() + "/drawables";
                    GlideApp.with(context)
                            .load(path+"/"+fileName+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(iconImage);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
                dialogInterface.onPhotoModeSelect(position);
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==editBoardIconButton)
        {
            if(isVisible)
                listView.setVisibility(View.GONE);
            else
                listView.setVisibility(View.VISIBLE);
            isVisible=!isVisible;
        }
        else if(v==saveButton)
            initSave();
        else if(v==cancelSaveBoard)
            dialog.cancel();
        else if(v==expandExpressive||v==hideShowLayout)
            toggleLayout();
        for(int i = 0 ;i<6;i++)
        {

            if(v==expListLayouts.get(i).findViewById(R.id.add_remove))
            {
                 if(expListLayouts.get(i).findViewById(R.id.verbiage_text).isEnabled()) {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(false);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(.6f);
                    ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
                }
                else
                {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(true);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(1.0f);
                    ((ImageView)expListLayouts.get(i).findViewById(R.id.add_remove)).
                            setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                }
            }
        }

    }

    private void initSave() {
        if(titleText.getText().toString().equals("")) {
            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!flag)
        {
            for(int i = 0;i<6;i++)
                verbiageList.add(i,defaultVerbiage.get(i)+titleText.getText().toString());
        }
        else for(int i = 0;i<6;i++) {
            //if view is enabled use it's verbiage
            if((expListLayouts.get(i).findViewById(R.id.verbiage_text)).isEnabled())
                verbiageList.add(((EditText)expListLayouts.
                    get(i).findViewById(R.id.verbiage_text))
                    .getText().toString());
            else // if view is disabled, write NA in place of that
                verbiageList.add("NA");
        }

        JellowVerbiageModel verbiageModel =new JellowVerbiageModel(
                titleText.getText().toString(),titleText.getText().toString(),
                verbiageList.get(0),
                verbiageList.get(0),
                verbiageList.get(1),
                verbiageList.get(1),
                verbiageList.get(2),
                verbiageList.get(2),
                verbiageList.get(3),
                verbiageList.get(3),
                verbiageList.get(4),
                verbiageList.get(4),
                verbiageList.get(5),
                verbiageList.get(5)
                );
        //Send data to the Controller
        dialogInterface.onSaveButtonClick(titleText.getText().toString(),
                ((BitmapDrawable) iconImage.getDrawable()).getBitmap(),verbiageModel);
        dialog.dismiss();
    }

    private void toggleLayout() {
        if(!flag) {
            for (int i = 0; i < 6; i++) {
                String name = defaultVerbiage.get(i) + titleText.getText();
                ((EditText) expListLayouts.get(i).findViewById(R.id.verbiage_text)).setText(name);
            }
            flag=!flag;
        }
        if(!expressiveIconIsExpanded)
        {   //SHOW VERBIAGE LIST
            iconImage.setVisibility(View.GONE);
            titleText.setVisibility(View.GONE);
            editBoardIconButton.setVisibility(View.GONE);
            expandExpressive.setVisibility(View.GONE);
            expList.setVisibility(View.VISIBLE);
            hideShowLayout.setVisibility(View.VISIBLE);
        }
        else
        {   //HIDE VERBIAGE LIST
            iconImage.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.VISIBLE);
            editBoardIconButton.setVisibility(View.VISIBLE);
            expList.setVisibility(View.GONE);
            expandExpressive.setVisibility(View.VISIBLE);
            hideShowLayout.setVisibility(View.GONE);
        }
        expressiveIconIsExpanded=!expressiveIconIsExpanded;
    }
}
