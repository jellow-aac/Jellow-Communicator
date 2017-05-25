package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 6/6/2016.
 */

public class Tutorial extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14;
    private SessionManager session;
    ImageView pic1, pic2, pic4, pic5, pic6, pic7, pic8, pic9, pic10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Tutorial</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        setContentView(R.layout.tutorial_new);

        session = new SessionManager(getApplicationContext());

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        tv7=(TextView)findViewById(R.id.tv7);
        tv8=(TextView)findViewById(R.id.tv8);
        tv9=(TextView)findViewById(R.id.tv9);
        tv10=(TextView)findViewById(R.id.tv10);
        tv11=(TextView)findViewById(R.id.tv11);
        tv12=(TextView)findViewById(R.id.tv12);
        tv13=(TextView)findViewById(R.id.tv13);
        tv14=(TextView)findViewById(R.id.tvtts);
        pic1=(ImageView)findViewById(R.id.pic1);
        pic2=(ImageView)findViewById(R.id.pic2);
        pic4=(ImageView)findViewById(R.id.pic4);
        pic5=(ImageView)findViewById(R.id.pic5);
        pic6=(ImageView)findViewById(R.id.pic6);
        pic7=(ImageView)findViewById(R.id.pic7);
        pic8=(ImageView)findViewById(R.id.pic8);
        pic9=(ImageView)findViewById(R.id.pic9);
        pic10=(ImageView)findViewById(R.id.pic10);

        String stringresources = "To use Jellow, please activate "+"<b>"+"'Google text-to-speech (tts)'"+"</b>"+" within "+"<b>"+"'Language'"+"</b>"+" settings of your device. Within Google tts settings, first change the "+"<b>"+" default language "+"</b>"+" to "+"<b>"+" 'Hindi(India)' "+"</b>"+" and then "+"<b>"+" download 'Hindi(India) voice "+"</b>"+" through the "+"<b>"+"'Install voice data' "+"</b>"+" option." ;
        tv14.setText(Html.fromHtml(stringresources));
        //To use Jellow, please activate 'Google text-to-speech (tts)' within 'Language' settings of your device. Within Google tts settings first change the default language to 'Hindi (India)' and then download 'Hindi (India)' voice through the 'Install voice data' option"

        String stringresourceshindi = "जेलो का इस्तमाल करने के लिए कृपया अपने यंत्र के"+"<b>"+" ‘भाषा'"+"</b>"+" सेटिंग्स में जाकर "+"<b>"+"‘गूगल टेक्सट टू स्पीच'"+"</b>"+" को सक्रिय करें| कृपया गूगल टेक्सट टू स्पीच के सेटिंग्स में जाकर पहले "+"<b>"+" 'हिंदी (इंडिया)' भाषा "+"</b>"+" को "+"<b>"+" डिफ़ॉल्ट "+"</b>"+" बनाएँ और फिर "+"<b>"+"'हिंदी (इंडिया)' आवाज़ "+"</b>"+" डाउन्लोड करें|";

        if (session.getLanguage()==1){
            tv1.setText("जेलो के स्क्रीन के मध्य भाग में ९ मुख्य वर्गों के बटन हैं तथा उनके दोनों तरफ ६ भाववाहक बटन हैं।");
            tv7.setText("मुख्य वर्गों के बटन:");
            tv8.setText("भाववाहक बटन: ");
            tv2.setText("जेलो का इस्तमाल कर बात करने के लिए प्रयोगकर्ता अॅप्लिकेशन में पहले किसी एक मुख्य वर्ग के बटन को चुनें एवं उसके पश्चात किसी एक भाववाहक बटन को चुनें। उदाहरण के तौर पर नीचे दिखाये हुए बटन दबाने पर जेलो बोलेगा  - \"मुझे खाना हैं\"।");
            tv3.setText("किसी भी मुख्य वर्ग के बटन पर डबल क्लिक करके प्रयोगकर्ता उनके विकल्प और उप-विकल्पों पर जा सकते हैं।");
            tv9.setText("श्रेणी स्तर १:");
            tv10.setText("श्रेणी स्तर २: ");
            tv11.setText("श्रेणी स्तर ३:");
            tv4.setText("स्क्रीन के शीर्ष दाहिने कोने पर क्लिक करके प्रयोगकर्ता अपनी प्रोफाइल बना सकते हैं एवं अॅप्लिकेशन के मुख्य सेटिंग्स को बदल सकते हैं।");
            tv5.setText("जेलो का इस्तमाल बच्चों को दैनिक कार्यों के अनुक्रमिक चरण सिखाने के लिए भी किया जा सकता हैं। जेलो का इस तरह प्रयोग करते वक्त किसी भी चरण के बटन पर क्लिक करने से प्रयोगकर्ता स्क्रीन के दोनों तरफ के भाववाहक बटन देख सकते हैं।");
            tv12.setText("शौचालय के उपयोग के चरण:");
            tv13.setText("चरण के भाववाहक बटन:");
            tv14.setText(Html.fromHtml(stringresourceshindi));
            tv6.setText("सॉफ्टवेयर संस्करण: ४.१");

            pic1.setImageResource(R.drawable.categorybuttons_hindi);
            pic2.setImageResource(R.drawable.expressivebuttons_hindi);
            pic4.setImageResource(R.drawable.speakingwithjelloimage2_hindi);
            pic5.setImageResource(R.drawable.eatingcategory1_hindi);
            pic6.setImageResource(R.drawable.eatingcategory2_hindi);
            pic7.setImageResource(R.drawable.eatingcategory3_hindi);
            pic8.setImageResource(R.drawable.settings_hindi);
            pic9.setImageResource(R.drawable.sequencewithoutexpressivebuttons_hindi);
            pic10.setImageResource(R.drawable.sequencewithexpressivebuttons_hindi);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>ट्यूटोरियल</font>"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (session.getLanguage()==1){
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_main, menu);
        }
        if (session.getLanguage()==0) {
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_1, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Tutorial.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent i = new Intent(Tutorial.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Tutorial.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Tutorial.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Tutorial.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Tutorial.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Tutorial.this, MainActivity.class);
                startActivity(intent5);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Tutorial.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}



