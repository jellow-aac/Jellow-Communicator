package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

public class BoardLanguageHelper {
    private static BoardLanguageHelper instance;

    private BoardLanguageHelper(){}

    public synchronized static BoardLanguageHelper getInstance(){
        if(instance!=null) return instance;
        else instance = new BoardLanguageHelper();
        return instance;
    }

    public synchronized Context changeLanguage(Context context){
        String boardLang = new SessionManager(context).getCurrentBoardLanguage();
        if(boardLang==null||boardLang.equals(""))
            return LanguageHelper.onAttach(context,SessionManager.ENG_IN);
        return LanguageHelper.onAttach(context,boardLang);
    }

    public synchronized void restoreDefault(Context context){
        LanguageHelper.onAttach(context);
    }

}
