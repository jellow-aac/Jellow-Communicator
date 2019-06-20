package com.dsource.idc.jellowintl.makemyboard.models;

import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.models.JellowIcon;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class IconModelTest {

    @Test
    public void iconModelTest(){
        //Init model class with default constructor
        JellowIcon jellowIcon = new JellowIcon("Apple", "0103050001GG", 3, 5, 1);
        IconModel iconModel = new IconModel(jellowIcon);
        //check default setup with default constructor
        assert  iconModel.getIcon().equals(jellowIcon);

        iconModel.addChild(jellowIcon);

        //checking getter and setter oj JellowIcon variable "icon"
        jellowIcon = new JellowIcon("Banana", "0103050002GG", 3, 5, 2);
        iconModel.setIcon(jellowIcon);
        assert iconModel.getIcon().equals(jellowIcon);

        //filling "children" var with 2nd JellowIcon
        iconModel.addChild(jellowIcon);

        //filling sublist
        ArrayList<JellowIcon> list = new ArrayList<>();
        jellowIcon = new JellowIcon("Grapes", "0103050003GG", 3, 5, 3);
        list.add(jellowIcon);
        jellowIcon = new JellowIcon("Guava", "0103050004GG", 3, 5, 4);
        list.add(jellowIcon);
        iconModel.addAllChild(list);
        //checking methods hasChild, getChildren, getSubList & getAllIcons
        assert iconModel.hasChild() &&
            iconModel.getChildren().size() != 0 &&
            iconModel.getSubList().size() == 4 &&
            iconModel.getAllIcons().size() != 0;
    }
}
