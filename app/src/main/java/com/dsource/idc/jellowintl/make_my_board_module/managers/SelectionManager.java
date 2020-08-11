package com.dsource.idc.jellowintl.make_my_board_module.managers;

import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectionManager {

    private ArrayList<JellowIcon> selectedIconList;

    private HashSet<String> hashSet;

    private static volatile SelectionManager instance;

    private SelectionManager() {
        selectedIconList = new ArrayList<>();
        hashSet = new HashSet<>();
    }

    public synchronized static SelectionManager getInstance() {
        if (instance == null) {
            if (instance == null)//Double checking the instance to ensure singleton behaviour
                instance = new SelectionManager();
        }
        return instance;
    }

    /**
     * Add the icon to the list if not already present
     *
     * @param icon Icon to be added to the list
     */
    public synchronized void addIconToList(JellowIcon icon) {
        if (!hashSet.contains(icon.getVerbiageId())) {
            selectedIconList.add(icon);
            hashSet.add(icon.getVerbiageId());
        }
    }

    /**
     * Removes the element from the current list
     *
     * @param icon to be removed
     */
    public synchronized void removeIconFromList(JellowIcon icon) {
        selectedIconList.remove(icon);
        hashSet.remove(icon.getVerbiageId());
    }

    public synchronized void setList(ArrayList<JellowIcon> selectedIconList) {
        this.selectedIconList = selectedIconList;
    }

    public ArrayList<JellowIcon> getList() {
        return selectedIconList;
    }

    /**
     * Code to implement select all/Deselect all capability
     *
     * @param selectAll this will decide whether to selectall-or deselect all
     */
    public synchronized void selectAll(boolean selectAll, ArrayList<JellowIcon> iconList) {
        if (selectAll) {
            for (int i = 0; i < iconList.size(); i++) {
                //Add if and only if the icon is not already present in the list
                if (!hashSet.contains(iconList.get(i).getVerbiageId())) {
                    hashSet.add(iconList.get(i).getVerbiageId());
                    selectedIconList.add(iconList.get(i));
                }
            }
        } else {// Deselect current list
            for (int i = 0; i < iconList.size(); i++)
                removeIconFromList(iconList.get(i));

        }

    }

    public void delete() {
        hashSet.clear();
        selectedIconList.clear();
        instance = null;
    }

    public boolean isPresent(JellowIcon icon) {
        return hashSet.contains(icon.getVerbiageId());
    }

    public boolean isSublist(ArrayList<JellowIcon> list) {
        for (JellowIcon icon : list)
            if (!hashSet.contains(icon.getVerbiageId())) return false;
        return true;
    }

    /**
     * Returns true if any of the element of argument list is present in the main list
     * @param list argument list
     * @return true if present otherwise false
     */
    public boolean containsAny(ArrayList<JellowIcon> list) {
        for (JellowIcon icon : list)
            if (hashSet.contains(icon.getVerbiageId())) return true;
        return false;
    }

    public void clearList() {
        hashSet.clear();
        selectedIconList.clear();
    }
}
