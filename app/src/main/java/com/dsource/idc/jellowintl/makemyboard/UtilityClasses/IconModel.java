package com.dsource.idc.jellowintl.makemyboard.UtilityClasses;

import android.content.Context;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.verbiage_model.VerbiageDatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

public class IconModel implements Serializable{

    ;
    private JellowIcon icon;
    private JellowVerbiageModel verbiage;
    private ArrayList<IconModel> children;

    public IconModel(JellowIcon icon,JellowVerbiageModel verbiage)
    {
            this.icon=icon;
            this.verbiage=verbiage;
            //this.verbiage=new VerbiageDatabaseHelper(context,new DataBaseHelper(context).getWritableDatabase()).getVerbiageById(getIconName(icon));
            children=new ArrayList<>();
    }


    public void addChild(JellowIcon childIcon,JellowVerbiageModel verbiage)
    {
            children.add(new IconModel(childIcon,verbiage));
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
    public void addAllChild(ArrayList<JellowIcon> subList, ArrayList<JellowVerbiageModel> verbiageModels) {
            for(int i=0;i<subList.size();i++)
                children.add(new IconModel(subList.get(i),verbiageModels.get(i)));
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
    public JellowVerbiageModel getVerbiage() {
        return verbiage;
    }

    public void setVerbiage(JellowVerbiageModel verbiage) {
        this.verbiage = verbiage;
    }

    public void addChild(IconModel newIconModel) {
        children.add(newIconModel);
    }

}
