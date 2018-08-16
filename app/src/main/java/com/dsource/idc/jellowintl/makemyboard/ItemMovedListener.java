package com.dsource.idc.jellowintl.makemyboard;

public interface ItemMovedListener {
    void onItemMovedIntoCategory(int from,int to);
    void onItemDraggedOutOfCategory(int from);
    void onItemDropped(int mode);
}
