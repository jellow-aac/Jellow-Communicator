package com.dsource.idc.jellowintl.makemyboard.addeditmodule;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.adapters.IconSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class IconManager {
    private RecyclerView recyclerView;
    private IconSelectorAdapter adapter;
    private ArrayList<JellowIcon> iconList;
    private int mode = BoardConstants.EDIT_ICON_MODE;

    public IconManager(Context context){

    }

}
