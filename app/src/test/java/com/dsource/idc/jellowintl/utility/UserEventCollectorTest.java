package com.dsource.idc.jellowintl.utility;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserEventCollectorTest {

    UserEventCollector userEventCollector;
    String eventName = "testEvent";
    String eventCategory = "testCategory";
    Context context;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        Analytics.getAnalytics(context, "9653238072");
        userEventCollector = new UserEventCollector();
    }

    @Test
    public void createSendFbEventFromTappedViewTest(){
        userEventCollector.createSendFbEventFromTappedView(11,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(10,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(9,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(8,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(7,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(6,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(5,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(4,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(3,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(2,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(1,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(0,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(12,eventName,"");
        userEventCollector.createSendFbEventFromTappedView(12,eventName,"");
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(99,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(26,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(99,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(26,eventName,"");
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(26,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(13,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(9,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(23,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(26,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(9,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(23,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(14,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(23,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(23,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(12,eventName,eventCategory);

        userEventCollector.clearPendingEvent();
        userEventCollector.createSendFbEventFromTappedView(9,eventName,eventCategory);
        userEventCollector.createSendFbEventFromTappedView(29,eventName,eventCategory);



    }

    @Test
    public void sendEventIfAnyTest(){
        userEventCollector.sendEventIfAny(eventCategory);
        userEventCollector.sendEventIfAny("");
    }

    @Test
    public void accessibilityPopupOpenedEvent(){
        userEventCollector.accessibilityPopupOpenedEvent(eventName);
    }

    @Test
    public void clearPendingEventTest(){
        userEventCollector.clearPendingEvent();
    }


}
