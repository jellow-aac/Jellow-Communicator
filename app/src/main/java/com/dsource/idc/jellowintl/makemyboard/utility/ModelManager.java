package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.makemyboard.models.Board;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import androidx.annotation.Keep;

public class ModelManager {
    private ArrayList<JellowIcon> mailIconList;
    private ArrayList<JellowIcon> levelOneIcons;
    private ArrayList<Integer> levelOneIndex;
    private ArrayList<JellowIcon> levelTwoIcons;
    private ArrayList<JellowIcon> levelThreeIcons;
    @Keep
    public IconModel parentNode;
    private BoardDatabase boardDatabase;

    @Keep
    public ModelManager(ArrayList<JellowIcon> mailIconList, Context context)
    {
        this.mailIconList = mailIconList;
        levelOneIcons=new ArrayList<>();
        levelOneIndex=new ArrayList<>();
        levelTwoIcons=new ArrayList<>();
        levelThreeIcons=new ArrayList<>();
        parentNode=new IconModel(new JellowIcon("","",-1,-1,-1));
        boardDatabase = new BoardDatabase(context);
        prepareLevels();
        prepareModel();
        refreshModel();
    }
    @Keep public ModelManager(Context context,IconModel parentNode)
    {
        boardDatabase = new BoardDatabase(context);
        this.parentNode=parentNode;
    }
    @Keep public ArrayList<JellowIcon> getAllIconsOfModel(){
        ArrayList<JellowIcon> list = new ArrayList<>();
        //LevelOne Icons
        for(int i = 0 ;i  < parentNode.getChildren().size();i++)
            list.add(parentNode.getChildren().get(i).getIcon());
        //LevelTwo Icons
        for(int i = 0 ;i  < parentNode.getChildren().size();i++)
        {
            IconModel levelTwo =  parentNode.getChildren().get(i);
            for(int j = 0 ;j  < levelTwo.getChildren().size();j++)
                list.add(levelTwo.getChildren().get(j).getIcon());
        }
        //Level Three
        for(int i = 0 ;i  < parentNode.getChildren().size();i++)
        {
            IconModel levelTwo =  parentNode.getChildren().get(i);
            for(int j = 0 ;j  < levelTwo.getChildren().size();j++)
            {
                IconModel levelThree = levelTwo.getChildren().get(j);
                for(int k = 0; k<levelThree.getChildren().size();k++)
                    list.add(levelThree.getChildren().get(k).getIcon());
            }
        }


        return list;
    }

    @Keep private void prepareLevels() {

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
            }
            else //level three
            {
                levelThreeIcons.add(icon);
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
    @Keep public ArrayList<JellowIcon> getLevelOneIcons()
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
    @Keep public ArrayList<JellowIcon> getLevelTwoIcons(JellowIcon icon)
    {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        if(icon.parent2!=-1)
            return subList;
        if(icon.parent1==-1)//if the Icon is of level one then it could have both level two icons and level three icons
            {
                ArrayList<Integer> levelTwoParents=new ArrayList<>();
                for(int i=0;i<levelTwoIcons.size();i++)
                    {
                        if(levelTwoIcons.get(i).parent0==icon.parent0)
                            {
                                subList.add(levelTwoIcons.get(i));
                                levelTwoParents.add(levelTwoIcons.get(i).parent1);
                            }

                    }

                for(int i=0;i<levelThreeIcons.size();i++)
                    {

                        if((!levelTwoParents.contains(levelThreeIcons.get(i).parent1))&&levelThreeIcons.get(i).parent0==icon.parent0)
                        {
                            subList.add(levelThreeIcons.get(i));
                        }
                    }
        }
        else
        if(icon.parent2==-1)//if the icon is of level two then the child can be only of level three
        {
            for(int i=0;i<levelThreeIcons.size();i++) {
                if (levelThreeIcons.get(i).parent0 == icon.parent0&&levelThreeIcons.get(i).parent1==icon.parent1)
                    subList.add(levelThreeIcons.get(i));
            }
        }
        return subList;
    }

    /**
     * This function returns the level Three Icon from the manager.
     * @param icon
     * @return
     */
    @Keep public ArrayList<JellowIcon> getLevelThreeIcons(JellowIcon icon)
    {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        if (icon.parent2!=-1)
            return subList;
        for(int i=0;i<levelThreeIcons.size();i++)
            if(levelThreeIcons.get(i).parent0==icon.parent0&&levelThreeIcons.get(i).parent1==icon.parent1)
                subList.add(levelThreeIcons.get(i));
        return subList;
    }

    @Keep private ArrayList<JellowIcon> getOtherLevelOneIcons() {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        ArrayList<indexHolder> presentItemList=new ArrayList<>();
        for(int i=0;i<levelTwoIcons.size();i++)
        {
            JellowIcon icon=levelTwoIcons.get(i);
            if(!levelOneIndex.contains(icon.parent0))
            {
                subList.add(icon);
                presentItemList.add(new indexHolder(icon.parent0,icon.parent1));
            }
        }

        for(int i=0;i<levelThreeIcons.size();i++)
        {
            JellowIcon icon=levelThreeIcons.get(i);
            indexHolder obj=new indexHolder(icon.parent0,icon.parent1);
            if(!levelOneIndex.contains(icon.parent0))
                if(!obj.presentInList(presentItemList))
                    subList.add(icon);
        }

        return subList;
    }

    @Keep public void deleteIconFromModel(int level, int levelOnePos, int levelTwoPos, int pos, Board board)
    {
        boolean updated=false;
        if(level==0)
        {
            updated=true;
            parentNode.getChildren().remove(pos);
        }
        else if(level==1)
        {
            updated=true;
            parentNode.getChildren().get(levelOnePos).getChildren().remove(pos);
        }
        else if(level==2)
        {
            updated=true;
            parentNode.getChildren().get(levelOnePos).getChildren().get(levelTwoPos).getChildren().remove(pos);
        }

        if(updated)
        {
            board.setBoardIconModel(parentNode);
            boardDatabase.updateBoardIntoDatabase(board);
        }
        refreshModel();

    }
    @Keep public void updateItemMoved(int level, int levelOneParent, int levelTwoParent, int fromPosition, int toPosition, Board currentBoard) {

        boolean updated=false;
        if(level==0)
        {
            updated=true;
            parentNode.getChildren().add(toPosition, parentNode.getChildren().remove(fromPosition));
        }
        else if(level==1)
        {
            updated=true;
            parentNode.getChildren().get(levelOneParent).getChildren().add(toPosition,
                    parentNode.getChildren().get(levelOneParent).getChildren().remove(fromPosition));
        }
        else if(level==2)
        {
            updated=true;
            parentNode.getChildren().get(levelOneParent).getChildren().get(levelTwoParent).getChildren().
                    add(toPosition,
                            parentNode.getChildren().get(levelOneParent).getChildren().get(levelTwoParent).getChildren().
                                    remove(fromPosition));
        }

        if(updated)
        {
            currentBoard.setBoardIconModel(parentNode);
        }
        refreshModel();
    }

    private void prepareModel() {

        if(levelOneIcons.size()>0) {
            parentNode.addAllChild(this.getLevelOneIcons());

            for (int i = 0; i < parentNode.getChildren().size(); i++) {
                JellowIcon icon = parentNode.getChildren().get(i).getIcon();
                ArrayList<JellowIcon> thisList = this.getLevelTwoIcons(icon);
                parentNode.getChildren().get(i).addAllChild(thisList);
            }

            for (int i = 0; i < parentNode.getChildren().size(); i++) {
                for (int j = 0; j < parentNode.getChildren().get(i).getChildren().size(); j++) {
                    JellowIcon icon = parentNode.getChildren().get(i).getChildren().get(j).getIcon();
                    ArrayList<JellowIcon> thisList = this.getLevelThreeIcons(icon);
                    parentNode.getChildren().get(i).getChildren().get(j).addAllChild(thisList);
                }
            }
        }
        else if(levelTwoIcons.size()>0)//if there is no level one parent then the base node will be the level 2 node
        {
            parentNode.addAllChild(this.getOtherLevelOneIcons());
            for(int i=0;i<parentNode.getChildren().size();i++)
            {
                JellowIcon icon = parentNode.getChildren().get(i).getIcon();
                ArrayList<JellowIcon> thisList = this.getLevelThreeIcons(icon);
                parentNode.getChildren().get(i).addAllChild(thisList);
            }

        }
        else {
            parentNode.addAllChild(levelThreeIcons);
        }
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

    @Keep public void moveIconToFrom(int fromLevel, int toLevel, int levelOneParent, int levelTwoParent, int levelThreeParent,Board board) {
            IconModel IconToMove=null;
            if(fromLevel==2)
            {
                IconToMove=parentNode.getChildren().get(levelOneParent).getChildren().get(levelTwoParent).getChildren().get(levelThreeParent);
                parentNode.getChildren().get(levelOneParent).getChildren().get(levelTwoParent).getChildren().remove(levelThreeParent);

            }
            else if(fromLevel==1)
            {
                IconToMove=parentNode.getChildren().get(levelOneParent).getChildren().get(levelTwoParent);
                parentNode.getChildren().get(levelOneParent).getChildren().remove(levelTwoParent);
            }
            if(IconToMove!=null)
            {
                if(toLevel==1)
                {
                    parentNode.getChildren().get(levelOneParent).getChildren().add(IconToMove);
                }
                else if(toLevel==0)
                {
                    parentNode.getChildren().add(IconToMove);
                }
                board.setBoardIconModel(parentNode);
                boardDatabase.updateBoardIntoDatabase(board);
            }
            refreshModel();

    }

    /**
     * Returns the list of icon matching icon in BoardModel
     * @param s | Search string
     * @return List of matching Icon
     */
    @Keep public ArrayList<JellowIcon> searchIconsForText(String s) {
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<parentNode.getChildren().size();i++)
            if(parentNode.getChildren().get(i).getIcon().IconTitle.toLowerCase().startsWith(s.toLowerCase()))
                list.add(parentNode.getChildren().get(i).getIcon());

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
                if(levelTwo.getChildren().get(j).getIcon().IconTitle.toLowerCase().startsWith(s.toLowerCase()))
                    list.add(levelTwo.getChildren().get(j).getIcon());

        }

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
            {
                IconModel levelThree = levelTwo.getChildren().get(j);
                for(int k = 0;k < levelThree.getChildren().size();k++)
                    if(levelThree.getChildren().get(k).getIcon().IconTitle.toLowerCase().startsWith(s.toLowerCase()))
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

    @Keep public boolean moveIconIntoCategory(int level, int levelOneParent, int levelTwoParent, int fromPosition, int position, Board board) {
        IconModel iconModel = null;

        if(level==0)
        {
            try {
                iconModel = parentNode.getChildren().get(fromPosition);
                parentNode.getChildren().remove(fromPosition);
            }
            catch (IndexOutOfBoundsException e)
            {
             return false;
            }
        }
        else if(level==1)
        {
            try {
                iconModel = parentNode.getChildren().get(levelOneParent).getChildren().get(position);
                parentNode.getChildren().get(levelOneParent).getChildren().remove(position);
            }
            catch (IndexOutOfBoundsException e)
            {
                return false;
            }
        }

        if(iconModel!=null)
        {
            if(level==0)
            {
                parentNode.getChildren().get(position).getChildren().add(iconModel);
            }
            else
            {
                parentNode.getChildren().get(levelOneParent).getChildren().get(position).getChildren().add(iconModel);
            }

            board.setBoardIconModel(parentNode);
            boardDatabase.updateBoardIntoDatabase(board);
            return true;
        }
        refreshModel();
        return false;
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
                String iconTitle = parentNode.getChildren().get(i).getIcon().IconTitle;
                if(!iconTitle.contains("…"))
                    parentNode.getChildren().get(i).getIcon().setIconTitle(iconTitle+"…");

            }
            else
            {
                String iconTitle = parentNode.getChildren().get(i).getIcon().IconTitle;
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
                    String iconTitle = levelTwoModel.getChildren().get(j).getIcon().IconTitle;
                    if(!iconTitle.contains("…"))
                        levelTwoModel.getChildren().get(j).getIcon().setIconTitle(iconTitle+"…");
                }
                else
                {
                    String iconTitle = levelTwoModel.getChildren().get(j).getIcon().IconTitle;
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
                        String iconTitle = levelThreeModel.getChildren().get(k).getIcon().IconTitle;
                        if(!iconTitle.contains("…"))
                            levelThreeModel.getChildren().get(k).getIcon().setIconTitle(iconTitle+"…");
                    }
                    else
                    {
                        String iconTitle = levelThreeModel.getChildren().get(k).getIcon().IconTitle;
                        iconTitle = iconTitle.replaceAll("…","");
                        levelThreeModel.getChildren().get(k).getIcon().setIconTitle(iconTitle);
                    }
                }

            }
        }

    }

    /**
     * A simple class to hold the index of the icon to be returned by the function.
     */
    @Keep private class indexHolder
    {
        int p0,p1;

        indexHolder(int p0, int p1) {
            this.p0 = p0;
            this.p1 = p1;
        }
        indexHolder getSelfObj()
        {
            return new indexHolder(p0,p1);
        }
         boolean equal(indexHolder h)
         {
             return this.p0 == h.p0 && this.p1 == h.p1;
         }
         boolean presentInList(ArrayList<indexHolder> list)
         {
             indexHolder obj= getSelfObj();
             for(int i=0;i<list.size();i++)
                 if(list.get(i).equal(obj))
                     return true;
             return false;
         }

    }
}
