package com.dsource.idc.jellowintl.makemyboard.utility;


import android.util.Log;

import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

/**
 * All the logic functions of the Make my board capability are declared here
 * I did this because it will help us to make the unit test for make my board easily
 * @Author Ayaz Alam
 */
public class UtilFunctions {

    /**
     * Checks a list is the sublist of the other symbol
     * @param selectedList
     * @param iconList
     * @return
     */
    public static boolean getSelection(ArrayList<JellowIcon> selectedList, ArrayList<JellowIcon> iconList)
    {
        boolean isOkay=true;
        //return  false if selected icon list is shorter than icon list
        if(selectedList.size()<iconList.size())
            return false;

        for(int i=0;i<iconList.size();i++)
        {
            isOkay=listContainsIcon(iconList.get(i),selectedList);
            if(!isOkay)//Return if there's any element that is not present in the list
                return false;
        }
        return isOkay;
    }

    /***
     * This function checks whether a icon is present in the list or not
     * @param icon
     * @param list
     * @return boolean
     */
    public static boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list)
    {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
    }

    public static boolean doReset(ArrayList<JellowIcon> list,ArrayList<JellowIcon> selectedList){
        for(JellowIcon icon : list){
            if(listContainsIcon(icon,selectedList))
                return true;
        }
        return false;
    }



}
