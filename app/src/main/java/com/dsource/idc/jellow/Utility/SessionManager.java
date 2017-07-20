package com.dsource.idc.jellow.Utility;

/**
 * Created by ekalpa on 23-Jun-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dsource.idc.jellow.R;

public class SessionManager {
    private final int LANG_ENG = 0;
    private SharedPreferences mPreferences;
    private Editor mEditor;
    private Context mContext;

    private final String PREF_NAME = "AndroidHiveLogin";
    private final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private final String KEY_IS_LOGGEDIN1 = "isLoggedIn1";
    public final String Name = "name";
    private final String EmergencyContact = "number";
    private final String Blood = "blood" ;
    private final String Father_name = "father";
    private final String Address = "address";
    private final String EmailId = "emailid";
    private final String Language = "lang";
    private final String Speed = "speechspeed";
    private final String Pitch = "voicepitch";
    private final String Keyboard = "Keyboard";
    private final String PictureViewMode = "PictureViewMode";
    private final String GridSize = "GridSize";

    public SessionManager(Context context) {
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        storePreferenceKeyWithValue(Boolean.class.toString(), KEY_IS_LOGGEDIN, isLoggedIn);
    }

    public boolean isUserLoggedIn(){
        return (Boolean) retrievePreferenceKeyWithValue(Boolean.class.toString(), KEY_IS_LOGGEDIN);
    }

    public void setBlood(int bloodGroup) {
        storePreferenceKeyWithValue(Integer.class.toString(), Blood, bloodGroup);
    }

    public int getBlood(){
        return (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Blood);
    }

    public void setName(String name) {
        storePreferenceKeyWithValue(String.class.toString(), Name, name);
    }

    public String getName() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Name);
    }

    public void setEmailId(String emailId) {
        storePreferenceKeyWithValue(String.class.toString(), EmailId, emailId);
    }

    public String getEmailId() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), EmailId);
    }

    public void setFather_no(String father_no) {
        storePreferenceKeyWithValue(String.class.toString(), EmergencyContact, father_no);
    }

    public String getFather_no() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), EmergencyContact);
    }

    public void setFather_name(String fname){
        storePreferenceKeyWithValue(String.class.toString(), Father_name, fname);
    }

    public String getFather_name(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Father_name);
    }

      public void setAddress(String address){
        storePreferenceKeyWithValue(String.class.toString(), Address, address);
    }

    public String getAddress(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Address);
    }

    public void setLanguage(int lang){
        storePreferenceKeyWithValue(Integer.class.toString(), Language, lang);
    }

    public int getLanguage(){
        return (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Language);
    }

    public void setPictureViewMode(int pictureViewMode){
        storePreferenceKeyWithValue(Integer.class.toString(), PictureViewMode, pictureViewMode);
    }

    public int getPictureViewMode(){
        return (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), PictureViewMode);
    }

    public void setGridSize(int gridSize){
        storePreferenceKeyWithValue(Integer.class.toString(), GridSize, gridSize);
    }

    public int getGridSize(){
        return (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), GridSize);
    }

    public void setSpeed(int speed){
        storePreferenceKeyWithValue(Integer.class.toString(), Speed, speed);
    }

    public int getSpeed(){
        int speed = (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Speed);
        if(speed == 0)
            return 100;              //100 is default value for speed of speech to keep.
        return  speed;
    }

    public void setPitch(int pitch){
        storePreferenceKeyWithValue(Integer.class.toString(), Pitch, pitch);
    }

    public int getPitch(){
        int pitch = (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Pitch);
        if(pitch == 0)
            return 100;              //100 is default value for pitch of speech to keep.
        return pitch;
    }

    public void setScreenWidth(float dpWidth) {
        storePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_width), dpWidth);
    }

    public float getScreenWidth(){
        return (Float) retrievePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_width));
    }

    public void setScreenHeight(float dpHeight) {
        storePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_height), dpHeight);
    }

    public float getScreenHeight(){
        return (Float) retrievePreferenceKeyWithValue(Float.class.toString(), mContext.getString(R.string.screen_height));
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

    public void setPeoplePreferences(String peoplePreferences) {
        if(getLanguage() == LANG_ENG)
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_eng), peoplePreferences);
        else
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_hindi), peoplePreferences);
    }

    public String getPeoplePreferences() {
        if(getLanguage() == LANG_ENG)
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_eng));
        else
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_hindi));
    }

    public void setPlacesPreferences(String placesPreferences) {
            storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_eng), placesPreferences);
    }

    public String getPlacesPreferences() {
            return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_eng));
    }

    public void resetUserPeoplePlacesPreferences(){
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_eng), "");
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.people_pref_count_hindi), "");
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.places_pref_count_eng), "");
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
            valueOfKey = mPreferences.getInt(key, 0);
        else if(classType.equals(Float.class.toString()))
            valueOfKey = mPreferences.getFloat(key, 0.0f);
        else if(classType.equals(Boolean.class.toString()))
            valueOfKey = mPreferences.getBoolean(key, false);
        else if(classType.equals(String.class.toString()))
            valueOfKey = mPreferences.getString(key, "");
        return valueOfKey;
    }
}