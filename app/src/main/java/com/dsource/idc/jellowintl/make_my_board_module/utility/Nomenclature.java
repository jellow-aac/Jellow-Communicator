package com.dsource.idc.jellowintl.make_my_board_module.utility;

import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.Locale;

public class Nomenclature {

    public static String getNameForExpressiveIcons(int code,String LCode) {
        String LangCode= LanguageFactory.getLanguageCode(LCode);
        String IconCode= getLevelMiscellaneousCode(code);
        return  LangCode+IconCode+"EE";
    }
    private static String getLevelMiscellaneousCode(int i) {
        return String.format(new Locale(SessionManager.ENG_US),"%02d", (i+1));
    }


}
