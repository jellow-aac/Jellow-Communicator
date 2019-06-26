package com.dsource.idc.jellowintl.utils;

import android.view.View;

import org.hamcrest.Matcher;

public class EspressoTestMatchers {

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }
}
