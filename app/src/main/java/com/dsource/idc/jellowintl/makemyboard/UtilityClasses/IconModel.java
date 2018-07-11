package com.dsource.idc.jellowintl.makemyboard.UtilityClasses;

import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.io.Serializable;
import java.util.ArrayList;

public class IconModel implements Serializable{

        private JellowIcon icon;
        private ArrayList<IconModel> children;

        public IconModel(JellowIcon icon)
        {
            this.icon=icon;
            children=new ArrayList<>();
        }

        public void addChild(JellowIcon childIcon)
        {
            children.add(new IconModel(childIcon));
        }

        public JellowIcon getIcon()
        {
            return icon;
        }
        public ArrayList<IconModel> getChildren()
        {
            return children;
        }
        public boolean hasChild()
        {
            return children.size() > 0;
        }

        public void addAllChild(ArrayList<JellowIcon> subList)
        {
            for(int i=0;i<subList.size();i++)
                children.add(new IconModel(subList.get(i)));
        }
        public ArrayList<JellowIcon> getSubList()
        {
            ArrayList<JellowIcon> list=new ArrayList<>();
            for(int i=0;i<children.size();i++)
            {
                list.add(children.get(i).getIcon());
            }

            return list;
        }


    public void addChild(IconModel newIconModel) {
        children.add(newIconModel);
    }
}
