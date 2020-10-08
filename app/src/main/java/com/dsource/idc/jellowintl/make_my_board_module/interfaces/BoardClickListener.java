package com.dsource.idc.jellowintl.make_my_board_module.interfaces;

public interface BoardClickListener {

    void onItemClick(int Position);

    void onItemDelete(int position);

    void onBoardEdit(int position);
}