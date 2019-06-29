package com.dsource.idc.jellowintl.utils;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.SessionManager;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class TestClassUtils {
    private static Context context = getInstrumentation().getTargetContext();
    private static SessionManager sessionManager;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        TestClassUtils.context = context;
    }

    public static SessionManager getSession() {
        return sessionManager != null ? sessionManager : new SessionManager(context);
    }
}
