package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VerbiageModel {

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String iconId;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "event_tag")
    private String eventTag;

    @ColumnInfo(name = "Search_Tag")
    private String searchTag;

    @ColumnInfo(name = "language_code")
    private String languageCode;

    @ColumnInfo(name = "title")
    private String title;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String icon_id) {
        this.iconId = icon_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }
}
