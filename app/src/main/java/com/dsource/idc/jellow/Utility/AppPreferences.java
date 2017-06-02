package com.dsource.idc.jellow.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.dsource.idc.jellow.R;


/**
 * Created by ekalpa on 5/11/2017.
 */

public class AppPreferences {
    private Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public AppPreferences(Context context){
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void setScreenWidth(float dpWidth) {
        storePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_width), dpWidth);
    }

    public float getScreenWidth(){
        return (float) retrievePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_width));
    }

    public void setScreenHeight(float dpHeight) {
        storePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_height), dpHeight);
    }

    public float getScreenHeight(){
        return (float) retrievePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_height));
    }
/*
    public void setMyIntegerValue(int value){
        storePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.my_integer_class), value);
    }

    public int getMyIntegerValue(){
        return (int) retrievePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.my_integer_class));
    }

    public void setMyBooleanValue(boolean value){
        storePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.my_boolean_class), value);
    }

    public Boolean getMyBooleanValue(){
        return (Boolean) retrievePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.my_boolean_class));
    }

    public void setMyStringValue(String value){
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.my_string_class), value);
    }

    public String getMyStringValue(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.my_string_class));
    }

    public void setDroppedItemData(String value) {
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.gridview_drop_item), value);
    }

    public String getDroppedItemData() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.gridview_drop_item));
    }
*/

    private void storePreferenceKeyWithValue(String classType, String key, Object val){
        if (classType.equals(Integer.class.toString())) {
            mEditor.putInt(key, (Integer) val).commit();
        }else if(classType.equals(Float.class.toString())) {
            mEditor.putFloat(key, (Float) val).commit();
        }else if(classType.equals(Boolean.class.toString())) {
            mEditor.putBoolean(key, (Boolean) val).commit();
        }else if(classType.equals(String.class.toString())) {
            mEditor.putString(key, (String) val).commit();
        }
    }

    private Object retrievePreferenceKeyWithValue(String classType, String key){
        Object valueofKey = null;
        if(classType.equals(Integer.class.toString())){
            valueofKey = mPreferences.getInt(key, -1);
        }else if(classType.equals(Float.class.toString())){
            valueofKey = mPreferences.getFloat(key, 0.0f);
        }else if(classType.equals(Boolean.class.toString())) {
            valueofKey = mPreferences.getBoolean(key, false);
        } if(classType.equals(String.class.toString())) {
            valueofKey = mPreferences.getString(key, "");
        }
        return valueofKey;
    }
}
