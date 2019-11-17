package com.dsource.idc.jellowintl.models;

import androidx.annotation.Keep;

import com.dsource.idc.jellowintl.Presentor.AbstractDataProvider;

import java.io.Serializable;

public class JellowIcon extends AbstractDataProvider.Data implements Serializable{
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
     * */
    @Keep
    private int iconType= GlobalConstants.NORMAL_TYPE;

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        this.iconDrawable = iconDrawable;
        this.verbiageId = iconDrawable;
        this.iconSpeech = iconTitle;
        this.iconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }
    public JellowIcon(String iconTitle,String speechText, String iconDrawable, int p1, int p2, int p3) {
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

    public boolean isEqual(JellowIcon icon){
        return icon.getIconDrawable().equals(iconDrawable);
    }

    public boolean isCustomIcon() {
        return parent0==-1&&verbiageId.length()<12;
    }

    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }

    @Override
    public long getId() {
        int numA=10;
        int numB=50;
        return  (numA*parent0+parent1)*numB+parent2;
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
        this.iconDrawable=iconDrawable;
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
        return iconType==GlobalConstants.CATEGORY_TYPE;
    }

    public void setType(int iconType) {
        this.iconType =iconType;
    }

    public boolean isSequenceIcon() {
        return isSequenceIcon;
    }

    public void setSequenceIcon(boolean sequenceIcon) {
        isSequenceIcon = sequenceIcon;
    }
}