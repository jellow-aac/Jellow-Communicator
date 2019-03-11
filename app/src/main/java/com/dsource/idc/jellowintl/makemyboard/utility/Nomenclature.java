package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Context;

import com.dsource.idc.jellowintl.models.JellowIcon;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

public class Nomenclature {

    public static String getIconName(JellowIcon icon, Context context) {

            String LC = getLanguageCode(context);
            String L1 = getLevelOneCode(icon);
            String L2 = getLevelTwoCode(icon);
            String L3 = getLevelThreeCode(icon);
            if(icon.parent0!=-1)
            return LC+L1+L2+L3;
            else return LC+L1+L2+L3+"CC";
    }

    public static String getIconName(int p1,int p2,int p3,Context context) {

        String LC = getLanguageCode(context);
        String L1 = getLevelOneCode(p1);
        String L2 = getLevelTwoCode(p1,p2,p3);
        String L3 = getLevelThreeCode(p1,p2,p3);
        if(p1!=-1)
            return LC+L1+L2+L3+getIconType(p1,p2);
        else return LC+L1+L2+L3+"CC";
    }
    //Just to know which icons are of Sequence category.
    public static String getIconType(int p1,int p2)
    {
        if(p1==1&&(p2==0||p2==1||p2==2||p2==7||p2==8))
            return "SS";
        else
            return "GG";
    }


    private static String getLevelTwoCode(int p1, int p2, int p3) {

        if(p1==-1)
            return "";
        if(p2==-1)
            return String.format("%02d", 0);

        return String.format("%02d", (p2+1));
    }

    private static String getLevelThreeCode(int p1, int p2, int p3) {

        if(p1==-1)
            return p3+"";
        if(p3==-1)
            return String.format("%04d", 0);

        return String.format("%04d", (p3+1));
    }

    private static String getLevelOneCode(int p1) {
        if(p1!=-1)
            return String.format("%02d", (p1+1));
        else return "";
    }


    private static String getLevelThreeCode(JellowIcon icon) {
        if(icon.parent0==-1)
            return icon.parent2+"";
        if(icon.parent2==-1)
            return String.format("%04d", 0);

        return String.format("%04d", (icon.parent2+1));
    }
    private static String getLevelTwoCode(JellowIcon icon) {
        if(icon.parent0==-1)
            return "";
        if(icon.parent1==-1)
            return String.format("%02d", 0);

        return String.format("%02d", (icon.parent1+1));
    }
    private static String getLevelOneCode(JellowIcon icon) {
        if(icon.parent0!=-1)
        return String.format("%02d", (icon.parent0+1));
        else return "";
    }
    public static String getLanguageCode(Context context) {
        String lang= ENG_IN;
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
    public static String getNameForExpressiveIcons(Context context,int code) {
        String LangCode=getLanguageCode(context);
        String IconCode=getLevelMicellaneousCode(code);
        return  LangCode+IconCode+"EE";
    }
    private static String getLevelMicellaneousCode(int i) {
        return String.format("%02d", (i+1));
    }


}
