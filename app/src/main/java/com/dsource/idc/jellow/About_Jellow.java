package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.SessionManager;

import java.util.Locale;

/**
 * Created by user on 5/27/2016.
 */

public class About_Jellow extends AppCompatActivity {

    TextToSpeech t1;
    Button speak, stop;
    String s;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20, tv21, tv22, tv23, tv24, tv25, tv26, tv27, tv28, tv29, tv30, tv31, tv32, tv33, tv34, tv35;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_jellow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>About Jellow</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        s  = "Jellow is a communicator using icons to enable speech and can be used by those learning to speak or those who have difficulty with speech.\n"+
                " It is developed at IDC School of Design at the Indian Institute of Technology Bombay located in Mumbai.\n"+
                " A novel Visual Emotional Language Proto call or VELP was conceived and developed by the project investigators in order to enhance the language ability of Jellow. VELP represents the 6 expressive icons that form the core of the interface.\n"+
                " Using these core icons, the child can communicate their likes, dislikes, and needs with others.\n"+
                " Jellow is conceived as children-friendly with an easy-to-learn interface.\n"+
                " The application is optimized for Android tablets.\n"+
                " Jellow is currently available in English and Hindi. The current version of Jellow is 4.1. The application is under further development. Therefore, if you have any suggestions for us, please submit it under the feedback tab.\n"+
                " Jellow is licensed under a Creative Commons Attribution Non-commercial Share Alike 4.0 International License. Work on Jellow is attributed to the Indian Institute of Technology Bombay. The content in this application comprising icons, layout, architecture, visual identity, translation, and the software codes can be shared and adopted but cannot be used for commercial purposes. When the content is used, we request you to acknowledge Indian Institute of Technology Bombay. Jellow is available for download at www dot d source.in/tools/jellow.\n"+
                " If interested in further developing the code, please write to Professor Ravi Poovaiah at ravi @ i i t b dot a c.in\n" ;

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
        tv14=(TextView)findViewById(R.id.tv14);
        tv15=(TextView)findViewById(R.id.tv15);
        tv16=(TextView)findViewById(R.id.tv16);
        tv17=(TextView)findViewById(R.id.tv17);
        tv18=(TextView)findViewById(R.id.tv18);
        tv19=(TextView)findViewById(R.id.tv19);
        tv20=(TextView)findViewById(R.id.tv20);
        tv21=(TextView)findViewById(R.id.tv21);
        tv22=(TextView)findViewById(R.id.tv22);
        tv23=(TextView)findViewById(R.id.tv23);
        tv24=(TextView)findViewById(R.id.tv24);
        tv25=(TextView)findViewById(R.id.tv25);
        tv26=(TextView)findViewById(R.id.tv26);
        tv27=(TextView)findViewById(R.id.tv27);
        tv28=(TextView)findViewById(R.id.tv28);
        tv29=(TextView)findViewById(R.id.tv29);
        tv30=(TextView)findViewById(R.id.tv30);
        tv31=(TextView)findViewById(R.id.tv31);
        tv32=(TextView)findViewById(R.id.tv32);
        tv33=(TextView)findViewById(R.id.tv33);
        tv34=(TextView)findViewById(R.id.tv34);
        tv35=(TextView)findViewById(R.id.tv35);
        speak= (Button)findViewById(R.id.speak);
        stop = (Button)findViewById(R.id.stop);

        if (session.getLanguage()==1){
            tv1.setText("सामान्य जानकारी");
            tv2.setText("जेलो एक संवाद सहायक अॅप्लिकेशन हैं, जो आइकनों एवं छवियों का उपयोग करके बातचीत  करने में मदद करता हैं। जो बोलना सीख रहें हैं, या जिन्हें बोलने में तकलीफ हैं, वे जेलो का इस्तमाल कर सकते हैं।");
            tv3.setText("जेलो का विकास,  भारतीय प्रौद्योगिकी संस्थान मुंबई (आई आई टी-बी), के आईडीसी अभिकल्प विद्यालय में किया गया हैं।");
            tv4.setText("जेलो की भाषा क्षमता बढ़ाने के लिए इस प्रोजेक्ट के प्रमुख जांचकर्ताओंने एक नये विज्युअल इमोशनल लैंगग्विज प्रोटोकॉल (VELP) का संकल्पन किया हैं। विजुअल इमोशनल लैंगग्विज प्रोटोकॉल के अंतर्गत जेलो में ६ भाववाहक आइकॉन हैं जो इस इंटरफेस के मूलभूत भाग हैं।");
            tv5.setText("इन मूल भाववाहक आइकॉनों की सहायता से जेलो के प्रयोगकर्ता अपनी पसंद-नापसंद तथा आवश्यकताएँ दूसरों को बता सकते हैं।");
            tv6.setText("जेलो का इस्तमाल बच्चें बहुत आसानी से सीख सकते हैं।");
            tv35.setText("जेलो अॅप्लिकेशन एंड्रॉयड टैबलेट के लिए उपयुक्त किया गया हैं।");
            tv7.setText("फिलहाल जेलो अॅप्लिकेशन अंग्रेजी और हिन्दी भाषाओं में उपलब्ध हैं।");
            tv8.setText("सॉफ्टवेयर संस्करण: ४.१");
            tv9.setText("जेलो का विकास फिलहाल जारी हैं।  इस अॅप्लिकेशन के सुधार से संबंधित यदि आप के कोई सुझाव हैं, तो कृपया उन्हें \"प्रतिक्रिया\" टैब में सबमिट करें।");
            tv10.setText("उपयोग के नियम एवं शर्तें:");
            tv11.setText("जेलो अॅप्लिकेशन को क्रिएटिव कॉमन्स एटट्रिब्यूशन-नॉन-कमर्शियल- शेयर-ए-लाइक ४.० लाइसेंस के तहत लाइसेंस प्राप्त हैं।");
            tv12.setText("जेलो अॅप्लिकेशन पर सभी अनुसंधान कार्यों का श्रेय भारतीय प्रौद्योगिकी संस्थान मुंबई (आई आई टी-बी) को दिया जाता हैं।");
            tv13.setText("इस जेलो अॅप्लिकेशन के सभी कन्टेन्ट (समावेश किये गए चिह्न, लेआउट, वास्तुकला, विज्युअल आइडेन्टिटी, अनुवाद, और सॉफ्टवेयर कोड) को बाँटा और अपनाया जा सकता हैं, लेकिन वाणिज्यिक प्रयोजनों के लिए इनका उपयोग नहीं किया जा सकता हैं।");
            tv14.setText("जब कन्टेन्ट का उपयोग किया जाता है, तो आपसे अनुरोध हैं की, आप भारतीय प्रौद्योगिकी संस्थान मुंबई (आई आई टी-बी) के प्रती आभार प्रकट करें।" );
            tv15.setText("जेलो अॅप्लिकेशन इस लिंक पर डाउनलोड करने के लिए उपलब्ध हैं।");
            tv34.setText("www.dsource.in/tools/jellow");
            tv16.setText("यदि कोई जेलो को आगे विकसित करना चाहें, तो उनसे अनुरोध हैं की, प्रमुख जांचकर्ता प्राध्यापक रवि पूवैया से ravi[at]iitb.ac.in इस ई-मेल आयडी पर संपर्क करें।  ");
            tv17.setText("आभार सूची:");
            tv18.setText("•  प्रोजेक्ट अन्वेषण, संकल्पना और पर्यवेक्षण");
            tv19.setText("\t \tप्राध्यापक रवि पूवैया और डॉ. अजंता सेन");
            tv20.setText("•  प्रारंभिक संकल्पना विकास");
            tv21.setText("\t \tआँचल कुमार, सम्राट सरदेसाई, पीटर जोसेफ, अंतरा हजारिका ");
            tv22.setText("•  सॉफ्टवेयर का विकास:");
            tv23.setText("\t \tसुमित अग्रवाल, रूप साहू, श्रुति गुप्ता, आस्था जोशी, धैर्य दण्ड,  प्रीतम पेबम");
            tv24.setText("•  वेबसाइट डिज़ाइन:");
            tv25.setText("\t \tप्राध्यापक. रवि पूवैया, योगेश मासये, सुमेध गरुड़, रूप साहू");
            tv26.setText("•  आइकॉन और विज्युअल डिज़ाइन:");
            tv27.setText("\t \tविनया तावड़े, अनीषा मल्होत्रा, निकिता अय्यर, गणेश गज्जेला");
            tv28.setText("•  अनुवाद:");
            tv29.setText("\t \tसचिन सोनावणे, डॉ. सुधा श्रीनिवासन");
            tv30.setText("•  अनुसंधान, शब्दावली विकसन और उपयोगकर्ता अध्ययन:");
            tv31.setText("\t \tडॉ. सुधा श्रीनिवासन");
            tv32.setText("•  समर्थन:");
            tv33.setText("ई-कल्पा प्रोजेक्ट NMEICT के अंतर्गत  मानव संसाधन विकास मंत्रालय एवं माइक्रोसॉफ्ट डिजाईन एक्सपो २००४ द्वारा प्रायोजित ");

            s="जेलो एक संवाद सहायक ऐपलिकेशन है, जो आइकॉनों एवं छवियों का उपयोग करके बातचीत  करने में मदद करता हैं। जो बोलना सीख रहें हैं, या जिन्हें बोलने में तकलीफ हैं, वे जेलो का इस्तमाल कर सकते हैं।जेलो का विकास,  भारतीय प्रौद्योगिकी संस्थान मुम्बई के आईडीसी अभिकल्प विद्यालय में किया गया हैं।जेलो की भाषा क्षमता बढ़ाने के लिए इस प्रोजेक्ट के प्रमुख जांचकर्ताओंने एक नये Visual Emotional Language Proto call अथवा VELP का संकल्पन किया हैं। VELP के अंतर्गत जेलो में ६ भाव वाहक आइकॉन हैं जो इस इंटरफेस के मूलभूत भाग हैं। इन मूल भाव वाहक आइकॉनों की सहायता से जेलो के प्रयोगकर्ता अपनी पसंद- नापसंद तथा आवश्यकताएँ दूस्रों को बता सकते हैं। जेलो का इस्तमाल बच्चें बहुत आसानी से सीख सकते हैं। जेलो अॅप्लिकेशन एंड्रॉयड टैबलेट के लिए उपयुक्त किया गया हैं। फिलहाल जेलो ऐपलिकेशन अंग्रेज़ी और हिन्दी भाषाओं में उपलब्ध हैं।जेलो का वर्तमान सॉफ्टवेयर संस्करण ३.१ हैं। जेलो का विकास फिलहाल जारी हैं।  इस ऐपलिकेशन के सुधार से संबंधित यदि आप के कोई सुझाव हैं, तो कृपया उन्हें \"प्रतिक्रिया\" टैब में सब्मिट करें। \n" +
                    "\tजेलो ऐपलिकेशन को क्रिएटिव कॉमन्स ऐट ट्रिब्युशन-नॉन-कमर्शियल- शेयर-ए-लाइक ४.०  लायसेन्स के तहत लायसेन्स प्राप्त हैं। जेलो ऐपलिकेशनपर सभी अनुसंधान कार्यों का श्रेय \tभारतीय प्रौद्योगिकी संस्थान मुंबई) को दिया जाता हैं। इस जेलो ऐपलिकेशन के सभी कनटेंट (समावेश किये गए चिह्न, लेआउट, वास्तुकला, Visual आइडेन्टिटी, अनुवाद, \tऔर सॉफ्टवेयर कोड) को बाँटा और अपनाया जा सकता हैं, लेकिन वाणिज्यिक प्रयोजनों के \tलिए इनका उपयोग नहीं किया जा सकता हैं। जब कनटेंट का उपयोग किया जाता है, तो \tआपसे अनुरोध हैं की, आप भारतीय प्रौद्योगिकी संस्थान मुम्बई के प्रती \tआभार प्रकट करें। जेलो application www dot d source.in/tools/jellow इस लिंक पर डाउनलोड \tकरने के लिए उपलब्ध हैं। यदि कोई जेलो को आगे विकसीत करना चाहें, तो उनसे अनुरोध हैं \tकी, प्रमुख जांचकर्ता प्राध्यापक रवी पू वय या से ravi @ i i t b dot a c.in\n" +
                    " इस ई-मेल आयडी पर संपर्क \tकरें। ";

            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>जेलो के बारे में</font>"));
            speak.setText("बोलें");
            stop.setText("रुकें");
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setEngineByPackageName("com.google.android.mTts");
                }
            }
        });

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setLanguage(new Locale("hin", "IND"));
                t1.setSpeechRate((float)0.8);
                t1.speak(s, TextToSpeech.QUEUE_ADD, null);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.stop();
            }
        });
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        finish();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(About_Jellow.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(About_Jellow.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(About_Jellow.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(About_Jellow.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(About_Jellow.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(About_Jellow.this, MainActivity.class);
                startActivity(intent5);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(About_Jellow.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(About_Jellow.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}