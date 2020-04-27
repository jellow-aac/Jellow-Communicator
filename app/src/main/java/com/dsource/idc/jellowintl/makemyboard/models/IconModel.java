package com.dsource.idc.jellowintl.makemyboard.models;

import androidx.annotation.Keep;

import com.dsource.idc.jellowintl.makemyboard.utility.MyPair;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.io.Serializable;
import java.util.ArrayList;

public class IconModel implements Serializable {

    @Keep
    public JellowIcon icon;

    @Keep
    private ArrayList<IconModel> children;

    @Keep
    public IconModel(JellowIcon icon) {
        this.icon = icon;
        children = new ArrayList<>();
    }

    @Keep
    public void addChild(JellowIcon childIcon) {
        children.add(new IconModel(childIcon));
    }

    @Keep
    public JellowIcon getIcon() {
        return icon;
    }

    @Keep
    public void setIcon(JellowIcon icon) {
        this.icon = icon;
    }

    @Keep
    public ArrayList<IconModel> getChildren() {
        return children;
    }

    @Keep
    public boolean hasChild() {
        return children.size() > 0;
    }

    @Keep
    public void addAllChild(ArrayList<JellowIcon> subList) {
        for (int i = 0; i < subList.size(); i++)
            children.add(new IconModel(subList.get(i)));
    }

    @Keep
    public ArrayList<JellowIcon> getSubList() {
        ArrayList<JellowIcon> list = new ArrayList<>();

        for (int i = 0; i < children.size(); i++)
            list.add(children.get(i).getIcon());

        return list;
    }

    @Keep
    public ArrayList<JellowIcon> getAllIcons() {
        ArrayList<JellowIcon> list = new ArrayList<>();
        for (int i = 0; i < children.size(); i++)
            list.add(children.get(i).getIcon());

        for (int i = 0; i < children.size(); i++)
            for (int j = 0; j < children.get(i).getChildren().size(); j++)
                list.add(children.get(i).getChildren().get(j).getIcon());


        for (int i = 0; i < children.size(); i++) {

            IconModel levelTwo = children.get(i);

            for (int j = 0; j < levelTwo.getChildren().size(); j++) {

                IconModel levelThree = levelTwo.getChildren().get(j);

                for (int k = 0; k < levelThree.getChildren().size(); k++) {
                    list.add(levelThree.getChildren().get(k).getIcon());
                }

            }
        }

        return list;
    }

    @Keep
    public void appendNewModelToPrevious(IconModel newIconModel) {
        children.addAll(newIconModel.getChildren());
    }

    public void removeIcon(int levelOneParent, int position) {
        if (levelOneParent == -1)
            children.remove(position);
        else children.get(levelOneParent).children.remove(position);
    }

    public MyPair<Integer, Integer> getIconPosition(JellowIcon icon) {
        MyPair<Integer, Integer> positionPair = new MyPair<>(-1, -1);
        for (int i = 0; i < getChildren().size(); i++)
            if (getChildren().get(i).getIcon().isEqual(icon)) {
                positionPair.setFirst(i);
                positionPair.setSecond(-1);
                return positionPair;
            }

        for (int i = 0; i < getChildren().size(); i++) {
            IconModel levelTwo = getChildren().get(i);
            for (int j = 0; j < levelTwo.getChildren().size(); j++)
                if (levelTwo.getChildren().get(j).getIcon().isEqual(icon)) {
                    positionPair.setFirst(i);
                    positionPair.setSecond(j);
                    return positionPair;
                }

        }

        return positionPair;
    }

    public void move(int to, int from) {
        children.add(to,children.remove(from));
    }
}
