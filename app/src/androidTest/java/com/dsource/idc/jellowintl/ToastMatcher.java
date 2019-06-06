package com.dsource.idc.jellowintl;

import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {
    @Override
    protected boolean matchesSafely(Root item) {
        int type = item.getWindowLayoutParams().get().type;
        if(type == WindowManager.LayoutParams.TYPE_TOAST){
            return item.getDecorView().getWindowToken() ==
                    item.getDecorView().getApplicationWindowToken();
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast.");
    }
}
