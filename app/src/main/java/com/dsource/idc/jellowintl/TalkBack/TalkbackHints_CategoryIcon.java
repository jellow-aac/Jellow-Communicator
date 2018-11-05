package com.dsource.idc.jellowintl.TalkBack;

import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.dsource.idc.jellowintl.R;

public class TalkbackHints_CategoryIcon extends AccessibilityDelegateCompat {

    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(host, info);

        AccessibilityNodeInfoCompat.AccessibilityActionCompat click =
                new AccessibilityNodeInfoCompat.AccessibilityActionCompat
                        (AccessibilityNodeInfo.ACTION_CLICK,
                                host.getContext().getString(R.string.talkback_recycler_view));
        info.addAction(click);
    }

    @Override
    public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
        super.sendAccessibilityEventUnchecked(host, event);
    }
}
