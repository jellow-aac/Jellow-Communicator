package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers;

import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class SelectionManager {
    private ArrayList<JellowIcon> selectedIconList;

    private static volatile SelectionManager instance;
    private SelectionManager() {
        selectedIconList =new ArrayList<>();
    }

    public synchronized static SelectionManager getInstance(){
        if(instance==null) {
            if(instance==null)//Double checking the instance to ensure singleton behaviour
                instance=new SelectionManager();
        }
        return instance;
    }

    /**
     * Add the icon to the list if not already present
     * @param icon Icon to be added to the list
     */
    public synchronized void addIconToList(JellowIcon icon)
    {
        boolean alreadyPresent=false;
        for(int i=0;i<selectedIconList.size();i++)
            if(icon.isEqual(selectedIconList.get(i)))
                alreadyPresent=true;
        if(!alreadyPresent)
            selectedIconList.add(icon);
    }
    /**
     * Removes the element from the current list
     * @param icon to be removed
     */
    public synchronized void removeIconFromList(JellowIcon icon)
    {
        int index=-1;
        for(int i=0;i<selectedIconList.size();i++)
            if(icon.isEqual(selectedIconList.get(i)))
                index=i;
        if(index!=-1)
            selectedIconList.remove(index);
    }

    public synchronized void setList(ArrayList<JellowIcon> selectedIconList) {
        this.selectedIconList =selectedIconList;
    }

    public ArrayList<JellowIcon> getList() {
        return selectedIconList;
    }

    /**
     * Code to implement select all/Deselect all capability
     * @param selectAll this will decide whether to selectall-or deselect all
     */
    public synchronized void selectAll(boolean selectAll,ArrayList<JellowIcon> iconList) {
        if(selectAll)
        {
            JellowIcon icon;
            for (int i = 0; i < iconList.size(); i++) {
                icon=iconList.get(i);
                boolean notAlreadyPresent=true;
                for(int j=0;j<selectedIconList.size();j++)
                    if(icon.isEqual(selectedIconList.get(j)))
                        notAlreadyPresent=false;

                if(notAlreadyPresent)
                    addIconToList(iconList.get(i));
            }
        }
        else {// Deselect current list
            for (int i = 0; i < iconList.size(); i++)
                removeIconFromList(iconList.get(i));
        }

    }

    public synchronized void resetSeletion(){
        selectedIconList.clear();
    }

    public void delete() {
        instance=null;
    }
}
