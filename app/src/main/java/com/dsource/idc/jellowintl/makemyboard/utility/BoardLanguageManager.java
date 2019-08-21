package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dsource.idc.jellowintl.LanguageDownloadActivity;
import com.dsource.idc.jellowintl.makemyboard.AddEditIconAndCategoryActivity;
import com.dsource.idc.jellowintl.makemyboard.HomeActivity;
import com.dsource.idc.jellowintl.makemyboard.RepositionIconsActivity;
import com.dsource.idc.jellowintl.makemyboard.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.FROM_MMB;

public class BoardLanguageManager {

    private BoardModel currentBoard;
    private Context context;
    private AppDatabase appDatabase;

    public BoardLanguageManager(BoardModel board, Context context, AppDatabase appDatabase){
        this.currentBoard = board;
        this.context =context;
        this.appDatabase = appDatabase;
    }



    public void checkLanguageAvailabilityInBoard() {

            if(new TextDatabase(context,currentBoard.getLanguage(), appDatabase).checkForTableExists()) {
                //if database for the language is ready
                if (currentBoard != null) {
                    Intent intent;
                    /*
                     * Board can have four stages.
                     * 1. Created but no icon selected -> conSelectActivity Opens
                     * 2. Icon selected and closed, didn't pass ADD_EDIT_SCREEN
                     * 3. Passed ADD_EDIT_ICON_SCREEN but repositions screen is not pass
                     * 4. Board Setup completed.
                     */
                    switch (currentBoard.getSetupStatus()) {
                        case BoardModel.STATUS_L0:intent = new Intent(context, IconSelectActivity.class);break;
                        case BoardModel.STATUS_L1:intent = new Intent(context, AddEditIconAndCategoryActivity.class);break;
                        case BoardModel.STATUS_L2:intent = new Intent(context, RepositionIconsActivity.class);break;
                        case BoardModel.STATUS_L3:intent = new Intent(context, HomeActivity.class);break;
                        default:intent = new Intent(context, IconSelectActivity.class);break;
                    }
                    intent.putExtra(BOARD_ID, currentBoard.getBoardId());
                    context.startActivity(intent);
                    new SessionManager(context).setCurrentBoardLanguage(currentBoard.getLanguage());
                }
            }
            else {
                //If database is not created, create the database
                Toast.makeText(context, "Database not created, creating database for the language", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, SetupMMB.class);
                intent.putExtra(BOARD_ID, currentBoard.getBoardId());
                intent.putExtra(LCODE, currentBoard.getLanguage());
                context.startActivity(intent);
            }
    }

    private void showDialogForLanguageDownload(){
        final CustomDialog customDialog =new CustomDialog(context,CustomDialog.NORMAL,currentBoard.getLanguage());
        customDialog.setText("Selected language is not downloaded into your device, please download it to proceed");
        customDialog.setPositiveText("Download");
        customDialog.setNegativeText("Cancel");
        customDialog.setOnPositiveClickListener(new CustomDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                startDownloading();
                customDialog.dismiss();
                //Download the language
            }
        });
        customDialog.setOnNegativeClickListener(new CustomDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                customDialog.dismiss();
                //Cancel the language download
            }
        });
        customDialog.setCancelable(false);
        customDialog.show();

    }

    private void startDownloading() {
        Intent intent = new Intent(context, LanguageDownloadActivity.class);
        intent.putExtra(LCODE,currentBoard.getLanguage());
        intent.putExtra(BOARD_ID,currentBoard.getBoardId());
        intent.putExtra(FROM_MMB,true);
        context.startActivity(intent);
    }

    public static int getPosition(String language){
        switch (language){
            case SessionManager.ENG_IN:return 0;
            case SessionManager.HI_IN:return 1;
            case SessionManager.ENG_US:return 2;
            case SessionManager.ENG_UK:return 3;
            default:return 0;
        }
    }



}
