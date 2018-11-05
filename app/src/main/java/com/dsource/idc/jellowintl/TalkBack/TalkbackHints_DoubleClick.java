package com.dsource.idc.jellowintl.TalkBack;

import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.dsource.idc.jellowintl.R;

public class TalkbackHints_DoubleClick extends AccessibilityDelegateCompat {
    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat
            info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        AccessibilityNodeInfoCompat.AccessibilityActionCompat click =
                new AccessibilityNodeInfoCompat.AccessibilityActionCompat
                        (AccessibilityNodeInfo.ACTION_CLICK,
                                host.getContext().getString(R.string.talkback_double_click));
        info.addAction(click);

    }
}
