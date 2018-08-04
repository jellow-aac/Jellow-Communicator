package com.dsource.idc.jellowintl.TalkBack;

import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

public class TalkbackHints_DropDownMenu extends AccessibilityDelegateCompat {
    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat
            info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        AccessibilityNodeInfoCompat.AccessibilityActionCompat click =
                new AccessibilityNodeInfoCompat.AccessibilityActionCompat
                        (AccessibilityNodeInfo.ACTION_CLICK,
                                " access countries list");
        info.addAction(click);

    }
}
