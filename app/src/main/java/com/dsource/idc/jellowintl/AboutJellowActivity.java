package com.dsource.idc.jellowintl;

import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

/**
 * Created by user on 5/27/2016.
 */

public class AboutJellowActivity extends SpeechEngineBaseActivity {
    private Button mBtnSpeak, mBtnStop;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16,
            tv17, tv18, tv19, tv20, tv21, tv22, tv23, tv24, tv25, tv26, tv27, tv28, tv29, tv30, tv31,
            tv32, tv33, tv34, tv35, tv36, tv37;
    private String mSpeechTxt, mGenInfo, mSoftInfo, mTermofUse, mCredits,
            mAppLink, mIntro1, mIntro2, mIntro3, mIntro4, mIntro5, mIntro6, mIntro7, mIntro8,
            mIntro9, mIntro10, mIntro11, mIntro12, mIntro13, mIntro14, mIntro15, mIntro16, mIntro17,
            mIntro18, mIntro19, mIntro20, mIntro21, mIntro22, mIntro23, mIntro24, mIntro25, mIntro26,
            mIntro27, mIntro28, mIntro29, mIntro30, mIntro31, mIntro32, mSpeak, mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_jellow);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuAbout));
        initializeViews();
        loadStrings();
        setTextToTextViews();

        mBtnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(mSpeechTxt);
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSpeaking();
                stopAudio();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();

        stopSpeaking();
        stopAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(AboutJellowActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSpeaking();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopSpeaking();
        finish();
    }

    private void initializeViews() {
        tv1= findViewById(R.id.tv1);
        tv2= findViewById(R.id.tv2);
        tv3= findViewById(R.id.tv3);
        tv4= findViewById(R.id.tv4);
        tv5= findViewById(R.id.tv5);
        tv6= findViewById(R.id.tv6);
        tv7= findViewById(R.id.tv7);
        tv8= findViewById(R.id.tv8);
        tv9= findViewById(R.id.tv9);
        tv10= findViewById(R.id.tv10);
        tv11= findViewById(R.id.tv11);
        tv12= findViewById(R.id.tv12);
        tv13= findViewById(R.id.tv13);
        tv14= findViewById(R.id.tv14);
        tv15= findViewById(R.id.tv15);
        tv16= findViewById(R.id.tv16);
        tv17= findViewById(R.id.tv17);
        tv18= findViewById(R.id.tv18);
        tv19= findViewById(R.id.tv19);
        tv20= findViewById(R.id.tv20);
        tv21= findViewById(R.id.tv21);
        tv22= findViewById(R.id.tv22);
        tv23= findViewById(R.id.tv23);
        tv24= findViewById(R.id.tv24);
        tv25= findViewById(R.id.tv25);
        tv26= findViewById(R.id.tv26);
        tv27= findViewById(R.id.tv27);
        tv28= findViewById(R.id.tv28);
        tv29= findViewById(R.id.tv29);
        tv30= findViewById(R.id.tv30);
        tv31= findViewById(R.id.tv31);
        tv32= findViewById(R.id.tv32);
        tv33= findViewById(R.id.tv33);
        tv34= findViewById(R.id.tv34);
        tv35= findViewById(R.id.tv35);
        tv36= findViewById(R.id.tv36);
        tv37= findViewById(R.id.tv37);
        mBtnSpeak = findViewById(R.id.speak);
        mBtnStop = findViewById(R.id.stop);

        if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            findViewById(R.id.bottomControls).setVisibility(View.GONE);
        }
    }

    private void loadStrings() {
        String versionCode = prepareRegionalVersionCode(getSession().getLanguage(),
                String.valueOf(BuildConfig.VERSION_NAME).
                        replace(".","@").split("@"));
        mGenInfo = getString(R.string.info);
        mIntro1 = getString(R.string.about_je_intro1);
        mIntro2 = getString(R.string.about_je_intro2);
        mIntro3 = getString(R.string.about_je_intro3);
        mIntro4 = getString(R.string.about_je_intro4);
        mIntro5 = getString(R.string.about_je_intro5);
        mIntro6 = getString(R.string.about_je_intro6);
        mIntro7 = getString(R.string.about_je_intro7);
        mSoftInfo = getString(R.string.software_info);
        mIntro8 = getString(R.string.about_je_intro8);
        mSoftInfo = mSoftInfo.replace("_", versionCode);
        mIntro8 = mIntro8.contains("_") ? mIntro8.replace("_", versionCode) : mIntro8;
        mTermofUse = getString(R.string.terms_of_use);
        mIntro9 = getString(R.string.about_je_intro9);
        mIntro10 = getString(R.string.about_je_intro10);
        mIntro11 = getString(R.string.about_je_intro11);
        mIntro12 = getString(R.string.about_je_intro12);
        mIntro13 = getString(R.string.about_je_intro13);
        mAppLink = getString(R.string.about_je_link);
        mIntro14 = getString(R.string.about_je_intro14);
        mIntro14 = mIntro14.concat(" "+
                Html.fromHtml("<a href=\"mailto:ravi@iitb.ac.in\">ravi@iitb.ac.in</a>") +
                " or "+ Html.fromHtml("<a href=\"mailto:jellowcommunicator@gmail.com\">jellowcommunicator@gmail.com</a>"));
        mCredits = getString(R.string.credits);
        mIntro15 = getString(R.string.about_je_intro15);
        mIntro16 = getString(R.string.about_je_intro16);
        mIntro17 = getString(R.string.about_je_intro17);
        mIntro18 = getString(R.string.about_je_intro18);
        mIntro19 = getString(R.string.about_je_intro19);
        mIntro20 = getString(R.string.about_je_intro20);
        mIntro21 = getString(R.string.about_je_intro21);
        mIntro22 = getString(R.string.about_je_intro22);
        mIntro23 = getString(R.string.about_je_intro23);
        mIntro24 = getString(R.string.about_je_intro24);
        mIntro25 = getString(R.string.about_je_intro25);
        mIntro26 = getString(R.string.about_je_intro26);
        mIntro27 = getString(R.string.about_je_intro27);
        mIntro28 = getString(R.string.about_je_intro28);
        mIntro29 = getString(R.string.about_je_intro29);
        mIntro30 = getString(R.string.about_je_intro30);
        mIntro31 = getString(R.string.about_je_intro31);
        mSpeak = getString(R.string.speak);
        mStop = getString(R.string.stop);
        mIntro32 = getString(R.string.about_je_intro32);

        mSpeechTxt = getString(R.string.about_jellow_speech);
        if(getSession().getLanguage().equals(HI_IN))
            versionCode = versionCode.replace(".", " दशम् लक ");
        mSpeechTxt = mSpeechTxt.contains("_") ?
                mSpeechTxt.replace("_", versionCode) : mSpeechTxt;
    }

    private String prepareRegionalVersionCode(String language, String[] versionStArr) {
        StringBuilder newVsnStr = new StringBuilder();
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            newVsnStr.append(versionStArr[0]);
            newVsnStr.append(" dot ");
            newVsnStr.append(versionStArr[1]);
            newVsnStr.append(" dot ");
            newVsnStr.append(versionStArr[2]);
        }else{
            switch (language) {
                case HI_IN:
                    HashMap<String, String> eng2hindi = new HashMap<String, String>() {
                        {
                            put("0", "०");
                            put("1", "१");
                            put("2", "२");
                            put("3", "३");
                            put("4", "४");
                            put("5", "५");
                            put("6", "६");
                            put("7", "७");
                            put("8", "८");
                            put("9", "९");
                        }
                    };
                    newVsnStr.append(eng2hindi.get(versionStArr[0]));
                    newVsnStr.append(".");
                    newVsnStr.append(eng2hindi.get(versionStArr[1]));
                    newVsnStr.append(".");
                    newVsnStr.append(eng2hindi.get(versionStArr[2]));
                    break;
                case BN_IN:
                    HashMap<String, String> eng2bangla = new HashMap<String, String>() {
                        {
                            put("0", "০");
                            put("1", "১");
                            put("2", "২");
                            put("3", "৩");
                            put("4", "৪");
                            put("5", "৫");
                            put("6", "৬");
                            put("7", "৭");
                            put("8", "৮");
                            put("9", "৯");
                        }
                    };
                    newVsnStr.append(eng2bangla.get(versionStArr[0]));
                    newVsnStr.append(".");
                    newVsnStr.append(eng2bangla.get(versionStArr[1]));
                    newVsnStr.append(".");
                    newVsnStr.append(eng2bangla.get(versionStArr[2]));
                    break;
                default:
                    return BuildConfig.VERSION_NAME;
            }
        }
        return newVsnStr.toString();
    }

    private void setTextToTextViews() {
        tv1.setText(mGenInfo);
        tv2.setText(mIntro1);
        tv3.setText(mIntro2);
        tv4.setText(mIntro3);
        tv5.setText(mIntro4);
        tv6.setText(mIntro5);
        tv7.setText(mIntro7);
        tv8.setText(mSoftInfo);
        tv9.setText(mIntro8);
        tv10.setText(mTermofUse);
        tv11.setText(mIntro9);
        tv12.setText(mIntro10);
        tv13.setText(mIntro11);
        tv14.setText(mIntro12);
        tv15.setText(mIntro13);
        tv16.setText(mIntro14);
        tv17.setText(mCredits);
        tv18.setText(mIntro15);
        tv19.setText(mIntro16);
        tv20.setText(mIntro17);
        tv21.setText(mIntro18);
        tv22.setText(mIntro19);
        tv23.setText(mIntro20);
        tv24.setText(mIntro21);
        tv25.setText(mIntro22);
        tv26.setText(mIntro23);
        tv27.setText(mIntro24);
        tv28.setText(mIntro25);
        tv29.setText(mIntro26);
        tv30.setText(mIntro27);
        tv31.setText(mIntro28);
        tv32.setText(mIntro29);
        tv33.setText(mIntro30);
        tv35.setText(mIntro6);
        tv36.setText(mIntro31);
        tv37.setText(mIntro32);
        mBtnSpeak.setText(mSpeak);
        mBtnStop.setText(mStop);
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            mIntro13 = mIntro13.concat(" "+ mAppLink);
            tv15.setText(mIntro13);
            tv34.setVisibility(View.GONE);
        }else {
            tv34.setText(mAppLink);
            Linkify.addLinks(tv16, Linkify.EMAIL_ADDRESSES);
        }
    }
}
