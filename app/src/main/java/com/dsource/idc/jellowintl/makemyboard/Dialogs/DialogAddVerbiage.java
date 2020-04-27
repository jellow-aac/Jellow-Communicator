package com.dsource.idc.jellowintl.makemyboard.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class DialogAddVerbiage extends BaseActivity implements View.OnClickListener {


    public static final String JELLOW_ID = "icon";

    public static final String FETCH_FLAG = "fetch_flag";

    public static final String IS_PRIMARY_FLAG = "is_primary_flag";

    public static OnSuccessListener<String> callback;

    //This class saves the verbiage
    private String id;
    private Context context;
    private LinearLayout expList;
    private ArrayList<RelativeLayout> expListLayouts;
    private ArrayList<String> verbiageList;
    private ArrayList<String> defaultVerbiage;
    private JellowIcon thisIcon = null;
    private Icon presentVerbiage = null;
    private TextDatabase database;
    private boolean iconUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_verbiage);
        context = this;

        if (getIntent().getStringExtra(BOARD_ID) != null) {
            id = getIntent().getStringExtra(BOARD_ID);
            Bundle d = getIntent().getExtras();
            if (d != null)
                thisIcon = (JellowIcon) d.getSerializable(JELLOW_ID);
            initViews();
            setUpFields();
        }

    }

    private void setUpFields() {
        //Setup database
        BoardDatabase boardDatabase = new BoardDatabase(getAppDatabase());
        BoardModel thisBoard = boardDatabase.getBoardById(id);
        this.database = new TextDatabase(this, thisBoard.getLanguage(), getAppDatabase());

        String fetchFlag = getIntent().getStringExtra(FETCH_FLAG);
        String primaryFlag = getIntent().getStringExtra(IS_PRIMARY_FLAG);

        if (fetchFlag != null && primaryFlag != null)
            //Condition one: New icon or category to be saved
            if (fetchFlag.equals("NULL") && primaryFlag.equals("NULL")) {
                presentVerbiage = null;
                updateUI(null);
                iconUpdate = false;
            }
            //Condition two: Custom icon is being edited
            else if (!fetchFlag.equals("NULL") && primaryFlag.equals("NULL")) {
                presentVerbiage = database.getVerbiageById(fetchFlag);
                updateUI(presentVerbiage);
                iconUpdate = true;
            }
            //Condition three: Primary icon is being edited
            else {
                presentVerbiage = database.getVerbiageById(fetchFlag);
                updateUI(presentVerbiage);
                iconUpdate = false;
            }
    }

    private void updateUI(Icon currentVerbiage) {
        if (currentVerbiage == null) {
            initVerbiageDialog();
            enableAllViews(false);
        } else {
            presentVerbiage(currentVerbiage);
        }

    }

    private void initViews() {
        Button save = findViewById(R.id.save_button);
        Button btnReset = findViewById(R.id.cancel_button);
        expList = findViewById(R.id.exp_verbiage_list);
        verbiageRelatedViews();
        //To close on touch outside
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.top_container)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do nothing
                    }
                });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(presentVerbiage);
            }
        });
        save.setText(getResources().getString(R.string.txtSave));
        btnReset.setText(getResources().getString(R.string.reset));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
                finish();
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveToDatabase() {
        //If icon is new and to be inserted to database
        if (!iconUpdate) {
            database.addNewVerbiage(thisIcon.getVerbiageId(), saveVerbiage());
        } else //if the icon is already a custom icon and need to updated
            database.updateVerbiage(thisIcon.getVerbiageId(), saveVerbiage());
        callback.onSuccess("Success");
    }


    private void verbiageRelatedViews() {

        defaultVerbiage = new ArrayList<>();
        verbiageList = new ArrayList<>();
        //Feeding default verbiage heads
        defaultVerbiage.add(getResources().getString(R.string.i_like));
        defaultVerbiage.add(getResources().getString(R.string.really_like));
        defaultVerbiage.add(getResources().getString(R.string.i_want));
        defaultVerbiage.add(getResources().getString(R.string.really_want));
        defaultVerbiage.add(getResources().getString(R.string.want_more));
        defaultVerbiage.add(getResources().getString(R.string.really_want_more));
        defaultVerbiage.add(getResources().getString(R.string.i_dont_like));
        defaultVerbiage.add(getResources().getString(R.string.really_dont_like));
        defaultVerbiage.add(getResources().getString(R.string.dont_want));
        defaultVerbiage.add(getResources().getString(R.string.really_dont_want));
        defaultVerbiage.add(getResources().getString(R.string.dont_want_more));
        defaultVerbiage.add(getResources().getString(R.string.really_dont_want_more));
        //Loading expressive icons
        @SuppressLint("Recycle")
        TypedArray iconImages = context.getResources().obtainTypedArray(R.array.expressive_icon_unpressed);

        expListLayouts = new ArrayList<>();
        //Populating the list item of verbiage
        for (int i = 0; i < 6; i++) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.verbiage_list_item, null);
            expListLayouts.add((RelativeLayout) view);
            view.findViewById(R.id.add_remove).setOnClickListener(this);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(iconImages.getDrawable(i));
            expList.addView(view);
        }

    }

    @SuppressLint("SetTextI18n")
    public void initVerbiageDialog() {
        String name = thisIcon.getIconTitle();
        /* This skips below codes for editing a icon'getS() verbiage */
        int j = 0;

        for (int i = 0; i < 6; i++) {
            ((EditText) expListLayouts.get(i).findViewById(R.id.verbiage_text)).setText(defaultVerbiage.get(j++) + " " + name);
            ((EditText) expListLayouts.get(i).findViewById(R.id.verbiage_really_text)).setText(defaultVerbiage.get(j++) + " " + name);
        }

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < 6; i++) {

            if (v == expListLayouts.get(i).findViewById(R.id.add_remove)) {
                if (expListLayouts.get(i).findViewById(R.id.verbiage_text).isEnabled()) {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(false);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(.6f);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(false);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                    ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
                } else {
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(true);
                    expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(1.0f);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(true);
                    expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(1.0f);
                    ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).
                            setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                }
            }
        }
    }


    private Icon saveVerbiage() {

        for (int i = 0; i < 6; i++) {
            //if view is enabled use it'getS() verbiage
            if ((expListLayouts.get(i).findViewById(R.id.verbiage_text)).isEnabled()) {


                if (((EditText) expListLayouts.// IF USER DID NOT ENTER ANYTHING IN THE FIELD
                        get(i).findViewById(R.id.verbiage_text))
                        .getText().toString().equals(""))
                    verbiageList.add("NA");
                else
                    verbiageList.add(((EditText) expListLayouts.
                            get(i).findViewById(R.id.verbiage_text))
                            .getText().toString());

                if (((EditText) expListLayouts.// IF USER DID NOT ENTER ANYTHING IN THE FIELD
                        get(i).findViewById(R.id.verbiage_really_text))
                        .getText().toString().equals(""))
                    verbiageList.add("NA");
                else
                    verbiageList.add(((EditText) expListLayouts.
                            get(i).findViewById(R.id.verbiage_really_text))
                            .getText().toString());
            } else // if view is disabled, write NA in place of that
            {
                verbiageList.add("NA");
                verbiageList.add("NA");
            }
        }

        Icon newIcon = new Icon();
        newIcon.setDisplay_Label(thisIcon.getText());
        newIcon.setSpeech_Label(thisIcon.getText());
        newIcon.setL(verbiageList.get(0));
        newIcon.setLL(verbiageList.get(1));
        newIcon.setY(verbiageList.get(2));
        newIcon.setYY(verbiageList.get(3));
        newIcon.setM(verbiageList.get(4));
        newIcon.setMM(verbiageList.get(5));
        newIcon.setD(verbiageList.get(6));
        newIcon.setDD(verbiageList.get(7));
        newIcon.setN(verbiageList.get(8));
        newIcon.setNN(verbiageList.get(9));
        newIcon.setS(verbiageList.get(10));
        newIcon.setSS(verbiageList.get(1));

        return newIcon;
    }

    private void enableAllViews(boolean disable) {
        if (disable)
            for (int i = 0; i < 6; i++) {
                expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(true);
                expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(1.0f);
                expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(true);
                expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(1.0f);
                ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
            }
        else
            for (int i = 0; i < 6; i++) {
                expListLayouts.get(i).findViewById(R.id.verbiage_text).setEnabled(false);
                expListLayouts.get(i).findViewById(R.id.verbiage_text).setAlpha(.6f);
                expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setEnabled(false);
                expListLayouts.get(i).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
                ((ImageView) expListLayouts.get(i).findViewById(R.id.add_remove)).
                        setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
            }

    }


    public void presentVerbiage(Icon verbiageModel) {
        this.presentVerbiage = verbiageModel;
        if (verbiageModel != null) {
            if (verbiageModel.getL().equals("NA")) {
                disableVerbiage(0, true);

            } else {
                disableVerbiage(0, false);
                ((EditText) expListLayouts.get(0).findViewById(R.id.verbiage_text)).setText(verbiageModel.getL());
                ((EditText) expListLayouts.get(0).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getLL());
            }
            if (verbiageModel.getY().equals("NA")) {
                disableVerbiage(1, true);
            } else {
                disableVerbiage(1, false);
                ((EditText) expListLayouts.get(1).findViewById(R.id.verbiage_text)).setText(verbiageModel.getY());
                ((EditText) expListLayouts.get(1).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getYY());
            }
            if (verbiageModel.getM().equals("NA")) {
                disableVerbiage(2, true);
            } else {

                disableVerbiage(2, false);
                ((EditText) expListLayouts.get(2).findViewById(R.id.verbiage_text)).setText(verbiageModel.getM());
                ((EditText) expListLayouts.get(2).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getMM());
            }
            if (verbiageModel.getD().equals("NA")) {
                disableVerbiage(3, true);
            } else {
                disableVerbiage(3, false);
                ((EditText) expListLayouts.get(3).findViewById(R.id.verbiage_text)).setText(verbiageModel.getD());
                ((EditText) expListLayouts.get(3).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getDD());
            }

            if (verbiageModel.getN().equals("NA")) {
                disableVerbiage(4, true);
            } else {
                disableVerbiage(4, false);
                ((EditText) expListLayouts.get(4).findViewById(R.id.verbiage_text)).setText(verbiageModel.getN());
                ((EditText) expListLayouts.get(4).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getNN());
            }
            if (verbiageModel.getS().equals("NA")) {
                disableVerbiage(5, true);
            } else {
                disableVerbiage(5, false);
                ((EditText) expListLayouts.get(5).findViewById(R.id.verbiage_text)).setText(verbiageModel.getS());
                ((EditText) expListLayouts.get(5).findViewById(R.id.verbiage_really_text)).setText(verbiageModel.getSS());
            }
        }
    }

    public void disableVerbiage(int index, boolean disable) {
        if (disable) {
            expListLayouts.get(index).findViewById(R.id.verbiage_text).setEnabled(false);
            expListLayouts.get(index).findViewById(R.id.verbiage_text).setAlpha(.6f);
            expListLayouts.get(index).findViewById(R.id.verbiage_really_text).setEnabled(false);
            expListLayouts.get(index).findViewById(R.id.verbiage_really_text).setAlpha(.6f);
            ((ImageView) expListLayouts.get(index).findViewById(R.id.add_remove)).
                    setImageDrawable(context.getResources().getDrawable(R.drawable.plus));
            ((EditText) expListLayouts.get(index).findViewById(R.id.verbiage_text)).setText(null);
            ((EditText) expListLayouts.get(index).findViewById(R.id.verbiage_really_text)).setText(null);

        } else {
            expListLayouts.get(index).findViewById(R.id.verbiage_text).setEnabled(true);
            expListLayouts.get(index).findViewById(R.id.verbiage_text).setAlpha(1f);
            expListLayouts.get(index).findViewById(R.id.verbiage_really_text).setEnabled(true);
            expListLayouts.get(index).findViewById(R.id.verbiage_really_text).setAlpha(1f);
            ((ImageView) expListLayouts.get(index).findViewById(R.id.add_remove)).
                    setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
        }
    }

}
