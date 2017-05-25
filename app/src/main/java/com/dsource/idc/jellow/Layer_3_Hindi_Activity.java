package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by ekalpa on 7/14/2016.
 **/

public class Layer_3_Hindi_Activity extends AppCompatActivity {

    int layer_1_id, layer_2_id;
    int x = -1, image_flag = -1;
    int clike = 0, cy = 0, cm = 0, cd = 0, cn = 0, cl = 0;
    MediaPlayer mp = new MediaPlayer();
    MediaPlayer mp1 = new MediaPlayer();
    int flag = 0, flag_keyboard = 0;
    View vi;
    ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    boolean a = true;
    private EditText et;
    private KeyListener originalKeyListener;
    TextToSpeech tts;
    private RecyclerView recyclerView; //new addition
    private CircularImageView im1,im2,im3,im4,im5,im6, im7,im8,im9;
    LinearLayout mLinearLayoutIconOne, mLinearLayoutIconTwo, mLinearLayoutIconThree;
    public ImageAdapter imgad;
    String[] myMusic;

    public static int more_count = 0;

    Integer[] color = {-5317, -12627531 , -7617718 , -2937298 , -648053365 , -1761607680 };

    public static String[] daily_activities_brushing_text =
            {"Rinse maauuthh", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush backkkteethh", "Brush tongue", "Rinse maauuthh", "All done"};

    public static String[] daily_activities_toilet_text =
            {"Pull pants down", "Sit on toil et", "Wash bottom", "Flushh toil et", "Pull pants up", "Wash hands", "All done"};

    public static String[] daily_activities_bathing_text =
            {"Remove clothes", "Turn on water", "Get in the shaaver", "Wet body", "Put soap", "Shampoo हैर", "Put face wash", "Wash हैर", "mohrr", "Wash body", "Turn off water", "Dry हैर", "Dry face", "Dry body", "Put on clothes", "All done"};

    public static String[] daily_activities_morning_schedule_text =
            {"Wake up", "Wash face", "Go to bathroom", "Brush teeth", "Remove clothes", "Have a बाथ", "Get dressed", "Comb हैर", "mohrr", "Eat brek fust", "Pack lunch box", "Pack school bag", "Go to school", "Have a great day!"};

    public static String[] daily_activities_bedtime_schedule_text =
            {"Eat dinner", "ware night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"};

    public static String[] greet_feel_greetings_text =
            {"नमस्ते!", "नमसकार", "अलविदा", "शुभ प्रभात", "शुभ दिन", "शुभ संध्या", "शुभ रात्रि", "ताली दो",
                    "आपसे मिलकर अच्छा लगा", "आप कैसे हैं?", "आपका दिन कैसा था?", "आपके क्या हाल हैं?"};
    public static String[] greet_feel_feelings_text =
            {"खुश", "उदास", "गुस्सा", "डर", "हैरान", "चिढ़ा हुआ", "उलझन", "शर्मिंदा",
                    "निराश", "बोर", "चिंता", "तनावग्रस्त", "थका हुआ", "गरम", "ठंडा", "बीमार", "दुखी"};
    public static String[] greet_feel_requests_text =
            {"क्रिपया", "धन्यवाद", "आपका स्वागत हैं", "क्रिपया मुझे दीजीए", "क्रिपया मुझे फिर से बताइए", "क्रिपया मुझे दिखाइए", "मुझे एक ब्रेक चाहिए", "मैंने खत्म कर दिया",  "क्षमा कीजिये!", "मुझे माफ करें", "मुझे समझ में नहीं आया", "मेरे साथ बाँटे", "क्रिपया थोड़ा धीरे जाइए", "मुझे मदद की ज़रूरत हैं", "क्रिपया यहाँ आईये", "क्रिपया मुझे लेके जाइए"};
    public static String[] greet_feel_questions_text =
            {"कैसे?", "कब?", "कहाँपे?", "क्यूं?", "क्या?", "कौन?", "कितने?", "कितना लंबा?", "कितनी देर"};
    public static String[] daily_activities_clothes_access_text =
            {"टी-शर्ट बदलना ", "फ्रॉक बदलना", "Skirt बदलना", "जीन्स बदलना", "pant बदलना", "लैगिंग्स बदलना", "slacks बदलना", "शॉर्ट्स बदलना",  "इनरवियर बदलना", "जूते बदलना", "बूट बदलना", "मोज़े बदलना", "रात के कपड़ऐ पैहनना", "shirt ", "t-shirt ", "फ्रॉक ",  "pant ", "slacks ", "लैगिंग्स ", "शॉर्ट्स ", "सलवार कमीज़ ", "sweater ", "jacket ", "दुपट्टा ",  "टोपी ", "बैल्ट ", "रेनकोट ", "चश्मा ", "घड़ी ", "कान की बाली ", "कंगन", "हार ", "बिंदी ", "चप्पल ", "मेरे कपड़े टाइट हैं", "मेरे कपड़े ढीले हैं",  "मुझे कपड़े निकालने में मदद चाहिए", "मुझे कपड़े पैहनने में मदद चाहिए"};
    public static String[] daily_activities_get_ready_text =
            {"कंघी करना", "फेस वॉश", "नाखून काटना", "नाक साफ करना", "साबुन", "shampoo"};
    public static String[] daily_activities_sleep_text =
            {"दरवाज़ा", "पंखा", "लाईट", "खिड़की", "बिस्तर", "तकिया", "कंबल", "गर्मी", "ठंडक"};
    public static String[] daily_activities_therapy_text =
            {"कसरत", "झूला", "trampoline", "स्विस बॉल", "कंबल", "बॉल पिट", "हातों की कसरत", "पैरों की कसरत", "बॉडी वेस्ट"};
    public static String[] foods_drinks_breakfast_text =
            {"ब्रेड", "कॉरनफ्लएक्स", "आलू पूरी", "अंडे", "पोहा", "उपमा", "खिचड़ी", "इड़ली ",
                    "डोसा", "पराठा", "aumm lett", "मेदु वड़ा", "दलिया", "सैंडविच", "चटनी", "सांबर", "उत्तप्पा"};
    public static String[] food_drinks_lunch_dinner_text =
            {"रोटी", "सब्ज़ी", "चावल", "दाल", "दालखिचड़ी", "रायता", "परा ठा", "दही",  "मछली", "चिकन", "पोर्क", "मटन", "केकड़े का मांस", "turkey", "pizza", "सलाड ",  "सूप ", "पास्ता ", "noodles", "इटालीयन खाना", "पाव भाजी", "भाकरी"};
    public static String[] food_drinks_sweets_text =
            {"केक", "आइसक्रीम", "गा जर का हलवा", "गुलाब जामुन", "लड्डू", "बर्फी", "जलेबी", "फलों का सलाड",  "रसगुल्ला", "शीरा"};
    public static String[] food_drinks_snacks_text =
            {"बिस्कुट", "चाट", "चॉकलेट", "वएफर्स", "सैंडविच", "noodles", "चीज़", "नट्स"};
    public static String[] food_drinks_fruits_text =
            {"सेब", "केला", "अंगूर", "अमरूद", "आम", "संतरा", "अनानास", "स्ट्रॉबेरी", "बेर", "अनार", "तरबूज", "पेर", "पपीता", "खरबूजा", "चिकू", "पनस", "चेरी"};
    public static String[] food_drinks_drinks_text =
            {"पानी", "दूध", "bournvita", "आम का ज्यूस", "सेब का ज्यूस", "संतरे का ज्यूस", "नींबू का ज्यूस", "अनानास का ज्यूस",  "पेप्सी", "कोका कोला", "मिरिंडा", "फैंटा", "माज़ा", "sprite ", "माउंटेन ड्यू", "मिल्कशेक",  "चॉकलेट मिल्कशेक", "स्ट्रॉबेरी मिल्कशेक ", "केला मिल्कशेक ", "आम मिल्कशेक ", "चिकू मिल्कशेक", "चाय ", "कॉफी ", "कोल्ड कॉफी", "ऐनरजी ड्रिंक्स"};
    public static String[] food_drinks_cutlery_text =
            {"कटोरा", "प्लेट", "चम्मच", "काँटे का चम्मच", "चाकू", "मग", "कप", "ग्लास"};
    public static String[] food_drinks_add_ons_text =
            {"मक्खन", "jam", "नमक", "काली मिर्च", "चीनी", "सॉस", "आचार", "पापड़", "मसाला"};
    public static String[] fun_indoor_games_text =
            {"पज़ल्स", "बोर्ड खेल", "ब्लॉक्स", "लेगो", "शतरंज", "सांप और सीढ़ी", "scrabble", "विडियो गेम",  "गुड़ियाँ", "action फिगर्स", "सॉफ्ट टॉयज़", "कार", "ट्रक", "आर्ट-क्राफ्ट", "मेरे साथ खेलो"};
    public static String[] fun_outdoor_games_text =
            {"खेल का मैदान", "पार्क", "झूला", "स्लाईड", "सी-सॉ", "मेरी-गो-राउंड", "लुकाछिपी", "बल्ला और गेंद", "स्टैचू", "ताला और चाबी", "पकड़ा-पकड़ी", "पतंग", "चोर-पुलिस", "कंचे", "चलना", "सायकल",  "दौड़ना", "तऐरना"};
    public static String[] fun_sports_text =
            {"क्रिकेट ", "badminton", "टेनिस ", "बास्केटबॉल", "dodgeball", "volleyball", "खो-खो", "फुटबॉल ",  "कबड़्डी", "gymnastics", "तऐरना"};
    public static String[] fun_tv_text =
            {"अगला चैनल", "पिछला चैनल", "उँची आवाज़", "धीमी आवाज़"};
    public static String[] fun_music_text =
            {"संगीत बदलना", "नाचना", "उँची आवाज़", "धीमी आवाज़"};
    public static String[] fun_activities_text =
            {"चित्र बनाना", "रंग भरना", "पढ़ना", "लिखना", "आर्ट-क्राफ्ट", "नाटक", "नाचना", "संगीत बजाना"};
    public static String[] learning_animals_birds_text =
            {"कुत्ता", "बिल्ली", "हाथी", "शेर ", "तोता ", "खरगोश ", "गाय", "बथख",  "गधा ", "चींटी", "बाघ", "बंदर", "कबूतर ", "तिलचट्टा", "कौउवा ", "घोड़ा ",  "हिरण ","उल्लू", "भेड़िया ", "लोम्ड़ी", "भालू ", "भेड़ ", "बकरी ", "सुअर ",  "मक्खी", "जिराफ़", "ज़ेब्रा", "मच्छर", "भैन्स", "चूहा", "साँप", "मगरमच्छ ",  "मधुमक्खीँ ", "दरियाई घोड़ा ", "गेंडा", "मछली ", "penguin ", "सील ", "डॉल्फिन ", "व्हेल ",  "शार्क ", "कछुआ ", "चिडिया", "गरुड़", "हॉक", "गिद्ध"};
    public static String[] learning_body_parts_text =
            {"सिर", "बाल", "आँखें", "नाक", "कान", "मुँह", "जीभ", "गर्दन",  "कंधा", "कोहनी", "कला ee", "हाथ", "उंग लियां ", "पीठ", "पेट", "कूल्हे का जोड़",  "घुटना", "घुटिका", "पैर", "पैर की उंग लियां "};
    public static String[] learning_books_text =
            {"सोने के समय की कहानीयाँ", "हास्यमय किताबें", "काव्यमय किताबें", " चित्र कला की किताबें", "कहानियों की किताबें", "चित्रों की किताबें", "जासूसी किताबें", "साहसी किताबें",  "पाठशाला की नोटबुक", "गणित की किताब", "विज्ञान की किताब", "इतिहास की किताब", "भूगोल की किताब", "सामाजिक अध्ययन की किताब ", "अंग्रेज़ी  की किताब", "हिंदी की किताब",  "मराठी की किताब", "पाठ्यपुस्तकें", "पसंदीदा किताब"};
    public static String[] learning_colours_text =
            {"काला", "नीला", "भूरा", "हरा", "लाल", "चाँदी", "सफेद", "पीला",  "सुनहरा", "गुलाबी", "नारंगी ", "जामुनी", "ग्रे"};
    public static String[] learning_shapes_text =
            {"सीधी रेखा ", "आड़ी रेखा", "तिरछी रेखा", "गोल", "आयत", "चौकोर", "त्रिकोण", "तारा",  "दिल", "अ समांतरभुज कोण", "घनाकार", "समअ चतुरभुज कोण", "शट कोन", "अंडाकार", "ईंट", "पंच कोन", "मुक्ताकार"};
    public static String[] learning_stationary_text =
            {"pencil", "pen", "स्केल", "रबर", "शारप्नर", "क्रेयौन", "कोरा कागज", "रंगीन कागज",  "कैंची", "सीसा", "कम्पास", "विभाजक", "स्टेप्लर", "यू-पिन", "सेलो टेप", "compass बॉक्स"};
    public static String[] learning_school_objects_text =
            {"bag", "खाने का डिब्बा", "पानी की बोतल", "compass बॉक्स", "गृहपाठ", "कापी", "पाठ्यपुस्तकें", "यूनिफार्म",  "जूते ", "मोज़े ", "pencil", "pen", "स्केल","रबर", "शारप्नर", "चॉक"};
    public static String[] learning_home_objects_text =
            {"खिड़की", "दरवाज़ा", "पंखा", "lamp", "डेस्क", "अलमारी", "टेबल", "कुर्सी",  "शौचा लय", "रसोईघर", "हॉल", "बेडरूम", "खेलने का कमरा", "बाथरूम", "बालकनी", "पढ़ाई का कमरा", "बिस्तर", "टीवी", "संगणक", "सोफ़ा", "फ्रिज", "माइक्रोवेव", "वॉशिंग मशीन", "vacuum cleaner",  "घड़ी", "ट्यूब लाइट" };
    public static String[] learning_transportation_text =
            {"बस", "स्कूल  बस", "कार", "साइकिल", "रेल गाड़ी", "रिक्शा", "मोटर साइकिल ", "हवाई जहाज़", "जहाज़"};
    public static String[] time_weather_time_text =
            {"समय क्या हुआ हैं?", "आज", "कअल", "कअल", "सुबह", "दोपहर", "शाम", "रात"};
    public static String[] time_weather_day_text =
            {"आज कौन सा दिन है?", "सोमवार", "मन्गल वार", "बुधवार", "गुरूवार", "शुक्रवार", "शनिवार", "रविवार"};
    public static String[] time_weather_month_text =
            {"वर्तमान महीना कौनसा हैं ?", "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई",  "अगस्त", "सितंबर ", "अक्टूबर", "नवंबर", "दिसंबर", "यह महीना", "पिछला महीना", "अगला महीना"};
    public static String[] time_weather_weather_text =
            {"आज का मौसम क्या हैं? ", "गरम", "बरसात", "धुंधला", "तूफ़ानी", "घना", "बर्फीला"};
    public static String[] time_weather_seasons_text =
            {"वर्तमान ऋतु कौनसा हैं? ", "वसंत ऋतु", "ग्रीष्म ऋतु", "वर्षा ऋतु", "शरद ऋतु", "शीत ऋतु" };
    public static String[] time_weather_holidays_festivals_text =
            {"दिवाली", "गणेश चतुर्थी", "क्रिसमस ", "दशहरा", "मकर संक्रांति", "होली", "ईद", "गुड फ्राइडे",  "गुड़ी पाड़वा", "गणतंत्र दिवस", "स्वतंत्रता दिवस", "नया साल"};
    public static String[] time_weather_brthdays_text =
            {"मेरा जन्मदिन", "माँ का जन्मदिन", "पिताजी का जन्मदिन", "भाई का जन्मदिन", "बहन का जन्मदिन", "बड़े पापा का जन्मदिन", "बड़ी मम्मी का जन्मदिन", "दादाजी का जन्मदिन",  "दादी माँ का जन्मदिन","नानाजी का जन्मदिन","नानी माँ का जन्मदिन","चाचा का जन्मदिन", "चाची का जन्मदिन", "मामा का जन्मदिन", "मामी का जन्मदिन","बुआ का जन्मदिन","फ़ुफ़ा का जन्मदिन","मौसी का जन्मदिन", "मौसा का जन्मदिन","मित्र का जन्मदिन", "शिक्षक का जन्मदिन"};

    DataBaseHelper myDbHelper;
    public static Integer[] count = new Integer[100];
    int[] sort = new int[100];
    int count_flag = 0;
    private SessionManager session;
    int location=-1;
    int locayy=-2;
    float dpHeight;
    int sr, bw;

    String[] side = new String[100];
    String[] below = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable( getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setElevation(0);
        session = new SessionManager(getApplicationContext());
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        more_count = 0;

        if (dpHeight >= 720) {
            sr = 0;
            bw = 15;
        }
        else
        {
            sr = 0;
            bw = 7;
        }

        final String[] side_hindi ={"अच्छा लगता हैं", "सच में अच्छा लगता हैं", "हाँ", "सच में हाँ", "ज़्यादा", "सच में ज़्यादा", "अच्छा नहीं लगता हैं", "सच में अच्छा नहीं लगता हैं", "नहीं", "सच में नहीं", "कम", "सच में कम"};
        final String[] side_english ={"like", "really like", "yes", "really yes", "mohrr", "really mohrr", "dont like", "really dont like", "no o", "really no", "less", "really less"};

        final String[] below_hindi ={"होम", "वापस", "कीबोर्ड"};
        final String[] below_english ={"Home", "back", "keyboard"};

        if (session.getLanguage()==1){
            side = side_hindi;
            below = below_hindi;
        }

        myDbHelper = new DataBaseHelper(this);
        session = new SessionManager(getApplicationContext());
        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setEngineByPackageName("com.google.android.tts");
                    new LongOperation().execute("");

                }
            }
        });

        tts.setSpeechRate((float) session.getSpeed()/50);
        tts.setPitch((float) session.getPitch()/50);

        myMusic = new String[100];

        like = (ImageView) findViewById(R.id.ivlike);
        dislike = (ImageView) findViewById(R.id.ivdislike);
        add = (ImageView) findViewById(R.id.ivadd);
        minus = (ImageView) findViewById(R.id.ivminus);
        yes = (ImageView) findViewById(R.id.ivyes);
        no = (ImageView) findViewById(R.id.ivno);
        home = (ImageView) findViewById(R.id.ivhome);
        back = (ImageView) findViewById(R.id.ivback);
        back.setAlpha(1f);
        Intent i = getIntent();
        layer_1_id = i.getExtras().getInt("layer_1_id");
        layer_2_id = i.getExtras().getInt("layer_2_id");

        keyboard = (ImageView) findViewById(R.id.keyboard);
        et = (EditText) findViewById(R.id.et);
        et.setVisibility(View.INVISIBLE);

        ttsButton = (ImageView)findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);

        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make to the field non-editable
        et.setKeyListener(null);
        if (layer_1_id == 0){
            if (layer_2_id == 0 || layer_2_id == 2 || layer_2_id == 3 ) {
                like.setAlpha(0.5f);
                dislike.setAlpha(0.5f);
                add.setAlpha(0.5f);
                minus.setAlpha(0.5f);
                yes.setAlpha(0.5f);
                no.setAlpha(0.5f);
                like.setEnabled(false);
                dislike.setEnabled(false);
                add.setEnabled(false);
                minus.setEnabled(false);
                yes.setEnabled(false);
                no.setEnabled(false);
            }}

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.setImageResource(R.drawable.homepressed);
                tts.speak(below[0], TextToSpeech.QUEUE_FLUSH, null);
                more_count = 0;
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

      //  listView = (ListView) findViewById(R.id.android_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgad = new ImageAdapter(Layer_3_Hindi_Activity.this);

        //imgad = new ImageAdapter(Main2LAyer.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        String savedString = myDbHelper.getlevel(layer_1_id, layer_2_id);
        Log.e("vygbkj",savedString+"");
        if (!savedString.equals("false")) {

            count_flag = 1;
            StringTokenizer st = new StringTokenizer(savedString, ",");
            System.out.println("ryty" + layer_3_speech[layer_1_id][layer_2_id].length);
            count = new Integer[layer_3_speech[layer_1_id][layer_2_id].length];
            Log.d("counting ",count.length+"");
            for (int j = 0; j < layer_3_speech[layer_1_id][layer_2_id].length; j++) {

                count[j] = Integer.parseInt(st.nextToken());
                System.out.println("count " + j + " "+ count[j]);
            }

            IndexSorter<Integer> is = new IndexSorter<Integer>(count);

            is.sort();

            System.out.print("Unsorted: ");

            for (Integer ij : count) {

                System.out.print(ij);

                System.out.print("\t");

            }

            System.out.println();

            System.out.print("Sorted");
            Integer[] indexes = new Integer[layer_3_speech[layer_1_id][layer_2_id].length];
            int g = 0;

            for (Integer ij : is.getIndexes()) {

                indexes[g] = ij;
                g++;
                System.out.print(ij);

                System.out.print("\t");

            }

            for (int j = 0; j < count.length; j++) {
                sort[j] = indexes[j];
            }

            for (int j = 0; j < count.length; j++) {
                System.out.println("Sorted" + sort[j]);
            }
myMusic_function(layer_1_id,layer_2_id);
            recyclerView.setAdapter(new Layer_3_Hindi_Adapter(this, layer_1_id, layer_2_id, sort));
        }else
        if((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4))  || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id ==1 || layer_2_id ==2 || layer_2_id ==3 || layer_2_id ==4 )))
        {
            myMusic_function(layer_1_id, layer_2_id);
            recyclerView.setAdapter(new Layer_3_Hindi_Adapter(this, layer_1_id, layer_2_id, sort));
        }else {
            count_flag = 0;
            myMusic_function(layer_1_id, layer_2_id);}


        final int[] prev_pos = {-1};
        final int[] cko = {-1};
        final View[] xx = {null};
        final int[] ix = {-1};

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(final View view, final int position) {
                        mLinearLayoutIconOne = (LinearLayout)view.findViewById(R.id.linearlayout_icon1);
                        mLinearLayoutIconTwo = (LinearLayout)view.findViewById(R.id.linearlayout_icon2);
                        mLinearLayoutIconThree = (LinearLayout)view.findViewById(R.id.linearlayout_icon3);


                        if(xx[0]!=null && xx[0]!=view) {
                            notifyDataSet(xx[0]);
                        }

                        if (session.getGridSize()==0) {
                            im1 = (CircularImageView) view.findViewById(R.id.icon1);
                            im2 = (CircularImageView) view.findViewById(R.id.icon2);
                            im3 = (CircularImageView) view.findViewById(R.id.icon3);

                            Log.d("Position", position + " " + prev_pos[0]);
                            mLinearLayoutIconOne.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    unset();
                                    im1.setBorderColor(-1283893945);
                                    im1.setShadowColor(-1283893945);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(bw);

                                    im2.setBorderColor(-1);
                                    im2.setShadowColor(0);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(0);

                                    im3.setBorderColor(-1);
                                    im3.setShadowColor(0);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(0);
                                    cko[0]++;
                                    locayy = position * 3;
                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);


                                }
                            });
                            mLinearLayoutIconTwo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    unset();
                                    im2.setBorderColor(-1283893945);
                                    im2.setShadowColor(-1283893945);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(bw);

                                    im1.setBorderColor(-1);
                                    im1.setShadowColor(0);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(0);

                                    im3.setBorderColor(-1);
                                    im3.setShadowColor(0);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(0);
                                    cko[0]++;
                                    locayy = position * 3 + 1;
                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);

                                }
                            });
                            mLinearLayoutIconThree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    unset();
                                    im3.setBorderColor(-1283893945);
                                    im3.setShadowColor(-1283893945);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(bw);

                                    im2.setBorderColor(-1);
                                    im2.setShadowColor(0);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(0);

                                    im1.setBorderColor(-1);
                                    im1.setShadowColor(0);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(0);
                                    cko[0]++;
                                    locayy = position * 3 + 2;

                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    Log.d("checkloc", locayy + " location " + location);
                                    location = locayy;


                                }
                            });

                            prev_pos[0] = position;
                            xx[0] = view;

                            flag = 1;
                            cy = 0;
                            cm = 0;
                            cd = 0;
                            cn = 0;
                            cl = 0;
                            clike = 0;
                        }

                        if (session.getGridSize()==1) {
                            im1 = (CircularImageView) view.findViewById(R.id.icon1);
                            im2 = (CircularImageView) view.findViewById(R.id.icon2);
                            im3 = (CircularImageView) view.findViewById(R.id.icon3);
                            im4 = (CircularImageView) view.findViewById(R.id.icon4);
                            im5 = (CircularImageView) view.findViewById(R.id.icon5);
                            im6 = (CircularImageView) view.findViewById(R.id.icon6);
                            im7 = (CircularImageView) view.findViewById(R.id.icon7);
                            im8 = (CircularImageView) view.findViewById(R.id.icon8);
                            im9 = (CircularImageView) view.findViewById(R.id.icon9);

                            Log.d("Position", position + " " + prev_pos[0]);
                            im1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    unset();
                                    im1.setBorderColor(-1283893945);
                                    im1.setShadowColor(-1283893945);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(bw);

                                    im2.setBorderColor(-1);
                                    im2.setShadowColor(0);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(0);

                                    im3.setBorderColor(-1);
                                    im3.setShadowColor(0);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(0);

                                    im4.setBorderColor(-1);
                                    im4.setShadowColor(0);
                                    im4.setShadowRadius(sr);
                                    im4.setBorderWidth(0);

                                    im5.setBorderColor(-1);
                                    im5.setShadowColor(0);
                                    im5.setShadowRadius(sr);
                                    im5.setBorderWidth(0);

                                    im6.setBorderColor(-1);
                                    im6.setShadowColor(0);
                                    im6.setShadowRadius(sr);
                                    im6.setBorderWidth(0);

                                    im7.setBorderColor(-1);
                                    im7.setShadowColor(0);
                                    im7.setShadowRadius(sr);
                                    im7.setBorderWidth(0);

                                    im8.setBorderColor(-1);
                                    im8.setShadowColor(0);
                                    im8.setShadowRadius(sr);
                                    im8.setBorderWidth(0);

                                    im9.setBorderColor(-1);
                                    im9.setShadowColor(0);
                                    im9.setShadowRadius(sr);
                                    im9.setBorderWidth(0);

                                    cko[0]++;
                                    locayy = position * 9;
                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);


                                }
                            });
                            im2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    unset();
                                    im2.setBorderColor(-1283893945);
                                    im2.setShadowColor(-1283893945);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(bw);

                                    im1.setBorderColor(-1);
                                    im1.setShadowColor(0);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(0);

                                    im3.setBorderColor(-1);
                                    im3.setShadowColor(0);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(0);

                                    im4.setBorderColor(-1);
                                    im4.setShadowColor(0);
                                    im4.setShadowRadius(sr);
                                    im4.setBorderWidth(0);

                                    im5.setBorderColor(-1);
                                    im5.setShadowColor(0);
                                    im5.setShadowRadius(sr);
                                    im5.setBorderWidth(0);

                                    im6.setBorderColor(-1);
                                    im6.setShadowColor(0);
                                    im6.setShadowRadius(sr);
                                    im6.setBorderWidth(0);

                                    im7.setBorderColor(-1);
                                    im7.setShadowColor(0);
                                    im7.setShadowRadius(sr);
                                    im7.setBorderWidth(0);

                                    im8.setBorderColor(-1);
                                    im8.setShadowColor(0);
                                    im8.setShadowRadius(sr);
                                    im8.setBorderWidth(0);

                                    im9.setBorderColor(-1);
                                    im9.setShadowColor(0);
                                    im9.setShadowRadius(sr);
                                    im9.setBorderWidth(0);

                                    cko[0]++;
                                    locayy = position * 9 + 1;
                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);

                                }
                            });
                            im3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    unset();
                                    im3.setBorderColor(-1283893945);
                                    im3.setShadowColor(-1283893945);
                                    im3.setShadowRadius(sr);
                                    im3.setBorderWidth(bw);

                                    im2.setBorderColor(-1);
                                    im2.setShadowColor(0);
                                    im2.setShadowRadius(sr);
                                    im2.setBorderWidth(0);

                                    im1.setBorderColor(-1);
                                    im1.setShadowColor(0);
                                    im1.setShadowRadius(sr);
                                    im1.setBorderWidth(0);

                                    im4.setBorderColor(-1);
                                    im4.setShadowColor(0);
                                    im4.setShadowRadius(sr);
                                    im4.setBorderWidth(0);

                                    im5.setBorderColor(-1);
                                    im5.setShadowColor(0);
                                    im5.setShadowRadius(sr);
                                    im5.setBorderWidth(0);

                                    im6.setBorderColor(-1);
                                    im6.setShadowColor(0);
                                    im6.setShadowRadius(sr);
                                    im6.setBorderWidth(0);

                                    im7.setBorderColor(-1);
                                    im7.setShadowColor(0);
                                    im7.setShadowRadius(sr);
                                    im7.setBorderWidth(0);

                                    im8.setBorderColor(-1);
                                    im8.setShadowColor(0);
                                    im8.setShadowRadius(sr);
                                    im8.setBorderWidth(0);

                                    im9.setBorderColor(-1);
                                    im9.setShadowColor(0);
                                    im9.setShadowRadius(sr);
                                    im9.setBorderWidth(0);

                                    cko[0]++;
                                    locayy = position * 9 + 2;

                                    ix[0] = sort[locayy];
                                    if (layer_1_id == 0 && layer_2_id == 1) {
                                        if (ix[0] == 0) {
                                            no.setAlpha(0.5f);
                                            no.setEnabled(false);
                                            yes.setAlpha(1f);
                                            yes.setEnabled(true);

                                        } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                            yes.setAlpha(0.5f);
                                            yes.setEnabled(false);
                                            no.setAlpha(1f);
                                            no.setEnabled(true);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }
                                    if (layer_1_id == 1 && layer_2_id == 3) {
                                        if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                        ix[0] = locayy;
                                        if (ix[0] == 0) {
                                            like.setAlpha(0.5f);
                                            dislike.setAlpha(0.5f);
                                            add.setAlpha(0.5f);
                                            minus.setAlpha(0.5f);
                                            yes.setAlpha(0.5f);
                                            no.setAlpha(0.5f);
                                            like.setEnabled(false);
                                            dislike.setEnabled(false);
                                            add.setEnabled(false);
                                            minus.setEnabled(false);
                                            yes.setEnabled(false);
                                            no.setEnabled(false);
                                        } else {
                                            like.setAlpha(1f);
                                            dislike.setAlpha(1f);
                                            add.setAlpha(1f);
                                            minus.setAlpha(1f);
                                            yes.setAlpha(1f);
                                            no.setAlpha(1f);
                                            like.setEnabled(true);
                                            dislike.setEnabled(true);
                                            add.setEnabled(true);
                                            minus.setEnabled(true);
                                            yes.setEnabled(true);
                                            no.setEnabled(true);
                                        }
                                    }

                                    if (count_flag == 1) {
                                        count[sort[locayy]] = count[sort[locayy]] + 1;
                                        StringBuilder str = new StringBuilder();
                                        for (int j = 0; j < count.length; j++) {
                                            str.append(count[j]).append(",");
                                        }
                                        System.out.println("dgh" + str.toString());
                                        myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                    }
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else

                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    Log.d("checkloc", locayy + " location " + location);
                                    location = locayy;


                                }
                            });

                        im4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                unset();
                                im4.setBorderColor(-1283893945);
                                im4.setShadowColor(-1283893945);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(bw);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im5.setBorderColor(-1);
                                im5.setShadowColor(0);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(0);

                                im6.setBorderColor(-1);
                                im6.setShadowColor(0);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(0);

                                im7.setBorderColor(-1);
                                im7.setShadowColor(0);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(0);

                                im8.setBorderColor(-1);
                                im8.setShadowColor(0);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(0);

                                im9.setBorderColor(-1);
                                im9.setShadowColor(0);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9+3;
                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

                                location = locayy;
                                Log.d("checkloc", locayy + " location " + location);


                            }
                        });
                        im5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unset();
                                im5.setBorderColor(-1283893945);
                                im5.setShadowColor(-1283893945);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(bw);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                im4.setBorderColor(-1);
                                im4.setShadowColor(0);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(0);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im6.setBorderColor(-1);
                                im6.setShadowColor(0);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(0);

                                im7.setBorderColor(-1);
                                im7.setShadowColor(0);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(0);

                                im8.setBorderColor(-1);
                                im8.setShadowColor(0);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(0);

                                im9.setBorderColor(-1);
                                im9.setShadowColor(0);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9 + 4;
                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                location = locayy;
                                Log.d("checkloc", locayy + " location " + location);

                            }
                        });
                        im6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unset();
                                im6.setBorderColor(-1283893945);
                                im6.setShadowColor(-1283893945);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(bw);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im4.setBorderColor(-1);
                                im4.setShadowColor(0);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(0);

                                im5.setBorderColor(-1);
                                im5.setShadowColor(0);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                im7.setBorderColor(-1);
                                im7.setShadowColor(0);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(0);

                                im8.setBorderColor(-1);
                                im8.setShadowColor(0);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(0);

                                im9.setBorderColor(-1);
                                im9.setShadowColor(0);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9 + 5;

                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                Log.d("checkloc", locayy + " location " + location);
                                location = locayy;


                            }
                        });

                        im7.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                unset();
                                im7.setBorderColor(-1283893945);
                                im7.setShadowColor(-1283893945);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(bw);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im4.setBorderColor(-1);
                                im4.setShadowColor(0);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(0);

                                im5.setBorderColor(-1);
                                im5.setShadowColor(0);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(0);

                                im6.setBorderColor(-1);
                                im6.setShadowColor(0);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                im8.setBorderColor(-1);
                                im8.setShadowColor(0);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(0);

                                im9.setBorderColor(-1);
                                im9.setShadowColor(0);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9+6;
                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

                                location = locayy;
                                Log.d("checkloc", locayy + " location " + location);


                            }
                        });
                        im8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unset();
                                im8.setBorderColor(-1283893945);
                                im8.setShadowColor(-1283893945);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(bw);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                im4.setBorderColor(-1);
                                im4.setShadowColor(0);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(0);

                                im5.setBorderColor(-1);
                                im5.setShadowColor(0);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(0);

                                im6.setBorderColor(-1);
                                im6.setShadowColor(0);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(0);

                                im7.setBorderColor(-1);
                                im7.setShadowColor(0);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(0);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im9.setBorderColor(-1);
                                im9.setShadowColor(0);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9 + 7;
                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                location = locayy;
                                Log.d("checkloc", locayy + " location " + location);

                            }
                        });
                        im9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unset();
                                im9.setBorderColor(-1283893945);
                                im9.setShadowColor(-1283893945);
                                im9.setShadowRadius(sr);
                                im9.setBorderWidth(bw);

                                im2.setBorderColor(-1);
                                im2.setShadowColor(0);
                                im2.setShadowRadius(sr);
                                im2.setBorderWidth(0);

                                im1.setBorderColor(-1);
                                im1.setShadowColor(0);
                                im1.setShadowRadius(sr);
                                im1.setBorderWidth(0);

                                im4.setBorderColor(-1);
                                im4.setShadowColor(0);
                                im4.setShadowRadius(sr);
                                im4.setBorderWidth(0);

                                im5.setBorderColor(-1);
                                im5.setShadowColor(0);
                                im5.setShadowRadius(sr);
                                im5.setBorderWidth(0);

                                im6.setBorderColor(-1);
                                im6.setShadowColor(0);
                                im6.setShadowRadius(sr);
                                im6.setBorderWidth(0);

                                im7.setBorderColor(-1);
                                im7.setShadowColor(0);
                                im7.setShadowRadius(sr);
                                im7.setBorderWidth(0);

                                im8.setBorderColor(-1);
                                im8.setShadowColor(0);
                                im8.setShadowRadius(sr);
                                im8.setBorderWidth(0);

                                im3.setBorderColor(-1);
                                im3.setShadowColor(0);
                                im3.setShadowRadius(sr);
                                im3.setBorderWidth(0);

                                cko[0]++;
                                locayy = position * 9+8;

                                ix[0] = sort[locayy];
                                if (layer_1_id == 0 && layer_2_id == 1) {
                                    if (ix[0] == 0) {
                                        no.setAlpha(0.5f);
                                        no.setEnabled(false);
                                        yes.setAlpha(1f);
                                        yes.setEnabled(true);

                                    } else if (ix[0] == 1 || ix[0] == 2 || ix[0] == 3 || ix[0] == 5 || ix[0] == 6 || ix[0] == 7 || ix[0] == 8 || ix[0] == 9 || ix[0] == 10 || ix[0] == 11 || ix[0] == 12 || ix[0] == 15 || ix[0] == 16) {
                                        yes.setAlpha(0.5f);
                                        yes.setEnabled(false);
                                        no.setAlpha(1f);
                                        no.setEnabled(true);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }
                                if (layer_1_id == 1 && layer_2_id == 3) {
                                    if (ix[0] == 34 || ix[0] == 35 || ix[0] == 36 || ix[0] == 37) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                } else if (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4)) {
                                    ix[0] = locayy;
                                    if (ix[0] == 0) {
                                        like.setAlpha(0.5f);
                                        dislike.setAlpha(0.5f);
                                        add.setAlpha(0.5f);
                                        minus.setAlpha(0.5f);
                                        yes.setAlpha(0.5f);
                                        no.setAlpha(0.5f);
                                        like.setEnabled(false);
                                        dislike.setEnabled(false);
                                        add.setEnabled(false);
                                        minus.setEnabled(false);
                                        yes.setEnabled(false);
                                        no.setEnabled(false);
                                    } else {
                                        like.setAlpha(1f);
                                        dislike.setAlpha(1f);
                                        add.setAlpha(1f);
                                        minus.setAlpha(1f);
                                        yes.setAlpha(1f);
                                        no.setAlpha(1f);
                                        like.setEnabled(true);
                                        dislike.setEnabled(true);
                                        add.setEnabled(true);
                                        minus.setEnabled(true);
                                        yes.setEnabled(true);
                                        no.setEnabled(true);
                                    }
                                }

                                if (count_flag == 1) {
                                    count[sort[locayy]] = count[sort[locayy]] + 1;
                                    StringBuilder str = new StringBuilder();
                                    for (int j = 0; j < count.length; j++) {
                                        str.append(count[j]).append(",");
                                    }
                                    System.out.println("dgh" + str.toString());
                                    myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
                                }
                                if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                    tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                } else

                                    tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                Log.d("checkloc", locayy + " location " + location);
                                location = locayy;


                            }
                        });


                            prev_pos[0] = position;
                            xx[0] = view;
                            //                                       location = locayy;

                            flag = 1;
                            cy = 0;
                            cm = 0;
                            cd = 0;
                            cn = 0;
                            cl = 0;
                            clike = 0;


                        }}


            private void unset()
            {
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
            }

                    private void notifyDataSet(View view) {
//                        Toast.makeText(MainActivity.this,"here",Toast.LENGTH_SHORT).show();
                        if (session.getGridSize()==0) {
                            im1 = (CircularImageView) view.findViewById(R.id.icon1);
                            im2 = (CircularImageView) view.findViewById(R.id.icon2);
                            im3 = (CircularImageView) view.findViewById(R.id.icon3);

                            im2.setBorderColor(-1);
                            im2.setShadowColor(0);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(0);

                            im1.setBorderColor(-1);
                            im1.setShadowColor(0);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(0);

                            im3.setBorderColor(-1);
                            im3.setShadowColor(0);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(0);


                        }
                        if (session.getGridSize()==1){
                            im1 = (CircularImageView) view.findViewById(R.id.icon1);
                            im2 = (CircularImageView) view.findViewById(R.id.icon2);
                            im3 = (CircularImageView) view.findViewById(R.id.icon3);
                            im4 = (CircularImageView) view.findViewById(R.id.icon4);
                            im5 = (CircularImageView) view.findViewById(R.id.icon5);
                            im6 = (CircularImageView) view.findViewById(R.id.icon6);
                            im7 = (CircularImageView) view.findViewById(R.id.icon7);
                            im8 = (CircularImageView) view.findViewById(R.id.icon8);
                            im9 = (CircularImageView) view.findViewById(R.id.icon9);

                            im1.setBorderColor(-1);
                            im1.setShadowColor(0);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(0);

                            im2.setBorderColor(-1);
                            im2.setShadowColor(0);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(0);

                            im3.setBorderColor(-1);
                            im3.setShadowColor(0);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(0);

                            im5.setBorderColor(-1);
                            im5.setShadowColor(0);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(0);

                            im6.setBorderColor(-1);
                            im6.setShadowColor(0);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(0);

                            im7.setBorderColor(-1);
                            im7.setShadowColor(0);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(0);

                            im8.setBorderColor(-1);
                            im8.setShadowColor(0);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(0);

                            im4.setBorderColor(-1);
                            im4.setShadowColor(0);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(0);

                            im9.setBorderColor(-1);
                            im9.setShadowColor(0);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(0);
                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                }
                )

        );
        imgad.notifyDataSetChanged();

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tts.speak(below[2], TextToSpeech.QUEUE_FLUSH, null);

                if (flag_keyboard  == 1)
                {
                    keyboard.setImageResource(R.drawable.keyboard_button);
                    back.setImageResource(R.drawable.back_button);
                    et.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    ttsButton.setVisibility(View.INVISIBLE);
                    flag_keyboard = 0;
                    like.setEnabled(true);
                    dislike.setEnabled(true);
                    add.setEnabled(true);
                    minus.setEnabled(true);
                    yes.setEnabled(true);
                    no.setEnabled(true);
                    like.setAlpha(1f);
                    dislike.setAlpha(1f);
                    add.setAlpha(1f);
                    minus.setAlpha(1f);
                    yes.setAlpha(1f);
                    no.setAlpha(1f);
                }

                else {

                    keyboard.setImageResource(R.drawable.keyboardpressed);
                    back.setImageResource(R.drawable.backpressed);
                    et.setVisibility(View.VISIBLE);

                    et.setKeyListener(originalKeyListener);
                    // Focus to the field.
                    recyclerView.setVisibility(View.INVISIBLE);
                    like.setAlpha(0.5f);
                    dislike.setAlpha(0.5f);
                    add.setAlpha(0.5f);
                    minus.setAlpha(0.5f);
                    yes.setAlpha(0.5f);
                    no.setAlpha(0.5f);
                    like.setEnabled(false);
                    dislike.setEnabled(false);
                    add.setEnabled(false);
                    minus.setEnabled(false);
                    yes.setEnabled(false);
                    no.setEnabled(false);
                    et.requestFocus();
                    ttsButton.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    flag_keyboard = 1;
                }
            }
        });

        ttsButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {

                        tts.setSpeechRate((float) session.getSpeed()/50);
                        tts.setPitch((float) session.getPitch()/50);
                        String s1 = et.getText().toString();
                        tts.speak(s1, TextToSpeech.QUEUE_FLUSH, null);

                        like.setEnabled(false);
                        dislike.setEnabled(false);
                        add.setEnabled(false);
                        minus.setEnabled(false);
                        yes.setEnabled(false);
                        no.setEnabled(false);

                    }
                });


        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...
                if (!hasFocus) {
                    // Hithe soft keyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    // Make it non-editable again.
                    et.setKeyListener(null);
                }
            }
        });

        back.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        tts.speak(below[1], TextToSpeech.QUEUE_FLUSH, null);
                        if (flag_keyboard == 1)
                        {
                            keyboard.setImageResource(R.drawable.keyboard_button);
                            back.setImageResource(R.drawable.back_button);
                            et.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            ttsButton.setVisibility(View.INVISIBLE);
                            flag_keyboard = 0;
                            like.setEnabled(true);
                            dislike.setEnabled(true);
                            add.setEnabled(true);
                            minus.setEnabled(true);
                            yes.setEnabled(true);
                            no.setEnabled(true);
                            like.setAlpha(1f);
                            dislike.setAlpha(1f);
                            add.setAlpha(1f);
                            minus.setAlpha(1f);
                            yes.setAlpha(1f);
                            no.setAlpha(1f);
                        } else if (more_count > 0) {
                            more_count  -= 1;
                            myMusic_function(layer_1_id, layer_2_id);
                           recyclerView.setAdapter(new Layer_3_Hindi_Adapter(Layer_3_Hindi_Activity.this, layer_1_id, layer_2_id,sort));
                            x = -1;
                        } else {
                            back.setImageResource(R.drawable.backpressed);
                            Intent i = new Intent(getApplicationContext(), Main2LAyer.class);
                            i.putExtra("id", layer_1_id);
                            startActivity(i);
                        }
                    }
                });


            like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cy = 0;
                cm = 0;
                cd = 0;
                cn = 0;
                cl = 0;
                image_flag = 0;
                like.setImageResource(R.drawable.ilikewithoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                if (flag == 0) {
                    if (clike == 1) {
                        tts.speak(side[1], TextToSpeech.QUEUE_FLUSH, null);
                        clike = 0;
                    } else {
                        tts.speak(side[0], TextToSpeech.QUEUE_FLUSH, null);
                        clike = 1;
                    }
                }else {
                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[0]);
                        im1.setShadowColor(color[0]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[0]);
                        im2.setShadowColor(color[0]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[0]);
                        im3.setShadowColor(color[0]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }
                    if (clike == 1) {

                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][1], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][1], TextToSpeech.QUEUE_FLUSH, null);
                        clike = 0;

                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][0], TextToSpeech.QUEUE_FLUSH, null);
                        else
                           tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][0], TextToSpeech.QUEUE_FLUSH, null);
                        clike = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[0]);
                            im1.setShadowColor(color[0]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[0]);
                            im2.setShadowColor(color[0]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[0]);
                            im3.setShadowColor(color[0]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        } if(location%9==3)
                        {
                            im4.setBorderColor(color[0]);
                            im4.setShadowColor(color[0]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[0]);
                            im5.setShadowColor(color[0]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[0]);
                            im6.setShadowColor(color[0]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        } if(location%9==6)
                        {
                            im7.setBorderColor(color[0]);
                            im7.setShadowColor(color[0]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[0]);
                            im8.setShadowColor(color[0]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[0]);
                            im9.setShadowColor(color[0]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (clike == 1) {

                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][1], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][1], TextToSpeech.QUEUE_FLUSH, null);
                            clike = 0;

                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][0], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][0], TextToSpeech.QUEUE_FLUSH, null);
                            clike = 1;
                        }
                    }
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clike = 0;
                cy = 0;
                cm = 0;
                cn = 0;
                cl = 0;
                image_flag = 1;
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithoutline);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                if (flag == 0) {
                    if (cd == 1) {
                        tts.speak(side[7], TextToSpeech.QUEUE_FLUSH, null);
                        cd = 0;
                    } else {
                        tts.speak(side[6], TextToSpeech.QUEUE_FLUSH, null);
                        cd = 1;
                    }
                } else {

                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[1]);
                        im1.setShadowColor(color[1]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[1]);
                        im2.setShadowColor(color[1]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[1]);
                        im3.setShadowColor(color[1]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }
                    if (cd == 1) {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][7], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][7], TextToSpeech.QUEUE_FLUSH, null);
                        cd = 0;
                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][6], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][6], TextToSpeech.QUEUE_FLUSH, null);
                        cd = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[1]);
                            im1.setShadowColor(color[1]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[1]);
                            im2.setShadowColor(color[1]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[1]);
                            im3.setShadowColor(color[1]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if(location%9==3)
                        {
                            im4.setBorderColor(color[1]);
                            im4.setShadowColor(color[1]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[1]);
                            im5.setShadowColor(color[1]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[1]);
                            im6.setShadowColor(color[1]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if(location%9==6)
                        {
                            im7.setBorderColor(color[1]);
                            im7.setShadowColor(color[1]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[1]);
                            im8.setShadowColor(color[1]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[1]);
                            im9.setShadowColor(color[1]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (cd == 1) {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][7], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][7], TextToSpeech.QUEUE_FLUSH, null);
                            cd = 0;
                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][6], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][6], TextToSpeech.QUEUE_FLUSH, null);
                            cd = 1;
                        }
                    }
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clike = 0; cm = 0; cd = 0; cn = 0; cl = 0;
                image_flag = 2;
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithoutline);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                if (flag == 0) {
                    if (cy == 1) {
                        tts.speak(side[3], TextToSpeech.QUEUE_FLUSH, null);
                        cy = 0;
                    } else {
                        tts.speak(side[2], TextToSpeech.QUEUE_FLUSH, null);
                        cy = 1;
                    }
                }else {
                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[2]);
                        im1.setShadowColor(color[2]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[2]);
                        im2.setShadowColor(color[2]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[2]);
                        im3.setShadowColor(color[2]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }                    if (cy == 1) {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][3], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][3], TextToSpeech.QUEUE_FLUSH, null);
                        cy = 0;
                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][2], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][2], TextToSpeech.QUEUE_FLUSH, null);
                        cy = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[2]);
                            im1.setShadowColor(color[2]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[2]);
                            im2.setShadowColor(color[2]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[2]);
                            im3.setShadowColor(color[2]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if(location%9==3)
                        {
                            im4.setBorderColor(color[2]);
                            im4.setShadowColor(color[2]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[2]);
                            im5.setShadowColor(color[2]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[2]);
                            im6.setShadowColor(color[2]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if(location%9==6)
                        {
                            im7.setBorderColor(color[2]);
                            im7.setShadowColor(color[2]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[2]);
                            im8.setShadowColor(color[2]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[2]);
                            im9.setShadowColor(color[2]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (cy == 1) {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][3], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][3], TextToSpeech.QUEUE_FLUSH, null);
                            cy = 0;
                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][2], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][2], TextToSpeech.QUEUE_FLUSH, null);
                            cy = 1;
                        }
                    }
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clike = 0; cy = 0; cm = 0; cd = 0; cl = 0;
                image_flag = 3;
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithoutline);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithout);
                if (flag == 0) {
                    if (cn == 1) {
                        tts.speak(side[9], TextToSpeech.QUEUE_FLUSH, null);
                        cn = 0;
                    } else {
                        tts.speak(side[8], TextToSpeech.QUEUE_FLUSH, null);
                        cn = 1;
                    }
                }else {
                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[3]);
                        im1.setShadowColor(color[3]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[3]);
                        im2.setShadowColor(color[3]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[3]);
                        im3.setShadowColor(color[3]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }
                    if (cn == 1) {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][9], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][9], TextToSpeech.QUEUE_FLUSH, null);
                        cn = 0;
                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][8], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][8], TextToSpeech.QUEUE_FLUSH, null);
                        cn = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[3]);
                            im1.setShadowColor(color[3]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[3]);
                            im2.setShadowColor(color[3]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[3]);
                            im3.setShadowColor(color[3]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }if(location%9==3)
                        {
                            im4.setBorderColor(color[3]);
                            im4.setShadowColor(color[3]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[3]);
                            im5.setShadowColor(color[3]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[3]);
                            im6.setShadowColor(color[3]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }if(location%9==6)
                        {
                            im7.setBorderColor(color[3]);
                            im7.setShadowColor(color[3]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[3]);
                            im8.setShadowColor(color[3]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[3]);
                            im9.setShadowColor(color[3]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (cn == 1) {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][9], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][9], TextToSpeech.QUEUE_FLUSH, null);
                            cn = 0;
                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][8], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][8], TextToSpeech.QUEUE_FLUSH, null);
                            cn = 1;
                        }
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clike = 0; cy = 0; cd = 0; cn = 0; cl = 0;
                image_flag = 4;
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithoutline);
                minus.setImageResource(R.drawable.lesswithout);
                if (flag == 0) {
                    if (cm == 1) {
                        tts.speak(side[5], TextToSpeech.QUEUE_FLUSH, null);
                        cm = 0;
                    } else {
                        tts.speak(side[4], TextToSpeech.QUEUE_FLUSH, null);
                        cm = 1;
                    }
                }else {
                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[4]);
                        im1.setShadowColor(color[4]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[4]);
                        im2.setShadowColor(color[4]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[4]);
                        im3.setShadowColor(color[4]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }                    if (cm == 1) {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][5], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][5], TextToSpeech.QUEUE_FLUSH, null);
                        cm = 0;
                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][4], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][4], TextToSpeech.QUEUE_FLUSH, null);
                        cm = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[4]);
                            im1.setShadowColor(color[4]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[4]);
                            im2.setShadowColor(color[4]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[4]);
                            im3.setShadowColor(color[4]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if(location%9==3)
                        {
                            im4.setBorderColor(color[4]);
                            im4.setShadowColor(color[4]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[4]);
                            im5.setShadowColor(color[4]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[4]);
                            im6.setShadowColor(color[4]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if(location%9==6)
                        {
                            im7.setBorderColor(color[4]);
                            im7.setShadowColor(color[4]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[4]);
                            im8.setShadowColor(color[4]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[4]);
                            im9.setShadowColor(color[4]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (cm == 1) {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][5], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][5], TextToSpeech.QUEUE_FLUSH, null);
                            cm = 0;
                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][4], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][4], TextToSpeech.QUEUE_FLUSH, null);
                            cm = 1;
                        }
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clike = 0; cy = 0; cm = 0; cd = 0; cn = 0;
                image_flag = 5;
                like.setImageResource(R.drawable.ilikewithoutoutline);
                dislike.setImageResource(R.drawable.idontlikewithout);
                yes.setImageResource(R.drawable.iwantwithout);
                no.setImageResource(R.drawable.idontwantwithout);
                add.setImageResource(R.drawable.morewithout);
                minus.setImageResource(R.drawable.lesswithoutline);
                if (flag == 0) {
                    if (cl == 1) {
                        tts.speak(side[11], TextToSpeech.QUEUE_FLUSH, null);
                        cl = 0;
                    } else {
                        tts.speak(side[10], TextToSpeech.QUEUE_FLUSH, null);
                        cl = 1;
                    }
                }else {
                    if (session.getGridSize()==0){
                    if(location%3==0)
                    {
                        im1.setBorderColor(color[5]);
                        im1.setShadowColor(color[5]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    }
                    else
                    if(location%3==1)
                    {
                        im2.setBorderColor(color[5]);
                        im2.setShadowColor(color[5]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    }
                    else
                    if(location%3==2)
                    {
                        im3.setBorderColor(color[5]);
                        im3.setShadowColor(color[5]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

                    }

                    if (cl == 1) {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][11], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][11], TextToSpeech.QUEUE_FLUSH, null);
                        cl = 0;
                    } else {
                        if (count_flag == 1)
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][10], TextToSpeech.QUEUE_FLUSH, null);
                        else
                            tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][10], TextToSpeech.QUEUE_FLUSH, null);
                        cl = 1;
                    }
                }
                    if (session.getGridSize()==1){
                        if(location%9==0)
                        {
                            im1.setBorderColor(color[5]);
                            im1.setShadowColor(color[5]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        }
                        else
                        if(location%9==1)
                        {
                            im2.setBorderColor(color[5]);
                            im2.setShadowColor(color[5]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        }
                        else
                        if(location%9==2)
                        {
                            im3.setBorderColor(color[5]);
                            im3.setShadowColor(color[5]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if(location%9==3)
                        {
                            im4.setBorderColor(color[5]);
                            im4.setShadowColor(color[5]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        }
                        else
                        if(location%9==4)
                        {
                            im5.setBorderColor(color[5]);
                            im5.setShadowColor(color[5]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        }
                        else
                        if(location%9==5)
                        {
                            im6.setBorderColor(color[5]);
                            im6.setShadowColor(color[5]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if(location%9==6)
                        {
                            im7.setBorderColor(color[5]);
                            im7.setShadowColor(color[5]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        }
                        else
                        if(location%9==7)
                        {
                            im8.setBorderColor(color[5]);
                            im8.setShadowColor(color[5]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        }
                        else
                        if(location%9==8)
                        {
                            im9.setBorderColor(color[5]);
                            im9.setShadowColor(color[5]);
                            im9.setShadowRadius(sr);
                            im9.setBorderWidth(bw);

                        }
                        if (cl == 1) {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][11], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][11], TextToSpeech.QUEUE_FLUSH, null);
                            cl = 0;
                        } else {
                            if (count_flag == 1)
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][sort[location]][10], TextToSpeech.QUEUE_FLUSH, null);
                            else
                                tts.speak(layer_3_speech[layer_1_id][layer_2_id][location][10], TextToSpeech.QUEUE_FLUSH, null);
                            cl = 1;
                        }
                    }
                }
            }
        });
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                if (session.getLanguage()==0 /*&& session.getAccent() == 0*/){
                    tts.setLanguage(new Locale("hin", "IND"));
                }
                if (session.getLanguage()==0 /*&& session.getAccent() == 1*/){
                    tts.setLanguage(new Locale("en", "IN"));
                }
                if (session.getLanguage()==0 /*&& session.getAccent() == 2*/){
                    tts.setLanguage(Locale.UK);

                }
                if (session.getLanguage()==1){
                    tts.setLanguage(new Locale("hin", "IND"));
                }
            } catch (Exception e) {
                Thread.interrupted();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    public void myMusic_function(int layer_1_id, int layer_2_id){

        if (layer_1_id == 0){
            if (layer_2_id == 0) {
                // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                myMusic = greet_feel_greetings_text;
            }
            else if (layer_2_id == 1){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = greet_feel_feelings_text;}
            else if (layer_2_id == 2){
                //  for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = greet_feel_requests_text;}
            else if (layer_2_id == 3){
                //  for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = greet_feel_questions_text;}
        }else if (layer_1_id == 1) {
            if (layer_2_id == 0) {
                myMusic = daily_activities_brushing_text;
            }else if (layer_2_id == 1){
                myMusic = daily_activities_toilet_text;
            } else if (layer_2_id == 2){
                myMusic = daily_activities_bathing_text;
            } else if (layer_2_id == 3){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = daily_activities_clothes_access_text;
            } else if (layer_2_id == 4){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = daily_activities_get_ready_text;
            } else if (layer_2_id == 5){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = daily_activities_sleep_text;
            } else if (layer_2_id == 6){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = daily_activities_therapy_text;
            } else if (layer_2_id == 7){
                myMusic = daily_activities_morning_schedule_text;
            } else if (layer_2_id == 8){
                myMusic = daily_activities_bedtime_schedule_text;
            }
        } else if (layer_1_id == 2){
            if (layer_2_id == 0){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = foods_drinks_breakfast_text;
            } else if (layer_2_id == 1){
                //  for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_lunch_dinner_text;
            } else if (layer_2_id == 2){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_sweets_text;
            } else if (layer_2_id == 3){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_snacks_text;
            } else if (layer_2_id == 4){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic= food_drinks_fruits_text;
            } else if (layer_2_id == 5){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_drinks_text;
            } else if (layer_2_id == 6){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_cutlery_text;
            } else if (layer_2_id == 7){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = food_drinks_add_ons_text;
            }
        } else if (layer_1_id == 3) {
            if (layer_2_id == 0) {
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = fun_indoor_games_text;
            }else if (layer_2_id == 1){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = fun_outdoor_games_text;
            } else if (layer_2_id == 2){
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = fun_sports_text;
            } else if (layer_2_id == 3){
                myMusic = fun_tv_text;
            } else if (layer_2_id == 4){
                myMusic = fun_music_text;
            } else if (layer_2_id == 5){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = fun_activities_text;
            }
        } else if (layer_1_id == 4) {
            if (layer_2_id == 0) {
                // for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_animals_birds_text;
            }else if (layer_2_id == 1){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_body_parts_text;
            } else if (layer_2_id == 2){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_books_text;
            } else if (layer_2_id == 3){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_colours_text;
            } else if (layer_2_id == 4){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_shapes_text;
            } else if (layer_2_id == 5){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_stationary_text;
            } else if (layer_2_id == 6){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_school_objects_text;
            } else if (layer_2_id == 7){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_home_objects_text;
            } else if (layer_2_id == 8){
                //for (int j = more_count*9; j < more_count*9 + 9; j++)
                myMusic = learning_transportation_text;
            }
        } else if (layer_1_id == 7) {
            if (layer_2_id == 0) {
                myMusic = time_weather_time_text;
            }else if (layer_2_id == 1){
                myMusic = time_weather_day_text;
            } else if (layer_2_id == 2){
                myMusic = time_weather_month_text;
            } else if (layer_2_id == 3){
                myMusic = time_weather_weather_text;
            } else if (layer_2_id == 4){
                myMusic = time_weather_seasons_text;
            } else if (layer_2_id == 5){
                //  for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                myMusic = time_weather_holidays_festivals_text;
            } else if (layer_2_id == 6) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                myMusic = time_weather_brthdays_text;
            }
        }
    }

    public class ArrayIndexComparator implements Comparator<Integer>
    {
        private final Integer[] array;

        public ArrayIndexComparator(Integer[] array)
        {
            this.array = array;
        }

        public Integer[] createIndexArray()
        {
            Integer[] indexes = new Integer[array.length];
            for (int i = 0; i < array.length; i++)
            {
                indexes[i] = i; // Autoboxing
            }
            return indexes;
        }

        @Override
        public int compare(Integer index1, Integer index2)
        {
            // Autounbox from Integer to int to use as array indexes
            return array[index2].compareTo(array[index1]);
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
            blowUp.inflate(R.menu.menu_1, menu); //shruti
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Layer_3_Hindi_Activity.this, Setting.class);
                startActivity(intent);
                break;
            case R.id.info:
                Intent i = new Intent(Layer_3_Hindi_Activity.this, About_Jellow.class);
                startActivity(i);
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Layer_3_Hindi_Activity.this, Profile_form.class);
                startActivity(intent1);
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Layer_3_Hindi_Activity.this, Feedback.class);
                startActivity(intent2);
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Layer_3_Hindi_Activity.this, Tutorial.class);
                startActivity(intent3);
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Layer_3_Hindi_Activity.this, Reset__preferences.class);
                startActivity(intent4);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Layer_3_Hindi_Activity.this, Keyboard_Input.class);
                startActivity(intent6);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public class IndexSorter<T extends Comparable<T>> implements Comparator<Integer> {



        private final T[] values;



        private final Integer[] indexes;

        /**

         * Constructs a new IndexSorter based upon the parameter array.

         * @param d

         */

        public IndexSorter(T[] d){

            this.values = d;

            indexes = new Integer[this.values.length];

            for ( int i = 0; i < indexes.length; i++ ){

                indexes[i] = i;

            }

        }

        /**

         * Constructs a new IndexSorter based upon the parameter List.

         * @param d

         */

        public IndexSorter(List<T> d){

            this.values = (T[])d.toArray();

            for ( int i = 0; i < values.length; i++ ){

                values[i] = d.get(i);

            }

            indexes = new Integer[this.values.length];

            for ( int i = 0; i < indexes.length; i++ ){

                indexes[i] = i;

            }

        }

        /**

         * Sorts the underlying index array based upon the values provided in the constructor. The underlying value array is not sorted.

         */

        public void sort(){

            Arrays.sort(indexes, this);

        }

        /**

         * Retrieves the indexes of the array. The returned array is sorted if this object has been sorted.

         * @return The array of indexes.

         */

        public Integer[] getIndexes(){

            return indexes;

        }

        /**

         * Compares the two values at index arg0 and arg0

         * @param arg0 The first index

         * @param arg1 The second index

         * @return The result of calling compareTo on T objects at position arg0 and arg1

         */

        @Override

        public int compare(Integer arg0, Integer arg1) {

            T d1 = values[arg0];

            T d2 = values[arg1];

            return d2.compareTo(d1);

        }
    }
/*

    public void myfunction(String position1, int loc ,int pos, View rowView) {
        System.out.println("Value of"+pos);
        myMusic_function(layer_1_id,layer_2_id);
        mMenuItemImage = (CircularImageView)rowView.findViewById(R.id.icon1);
        im2 = (CircularImageView)rowView.findViewById(R.id.icon2);
        im3 = (CircularImageView)rowView.findViewById(R.id.icon3);
        if (count_flag == 1) {
            count[sort[loc]] = count[sort[loc]] + 1;
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < count.length; j++) {
                str.append(count[j]).append(",");
            }
            System.out.println("dgh" + str.toString());
            myDbHelper.setlevel(layer_1_id, layer_2_id, str.toString());
        }

        if (image_flag >= 0) {

            image_flag = -1;
            like.setImageResource(R.drawable.ilikewithoutoutline);
            dislike.setImageResource(R.drawable.idontlikewithout);
            yes.setImageResource(R.drawable.iwantwithout);
            no.setImageResource(R.drawable.idontwantwithout);
            add.setImageResource(R.drawable.morewithout);
            minus.setImageResource(R.drawable.lesswithout);

        }
        if(layer_1_id == 0 && layer_2_id == 1)
        {
            if(position1.equals("खुश"))
            {
                no.setAlpha(0.5f);
                no.setEnabled(false);
                yes.setAlpha(1f);
                yes.setEnabled(true);
            }
            else
            if(position1.equals("उदास") || position1.equals("गुस्सा") || position1.equals("डर") || position1.equals("चिढ़ा हुआ") || position1.equals("उलझन") || position1.equals("शर्मिंदा") || position1.equals("निराश") || position1.equals("बोर") || position1.equals("चिंता") || position1.equals("Stressed") || position1.equals("थका हुआ") || position1.equals("बीमार") || position1.equals("दुखी") )
            {
                yes.setAlpha(0.5f);
                yes.setEnabled(false);
                no.setAlpha(1f);
                no.setEnabled(true);
            }
            else
            {
                like.setAlpha(1f);
                dislike.setAlpha(1f);
                add.setAlpha(1f);
                minus.setAlpha(1f);
                yes.setAlpha(1f);
                no.setAlpha(1f);
                like.setEnabled(true);
                dislike.setEnabled(true);
                add.setEnabled(true);
                minus.setEnabled(true);
                yes.setEnabled(true);
                no.setEnabled(true);
            }
        }
        if(layer_1_id == 1 && layer_2_id == 3)
        {
            if(position1.equals("मेरे कपड़े टाइट हैं") ||position1.equals("मेरे कपड़े ढीले हैं") || position1.equals("ुझे कपड़े निकालने में मदद चाहिए") || position1.equals("ुझे कपड़े पैहनने में मदद चाहिए") )
            {
                like.setAlpha(0.5f);
                dislike.setAlpha(0.5f);
                add.setAlpha(0.5f);
                minus.setAlpha(0.5f);
                yes.setAlpha(0.5f);
                no.setAlpha(0.5f);
                like.setEnabled(false);
                dislike.setEnabled(false);
                add.setEnabled(false);
                minus.setEnabled(false);
                yes.setEnabled(false);
                no.setEnabled(false);
            }
            else
            {
                like.setAlpha(1f);
                dislike.setAlpha(1f);
                add.setAlpha(1f);
                minus.setAlpha(1f);
                yes.setAlpha(1f);
                no.setAlpha(1f);
                like.setEnabled(true);
                dislike.setEnabled(true);
                add.setEnabled(true);
                minus.setEnabled(true);
                yes.setEnabled(true);
                no.setEnabled(true);
            }
        }
        else
        if(layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))
        {
            if(loc == 0)
            {
                like.setAlpha(0.5f);
                dislike.setAlpha(0.5f);
                add.setAlpha(0.5f);
                minus.setAlpha(0.5f);
                yes.setAlpha(0.5f);
                no.setAlpha(0.5f);
                like.setEnabled(false);
                dislike.setEnabled(false);
                add.setEnabled(false);
                minus.setEnabled(false);
                yes.setEnabled(false);
                no.setEnabled(false);
            }
            else
            {
                like.setAlpha(1f);
                dislike.setAlpha(1f);
                add.setAlpha(1f);
                minus.setAlpha(1f);
                yes.setAlpha(1f);
                no.setAlpha(1f);
                like.setEnabled(true);
                dislike.setEnabled(true);
                add.setEnabled(true);
                minus.setEnabled(true);
                yes.setEnabled(true);
                no.setEnabled(true);
            }
        }

        if (x != loc) {
            tts.speak(myMusic[sort[loc]], TextToSpeech.QUEUE_FLUSH, null);
            Log.d("ppprint",loc+"");
            if ((loc%3) == 0 && (loc/3) == pos) {

                mMenuItemImage.setBorderColor(-1283893945);
                mMenuItemImage.setShadowColor(-1283893945);
                mMenuItemImage.setShadowRadius(sr);
                mMenuItemImage.setBorderWidth(bw);

                im2.setBorderColor(-1);
                im2.setShadowColor(0);
                im2.setShadowRadius(sr);
                im2.setBorderWidth(0);

                im3.setBorderColor(-1);
                im3.setShadowColor(0);
                im3.setShadowRadius(sr);
                im3.setBorderWidth(0);
            }
            else
            if ((loc%3) == 1 && ((loc-1)/3) == pos) {
                im2.setBorderColor(-1283893945);
                im2.setShadowColor(-1283893945);
                im2.setShadowRadius(sr);
                im2.setBorderWidth(bw);

                mMenuItemImage.setBorderColor(-1);
                mMenuItemImage.setShadowColor(0);
                mMenuItemImage.setShadowRadius(sr);
                mMenuItemImage.setBorderWidth(0);

                im3.setBorderColor(-1);
                im3.setShadowColor(0);
                im3.setShadowRadius(sr);
                im3.setBorderWidth(0);
            }
            else
            if ((loc%3) == 2 && ((loc-2)/3) == pos) {
                im3.setBorderColor(-1283893945);
                im3.setShadowColor(-1283893945);
                im3.setShadowRadius(sr);
                im3.setBorderWidth(bw);

                im2.setBorderColor(-1);
                im2.setShadowColor(0);
                im2.setShadowRadius(sr);
                im2.setBorderWidth(0);

                mMenuItemImage.setBorderColor(-1);
                mMenuItemImage.setShadowColor(0);
                mMenuItemImage.setShadowRadius(sr);
                mMenuItemImage.setBorderWidth(0);
            }
        }
        if((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))
            tts.speak(myMusic[loc], TextToSpeech.QUEUE_FLUSH, null);
else
        tts.speak(myMusic[sort[loc]], TextToSpeech.QUEUE_FLUSH, null);
        x=loc;

        flag = 1;
        mCy = 0;
        mCm = 0;
        mCd = 0;
        mCn = 0;
        mCl = 0;
        mCk = 0;


    }

*/

    String[][][][] layer_3_speech = {{{{},{},{},{},{},{},{},{},{},{},{},{}},

            {{"मैं खुश हूँ",
                    "मैं सच में खुश हूँ",
                    "मुझे खुश रहना हैं",
                    "मुझे सच में खुश रहना हैं",
                    "मैं बहुत खुश हूँ",
                    "मैं सच में बहुत खुश हूँ",
                    "मैं खुश नहीं हूँ",
                    "मैं सच में खुश नहीं हूँ",
                    "मुझे खुश नहीं रहना हैं",
                    "मुझे सच में खुश नहीं रहना हैं",
                    "मैं बहुत खुश नहीं हूँ",
                    "मैं सच में बिल्कुल खुश नहीं हूँ"

            },{"मैं उदास हूँ",
                    "मैं सच में उदास हूँ",
                    "मैं उदास हूँ",
                    "मैं सच में उदास हूँ",
                    "मैं बहुत उदास हूँ",
                    "मैं सच में बहुत उदास हूँ",
                    "मैं उदास नहीं हूँ",
                    "मैं सच में उदास नहीं हूँ",
                    "मुझे उदास नहीं होना हैं",
                    "मुझे सच में उदास नहीं होना हैं",
                    "मैं बहुत उदास नहीं हूँ",
                    "मैं सच में बिल्कुल उदास नहीं हूँ"

            },{"मैं गुस्से में हूँ",
                    "मैं सच में गुस्से में हूँ",
                    "मैं गुस्से में हूँ",
                    "मैं सच में गुस्से में हूँ",
                    "मैं बहुत गुस्से में हूँ",
                    "मैं सच में बहुत गुस्से में हूँ",
                    "मैं गुस्से में नहीं हूँ",
                    "मैं सच में गुस्से में नहीं हूँ",
                    "मुझे गुस्सा नहीं होना हैं",
                    "मुझे सच में गुस्सा नहीं होना हैं",
                    "मैं बहुत गुस्से में नहीं हूँ",
                    "मैं सच में बिल्कुल गुस्से में नहीं हूँ"

            },{"मुझे डर लग रहा हैं",
                    "मुझे सच में डर लग रहा हैं",
                    "मुझे डर लग रहा हैं",
                    "मुझे सच में डर लग रहा हैं",
                    "मुझे बहुत डर लग रहा हैं",
                    "मुझे सच में बहुत डर लग रहा हैं",
                    "मुझे डर नहीं लग रहा हैं",
                    "मुझे सच में डर नहीं लग रहा हैं",
                    "मुझे डरना नहीं हैं",
                    "मुझे सच में डरना नहीं हैं",
                    "मुझे बहुत डर नहीं लग रहा हैं",
                    "मुझे सच में बिल्कुल डर नहीं लग रहा हैं"

            },{"मैं हैरान हूँ",
                    "मैं सच में हैरान हूँ",
                    "मुझे आश्चर्यचकित होना हैं",
                    "मुझे सच में आश्चर्यचकित होना हैं",
                    "मैं बहुत हैरान हूँ",
                    "मैं सच में बहुत हैरान हूँ",
                    "मैं हैरान नही हूँ",
                    "मैं सच में हैरान नही हूँ",
                    "मुझे आश्चर्यचकित नहीं होना हैं",
                    "मुझे सच में आश्चर्यचकित नहीं होना हैं",
                    "मैं बहुत हैरान नहीं हूँ",
                    "मैं सच में बिल्कुल हैरान नहीं हूँ"

            },{"मुझे चिढ़ आ रही हैं",
                    "मुझे सच में चिढ़ आ रही हैं",
                    "मुझे चिढ़ आ रही हैं",
                    "मुझे सच में चिढ़ आ रही हैं",
                    "मुझे बहुत चिढ़ आ रही हैं",
                    "मुझे सच में बहुत चिढ़ आ रही हैं",
                    "मुझे चिढ़ नहीं आ रही हैं",
                    "मुझे सच में चिढ़ नहीं आ रही हैं",
                    "मुझे चिढ़ना नही हैं",
                    "मुझे सच में चिढ़ना नही हैं",
                    "मुझे बहुत चिढ़ना नही हैं",
                    "मुझे सच में बिल्कुल चिढ़ना नही हैं"

            },{"मुझे समज में नहीं आ रहा हैं ",
                    "मुझे सच में समज में नहीं आ रहा हैं",
                    "मुझे समज में नहीं आ रहा हैं ",
                    "मुझे सच में समज में नहीं आ रहा हैं",
                    "मुझे बहुत उलझन महसूस हो रही हैं",
                    "मुझे सच में बहुत उलझन महसूस हो रही हैं",
                    "मुझे उलझन नहीं महसूस हो रही हैं",
                    "मुझे सच में उलझन नहीं महसूस हो रही हैं",
                    "मुझे उलझन नहीं महसूस हो रही हैं",
                    "मुझे सच में उलझन नहीं महसूस हो रही हैं",
                    "मुझे बहुत उलझन नहीं महसूस हो रही हैं",
                    "मुझे सच में बिल्कुल उलझन नहीं महसूस हो रही हैं"

            },{"मैं शर्मिंदा हूँ",
                    "मैं सच में शर्मिंदा हूँ",
                    "मैं शर्मिंदा हूँ",
                    "मैं सच में शर्मिंदा हूँ",
                    "मैं बहुत शर्मिंदा हूँ",
                    "मैं सच में बहुत शर्मिंदा हूँ",
                    "मैं शर्मिंदा नहीं हूँ",
                    "मैं सच में शर्मिंदा नहीं हूँ",
                    "मुझे शर्मिंदा नहीं होना हैं",
                    "मुझे सच में शर्मिंदा नहीं होना हैं",
                    "मैं बहुत शर्मिंदा नहीं हूँ",
                    "मैं सच में बिल्कुल शर्मिंदा नहीं हूँ "

            },{"मैं निराश हूँ",
                    "मैं सच में निराश हूँ",
                    "मैं निराश हूँ",
                    "मैं सच में निराश हूँ",
                    "मैं बहुत निराश हूँ",
                    "मैं सच में बहुत निराश हूँ",
                    "मैं निराश नहीं हूँ",
                    "मैं सच में निराश नहीं हूँ",
                    "मुझे निराश नहीं होना हैं",
                    "मुझे सच में निराश नहीं होना हैं",
                    "मैं बहुत निराश नहीं हूँ",
                    "मैं सच में बिल्कुल निराश नहीं हूँ"

            },{"मुझे बोर हो रहा हैं",
                    "मुझे सच में बोर हो रहा हैं",
                    "मुझे बोर हो रहा हैं",
                    "मुझे सच में बोर हो रहा हैं",
                    "मुझे बहुत बोर हो रहा हैं",
                    "मुझे सच में बहुत बोर हो रहा हैं",
                    "मुझे बोर नहीं हो रहा हैं",
                    "मुझे सच में बोर नहीं हो रहा हैं",
                    "मुझे बोर नहीं होना हैं",
                    "मुझे सच में बोर नहीं होना हैं",
                    "मुझे ज़्यादा बोर नहीं हो रहा हैं",
                    "मुझे सच में बिल्कुल बोर नहीं हो रहा हैं"

            },{"मैं चिंता हो रही हैं",
                    "मैं सच में चिंता हो रही हैं",
                    "मैं चिंता हो रही हैं",
                    "मैं सच में चिंता हो रही हैं",
                    "मुझे बहुत चिंता हो रही हैं",
                    "मुझे सच में बहुत चिंता हो रही हैं",
                    "मुझे चिंता नहीं हो रही हैं",
                    "मुझे सच में चिंता नहीं हो रही हैं",
                    "मुझे चिंता नहीं हो रही हैं",
                    "मुझे सच में चिंता नहीं हो रही हैं",
                    "मुझे बहुत चिंता नहीं हो रही हैं",
                    "मुझे सच में बिल्कुल चिंता नहीं हो रही हैं"

            },{"मुझे तनाव महसूस हो रहा हैं",
                    "मुझे सच में तनाव महसूस हो रहा हैं",
                    "मुझे तनाव महसूस हो रहा हैं",
                    "मुझे सच में तनाव महसूस हो रहा हैं",
                    "मुझे बहुत तनाव महसूस हो रहा हैं",
                    "मुझे सच में बहुत तनाव महसूस हो रहा हैं",
                    "मुझे तनाव महसूस नहीं हो रहा हैं",
                    "मुझे सच में तनाव महसूस नहीं हो रहा हैं",
                    "मुझे तनाव में नहीं रहना हैं",
                    "मुझे सच में तनाव में नहीं रहना हैं",
                    "मुझे बहुत तनाव नहीं महसूस हो रहा हैं",
                    "मुझे सच में बिल्कुल तनाव नहीं महसूस हो रहा हैं"

            },{"मुझे थकान महसूस हो रही हैं",
                    "मुझे सच में थकान महसूस हो रही हैं",
                    "मुझे थकान महसूस हो रही हैं",
                    "मुझे सच में थकान महसूस हो रही हैं",
                    "मुझे बहुत थकान महसूस हो रही हैं",
                    "मुझे सच में बहुत थकान महसूस हो रही हैं",
                    "मुझे थकान नहीं महसूस करनी हैं",
                    "मुझे सच में थकान नहीं महसूस करनी हैं",
                    "मुझे थकान नहीं महसूस करनी हैं",
                    "मुझे सच में थकान नहीं महसूस करनी हैं",
                    "मुझे बहुत थकान नहीं हो रही हैं",
                    "मुझे सच में बिल्कुल थकान नहीं हो रही हैं"

            },{"मुझे गरम महसूस करना हैं ",
                    "मुझे सच में गरम महसूस करना हैं",
                    "मुझे गरम महसूस करना हैं ",
                    "मुझे सच में गरम महसूस करना हैं",
                    "मुझे और गरम महसूस करना हैं",
                    "मुझे सच में और गरम महसूस करना हैं",
                    "मुझे गरम नहीं महसूस करना हैं",
                    "मुझे सच में गरम नहीं महसूस करना हैं",
                    "मुझे गरम नहीं महसूस करना हैं",
                    "मुझे सच में गरम नहीं महसूस करना हैं" ,
                    "मुझे और गर्मी नहीं चाहिए",
                    "मुझे सच में बिल्कुल गर्मी नहीं चाहिए"

            },{"मुझे ठंडक महसूस करनी हैं ",
                    "मुझे सच में ठंडक महसूस करनी हैं",
                    "मुझे ठंडक महसूस करनी हैं ",
                    "मुझे सच में ठंडक महसूस करनी हैं",
                    "मुझे और ठंडक चाहिए",
                    "मुझे सच में और ठंडक चाहिए",
                    "मुझे ठंडक नहीं महसूस करनी हैं",
                    "मुझे सच में ठंडक नहीं महसूस करनी हैं",
                    "मुझे ठंडक नहीं महसूस करनी हैं",
                    "मुझे सच में ठंडक नहीं महसूस करनी हैं",
                    "मुझे और ठंडक नहीं चाहिए",
                    "मुझे सच में बिल्कुल ठंडक नहीं चाहिए"

            },{"मैं बीमार हूँ",
                    "मैं सच में बीमार हूँ",
                    "मैं बीमार हूँ",
                    "मैं सच में बीमार हूँ",
                    "मैं बहुत बीमार हूँ",
                    "मैं सच में बहुत बीमार हूँ",
                    "मैं बीमार नहीं हूँ",
                    "मैं सच में बीमार नहीं हूँ",
                    "मुझे बीमार नहीं होना हैं",
                    "मुझे सच में बीमार नहीं होना हैं",
                    "मैं ज़्यादा बीमार नहीं हूँ",
                    "मैं सच में बिल्कुल बीमार नहीं हूँ"

            },{"मैं दुखी हूँ",
                    "मैं सच में दुखी हूँ",
                    "मैं दुखी हूँ",
                    "मैं सच में दुखी हूँ",
                    "मैं बहुत दुखी हूँ",
                    "मैं सच में बहुत दुखी हूँ",
                    "मैं दुखी नहीं हूँ",
                    "मैं सच में दुखी नहीं हूँ",
                    "मुझे दुखी नहीं होना हैं",
                    "मुझे सच में दुखी नहीं होना",
                    "मैं बहुत दुखी नहीं हूँ",
                    "मैं सच में बिल्कुल दुखी नहीं हूँ"

            }},{{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},{{},{},{},{},{},{},{},{},{

    }}},{{{"मुझे अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे अपना मुँह पानी से धोना हैं",
            "मुझे सच में अपना मुँह पानी से धोना हैं",
            "मुझे अपना मुँह पानी से थोड़ा और धोना हैं",
            "मुझे सच में अपना मुँह पानी से थोड़ा और धोना हैं",
            "मुझे अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना मुँह पानी से धोना नहीं हैं",
            "मुझे सच में अपना मुँह पानी से धोना नहीं हैं",
            "मुझे अपना मुँह पानी से और नहीं धोना हैं",
            "मुझे सच में अपना मुँह पानी से और नहीं धोना हैं"

    },{"मुझे अपना टूथब्रश पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना टूथब्रश पानी से धोना अच्छा लगता हैं",
            "मुझे अपना टूथब्रश पानी से धोना हैं",
            "मुझे सच में अपना टूथब्रश पानी से धोना हैं",
            "मुझे अपना टूथब्रश पानी से और धोना हैं",
            "मुझे सच में अपना टूथब्रश पानी से और धोना हैं",
            "मुझे अपना टूथब्रश पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना टूथब्रश पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना टूथब्रश पानी से धोना नहीं हैं",
            "मुझे सच में अपना टूथब्रश पानी से धोना नहीं हैं",
            "मुझे अपना टूथब्रश पानी से और नहीं धोना हैं",
            "मुझे सच में अपना टूथब्रश पानी से और नहीं धोना हैं"

    },{"मुझे अपने ब्रश पर टूथपेस्ट लगना अच्छा लगता हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगना अच्छा लगता हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट लगना हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगना हैं",
            "मुझे अपने ब्रश पर थोड़ा टूथपेस्ट लगना हैं",
            "मुझे सच में अपने ब्रश पर बहुत टूथपेस्ट लगना हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट लगना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट लगना अच्छा नहीं लगता हैं",
            "मुझे अपने ब्रश पर टूथपेस्ट नहीं लगना हैं",
            "मुझे सच में अपने ब्रश पर टूथपेस्ट नहीं लगना हैं",
            "मुझे अपने ब्रश पर और टूथपेस्ट नहीं लगना हैं",
            "मुझे सच में अपने ब्रश पर और टूथपेस्ट नहीं लगना हैं"

    },{"मुझे अपने सामने के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने सामने के दांत साफ़ करने हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करने हैं",
            "मुझे अपने सामने के दांत थोड़े और साफ़ करने हैं",
            "मुझे सच में अपने सामने के दांत थोड़े और साफ़ करने हैं",
            "मुझे अपने सामने के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने सामने के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने सामने के दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने सामने के दांत साफ़ नहीं करने हैं",
            "मुझे अपने सामने के दांत और साफ़ नहीं करने हैं",
            "मुझे सच में अपने सामने के दांत और साफ़ नहीं करने हैं"

    },{"मुझे अपने पीछे के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने पीछे के दांत साफ़ करने हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करने हैं",
            "मुझे अपने पीछे के दांत और साफ़ करने हैं",
            "मुझे सच में अपने पीछे के दांत और साफ़ करने हैं",
            "मुझे अपने पीछे के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने पीछे के दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने पीछे के दांत साफ़ नहीं करने हैं",
            "मुझे अपने पीछे के दांत और साफ़ नहीं करने हैं",
            "मुझे सच में अपने पीछे के दांत और साफ़ नहीं करने हैं"

    },{"मुझे अपनी जीभ साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपनी जीभ साफ़ करना अच्छा लगता हैं",
            "मुझे अपनी जीभ साफ़ करनी हैं",
            "मुझे सच में अपनी जीभ साफ़ करनी हैं",
            "मुझे अपनी जीभ थोड़ी और साफ़ करनी हैं",
            "मुझे सच में अपनी जीभ और साफ़ करनी हैं",
            "मुझे अपनी जीभ साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी जीभ साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपनी जीभ साफ़ नहीं करनी हैं",
            "मुझे सच में अपनी जीभ साफ़ नहीं करनी हैं",
            "मुझे अपनी जीभ और साफ़ नहीं करनी हैं",
            "मुझे सच में अपनी जीभ और साफ़ नहीं करनी हैं"

    },{"मुझे अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा लगता हैं",
            "मुझे अपना मुँह पानी से धोना हैं",
            "मुझे सच में अपना मुँह पानी से धोना हैं",
            "मुझे अपना मुँह पानी से थोड़ा और धोना हैं",
            "मुझे सच में अपना मुँह पानी से थोड़ा और धोना हैं",
            "मुझे अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना मुँह पानी से धोना अच्छा नहीं लगता हैं",
            "मुझे अपना मुँह पानी से धोना नहीं हैं",
            "मुझे सच में अपना मुँह पानी से धोना नहीं हैं",
            "मुझे अपना मुँह पानी से और नहीं धोना हैं",
            "मुझे सच में अपना मुँह पानी से और नहीं धोना हैं"

    }},{{"मुझे अपनी pant नीचे खींचनी हैं",
            "मुझे सच में अपनी pant नीचे खींचनी हैं",
            "मुझे अपनी pant नीचे खींचनी हैं",
            "मुझे सच में अपनी pant नीचे खींचनी हैं",
            "मुझे फिर से अपनी pant नीचे खींचनी हैं",
            "मुझे सच में फिर से अपनी pant नीचे खींचनी हैं",
            "मुझे अपनी pant नीचे नहीं खींचनी हैं",
            "मुझे सच में अपनी pant नीचे नहीं खींचनी हैं",
            "मुझे अपनी pant नीचे नहीं खींचनी हैं",
            "मुझे सच में अपनी pant नीचे नहीं खींचनी हैं",
            "मुझे फिर से अपनी pant नीचे नहीं खींचनी हैं",
            "मुझे सच में फिर से अपनी pant नहीं नीचे खींचनी हैं"

    },{"मुझे शौचा लय में बैठना हैं",
            "मुझे सच में शौचा लय में बैठना हैं",
            "मुझे शौचा लय में बैठना हैं",
            "मुझे सच में शौचा लय में बैठना हैं",
            "मुझे फिर से शौचा लय में बैठना हैं",
            "मुझे सच में फिर से शौचा लय में बैठना हैं",
            "मुझे शौचा लय में नहीं बैठना हैं",
            "मुझे सच में शौचा लय में नहीं बैठना हैं",
            "मुझे शौचा लय में नहीं बैठना हैं",
            "मुझे सच में शौचा लय में नहीं बैठना हैं",
            "मुझे फिर से शौचा लय में नहीं बैठना हैं",
            "मुझे सच में फिर से शौचा लय में नहीं बैठना हैं"

    },{"मुझे मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा धोना हैं",
            "मुझे मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा धोना हैं",
            "मुझे फिर से मेरा पिछला हिस्सा धोना हैं",
            "मुझे सच में फिर से मेरा पिछला हिस्सा धोना हैं",
            "मुझे मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे फिर से मेरा पिछला हिस्सा नहीं धोना हैं",
            "मुझे सच में फिर से मेरा पिछला हिस्सा नहीं धोना हैं"

    },{"मुझे टॉयलेट फ़्लश करना हैं",
            "मुझे सच में टॉयलेट फ़्लश करना हैं",
            "मुझे टॉयलेट फ़्लश करना हैं",
            "मुझे सच में टॉयलेट फ़्लश करना हैं",
            "मुझे फिर से टॉयलेट फ़्लश करना हैं",
            "मुझे सच में फिर से टॉयलेट फ़्लश करना हैं",
            "मुझे टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे फिर से टॉयलेट फ़्लश नहीं करना हैं",
            "मुझे सच में फिर से टॉयलेट फ़्लश नहीं करना हैं"

    },{"मुझे अपनी pant ऊपर खींचनी हैं",
            "मुझे सच में अपनी pant ऊपर खींचनी हैं",
            "मुझे अपनी pant ऊपर खींचनी हैं",
            "मुझे सच में अपनी pant ऊपर खींचनी हैं",
            "मुझे फिर से अपनी pant ऊपर खींचनी हैं",
            "मुझे सच में फिर से अपनी pant ऊपर खींचनी हैं",
            "मुझे अपनी pant ऊपर नहीं खींचनी हैं",
            "मुझे सच में अपनी pant ऊपर नहीं खींचनी हैं",
            "मुझे अपनी pant ऊपर नहीं खींचनी हैं",
            "मुझे सच में अपनी pant ऊपर नहीं खींचनी हैं",
            "मुझे फिर से अपनी pant ऊपर नहीं खींचनी हैं",
            "मुझे सच में फिर से अपनी pant ऊपर नहीं खींचनी हैं"

    },{"मुझे अपने हाथ धोने हैं",
            "मुझे सच में अपने हाथ धोने हैं",
            "मुझे अपने हाथ धोने हैं",
            "मुझे सच में अपने हाथ धोने हैं",
            "मुझे फिर से अपने हाथ धोने हैं",
            "मुझे सच में फिर से अपने हाथ धोने हैं",
            "मुझे अपने हाथ नहीं धोने हैं",
            "मुझे सच में अपने हाथ नहीं धोने हैं",
            "मुझे अपने हाथ नहीं धोने हैं",
            "मुझे सच में मेरा अपने हाथ नहीं धोने हैं",
            "मुझे फिर से अपने हाथ नहीं धोने हैं",
            "मुझे सच में फिर से अपने हाथ नहीं धोने हैं"

    },{"I like to be all done",
            "I really like to be all done",
            "I want to be all done",
            "I really want to be all done",
            "I want to be all done again",
            "I really want to be all done again",
            "I don’t like to be all done",
            "I really don’t like to be all done",
            "I don’t want to be all done",
            "I really don’t want to be all done",
            "I don’t want to be all done again",
            "I really don’t want to be all done again"

    }},{{"मुझे अपने कपड़ऐ निकालने हैं",
            "मुझे सच में अपने कपड़ऐ निकालने हैं",
            "मुझे अपने कपड़ऐ निकालने हैं",
            "मुझे सच में अपने कपड़ऐ निकालने हैं",
            "मुझे फिर से अपने कपड़ऐ निकालने हैं",
            "मुझे सच में फिर से अपने कपड़ऐ निकालने हैं",
            "मुझे अपने कपड़ऐ नहीं निकालने हैं",
            "मुझे सच में अपने कपड़ऐ नहीं निकालने हैं",
            "मुझे अपने कपड़ऐ नहीं निकालने हैं",
            "मुझे सच में अपने कपड़ऐ नहीं निकालने हैं",
            "मुझे फिर से अपने कपड़ऐ नहीं निकालने हैं",
            "मुझे सच में फिर से अपने कपड़ऐ नहीं निकालने हैं"

    },{"मुझे पानी चालू करना हैं",
            "मुझे सच में पानी चालू करना हैं",
            "मुझे पानी चालू करना हैं",
            "मुझे सच में पानी चालू करना हैं",
            "मुझे फिर से पानी चालू करना हैं",
            "मुझे सच में फिर से पानी चालू करना हैं",
            "मुझे पानी चालू नहीं करना हैं",
            "मुझे सच में पानी चालू नहीं करना हैं",
            "मुझे पानी चालू नहीं करना हैं",
            "मुझे सच में पानी चालू नहीं करना हैं",
            "मुझे फिर से पानी चालू नहीं करना हैं",
            "मुझे सच में फिर से पानी चालू नहीं करना हैं"

    },{"मुझे शॉवर लेना हैं",
            "मुझे सच में शॉवर लेना हैं",
            "मुझे शॉवर लेना हैं",
            "मुझे सच में शॉवर लेना हैं",
            "मुझे फिर से शॉवर लेना हैं",
            "मुझे सच में फिर से शॉवर लेना हैं",
            "मुझे शॉवर नहीं लेना हैं",
            "मुझे सच में शॉवर नहीं लेना हैं",
            "मुझे शॉवर नहीं लेना हैं",
            "मुझे सच में शॉवर नहीं लेना हैं",
            "मुझे फिर से शॉवर नहीं लेना हैं",
            "मुझे सच में फिर से शॉवर नहीं लेना हैं"

    },{"मुझे अपने शरीर को भिगोना अच्छा लगता हैं",
            "मुझे सच में अपने शरीर को भिगोना अच्छा लगता हैं",
            "मुझे अपने शरीर को भिगोना हैं/ चाहती हूँ",
            "मुझे सच में अपने शरीर को भिगोना हैं",
            "मुझे फिर से अपने शरीर को भिगोना हैं",
            "मुझे सच में फिर से अपने शरीर को भिगोना हैं",
            "मुझे अपने शरीर को भिगोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शरीर को भिगोना अच्छा नहीं लगता हैं",
            "मुझे अपने शरीर को नहीं भिगोना हैं",
            "मुझे सच में अपने शरीर को नहीं भिगोना हैं",
            "मुझे फिर से अपने शरीर को नहीं भिगोना हैं",
            "मुझे सच में फिर से अपने शरीर को नहीं भिगोना हैं"

    },{"मुझे अपने शरीर पर साबुन लगाना अच्छा लगता हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना अच्छा लगता हैं",
            "मुझे अपने शरीर पर साबुन लगाना हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना हैं",
            "मुझे अपने शरीर पर थोड़ा और साबुन लगाना हैं",
            "मुझे सच में अपने शरीर पर थोड़ा और साबुन लगाना हैं",
            "मुझे अपने शरीर पर साबुन लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शरीर पर साबुन लगाना अच्छा नहीं लगता हैं",
            "मुझे अपने शरीर पर साबुन नहीं लगाना हैं",
            "मुझे सच में अपने शरीर पर साबुन नहीं लगाना हैं",
            "मुझे अपने शरीर पर और साबुन नहीं लगाना हैं",
            "मुझे सच में अपने शरीर पर और साबुन नहीं लगाना हैं"

    },{"मुझे अपने बालों को शैंपू लगाना अच्छा लगता हैं",
            "मुझे सच में अपने बालों को शैंपू लगाना अच्छा लगता हैं",
            "मुझे अपने बालों को शैंपू लगाना हैं",
            "मुझे सच में अपने बालों को शैंपू लगाना हैं",
            "मुझे फिर से अपने बालों को शैंपू लगाना हैं",
            "मुझे सच में फिर से अपने बालों को शैंपू लगाना हैं",
            "मुझे अपने बालों को शैंपू लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बालों को शैंपू लगाना अच्छा नहीं लगता हैं",
            "मुझे अपने बालों को शैंपू नहीं लगाना हैं",
            "मुझे सच में बालों को शैंपू नहीं लगाना हैं",
            "मुझे फिर से अपने बालों को शैंपू नहीं लगाना हैं",
            "मुझे सच में फिर से अपने बालों को शैंपू नहीं लगाना हैं"

    },{"मुझे फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल नहीं करना हैं"

    },{"मुझे अपने बाल धोना अच्छा लगता हैं",
            "मुझे सच में अपने बाल धोना अच्छा लगता हैं",
            "मुझे अपने बाल धोने हैं",
            "मुझे सच में अपने बाल धोने हैं",
            "मुझे फिर से अपने बाल धोने हैं",
            "मुझे सच में फिर से अपने बाल धोने हैं",
            "मुझे अपने बाल धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बाल धोना अच्छा नहीं लगता हैं",
            "मुझे अपने बाल नहीं धोने हैं",
            "मुझे सच में अपने बाल नहीं धोने हैं",
            "मुझे फिर से अपने बाल नहीं धोने हैं",
            "मुझे सच में फिर से अपने बाल नहीं धोने हैं"

    },{"मुझे अपना शरीर धोना अच्छा लगता हैं",
            "मुझे सच में अपना शरीर धोना अच्छा लगता हैं",
            "मुझे अपना शरीर धोना हैं",
            "मुझे सच में अपना शरीर धोना हैं",
            "मुझे फिर से अपना शरीर धोना हैं",
            "मुझे सच में फिर से अपना शरीर धोना हैं",
            "मुझे अपना शरीर धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना शरीर धोना अच्छा नहीं लगता हैं",
            "मुझे अपना शरीर नहीं धोना हैं",
            "मुझे सच में अपना शरीर नहीं धोना हैं",
            "मुझे फिर से अपना शरीर नहीं धोना हैं",
            "मुझे सच में फिर से अपना शरीर नहीं धोना हैं"

    },{"मुझे पानी बंद करना हैं",
            "मुझे सच में पानी बंद करना हैं",
            "मुझे पानी बंद करना हैं",
            "मुझे सच में पानी बंद करना हैं",
            "मुझे फिर से पानी बंद करना हैं",
            "मुझे सच में फिर से पानी बंद करना हैं",
            "मुझे पानी बंद नहीं करना हैं",
            "मुझे सच में पानी बंद नहीं करना हैं",
            "मुझे पानी बंद नहीं करना हैं",
            "मुझे सच में पानी बंद नहीं करना हैं",
            "मुझे फिर से पानी बंद नहीं करना हैं",
            "मुझे सच में फिर से पानी बंद नहीं करना हैं"

    },{"मुझे अपने बाल सूखाना अच्छा लगता हैं",
            "मुझे सच में अपने बाल सूखाना अच्छा लगता हैं",
            "मुझे अपने बाल सूखाने हैं",
            "मुझे सच में अपने बाल सूखाने हैं",
            "मुझे फिर से अपने बाल सूखाने हैं",
            "मुझे सच में फिर से अपने बाल सूखाने हैं",
            "मुझे अपने बाल सूखाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बाल सूखाना अच्छा नहीं लगता हैं",
            "मुझे अपने बाल नहीं सूखाने हैं",
            "मुझे सच में अपने बाल नहीं सूखाने हैं",
            "मुझे फिर से अपने बाल नहीं सूखाने हैं",
            "मुझे सच में फिर से अपने बाल नहीं सूखाने हैं"

    },{"मुझे अपना चेहरा पोंछना अच्छा लगता हैं",
            "मुझे सच में अपना चेहरा पोंछना अच्छा लगता हैं",
            "मुझे अपना चेहरा पोंछना हैं",
            "मुझे सच में अपना चेहरा पोंछना हैं",
            "मुझे फिर से अपना चेहरा पोंछना हैं",
            "मुझे सच में फिर से अपना चेहरा पोंछना हैं",
            "मुझे अपना चेहरा पोंछना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना चेहरा पोंछना अच्छा नहीं लगता हैं",
            "मुझे अपना चेहरा नहीं पोंछना हैं",
            "मुझे सच में अपना चेहरा नहीं पोंछना हैं",
            "मुझे फिर से अपना चेहरा नहीं पोंछना हैं",
            "मुझे सच में फिर से अपना चेहरा नहीं पोंछना हैं"

    },{"मुझे अपना शरीर पोंछना अच्छा लगता हैं",
            "मुझे सच में अपना शरीर पोंछना अच्छा लगता हैं",
            "मुझे अपना शरीर पोंछना हैं",
            "मुझे सच में अपना शरीर पोंछना हैं",
            "मुझे फिर से अपना शरीर पोंछना हैं",
            "मुझे सच में फिर से अपना शरीर पोंछना हैं",
            "मुझे अपना शरीर पोंछना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना शरीर पोंछना अच्छा नहीं लगता हैं",
            "मुझे अपना शरीर नहीं पोंछना हैं",
            "मुझे सच में अपना शरीर नहीं पोंछना हैं",
            "मुझे फिर से अपना शरीर नहीं पोंछना हैं",
            "मुझे सच में फिर से अपना शरीर नहीं पोंछना हैं"

    },{"मुझे अपने कपड़ऐ पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ पैहनने हैं",
            "मुझे अपने कपड़ऐ पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ पैहनने हैं",
            "मुझे अपने कपड़ऐ फिर से पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ फिर से पैहनने हैं",
            "मुझे अपने कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ कपड़ऐ नहीं पैहनने हैं",
            "मुझे अपने कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ नहीं पैहनने हैं",
            "मुझे अपने कपड़ऐ फिर से नहीं पैहनने हैं",
            "मुझे सच में अपने कपड़ऐ फिर से नहीं पैहनने हैं"

    },{"I like to be all done",
            "I really like to be all done",
            "I want to be all done",
            "I really want to be all done",
            "I want to be all done again",
            "I really want to be all done again",
            "I don’t like to be all done",
            "I really don’t like to be all done",
            "I don’t want to be all done",
            "I really don’t want to be all done",
            "I don’t want to be all done again",
            "I really don’t want to be all done again"

    }},{{"मुझे अपना टी-शर्ट बदलना अच्छा लगता हैं",
            "मुझे सच में अपना टी-शर्ट बदलना अच्छा लगता हैं",
            "मुझे अपना टी-शर्ट बदलना हैं",
            "मुझे सच में अपना टी-शर्ट बदलना हैं",
            "मुझे फिर से अपना टी-शर्ट बदलना हैं",
            "मुझे सच में फिर से अपना टी-शर्ट बदलना हैं",
            "मुझे अपना टी-शर्ट बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना टी-शर्ट बदलना अच्छा नहीं लगता हैं",
            "मुझे अपना टी-शर्ट नहीं बदलना हैं",
            "मुझे सच में अपना टी-शर्ट नहीं बदलना हैं",
            "मुझे फिर से अपना टी-शर्ट नहीं बदलना हैं",
            "मुझे सच में फिर से अपना टी-शर्ट नहीं बदलना हैं"

    },{"मुझे अपना फ्रॉक बदलना अच्छा लगता हैं",
            "मुझे सच में अपना फ्रॉक बदलना अच्छा लगता हैं",
            "मुझे अपना फ्रॉक बदलना हैं",
            "मुझे सच में अपना फ्रॉक बदलना हैं",
            "मुझे फिर से अपना फ्रॉक बदलना हैं",
            "मुझे सच में फिर से अपना फ्रॉक बदलना हैं",
            "मुझे अपना फ्रॉक बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना फ्रॉक बदलना अच्छा नहीं लगता हैं",
            "मुझे अपना फ्रॉक नहीं बदलना हैं",
            "मुझे सच में अपना फ्रॉक नहीं बदलना हैं",
            "मुझे फिर से अपना फ्रॉक नहीं बदलना हैं",
            "मुझे सच में फिर से अपना फ्रॉक नहीं बदलना हैं"

    },{"मुझे अपनी Skirt बदलना अच्छा लगता हैं",
            "मुझे सच में अपनी Skirt बदलना अच्छा लगता हैं",
            "मुझे अपनी Skirt बदलनी हैं",
            "मुझे सच में अपनी Skirt बदलनी हैं",
            "मुझे फिर से अपनी Skirt बदलनी हैं",
            "मुझे सच में फिर से अपनी Skirt बदलनी हैं",
            "मुझे अपनी Skirt बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी Skirt बदलनी अच्छा नहीं लगता हैं",
            "मुझे अपनी Skirt नहीं बदलनी हैं",
            "मुझे सच में अपनी Skirt नहीं बदलनी हैं",
            "मुझे फिर से अपनी Skirt नहीं बदलनी हैं",
            "मुझे सच में फिर से अपनी Skirt नहीं बदलनी हैं"

    },{"मुझे अपनी जीन्स बदलना अच्छा लगता हैं",
            "मुझे सच में अपनी जीन्स बदलना अच्छा लगता हैं",
            "मुझे अपनी जीन्स बदलनी हैं",
            "मुझे सच में अपनी जीन्स बदलनी हैं",
            "मुझे फिर से अपनी जीन्स बदलनी हैं",
            "मुझे सच में फिर से अपनी जीन्स बदलनी हैं",
            "मुझे अपनी जीन्स बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी जीन्स बदलना अच्छा नहीं लगता हैं",
            "मुझे अपनी जीन्स नहीं बदलनी हैं",
            "मुझे सच में अपनी जीन्स नहीं बदलनी हैं",
            "मुझे फिर से अपनी जीन्स नहीं बदलनी हैं",
            "मुझे सच में फिर से अपनी जीन्स नहीं बदलनी हैं"

    },{"मुझे अपनी pant बदलना अच्छा लगता हैं",
            "मुझे सच में अपनी pant बदलना अच्छा लगता हैं",
            "मुझे अपनी pant बदलनी हैं",
            "मुझे सच में अपनी pant बदलनी हैं",
            "मुझे फिर से अपनी pant बदलनी हैं",
            "मुझे सच में फिर से अपनी pant बदलनी हैं",
            "मुझे अपनी pant बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी pant बदलना अच्छा नहीं लगता हैं",
            "मुझे अपनी pant नहीं बदलनी हैं",
            "मुझे सच में अपनी pant नहीं बदलनी हैं",
            "मुझे फिर से अपनी pant नहीं बदलनी हैं",
            "मुझे सच में फिर से अपनी pant नहीं बदलनी हैं"

    },{"मुझे अपने लैगिंग्स बदलना अच्छा लगता हैं",
            "मुझे सच में अपने लैगिंग्स बदलना अच्छा लगता हैं",
            "मुझे अपने लैगिंग्स बदलने हैं",
            "मुझे सच में अपने लैगिंग्स बदलने हैं",
            "मुझे फिर से अपने लैगिंग्स बदलने हैं",
            "मुझे सच में फिर से अपने लैगिंग्स बदलने हैं",
            "मुझे अपने लैगिंग्स बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने लैगिंग्स बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने लैगिंग्स नहीं बदलने हैं",
            "मुझे सच में अपने लैगिंग्स नहीं बदलने हैं",
            "मुझे फिर से अपने लैगिंग्स नहीं बदलने हैं",
            "मुझे सच में फिर से अपने लैगिंग्स नहीं बदलने हैं"

    },{"मुझे अपने slacks बदलना अच्छा लगता हैं",
            "मुझे सच में अपने slacks बदलना अच्छा लगता हैं",
            "मुझे अपने slacks बदलने हैं",
            "मुझे सच में अपने slacks बदलने हैं",
            "मुझे फिर से अपने slacks बदलने हैं",
            "मुझे सच में फिर से अपने slacks बदलने हैं",
            "मुझे अपने slacks बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने slacks बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने slacks नहीं बदलने हैं",
            "मुझे सच में अपने slacks नहीं बदलने हैं",
            "मुझे फिर से अपने slacks नहीं बदलने हैं",
            "मुझे सच में फिर से अपने slacks नहीं बदलने हैं"

    },{"मुझे अपने शॉर्ट्स बदलना अच्छा लगता हैं",
            "मुझे सच में अपने शॉर्ट्स बदलना अच्छा लगता हैं",
            "मुझे अपने शॉर्ट्स बदलने हैं",
            "मुझे सच में अपने शॉर्ट्स बदलने हैं",
            "मुझे फिर से अपने शॉर्ट्स बदलने हैं",
            "मुझे सच में फिर से अपने शॉर्ट्स बदलने हैं",
            "मुझे अपने शॉर्ट्स बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शॉर्ट्स बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने शॉर्ट्स नहीं बदलने हैं",
            "मुझे सच में अपने शॉर्ट्स नहीं बदलने हैं",
            "मुझे फिर से अपने शॉर्ट्स नहीं बदलने हैं",
            "मुझे सच में फिर से अपने शॉर्ट्स नहीं बदलने हैं"

    },{"मुझे अपना इनर वेअर बदलना अच्छा लगता हैं",
            "मुझे सच में अपना इनर वेअर बदलना अच्छा लगता हैं",
            "मुझे अपना इनर वेअर बदलना हैं",
            "मुझे सच में अपना इनर वेअर बदलना हैं",
            "मुझे फिर से अपना इनर वेअर बदलना हैं",
            "मुझे सच में फिर से अपना इनर वेअर बदलना हैं",
            "मुझे अपना इनर वेअर बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना इनर वेअर बदलना अच्छा नहीं लगता हैं",
            "मुझे अपना इनर वेअर नहीं बदलना हैं",
            "मुझे सच में अपना इनर वेअर नहीं बदलना हैं",
            "मुझे फिर से अपना इनर वेअर नहीं बदलना हैं",
            "मुझे सच में फिर से अपना इनर वेअर नहीं बदलना हैं"

    },{"मुझे अपने जूते बदलना अच्छा लगता हैं",
            "मुझे सच में अपने जूते बदलना अच्छा लगता हैं",
            "मुझे अपने जूते बदलने हैं",
            "मुझे सच में अपने जूते बदलने हैं",
            "मुझे फिर से अपने जूते बदलने हैं",
            "मुझे सच में फिर से अपने जूते बदलने हैं",
            "मुझे अपने जूते बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने जूते बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने जूते नहीं बदलने हैं",
            "मुझे सच में अपने जूते नहीं बदलने हैं",
            "मुझे फिर से अपने जूते नहीं बदलने हैं",
            "मुझे सच में फिर से अपने जूते नहीं बदलने हैं"

    },{"मुझे अपने बूट बदलना अच्छा लगता हैं",
            "मुझे सच में अपने बूट बदलना अच्छा लगता हैं",
            "मुझे अपने बूट बदलने हैं",
            "मुझे सच में अपने बूट बदलने हैं",
            "मुझे फिर से अपने बूट बदलने हैं",
            "मुझे सच में फिर से अपने बूट बदलने हैं",
            "मुझे अपने बूट बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बूट बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने बूट नहीं बदलने हैं",
            "मुझे सच में अपने बूट नहीं बदलने हैं",
            "मुझे फिर से अपने बूट नहीं बदलने हैं",
            "मुझे सच में फिर से अपने बूट नहीं बदलने हैं"

    },{"मुझे अपने मोज़े बदलना अच्छा लगता हैं",
            "मुझे सच में अपने मोज़े बदलना अच्छा लगता हैं",
            "मुझे अपने मोज़े बदलने हैं",
            "मुझे सच में अपने मोज़े बदलने हैं",
            "मुझे फिर से अपने मोज़े बदलने हैं",
            "मुझे सच में फिर से अपने मोज़े बदलने हैं",
            "मुझे अपने मोज़े बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने मोज़े बदलना अच्छा नहीं लगता हैं",
            "मुझे अपने मोज़े नहीं बदलने हैं",
            "मुझे सच में अपने मोज़े नहीं बदलने हैं",
            "मुझे फिर से अपने मोज़े नहीं बदलने हैं",
            "मुझे सच में फिर से अपने मोज़े नहीं बदलने हैं"

    },{"मुझे रात के कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे सच में रात के कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे रात के कपड़ऐ पैहनने हैं",
            "मुझे सच में रात के कपड़ऐ पैहनने हैं",
            "मुझे फिर से रात के कपड़ऐ पैहनने हैं",
            "मुझे सच में फिर से रात के कपड़ऐ पैहनने हैं",
            "मुझे रात के कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में रात के कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे फिर से रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में फिर से रात के कपड़ऐ नहीं पैहनने हैं"

    },{"मुझे शर्ट पैहनना अच्छा लगता हैं",
            "मुझे सच में शर्ट पैहनना अच्छा लगता हैं",
            "मुझे शर्ट पैहनना हैं",
            "मुझे सच में शर्ट पैहनना हैं",
            "मुझे फिर से शर्ट पैहनना हैं",
            "मुझे सच में फिर से शर्ट पैहनना हैं",
            "मुझे शर्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में शर्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे शर्ट नहीं पैहनना हैं",
            "मुझे सच में शर्ट नहीं पैहनना हैं",
            "मुझे फिर से शर्ट नहीं पैहनना हैं",
            "मुझे सच में फिर से शर्ट नहीं पैहनना हैं"

    },{"मुझे टी-शर्ट पैहनना अच्छा लगता हैं",
            "मुझे सच में टी-शर्ट पैहनना अच्छा लगता हैं",
            "मुझे टी-शर्ट पैहनना हैं",
            "मुझे सच में टी-शर्ट पैहनना हैं",
            "मुझे फिर से टी-शर्ट पैहनना हैं",
            "मुझे सच में फिर से टी-शर्ट पैहनना हैं",
            "मुझे टी-शर्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में टी-शर्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे टी-शर्ट नहीं पैहनना हैं",
            "मुझे सच में टी-शर्ट नहीं पैहनना हैं",
            "मुझे फिर से टी-शर्ट नहीं पैहनना हैं",
            "मुझे सच में फिर से टी-शर्ट नहीं पैहनना हैं"

    },{"मुझे फ्रॉक पैहनना अच्छा लगता हैं",
            "मुझे सच में फ्रॉक पैहनना अच्छा लगता हैं",
            "मुझे फ्रॉक पैहनना हैं",
            "मुझे सच में फ्रॉक पैहनना हैं",
            "मुझे फिर से फ्रॉक पैहनना हैं",
            "मुझे सच में फिर से फ्रॉक पैहनना हैं",
            "मुझे फ्रॉक पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में फ्रॉक पैहनना अच्छा नहीं लगता हैं",
            "मुझे फ्रॉक नहीं पैहनना हैं",
            "मुझे सच में फ्रॉक नहीं पैहनना हैं",
            "मुझे फिर से फ्रॉक नहीं पैहनना हैं",
            "मुझे सच में फिर से फ्रॉक नहीं पैहनना हैं"

    },{"मुझे pant पैहनना अच्छा लगता हैं",
            "मुझे सच में pant पैहनना अच्छा लगता हैं",
            "मुझे pant पैहननी हैं",
            "मुझे सच में pant पैहननी हैं",
            "मुझे फिर से pant पैहननी हैं",
            "मुझे सच में फिर से pant पैहननी हैं",
            "मुझे pant पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में pant पैहनना अच्छा नहीं लगता हैं",
            "मुझे pant नहीं पैहननी हैं",
            "मुझे सच में pant नहीं पैहननी हैं",
            "मुझे फिर से pant नहीं पैहननी हैं",
            "मुझे सच में फिर से pant नहीं पैहननी हैं"

    },{"मुझे slacks पैहनना अच्छा लगता हैं",
            "मुझे सच में slacks पैहनना अच्छा लगता हैं",
            "मुझे slacks पैहनने हैं",
            "मुझे सच में slacks पैहनने हैं",
            "मुझे फिर से slacks पैहनने हैं",
            "मुझे सच में फिर से slacks पैहनने हैं",
            "मुझे slacks पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में slacks पैहनना अच्छा नहीं लगता हैं",
            "मुझे slacks नहीं पैहनने हैं",
            "मुझे सच में slacks नहीं पैहनने हैं",
            "मुझे फिर से slacks नहीं पैहनने हैं",
            "मुझे सच में फिर से slacks नहीं पैहनने हैं"

    },{"मुझे लेगिंज पैहनना अच्छा लगता हैं",
            "मुझे सच में लेगिंज पैहनना अच्छा लगता हैं",
            "मुझे लेगिंज पैहनने हैं",
            "मुझे सच में लेगिंज पैहनने हैं",
            "मुझे फिर से लेगिंज पैहनने हैं",
            "मुझे सच में फिर से लेगिंज पैहनने हैं",
            "मुझे लैगिंग्स पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में लैगिंग्स पैहनना अच्छा नहीं लगता हैं",
            "मुझे लेगिंज नहीं पैहनने हैं",
            "मुझे सच में लेगिंज नहीं पैहनने हैं",
            "मुझे फिर से लेगिंज नहीं पैहनने हैं",
            "मुझे सच में फिर से लेगिंज नहीं पैहनने हैं"

    },{"मुझे शॉर्ट्स पैहनना अच्छा लगता हैं",
            "मुझे सच में शॉर्ट्स पैहनना अच्छा लगता हैं",
            "मुझे शॉर्ट्स पैहनने हैं",
            "मुझे सच में शॉर्ट्स पैहनने हैं",
            "मुझे फिर से शॉर्ट्स पैहनने हैं",
            "मुझे सच में फिर से शॉर्ट्स पैहनने हैं",
            "मुझे शॉर्ट्स पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में शॉर्ट्स पैहनना अच्छा नहीं लगता हैं",
            "मुझे शॉर्ट्स नहीं पैहनने हैं",
            "मुझे सच में शॉर्ट्स नहीं पैहनने हैं",
            "मुझे फिर से शॉर्ट्स नहीं पैहनने हैं",
            "मुझे सच में फिर से शॉर्ट्स नहीं पैहनने हैं"

    },{"मुझे सलवार कमीज़ पैहनना अच्छा लगता हैं",
            "मुझे सच में सलवार कमीज़ पैहनना अच्छा लगता हैं",
            "मुझे सलवार कमीज़ पैहनना हैं",
            "मुझे सच में सलवार कमीज़ पैहनना हैं",
            "मुझे फिर से सलवार कमीज़ पैहनना हैं",
            "मुझे सच में फिर से सलवार कमीज़ पैहनना हैं",
            "मुझे सलवार कमीज़ पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में सलवार कमीज़ पैहनना अच्छा नहीं लगता हैं",
            "मुझे सलवार कमीज़ नहीं पैहनना हैं",
            "मुझे सच में सलवार कमीज़ नहीं पैहनना हैं",
            "मुझे फिर से सलवार कमीज़ नहीं पैहनना हैं",
            "मुझे सच में फिर से सलवार कमीज़ नहीं पैहनना हैं"

    },{"मुझे sweater पैहनना अच्छा लगता हैं",
            "मुझे सच में sweater पैहनना अच्छा लगता हैं",
            "मुझे sweater पैहनना हैं",
            "मुझे सच में sweater पैहनना हैं",
            "मुझे फिर से sweater पैहनना हैं",
            "मुझे सच में फिर से sweater पैहनना हैं",
            "मुझे sweater पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में sweater पैहनना अच्छा नहीं लगता हैं",
            "मुझे sweater नहीं पैहनना हैं",
            "मुझे सच में sweater नहीं पैहनना हैं",
            "मुझे फिर से sweater नहीं पैहनना हैं",
            "मुझे सच में फिर से sweater नहीं पैहनना हैं"

    },{"मुझे jacket पैहनना अच्छा लगता हैं",
            "मुझे सच में jacket पैहनना अच्छा लगता हैं",
            "मुझे jacket पैहनना हैं",
            "मुझे सच में jacket पैहनना हैं",
            "मुझे फिर से jacket पैहनना हैं",
            "मुझे सच में फिर से jacket पैहनना हैं",
            "मुझे jacket पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में jacket पैहनना अच्छा नहीं लगता हैं",
            "मुझे jacket नहीं पैहनना हैं",
            "मुझे सच में jacket नहीं पैहनना हैं",
            "मुझे फिर से jacket नहीं पैहनना हैं",
            "मुझे सच में फिर से jacket नहीं पैहनना हैं"

    },{"मुझे दुपट्टा पैहनना अच्छा लगता हैं",
            "मुझे सच में दुपट्टा पैहनना अच्छा लगता हैं",
            "मुझे दुपट्टा पैहनना हैं",
            "मुझे सच में दुपट्टा पैहनना हैं",
            "मुझे फिर से दुपट्टा पैहनना हैं",
            "मुझे सच में फिर से दुपट्टा पैहनना हैं",
            "मुझे दुपट्टा पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में दुपट्टा पैहनना अच्छा नहीं लगता हैं",
            "मुझे दुपट्टा नहीं पैहनना हैं",
            "मुझे सच में दुपट्टा नहीं पैहनना हैं",
            "मुझे फिर से दुपट्टा नहीं पैहनना हैं",
            "मुझे सच में फिर से दुपट्टा नहीं पैहनना हैं"

    },{"मुझे टोपी पैहनना अच्छा लगता हैं",
            "मुझे सच में टोपी पैहनना अच्छा लगता हैं",
            "मुझे टोपी पैहननी हैं",
            "मुझे सच में टोपी पैहननी हैं",
            "मुझे फिर से टोपी पैहननी हैं",
            "मुझे सच में फिर से टोपी पैहननी हैं",
            "मुझे टोपी पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में टोपी पैहनना अच्छा नहीं लगता हैं",
            "मुझे टोपी नहीं पैहननी हैं",
            "मुझे सच में टोपी नहीं पैहननी हैं",
            "मुझे फिर से टोपी नहीं पैहननी हैं",
            "मुझे सच में फिर से टोपी नहीं पैहननी हैं"

    },{"मुझे बैल्ट पैहनना अच्छा लगता हैं",
            "मुझे सच में बैल्ट पैहनना अच्छा लगता हैं",
            "मुझे बैल्ट पैहनना हैं",
            "मुझे सच में बैल्ट पैहनना हैं",
            "मुझे फिर से बैल्ट पैहनना हैं",
            "मुझे सच में फिर से बैल्ट पैहनना हैं",
            "मुझे बैल्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में बैल्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे बैल्ट नहीं पैहनना हैं",
            "मुझे सच में बैल्ट नहीं पैहनना हैं",
            "मुझे फिर से बैल्ट नहीं पैहनना हैं",
            "मुझे सच में फिर से बैल्ट नहीं पैहनना हैं"

    },{"मुझे रेनकोट पैहनना अच्छा लगता हैं",
            "मुझे सच में रेनकोट पैहनना अच्छा लगता हैं",
            "मुझे रेनकोट पैहनना हैं",
            "मुझे सच में रेनकोट पैहनना हैं",
            "मुझे फिर से रेनकोट पैहनना हैं",
            "मुझे सच में फिर से रेनकोट पैहनना हैं",
            "मुझे रेनकोट पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में रेनकोट पैहनना अच्छा नहीं लगता हैं",
            "मुझे रेनकोट नहीं पैहनना हैं",
            "मुझे सच में रेनकोट नहीं पैहनना हैं",
            "मुझे फिर से रेनकोट नहीं पैहनना हैं",
            "मुझे सच में फिर से रेनकोट नहीं पैहनना हैं"

    },{"मुझे अपना चश्मा पैहनना अच्छा लगता हैं",
            "मुझे सच में अपना चश्मा पैहनना अच्छा लगता हैं",
            "मुझे अपना चश्मा पैहनना हैं",
            "मुझे सच में अपना चश्मा पैहनना हैं",
            "मुझे फिर से अपना चश्मा पैहनना हैं",
            "मुझे सच में फिर से अपना चश्मा पैहनना हैं",
            "मुझे अपना चश्मा पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना चश्मा पैहनना अच्छा नहीं लगता हैं",
            "मुझे अपना चश्मा नहीं पैहनना हैं",
            "मुझे सच में अपना चश्मा नहीं पैहनना हैं",
            "मुझे फिर से अपना चश्मा नहीं पैहनना हैं",
            "मुझे सच में फिर से अपना चश्मा नहीं पैहनना हैं"

    },{"मुझे अपनी घड़ी पैहनना अच्छा लगता हैं",
            "मुझे सच में अपनी घड़ी पैहनना अच्छा लगता हैं",
            "मुझे अपनी घड़ी पैहननी हैं",
            "मुझे सच में अपनी घड़ी पैहननी हैं",
            "मुझे फिर से अपनी घड़ी पैहननी हैं",
            "मुझे सच में फिर से अपनी घड़ी पैहननी हैं",
            "मुझे अपनी घड़ी पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी घड़ी पैहनना अच्छा नहीं लगता हैं",
            "मुझे अपनी घड़ी नहीं पैहननी हैं",
            "मुझे सच में अपनी घड़ी नहीं पैहननी हैं",
            "मुझे फिर से अपनी घड़ी नहीं पैहननी हैं",
            "मुझे सच में फिर से अपनी घड़ी नहीं पैहननी हैं"

    },{"मुझे कान में बालि पैहनना अच्छा लगता हैं",
            "मुझे सच में कान में बालि पैहनना अच्छा लगता हैं",
            "मुझे कान में बालि पैहननी हैं",
            "मुझे सच में कान में बालि पैहननी हैं",
            "मुझे फिर से कान में बालि पैहननी हैं",
            "मुझे सच में फिर से कान में बालि पैहननी हैं",
            "मुझे कान में बालि पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में कान में बालि पैहनना अच्छा नहीं लगता हैं",
            "मुझे कान में बालि नहीं पैहननी हैं",
            "मुझे सच में कान में बालि नहीं पैहननी हैं",
            "मुझे फिर से कान में बालि नहीं पैहननी हैं",
            "मुझे सच में फिर से कान में बालि नहीं पैहननी हैं"

    },{"मुझे कंगन पैहनना अच्छा लगता हैं",
            "मुझे सच में कंगन पैहनना अच्छा लगता हैं",
            "मुझे कंगन पैहनने हैं",
            "मुझे सच में कंगन पैहनने हैं",
            "मुझे फिर से कंगन पैहनने हैं",
            "मुझे सच में फिर से कंगन पैहनने हैं",
            "मुझे कंगन पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में कंगन पैहनना अच्छा नहीं लगता हैं",
            "मुझे कंगन नहीं पैहनने हैं",
            "मुझे सच में कंगन नहीं पैहनने हैं",
            "मुझे फिर से कंगन नहीं पैहनने हैं",
            "मुझे सच में फिर से कंगन नहीं पैहनने हैं"

    },{"मुझे हार पैहनना अच्छा लगता हैं",
            "मुझे सच में हार पैहनना अच्छा लगता हैं",
            "मुझे एक हार पैहनना हैं",
            "मुझे सच में एक हार पैहनना हैं",
            "मुझे फिर से हार पैहनना हैं",
            "मुझे सच में फिर से हार पैहनना हैं",
            "मुझे हार पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में हार पैहनना अच्छा नहीं लगता हैं",
            "मुझे हार नहीं पैहनना हैं",
            "मुझे सच में हार नहीं पैहनना हैं",
            "मुझे फिर से हार नहीं पैहनना हैं",
            "मुझे सच में फिर से हार नहीं पैहनना हैं"

    },{"मुझे बिंदी लगाना अच्छा लगता हैं",
            "मुझे सच में बिंदी लगाना अच्छा लगता हैं",
            "मुझे बिंदी लगानी हैं",
            "मुझे सच में बिंदी लगानी हैं",
            "मुझे फिर से बिंदी लगानी हैं",
            "मुझे सच में फिर से बिंदी लगानी हैं",
            "मुझे बिंदी लगाना अच्छा नहीं लगता हैं",
            "मुझे सच में बिंदी लगाना अच्छा नहीं लगता हैं",
            "मुझे बिंदी नहीं लगानी हैं",
            "मुझे सच में बिंदी नहीं लगानी हैं",
            "मुझे फिर से बिंदी नहीं लगानी हैं",
            "मुझे सच में फिर से बिंदी नहीं लगानी हैं"

    },{"मुझे चप्पल पैहनना अच्छा लगता हैं",
            "मुझे सच में चप्पल पैहनना अच्छा लगता हैं",
            "मुझे चप्पल पैहननी हैं",
            "मुझे सच में चप्पल पैहननी हैं",
            "मुझे फिर से चप्पल पैहननी हैं",
            "मुझे सच में फिर से चप्पल पैहननी हैं",
            "मुझे चप्पल पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में चप्पल पैहनना अच्छा नहीं लगता हैं",
            "मुझे चप्पल नहीं पैहननी हैं",
            "मुझे सच में चप्पल नहीं पैहननी हैं",
            "मुझे फिर से चप्पल नहीं पैहननी हैं",
            "मुझे सच में फिर से चप्पल नहीं पैहननी हैं"

    },{},{},{},{}},{{"मुझे अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे अपने बालों को कंघी करनी हैं",
            "मुझे सच में अपने बालों को कंघी करनी हैं",
            "मुझे फिर से अपने बालों को कंघी करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी करनी हैं",
            "मुझे अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में अपने बालों को कंघी नहीं करनी हैं",
            "मुझे फिर से अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी नहीं करनी हैं"

    },{"मुझे फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा लगता हैं",
            "मुझे फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल करना हैं",
            "मुझे फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में फेस वॉश का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे फिर से फेस वॉश का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से फेस वॉश का इस्तमाल नहीं करना हैं"

    },{"मुझे अपने नाखून काटना अच्छा लगता हैं",
            "मुझे सच में अपने नाखून काटना अच्छा लगता हैं",
            "मुझे अपने नाखून काटने हैं",
            "मुझे सच में अपने नाखून काटने हैं",
            "मुझे फिर से अपने नाखून काटने हैं",
            "मुझे सच में फिर से अपने नाखून काटने हैं",
            "मुझे अपने नाखून काटने अच्छा नहीं लगता हैं",
            "मुझे सच में अपने नाखून काटने अच्छा नहीं लगता हैं",
            "मुझे अपने नाखून नहीं काटने हैं",
            "मुझे सच में अपने नाखून नहीं काटनै हैं",
            "मुझे फिर से अपने नाखून नहीं काटने हैं",
            "मुझे सच में फिर से अपने नाखून नहीं काटने हैं"

    },{"मुझे अपनी नाक साफ करना अच्छा लगता हैं",
            "मुझे सच में अपनी नाक साफ करना अच्छा लगता हैं",
            "मुझे अपनी नाक साफ करनी हैं",
            "मुझे सच में अपनी नाक साफ करनी हैं",
            "मुझे फिर से अपनी नाक साफ करनी हैं",
            "मुझे सच में फिर से अपनी नाक साफ करनी हैं",
            "मुझे अपनी नाक साफ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी नाक साफ करना अच्छा नहीं लगता हैं",
            "मुझे अपनी नाक साफ नहीं करनी हैं",
            "मुझे सच में अपनी नाक साफ नहीं करनी हैं",
            "मुझे फिर से अपनी नाक साफ नहीं करनी हैं",
            "मुझे सच में फिर से अपनी नाक साफ नहीं करनी हैं"

    },{"मुझे साबुन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में साबुन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे साबुन चाहिए",
            "मुझे सच में साबुन चाहिए",
            "मुझे फिर से साबुन का इस्तमाल करना हैं",
            "मुझे सच में फिर से साबुन का इस्तमाल करना हैं",
            "मुझे साबुन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में साबुन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे साबुन नहीं चाहिए",
            "मुझे सच में साबुन नहीं चाहिए",
            "मुझे फिर से साबुन का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से साबुन का इस्तमाल नहीं करना हैं"

    },{"मुझे shampoo का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में shampoo का इस्तमाल करना अच्छा लगता हैं",
            "मुझे shampoo का इस्तमाल करना हैं",
            "मुझे सच में shampoo का इस्तमाल करना हैं",
            "मुझे और shampoo चाहिए",
            "मुझे सच में shampoo थोड़ा और चाहिए",
            "मुझे shampoo का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में shampoo का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे shampoo का इस्तमाल नहीं करना हैं",
            "मुझे सच में shampoo का इस्तमाल नहीं करना हैं",
            "मुझे और shampoo नहीं चाहिए",
            "मुझे सच में shampo oबिल्कुल नहीं चाहिए"

    }},{{"मुझे दरवाज़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में दरवाज़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे दरवाज़ा खोलना हैं",
            "मुझे सच में दरवाज़ा खोलना हैं",
            "मुझे फिर से दरवाज़ा खोलना हैं",
            "मुझे सच में फिर से दरवाज़ा खोलना हैं",
            "मुझे दरवाज़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में दरवाज़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे दरवाज़ा बंद करना हैं",
            "मुझे सच में दरवाज़ा बंद करना हैं",
            "मुझे फिर से दरवाज़ा बंद करना हैं",
            "मुझे सच में फिर से दरवाज़ा बंद करना हैं"

    },{"मुझे पंखे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में पंखे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे पंखा चालू करना हैं",
            "मुझे सच में पंखा चालू करना हैं",
            "मुझे फिर से पंखा चालू करना हैं",
            "मुझे सच में फिर से पंखा चालू करना हैं",
            "मुझे पंखे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में पंखे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे पंखा बंद करना हैं",
            "मुझे सच में पंखा बंद करना हैं",
            "मुझे फिर से पंखा बंद करना हैं",
            "मुझे सच में फिर से पंखा बंद करना हैं"

    },{"मुझे लाईट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में लाईट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे लाईट चालू करनी हैं",
            "मुझे सच में लाईट चालू करनी हैं",
            "मुझे और रौशनी चाहिए",
            "मुझे सच में थोड़ी और रौशनी चाहिए",
            "मुझे लाईट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में लाईट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे लाईट बंद करनी हैं",
            "मुझे सच में लाईट बंद करनी हैं",
            "मुझे और रौशनी नहीं चाहिए",
            "मुझे सच में और रौशनी नहीं चाहिए"

    },{"मुझे खिड़की का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खिड़की का इस्तमाल करना अच्छा लगता हैं",
            "मुझे खिड़की खोलनी हैं",
            "मुझे सच में खिड़की खोलनी हैं",
            "मुझे फिर से खिड़की खोलनी हैं",
            "मुझे सच में फिर से खिड़की खोलनी हैं",
            "मुझे खिड़की का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खिड़की का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे खिड़की बंद करनी हैं",
            "मुझे सच में खिड़की बंद करनी हैं",
            "मुझे फिर से खिड़की बंद करनी हैं",
            "मुझे सच में फिर से खिड़की बंद करनी हैं",

    },{"मुझे बिस्तर पर सोना अच्छा लगता हैं",
            "मुझे सच में बिस्तर पर सोना अच्छा लगता हैं",
            "मुझे बिस्तर पर सोना हैं",
            "मुझे सच में बिस्तर पर सोना हैं",
            "मुझे बिस्तर पर और समय के लिए सोना हैं",
            "मुझे सच में बिस्तर पर थोड़े और समय के लिए सोना हैं",
            "मुझे बिस्तर पर सोना अच्छा नहीं लगता हैं",
            "मुझे सच में बिस्तर पर सोना अच्छा नहीं लगता हैं",
            "मुझे बिस्तर पर नहीं सोना हैं",
            "मुझे सच में बिस्तर पर नहीं सोना हैं",
            "मुझे बिस्तर पर और समय के लिए नहीं सोना हैं",
            "मुझे सच में बिस्तर पर बिल्कुल नहीं सोना हैं"

    },{"मुझे तकिए पर सोना अच्छा लगता हैं",
            "मुझे सच में तकिए पर सोना अच्छा लगता हैं",
            "मुझे एक तकिया चाहिए",
            "मुझे सच में एक तकिया चाहिए",
            "मुझे और तकियें चाहिए",
            "मुझे सच में कुछ और तकियें चाहिए",
            "मुझे तकिए पर सोना अच्छा नहीं लगता हैं",
            "मुझे सच में तकिए पर सोना अच्छा नहीं लगता हैं",
            "मुझे तकिया नहीं चाहिए",
            "मुझे सच में तकिया नहीं चाहिए",
            "मुझे और तकियें नहीं चाहिए",
            "मुझे सच में और तकियें नहीं चाहिए"

    },{"मुझे कंबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कंबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक कंबल चाहिए",
            "मुझे सच में एक कंबल चाहिए",
            "मुझे और कंबल चाहिए",
            "मुझे सच में कुछ और कंबल चाहिए",
            "मुझे कंबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कंबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कंबल नहीं चाहिए",
            "मुझे सच में कंबल नहीं चाहिए",
            "मुझे और कंबल नहीं चाहिए",
            "मुझे सच में और कंबल नहीं चाहिए"

    },{"मुझे गरम महसूस करना अच्छा लगता हैं",
            "मुझे सच में गरम महसूस करना अच्छा लगता हैं",
            "मुझे गरम महसूस करना हैं",
            "मुझे सच में गरम महसूस करना हैं",
            "मुझे और गरम महसूस करना हैं",
            "मुझे सच में और गरम महसूस करना हैं",
            "मुझे गरम महसूस करना अच्छा नहीं लगता हैं",
            "मुझे सच में गरम महसूस करना अच्छा नहीं लगता हैं",
            "मुझे गरम नहीं महसूस करना हैं",
            "मुझे सच में गरम नहीं महसूस करना हैं",
            "मुझे और गर्मी नहीं चाहिए",
            "मुझे सच में बिल्कुल गर्मी नहीं चाहिए"

    },{"मुझे ठंडक महसूस करना अच्छा लगता हैं",
            "मुझे सच में ठंडक महसूस करना अच्छा लगता हैं",
            "मुझे ठंडक महसूस करनी हैं",
            "मुझे सच में ठंडक महसूस करनी हैं",
            "मुझे और ठंडक चाहिए",
            "मुझे सच में और ठंडक चाहिए",
            "मुझे ठंडक महसूस करना अच्छा नहीं लगता हैं",
            "मुझे सच में ठंडक महसूस करना अच्छा नहीं लगता हैं",
            "मुझे ठंडक नहीं महसूस करनी हैं",
            "मुझे सच में ठंडक नहीं महसूस करनी हैं",
            "मुझे और ठंडक नहीं चाहिए",
            "मुझे सच में बिल्कुल ठंडक नहीं चाहिए"

    }},{{"मुझे कसरत करना अच्छा लगता हैं",
            "मुझे सच में कसरत करना अच्छा लगता हैं",
            "मुझे कसरत करनी हैं",
            "मुझे सच में कसरत करनी हैं",
            "मुझे और कसरत करनी हैं",
            "मुझे सच में थोड़ी और कसरत करनी हैं",
            "मुझे कसरत करना अच्छा नहीं लगता हैं",
            "मुझे सच में कसरत करना अच्छा नहीं लगता हैं",
            "मुझे कसरत नहीं करनी हैं",
            "मुझे सच में कसरत नहीं करनी हैं",
            "मुझे और कसरत नहीं करनी हैं",
            "मुझे सच में और कसरत नहीं करनी हैं"

    },{"मुझे झूले पर बैठना अच्छा लगता हैं",
            "मुझे सच में झूले पर बैठना अच्छा लगता हैं",
            "मुझे झूले पर बैठना हैं",
            "मुझे सच में झूले पर बैठना हैं",
            "मुझे और समय के लिए झूले पर बैठना हैं",
            "मुझे सच में थोड़े और समय के लिए झूले पर बैठना हैं",
            "मुझे झूले पर बैठना अच्छा नहीं लगता हैं",
            "मुझे सच में झूले पर बैठना अच्छा नहीं लगता हैं",
            "मुझे झूले पर नहीं बैठना हैं",
            "मुझे सच में झूले पर नहीं बैठना हैं",
            "मुझे झूले पर और समय के लिए नहीं बैठना हैं",
            "मुझे सच में झूले पर बिल्कुल नहीं बैठना हैं"

    },{"मुझे trampoline का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में trampoline का इस्तमाल करना अच्छा लगता हैं",
            "मुझे trampoline का इस्तमाल करना हैं",
            "मुझे सच में trampoline का इस्तमाल करना हैं",
            "मुझे और समय के लिए trampoline का इस्तमाल करना हैं",
            "मुझे सच में थोड़े और समय के लिए trampoline का इस्तमाल करना हैं",
            "मुझे trampoline का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में trampoline का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे trampoline का इस्तमाल नहीं करना हैं",
            "मुझे सच में trampoline का इस्तमाल नहीं करना हैं",
            "मुझे और समय के लिए trampoline का इस्तमाल नहीं करना हैं",
            "मुझे सच में trampoline का इस्तमाल बिल्कुल नहीं करना हैं"

    },{"मुझे स्विस बॉल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में स्विस बॉल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे स्विस बॉल का इस्तमाल करना हैं",
            "मुझे सच में स्विस बॉल का इस्तमाल करना हैं",
            "मुझे और समय के लिए स्विस बॉल का इस्तमाल करना हैं",
            "मुझे सच में थोड़े और समय के लिए स्विस बॉल का इस्तमाल करना हैं",
            "मुझे स्विस बॉल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्विस बॉल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे स्विस बॉल का इस्तमाल नहीं करना हैं",
            "मुझे सच में स्विस बॉल का इस्तमाल नहीं करना हैं",
            "मुझे और समय के लिए स्विस बॉल का इस्तमाल नहीं करना हैं",
            "मुझे सच में स्विस बॉल का इस्तमाल बिल्कुल नहीं करना हैं"

    },{"मुझे कंबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कंबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे कंबल का इस्तमाल करना हैं",
            "मुझे सच में कंबल का इस्तमाल करना हैं",
            "मुझे फिर से कंबल का इस्तमाल करना हैं",
            "मुझे सच में फिर से कंबल का इस्तमाल करना हैं",
            "मुझे कंबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कंबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कंबल का इस्तमाल नहीं करना हैं",
            "मुझे सच में कंबल का इस्तमाल नहीं करना हैं",
            "मुझे फिर से कंबल का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से कंबल का इस्तमाल नहीं करना हैं"

    },{"मुझे बॉल पिट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में बॉल पिट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे बॉल पिट का इस्तमाल करना हैं",
            "मुझे सच में बॉल पिट का इस्तमाल करना हैं",
            "मुझे फिर से बॉल पिट का इस्तमाल करना हैं",
            "मुझे सच में फिर से बॉल पिट का इस्तमाल करना हैं",
            "मुझे बॉल पिट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में बॉल पिट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे बॉल पिट का इस्तमाल नहीं करना हैं",
            "मुझे सच में बॉल पिट का इस्तमाल नहीं करना हैं",
            "मुझे फिर से बॉल पिट का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से बॉल पिट का इस्तमाल नहीं करना हैं"

    },{"मुझे हातों की कसरत करना अच्छा लगता हैं",
            "मुझे सच में हातों की कसरत करना अच्छा लगता हैं",
            "मुझे हातों की कसरत करनी हैं",
            "मुझे सच में हातों की कसरत करनी हैं",
            "मुझे हातों की और कसरत करनी हैं",
            "मुझे सच में हातों की थोड़ी और कसरत करनी हैं",
            "मुझे हातों की कसरत करना अच्छा नहीं लगता हैं",
            "मुझे सच में हातों की कसरत करना अच्छा नहीं लगता हैं",
            "मुझे हातों की कसरत नहीं करनी हैं",
            "मुझे सच में हातों की कसरत नहीं करनी हैं",
            "मुझे हातों की और कसरत नहीं करनी हैं",
            "मुझे सच में हातों की और कसरत नहीं करनी हैं"

    },{"मुझे पैरों की कसरत करना अच्छा लगता हैं",
            "मुझे सच में पैरों की कसरत करना अच्छा लगता हैं",
            "मुझे पैरों की कसरत करनी हैं",
            "मुझे सच में पैरों की कसरत करनी हैं",
            "मुझे पैरों की, और कसरत करनी हैं",
            "मुझे सच में पैरों की, थोड़ी और कसरत करनी हैं",
            "मुझे पैरों की कसरत करना अच्छा नहीं लगता हैं",
            "मुझे सच में पैरों की कसरत करना अच्छा नहीं लगता हैं",
            "मुझे पैरों की कसरत नहीं करनी हैं",
            "मुझे सच में पैरों की कसरत नहीं करनी हैं",
            "मुझे पैरों की, और कसरत नहीं करनी हैं",
            "मुझे सच में पैरों की, और कसरत नहीं करनी हैं"

    },{"मुझे बॉडी वेस्ट पैहनना अच्छा लगता हैं",
            "मुझे सच में बॉडी वेस्ट पैहनना अच्छा लगता हैं",
            "मुझे बॉडी वेस्ट पैहनना हैं",
            "मुझे सच में बॉडी वेस्ट पैहनना हैं",
            "मुझे फिर से बॉडी वेस्ट पैहनना हैं",
            "मुझे सच में फिर से बॉडी वेस्ट पैहनना हैं",
            "मुझे बॉडी वेस्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में बॉडी वेस्ट पैहनना अच्छा नहीं लगता हैं",
            "मुझे बॉडी वेस्ट नहीं पैहनना हैं",
            "मुझे सच में बॉडी वेस्ट नहीं पैहनना हैं",
            "मुझे फिर से बॉडी वेस्ट नहीं पैहनना हैं",
            "मुझे सच में फिर से बॉडी वेस्ट नहीं पैहनना हैं"

    }},{{"मुझे सुबह जल्दी उठना अच्छा लगता हैं",
            "मुझे सच में सुबह जल्दी उठना अच्छा लगता हैं",
            "मुझे सुबह जल्दी उठना हैं",
            "मुझे सच में सुबह जल्दी उठना हैं",
            "मुझे फिर से सुबह जल्दी उठना हैं",
            "मुझे सच में फिर से सुबह जल्दी उठना हैं",
            "मुझे सुबह जल्दी उठना अच्छा नहीं लगता हैं",
            "मुझे सच में सुबह जल्दी उठना अच्छा नहीं लगता हैं",
            "मुझे सुबह जल्दी नहीं उठना हैं",
            "मुझे सच में सुबह जल्दी नहीं उठना हैं",
            "मुझे फिर से सुबह जल्दी नहीं उठना हैं",
            "मुझे सच में फिर से सुबह जल्दी नहीं उठना हैं",

    },{"मुझे अपना चेहरा धोना अच्छा लगता हैं",
            "मुझे सच में अपना चेहरा धोना अच्छा लगता हैं",
            "मुझे अपना चेहरा धोना हैं",
            "मुझे सच में अपना चेहरा धोना हैं",
            "मुझे फिर से अपना चेहरा धोना हैं",
            "मुझे सच में फिर से अपना चेहरा धोना हैं",
            "मुझे अपना चेहरा धोना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना चेहरा धोना अच्छा नहीं लगता हैं",
            "मुझे अपना चेहरा नहीं धोना हैं",
            "मुझे सच में अपना चेहरा नहीं धोना हैं",
            "मुझे फिर से अपना चेहरा नहीं धोना हैं",
            "मुझे सच में फिर से अपना चेहरा नहीं धोना हैं"

    },{"मुझे शौचा लय जाना अच्छा लगता हैं",
            "मुझे सच में शौचा लय जाना अच्छा लगता हैं",
            "मुझे शौचा लय जाना हैं",
            "मुझे सच में शौचा लय जाना हैं",
            "मुझे फिर से शौचा लय जाना हैं",
            "मुझे सच में फिर से शौचा लय जाना हैं",
            "मुझे शौचा लय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में शौचा लय जाना अच्छा नहीं लगता हैं",
            "मुझे शौचा लय नहीं जाना हैं",
            "मुझे सच में शौचा लय नहीं जाना हैं",
            "मुझे फिर से शौचा लय नहीं जाना हैं",
            "मुझे सच में फिर से शौचा लय नहीं जाना हैं"

    },{"मुझे अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने दांत साफ़ करने हैं",
            "मुझे सच में अपने दांत साफ़ करने हैं",
            "मुझे फिर से अपने दांत साफ़ करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ करने हैं",
            "मुझे अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत साफ़ नहीं करने हैं",
            "मुझे फिर से अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ नहीं करने हैं"

    },{"मुझे अपने कपड़ऐ उतारना अच्छा लगता हैं",
            "मुझे सच में अपने कपड़ऐ उतारना अच्छा लगता हैं",
            "मुझे अपने कपड़ऐ उतारने हैं",
            "मुझे सच में अपने कपड़ऐ उतारने हैं",
            "मुझे फिर से अपने कपड़ऐ उतारने हैं",
            "मुझे सच में फिर से अपने कपड़ऐ उतारने हैं",
            "मुझे अपने कपड़ऐ उतारना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने कपड़ऐ उतारना अच्छा नहीं लगता हैं",
            "मुझे अपने कपड़ऐ नहीं उतारने हैं",
            "मुझे सच में अपने कपड़ऐ नहीं उतारने हैं",
            "मुझे फिर से अपने कपड़ऐ नहीं उतारने हैं",
            "मुझे सच में फिर से अपने कपड़ऐ नहीं उतारने हैं"

    },{"मुझे नहाना अच्छा लगता हैं",
            "मुझे सच में नहाना अच्छा लगता हैं",
            "मुझे नहाना हैं",
            "मुझे सच में नहाना हैं",
            "मुझे फिर से नहाना हैं",
            "मुझे सच में फिर से नहाना हैं",
            "मुझे नहाना अच्छा नहीं लगता हैं",
            "मुझे सच में नहाना अच्छा नहीं लगता हैं",
            "मुझे नहाना नहीं हैं",
            "मुझे सच में नहाना नहीं हैं",
            "मुझे फिर से नहाना नहीं हैं",
            "मुझे सच में फिर से नहाना नहीं हैं"

    },{"मुझे कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे सच में कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे कपड़ऐ पैहनने हैं",
            "मुझे सच में कपड़ऐ पैहनने हैं",
            "मुझे फिर से कपड़ऐ पैहनने हैं",
            "मुझे सच में फिर से कपड़ऐ पैहनने हैं",
            "मुझे कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में कपड़ऐ नहीं पैहनने हैं",
            "मुझे फिर से कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में फिर से कपड़ऐ नहीं पैहनने हैं"

    },{"मुझे अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा लगता हैं",
            "मुझे अपने बालों को कंघी करनी हैं",
            "मुझे सच में अपने बालों को कंघी करनी हैं",
            "मुझे फिर से अपने बालों को कंघी करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी करनी हैं",
            "मुझे अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बालों को कंघी करना अच्छा नहीं लगता हैं",
            "मुझे अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में अपने बालों को कंघी नहीं करनी हैं",
            "मुझे फिर से अपने बालों को कंघी नहीं करनी हैं",
            "मुझे सच में फिर से अपने बालों को कंघी नहीं करनी हैं"

    },{"मुझे सुबह का नाश्ता करना अच्छा लगता हैं",
            "मुझे सच में सुबह का नाश्ता करना अच्छा लगता हैं",
            "मुझे सुबह का नाश्ता चाहिए",
            "मुझे सच में सुबह का नाश्ता चाहिए",
            "मुझे थोड़ा और नाश्ता चाहिए",
            "मुझे सच में थोड़ा और नाश्ता चाहिए",
            "मुझे सुबह का नाश्ता करना अच्छा नहीं लगता हैं",
            "मुझे सच में सुबह का नाश्ता करना अच्छा नहीं लगता हैं",
            "मुझे नाश्ता नहीं चाहिए",
            "मुझे सच में नाश्ता नहीं चाहिए",
            "मुझे और नाश्ता नहीं चाहिए",
            "मुझे सच में कुछ भी नाश्ता नहीं चाहिए"

    },{"मुझे अपना लंच बॉक्स पैक करना अच्छा लगता हैं",
            "मुझे सच में अपना लंच बॉक्स पैक करना अच्छा लगता हैं",
            "मुझे अपना लंच बॉक्स पैक करना हैं",
            "मुझे सच में अपना लंच बॉक्स पैक करना हैं",
            "मुझे फिर से अपना लंच बॉक्स पैक करना हैं",
            "मुझे सच में फिर से अपना लंच बॉक्स पैक करना हैं",
            "मुझे अपना लंच बॉक्स पैक करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना लंच बॉक्स पैक करना अच्छा नहीं लगता हैं",
            "मुझे अपना लंच बॉक्स पैक नहीं करना हैं",
            "मुझे सच में अपना लंच बॉक्स पैक नहीं करना हैं",
            "मुझे फिर से अपना लंच बॉक्स पैक नहीं करना हैं",
            "मुझे सच में फिर से अपना लंच बॉक्स पैक नहीं करना हैं"

    },{"मुझे अपनी स्कूल bag पैक करना अच्छा लगता हैं",
            "मुझे सच में अपनी स्कूल bag पैक करना अच्छा लगता हैं",
            "मुझे अपनी स्कूल bag पैक करनी हैं",
            "मुझे सच में अपनी स्कूल bag पैक करनी हैं",
            "मुझे फिर से अपनी स्कूल bag पैक करनी हैं",
            "मुझे सच में फिर से अपनी स्कूल bag पैक करनी हैं",
            "मुझे अपनी स्कूल bag पैक करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी स्कूल bag पैक करना अच्छा नहीं लगता हैं",
            "मुझे अपनी स्कूल bag पैक नहीं करनी हैं",
            "मुझे सच में अपनी स्कूल bag पैक नहीं करनी हैं",
            "मुझे फिर से अपनी स्कूल bag पैक नहीं करनी हैं",
            "मुझे सच में फिर से अपनी स्कूल bag पैक नहीं करनी हैं"

    },{"मुझे पाठशाला जाना अच्छा लगता हैं",
            "मुझे सच में पाठशाला जाना अच्छा लगता हैं",
            "मुझे पाठशाला जाना हैं",
            "मुझे सच में पाठशाला जाना हैं",
            "मुझे फिर से पाठशाला जाना हैं",
            "मुझे सच में फिर से पाठशाला जाना हैं",
            "मुझे पाठशाला जाना अच्छा नहीं लगता हैं",
            "मुझे सच में पाठशाला जाना अच्छा नहीं लगता हैं",
            "मुझे पाठशाला नहीं जाना हैं",
            "मुझे सच में पाठशाला नहीं जाना हैं",
            "मुझे फिर से पाठशाला नहीं जाना हैं",
            "मुझे सच में फिर से पाठशाला नहीं जाना हैं"

    }},{{"मुझे रात का भोजन करना अच्छा लगता हैं",
            "मुझे सच में रात का भोजन करना अच्छा लगता हैं",
            "मुझे रात का भोजन चाहिए",
            "मुझे सच में रात का भोजन चाहिए",
            "मुझे और भोजन चाहिए",
            "मुझे सच में और भोजन चाहिए",
            "मुझे रात का भोजन करना अच्छा नहीं लगता हैं",
            "मुझे सच में रात का भोजन करना अच्छा नहीं लगता हैं",
            "मुझे भोजन नहीं चाहिए",
            "मुझे सच में भोजन नहीं चाहिए",
            "मुझे और रात का भोजन नहीं चाहिए",
            "मुझे सच में और रात का भोजन नहीं चाहिए"

    },{"मुझे रात के कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे सच में रात के कपड़ऐ पैहनना अच्छा लगता हैं",
            "मुझे रात के कपड़ऐ पैहनने हैं",
            "मुझे सच में रात के कपड़ऐ पैहनने हैं",
            "मुझे फिर से रात के कपड़ऐ पैहनने हैं",
            "मुझे सच में फिर से रात के कपड़ऐ पहने हैं",
            "मुझे रात के कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे सच में रात के कपड़ऐ पैहनना अच्छा नहीं लगता हैं",
            "मुझे रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे फिर से रात के कपड़ऐ नहीं पैहनने हैं",
            "मुझे सच में फिर से रात के कपड़ऐ नहीं पैहनने हैं"

    },{"मुझे अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा लगता हैं",
            "मुझे अपने दांत साफ़ करने हैं",
            "मुझे सच में अपने दांत साफ़ करने हैं",
            "मुझे फिर से अपने दांत साफ़ करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ करने हैं",
            "मुझे अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दांत साफ़ करना अच्छा नहीं लगता हैं",
            "मुझे अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में अपने दांत साफ़ नहीं करने हैं",
            "मुझे फिर से अपने दांत साफ़ नहीं करने हैं",
            "मुझे सच में फिर से अपने दांत साफ़ नहीं करने हैं"

    },{"मुझे कहानियाँ पढ़ना अच्छा लगता हैं",
            "मुझे सच में कहानियाँ पढ़ना अच्छा लगता हैं",
            "मुझे एक कहानी पढ़नी हैं",
            "मुझे सच में एक कहानी पढ़नी हैं",
            "मुझे दुसरी कहानी पढ़नी हैं",
            "मुझे सच में दुसरी कहानी पढ़नी हैं",
            "मुझे कहानियाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में कहानियाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे कहानी नहीं पढ़नी हैं",
            "मुझे सच में कहानी नहीं पढ़नी हैं",
            "मुझे और एक कहानी नहीं पढ़नी हैं",
            "मुझे सच में और एक कहानी नहीं पढ़नी हैं"

    },{"मुझे सबको शुभ रात्रि बोलना अच्छा लगता हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना अच्छा लगता हैं",
            "मुझे सबको शुभ रात्रि बोलना हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना हैं",
            "मुझे फिर से सबको शुभ रात्रि बोलना हैं",
            "मुझे सच में फिर से सबको शुभ रात्रि बोलना हैं",
            "मुझे सबको शुभ रात्रि बोलना अच्छा नहीं लगता हैं",
            "मुझे सच में सबको शुभ रात्रि बोलना अच्छा नहीं लगता हैं",
            "मुझे सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे सच में सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे फिर से सबको शुभ रात्रि नहीं बोलना हैं",
            "मुझे सच में फिर से सबको शुभ रात्रि नहीं बोलना हैं"

    },{"मुझे प्रार्थना करना अच्छा लगता हैं",
            "मुझे सच में प्रार्थना करना अच्छा लगता हैं",
            "मुझे प्रार्थना करनी हैं",
            "मुझे सच में प्रार्थना करनी हैं",
            "मुझे फिर से प्रार्थना करनी हैं",
            "मुझे सच में फिर से प्रार्थना करनी हैं",
            "मुझे प्रार्थना करना अच्छा नहीं लगता हैं",
            "मुझे सच में प्रार्थना करना अच्छा नहीं लगता हैं",
            "मुझे प्रार्थना नहीं करनी हैं",
            "मुझे सच में प्रार्थना नहीं करनी हैं",
            "मुझे फिर से प्रार्थना नहीं करनी हैं",
            "मुझे सच में फिर से प्रार्थना नहीं करनी हैं",
    },{

    }}},{{{"मुझे ब्रेड अच्छा लगता हैं",
            "मुझे सच में ब्रेड अच्छा लगता हैं",
            "मुझे थोड़ा ब्रेड चाहिए",
            "मुझे सच में थोड़ा ब्रेड चाहिए",
            "मुझे और ब्रेड चाहिए",
            "मुझे सच में थोड़ा और ब्रेड चाहिए",
            "मुझे ब्रेड अच्छा नहीं लगता हैं",
            "मुझे सच में ब्रेड अच्छा नहीं लगता हैं",
            "मुझे ब्रेड नहीं चाहिए",
            "मुझे सच में ब्रेड नहीं चाहिए",
            "मुझे और ब्रेड नहीं चाहिए",
            "मुझे सच में और ब्रेड नहीं चाहिए"

    },{"मुझे कॉरनफ्लएक्स अच्छा लगता हैं",
            "मुझे सच में कॉरनफ्लएक्स अच्छा लगता हैं",
            "मुझे थोड़ा कॉरनफ्लएक्स चाहिए",
            "मुझे सच में थोड़ा कॉरनफ्लएक्स चाहिए",
            "मुझे और कॉरनफ्लएक्स चाहिए",
            "मुझे सच में थोड़ा और कॉरनफ्लएक्स चाहिए",
            "मुझे कॉरनफ्लएक्स अच्छा नहीं लगता हैं",
            "मुझे सच में कॉरनफ्लएक्स अच्छा नहीं लगता हैं",
            "मुझे कॉरनफ्लएक्स नहीं चाहिए",
            "मुझे सच में कॉरनफ्लएक्स नहीं चाहिए",
            "मुझे और कॉरनफ्लएक्स नहीं चाहिए",
            "मुझे सच में और कॉरनफ्लएक्स नहीं चाहिए"

    },{"मुझे आलू पूरी अच्छी लगती हैं",
            "मुझे सच में आलू पूरी अच्छी लगती हैं",
            "मुझे आलू पूरी चाहिए",
            "मुझे सच में आलू पूरी चाहिए",
            "मुझे और आलू पूरी चाहिए",
            "मुझे सच में थोड़ी और आलू पूरी चाहिए",
            "मुझे आलू पूरी अच्छी नहीं लगती हैं",
            "मुझे सच में आलू पूरी अच्छी नहीं लगती हैं",
            "मुझे आलू पूरी नहीं चाहिए",
            "मुझे सच में आलू पूरी नहीं चाहिए",
            "मुझे और आलू पूरी नहीं चाहिए",
            "मुझे सच में और आलू पूरी नहीं चाहिए"

    },{"मुझे अंडे अच्छे लगते हैं",
            "मुझे सच में अंडे अच्छे लगते हैं",
            "मुझे थोड़े अंडे चाहिए",
            "मुझे सच में थोड़े अंडे चाहिए",
            "मुझे और अंडे चाहिए",
            "मुझे सच में थोड़े और अंडे चाहिए",
            "मुझे अंडे अच्छे नहीं लगते हैं",
            "मुझे सच में अंडे अच्छे नहीं लगते हैं",
            "मुझे अंडे नहीं चाहिए",
            "मुझे सच में अंडे नहीं चाहिए",
            "मुझे और अंडे नहीं चाहिए",
            "मुझे सच में और अंडे नहीं चाहिए"

    },{"मुझे पोहा अच्छा लगता हैं",
            "मुझे सच में पोहा अच्छा लगता हैं",
            "मुझे थोड़ा पोहा चाहिए",
            "मुझे सच में थोड़ा पोहा चाहिए",
            "मुझे और पोहा चाहिए",
            "मुझे सच में थोड़ा और पोहा चाहिए",
            "मुझे पोहा अच्छा नहीं लगता हैं",
            "मुझे सच में पोहा अच्छा नहीं लगता हैं",
            "मुझे पोहा नहीं चाहिए",
            "मुझे सच में पोहा नहीं चाहिए",
            "मुझे और पोहा नहीं चाहिए",
            "मुझे सच में और पोहा नहीं चाहिए"

    },{"मुझे उपमा अच्छा लगता हैं",
            "मुझे सच में उपमा अच्छा लगता हैं",
            "मुझे थोड़ा उपमा चाहिए",
            "मुझे सच में थोड़ा उपमा चाहिए",
            "मुझे और उपमा चाहिए",
            "मुझे सच में थोड़ा और उपमा चाहिए",
            "मुझे उपमा अच्छा नहीं लगता हैं",
            "मुझे सच में उपमा अच्छा नहीं लगता हैं",
            "मुझे उपमा नहीं चाहिए",
            "मुझे सच में उपमा नहीं चाहिए",
            "मुझे और उपमा नहीं चाहिए",
            "मुझे सच में और उपमा नहीं चाहिए"

    },{"मुझे खिचड़ी अच्छी लगती हैं",
            "मुझे सच में खिचड़ी अच्छी लगती हैं",
            "मुझे थोड़ी खिचड़ी चाहिए",
            "मुझे सच में थोड़ी खिचड़ी चाहिए",
            "मुझे और खिचड़ी चाहिए",
            "मुझे सच में थोड़ी और खिचड़ी चाहिए",
            "मुझे खिचड़ी अच्छी नहीं लगती हैं",
            "मुझे सच में खिचड़ी अच्छी नहीं लगती हैं",
            "मुझे खिचड़ी नहीं चाहिए",
            "मुझे सच में खिचड़ी नहीं चाहिए",
            "मुझे और खिचड़ी नहीं चाहिए",
            "मुझे सच में और खिचड़ी नहीं चाहिए"

    },{"मुझे इड़ली अच्छी लगती हैं",
            "मुझे सच में इड़ली अच्छी लगती हैं",
            "मुझे इड़ली चाहिए",
            "मुझे सच में इड़ली चाहिए",
            "मुझे और इड़ली चाहिए",
            "मुझे सच में थोड़ी और इड़ली चाहिए",
            "मुझे इड़ली अच्छी नहीं लगती हैं",
            "मुझे सच में इड़ली अच्छी नहीं लगती हैं",
            "मुझे इड़ली नहीं चाहिए",
            "मुझे सच में इड़ली नहीं चाहिए",
            "मुझे और इड़ली नहीं चाहिए",
            "मुझे सच में और इड़ली नहीं चाहिए"

    },{"मुझे डोसा अच्छा लगता हैं",
            "मुझे सच में डोसा अच्छा लगता हैं",
            "मुझे डोसा चाहिए",
            "मुझे सच में डोसा चाहिए",
            "मुझे और डोसे चाहिए",
            "मुझे सच में थोड़े और डोसे चाहिए",
            "मुझे डोसा अच्छा नहीं लगता हैं",
            "मुझे सच में डोसा अच्छा नहीं लगता हैं",
            "मुझे डोसा नहीं चाहिए",
            "मुझे सच में डोसा नहीं चाहिए",
            "मुझे और डोसे नहीं चाहिए",
            "मुझे सच में और डोसे नहीं चाहिए"

    },{"मुझे पराठे अच्छे लगते हैं",
            "मुझे सच में पराठे अच्छे लगते हैं",
            "मुझे एक पराठा चाहिए",
            "मुझे सच में एक पराठा चाहिए",
            "मुझे और पराठे चाहिए",
            "मुझे सच में थोड़े और पराठे चाहिए",
            "मुझे पराठे अच्छे नहीं लगते हैं",
            "मुझे सच में पराठे अच्छे नहीं लगते हैं",
            "मुझे पराठा नहीं चाहिए",
            "मुझे सच में पराठा नहीं चाहिए",
            "मुझे और पराठे नहीं चाहिए",
            "मुझे सच में और पराठे नहीं चाहिए"

    },{"मुझे aumm lett अच्छा लगता हैं",
            "मुझे सच में aumm lett अच्छा लगता हैं",
            "मुझे एक aumm lett चाहिए",
            "मुझे सच में एक aumm lett चाहिए",
            "मुझे और aumm lett चाहिए",
            "मुझे सच में थोड़े और aumm lett चाहिए",
            "मुझे aumm lett अच्छा नहीं लगता हैं",
            "मुझे सच में aumm lett अच्छा नहीं लगता हैं",
            "मुझे aumm lett नहीं चाहिए",
            "मुझे सच में aumm lett नहीं चाहिए",
            "मुझे और aumm lett नहीं चाहिए",
            "मुझे सच में और aumm lett नहीं चाहिए"

    },{"मुझे मेदु वड़ा अच्छा लगता हैं",
            "मुझे सच में मेदु वड़ा अच्छा लगता हैं",
            "मुझे एक मेदु वड़ा चाहिए",
            "मुझे सच में एक मेदु वड़ा चाहिए",
            "मुझे और मेदु वड़ा चाहिए",
            "मुझे सच में और मेदु वड़ा चाहिए",
            "मुझे मेदु वड़ा अच्छा नहीं लगता हैं",
            "मुझे सच में मेदु वड़ा अच्छा नहीं लगता हैं",
            "मुझे मेदु वड़ा नहीं चाहिए",
            "मुझे सच में मेदु वड़ा नहीं चाहिए",
            "मुझे और मेदु वड़ा नहीं चाहिए",
            "मुझे सच में और मेदु वड़ा नहीं चाहिए"

    },{"मुझे दलिया अच्छा लगता हैं",
            "मुझे सच में दलिया अच्छा लगता हैं",
            "मुझे थोड़ा दलिया चाहिए",
            "मुझे सच में थोड़ा दलिया चाहिए",
            "मुझे और दलिया चाहिए",
            "मुझे सच में थोड़ा और दलिया चाहिए",
            "मुझे दलिया अच्छा नहीं लगता हैं",
            "मुझे सच में दलिया अच्छा नहीं लगता हैं",
            "मुझे दलिया नहीं चाहिए",
            "मुझे सच में दलिया नहीं चाहिए",
            "मुझे और दलिया नहीं चाहिए",
            "मुझे सच में और दलिया नहीं चाहिए"

    },{"मुझे सैंडविच अच्छा लगता हैं",
            "मुझे सच में सैंडविच अच्छा लगता हैं",
            "मुझे एक सैंडविच चाहिए",
            "मुझे सच में एक सैंडविच चाहिए",
            "मुझे और सैंडविच चाहिए",
            "मुझे सच में थोड़े और सैंडविच चाहिए",
            "मुझे सैंडविच अच्छा नहीं लगता हैं",
            "मुझे सच में सैंडविच अच्छा नहीं लगता हैं",
            "मुझे सैंडविच नहीं चाहिए",
            "मुझे सच में सैंडविच नहीं चाहिए",
            "मुझे और सैंडविच नहीं चाहिए",
            "मुझे सच में और सैंडविच नहीं चाहिए"

    },{"मुझे चटनी अच्छी लगती हैं",
            "मुझे सच में चटनी अच्छी लगती हैं",
            "मुझे थोड़ी चटनी चाहिए",
            "मुझे सच में थोड़ी चटनी चाहिए",
            "मुझे और चटनी चाहिए",
            "मुझे सच में थोड़ी और चटनी चाहिए",
            "मुझे चटनी अच्छी नहीं लगती हैं",
            "मुझे सच में चटनी अच्छी नहीं लगती हैं",
            "मुझे चटनी नहीं चाहिए",
            "मुझे सच में चटनी नहीं चाहिए",
            "मुझे और चटनी नहीं चाहिए",
            "मुझे सच में और चटनी नहीं चाहिए"

    },{"मुझे सांबर अच्छा लगता हैं",
            "मुझे सच में सांबर अच्छा लगता हैं",
            "मुझे थोड़ा सांबर चाहिए",
            "मुझे सच में थोड़ा सांबर चाहिए",
            "मुझे और सांबर चाहिए",
            "मुझे सच में थोड़ा और सांबर चाहिए",
            "मुझे सांबर अच्छा नहीं लगता हैं",
            "मुझे सच में सांबर अच्छा नहीं लगता हैं",
            "मुझे सांबर नहीं चाहिए",
            "मुझे सच में सांबर नहीं चाहिए",
            "मुझे और सांबर नहीं चाहिए",
            "मुझे सच में और सांबर नहीं चाहिए"

    },{"मुझे उत्तप्पा अच्छा लगता हैं",
            "मुझे सच में उत्तप्पा अच्छा लगता हैं",
            "मुझे उत्तप्पा चाहिए",
            "मुझे सच में उत्तप्पा चाहिए",
            "मुझे और उत्तप्पा चाहिए",
            "मुझे सच में थोड़े और उत्तप्पा चाहिए",
            "मुझे उत्तप्पा अच्छा नहीं लगता हैं",
            "मुझे सच में उत्तप्पा अच्छा नहीं लगता हैं",
            "मुझे उत्तप्पा नहीं चाहिए",
            "मुझे सच में उत्तप्पा नहीं चाहिए",
            "मुझे और उत्तप्पा नहीं चाहिए",
            "मुझे सच में और उत्तप्पा नहीं चाहिए"

    }},{{"मुझे रोटी अच्छी लगती हैं",
            "मुझे सच में रोटी अच्छी लगती हैं",
            "मुझे रोटी चाहिए",
            "मुझे सच में रोटी चाहिए",
            "मुझे और रोटियाँ चाहिए",
            "मुझे सच में थोड़ी और रोटियाँ चाहिए",
            "मुझे रोटी अच्छी नहीं लगती हैं",
            "मुझे सच में रोटी अच्छी नहीं लगती हैं",
            "मुझे रोटी नहीं चाहिए",
            "मुझे सच में रोटी नहीं चाहिए",
            "मुझे और रोटियाँ नहीं चाहिए",
            "मुझे सच में और रोटियाँ नहीं चाहिए"

    },{"मुझे सब्ज़ी अच्छी लगती हैं",
            "मुझे सच में सब्ज़ी अच्छी लगती हैं",
            "मुझे थोड़ी सब्ज़ी चाहिए",
            "मुझे सच में थोड़ी सब्ज़ी चाहिए",
            "मुझे और सब्ज़ी चाहिए",
            "मुझे सच में थोड़ी और सब्ज़ी चाहिए",
            "मुझे सब्ज़ी अच्छी नहीं लगती हैं",
            "मुझे सच में सब्ज़ी अच्छी नहीं लगती हैं",
            "मुझे सब्ज़ी नहीं चाहिए",
            "मुझे सच में सब्ज़ी नहीं चाहिए",
            "मुझे और सब्ज़ी नहीं चाहिए",
            "मुझे सच में और सब्ज़ी नहीं चाहिए"

    },{"मुझे चावल खाना अच्छा लगता हैं",
            "मुझे सच में चावल खाना अच्छा लगता हैं",
            "मुझे थोड़ा चावल चाहिए",
            "मुझे सच में थोड़ा चावल चाहिए",
            "मुझे और चावल चाहिए",
            "मुझे सच में थोड़ा और चावल चाहिए",
            "मुझे चावल खाना अच्छा नहीं लगता हैं",
            "मुझे सच में चावल खाना अच्छा नहीं लगता हैं",
            "मुझे चावल नहीं चाहिए",
            "मुझे सच में चावल नहीं चाहिए",
            "मुझे और चावल नहीं चाहिए",
            "मुझे सच में और चावल नहीं चाहिए"

    },{"मुझे दाल अच्छी लगती हैं",
            "मुझे सच में दाल अच्छी लगती हैं",
            "मुझे थोड़ी दाल चाहिए",
            "मुझे सच में थोड़ी दाल चाहिए",
            "मुझे और दाल चाहिए",
            "मुझे सच में थोड़ी और दाल चाहिए",
            "मुझे दाल अच्छी नहीं लगती हैं",
            "मुझे सच में दाल अच्छी नहीं लगती हैं",
            "मुझे दाल नहीं चाहिए",
            "मुझे सच में दाल नहीं चाहिए",
            "मुझे और दाल नहीं चाहिए",
            "मुझे सच में और दाल नहीं चाहिए"

    },{"मुझे दालखिचड़ी अच्छी लगती हैं",
            "मुझे सच में दालखिचड़ी अच्छी लगती हैं",
            "मुझे थोड़ी दालखिचड़ी चाहिए",
            "मुझे सच में थोड़ी दालखिचड़ी चाहिए",
            "मुझे और दालखिचड़ी चाहिए",
            "मुझे सच में थोड़ी और दालखिचड़ी चाहिए",
            "मुझे दालखिचड़ी अच्छी नहीं लगती हैं",
            "मुझे सच में दालखिचड़ी अच्छी नहीं लगती हैं",
            "मुझे और दालखिचड़ी नहीं चाहिए",
            "मुझे सच में और दालखिचड़ी नहीं चाहिए",
            "मुझे और दालखिचड़ी नहीं चाहिए",
            "मुझे सच में और दालखिचड़ी नहीं चाहिए",

    },{"मुझे रायता अच्छा लगता हैं",
            "मुझे सच में रायता अच्छा लगता हैं",
            "मुझे थोड़ा रायता चाहिए",
            "मुझे सच में थोड़ा रायता चाहिए",
            "मुझे और रायता चाहिए",
            "मुझे सच में थोड़ा और रायता चाहिए",
            "मुझे रायता अच्छा नहीं लगता हैं",
            "मुझे सच में रायता अच्छा नहीं लगता हैं",
            "मुझे रायता नहीं चाहिए",
            "मुझे सच में रायता नहीं चाहिए",
            "मुझे और रायता नहीं चाहिए",
            "मुझे सच में और रायता नहीं चाहिए"

    },{"मुझे पराठे अच्छे लगते हैं",
            "मुझे सच में पराठे अच्छे लगते हैं",
            "मुझे एक पराठा चाहिए",
            "मुझे सच में एक पराठा चाहिए",
            "मुझे और पराठे चाहिए",
            "मुझे सच में थोड़े और पराठे चाहिए",
            "मुझे पराठे अच्छे नहीं लगते हैं",
            "मुझे सच में पराठे अच्छे नहीं लगते हैं",
            "मुझे पराठा नहीं चाहिए",
            "मुझे सच में पराठा नहीं चाहिए",
            "मुझे और पराठे नहीं चाहिए",
            "मुझे सच में और पराठे नहीं चाहिए"

    },{"मुझे दही अच्छा लगता हैं",
            "मुझे सच में दही अच्छा लगता हैं",
            "मुझे थोड़ा दही चाहिए",
            "मुझे सच में थोड़ा दही चाहिए",
            "मुझे और दही चाहिए",
            "मुझे सच में थोड़ा और दही चाहिए",
            "मुझे दही अच्छा नहीं लगता हैं",
            "मुझे सच में दही अच्छा नहीं लगता हैं",
            "मुझे दही नहीं चाहिए",
            "मुझे सच में दही नहीं चाहिए",
            "मुझे और दही नहीं चाहिए",
            "मुझे सच में और दही नहीं चाहिए"

    },{"मुझे मछली खाना अच्छा लगता हैं",
            "मुझे सच में मछली खाना अच्छा लगता हैं",
            "मुझे थोड़ी मछली चाहिए",
            "मुझे सच में थोड़ी मछली चाहिए",
            "मुझे और मछली चाहिए",
            "मुझे सच में थोड़ी और मछली चाहिए",
            "मुझे मछली खाना अच्छा नहीं लगता हैं",
            "मुझे सच में मछली खाना अच्छा नहीं लगता हैं",
            "मुझे मछली नहीं चाहिए",
            "मुझे सच में मछली नहीं चाहिए",
            "मुझे और मछली नहीं चाहिए",
            "मुझे सच में और मछली नहीं चाहिए"

    },{"मुझे चिकन खाना अच्छा लगता हैं",
            "मुझे सच में चिकन खाना अच्छा लगता हैं",
            "मुझे थोड़ा चिकन चाहिए",
            "मुझे सच में थोड़ा चिकन चाहिए",
            "मुझे और चिकन चाहिए",
            "मुझे सच में थोड़ा और चिकन चाहिए",
            "मुझे चिकन खाना अच्छा नहीं लगता हैं",
            "मुझे सच में चिकन खाना अच्छा नहीं लगता हैं",
            "मुझे चिकन नहीं चाहिए",
            "मुझे सच में चिकन नहीं चाहिए",
            "मुझे और चिकन नहीं चाहिए",
            "मुझे सच में और चिकन नहीं चाहिए"

    },{"मुझे पोर्क खाना अच्छा लगता हैं",
            "मुझे सच में पोर्क खाना अच्छा लगता हैं",
            "मुझे थोड़ा पोर्क चाहिए",
            "मुझे सच में थोड़ा पोर्क चाहिए",
            "मुझे और पोर्क चाहिए",
            "मुझे सच में थोड़ा और पोर्क चाहिए",
            "मुझे पोर्क खाना अच्छा नहीं लगता हैं",
            "मुझे सच में पोर्क खाना अच्छा नहीं लगता हैं",
            "मुझे पोर्क नहीं चाहिए",
            "मुझे सच में पोर्क नहीं चाहिए",
            "मुझे और पोर्क नहीं चाहिए",
            "मुझे सच में और पोर्क नहीं चाहिए"

    },{"मुझे मटन खाना अच्छा लगता हैं",
            "मुझे सच में मटन खाना अच्छा लगता हैं",
            "मुझे थोड़ा मटन चाहिए",
            "मुझे सच में थोड़ा मटन चाहिए",
            "मुझे और मटन चाहिए",
            "मुझे सच में थोड़ा और मटन चाहिए",
            "मुझे मटन खाना अच्छा नहीं लगता हैं",
            "मुझे सच में मटन खाना अच्छा नहीं लगता हैं",
            "मुझे मटन नहीं चाहिए",
            "मुझे सच में मटन नहीं चाहिए",
            "मुझे और मटन नहीं चाहिए",
            "मुझे सच में और मटन नहीं चाहिए"

    },{"मुझे केकड़े का मांस खाना अच्छा लगता हैं",
            "मुझे सच में केकड़े का मांस खाना अच्छा लगता हैं",
            "मुझे थोड़ा केकड़े का मांस चाहिए",
            "मुझे सच में थोड़ा केकड़े का मांस चाहिए",
            "मुझे और केकड़े का मांस चाहिए",
            "मुझे सच में थोड़ा और केकड़े का मांस चाहिए",
            "मुझे केकड़े का मांस खाना अच्छा नहीं लगता हैं",
            "मुझे सच में केकड़े का मांस खाना अच्छा नहीं लगता हैं",
            "मुझे केकड़े का मांस नहीं चाहिए",
            "मुझे सच में केकड़े का मांस नहीं चाहिए",
            "मुझे और केकड़े का मांस नहीं चाहिए",
            "मुझे सच में और केकड़े का मांस नहीं चाहिए"

    },{"मुझे turkey खाना अच्छा लगता हैं",
            "मुझे सच में turkey खाना अच्छा लगता हैं",
            "मुझे थोड़ा turkey चाहिए",
            "मुझे सच में थोड़ा turkey चाहिए",
            "मुझे और turkey चाहिए",
            "मुझे सच में थोड़ा और turkey चाहिए",
            "मुझे turkey खाना अच्छा नहीं लगता हैं",
            "मुझे सच में turkey खाना अच्छा नहीं लगता हैं",
            "मुझे turkey नहीं चाहिए",
            "मुझे सच में turkey नहीं चाहिए",
            "मुझे और turkey नहीं चाहिए",
            "मुझे सच में और turkey नहीं चाहिए"

    },{"मुझे pizza अच्छा लगता हैं",
            "मुझे सच में pizza अच्छा लगता हैं",
            "मुझे थोड़ा pizza चाहिए",
            "मुझे सच में थोड़ा pizza चाहिए",
            "मुझे और pizza चाहिए",
            "मुझे सच में थोड़ा और pizza चाहिए",
            "मुझे pizza अच्छा नहीं लगता हैं",
            "मुझे सच में pizza अच्छा नहीं लगता हैं",
            "मुझे pizza नहीं चाहिए",
            "मुझे सच में pizza नहीं चाहिए",
            "मुझे और pizza नहीं चाहिए",
            "मुझे सच में और pizza नहीं चाहिए"

    },{"मुझे सलाड अच्छा लगता हैं",
            "मुझे सच में सलाड अच्छा लगता हैं",
            "मुझे थोड़ा सलाड चाहिए",
            "मुझे सच में थोड़ा सलाड चाहिए",
            "मुझे और सलाड चाहिए",
            "मुझे सच में थोड़ा और सलाड चाहिए",
            "मुझे सलाड अच्छा नहीं लगता हैं",
            "मुझे सच में सलाड अच्छा नहीं लगता हैं",
            "मुझे सलाड नहीं चाहिए",
            "मुझे सच में सलाड नहीं चाहिए",
            "मुझे और सलाड नहीं चाहिए",
            "मुझे सच में और सलाड नहीं चाहिए"

    },{"मुझे सूप अच्छा लगता हैं",
            "मुझे सच में सूप अच्छा लगता हैं",
            "मुझे थोड़ा सूप चाहिए",
            "मुझे सच में थोड़ा सूप चाहिए",
            "मुझे और सूप चाहिए",
            "मुझे सच में थोड़ा और सूप चाहिए",
            "मुझे सूप अच्छा नहीं लगता हैं",
            "मुझे सच में सूप अच्छा नहीं लगता हैं",
            "मुझे सूप नहीं चाहिए",
            "मुझे सच में सूप नहीं चाहिए",
            "मुझे और सूप नहीं चाहिए",
            "मुझे सच में और सूप नहीं चाहिए"

    },{"मुझे पास्ता अच्छा लगता हैं",
            "मुझे सच में पास्ता अच्छा लगता हैं",
            "मुझे थोड़ा पास्ता चाहिए",
            "मुझे सच में थोड़ा पास्ता चाहिए",
            "मुझे और पास्ता चाहिए",
            "मुझे सच में थोड़ा और पास्ता चाहिए",
            "मुझे पास्ता अच्छा नहीं लगता हैं",
            "मुझे सच में पास्ता अच्छा नहीं लगता हैं",
            "मुझे पास्ता नहीं चाहिए",
            "मुझे सच में पास्ता नहीं चाहिए",
            "मुझे और पास्ता नहीं चाहिए",
            "मुझे सच में और पास्ता नहीं चाहिए"

    },{"मुझे noodles अच्छे लगते हैं",
            "मुझे सच में noodles अच्छे लगते हैं",
            "मुझे थोड़े noodles चाहिए",
            "मुझे सच में थोड़े noodles चाहिए",
            "मुझे और noodles चाहिए",
            "मुझे सच में थोड़े और noodles चाहिए",
            "मुझे noodles अच्छे नहीं लगते हैं",
            "मुझे सच में noodles अच्छे नहीं लगते हैं",
            "मुझे noodles नहीं चाहिए",
            "मुझे सच में noodles नहीं चाहिए",
            "मुझे और noodles नहीं चाहिए",
            "मुझे सच में और noodles नहीं चाहिए"

    },{"मुझे इटालीयन खाना अच्छा लगता हैं",
            "मुझे सच में इटालीयन खाना अच्छा लगता हैं",
            "मुझे इटालीयन खाना चाहिए",
            "मुझे सच में इटालीयन खाना चाहिए",
            "मुझे और इटालीयन खाना चाहिए",
            "मुझे सच में थोड़ा और इटालीयन खाना चाहिए",
            "मुझे इटालीयन खाना अच्छा नहीं लगता हैं",
            "मुझे सच में इटालीयन खाना अच्छा नहीं लगता हैं",
            "मुझे इटालीयन खाना नहीं चाहिए",
            "मुझे सच में इटालीयन खाना नहीं चाहिए",
            "मुझे और इटालीयन खाना नहीं चाहिए",
            "मुझे सच में और इटालीयन खाना नहीं चाहिए"

    },{"मुझे पाव भाजी अच्छी लगती हैं",
            "मुझे सच में पाव भाजी अच्छी लगती हैं",
            "मुझे थोड़ी पाव भाजी चाहिए",
            "मुझे सच में थोड़ी पाव भाजी चाहिए",
            "मुझे और पाव भाजी चाहिए",
            "मुझे सच में थोड़ी और पाव भाजी चाहिए",
            "मुझे पाव भाजी अच्छी नहीं लगती हैं",
            "मुझे सच में पाव भाजी अच्छी नहीं लगती हैं",
            "मुझे पाव भाजी नहीं चाहिए",
            "मुझे सच में पाव भाजी नहीं चाहिए",
            "मुझे और पाव भाजी नहीं चाहिए",
            "मुझे सच में और पाव भाजी नहीं चाहिए"

    },{"मुझे भाकरी अच्छी लगती हैं",
            "मुझे सच में भाकरी अच्छी लगती हैं",
            "मुझे भाकरी चाहिए",
            "मुझे सच में भाकरी चाहिए",
            "मुझे और एक भाकरी चाहिए",
            "मुझे सच में और एक भाकरी चाहिए",
            "मुझे भाकरी अच्छी नहीं लगती हैं",
            "मुझे सच में भाकरी अच्छी नहीं लगती हैं",
            "मुझे भाकरी नहीं चाहिए",
            "मुझे सच में भाकरी नहीं चाहिए",
            "मुझे और भाकरी नहीं चाहिए",
            "मुझे सच में और भाकरी नहीं चाहिए"

    }},{{"मुझे केक अच्छा लगता हैं",
            "मुझे सच में केक अच्छा लगता हैं",
            "मुझे केक चाहिए",
            "मुझे सच में केक चाहिए",
            "मुझे और केक चाहिए",
            "मुझे सच में थोड़ा और केक चाहिए",
            "मुझे केक अच्छा नहीं लगता हैं",
            "मुझे सच में केक अच्छा नहीं लगता हैं",
            "मुझे केक नहीं चाहिए",
            "मुझे सच में केक नहीं चाहिए",
            "मुझे और केक नहीं चाहिए",
            "मुझे सच में और केक नहीं चाहिए"

    },{"मुझे आइसक्रीम अच्छा लगता हैं",
            "मुझे सच में आइसक्रीम अच्छा लगता हैं",
            "मुझे आइसक्रीम चाहिए",
            "मुझे सच में आइसक्रीम चाहिए",
            "मुझे और आइसक्रीम चाहिए",
            "मुझे सच में थोड़ा और आइसक्रीम चाहिए",
            "मुझे आइसक्रीम अच्छा नहीं लगता हैं",
            "मुझे सच में आइसक्रीम अच्छा नहीं लगता हैं",
            "मुझे आइसक्रीम नहीं चाहिए",
            "मुझे सच में आइसक्रीम नहीं चाहिए",
            "मुझे और आइसक्रीम नहीं चाहिए",
            "मुझे सच में और आइसक्रीम नहीं चाहिए"

    },{"मुझे गा जर का हलवा अच्छा लगता हैं",
            "मुझे सच में गा जर का हलवा अच्छा लगता हैं",
            "मुझे थोड़ा गा जर का हलवा चाहिए",
            "मुझे सच में थोड़ा गा जर का हलवा चाहिए",
            "मुझे और गा जर का हलवा चाहिए",
            "मुझे सच में थोड़ा और गा जर का हलवा चाहिए",
            "मुझे गा जर का हलवा अच्छा नहीं लगता हैं",
            "मुझे सच में गा जर का हलवा अच्छा नहीं लगता हैं",
            "मुझे गा जर का हलवा नहीं चाहिए",
            "मुझे सच में गा जर का हलवा नहीं चाहिए",
            "मुझे और गा जर का हलवा नहीं चाहिए",
            "मुझे सच में और गा जर का हलवा नहीं चाहिए"

    },{"मुझे गुलाब जामुन अच्छे लगते हैं",
            "मुझे सच में गुलाब जामुन अच्छे लगते हैं",
            "मुझे गुलाब जामुन चाहिए",
            "मुझे सच में गुलाब जामुन चाहिए",
            "मुझे और गुलाब जामुन चाहिए",
            "मुझे सच में थोड़े और गुलाब जामुन चाहिए",
            "मुझे गुलाब जामुन अच्छे नहीं लगते हैं",
            "मुझे सच में गुलाब जामुन अच्छे नहीं लगते हैं",
            "मुझे गुलाब जामुन नहीं चाहिए",
            "मुझे सच में गुलाब जामुन नहीं चाहिए",
            "मुझे और गुलाब जामुन नहीं चाहिए",
            "मुझे सच में और गुलाब जामुन नहीं चाहिए"

    },{"मुझे लड्डू अच्छे लगते हैं",
            "मुझे सच में लड्डू अच्छे लगते हैं",
            "मुझे एक लड्डू चाहिए",
            "मुझे सच में एक लड्डू चाहिए",
            "मुझे और लड्डू चाहिए",
            "मुझे सच में थोड़े और लड्डू चाहिए",
            "मुझे लड्डू अच्छे नहीं लगते हैं",
            "मुझे सच में लड्डू अच्छे नहीं लगते हैं",
            "मुझे लड्डू नहीं चाहिए",
            "मुझे सच में लड्डू नहीं चाहिए",
            "मुझे और लड्डू नहीं चाहिए",
            "मुझे सच में और लड्डू नहीं चाहिए"

    },{"मुझे बर्फी अच्छी लगती हैं",
            "मुझे सच में बर्फी अच्छी लगती है",
            "मुझे एक बर्फी चाहिए",
            "मुझे सच में एक बर्फी चाहिए",
            "मुझे और बर्फी चाहिए",
            "मुझे सच में थोड़ी और बर्फी चाहिए",
            "मुझे बर्फी अच्छी नहीं लगती हैं",
            "मुझे सच में बर्फी अच्छी नहीं लगती हैं",
            "मुझे बर्फी नहीं चाहिए",
            "मुझे सच में बर्फी नहीं चाहिए",
            "मुझे और बर्फी नहीं चाहिए",
            "मुझे सच में और बर्फी नहीं चाहिए"

    },{"मुझे जलेबी अच्छी लगती हैं",
            "मुझे सच में जलेबी अच्छी लगती हैं",
            "मुझे जलेबी चाहिए",
            "मुझे सच में जलेबी चाहिए",
            "मुझे और जलेबी चाहिए",
            "मुझे सच में थोड़ी और जलेबी चाहिए",
            "मुझे जलेबी अच्छी नहीं लगती हैं",
            "मुझे सच में जलेबी अच्छी नहीं लगती हैं",
            "मुझे जलेबी नहीं चाहिए",
            "मुझे सच में जलेबी नहीं चाहिए",
            "मुझे और जलेबी नहीं चाहिए",
            "मुझे सच में और जलेबी नहीं चाहिए"

    },{"मुझे फलों का सलाड अच्छा लगता हैं",
            "मुझे सच में फलों का सलाड अच्छा लगता हैं",
            "मुझे थोड़ा फलों का सलाड चाहिए",
            "मुझे सच में थोड़ा फलों का सलाड चाहिए",
            "मुझे और फलों का सलाड चाहिए",
            "मुझे सच में थोड़ा और फलों का सलाड चाहिए",
            "मुझे फलों का सलाड अच्छा नहीं लगता हैं",
            "मुझे सच में फलों का सलाड अच्छा नहीं लगता हैं",
            "मुझे फलों का सलाड नहीं चाहिए",
            "मुझे सच में फलों का सलाड नहीं चाहिए",
            "मुझे और फलों का सलाड नहीं चाहिए",
            "मुझे सच में और फलों का सलाड नहीं चाहिए"

    },{"मुझे रसगुल्ले अच्छे लगते हैं",
            "मुझे सच में रसगुल्ले अच्छे लगते हैं",
            "मुझे रसगुल्ला चाहिए",
            "मुझे सच में रसगुल्ला चाहिए",
            "मुझे और रसगुल्ले चाहिए",
            "मुझे सच में थोड़े और रसगुल्ले चाहिए",
            "मुझे रसगुल्ले अच्छे नहीं लगते हैं",
            "मुझे सच में रसगुल्ले अच्छे नहीं लगते हैं",
            "मुझे रसगुल्ला नहीं चाहिए",
            "मुझे सच में रसगुल्ला नहीं चाहिए",
            "मुझे और रसगुल्ले नहीं चाहिए",
            "मुझे सच में और रसगुल्ले नहीं चाहिए"

    },{"मुझे शीरा अच्छा लगता हैं",
            "मुझे सच में शीरा अच्छा लगता हैं",
            "मुझे थोड़ा शीरा चाहिए",
            "मुझे सच में थोड़ा शीरा चाहिए",
            "मुझे और शीरा चाहिए",
            "मुझे सच में थोड़ा और शीरा चाहिए",
            "मुझे शीरा अच्छा नहीं लगता हैं",
            "मुझे सच में शीरा अच्छा नहीं लगता हैं",
            "मुझे शीरा नहीं चाहिए",
            "मुझे सच में शीरा नहीं चाहिए",
            "मुझे और शीरा नहीं चाहिए",
            "मुझे सच में और शीरा नहीं चाहिए"

    }},{{"मुझे बिस्कुट अच्छा लगता हैं",
            "मुझे सच में बिस्कुट अच्छा लगता हैं",
            "मुझे एक बिस्कुट चाहिए",
            "मुझे सच में एक बिस्कुट चाहिए",
            "मुझे और बिस्कुट चाहिए",
            "मुझे सच में थोड़े और बिस्कुट चाहिए",
            "मुझे बिस्कुट अच्छा नहीं लगता हैं",
            "मुझे सच में बिस्कुट अच्छा नहीं लगता हैं",
            "मुझे बिस्कुट नहीं चाहिए",
            "मुझे सच में बिस्कुट नहीं चाहिए",
            "मुझे और बिस्कुट नहीं चाहिए",
            "मुझे सच में और बिस्कुट नहीं चाहिए"

    },{"मुझे चाट अच्छा लगता हैं",
            "मुझे सच में चाट अच्छा लगता हैं",
            "मुझे चाट चाहिए",
            "मुझे सच में चाट चाहिए",
            "मुझे और चाट चाहिए",
            "मुझे सच में थोड़ा और चाट चाहिए",
            "मुझे चाट अच्छा नहीं लगता हैं",
            "मुझे सच में चाट अच्छा नहीं लगता हैं",
            "मुझे चाट नहीं चाहिए",
            "मुझे सच में चाट नहीं चाहिए",
            "मुझे और चाट नहीं चाहिए",
            "मुझे सच में और चाट नहीं चाहिए"

    },{"मुझे चॉकलेट अच्छा लगता हैं",
            "मुझे सच में चॉकलेट अच्छा लगता हैं",
            "मुझे एक चॉकलेट चाहिए",
            "मुझे सच में एक चॉकलेट चाहिए",
            "मुझे और चॉकलेट चाहिए",
            "मुझे सच में थोड़े और चॉकलेट चाहिए",
            "मुझे चॉकलेट अच्छा नहीं लगता हैं",
            "मुझे सच में चॉकलेट अच्छा नहीं लगता हैं",
            "मुझे चॉकलेट नहीं चाहिए",
            "मुझे सच में चॉकलेट नहीं चाहिए",
            "मुझे और चॉकलेट नहीं चाहिए",
            "मुझे सच में और चॉकलेट नहीं चाहिए"

    },{"मुझे वएफर्स अच्छे लगते हैं",
            "मुझे सच में वएफर्स अच्छे लगते हैं",
            "मुझे थोड़े वएफर्स चाहिए",
            "मुझे सच में थोड़े वएफर्स चाहिए",
            "मुझे और वएफर्स चाहिए",
            "मुझे सच में थोड़े और वएफर्स चाहिए",
            "मुझे वएफर्स अच्छे नहीं लगते हैं",
            "मुझे सच में वएफर्स अच्छे नहीं लगते हैं",
            "मुझे वएफर्स नहीं चाहिए",
            "मुझे सच में वएफर्स नहीं चाहिए",
            "मुझे और वएफर्स नहीं चाहिए",
            "मुझे सच में और वएफर्स नहीं चाहिए"

    },{"मुझे सैंडविच अच्छा लगता हैं",
            "मुझे सच में सैंडविच अच्छा लगता हैं",
            "मुझे एक सैंडविच चाहिए",
            "मुझे सच में एक सैंडविच चाहिए",
            "मुझे और सैंडविच चाहिए",
            "मुझे सच में थोड़े और सैंडविच चाहिए",
            "मुझे सैंडविच अच्छा नहीं लगता हैं",
            "मुझे सच में सैंडविच अच्छा नहीं लगता हैं",
            "मुझे सैंडविच नहीं चाहिए",
            "मुझे सच में सैंडविच नहीं चाहिए",
            "मुझे और सैंडविच नहीं चाहिए",
            "मुझे सच में और सैंडविच नहीं चाहिए"

    },{"मुझे noodles अच्छे लगते हैं",
            "मुझे सच में noodles अच्छे लगते हैं",
            "मुझे थोड़े noodles चाहिए",
            "मुझे सच में थोड़े noodles चाहिए",
            "मुझे और noodles चाहिए",
            "मुझे सच में थोड़े और noodles चाहिए",
            "मुझे noodles अच्छे नहीं लगते हैं",
            "मुझे सच में noodles अच्छे नहीं लगते हैं",
            "मुझे noodles नहीं चाहिए",
            "मुझे सच में noodles नहीं चाहिए",
            "मुझे और noodles नहीं चाहिए",
            "मुझे सच में और noodles नहीं चाहिए"

    },{"मुझे चीज़ अच्छा लगता हैं",
            "मुझे सच में चीज़ अच्छा लगता हैं",
            "मुझे थोड़ा चीज़ चाहिए",
            "मुझे सच में थोड़ा चीज़ चाहिए",
            "मुझे और चीज़ चाहिए",
            "मुझे सच में थोड़ा और चीज़ चाहिए",
            "मुझे चीज़ अच्छा नहीं लगता हैं",
            "मुझे सच में चीज़ अच्छा नहीं लगता हैं",
            "मुझे चीज़ नहीं चाहिए",
            "मुझे सच में चीज़ नहीं चाहिए",
            "मुझे और चीज़ नहीं चाहिए",
            "मुझे सच में और चीज़ नहीं चाहिए"

    }, {"मुझे नट्स अच्छे लगते हैं",
            "मुझे सच में नट्स अच्छे लगते हैं",
            "मुझे थोड़े नट्स चाहिए",
            "मुझे सच में थोड़े नट्स चाहिए",
            "मुझे और नट्स चाहिए",
            "मुझे सच में थोड़े और नट्स चाहिए",
            "मुझे नट्स अच्छे नहीं लगते हैं",
            "मुझे सच में नट्स अच्छे नहीं लगते हैं",
            "मुझे नट्स नहीं चाहिए",
            "मुझे सच में नट्स नहीं चाहिए",
            "मुझे और नट्स नहीं चाहिए",
            "मुझे सच में और नट्स नहीं चाहिए"

    }},{{"मुझे सेब अच्छे लगते हैं",
            "मुझे सच में सेब अच्छे लगते हैं",
            "मुझे एक सेब चाहिए",
            "मुझे सच में एक सेब चाहिए",
            "मुझे और सेब चाहिए",
            "मुझे सच में थोड़े और सेब चाहिए",
            "मुझे सेब अच्छे नहीं लगते हैं",
            "मुझे सच में सेब अच्छे नहीं लगते हैं",
            "मुझे सेब नहीं चाहिए",
            "मुझे सच में सेब नहीं चाहिए",
            "मुझे और सेब नहीं चाहिए",
            "मुझे सच में और सेब नहीं चाहिए"

    },{"मुझे केले अच्छे लगते हैं",
            "मुझे सच में केले अच्छे लगते हैं",
            "मुझे एक केला चाहिए",
            "मुझे सच में एक केला चाहिए",
            "मुझे और केले चाहिए",
            "मुझे सच में थोड़े और केले चाहिए",
            "मुझे केले अच्छे नहीं लगते हैं",
            "मुझे सच में केले अच्छे नहीं लगते हैं",
            "मुझे केले नहीं चाहिए",
            "मुझे सच में केले नहीं चाहिए",
            "मुझे और केले नहीं चाहिए",
            "मुझे सच में और केले नहीं चाहिए"

    },{"मुझे अंगूर अच्छे लगते हैं",
            "मुझे सच में अंगूर अच्छे लगते हैं",
            "मुझे थोड़े अंगूर चाहिए",
            "मुझे सच में थोड़े अंगूर चाहिए",
            "मुझे और अंगूर चाहिए",
            "मुझे सच में थोड़े और अंगूर चाहिए",
            "मुझे अंगूर अच्छे नहीं लगते हैं",
            "मुझे सच में अंगूर अच्छे नहीं लगते हैं",
            "मुझे अंगूर नहीं चाहिए",
            "मुझे सच में अंगूर नहीं चाहिए",
            "मुझे और अंगूर नहीं चाहिए",
            "मुझे सच में और अंगूर नहीं चाहिए"

    },{"मुझे अमरूद अच्छे लगते हैं",
            "मुझे सच में अमरूद अच्छे लगते हैं",
            "मुझे एक अमरूद चाहिए",
            "मुझे सच में एक अमरूद चाहिए",
            "मुझे और अमरूद चाहिए",
            "मुझे सच में थोड़े और अमरूद चाहिए",
            "मुझे अमरूद अच्छे नहीं लगते हैं",
            "मुझे सच में अमरूद अच्छे नहीं लगते हैं",
            "मुझे अमरूद नहीं चाहिए",
            "मुझे सच में अमरूद नहीं चाहिए",
            "मुझे और अमरूद नहीं चाहिए",
            "मुझे सच में और अमरूद नहीं चाहिए"

    },{"मुझे आम अच्छे लगते हैं",
            "मुझे सच में आम अच्छे लगते हैं",
            "मुझे एक आम चाहिए",
            "मुझे सच में एक आम चाहिए",
            "मुझे और आम चाहिए",
            "मुझे सच में थोड़े और आम चाहिए",
            "मुझे आम अच्छे नहीं लगते हैं",
            "मुझे सच में आम अच्छे नहीं लगते हैं",
            "मुझे आम नहीं चाहिए",
            "मुझे सच में आम नहीं चाहिए",
            "मुझे और आम नहीं चाहिए",
            "मुझे सच में और आम नहीं चाहिए"

    },{"मुझे संतरे अच्छे लगते हैं",
            "मुझे सच में संतरे अच्छे लगते हैं",
            "मुझे एक संतरा चाहिए",
            "मुझे सच में एक संतरा चाहिए",
            "मुझे और संतरे चाहिए",
            "मुझे सच में थोड़े और संतरे चाहिए",
            "मुझे संतरे अच्छे नहीं लगते हैं",
            "मुझे सच में संतरे अच्छे नहीं लगते हैं",
            "मुझे संतरा नहीं चाहिए",
            "मुझे सच में संतरा नहीं चाहिए",
            "मुझे और संतरे नहीं चाहिए",
            "मुझे सच में और संतरे नहीं चाहिए"

    },{"मुझे अनानास अच्छे लगते हैं",
            "मुझे सच में अनानास अच्छे लगते हैं",
            "मुझे एक अनानास चाहिए",
            "मुझे सच में एक अनानास चाहिए",
            "मुझे और अनानास चाहिए",
            "मुझे सच में थोड़े और अनानास चाहिए",
            "मुझे अनानास अच्छे नहीं लगते हैं",
            "मुझे सच में अनानास अच्छे नहीं लगते हैं",
            "मुझे अनानास नहीं चाहिए",
            "मुझे सच में अनानास नहीं चाहिए",
            "मुझे और अनानास नहीं चाहिए",
            "मुझे सच में और अनानास नहीं चाहिए"

    },{"मुझे स्ट्रॉबेरी अच्छे लगते हैं",
            "मुझे सच में स्ट्रॉबेरी अच्छे लगते हैं",
            "मुझे थोड़े स्ट्रॉबेरी चाहिए",
            "मुझे सच में थोड़े स्ट्रॉबेरी चाहिए",
            "मुझे और स्ट्रॉबेरी चाहिए",
            "मुझे सच में थोड़े और स्ट्रॉबेरी चाहिए",
            "मुझे स्ट्रॉबेरी अच्छे नहीं लगते हैं",
            "मुझे सच में स्ट्रॉबेरी अच्छे नहीं लगते हैं",
            "मुझे स्ट्रॉबेरी नहीं चाहिए",
            "मुझे सच में स्ट्रॉबेरी नहीं चाहिए",
            "मुझे और स्ट्रॉबेरी नहीं चाहिए",
            "मुझे सच में और स्ट्रॉबेरी नहीं चाहिए"

    },{"मुझे बेर अच्छे लगते हैं",
            "मुझे सच में बेर अच्छे लगते हैं",
            "मुझे थोड़े बेर चाहिए",
            "मुझे सच में थोड़े बेर चाहिए",
            "मुझे और बेर चाहिए",
            "मुझे सच में थोड़े और बेर चाहिए",
            "मुझे बेर अच्छे नहीं लगते हैं",
            "मुझे सच में बेर अच्छे नहीं लगते हैं",
            "मुझे बेर नहीं चाहिए",
            "मुझे सच में बेर नहीं चाहिए",
            "मुझे और बेर नहीं चाहिए",
            "मुझे सच में और बेर नहीं चाहिए"

    },{"मुझे अनार अच्छे लगते हैं",
            "मुझे सच में अनार अच्छे लगते हैं",
            "मुझे एक अनार चाहिए",
            "मुझे सच में एक अनार चाहिए",
            "मुझे और अनार चाहिए",
            "मुझे सच में थोड़े और अनार चाहिए",
            "मुझे अनार अच्छे नहीं लगते हैं",
            "मुझे सच में अनार अच्छे नहीं लगते हैं",
            "मुझे अनार नहीं चाहिए",
            "मुझे सच में अनार नहीं चाहिए",
            "मुझे और अनार नहीं चाहिए",
            "मुझे सच में और अनार नहीं चाहिए"

    },{"मुझे तरबूज अच्छे लगते हैं",
            "मुझे सच में तरबूज अच्छे लगते हैं",
            "मुझे एक तरबूज चाहिए",
            "मुझे सच में एक तरबूज चाहिए",
            "मुझे और तरबूज चाहिए",
            "मुझे सच में थोड़े और तरबूज चाहिए",
            "मुझे तरबूज अच्छे नहीं लगते हैं",
            "मुझे सच में तरबूज अच्छे नहीं लगते हैं",
            "मुझे तरबूज नहीं चाहिए",
            "मुझे सच में तरबूज नहीं चाहिए",
            "मुझे और तरबूज नहीं चाहिए",
            "मुझे सच में और तरबूज नहीं चाहिए"

    },{"मुझे पेर अच्छे लगते हैं",
            "मुझे सच में पेर अच्छे लगते हैं",
            "मुझे एक पेर चाहिए",
            "मुझे सच में एक पेर चाहिए",
            "मुझे और पेर चाहिए",
            "मुझे सच में थोड़े और पेर चाहिए",
            "मुझे पेर अच्छे नहीं लगते हैं",
            "मुझे सच में पेर अच्छे नहीं लगते हैं",
            "मुझे पेर नहीं चाहिए",
            "मुझे सच में पेर नहीं चाहिए",
            "मुझे और पेर नहीं चाहिए",
            "मुझे सच में और पेर नहीं चाहिए"

    },{"मुझे पपीता अच्छा लगता हैं",
            "मुझे सच में पपीता अच्छा लगता हैं",
            "मुझे एक पपीता चाहिए",
            "मुझे सच में एक पपीता चाहिए",
            "मुझे और पपीता चाहिए",
            "मुझे सच में थोड़ा और पपीता चाहिए",
            "मुझे पपीता अच्छा नहीं लगता हैं",
            "मुझे सच में पपीता अच्छा नहीं लगता हैं",
            "मुझे पपीता नहीं चाहिए",
            "मुझे सच में पपीता नहीं चाहिए",
            "मुझे और पपीता नहीं चाहिए",
            "मुझे सच में और पपीता नहीं चाहिए"

    },{"मुझे खरबूजा अच्छा लगता हैं",
            "मुझे सच में खरबूजा अच्छा लगता हैं",
            "मुझे एक खरबूजा चाहिए",
            "मुझे सच में एक  खरबूजा चाहिए",
            "मुझे और खरबूजा चाहिए",
            "मुझे सच में थोड़ा और खरबूजा चाहिए",
            "मुझे खरबूजा अच्छा नहीं लगता हैं",
            "मुझे सच में खरबूजा अच्छा नहीं लगता हैं",
            "मुझे खरबूजा नहीं चाहिए",
            "मुझे सच में खरबूजा नहीं चाहिए",
            "मुझे और खरबूजा नहीं चाहिए",
            "मुझे सच में और खरबूजा नहीं चाहिए"

    },{"मुझे चिकू अच्छे लगते हैं",
            "मुझे सच में चिकू अच्छे लगते हैं",
            "मुझे एक चिकू चाहिए",
            "मुझे सच में एक चिकू चाहिए",
            "मुझे और चिकू चाहिए",
            "मुझे सच में थोड़े और चिकू चाहिए",
            "मुझे चिकू अच्छे नहीं लगते हैं",
            "मुझे सच में चिकू अच्छे नहीं लगते हैं",
            "मुझे चिकू नहीं चाहिए",
            "मुझे सच में चिकू नहीं चाहिए",
            "मुझे और चिकू नहीं चाहिए",
            "मुझे सच में और चिकू नहीं चाहिए"

    },{"मुझे पनस अच्छा लगता हैं",
            "मुझे सच में पनस अच्छा लगता हैं",
            "मुझे पनस चाहिए",
            "मुझे सच में पनस चाहिए",
            "मुझे और पनस चाहिए",
            "मुझे सच में थोड़े और पनस चाहिए",
            "मुझे पनस अच्छा नहीं लगता हैं",
            "मुझे सच में पनस अच्छा नहीं लगता हैं",
            "मुझे पनस नहीं चाहिए",
            "मुझे सच में पनस नहीं चाहिए",
            "मुझे और पनस नहीं चाहिए",
            "मुझे सच में और पनस नहीं चाहिए"
    },{"मुझे चेरी अच्छे लगते हैं",
            "मुझे सच में चेरी अच्छे लगते हैं",
            "मुझे एक चेरी चाहिए",
            "मुझे सच में एक चेरी चाहिए",
            "मुझे और चेरी चाहिए",
            "मुझे सच में थोड़े और चेरी चाहिए",
            "मुझे चेरी अच्छे नहीं लगते हैं",
            "मुझे सच में चेरी अच्छे नहीं लगते हैं",
            "मुझे चेरी नहीं चाहिए",
            "मुझे सच में चेरी नहीं चाहिए",
            "मुझे और चेरी नहीं चाहिए",
            "मुझे सच में और चेरी नहीं चाहिए"
    }},{{"मुझे पानी अच्छा लगता हैं",
            "मुझे सच में पानी अच्छा लगता हैं",
            "मुझे थोड़ा पानी चाहिए",
            "मुझे सच में थोड़ा पानी चाहिए",
            "मुझे और पानी चाहिए",
            "मुझे सच में थोड़ा और पानी चाहिए",
            "मुझे पानी अच्छा नहीं लगता हैं",
            "मुझे सच में पानी अच्छा नहीं लगता हैं",
            "मुझे पानी नहीं चाहिए",
            "मुझे सच में पानी नहीं चाहिए",
            "मुझे और पानी नहीं चाहिए",
            "मुझे सच में और पानी नहीं चाहिए"

    },{"मुझे दूध अच्छा लगता हैं",
            "मुझे सच में दूध अच्छा लगता हैं",
            "मुझे थोड़ा दूध चाहिए",
            "मुझे सच में थोड़ा दूध चाहिए",
            "मुझे और दूध चाहिए",
            "मुझे सच में थोड़ा और दूध चाहिए",
            "मुझे दूध अच्छा नहीं लगता हैं",
            "मुझे सच में दूध अच्छा नहीं लगता हैं",
            "मुझे दूध नहीं चाहिए",
            "मुझे सच में दूध नहीं चाहिए",
            "मुझे और दूध नहीं चाहिए",
            "मुझे सच में और दूध नहीं चाहिए"

    },{"मुझे bournvita अच्छा लगता हैं",
            "मुझे सच में bournvita अच्छा लगता हैं",
            "मुझे थोड़ा bournvita चाहिए",
            "मुझे सच में थोड़ा bournvita चाहिए",
            "मुझे और bournvita चाहिए",
            "मुझे सच में थोड़ा और bournvita चाहिए",
            "मुझे bournvita अच्छा नहीं लगता हैं",
            "मुझे सच में bournvita अच्छा नहीं लगता हैं",
            "मुझे bournvita नहीं चाहिए",
            "मुझे सच में bournvita नहीं चाहिए",
            "मुझे और bournvita नहीं चाहिए",
            "मुझे सच में और bournvita नहीं चाहिए"

    },{"मुझे आम का ज्यूस अच्छा लगता हैं",
            "मुझे सच में आम का ज्यूस अच्छा लगता हैं",
            "मुझे थोड़ा आम का ज्यूस चाहिए",
            "मुझे सच में थोड़ा आम का ज्यूस चाहिए",
            "मुझे थोड़ा और आम का ज्यूस चाहिए",
            "मुझे सच में थोड़ा और आम का ज्यूस चाहिए",
            "मुझे आम का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सच में आम का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे आम का ज्यूस नहीं चाहिए",
            "मुझे सच में आम का ज्यूस नहीं चाहिए",
            "मुझे और आम का ज्यूस नहीं चाहिए",
            "मुझे सच में और आम का ज्यूस नहीं चाहिए"

    },{"मुझे सेब का ज्यूस अच्छा लगता हैं",
            "मुझे सच में सेब का ज्यूस अच्छा लगता हैं",
            "मुझे थोड़ा सेब का ज्यूस चाहिए",
            "मुझे सच में थोड़ा सेब का ज्यूस चाहिए",
            "मुझे थोड़ा और सेब का ज्यूस चाहिए",
            "मुझे सच में थोड़ा और सेब का ज्यूस चाहिए",
            "मुझे सेब का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सच में सेब का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सेब का ज्यूस नहीं चाहिए",
            "मुझे सच में सेब का ज्यूस नहीं चाहिए",
            "मुझे और सेब का ज्यूस नहीं चाहिए",
            "मुझे सच में और सेब का ज्यूस नहीं चाहिए"

    },{"मुझे संतरे का ज्यूस अच्छा लगता हैं",
            "मुझे सच में संतरे का ज्यूस अच्छा लगता हैं",
            "मुझे थोड़ा संतरे का ज्यूस चाहिए",
            "मुझे सच में थोड़ा संतरे का ज्यूस चाहिए",
            "मुझे थोड़ा और संतरे का ज्यूस चाहिए",
            "मुझे सच में थोड़ा और संतरे का ज्यूस चाहिए",
            "मुझे संतरे का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सच में संतरे का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे संतरे का ज्यूस नहीं चाहिए",
            "मुझे सच में संतरे का ज्यूस नहीं चाहिए",
            "मुझे और संतरे का ज्यूस नहीं चाहिए",
            "मुझे सच में और संतरे का ज्यूस नहीं चाहिए"

    },{"मुझे नींबू का ज्यूस अच्छा लगता हैं",
            "मुझे सच में नींबू का ज्यूस अच्छा लगता हैं",
            "मुझे थोड़ा नींबू का ज्यूस चाहिए",
            "मुझे सच में थोड़ा नींबू का ज्यूस चाहिए",
            "मुझे थोड़ा और नींबू का ज्यूस चाहिए",
            "मुझे सच में थोड़ा और नींबू का ज्यूस चाहिए",
            "मुझे नींबू का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सच में नींबू का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे नींबू का ज्यूस नहीं चाहिए",
            "मुझे सच में नींबू का ज्यूस नहीं चाहिए",
            "मुझे और नींबू का ज्यूस नहीं चाहिए",
            "मुझे सच में और नींबू का ज्यूस नहीं चाहिए"

    },{"मुझे अनानास का ज्यूस अच्छा लगता हैं",
            "मुझे सच में अनानास का ज्यूस अच्छा लगता हैं",
            "मुझे थोड़ा अनानास का ज्यूस चाहिए",
            "मुझे सच में थोड़ा अनानास का ज्यूस चाहिए",
            "मुझे थोड़ा और अनानास का ज्यूस चाहिए",
            "मुझे सच में थोड़ा और अनानास का ज्यूस चाहिए",
            "मुझे अनानास का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे सच में अनानास का ज्यूस अच्छा नहीं लगता हैं",
            "मुझे अनानास का ज्यूस नहीं चाहिए",
            "मुझे सच में अनानास का ज्यूस नहीं चाहिए",
            "मुझे और अनानास का ज्यूस नहीं चाहिए",
            "मुझे सच में और अनानास का ज्यूस नहीं चाहिए"

    },{"मुझे पेप्सी अच्छा लगता हैं",
            "मुझे सच में पेप्सी अच्छा लगता हैं",
            "मुझे थोड़ा पेप्सी चाहिए",
            "मुझे सच में थोड़ा पेप्सी चाहिए",
            "मुझे और पेप्सी चाहिए",
            "मुझे सच में थोड़ा और पेप्सी चाहिए",
            "मुझे पेप्सी अच्छा नहीं लगता हैं",
            "मुझे सच में पेप्सी अच्छा नहीं लगता हैं",
            "मुझे पेप्सी नहीं चाहिए",
            "मुझे सच में पेप्सी नहीं चाहिए",
            "मुझे और पेप्सी नहीं चाहिए",
            "मुझे सच में और पेप्सी नहीं चाहिए"

    },{"मुझे कोका कोला अच्छा लगता हैं",
            "मुझे सच में कोका कोला अच्छा लगता हैं",
            "मुझे थोड़ा कोका कोला चाहिए",
            "मुझे सच में थोड़ा कोका कोला चाहिए",
            "मुझे और कोका कोला चाहिए",
            "मुझे सच में थोड़ा और कोका कोला चाहिए",
            "मुझे कोका कोला अच्छा नहीं लगता हैं",
            "मुझे सच में कोका कोला अच्छा नहीं लगता हैं",
            "मुझे कोका कोला नहीं चाहिए",
            "मुझे सच में कोका कोला नहीं चाहिए",
            "मुझे और कोका कोला नहीं चाहिए",
            "मुझे सच में और कोका कोला नहीं चाहिए"

    },{"मुझे मिरिंडा अच्छा लगता हैं",
            "मुझे सच में मिरिंडा अच्छा लगता हैं",
            "मुझे थोड़ा मिरिंडा चाहिए",
            "मुझे सच में थोड़ा मिरिंडा चाहिए",
            "मुझे और मिरिंडा चाहिए",
            "मुझे सच में थोड़ा और मिरिंडा चाहिए",
            "मुझे मिरिंडा अच्छा नहीं लगता हैं",
            "मुझे सच में मिरिंडा अच्छा नहीं लगता हैं",
            "मुझे मिरिंडा नहीं चाहिए",
            "मुझे सच में मिरिंडा नहीं चाहिए",
            "मुझे और मिरिंडा नहीं चाहिए",
            "मुझे सच में और मिरिंडा नहीं चाहिए"

    },{"मुझे फैंटा अच्छा लगता हैं",
            "मुझे सच में फैंटा अच्छा लगता हैं",
            "मुझे थोड़ा फैंटा चाहिए",
            "मुझे सच में थोड़ा फैंटा चाहिए",
            "मुझे और फैंटा चाहिए",
            "मुझे सच में थोड़ा और फैंटा चाहिए",
            "मुझे फैंटा अच्छा नहीं लगता हैं",
            "मुझे सच में फैंटा अच्छा नहीं लगता हैं",
            "मुझे फैंटा नहीं चाहिए",
            "मुझे सच में फैंटा नहीं चाहिए",
            "मुझे और फैंटा नहीं चाहिए",
            "मुझे सच में और फैंटा नहीं चाहिए"

    },{"मुझे माज़ा अच्छा लगता हैं",
            "मुझे सच में माज़ा अच्छा लगता हैं",
            "मुझे थोड़ा माज़ा चाहिए",
            "मुझे सच में थोड़ा माज़ा चाहिए",
            "मुझे और माज़ा चाहिए",
            "मुझे सच में थोड़ा और माज़ा चाहिए",
            "मुझे माज़ा अच्छा नहीं लगता हैं",
            "मुझे सच में माज़ा अच्छा नहीं लगता हैं",
            "मुझे माज़ा नहीं चाहिए",
            "मुझे सच में माज़ा नहीं चाहिए",
            "मुझे और माज़ा नहीं चाहिए",
            "मुझे सच में और माज़ा नहीं चाहिए"

    },{"मुझे sprite अच्छा लगता हैं",
            "मुझे सच में sprite अच्छा लगता हैं",
            "मुझे थोड़ा sprite चाहिए",
            "मुझे सच में थोड़ा sprite चाहिए",
            "मुझे और sprite चाहिए",
            "मुझे सच में थोड़ा और sprite चाहिए",
            "मुझे sprite अच्छा नहीं लगता हैं",
            "मुझे सच में sprite अच्छा नहीं लगता हैं",
            "मुझे sprite नहीं चाहिए",
            "मुझे सच में sprite नहीं चाहिए",
            "मुझे और sprite नहीं चाहिए",
            "मुझे सच में और sprite नहीं चाहिए"

    },{"मुझे माउंटेन ड्यू अच्छा लगता हैं",
            "मुझे सच में माउंटेन ड्यू अच्छा लगता हैं",
            "मुझे थोड़ा माउंटेन ड्यू चाहिए",
            "मुझे सच में थोड़ा माउंटेन ड्यू चाहिए",
            "मुझे और माउंटेन ड्यू चाहिए",
            "मुझे सच में थोड़ा और माउंटेन ड्यू चाहिए",
            "मुझे माउंटेन ड्यू अच्छा नहीं लगता हैं",
            "मुझे सच में माउंटेन ड्यू अच्छा नहीं लगता हैं",
            "मुझे माउंटेन ड्यू नहीं चाहिए",
            "मुझे सच में माउंटेन ड्यू नहीं चाहिए",
            "मुझे और माउंटेन ड्यू नहीं चाहिए",
            "मुझे सच में और माउंटेन ड्यू नहीं चाहिए"

    }, {"मुझे मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा मिल्कशेक चाहिए",
            "मुझे और मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और मिल्कशेक चाहिए",
            "मुझे मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे मिल्कशेक नहीं चाहिए",
            "मुझे सच में मिल्कशेक नहीं चाहिए",
            "मुझे और मिल्कशेक नहीं चाहिए",
            "मुझे सच में और मिल्कशेक नहीं चाहिए"

    },{"मुझे चॉकलेट मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में चॉकलेट मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा चॉकलेट मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा चॉकलेट मिल्कशेक चाहिए",
            "मुझे और चॉकलेट मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और चॉकलेट मिल्कशेक चाहिए",
            "मुझे चॉकलेट मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में चॉकलेट मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे चॉकलेट मिल्कशेक नहीं चाहिए",
            "मुझे सच में चॉकलेट मिल्कशेक नहीं चाहिए",
            "मुझे और चॉकलेट मिल्कशेक नहीं चाहिए",
            "मुझे सच में और चॉकलेट मिल्कशेक नहीं चाहिए"

    },{"मुझे स्ट्रॉबेरी मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में स्ट्रॉबेरी मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा स्ट्रॉबेरी मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा स्ट्रॉबेरी मिल्कशेक चाहिए",
            "मुझे और स्ट्रॉबेरी मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और स्ट्रॉबेरी मिल्कशेक चाहिए",
            "मुझे स्ट्रॉबेरी मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में स्ट्रॉबेरी मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे स्ट्रॉबेरी मिल्कशेक नहीं चाहिए",
            "मुझे सच में स्ट्रॉबेरी मिल्कशेक नहीं चाहिए",
            "मुझे और स्ट्रॉबेरी मिल्कशेक नहीं चाहिए",
            "मुझे सच में और स्ट्रॉबेरी मिल्कशेक नहीं चाहिए"

    },{"मुझे केला मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में केला मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा केला मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा केला मिल्कशेक चाहिए",
            "मुझे और केला मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और केला मिल्कशेक चाहिए",
            "मुझे केला मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में केला मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे केला मिल्कशेक नहीं चाहिए",
            "मुझे सच में केला मिल्कशेक नहीं चाहिए",
            "मुझे और केला मिल्कशेक नहीं चाहिए",
            "मुझे सच में और केला मिल्कशेक नहीं चाहिए"

    },{"मुझे आम मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में आम मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा आम मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा आम मिल्कशेक चाहिए",
            "मुझे और आम मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और आम मिल्कशेक चाहिए",
            "मुझे आम मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में आम मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे आम मिल्कशेक नहीं चाहिए",
            "मुझे सच में आम मिल्कशेक नहीं चाहिए",
            "मुझे और आम मिल्कशेक नहीं चाहिए",
            "मुझे सच में और आम मिल्कशेक नहीं चाहिए"

    },{"मुझे चिकू मिल्कशेक अच्छा लगता हैं",
            "मुझे सच में चिकू मिल्कशेक अच्छा लगता हैं",
            "मुझे थोड़ा चिकू मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा चिकू मिल्कशेक चाहिए",
            "मुझे और चिकू मिल्कशेक चाहिए",
            "मुझे सच में थोड़ा और चिकू मिल्कशेक चाहिए",
            "मुझे चिकू मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे सच में चिकू मिल्कशेक अच्छा नहीं लगता हैं",
            "मुझे चिकू मिल्कशेक नहीं चाहिए",
            "मुझे सच में चिकू मिल्कशेक नहीं चाहिए",
            "मुझे और चिकू मिल्कशेक नहीं चाहिए",
            "मुझे सच में और चिकू मिल्कशेक नहीं चाहिए"

    },{"मुझे चाय अच्छी लगती हैं",
            "मुझे सच में चाय अच्छी लगती हैं",
            "मुझे थोड़ी चाय चाहिए",
            "मुझे सच में थोड़ी चाय चाहिए",
            "मुझे और चाय चाहिए",
            "मुझे सच में थोड़ी और चाय चाहिए",
            "मुझे चाय अच्छी नहीं लगती हैं",
            "मुझे सच में चाय अच्छी नहीं लगती हैं",
            "मुझे चाय नहीं चाहिए",
            "मुझे सच में चाय नहीं चाहिए",
            "मुझे और चाय नहीं चाहिए",
            "मुझे सच में और चाय नहीं चाहिए"

    },{"मुझे कॉफी अच्छी लगती हैं",
            "मुझे सच में कॉफी अच्छी लगती हैं",
            "मुझे थोड़ी कॉफी चाहिए",
            "मुझे सच में थोड़ी कॉफी चाहिए",
            "मुझे और कॉफी चाहिए",
            "मुझे सच में थोड़ी और कॉफी चाहिए",
            "मुझे कॉफी अच्छी नहीं लगती हैं",
            "मुझे सच में कॉफी अच्छी नहीं लगती हैं",
            "मुझे कॉफी नहीं चाहिए",
            "मुझे सच में कॉफी नहीं चाहिए",
            "मुझे और कॉफी नहीं चाहिए",
            "मुझे सच में और कॉफी नहीं चाहिए"

    },{"मुझे कोल्ड कॉफी अच्छी लगती हैं",
            "मुझे सच में कोल्ड कॉफी अच्छी लगती हैं",
            "मुझे थोड़ी कोल्ड कॉफी चाहिए",
            "मुझे सच में थोड़ी कोल्ड कॉफी चाहिए",
            "मुझे और कोल्ड कॉफी चाहिए",
            "मुझे सच में थोड़ी और कोल्ड कॉफी चाहिए",
            "मुझे कोल्ड कॉफी अच्छी नहीं लगती हैं",
            "मुझे सच में कोल्ड कॉफी अच्छी नहीं लगती हैं",
            "मुझे कोल्ड कॉफी नहीं चाहिए",
            "मुझे सच में कोल्ड कॉफी नहीं चाहिए",
            "मुझे और कोल्ड कॉफी नहीं चाहिए",
            "मुझे सच में और कोल्ड कॉफी नहीं चाहिए"

    },{"मुझे ऐनरजी ड्रिंक्स अच्छे लगते हैं",
            "मुझे सच में ऐनरजी ड्रिंक्स अच्छे लगते हैं",
            "मुझे एक ऐनरजी ड्रिंक चाहिए",
            "मुझे सच में एक ऐनरजी ड्रिंक चाहिए",
            "मुझे और ऐनरजी ड्रिंक्स चाहिए",
            "मुझे सच में थोड़े और ऐनरजी ड्रिंक्स चाहिए",
            "मुझे ऐनरजी ड्रिंक्स अच्छे नहीं लगते हैं",
            "मुझे सच में ऐनरजी ड्रिंक्स अच्छे नहीं लगते हैं",
            "मुझे ऐनरजी ड्रिंक नहीं चाहिए",
            "मुझे सच में ऐनरजी ड्रिंक नहीं चाहिए",
            "मुझे और ऐनरजी ड्रिंक्स नहीं चाहिए",
            "मुझे सच में और ऐनरजी ड्रिंक्स नहीं चाहिए"

    }},{{"मुझे खाने के लिए कटोरे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खाने के लिए कटोरे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक कटोरा चाहिए",
            "मुझे सच में एक कटोरा चाहिए",
            "मुझे और एक कटोरा चाहिए",
            "मुझे सच में और एक कटोरा चाहिए",
            "मुझे खाने के लिए कटोरे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खाने के लिए कटोरे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कटोरा नहीं चाहिए",
            "मुझे सच में कटोरा नहीं चाहिए",
            "मुझे और एक कटोरा नहीं चाहिए",
            "मुझे सच में और एक कटोरा नहीं चाहिए"

    },{"मुझे खाने के लिए प्लेट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खाने के लिए प्लेट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक प्लेट चाहिए",
            "मुझे सच में एक प्लेट चाहिए",
            "मुझे और एक प्लेट चाहिए",
            "मुझे सच में और एक प्लेट चाहिए",
            "मुझे खाने के लिए प्लेट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खाने के लिए प्लेट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे प्लेट नहीं चाहिए",
            "मुझे सच में प्लेट नहीं चाहिए",
            "मुझे और एक प्लेट नहीं चाहिए",
            "मुझे सच में और एक प्लेट नहीं चाहिए"

    },{"मुझे खाने के लिए चम्मच का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खाने के लिए चम्मच का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक चम्मच चाहिए",
            "मुझे सच में एक चम्मच चाहिए",
            "मुझे और एक चम्मच चाहिए",
            "मुझे सच में और एक चम्मच चाहिए",
            "मुझे खाने के लिए चम्मच का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खाने के लिए चम्मच का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे चम्मच नहीं चाहिए",
            "मुझे सच में चम्मच नहीं चाहिए",
            "मुझे और एक चम्मच नहीं चाहिए",
            "मुझे सच में और एक चम्मच नहीं चाहिए"

    },{"मुझे खाने के लिए काँटे के चम्मच का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खाने के लिए काँटे के चम्मच का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक काँटे का चम्मच चाहिए",
            "मुझे सच में एक काँटे का चम्मच चाहिए",
            "मुझे और एक काँटे का चम्मच चाहिए",
            "मुझे सच में और एक काँटे का चम्मच चाहिए",
            "मुझे खाने के लिए काँटे के चम्मच का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खाने के लिए काँटे के चम्मच का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे काँटे का चम्मच नहीं चाहिए",
            "मुझे सच में काँटे का चम्मच नहीं चाहिए",
            "मुझे और एक काँटे का चम्मच नहीं चाहिए",
            "मुझे सच में और एक काँटे का चम्मच नहीं चाहिए"

    },{"मुझे खाने के लिए चाकू का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खाने के लिए चाकू का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक चाकू चाहिए",
            "मुझे सच में एक चाकू चाहिए",
            "मुझे और एक चाकू चाहिए",
            "मुझे सच में और एक चाकू चाहिए",
            "मुझे खाने के लिए चाकू का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खाने के लिए चाकू का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे चाकू नहीं चाहिए",
            "मुझे सच में चाकू नहीं चाहिए",
            "मुझे और एक चाकू नहीं चाहिए",
            "मुझे सच में और एक चाकू नहीं चाहिए"

    },{"मुझे मग का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में मग का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक मग चाहिए",
            "मुझे सच में एक मग चाहिए",
            "मुझे और एक मग चाहिए",
            "मुझे सच में और एक मग चाहिए",
            "मुझे मग का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में मग का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे मग नहीं चाहिए",
            "मुझे सच में मग नहीं चाहिए",
            "मुझे और एक मग नहीं चाहिए",
            "मुझे सच में और एक मग नहीं चाहिए"

    },{"मुझे कप का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कप का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक कप चाहिए",
            "मुझे सच में एक कप चाहिए",
            "मुझे और एक कप चाहिए",
            "मुझे सच में और एक कप चाहिए",
            "मुझे कप का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कप का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कप नहीं चाहिए",
            "मुझे सच में कप नहीं चाहिए",
            "मुझे और एक कप नहीं चाहिए",
            "मुझे सच में और एक कप नहीं चाहिए"

    },{"मुझे पानी पीने के लिए ग्लास का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में पानी पीने के लिए ग्लास का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक ग्लास चाहिए",
            "मुझे सच में एक ग्लास चाहिए",
            "मुझे और एक ग्लास चाहिए",
            "मुझे सच में और एक ग्लास चाहिए",
            "मुझे पानी पीने के लिए ग्लास का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में पानी पीने के लिए ग्लास का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे ग्लास नहीं चाहिए",
            "मुझे सच में ग्लास नहीं चाहिए",
            "मुझे और एक ग्लास नहीं चाहिए",
            "मुझे सच में और एक ग्लास नहीं चाहिए"

    }},{{"मुझे मक्खन अच्छा लगता हैं",
            "मुझे सच में मक्खन अच्छा लगता हैं",
            "मुझे थोड़ा मक्खन चाहिए",
            "मुझे सच में थोड़ा मक्खन चाहिए",
            "मुझे और मक्खन चाहिए",
            "मुझे सच में थोड़ा और मक्खन चाहिए",
            "मुझे मक्खन अच्छा नहीं लगता हैं",
            "मुझे सच में मक्खन अच्छा नहीं लगता हैं",
            "मुझे मक्खन नहीं चाहिए",
            "मुझे सच में मक्खन नहीं चाहिए",
            "मुझे और मक्खन नहीं चाहिए",
            "मुझे सच में और मक्खन नहीं चाहिए"

    },{"मुझे jam अच्छा लगता हैं",
            "मुझे सच में jam अच्छा लगता हैं",
            "मुझे थोड़ा jam चाहिए",
            "मुझे सच में थोड़ा jam चाहिए",
            "मुझे और jam चाहिए",
            "मुझे सच में थोड़ा और jam चाहिए",
            "मुझे jam अच्छा नहीं लगता हैं",
            "मुझे सच में jam अच्छा नहीं लगता हैं",
            "मुझे jam नहीं चाहिए",
            "मुझे सच में jam नहीं चाहिए",
            "मुझे और jam नहीं चाहिए",
            "मुझे सच में और jam नहीं चाहिए"

    },{"मुझे नमक अच्छा लगता हैं",
            "मुझे सच में नमक अच्छा लगता हैं",
            "मुझे थोड़ा नमक चाहिए",
            "मुझे सच में थोड़ा नमक चाहिए",
            "मुझे और नमक चाहिए",
            "मुझे सच में थोड़ा और नमक चाहिए",
            "मुझे नमक अच्छा नहीं लगता हैं",
            "मुझे सच में नमक अच्छा नहीं लगता हैं",
            "मुझे नमक नहीं चाहिए",
            "मुझे सच में नमक नहीं चाहिए",
            "मुझे और नमक नहीं चाहिए",
            "मुझे सच में और नमक नहीं चाहिए"

    },{"मुझे काली मिर्च अच्छी लगती हैं",
            "मुझे सच में काली मिर्च अच्छी लगती हैं",
            "मुझे थोड़ी काली मिर्च चाहिए",
            "मुझे सच में थोड़ी काली मिर्च चाहिए",
            "मुझे और काली मिर्च चाहिए",
            "मुझे सच में थोड़ी और काली मिर्च चाहिए",
            "मुझे काली मिर्च अच्छी नहीं लगती हैं",
            "मुझे सच में काली मिर्च अच्छी नहीं लगती हैं",
            "मुझे काली मिर्च नहीं चाहिए",
            "मुझे सच में काली मिर्च नहीं चाहिए",
            "मुझे और काली मिर्च नहीं चाहिए",
            "मुझे सच में और काली मिर्च नहीं चाहिए"

    },{"मुझे चीनी अच्छी लगती हैं",
            "मुझे सच में चीनी अच्छी लगती हैं",
            "मुझे थोड़ी चीनी चाहिए",
            "मुझे सच में थोड़ी चीनी चाहिए",
            "मुझे और चीनी चाहिए",
            "मुझे सच में थोड़ी और चीनी चाहिए",
            "मुझे चीनी अच्छी नहीं लगती हैं",
            "मुझे सच में चीनी अच्छी नहीं लगती हैं",
            "मुझे चीनी नहीं चाहिए",
            "मुझे सच में चीनी नहीं चाहिए",
            "मुझे और चीनी नहीं चाहिए",
            "मुझे सच में और चीनी नहीं चाहिए"

    },{"मुझे सॉस अच्छा लगता हैं",
            "मुझे सच में सॉस अच्छा लगता हैं",
            "मुझे थोड़ा सॉस चाहिए",
            "मुझे सच में थोड़ा सॉस चाहिए",
            "मुझे और सॉस चाहिए",
            "मुझे सच में थोड़ा और सॉस चाहिए",
            "मुझे सॉस अच्छा नहीं लगता हैं",
            "मुझे सच में सॉस अच्छा नहीं लगता हैं",
            "मुझे सॉस नहीं चाहिए",
            "मुझे सच में सॉस नहीं चाहिए",
            "मुझे और सॉस नहीं चाहिए",
            "मुझे सच में और सॉस नहीं चाहिए"

    },{"मुझे आचार अच्छा लगता हैं",
            "मुझे सच में आचार अच्छा लगता हैं",
            "मुझे थोड़ा आचार चाहिए",
            "मुझे सच में थोड़ा आचार चाहिए",
            "मुझे और आचार चाहिए",
            "मुझे सच में थोड़ा और आचार चाहिए",
            "मुझे आचार अच्छा नहीं लगता हैं",
            "मुझे सच में आचार अच्छा नहीं लगता हैं",
            "मुझे आचार नहीं चाहिए",
            "मुझे सच में आचार नहीं चाहिए",
            "मुझे और आचार नहीं चाहिए",
            "मुझे सच में और आचार नहीं चाहिए"

    },{"मुझे पापड़ अच्छा लगता हैं",
            "मुझे सच में पापड़ अच्छा लगता हैं",
            "मुझे थोड़ा पापड़ चाहिए",
            "मुझे सच में थोड़ा पापड़ चाहिए",
            "मुझे और पापड़ चाहिए",
            "मुझे सच में थोड़े और पापड़ चाहिए",
            "मुझे पापड़ अच्छा नहीं लगता हैं",
            "मुझे सच में पापड़ अच्छा नहीं लगता हैं",
            "मुझे पापड़ नहीं चाहिए",
            "मुझे सच में पापड़ नहीं चाहिए",
            "मुझे और पापड़ नहीं चाहिए",
            "मुझे सच में और पापड़ नहीं चाहिए"

    },{"मुझे मसाला अच्छा लगता हैं",
            "मुझे सच में मसाला अच्छा लगता हैं",
            "मुझे थोड़ा मसाला चाहिए",
            "मुझे सच में थोड़ा मसाला चाहिए",
            "मुझे और मसाला चाहिए",
            "मुझे सच में थोड़ा और मसाला चाहिए",
            "मुझे मसाला अच्छा नहीं लगता हैं",
            "मुझे सच में मसाला अच्छा नहीं लगता हैं",
            "मुझे मसाला नहीं चाहिए",
            "मुझे सच में मसाला नहीं चाहिए",
            "मुझे और मसाला नहीं चाहिए",
            "मुझे सच में और मसाला नहीं चाहिए"
    }}},{{{"मुझे पज़ल्स अच्छे लगते हैं",
            "मुझे सच में पज़ल्स अच्छे लगते हैं",
            "मुझे पज़ल्स खेलने हैं",
            "मुझे सच में पज़ल्स खेलने हैं",
            "मुझे और पज़ल्स खेलने हैं",
            "मुझे सच में थोड़े और पज़ल्स खेलने हैं",
            "मुझे पज़ल्स अच्छे नहीं लगते हैं",
            "मुझे सच में पज़ल्स अच्छे नहीं लगते हैं",
            "मुझे पज़ल्स नहीं खेलने हैं",
            "मुझे सच में पज़ल्स नहीं खेलने हैं",
            "मुझे और पज़ल्स नहीं खेलने हैं",
            "मुझे सच में और पज़ल्स नहीं खेलने हैं"

    },{"मुझे बोर्ड खेल  खेलना अच्छा लगता हैं",
            "मुझे सच में बोर्ड खेल  खेलना अच्छा लगता हैं",
            "मुझे बोर्ड खेल  खेलने हैं",
            "मुझे सच में बोर्ड खेल  खेलने हैं",
            "मुझे और बोर्ड खेल  खेलने हैं",
            "मुझे सच में थोड़े और बोर्ड खेल  खेलने हैं",
            "मुझे बोर्ड खेल  खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में बोर्ड खेल  खेलना अच्छा नहीं लगता हैं",
            "मुझे बोर्ड खेल  नहीं खेलने हैं",
            "मुझे सच में बोर्ड खेल  नहीं खेलने हैं",
            "मुझे और बोर्ड खेल  नहीं खेलने हैं",
            "मुझे सच में और बोर्ड खेल  नहीं खेलने हैं"

    },{"मुझे ब्लॉक्स के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में ब्लॉक्स के साथ खेलना अच्छा लगता हैं",
            "मुझे ब्लॉक्स के साथ खेलना हैं",
            "मुझे सच में ब्लॉक्स के साथ खेलना हैं",
            "मुझे और समय के लिए ब्लॉक्स के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए ब्लॉक्स के साथ खेलना हैं",
            "मुझे ब्लॉक्स के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में ब्लॉक्स के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे ब्लॉक्स के साथ नहीं खेलना हैं",
            "मुझे सच में ब्लॉक्स के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए ब्लॉक्स के साथ नहीं खेलना  हैं",
            "मुझे सच में और समय के लिए ब्लॉक्स के साथ नहीं खेलना हैं"

    },{"मुझे लेगो के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में लेगो के साथ खेलना अच्छा लगता हैं",
            "मुझे लेगो के साथ खेलना हैं",
            "मुझे सच में लेगो के साथ खेलना हैं",
            "मुझे और समय के लिए लेगो के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए लेगो के साथ खेलना हैं",
            "मुझे लेगो के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में लेगो के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे लेगो के साथ नहीं खेलना हैं",
            "मुझे सच में लेगो के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए लेगो के साथ नहीं खेलना  हैं",
            "मुझे सच में और समय के लिए लेगो के साथ नहीं खेलना हैं"

    },{"मुझे शतरंज खेलना अच्छा लगता हैं",
            "मुझे सच में शतरंज खेलना अच्छा लगता हैं",
            "मुझे शतरंज खेलना हैं",
            "मुझे सच में शतरंज खेलना हैं",
            "मुझे और समय के लिए शतरंज खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए शतरंज खेलना हैं",
            "मुझे शतरंज खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में शतरंज खेलना अच्छा नहीं लगता हैं",
            "मुझे शतरंज नहीं खेलना हैं",
            "मुझे सच में शतरंज नहीं खेलना हैं",
            "मुझे और समय के लिए शतरंज नहीं खेलना हैं",
            "मुझे सच में और समय के लिए शतरंज नहीं खेलना हैं"

    },{"मुझे सांप और सीढ़ी खेलना अच्छा लगता हैं",
            "मुझे सच में सांप और सीढ़ी खेलना अच्छा लगता हैं",
            "मुझे सांप और सीढ़ी खेलना हैं",
            "मुझे सच में सांप और सीढ़ी खेलना हैं",
            "मुझे और समय के लिए सांप और सीढ़ी खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए सांप और सीढ़ी खेलना हैं",
            "मुझे सांप और सीढ़ी खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में सांप और सीढ़ी खेलना अच्छा नहीं लगता हैं",
            "मुझे सांप और सीढ़ी नहीं खेलना हैं",
            "मुझे सच में सांप और सीढ़ी नहीं खेलना हैं",
            "मुझे और समय के लिए सांप और सीढ़ी नहीं खेलना हैं",
            "मुझे सच में और समय के लिए सांप और सीढ़ी नहीं खेलना हैं"

    },{"मुझे scrabble खेलना अच्छा लगता हैं",
            "मुझे सच में scrabble खेलना अच्छा लगता हैं",
            "मुझे scrabble खेलना हैं",
            "मुझे सच में scrabble खेलना हैं",
            "मुझे और समय के लिए scrabble खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए scrabble खेलना हैं",
            "मुझे scrabble खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में scrabble खेलना अच्छा नहीं लगता हैं",
            "मुझे scrabble नहीं खेलना हैं",
            "मुझे सच में scrabble नहीं खेलना हैं",
            "मुझे और समय के लिए scrabble नहीं खेलना हैं",
            "मुझे सच में और समय के लिए scrabble नहीं खेलना हैं"

    },{"मुझे विडियो गेम खेलना अच्छा लगता हैं",
            "मुझे सच में विडियो गेम खेलना अच्छा लगता हैं",
            "मुझे विडियो गेम खेलना हैं",
            "मुझे सच में विडियो गेम खेलना हैं",
            "मुझे और समय के लिए विडियो गेम खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए विडियो गेम खेलना हैं",
            "मुझे विडियो गेम खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में विडियो गेम खेलना अच्छा नहीं लगता हैं",
            "मुझे विडियो गेम नहीं खेलना हैं",
            "मुझे सच में विडियो गेम नहीं खेलना हैं",
            "मुझे और समय के लिए विडियो गेम नहीं खेलना हैं",
            "मुझे सच में और समय के लिए विडियो गेम नहीं खेलना हैं"

    },{"मुझे गुड़ियों के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में गुड़ियों के साथ खेलना अच्छा लगता हैं",
            "मुझे गुड़ियों के साथ खेलना हैं",
            "मुझे सच में गुड़ियों के साथ खेलना हैं",
            "मुझे और समय के लिए गुड़ियों के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए गुड़ियों के साथ खेलना हैं",
            "मुझे गुड़ियों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में गुड़ियों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे गुड़ियों के साथ नहीं खेलना हैं",
            "मुझे सच में गुड़ियों के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए गुड़ियों के साथ नहीं खेलना हैं",
            "मुझे सच में और समय के लिए गुड़ियों के साथ नहीं खेलना हैं"

    },{"मुझे action फिगर्स के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में action फिगर्स के साथ खेलना अच्छा लगता हैं",
            "मुझे action फिगर्स के साथ खेलना हैं",
            "मुझे सच में action फिगर्स के साथ खेलना हैं",
            "मुझे और समय के लिए action फिगर्स के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए action फिगर्स के साथ खेलना हैं",
            "मुझे action फिगर्स के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में action फिगर्स के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे action फिगर्स के साथ नहीं खेलना हैं",
            "मुझे सच में action फिगर्स के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए action फिगर्स के साथ नहीं खेलना हैं",
            "मुझे सच में और समय के लिए action फिगर्स के साथ नहीं खेलना हैं"

    },{"मुझे सॉफ्ट टॉयज़ के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में सॉफ्ट टॉयज़ के साथ खेलना अच्छा लगता हैं",
            "मुझे सॉफ्ट टॉयज़ के साथ खेलना हैं",
            "मुझे सच में सॉफ्ट टॉयज़ के साथ खेलना हैं",
            "मुझे और समय के लिए सॉफ्ट टॉयज़ के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए सॉफ्ट टॉयज़ के साथ खेलना हैं",
            "मुझे सॉफ्ट टॉयज़ के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में सॉफ्ट टॉयज़ के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सॉफ्ट टॉयज़ के साथ नहीं खेलना हैं",
            "मुझे सच में सॉफ्ट टॉयज़ के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए सॉफ्ट टॉयज़ के साथ नहीं खेलना हैं",
            "मुझे सच में और समय के लिए सॉफ्ट टॉयज़ के साथ नहीं खेलना हैं"

    },{"मुझे कारों के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में कारों के साथ खेलना अच्छा लगता हैं",
            "मुझे कारों के साथ खेलना हैं",
            "मुझे सच में कारों के साथ खेलना हैं",
            "मुझे और समय के लिए कारों के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए कारों के साथ खेलना हैं",
            "मुझे कारों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में कारों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे कारों के साथ नहीं खेलना हैं",
            "मुझे सच में कारों के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए कारों के साथ नहीं खेलना हैं",
            "मुझे सच में और समय के लिए कारों के साथ नहीं खेलना हैं"

    },{"मुझे ट्रकों के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में ट्रकों के साथ खेलना अच्छा लगता हैं",
            "मुझे ट्रकों के साथ खेलना हैं",
            "मुझे सच में ट्रकों के साथ खेलना हैं",
            "मुझे और समय के लिए ट्रकों के साथ खेलना हैं",
            "मुझे सच में थोड़े और समय के लिए ट्रकों के साथ खेलना हैं",
            "मुझे ट्रकों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में ट्रकों के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे ट्रकों के साथ नहीं खेलना हैं",
            "मुझे सच में ट्रकों के साथ नहीं खेलना हैं",
            "मुझे और समय के लिए ट्रकों के साथ नहीं खेलना हैं",
            "मुझे सच में और समय के लिए ट्रकों के साथ नहीं खेलना हैं"

    },{"मुझे आर्ट-क्राफ्ट कार्य करना अच्छा लगता हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करना अच्छा लगता हैं",
            "मुझे आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे और समय के लिए आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे सच में थोड़े और समय के लिए आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे आर्ट-क्राफ्ट कार्य करना अच्छा नहीं लगता हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करना अच्छा नहीं लगता हैं",
            "मुझे आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे और समय के लिए आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे सच में थोड़े और समय के लिए आर्ट-क्राफ्ट कार्य नहीं करने हैं"

    },{"मुझे आप के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में आप के साथ खेलना अच्छा लगता हैं",
            "मुझे आप के साथ खेलना हैं",
            "मुझे सच में आप के साथ खेलना हैं",
            "मुझे आप के साथ, और समय के लिए खेलना हैं",
            "मुझे सच में आप के साथ थोड़े और समय के लिए खेलना हैं",
            "मुझे आप के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में आप के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे आप के साथ नहीं खेलना हैं",
            "मुझे सच में आप के साथ नहीं खेलना हैं",
            "मुझे आप के साथ और समय के लिए नहीं खेलना हैं",
            "मुझे सच में आप के साथ और समय के लिए नहीं खेलना हैं"

    }},{{"मुझे खेल के मैदान पर जाना अच्छा लगता हैं",
            "मुझे सच में खेल के मैदान पर जाना अच्छा लगता हैं",
            "मुझे खेल के मैदान पर जाना हैं",
            "मुझे सच में खेल के मैदान पर जाना हैं",
            "मुझे फिर से खेल के मैदान पर जाना हैं",
            "मुझे सच में फिर से खेल के मैदान पर जाना हैं",
            "मुझे खेल के मैदान पर जाना अच्छा नहीं लगता हैं",
            "मुझे सच में खेल के मैदान पर जाना अच्छा नहीं लगता हैं",
            "मुझे खेल के मैदान पर नहीं जाना हैं",
            "मुझे सच में खेल के मैदान पर नहीं जाना हैं",
            "मुझे फिर से खेल के मैदान पर नहीं जाना हैं",
            "मुझे सच में फिर से खेल के मैदान पर नहीं जाना हैं"

    },{"मुझे पार्क में खेलना अच्छा लगता हैं",
            "मुझे सच में पार्क में खेलना अच्छा लगता हैं",
            "मुझे पार्क में खेलना  हैं",
            "मुझे सच में पार्क में खेलना हैं",
            "मुझे पार्क में और समय के लिए खेलना  हैं",
            "मुझे सच में पार्क में थोड़े और समय के लिए खेलना हैं",
            "मुझे पार्क में खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में पार्क में खेलना अच्छा नहीं लगता हैं",
            "मुझे पार्क में नहीं खेलना  हैं",
            "मुझे सच में पार्क में नहीं खेलना हैं",
            "मुझे पार्क में और समय के लिए नहीं खेलना  हैं",
            "मुझे सच में पार्क में और समय के लिए नहीं खेलना हैं"

    },{"मुझे  झूले पर खेलना अच्छा लगता हैं",
            "मुझे सच में झूले पर खेलना अच्छा लगता हैं",
            "मुझे झूले पर खेलना  हैं",
            "मुझे सच में झूले पर खेलना हैं",
            "मुझे झूले पर और समय के लिए खेलना  हैं",
            "मुझे सच में झूले पर थोड़े और समय के लिए खेलना हैं",
            "मुझे झूले पर खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में झूले पर खेलना अच्छा नहीं लगता हैं",
            "मुझे झूले पर नहीं खेलना  हैं",
            "मुझे सच में झूले पर नहीं खेलना हैं",
            "मुझे झूले पर और समय के लिए नहीं खेलना  हैं",
            "मुझे सच में झूले पर और समय के लिए नहीं खेलना हैं"

    },{"मुझे  स्लाइड पर फ़िसल ना अच्छा लगता हैं",
            "मुझे सच में स्लाइड पर फ़िसल ना अच्छा लगता हैं",
            "मुझे स्लाइड पर फ़िसल ना हैं",
            "मुझे सच में स्लाइड पर फ़िसल ना हैं",
            "मुझे और समय के लिए स्लाइड पर फ़िसल ना हैं",
            "मुझे सच में थोड़े और समय के लिए स्लाइड पर फ़िसल ना हैं",
            "मुझे स्लाइड पर फ़िसल ना अच्छा नहीं लगता हैं",
            "मुझे सच में स्लाइड पर फ़िसल ना अच्छा नहीं लगता हैं",
            "मुझे स्लाइड पर फ़िसल ना नहीं  हैं",
            "मुझे सच में स्लाइड पर फ़िसल ना नहीं हैं",
            "मुझे और समय के लिए स्लाइड पर फ़िसल ना नहीं हैं",
            "मुझे सच में और समय के लिए स्लाइड पर फ़िसल ना नहीं हैं"

    },{"मुझे  सी-सॉ पर खेलना अच्छा लगता हैं",
            "मुझे सच में सी-सॉ पर खेलना अच्छा लगता हैं",
            "मुझे सी-सॉ पर खेलना  हैं",
            "मुझे सच में सी-सॉ पर खेलना हैं",
            "मुझे और समय के लिए सी-सॉ पर खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए सी-सॉ पर खेलना हैं",
            "मुझे सी-सॉ पर खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में सी-सॉ पर खेलना अच्छा नहीं लगता हैं",
            "मुझे सी-सॉ पर नहीं खेलना  हैं",
            "मुझे सच में सी-सॉ पर नहीं खेलना हैं",
            "मुझे सी-सॉ पर और समय के लिए नहीं खेलना  हैं",
            "मुझे सच में सी-सॉ पर और समय के लिए नहीं खेलना हैं"

    },{"मुझे  मेरी-गो-राउंड पर खेलना अच्छा लगता हैं",
            "मुझे सच में मेरी-गो-राउंड पर खेलना अच्छा लगता हैं",
            "मुझे मेरी-गो-राउंड पर खेलना  हैं",
            "मुझे सच में मेरी-गो-राउंड पर खेलना हैं",
            "मुझे और समय के लिए मेरी-गो-राउंड पर खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए मेरी-गो-राउंड पर खेलना हैं",
            "मुझे मेरी-गो-राउंड पर खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में मेरी-गो-राउंड पर खेलना अच्छा नहीं लगता हैं",
            "मुझे मेरी-गो-राउंड पर नहीं खेलना  हैं",
            "मुझे सच में मेरी-गो-राउंड पर नहीं खेलना हैं",
            "मुझे मेरी-गो-राउंड पर, और समय के लिए नहीं खेलना  हैं",
            "मुझे सच में मेरी-गो-राउंड पर, और समय के लिए नहीं खेलना हैं"

    },{"मुझे लुकाछिपी खेलना अच्छा लगता हैं",
            "मुझे सच में लुकाछिपी खेलना अच्छा लगता हैं",
            "मुझे लुकाछिपी खेलना  हैं",
            "मुझे सच में लुकाछिपी खेलना हैं",
            "मुझे फिर से लुकाछिपी खेलना  हैं",
            "मुझे सच में फिर से लुकाछिपी खेलना हैं",
            "मुझे लुकाछिपी खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में लुकाछिपी खेलना अच्छा नहीं लगता हैं",
            "मुझे लुकाछिपी नहीं खेलना  हैं",
            "मुझे सच में लुकाछिपी नहीं खेलना हैं",
            "मुझे फिर से लुकाछिपी नहीं खेलना  हैं",
            "मुझे सच में फिर से लुकाछिपी नहीं खेलना हैं"

    },{"मुझे बल्ला और गेंद खेलना अच्छा लगता हैं",
            "मुझे सच में बल्ला और गेंद खेलना अच्छा लगता हैं",
            "मुझे बल्ला और गेंद खेलना  हैं",
            "मुझे सच में बल्ला और गेंद खेलना हैं",
            "मुझे फिर से बल्ला और गेंद खेलना  हैं",
            "मुझे सच में फिर से बल्ला और गेंद खेलना हैं",
            "मुझे बल्ला और गेंद खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में बल्ला और गेंद खेलना अच्छा नहीं लगता हैं",
            "मुझे बल्ला और गेंद नहीं खेलना  हैं",
            "मुझे सच में बल्ला और गेंद नहीं खेलना हैं",
            "मुझे फिर से बल्ला और गेंद नहीं खेलना  हैं",
            "मुझे सच में फिर से बल्ला और गेंद नहीं खेलना हैं"

    },{"मुझे स्टैचू खेल खेलना अच्छा लगता हैं",
            "मुझे सच में स्टैचू खेल खेलना अच्छा लगता हैं",
            "मुझे स्टैचू खेल खेलना  हैं",
            "मुझे सच में स्टैचू खेल खेलना हैं",
            "मुझे फिर से स्टैचू खेल खेलना  हैं",
            "मुझे सच में फिर से स्टैचू खेल खेलना हैं",
            "मुझे स्टैचू खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में स्टैचू खेल खेलना अच्छा नहीं लगता हैं",
            "मुझे स्टैचू खेल नहीं खेलना  हैं",
            "मुझे सच में स्टैचू खेल नहीं खेलना हैं",
            "मुझे फिर से स्टैचू खेल नहीं खेलना  हैं",
            "मुझे सच में फिर से स्टैचू खेल नहीं खेलना हैं"

    },{"मुझे ताला और चाबी खेलना अच्छा लगता हैं",
            "मुझे सच में ताला और चाबी खेलना अच्छा लगता हैं",
            "मुझे ताला और चाबी खेलना  हैं",
            "मुझे सच में ताला और चाबी खेलना हैं",
            "मुझे फिर से ताला और चाबी खेलना  हैं",
            "मुझे सच में फिर से ताला और चाबी खेलना हैं",
            "मुझे ताला और चाबी खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में ताला और चाबी खेलना अच्छा नहीं लगता हैं",
            "मुझे ताला और चाबी नहीं खेलना  हैं",
            "मुझे सच में ताला और चाबी नहीं खेलना हैं",
            "मुझे फिर से ताला और चाबी नहीं खेलना  हैं",
            "मुझे सच में फिर से ताला और चाबी नहीं खेलना हैं"

    },{"मुझे पकड़ा-पकड़ी खेलना अच्छा लगता हैं",
            "मुझे सच में पकड़ा-पकड़ी खेलना अच्छा लगता हैं",
            "मुझे पकड़ा-पकड़ी खेलना  हैं",
            "मुझे सच में पकड़ा-पकड़ी खेलना हैं",
            "मुझे फिर से पकड़ा-पकड़ी खेलना  हैं",
            "मुझे सच में फिर से पकड़ा-पकड़ी खेलना हैं",
            "मुझे पकड़ा-पकड़ी खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में पकड़ा-पकड़ी खेलना अच्छा नहीं लगता हैं",
            "मुझे पकड़ा-पकड़ी नहीं खेलना  हैं",
            "मुझे सच में पकड़ा-पकड़ी नहीं खेलना हैं",
            "मुझे फिर से पकड़ा-पकड़ी नहीं खेलना  हैं",
            "मुझे सच में फिर से पकड़ा-पकड़ी नहीं खेलना हैं"

    },{"मुझे  पतंग उड़ाना अच्छा लगता हैं",
            "मुझे सच में पतंग उड़ाना अच्छा लगता हैं",
            "मुझे  पतंग उड़ाना हैं",
            "मुझे सच में पतंग उड़ाना हैं",
            "मुझे  फिर से पतंग उड़ाना हैं",
            "मुझे सच में फिर से पतंग उड़ाना हैं",
            "मुझे  पतंग उड़ाना अच्छा नहीं लगता हैं",
            "मुझे सच में पतंग उड़ाना अच्छा नहीं लगता हैं",
            "मुझे  पतंग नहीं उड़ाना हैं",
            "मुझे सच में पतंग नहीं उड़ाना हैं",
            "मुझे  फिर से पतंग नहीं उड़ाना हैं",
            "मुझे सच में फिर से पतंग नहीं उड़ाना हैं"

    },{"मुझे चोर-पुलिस खेलना अच्छा लगता हैं",
            "मुझे सच में चोर-पुलिस खेलना अच्छा लगता हैं",
            "मुझे चोर-पुलिस खेलना  हैं",
            "मुझे सच में चोर-पुलिस खेलना हैं",
            "मुझे फिर से चोर-पुलिस खेलना  हैं",
            "मुझे सच में फिर से चोर-पुलिस खेलना हैं",
            "मुझे चोर-पुलिस खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में चोर-पुलिस खेलना अच्छा नहीं लगता हैं",
            "मुझे चोर-पुलिस नहीं खेलना  हैं",
            "मुझे सच में चोर-पुलिस नहीं खेलना हैं",
            "मुझे फिर से चोर-पुलिस नहीं खेलना  हैं",
            "मुझे सच में फिर से चोर-पुलिस नहीं खेलना हैं",

    },{"मुझे कंचे के साथ खेलना अच्छा लगता हैं",
            "मुझे सच में कंचे के साथ खेलना अच्छा लगता हैं",
            "मुझे कंचे के साथ खेलना  हैं",
            "मुझे सच में कंचे के साथ खेलना हैं",
            "मुझे फिर से कंचे के साथ खेलना  हैं",
            "मुझे सच में फिर से कंचे के साथ खेलना हैं",
            "मुझे कंचे के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में कंचे के साथ खेलना अच्छा नहीं लगता हैं",
            "मुझे कंचे के साथ नहीं खेलना  हैं",
            "मुझे सच में कंचे के साथ नहीं खेलना हैं",
            "मुझे फिर से कंचे के साथ नहीं खेलना  हैं",
            "मुझे सच में फिर से कंचे के साथ नहीं खेलना हैं"

    },{"मुझे चलना अच्छा लगता हैं",
            "मुझे सच में चलना अच्छा लगता हैं",
            "मुझे चलना हैं",
            "मुझे सच में चलना हैं",
            "मुझे और समय के लिए चलना  हैं",
            "मुझे सच में थोड़े और समय के लिए चलना हैं",
            "मुझे चलना अच्छा नहीं लगता हैं",
            "मुझे सच में चलना अच्छा नहीं लगता हैं",
            "मुझे चलना नहीं हैं",
            "मुझे सच में चलना नहीं हैं",
            "मुझे और समय के लिए चलना नहीं हैं",
            "मुझे सच में और समय के लिए चलना नहीं हैं"

    },{"मुझे सायकल चलाना अच्छा लगता हैं",
            "मुझे सच में सायकल चलाना अच्छा लगता हैं",
            "मुझे सायकल चलानी हैं",
            "मुझे सच में सायकल चलानी हैं",
            "मुझे और समय के लिए सायकल चलानी  हैं",
            "मुझे सच में थोड़े और समय के लिए सायकल चलानी हैं",
            "मुझे सायकल चलाना अच्छा नहीं लगता हैं",
            "मुझे सच में सायकल चलाना अच्छा नहीं लगता हैं",
            "मुझे सायकल नहीं चलानी हैं",
            "मुझे सच में नहीं चलानी हैं",
            "मुझे और समय के लिए सायकल नहीं चलानी हैं",
            "मुझे सच में और समय के लिए सायकल नहीं चलानी हैं"

    },{"मुझे दौड़ना अच्छा लगता हैं",
            "मुझे सच में दौड़ना अच्छा लगता हैं",
            "मुझे दौड़ना हैं",
            "मुझे सच में दौड़ना हैं",
            "मुझे और समय के लिए दौड़ना  हैं",
            "मुझे सच में थोड़े और समय के लिए दौड़ना हैं",
            "मुझे दौड़ना अच्छा नहीं लगता हैं",
            "मुझे सच में दौड़ना अच्छा नहीं लगता हैं",
            "मुझे दौड़ना नहीं हैं",
            "मुझे सच में दौड़ना नहीं हैं",
            "मुझे और समय के लिए दौड़ना नहीं हैं",
            "मुझे सच में और समय के लिए दौड़ना नहीं हैं"

    },{"मुझे तऐरना अच्छा लगता हैं",
            "मुझे सच में तऐरना अच्छा लगता हैं",
            "मुझे तऐरना हैं",
            "मुझे सच में तऐरना हैं",
            "मुझे और समय के लिए तऐरना  हैं",
            "मुझे सच में थोड़े और समय के लिए तऐरना हैं",
            "मुझे तऐरना अच्छा नहीं लगता हैं",
            "मुझे सच में तऐरना अच्छा नहीं लगता हैं",
            "मुझे तऐरना नहीं हैं",
            "मुझे सच में तऐरना नहीं हैं",
            "मुझे और समय के लिए तऐरना नहीं हैं",
            "मुझे सच में और समय के लिए तऐरना नहीं हैं"

    }},{{"मुझे क्रिकेट खेलना अच्छा लगता हैं",
            "मुझे सच में क्रिकेट खेलना अच्छा लगता हैं",
            "मुझे क्रिकेट खेलना हैं",
            "मुझे सच में क्रिकेट खेलना हैं",
            "मुझे और समय के लिए क्रिकेट खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए क्रिकेट खेलना हैं",
            "मुझे क्रिकेट खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में क्रिकेट खेलना अच्छा नहीं लगता हैं",
            "मुझे क्रिकेट नहीं खेलना हैं",
            "मुझे सच में क्रिकेट नहीं खेलना हैं",
            "मुझे और समय के लिए क्रिकेट नहीं खेलना हैं",
            "मुझे सच में और समय के लिए क्रिकेट नहीं खेलना हैं"

    },{"मुझे badminton खेलना अच्छा लगता हैं",
            "मुझे सच में badminton खेलना अच्छा लगता हैं",
            "मुझे badminton खेलना हैं",
            "मुझे सच में badminton खेलना हैं",
            "मुझे और समय के लिए badminton खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए badminton खेलना हैं",
            "मुझे badminton खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में badminton खेलना अच्छा नहीं लगता हैं",
            "मुझे badminton नहीं खेलना हैं",
            "मुझे सच में badminton नहीं खेलना हैं",
            "मुझे और समय के लिए badminton नहीं खेलना हैं",
            "मुझे सच में और समय के लिए badminton नहीं खेलना हैं"

    },{"मुझे टेनिस खेलना अच्छा लगता हैं",
            "मुझे सच में टेनिस खेलना अच्छा लगता हैं",
            "मुझे टेनिस खेलना हैं",
            "मुझे सच में टेनिस खेलना हैं",
            "मुझे और समय के लिए टेनिस खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए टेनिस खेलना हैं",
            "मुझे टेनिस खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में टेनिस खेलना अच्छा नहीं लगता हैं",
            "मुझे टेनिस नहीं खेलना हैं",
            "मुझे सच में टेनिस नहीं खेलना हैं",
            "मुझे और समय के लिए टेनिस नहीं खेलना हैं",
            "मुझे सच में और समय के लिए टेनिस नहीं खेलना हैं"

    },{"मुझे बास्केटबॉल खेलना अच्छा लगता हैं",
            "मुझे सच में बास्केटबॉल खेलना अच्छा लगता हैं",
            "मुझे बास्केटबॉल खेलना हैं",
            "मुझे सच में बास्केटबॉल खेलना हैं",
            "मुझे और समय के लिए बास्केटबॉल खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए बास्केटबॉल खेलना हैं",
            "मुझे बास्केटबॉल खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में बास्केटबॉल खेलना अच्छा नहीं लगता हैं",
            "मुझे बास्केटबॉल नहीं खेलना हैं",
            "मुझे सच में बास्केटबॉल नहीं खेलना हैं",
            "मुझे और समय के लिए बास्केटबॉल नहीं खेलना हैं",
            "मुझे सच में और समय के लिए बास्केटबॉल नहीं खेलना हैं"

    },{"मुझे dodgeball खेलना अच्छा लगता हैं",
            "मुझे सच में dodgeball खेलना अच्छा लगता हैं",
            "मुझे dodgeball खेलना हैं",
            "मुझे सच में dodgeball खेलना हैं",
            "मुझे और समय के लिए dodgeball खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए dodgeball खेलना हैं",
            "मुझे dodgeball खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में dodgeball खेलना अच्छा नहीं लगता हैं",
            "मुझे dodgeball नहीं खेलना हैं",
            "मुझे सच में dodgeball नहीं खेलना हैं",
            "मुझे और समय के लिए dodgeball नहीं खेलना हैं",
            "मुझे सच में और समय के लिए dodgeball नहीं खेलना हैं"

    },{"मुझे volleyball खेलना अच्छा लगता हैं",
            "मुझे सच में volleyball खेलना अच्छा लगता हैं",
            "मुझे volleyball खेलना हैं",
            "मुझे सच में volleyball खेलना हैं",
            "मुझे और समय के लिए volleyball खेलना  हैं",
            "मुझे सच में थोड़े और समय volleyball खेलना हैं",
            "मुझे volleyball खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में volleyball खेलना अच्छा नहीं लगता हैं",
            "मुझे volleyball नहीं खेलना हैं",
            "मुझे सच में volleyball नहीं खेलना हैं",
            "मुझे और समय के लिए volleyball नहीं खेलना हैं",
            "मुझे सच में और समय के लिए volleyball नहीं खेलना हैं"

    },{"मुझे खो-खो खेलना अच्छा लगता हैं",
            "मुझे सच में खो-खो खेलना अच्छा लगता हैं",
            "मुझे खो-खो खेलना हैं",
            "मुझे सच में खो-खो खेलना हैं",
            "मुझे और समय के लिए खो-खो खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए खो-खो खेलना हैं",
            "मुझे खो-खो खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में खो-खो खेलना अच्छा नहीं लगता हैं",
            "मुझे खो-खो नहीं खेलना हैं",
            "मुझे सच में खो-खो नहीं खेलना हैं",
            "मुझे और समय के लिए खो-खो नहीं खेलना हैं",
            "मुझे सच में और समय के लिए खो-खो नहीं खेलना हैं"

    },{"मुझे फुटबॉल खेलना अच्छा लगता हैं",
            "मुझे सच में फुटबॉल खेलना अच्छा लगता हैं",
            "मुझे फुटबॉल खेलना हैं",
            "मुझे सच में फुटबॉल खेलना हैं",
            "मुझे और समय के लिए फुटबॉल खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए फुटबॉल खेलना हैं",
            "मुझे फुटबॉल खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में फुटबॉल खेलना अच्छा नहीं लगता हैं",
            "मुझे फुटबॉल नहीं खेलना हैं",
            "मुझे सच में फुटबॉल नहीं खेलना हैं",
            "मुझे और समय के लिए फुटबॉल नहीं खेलना हैं",
            "मुझे सच में और समय के लिए फुटबॉल नहीं खेलना हैं"

    },{"मुझे कबड़्डी खेलना अच्छा लगता हैं",
            "मुझे सच में कबड़्डी खेलना अच्छा लगता हैं",
            "मुझे कबड़्डी खेलना हैं",
            "मुझे सच में कबड़्डी खेलना हैं",
            "मुझे और समय के लिए कबड़्डी खेलना  हैं",
            "मुझे सच में थोड़े और समय के लिए कबड़्डी खेलना हैं",
            "मुझे कबड़्डी खेलना अच्छा नहीं लगता हैं",
            "मुझे सच में कबड़्डी खेलना अच्छा नहीं लगता हैं",
            "मुझे कबड़्डी नहीं खेलना हैं",
            "मुझे सच में कबड़्डी नहीं खेलना हैं",
            "मुझे और समय के लिए कबड़्डी नहीं खेलना हैं",
            "मुझे सच में और समय के लिए कबड़्डी नहीं खेलना हैं"

    },{"मुझे gymnastics करना अच्छा लगता हैं",
            "मुझे सच में gymnastics करना अच्छा लगता हैं",
            "मुझे gymnastics करना हैं",
            "मुझे सच में gymnastics करना हैं",
            "मुझे और समय के लिए gymnastics करना हैं",
            "मुझे सच में थोड़े और समय के लिए gymnastics करना हैं",
            "मुझे gymnastics करना अच्छा नहीं लगता हैं",
            "मुझे सच में gymnastics करना अच्छा नहीं लगता हैं",
            "मुझे gymnastics नहीं करना हैं",
            "मुझे सच में gymnastics नहीं करना हैं",
            "मुझे और समय के लिए gymnastics नहीं करना हैं",
            "मुझे सच में और समय के लिए gymnastics नहीं करना हैं"

    },{"मुझे तऐरना अच्छा लगता हैं",
            "मुझे सच में तऐरना अच्छा लगता हैं",
            "मुझे तऐरना हैं",
            "मुझे सच में तऐरना हैं",
            "मुझे और समय के लिए तऐरना  हैं",
            "मुझे सच में थोड़े और समय के लिए तऐरना हैं",
            "मुझे तऐरना अच्छा नहीं लगता हैं",
            "मुझे सच में तऐरना अच्छा नहीं लगता हैं",
            "मुझे तऐरना नहीं हैं",
            "मुझे सच में तऐरना नहीं हैं",
            "मुझे और समय के लिए तऐरना नहीं हैं",
            "मुझे सच में और समय के लिए तऐरना नहीं हैं"

    }},{{"मुझे अगला चैनल अच्छा लगता हैं",
            "मुझे सच में अगला चैनल अच्छा लगता हैं",
            "मुझे अगला चैनल देखना हैं",
            "मुझे सच में अगला चैनल देखना हैं",
            "मुझे फिर से अगला चैनल देखना हैं",
            "मुझे सच में फिर से अगला चैनल देखना हैं",
            "मुझे अगला चैनल अच्छा नहीं लगता हैं",
            "मुझे सच में अगला चैनल अच्छा नहीं लगता हैं",
            "मुझे अगला चैनल नहीं देखना हैं",
            "मुझे सच में अगला चैनल नहीं देखना हैं",
            "मुझे फिर से अगला चैनल नहीं देखना हैं",
            "मुझे सच में फिर से अगला चैनल नहीं देखना हैं"

    },{"मुझे पिछला चैनल अच्छा लगता हैं",
            "मुझे सच में पिछला चैनल अच्छा लगता हैं",
            "मुझे पिछला चैनल देखना हैं",
            "मुझे सच में पिछला चैनल देखना हैं",
            "मुझे फिर से पिछला चैनल देखना हैं",
            "मुझे सच में फिर से पिछला चैनल देखना हैं",
            "मुझे पिछला चैनल अच्छा नहीं लगता हैं",
            "मुझे सच में पिछला चैनल अच्छा नहीं लगता हैं",
            "मुझे पिछला चैनल नहीं देखना हैं",
            "मुझे सच में पिछला चैनल नहीं देखना हैं",
            "मुझे फिर से पिछला चैनल नहीं देखना हैं",
            "मुझे सच में फिर से पिछला चैनल नहीं देखना हैं"

    },{"मुझे उँची आवाज़ अच्छी लगती हैं",
            "मुझे सच में उँची आवाज़ अच्छी लगती हैं",
            "क्रिपया आवाज़ बढ़ाइए",
            "क्रिपया आवाज़ बढ़ाइए",
            "क्रिपया आवाज़ और बढ़ाइए",
            "क्रिपया आवाज़ थोड़ा और बढ़ाइए",
            "मुझे उँची आवाज़़ अच्छी नहीं लगती हैं",
            "मुझे सच में उँची आवाज़़ अच्छी नहीं लगती हैं",
            "क्रिपया आवाज़ मत बढ़ाइए",
            "क्रिपया आवाज़ मत बढ़ाइए",
            "क्रिपया आवाज़ और मत बढ़ाइए",
            "क्रिपया आवाज़ और मत बढ़ाइए"

    },{"मुझे धीमी आवाज़़ अच्छी लगती हैं",
            "मुझे सच में धीमी आवाज़़ अच्छी लगती हैं",
            "क्रिपया आवाज़ कम कीजिए",
            "क्रिपया आवाज़ कम कीजिए",
            "क्रिपया आवाज़ और कम कीजिए",
            "क्रिपया आवाज़ थोड़ा और कम कीजिए",
            "मुझे धीमी आवाज़़ अच्छी नहीं लगती हैं",
            "मुझे सच में धीमी आवाज़़ अच्छी नहीं लगती हैं",
            "क्रिपया आवाज़ कम मत कीजिए",
            "क्रिपया आवाज़ कम मत कीजिए",
            "क्रिपया आवाज़ और कम मत कीजिए",
            "क्रिपया आवाज़ और कम मत कीजिए"

    }},{{"मुझे संगीत बदलना अच्छा लगता हैं",
            "मुझे सच में संगीत बदलना अच्छा लगता हैं",
            "मुझे संगीत बदलना हैं",
            "मुझे सच में संगीत बदलना हैं",
            "मुझे फिर से संगीत बदलना हैं",
            "मुझे सच में फिर से संगीत बदलना हैं",
            "मुझे संगीत बदलना अच्छा नहीं लगता हैं",
            "मुझे सच में संगीत बदलना अच्छा नहीं लगता हैं",
            "मुझे संगीत नहीं बदलना हैं",
            "मुझे सच में संगीत नहीं बदलना हैं",
            "मुझे फिर से संगीत नहीं बदलना हैं",
            "मुझे सच में फिर से संगीत नहीं बदलना हैं"

    },{"मुझे नाचना अच्छा लगता हैं",
            "मुझे सच में नाचना अच्छा लगता हैं",
            "मुझे नाचना हैं",
            "मुझे सच में नाचना हैं",
            "मुझे और नाचना हैं",
            "मुझे सच में थोड़ा और नाचना हैं",
            "मुझे नाचना अच्छा नहीं लगता हैं",
            "मुझे सच में नाचना अच्छा नहीं लगता हैं",
            "मुझे नाचना नहीं हैं",
            "मुझे सच में नाचना नहीं हैं",
            "मुझे और नाचना नहीं हैं",
            "मुझे सच में और नाचना नहीं हैं"

    },{"मुझे उँची आवाज़़ अच्छी लगती हैं",
            "मुझे सच में उँची आवाज़ अच्छी लगती हैं",
            "क्रिपया आवाज़ बढ़ाइए",
            "क्रिपया आवाज़ बढ़ाइए",
            "क्रिपया आवाज़ और बढ़ाइए",
            "क्रिपया आवाज़़ थोड़ा और बढ़ाइए",
            "मुझे उँची आवाज़ अच्छी नहीं लगती हैं",
            "मुझे सच में उँची आवाज़ अच्छी नहीं लगती हैं",
            "क्रिपया आवाज़ मत बढ़ाइए",
            "क्रिपया आवाज़ मत बढ़ाइए",
            "क्रिपया आवाज़ और मत बढ़ाइए",
            "क्रिपया आवाज़ और मत बढ़ाइए"

    },{"मुझे धीमी आवाज़ अच्छी लगती हैं",
            "मुझे सच में धीमी आवाज़ अच्छी लगती हैं",
            "क्रिपया आवाज़ कम कीजिए",
            "क्रिपया आवाज़ कम कीजिए",
            "क्रिपया आवाज़ और कम कीजिए",
            "क्रिपया आवाज़ थोड़ा और कम कीजिए",
            "मुझे धीमी आवाज़ अच्छी नहीं लगती हैं",
            "मुझे सच में धीमी आवाज़ अच्छी नहीं लगती हैं",
            "क्रिपया आवाज़ कम मत कीजिए",
            "क्रिपया आवाज़ कम मत कीजिए",
            "क्रिपया आवाज़ और कम मत कीजिए",
            "क्रिपया आवाज़ और कम मत कीजिए"

    }},{{"मुझे चित्र बनाना अच्छा लगता हैं",
            "मुझे सच में चित्र बनाना अच्छा लगता हैं",
            "मुझे चित्र बनाने हैं",
            "मुझे सच में चित्र बनाने हैं",
            "मुझे और समय के लिए चित्र बनाने हैं",
            "मुझे सच में फिर से चित्र बनाने हैं",
            "मुझे चित्र बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में चित्र बनाना अच्छा नहीं लगता हैं",
            "मुझे चित्र नहीं बनाने हैं",
            "मुझे सच में चित्र नहीं बनाने हैं",
            "मुझे और समय के लिए चित्र नहीं बनाने हैं",
            "मुझे सच में और समय के लिए चित्र नहीं बनाने हैं"

    },{"मुझे चित्रों में रंग भरना अच्छा लगता हैं",
            "मुझे सच में चित्रों में रंग भरना अच्छा लगता हैं",
            "मुझे चित्रों में रंग भरने हैं",
            "मुझे सच में चित्रों में रंग भरने हैं",
            "मुझे थोड़े और चित्रों में रंग भरने हैं",
            "मुझे सच में फिर से चित्रों में रंग भरने हैं",
            "मुझे चित्रों में रंग भरना अच्छा नहीं लगता हैं",
            "मुझे सच में चित्रों में रंग भरना अच्छा नहीं लगता",
            "मुझे चित्रों में रंग नहीं भरने हैं",
            "मुझे सच में चित्रों में रंग नहीं भरने हैं",
            "मुझे और समय के लिए चित्रों में रंग नहीं भरने हैं",
            "मुझे सच में और समय के लिए चित्रों में रंग नहीं भरने हैं"

    },{"मुझे पढ़ना अच्छा लगता हैं",
            "मुझे सच में पढ़ना अच्छा लगता हैं",
            "मुझे पढ़ना हैं",
            "मुझे सच में पढ़ना हैं",
            "मुझे और समय के लिए पढ़ना हैं",
            "मुझे सच में थोड़े और समय के लिए पढ़ना हैं",
            "मुझे पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में पढ़ना अच्छा नहीं लगता हैं",
            "मुझे पढ़ना नहीं हैं",
            "मुझे सच में पढ़ना नहीं हैं",
            "मुझे और समय के लिए पढ़ना नहीं हैं",
            "मुझे सच में और समय के लिए पढ़ना नहीं हैं"

    },{"मुझे लिखना अच्छा लगता हैं",
            "मुझे सच में लिखना अच्छा लगता हैं",
            "मुझे लिखना हैं",
            "मुझे सच में लिखना हैं",
            "मुझे और समय के लिए लिखना हैं",
            "मुझे सच में थोड़े और समय के लिए लिखना हैं",
            "मुझे लिखना अच्छा नहीं लगता हैं",
            "मुझे सच में लिखना अच्छा नहीं लगता हैं",
            "मुझे लिखना नहीं हैं",
            "मुझे सच में लिखना नहीं हैं",
            "मुझे और समय के लिए लिखना नहीं हैं",
            "मुझे सच में और समय के लिए लिखना नहीं हैं"

    },{"मुझे आर्ट-क्राफ्ट कार्य करना अच्छा लगता हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करना अच्छा लगता हैं",
            "मुझे आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे और समय के लिए आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे सच में थोड़े और समय के लिए आर्ट-क्राफ्ट कार्य करने हैं",
            "मुझे आर्ट-क्राफ्ट कार्य करना अच्छा नहीं लगता हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य करना अच्छा नहीं लगता हैं",
            "मुझे आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे सच में आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे और समय के लिए आर्ट-क्राफ्ट कार्य नहीं करने हैं",
            "मुझे सच में थोड़े और समय के लिए आर्ट-क्राफ्ट कार्य नहीं करने हैं"

    },{"मुझे नाटक में भाग लेना अच्छा लगता हैं",
            "मुझे सच में नाटक में भाग लेना अच्छा लगता हैं",
            "मुझे नाटक में भाग लेना हैं",
            "मुझे सच में नाटक में भाग लेना हैं",
            "मुझे फिर से नाटक में भाग लेना हैं",
            "मुझे सच में फिर से नाटक में भाग लेना हैं",
            "मुझे नाटक में भाग लेना अच्छा नहीं लगता हैं",
            "मुझे सच में नाटक में भाग लेना अच्छा नहीं लगता हैं",
            "मुझे नाटक में भाग नहीं लेना हैं",
            "मुझे सच में नाटक में भाग नहीं लेना हैं",
            "मुझे फिर से नाटक में भाग नहीं लेना हैं",
            "मुझे सच में फिर से नाटक में भाग नहीं लेना हैं"

    },{"मुझे नाचना अच्छा लगता हैं",
            "मुझे सच में नाचना अच्छा लगता हैं",
            "मुझे नाचना हैं",
            "मुझे सच में नाचना हैं",
            "मुझे और समय के लिए नाचना हैं",
            "मुझे सच में थोड़े और समय के लिए नाचना हैं",
            "मुझे नाचना अच्छा नहीं लगता हैं",
            "मुझे सच में नाचना अच्छा नहीं लगता हैं",
            "मुझे नाचना नहीं हैं",
            "मुझे सच में नाचना नहीं हैं",
            "मुझे और समय के लिए नाचना नहीं हैं",
            "मुझे सच में थोड़े और समय के लिए नाचना नहीं हैं"

    }, {"मुझे संगीत बजाना अच्छा लगता हैं",
            "मुझे सच में संगीत बजाना अच्छा लगता हैं",
            "मुझे संगीत बजाना हैं",
            "मुझे सच में संगीत बजाना हैं",
            "मुझे और समय के लिए संगीत बजाना हैं",
            "मुझे सच में थोड़े और  समय के लिए संगीत बजाना हैं",
            "मुझे संगीत बजाना अच्छा नहीं लगता हैं",
            "मुझे सच में संगीत बजाना अच्छा नहीं लगता हैं",
            "मुझे संगीत नहीं बजाना हैं",
            "मुझे सच में संगीत नहीं बजाना हैं",
            "मुझे और समय के लिए संगीत नहीं बजाना हैं",
            "मुझे सच में और समय के लिए संगीत नहीं बजाना हैं",

    }}}, {{{"मुझे कुत्तें अच्छे लगते हैं",
            "मुझे सच में कुत्तें अच्छे लगते हैं",
            "मुझे कुत्तों के बारें में जानना हैं",
            "मुझे सच में कुत्तों के बारें में जानना हैं",
            "मुझे कुत्तों के बारें में और जानना हैं",
            "मुझे सच में कुत्तों के बारें में थोड़ा और  जानना हैं",
            "मुझे कुत्तें अच्छे नहीं लगते हैं",
            "मुझे सच में कुत्तें अच्छे नहीं लगते हैं",
            "मुझे कुत्तों के बारें में नहीं जानना हैं",
            "मुझे सच में कुत्तों के बारें में नहीं जानना हैं",
            "मुझे कुत्तों के बारें में और नहीं जानना हैं",
            "मुझे सच में कुत्तों के बारें में और नहीं जानना हैं"

    }, {"मुझे बिल्लियाँ अच्छी लगती हैं",
            "मुझे सच में बिल्लियाँ अच्छी लगती हैं",
            "मुझे बिल्लियों के बारें में जानना हैं",
            "मुझे सच में बिल्लियों के बारें में जानना हैं",
            "मुझे बिल्लियों के बारें में और जानना हैं",
            "मुझे सच में बिल्लियों के बारें में थोड़ा और  जानना हैं",
            "मुझे बिल्लियाँ अच्छी नहीं लगती हैं",
            "मुझे सच में बिल्लियाँ अच्छी नहीं लगती हैं",
            "मुझे बिल्लियों के बारें में नहीं जानना हैं",
            "मुझे सच में बिल्लियों के बारें में नहीं जानना हैं",
            "मुझे बिल्लियों के बारें में और नहीं जानना हैं",
            "मुझे सच में बिल्लियों के बारें में और नहीं जानना हैं"

    }, {"मुझे हाथी अच्छे लगते हैं",
            "मुझे सच में हाथी अच्छे लगते हैं",
            "मुझे हाथियों के बारें में जानना हैं",
            "मुझे सच में हाथियों के बारें में जानना हैं",
            "मुझे हाथियों के बारें में और जानना हैं",
            "मुझे सच में हाथियों के बारें में थोड़ा और  जानना हैं",
            "मुझे हाथी अच्छे नहीं लगते हैं",
            "मुझे सच में हाथी अच्छे नहीं लगते हैं",
            "मुझे हाथियों के बारें में नहीं जानना हैं",
            "मुझे सच में हाथियों के बारें में नहीं जानना हैं",
            "मुझे हाथियों के बारें में और नहीं जानना हैं",
            "मुझे सच में हाथियों के बारें में और नहीं जानना हैं"

    }, {"मुझे शेर अच्छे लगते हैं",
            "मुझे सच में शेर अच्छे लगते हैं",
            "मुझे शेरों के बारें में जानना हैं",
            "मुझे सच में शेरों के बारें में जानना हैं",
            "मुझे शेरों के बारें में और जानना हैं",
            "मुझे सच में शेरों के बारें में थोड़ा और  जानना हैं",
            "मुझे शेर अच्छे नहीं लगते हैं",
            "मुझे सच में शेर अच्छे नहीं लगते हैं",
            "मुझे शेरों के बारें में नहीं जानना हैं",
            "मुझे सच में शेरों के बारें में नहीं जानना हैं",
            "मुझे शेरों के बारें में और नहीं जानना हैं",
            "मुझे सच में शेरों के बारें में और नहीं जानना हैं"

    }, {"मुझे तोते अच्छे लगते हैं",
            "मुझे सच में तोते अच्छे लगते हैं",
            "मुझे तोतों के बारें में जानना हैं",
            "मुझे सच में तोतों के बारें में जानना हैं",
            "मुझे तोतों के बारें में और जानना हैं",
            "मुझे सच में तोतों के बारें में थोड़ा और  जानना हैं",
            "मुझे तोते अच्छे नहीं लगते हैं",
            "मुझे सच में तोते अच्छे नहीं लगते हैं",
            "मुझे तोतों के बारें में नहीं जानना हैं",
            "मुझे सच में तोतों के बारें में नहीं जानना हैं",
            "मुझे तोतों के बारें में और नहीं जानना हैं",
            "मुझे सच में तोतों के बारें में और नहीं जानना हैं"

    }, {"मुझे खरगोश अच्छे लगते हैं",
            "मुझे सच में खरगोश  अच्छे लगते हैं",
            "मुझे खरगोशों के बारें में जानना हैं",
            "मुझे सच में खरगोशों के बारें में जानना हैं",
            "मुझे खरगोशों के बारें में और जानना हैं",
            "मुझे सच में खरगोशों के बारें में थोड़ा और  जानना हैं",
            "मुझे खरगोश अच्छे नहीं लगते हैं",
            "मुझे सच में खरगोश अच्छे नहीं लगते हैं",
            "मुझे खरगोशों के बारें में नहीं जानना हैं",
            "मुझे सच में खरगोशों के बारें में नहीं जानना हैं",
            "मुझे खरगोशों के बारें में और नहीं जानना हैं",
            "मुझे सच में खरगोशों के बारें में और नहीं जानना हैं"

    }, {"मुझे गायें अच्छी लगती हैं",
            "मुझे सच में गायें  अच्छी लगती हैं",
            "मुझे गायों के बारें में जानना हैं",
            "मुझे सच में गायों के बारें में जानना हैं",
            "मुझे गायों के बारें में और जानना हैं",
            "मुझे सच में गायों के बारें में थोड़ा और  जानना हैं",
            "मुझे गायें अच्छी नहीं लगती हैं",
            "मुझे सच में गायें अच्छी नहीं लगती हैं",
            "मुझे गायों के बारें में नहीं जानना हैं",
            "मुझे सच में गायों के बारें में नहीं जानना हैं",
            "मुझे गायों के बारें में और नहीं जानना हैं",
            "मुझे सच में गायों के बारें में और नहीं जानना हैं"

    }, {"मुझे बथख अच्छे लगते हैं",
            "मुझे सच में बथख  अच्छे लगते हैं",
            "मुझे बथखों के बारें में जानना हैं",
            "मुझे सच में बथखों के बारें में जानना हैं",
            "मुझे बथखों के बारें में और जानना हैं",
            "मुझे सच में बथखों के बारें में थोड़ा और  जानना हैं",
            "मुझे बथख अच्छे नहीं लगते हैं",
            "मुझे सच में बथख अच्छे नहीं लगते हैं",
            "मुझे बथखों के बारें में नहीं जानना हैं",
            "मुझे सच में बथखों के बारें में नहीं जानना हैं",
            "मुझे बथखों के बारें में और नहीं जानना हैं",
            "मुझे सच में बथखों के बारें में और नहीं जानना हैं"

    }, {"मुझे गधे अच्छे लगते हैं",
            "मुझे सच में गधे अच्छे लगते हैं",
            "मुझे गधों के बारें में जानना हैं",
            "मुझे सच में गधों के बारें में जानना हैं",
            "मुझे गधों के बारें में और जानना हैं",
            "मुझे सच में गधों के बारें में थोड़ा और  जानना हैं",
            "मुझे गधे अच्छे नहीं लगते हैं",
            "मुझे सच में गधे अच्छे नहीं लगते हैं",
            "मुझे गधों के बारें में नहीं जानना हैं",
            "मुझे सच में गधों के बारें में नहीं जानना हैं",
            "मुझे गधों के बारें में और नहीं जानना हैं",
            "मुझे सच में गधों के बारें में और नहीं जानना हैं"

    }, {"मुझे चींटियाँ अच्छी लगती हैं",
            "मुझे सच में चींटियाँ अच्छी लगती हैं",
            "मुझे चींटियों के बारें में जानना हैं",
            "मुझे सच में चींटियों के बारें में जानना हैं",
            "मुझे चींटियों के बारें में और जानना हैं",
            "मुझे सच में चींटियों के बारें में थोड़ा और  जानना हैं",
            "मुझे चींटियाँ अच्छी नहीं लगती हैं",
            "मुझे सच में चींटियाँ अच्छी नहीं लगती हैं",
            "मुझे चींटियों के बारें में नहीं जानना हैं",
            "मुझे सच में चींटियों के बारें में नहीं जानना हैं",
            "मुझे चींटियों के बारें में और नहीं जानना हैं",
            "मुझे सच में चींटियों के बारें में और नहीं जानना हैं"

    }, {"मुझे बाघ अच्छे लगते हैं",
            "मुझे सच में बाघ अच्छे लगते हैं",
            "मुझे बाघों के बारें में जानना हैं",
            "मुझे सच में बाघों के बारें में जानना हैं",
            "मुझे बाघों के बारें में और जानना हैं",
            "मुझे सच में बाघों के बारें में थोड़ा और  जानना हैं",
            "मुझे बाघ अच्छे नहीं लगते हैं",
            "मुझे सच में बाघ अच्छे नहीं लगते हैं",
            "मुझे बाघों के बारें में नहीं जानना हैं",
            "मुझे सच में बाघों के बारें में नहीं जानना हैं",
            "मुझे बाघों के बारें में और नहीं जानना हैं",
            "मुझे सच में बाघों के बारें में और नहीं जानना हैं"

    }, {"मुझे बंदर अच्छे लगते हैं",
            "मुझे सच में बंदर अच्छे लगते हैं",
            "मुझे बंदरों के बारें में जानना हैं",
            "मुझे सच में बंदरों के बारें में जानना हैं",
            "मुझे बंदरों के बारें में और जानना हैं",
            "मुझे सच में बंदरों के बारें में थोड़ा और  जानना हैं",
            "मुझे बंदर अच्छे नहीं लगते हैं",
            "मुझे सच में बंदर अच्छे नहीं लगते हैं",
            "मुझे बंदरों के बारें में नहीं जानना हैं",
            "मुझे सच में बंदरों के बारें में नहीं जानना हैं",
            "मुझे बंदरों के बारें में और नहीं जानना हैं",
            "मुझे सच में बंदरों के बारें में और नहीं जानना हैं"

    }, {"मुझे कबूतर अच्छे लगते हैं",
            "मुझे सच में कबूतर अच्छे लगते हैं",
            "मुझे कबूतरों के बारें में जानना हैं",
            "मुझे सच में कबूतरों के बारें में जानना हैं",
            "मुझे कबूतरों के बारें में और जानना हैं",
            "मुझे सच में कबूतरों के बारें में थोड़ा और  जानना हैं",
            "मुझे कबूतर अच्छे नहीं लगते हैं",
            "मुझे सच में कबूतर अच्छे नहीं लगते हैं",
            "मुझे कबूतरों के बारें में नहीं जानना हैं",
            "मुझे सच में कबूतरों के बारें में नहीं जानना हैं",
            "मुझे कबूतरों के बारें में और नहीं जानना हैं",
            "मुझे सच में कबूतरों के बारें में और नहीं जानना हैं"

    }, {"मुझे तिलचट्टे अच्छे लगते हैं",
            "मुझे सच में तिलचट्टे अच्छे लगते हैं",
            "मुझे तिलचट्टों के बारें में जानना हैं",
            "मुझे सच में तिलचट्टों के बारें में जानना हैं",
            "मुझे तिलचट्टों के बारें में और जानना हैं",
            "मुझे सच में तिलचट्टों के बारें में थोड़ा और  जानना हैं",
            "मुझे तिलचट्टे अच्छे नहीं लगते हैं",
            "मुझे सच में तिलचट्टे अच्छे नहीं लगते हैं",
            "मुझे तिलचट्टों के बारें में नहीं जानना हैं",
            "मुझे सच में तिलचट्टों के बारें में नहीं जानना हैं",
            "मुझे तिलचट्टों के बारें में और नहीं जानना हैं",
            "मुझे सच में तिलचट्टों के बारें में और नहीं जानना हैं"

    }, {"मुझे कौउवे अच्छे लगते हैं",
            "मुझे सच में कौउवे अच्छे लगते हैं",
            "मुझे कौउवों के बारें में जानना हैं",
            "मुझे सच में कौउवों के बारें में जानना हैं",
            "मुझे कौउवों के बारें में और जानना हैं",
            "मुझे सच में कौउवों के बारें में थोड़ा और  जानना हैं",
            "मुझे कौउवे अच्छे नहीं लगते हैं",
            "मुझे सच में कौउवे अच्छे नहीं लगते हैं",
            "मुझे कौउवों के बारें में नहीं जानना हैं",
            "मुझे सच में कौउवों के बारें में नहीं जानना हैं",
            "मुझे कौउवों के बारें में और नहीं जानना हैं",
            "मुझे सच में कौउवों के बारें में और नहीं जानना हैं"

    }, {"मुझे घोड़े अच्छे लगते हैं",
            "मुझे सच में घोड़े अच्छे लगते हैं",
            "मुझे घोड़ों के बारें में जानना हैं",
            "मुझे सच में घोड़ों के बारें में जानना हैं",
            "मुझे घोड़ों के बारें में और जानना हैं",
            "मुझे सच में घोड़ों के बारें में थोड़ा और  जानना हैं",
            "मुझे घोड़े अच्छे नहीं लगते हैं",
            "मुझे सच में घोड़े अच्छे नहीं लगते हैं",
            "मुझे घोड़ों के बारें में नहीं जानना हैं",
            "मुझे सच में घोड़ों के बारें में नहीं जानना हैं",
            "मुझे घोड़ों के बारें में और नहीं जानना हैं",
            "मुझे सच में घोड़ों के बारें में और नहीं जानना हैं"

    }, {"मुझे हिरण अच्छे लगते हैं",
            "मुझे सच में हिरण  अच्छे लगते हैं",
            "मुझे हिरणों के बारें में जानना हैं",
            "मुझे सच में हिरणों के बारें में जानना हैं",
            "मुझे हिरणों के बारें में और जानना हैं",
            "मुझे सच में हिरणों के बारें में थोड़ा और  जानना हैं",
            "मुझे हिरण अच्छे नहीं लगते हैं",
            "मुझे सच में हिरण अच्छे नहीं लगते हैं",
            "मुझे हिरणों के बारें में नहीं जानना हैं",
            "मुझे सच में हिरणों के बारें में नहीं जानना हैं",
            "मुझे हिरणों के बारें में और नहीं जानना हैं",
            "मुझे सच में हिरणों के बारें में और नहीं जानना हैं"

    }, {"मुझे उल्लू अच्छे लगते हैं",
            "मुझे सच में उल्लू अच्छे लगते हैं",
            "मुझे उल्लूओं के बारें में जानना हैं",
            "मुझे सच में उल्लूओं के बारें में जानना हैं",
            "मुझे उल्लूओं के बारें में और जानना हैं",
            "मुझे सच में उल्लूओं के बारें में थोड़ा और  जानना हैं",
            "मुझे उल्लू अच्छे नहीं लगते हैं",
            "मुझे सच में उल्लू अच्छे नहीं लगते हैं",
            "मुझे उल्लूओं के बारें में नहीं जानना हैं",
            "मुझे सच में उल्लूओं के बारें में नहीं जानना हैं",
            "मुझे उल्लूओं के बारें में और नहीं जानना हैं",
            "मुझे सच में उल्लूओं के बारें में और नहीं जानना हैं"

    }, {"मुझे भेड़िया अच्छे लगते हैं",
            "मुझे सच में भेड़िया अच्छे लगते हैं",
            "मुझे भेड़ियों के बारें में जानना हैं",
            "मुझे सच में भेड़ियों के बारें में जानना हैं",
            "मुझे भेड़ियों के बारें में और जानना हैं",
            "मुझे सच में भेड़ियों के बारें में थोड़ा और  जानना हैं",
            "मुझे भेड़िया अच्छे नहीं लगते हैं",
            "मुझे सच में भेड़िया अच्छे नहीं लगते हैं",
            "मुझे भेड़ियों के बारें में नहीं जानना हैं",
            "मुझे सच में भेड़ियों के बारें में नहीं जानना हैं",
            "मुझे भेड़ियों के बारें में और नहीं जानना हैं",
            "मुझे सच में भेड़ियों के बारें में और नहीं जानना हैं"

    }, {"मुझे लोमड़ियां अच्छी लगती हैं",
            "मुझे सच में लोमड़ियां अच्छी लगती हैं",
            "मुझे लोमड़ीयों के बारें में जानना हैं",
            "मुझे सच में लोमड़ीयों के बारें में जानना हैं",
            "मुझे लोमड़ीयों के बारें में और जानना हैं",
            "मुझे सच में लोमड़ीयों के बारें में थोड़ा और  जानना हैं",
            "मुझे लोमड़ियां अच्छी नहीं लगती हैं",
            "मुझे सच में लोमड़ियां अच्छी नहीं लगती हैं",
            "मुझे लोमड़ीयों के बारें में नहीं जानना हैं",
            "मुझे सच में लोमड़ीयों के बारें में नहीं जानना हैं",
            "मुझे लोमड़ीयों के बारें में और नहीं जानना हैं",
            "मुझे सच में लोमड़ीयों के बारें में और नहीं जानना हैं"

    }, {"मुझे भालू अच्छे लगते हैं",
            "मुझे सच में भालू अच्छे लगते हैं",
            "मुझे भालूओं के बारें में जानना हैं",
            "मुझे सच में भालूओं के बारें में जानना हैं",
            "मुझे भालूओं के बारें में और जानना हैं",
            "मुझे सच में भालूओं के बारें में थोड़ा और  जानना हैं",
            "मुझे भालू अच्छे नहीं लगते हैं",
            "मुझे सच में भालू अच्छे नहीं लगते हैं",
            "मुझे भालूओं के बारें में नहीं जानना हैं",
            "मुझे सच में भालूओं के बारें में नहीं जानना हैं",
            "मुझे भालूओं के बारें में और नहीं जानना हैं",
            "मुझे सच में भालूओं के बारें में और नहीं जानना हैं"

    }, {"मुझे भेड़ अच्छे लगते हैं",
            "मुझे सच में भेड़  अच्छे लगते हैं",
            "मुझे भेड़ों के बारें में जानना हैं",
            "मुझे सच में भेड़ों के बारें में जानना हैं",
            "मुझे भेड़ों के बारें में और जानना हैं",
            "मुझे सच में भेड़ों के बारें में थोड़ा और  जानना हैं",
            "मुझे भेड़ अच्छे नहीं लगते हैं",
            "मुझे सच में भेड़ अच्छे नहीं लगते हैं",
            "मुझे भेड़ों के बारें में नहीं जानना हैं",
            "मुझे सच में भेड़ों के बारें में नहीं जानना हैं",
            "मुझे भेड़ों के बारें में और नहीं जानना हैं",
            "मुझे सच में भेड़ों के बारें में और नहीं जानना हैं"

    }, {"मुझे बकरीयाँ अच्छी लगती हैं",
            "मुझे सच में बकरीयाँ  अच्छी लगती हैं",
            "मुझे बक रियौं के बारें में जानना हैं",
            "मुझे सच में बक रियौं के बारें में जानना हैं",
            "मुझे बक रियौं के बारें में और जानना हैं",
            "मुझे सच में बक रियौं के बारें में थोड़ा और  जानना हैं",
            "मुझे बकरीयाँ अच्छी नहीं लगती हैं",
            "मुझे सच में बकरीयाँ अच्छी नहीं लगती हैं",
            "मुझे बक रियौं के बारें में नहीं जानना हैं",
            "मुझे सच में बक रियौं के बारें में नहीं जानना हैं",
            "मुझे बक रियौं के बारें में और नहीं जानना हैं",
            "मुझे सच में बक रियौं के बारें में और नहीं जानना हैं"

    }, {"मुझे सुअर अच्छे लगते हैं",
            "मुझे सच में सुअर  अच्छे लगते हैं",
            "मुझे सुअरों के बारें में जानना हैं",
            "मुझे सच में सुअरों के बारें में जानना हैं",
            "मुझे सुअरों के बारें में और जानना हैं",
            "मुझे सच में सुअरों के बारें में थोड़ा और  जानना हैं",
            "मुझे सुअर अच्छे नहीं लगते हैं",
            "मुझे सच में सुअर अच्छे नहीं लगते हैं",
            "मुझे सुअरों के बारें में नहीं जानना हैं",
            "मुझे सच में सुअरों के बारें में नहीं जानना हैं",
            "मुझे सुअरों के बारें में और नहीं जानना हैं",
            "मुझे सच में सुअरों के बारें में और नहीं जानना हैं"

    }, {"मुझे मक्खीयाँ अच्छी लगती हैं",
            "मुझे सच में मक्खीयाँ अच्छी लगती हैं",
            "मुझे मक्खीयों के बारें में जानना हैं",
            "मुझे सच में मक्खीयों के बारें में जानना हैं",
            "मुझे मक्खीयों के बारें में और जानना हैं",
            "मुझे सच में मक्खीयों के बारें में थोड़ा और जानना हैं",
            "मुझे मक्खीयाँ अच्छी नहीं लगती हैं",
            "मुझे सच में मक्खीयाँ अच्छी नहीं लगती हैं",
            "मुझे मक्खीयों के बारें में नहीं जानना हैं",
            "मुझे सच में मक्खीयों के बारें में नहीं जानना हैं",
            "मुझे मक्खीयों के बारें में और नहीं जानना हैं",
            "मुझे सच में मक्खीयों के बारें में और नहीं जानना हैं"

    }, {"मुझे जिराफ़ अच्छे लगते हैं",
            "मुझे सच में जिराफ़  अच्छे लगते हैं",
            "मुझे जिराफ़ के बारें में जानना हैं",
            "मुझे सच में जिराफ़ के बारें में जानना हैं",
            "मुझे जिराफ़ के बारें में और जानना हैं",
            "मुझे सच में जिराफ़ के बारें में थोड़ा और  जानना हैं",
            "मुझे जिराफ़ अच्छे नहीं लगते हैं",
            "मुझे सच में जिराफ़ अच्छे नहीं लगते हैं",
            "मुझे जिराफ़ के बारें में नहीं जानना हैं",
            "मुझे सच में जिराफ़ के बारें में नहीं जानना हैं",
            "मुझे जिराफ़ के बारें में और नहीं जानना हैं",
            "मुझे सच में जिराफ़ के बारें में और नहीं जानना हैं"

    }, {"मुझे ज़ेब्रा अच्छे लगते  हैं",
            "मुझे सच में ज़ेब्रा अच्छे लगते  हैं",
            "मुझे ज़ेब्रा के बारें में जानना हैं",
            "मुझे सच में ज़ेब्रा के बारें में जानना हैं",
            "मुझे ज़ेब्रा के बारें में और जानना हैं",
            "मुझे सच में ज़ेब्रा के बारें में थोड़ा और जानना हैं",
            "मुझे ज़ेब्रा अच्छे नहीं लगते हैं",
            "मुझे सच में ज़ेब्रा अच्छे नहीं लगते  हैं",
            "मुझे ज़ेब्रा के बारें में नहीं जानना हैं",
            "मुझे सच में ज़ेब्रा के बारें में नहीं जानना हैं",
            "मुझे ज़ेब्रा के बारें में और नहीं जानना हैं",
            "मुझे सच में ज़ेब्रा के बारें में और नहीं जानना हैं"

    }, {"मुझे मच्छर अच्छे लगते हैं",
            "मुझे सच में मच्छर अच्छे लगते हैं",
            "मुझे मच्छरों के बारें में जानना हैं",
            "मुझे सच में मच्छरों के बारें में जानना हैं",
            "मुझे मच्छरों के बारें में और जानना हैं",
            "मुझे सच में मच्छरों के बारें में थोड़ा और  जानना हैं",
            "मुझे मच्छर अच्छे नहीं लगते हैं",
            "मुझे सच में मच्छर अच्छे नहीं लगते हैं",
            "मुझे मच्छरों के बारें में नहीं जानना हैं",
            "मुझे सच में मच्छरों के बारें में नहीं जानना हैं",
            "मुझे मच्छरों के बारें में और नहीं जानना हैं",
            "मुझे सच में मच्छरों के बारें में और नहीं जानना हैं"

    }, {"मुझे भैंस अच्छे लगते हैं",
            "मुझे सच में भैंस अच्छे लगते हैं",
            "मुझे भैंसों के बारें में जानना हैं",
            "मुझे सच में भैंसों के बारें में जानना हैं",
            "मुझे भैंसों के बारें में और जानना हैं",
            "मुझे सच में भैंसों के बारें में थोड़ा और  जानना हैं",
            "मुझे भैंस अच्छे नहीं लगते हैं",
            "मुझे सच में भैंस अच्छे नहीं लगते हैं",
            "मुझे भैंसों के बारें में नहीं जानना हैं",
            "मुझे सच में भैंसों के बारें में नहीं जानना हैं",
            "मुझे भैंसों के बारें में और नहीं जानना हैं",
            "मुझे सच में भैंसों के बारें में और नहीं जानना हैं"

    }, {"मुझे चूहें अच्छे लगते हैं",
            "मुझे सच में चूहें  अच्छे लगते हैं",
            "मुझे चूहों के बारें में जानना हैं",
            "मुझे सच में चूहों के बारें में जानना हैं",
            "मुझे चूहों के बारें में और जानना हैं",
            "मुझे सच में चूहों के बारें में थोड़ा और जानना हैं",
            "मुझे चूहें अच्छे नहीं लगते हैं",
            "मुझे सच में चूहें अच्छे नहीं लगते हैं",
            "मुझे चूहों के बारें में नहीं जानना हैं",
            "मुझे सच में चूहों के बारें में नहीं जानना हैं",
            "मुझे चूहों के बारें में और नहीं जानना हैं",
            "मुझे सच में चूहों के बारें में और नहीं जानना हैं"

    }, {"मुझे साँप अच्छे लगते हैं",
            "मुझे सच में साँप अच्छे लगते हैं",
            "मुझे साँपों के बारें में जानना हैं",
            "मुझे सच में साँपों के बारें में जानना हैं",
            "मुझे साँपों के बारें में और जानना हैं",
            "मुझे सच में साँपों के बारें में थोड़ा और जानना हैं",
            "मुझे साँप अच्छे नहीं लगते हैं",
            "मुझे सच में साँप अच्छे नहीं लगते हैं",
            "मुझे साँपों के बारें में नहीं जानना हैं",
            "मुझे सच में साँपों के बारें में नहीं जानना हैं",
            "मुझे साँपों के बारें में और नहीं जानना हैं",
            "मुझे सच में साँपों के बारें में और नहीं जानना हैं"

    }, {"मुझे मगरमच्छ अच्छे लगते हैं",
            "मुझे सच में मगरमच्छ  अच्छे लगते हैं",
            "मुझे मगरमच्छ के बारें में जानना हैं",
            "मुझे सच में मगरमच्छ के बारें में जानना हैं",
            "मुझे मगरमच्छ के बारें में और जानना हैं",
            "मुझे सच में मगरमच्छ के बारें में थोड़ा और  जानना हैं",
            "मुझे मगरमच्छ अच्छे नहीं लगते हैं",
            "मुझे सच में मगरमच्छ अच्छे नहीं लगते हैं",
            "मुझे मगरमच्छ के बारें में नहीं जानना हैं",
            "मुझे सच में मगरमच्छ के बारें में नहीं जानना हैं",
            "मुझे मगरमच्छ के बारें में और नहीं जानना हैं",
            "मुझे सच में मगरमच्छ के बारें में और नहीं जानना हैं"

    }, {"मुझे मधुमक्खीयाँ अच्छी लगती हैं",
            "मुझे सच में मधुमक्खीयाँ  अच्छी लगती हैं",
            "मुझे मधुमक्खीयों के बारें में जानना हैं",
            "मुझे सच में मधुमक्खीयों के बारें में जानना हैं",
            "मुझे मधुमक्खीयों के बारें में और जानना हैं",
            "मुझे सच में मधुमक्खीयों के बारें में थोड़ा और  जानना हैं",
            "मुझे मधुमक्खीयाँ अच्छी नहीं लगती हैं",
            "मुझे सच में मधुमक्खीयाँ अच्छी नहीं लगती हैं",
            "मुझे मधुमक्खीयों के बारें में नहीं जानना हैं",
            "मुझे सच में मधुमक्खीयों के बारें में नहीं जानना हैं",
            "मुझे मधुमक्खीयों के बारें में और नहीं जानना हैं",
            "मुझे सच में मधुमक्खीयों के बारें में और नहीं जानना हैं"

    }, {"मुझे दरियाई घोड़े अच्छे लगते हैं",
            "मुझे सच में दरियाई घोड़े अच्छे लगते हैं",
            "मुझे दरियाई घोड़ों के बारें में जानना हैं",
            "मुझे सच में दरियाई घोड़ों के बारें में जानना हैं",
            "मुझे दरियाई घोड़ों के बारें में और जानना हैं",
            "मुझे सच में दरियाई घोड़ों के बारें में थोड़ा और  जानना हैं",
            "मुझे दरियाई घोड़े अच्छे नहीं लगते हैं",
            "मुझे सच में दरियाई घोड़े अच्छे नहीं लगते हैं",
            "मुझे दरियाई घोड़ों के बारें में नहीं जानना हैं",
            "मुझे सच में दरियाई घोड़ों के बारें में नहीं जानना हैं",
            "मुझे दरियाई घोड़ें के बारें में और नहीं जानना हैं",
            "मुझे सच में दरियाई घोड़ों के बारें में और नहीं जानना हैं"

    }, {"मुझे गेन्डे  अच्छे लगते हैं",
            "मुझे सच में गेन्डे अच्छे लगते हैं",
            "मुझे गेन्डो के बारें में जानना हैं",
            "मुझे सच में गेन्डो के बारें में जानना हैं",
            "मुझे गेन्डो के बारें में और जानना हैं",
            "मुझे सच में गेन्डो के बारें में थोड़ा और  जानना हैं",
            "मुझे गेन्डे अच्छे नहीं लगते हैं",
            "मुझे सच में गेन्डे अच्छे नहीं लगते हैं",
            "मुझे गेन्डो के बारें में नहीं जानना हैं",
            "मुझे सच में गेन्डो के बारें में नहीं जानना हैं",
            "मुझे गेन्डो के बारें में और नहीं जानना हैं",
            "मुझे सच में गेन्डो के बारें में और नहीं जानना हैं"

    }, {"मुझे मछलीयाँ अच्छी लगती हैं",
            "मुझे सच में मछलीयाँ  अच्छी लगती हैं",
            "मुझे मछलीयों के बारें में जानना हैं",
            "मुझे सच में मछलीयों के बारें में जानना हैं",
            "मुझे मछलीयों के बारें में और जानना हैं",
            "मुझे सच में मछलीयों के बारें में थोड़ा और  जानना हैं",
            "मुझे मछलीयाँ अच्छी नहीं लगती हैं",
            "मुझे सच में मछलीयाँ अच्छी नहीं लगती हैं",
            "मुझे मछलीयों के बारें में नहीं जानना हैं",
            "मुझे सच में मछलीयों के बारें में नहीं जानना हैं",
            "मुझे मछलीयों के बारें में और नहीं जानना हैं",
            "मुझे सच में मछलीयों के बारें में और नहीं जानना हैं"

    }, {"मुझे penguin अच्छे लगते हैं",
            "मुझे सच में penguin अच्छे लगते हैं",
            "मुझे penguin के बारें में जानना हैं",
            "मुझे सच में penguin के बारें में जानना हैं",
            "मुझे penguin के बारें में और जानना हैं",
            "मुझे सच में penguin के बारें में थोड़ा और  जानना हैं",
            "मुझे penguin अच्छे नहीं लगते हैं",
            "मुझे सच में penguin अच्छे नहीं लगते हैं",
            "मुझे penguin के बारें में नहीं जानना हैं",
            "मुझे सच में penguin के बारें में नहीं जानना हैं",
            "मुझे penguin के बारें में और नहीं जानना हैं",
            "मुझे सच में penguin के बारें में और नहीं जानना हैं"

    }, {"मुझे सील अच्छे लगते हैं",
            "मुझे सच में सील अच्छे लगते हैं",
            "मुझे सील के बारें में जानना हैं",
            "मुझे सच में सील के बारें में जानना हैं",
            "मुझे सील के बारें में और जानना हैं",
            "मुझे सच में सील के बारें में थोड़ा और  जानना हैं",
            "मुझे सील अच्छे नहीं लगते हैं",
            "मुझे सच में सील अच्छे नहीं लगते हैं",
            "मुझे सील के बारें में नहीं जानना हैं",
            "मुझे सच में सील के बारें में नहीं जानना हैं",
            "मुझे सील के बारें में और नहीं जानना हैं",
            "मुझे सच में सील के बारें में और नहीं जानना हैं"

    }, {"मुझे डॉल्फिन अच्छे लगते हैं",
            "मुझे सच में डॉल्फिन अच्छे लगते हैं",
            "मुझे डॉल्फिन के बारें में जानना हैं",
            "मुझे सच में डॉल्फिन के बारें में जानना हैं",
            "मुझे डॉल्फिन के बारें में और जानना हैं",
            "मुझे सच में डॉल्फिन के बारें में थोड़ा और  जानना हैं",
            "मुझे डॉल्फिन अच्छे नहीं लगते हैं",
            "मुझे सच में डॉल्फिन अच्छे नहीं लगते हैं",
            "मुझे डॉल्फिन के बारें में नहीं जानना हैं",
            "मुझे सच में डॉल्फिन के बारें में नहीं जानना हैं",
            "मुझे डॉल्फिन के बारें में और नहीं जानना हैं",
            "मुझे सच में डॉल्फिन के बारें में और नहीं जानना हैं"

    }, {"मुझे व्हेल अच्छे लगते हैं",
            "मुझे सच में व्हेल अच्छे लगते हैं",
            "मुझे व्हेल के बारें में जानना हैं",
            "मुझे सच में व्हेल के बारें में जानना हैं",
            "मुझे व्हेल के बारें में और जानना हैं",
            "मुझे सच में व्हेल के बारें में थोड़ा और  जानना हैं",
            "मुझे व्हेल अच्छे नहीं लगते हैं",
            "मुझे सच में व्हेल अच्छे नहीं लगते हैं",
            "मुझे व्हेल के बारें में नहीं जानना हैं",
            "मुझे सच में व्हेल के बारें में नहीं जानना हैं",
            "मुझे व्हेल के बारें में और नहीं जानना हैं",
            "मुझे सच में व्हेल के बारें में और नहीं जानना हैं"

    }, {"मुझे शार्क अच्छे लगते हैं",
            "मुझे सच में शार्क अच्छे लगते हैं",
            "मुझे शार्क के बारें में जानना हैं",
            "मुझे सच में शार्क के बारें में जानना हैं",
            "मुझे शार्क के बारें में और जानना हैं",
            "मुझे सच में शार्क के बारें में थोड़ा और  जानना हैं",
            "मुझे शार्क अच्छे नहीं लगते हैं",
            "मुझे सच में शार्क अच्छे नहीं लगते हैं",
            "मुझे शार्क के बारें में नहीं जानना हैं",
            "मुझे सच में शार्क के बारें में नहीं जानना हैं",
            "मुझे शार्क के बारें में और नहीं जानना हैं",
            "मुझे सच में शार्क के बारें में और नहीं जानना हैं"

    }, {"मुझे कछुएं अच्छे लगते हैं",
            "मुझे सच में कछुएं अच्छे लगते हैं",
            "मुझे कछुओं के बारें में जानना हैं",
            "मुझे सच में कछुओं के बारें में जानना हैं",
            "मुझे कछुओं के बारें में और जानना हैं",
            "मुझे सच में कछुओं के बारें में थोड़ा और  जानना हैं",
            "मुझे कछुएं अच्छे नहीं लगते हैं",
            "मुझे सच में कछुएं अच्छे नहीं लगते हैं",
            "मुझे कछुओं के बारें में नहीं जानना हैं",
            "मुझे सच में कछुओं के बारें में नहीं जानना हैं",
            "मुझे कछुओं के बारें में और नहीं जानना हैं",
            "मुझे सच में कछुओं के बारें में और नहीं जानना हैं"

    }, {"मुझे चिड़ियाँ अच्छी लगती हैं",
            "मुझे सच में चिड़ियाँ  अच्छी लगती हैं",
            "मुझे चिड़ियों के बारें में जानना हैं",
            "मुझे सच में चिड़ियों के बारें में जानना हैं",
            "मुझे चिड़ियों के बारें में और जानना हैं",
            "मुझे सच में चिड़ियों के बारें में थोड़ा और  जानना हैं",
            "मुझे चिड़ियाँ अच्छी नहीं लगती हैं",
            "मुझे सच में चिड़ियाँ अच्छी नहीं लगती हैं",
            "मुझे चिड़ियों के बारें में नहीं जानना हैं",
            "मुझे सच में चिड़ियों के बारें में नहीं जानना हैं",
            "मुझे चिड़ियों के बारें में और नहीं जानना हैं",
            "मुझे सच में चिड़ियों के बारें में और नहीं जानना हैं"

    }, {"मुझे गरुड़ अच्छे लगते हैं",
            "मुझे सच में गरुड़ अच्छे लगते हैं",
            "मुझे गरुड़ के बारें में जानना हैं",
            "मुझे सच में गरुड़ के बारें में जानना हैं",
            "मुझे गरुड़ के बारें में और जानना हैं",
            "मुझे सच में गरुड़ के बारें में थोड़ा और  जानना हैं",
            "मुझे गरुड़ अच्छे नहीं लगते हैं",
            "मुझे सच में गरुड़ अच्छे नहीं लगते हैं",
            "मुझे गरुड़ के बारें में नहीं जानना हैं",
            "मुझे सच में गरुड़ के बारें में नहीं जानना हैं",
            "मुझे गरुड़ के बारें में और नहीं जानना हैं",
            "मुझे सच में गरुड़ के बारें में और नहीं जानना हैं"

    }, {"मुझे हॉक अच्छे लगते हैं",
            "मुझे सच में हॉक अच्छे लगते हैं",
            "मुझे हॉक के बारें में जानना हैं",
            "मुझे सच में हॉक के बारें में जानना हैं",
            "मुझे हॉक के बारें में और जानना हैं",
            "मुझे सच में हॉक के बारें में थोड़ा और  जानना हैं",
            "मुझे हॉक अच्छे नहीं लगते हैं",
            "मुझे सच में हॉक अच्छे नहीं लगते हैं",
            "मुझे हॉक के बारें में नहीं जानना हैं",
            "मुझे सच में हॉक के बारें में नहीं जानना हैं",
            "मुझे हॉक के बारें में और नहीं जानना हैं",
            "मुझे सच में हॉक के बारें में और नहीं जानना हैं"

    }, {"मुझे गिद्ध अच्छे लगते हैं",
            "मुझे सच में गिद्ध अच्छे लगते हैं",
            "मुझे गिद्धों के बारें में जानना हैं",
            "मुझे सच में गिद्धों के बारें में जानना हैं",
            "मुझे गिद्धों के बारें में और जानना हैं",
            "मुझे सच में गिद्धों के बारें में थोड़ा और  जानना हैं",
            "मुझे गिद्ध अच्छे नहीं लगते हैं",
            "मुझे सच में गिद्ध अच्छे नहीं लगते हैं",
            "मुझे गिद्धों के बारें में नहीं जानना हैं",
            "मुझे सच में गिद्धों के बारें में नहीं जानना हैं",
            "मुझे गिद्धों के बारें में और नहीं जानना हैं",
            "मुझे सच में गिद्धों के बारें में और नहीं जानना हैं"

    }}, {{"मेरा सिर दुःख नहीं रहा हैं, और मुझे सोचने में कोई तकलीफ नहीं हैं",
            "मेरा सिर सच में दुःख नहीं रहा  हैं",
            "मुझे सोचना हैं",
            "मुझे सच में सोचना हैं",
            "मुझे और सोचना हैं",
            "मुझे सच में थोड़ा और सोचना हैं",
            "मेरा सिर दुःख रहा  हैं, और मुझे सोचने में तकलीफ हो रहीं हैं",
            "मेरा सिर सच में बहुत दुःख रहा  हैं",
            "मुझे सोचना नहीं हैं",
            "मुझे सच में सोचना नहीं हैं",
            "मुझे और सोचना नहीं हैं",
            "मुझे सच में बिल्कुल सोचना नहीं हैं"

    }, {"मेरे बाल अच्छे दिख रहें हैं",
            "मेरे बाल सच में अच्छे दिख रहें हैं",
            "मुझे अपने बालों को हाथ लगाना हैं",
            "मुझे सच में अपने बालों को हाथ लगाना हैं",
            "मुझे फिर से अपने बालों को हाथ लगाना हैं",
            "मुझे सच में फिर से अपने बालों को हाथ लगाना हैं",
            "मेरे बाल अच्छे नहीं दिख रहें हैं",
            "मेरे बाल सच में अच्छे नहीं दिख रहें हैं",
            "मुझे अपने बालों को हाथ नहीं लगाना हैं",
            "मुझे सच में अपने बालों को हाथ नहीं लगाना हैं",
            "मुझे फिर से अपने बालों को हाथ नहीं लगाना हैं",
            "मुझे सच में फिर से अपने बालों को हाथ नहीं लगाना हैं"

    }, {"मेरी आँखें ठीक हैं, और मुझे अच्छी तरह से दिख रहा हैं",
            "मेरी आँखें सच में ठीक हैं",
            "मुझे देखना हैं",
            "मुझे सच में देखना हैं",
            "मुझे अच्छे से देखना हैं",
            "मुझे सच में, और, अच्छे से देखना हैं",
            "मेरी आँखें दुःख रही हैं, और मुझे  देखने में तकलीफ हो रहीं हैं",
            "मेरी आँखें सच में बहुत दुःख रही हैं",
            "मुझे देखना नहीं हैं",
            "मुझे सच में देखना नहीं हैं",
            "मुझे और देखना नहीं हैं",
            "मुझे सच में बिल्कुल देखना नहीं हैं"

    }, {"मेरी नाक ठीक हैं, और मुझे सूंगने में तकलीफ नहीं हो रहीं हैं",
            "मेरी नाक सच में ठीक हैं",
            "मुझे सूंगना हैं",
            "मुझे सच में सूंगना हैं",
            "मुझे वह चीज़ फिर से सूंगनी हैं",
            "मुझे सच में वह चीज़ फिर से सूंगनी हैं",
            "मेरी नाक दुःख रही हैं, और मुझे सूंगने में  तकलीफ हो रहीं हैं",
            "मेरी नाक सच में बहुत दुःख रही हैं",
            "मुझे सूंगना नहीं हैं",
            "मुझे सच में सूंगना नहीं हैं",
            "मुझे वह चीज़ फिर से सूंगनी नहीं हैं",
            "मुझे सच में वह चीज़ बिल्कुल सूंगनी नहीं हैं"

    }, {"मेरे कान ठीक हैं, और मुझे ठीक से सुनाई दे रहा हैं",
            "मेरे कान सच में ठीक हैं",
            "मुझे सुनना हैं",
            "मुझे सच में सुनना हैं",
            "मुझे अच्छे से सुनना हैं",
            "मुझे सच में बहुत अच्छे से सुनना हैं",
            "मेरे कान दुःख रहे हैं, और मुझे सुनने में तकलीफ हो रहीं हैं",
            "मेरे कान सच में बहुत दुःख रहे हैं",
            "मुझे सुनना नहीं हैं",
            "मुझे सच में सुनना नहीं हैं",
            "मुझे और सुनना नहीं हैं",
            "मुझे सच में कुछ और सुनना नहीं हैं"

    }, {"मेरा मुँह ठीक हैं, और मुझे बोलने में कोई  तकलीफ नहीं हैं",
            "मेरा मुँह सच में ठीक हैं",
            "मुझे बोलना हैं",
            "मुझे सच में बोलना हैं",
            "मुझे और बोलना हैं",
            "मुझे सच में कुछ और बोलना हैं",
            "मेरा मुँह दुःख रहा हैं, और मुझे बोलने में  तकलीफ हो रही हैं",
            "मेरा मुँह सच में बहुत दुःख रहा हैं",
            "मुझे बोलना नहीं हैं",
            "मुझे सच में बोलना नहीं हैं",
            "मुझे और बोलना नहीं हैं",
            "मुझे सच में कुछ भी बोलना नहीं हैं"

    }, {"मेरी जीभ ठीक हैं, और मुझे स्वाद लेने में कोई तकलीफ नहीं हैं",
            "मेरी जीभ सच में ठीक हैं",
            "मुझे स्वाद लेना हैं",
            "मुझे सच में स्वाद लेना हैं",
            "मुझे और स्वाद लेना हैं",
            "मुझे सच में थोड़ा और स्वाद लेना हैं",
            "मेरी जीभ दुःख रही हैं, और मुझे स्वाद लेने में तकलीफ हो रहीं हैं",
            "मेरी जीभ सच में बहुत दुःख रही हैं",
            "मुझे स्वाद नहीं लेना हैं",
            "मुझे सच में स्वाद नहीं लेना हैं",
            "मुझे और स्वाद नहीं लेना हैं",
            "मुझे सच में और स्वाद नहीं लेना हैं"

    }, {"मेरी गर्दन ठीक हैं,  और मुझे उसे घुमाने में कोई तकलीफ नहीं हैं",
            "मेरी गर्दन सच में ठीक हैं",
            "मुझे अपनी गर्दन घुमानी हैं",
            "मुझे सच में अपनी गर्दन घुमानी हैं",
            "मुझे अपनी गर्दन थोड़ी और घुमानी हैं",
            "मुझे सच में अपनी गर्दन थोड़ी और घुमानी हैं",
            "मेरी गर्दन दुःख रही हैं, और मुझे उसे घुमाने में तकलीफ हो रहीं हैं",
            "मेरी गर्दन सच में  बहुत दुःख रही हैं",
            "मुझे अपनी गर्दन घुमानी नहीं हैं",
            "मुझे सच में अपनी गर्दन घुमानी नहीं हैं",
            "मुझे अपनी गर्दन और घुमानी नहीं हैं",
            "मुझे सच में अपनी गर्दन बिल्कुल घुमानी नहीं हैं"

    }, {"मेरा कंधा ठीक हैं, और मुझे उसे हिलाने में कोई तकलीफ नहीं हैं",
            "मेरा कंधा सच में ठीक हैं",
            "मुझे अपना कंधा हिलाना हैं",
            "मुझे सच में अपना कंधा हिलाना हैं",
            "मुझे अपना कंधा थोड़ा और हिलाना हैं",
            "मुझे सच में अपना कंधा थोड़ा और हिलाना हैं",
            "मेरा कंधा दुःख रहा हैं, और मुझे उसे हिलाने में तकलीफ हो रहीं हैं",
            "मेरा कंधा सच में बहुत दुःख रहा हैं",
            "मुझे अपना कंधा हिलाना नहीं हैं",
            "मुझे सच में अपना कंधा हिलाना नहीं हैं",
            "मुझे अपना कंधा और हिलाना नहीं हैं",
            "मुझे सच में अपना कंधा बिल्कुल हिलाना नहीं हैं"

    }, {"मेरी कोहनी ठीक हैं, और मुझे उसे मोड़ने में कोई तकलीफ नहीं हैं",
            "मेरी कोहनी सच में ठीक हैं",
            "मुझे अपनी कोहनी मोड़नी हैं",
            "मुझे सच में अपनी कोहनी मोड़नी हैं",
            "मुझे अपनी कोहनी थोड़ी और मोड़नी हैं",
            "मुझे सच में अपनी कोहनी थोड़ी और मोड़नी हैं",
            "मेरी कोहनी दुःख रही हैं, और मुझे उसे मोड़ने में तकलीफ हो रहीं हैं",
            "मेरी कोहनी सच में बहुत दुःख रही हैं",
            "मुझे अपनी कोहनी नहीं मोड़नी हैं",
            "मुझे सच में अपनी कोहनी नहीं मोड़नी हैं",
            "मुझे अपनी कोहनी और नहीं मोड़नी हैं",
            "मुझे सच में अपनी कोहनी बिल्कुल नहीं मोड़नी हैं"

    }, {"मेरी कलाई ठीक हैं, और मुझे उसे घुमाने में कोई तकलीफ नहीं हैं",
            "मेरी कलाई सच में ठीक हैं",
            "मुझे अपनी कलाई घुमानी हैं",
            "मुझे सच में अपनी कलाई घुमानी हैं",
            "मुझे अपनी कलाई थोड़ी और घुमानी हैं",
            "मुझे सच में अपनी कलाई थोड़ी और घुमानी हैं",
            "मेरी कलाई दुःख रही हैं, और मुझे उसे घुमाने में तकलीफ हो रहीं हैं",
            "मेरी कलाई सच में  बहुत दुःख रही हैं",
            "मुझे अपनी कलाई घुमानी नहीं हैं",
            "मुझे सच में अपनी कलाई घुमानी नहीं हैं",
            "मुझे अपनी कलाई और घुमानी नहीं हैं",
            "मुझे सच में अपनी कलाई बिल्कुल घुमानी नहीं हैं"

    }, {"मेरे हाथ ठीक हैं, और मुझे काम करने में कोई तकलीफ नहीं हैं",
            "मेरे हाथ सच में ठीक हैं",
            "मुझे काम करना हैं",
            "मुझे सच में काम करना हैं",
            "मुझे और काम करना हैं",
            "मुझे सच में थोड़ा और काम करना हैं",
            "मेरे हाथ दुःख रहे हैं, और मुझे काम करने में तकलीफ हो रहीं हैं",
            "मेरे हाथ सच में बहुत दुःख रहे हैं",
            "मुझे काम नहीं करना हैं",
            "मुझे सच में काम नहीं करना हैं",
            "मुझे और काम नहीं करना हैं",
            "मुझे सच में बिल्कुल काम नहीं करना हैं"

    }, {"मेरी उंग लियां ठीक हैं, और मुझे उन्हें हिलाने में कोई तकलीफ नहीं हैं",
            "मेरी उंग लियां  सच में ठीक हैं",
            "मुझे अपनी उंग लियां  हिलानी हैं",
            "मुझे सच में अपनी उंग लियां  हिलानी हैं",
            "मुझे अपनी उंग लियां  थोड़ी और हिलानी हैं",
            "मुझे सच में अपनी उंग लियां  थोड़ी और हिलानी हैं",
            "मेरी उंग लियां  दुःख रही हैं, और मुझे उन्हें हिलाने में तकलीफ हो रहीं हैं",
            "मेरी उंग लियां  सच में बहुत दुःख रही हैं",
            "मुझे अपनी उंग लियां  हिलानी नहीं हैं",
            "मुझे सच में अपनी उंग लियां  हिलानी नहीं हैं",
            "मुझे अपनी उंग लियां  और हिलानी नहीं हैं",
            "मुझे सच में अपनी उंग लियां  बिल्कुल हिलानी नहीं हैं"

    }, {"मेरी पीठ ठीक हैं, और मुझे उसे हिलाने में कोई  तकलीफ नहीं हैं",
            "मेरी पीठ सच में ठीक हैं",
            "मुझे अपनी पीठ हिलानी हैं",
            "मुझे सच में अपनी पीठ हिलानी हैं",
            "मुझे अपनी पीठ थोड़ी और हिलानी हैं",
            "मुझे सच में अपनी पीठ थोड़ी और हिलानी हैं",
            "मेरी पीठ दुःख रही हैं, और मुझे उसे हिलाने में तकलीफ हो रहीं हैं",
            "मेरी पीठ सच में बहुत दुःख रही हैं",
            "मुझे अपनी पीठ हिलानी नहीं हैं",
            "मुझे सच में अपनी पीठ हिलानी नहीं हैं",
            "मुझे अपनी पीठ और हिलानी नहीं हैं",
            "मुझे सच में अपनी पीठ बिल्कुल हिलानी नहीं हैं"

    }, {"मेरा पेट ठीक हैं, और मुझे खाने में कोई  तकलीफ नहीं हैं",
            "मेरा पेट सच में ठीक हैं",
            "मुझे खाना हैं",
            "मुझे सच में खाना हैं",
            "मुझे और खाना हैं",
            "मुझे सच में थोड़ा और खाना हैं",
            "मेरा पेट दुःख रहा हैं, और मुझे खाने में तकलीफ हो रहीं हैं",
            "मेरा पेट सच में बहुत दुःख रहा हैं",
            "मुझे खाना नहीं हैं",
            "मुझे सच में खाना नहीं हैं",
            "मुझे और खाना नहीं हैं",
            "मुझे सच में बिल्कुल खाना नहीं हैं"

    }, {"मेरे कूल्हे का जोड़ ठीक हैं, और मुझे उसे हिलाने में कोई  तकलीफ नहीं हैं",
            "मेरे कूल्हे का जोड़ सच में ठीक हैं",
            "मुझे अपने कूल्हे का जोड़ हिलाना हैं",
            "मुझे सच में अपने कूल्हे का जोड़ हिलाना हैं",
            "मुझे अपने कूल्हे का जोड़ थोड़ा और हिलाना हैं",
            "मुझे सच में अपने कूल्हे का जोड़ थोड़ा और हिलाना हैं",
            "मेरे कूल्हे का जोड़ दुःख रहा हैं, और मुझे उसे हिलाने में तकलीफ हो रहीं हैं",
            "मेरे कूल्हे का जोड़ सच में बहुत दुःख रहा हैं",
            "मुझे अपने कूल्हे का जोड़ हिलाना नहीं हैं",
            "मुझे सच में अपने कूल्हे का जोड़ हिलाना नहीं हैं",
            "मुझे अपने कूल्हे का जोड़ और हिलाना नहीं हैं",
            "मुझे सच में अपने कूल्हे का जोड़ बिल्कुल हिलाना नहीं हैं"

    }, {"मेरा घुटना ठीक हैं, और मुझे उसे मोड़ने में कोई तकलीफ नहीं हैं",
            "मेरा घुटना सच में ठीक हैं",
            "मुझे अपना घुटना मोड़ना हैं",
            "मुझे सच में अपना घुटना मोड़ना हैं",
            "मुझे अपना घुटना थोड़ा और मोड़ना हैं",
            "मुझे सच में अपना घुटना थोड़ा और मोड़ना हैं",
            "मेरा घुटना दुःख रहा हैं, और मुझे उसे मोड़ने में तकलीफ हो रहीं हैं",
            "मेरा घुटना सच में बहुत दुःख रहा हैं",
            "मुझे अपना घुटना नहीं मोड़ना हैं",
            "मुझे सच में अपना घुटना नहीं मोड़ना हैं",
            "मुझे अपना घुटना और नहीं मोड़ना हैं",
            "मुझे सच में अपना घुटना बिल्कुल नहीं मोड़ना हैं"

    }, {"मेरी घुटिका ठीक हैं, और मुझे उसे हिलाने में कोई तकलीफ नहीं हैं",
            "मेरी घुटिका सच में ठीक हैं",
            "मुझे अपनी घुटिका हिलानी हैं",
            "मुझे सच में अपनी घुटिका हिलानी हैं",
            "मुझे अपनी घुटिका थोड़ी और हिलानी हैं",
            "मुझे सच में अपनी घुटिका थोड़ी और हिलानी हैं",
            "मेरी घुटिका दुःख रही हैं, और मुझे उसे हिलाने में तकलीफ हो रहीं हैं",
            "मेरी घुटिका सच में बहुत दुःख रही हैं",
            "मुझे अपनी घुटिका हिलानी नहीं हैं",
            "मुझे सच में अपनी घुटिका हिलानी नहीं हैं",
            "मुझे अपनी घुटिका और हिलानी नहीं हैं",
            "मुझे सच में अपनी घुटिका बिल्कुल हिलानी नहीं हैं"

    }, {"मेरे पैर ठीक हैं, और मुझे चलने में कोई तकलीफ नहीं हैं",
            "मेरे पैर सच में ठीक हैं",
            "मुझे चलना हैं",
            "मुझे सच में चलना हैं",
            "मुझे और चलना हैं",
            "मुझे सच में थोड़ा और चलना हैं",
            "मेरे पैर दुःख रहे हैं, और मुझे चलने में तकलीफ हो रहीं हैं",
            "मेरे पैर सच में बहुत दुःख रहे हैं",
            "मुझे चलना नहीं हैं",
            "मुझे सच में चलना नहीं हैं",
            "मुझे और चलना नहीं हैं",
            "मुझे सच में बिल्कुल चलना नहीं हैं"

    }, {"मेरी पैर की उंग लियां  ठीक हैं, और मुझे उन्हें हिलाने में कोई तकलीफ नहीं हैं",
            "मेरी पैर की उंग लियां  सच में ठीक हैं",
            "मुझे अपनी पैर की उंग लियां  हिलानी हैं",
            "मुझे सच में अपनी पैर की उंग लियां  हिलानी हैं",
            "मुझे अपनी पैर की उंग लियां  थोड़ी और हिलानी हैं",
            "मुझे सच में अपनी पैर की उंग लियां  थोड़ी और हिलानी हैं",
            "मेरी पैर की उंग लियां  दुःख रही हैं, और मुझे उन्हें हिलाने में तकलीफ हो रहीं हैं",
            "मेरी पैर की उंग लियां  सच में बहुत दुःख रही हैं",
            "मुझे अपनी पैर की उंग लियां  हिलानी नहीं हैं",
            "मुझे सच में अपनी पैर की उंग लियां  हिलानी नहीं हैं",
            "मुझे अपनी पैर की उंग लियां  और हिलानी नहीं हैं",
            "मुझे सच में अपनी पैर की उंग लियां  बिल्कुल हिलानी नहीं हैं"

    }}, {{"मुझे रात को सोने के पहले कहानीयाँ पढ़ना अच्छा लगता हैं",
            "मुझे सच में रात को सोने के पहले कहानीयाँ पढ़ना अच्छा लगता हैं",
            "मुझे रात को सोने के पहले कहानीयाँ पढ़नी हैं",
            "मुझे सच में रात को सोने के पहले कहानीयाँ पढ़नी हैं",
            "मुझे और कहानीयाँ पढ़नी हैं",
            "मुझे सच में थोड़ी और कहानीयाँ पढ़नी हैं",
            "मुझे रात को सोने के पहले कहानीयाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में रात को सोने के पहले कहानीयाँ पढ़ना अच्छा नहीं लगता हैं",
            "मुझे रात को सोने के पहले कहानीयाँ नहीं पढ़नी हैं",
            "मुझे सच में रात को सोने के पहले कहानीयाँ नहीं पढ़नी हैं",
            "मुझे और कहानीयाँ नहीं पढ़नी हैं",
            "मुझे सच में और कहानीयाँ नहीं पढ़नी हैं"

    }, {"मुझे हास्यमय किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में हास्यमय किताबें पढ़ना अच्छा लगता हैं",
            "मुझे हास्यमय किताबें पढ़नी हैं",
            "मुझे सच में हास्यमय किताबें पढ़नी हैं",
            "मुझे और हास्यमय किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और हास्यमय किताबें पढ़नी हैं",
            "मुझे हास्यमय किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में हास्यमय किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे हास्यमय किताबें नहीं पढ़नी हैं",
            "मुझे सच में हास्यमय किताबें नहीं पढ़नी हैं",
            "मुझे और हास्यमय किताबें नहीं पढ़नी हैं",
            "मुझे सच में और हास्यमय किताबें नहीं पढ़नी हैं"

    }, {"मुझे काव्यमय किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में काव्यमय किताबें पढ़ना अच्छा लगता हैं",
            "मुझे काव्यमय किताबें पढ़नी हैं",
            "मुझे सच में काव्यमय किताबें पढ़नी हैं",
            "मुझे और काव्यमय किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और काव्यमय किताबें पढ़नी हैं",
            "मुझे काव्यमय किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में काव्यमय किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे काव्यमय किताबें नहीं पढ़नी हैं",
            "मुझे सच में काव्यमय किताबें नहीं पढ़नी हैं",
            "मुझे और काव्यमय किताबें नहीं पढ़नी हैं",
            "मुझे सच में और काव्यमय किताबें नहीं पढ़नी हैं"

    }, {"मुझे  चित्र कला की किताबें अच्छी लगती हैं",
            "मुझे सच में  चित्र कला की किताबें अच्छी लगती हैं",
            "मुझे  चित्र कला की किताबें चाहिए",
            "मुझे सच में  चित्र कला की किताबें चाहिए",
            "मुझे और  चित्र कला की किताबें चाहिए",
            "मुझे सच में थोड़ी और  चित्र कला की किताबें चाहिए",
            "मुझे  चित्र कला की किताबें अच्छी नहीं लगती हैं",
            "मुझे सच में  चित्र कला की किताबें अच्छी नहीं लगती हैं",
            "मुझे  चित्र कला की किताबें नहीं चाहिए",
            "मुझे सच में  चित्र कला की किताबें नहीं चाहिए",
            "मुझे और  चित्र कला की किताबें नहीं चाहिए",
            "मुझे सच में और  चित्र कला की किताबें नहीं चाहिए"

    }, {"मुझे कहानियों की किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में कहानियों की किताबें पढ़ना अच्छा लगता हैं",
            "मुझे कहानियों की किताबें पढ़नी हैं",
            "मुझे सच में कहानियों की किताबें पढ़नी हैं",
            "मुझे और कहानियों की किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और कहानियों की किताबें पढ़नी हैं",
            "मुझे कहानियों की किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में कहानियों की किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे कहानियों की किताबें नहीं पढ़नी हैं",
            "मुझे सच में कहानियों की किताबें नहीं पढ़नी हैं",
            "मुझे और कहानियों की किताबें नहीं पढ़नी हैं",
            "मुझे सच में और कहानियों की किताबें नहीं पढ़नी हैं"

    }, {"मुझे चित्रों की किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में चित्रों की किताबें पढ़ना अच्छा लगता हैं",
            "मुझे चित्रों की किताबें पढ़नी हैं",
            "मुझे सच में चित्रों की किताबें पढ़नी हैं",
            "मुझे और चित्रों की किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और चित्रों की किताबें पढ़नी हैं",
            "मुझे चित्रों की किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में चित्रों की किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे चित्रों की किताबें नहीं पढ़नी हैं",
            "मुझे सच में चित्रों की किताबें नहीं पढ़नी हैं",
            "मुझे और चित्रों की किताबें नहीं पढ़नी हैं",
            "मुझे सच में और चित्रों की किताबें नहीं पढ़नी हैं"

    }, {"मुझे जासूसी किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में जासूसी किताबें पढ़ना अच्छा लगता हैं",
            "मुझे जासूसी किताबें पढ़नी हैं",
            "मुझे सच में जासूसी किताबें पढ़नी हैं",
            "मुझे और जासूसी किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और जासूसी किताबें पढ़नी हैं",
            "मुझे जासूसी किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में जासूसी किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे जासूसी किताबें नहीं पढ़नी हैं",
            "मुझे सच में जासूसी किताबें नहीं पढ़नी हैं",
            "मुझे और जासूसी किताबें नहीं पढ़नी हैं",
            "मुझे सच में और जासूसी किताबें नहीं पढ़नी हैं"

    }, {"मुझे साहसी किताबें पढ़ना अच्छा लगता हैं",
            "मुझे सच में साहसी किताबें पढ़ना अच्छा लगता हैं",
            "मुझे साहसी किताबें पढ़नी हैं",
            "मुझे सच में साहसी किताबें पढ़नी हैं",
            "मुझे और साहसी किताबें पढ़नी हैं",
            "मुझे सच में थोड़ी और साहसी किताबें पढ़नी हैं",
            "मुझे साहसी किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में साहसी किताबें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे साहसी किताबें नहीं पढ़नी हैं",
            "मुझे सच में साहसी किताबें नहीं पढ़नी हैं",
            "मुझे और साहसी किताबें नहीं पढ़नी हैं",
            "मुझे सच में और साहसी किताबें नहीं पढ़नी हैं"

    }, {"मुझे अपनी पाठशाला की नोटबुक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में अपनी पाठशाला की नोटबुक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे अपनी पाठशाला की नोटबुक चाहिए",
            "मुझे सच में अपनी पाठशाला की नोटबुक का इस्तमाल करना हैं",
            "मुझे कुछ और पाठशाला की नोटबुक चाहिए",
            "मुझे सच में कुछ और पाठशाला की नोटबुक चाहिए",
            "मुझे अपनी पाठशाला की नोटबुक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठशाला की नोटबुक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे अपनी पाठशाला की नोटबुक नहीं चाहिए",
            "मुझे सच में अपनी पाठशाला की नोटबुक का इस्तमाल नहीं करना हैं",
            "मुझे और पाठशाला की नोटबुक नहीं चाहिए",
            "मुझे सच में और पाठशाला की नोटबुक नहीं चाहिए"

    }, {"मुझे गणित सीखना  अच्छा लगता हैं",
            "मुझे सच में गणित सीखना  अच्छा लगता हैं",
            "मुझे अपनी गणित की किताब चाहिए",
            "मुझे सच में अपनी गणित की किताब चाहिए",
            "मुझे और समय के लिए गणित सीखना  हैं",
            "मुझे सच में थोड़ी और गणित की किताबें चाहिए",
            "मुझे गणित सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में गणित सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी गणित की किताब नहीं चाहिए",
            "मुझे सच में अपनी गणित की किताब नहीं चाहिए",
            "मुझे और समय के लिए गणित नहीं सीखना  हैं",
            "मुझे सच में और गणित की किताबें नहीं चाहिए"

    }, {"मुझे विज्ञान विषय सीखना  अच्छा लगता हैं",
            "मुझे सच में विज्ञान विषय सीखना  अच्छा लगता हैं",
            "मुझे अपनी विज्ञान की किताब चाहिए",
            "मुझे सच में अपनी विज्ञान की किताब चाहिए",
            "मुझे और समय के लिए विज्ञान विषय सीखना  हैं",
            "मुझे सच में थोड़ी और विज्ञान की किताबें चाहिए",
            "मुझे विज्ञान विषय सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में विज्ञान विषय सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी विज्ञान की किताब नहीं चाहिए",
            "मुझे सच में अपनी विज्ञान की किताब नहीं चाहिए",
            "मुझे और समय के लिए विज्ञान विषय नहीं सीखना  हैं",
            "मुझे सच में और विज्ञान  की किताबें नहीं चाहिए"

    }, {"मुझे इतिहास सीखना  अच्छा लगता हैं",
            "मुझे सच में इतिहास सीखना  अच्छा लगता हैं",
            "मुझे अपनी इतिहास की किताब चाहिए",
            "मुझे सच में अपनी इतिहास की किताब चाहिए",
            "मुझे और समय के लिए इतिहास सीखना  हैं",
            "मुझे सच में थोड़ी और इतिहास की किताबें चाहिए",
            "मुझे इतिहास सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में इतिहास सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी इतिहास की किताब नहीं चाहिए",
            "मुझे सच में अपनी इतिहास की किताब नहीं चाहिए",
            "मुझे और समय के लिए इतिहास नहीं सीखना  हैं",
            "मुझे सच में और इतिहास की किताबें नहीं चाहिए"

    }, {"मुझे भूगोल सीखना  अच्छा लगता हैं",
            "मुझे सच में भूगोल सीखना  अच्छा लगता हैं",
            "मुझे अपनी भूगोल की किताब चाहिए",
            "मुझे सच में अपनी भूगोल की किताब चाहिए",
            "मुझे और समय के लिए भूगोल सीखना  हैं",
            "मुझे सच में थोड़ी और भूगोल की किताबें चाहिए",
            "मुझे भूगोल सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में भूगोल सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी भूगोल की किताब नहीं चाहिए",
            "मुझे सच में अपनी भूगोल की किताब नहीं चाहिए",
            "मुझे और समय के लिए भूगोल नहीं सीखना  हैं",
            "मुझे सच में और भूगोल की किताबें नहीं चाहिए"

    }, {"मुझे सामाजिक अध्ययन विषय अच्छा लगता हैं",
            "मुझे सच में सामाजिक अध्ययन विषय अच्छा लगता हैं",
            "मुझे अपनी सामाजिक अध्ययन की किताब चाहिए",
            "मुझे सच में अपनी सामाजिक अध्ययन की किताब चाहिए",
            "मुझे और समय के लिए सामाजिक अध्ययन विषय सीखना  हैं",
            "मुझे सच में थोड़ी और सामाजिक अध्ययन की किताबें चाहिए",
            "मुझे सामाजिक अध्ययन विषय अच्छा नहीं लगता हैं",
            "मुझे सच में सामाजिक अध्ययन विषय अच्छा नहीं लगता हैं",
            "मुझे अपनी सामाजिक अध्ययन की किताब नहीं चाहिए",
            "मुझे सच में अपनी सामाजिक अध्ययन की किताब नहीं चाहिए",
            "मुझे और समय के लिए सामाजिक अध्ययन विषय नहीं सीखना  हैं",
            "मुझे सच में और सामाजिक अध्ययन की किताबें नहीं चाहिए"

    }, {"मुझे अंग्रेज़ी  सीखना  अच्छा लगता हैं",
            "मुझे सच में अंग्रेज़ी  सीखना  अच्छा लगता हैं",
            "मुझे अपनी अंग्रेज़ी  की किताब चाहिए",
            "मुझे सच में अपनी अंग्रेज़ी  की किताब चाहिए",
            "मुझे और समय के लिए अंग्रेज़ी  सीखना  हैं",
            "मुझे सच में थोड़ी और अंग्रेज़ी  की किताबें चाहिए",
            "मुझे अंग्रेज़ी  सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में अंग्रेज़ी  सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी अंग्रेज़ी  की किताब नहीं चाहिए",
            "मुझे सच में अपनी अंग्रेज़ी  की किताब नहीं चाहिए",
            "मुझे और समय के लिए अंग्रेज़ी  नहीं सीखना  हैं",
            "मुझे सच में और अंग्रेज़ी  की किताबें नहीं चाहिए"

    }, {"मुझे हिंदी सीखना  अच्छा लगता हैं",
            "मुझे सच में हिंदी सीखना  अच्छा लगता हैं",
            "मुझे अपनी हिंदी की किताब चाहिए",
            "मुझे सच में अपनी हिंदी की किताब चाहिए",
            "मुझे और समय के लिए हिंदी सीखना  हैं",
            "मुझे सच में थोड़ी और हिंदी की किताबें चाहिए",
            "मुझे हिंदी सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में हिंदी सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी हिंदी की किताब नहीं चाहिए",
            "मुझे सच में अपनी हिंदी की किताब नहीं चाहिए",
            "मुझे और समय के लिए हिंदी नहीं सीखना  हैं",
            "मुझे सच में और हिंदी की किताबें नहीं चाहिए"},

             {"मुझे मराठी सीखना  अच्छा लगता हैं",
            "मुझे सच में मराठी सीखना  अच्छा लगता हैं",
            "मुझे अपनी मराठी की किताब चाहिए",
            "मुझे सच में अपनी मराठी की किताब चाहिए",
            "मुझे और समय के लिए मराठी सीखना  हैं",
            "मुझे सच में थोड़ी और मराठी की किताबें चाहिए",
            "मुझे मराठी सीखना  अच्छा नहीं लगता हैं",
            "मुझे सच में मराठी सीखना  अच्छा नहीं लगता हैं",
            "मुझे अपनी मराठी की किताब नहीं चाहिए",
            "मुझे सच में अपनी मराठी की किताब नहीं चाहिए",
            "मुझे और समय के लिए मराठी नहीं सीखना  हैं",
            "मुझे सच में और मराठी की किताबें नहीं चाहिए"

    }, {"मुझे अपनी पाठ्यपुस्तकें पढ़ना अच्छा लगता हैं",
            "मुझे सच में अपनी पाठ्यपुस्तकें पढ़ना अच्छा लगता हैं",
            "मुझे अपना पाठ्यपुस्तक चाहिए",
            "मुझे सच में अपना पाठ्यपुस्तक पढ़ना हैं",
            "मुझे, और पाठ्यपुस्तकें पढ़नी हैं",
            "मुझे सच में थोड़ी और पाठ्यपुस्तकें पढ़नी हैं",
            "मुझे अपनी पाठ्यपुस्तकें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठ्यपुस्तकें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे अपना पाठ्यपुस्तक नहीं चाहिए",
            "मुझे सच में अपना पाठ्यपुस्तक नहीं पढ़ना हैं",
            "मुझे और पाठ्यपुस्तकें नहीं पढ़नी हैं",
            "मुझे सच में, और पाठ्यपुस्तकें नहीं पढ़नी हैं"

    },{"मुझे अपनी पसंदीदा किताब पढ़ना अच्छा लगता हैं",
            "मुझे सच में अपनी पसंदीदा किताब पढ़ना अच्छा लगता हैं",
            "मुझे अपनी पसंदीदा किताब पढ़नी हैं",
            "मुझे सच में अपनी पसंदीदा किताब पढ़नी हैं",
            "मुझे फ़िर से अपनी पसंदीदा किताब पढ़नी हैं",
            "मुझे सच में फ़िर से अपनी पसंदीदा किताब पढ़नी हैं",
            "मुझे अपनी पसंदीदा किताब पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पसंदीदा किताब पढ़ना अच्छा नहीं लगता हैं",
            "मुझे अपनी पसंदीदा किताब नहीं पढ़नी हैं",
            "मुझे सच में अपनी पसंदीदा किताब नहीं पढ़नी हैं",
            "मुझे फ़िर से अपनी पसंदीदा किताब नहीं पढ़नी हैं",
            "मुझे सच में फ़िर से अपनी पसंदीदा किताब नहीं पढ़नी हैं"

    }}, {{"मुझे काला रंग अच्छा लगता हैं",
            "मुझे सच में काला रंग अच्छा लगता हैं",
            "मुझे काला रंग चाहिए",
            "मुझे सच में काला रंग चाहिए",
            "मुझे और काला रंग चाहिए",
            "मुझे सच में थोड़ा और काला रंग चाहिए",
            "मुझे काला रंग अच्छा नहीं लगता हैं",
            "मुझे सच में काला रंग अच्छा नहीं लगता हैं",
            "मुझे काला रंग नहीं चाहिए",
            "मुझे सच में काला रंग नहीं चाहिए",
            "मुझे और काला रंग नहीं चाहिए",
            "मुझे सच में काला रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे नीला रंग अच्छा लगता हैं",
            "मुझे सच में नीला रंग अच्छा लगता हैं",
            "मुझे नीला रंग चाहिए",
            "मुझे सच में नीला रंग चाहिए",
            "मुझे और नीला रंग चाहिए",
            "मुझे सच में थोड़ा और नीला रंग चाहिए",
            "मुझे नीला रंग अच्छा नहीं लगता हैं",
            "मुझे सच में नीला रंग अच्छा नहीं लगता हैं",
            "मुझे नीला रंग नहीं चाहिए",
            "मुझे सच में नीला रंग नहीं चाहिए",
            "मुझे और नीला रंग नहीं चाहिए",
            "मुझे सच में नीला रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे भूरा रंग अच्छा लगता हैं",
            "मुझे सच में भूरा रंग अच्छा लगता हैं",
            "मुझे भूरा रंग चाहिए",
            "मुझे सच में भूरा रंग चाहिए",
            "मुझे और भूरा रंग चाहिए",
            "मुझे सच में थोड़ा और भूरा रंग चाहिए",
            "मुझे भूरा रंग अच्छा नहीं लगता हैं",
            "मुझे सच में भूरा रंग अच्छा नहीं लगता हैं",
            "मुझे भूरा रंग नहीं चाहिए",
            "मुझे सच में भूरा रंग नहीं चाहिए",
            "मुझे और भूरा रंग नहीं चाहिए",
            "मुझे सच में भूरा रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे हरा रंग अच्छा लगता हैं",
            "मुझे सच में हरा  रंग अच्छा लगता हैं",
            "मुझे हरा रंग चाहिए",
            "मुझे सच में हरा रंग चाहिए",
            "मुझे और हरा रंग चाहिए",
            "मुझे सच में थोड़ा और हरा रंग चाहिए",
            "मुझे हरा रंग अच्छा नहीं लगता हैं",
            "मुझे सच में हरा रंग अच्छा नहीं लगता हैं",
            "मुझे हरा रंग नहीं चाहिए",
            "मुझे सच में हरा रंग नहीं चाहिए",
            "मुझे और हरा रंग नहीं चाहिए",
            "मुझे सच में हरा रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे लाल रंग अच्छा लगता हैं",
            "मुझे सच में लाल रंग अच्छा लगता हैं",
            "मुझे लाल रंग चाहिए",
            "मुझे सच में लाल रंग चाहिए",
            "मुझे और लाल रंग चाहिए",
            "मुझे सच में थोड़ा और लाल रंग चाहिए",
            "मुझे लाल रंग अच्छा नहीं लगता हैं",
            "मुझे सच में लाल रंग अच्छा नहीं लगता हैं",
            "मुझे लाल रंग नहीं चाहिए",
            "मुझे सच में लाल रंग नहीं चाहिए",
            "मुझे और लाल रंग नहीं चाहिए",
            "मुझे सच में लाल रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे चाँदी का रंग अच्छा लगता हैं",
            "मुझे सच में चाँदी का रंग अच्छा लगता हैं",
            "मुझे चाँदी का रंग चाहिए",
            "मुझे सच में चाँदी का रंग चाहिए",
            "मुझे और चाँदी का रंग चाहिए",
            "मुझे सच में थोड़ा और चाँदी का रंग चाहिए",
            "मुझे चाँदी का रंग अच्छा नहीं लगता हैं",
            "मुझे सच में चाँदी का रंग अच्छा नहीं लगता हैं",
            "मुझे चाँदी का रंग नहीं चाहिए",
            "मुझे सच में चाँदी का रंग नहीं चाहिए",
            "मुझे और चाँदी का रंग नहीं चाहिए",
            "मुझे सच में चाँदी का रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे सफेद रंग अच्छा लगता हैं",
            "मुझे सच में सफेद रंग अच्छा लगता हैं",
            "मुझे सफेद रंग चाहिए",
            "मुझे सच में सफेद रंग चाहिए",
            "मुझे और सफेद रंग चाहिए",
            "मुझे सच में थोड़ा और सफेद रंग चाहिए",
            "मुझे सफेद रंग अच्छा नहीं लगता हैं",
            "मुझे सच में सफेद रंग अच्छा नहीं लगता हैं",
            "मुझे सफेद रंग नहीं चाहिए",
            "मुझे सच में सफेद रंग नहीं चाहिए",
            "मुझे और सफेद रंग नहीं चाहिए",
            "मुझे सच में सफेद रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे पीला रंग अच्छा लगता हैं",
            "मुझे सच में पीला रंग अच्छा लगता हैं",
            "मुझे पीला रंग चाहिए",
            "मुझे सच में पीला रंग चाहिए",
            "मुझे और पीला रंग चाहिए",
            "मुझे सच में थोड़ा और पीला रंग चाहिए",
            "मुझे पीला रंग अच्छा नहीं लगता हैं",
            "मुझे सच में पीला रंग अच्छा नहीं लगता हैं",
            "मुझे पीला रंग नहीं चाहिए",
            "मुझे सच में पीला रंग नहीं चाहिए",
            "मुझे और पीला रंग नहीं चाहिए",
            "मुझे सच में पीला रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे सुनहरा रंग अच्छा लगता हैं",
            "मुझे सच में सुनहरा रंग अच्छा लगता हैं",
            "मुझे सुनहरा रंग चाहिए",
            "मुझे सच में सुनहरा रंग चाहिए",
            "मुझे और सुनहरा रंग चाहिए",
            "मुझे सच में थोड़ा और सुनहरा रंग चाहिए",
            "मुझे सुनहरा रंग अच्छा नहीं लगता हैं",
            "मुझे सच में सुनहरा रंग अच्छा नहीं लगता हैं",
            "मुझे सुनहरा रंग नहीं चाहिए",
            "मुझे सच में सुनहरा रंग नहीं चाहिए",
            "मुझे और सुनहरा रंग नहीं चाहिए",
            "मुझे सच में सुनहरा रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे गुलाबी रंग अच्छा लगता हैं",
            "मुझे सच में गुलाबी रंग अच्छा लगता हैं",
            "मुझे गुलाबी रंग चाहिए",
            "मुझे सच में गुलाबी रंग चाहिए",
            "मुझे और गुलाबी रंग चाहिए",
            "मुझे सच में थोड़ा और गुलाबी रंग चाहिए",
            "मुझे गुलाबी रंग अच्छा नहीं लगता हैं",
            "मुझे सच में गुलाबी रंग अच्छा नहीं लगता हैं",
            "मुझे गुलाबी रंग नहीं चाहिए",
            "मुझे सच में गुलाबी रंग नहीं चाहिए",
            "मुझे और गुलाबी रंग नहीं चाहिए",
            "मुझे सच में गुलाबी रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे नारंगी रंग अच्छा लगता हैं",
            "मुझे सच में नारंगी रंग अच्छा लगता हैं",
            "मुझे नारंगी रंग चाहिए",
            "मुझे सच में नारंगी रंग चाहिए",
            "मुझे और नारंगी रंग चाहिए",
            "मुझे सच में थोड़ा और नारंगी रंग चाहिए",
            "मुझे नारंगी रंग अच्छा नहीं लगता हैं",
            "मुझे सच में नारंगी रंग अच्छा नहीं लगता हैं",
            "मुझे नारंगी रंग नहीं चाहिए",
            "मुझे सच में नारंगी रंग नहीं चाहिए",
            "मुझे और नारंगी रंग नहीं चाहिए",
            "मुझे सच में नारंगी रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे जामुनी रंग अच्छा लगता हैं",
            "मुझे सच में जामुनी रंग अच्छा लगता हैं",
            "मुझे जामुनी रंग चाहिए",
            "मुझे सच में जामुनी रंग चाहिए",
            "मुझे और जामुनी रंग चाहिए",
            "मुझे सच में थोड़ा और जामुनी रंग चाहिए",
            "मुझे जामुनी रंग अच्छा नहीं लगता हैं",
            "मुझे सच में जामुनी रंग अच्छा नहीं लगता हैं",
            "मुझे जामुनी रंग नहीं चाहिए",
            "मुझे सच में जामुनी रंग नहीं चाहिए",
            "मुझे और जामुनी रंग नहीं चाहिए",
            "मुझे सच में जामुनी रंग बिल्कुल नहीं चाहिए"

    }, {"मुझे gray रंग अच्छा लगता हैं",
            "मुझे सच में gray रंग अच्छा लगता हैं",
            "मुझे gray रंग चाहिए",
            "मुझे सच में gray रंग चाहिए",
            "मुझे और gray रंग चाहिए",
            "मुझे सच में थोड़ा और gray रंग चाहिए",
            "मुझे gray रंग अच्छा नहीं लगता हैं",
            "मुझे सच में gray रंग अच्छा नहीं लगता हैं",
            "मुझे gray रंग नहीं चाहिए",
            "मुझे सच में gray रंग नहीं चाहिए",
            "मुझे और gray रंग नहीं चाहिए",
            "मुझे सच में gray रंग बिल्कुल नहीं चाहिए"

    }}, {{"मुझे सीधी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे सच में सीधी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे एक सीधी रेखा बनानी हैं",
            "मुझे सच में एक सीधी रेखा बनानी हैं",
            "मुझे और सीधी रेखाएं बनानी हैं",
            "मुझे सच में थोड़ी और सीधी रेखाएं बनानी हैं",
            "मुझे सीधी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में सीधी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे सीधी रेखा नहीं बनानी हैं",
            "मुझे सच में सीधी रेखा नहीं बनानी हैं",
            "मुझे और सीधी रेखाएं नहीं बनानी हैं",
            "मुझे सच में और सीधी रेखाएं नहीं बनानी हैं"

    }, {"मुझे आड़ी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे सच में आड़ी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे एक आड़ी रेखा बनानी हैं",
            "मुझे सच में एक आड़ी रेखा बनानी हैं",
            "मुझे और आड़ी रेखाएं बनानी हैं",
            "मुझे सच में थोड़ी और आड़ी रेखाएं बनानी हैं",
            "मुझे आड़ी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में आड़ी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे आड़ी रेखा नहीं बनानी हैं",
            "मुझे सच में आड़ी रेखा नहीं बनानी हैं",
            "मुझे और आड़ी रेखाएं नहीं बनानी हैं",
            "मुझे सच में और आड़ी रेखाएं नहीं बनानी हैं"

    }, {"मुझे तिरछी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे सच में तिरछी रेखाएं बनाना अच्छा लगता हैं",
            "मुझे एक तिरछी रेखा बनानी हैं",
            "मुझे सच में एक तिरछी रेखा बनानी हैं",
            "मुझे और तिरछी रेखाएं बनानी हैं",
            "मुझे सच में थोड़ी और तिरछी रेखाएं बनानी हैं",
            "मुझे तिरछी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में तिरछी रेखाएं बनाना अच्छा नहीं लगता हैं",
            "मुझे तिरछी रेखा नहीं बनानी हैं",
            "मुझे सच में तिरछी रेखा नहीं बनानी हैं",
            "मुझे और तिरछी रेखाएं नहीं बनानी हैं",
            "मुझे सच में और तिरछी रेखाएं नहीं बनानी हैं",

    }, {"मुझे गोल बनाना अच्छा लगता हैं",
            "मुझे सच में गोल बनाना अच्छा लगता हैं",
            "मुझे एक गोल बनाना हैं",
            "मुझे सच में एक गोल बनाना हैं",
            "मुझे और गोल बनाने हैं",
            "मुझे सच में थोड़े और गोल बनाने हैं",
            "मुझे गोल बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में गोल बनाना अच्छा नहीं लगता हैं",
            "मुझे गोल नहीं बनाना हैं",
            "मुझे सच में गोल नहीं बनाना हैं",
            "मुझे और गोल नहीं बनाने हैं",
            "मुझे सच में और गोल नहीं बनाने हैं"

    }, {"मुझे आयत बनाना अच्छा लगता हैं",
            "मुझे सच में आयत बनाना अच्छा लगता हैं",
            "मुझे एक आयत बनाना हैं",
            "मुझे सच में एक आयत बनाना हैं",
            "मुझे और आयत बनाने हैं",
            "मुझे सच में थोड़े और आयत बनाने हैं",
            "मुझे आयत बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में आयत बनाना अच्छा नहीं लगता हैं",
            "मुझे आयत नहीं बनाना हैं",
            "मुझे सच में आयत नहीं बनाना हैं",
            "मुझे और आयत नहीं बनाने हैं",
            "मुझे सच में और आयत नहीं बनाने हैं"

    }, {"मुझे चौकोर बनाना अच्छा लगता हैं",
            "मुझे सच में चौकोर बनाना अच्छा लगता हैं",
            "मुझे एक चौकोर बनाना हैं",
            "मुझे सच में एक चौकोर बनाना हैं",
            "मुझे और चौकोर बनाने हैं",
            "मुझे सच में थोड़े और चौकोर बनाने हैं",
            "मुझे चौकोर बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में चौकोर बनाना अच्छा नहीं लगता हैं",
            "मुझे चौकोर नहीं बनाना हैं",
            "मुझे सच में चौकोर नहीं बनाना हैं",
            "मुझे और चौकोर नहीं बनाने हैं",
            "मुझे सच में और चौकोर नहीं बनाने हैं"

    }, {"मुझे त्रिकोण बनाना अच्छा लगता हैं",
            "मुझे सच में त्रिकोण बनाना अच्छा लगता हैं",
            "मुझे एक त्रिकोण बनाना हैं",
            "मुझे सच में एक त्रिकोण बनाना हैं",
            "मुझे और त्रिकोण बनाने हैं",
            "मुझे सच में थोड़े और त्रिकोण बनाने हैं",
            "मुझे त्रिकोण बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में त्रिकोण बनाना अच्छा नहीं लगता हैं",
            "मुझे त्रिकोण नहीं बनाना हैं",
            "मुझे सच में त्रिकोण नहीं बनाना हैं",
            "मुझे और त्रिकोण नहीं बनाने हैं",
            "मुझे सच में और त्रिकोण नहीं बनाने हैं"

    }, {"मुझे तारा बनाना अच्छा लगता हैं",
            "मुझे सच में तारा बनाना अच्छा लगता हैं",
            "मुझे एक तारा बनाना हैं",
            "मुझे सच में एक तारा बनाना हैं",
            "मुझे और तारे बनाने हैं",
            "मुझे सच में थोड़े और तारे बनाने हैं",
            "मुझे तारा बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में तारा बनाना अच्छा नहीं लगता हैं",
            "मुझे तारा नहीं बनाना हैं",
            "मुझे सच में तारा नहीं बनाना हैं",
            "मुझे और तारे नहीं बनाने हैं",
            "मुझे सच में और तारे नहीं बनाने हैं"

    }, {"मुझे दिल का आकार बनाना अच्छा लगता हैं",
            "मुझे सच में दिल का आकार बनाना अच्छा लगता हैं",
            "मुझे एक दिल का आकार बनाना हैं",
            "मुझे सच में एक दिल का आकार बनाना हैं",
            "मुझे और दिल के आकार बनाने हैं",
            "मुझे सच में थोड़े और दिल के आकार बनाने हैं",
            "मुझे दिल का आकार बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में दिल का आकार बनाना अच्छा नहीं लगता हैं",
            "मुझे दिल का आकार नहीं बनाना हैं",
            "मुझे सच में दिल का आकार नहीं बनाना हैं",
            "मुझे और दिल के आकार नहीं बनाने हैं",
            "मुझे सच में और दिल के आकार नहीं बनाने हैं"

    }, {"मुझे अ समांतरभुज कोण बनाना अच्छा लगता हैं",
            "मुझे सच में अ समांतरभुज कोण बनाना अच्छा लगता हैं",
            "मुझे एक, अ समांतरभुज कोण बनाना हैं",
            "मुझे सच में एक, अ समांतरभुज कोण बनाना हैं",
            "मुझे, और, अ समांतरभुज कोण बनाने हैं",
            "मुझे सच में थोड़े, और, अ समांतरभुज कोण बनाने हैं",
            "मुझे अ समांतरभुज कोण बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अ समांतरभुज कोण बनाना अच्छा नहीं लगता हैं",
            "मुझे अ समांतरभुज कोण नहीं बनाना हैं",
            "मुझे सच में अ समांतरभुज कोण नहीं बनाना हैं",
            "मुझे, और, अ समांतरभुज कोण नहीं बनाने हैं",
            "मुझे सच में, और, अ समांतरभुज कोण नहीं बनाने हैं"

    }, {"मुझे घनाकार बनाना अच्छा लगता हैं",
            "मुझे सच में घनाकार बनाना अच्छा लगता हैं",
            "मुझे एक घनाकार बनाना हैं",
            "मुझे सच में एक घनाकार बनाना हैं",
            "मुझे और घनाकार बनाने हैं",
            "मुझे सच में थोड़े और घनाकार बनाने हैं",
            "मुझे घनाकार बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में घनाकार बनाना अच्छा नहीं लगता हैं",
            "मुझे घनाकार नहीं बनाना हैं",
            "मुझे सच में घनाकार नहीं बनाना हैं",
            "मुझे और घनाकार नहीं बनाने हैं",
            "मुझे सच में और घनाकार नहीं बनाने हैं"

    }, {"मुझे समअ चतुरभुज कोण बनाना अच्छा लगता हैं",
            "मुझे सच में समअ चतुरभुज कोण बनाना अच्छा लगता हैं",
            "मुझे एक2q समअ चतुरभुज कोण बनाना हैं",
            "मुझे सच में एक समअ चतुरभुज कोण बनाना हैं",
            "मुझे, और, समअ चतुरभुज कोण बनाने हैं",
            "मुझे सच में थोड़े, और, समअ चतुरभुज कोण बनाने हैं",
            "मुझे समअ चतुरभुज कोण बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में समअ चतुरभुज कोण बनाना अच्छा नहीं लगता हैं",
            "मुझे समअ चतुरभुज कोण नहीं बनाना हैं",
            "मुझे सच में समअ चतुरभुज कोण नहीं बनाना हैं",
            "मुझे, और, समअ चतुरभुज कोण नहीं बनाने हैं",
            "मुझे सच में, और, समअ चतुरभुज कोण नहीं बनाने हैं"

    }, {"मुझे शट कोन बनाना अच्छा लगता हैं",
            "मुझे सच में शट कोन बनाना अच्छा लगता हैं",
            "मुझे एक शट कोन बनाना हैं",
            "मुझे सच में एक शट कोन बनाना हैं",
            "मुझे और शट कोन बनाने हैं",
            "मुझे सच में थोड़े और शट कोन बनाने हैं",
            "मुझे शट कोन बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में शट कोन बनाना अच्छा नहीं लगता हैं",
            "मुझे शट कोन नहीं बनाना हैं",
            "मुझे सच में शट कोन नहीं बनाना हैं",
            "मुझे और शट कोन नहीं बनाने हैं",
            "मुझे सच में और शट कोन नहीं बनाने हैं"

    }, {"मुझे अंडाकार बनाना अच्छा लगता हैं",
            "मुझे सच में अंडाकार बनाना अच्छा लगता हैं",
            "मुझे एक अंडाकार बनाना हैं",
            "मुझे सच में एक अंडाकार बनाना हैं",
            "मुझे और अंडाकार बनाने हैं",
            "मुझे सच में थोड़े और अंडाकार बनाने हैं",
            "मुझे अंडाकार बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अंडाकार बनाना अच्छा नहीं लगता हैं",
            "मुझे अंडाकार नहीं बनाना हैं",
            "मुझे सच में अंडाकार नहीं बनाना हैं",
            "मुझे और अंडाकार नहीं बनाने हैं",
            "मुझे सच में और अंडाकार नहीं बनाने हैं"

    }, {"मुझे ईंट का आकार बनाना अच्छा लगता हैं",
            "मुझे सच में ईंट का आकार बनाना अच्छा लगता हैं",
            "मुझे एक ईंट का आकार बनाना हैं",
            "मुझे सच में एक ईंट का आकार बनाना हैं",
            "मुझे, और, ईंट के आकार बनाने हैं",
            "मुझे सच में थोड़े, और, ईंट के आकार बनाने हैं",
            "मुझे ईंट का आकार बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में ईंट का आकार बनाना अच्छा नहीं लगता हैं",
            "मुझे ईंट का आकार नहीं बनाना हैं",
            "मुझे सच में ईंट का आकार नहीं बनाना हैं",
            "मुझे, और, ईंट के आकार नहीं बनाने हैं",
            "मुझे सच में, और, ईंट के आकार नहीं बनाने हैं"

    }, {"मुझे पंच कोन बनाना अच्छा लगता हैं",
            "मुझे सच में पंच कोन बनाना अच्छा लगता हैं",
            "मुझे एक पंच कोन बनाना हैं",
            "मुझे सच में एक पंच कोन बनाना हैं",
            "मुझे और पंच कोन बनाने हैं",
            "मुझे सच में थोड़े और पंच कोन बनाने हैं",
            "मुझे पंच कोन बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में पंच कोन बनाना अच्छा नहीं लगता हैं",
            "मुझे पंच कोन नहीं बनाना हैं",
            "मुझे सच में पंच कोन नहीं बनाना हैं",
            "मुझे और पंच कोन नहीं बनाने हैं",
            "मुझे सच में और पंच कोन नहीं बनाने हैं"

    }, {"मुझे मुक्ताकार बनाना अच्छा लगता हैं",
            "मुझे सच में मुक्ताकार बनाना अच्छा लगता हैं",
            "मुझे एक मुक्ताकार बनाना हैं",
            "मुझे सच में एक मुक्ताकार बनाना हैं",
            "मुझे और मुक्ताकार बनाने हैं",
            "मुझे सच में थोड़े और मुक्ताकार बनाने हैं",
            "मुझे मुक्ताकार बनाना अच्छा नहीं लगता हैं",
            "मुझे सच में मुक्ताकार बनाना अच्छा नहीं लगता हैं",
            "मुझे मुक्ताकार नहीं बनाना हैं",
            "मुझे सच में मुक्ताकार नहीं बनाना हैं",
            "मुझे और मुक्ताकार नहीं बनाने हैं",
            "मुझे सच में और मुक्ताकार नहीं बनाने हैं"

    }}, {{"मुझे pencil का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में pencil का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक pencil चाहिए",
            "मुझे सच में एक pencil चाहिए",
            "मुझे और pencil चाहिए",
            "मुझे सच में थोड़े और pencil चाहिए",
            "मुझे pencil का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में pencil का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे pencil नहीं चाहिए",
            "मुझे सच में pencil नहीं चाहिए",
            "मुझे और pencil नहीं चाहिए",
            "मुझे सच में और pencil नहीं चाहिए"

    }, {"मुझे pen का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में pen का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक pen चाहिए",
            "मुझे सच में एक pen चाहिए",
            "मुझे और pen चाहिए",
            "मुझे सच में थोड़े और pen चाहिए",
            "मुझे pen का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में pen का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे pen नहीं चाहिए",
            "मुझे सच में pen नहीं चाहिए",
            "मुझे और pen नहीं चाहिए",
            "मुझे सच में और pen नहीं चाहिए"

    }, {"मुझे स्केल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में स्केल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक स्केल चाहिए",
            "मुझे सच में एक स्केल चाहिए",
            "मुझे फ़िर से स्केल का इस्तमाल करना हैं",
            "मुझे सच में और एक स्केल चाहिए",
            "मुझे स्केल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्केल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे स्केल नहीं चाहिए",
            "मुझे सच में स्केल नहीं चाहिए",
            "मुझे फ़िर से स्केल का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक स्केल नहीं चाहिए"

    }, {"मुझे रबर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में रबर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक रबर चाहिए",
            "मुझे सच में एक रबर चाहिए",
            "मुझे फ़िर से रबर का इस्तमाल करना हैं",
            "मुझे सच में और एक रबर चाहिए",
            "मुझे रबर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में रबर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे रबर नहीं चाहिए",
            "मुझे सच में रबर नहीं चाहिए",
            "मुझे फ़िर से रबर का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक रबर नहीं चाहिए",

    }, {"मुझे शारप्नर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में शारप्नर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक शारप्नर चाहिए",
            "मुझे सच में एक शारप्नर चाहिए",
            "मुझे फ़िर से शारप्नर का इस्तमाल करना हैं",
            "मुझे सच में और एक शारप्नर चाहिए",
            "मुझे शारप्नर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में शारप्नर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे शारप्नर नहीं चाहिए",
            "मुझे सच में शारप्नर नहीं चाहिए",
            "मुझे फ़िर से शारप्नर का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक शारप्नर नहीं चाहिए"

    }, {"मुझे क्रेयौन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में क्रेयौन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे क्रेयौन चाहिए",
            "मुझे सच में क्रेयौन चाहिए",
            "मुझे और क्रेयौन चाहिए",
            "मुझे सच में थोड़े और क्रेयौन चाहिए",
            "मुझे क्रेयौन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में क्रेयौन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे क्रेयौन नहीं चाहिए",
            "मुझे सच में क्रेयौन नहीं चाहिए",
            "मुझे और क्रेयौन नहीं चाहिए",
            "मुझे सच में और क्रेयौन नहीं चाहिए"

    }, {"मुझे कोरे कागज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कोरे कागज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे थोड़े कोरे कागज चाहिए",
            "मुझे सच में थोड़े कोरे कागज चाहिए",
            "मुझे और कोरे कागज चाहिए",
            "मुझे सच में थोड़े और कोरे कागज चाहिए",
            "मुझे कोरे कागज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कोरे कागज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कोरे कागज नहीं चाहिए",
            "मुझे सच में कोरे कागज नहीं चाहिए",
            "मुझे और कोरे कागज नहीं चाहिए",
            "मुझे सच में और कोरे कागज नहीं चाहिए"

    }, {"मुझे रंगीन कागज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में रंगीन कागज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे थोड़े रंगीन कागज चाहिए",
            "मुझे सच में थोड़े रंगीन कागज चाहिए",
            "मुझे और रंगीन कागज चाहिए",
            "मुझे सच में थोड़े और रंगीन कागज चाहिए",
            "मुझे रंगीन कागज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में रंगीन कागज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे रंगीन कागज नहीं चाहिए",
            "मुझे सच में रंगीन कागज नहीं चाहिए",
            "मुझे और रंगीन कागज नहीं चाहिए",
            "मुझे सच में और रंगीन कागज नहीं चाहिए"

    }, {"मुझे कैंची का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कैंची का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक कैंची चाहिए",
            "मुझे सच में एक कैंची चाहिए",
            "मुझे फ़िर से कैंची का इस्तमाल करना हैं",
            "मुझे सच में और एक  कैंची चाहिए",
            "मुझे कैंची का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कैंची का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कैंची नहीं चाहिए",
            "मुझे सच में कैंची नहीं चाहिए",
            "मुझे फ़िर से कैंची का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक  कैंची नहीं चाहिए"

    }, {"मुझे सीसेवाली pencil का इस्तमाल करना  अच्छा लगता हैं",
            "मुझे सच में सीसेवाली pencil का इस्तमाल करना  अच्छा लगता हैं",
            "मुझे pencil के लिए सीसा चाहिए",
            "मुझे  सच में pencil के लिए सीसा चाहिए",
            "मुझे pencil के लिए थोड़ा और सीसा चाहिए",
            "मुझे  सच में pencil के लिए थोड़ा और सीसा चाहिए",
            "मुझे सीसेवाली pencil का इस्तमाल करना  अच्छा नहीं लगता हैं",
            "मुझे सच में सीसेवाली pencil का इस्तमाल करना  अच्छा नहीं लगता हैं",
            "मुझे pencil के लिए सीसा नहीं चाहिए",
            "मुझे  सच में pencil के लिए सीसा नहीं चाहिए",
            "मुझे pencil के लिए और सीसा नहीं चाहिए",
            "मुझे  सच में pencil के लिए और सीसा चाहिए"

    }, {"मुझे कम्पास का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में कम्पास का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक कम्पास  चाहिए",
            "मुझे सच में एक कम्पास  चाहिए",
            "मुझे फ़िर से कम्पास का इस्तमाल करना हैं",
            "मुझे सच में और एक  कम्पास चाहिए",
            "मुझे कम्पास का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में कम्पास का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे कम्पास नहीं चाहिए",
            "मुझे सच में कम्पास नहीं चाहिए",
            "मुझे फ़िर से कम्पास का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक  कम्पास नहीं चाहिए",

    }, {"मुझे विभाजक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में विभाजक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक विभाजक  चाहिए",
            "मुझे सच में एक विभाजक  चाहिए",
            "मुझे फ़िर से विभाजक का इस्तमाल करना हैं",
            "मुझे सच में और एक  विभाजक चाहिए",
            "मुझे विभाजक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में विभाजक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे विभाजक नहीं चाहिए",
            "मुझे सच में विभाजक नहीं चाहिए",
            "मुझे फ़िर से विभाजक का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक विभाजक नहीं चाहिए"

    }, {"मुझे स्टेप्लर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में स्टेप्लर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक स्टेप्लर  चाहिए",
            "मुझे सच में एक स्टेप्लर  चाहिए",
            "मुझे फ़िर से स्टेप्लर का इस्तमाल करना हैं",
            "मुझे सच में और एक  स्टेप्लर चाहिए",
            "मुझे स्टेप्लर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्टेप्लर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे स्टेप्लर नहीं चाहिए",
            "मुझे सच में स्टेप्लर नहीं चाहिए",
            "मुझे फ़िर से स्टेप्लर का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक स्टेप्लर नहीं चाहिए"

    }, {"मुझे यू-पिन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में यू-पिन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक यू-पिन चाहिए",
            "मुझे सच में एक यू-पिन  चाहिए",
            "मुझे और यू-पिन चाहिए",
            "मुझे सच में थोड़े और यू-पिन चाहिए",
            "मुझे यू-पिन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में यू-पिन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे यू-पिन नहीं चाहिए",
            "मुझे सच में यू-पिन नहीं चाहिए",
            "मुझे और  यू-पिन नहीं चाहिए",
            "मुझे सच में और यू-पिन नहीं चाहिए"

    }, {"मुझे सेलो टेप का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में सेलो टेप का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सेलो टेप चाहिए",
            "मुझे सच में सेलो टेप चाहिए",
            "मुझे और सेलो टेप चाहिए",
            "मुझे सच में थोड़ा और सेलो टेप चाहिए",
            "मुझे सेलो टेप का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में सेलो टेप का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सेलो टेप नहीं चाहिए",
            "मुझे सच में सेलो टेप नहीं चाहिए",
            "मुझे और  सेलो टेप नहीं चाहिए",
            "मुझे सच में और सेलो टेप नहीं चाहिए"

    }, {"मुझे compass बॉक्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में compass बॉक्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे अपना compass बॉक्स चाहिए",
            "मुझे सच में अपना compass  बॉक्स चाहिए",
            "मुझे फ़िर से अपने  compass बॉक्स का इस्तमाल करना हैं",
            "मुझे सच में और एक  compass बॉक्स चाहिए",
            "मुझे compass बॉक्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में compass बॉक्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे अपना compass बॉक्स नहीं चाहिए",
            "मुझे सच में अपना compass बॉक्स नहीं चाहिए",
            "मुझे फ़िर से अपने compass बॉक्स का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक  compass बॉक्स नहीं चाहिए"

    }}, {{"मुझे अपनी पाठशाला की  bag अच्छी लगती हैं",
            "मुझे सच में अपनी पाठशाला की bag अच्छी लगती हैं",
            "मुझे अपनी पाठशाला की bag चाहिए",
            "मुझे सच में अपनी पाठशाला की bag चाहिए",
            "मुझे फ़िर से अपनी पाठशाला की bag चाहिए",
            "मुझे सच में फ़िर से अपनी पाठशाला की bag चाहिए",
            "मुझे अपनी पाठशाला की bag अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी पाठशाला की bag अच्छी नहीं लगती हैं",
            "मुझे अपनी पाठशाला की bag नहीं चाहिए",
            "मुझे सच में अपनी पाठशाला की bag नहीं चाहिए",
            "मुझे फ़िर से अपनी पाठशाला की bag नहीं चाहिए",
            "मुझे सच में फ़िर से अपनी पाठशाला की bag नहीं चाहिए"

    }, {"मुझे अपना खाने का डिब्बा अच्छा लगता हैं",
            "मुझे सच में अपना खाने का डिब्बा अच्छा लगता हैं",
            "मुझे अपना खाने का डिब्बा चाहिए",
            "मुझे सच में अपना खाने का डिब्बा चाहिए",
            "मुझे फ़िर से अपना खाने का डिब्बा चाहिए",
            "मुझे सच में फ़िर से अपना खाने का डिब्बा चाहिए",
            "मुझे अपना खाने का डिब्बा अच्छा नहीं लगता हैं",
            "मुझे सच में अपना खाने का डिब्बा अच्छा नहीं लगता हैं",
            "मुझे अपना खाने का डिब्बा नहीं चाहिए",
            "मुझे सच में अपना खाने का डिब्बा नहीं चाहिए",
            "मुझे फ़िर से अपना खाने का डिब्बा नहीं चाहिए",
            "मुझे सच में फ़िर से अपना खाने का डिब्बा नहीं चाहिए"

    }, {"मुझे अपनी पानी की बोतल अच्छी लगती हैं",
            "मुझे सच में अपनी पानी की बोतल अच्छी लगती हैं",
            "मुझे अपनी पानी की बोतल चाहिए",
            "मुझे सच में अपनी पानी की बोतल चाहिए",
            "मुझे फ़िर से अपनी पानी की बोतल चाहिए",
            "मुझे सच में फ़िर से अपनी पानी की बोतल चाहिए",
            "मुझे अपनी पानी की बोतल अच्छी नहीं लगती हैं",
            "मुझे सच में अपनी पानी की बोतल अच्छी नहीं लगती हैं",
            "मुझे अपनी पानी की बोतल नहीं चाहिए",
            "मुझे सच में अपनी पानी की बोतल नहीं चाहिए",
            "मुझे फ़िर से अपनी पानी की बोतल नहीं चाहिए",
            "मुझे सच में फ़िर से अपनी पानी की बोतल नहीं चाहिए"

    }, {"मुझे compass बॉक्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में compass बॉक्स का इस्तमाल करना अच्छा लगता हैं",
            "मुझे अपना compass बॉक्स चाहिए",
            "मुझे सच में अपना compass  बॉक्स चाहिए",
            "मुझे फ़िर से अपने  compass बॉक्स का इस्तमाल करना हैं",
            "मुझे सच में और एक  compass बॉक्स चाहिए",
            "मुझे compass बॉक्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में compass बॉक्स का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे अपना compass बॉक्स नहीं चाहिए",
            "मुझे सच में अपना compass बॉक्स नहीं चाहिए",
            "मुझे फ़िर से अपने compass बॉक्स का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक  compass बॉक्स नहीं चाहिए"

    }, {"मुझे गृहपाठ करना अच्छा लगता हैं",
            "मुझे सच में गृहपाठ करना अच्छा लगता हैं",
            "मुझे अपना गृहपाठ करना हैं",
            "मुझे सच में अपना गृहपाठ करना हैं",
            "मुझे और समय के लिए गृहपाठ करना हैं",
            "मुझे सच में थोड़े और समय के लिए गृहपाठ करना हैं",
            "मुझे गृहपाठ करना अच्छा नहीं लगता हैं",
            "मुझे सच में गृहपाठ करना अच्छा नहीं लगता हैं",
            "मुझे अपना गृहपाठ नहीं करना हैं",
            "मुझे सच में अपना गृहपाठ नहीं करना हैं",
            "मुझे और समय के लिए गृहपाठ नहीं करना हैं",
            "मुझे सच में और समय के लिए गृहपाठ नहीं करना हैं"

    }, {"मुझे अपनी पाठशाला की कापी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में अपनी पाठशाला की कापी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे अपनी पाठशाला की कापी चाहिए",
            "मुझे सच में अपनी पाठशाला की कापी चाहिए",
            "मुझे और कापीयाँ चाहिए",
            "मुझे सच में कुछ और कापीयाँ चाहिए",
            "मुझे अपनी पाठशाला की कापी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठशाला की कापी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे अपनी पाठशाला की कापी नहीं चाहिए",
            "मुझे सच में अपनी पाठशाला की कापी नहीं चाहिए",
            "मुझे और कापीयाँ नहीं चाहिए",
            "मुझे सच में और कापीयाँ नहीं चाहिए"

    }, {"मुझे अपनी पाठ्यपुस्तकें पढ़ना अच्छा लगता हैं",
            "मुझे सच में अपनी पाठ्यपुस्तकें पढ़ना अच्छा लगता हैं",
            "मुझे अपनी पाठ्यपुस्तकें चाहिए",
            "मुझे सच में अपनी पाठ्यपुस्तकें चाहिए",
            "मुझे और पाठ्यपुस्तकें चाहिए",
            "मुझे सच में कुछ और पाठ्यपुस्तकें चाहिए",
            "मुझे अपनी पाठ्यपुस्तकें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठ्यपुस्तकें पढ़ना अच्छा नहीं लगता हैं",
            "मुझे अपनी पाठ्यपुस्तकें नहीं चाहिए",
            "मुझे सच में अपनी पाठ्यपुस्तकें नहीं चाहिए",
            "मुझे और पाठ्यपुस्तकें नहीं चाहिए",
            "मुझे सच में और पाठ्यपुस्तकें नहीं चाहिए"

    }, {"मुझे अपनी पाठशाला का यूनिफार्म अच्छा लगता हैं",
            "मुझे सच में अपनी पाठशाला का यूनिफार्म अच्छा लगता हैं",
            "मुझे अपनी पाठशाला का यूनिफार्म पहनना हैं",
            "मुझे सच में अपनी पाठशाला का यूनिफार्म पहनना हैं",
            "मुझे फ़िर से अपनी पाठशाला का यूनिफार्म पहनना हैं",
            "मुझे सच में फ़िर से अपनी पाठशाला का यूनिफार्म पहनना हैं",
            "मुझे अपनी पाठशाला का यूनिफार्म अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी पाठशाला का यूनिफार्म अच्छा नहीं लगता हैं",
            "मुझे अपनी पाठशाला का यूनिफार्म नहीं पहनना हैं",
            "मुझे सच में अपनी पाठशाला का यूनिफार्म नहीं पहनना हैं",
            "मुझे फ़िर से अपनी पाठशाला का यूनिफार्म नहीं पहनना हैं",
            "मुझे सच में फ़िर से अपनी पाठशाला का यूनिफार्म नहीं पहनना हैं"

    }, {"मुझे अपने जूते अच्छे लगते हैं",
            "मुझे सच में अपने जूते अच्छे लगते हैं",
            "मुझे अपने जूते पहनने हैं",
            "मुझे सच में अपने जूते पहनने हैं",
            "मुझे फिर से अपने जूते पहनने हैं",
            "मुझे सच में फिर से अपने जूते पहनने हैं",
            "मुझे अपने जूते अच्छे नहीं लगते हैं",
            "मुझे सच में अपने जूते अच्छे नहीं लगते हैं",
            "मुझे अपने जूते नहीं पहनने हैं",
            "मुझे सच में अपने जूते नहीं पहनने हैं",
            "मुझे फिर से अपने जूते नहीं पहनने हैं",
            "मुझे सच में फिर से अपने जूते नहीं पहनने हैं"

    }, {"मुझे अपने मोज़े अच्छे लगते हैं",
            "मुझे सच में अपने मोज़े अच्छे लगते हैं",
            "मुझे अपने मोज़े पहनने हैं",
            "मुझे सच में अपने मोज़े पहनने हैं",
            "मुझे फिर से अपने मोज़े पहनने हैं",
            "मुझे सच में फिर से अपने मोज़े पहनने हैं",
            "मुझे अपने मोज़े अच्छे नहीं लगते हैं",
            "मुझे सच में अपने मोज़े अच्छे नहीं लगते हैं",
            "मुझे अपने मोज़े नहीं पहनने हैं",
            "मुझे सच में अपने मोज़े नहीं पहनने हैं",
            "मुझे फिर से अपने मोज़े नहीं पहनने हैं",
            "मुझे सच में फिर से अपने मोज़े नहीं पहनने हैं",


    }, {"मुझे pencil का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में pencil का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक pencil चाहिए",
            "मुझे सच में एक pencil चाहिए",
            "मुझे और pencil चाहिए",
            "मुझे सच में थोड़े और pencil चाहिए",
            "मुझे pencil का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में pencil का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे pencil नहीं चाहिए",
            "मुझे सच में pencil नहीं चाहिए",
            "मुझे और pencil नहीं चाहिए",
            "मुझे सच में और pencil नहीं चाहिए"

    }, {"मुझे pen का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में pen का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक pen चाहिए",
            "मुझे सच में एक pen चाहिए",
            "मुझे और pen चाहिए",
            "मुझे सच में थोड़े और pen चाहिए",
            "मुझे pen का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में pen का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे pen नहीं चाहिए",
            "मुझे सच में pen नहीं चाहिए",
            "मुझे और pen नहीं चाहिए",
            "मुझे सच में और pen नहीं चाहिए"

    }, {"मुझे स्केल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में स्केल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक स्केल चाहिए",
            "मुझे सच में एक स्केल चाहिए",
            "मुझे फ़िर से स्केल का इस्तमाल करना हैं",
            "मुझे सच में और एक स्केल चाहिए",
            "मुझे स्केल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्केल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे स्केल नहीं चाहिए",
            "मुझे सच में स्केल नहीं चाहिए",
            "मुझे फ़िर से स्केल का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक स्केल नहीं चाहिए"

    }, {"मुझे रबर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में रबर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक रबर चाहिए",
            "मुझे सच में एक रबर चाहिए",
            "मुझे फ़िर से रबर का इस्तमाल करना हैं",
            "मुझे सच में और एक रबर चाहिए",
            "मुझे रबर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में रबर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे रबर नहीं चाहिए",
            "मुझे सच में रबर नहीं चाहिए",
            "मुझे फ़िर से रबर का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक रबर नहीं चाहिए",

    }, {"मुझे शारप्नर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में शारप्नर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे एक शारप्नर चाहिए",
            "मुझे सच में एक शारप्नर चाहिए",
            "मुझे फ़िर से शारप्नर का इस्तमाल करना हैं",
            "मुझे सच में और एक शारप्नर चाहिए",
            "मुझे शारप्नर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में शारप्नर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे शारप्नर नहीं चाहिए",
            "मुझे सच में शारप्नर नहीं चाहिए",
            "मुझे फ़िर से शारप्नर का इस्तमाल नहीं करना हैं",
            "मुझे सच में और एक शारप्नर नहीं चाहिए"

    }, {"मुझे चॉक से बोर्ड पर लिखना अच्छा लगता हैं",
            "मुझे सच में चॉक से बोर्ड पर लिखना अच्छा लगता हैं",
            "मुझे और चॉक चाहिए",
            "मुझे सच में और चॉक चाहिए",
            "मुझे थोड़े और चॉक चाहिए",
            "मुझे सच में थोड़े और चॉक चाहिए",
            "मुझे चॉक से बोर्ड पर लिखना अच्छा नहीं लगता हैं",
            "मुझे सच में चॉक से बोर्ड पर लिखना अच्छा नहीं लगता हैं",
            "मुझे चॉक नहीं चाहिए",
            "मुझे सच में चॉक नहीं चाहिए",
            "मुझे और चॉक नहीं चाहिए",
            "मुझे सच में और चॉक नहीं चाहिए"

    }}, {{"मुझे खिड़की का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में खिड़की का इस्तमाल करना अच्छा लगता हैं",
            "मुझे खिड़की खोलनी हैं",
            "मुझे सच में खिड़की खोलनी हैं",
            "मुझे फिर से खिड़की खोलनी हैं",
            "मुझे सच में फिर से खिड़की खोलनी हैं",
            "मुझे खिड़की का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में खिड़की का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे खिड़की बंद करनी हैं",
            "मुझे सच में खिड़की बंद करनी हैं",
            "मुझे फिर से खिड़की बंद करनी हैं",
            "मुझे सच में फिर से खिड़की बंद करनी हैं"

    }, {"मुझे दरवाज़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में दरवाज़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे दरवाज़ा खोलना हैं",
            "मुझे सच में दरवाज़ा खोलना हैं",
            "मुझे फिर से दरवाज़ा खोलना हैं",
            "मुझे सच में फिर से दरवाज़ा खोलना हैं",
            "मुझे दरवाज़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में दरवाज़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे दरवाज़ा बंद करना हैं",
            "मुझे सच में दरवाज़ा बंद करना हैं",
            "मुझे फिर से दरवाज़ा बंद करना हैं",
            "मुझे सच में फिर से दरवाज़ा बंद करना हैं",

    }, {"मुझे पंखे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में पंखे का इस्तमाल करना अच्छा लगता हैं",
            "मुझे पंखा चालू करना हैं",
            "मुझे सच में पंखा चालू करना हैं",
            "मुझे फिर से पंखा चालू करना हैं",
            "मुझे सच में फिर से पंखा चालू करना हैं",
            "मुझे पंखे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में पंखे का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे पंखा बंद करना हैं",
            "मुझे सच में पंखा बंद करना हैं",
            "मुझे फिर से पंखा बंद करना हैं",
            "मुझे सच में फिर से पंखा बंद करना हैं"

    }, {"मुझे lamp का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में lamp का इस्तमाल करना अच्छा लगता हैं",
            "मुझे lamp चालू करना हैं",
            "मुझे सच में lamp चालू करना हैं",
            "मुझे फिर से lamp चालू करना हैं",
            "मुझे सच में फिर से lamp चालू करना हैं",
            "मुझे lamp का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में lamp का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे lamp बंद करना हैं",
            "मुझे सच में lamp बंद करना हैं",
            "मुझे फिर से lamp बंद करना हैं",
            "मुझे सच में फिर से lamp बंद करना हैं"

    }, {"मुझे डेस्क का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में डेस्क का इस्तमाल करना अच्छा लगता हैं",
            "मुझे डेस्क का इस्तमाल करना हैं",
            "मुझे सच में डेस्क का इस्तमाल करना हैं",
            "मुझे फ़िर से डेस्क का इस्तमाल करना हैं",
            "मुझे सच में फ़िर से डेस्क का इस्तमाल करना हैं",
            "मुझे डेस्क का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में डेस्क का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे डेस्क का इस्तमाल नहीं करना हैं",
            "मुझे सच में डेस्क का इस्तमाल नहीं करना हैं",
            "मुझे फ़िर से डेस्क का इस्तमाल नहीं करना हैं",
            "मुझे सच में फ़िर से डेस्क का इस्तमाल नहीं करना हैं",


    }, {"मुझे अलमारी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में अलमारी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे अलमारी खोलनी हैं",
            "मुझे सच में अलमारी खोलनी हैं",
            "मुझे फिर से अलमारी खोलनी हैं",
            "मुझे सच में फिर से अलमारी खोलनी हैं",
            "मुझे अलमारी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में अलमारी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे अलमारी बंद करनी हैं",
            "मुझे सच में अलमारी बंद करनी हैं",
            "मुझे फिर से अलमारी बंद करनी हैं",
            "मुझे सच में फिर से अलमारी बंद करनी हैं"

    }, {"मुझे टेबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में टेबल का इस्तमाल करना अच्छा लगता हैं",
            "मुझे टेबल का इस्तमाल करना हैं",
            "मुझे सच में टेबल का इस्तमाल करना हैं",
            "मुझे फिर से टेबल का इस्तमाल करना हैं",
            "मुझे सच में फिर से टेबल का इस्तमाल करना हैं",
            "मुझे टेबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में टेबल का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे टेबल का इस्तमाल नहीं करना हैं",
            "मुझे सच में टेबल का इस्तमाल नहीं करना हैं",
            "मुझे फिर से टेबल का इस्तमाल नहीं करना हैं",
            "मुझे सच में फिर से टेबल का इस्तमाल नहीं करना हैं"

    }, {"मुझे कुर्सी पर बैठना अच्छा लगता हैं",
            "मुझे सच में कुर्सी पर बैठना अच्छा लगता हैं",
            "मुझे कुर्सी पर बैठना हैं",
            "मुझे सच में कुर्सी पर बैठना हैं",
            "मुझे थोड़े समय के लिए कुर्सी पर बैठना हैं",
            "मुझे सच में थोड़े और समय के लिए कुर्सी पर बैठना हैं",
            "मुझे कुर्सी पर बैठना अच्छा नहीं लगता हैं",
            "मुझे सच में कुर्सी पर बैठना अच्छा नहीं लगता हैं",
            "मुझे कुर्सी पर नहीं बैठना हैं",
            "मुझे सच में कुर्सी पर नहीं बैठना हैं",
            "मुझे और समय के लिए कुर्सी पर नहीं बैठना हैं",
            "मुझे सच में और समय के लिए कुर्सी पर नहीं बैठना हैं"

    }, {"मुझे शौचा लय जाना अच्छा लगता हैं",
            "मुझे सच में शौचा लय जाना अच्छा लगता हैं",
            "मुझे शौचा लय जाना हैं",
            "मुझे सच में शौचा लय जाना हैं",
            "मुझे फिर से शौचा लय जाना हैं",
            "मुझे सच में फिर से शौचा लय जाना हैं",
            "मुझे शौचा लय जाना अच्छा नहीं लगता हैं",
            "मुझे सच में शौचा लय जाना अच्छा नहीं लगता हैं",
            "मुझे शौचा लय नहीं जाना हैं",
            "मुझे सच में शौचा लय नहीं जाना हैं",
            "मुझे फिर से शौचा लय नहीं जाना हैं",
            "मुझे सच में फिर से शौचा लय नहीं जाना हैं"

    }, {"मुझे रसोईघर में जाना अच्छा लगता हैं",
            "मुझे सच में रसोईघर में जाना अच्छा लगता हैं",
            "मुझे रसोईघर में जाना हैं",
            "मुझे सच में रसोईघर में जाना हैं",
            "मुझे फिर से रसोईघर में जाना हैं",
            "मुझे सच में फिर से रसोईघर में जाना हैं",
            "मुझे रसोईघर में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में रसोईघर में जाना अच्छा नहीं लगता हैं",
            "मुझे रसोईघर में नहीं जाना हैं",
            "मुझे सच में रसोईघर में नहीं जाना हैं",
            "मुझे फिर से रसोईघर में नहीं जाना हैं",
            "मुझे सच में फिर से रसोईघर में नहीं जाना हैं"

    }, {"मुझे हॉल में जाना अच्छा लगता हैं",
            "मुझे सच में हॉल में जाना अच्छा लगता हैं",
            "मुझे हॉल में जाना हैं",
            "मुझे सच में हॉल में जाना हैं",
            "मुझे फिर से हॉल में जाना हैं",
            "मुझे सच में फिर से हॉल में जाना हैं",
            "मुझे हॉल में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में हॉल में जाना अच्छा नहीं लगता हैं",
            "मुझे हॉल में नहीं जाना हैं",
            "मुझे सच में हॉल में नहीं जाना हैं",
            "मुझे फिर से हॉल में नहीं जाना हैं",
            "मुझे सच में फिर से हॉल में नहीं जाना हैं"

    }, {"मुझे बेडरूम में जाना अच्छा लगता हैं",
            "मुझे सच में बेडरूम में जाना अच्छा लगता हैं",
            "मुझे बेडरूम में जाना हैं",
            "मुझे सच में बेडरूम में जाना हैं",
            "मुझे फिर से बेडरूम में जाना हैं",
            "मुझे सच में फिर से बेडरूम में जाना हैं",
            "मुझे बेडरूम में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में बेडरूम में जाना अच्छा नहीं लगता हैं",
            "मुझे बेडरूम में नहीं जाना हैं",
            "मुझे सच में बेडरूम में नहीं जाना हैं",
            "मुझे फिर से बेडरूम में नहीं जाना हैं",
            "मुझे सच में फिर से बेडरूम में नहीं जाना हैं"

    }, {"मुझे खेलने के कमरे में जाना अच्छा लगता हैं",
            "मुझे सच में खेलने के कमरे में जाना अच्छा लगता हैं",
            "मुझे खेलने के कमरे में जाना हैं",
            "मुझे सच में खेलने के कमरे में जाना हैं",
            "मुझे फिर से खेलने के कमरे में जाना हैं",
            "मुझे सच में फिर से खेलने के कमरे में जाना हैं",
            "मुझे खेलने के कमरे में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में खेलने के कमरे में जाना अच्छा नहीं लगता हैं",
            "मुझे खेलने के कमरे में नहीं जाना हैं",
            "मुझे सच में खेलने के कमरे में नहीं जाना हैं",
            "मुझे फिर से खेलने के कमरे में नहीं जाना हैं",
            "मुझे सच में फिर से खेलने के कमरे में नहीं जाना हैं"

    }, {"मुझे बाथरूम में जाना अच्छा लगता हैं",
            "मुझे सच में बाथरूम में जाना अच्छा लगता हैं",
            "मुझे बाथरूम में जाना हैं",
            "मुझे सच में बाथरूम में जाना हैं",
            "मुझे फिर से बाथरूम में जाना हैं",
            "मुझे सच में फिर से बाथरूम में जाना हैं",
            "मुझे बाथरूम में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में बाथरूम में जाना अच्छा नहीं लगता हैं",
            "मुझे बाथरूम में नहीं जाना हैं",
            "मुझे सच में बाथरूम में नहीं जाना हैं",
            "मुझे फिर से बाथरूम में नहीं जाना हैं",
            "मुझे सच में फिर से बाथरूम में नहीं जाना हैं"

    }, {"मुझे बालकनी में जाना अच्छा लगता हैं",
            "मुझे सच में बालकनी में जाना अच्छा लगता हैं",
            "मुझे बालकनी में जाना हैं",
            "मुझे सच में बालकनी में जाना हैं",
            "मुझे फिर से बालकनी में जाना हैं",
            "मुझे सच में फिर से बालकनी में जाना हैं",
            "मुझे बालकनी में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में बालकनी में जाना अच्छा नहीं लगता हैं",
            "मुझे बालकनी में नहीं जाना हैं",
            "मुझे सच में बालकनी में नहीं जाना हैं",
            "मुझे फिर से बालकनी में नहीं जाना हैं",
            "मुझे सच में फिर से बालकनी में नहीं जाना हैं"

    }, {"मुझे पढ़ाई के कमरे में जाना अच्छा लगता हैं",
            "मुझे सच में पढ़ाई के कमरे में जाना अच्छा लगता हैं",
            "मुझे पढ़ाई के कमरे में जाना हैं",
            "मुझे सच में पढ़ाई के कमरे में जाना हैं",
            "मुझे फिर से पढ़ाई के कमरे में जाना हैं",
            "मुझे सच में फिर से पढ़ाई के कमरे में जाना हैं",
            "मुझे पढ़ाई के कमरे में जाना अच्छा नहीं लगता हैं",
            "मुझे सच में पढ़ाई के कमरे में जाना अच्छा नहीं लगता हैं",
            "मुझे पढ़ाई के कमरे में नहीं जाना हैं",
            "मुझे सच में पढ़ाई के कमरे में नहीं जाना हैं",
            "मुझे फिर से पढ़ाई के कमरे में नहीं जाना हैं",
            "मुझे सच में फिर से पढ़ाई के कमरे में नहीं जाना हैं"

    }, {"मुझे बिस्तर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में बिस्तर का इस्तमाल करना अच्छा लगता हैं",
            "मुझे बिस्तर पर लेटना हैं",
            "मुझे सच में बिस्तर पर लेटना हैं",
            "मुझे फिर से बिस्तर पर लेटना हैं",
            "मुझे सच में फिर से बिस्तर पर लेटना हैं",
            "मुझे बिस्तर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में बिस्तर का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे बिस्तर पर नहीं लेटना हैं",
            "मुझे सच में बिस्तर पर नहीं लेटना हैं",
            "मुझे फिर से बिस्तर पर नहीं लेटना हैं",
            "मुझे सच में फिर से बिस्तर पर नहीं लेटना हैं"

    }, {"मुझे टीवी देखना अच्छा लगता हैं",
            "मुझे सच में टीवी देखना अच्छा लगता हैं",
            "मुझे टीवी देखना हैं",
            "मुझे सच में टीवी देखना हैं",
            "मुझे कुछ और समय के लिए टीवी देखना हैं",
            "मुझे सच में कुछ और समय के लिए टीवी देखना हैं",
            "मुझे टीवी देखना अच्छा नहीं लगता हैं",
            "मुझे सच में टीवी देखना अच्छा नहीं लगता हैं",
            "मुझे टीवी नहीं देखना हैं",
            "मुझे सच में टीवी नहीं देखना हैं",
            "मुझे और समय के लिए टीवी नहीं देखना हैं",
            "मुझे सच में टीवी बिल्कुल नहीं देखना हैं"

    }, {"मुझे संगणक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में संगणक का इस्तमाल करना अच्छा लगता हैं",
            "मुझे संगणक का इस्तमाल करना हैं",
            "मुझे सच में संगणक का इस्तमाल करना हैं",
            "मुझे थोड़े और समय के लिए संगणक का इस्तमाल करना हैं",
            "मुझे सच में थोड़े और समय के लिए संगणक का इस्तमाल करना हैं",
            "मुझे संगणक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में संगणक का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे संगणक का इस्तमाल नहीं करना हैं",
            "मुझे सच में संगणक का इस्तमाल नहीं करना हैं",
            "मुझे और समय के लिए संगणक का इस्तमाल नहीं करना हैं",
            "मुझे सच में और समय के लिए संगणक का इस्तमाल नहीं करना हैं"

    }, {"मुझे सोफ़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में सोफ़े का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सोफ़े पर बैठना हैं",
            "मुझे सच में सोफ़े पर बैठना हैं",
            "मुझे थोड़े और समय के लिए सोफ़े पर बैठना हैं",
            "मुझे सच में थोड़े और समय के लिए सोफ़े पर बैठना हैं",
            "मुझे सोफ़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में सोफ़े का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सोफ़े पर नहीं बैठना हैं",
            "मुझे सच में सोफ़े पर नहीं बैठना हैं",
            "मुझे और समय के लिए सोफ़े पर नहीं बैठना हैं",
            "मुझे सच में और समय के लिए सोफ़े पर नहीं बैठना हैं"

    }, {"मुझे फ्रिज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में फ्रिज का इस्तमाल करना अच्छा लगता हैं",
            "मुझे फ्रिज खोलना हैं",
            "मुझे सच में फ्रिज खोलना हैं",
            "मुझे फ़िर से फ्रिज का इस्तमाल करना हैं",
            "मुझे सच में फ़िर से फ्रिज का इस्तमाल करना हैं",
            "मुझे फ्रिज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में फ्रिज का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे फ्रिज बंद करना हैं",
            "मुझे सच में फ्रिज बंद करना हैं",
            "मुझे फ़िर से फ्रिज का इस्तमाल नहीं करना हैं",
            "मुझे सच में फ़िर से फ्रिज का इस्तमाल नहीं करना हैं"

    }, {"मुझे माइक्रोवेव का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में माइक्रोवेव का इस्तमाल करना अच्छा लगता हैं",
            "मुझे माइक्रोवेव का इस्तमाल करना हैं",
            "मुझे सच में माइक्रोवेव का इस्तमाल करना हैं",
            "मुझे फ़िर से माइक्रोवेव का इस्तमाल करना हैं",
            "मुझे सच में फ़िर से माइक्रोवेव का इस्तमाल करना हैं",
            "मुझे माइक्रोवेव का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में माइक्रोवेव का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे माइक्रोवेव का इस्तमाल नहीं करना हैं",
            "मुझे सच में माइक्रोवेव का इस्तमाल नहीं करना हैं",
            "मुझे फ़िर से माइक्रोवेव का इस्तमाल नहीं करना हैं",
            "मुझे सच में फ़िर से माइक्रोवेव का इस्तमाल नहीं करना हैं"

    }, {"मुझे वॉशिंग मशीन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में वॉशिंग मशीन का इस्तमाल करना अच्छा लगता हैं",
            "मुझे वॉशिंग मशीन का इस्तमाल करना हैं",
            "मुझे सच में वॉशिंग मशीन का इस्तमाल करना हैं",
            "मुझे फ़िर से वॉशिंग मशीन का इस्तमाल करना हैं",
            "मुझे सच में फ़िर से वॉशिंग मशीन का इस्तमाल करना हैं",
            "मुझे वॉशिंग मशीन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में वॉशिंग मशीन का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे वॉशिंग मशीन का इस्तमाल नहीं करना हैं",
            "मुझे सच में वॉशिंग मशीन का इस्तमाल नहीं करना हैं",
            "मुझे फ़िर से वॉशिंग मशीन का इस्तमाल नहीं करना हैं",
            "मुझे सच में फ़िर से वॉशिंग मशीन का इस्तमाल नहीं करना हैं"

    }, {"मुझे vacuum cleaner का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में vacuum cleaner का इस्तमाल करना अच्छा लगता हैं",
            "मुझे vacuum cleaner का इस्तमाल करना हैं",
            "मुझे सच में vacuum cleaner का इस्तमाल करना हैं",
            "मुझे फ़िर से vacuum cleaner का इस्तमाल करना हैं",
            "मुझे सच में फ़िर से vacuum cleaner का इस्तमाल करना हैं",
            "मुझे vacuum cleaner का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में vacuum cleaner का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे vacuum cleaner का इस्तमाल नहीं करना हैं",
            "मुझे सच में vacuum cleaner का इस्तमाल नहीं करना हैं",
            "मुझे फ़िर से vacuum cleaner का इस्तमाल नहीं करना हैं",
            "मुझे सच में फ़िर से vacuum cleaner का इस्तमाल नहीं करना हैं"

    }, {"मुझे घड़ी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में घड़ी का इस्तमाल करना अच्छा लगता हैं",
            "मुझे घड़ी देखनी हैं",
            "मुझे सच में घड़ी देखनी हैं",
            "मुझे फ़िर से घड़ी देखनी हैं",
            "मुझे सच में फ़िर से घड़ी देखनी हैं",
            "मुझे घड़ी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में घड़ी का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे घड़ी नहीं देखनी हैं",
            "मुझे सच में घड़ी नहीं देखनी हैं",
            "मुझे फ़िर से घड़ी नहीं देखनी हैं",
            "मुझे सच में फ़िर से घड़ी नहीं देखनी हैं"

    }, {"मुझे ट्यूब लाइट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे सच में ट्यूब लाइट का इस्तमाल करना अच्छा लगता हैं",
            "मुझे ट्यूब लाइट चालू करनी हैं",
            "मुझे सच में ट्यूब लाइट चालू करनी हैं",
            "मुझे फिर से ट्यूब लाइट चालू करनी हैं",
            "मुझे सच में फिर से ट्यूब लाइट चालू करनी हैं",
            "मुझे ट्यूब लाइट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे सच में ट्यूब लाइट का इस्तमाल करना अच्छा नहीं लगता हैं",
            "मुझे ट्यूब लाइट बंद करनी हैं",
            "मुझे सच में ट्यूब लाइट बंद करनी हैं",
            "मुझे फिर से ट्यूब लाइट बंद करनी हैं",
            "मुझे सच में फिर से ट्यूब लाइट बंद करनी हैं"

    }}, {{"मुझे बस से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में बस से सफ़र करना अच्छा लगता हैं",
            "मुझे बस में सफ़र करना हैं",
            "मुझे सच में बस में सफ़र करना हैं",
            "मुझे फ़िर से बस में सफ़र करना हैं",
            "मुझे सच में फ़िर से बस में सफ़र करना हैं",
            "मुझे बस से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में बस से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे बस में सफ़र नहीं करना हैं",
            "मुझे सच में बस में सफ़र नहीं करना हैं",
            "मुझे फ़िर से बस में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से बस में सफ़र नहीं करना हैं"

    }, {"मुझे स्कूल  बस से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में स्कूल बस से सफ़र करना अच्छा लगता हैं",
            "मुझे स्कूल बस में सफ़र करना हैं",
            "मुझे सच में स्कूल बस में सफ़र करना हैं",
            "मुझे फ़िर से स्कूल बस में सफ़र करना हैं",
            "मुझे सच में फ़िर से स्कूल बस में सफ़र करना हैं",
            "मुझे स्कूल बस से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में स्कूल बस से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे स्कूल बस में सफ़र नहीं करना हैं",
            "मुझे सच में स्कूल बस में सफ़र नहीं करना हैं",
            "मुझे फ़िर से स्कूल बस में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से स्कूल बस में सफ़र नहीं करना हैं"

    }, {"मुझे कार से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में कार से सफ़र करना अच्छा लगता हैं",
            "मुझे कार में सफ़र करना हैं",
            "मुझे सच में कार में सफ़र करना हैं",
            "मुझे फ़िर से कार में सफ़र करना हैं",
            "मुझे सच में फ़िर से कार में सफ़र करना हैं",
            "मुझे कार से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में कार से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे कार में सफ़र नहीं करना हैं",
            "मुझे सच में कार में सफ़र नहीं करना हैं",
            "मुझे फ़िर से कार में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से कार में सफ़र नहीं करना हैं"

    }, {"मुझे अपनी cycle पर सवारी करना अच्छा लगता हैं",
            "मुझे सच में अपनी cycle पर सवारी करना अच्छा लगता हैं",
            "मुझे अपनी cycle पर सवारी करनी हैं",
            "मुझे सच में अपनी cycle पर सवारी करनी हैं",
            "मुझे फ़िर से अपनी cycle पर सवारी करनी हैं",
            "मुझे सच में फ़िर से अपनी cycle पर सवारी करनी हैं",
            "मुझे अपनी cycle पर सवारी करना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी cycle पर सवारी करना अच्छा नहीं लगता हैं",
            "मुझे अपनी cycle पर सवारी नहीं करनी हैं",
            "मुझे सच में अपनी cycle पर सवारी नहीं करनी हैं",
            "मुझे फ़िर से अपनी cycle पर सवारी नहीं करनी हैं",
            "मुझे सच में फ़िर से अपनी cycle पर सवारी नहीं करनी हैं"

    }, {"मुझे रेल गाड़ी से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में रेल गाड़ी से सफ़र करना अच्छा लगता हैं",
            "मुझे रेल गाड़ी में सफ़र करना हैं",
            "मुझे सच में रेल गाड़ी में सफ़र करना हैं",
            "मुझे फ़िर से रेल गाड़ी में सफ़र करना हैं",
            "मुझे सच में फ़िर से रेल गाड़ी में सफ़र करना हैं",
            "मुझे रेल गाड़ी से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में रेल गाड़ी से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे रेल गाड़ी में सफ़र नहीं करना हैं",
            "मुझे सच में रेल गाड़ी में सफ़र नहीं करना हैं",
            "मुझे फ़िर से रेल गाड़ी में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से रेल गाड़ी में सफ़र नहीं करना हैं"

    }, {"मुझे रिक्शा से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में रिक्शा से सफ़र करना अच्छा लगता हैं",
            "मुझे रिक्शा में सफ़र करना हैं",
            "मुझे सच में रिक्शा में सफ़र करना हैं",
            "मुझे फ़िर से रिक्शा में सफ़र करना हैं",
            "मुझे सच में फ़िर से रिक्शा में सफ़र करना हैं",
            "मुझे रिक्शा से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में रिक्शा से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे रिक्शा में सफ़र नहीं करना हैं",
            "मुझे सच में रिक्शा में सफ़र नहीं करना हैं",
            "मुझे फ़िर से रिक्शा में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से रिक्शा में सफ़र नहीं करना हैं"

    }, {"मुझे मोटर cycle पर सवारी करना अच्छा लगता हैं",
            "मुझे सच में मोटर cycle पर सवारी करना अच्छा लगता हैं",
            "मुझे मोटर cycle पर सवारी करनी हैं",
            "मुझे सच में मोटर cycle पर सवारी करनी हैं",
            "मुझे फ़िर से मोटर cycle पर सवारी करनी हैं",
            "मुझे सच में फ़िर से मोटर cycle पर सवारी करनी हैं",
            "मुझे मोटर cycle पर सवारी करना अच्छा नहीं लगता हैं",
            "मुझे सच में मोटर cycle पर सवारी करना अच्छा नहीं लगता हैं",
            "मुझे मोटर cycle पर सवारी नहीं करनी हैं",
            "मुझे सच में मोटर cycle पर सवारी नहीं करनी हैं",
            "मुझे फ़िर से मोटर cycle पर सवारी नहीं करनी हैं",
            "मुझे सच में फ़िर से मोटर cycle पर सवारी नहीं करनी हैं"

    }, {"मुझे हवाई जहाज़ से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में हवाई जहाज़ से सफ़र करना अच्छा लगता हैं",
            "मुझे हवाई जहाज़ में सफ़र करना हैं",
            "मुझे सच में हवाई जहाज़ में सफ़र करना हैं",
            "मुझे फ़िर से हवाई जहाज़ में सफ़र करना हैं",
            "मुझे सच में फ़िर से हवाई जहाज़ में सफ़र करना हैं",
            "मुझे हवाई जहाज़ से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में हवाई जहाज़ से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे हवाई जहाज़ में सफ़र नहीं करना हैं",
            "मुझे सच में हवाई जहाज़ में सफ़र नहीं करना हैं",
            "मुझे फ़िर से हवाई जहाज़ में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से हवाई जहाज़ में सफ़र नहीं करना हैं"

    }, {"मुझे जहाज़ से सफ़र करना अच्छा लगता हैं",
            "मुझे सच में जहाज़ से सफ़र करना अच्छा लगता हैं",
            "मुझे जहाज़ में सफ़र करना हैं",
            "मुझे सच में जहाज़ में सफ़र करना हैं",
            "मुझे फ़िर से जहाज़ में सफ़र करना हैं",
            "मुझे सच में फ़िर से जहाज़ में सफ़र करना हैं",
            "मुझे जहाज़ से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे सच में जहाज़ से सफ़र करना अच्छा नहीं लगता हैं",
            "मुझे जहाज़ में सफ़र नहीं करना हैं",
            "मुझे सच में जहाज़ में सफ़र नहीं करना हैं",
            "मुझे फ़िर से जहाज़ में सफ़र नहीं करना हैं",
            "मुझे सच में फ़िर से जहाज़ में सफ़र नहीं करना हैं"

    }}}, {{{}}}, {{{}}}, {{{}, {"मेरा आज का दिन अच्छा चल रहा हैं",
            "मेरा आज का दिन सच में अच्छा चल रहा हैं",
            "मुझे आज मज़ें करने हैं",
            "मुझे सच में आज मज़ें करने हैं",
            "मुझे आज फ़िर से बाहर जाना हैं",
            "मुझे सच में आज फ़िर से बाहर जाना हैं",
            "मेरा आज का दिन अच्छा नहीं चल रहा हैं",
            "मेरा आज का दिन सच में अच्छा नहीं चल रहा हैं",
            "मुझे आज मज़ें नहीं करने हैं",
            "मुझे सच में आज मज़ें नहीं करने हैं",
            "मुझे आज फ़िर से बाहर नहीं जाना हैं",
            "मुझे सच में आज फ़िर से बाहर नहीं जाना हैं"

    }, {"मेरा कल का दिन अच्छा था",
            "मेरा कल का दिन सच में अच्छा था",
            "मैंने कल मज़े किए थे",
            "मैंने सच में कल मज़े किए थे",
            "मुझे कल बाहर जाना पड़ा",
            "मुझे सच में कल बाहर जाना पड़ा",
            "मेरा कल का दिन अच्छा नहीं था",
            "मेरा कल का दिन सच में अच्छा नहीं था",
            "मैंने कल मज़े नहीं किए थे",
            "मैंने सच में कल मज़े नहीं किए थे",
            "मुझे कल बाहर नहीं जाना पड़ा",
            "मुझे सच में कल बाहर नहीं जाना पड़ा",

    }, {"मुझे कल का  इंतज़ार हैं",
            "मुझे सच में कल का इंतज़ार हैं",
            "मुझे कल मज़ें करने हैं",
            "मुझे सच में कल मज़ें करने हैं",
            "मुझे कल फ़िर से बाहर जाना हैं",
            "मुझे सच में कल फ़िर से बाहर जाना हैं",
            "मुझे कल का इंतज़ार नहीं हैं",
            "मुझे सच में कल का इंतज़ार हैं",
            "मुझे कल मज़ें नहीं करने हैं",
            "मुझे सच में कल मज़ें नहीं करने हैं",
            "मुझे कल फ़िर से बाहर नहीं जाना हैं",
            "मुझे सच में कल फ़िर से बाहर नहीं जाना हैं"

    }, {"मुझे सुबह का समय अच्छा लगता हैं",
            "मुझे सच में सुबह का समय अच्छा लगता हैं",
            "मुझे सुबह के समय जाना हैं",
            "मुझे सच में सुबह के समय जाना हैं",
            "मुझे फ़िर से सुबह के समय जाना हैं",
            "मुझे सच में फ़िर से सुबह के समय जाना हैं",
            "मुझे सुबह का समय अच्छा नहीं लगता हैं",
            "मुझे सच में सुबह का समय अच्छा नहीं लगता हैं",
            "मुझे सुबह के समय नहीं जाना हैं",
            "मुझे सच में सुबह के समय नहीं जाना हैं",
            "मुझे फ़िर से सुबह के समय नहीं जाना हैं",
            "मुझे सच में फ़िर से सुबह के समय नहीं जाना हैं"

    }, {"मुझे दोपहर का समय अच्छा लगता हैं",
            "मुझे सच में दोपहर का समय अच्छा लगता हैं",
            "मुझे दोपहर के समय जाना हैं",
            "मुझे सच में दोपहर के समय जाना हैं",
            "मुझे फ़िर से दोपहर के समय जाना हैं",
            "मुझे सच में फ़िर से दोपहर के समय जाना हैं",
            "मुझे दोपहर का समय अच्छा नहीं लगता हैं",
            "मुझे सच में दोपहर का समय अच्छा नहीं लगता हैं",
            "मुझे दोपहर के समय नहीं जाना हैं",
            "मुझे सच में दोपहर के समय नहीं जाना हैं",
            "मुझे फ़िर से दोपहर के समय नहीं जाना हैं",
            "मुझे सच में फ़िर से दोपहर के समय नहीं जाना हैं"

    }, {"मुझे शाम का समय अच्छा लगता हैं",
            "मुझे सच में शाम का समय अच्छा लगता हैं",
            "मुझे शाम के समय जाना हैं",
            "मुझे सच में शाम के समय जाना हैं",
            "मुझे फ़िर से शाम के समय जाना हैं",
            "मुझे सच में फ़िर से शाम के समय जाना हैं",
            "मुझे शाम का समय अच्छा नहीं लगता हैं",
            "मुझे सच में शाम का समय अच्छा नहीं लगता हैं",
            "मुझे शाम के समय नहीं जाना हैं",
            "मुझे सच में शाम के समय नहीं जाना हैं",
            "मुझे फ़िर से शाम के समय नहीं जाना हैं",
            "मुझे सच में फ़िर से शाम के समय नहीं जाना हैं"

    }, {"मुझे रात का समय अच्छा लगता हैं",
            "मुझे सच में रात का समय अच्छा लगता हैं",
            "मुझे रात के समय जाना हैं",
            "मुझे सच में रात के समय जाना हैं",
            "मुझे फ़िर से रात के समय जाना हैं",
            "मुझे सच में फ़िर से रात के समय जाना हैं",
            "मुझे रात का समय अच्छा नहीं लगता हैं",
            "मुझे सच में रात का समय अच्छा नहीं लगता हैं",
            "मुझे रात के समय नहीं जाना हैं",
            "मुझे सच में रात के समय नहीं जाना हैं",
            "मुझे फ़िर से रात के समय नहीं जाना हैं",
            "मुझे सच में फ़िर से रात के समय नहीं जाना हैं"

    }}, {{}, {"मुझे सोमवार का दिन अच्छा लगता हैं",
            "मुझे सच में सोमवार का दिन अच्छा लगता हैं",
            "मुझे सोमवार के दिन जाना हैं",
            "मुझे सच में सोमवार के दिन जाना हैं",
            "मुझे फ़िर से सोमवार के दिन जाना हैं",
            "मुझे सच में फ़िर से सोमवार के दिन जाना हैं",
            "मुझे सोमवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में सोमवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सोमवार के दिन नहीं जाना हैं",
            "मुझे सच में सोमवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से सोमवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से सोमवार के दिन नहीं जाना हैं"

    }, {"मुझे मन्गल वार का दिन अच्छा लगता हैं",
            "मुझे सच में मन्गल वार का दिन अच्छा लगता हैं",
            "मुझे मन्गल वार के दिन जाना हैं",
            "मुझे सच में मन्गल वार के दिन जाना हैं",
            "मुझे फ़िर से मन्गल वार के दिन जाना हैं",
            "मुझे सच में फ़िर से मन्गल वार के दिन जाना हैं",
            "मुझे मन्गल वार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में मन्गल वार का दिन अच्छा नहीं लगता हैं",
            "मुझे मन्गल वार के दिन नहीं जाना हैं",
            "मुझे सच में मन्गल वार के दिन नहीं जाना हैं",
            "मुझे फ़िर से मन्गल वार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से मन्गल वार के दिन नहीं जाना हैं"

    }, {"मुझे बुधवार का दिन अच्छा लगता हैं",
            "मुझे सच में बुधवार का दिन अच्छा लगता हैं",
            "मुझे बुधवार के दिन जाना हैं",
            "मुझे सच में बुधवार के दिन जाना हैं",
            "मुझे फ़िर से बुधवार के दिन जाना हैं",
            "मुझे सच में फ़िर से बुधवार के दिन जाना हैं",
            "मुझे बुधवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में बुधवार का दिन अच्छा नहीं लगता हैं",
            "मुझे बुधवार के दिन नहीं जाना हैं",
            "मुझे सच में बुधवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से बुधवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से बुधवार के दिन नहीं जाना हैं"

    }, {"मुझे गुरूवार का दिन अच्छा लगता हैं",
            "मुझे सच में गुरूवार का दिन अच्छा लगता हैं",
            "मुझे गुरूवार के दिन जाना हैं",
            "मुझे सच में गुरूवार के दिन जाना हैं",
            "मुझे फ़िर से गुरूवार के दिन जाना हैं",
            "मुझे सच में फ़िर से गुरूवार के दिन जाना हैं",
            "मुझे गुरूवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में गुरूवार का दिन अच्छा नहीं लगता हैं",
            "मुझे गुरूवार के दिन नहीं जाना हैं",
            "मुझे सच में गुरूवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से गुरूवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से गुरूवार के दिन नहीं जाना हैं"

    }, {"मुझे शुक्रवार का दिन अच्छा लगता हैं",
            "मुझे सच में शुक्रवार का दिन अच्छा लगता हैं",
            "मुझे शुक्रवार के दिन जाना हैं",
            "मुझे सच में शुक्रवार के दिन जाना हैं",
            "मुझे फ़िर से शुक्रवार के दिन जाना हैं",
            "मुझे सच में फ़िर से शुक्रवार के दिन जाना हैं",
            "मुझे शुक्रवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में शुक्रवार का दिन अच्छा नहीं लगता हैं",
            "मुझे शुक्रवार के दिन नहीं जाना हैं",
            "मुझे सच में शुक्रवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से शुक्रवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से शुक्रवार के दिन नहीं जाना हैं"

    }, {"मुझे शनिवार का दिन अच्छा लगता हैं",
            "मुझे सच में शनिवार का दिन अच्छा लगता हैं",
            "मुझे शनिवार के दिन जाना हैं",
            "मुझे सच में शनिवार के दिन जाना हैं",
            "मुझे फ़िर से शनिवार के दिन जाना हैं",
            "मुझे सच में फ़िर से शनिवार के दिन जाना हैं",
            "मुझे शनिवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में शनिवार का दिन अच्छा नहीं लगता हैं",
            "मुझे शनिवार के दिन नहीं जाना हैं",
            "मुझे सच में शनिवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से शनिवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से शनिवार के दिन नहीं जाना हैं"

    }, {"मुझे रविवार का दिन अच्छा लगता हैं",
            "मुझे सच में रविवार का दिन अच्छा लगता हैं",
            "मुझे रविवार के दिन जाना हैं",
            "मुझे सच में रविवार के दिन जाना हैं",
            "मुझे फ़िर से रविवार के दिन जाना हैं",
            "मुझे सच में फ़िर से रविवार के दिन जाना हैं",
            "मुझे रविवार का दिन अच्छा नहीं लगता हैं",
            "मुझे सच में रविवार का दिन अच्छा नहीं लगता हैं",
            "मुझे रविवार के दिन नहीं जाना हैं",
            "मुझे सच में रविवार के दिन नहीं जाना हैं",
            "मुझे फ़िर से रविवार के दिन नहीं जाना हैं",
            "मुझे सच में फ़िर से रविवार के दिन नहीं जाना हैं"

    }}, {{}, {"मुझे जनवरी महीना अच्छा लगता हैं",
            "मुझे सच में जनवरी महीना अच्छा लगता हैं",
            "मुझे जनवरी महीने में जाना हैं",
            "मुझे सच में जनवरी महीने में जाना हैं",
            "मुझे फ़िर से जनवरी महीने में जाना हैं",
            "मुझे सच में फ़िर से जनवरी महीने में जाना हैं",
            "मुझे जनवरी महीना अच्छा नहीं लगता हैं",
            "मुझे सच में जनवरी महीना अच्छा नहीं लगता हैं",
            "मुझे जनवरी महीने में नहीं जाना हैं",
            "मुझे सच में जनवरी महीने में नहीं जाना हैं",
            "मुझे फ़िर से जनवरी महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से जनवरी महीने में नहीं जाना हैं"

    }, {"मुझे फरवरी महीना अच्छा लगता हैं",
            "मुझे सच में फरवरी महीना अच्छा लगता हैं",
            "मुझे फरवरी महीने में जाना हैं",
            "मुझे सच में फरवरी महीने में जाना हैं",
            "मुझे फ़िर से फरवरी महीने में जाना हैं",
            "मुझे सच में फ़िर से फरवरी महीने में जाना हैं",
            "मुझे फरवरी महीना अच्छा नहीं लगता हैं",
            "मुझे सच में फरवरी महीना अच्छा नहीं लगता हैं",
            "मुझे फरवरी महीने में नहीं जाना हैं",
            "मुझे सच में फरवरी महीने में नहीं जाना हैं",
            "मुझे फ़िर से फरवरी महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से फरवरी महीने में नहीं जाना हैं"

    }, {"मुझे मार्च महीना अच्छा लगता हैं",
            "मुझे सच में मार्च महीना अच्छा लगता हैं",
            "मुझे मार्च महीने में जाना हैं",
            "मुझे सच में मार्च महीने में जाना हैं",
            "मुझे फ़िर से मार्च महीने में जाना हैं",
            "मुझे सच में फ़िर से मार्च महीने में जाना हैं",
            "मुझे मार्च महीना अच्छा नहीं लगता हैं",
            "मुझे सच में मार्च महीना अच्छा नहीं लगता हैं",
            "मुझे मार्च महीने में नहीं जाना हैं",
            "मुझे सच में मार्च महीने में नहीं जाना हैं",
            "मुझे फ़िर से मार्च महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से मार्च महीने में नहीं जाना हैं"

    }, {"मुझे अप्रैल महीना अच्छा लगता हैं",
            "मुझे सच में अप्रैल महीना अच्छा लगता हैं",
            "मुझे अप्रैल महीने में जाना हैं",
            "मुझे सच में अप्रैल महीने में जाना हैं",
            "मुझे फ़िर से अप्रैल महीने में जाना हैं",
            "मुझे सच में फ़िर से अप्रैल महीने में जाना हैं",
            "मुझे अप्रैल महीना अच्छा नहीं लगता हैं",
            "मुझे सच में अप्रैल महीना अच्छा नहीं लगता हैं",
            "मुझे अप्रैल महीने में नहीं जाना हैं",
            "मुझे सच में अप्रैल महीने में नहीं जाना हैं",
            "मुझे फ़िर से अप्रैल महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से अप्रैल महीने में नहीं जाना हैं"

    }, {"मुझे मई महीना अच्छा लगता हैं",
            "मुझे सच में मई महीना अच्छा लगता हैं",
            "मुझे मई महीने में जाना हैं",
            "मुझे सच में मई महीने में जाना हैं",
            "मुझे फ़िर से मई महीने में जाना हैं",
            "मुझे सच में फ़िर से मई महीने में जाना हैं",
            "मुझे मई महीना अच्छा नहीं लगता हैं",
            "मुझे सच में मई महीना अच्छा नहीं लगता हैं",
            "मुझे मई महीने में नहीं जाना हैं",
            "मुझे सच में मई महीने में नहीं जाना हैं",
            "मुझे फ़िर से मई महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से मई महीने में नहीं जाना हैं"

    }, {"मुझे जून महीना अच्छा लगता हैं",
            "मुझे सच में जून महीना अच्छा लगता हैं",
            "मुझे जून महीने में जाना हैं",
            "मुझे सच में जून महीने में जाना हैं",
            "मुझे फ़िर से जून महीने में जाना हैं",
            "मुझे सच में फ़िर से जून महीने में जाना हैं",
            "मुझे जून महीना अच्छा नहीं लगता हैं",
            "मुझे सच में जून महीना अच्छा नहीं लगता हैं",
            "मुझे जून महीने में नहीं जाना हैं",
            "मुझे सच में जून महीने में नहीं जाना हैं",
            "मुझे फ़िर से जून महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से जून महीने में नहीं जाना हैं"

    }, {"मुझे जुलाई महीना अच्छा लगता हैं",
            "मुझे सच में जुलाई महीना अच्छा लगता हैं",
            "मुझे जुलाई महीने में जाना हैं",
            "मुझे सच में जुलाई महीने में जाना हैं",
            "मुझे फ़िर से जुलाई महीने में जाना हैं",
            "मुझे सच में फ़िर से जुलाई महीने में जाना हैं",
            "मुझे जुलाई महीना अच्छा नहीं लगता हैं",
            "मुझे सच में जुलाई महीना अच्छा नहीं लगता हैं",
            "मुझे जुलाई महीने में नहीं जाना हैं",
            "मुझे सच में जुलाई महीने में नहीं जाना हैं",
            "मुझे फ़िर से जुलाई महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से जुलाई महीने में नहीं जाना हैं"

    }, {"मुझे अगस्त महीना अच्छा लगता हैं",
            "मुझे सच में अगस्त महीना अच्छा लगता हैं",
            "मुझे अगस्त महीने में जाना हैं",
            "मुझे सच में अगस्त महीने में जाना हैं",
            "मुझे फ़िर से अगस्त महीने में जाना हैं",
            "मुझे सच में फ़िर से अगस्त महीने में जाना हैं",
            "मुझे अगस्त महीना अच्छा नहीं लगता हैं",
            "मुझे सच में अगस्त महीना अच्छा नहीं लगता हैं",
            "मुझे अगस्त महीने में नहीं जाना हैं",
            "मुझे सच में अगस्त महीने में नहीं जाना हैं",
            "मुझे फ़िर से अगस्त महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से अगस्त महीने में नहीं जाना हैं"

    }, {"मुझे सितंबर महीना अच्छा लगता हैं",
            "मुझे सच में सितंबर महीना अच्छा लगता हैं",
            "मुझे सितंबर महीने में जाना हैं",
            "मुझे सच में सितंबर महीने में जाना हैं",
            "मुझे फ़िर से सितंबर महीने में जाना हैं",
            "मुझे सच में फ़िर से सितंबर महीने में जाना हैं",
            "मुझे सितंबर महीना अच्छा नहीं लगता हैं",
            "मुझे सच में सितंबर महीना अच्छा नहीं लगता हैं",
            "मुझे सितंबर महीने में नहीं जाना हैं",
            "मुझे सच में सितंबर महीने में नहीं जाना हैं",
            "मुझे फ़िर से सितंबर महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से सितंबर महीने में नहीं जाना हैं"

    }, {"मुझे अक्टूबर महीना अच्छा लगता हैं",
            "मुझे सच में अक्टूबर महीना अच्छा लगता हैं",
            "मुझे अक्टूबर महीने में जाना हैं",
            "मुझे सच में अक्टूबर महीने में जाना हैं",
            "मुझे फ़िर से अक्टूबर महीने में जाना हैं",
            "मुझे सच में फ़िर से अक्टूबर महीने में जाना हैं",
            "मुझे अक्टूबर महीना अच्छा नहीं लगता हैं",
            "मुझे सच में अक्टूबर महीना अच्छा नहीं लगता हैं",
            "मुझे अक्टूबर महीने में नहीं जाना हैं",
            "मुझे सच में अक्टूबर महीने में नहीं जाना हैं",
            "मुझे फ़िर से अक्टूबर महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से अक्टूबर महीने में नहीं जाना हैं"

    }, {"मुझे नवंबर महीना अच्छा लगता हैं",
            "मुझे सच में नवंबर महीना अच्छा लगता हैं",
            "मुझे नवंबर महीने में जाना हैं",
            "मुझे सच में नवंबर महीने में जाना हैं",
            "मुझे फ़िर से नवंबर महीने में जाना हैं",
            "मुझे सच में फ़िर से नवंबर महीने में जाना हैं",
            "मुझे नवंबर महीना अच्छा नहीं लगता हैं",
            "मुझे सच में नवंबर महीना अच्छा नहीं लगता हैं",
            "मुझे नवंबर महीने में नहीं जाना हैं",
            "मुझे सच में नवंबर महीने में नहीं जाना हैं",
            "मुझे फ़िर से नवंबर महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से नवंबर महीने में नहीं जाना हैं"

    }, {"मुझे दिसंबर महीना अच्छा लगता हैं",
            "मुझे सच में दिसंबर महीना अच्छा लगता हैं",
            "मुझे दिसंबर महीने में जाना हैं",
            "मुझे सच में दिसंबर महीने में जाना हैं",
            "मुझे फ़िर से दिसंबर महीने में जाना हैं",
            "मुझे सच में फ़िर से दिसंबर महीने में जाना हैं",
            "मुझे दिसंबर महीना अच्छा नहीं लगता हैं",
            "मुझे सच में दिसंबर महीना अच्छा नहीं लगता हैं",
            "मुझे दिसंबर महीने में नहीं जाना हैं",
            "मुझे सच में दिसंबर महीने में नहीं जाना हैं",
            "मुझे फ़िर से दिसंबर महीने में नहीं जाना हैं",
            "मुझे सच में फ़िर से दिसंबर महीने में नहीं जाना हैं"

    }, {"मेरा यह महीना अच्छा चल रहा हैं",
            "मेरा यह महीना सच में अच्छा चल रहा हैं",
            "मुझे इस महीने मज़ें करने हैं",
            "मुझे सच में इस महीने मज़ें करने हैं",
            "मुझे इस महीने फ़िर से बाहर जाना हैं",
            "मुझे सच में इस महीने फ़िर से बाहर जाना हैं",
            "मेरा यह महीना अच्छा नहीं चल रहा हैं",
            "मेरा यह महीना सच में अच्छा नहीं चल रहा हैं",
            "मुझे इस महीने मज़ें नहीं करने हैं",
            "मुझे सच में इस महीने मज़ें नहीं करने हैं",
            "मुझे इस महीने फ़िर से बाहर नहीं जाना हैं",
            "मुझे सच में इस महीने फ़िर से बाहर नहीं जाना हैं"

    }, {"मेरा पिछला महीना अच्छा था",
            "मेरा पिछला महीना सच में अच्छा था",
            "मैंने पिछले महिने मज़ें किये थे",
            "मैंने सच में पिछले महिने मज़ें किये थे",
            "मुझे पिछले महिने बाहर जाना पड़ा",
            "मुझे सच में पिछले महिने बाहर जाना पड़ा",
            "मेरा पिछला महीना अच्छा नहीं था",
            "मेरा पिछला महीना सच में अच्छा नहीं था",
            "मैंने पिछले महिने मज़ें नहीं किये थे",
            "मैंने सच में पिछले महिने मज़ें नहीं किये थे",
            "मुझे पिछले महिने बाहर नहीं जाना पड़ा",
            "मुझे सच में पिछले महिने बाहर नहीं जाना पड़ा",

    }, {"मुझे अगले महीने का  इंतज़ार हैं",
            "मुझे सच में अगले महीने का इंतज़ार हैं",
            "मुझे अगले महीने मज़ें करने हैं",
            "मुझे सच में अगले महीने मज़ें करने हैं",
            "मुझे फ़िर से अगले महीने बाहर जाना हैं",
            "मुझे सच में फ़िर से अगले महीने बाहर जाना हैं",
            "मुझे अगले महीने का इंतज़ार नहीं हैं",
            "मुझे सच में अगले महीने का इंतज़ार नहीं हैं",
            "मुझे अगले महीने मज़ें नहीं करने हैं",
            "मुझे सच में अगले महीने मज़ें नहीं करने हैं",
            "मुझे फ़िर से अगले महीने बाहर नहीं जाना हैं",
            "मुझे सच में फ़िर से अगले महीने बाहर नहीं जाना हैं"

    }}, {{}, {"मुझे गरमी का मौसम अच्छा लगता हैं",
            "मुझे सच में गरमी का मौसम अच्छा लगता हैं",
            "मुझे थोड़ी धूप चाहिए",
            "मुझे सच में थोड़ी धूप चाहिए",
            "मुझे और समय के लिए गरमी का मौसम चाहिए",
            "मुझे सच में फ़िर से गरमी का मौसम चाहिए",
            "मुझे गरमी का मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में गरमी का मौसम अच्छा नहीं लगता हैं",
            "मुझे धूप नहीं चाहिए",
            "मुझे सच में धूप नहीं चाहिए",
            "मुझे और समय के लिए गरमी का मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से गरमी का मौसम नहीं चाहिए"

    }, {"मुझे बरसात का मौसम अच्छा लगता हैं",
            "मुझे सच में बरसात का मौसम अच्छा लगता हैं",
            "मुझे थोड़ी बारिश चाहिए",
            "मुझे सच में थोड़ी बारिश चाहिए",
            "मुझे और समय के लिए बरसात का मौसम चाहिए",
            "मुझे सच में फ़िर से बरसात का मौसम चाहिए",
            "मुझे बरसात का मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में बरसात का मौसम अच्छा नहीं लगता हैं",
            "मुझे बारिश नहीं चाहिए",
            "मुझे सच में बारिश नहीं चाहिए",
            "मुझे और समय के लिए बरसात का मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से बरसात का मौसम नहीं चाहिए"

    }, {"मुझे धुंधला मौसम अच्छा लगता हैं",
            "मुझे सच में धुंधला मौसम अच्छा लगता हैं",
            "मुझे धुंधला मौसम चाहिए",
            "मुझे सच में धुंधला मौसम चाहिए",
            "मुझे और समय के लिए धुंधला मौसम चाहिए",
            "मुझे सच में फ़िर से धुंधला मौसम चाहिए",
            "मुझे धुंधला मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में धुंधला मौसम अच्छा नहीं लगता हैं",
            "मुझे धुंधला मौसम नहीं चाहिए",
            "मुझे सच में धुंधला मौसम नहीं चाहिए",
            "मुझे और समय के लिए धुंधला मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से धुंधला मौसम नहीं चाहिए"

    }, {"मुझे तूफ़ानी मौसम अच्छा लगता हैं",
            "मुझे सच में तूफ़ानी मौसम अच्छा लगता हैं",
            "मुझे तूफ़ानी मौसम चाहिए",
            "मुझे सच में तूफ़ानी मौसम चाहिए",
            "मुझे और समय के लिए तूफ़ानी मौसम चाहिए",
            "मुझे सच में फ़िर से तूफ़ानी मौसम चाहिए",
            "मुझे तूफ़ानी मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में तूफ़ानी मौसम अच्छा नहीं लगता हैं",
            "मुझे तूफ़ानी मौसम नहीं चाहिए",
            "मुझे सच में तूफ़ानी मौसम नहीं चाहिए",
            "मुझे और समय के लिए तूफ़ानी मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से तूफ़ानी मौसम नहीं चाहिए"

    }, {"मुझे घना मौसम अच्छा लगता हैं",
            "मुझे सच में घना मौसम अच्छा लगता हैं",
            "मुझे घना मौसम चाहिए",
            "मुझे सच में घना मौसम चाहिए",
            "मुझे और समय के लिए घना मौसम चाहिए",
            "मुझे सच में फ़िर से घना मौसम चाहिए",
            "मुझे घना मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में घना मौसम अच्छा नहीं लगता हैं",
            "मुझे घना मौसम नहीं चाहिए",
            "मुझे सच में घना मौसम नहीं चाहिए",
            "मुझे और समय के लिए घना मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से घना मौसम नहीं चाहिए"

    }, {"मुझे बर्फीला मौसम अच्छा लगता हैं",
            "मुझे सच में बर्फीला मौसम अच्छा लगता हैं",
            "मुझे थोड़ा बर्फ़ चाहिए",
            "मुझे सच में थोड़ा बर्फ़ चाहिए",
            "मुझे और समय के लिए बर्फीला मौसम चाहिए",
            "मुझे सच में फ़िर से बर्फीला मौसम चाहिए",
            "मुझे बर्फीला मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में बर्फीला मौसम अच्छा नहीं लगता हैं",
            "मुझे बर्फ़ नहीं चाहिए",
            "मुझे सच में बर्फ़ नहीं चाहिए",
            "मुझे और समय के लिए बर्फीला मौसम नहीं चाहिए",
            "मुझे सच में फ़िर से बर्फीला मौसम नहीं चाहिए"

    }}, {{}, {"मुझे वसंत ऋतु अच्छा लगता हैं",
            "मुझे सच में वसंत ऋतु अच्छा लगता हैं",
            "मुझे वसंत ऋतु चाहिए",
            "मुझे सच में वसंत ऋतु चाहिए",
            "मुझे वसंत ऋतु और समय के लिए चाहिए",
            "मुझे सच में वसंत ऋतु थोड़े और समय के लिए चाहिए",
            "मुझे वसंत ऋतु अच्छा नहीं लगता हैं",
            "मुझे सच में वसंत ऋतु अच्छा नहीं लगता हैं",
            "मुझे वसंत ऋतु नहीं चाहिए",
            "मुझे सच में वसंत ऋतु नहीं चाहिए",
            "मुझे वसंत ऋतु और समय के लिए नहीं चाहिए",
            "मुझे सच में वसंत ऋतु और समय के लिए नहीं चाहिए"

    }, {"मुझे गरमी का मौसम अच्छा लगता हैं",
            "मुझे सच में गरमी का मौसम अच्छा लगता हैं",
            "मुझे गरमी का मौसम चाहिए",
            "मुझे सच में गरमी का मौसम चाहिए",
            "मुझे मौसम और गरम चाहिए",
            "मुझे सच में मौसम थोड़ा और गरम चाहिए",
            "मुझे गरमी का मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में गरमी का मौसम अच्छा नहीं लगता हैं",
            "मुझे गरमी का मौसम नहीं चाहिए",
            "मुझे सच में गरमी का मौसम नहीं चाहिए",
            "मुझे मौसम और गरम नहीं चाहिए",
            "मुझे सच में मौसम और गरम नहीं चाहिए"

    }, {"मुझे बरसात का मौसम अच्छा लगता हैं",
            "मुझे सच में बरसात का मौसम अच्छा लगता हैं",
            "मुझे बरसात का मौसम चाहिए",
            "मुझे सच में बरसात का मौसम चाहिए",
            "मुझे और बारिश चाहिए",
            "मुझे सच में थोड़ी और बारिश चाहिए",
            "मुझे बरसात का मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में बरसात का मौसम अच्छा नहीं लगता हैं",
            "मुझे बरसात का मौसम नहीं चाहिए",
            "मुझे सच में बरसात का मौसम नहीं चाहिए",
            "मुझे और बारिश नहीं चाहिए",
            "मुझे सच में और बारिश नहीं चाहिए"

    }, {"मुझे शरद ऋतु अच्छा लगता हैं",
            "मुझे सच में शरद ऋतु अच्छा लगता हैं",
            "मुझे शरद ऋतु चाहिए",
            "मुझे सच में शरद ऋतु चाहिए",
            "मुझे शरद ऋतु और समय के लिए चाहिए",
            "मुझे सच में शरद ऋतु थोड़े और समय के लिए चाहिए",
            "मुझे शरद ऋतु अच्छा नहीं लगता हैं",
            "मुझे सच में शरद ऋतु अच्छा नहीं लगता हैं",
            "मुझे शरद ऋतु नहीं चाहिए",
            "मुझे सच में शरद ऋतु नहीं चाहिए",
            "मुझे शरद ऋतु और समय के लिए नहीं चाहिए",
            "मुझे सच में शरद ऋतु और समय के लिए नहीं चाहिए"

    }, {"मुझे सर्दी का मौसम अच्छा लगता हैं",
            "मुझे सच में सर्दी का मौसम अच्छा लगता हैं",
            "मुझे सर्दी का मौसम चाहिए",
            "मुझे सच में सर्दी का मौसम चाहिए",
            "मुझे मौसम और ठंडा चाहिए",
            "मुझे सच में मौसम थोड़ा और ठंडा चाहिए",
            "मुझे सर्दी का मौसम अच्छा नहीं लगता हैं",
            "मुझे सच में सर्दी का मौसम अच्छा नहीं लगता हैं",
            "मुझे सर्दी का मौसम नहीं चाहिए",
            "मुझे सच में सर्दी का मौसम नहीं चाहिए ",
            "मुझे मौसम और ठंडा नहीं चाहिए",
            "मुझे सच में मौसम और ठंडा नहीं चाहिए",

    }}, {{"मुझे दिवाली का त्योहार  अच्छा लगता हैं",
            "मुझे सच में दिवाली का त्योहार मनाना अच्छा लगता हैं",
            "मुझे दिवाली के वक्त मज़े करने हैं",
            "मुझे सच में दिवाली के वक्त मज़े करने हैं",
            "मुझे दिवाली के वक्त और मज़े करने हैं",
            "मुझे सच में दिवाली के वक्त थोड़े और मज़े करने हैं",
            "मुझे दिवाली का त्योहार  अच्छा नहीं  लगता हैं",
            "मुझे सच में दिवाली का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे दिवाली के वक्त मज़े नहीं करने हैं",
            "मुझे सच में दिवाली के वक्त मज़े नहीं करने हैं",
            "मुझे दिवाली के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में दिवाली के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे गणेश चतुर्थी का त्योहार  अच्छा लगता हैं",
            "मुझे सच में गणेश चतुर्थी का त्योहार मनाना अच्छा लगता हैं",
            "मुझे गणेश चतुर्थी के वक्त मज़े करने हैं",
            "मुझे सच में गणेश चतुर्थी के वक्त मज़े करने हैं",
            "मुझे गणेश चतुर्थी के वक्त और मज़े करने हैं",
            "मुझे सच में गणेश चतुर्थी के वक्त थोड़े और मज़े करने हैं",
            "मुझे गणेश चतुर्थी का त्योहार  अच्छा नहीं लगता  हैं",
            "मुझे सच में गणेश चतुर्थी का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे गणेश चतुर्थी के वक्त मज़े नहीं करने हैं",
            "मुझे सच में गणेश चतुर्थी के वक्त मज़े नहीं करने हैं",
            "मुझे गणेश चतुर्थी के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में गणेश चतुर्थी के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे क्रिसमस का त्योहार  अच्छा लगता हैं",
            "मुझे सच में क्रिसमस का त्योहार मनाना अच्छा लगता हैं",
            "मुझे क्रिसमस के वक्त मज़े करने हैं",
            "मुझे सच में क्रिसमस के वक्त मज़े करने हैं",
            "मुझे क्रिसमस के वक्त और मज़े करने हैं",
            "मुझे सच में क्रिसमस के वक्त थोड़े और मज़े करने हैं",
            "मुझे क्रिसमस का  त्योहार  अच्छा नहीं लगता  हैं",
            "मुझे सच में क्रिसमस का  त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे क्रिसमस के वक्त मज़े नहीं करने हैं",
            "मुझे सच में क्रिसमस के वक्त मज़े नहीं करने हैं",
            "मुझे क्रिसमस के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में क्रिसमस के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे दशहरे का त्योहार अच्छा लगता  हैं",
            "मुझे सच में दशहरे का त्योहार मनाना अच्छा लगता हैं",
            "मुझे दशहरे के वक्त मज़े करने हैं",
            "मुझे सच में दशहरे के वक्त मज़े करने हैं",
            "मुझे दशहरे के वक्त और मज़े करने हैं",
            "मुझे सच में दशहरे के वक्त थोड़े और मज़े करने हैं",
            "मुझे दशहरे का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में दशहरे का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे दशहरे के वक्त मज़े नहीं करने हैं",
            "मुझे सच में दशहरे के वक्त मज़े नहीं करने हैं",
            "मुझे दशहरे के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में दशहरे के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे  संक्रांति का त्योहार अच्छा लगता  हैं",
            "मुझे सच में  संक्रांति का त्योहार मनाना अच्छा लगता हैं",
            "मुझे  संक्रांति के वक्त मज़े करने हैं",
            "मुझे सच में  संक्रांति के वक्त मज़े करने हैं",
            "मुझे  संक्रांति के वक्त और मज़े करने हैं",
            "मुझे सच में  संक्रांति के वक्त थोड़े और मज़े करने हैं",
            "मुझे  संक्रांति का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में  संक्रांति का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे  संक्रांति के वक्त मज़े नहीं करने हैं",
            "मुझे सच में  संक्रांति के वक्त मज़े नहीं करने हैं",
            "मुझे  संक्रांति के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में  संक्रांति के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे होली का त्योहार अच्छा लगता  हैं",
            "मुझे सच में होली का त्योहार मनाना अच्छा लगता हैं",
            "मुझे होली के वक्त मज़े करने हैं",
            "मुझे सच में होली के वक्त मज़े करने हैं",
            "मुझे होली के वक्त और मज़े करने हैं",
            "मुझे सच में होली के वक्त थोड़े और मज़े करने हैं",
            "मुझे होली का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में होली का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे होली के वक्त मज़े नहीं करने हैं",
            "मुझे सच में होली के वक्त मज़े नहीं करने हैं",
            "मुझे होली के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में होली के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे ईद का त्योहार अच्छा लगता  हैं",
            "मुझे सच में ईद का त्योहार मनाना अच्छा लगता हैं",
            "मुझे ईद के वक्त मज़े करने हैं",
            "मुझे सच में ईद के वक्त मज़े करने हैं",
            "मुझे ईद के वक्त और मज़े करने हैं",
            "मुझे सच में ईद के वक्त थोड़े और मज़े करने हैं",
            "मुझे ईद का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में ईद का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे ईद के वक्त मज़े नहीं करने हैं",
            "मुझे सच में ईद के वक्त मज़े नहीं करने हैं",
            "मुझे ईद के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में ईद के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे गुड फ्राइडे का त्योहार अच्छा लगता  हैं",
            "मुझे सच में गुड फ्राइडे का त्योहार मनाना अच्छा लगता हैं",
            "मुझे गुड फ्राइडे के वक्त मज़े करने हैं",
            "मुझे सच में गुड फ्राइडे के वक्त मज़े करने हैं",
            "मुझे गुड फ्राइडे के वक्त और मज़े करने हैं",
            "मुझे सच में गुड फ्राइडे के वक्त थोड़े और मज़े करने हैं",
            "मुझे गुड फ्राइडे का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में गुड फ्राइडे का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे गुड फ्राइडे के वक्त मज़े नहीं करने हैं",
            "मुझे सच में गुड फ्राइडे के वक्त मज़े नहीं करने हैं",
            "मुझे गुड फ्राइडे के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में गुड फ्राइडे के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे गुड़ी पाड़वा का त्योहार अच्छा लगता  हैं",
            "मुझे सच में गुड़ी पाड़वा का त्योहार मनाना अच्छा लगता हैं",
            "मुझे गुड़ी पाड़वा के वक्त मज़े करने हैं",
            "मुझे सच में गुड़ी पाड़वा के वक्त मज़े करने हैं",
            "मुझे गुड़ी पाड़वा के वक्त और मज़े करने हैं",
            "मुझे सच में गुड़ी पाड़वा के वक्त थोड़े और मज़े करने हैं",
            "मुझे गुड़ी पाड़वा का त्योहार अच्छा नहीं लगता हैं",
            "मुझे सच में गुड़ी पाड़वा का त्योहार मनाना अच्छा नहीं लगता हैं",
            "मुझे गुड़ी पाड़वा के वक्त मज़े नहीं करने हैं",
            "मुझे सच में गुड़ी पाड़वा के वक्त मज़े नहीं करने हैं",
            "मुझे गुड़ी पाड़वा के वक्त और मज़े नहीं करने हैं",
            "मुझे सच में गुड़ी पाड़वा के वक्त और मज़े नहीं करने हैं"

    }, {"मुझे गणतंत्र दिवस की छुट्टी अच्छी लगती हैं",
            "मुझे सच में गणतंत्र दिवस की छुट्टी अच्छी लगती हैं",
            "मुझे गणतंत्र दिवस की छुट्टी का आनंद लेना हैं",
            "मुझे सच में गणतंत्र दिवस की छुट्टी का आनंद लेना हैं",
            "मुझे गणतंत्र दिवस छुट्टी के और मज़े लेने हैं",
            "मुझे सच में गणतंत्र दिवस छुट्टी के थोड़े और मज़े लेने हैं",
            "मुझे गणतंत्र दिवस की छुट्टी अच्छी नहीं लगती हैं",
            "मुझे सच में गणतंत्र दिवस की छुट्टी अच्छी नहीं लगती हैं",
            "मुझे गणतंत्र दिवस की छुट्टी का आनंद नहीं लेना हैं",
            "मुझे सच में गणतंत्र दिवस की छुट्टी का आनंद नहीं लेना हैं",
            "मुझे गणतंत्र दिवस छुट्टी के और मज़े नहीं लेने हैं",
            "मुझे सच में गणतंत्र दिवस छुट्टी के और मज़े नहीं लेने हैं"

    }, {"मुझे स्वतंत्रता दिवस की छुट्टी अच्छी लगती हैं",
            "मुझे सच में स्वतंत्रता दिवस की छुट्टी अच्छी लगती हैं",
            "मुझे स्वतंत्रता दिवस की छुट्टी का आनंद लेना हैं",
            "मुझे सच में स्वतंत्रता दिवस की छुट्टी का आनंद लेना हैं",
            "मुझे स्वतंत्रता दिवस छुट्टी के और मज़े लेने हैं",
            "मुझे सच में स्वतंत्रता दिवस छुट्टी के थोड़े और मज़े लेने हैं",
            "मुझे स्वतंत्रता दिवस की छुट्टी अच्छी नहीं लगती हैं",
            "मुझे सच में स्वतंत्रता दिवस की छुट्टी अच्छी नहीं लगती हैं",
            "मुझे स्वतंत्रता दिवस की छुट्टी का आनंद नहीं लेना हैं",
            "मुझे सच में स्वतंत्रता दिवस की छुट्टी का आनंद नहीं लेना हैं",
            "मुझे स्वतंत्रता दिवस छुट्टी के और मज़े नहीं लेने हैं",
            "मुझे सच में स्वतंत्रता दिवस छुट्टी के और मज़े नहीं लेने हैं"

    }, {"मुझे नया साल अच्छा लगता  हैं",
            "मुझे सच में नया साल मनाना अच्छा लगता हैं",
            "मुझे नये साल में मज़े करने हैं",
            "मुझे सच में नये साल में मज़े करने हैं",
            "मुझे नये साल में और मज़े करने हैं",
            "मुझे सच में नये साल में थोड़े और मज़े करने हैं",
            "मुझे नया साल अच्छा लगता  हैं",
            "मुझे सच में नया साल मनाना अच्छा नहीं लगता हैं",
            "मुझे नये साल में मज़े नहीं करने हैं",
            "मुझे सच में नये साल में मज़े नहीं करने हैं",
            "मुझे नये साल में और मज़े नहीं करने हैं",
            "मुझे सच में नये साल में और मज़े नहीं करने हैं"

    }}, {{"मुझे अपना जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपना जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने जन्मदिन पर मज़े करने हैं",
            "मुझे अपने जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपना जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपना जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपनी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी माँ के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी माँ के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी माँ के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी माँ के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने पिताजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने पिताजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने पिताजी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने पिताजी के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने पिताजी के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने पिताजी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने पिताजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने पिताजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने पिताजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने पिताजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने पिताजी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने पिताजी के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने भाई का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने भाई का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने भाई के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने भाई के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने भाई के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने भाई के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने भाई का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने भाई का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने भाई के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने भाई के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने भाई के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने भाई के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपनी बहन का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी बहन का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी बहन के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी बहन के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी बहन के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी बहन के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी बहन का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी बहन का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी बहन के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी बहन के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी बहन के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी बहन के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने बड़े पापा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने बड़े पापा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने बड़े पापा के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने बड़े पापा के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने बड़े पापा के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने बड़े पापा के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने बड़े पापा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने बड़े पापा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने बड़े पापा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने बड़े पापा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने बड़े पापा के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने बड़े पापा के जन्मदिन पर और मज़े नहीं करने हैं"

    },  {"मुझे अपनी बड़ी मम्मी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी बड़ी मम्मी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी बड़ी मम्मी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी बड़ी मम्मीँ के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी बड़ी मम्मीँ के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी बड़ी मम्मी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी बड़ी मम्मी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी बड़ी मम्मी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी बड़ी मम्मी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी बड़ी मम्मी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी बड़ी मम्मी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी बड़ी मम्मी के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने दादाजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने दादाजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने दादाजी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने दादाजी के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने दादाजी के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने दादाजी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने दादाजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने दादाजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने दादाजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने दादाजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने दादाजी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने दादाजी के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपनी दादी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी दादी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी दादी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी दादी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी दादी माँ के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी दादी माँ के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी दादी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी दादी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी दादी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी दादी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी दादी माँ के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी दादी माँ के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपने नानाजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने नानाजी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने नानाजी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने नानाजी के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने नानाजी के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने नानाजी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने नानाजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने नानाजी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने नानाजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने नानाजी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने नानाजी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने नानाजी के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपनी नानी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी नानी माँ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी नानी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी नानी माँ के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी नानी माँ के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी नानी माँ के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी नानी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी नानी माँ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी नानी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी नानी माँ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी नानी माँ के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी नानी माँ के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने चाचा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने चाचा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने चाचा के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने चाचा के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने चाचा के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने चाचा के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने चाचा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने चाचा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने चाचा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने चाचा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने चाचा के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने चाचा के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपनी चाची का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी चाची का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी चाची के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी चाची के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी चाची के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी चाची के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी चाची का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी चाची का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी चाची के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी चाची के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी चाची के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी चाची के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपने मामा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने मामा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने मामा के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने मामा के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने मामा के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने मामा के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने मामा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने मामा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने मामा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने मामा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने मामा के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने मामा के जन्मदिन पर और मज़े नहीं करने हैं"

    }, {"मुझे अपनी मामी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी मामी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी मामी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी मामी के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी मामी के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी मामी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी मामी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी मामी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी मामी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी मामी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी मामी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी मामी के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपनी बुआ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी बुआ का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी बुआ के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी बुआ के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी बुआ के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी बुआ के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी बुआ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी बुआ का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी बुआ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी बुआ के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी बुआ के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी बुआ के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपने फ़ुफ़ा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने फ़ुफ़ा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने फ़ुफ़ा के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने फ़ुफ़ा के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने फ़ुफ़ा के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने फ़ुफ़ा के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने फ़ुफ़ा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने फ़ुफ़ा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने फ़ुफ़ा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने फ़ुफ़ा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने फ़ुफ़ा के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने फ़ुफ़ा के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपनी मौसी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपनी मौसी का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपनी मौसी के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपनी मौसी के जन्मदिन पर मज़े करने हैं",
            "मुझे अपनी मौसी के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपनी मौसी के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपनी मौसी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपनी मौसी का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपनी मौसी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपनी मौसी के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपनी मौसी के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपनी मौसी के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपने मौसा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने मौसा का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने मौसा के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने मौसा के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने मौसा के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने मौसा के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने मौसा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने मौसा का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने मौसा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने मौसा के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने मौसा के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने मौसा के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपने मित्र का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने मित्र का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने मित्र के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने मित्र के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने मित्र के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने मित्र के जन्मदिन पर थोड़े और मज़े करने हैं",
            "मुझे अपने मित्र का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने मित्र का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने मित्र के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने मित्र के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने मित्र के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने मित्र के जन्मदिन पर और मज़े नहीं करने हैं"

    },{"मुझे अपने शिक्षक का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे सच में अपने शिक्षक का जन्मदिन मनाना अच्छा लगता हैं",
            "मुझे अपने शिक्षक के जन्मदिन पर मज़े करने हैं",
            "मुझे सच में अपने शिक्षक के जन्मदिन पर मज़े करने हैं",
            "मुझे अपने शिक्षक के जन्मदिन पर और मज़े करने हैं",
            "मुझे सच में अपने शिक्षक के जन्मदिन पर थोड़े और  मज़े करने हैं",
            "मुझे अपने शिक्षक का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे सच में अपने शिक्षक का जन्मदिन मनाना अच्छा नहीं लगता हैं",
            "मुझे अपने शिक्षक के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे सच में अपने शिक्षक के जन्मदिन पर मज़े नहीं करने हैं",
            "मुझे अपने शिक्षक के जन्मदिन पर और मज़े नहीं करने हैं",
            "मुझे सच में अपने शिक्षक के जन्मदिन पर और  मज़े नहीं करने हैं"
    }}}, {{{}}}};
}
