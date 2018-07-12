package com.dsource.idc.jellowintl;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.verbiage_model.Verbiage.ENG_UK;

public class Nomenclature {


    public static String getIconName(JellowIcon icon,Context context) {
        String LC=getLanguageCode(context);
        String L1=getLevelOneCode(icon);
        String L2=getLevelTwoCode(icon);
        String L3=getLevelThreeCode(icon);
        return LC+L1+L2+L3;
    }
    private static String getLevelThreeCode(JellowIcon icon) {
        if(icon.parent2==-1)
            return String.format("%04d", 0);

        return String.format("%04d", (icon.parent2+1));
    }
    private static String getLevelTwoCode(JellowIcon icon) {
        if(icon.parent1==-1)
            return String.format("%02d", 0);

        return String.format("%02d", (icon.parent1+1));
    }
    private static String getLevelOneCode(JellowIcon icon) {
        return String.format("%02d", (icon.parent0+1));
    }
    private static String getLanguageCode(Context context) {
        String lang=new SessionManager(context).getLanguage();
        int lang_code=1;
        if(lang.equals(ENG_IN))
            lang_code=1;
        else if(lang.equals(ENG_US))
            lang_code=2;
        else if(lang.equals(ENG_UK))
            lang_code=3;
        else if(lang.equals(HI_IN))
            lang_code=4;
        //Calling this function because it generates two digit string
        return String.format("%02d", lang_code);//for English india
    }
}
