package com.dsource.idc.jellowintl.makemyboard;


import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.IconDatabase;
import com.dsource.idc.jellowintl.utility.JellowIcon;

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
    public boolean getSelection(ArrayList<JellowIcon> selectedList,ArrayList<JellowIcon> iconList)
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
    public boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list)
    {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
    }

    /**
     * Here I am using SQL to sort the list
     * @param list
     * @param Level
     * @param context
     * @return
     */
    public ArrayList<JellowIcon>sortUsingSQL(ArrayList<JellowIcon> list, int Level, Context context)
    {
        ArrayList<JellowIcon> levelList=new IconDatabase(context).myBoardQuery(Level,-1);
        ArrayList<JellowIcon> sortedList=new ArrayList<>();
        for(int i=0;i<levelList.size();i++)
        {
            if(listContainsIcon(levelList.get(i),list))
                sortedList.add(levelList.get(i));

            if(sortedList.size()==list.size())
                break;
        }

       return sortedList;
    }

    /**
     * Here using SQL to sort the list
     * @param list
     * @param context
     * @return
     */
    public ArrayList<JellowIcon>sortUsingSQL(ArrayList<JellowIcon> list, Context context)
    {
        ArrayList<JellowIcon> levelList=new IconDatabase(context).getAllIcons();
        ArrayList<JellowIcon> sortedList=new ArrayList<>();
        for(int i=0;i<levelList.size();i++)
        {
            if(listContainsIcon(levelList.get(i),list))
                sortedList.add(levelList.get(i));

            if(sortedList.size()==list.size())
                break;
        }

        return sortedList;
    }

    /**
     * This functions returns the list of second level parents, even if the level two parents are not selected but the level three of that icon is selected.
     * @param populatedList Current populated list
     * @param parent_1_pos  Level 1 parent
     * @param mContext Context
     * @return a list of level second elements.
     */
    public static ArrayList<JellowIcon> getLevelTwoIcons(ArrayList<JellowIcon> populatedList, int parent_1_pos,Context mContext)
    {
        ArrayList<JellowIcon> finalList=new ArrayList<>();
        ArrayList<Integer> level2IconPresent=new ArrayList<>();

        for(int i=0;i<populatedList.size();i++)
        {
            Log.d("LEVEL_2","Level two Icon: "+populatedList.get(i).parent1);
            int par=populatedList.get(i).parent1;
            if(level2IconPresent.indexOf(par)==-1)
                level2IconPresent.add(par);
        }
        IconDatabase data=new IconDatabase(mContext);

        for(int i=0;i<level2IconPresent.size();i++)
        {
            Log.d("LEVEL_2","Icon: "+data.fetchIcon(parent_1_pos,level2IconPresent.get(i),-1));
            JellowIcon icon=data.fetchIcon(parent_1_pos,level2IconPresent.get(i),-1);
            if(icon.parent1!=-1)
                finalList.add(icon);
        }
        return finalList;
    }


}
