package com.dsource.idc.jellowintl.utility;

import android.os.Bundle;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;

public class UserEventCollector {
    /*Firebase event variables. Variables are initialized to empty values.*/
    private int mFirstEventNo = 99;
    private String mEventName = "";
    private boolean isLastEventExprGrid = false;

    /**
     *Like - 0
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
}
