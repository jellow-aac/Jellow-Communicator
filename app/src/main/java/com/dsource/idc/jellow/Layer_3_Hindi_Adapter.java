package com.dsource.idc.jellow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by ekalpa on 7/14/2016.
 */

public class Layer_3_Hindi_Adapter extends android.support.v7.widget.RecyclerView.Adapter<Layer_3_Hindi_Adapter.MyViewHolder> {
    private Context mContext;
    //private int layer_1_id, layer_2_id;
    public static Integer[] mThumbIds = new Integer[100];
    public static String[] belowText = new String[100];

    public static int more = 0;
    CircularImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
    private SessionManager session;

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;

    // Keep all Images in array

    public Integer[] greet_feel_greetings = {
            R.drawable.thankyou, R.drawable.thankyou,
            R.drawable.bye, R.drawable.goodmorning,
            R.drawable.goodafternoon, R.drawable.goodevening, R.drawable.goodnight, R.drawable.highfive
            , R.drawable.nicetomeetyou, R.drawable.howareyou, R.drawable.howwasyourday, R.drawable.howareyou
    };

    public Integer[] greet_feel_feelings = {
            R.drawable.happy, R.drawable.sad,
            R.drawable.angry, R.drawable.scared,
            R.drawable.surprised, R.drawable.irritated, R.drawable.confused, R.drawable.ashamed,
            R.drawable.disappointed, R.drawable.bored,
            R.drawable.worried, R.drawable.stressed,
            R.drawable.tired, R.drawable.hot, R.drawable.cold, R.drawable.sick, R.drawable.hurt
    };

    public Integer[] greet_feel_requests = {
            R.drawable.please, R.drawable.thankyou,
            R.drawable.welcome, R.drawable.pleasegiveme,
            R.drawable.pleasetellmeagain, R.drawable.pleaseshowme, R.drawable.ineedabreak, R.drawable.iamalldone,
            R.drawable.excuseme,
            R.drawable.iamsorry, R.drawable.idontunderstand,
            R.drawable.pleaseshare, R.drawable.pleaseslowdown, R.drawable.ineedhelp, R.drawable.comehere, R.drawable.takeme
    };

    public Integer[] greet_feel_questions = {
            R.drawable.how, R.drawable.when,
            R.drawable.where, R.drawable.why,
            R.drawable.what, R.drawable.who, R.drawable.howmuch, R.drawable.howmany, R.drawable.howlong
    };

    public Integer[] daily_activities_brushing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_brushing_text =
            {"Rinse mouth", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush back teeth", "Brush tongue", "Rinse mouth", "All done"};

    public Integer[] daily_activities_toilet = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_toilet_text =
            {"Pull pants down", "Sit on toilet", "Wash bottom", "Flush toilet", "Pull pants up", "Wash hands", "All done"};

    public Integer[] daily_activities_bathing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_bathing_text =
            {"Remove clothes", "Turn on water", "Get in the shower", "Wet body", "Put soap", "Shampoo hair", "Put face wash", "Wash hair",
                    "Wash body", "Turn off water", "Dry hair", "Dry face", "Dry body", "Put on clothes", "All done"};

    public Integer[] daily_activities_clothes_access = {
            R.drawable.tshirt_clothing, R.drawable.dress_clothing,
            R.drawable.skirt_clothing, R.drawable.jeans_clothing,
            R.drawable.pants_clothing, R.drawable.leggings, R.drawable.slacks_clothing, R.drawable.shorts,
            R.drawable.underwear_clothing, R.drawable.slippers_clothing,
            R.drawable.shoes_clothing, R.drawable.socks_school,
            R.drawable.nightwear_clothing, R.drawable.shirt, R.drawable.tshirt_clothing, R.drawable.dress_clothing,
            R.drawable.pants_clothing, R.drawable.slacks_clothing,
            R.drawable.leggings, R.drawable.shorts,
            R.drawable.salwarkhamez_clothing, R.drawable.sweater_clothing, R.drawable.jcket_clothing, R.drawable.scarf_clothing,
            R.drawable.cap_clothing, R.drawable.belt_clothing,
            R.drawable.raincoat_clothing, R.drawable.spectacles_clothing,
            R.drawable.watch_clothing, R.drawable.earings_clothing, R.drawable.bracelet_clothing, R.drawable.necklace_clothing,
            R.drawable.bindi, R.drawable.slippers_clothing, R.drawable.myclothesaretight, R.drawable.myclothesareloose, R.drawable.ineedhelpremovingclothes, R.drawable.ineedhelpputtingclotheson,
    };

    public Integer[] daily_activities_get_ready = {
            R.drawable.combhair, R.drawable.facewash,
            R.drawable.cuttingnails, R.drawable.blowingnose,
            R.drawable.soap, R.drawable.shampoo
    };

    public Integer[] daily_activities_sleep = {
            R.drawable.door_homeobjects, R.drawable.fan,
            R.drawable.light, R.drawable.window,
            R.drawable.bed, R.drawable.pilllow, R.drawable.blanket, R.drawable.feelinghot, R.drawable.feelingcold
    };

    public Integer[] daily_activities_therapy = {
            R.drawable.exercise, R.drawable.swing,
            R.drawable.trampoline, R.drawable.swissball,
            R.drawable.blanket, R.drawable.ballpit, R.drawable.handmovements, R.drawable.legexercises, R.drawable.bodyvest

    };

    public Integer[] daily_activities_morning_schedule = {
            R.drawable.wakeup, R.drawable.washface,
            R.drawable.gototoilet, R.drawable.brushteeth,
            R.drawable.removeclothes, R.drawable.haveabath, R.drawable.getdressed, R.drawable.combhair,
            R.drawable.eatbreakfast, R.drawable.packlunch, R.drawable.packbagpack, R.drawable.gradient, R.drawable.haveagreatday
    };

    public static String[] daily_activities_morning_schedule_text =
            {"Wake up", "Wash face", "Go to bathroom", "Brush teeth", "Remove clothes", "Have a bath", "Get dressed", "Comb hair",
                    "Eat breakfast", "Pack lunchbox", "Pack school bag", "Go to school", "Have a great day!"};

    public Integer[] daily_activities_bedtime_schedule = {
            R.drawable.eatdinner, R.drawable.wearnightsuit,
            R.drawable.brushteeth, R.drawable.readstory,
            R.drawable.saygoodnight, R.drawable.sayprayres, R.drawable.sweetdreams
    };

    public static String[] daily_activities_bedtime_schedule_text =
            {"Eat dinner", "Wear night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"};

    public Integer[] eating_foods_drinks_breakfast = {
            R.drawable.bread, R.drawable.cornflakes,
            R.drawable.aloopuri, R.drawable.eggs,
            R.drawable.poha, R.drawable.upma, R.drawable.khichdi, R.drawable.idli,
            R.drawable.dosa, R.drawable.paratha,
            R.drawable.omelette, R.drawable.meduvada,
            R.drawable.porriage, R.drawable.sandwich, R.drawable.chutney, R.drawable.sambar, R.drawable.uttapam
    };

    public Integer[] eating_food_drinks_lunch_dinner = {
            R.drawable.roti, R.drawable.sabzi,
            R.drawable.rice, R.drawable.dal,
            R.drawable.dalkhichdi, R.drawable.raita,
            R.drawable.paratha, R.drawable.curd,
            R.drawable.fish, R.drawable.chicken,
            R.drawable.pork, R.drawable.mutton,
            R.drawable.crab, R.drawable.turkey, R.drawable.pizza, R.drawable.salad,
            R.drawable.soup, R.drawable.pasta,
            R.drawable.noodles,
            R.drawable.italian, R.drawable.pavbhaji,
            R.drawable.bhakri
    };

    public Integer[] eating_food_drinks_sweets = {
            R.drawable.cake, R.drawable.icecream,
            R.drawable.gajarhalwa, R.drawable.gulabjamun,
            R.drawable.ladoo, R.drawable.barfi,
            R.drawable.jalebi, R.drawable.fruitsalad,
            R.drawable.rasgulla, R.drawable.sheera
    };

    public Integer[] eating_food_drinks_snacks = {
            R.drawable.biscuits, R.drawable.chaat,
            R.drawable.chocolate, R.drawable.chips,
            R.drawable.sandwich, R.drawable.noodles,
            R.drawable.cheese, R.drawable.peanuts
    };

    public Integer[] eating_food_drinks_fruits = {
            R.drawable.apple, R.drawable.banana,
            R.drawable.grapes, R.drawable.guaua,
            R.drawable.mango, R.drawable.orange,
            R.drawable.pineapple, R.drawable.strawberry,
            R.drawable.blueberry, R.drawable.pomogranate,
            R.drawable.watermelon, R.drawable.pear,
            R.drawable.papaya, R.drawable.muskmelon,
            R.drawable.chikoo, R.drawable.jackfruit, R.drawable.cherry
    };

    public Integer[] eating_food_drinks_drinks = {
            R.drawable.water, R.drawable.milk,
            R.drawable.bournvita,
            R.drawable.mangojuice, R.drawable.applejuice,
            R.drawable.orangejuice, R.drawable.lemonjuice, R.drawable.pineapplejuice,
            R.drawable.pepsi,
            R.drawable.cocacola, R.drawable.mirinda,
            R.drawable.fanta, R.drawable.mazzaa,
            R.drawable.sprite, R.drawable.mountaindew, R.drawable.milkshakes,
            R.drawable.chocolatemilkshake,
            R.drawable.strawberrymilkshake, R.drawable.bananamilkshake,
            R.drawable.mangomilkshake, R.drawable.chikoomilkshake,
            R.drawable.tea, R.drawable.coffee, R.drawable.coldcoffee, R.drawable.energydrink
    };

    public Integer[] eating_food_drinks_cutlery = {
            R.drawable.bowl_cutlery, R.drawable.plate_cutlery,
            R.drawable.spoon_cutlery, R.drawable.fork_cutlery,
            R.drawable.knife_cutlery, R.drawable.mug_cutlery, R.drawable.cup_cutlery, R.drawable.glass_cutlery
    };

    public Integer[] eating_food_drinks_add_ons = {
            R.drawable.butter_addons, R.drawable.jam_addons,
            R.drawable.salt_addons, R.drawable.pepper_addons,
            R.drawable.sugar_addons, R.drawable.ketchup_addons,
            R.drawable.pickle, R.drawable.papad_addons, R.drawable.masala_addons
    };

    public Integer[] fun_indoor_games = {
            R.drawable.puzzles, R.drawable.boradgames,
            R.drawable.blocks, R.drawable.lego,
            R.drawable.chess, R.drawable.snakesandladders, R.drawable.scrabble, R.drawable.videogames,
            R.drawable.doll, R.drawable.actionfigure,
            R.drawable.teddy, R.drawable.cars,
            R.drawable.truck, R.drawable.artandcraft_activities, R.drawable.playwithme
    };

    public Integer[] fun_outdoor_games = {
            R.drawable.level2_places_playground, R.drawable.level2_places_park, R.drawable.swing,
            R.drawable.slide, R.drawable.seesaw, R.drawable.merrygoround, R.drawable.hideandseek, R.drawable.batandball,
            R.drawable.statue,
            R.drawable.lockandkey, R.drawable.catchcatch,
            R.drawable.kite, R.drawable.choorpolice, R.drawable.marbles, R.drawable.walking, R.drawable.cycle_transport,
            R.drawable.running, R.drawable.swimming

    };

    public Integer[] fun_sports = {
            R.drawable.cricket_activities, R.drawable.badminton_activities,
            R.drawable.tennis_activities, R.drawable.basketball_activities,
            R.drawable.dodgeball_activities, R.drawable.vollyball_activities, R.drawable.khokho, R.drawable.football_activities, R.drawable.kabaddi, R.drawable.gymnastics, R.drawable.swimming
    };

    public Integer[] fun_tv = {
            R.drawable.nextchannel, R.drawable.previouschannel,
            R.drawable.volumeup, R.drawable.volumedown
    };

    public Integer[] fun_music = {
            R.drawable.changemusic, R.drawable.dancing,
            R.drawable.volumeup, R.drawable.volumedown
    };

    public Integer[] fun_activities = {
            R.drawable.draw_activities, R.drawable.painting_activities,
            R.drawable.read_activities, R.drawable.write_activities,
            R.drawable.artandcraft_activities, R.drawable.drama_activities, R.drawable.dancing, R.drawable.musicalinstrument_activities
    };

    public Integer[] learning_animals_birds = {
            R.drawable.dog_animals, R.drawable.cat_animals,
            R.drawable.elephant_animals, R.drawable.lion_animals,
            R.drawable.parrot, R.drawable.rabbit_animals, R.drawable.cow_animals, R.drawable.duck_animals,
            R.drawable.donkey, R.drawable.ant,
            R.drawable.tiger, R.drawable.monkey,
            R.drawable.pigeon, R.drawable.cockroach, R.drawable.crow, R.drawable.horse,
            R.drawable.deer, R.drawable.owl,
            R.drawable.wolf, R.drawable.fox,
            R.drawable.bear, R.drawable.sheep, R.drawable.goat, R.drawable.pig,
            R.drawable.fly, R.drawable.giraffee,
            R.drawable.zebra, R.drawable.mosquito,
            R.drawable.buffallo, R.drawable.mouse, R.drawable.snake, R.drawable.crocodile,  R.drawable.honeybee, R.drawable.hippo,
            R.drawable.rhino, R.drawable.fish,
            R.drawable.penguin, R.drawable.seal, R.drawable.dolphin, R.drawable.whale,
            R.drawable.shark, R.drawable.tortoise, R.drawable.sparrow, R.drawable.eagle, R.drawable.hawk, R.drawable.vulture
    };

    public Integer[] learning_body_parts = {
            R.drawable.head, R.drawable.hair_bodyparts,
            R.drawable.eye_bodyparts, R.drawable.nose_bodyparts,
            R.drawable.ear_bodyparts, R.drawable.mouth_bodyparts, R.drawable.tongue, R.drawable.neck, R.drawable.shoulder, R.drawable.elbow,
            R.drawable.wrist, R.drawable.hand_bodyparts,
            R.drawable.fingers, R.drawable.back, R.drawable.stomach_bodyparts, R.drawable.hips, R.drawable.knee_bodyparts, R.drawable.ankle, R.drawable.legs, R.drawable.toes
    };

    public Integer[] learning_books = {
            R.drawable.bedtimebook, R.drawable.comics_books,
            R.drawable.rhymebook, R.drawable.drawingbook_books,
            R.drawable.storybook, R.drawable.picturebook, R.drawable.mysterybook, R.drawable.adventurebook,  R.drawable.notebook_books, R.drawable.maths,
            R.drawable.science_books, R.drawable.history,
            R.drawable.geography_books, R.drawable.socialscience, R.drawable.english, R.drawable.hindi_books,  R.drawable.marathi_books, R.drawable.wordbook, R.drawable.favouritebook
    };

    public Integer[] learning_colours = {
            R.drawable.black_colour, R.drawable.blue_colour,
            R.drawable.brown_colour, R.drawable.greem_colour,
            R.drawable.redcolour, R.drawable.silvercolour,
            R.drawable.white_colour, R.drawable.yellow_colour,
            R.drawable.golden_colour, R.drawable.pink_colour, R.drawable.orange_colour, R.drawable.purple_colour, R.drawable.grey_colour
    };

    public Integer[] learning_shapes = {
            R.drawable.standingline_shapes, R.drawable.sleepingline_shapes,
            R.drawable.slantingline_shapes, R.drawable.circle_shapes,
            R.drawable.rectangle_shapes, R.drawable.square_shapes, R.drawable.triangle_shapes, R.drawable.star_shapes, R.drawable.heart_shape, R.drawable.trapezium_shapes,
            R.drawable.cube_shapes, R.drawable.rhombus_shapes,
            R.drawable.hexagon_shapes, R.drawable.oval_shapes, R.drawable.diamond_shapes, R.drawable.pentagon_shapes, R.drawable.freeform_shapes
    };

    public Integer[] learning_stationary = {
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary, R.drawable.crayon_stationary,
            R.drawable.blankpaper_stationary, R.drawable.colouredpaper_stationary, R.drawable.sissors, R.drawable.pencillead,
            R.drawable.compass, R.drawable.divider,
            R.drawable.stapler, R.drawable.upin,
            R.drawable.cellotape, R.drawable.pencilbox
    };

    public Integer[] learning_school_objects = {
            R.drawable.bag_school, R.drawable.lunchbox,
            R.drawable.waterbottle_school, R.drawable.pencilbox,
            R.drawable.homework_school, R.drawable.notebook_books,
            R.drawable.textbooks_school, R.drawable.uniform_school,  R.drawable.shoes_school, R.drawable.socks_school,
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary, R.drawable.chalk
    };

    public Integer[] learning_home_objects = {
            R.drawable.window, R.drawable.door_homeobjects,
            R.drawable.fan, R.drawable.lamp_homeobjects,
            R.drawable.desk_homeobjects, R.drawable.cupboard,
            R.drawable.table, R.drawable.chair_homeobjects,  R.drawable.toilet, R.drawable.kitchen,
            R.drawable.livingroom, R.drawable.bedroom,
            R.drawable.playroom, R.drawable.bathroom,
            R.drawable.balcony, R.drawable.studyroom, R.drawable.bed, R.drawable.level2_fun_tv,
            R.drawable.computer, R.drawable.sofa,
            R.drawable.fridge, R.drawable.microwave,
            R.drawable.washingmachie, R.drawable.vaccumcleaner, R.drawable.clock_homeobjects, R.drawable.tubelight
    };

    public Integer[] learning_transportation = {
            R.drawable.bus_transport, R.drawable.schoolbus_transport,
            R.drawable.car_transport, R.drawable.cycle_transport,
            R.drawable.train_transport, R.drawable.rickshaw_transport,
            R.drawable.motorbike_transport, R.drawable.plane_transport, R.drawable.shop_transport
    };

    public Integer[] time_weather_time = {
            R.drawable.currenttime, R.drawable.today,
            R.drawable.yesterday, R.drawable.tomorrow,
            R.drawable.morning, R.drawable.afternoon,
            R.drawable.goodevening, R.drawable.night
    };

    public Integer[] time_weather_day = {
            R.drawable.currentday, R.drawable.somvaar,
            R.drawable.mangalvaaar, R.drawable.budhvaar,
            R.drawable.guruvaar, R.drawable.sukravaar,
            R.drawable.shanivaar, R.drawable.ravivaar
    };

    public Integer[] time_weather_month = {
            R.drawable.whatisthemonth, R.drawable.januaryhindi,
            R.drawable.februaryhjindi, R.drawable.marchhindi,
            R.drawable.aprilhindi, R.drawable.mayhindi,
            R.drawable.junehindi, R.drawable.julyhindi,  R.drawable.augusthindi, R.drawable.septemberhindi,
            R.drawable.octoberhindi, R.drawable.novemberhindi,
            R.drawable.decemberhindi, R.drawable.currentmonthhindi,
            R.drawable.previousmonthhindi, R.drawable.nextmonthhindi
    };

    public Integer[] time_weather_weather = {
            R.drawable.todaysweather, R.drawable.afternoon,
            R.drawable.rainy, R.drawable.cloudy,
            R.drawable.windy, R.drawable.foggy,
            R.drawable.snowy
    };

    public Integer[] time_weather_seasons = {
            R.drawable.currentseason, R.drawable.spring,
            R.drawable.afternoon, R.drawable.rainy,
            R.drawable.autumn, R.drawable.winter
    };

    public Integer[] time_weather_holidays_festivals = {
            R.drawable.diwali, R.drawable.ganeshchaturthi,
            R.drawable.christmas, R.drawable.duserra,
            R.drawable.mararsankranti, R.drawable.holi,
            R.drawable.eid, R.drawable.goodfriday,R.drawable.gudipadwa,
            R.drawable.republicday, R.drawable.independance, R.drawable.newyear
    };

    public Integer[] time_weather_birthdays = {
            R.drawable.mybirthday, R.drawable.momsbirthday,
            R.drawable.dadsbirthday, R.drawable.brothersbirthday,
            R.drawable.sistersbirthday, R.drawable.badepapa,
            R.drawable.badimummy, R.drawable.grandfathersbirthday,  R.drawable.grandmomsbirthday, R.drawable.nanaji,
            R.drawable.naniji, R.drawable.chacha,
            R.drawable.chachi, R.drawable.mama, R.drawable.mami,
            R.drawable.bua,  R.drawable.fufaji, R.drawable.mausi, R.drawable.mausa,
            R.drawable.friendsbirthday, R.drawable.teachersbirthday
    };


    public static String[] greet_feel_greetings_text =
            {"नमस्ते!", "नमस्कार", "अलविदा", "शुभ प्रभात", "शुभ दिन", "शुभ संध्या", "शुभ रात्रि", "ताली दो",  "आपसे मिलकर अच्छा लगा", "आप कैसे हैं?", "आपका दिन कैसा था?", "आपके क्या हाल हैं?"};
    public static String[] greet_feel_feelings_text =
            {"खुश", "उदास", "गुस्सा", "डर", "हैरान", "चिढ़ा हुआ", "उलझन", "शर्मिंदा", "निराश", "बोर", "चिंता", "तनावग्रस्त", "थका हुआ", "गरम", "ठंडा", "बीमार", "दुःखी"};
    public static String[] greet_feel_requests_text =
            {"कृपया", "धन्यवाद", "आपका स्वागत हैं", "कृपया दिजीए", "कृपया फिर से बताईए", "कृपया दिखाईए", "मुझे एक ब्रेक चाहिए", "मैंने खत्म कर दिया",  "क्षमा कीजिये!", "मुझे माफ करें", "मुझे समझ में नहीं आया", "मेरे साथ बाँटे", "कृपया थोड़ा धीरे जाइए", "मुझे मदद की ज़रूरत हैं", "कृपया यहाँ आइये", "कृपया मुझे लेके जाईए"};
    public static String[] greet_feel_questions_text =
            {"कैसे?", "कब?", "कहॉं पे?", "क्यूं?", "क्या?", "कौन?", "कितने?", "कितना लंबा?", "कितनी देर?"};
    public static String[] daily_activities_clothes_access_text =
            {"टी-शर्ट बदलना ", "फ्रॉक बदलना", "स्कर्ट बदलना", "जीन्स बदलना", "पैंट बदलना", "लेगिंग बदलना", "स्लैक्स बदलना", "शॉर्ट्स बदलना", "इनरवियर बदलना", "जूते बदलना", "बूट बदलना", "मोज़े बदलना", "रात के कपड़े पहनना", "शर्ट ", "टी-शर्ट ", "फ्रॉक ",  "पैंट ", "स्लैक्स ", "लेगिंग ", "शॉर्ट्स ", "सलवार कमीज़ ", "स्वेटर ", "जैकेट ", "दुपट्टा ",  "टोपी ", "बेल्ट ", "रेनकोट ", "चश्मा ", "घड़ी ", "कान की बाली ", "कंगन", "हार ",  "बिंदी ", "चप्पल ", "मेरे कपड़े टाइट हैं", "मेरे कपड़े ढीले हैं", "कपड़े निकालने में मदद", "कपड़े पैहनने में मदद"};
    public static String[] daily_activities_get_ready_text =
            {"कंघी करना", "फेस वॉश", "नाखून काटना", "नाक साफ करना", "साबुन", "शैम्पू"};
    public static String[] daily_activities_sleep_text =
            {"दरवाज़ा", "पंखा", "लाईट", "खिड़की", "बिस्तर", "तकिया", "कंबल", "गर्मी", "ठंडक"};
    public static String[] daily_activities_therapy_text =
            {"कसरत", "झूला", "ट्रेम्पोलिन", "स्विस बॉल", "कंबल", "बॉल पिट", "हातों की कसरत", "पैरों की कसरत", "बॉडी वेस्ट"};
    public static String[] foods_drinks_breakfast_text =
            {"ब्रेड", "कॉर्नफ़्लेक्स", "आलू पूरी", "अंडे", "पोहा", "उपमा", "खिचड़ी", "इड़ली ",  "डोसा", "पराठा", "आमलेट", "मेदु वड़ा", "दलिया", "सैंडविच", "चटनी", "सांबर", "उत्तपा"};
    public static String[] food_drinks_lunch_dinner_text =
            {"रोटी", "सब्ज़ी", "चावल", "दाल", "दालखिचड़ी", "रायता", "पराठा", "दही",  "मछली", "चिकन", "पोर्क", "मटन", "केकड़े का मांस", "टर्की", "पिज्जा", "सलाद ",  "सूप ", "पास्ता ", "नूडल्स", "इटालियन खाना", "पाव भाजी", "भाकरी"};
    public static String[] food_drinks_sweets_text =
            {"केक", "आइसक्रीम", "गाजर का हलवा", "गुलाब जामुन", "लड्डू", "बर्फी", "जलेबी", "फलों का सलाद",  "रसगुल्ला", "शीरा"};
    public static String[] food_drinks_snacks_text =
            {"बिस्कुट", "चाट", "चॉकलेट", "वेफर्स", "सैंडविच", "नूडल्स", "चीज़", "नट्स"};
    public static String[] food_drinks_fruits_text =
            {"सेब", "केला", "अंगूर", "अमरूद", "आम", "संतरा", "अनानास", "स्ट्रॉबेरी",  "बेर", "अनार", "तरबूज", "पेर", "पपीता", "खरबूजा", "चिकू", "पनस", "चेरी"};
    public static String[] food_drinks_drinks_text =
            {"पानी", "दूध", "बॉर्नविटा", "आम का ज्यूस", "सेब का ज्यूस", "संतरे का ज्यूस", "नींबू का ज्यूस", "अनानास का ज्यूस",  "पेप्सी", "कोका कोला", "मिरिंडा", "फैंटा", "माझा", "स्प्राइट ", "माउंटेन ड्यू", "मिल्कशेक",  "चॉकलेट मिल्कशेक", "स्ट्रॉबेरी मिल्कशेक ", "केला मिल्कशेक ", "आम मिल्कशेक ", "चिकू मिल्कशेक", "चाय ", "कॉफी ", "कोल्ड कॉफी", "एनर्जी ड्रिंक्स"};
    public static String[] food_drinks_cutlery_text =
            {"कटोरा", "प्लेट", "चम्मच", "काँटे का चम्मच", "चाकू", "मग", "कप", "ग्लास"};
    public static String[] food_drinks_add_ons_text =
            {"मक्खन", "जाम", "नमक", "काली मिर्च", "चीनी", "सॉस", "आचार", "पापड़", "मसाला"};
    public static String[] fun_indoor_games_text =
            {"पज़ल्स", "बोर्ड खेल", "ब्लॉक्स", "लेगो", "शतरंज", "सांप और सीढ़ी", "स्क्रैबल", "विडियो गेम",  "गुड़ियाँ", "एक्शन फिगर्स", "सॉफ्ट टॉयज़", "कार", "ट्रक", "आर्ट-क्राफ्ट", "मेरे साथ खेलो"};
    public static String[] fun_outdoor_games_text =
            {"खेल का मैदान", "पार्क", "झूला", "स्लाईड ", "सी-सॉ", "मेरी-गो-राउंड", "लुकाछिपी", "बल्ला और गेंद", "स्टैचू", "ताला और चाबी", "पकड़ा-पकड़ी", "पतंग", "चोर-पुलिस", "कंचे", "चलना", "साइकिल", "दौड़ना", "तैरना"};
    public static String[] fun_sports_text =
            {"क्रिकेट ", "बैडमिंटन", "टेनिस ", "बास्केटबॉल", "डॉजबॉल", "वॉलीबॉल", "खो-खो", "फुटबॉल ",  "कबड्डी", "जिमनास्टिक", "तैरना"};
    public static String[] fun_tv_text =
            {"अगला चैनल", "पिछला चैनल", "उँची आवाज़", "धीमी आवाज़"};
    public static String[] fun_music_text =
            {"संगीत बदलना", "नाचना", "उँची आवाज़", "धीमी आवाज़"};
    public static String[] fun_activities_text =
            {"चित्र बनाना", "रंग भरना", "पढ़ना", "लिखना", "आर्ट-क्राफ्ट", "नाटक", "नाचना", "संगीत बजाना"};
    public static String[] learning_animals_birds_text =
            {"कुत्ता", "बिल्ली", "हाथी", "शेर ", "तोता ", "खरगोश ", "गाय", "बत्तख", "गधा ", "चींटी", "बाघ", "बंदर", "कबूतर ", "तिलचट्टा", "कौआ ", "घोड़ा ",  "हिरण ", "उल्लू", "भेड़िया ", "लोमड़ी", "भालू ", "भेड़ ", "बकरी ", "सुअर ", "मक्खी", "जिराफ़", "ज़ेब्रा", "मच्छर", "भैंस", "चूहा", "साँप", "मगरमच्छ ",  "मधुमक्खी ", "दरियाई घोड़ा ", "गेंडा", "मछली ", "पेंगविन ", "सील ", "डॉल्फिन ", "व्हेल ", "शार्क ", "कछुआ ", "चिडिया", "गरुड़", "हॉक", "गिद्ध"};
    public static String[] learning_body_parts_text =
            {"सिर", "बाल", "आँखें", "नाक", "कान", "मुँह", "जीभ", "गर्दन", "कंधा", "कोहनी", "कलाई", "हाथ", "उंगलियाँ", "पीठ", "पेट", "कूल्हे का जोड़", "घुटना", "घुटिका", "पैर", "पैर की उंगलियाँ"};
    public static String[] learning_books_text =
            {"सोने के समय की कहानीयाँ", "हास्यमय किताबें", "काव्यमय किताबें", "चित्रकला की किताबें", "कहानियों की किताबें", "चित्रों की किताबें", "जासूसी किताबें", "साहसी किताबें",  "पाठशाला की नोटबुक", "गणित किताब", "विज्ञान किताब", "इतिहास किताब", "भूगोल किताब", "सामाजिक अध्ययन किताब ", "अंग्रेजी किताब", "हिंदी किताब",  "मराठी किताब", "पाठ्यपुस्तकें", "पसंदीदा किताब"};
    public static String[] learning_colours_text =
            {"काला", "नीला", "भूरा", "हरा", "लाल", "चाँदी", "सफेद", "पीला",  "सुनहरा", "गुलाबी", "नारंगी ", "जामुनी", "ग्रे "};
    public static String[] learning_shapes_text =
            {"सीधी रेखा ", "आड़ी रेखा", "तिरछी रेखा", "गोल", "आयत", "चौकोर", "त्रिकोण", "तारा",  "दिल", "असमांतरभुज कोण", "घनाकार", "समचतुर्भुज कोण", "षट्कोण", "अंडाकार", "ईंट", "पंचकोण", "मुक्ताकार"};
    public static String[] learning_stationary_text =
            {"पेंसिल", "पेन", "स्केल", "रबर", "शार्पनर", "क्रेयान", "कोरा कागज", "रंगीन कागज",  "कैंची", "सीसा", "कम्पास", "विभाजक", "स्टेप्लर", "यू-पिन", "सेलो टेप", "कम्पास बॉक्स"};
    public static String[] learning_school_objects_text =
            {"बैग", "खाने का डिब्बा", "पानी की बोतल", "कम्पास बॉक्स", "गृहपाठ", "कापी", "पाठ्यपुस्तकें", "यूनिफार्म",  "जूते ", "मोज़े ", "पेंसिल", "पेन", "स्केल", "रबर", "शार्पनर", "चॉक"};
    public static String[] learning_home_objects_text =
            {"खिड़की", "दरवाज़ा", "पंखा", "लैंप", "डेस्क", "अलमारी", "टेबल", "कुर्सी","शौचालय", "रसोईघर", "हॉल", "बेडरूम", "खेलने का कमरा", "बाथरूम", "बालकनी", "पढ़ाई का कमरा", "बिस्तर", "टीवी", "संगणक", "सोफ़ा", "फ्रिज़", "माइक्रोवेव", "वॉशिंग मशीन", "वैक्यूम क्लीनर",  "घड़ी", "ट्यूब लाइट"};
    public static String[] learning_transportation_text =
            {"बस", "स्कूल  बस", "कार", "साइकिल", "रेलगाड़ी", "रिक्शा", "मोटर साइकिल ", "हवाई जहाज़", "जहाज़"};
    public static String[] time_weather_time_text =
            {"समय क्या हुआ हैं?", "आज़", "कल", "कल", "सुबह", "दोपहर", "शाम", "रात"};
    public static String[] time_weather_day_text =
            {"आज कौनसा दिन है?", "सोमवार", "मंगलवार", "बुधवार", "गुरूवार", "शुक्रवार", "शनिवार", "रविवार"};
    public static String[] time_weather_month_text =
            {"वर्तमान महीना कौनसा हैं ?", "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई",  "अगस्त", "सितंबर ", "अक्टूबर", "नवंबर", "दिसंबर", "यह महीना", "पिछला महीना", "अगला महीना"};
    public static String[] time_weather_weather_text =
            {"आज का मौसम क्या हैं? ", "गरम", "बरसात", "धुंधला", "तूफ़ानी", "घना", "बर्फीला"};
    public static String[] time_weather_seasons_text =
            {"वर्तमान ऋतु कौनसा हैं? ", "वसंत ऋतु", "ग्रीष्म ऋतु", "वर्षा ऋतु", "शरद ऋतु", "शीत ऋतु"};
    public static String[] time_weather_holidays_festivals_text =
            {"दिवाली", "गणेश चतुर्थी", "क्रिसमस ", "दशहरा", "मकर संक्रांति", "होली", "ईद", "गुड फ्राइडे", "गुड़ी पाड़वा", "गणतंत्र दिवस", "स्वतंत्रता दिवस", "नया साल"};
    public static String[] time_weather_brthdays_text =
            {"मेरा जन्मदिन", "माँ का जन्मदिन", "पिताजी का जन्मदिन", "भाई का जन्मदिन", "बहन का जन्मदिन", "बड़े पापा का जन्मदिन", "बड़ी मम्मी का जन्मदिन", "दादाज़ी का जन्मदिन",
                    "दादी माँ का जन्मदिन", "नानाज़ी का जन्मदिन", "नानी माँ का जन्मदिन", "चाचा का जन्मदिन", "चाची का जन्मदिन", "मामा का जन्मदिन", "मामी का जन्मदिन", "बुआ का जन्मदिन",
                    "फ़ुफ़ा का जन्मदिन", "मौसी का जन्मदिन", "मौसा का जन्मदिन", "मित्र का जन्मदिन", "शिक्षक का जन्मदिन"};
    int k = 0, kk = 0, l = 0, j = 0,u=0;
    int store=0;


    // Constructor
    public Layer_3_Hindi_Adapter(Context c, int layer_1_id, int layer_2_id,int sort[]) {
        mContext = c;
        session = new SessionManager(mContext);
        /*this.layer_1_id = layer_1_id;
        this.layer_2_id = layer_2_id;*/

        if (layer_1_id == 0) {
            if (layer_2_id == 0) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_greetings.length; j++) {
                    temp1[j] = greet_feel_greetings[sort[j]];
                    temp2[j] = greet_feel_greetings_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
//       belowText = greet_feel_greetings_text;
//        mThumbIds = greet_feel_greetings;
                Log.d("lengtth",store+"");
                Log.d("lengtth 1",sort.length+"");
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 1) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_feelings.length; j++) {
                    temp1[j] = greet_feel_feelings[sort[j]];
                    temp2[j] = greet_feel_feelings_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_requests.length; j++) {
                    temp1[j] = greet_feel_requests[sort[j]];
                    temp2[j] = greet_feel_requests_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_questions.length; j++) {
                    temp1[j] = greet_feel_questions[sort[j]];
                    temp2[j] = greet_feel_questions_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        } else if (layer_1_id == 1) {
            if (layer_2_id == 0) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_brushing.length; j++) {
                    temp1[j] = daily_activities_brushing[sort[j]];
                    temp2[j] = daily_activities_brushing_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 1) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_toilet.length; j++) {
                    temp1[j] = daily_activities_toilet[sort[j]];
                    temp2[j] = daily_activities_toilet_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_bathing.length; j++) {
                    temp1[j] = daily_activities_bathing[sort[j]];
                    temp2[j] = daily_activities_bathing_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_clothes_access.length; j++) {
                    temp1[j] = daily_activities_clothes_access[sort[j]];
                    temp2[j] = daily_activities_clothes_access_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 4) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_get_ready.length; j++) {
                    temp1[j] = daily_activities_get_ready[sort[j]];
                    temp2[j] = daily_activities_get_ready_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 5) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_sleep.length; j++) {
                    temp1[j] = daily_activities_sleep[sort[j]];
                    temp2[j] = daily_activities_sleep_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 6) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_therapy.length; j++) {
                    temp1[j] = daily_activities_therapy[sort[j]];
                    temp2[j] = daily_activities_therapy_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 7) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_morning_schedule.length; j++) {
                    temp1[j] = daily_activities_morning_schedule[sort[j]];
                    temp2[j] = daily_activities_morning_schedule_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 8) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_bedtime_schedule.length; j++) {
                    temp1[j] = daily_activities_bedtime_schedule[sort[j]];
                    temp2[j] = daily_activities_bedtime_schedule_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        } else if (layer_1_id == 2) {
            if (layer_2_id == 0) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_foods_drinks_breakfast.length; j++) {
                    temp1[j] = eating_foods_drinks_breakfast[sort[j]];
                    temp2[j] = foods_drinks_breakfast_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 1) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_lunch_dinner.length; j++) {
                    temp1[j] = eating_food_drinks_lunch_dinner[sort[j]];
                    temp2[j] = food_drinks_lunch_dinner_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_sweets.length; j++) {
                    temp1[j] = eating_food_drinks_sweets[sort[j]];
                    temp2[j] = food_drinks_sweets_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_snacks.length; j++) {
                    temp1[j] = eating_food_drinks_snacks[sort[j]];
                    temp2[j] = food_drinks_snacks_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 4) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_fruits.length; j++) {
                    temp1[j] = eating_food_drinks_fruits[sort[j]];
                    temp2[j] = food_drinks_fruits_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 5) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_drinks.length; j++) {
                    temp1[j] = eating_food_drinks_drinks[sort[j]];
                    temp2[j] = food_drinks_drinks_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 6) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_cutlery.length; j++) {
                    temp1[j] = eating_food_drinks_cutlery[sort[j]];
                    temp2[j] = food_drinks_cutlery_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (layer_2_id == 7) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_add_ons.length; j++) {
                    temp1[j] = eating_food_drinks_add_ons[sort[j]];
                    temp2[j] = food_drinks_add_ons_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }

        } else
        if(layer_1_id == 3)
        {
            if(layer_2_id == 0)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_indoor_games.length; j++) {
                    temp1[j] = fun_indoor_games[sort[j]];
                    temp2[j] = fun_indoor_games_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 1)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_outdoor_games.length; j++) {
                    temp1[j] = fun_outdoor_games[sort[j]];
                    temp2[j] = fun_outdoor_games_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 2)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_sports.length; j++) {
                    temp1[j] = fun_sports[sort[j]];
                    temp2[j] = fun_sports_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else
            if(layer_2_id == 3)
            {

                mThumbIds = fun_tv;
                belowText = fun_tv_text;
            }
            else
            if(layer_2_id == 4)
            {

                mThumbIds = fun_music;
                belowText = fun_music_text;
            }else
        if(layer_2_id == 5)
        {
            Integer[] temp1 = new Integer[sort.length];
            String[] temp2 = new String[sort.length];
            for (int j = 0 ; j< fun_activities.length; j++) {
                temp1[j] = fun_activities[sort[j]];
                temp2[j] = fun_activities_text[sort[j]];
                Log.e("printing ",temp2[j]+" ");
                store = j;
            }
            store = store+1;
            mThumbIds = Arrays.copyOfRange(temp1, 0, store);
            belowText = Arrays.copyOfRange(temp2, 0, store);
        }
        }

        else if(layer_1_id ==4)
        {
            if(layer_2_id == 0)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_animals_birds.length; j++) {
                    temp1[j] = learning_animals_birds[sort[j]];
                    temp2[j] = learning_animals_birds_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else if(layer_2_id == 1)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_body_parts.length; j++) {
                    temp1[j] = learning_body_parts[sort[j]];
                    temp2[j] = learning_body_parts_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else
            if(layer_2_id == 2)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_books.length; j++) {
                    temp1[j] = learning_books[sort[j]];
                    temp2[j] = learning_books_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 3)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_colours.length; j++) {
                    temp1[j] = learning_colours[sort[j]];
                    temp2[j] = learning_colours_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else
            if(layer_2_id == 4)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_shapes.length; j++) {
                    temp1[j] = learning_shapes[sort[j]];
                    temp2[j] = learning_shapes_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 5)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_stationary.length; j++) {
                    temp1[j] = learning_stationary[sort[j]];
                    temp2[j] = learning_stationary_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 6)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_school_objects.length; j++) {
                    temp1[j] = learning_school_objects[sort[j]];
                    temp2[j] = learning_school_objects_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 7)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_home_objects.length; j++) {
                    temp1[j] = learning_home_objects[sort[j]];
                    temp2[j] = learning_home_objects_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 8)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_transportation.length; j++) {
                    temp1[j] = learning_transportation[sort[j]];
                    temp2[j] = learning_transportation_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }
        else if(layer_1_id == 7) {
                if(layer_2_id == 0)
                {

                    mThumbIds = time_weather_time;
                    belowText = time_weather_time_text;
                }else if(layer_2_id == 1)
                {

                    mThumbIds = time_weather_day;
                    belowText = time_weather_day_text;
                }
                else if(layer_2_id == 2)
                {

                    mThumbIds = time_weather_month;
                    belowText = time_weather_month_text;
                }
                else if(layer_2_id == 3)
                {

                    mThumbIds = time_weather_weather;
                    belowText = time_weather_weather_text;
                }
                else if(layer_2_id == 4)
                {

                    mThumbIds = time_weather_seasons;
                    belowText = time_weather_seasons_text;
                }
                else
            if(layer_2_id == 5)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< time_weather_holidays_festivals.length; j++) {
                    temp1[j] = time_weather_holidays_festivals[sort[j]];
                    temp2[j] = time_weather_holidays_festivals_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }

            else
            if(layer_2_id == 6)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< time_weather_birthdays.length; j++) {
                    temp1[j] = time_weather_birthdays[sort[j]];
                    temp2[j] = time_weather_brthdays_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        int position=-1;
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Mukta-Regular.ttf");
        public MyViewHolder(final View view) {

            super(view);
            if (session.getGridSize() == 0) {
                img1 = (CircularImageView) view.findViewById(R.id.icon1);
                img2 = (CircularImageView) view.findViewById(R.id.icon2);
                img3 = (CircularImageView) view.findViewById(R.id.icon3);


                tv1 = (TextView) view.findViewById(R.id.te1);
                tv2 = (TextView) view.findViewById(R.id.te2);
                tv3 = (TextView) view.findViewById(R.id.te3);


            }
            if(session.getGridSize()==1)
            {
                img1 = (CircularImageView)view.findViewById(R.id.icon1);
                img2 = (CircularImageView)view.findViewById(R.id.icon2);
                img3 = (CircularImageView)view.findViewById(R.id.icon3);
                img4 = (CircularImageView)view.findViewById(R.id.icon4);
                img5 = (CircularImageView)view.findViewById(R.id.icon5);
                img6 = (CircularImageView)view.findViewById(R.id.icon6);
                img7 = (CircularImageView)view.findViewById(R.id.icon7);
                img8 = (CircularImageView)view.findViewById(R.id.icon8);
                img9 = (CircularImageView)view.findViewById(R.id.icon9);
                tv1 = (TextView)view.findViewById(R.id.te1);
                tv2 = (TextView)view.findViewById(R.id.te2);
                tv3 = (TextView)view.findViewById(R.id.te3);

                tv1.setTypeface(custom_font);
                tv1.setTextColor(Color.rgb(64,64,64));
                tv2.setTypeface(custom_font);
                tv2.setTextColor(Color.rgb(64,64,64));
                tv3.setTypeface(custom_font);
                tv3.setTextColor(Color.rgb(64,64,64));

                tv4 = (TextView)view.findViewById(R.id.te4);
                tv5 = (TextView)view.findViewById(R.id.te5);
                tv6 = (TextView)view.findViewById(R.id.te6);
                tv7 = (TextView)view.findViewById(R.id.te7);
                tv8 = (TextView)view.findViewById(R.id.te8);
                tv9 = (TextView)view.findViewById(R.id.te9);
                tv4.setTypeface(custom_font);
                tv4.setTextColor(Color.rgb(64,64,64));
                tv5.setTypeface(custom_font);
                tv5.setTextColor(Color.rgb(64,64,64));
                tv6.setTypeface(custom_font);
                tv6.setTextColor(Color.rgb(64,64,64));
                tv7.setTypeface(custom_font);
                tv7.setTextColor(Color.rgb(64,64,64));
                tv8.setTypeface(custom_font);
                tv8.setTextColor(Color.rgb(64,64,64));
                tv9.setTypeface(custom_font);
                tv9.setTextColor(Color.rgb(64,64,64));
            }

        }
    }

    @Override
    public Layer_3_Hindi_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if (session.getGridSize()==0){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        }else if (dpHeight >= 720 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist233, parent, false);
        else if (dpWidth >640 && dpWidth <=1024 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist033, parent, false);
        else if (dpWidth > 600 && dpWidth <=640 && session.getGridSize()==1) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }else {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }

        return new Layer_3_Hindi_Adapter.MyViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position1) {
        int position;
        j=mThumbIds.length;
        // Log.d("Possss1",position+"");
        position=holder.getAdapterPosition();
        Log.d("Possss",position+"");

        if (session.getGridSize()==0) {
            k = position;
            if (k > 0) {
                j = j - position * 3;
                Log.d("kvalueold", k + " ");
                k = k + 3 * position - position;
                Log.d("kvalue", k + " ");

            }

            if (j > 0) {
                tv1.setText(belowText[k]);
                img1.setImageResource(mThumbIds[k]);
                j = j - 1;
            } else {
                tv1.setText(" ");
                img1.setImageResource(R.drawable.background_circle);
                img1.setEnabled(false);

            }
            if (j > 0) {
                tv2.setText(belowText[k + 1]);
                img2.setImageResource(mThumbIds[k + 1]);
                j = j - 1;
            } else {
                tv2.setText(" ");
                img2.setImageResource(R.drawable.background_circle);
                img2.setEnabled(false);

            }

            if (j > 0) {
                tv3.setText(belowText[k + 2]);

                img3.setImageResource(mThumbIds[k + 2]);
                j = j - 1;
            } else {
                tv3.setText(" ");
                img3.setImageResource(R.drawable.background_circle);
                img3.setEnabled(false);

            }
        }

        if (session.getGridSize()==1)
        {
            k = position;
            if (k > 0) {
                j = j - position * 9;
                Log.d("kvalueold", k + " ");
                k = k + 9 * position - position;
                Log.d("kvalue", k + " ");

            }

            if (j > 0) {
                tv1.setText(belowText[k]);
                img1.setImageResource(mThumbIds[k]);
                j = j - 1;
            } else {
                tv1.setText(" ");
                //   img1.setImageResource(0);
                img1.setImageResource(R.drawable.background_circle);
                img1.setEnabled(false);

            }
            if (j > 0) {
                tv2.setText(belowText[k + 1]);
                img2.setImageResource(mThumbIds[k + 1]);
                j = j - 1;
            } else {
                tv2.setText(" ");
                //img2.setImageResource(0);
                img2.setImageResource(R.drawable.background_circle);
                img2.setEnabled(false);

            }

            if (j > 0) {
                tv3.setText(belowText[k + 2]);

                img3.setImageResource(mThumbIds[k + 2]);
                j = j - 1;
            } else {
                tv3.setText(" ");
                //img3.setImageResource(0);
                img3.setImageResource(R.drawable.background_circle);
                img3.setEnabled(false);
            }
            if (j > 0) {
                tv4.setText(belowText[k + 3]);

                img4.setImageResource(mThumbIds[k + 3]);
                j = j - 1;
            } else {
                tv4.setText(" ");
                //img3.setImageResource(0);
                img4.setImageResource(R.drawable.background_circle);
                img4.setEnabled(false);
            }
            if (j > 0) {
                tv5.setText(belowText[k + 4]);

                img5.setImageResource(mThumbIds[k + 4]);
                j = j - 1;
            } else {
                tv5.setText(" ");
                //img3.setImageResource(0);
                img5.setImageResource(R.drawable.background_circle);
                img5.setEnabled(false);
            }if (j > 0) {
            tv6.setText(belowText[k + 5]);

            img6.setImageResource(mThumbIds[k + 5]);
            j = j - 1;
        } else {
            tv6.setText(" ");
            //img3.setImageResource(0);
            img6.setImageResource(R.drawable.background_circle);
            img6.setEnabled(false);
        }if (j > 0) {
            tv7.setText(belowText[k + 6]);

            img7.setImageResource(mThumbIds[k + 6]);
            j = j - 1;
        } else {
            tv7.setText(" ");
            //img3.setImageResource(0);
            img7.setImageResource(R.drawable.background_circle);
            img7.setEnabled(false);
        }if (j > 0) {
            tv8.setText(belowText[k + 7]);

            img8.setImageResource(mThumbIds[k + 7]);
            j = j - 1;
        } else {
            tv8.setText(" ");
            //img3.setImageResource(0);
            img8.setImageResource(R.drawable.background_circle);
            img8.setEnabled(false);
        }if (j > 0) {
            tv9.setText(belowText[k + 8]);

            img9.setImageResource(mThumbIds[k + 8]);
            j = j - 1;
        } else {
            tv9.setText(" ");
            //img3.setImageResource(0);
            img9.setImageResource(R.drawable.background_circle);
            img9.setEnabled(false);
        }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        System.out.println("MTHUMBSID"+(mThumbIds.length));
        if(session.getGridSize()==1)
        {
            kk = (mThumbIds.length % 9);
            System.out.println("kkkkkkkk" + kk);
            l = (mThumbIds.length / 9);
            System.out.println("llllll" + l);
            if (kk > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);

            Log.d("valueeeee",l+":");
        }
        if(session.getGridSize()==0) {
            kk = (mThumbIds.length % 3);
            System.out.println("kkkkkkkk" + kk);
            l = (mThumbIds.length / 3);
            System.out.println("llllll" + l);
            if (kk > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);

            Log.d("valueeeee", l + ":");
        }
        return l;
    }

   private void handleItemClick(String position1, int local) {
       System.out.print("CHECCCC "+position1);
       //((Main2LAyer) mContext).myfunction(position1,local);

   }

    private String putpos(String s) {
        String position="";
        position+=s;
        System.out.println("ppppp "+position);
        return position;
    }
}