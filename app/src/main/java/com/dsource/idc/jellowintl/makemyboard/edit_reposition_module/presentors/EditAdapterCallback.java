package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.presentors;

public interface EditAdapterCallback {
    void onIconClicked(int adapterPosition);
    void onIconEdit(int adapterPosition);
    void onIconRemove(int adapterPosition);
}
