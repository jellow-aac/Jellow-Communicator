package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.os.Bundle;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class UserEventCollector {
    /*Firebase event variables. Variables are initialized to empty values.*/
    private int mFirstEventNo = 99;
    private String mEventName = "";
    private boolean isLastEventExprGrid = false;
    private List<String> mEventTag = new ArrayList<>();

    public String getEtTag(int pos) {
        return mEventTag.get(pos);
    }

    /**
     * Like - 0
     *ReallyLike - 1
     *Yes - 2
     *ReallyYes - 3
     *More - 4
     *ReallyMore - 5
     *Don'tLike - 6
     *ReallyDon'tLike - 7
     *No - 8
     *ReallyNo - 9
     *Less - 10
     *ReallyLess - 11
     *GridIcon - 12
     *GridExp Like - 13
     *GridExp ReallyLike - 14
     *GridExp Yes - 15
     *GridExp ReallyYes - 16
     *GridExp More - 17
     *GridExp ReallyMore - 18
     *GridExp Don'tLike - 19
     *GridExp ReallyDon'tLike - 20
     *GridExp No - 21
     *GridExp ReallyNo - 22
     *GridExp Less - 23
     *GridExp ReallyLess - 24
     *Opened Grid - 25
     *home - 26
     *back - 27
     *nothing is selected 99
     **/
    public void createSendFbEventFromTappedView(int newEventNo, String eventName, String category){
        // if home tapped and no other previous event then home event.
        if(mFirstEventNo == 99 && newEventNo == 26)
            return;
        // If 1st event is GridExpressive and 2nd event is Grid then
        // break (clear) all event. Set 2nd event as 1st event.
        if (isLastEventExprGrid && newEventNo == 12){
            mEventName = eventName;
            mFirstEventNo = newEventNo;
            isLastEventExprGrid = false;
            return;
        }
        //isLastEventExprGrid = false;
        if(mFirstEventNo == 99 && newEventNo < 26) {
            mFirstEventNo = newEventNo;
            if(newEventNo == 12)
                mEventName = eventName;
        }else{
            String exprBtName = "";
            switch (mFirstEventNo){
                case 0: exprBtName = "Like"; break;
                case 1: exprBtName = "ReallyLike"; break;
                case 2: exprBtName = "Yes"; break;
                case 3: exprBtName = "ReallyYes"; break;
                case 4: exprBtName = "More"; break;
                case 5: exprBtName = "ReallyMore"; break;
                case 6: exprBtName = "Don'tLike"; break;
                case 7: exprBtName = "ReallyDon'tLike"; break;
                case 8: exprBtName = "No"; break;
                case 9: exprBtName = "ReallyNo"; break;
                case 10: exprBtName = "Less"; break;
                case 11: exprBtName = "ReallyLess"; break;
            }
            //User tapped first {mFirstEventNo} expressive icon then tapped {newEventNo} expressive icon second.
            if (mFirstEventNo < 12 && newEventNo < 12){
                singleEvent("ExpressiveIcon",exprBtName);
                mFirstEventNo = newEventNo;

            //User tapped first {mFirstEventNo} expressive icon then tapped {newEventNo} grid icon second.
            }else if (mFirstEventNo < 12 && newEventNo == 12) {
                singleEvent("ExpressiveGridIcon", exprBtName+"_"+eventName);
                mFirstEventNo = newEventNo;
                mEventName = eventName;

            //User tapped first {mFirstEventNo} expressive icon then tapped {newEventNo} navigation icon second.
            }else if (mFirstEventNo < 12 && newEventNo > 25) {
                singleEvent("ExpressiveIcon",exprBtName);
                mFirstEventNo = 99;

            //User tapped first {mFirstEventNo} grid icon then tapped {newEventNo} expressive icon second.
            }else if (mFirstEventNo == 12 && newEventNo > 12 && newEventNo < 25) {
                singleEvent("GridExpressiveIcon", eventName);
                isLastEventExprGrid = true;

            //User tapped {mFirstEventNo} Grid Expression icon then tapped {newEventNo} navigation icon second.
            }else if (mFirstEventNo == 12 && isLastEventExprGrid && newEventNo > 25) {

            //User tapped first {mFirstEventNo} grid icon then tapped {newEventNo} grid icon second.
            }else if (mFirstEventNo == 12 && newEventNo == 12) {
                Bundle bundle = new Bundle();
                bundle.putString("Icon", mEventName);
                if(!category.isEmpty())
                    bundle.putString("Category", category);
                bundleEvent("Grid", bundle);
                mFirstEventNo = newEventNo;
                mEventName = eventName;

            //User tapped first {mFirstEventNo} grid icon then tapped {newEventNo} home icon second.
            }else if (mFirstEventNo == 12 && (newEventNo == 26 || newEventNo == 27)) {
                Bundle bundle = new Bundle();
                bundle.putString("Icon", mEventName);
                if(!category.isEmpty())
                    bundle.putString("Category", category);
                bundleEvent("Grid", bundle);
                mFirstEventNo = 99;
                mEventName = "";
            }

            if(newEventNo == 26){
                mFirstEventNo = 99;
                mEventName = "";
                isLastEventExprGrid = false;
            }
        }
    }

    public void sendEventIfAny(String category) {
        Bundle bundle = new Bundle();
        bundle.putString("Icon", mEventName);
        if(!category.isEmpty())
            bundle.putString("Category", category);
        bundleEvent("Grid", bundle);
        mFirstEventNo = 99;
        mEventName = "";
    }

    /**
     * This event sent only TalkBack accessibility is on and user open Accessibility
     * dialog.**/
    public void accessibilityPopupOpenedEvent(String eventName) {
        singleEvent("AccessiblePopup", eventName);
        // Create grid event base for up dialog. This base will help us to log subsequent
        // grid events, expressiveGrid events aon Accessibility dialog.
        mEventName = eventName;
        mFirstEventNo = 12;
    }

    /**
     * This function call only when Accessibility TalkBack popup is closed and it clears
     * used variables.
     * **/
    public void clearPendingEvent() {
        mFirstEventNo = 99;
        mEventName = "";
    }

    public void setEventTag(Context context) {
        mEventTag = Arrays.asList(context.getResources().getStringArray(R.array.arrLvlOneTag));
    }

    public void setEventTag(Context context, String lang, int l1Pos){
        int ar[] = {R.array.arrLvlTwoTagGreetFeel, R.array.arrLvlTwoTagDailyAct,
            R.array.arrLvlTwoTagEating, R.array.arrLvlTwoTagFun,
            R.array.arrLvlTwoTagLearning, R.array.arrLvlTwoTagPeople,
            R.array.arrLvlTwoTagPlaces, R.array.arrLvlTwoTagTimeWeather,
            R.array.arrLvlTwoTagHelp};
        if(lang.equals(MR_IN))
            ar[5] = R.array.arrLvlTwoTagPeople_MR_RIN;
        else if (lang.equals(HI_IN))
            ar[5] = R.array.arrLvlTwoTagPeople_HI_RIN;
        mEventTag = Arrays.asList(context.getResources().getStringArray(ar[l1Pos]));
    }

    public void setEventTag(Context context, String lang, int l1Pos, int l2Pos){
        int ar[][]={ {R.array.arrLvlThreeTagGreetings, R.array.arrLvlThreeTagFeelings, R.array.arrLvlThreeTagRequests, R.array.arrLvlThreeTagQuestions},
                {R.array.arrLvlThreeTagBrushing, R.array.arrLvlThreeTagToilet, R.array.arrLvlThreeTagBathing,
                    R.array.arrLvlThreeTagClothAccessories, R.array.arrLvlThreeTagGettingReady, R.array.arrLvlThreeTagSleep, R.array.arrLvlThreeTagTherapy,
                    R.array.arrLvlThreeTagTherapyMorningRoutine, R.array.arrLvlThreeTagBedTimeRoutine, R.array.arrLvlThreeTagHabits},
                {R.array.arrLvlThreeTagBreakfast, R.array.arrLvlThreeTagLunchDinner, R.array.arrLvlThreeTagSweets, R.array.arrLvlThreeTagSnacks,
                    R.array.arrLvlThreeTagFruits, R.array.arrLvlThreeTagDrinks, R.array.arrLvlThreeTagCutlery, R.array.arrLvlThreeTagAddOns},
                {R.array.arrLvlThreeTagIndoorGames, R.array.arrLvlThreeTagOutdoorGames, R.array.arrLvlThreeTagSports,
                    R.array.arrLvlThreeTagTv, R.array.arrLvlThreeTagMusic, R.array.arrLvlThreeTagActivities},
                {R.array.arrLvlThreeTagAnimalBirds, R.array.arrLvlThreeTagBody, R.array.arrLvlThreeTagBooks, R.array.arrLvlThreeTagColors,
                    R.array.arrLvlThreeTagShapes, R.array.arrLvlThreeTagStationary, R.array.arrLvlThreeTagSchoolObjects,
                    R.array.arrLvlThreeTagHomeObjects, R.array.arrLvlThreeTagTransportModes, R.array.arrLvlThreeTagMoney},
                {},
                {R.array.arrLvlThreeTagMyHouse, R.array.arrLvlThreeTagSchool, R.array.arrLvlThreeTagMall, R.array.arrLvlThreeTagMuseum,
                    R.array.arrLvlThreeTagHotel, R.array.arrLvlThreeTagTheatre, R.array.arrLvlThreeTagPlayground, R.array.arrLvlThreeTagPark,
                    R.array.arrLvlThreeTagStore, R.array.arrLvlThreeTagFriendsHouse, R.array.arrLvlThreeTagRelativesHouse,
                    R.array.arrLvlThreeTagHospital, R.array.arrLvlThreeTagClinic, R.array.arrLvlThreeTagLibrary, R.array.arrLvlThreeTagZoo,
                    R.array.arrLvlThreeTagWorship},
                {R.array.arrLvlThreeTagTime, R.array.arrLvlThreeTagDay, R.array.arrLvlThreeTagMonth, R.array.arrLvlThreeTagWeather,
                    R.array.arrLvlThreeTagSeason, R.array.arrLvlThreeTagFestivalHolidays, R.array.arrLvlThreeTagBirthdays},
                {}
        };
        switch (lang) {
            case HI_IN:
                ar[7][6] = R.array.arrLvlThreeTagBirthdays_HI_RIN;
                break;
            case MR_IN:
                ar[7][6] = R.array.arrLvlThreeTagBirthdays_MR_RIN;
                break;
            case ENG_US:
            case ENG_UK:
            case ENG_AU:
                ar[1][3] = R.array.arrLvlThreeTagClothAccessories_EN_RUS_RGB;
                ar[1][5] = R.array.arrLvlThreeTagSleep_EN_RUS_RGB;
                ar[2][0] = R.array.arrLvlThreeTagBreakfast_EN_RUS_RGB;
                ar[2][1] = R.array.arrLvlThreeTagLunchDinner_EN_RUS_RGB;
                ar[2][2] = R.array.arrLvlThreeTagSweets_EN_RUS_RGB;
                ar[2][3] = R.array.arrLvlThreeTagSnacks_EN_RUS_RGB;
                ar[2][4] = R.array.arrLvlThreeTagFruits_EN_RUS_RGB;
                ar[2][5] = R.array.arrLvlThreeTagDrinks_EN_RUS_RGB;
                ar[2][7] = R.array.arrLvlThreeTagAddOns_EN_RUS_RGB;
                ar[3][0] = R.array.arrLvlThreeTagIndoorGames_EN_RUS_RGB;
                ar[3][1] = R.array.arrLvlThreeTagOutdoorGames_EN_RUS_RGB;
                ar[3][2] = R.array.arrLvlThreeTagSports_EN_RUS_RGB;
                ar[4][0] = R.array.arrLvlThreeTagAnimalBirds_EN_RUS_RGB;
                ar[4][2] = R.array.arrLvlThreeTagBooks_EN_RUS_RGB;
                ar[4][7] = R.array.arrLvlThreeTagHomeObjects_EN_RUS_RGB;
                ar[4][8] = R.array.arrLvlThreeTagTransportModes_EN_RUS_RGB;
                if (lang.equals(ENG_US)) {
                    ar[4][9] = R.array.arrLvlThreeTagMoney_EN_RUS;
                    ar[6][7] = R.array.arrLvlThreeTagPark_EN_RUS;
                } else {
                    ar[4][9] = R.array.arrLvlThreeTagMoney_EN_RGB;
                    ar[6][7] = R.array.arrLvlThreeTagPark_EN_RGB;
                }
                ar[7][5] = R.array.arrLvlThreeTagFestivalHolidays_EN_RUS_RGB;
                ar[7][6] = R.array.arrLvlThreeTagBirthdays_EN_RUS_RGB;
                break;
        }
        mEventTag = Arrays.asList(context.getResources().getStringArray(ar[l1Pos][l2Pos]));
    }
}
