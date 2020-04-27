package com.dsource.idc.jellowintl.makemyboard.utility;

import androidx.annotation.Keep;

import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class ModelManager {

    @Keep private IconModel parentNode;

    @Keep public ModelManager(IconModel parentNode){
        this.parentNode=parentNode;
    }

    @Keep public IconModel getModel()
    {
        return parentNode;
    }

    @Keep public void setModel(IconModel model)
    {
        this.parentNode=model;
        refreshModel();
    }

    @Keep public ArrayList<JellowIcon> getLevelOneFromModel()
    {
        return parentNode.getSubList();
    }

    @Keep public ArrayList<JellowIcon> getLevelTwoFromModel(int parent1)
    {
        return parentNode.getChildren().get(parent1).getSubList();
    }

    @Keep public ArrayList<JellowIcon> getLevelThreeFromModel(int parent1,int parent2)
    {
        return parentNode.getChildren().get(parent1).getChildren().get(parent2).getSubList();
    }

    /**
     * Returns the list of icon matching icon in BoardModel
     * @param s | Search string
     * @return List of matching Icon
     */
    @Keep public ArrayList<JellowIcon> searchIconsForText(String s) {
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<parentNode.getChildren().size();i++)
            if(parentNode.getChildren().get(i).getIcon().getIconTitle().toLowerCase().startsWith(s.toLowerCase()))
                list.add(parentNode.getChildren().get(i).getIcon());

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
                if(levelTwo.getChildren().get(j).getIcon().getIconTitle().toLowerCase().startsWith(s.toLowerCase()))
                    list.add(levelTwo.getChildren().get(j).getIcon());

        }

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
            {
                IconModel levelThree = levelTwo.getChildren().get(j);
                for(int k = 0;k < levelThree.getChildren().size();k++)
                    if(levelThree.getChildren().get(k).getIcon().getIconTitle().toLowerCase().startsWith(s.toLowerCase()))
                         list.add(levelThree.getChildren().get(k).getIcon());
            }

        }


        return list;
    }

    /**
     * This returns position of any icon position in the model
     * @param icon | Icon whose position is to be searched
     * @return  Position of the Icon
     */
    @Keep public ArrayList<Integer> getIconPositionInModel(JellowIcon icon)
    {
        ArrayList<Integer> position= new ArrayList<>();
        for(int i=0;i<parentNode.getChildren().size();i++)
            if(parentNode.getChildren().get(i).getIcon().isEqual(icon)) {
                position.add(i);
                position.add(-1);
                position.add(-1);
                return position;
            }

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
                if(levelTwo.getChildren().get(j).getIcon().isEqual(icon)) {
                    position.add(i);
                    position.add(j);
                    position.add(-1);
                    return position;
                }

        }

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
            {
                IconModel levelThree = levelTwo.getChildren().get(j);
                for(int k = 0;k < levelThree.getChildren().size();k++)
                    if(levelThree.getChildren().get(k).getIcon().isEqual(icon))
                    {
                        position.add(i);
                        position.add(j);
                        position.add(k);
                        return position;
                    }
            }

        }

    return position;
    }

    /**
     * This function is responsible for putting the 3-dots in the icon's title.
     */
    @Keep public void refreshModel()
    {
        //Level One
        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            if(parentNode.getChildren().get(i).getChildren().size()>0)
            {
                String iconTitle = parentNode.getChildren().get(i).getIcon().getIconTitle();
                if(!iconTitle.contains("…"))
                    parentNode.getChildren().get(i).getIcon().setIconTitle(iconTitle+"…");

            }
            else
            {
                String iconTitle = parentNode.getChildren().get(i).getIcon().getIconTitle();
                iconTitle = iconTitle.replaceAll("…","");
                parentNode.getChildren().get(i).getIcon().setIconTitle(iconTitle);
            }
        }

        //LevelTwo
        for(int i=0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwoModel = parentNode.getChildren().get(i);
            for(int j=0;j<levelTwoModel.getChildren().size();j++)
            {
                if(levelTwoModel.getChildren().get(j).getChildren().size()>0)
                {
                    String iconTitle = levelTwoModel.getChildren().get(j).getIcon().getIconTitle();
                    if(!iconTitle.contains("…"))
                        levelTwoModel.getChildren().get(j).getIcon().setIconTitle(iconTitle+"…");
                }
                else
                {
                    String iconTitle = levelTwoModel.getChildren().get(j).getIcon().getIconTitle();
                    iconTitle = iconTitle.replaceAll("…","");
                    levelTwoModel.getChildren().get(j).getIcon().setIconTitle(iconTitle);
                }

            }
        }

        //LevelThree
        for(int i=0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwoModel = parentNode.getChildren().get(i);
            for(int j=0;j<levelTwoModel.getChildren().size();j++)
            {
                IconModel levelThreeModel = levelTwoModel.getChildren().get(j);
                for(int k = 0; k<levelThreeModel.getChildren().size();k++)
                {
                    if(levelThreeModel.getChildren().get(k).getChildren().size()>0)
                    {
                        String iconTitle = levelThreeModel.getChildren().get(k).getIcon().getIconTitle();
                        if(!iconTitle.contains("…"))
                            levelThreeModel.getChildren().get(k).getIcon().setIconTitle(iconTitle+"…");
                    }
                    else
                    {
                        String iconTitle = levelThreeModel.getChildren().get(k).getIcon().getIconTitle();
                        iconTitle = iconTitle.replaceAll("…","");
                        levelThreeModel.getChildren().get(k).getIcon().setIconTitle(iconTitle);
                    }
                }

            }
        }

    }
}
