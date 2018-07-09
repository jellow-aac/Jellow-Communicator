package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.io.Serializable;
import java.util.ArrayList;

public class TheNode implements Serializable{

        private JellowIcon icon;
        private ArrayList<TheNode> children;

        public TheNode(JellowIcon icon)
        {
            this.icon=icon;
            children=new ArrayList<>();
        }

        public void addChild(JellowIcon childIcon)
        {
            children.add(new TheNode(childIcon));
        }

        public JellowIcon getIcon()
        {
            return icon;
        }
        public ArrayList<TheNode> getChildren()
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
                children.add(new TheNode(subList.get(i)));
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




}
