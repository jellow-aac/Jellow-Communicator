package com.dsource.idc.jellow.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.dsource.idc.jellow.R;
import com.dsource.idc.jellow.SessionManager;

/**
 * Created by ekalpa on 5/11/2017.
 */
public class AppPreferences {
    private Context mContext;
    private SharedPreferences mPreferences;
    private SessionManager mSession;
    private SharedPreferences.Editor mEditor;

    public AppPreferences(Context context){
        this.mContext = context;
        this.mSession = new SessionManager(mContext);
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

    public void setShadowRadiusAndBorderWidth(int shadowRadius, int borderWidth) {
        storePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.shadow_radius), shadowRadius);
        storePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.border_width), borderWidth);
    }

    public String getShadowRadiusAndBorderWidth(){
        String strSrBw = String.valueOf(retrievePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.shadow_radius)));
        strSrBw += "," + String.valueOf(retrievePreferenceKeyWithValue(Integer.class.toString(), mContext.getString(R.string.border_width)));
        return strSrBw;
    }

    public void setPeoplePreferences(String peoplePreferences, int langEng) {
        if(mSession.getLanguage() == langEng)
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_eng), peoplePreferences);
        else
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_hindi), peoplePreferences);
    }

    public String getPeoplePreferences(int langEng) {
        if(mSession.getLanguage() == langEng)
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_eng));
        else
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_hindi));
    }

    public void setPlacesPreferences(String placesPreferences, int langEng) {
        if(mSession.getLanguage() == langEng)
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_eng), placesPreferences);
        else
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_hindi), placesPreferences);
    }

    public String getPlacesPreferences(int langEng) {
        if(mSession.getLanguage() == langEng)
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_eng));
        else
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_hindi));
    }

    public void resetUserPeoplePlacesPreferences(){

    }

    private void storePreferenceKeyWithValue(String classType, String key, Object val){
        if (classType.equals(Integer.class.toString()))
            mEditor.putInt(key, (Integer) val).commit();
        else if(classType.equals(Float.class.toString()))
            mEditor.putFloat(key, (Float) val).commit();
        else if(classType.equals(Boolean.class.toString()))
            mEditor.putBoolean(key, (Boolean) val).commit();
        else if(classType.equals(String.class.toString()))
            mEditor.putString(key, (String) val).commit();
    }

    private Object retrievePreferenceKeyWithValue(String classType, String key){
        Object valueOfKey = null;
        if(classType.equals(Integer.class.toString()))
            valueOfKey = mPreferences.getInt(key, -1);
        else if(classType.equals(Float.class.toString()))
            valueOfKey = mPreferences.getFloat(key, 0.0f);
        else if(classType.equals(Boolean.class.toString()))
            valueOfKey = mPreferences.getBoolean(key, false);
        else if(classType.equals(String.class.toString()))
            valueOfKey = mPreferences.getString(key, "");
        return valueOfKey;
    }
}
