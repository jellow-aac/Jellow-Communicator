package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.IconDatabase;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class BoardSearchAdapter extends BaseRecyclerAdapter<JellowIcon> {

    private IconDatabase iconDatabase;

    /**
     * public constructor
     *
     * @param mContext context
     * @param items items
     *
     */
    public BoardSearchAdapter(Context mContext, ArrayList<JellowIcon> items, String langCode, AppDatabase appDatabase) {
        super(mContext, R.layout.icon_search_list_item, items);
        if (!langCode.equals(""))
            iconDatabase = new IconDatabase(langCode, appDatabase);
    }


    @Override
    public void bindData(BaseViewHolder viewHolder, JellowIcon icon, int position) {


        viewHolder.setVisible(R.id.speak_button,false);

        //If the "No icon found" condition comes the remove speakButton
        if (icon.getIconDrawable().equals("NULL") || icon.getIconTitle().equals(getContext().getResources().getString(R.string.not_found))) {
            viewHolder.setText(R.id.search_icon_title, getContext().getResources().getString(R.string.icon_not_found));
            if (icon.getIconTitle().equals(getContext().getResources().getString(R.string.board_not_found)))
                viewHolder.setText(R.id.search_icon_title, getContext().getResources().getString(R.string.board_not_found));
            viewHolder.setVisible(R.id.parent_directory, false);
            viewHolder.setImageDrawable(R.id.search_icon_drawable, getContext().getResources().getDrawable(R.drawable.ic_icon_not_found));
            return;
        }


        viewHolder.setVisible(R.id.parent_directory, true);

        viewHolder.setText(R.id.search_icon_title, icon.getIconTitle());

        if ((icon.getParent0() == -1)) {
            viewHolder.setImageFromBoard(R.id.search_icon_drawable, icon.getIconDrawable());
            viewHolder.setBackgroundDrawable(R.id.search_icon_drawable, getContext().getResources().getDrawable(R.drawable.icon_back_grey));
        } else
            viewHolder.setImageFromLibrary(R.id.search_icon_drawable, icon.getIconDrawable());


        /*
         * Adding the directory hint in the search list item
         * To this, we are using the function same as in menuSelected path used
         * */

        String dir = "";
        try {
            String[] arr = getLevel1IconLabels();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].split("…")[0];
            }

            if (icon.getLevelOne() == -1 && icon.getLevelTwo() == -1) {
                dir = getContext().getResources().getString(R.string.custom_icon);
            } else if (icon.getLevelTwo() == -1) {
                dir = getContext().getResources().getString(R.string.home);
            } else if (icon.getLevelThree() == -1) {
                dir = arr[icon.getParent0()];
            } else {
                String levelTitle = getIconTitleLevel2(icon.getParent0())[icon.getParent1()].
                        replace("…", "");
                dir = arr[icon.getParent0()] + "->" + levelTitle;

            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            ignored.printStackTrace();
        }
        viewHolder.setText(R.id.parent_directory, dir);

    }

    /**
     * A function to return array of Titles of level 2
     */
    private String[] getIconTitleLevel2(int pos) {
        if (iconDatabase == null) return new String[]{""};
        ArrayList<String> list = iconDatabase.getLevelTwoIconsTitles(pos);
        if (list != null && list.size() > 0)
            list.remove(0);
        assert list != null;
        return list.toArray(new String[list.size()]);
    }

    @NonNull
    private String[] getLevel1IconLabels() {
        if (iconDatabase == null) return new String[]{""};
        ArrayList<String> list = iconDatabase.getLevelOneIconsTitles();
        return list.toArray(new String[list.size()]);
    }


}
