package com.dsource.idc.jellowintl.makemyboard.UtilityClasses;

import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.makemyboard.Board;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ModelManager {
    private ArrayList<JellowIcon> mailIconList;
    private ArrayList<JellowIcon> levelOneIcons;
    private ArrayList<Integer> levelOneIndex;
    private ArrayList<JellowIcon> levelTwoIcons;
    private ArrayList<JellowIcon> levelThreeIcons;
    private IconModel parentNode;
    private Context context;

    public ModelManager(ArrayList<JellowIcon> mailIconList, Context context)
    {
        this.mailIconList = mailIconList;
        this.context=context;
        levelOneIcons=new ArrayList<>();
        levelOneIndex=new ArrayList<>();
        levelTwoIcons=new ArrayList<>();
        levelThreeIcons=new ArrayList<>();
        parentNode=new IconModel(new JellowIcon("","",-1,-1,-1));
        prepareLevels();
        prepareModel();
    }
    public ModelManager(Context context,IconModel parentNode)
    {
        this.context=context;
        this.parentNode=parentNode;
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
     * @param icon
     * @return
     */
    public ArrayList<JellowIcon> getLevelThreeIcons(JellowIcon icon)
    {
        ArrayList<JellowIcon> subList=new ArrayList<>();
        if (icon.parent2!=-1)
            return subList;
        for(int i=0;i<levelThreeIcons.size();i++)
            if(levelThreeIcons.get(i).parent0==icon.parent0&&levelThreeIcons.get(i).parent1==icon.parent1)
                subList.add(levelThreeIcons.get(i));
        return subList;
    }

    private ArrayList<JellowIcon> getOtherLevelOneIcons() {
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

    public void deleteIconFromModel(int level,int levelOnePos,int levelTwoPos,int pos,Board board)
    {
        Log.d("Delete Mode","Onto Database "+level);
        boolean updated=false;
        if(level==0)
        {
            updated=true;
            Log.d("IconDeleted",""+parentNode.getChildren().get(pos).getIcon().IconTitle);
            parentNode.getChildren().remove(pos);
        }
        else if(level==1)
        {
            updated=true;
            Log.d("IconDeleted",""+parentNode.getChildren().get(levelOnePos).getChildren().get(pos).getIcon().IconTitle);
            parentNode.getChildren().get(levelOnePos).getChildren().remove(pos);
        }
        else if(level==2)
        {
            updated=true;
            Log.d("IconDeleted",""+
            parentNode.getChildren().get(levelOnePos).getChildren().get(levelTwoPos).getChildren().get(pos).getIcon().IconTitle);
            parentNode.getChildren().get(levelOnePos).getChildren().get(levelTwoPos).getChildren().remove(pos);
        }

        if(updated)
        {
            board.setBoardIconModel(parentNode);
            new BoardDatabase(context).updateBoardIntoDatabase(new DataBaseHelper(context).getWritableDatabase(),board);
        }

    }
    public void updateItemMoved(int level, int levelOneParent, int levelTwoParent, int fromPosition, int toPosition, Board currentBoard) {
        Log.d("Delete Mode","Onto Database "+level);
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
            new BoardDatabase(context).updateBoardIntoDatabase(new DataBaseHelper(context).getWritableDatabase(),currentBoard);
        }

    }


    private void prepareModel() {

        if(levelOneIcons.size()>0) {
            parentNode.addAllChild(this.getLevelOneIcons());
            Log.d("WhichLevel","One");
            for (int i = 0; i < parentNode.getChildren().size(); i++) {
                JellowIcon icon = parentNode.getChildren().get(i).getIcon();
                ArrayList<JellowIcon> thisList = this.getLevelTwoIcons(icon);
                Log.d("LevelL2forL1Size " + icon.IconTitle, "" + thisList.size());
                parentNode.getChildren().get(i).addAllChild(thisList);
            }
            for (int i = 0; i < parentNode.getChildren().size(); i++) {
                for (int j = 0; j < parentNode.getChildren().get(i).getChildren().size(); j++) {
                    Log.d("LevelThreeChildren", "L1 " + parentNode.getChildren().get(i).getIcon().IconTitle
                            + " L2 " + parentNode.getChildren().get(i).getChildren().size()
                    );
                    JellowIcon icon = parentNode.getChildren().get(i).getChildren().get(j).getIcon();
                    ArrayList<JellowIcon> thisList = this.getLevelThreeIcons(icon);
                    parentNode.getChildren().get(i).getChildren().get(j).addAllChild(thisList);

                }
            }
        }
        else if(levelTwoIcons.size()>0)//if there is no level one parent then the base node will be the level 2 node
        {
            Log.d("WhichLevel","Two");
            parentNode.addAllChild(this.getOtherLevelOneIcons());
            for(int i=0;i<parentNode.getChildren().size();i++)
            {
                JellowIcon icon = parentNode.getChildren().get(i).getIcon();
                ArrayList<JellowIcon> thisList = this.getLevelThreeIcons(icon);
                parentNode.getChildren().get(i).addAllChild(thisList);
            }

        }
        else {
            Log.d("WhichLevel","Three");
            parentNode.addAllChild(levelThreeIcons);
        }


        Log.d("parentNode",new Gson().toJson(parentNode));
        //writeJsonStringToFile(new Gson().toJson(parentNode),context );



    }

    public IconModel getModel()
    {
        return parentNode;
    }

    public ArrayList<JellowIcon> getLevelOneFromModel()
    {
        return parentNode.getSubList();
    }
    public ArrayList<JellowIcon> getLevelTwoFromModel(int parent1)
    {
        return parentNode.getChildren().get(parent1).getSubList();
    }
    public ArrayList<JellowIcon> getLevelThreeFromModel(int parent1,int parent2)
    {
        return parentNode.getChildren().get(parent1).getChildren().get(parent2).getSubList();
    }
    public void setModel(IconModel model)
    {
        this.parentNode=model;
    }

    /**
     * This function is to write string data to the file
     * @param data
     * @param context
     */
    private void writeJsonStringToFile(String data, Context context) {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, "Model.json");
        Log.d("Path",path.getAbsolutePath());
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(data.getBytes());
            Log.d("Verbiage:","Written Successfully");

        }
        catch (IOException e)
        {

        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Jellow.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void moveIconToFrom(int fromLevel, int toLevel, int levelOneParent, int levelTwoParent, int levelThreeParent,Board board) {
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
                new BoardDatabase(context).updateBoardIntoDatabase(new DataBaseHelper(context).getWritableDatabase(),board);
            }

    }

    public ArrayList<JellowIcon> searchIconsForText(String s) {
        ArrayList<JellowIcon> list = new ArrayList<>();
        for(int i=0;i<parentNode.getChildren().size();i++)
            if(parentNode.getChildren().get(i).getIcon().IconTitle.toLowerCase().contains(s.toLowerCase()))
                list.add(parentNode.getChildren().get(i).getIcon());

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
                if(levelTwo.getChildren().get(j).getIcon().IconTitle.toLowerCase().contains(s.toLowerCase()))
                    list.add(levelTwo.getChildren().get(j).getIcon());

        }

        for(int i= 0;i<parentNode.getChildren().size();i++)
        {
            IconModel levelTwo = parentNode.getChildren().get(i);
            for(int j = 0;j < levelTwo.getChildren().size();j++)
            {
                IconModel levelThree = levelTwo.getChildren().get(j);
                for(int k = 0;k < levelThree.getChildren().size();k++)
                    if(levelThree.getChildren().get(k).getIcon().IconTitle.toLowerCase().contains(s.toLowerCase()))
                         list.add(levelThree.getChildren().get(k).getIcon());
            }

        }


        return list;
    }

    public ArrayList<Integer> getIconPositionInModel(JellowIcon icon)
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


    private class indexHolder
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
