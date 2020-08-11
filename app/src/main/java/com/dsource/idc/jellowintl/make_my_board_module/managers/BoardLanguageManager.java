package com.dsource.idc.jellowintl.make_my_board_module.managers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.activity.AddEditActivity;
import com.dsource.idc.jellowintl.make_my_board_module.activity.HomeActivity;
import com.dsource.idc.jellowintl.make_my_board_module.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.make_my_board_module.activity.SetupMMB;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.activities.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;

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
                        case BoardModel.STATUS_L2:
                        case BoardModel.STATUS_L1:intent = new Intent(context, AddEditActivity.class);break;
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
                Toast.makeText(context, context.getResources().getString(R.string.database_not_created_info), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, SetupMMB.class);
                intent.putExtra(BOARD_ID, currentBoard.getBoardId());
                intent.putExtra(LCODE, currentBoard.getLanguage());
                context.startActivity(intent);
            }
    }


}
