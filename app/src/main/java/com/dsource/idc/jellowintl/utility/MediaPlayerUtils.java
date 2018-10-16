package com.dsource.idc.jellowintl.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class MediaPlayerUtils {
    private Context mContext;
    private String mUserLang;
    private HashMap<Integer, String> vbgCode = new HashMap<Integer, String>(){
        {
            put(0, "L0");
            put(1, "LL");
            put(2, "Y0");
            put(3, "YY");
            put(4, "M0");
            put(5, "MM");
            put(6, "D0");
            put(7, "DD");
            put(8, "N0");
            put(9, "NN");
            put(10, "S0");
            put(11, "SS");
        }
    };
    private HashMap<String, String> langCode = new HashMap<String, String>(){
        {
            put(ENG_IN, "01");
            put(ENG_US, "02");
            put(ENG_UK, "03");
            put(HI_IN, "04");
            put(MR_IN, "05");
            put(BN_IN, "06");
        }
    };

    public MediaPlayerUtils (Context context){
        mContext = context;
        SessionManager session = new SessionManager(mContext);
        mUserLang = session.getLanguage();
    }

    /**
     * <p>This function provide create filename using app current language, current level,
     * selected category icon and expressive button tapped.
     * @param speechCode, position of the verbiage.
     * @return the full filename string.</p>
     * */
    @SuppressLint("LongLogTag")
    public String getFilePath(String speechCode) {
        String filePath = "";
        // Add file path
        filePath += mContext.getDir(mUserLang, Context.MODE_PRIVATE).getAbsolutePath() + "/audio/";
        // Add country specific code to filename
        if(filePath.contains(MR_IN))
            filePath += "05";
        else
            //TODO add more non-tts language code here.
            filePath = "";

        switch (speechCode.split("_")[0]){
            case "EXP":
            case "MIS":
                filePath += speechCode.split("_")[1];
                break;
            case "CATL1":
                String gridCodeL1 = speechCode.split("_")[1];
                gridCodeL1 = gridCodeL1.length() != 2 ? 0 + gridCodeL1 : gridCodeL1;
                filePath += gridCodeL1 + "000000GGTT";
                break;
            case "GRXL1":
                String[] gridExprL1 = speechCode.split("_");
                gridExprL1[1] = gridExprL1[1].length() != 2 ? 0 + gridExprL1[1] : gridExprL1[1];
                filePath += gridExprL1[1] + "000000GG" + gridExprL1[2];
                break;
            case "CATL2":
                String[] gridCodeL2 = speechCode.split("_");
                gridCodeL2[1] = gridCodeL2[1].length() != 2 ? 0 + gridCodeL2[1] : gridCodeL2[1];
                gridCodeL2[2] = gridCodeL2[2].length() != 2 ? 0 + gridCodeL2[2] : gridCodeL2[2];
                filePath += gridCodeL2[1]+ gridCodeL2[2] + "0000GGTT";
                break;
            case "GRXL2":
                String[] gridExprL2 = speechCode.split("_");
                gridExprL2[1] = gridExprL2[1].length() != 2 ? 0 + gridExprL2[1] : gridExprL2[1];
                gridExprL2[2] = gridExprL2[2].length() != 2 ? 0 + gridExprL2[2] : gridExprL2[2];
                filePath += gridExprL2[1] + gridExprL2[2] +"0000GG" + gridExprL2[3];
                break;
            case "CATL3":
                String[] gridCodeL3 = speechCode.split("_");
                gridCodeL3[1] = gridCodeL3[1].length() != 2 ? 0 + gridCodeL3[1] : gridCodeL3[1];
                gridCodeL3[2] = gridCodeL3[2].length() != 2 ? 0 + gridCodeL3[2] : gridCodeL3[2];
                while (gridCodeL3[3].length() != 4)
                    gridCodeL3[3] = "0" + gridCodeL3[3];
                filePath += gridCodeL3[1]+ gridCodeL3[2] + gridCodeL3[3] + "GGTT";
                break;
            case "GRXL3":
                String[] gridExprL3 = speechCode.split("_");
                gridExprL3[1] = gridExprL3[1].length() != 2 ? 0 + gridExprL3[1] : gridExprL3[1];
                gridExprL3[2] = gridExprL3[2].length() != 2 ? 0 + gridExprL3[2] : gridExprL3[2];
                while (gridExprL3[3].length() < 4)
                    gridExprL3[3] = "0" + gridExprL3[3];
                filePath += gridExprL3[1] + gridExprL3[2] + gridExprL3[3] + "GG" + gridExprL3[4];
                break;
            case "CATSQ":
                String[] gridCodeSeq = speechCode.split("_");
                gridCodeSeq[1] =  "0" + (Integer.valueOf(gridCodeSeq[1])+1);
                while (gridCodeSeq[2].length() != 4)
                    gridCodeSeq[2] = "0" + gridCodeSeq[2];
                filePath += "02"+ gridCodeSeq[1]+ gridCodeSeq[2] + "SSTT";
                break;
            case "GRXSQ":
                String[] gridExprSeq = speechCode.split("_");
                gridExprSeq[1] =  "0" + (Integer.valueOf(gridExprSeq[1])+1);
                gridExprSeq[1] = gridExprSeq[1].length() != 2 ? 0 + gridExprSeq[1] : gridExprSeq[1];
                while (gridExprSeq[2].length() < 4)
                    gridExprSeq[2] = "0" + gridExprSeq[2];
                filePath += "02"+ gridExprSeq[1] + gridExprSeq[2] + "SS" + gridExprSeq[3];
                break;
        }

        filePath += ".mp3";
        return filePath;
    }

    /**
     * <p>This function checks if current app language is supported by tts or not.
     *  currently, MR_IN only language which does not have tts support.
     * @return If tts support the current language then true otherwise false.</p>
     * */
    public boolean isTtsAvailForLang(){
        //TODO add more non-tts language code here.
        return !mUserLang.equals(MR_IN);
    }

    /**
     * <p>This function send the broadcast message to Text-to-speech service about
     * requesting to play recorded audio stream given in {@param audioPath}. For a given language
     * if text-to-speech engine is available then do not play recorded audio stream.
     * @param audioPath is path of recorded audio file.</p>
     * */
    public void playAudio(String audioPath) {
        //If text-to-speech is available for language then do not play recorded audio stream.
        if(isTtsAvailForLang())
            return;
        Intent intent = new Intent("com.dsource.idc.jellowintl.AUDIO_PATH");
        intent.putExtra("audioPath", audioPath);
        mContext.sendBroadcast(intent);
    }

    public void playAudioInQueue(String audioPaths) {
        //If text-to-speech is available for language then do not play recorded audio stream.
        if(isTtsAvailForLang())
            return;
        String filePath = mContext.getDir(mUserLang, Context.MODE_PRIVATE).getAbsolutePath() + "/audio/";
        audioPaths = filePath + audioPaths.split(",")[0]+".mp3," +
                filePath + audioPaths.split(",")[1]+".mp3" ;
        Intent intent = new Intent("com.dsource.idc.jellowintl.AUDIO_IN_QUEUE");
        intent.putExtra("speechTextInQueue", audioPaths);
        mContext.sendBroadcast(intent);
    }

    public String getIconCode(int pos, String verbiage) {
        // "$0_000000GG--" Base icon code for level one.
        //Increment the position by 1 as the Icon code starts from 1 instead of 0.
        return "$$0_000000GG--"
                .replace("$$", langCode.get(mUserLang))
                .replace("_", String.valueOf(pos+1))
                .replace("--", verbiage);
    }

    public String getIconCode(int l1Pos, int l2Pos, String verbiage) {
        // "$$0_--0000GG##" Base icon code for level two.
        //Increment the position by 1 as the Icon code starts from 1 instead of 0.
        return "$$0_--0000@@##"
                .replace("$$", langCode.get(mUserLang))
                .replace("_", String.valueOf(l1Pos+1))
                .replace("--", (l2Pos+1 > 9) ? String.valueOf(l2Pos+1) : "0"+ String.valueOf(l2Pos+1))
                //if sequence position then add "SS" instead of "GG"
                .replace("@@",(l1Pos == 1 &&(l2Pos == 0 || l2Pos == 1 || l2Pos == 2 || l2Pos == 7 || l2Pos == 8) ? "SS" :"GG"))
                .replace("##", verbiage);
    }

    public String getIconCode(int l1Pos, int l2Pos, int l3Pos, String verbiage) {
        // "$$0_--@@@@GG##" Base icon code for level three.
        //Increment the position by 1 as the Icon code starts from 1 instead of 0.
        String rStr = (l3Pos+1)+"";
        while (rStr.length() < 4)
            rStr = "0".concat(rStr);
        return "$$0_--@@@@GG##"
                .replace("$$", langCode.get(mUserLang))
                .replace("_", String.valueOf(l1Pos+1))
                .replace("--", (l2Pos+1 > 9) ? String.valueOf(l2Pos+1) : "0"+ String.valueOf(l2Pos+1))
                .replace("@@@@", rStr)
                .replace("##", verbiage);
    }

    public String getIconCodeSeq(int l2Pos, int l3Pos, String verbiage) {
        // "$$02--@@@@SS##" Base icon code for level Sequence.
        //Increment the position by 1 as the Icon code starts from 1 instead of 0.
        String rStr = l3Pos+"";
        while (rStr.length() < 4)
            rStr = "0".concat(rStr);
        return "$$02--@@@@SS##"
                .replace("$$", langCode.get(mUserLang))
                .replace("--", "0" + String.valueOf(l2Pos+1))
                .replace("@@@@", rStr)
                .replace("##", verbiage);
    }

    public int getSizeForCode(int level1Pos) {
        switch(level1Pos){
            case 0: return mContext.getResources().getStringArray(R.array.arrLevelTwoGreetFeelIconAdapter).length;
            case 1: return mContext.getResources().getStringArray(R.array.arrLevelTwoDailyActIconAdapter).length;
            case 2: return mContext.getResources().getStringArray(R.array.arrLevelTwoEatingIconAdapter).length;
            case 3: return mContext.getResources().getStringArray(R.array.arrLevelTwoFunIconAdapter).length;
            case 4: return mContext.getResources().getStringArray(R.array.arrLevelTwoLearningIconAdapter).length;
            case 5: return mContext.getResources().getStringArray(R.array.arrLevelTwoPeopleIcon).length;
            case 6: return mContext.getResources().getStringArray(R.array.arrLevelTwoPlacesIcon).length;
            case 7: return mContext.getResources().getStringArray(R.array.arrLevelTwoTimeIconAdapter).length;
            case 8: return mContext.getResources().getStringArray(R.array.arrLevelTwoHelpIconAdapter).length;
        }
        return 0;
    }

    public String[] getSizeForCode(int level1Pos, int level2Pos) {
        if (level1Pos == 0) {
            switch(level2Pos){
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsAdapterText);
            }
        } else if (level1Pos == 1) {
            switch(level2Pos){
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepAdapterText);
                case 6: return mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyAdapterText);
                case 9: return mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActHabitsAdapterText);
            }
        } else if (level1Pos == 2) {
            switch(level2Pos) {
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksAdapterText);
                case 6: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryAdapterText);
                case 7: return mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonAdapterText);
            }
        } else if (level1Pos == 3) {
            switch(level2Pos){
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunSportsAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunTvAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunMusicAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesAdapterText);
            }
        } else if (level1Pos == 4) {
            switch(level2Pos) {
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBooksAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningColorsAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningShapesAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryAdapterText);
                case 6: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjAdapterText);
                case 7: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjAdapterText);
                case 8: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningTransportAdapterText);
                case 9: return mContext.getResources().getStringArray(R.array.arrLevelThreeLearningMoneyAdapterText);
            }
        }else if (level1Pos == 6) {
            switch(level2Pos) {
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesMyHouseAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesSchoolAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesMallAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesMuseumAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesRestaurantAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesTheatreAdapterText);
                case 6: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesPlaygroundAdapterText);
                case 7: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesParkAdapterText);
                case 8: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesStoreAdapterText);
                case 9: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesFriendHouseAdapterText);
                case 10: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesRelativeHouseAdapterText);
                case 11: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesHospitalAdapterText);
                case 12: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesClinicAdapterText);
                case 13: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesLibraryAdapterText);
                case 14: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesZooAdapterText);
                case 15: return mContext.getResources().getStringArray(R.array.arrLevelThreePlacesWorshipAdapterText);
            }
        } else if (level1Pos == 7) {
            switch(level2Pos) {
                case 0: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeAdapterText);
                case 1: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayAdapterText);
                case 2: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthAdapterText);
                case 3: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherAdapterText);
                case 4: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsAdapterText);
                case 5: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestAdapterText);
                case 6: return mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysAdapterText);
            }
        }
        return null;
    }

    public ArrayList<ArrayList<String>> getEmptyList(int size) {
        ArrayList<String> iconList = new ArrayList<String>(Collections.nCopies(12,""));
        return new ArrayList<ArrayList<String>>(Collections.nCopies(size,iconList));
    }
}