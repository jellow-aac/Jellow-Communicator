package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import android.util.Log;

import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class Sorter {
    private ArrayList<JellowIcon> mailIconList;
    private ArrayList<JellowIcon> levelOneIcons;
    private ArrayList<Integer> levelOneIndex;
    private ArrayList<JellowIcon> levelTwoIcons;
    private ArrayList<Integer> levelTwoIndex;
    private ArrayList<JellowIcon> levelThreeIcons;
    private ArrayList<Integer> levelThreeIndex;

    public Sorter(ArrayList<JellowIcon> mailIconList) {
        this.mailIconList = mailIconList;
        levelOneIcons=new ArrayList<>();
        levelOneIndex=new ArrayList<>();
        levelTwoIcons=new ArrayList<>();
        levelTwoIndex=new ArrayList<>();
        levelThreeIcons=new ArrayList<>();
        levelThreeIndex=new ArrayList<>();
        prepareLevels();
    }

    private void prepareLevels() {

        for(int i=0;i<mailIconList.size();i++)
        {
            JellowIcon icon=mailIconList.get(i);
            if(icon.parent1==-1)//level one icon
            {
                levelOneIcons.add(icon);
                levelOneIndex.add(icon.parent0);
            }
            else if(icon.parent2==-1)//level two
            {
                levelTwoIcons.add(icon);
                levelTwoIndex.add(icon.parent1);
            }
            else //level three
            {
                levelThreeIcons.add(icon);
                levelThreeIndex.add(icon.parent2);
            }
        }

        Log.d("Level","Level 1: "+levelOneIcons.size()+" Level 2: "+levelTwoIcons.size()+" Level 3: "+levelThreeIcons.size());
    }

    /**
     * Logic behind this function is
     * 1. Get all the level One Icons ( with or without parents)
     * 2. Get all the level two parents with no level one parents
     * 3. Get all the level three icons with no parents
     */
    public ArrayList<JellowIcon> getLevelOneIcons()
    {
        ArrayList<JellowIcon> l1Icons=new ArrayList<>();
        l1Icons.addAll(levelOneIcons);
        l1Icons.addAll(getOtherLevelOneIcons());
        return l1Icons;
    }

    /**
     * This function returns the level two children of any level one item which could be of Level 1, 2 or 3
     * it will return level 2 and 3 child if the level 3 items without the level two parent are also added into the list.
     * if the icon is of level 3 then an empty list is returned
     * if the icon is of level 2 then all the level three elements of that icon is returned
     * if the icon is of level 1 then all the level two and level three(with no level two parent) is returned
     * @param icon
     * @return
     */
    public ArrayList<JellowIcon> getLevelTwoIcons(JellowIcon icon)
    {

        ArrayList<JellowIcon> subList=new ArrayList<>();
        if(icon.parent2==-1)//if the icon is of level two then the child can be only of level three
            for(int i=0;i<levelThreeIcons.size();i++) {
                if (levelThreeIcons.get(i).parent1 == icon.parent1)
                    subList.add(levelThreeIcons.get(i));
            }
            else if(icon.parent1==-1)//if the Icon is of level one then it could have both level two icons and level three icons
            {
                ArrayList<Integer> levelTwoParents=new ArrayList<>();
                for(int i=0;i<levelTwoIcons.size();i++)
                    {
                        if(levelTwoIcons.get(i).parent0==icon.parent0)
                            {
                                subList.add(levelOneIcons.get(i));
                                levelTwoParents.add(icon.parent1);
                            }

                    }

                for(int i=0;i<levelThreeIcons.size();i++)
                    {
                        if(!levelTwoParents.contains(levelThreeIcons.get(i).parent1)&&levelThreeIcons.get(i).parent0==icon.parent0)
                            subList.add(levelThreeIcons.get(i));

                    }
        }

        return subList;
    }

    /**
     * @param icon
     * @return
     */
    public ArrayList<JellowIcon> getLevelThreeIcons(JellowIcon icon)
    {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        for(int i=0;i<levelThreeIcons.size();i++)
            if(levelThreeIcons.get(i).parent0==icon.parent0&&levelThreeIcons.get(i).parent1==icon.parent1)
                subList.add(levelThreeIcons.get(i));

        return subList;
    }

    private ArrayList<JellowIcon> getOtherLevelOneIcons() {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        ArrayList<Integer> level2=new ArrayList<>();

        for(int i=0;i<levelTwoIcons.size();i++)
        {
            JellowIcon icon=levelTwoIcons.get(i);
            if(!levelOneIndex.contains(icon.parent0))
            {
                subList.add(icon);
                level2.add(icon.parent1);
            }
        }

        for(int i=0;i<levelThreeIcons.size();i++)
        {


            JellowIcon icon=levelThreeIcons.get(i);
            if(!levelOneIndex.contains(icon.parent0)&&!level2.contains(icon.parent1))
            subList.add(icon);
        }


        return subList;
    }



}
