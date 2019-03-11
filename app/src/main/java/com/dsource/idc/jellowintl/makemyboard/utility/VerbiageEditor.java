package com.dsource.idc.jellowintl.makemyboard.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.SimpleListAdapter;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.makemyboard.models.ListItem;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.rey.material.app.Dialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.LIBRARY_REQUEST;

public class VerbiageEditor implements View.OnClickListener {

    //Static variables to set the modes
    public static final int ADD_BOARD_MODE =111;
    public static final int ADD_EDIT_ICON_MODE=222;
    public static final int VERBIAGE_MODE = 333;

    private Context context;
    private boolean isVisible=false;
    private TextView saveButton;
    private Dialog dialog;
    private EditText titleText;
    private TextView cancelSaveBoard;
    private ImageView editBoardIconButton;
    private ImageView iconImage;
    private ListView listView;
    private VerbiageEditorInterface dialogInterface;
    private LinearLayout expList;
    private ArrayList<RelativeLayout> expListLayouts;
    private ArrayList<String> verbiageList;
    private ArrayList<String> defaultVerbiage;
    private JellowIcon thisIcon=null;
    private int mode;
    private boolean verbiageDialog=false;
    private String name;
    private JellowVerbiageModel presentVerbiage=null;

    public VerbiageEditor(Context context,int mode,VerbiageEditorInterface dialogInterface) {
        this.dialogInterface=dialogInterface;
        this.context = context;
        this.mode = mode;
        if(mode==VERBIAGE_MODE)
            initVerbiageViews();
        else
        initViews();
    }

    private void initVerbiageViews() {
        verbiageDialog=true;
        View dialogContainerView;
        int resFile= R.layout.verbiage_edit_dialog;
        dialogContainerView = LayoutInflater.from(context).inflate(resFile, null);
        dialog = new Dialog(context,R.style.MyDialogBox);
        dialog.applyStyle(R.style.MyDialogBox);
        dialog.backgroundColor(context.getResources().getColor(R.color.transparent));
        dialog.setCancelable(false);
        dialog.setContentView(dialogContainerView);
        if(dialog.getWindow()!=null)
        dialog.getWindow().setLayout(1200,RelativeLayout.LayoutParams.MATCH_PARENT);
        //Views related to the Dialogs
        saveButton=dialogContainerView.findViewById(R.id.save_baord);
        cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        expList = dialogContainerView.findViewById(R.id.exp_verbiage_list);
        //Click Listeners
        saveButton.setOnClickListener(this);
        cancelSaveBoard.setOnClickListener(this);
        verbiageRelatedViews();
    }

    public VerbiageEditor presentVerbiage(JellowVerbiageModel verbiageModel){
        this.presentVerbiage =verbiageModel;
        if(verbiageModel!=null) {
            if (verbiageModel.L.equals("NA"))
            {
                expListLayouts.get(0).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(0).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(0).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(0).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(0).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {
                ((EditText)expListLayouts.get(0).findViewById(R.id.verbiage_text)).setText(verbiageModel.L);
                ((EditText)expListLayouts.get(0).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.LL);
            }
            if (verbiageModel.Y.equals("NA"))
            {
                expListLayouts.get(1).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(1).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(1).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(1).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(1).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {
                ((EditText)expListLayouts.get(1).findViewById(R.id.verbiage_text)).setText(verbiageModel.Y);
                ((EditText)expListLayouts.get(1).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.YY);}
            if (verbiageModel.M.equals("NA"))
            {
                expListLayouts.get(2).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(2).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(2).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(2).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(2).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {
                ((EditText)expListLayouts.get(2).findViewById(R.id.verbiage_text)).setText(verbiageModel.M);
                ((EditText)expListLayouts.get(2).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.MM);
            }
            if (verbiageModel.D.equals("NA"))
            {
                expListLayouts.get(3).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(3).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(3).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(3).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(3).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {
                ((EditText)expListLayouts.get(3).findViewById(R.id.verbiage_text)).setText(verbiageModel.D);
                ((EditText)expListLayouts.get(3).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.DD);
            }

            if (verbiageModel.N.equals("NA"))
            {
                expListLayouts.get(4).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(4).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(4).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(4).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(4).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {

                ((EditText)expListLayouts.get(4).findViewById(R.id.verbiage_text)).setText(verbiageModel.N);
                ((EditText)expListLayouts.get(4).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.NN);
            }
            if (verbiageModel.S.equals("NA"))
            {
                expListLayouts.get(5).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(5).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(5).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(5).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(5).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));

            }
            else
            {
                ((EditText)expListLayouts.get(5).findViewById(R.id.verbiage_text)).setText(verbiageModel.S);
                ((EditText)expListLayouts.get(5).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.SS);
            }
        }
        return this;
    }

    public VerbiageEditor initVerbiageDialog(String name){
        this.name =name;
        if(presentVerbiage==null)/* This skips below codes for editing a icon's verbiage */ {
            int j = 0;
            for (int i = 0; i < 6; i++) {
                ((EditText) expListLayouts.get(i).findViewById(R.id.verbiage_text)).setText(String.format("%s%s", defaultVerbiage.get(j++), name));
                ((EditText) expListLayouts.get(i).findViewById(R.id.verbiage_really_text)).setText(String.format("%s%s", defaultVerbiage.get(j++), name));
            }
        }
        return this;
    }

    public void setAlreadyPresentIcon(JellowIcon Icon){this.thisIcon = Icon;setIconImage(null);}

    @SuppressLint("ResourceType")
    public void initAddEditDialog(String edittextHint) {
        //List on the dialog.
        listView.setVisibility(View.GONE);
        titleText.setHint(edittextHint);
        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=context.getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Photos",mArray.getDrawable(0)));
        list.add(new ListItem("Library ",mArray.getDrawable(1)));
        SimpleListAdapter adapter=new SimpleListAdapter(context,list);
        listView.setAdapter(adapter);
        dialogInterface.initPhotoResultListener(new VerbiageEditorReverseInterface() {
            @Override
            public void onPhotoResult(Bitmap bitmap, int code, String fileName) {
                if(code!=LIBRARY_REQUEST)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    if(mode==ADD_EDIT_ICON_MODE)
                        Glide.with(context)
                            .asBitmap()
                            .load(stream.toByteArray())
                            .apply(RequestOptions.
                                    circleCropTransform()).into(iconImage);
                    else if(mode==ADD_BOARD_MODE)
                        Glide.with(context).load(stream.toByteArray())
                            .apply(new RequestOptions().
                                    transform(new RoundedCorners(50)).
                                    error(R.drawable.ic_board_person).skipMemoryCache(true).
                                    diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(iconImage);
                }
                else
                {
                    File en_dir = context.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
                    String path = en_dir.getAbsolutePath() + "/drawables";
                    GlideApp.with(context)
                            .load(path+"/"+fileName+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(iconImage);
                }
                if(mode== ADD_EDIT_ICON_MODE)
                    iconImage.setBackground(context.getResources().getDrawable(R.drawable.icon_back_grey));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
                dialogInterface.onPhotoModeSelect(position);
            }
        });

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    }

    public void setPositiveButtonText(String name){saveButton.setText(name);}

    public void setBoardImage(String boardID){setIconImage(boardID);}

    public Dialog show(){dialog.show();return dialog;}

    public void setTitleText(String name){if(titleText!=null)titleText.setText(name);}

    private void initViews()
    {
        View dialogContainerView;
        int resFile =R.layout.edit_board_dialog;
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
        listView=dialogContainerView.findViewById(R.id.camera_list);
        expList = dialogContainerView.findViewById(R.id.exp_verbiage_list);
        //Setting the image icon
        if(thisIcon!=null)
        setIconImage(null);

        //Click Listeners
        saveButton.setOnClickListener(this);
        editBoardIconButton.setOnClickListener(this);
        cancelSaveBoard.setOnClickListener(this);
    }

    /**
     * Sets image for the Dialog
     * @param id URI
     */
    private void setIconImage(String id) {

        File en_dir = context.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);

        if(id!=null)
        {
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(context)
                    .load(path+"/"+id+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
        }
        else if(thisIcon.parent0==-1)//Is a custom Icon
        {
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
        }
        else
        {
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
        }

    }

    private void verbiageRelatedViews()
    {

        defaultVerbiage = new ArrayList<>();
        verbiageList = new ArrayList<>();
        //Feeding default verbiage heads
        defaultVerbiage.add("I like ");
        defaultVerbiage.add("I really like ");
        defaultVerbiage.add("I want ");
        defaultVerbiage.add("I really want ");
        defaultVerbiage.add("I want more ");
        defaultVerbiage.add("I really want more ");
        defaultVerbiage.add("I don't like ");
        defaultVerbiage.add("I really don't like ");
        defaultVerbiage.add("I don't want ");
        defaultVerbiage.add("I really don't want ");
        defaultVerbiage.add("I don't want more ");
        defaultVerbiage.add("I really don't want more ");
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

            //For add/remove of the verbiage
        if(verbiageDialog)
        for(int i = 0 ;i<6;i++)
        {

            if(v==expListLayouts.get(i).findViewById(R.id.add_remove))
            {
                 if(expListLayouts.get(i).findViewById(R.id.verbiage_text).isEnabled()) {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(false);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(.6f);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(false);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                    ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
                }
                else
                {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(true);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(1.0f);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(true);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(1.0f);
                    ((ImageView)expListLayouts.get(i).findViewById(R.id.add_remove)).
                            setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                }
            }
        }

    }

    private void initSave() {
        if(!verbiageDialog)
        if(titleText.getText().toString().equals("")) {
            Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mode==ADD_EDIT_ICON_MODE)
            dialogInterface.onPositiveButtonClick(titleText.getText().toString(),
                ((BitmapDrawable) iconImage.getDrawable()).getBitmap(),null);
        else if(mode ==VERBIAGE_MODE)
            dialogInterface.onPositiveButtonClick(null,
                    null,saveVerbiage());
        else if(mode==ADD_BOARD_MODE)
            dialogInterface.onPositiveButtonClick(titleText.getText().toString(),
                    ((BitmapDrawable) iconImage.getDrawable()).getBitmap(),null);
        dialog.dismiss();
    }

    private JellowVerbiageModel saveVerbiage() {

    for(int i = 0;i<6;i++) {
            //if view is enabled use it's verbiage
            if((expListLayouts.get(i).findViewById(R.id.verbiage_text)).isEnabled())
            {
                verbiageList.add(((EditText)expListLayouts.
                        get(i).findViewById(R.id.verbiage_text))
                        .getText().toString());
                verbiageList.add(((EditText)expListLayouts.
                        get(i).findViewById(R.id.verbiage_really_text))
                        .getText().toString());
            }
            else // if view is disabled, write NA in place of that
            {
                verbiageList.add("NA");
                verbiageList.add("NA");
            }
        }

        return new JellowVerbiageModel(
                name,name,
                verbiageList.get(0),
                verbiageList.get(1),
                verbiageList.get(2),
                verbiageList.get(3),
                verbiageList.get(4),
                verbiageList.get(5),
                verbiageList.get(6),
                verbiageList.get(7),
                verbiageList.get(8),
                verbiageList.get(9),
                verbiageList.get(10),
                verbiageList.get(11)
        );
    }

}
