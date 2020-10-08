package com.dsource.idc.jellowintl.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.LanguageHelper;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

/**
 * Created by user on 6/6/2016.
 */
public class TutorialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.menuTutorials));
        setNavigationUiConditionally();
        setImagesToImageViewUsingGlide();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    private void setImagesToImageViewUsingGlide() {
        setImageUsingGlide(getResources().getDrawable(R.drawable.categorybuttons), ((ImageView)findViewById(R.id.pic1)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.expressivebuttons), ((ImageView)findViewById(R.id.pic2)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.speakingwithjellowimage2), ((ImageView)findViewById(R.id.pic4)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.eatingcategory1), ((ImageView)findViewById(R.id.pic5)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.eatingcategory2), ((ImageView)findViewById(R.id.pic6)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.eatingcategory3), ((ImageView)findViewById(R.id.pic7)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.settings), ((ImageView)findViewById(R.id.pic8)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.sequencewithoutexpressivebuttons), ((ImageView)findViewById(R.id.pic9)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.sequencewithexpressivebuttons), ((ImageView)findViewById(R.id.pic10)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.gtts1), ((ImageView)findViewById(R.id.gtts1)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.gtts2), ((ImageView)findViewById(R.id.gtts2)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.gtts3), ((ImageView)findViewById(R.id.gtts3)));

        setImageUsingGlide(getResources().getDrawable(R.drawable.my_boards), ((ImageView)findViewById(R.id.pic11)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.my_boards_edit), ((ImageView)findViewById(R.id.pic12)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.add_boards), ((ImageView)findViewById(R.id.pic13)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.add_icons), ((ImageView)findViewById(R.id.pic14)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.add_edit_icon), ((ImageView)findViewById(R.id.pic15)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.edit_icon), ((ImageView)findViewById(R.id.pic16)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.edit_verbiage), ((ImageView)findViewById(R.id.pic17)));
        setImageUsingGlide(getResources().getDrawable(R.drawable.board_home), ((ImageView)findViewById(R.id.pic18)));
    }

    private void setImageUsingGlide(Drawable image, ImageView imgView) {
        GlideApp.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imgView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(TutorialActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        stopMeasuring("TutorialActivity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
