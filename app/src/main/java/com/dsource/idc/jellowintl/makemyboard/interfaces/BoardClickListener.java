package com.dsource.idc.jellowintl.makemyboard.interfaces;

public interface BoardClickListener {

    void onItemClick(int Position);

    void onItemDelete(int position);

    void onBoardEdit(int position);
}