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
import com.dsource.idc.jellowintl.makemyboard.adapters.SimpleListAdapter;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.makemyboard.models.ListItem;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.rey.material.app.Dialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.LIBRARY_REQUEST;

public class VerbiageEditor extends android.app.Dialog implements View.OnClickListener {

    //Static variables to set the modes
    public static final int ADD_BOARD_MODE = 111;
    public static final int ADD_EDIT_ICON_MODE = 222;
    private Context context;
    private boolean isVisible = false;
    private TextView saveButton;
    private Dialog dialog;
    private EditText titleText;
    private TextView cancelSaveBoard;
    private ImageView editBoardIconButton;
    private ImageView iconImage;
    private ListView listView;
    private VerbiageEditorInterface dialogInterface;
    private JellowIcon thisIcon = null;
    private int mode;
    private String name;

    public VerbiageEditor(Context context, int mode, VerbiageEditorInterface dialogInterface,String languageCode) {
        super(context);
        this.dialogInterface = dialogInterface;
        this.context = context;
        this.mode = mode;
        String langCode = languageCode;
        initViews();
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
        list.add(new ListItem(context.getResources().getString(R.string.photos),mArray.getDrawable(0)));
        list.add(new ListItem(context.getResources().getString(R.string.library),mArray.getDrawable(1)));
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
                                .placeholder(R.drawable.ic_board_person)
                                .apply(RequestOptions
                                        .circleCropTransform()).into(iconImage);
                    else if(mode==ADD_BOARD_MODE)
                        Glide.with(context).load(stream.toByteArray())
                            .apply(new RequestOptions().
                                    transform(new RoundedCorners(50)).
                                    placeholder(R.drawable.ic_board_person).
                                    error(R.drawable.ic_board_person).skipMemoryCache(true).
                                    diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(iconImage);
                }
                else
                {
                    GlideApp.with(context).load(getIconPath(context, fileName+EXTENSION))
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

    public void showDialog(){
        if(dialog!=null)
        dialog.show();
    }

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



        if(id!=null)
        {
            File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(context)
                    .load(path+"/"+id+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
        }
        else if(thisIcon.isCustomIcon())//Is a custom Icon
        {
            File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.getIconDrawable()+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
        }
        else
        {
            GlideApp.with(context).load(getIconPath(context, thisIcon.getIconDrawable()+EXTENSION))
                    .skipMemoryCache(true)
                    .into(iconImage);
        }

    }


    @Override
    public void onClick(View v) {
        if (v == editBoardIconButton) {
            if (isVisible)
                listView.setVisibility(View.GONE);
            else
                listView.setVisibility(View.VISIBLE);
            isVisible = !isVisible;
        } else if (v == saveButton)
            initSave();
        else if (v == cancelSaveBoard) {
            if (dialog != null) dialog.cancel();

        }


    }

    private void initSave() {

        if(titleText.getText().toString().equals("")) {
            Toast.makeText(context, context.getResources().getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if(mode==ADD_EDIT_ICON_MODE)
            dialogInterface.onPositiveButtonClick(titleText.getText().toString(),
                ((BitmapDrawable) iconImage.getDrawable()).getBitmap(),null);
        else if(mode==ADD_BOARD_MODE)
            dialogInterface.onPositiveButtonClick(titleText.getText().toString(),
                    ((BitmapDrawable) iconImage.getDrawable()).getBitmap(),null);
        if(dialog!=null)
            dialog.dismiss();
    }


}
