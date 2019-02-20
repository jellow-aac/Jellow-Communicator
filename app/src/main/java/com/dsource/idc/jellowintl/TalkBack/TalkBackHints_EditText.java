package com.dsource.idc.jellowintl.TalkBack;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class TalkBackHints_EditText extends AccessibilityDelegateCompat {
    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat
            info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        final AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_VIEW_CLICKED);
        event.getText().add("nothing to say here");
        event.setClassName("UserRegistrationActivity.java");
        event.setPackageName("com.dsource.idc.jellowintl.debug");

        host.getParent().requestSendAccessibilityEvent(host, event);
        /*host.setContentDescription("Nothing to say here");
        AccessibilityNodeInfoCompat.AccessibilityActionCompat click =
                new AccessibilityNodeInfoCompat.AccessibilityActionCompat
                        (AccessibilityNodeInfo.ACTION_CLICK,
                                "Edit the text in edit text box");
        info.addAction(click);*/
    }

    /*@Override
    public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(host, event);
        host.setContentDescription("nothing to say here");
    }*/

    /*@Override
    public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(host, event);
        event.setAction(AccessibilityEvent.TYPE_VIEW_CLICKED);
        event.setContentDescription("Event is here");
    }*/
}