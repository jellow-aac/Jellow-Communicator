package com.dsource.idc.jellow;

/**
 * Created by ekalpa on 23-Jun-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_LOGGEDIN1 = "isLoggedIn1";
    public static final String Name = "name";
    public static final String EmergencyContact = "number";
    public static final String Blood = "blood" ;
    public static final String Father_name = "father";
    public static final String Address = "address";
    public static final String EmailId = "emailid";
    public static final String Language = "lang";
    public static final String Speed = "speechspeed";
    public static final String Pitch = "voicepitch";
    public static final String Keyboard = "Keyboard";
    public static final String PictureViewMode = "PictureViewMode";
    public static final String GridSize = "GridSize";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    //another login for swipe
    public void setLogin1(boolean isLoggedIn1) {
        editor.putBoolean(KEY_IS_LOGGEDIN1, isLoggedIn1);
        // commit changes
        editor.commit();
        Log.d(TAG, "1User login session modified!");
    }

    public boolean isLoggedIn1(){
        return pref.getBoolean(KEY_IS_LOGGEDIN1, false);
    }


    public void setName(String name) {
        editor.putString(Name, name);
        // commit changes
        editor.commit();
        Log.d(TAG, "user details saved!");
    }

    public void setFather_no(String father_no) {
        editor.putString(EmergencyContact, father_no);
        // commit changes
        editor.commit();
        Log.d(TAG, "user details saved!");
    }

    public void setEmailId(String emailId) {
        editor.putString(EmailId, emailId);
        // commit changes
        editor.commit();
        Log.d(TAG, "user details saved!");
    }

    public void setBlood(int bg) {
        editor.putInt(Blood, bg);
        // commit changes
        editor.commit();
        Log.d(TAG, "user details saved!");
    }

    public int getBlood(){
        return pref.getInt(Blood, 0);
    }

    public String getName() {
        return pref.getString(Name, "");

    }
    public String getFather_no() {
         return  pref.getString(EmergencyContact, "");
    }

    public String getEmailId() {
        return  pref.getString(EmailId, "");
    }

    public void setFather_name(String fname){
        editor.putString(Father_name, fname);
        editor.commit();
    }

    public String getFather_name(){
        return pref.getString(Father_name, "");
    }

      public void setAddress(String address){
        editor.putString(Address, address);
        editor.commit();
    }

    public String getAddress(){
        return pref.getString(Address, "");
    }

    public void setLanguage(int lang){
        editor.putInt(Language, lang);
        editor.commit();
    }

    public int getLanguage(){
        return pref.getInt(Language, 0);
    }

   /* public void setAccent(int acc){
        editor.putInt(Accent, acc);
        editor.commit();
    }

    public int getAccent(){
        return pref.getInt(Accent, 0);
    }*/

    public void setPictureViewMode(int key){
        editor.putInt(PictureViewMode, key);
        editor.commit();
    }

    public int getPictureViewMode(){
        return pref.getInt(PictureViewMode, 0);
    }

    public void setGridSize(int key){
        editor.putInt(GridSize, key);
        editor.commit();
    }

    public int getGridSize(){
        return pref.getInt(GridSize, 0);
    }


    public void setSpeed(int speed){
        editor.putInt(Speed, speed);
        editor.commit();
    }

    public int getSpeed(){
        return pref.getInt(Speed, 40);
    }

    public void setPitch(int pitch){
        editor.putInt(Pitch, pitch);
        editor.commit();
    }

    public int getPitch(){
        return pref.getInt(Pitch, 50);
    }
}
