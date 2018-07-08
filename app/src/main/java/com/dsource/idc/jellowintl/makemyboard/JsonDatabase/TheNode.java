package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class TheNode {

        private JellowIcon icon;
        private ArrayList<com.dsource.idc.jellowintl.makemyboard.JsonDatabase.TheNode> children;

        public TheNode(JellowIcon icon)
        {
            this.icon=icon;
            children=new ArrayList<>();
        }

        public void addChild(JellowIcon childIcon)
        {
            children.add(new com.dsource.idc.jellowintl.makemyboard.JsonDatabase.TheNode(childIcon));
        }

        public JellowIcon getIcon()
        {
            return icon;
        }
        public ArrayList<com.dsource.idc.jellowintl.makemyboard.JsonDatabase.TheNode> getChildren()
        {
            return children;
        }
        public boolean hasChild()
        {
            return children.size() > 0;
        }



}
