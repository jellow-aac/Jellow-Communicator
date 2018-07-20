package com.dsource.idc.jellowintl.verbiage_model;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.LevelOneVerbiageModel;
import com.dsource.idc.jellowintl.models.LevelThreeVerbiageModel;
import com.dsource.idc.jellowintl.models.LevelTwoVerbiageModel;
import com.dsource.idc.jellowintl.models.SeqActivityVerbiageModel;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Ayaz Alam
 */
public class Verbiage {

    private static final int GENERAL = 00;
    private static final int SEQUENCE = 11;
    private static final int EXPRESSIVE = 22;
    private static final int MISCELLANEOUS = 33;
    private Context context;
    private ArrayList<ArrayList<String>> mLayerOneSpeech;
    private ArrayList<ArrayList<ArrayList<String>>> mLayerTwoSpeech;
    private ArrayList<ArrayList<ArrayList<String>>> mSeqActSpeech;
    private String[] mCategoryIconBelowText;
    private String[] mCategoryIconSpeechText;
    private String[] mCategoryIconText;
    private int nullCOunter=0;
    private  int co=0;
    private boolean noChildInThird = false;
    private static int LANG_CODE = 4;
    public final static String ENG_US = "en-rUS";
    public final static String ENG_UK = "en-rGB";
    public final static String ENG_IN = "en-rIN";
    public final static String HI_IN = "hi-rIN";


    /**
     * Public constructor
     * @param converterActivity Context
     */
    public Verbiage(Context converterActivity) {
        context=converterActivity;
    }

    /**
     * This is the main function that is called from the activity
     * @param levelOne level one Icons
     * @param levelTwo level two Icons
     * @param levelThree level three Icons
     * @return Json string
     */
    public String createJson(ArrayList<JellowIcon> levelOne, ArrayList<JellowIcon> levelTwo, ArrayList<JellowIcon> levelThree) {
        String s="";
        s=s+getLevelOneVerbiage(levelOne);
        s=s+getLevelTwoVerbiage(levelTwo);
        s=s+getLevelThreeVerbiage(levelThree);
        s= s+getSequenceVerbiage();
        s=s+getVerbiageModelMiscellaneous();
        writeJsonStringToFile(s,context);
        return s;
    }

    /**
     * These functions are designed to return the Verbiage model for all the different icons
     * 1.) Level 1
     * 2.) Level 2
     * 3.) Level 3
     * 4.) Sequence
     * 5.) Expressive
     *
     */
    private ArrayList<ArrayList<String>> getVerbiageModelLevel1() {

        LevelOneVerbiageModel verbiageModel = new Gson()
                .fromJson(context.getString(R.string.levelOneVerbiage), LevelOneVerbiageModel.class);
        mLayerOneSpeech = verbiageModel.getVerbiageModel();
        if(mLayerOneSpeech==null)
            Toast.makeText(context,"List is null",Toast.LENGTH_SHORT).show();
        return mLayerOneSpeech;
    }
    private ArrayList<ArrayList<ArrayList<String>>> getVerbiageModelLevel2() {
        String verbString = context.getString(R.string.levelTwoVerbiage1) +
                context.getString(R.string.levelTwoVerbiage2) +
                context.getString(R.string.levelTwoVerbiage3) +
                context.getString(R.string.levelTwoVerbiage4);
        LevelTwoVerbiageModel verbiageModel = new Gson().
                fromJson(verbString, LevelTwoVerbiageModel.class);
        mLayerTwoSpeech = verbiageModel.getVerbiageModel();
        return mLayerTwoSpeech;


    }
    private ArrayList<ArrayList<ArrayList<ArrayList<String>>>> getVerbiageModelLevel3() {

        String verbString = context.getString(R.string.levelThreeVerbiage1) +
                context.getString(R.string.levelThreeVerbiage2) +
                context.getString(R.string.levelThreeVerbiage3)+
                context.getString(R.string.levelThreeVerbiage4) +
                context.getString(R.string.levelThreeVerbiage5) +
                context.getString(R.string.levelThreeVerbiage6) +
                context.getString(R.string.levelThreeVerbiage7) +
                context.getString(R.string.levelThreeVerbiage8) +
                context.getString(R.string.levelThreeVerbiage9) +
                context.getString(R.string.levelThreeVerbiage10) +
                context.getString(R.string.levelThreeVerbiage11) +
                context.getString(R.string.levelThreeVerbiage12) +
                context.getString(R.string.levelThreeVerbiage13) +
                context.getString(R.string.levelThreeVerbiage14) +
                context.getString(R.string.levelThreeVerbiage15) +
                context.getString(R.string.levelThreeVerbiage16) +
                context.getString(R.string.levelThreeVerbiage17) +
                context.getString(R.string.levelThreeVerbiage18) +
                context.getString(R.string.levelThreeVerbiage19) +
                context.getString(R.string.levelThreeVerbiage20);
        LevelThreeVerbiageModel mLevelThreeVerbiageModel = new Gson().
                fromJson(verbString, LevelThreeVerbiageModel.class);
        return mLevelThreeVerbiageModel.getVerbiageModel();

    }
    private ArrayList<ArrayList<ArrayList<String>>> getVerbiageModelSequence() {
        String verbString = context.getString(R.string.sequenceActVerbiage1) +
                context.getString(R.string.sequenceActVerbiage2);
        SeqActivityVerbiageModel verbiageModel = new Gson()
                .fromJson(verbString, SeqActivityVerbiageModel.class);
        mSeqActSpeech = verbiageModel.getVerbiageModel();
        return mSeqActSpeech;
    }
    private String getVerbiageModelMiscellaneous(){
        String[] mExprBtnTxt = context.getResources().getStringArray(R.array.arrActionSpeech);
        int arr[]=new int[]{0,2,4,6,8,10};
        JsonObject jsonObject=new JsonObject();
        for(int i=0;i<arr.length;i++)
        {
            String Title=mExprBtnTxt[arr[i]].toUpperCase();
            String L=mExprBtnTxt[arr[i]];
            String LL=mExprBtnTxt[arr[i]+1];
            String Name=getNameForMiscellaneous(i,EXPRESSIVE);
            MiscellaneousIcons expIcon=new MiscellaneousIcons(Title,L,LL);
            String jsonForObj=new Gson().toJson(expIcon);
            jsonObject.addProperty(Name,jsonForObj);

        }

        String[] mNavigationBtnTxt = context.getResources().getStringArray(R.array.arrNavigationSpeech);

        for(int i=0;i<mNavigationBtnTxt.length;i++)
        {
            String Title=mNavigationBtnTxt[i].toUpperCase();
            String L="NA";
            String LL="NA";
            String Name=getNameForMiscellaneous(i,MISCELLANEOUS);
            MiscellaneousIcons expIcon=new MiscellaneousIcons(Title,L,LL);
            String jsonForObj=new Gson().toJson(expIcon);
            jsonObject.addProperty(Name,jsonForObj);
        }


        return jsonObject.toString();
    }

    /**
     * These function generate a Json file for a Icon of there respective level
     * @param icon
     * @return
     */
    private String getJsonForLevel1(JellowIcon icon) {
        mLayerOneSpeech= getVerbiageModelLevel1();
        ArrayList<String> v=new ArrayList<>();
        for(int j=0;j<12;j++)
        {
            Log.d("Verbiage: ","i: "+0+ " Verbiage: "+mLayerOneSpeech.get(icon.parent0).get(j)+"\n");
            try{
                v.add(mLayerOneSpeech.get(icon.parent0).get(j));
            }catch (IndexOutOfBoundsException e)
            {
                v.add("NA");
            }

        }

        String SpeechLable=levelOneSpeech(icon);
        if(SpeechLable.equals(null)||SpeechLable.equals(""))
            Log.d("Null","Icon: "+icon.IconTitle+" COunt"+nullCOunter++);
        JellowVerbiageModel thisIcon=new JellowVerbiageModel(icon.IconTitle,SpeechLable,v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),
                v.get(6),v.get(7),v.get(8),v.get(9),v.get(10),v.get(11));
        String s=new Gson().toJson(thisIcon);
        return  s;
    }
    private String getJsonForLevel2(JellowIcon icon) {
        mLayerTwoSpeech= getVerbiageModelLevel2();
        ArrayList<String> v=new ArrayList<>();
        for(int i=0;i<12;i++)
        {
            try {
                v.add(mLayerTwoSpeech.get(icon.parent0).get(icon.parent1).get(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                v.add("NA");
            }

        }

        String speech_Label=levelTwoSpeech(icon);
        if(speech_Label.equals(null))
            Log.d("Null_Speech","Icon: "+icon.IconTitle+" count"+nullCOunter++);

        JellowVerbiageModel thisIcon=new JellowVerbiageModel(icon.IconTitle,speech_Label,v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),
                v.get(6),v.get(7),v.get(8),v.get(9),v.get(10),v.get(11));
        String s=new Gson().toJson(thisIcon);
        return s;

    }
    private String getJsonForLevel3(JellowIcon icon) {
        ArrayList<ArrayList<ArrayList<ArrayList<String>>>> Model = getVerbiageModelLevel3();

        ArrayList<String> v=new ArrayList<>();
        for(int i=0;i<12;i++)
        {
            try {
                v.add(Model.get(icon.parent0).get(icon.parent1).get(icon.parent2).get(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                v.add("NA");
            }

        }

        String speech_lable=levelThreeSpeech(icon);
        if(speech_lable.equals(null))
            Log.d("Null_Speech","Icon: "+icon.IconTitle+" count"+nullCOunter++);
        JellowVerbiageModel thisIcon=new JellowVerbiageModel(icon.IconTitle,speech_lable,v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),
                v.get(6),v.get(7),v.get(8),v.get(9),v.get(10),v.get(11));
        String s=new Gson().toJson(thisIcon);
        return s;

    }
    private String getJsonForSequence(JellowIcon icon) {

        ArrayList<ArrayList<ArrayList<String>>> sde = getVerbiageModelSequence();
        ArrayList<String> v=new ArrayList<>();
        for(int i=0;i<12;i++)
        {
            int temp=icon.parent1;
            if(temp == 7)
                temp = 3;
            else if(temp == 8)
                temp = 4;

            try {
                Log.d("SeqVerbiage","Icons: "+icon.IconTitle+" p0 p1 p2"+icon.parent0+""+icon.parent1+" "+icon.parent2+" :new temp "+temp+ "verbiage "+i+""+sde.get(icon.parent0).get(temp).get(i));
                v.add(sde.get(temp).get(icon.parent2).get(i));
            }
            catch (IndexOutOfBoundsException e)
            {
                v.add("NA");
            }

        }

        String speech_Label= levelSequenceSpeech(icon);
        if(speech_Label.equals(null))
            Log.d("Null_Speech","Icon: "+icon.IconTitle+" count"+nullCOunter++);

        JellowVerbiageModel thisIcon=new JellowVerbiageModel(icon.IconTitle,speech_Label,v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),
                v.get(6),v.get(7),v.get(8),v.get(9),v.get(10),v.get(11));
        String s=new Gson().toJson(thisIcon);
        return s;

    }

    /**
     * This function is responsible for generation of name of verbiage and icon according to the
     * Nomenclature
     * @param icon
     * @return "String Name" of the verbiage and icon
     */
    public String getIconName(JellowIcon icon, int iconType) {
        String LC=getLanguageCode();
        String L1=getLevelOneCode(icon);
        String L2=getLevelTwoCode(icon);
        String L3=getLevelThreeCode(icon);
        String IconType=getIconType(iconType);

        return LC+L1+L2+L3+IconType;
    }
    private String getNameForMiscellaneous(int code,int type) {
        String LangCode=getLanguageCode();
        String IconCode=getLevelMicellaneousCode(code);
        String IconType=getIconType(type);
        Log.d("LangCode","Language_Code"+LangCode);
        return  LangCode+IconCode+IconType;
    }
    private String getIconType(int code) {
        if(code==GENERAL)
            return "GG";
        else if(code==SEQUENCE)
            return "SS";
        else if(code==EXPRESSIVE)
            return "EE";
        else if(code== MISCELLANEOUS)
            return "MS";
        return null;
    }
    private String getLevelThreeCode(JellowIcon icon) {
        if(icon.parent2==-1)
            return String.format("%04d", 0);

        return String.format("%04d", (icon.parent2+1));
    }
    private String getLevelTwoCode(JellowIcon icon) {
        if(icon.parent1==-1)
            return String.format("%02d", 0);

        return String.format("%02d", (icon.parent1+1));
    }
    private String getLevelOneCode(JellowIcon icon) {
        return String.format("%02d", (icon.parent0+1));
    }

    private String getLanguageCode() {
        String lang=new SessionManager(context).getLanguage();
        int lang_code=1;
        if(lang.equals(ENG_IN))
            lang_code=1;
        else if(lang.equals(ENG_US))
            lang_code=2;
        else if(lang.equals(ENG_UK))
            lang_code=3;
        else if(lang.equals(HI_IN))
            lang_code=4;
        //Calling this function because it generates two digit string
        return String.format("%02d", lang_code);//for English india
    }
    private String getLevelMicellaneousCode(int i) {
        return String.format("%02d", (i+1));
    }

    /**
     * These functions are designed to return the Json for the ArrayList of different Levels of the Icons
     * @param list
     * @return
     */
    public String getLevelOneVerbiage(ArrayList<JellowIcon> list) {
        getVerbiageModelLevel1();
        JsonObject jsonObject=new JsonObject();
        for(int i=0;i<list.size();i++)
        {
            JellowIcon icon=list.get(i);
            String IconName=getIconName(icon,GENERAL);
            String JsonString= getJsonForLevel1(icon);
            Log.d("IconName "+(++co),IconName);
            jsonObject.addProperty(IconName,JsonString);

        }

        return jsonObject.toString();
    }
    private String getLevelTwoVerbiage(ArrayList<JellowIcon> l2) {
        JsonObject jsonObject=new JsonObject();
        for(int i=0;i<l2.size();i++)
        {
            JellowIcon icon=l2.get(i);
            String IconName=getIconName(icon,GENERAL);
            String JsonString= getJsonForLevel2(icon);
            Log.d("IconName "+(++co),IconName);
            jsonObject.addProperty(IconName,JsonString);

        }

        return jsonObject.toString();
    }
    private String getLevelThreeVerbiage(ArrayList<JellowIcon> l3) {

        JsonObject jsonObject=new JsonObject();
        for(int i=0;i<l3.size();i++)
        {
            JellowIcon icon=l3.get(i);

            String IconName=getIconName(icon,GENERAL);
            String JsonString = getJsonForLevel3(icon);
            Log.d("IconName "+(++co), IconName);
            jsonObject.addProperty(IconName, JsonString);

        }

        return jsonObject.toString();
    }
    private String getSequenceVerbiage() {

        getVerbiageModelSequence();
        JsonObject jsonObject=new JsonObject();
        generateIconListForSequence();
        if(seqList.size()>0)
        {

            for(int i = 0; i< seqList.size(); i++)
            {
                JellowIcon icon=seqList.get(i);
                String IconName=getIconName(icon,SEQUENCE);
                String JsonString= getJsonForSequence(icon);
                Log.d("IconName "+(++co),IconName);
                jsonObject.addProperty(IconName,JsonString);

            }


        }

        return jsonObject.toString();

    }

    /**
     * This function is responsible for returning the speech text for level three
     * @param levelOneItemPos
     * @param levelTwoItemPos
     * @return
     */
    String[] thirdLevelIcons=null;
    String[] thirdLevelSpeech =null;
    private String getSpeechTextLevel2(JellowIcon icon) {
        String arr[]=null;
        switch (icon.parent0)
        {
            case 0:arr=context.getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText);
                break;
            case 1:arr=context.getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechText);
                break;
            case 2:arr=context.getResources().getStringArray(R.array.arrLevelTwoEatSpeechText);
                break;
            case 3:arr=context.getResources().getStringArray(R.array.arrLevelTwoFunSpeechText);
                break;
            case 4:arr=context.getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText);
                break;
            case 5:arr=context.getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText);
                break;
            case 6:arr=context.getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText);
                break;
            case 7:arr=context.getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText);
                break;
            case 8:arr=context.getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText);
                break;
            default:
        }
        return arr[icon.parent1];
    }
    private boolean getSpeechTextLevel3(int levelOneItemPos, int levelTwoItemPos) {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingSpeechText));
                    break;
                case 1: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsSpeechText));
                    break;
                case 2: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsSpeechText));
                    break;
                case 3: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechText));
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0:
                    noChildInThird =true;break;
                case 1:
                    noChildInThird =true;break;
                case 2:
                    noChildInThird =true;break;
                case 3: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccSpeechText));
                    break;
                case 4: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadySpeechText));
                    break;
                case 5: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepSpeechText));
                    break;
                case 6: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapySpeechText));
                    break;
                case 7:
                    noChildInThird =true;break;
                case 8:
                    noChildInThird =true;break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos) {
                case 0: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastSpeechText));
                    break;
                case 1: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerSpeechText));
                    break;
                case 2: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsSpeechText));
                    break;
                case 3: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksSpeechText));
                    break;
                case 4: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsSpeechText));
                    break;
                case 5: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksSpeechText));
                    break;
                case 6: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutlerySpeechText));
                    break;
                case 7: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonSpeechText));
                    break;
            }
        } else if (levelOneItemPos == 3) {
            switch(levelTwoItemPos){
                case 0: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesSpeechText));
                    break;
                case 1: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesSpeechText));
                    break;
                case 2: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunSportsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunSportsSpeechText));
                    break;
                case 3: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunTvIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunTvSpeechText));
                    break;
                case 4: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunMusicIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunMusicSpeechText));
                    break;
                case 5: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesSpeechText));
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos) {
                case 0: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsSpeechText));
                    break;
                case 1: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsSpeechText));
                    break;
                case 2: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningBooksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningBooksSpeechText));
                    break;
                case 3: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningColorsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningColorsSpeechText));
                    break;
                case 4: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningShapesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningShapesSpeechText));
                    break;
                case 5: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningStationarySpeechText));
                    break;
                case 6: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjSpeechText));
                    break;
                case 7: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningHomeIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjSpeechText));
                    break;
                case 8: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningTransportationIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningTransportSpeechText));
                    break;
                case 9: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeLearningMoneyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningMoneySpeechText));
                    break;
            }
        }
        else if(levelOneItemPos==5)
        {
            noChildInThird =true;
        }
        else if(levelOneItemPos==6)
        {
            noChildInThird =true;
        }
        else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos) {
                case 0: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeSpeechText);
                    break;
                case 1: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDaySpeechText);
                    break;
                case 2: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthSpeechText);
                    break;
                case 3: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherSpeechText);
                    break;
                case 4: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsSpeechText);
                    break;
                case 5: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestIconAdapter);
                    thirdLevelSpeech =context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestSpeechText);
                    break;
                case 6: prepareSpeechLevel3(context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysSpeechText));
                    break;
            }
        }
        else if(levelOneItemPos==8)
        {
            noChildInThird =true;
        }

        return true;
    }
    private void prepareSpeechLevel3(String[] typeIconArray, String[] stringBelowTextArray) {
        thirdLevelIcons=typeIconArray;
        thirdLevelSpeech =stringBelowTextArray;
    }
    private String[] getSpeechTextSequence(int mLevelTwoItemPos) {
        /* Sequence activity Morning Routine (original index is 7 in level 2) has new index 3, and
         * Bed time Routine (original index is 8 in level 2) has new index 4 */

        int temp=mLevelTwoItemPos;
        if(mLevelTwoItemPos == 7)
            mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)
            mLevelTwoItemPos = 4;

        switch(mLevelTwoItemPos) {
            case 0:

                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBrushingSpeechText);

                break;
            case 1:

                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityToiletSpeechText);

                break;
            case 2:

                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBathingSpeechText);

                break;
            case 3:

                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineSpeechText);

                break;
            case 4:

                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineSpeechText);

                break;


        }

        return mCategoryIconSpeechText;
    }

    /**
     * These functions returns the speech text for a particular icon
     * @param icon
     * @return
     */
    private String levelOneSpeech(JellowIcon icon) {
        String arr[] = context.getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        return arr[icon.parent0];
    }
    private String levelTwoSpeech(JellowIcon icn) {
        String s= getSpeechTextLevel2(icn);
        return s;

    }
    private String levelThreeSpeech(JellowIcon icon) {
        noChildInThird = false;
        if (getSpeechTextLevel3(icon.parent0, icon.parent1)) {
            if (thirdLevelSpeech != null) {
                String[] thirdLevelSpeech = this.thirdLevelSpeech;
                return thirdLevelSpeech[icon.parent2];
            }

            return null;
        }


        return null;
    }
    private String levelSequenceSpeech(JellowIcon icon) {

        int mLevelTwoItemPos=icon.parent1;
        int temp=mLevelTwoItemPos;
        if(mLevelTwoItemPos == 7)
            mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)
            mLevelTwoItemPos = 4;
        String s= getSpeechTextSequence(mLevelTwoItemPos)[icon.parent2];
        return s;
    }

    /**
     * These function calls are to fetch file from the Apps database and rename them accordingly
     */
    public void createNomenclatureDrawables() {
        ArrayList<JellowIcon> icons=  new IconDataBaseHelper(context).getAllIcons();
        generateIconListForSequence();
        for(int i=0;i<icons.size();i++)
            renameIcon(icons.get(i),GENERAL);
        for(int i=0;i<seqList.size();i++)
            renameIcon(seqList.get(i),SEQUENCE);
    }
    private void renameIcon(JellowIcon icon, int iconType) {

        SessionManager mSession=new SessionManager(context);
        File en_dir = context.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/drawables";
        String IconCurrentName=icon.IconDrawable+".png";
        Log.d("Rename","File: "+IconCurrentName);
        File output = context.getExternalFilesDir(null);
        String outPath=output.getAbsolutePath();
        String inPath=path;
        moveFile(inPath,IconCurrentName,outPath,icon,iconType);
    }
    private void moveFile(String inputPath,String inputFile, String outputPath, JellowIcon icon,int iconType) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath +"/"+ inputFile);
            out = new FileOutputStream(outputPath +"/"+getIconName(icon,iconType)+".png");
            Log.d("Rename","OutPutPath :"+out.toString());

            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;



        }

        catch (FileNotFoundException fnfe1) {
            Log.e("Rename", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("Rename", e.getMessage());
        }

    }
    public void renameLevelOneIcon(ArrayList<JellowIcon> icons, ImageView imageView) {
        for(int i=0;i<icons.size();i++)
            renameL1Icon(icons.get(i),GENERAL,imageView);
    }
    private void renameL1Icon(JellowIcon icon, int iconType, ImageView imageView) {
        String IconName=getIconName(icon,iconType);
        TypedArray mArray=context.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
        imageView.setImageDrawable(mArray.getDrawable(icon.parent0));

        Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        File output = context.getExternalFilesDir(null);
        String outPath=output.getAbsolutePath();
        File file = new File(outPath, IconName+".png");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        try {
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.GONE);
    }
    public void renameMiscellaneousIcons(ImageView imageView) {
        String arr[]=context.getResources().getStringArray(R.array.exp);
        for(int i=0;i<arr.length;i++)
            renameExp(i,imageView,EXPRESSIVE);
        String ar[]=context.getResources().getStringArray(R.array.nav);
        for(int i=0;i<ar.length;i++)
            renameExp(i,imageView,MISCELLANEOUS);


    }
    private void renameExp(int i, ImageView imageView,int Type) {
        TypedArray array=null;
        if(Type==EXPRESSIVE)
            array=context.getResources().obtainTypedArray(R.array.exp);
        else if(Type==MISCELLANEOUS)
           array=context.getResources().obtainTypedArray(R.array.nav);

        imageView.setImageDrawable(array.getDrawable(i));
        String IconName=getNameForMiscellaneous(i,Type);
        Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        File output = context.getExternalFilesDir(null);
        String outPath=output.getAbsolutePath();
        File file = new File(outPath, IconName+".png");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        try {
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.GONE);

    }

    /**
     * Generates Icon list for SequenceActivity
     */
    ArrayList<JellowIcon > seqList;
    private void generateIconListForSequence() {
        getVerbiageModelSequence();

        seqList =new ArrayList<>();
        int arr[] ={0,1,2,7,8};
        int p1=1;
        for(int i=0;i<arr.length;i++)
            prepareSequenceIcon(p1,arr[i]);
    }
    private void prepareSequenceIcon(int LevelOnePos, int mLevelTwoItemPos) {
        /* Sequence activity Morning Routine (original index is 7 in level 2) has new index 3, and
         * Bed time Routine (original index is 8 in level 2) has new index 4 */

        int temp=mLevelTwoItemPos;
        if(mLevelTwoItemPos == 7)
            mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)
            mLevelTwoItemPos = 4;

        switch(mLevelTwoItemPos) {
            case 0:
                mCategoryIconBelowText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBrushingBelowText);
                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBrushingSpeechText);
                mCategoryIconText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBrushingIcon);
                break;
            case 1:
                mCategoryIconBelowText = context.getResources().getStringArray
                        (R.array.arrSeqActivityToiletBelowText);
                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityToiletSpeechText);
                mCategoryIconText = context.getResources().getStringArray
                        (R.array.arrSeqActivityToiletIcon);
                break;
            case 2:
                mCategoryIconBelowText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBathingBelowText);
                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBathingSpeechText);
                mCategoryIconText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBathingIcon);
                break;
            case 3:
                mCategoryIconBelowText = context.getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineBelowText);
                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineSpeechText);
                mCategoryIconText = context.getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineIcon);
                break;
            case 4:
                mCategoryIconBelowText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineBelowText);
                mCategoryIconSpeechText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineSpeechText);
                mCategoryIconText = context.getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineIcon);
                break;


        }

        prepareArrayListForSequence(LevelOnePos,temp);


    }
    private void prepareArrayListForSequence(int levelOnePos, int temp) {


        for(int i=0;i<mCategoryIconBelowText.length;i++)
        {
            String Name=mCategoryIconBelowText[i];
            String drawable=mCategoryIconText[i];
            JellowIcon jellowIcon=new JellowIcon(
                    Name,drawable,levelOnePos,temp,i
            );
            seqList.add(jellowIcon);
            Log.d("SequenceIcons","Sequence Icon added: "+jellowIcon.IconTitle+" : Dr : "+jellowIcon.IconDrawable+" :  p1 :"+jellowIcon.parent0
            +":  p2 "+jellowIcon.parent1+" p 3:  "+jellowIcon.parent2);

        }


    }

    /**
     * This function is to write string data to the file
     * @param data
     * @param context
     */
    private void writeJsonStringToFile(String data, Context context) {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, "Jellow_unformatted.json");
        Log.d("Path",path.getAbsolutePath());
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(data.getBytes());
            Log.d("Verbiage:","Written Successfully");

        }
        catch (IOException e)
        {

        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Jellow.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}



