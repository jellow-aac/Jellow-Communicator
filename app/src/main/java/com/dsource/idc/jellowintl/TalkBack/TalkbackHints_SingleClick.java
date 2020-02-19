package com.dsource.idc.jellowintl.TalkBack;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.dsource.idc.jellowintl.R;

public class TalkbackHints_SingleClick extends AccessibilityDelegateCompat {
    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat
            info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        AccessibilityNodeInfoCompat.AccessibilityActionCompat click =
                new AccessibilityNodeInfoCompat.AccessibilityActionCompat
                        (AccessibilityNodeInfo.ACTION_CLICK,
                                host.getContext().getString(R.string.talkback_single_click));
        info.addAction(click);
    }
}

