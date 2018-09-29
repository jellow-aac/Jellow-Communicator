package com.dsource.idc.jellowboard.makemyboard.UtilityClasses;

import android.support.annotation.Keep;

import com.dsource.idc.jellowboard.utility.JellowIcon;

import java.io.Serializable;
import java.util.ArrayList;

public class IconModel implements Serializable{

        @Keep public JellowIcon icon;
        @Keep public ArrayList<IconModel> children;
        @Keep public IconModel(JellowIcon icon)
        {
            this.icon=icon;
            children=new ArrayList<>();
        }
        @Keep public void addChild(JellowIcon childIcon)
        {
            children.add(new IconModel(childIcon));
        }
        @Keep public JellowIcon getIcon()
        {
            return icon;
        }

        @Keep public void setIcon(JellowIcon icon) {
        this.icon = icon;
    }

        @Keep public ArrayList<IconModel> getChildren()
        {
            return children;
        }

        @Keep public boolean hasChild()
        {
            return children.size() > 0;
        }

        @Keep public void addAllChild(ArrayList<JellowIcon> subList)
        {
            for(int i=0;i<subList.size();i++)
                children.add(new IconModel(subList.get(i)));
        }

        @Keep public ArrayList<JellowIcon> getSubList()
        {
            ArrayList<JellowIcon> list=new ArrayList<>();
            for(int i=0;i<children.size();i++)
            {
                list.add(children.get(i).getIcon());
            }

            return list;
        }
}
