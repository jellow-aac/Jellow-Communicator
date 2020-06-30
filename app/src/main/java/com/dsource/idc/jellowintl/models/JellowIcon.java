package com.dsource.idc.jellowintl.models;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.dsource.idc.jellowintl.make_my_board_module.interfaces.AbstractDataProvider;
import com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants;

import java.io.Serializable;

public class JellowIcon extends AbstractDataProvider.Data implements Serializable, Comparable<JellowIcon> {
    @Keep
    private String verbiageId, iconDrawable, iconTitle, iconSpeech;
    @Keep
    private int parent0, parent1, parent2;
    @Keep
    private boolean isSequenceIcon;

    /**
     * @iconType stores the type of the icon
     * Normal Icon : BoardConstants.NORMAL_TYPE
     * Category Icon : BoardConstants.CATEGORY_TYPE
     */
    @Keep
    private int iconType = BoardConstants.NORMAL_TYPE;

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        this.iconDrawable = iconDrawable;
        this.verbiageId = iconDrawable;
        this.iconSpeech = iconTitle;
        this.iconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }

    public JellowIcon(String iconTitle, String speechText, String iconDrawable, int p1, int p2, int p3) {
        this.iconDrawable = iconDrawable;
        this.iconTitle = iconTitle;
        iconSpeech = speechText;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }

    public JellowIcon(String verbiageID,String IconTitle,String IconSpeech,String drawable){
        this.iconDrawable = drawable;
        this.iconTitle =IconTitle;
        this.iconSpeech = IconSpeech;
        this.verbiageId=verbiageID;
        try {
            parent0 = Integer.parseInt(verbiageID.substring(2,4))-1;
            parent1 = Integer.parseInt(verbiageID.substring(4,6))-1;
            parent2 = Integer.parseInt(verbiageID.substring(6,10))-1;
            isSequenceIcon = verbiageID.contains("SS") && parent2 != -1;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getIconDrawable() {
        return iconDrawable;
    }

    public String getIconTitle() {
        return iconTitle;
    }

    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }

    public String getIconSpeech() {
        return iconSpeech;
    }

    public void setIconSpeech(String iconSpeech) {
        this.iconSpeech = iconSpeech;
    }

    public int getParent0() {
        return parent0;
    }

    public int getParent1() {
        return parent1;
    }

    public int getParent2() {
        return parent2;
    }

    public boolean isEqual(JellowIcon icon) {
        return icon.getIconDrawable().equals(iconDrawable);
    }

    public boolean isCustomIcon() {
        return parent0 == -1 && verbiageId.length() < 12;
    }

    @Override
    public long getId() {
        int numA = 11;
        int numB = 98;
        return (numA * parent0 + parent1) * numB + parent2;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public String getText() {
        return iconTitle;
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @Override
    public void setPinned(boolean pinned) {

    }

    public String getVerbiageId() {
        return verbiageId;
    }

    public void setVerbiageId(String verbiageId) {
        this.verbiageId = verbiageId;
    }

    public void setDrawable(String iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public int getLevelOne() {
        return parent0;
    }

    public int getLevelTwo() {
        return parent1;
    }

    public int getLevelThree() {
        return parent2;
    }

    public boolean isCategory() {
        return iconType == BoardConstants.CATEGORY_TYPE;
    }

    public void setType(int iconType) {
        this.iconType = iconType;
    }

    public boolean isSequenceIcon() {
        return isSequenceIcon;
    }

    public void setSequenceIcon(boolean sequenceIcon) {
        isSequenceIcon = sequenceIcon;
    }

    @Override
    public int compareTo(JellowIcon o) {
        return this.getVerbiageId().compareTo(o.getVerbiageId());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof JellowIcon)
        return verbiageId.equals(((JellowIcon)obj).getVerbiageId());
        return false;
    }
}
