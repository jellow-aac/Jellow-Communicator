package com.dsource.idc.jellowintl.utility;

/**
 * Created by ekalpa on 23-Jun-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.GlobalConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    public final static String ENG_US = "en-rUS";
    public final static String ENG_UK = "en-rGB";
    public final static String ENG_IN = "en-rIN";
    public final static String ENG_AU = "en-rAU";
    public final static String HI_IN = "hi-rIN";
    public final static String BN_IN = "bn-rIN";    // BN_IN -> Bengali
    public final static String BE_IN = "be-rIN";    // BE_IN -> Bengali (for some old API devices which return Bengali locale as be-rIN)
    public final static String MR_IN = "mr-rIN";
    public final static String ES_ES = "es-rES";
    public final static String TA_IN = "ta-rIN";
    public final static String DE_DE = "de-rDE";
    public final static String FR_FR = "fr-rFR";
    public static final String UNIVERSAL_PACKAGE = "universal";
    public final static String BOARD_ICON_LOCATION = "board_icons";

    public final static HashMap<String,String> LangMap = new HashMap<String,String>(){
        {
            put("German (Deutschland)", DE_DE);
            put("English (Australia)", ENG_AU);
            put("English (India)", ENG_IN);
            put("English (United Kingdom)", ENG_UK);
            put("English (United States)", ENG_US);
            put ("French (France)", FR_FR);
            put("Spanish (Spain)", ES_ES);
            put("मराठी (Marathi)", MR_IN);
            put("हिंदी (Hindi)", HI_IN);
            put("বাংলা (Bengali)", BN_IN);
            put("தமிழ் (Tamil)", TA_IN);
        }
    };

    public final static HashMap<String,String> LangValueMap = new HashMap<String,String>(){
        {
            put(DE_DE, "German (Deutschland)");
            put(ENG_AU,"English (Australia)");
            put(ENG_IN,"English (India)");
            put(ENG_UK,"English (United Kingdom)");
            put(ENG_US,"English (United States)");
            put(FR_FR, "French (France)");
            put(ES_ES, "Spanish (Spain)");
            put(MR_IN,"मराठी (Marathi)");
            put(HI_IN,"हिंदी (Hindi)");
            put(BN_IN,"বাংলা (Bengali)");
            put(TA_IN, "தமிழ் (Tamil)");
        }
    };

    public final static ArrayList<String> NoTTSLang = new ArrayList<String>(){{
        add(MR_IN);
    }};

    public final static ArrayList<String> NOT_SUPPORTED_API_BELOW_21 = new ArrayList<String>(){{
        add(TA_IN);
    }};

    private final String PREF_NAME = "JellowPreferences";
    private final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private final String Name = "name";
    private final String EmergencyContact = "number";
    private final String Blood = "blood" ;
    private final String Father_name = "father";
    private final String Address = "address";
    private final String EmailId = "emailid";
    private final String Language = "lang";
    private final String Speed = "speechspeed";
    private final String Pitch = "voicepitch";
    private final String PictureViewMode = "PictureViewMode";
    private final String GridSize = "GridSize";
    private final String sessionId = "SessionId";

    private SharedPreferences mPreferences;
    private Editor mEditor;
    private Context mContext;


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

    public void setCaregiverNumber(String father_no) {
        storePreferenceKeyWithValue(String.class.toString(), EmergencyContact, father_no);
    }

    public String getCaregiverNumber() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), EmergencyContact);
    }

    public void setCaregiverName(String fname){
        storePreferenceKeyWithValue(String.class.toString(), Father_name, fname);
    }

    public String getCaregiverName(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Father_name);
    }

    public void setAddress(String address){
        storePreferenceKeyWithValue(String.class.toString(), Address, address);
    }

    public String getAddress(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Address);
    }

    public void setLanguage(String lang){
        storePreferenceKeyWithValue(String.class.toString(), Language, lang);
    }

    public String getLanguage(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), Language);
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

    public boolean isGridSizeKeyExist(){
        return mPreferences.contains(GridSize);
    }

    public void setSpeed(int speed){
        storePreferenceKeyWithValue(Integer.class.toString(), Speed, speed);
    }

    public int getSpeed(){
        int speed = (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Speed);
        if(speed == 0)
            return 50;              //50 is default value for speed of speech to keep.
        return  speed;
    }

    public void setPitch(int pitch){
        storePreferenceKeyWithValue(Integer.class.toString(), Pitch, pitch);
    }

    public int getPitch(){
        int pitch = (Integer) retrievePreferenceKeyWithValue(Integer.class.toString(), Pitch);
        if(pitch == 0)
            return 50;              //50 is default value for pitch of speech to keep.
        return pitch;
    }

    public void setUserCountryCode(String selectedCountryCode) {
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.user_country_code), selectedCountryCode);
    }

    public String getUserCountryCode() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.user_country_code));
    }

    public boolean isCompletedIntro() {
        return (boolean) retrievePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.completed_intro));
    }

    public void setCompletedIntro(boolean value) {
        storePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.completed_intro), value);
    }

    public void setSessionCreatedAt(long timeStamp) {
        storePreferenceKeyWithValue(Long.class.toString(), mContext.getString(R.string.last_session_created), timeStamp);
    }

    public Long getSessionCreatedAt() {
        return ((Long) retrievePreferenceKeyWithValue(Long.class.toString(), mContext.getString(R.string.last_session_created)));
    }

    public void setEnableCalling(boolean enableCalling) {
        storePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.enable_calling), enableCalling);
    }

    public boolean isCallingEnabled() {
        return ((Boolean) retrievePreferenceKeyWithValue(Boolean.class.toString(), mContext.getString(R.string.enable_calling)));
    }

    public void setToastMessage(String message) {
        storePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.string_msg), message);
    }

    public String getToastMessage(){
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), mContext.getString(R.string.string_msg));
    }

    public void setLastCrashReported(long timeStamp) {
        storePreferenceKeyWithValue(Long.class.toString(), mContext.getString(R.string.last_crash_reported), timeStamp);
    }

    public Long getLastCrashReported() {
        return ((Long) retrievePreferenceKeyWithValue(Long.class.toString(), mContext.getString(R.string.last_crash_reported)));
    }

    public String getUserId() {
        return (String) retrievePreferenceKeyWithValue(String.class.toString(), sessionId);
    }
    public void setUserId(String uuid) {
        storePreferenceKeyWithValue(String.class.toString(), sessionId, uuid);
    }

    /**
     * Created by Ayaz
     **
     * Updated by Rahul on 30th June 2020*
     **/
    public void setLanguageDataUpdateState(String langCode, int state){
        storePreferenceKeyWithValue(Integer.class.toString(), langCode, state);
    }
    public int getLanguageDataUpdateState(String langCode){
        try{
            return (!checkPreferenceExist(langCode)) ?
                    GlobalConstants.LANGUAGE_STATE_CREATE_DB :
                    (Integer)retrievePreferenceKeyWithValue(Integer.class.toString(), langCode);
        }catch (ClassCastException e){
            return GlobalConstants.LANGUAGE_STATE_CREATE_DB;
        }
    }

    private boolean checkPreferenceExist(String langCode) {
        return mPreferences.contains(langCode);
    }

    /**
     * Ayaz
     **/
    public void setBoardDatabaseStatus(boolean set,String language)
    {
        String Tag="Board_Database"+language;
        if(set)
            storePreferenceKeyWithValue(String.class.toString(),Tag,"Yes");
        else
            storePreferenceKeyWithValue(String.class.toString(),Tag,"No");
    }

    public String getCurrentBoardLanguage() {
        String Tag = "cur_board_lang";
        return retrievePreferenceKeyWithValue(String.class.toString(),Tag).toString();
    }

    public void setCurrentBoardLanguage(String language){
        String Tag = "cur_board_lang";
        storePreferenceKeyWithValue(String.class.toString(),Tag,language);
    }

    public void changePreferencesFile(Context context){
        SharedPreferences settingsOld = context.getSharedPreferences("AndroidHiveLogin", Context.MODE_PRIVATE);
        settingsOld.edit().remove(mContext.getString(R.string.perform_db_update)).apply();
        SharedPreferences settingsNew = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNew = settingsNew.edit();
        Map<String, ?> all = settingsOld.getAll();
        for (Map.Entry<String, ?> x : all.entrySet()) {

            if      (x.getValue().getClass().equals(Boolean.class)) editorNew.putBoolean(x.getKey(), (Boolean)x.getValue());
            else if (x.getValue().getClass().equals(Float.class))   editorNew.putFloat(x.getKey(),   (Float)x.getValue());
            else if (x.getValue().getClass().equals(Integer.class)) editorNew.putInt(x.getKey(),     (Integer)x.getValue());
            else if (x.getValue().getClass().equals(Long.class))    editorNew.putLong(x.getKey(),    (Long)x.getValue());
            else if (x.getValue().getClass().equals(String.class))  editorNew.putString(x.getKey(),  (String)x.getValue());

        }
        editorNew.apply();
        settingsOld.edit().clear().apply();
    }


    private void storePreferenceKeyWithValue(String classType, String key, Object val){
        if (classType.equals(Integer.class.toString()))
            mEditor.putInt(key, (Integer) val).commit();
        else if(classType.equals(Long.class.toString()))
            mEditor.putLong(key, (Long) val).commit();
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
        else if(classType.equals(Long.class.toString()))
            valueOfKey = mPreferences.getLong(key, 0L);
        else if(classType.equals(Float.class.toString()))
            valueOfKey = mPreferences.getFloat(key, 0.0f);
        else if(classType.equals(Boolean.class.toString()))
            valueOfKey = mPreferences.getBoolean(key, false);
        else if(classType.equals(String.class.toString()))
            valueOfKey = mPreferences.getString(key, "");
        return valueOfKey;
    }
}
