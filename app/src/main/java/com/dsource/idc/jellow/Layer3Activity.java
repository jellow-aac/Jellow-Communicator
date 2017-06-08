package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsource.idc.jellow.Utility.AppPreferences;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Layer3Activity extends AppCompatActivity {
    int layer_1_id, layer_2_id;
    int x = -1, image_flag = -1;
    int clike = 0, cy = 0, cm = 0, cd = 0, cn = 0, cl = 0;
    int location=-2;
    int locayy = -1;
    int flag = 0, flag_keyboard = 0;
    View vi;
    ImageView like, dislike, add, minus, yes, no, home, keyboard, ttsButton, back;
    boolean a = true;
    private EditText et;
    private KeyListener originalKeyListener;
    TextToSpeech tts;
    private RecyclerView recyclerView; //new addition
    String[] myMusic;
    public static int more_count = 0;
    //public ImageAdapter imgad;
    private CircularImageView im1,im2,im3, im4,im5,im6,im7,im8,im9;
    private LinearLayout mLinearLayoutIconOne, mLinearLayoutIconTwo, mLinearLayoutIconThree;

    Integer[] color = {-5317, -12627531, -7617718, -2937298, -648053365, -1761607680};

    final String[] level1_english ={"Greet and Feel", "Daily Activities", "Eating", "Fun", "Learning", "People", "Places", "Time and Weather", "Help"};

    final String[][] level2_english = {
            {"Greetings", "Feelings", "Requests", "Questions"},
            {"Brushing", "Toilet", "Bathing", "Clothes", "Getting Ready", "Sleep", "Therapy", "Morning Routine", "Bedtime Routine"},
            {"Breakfast", "Lunch/Dinner", "Sweets", "Snacks", "Fruits", "Drinks", "Cutlery", "Add-ons"},
            {"Indoor Games", "Outdoor Games", "Sports", "TV", "Music", "Activities"},
            {"Animals & Birds", "Body", "Books", "Colors", "Shapes", "Stationery", "School Objects", "Home Objects", "Transport Modes"},
            {"Mother", "Daddy", "Brother", "Sister", "Grandfather", "Grandmother", "Uncle", "Aunt", "More", "Cousin", "Baby", "Friends", "Teacher", "Doctor", "Nurse", "Caregiver", "Stranger", "About Me"},
            {"My House", "School", "Mall", "Museum", "Hotel", "Theater", "Playground", "Park", "More", "Store", "Friend's House", "Relative's House", "Hospital", "Clinic", "Library", "Terrace"},
            {"Time", "Day", "Month", "Weather", "Seasons", "Festivals & Holidays", "Birthdays"},
            {"About Me", "I am hurt", "I feel sick", "I feel tired", "Help me do this", "Medicine", "Bandage", "Water"}};

    public static String[] greet_feel_greetings_text =
            {"Hi", "Hello", "Bye", "Good morning", "Good afternoon", "Good evening", "Good night", "High-five",  "Nice to meet you", "How are you?", "How was your day?", "How do you do?"};

    public static String[] greet_feel_feelings_text =
            {"Happy", "Sad", "Angry", "afraid", "Amazed", "Irritated", "Confused", "Ashamed",  "Disappointed", "bored", "Worried", "Stressed", "Tired", "Hot", "Cold", "Sick", "Hurt"};

    public static String[] greet_feel_requests_text =
            {"Please", "Thank you", "You're welcome", "Please give me", "Please tell me again", "Please show me", "I need a break", "I am all done",  "Excuse me", "I am sorry", "I don’t understand", "Please share with me", "Please slow down", "I need help", "Please come here", "Please take me"};

    public static String[] greet_feel_questions_text =
            {"How?", "When?", "Where?", "Why?", "What?", "Who?", "How much?", "How many?", "How long?"};

    public static String[] daily_activities_brushing_text =
            {"Rinse mouth", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush back teeth", "Brush tongue", "Rinse mouth", "All done"};

    public static String[] daily_activities_toilet_text =
            {"Pull pants down", "Sit on toilet", "Wash bottom", "Flushh toilet", "Pull pants up", "Wash hands", "All done"};

    public static String[] daily_activities_bathing_text =
            {"Remove clothes", "Turn on water", "Get in the shower", "Wet body", "Put soap", "Shampoo हैर", "Put face wash", "Wash हैर",  "Wash body", "Turn off water", "Dry हैर", "Dry face", "Dry body", "Put on clothes", "All done"};

    public static String[] daily_activities_clothes_access_text =
            {"Change t-shirt", "Change frock", "Change skirt", "Change jeans", "Change pants", "Change leggings", "Change slacks", "Change shorts", "Change inner wear", "Change footwear", "Change shoes", "Change socks", "wear night clothes", "Shirt", "T-shirt", "Frock",
                    "Pants", "slacks", "Leggings", "Shorts", "salwarkameez", "Sweater", "Jacket", "Scarf",  "Cap", "Belt", "Raincoat", "Spectacles", "Wrist watch", "Earrings", "brace let", "Necklace", "बिंदी", "Chappals", "My clothes are tight", "My clothes are loose", "I need help removing clothes", "I need help putting on clothes"};

    public static String[] daily_activities_get_ready_text =
            {"Comb हैर", "Face wash", "Cut nails", "Blow nose", "Soap", "Shampoo"};

    public static String[] daily_activities_sleep_text =
            {"Door", "Fan", "Light", "Window", "bed", "Pillows", "Blanket", "Feeling warm", "Feeling cold"};

    public static String[] daily_activities_therapy_text =
            {"Exercises", "Swing", "Trampoline", "Swiss ball", "Blanket", "Ball pit", "Hand activities", "Leg exercises", "Body vests"};

    public static String[] daily_activities_morning_schedule_text =
            {"Wake up", "Wash face", "Go to bathroom", "Brush teeth", "Remove clothes", "Have a बाथ", "Get dressed", "Comb हैर",  "Eat brekfust", "Pack lunch box", "Pack school bag", "Go to school", "Have a great day!"};

    public static String[] daily_activities_bedtime_schedule_text =
            {"Eat dinner", "wear night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"};

    public static String[] foods_drinks_breakfast_text =
            {"Bread", "Cornflakes", "Aaloo Poori", "Eggs", "Poha", "Upma", "khichdi", "Idli",  "dosaa", "paraathaa", "omlette", "मेदु वड़ा", "Porridge", "Sandwich", "Chutney", "saambaar", "Uutappam"};

    public static String[] food_drinks_lunch_dinner_text =
            {"Roti", "Sabzi", "Rice", "Dal", "Dal khichdi", "Raaigh ta", "paraathaa", "Curd",  "Fish", "Chicken", "Pork", "Mutton", "crabbb meat", "Turkey", "Pizza", "Salad",  "Soup", "pastaa", "Noodles", "Italian", "pav bhaji", "Bhakri"};

    public static String[] food_drinks_sweets_text =
            {"Cake", "Icecream", "Gajar halva", "Gulab jamun", "Lud doo", "Barfi", "Jalebi", "Fruit salad",  "rass gulla", "sheeraa"};

    public static String[] food_drinks_snacks_text =
            {"Biscuits", "Chaat", "Chocolate", "Wafers", "Sandwich", "Noodles", "Cheese", "Nuts"};

    public static String[] food_drinks_fruits_text =
            {"Apple", "Banana", "Grapes", "Guava", "Mango", "Orange", "Pineapple", "Strawberry",  "Blueberry", "Pomegranate", "Watermelon", "pear", "Papaya", "Muskmelon", "Chikoo", "Jackfruit", "Cherry"};

    public static String[] food_drinks_drinks_text =
            {"Water", "Milk", "Bournvita", "Mango juice", "Apple juice", "Orange juice", "Lemon juice", "Pineapple juice",  "Pepsi", "cocacola", "Mirinda", "Fanta", "maaza", "Sprite", "Mountain dew", "Milk shake",  "Chocolate milk shake", "Strawberry milk shake", "Banana milk shake", "Mango milk shake", "Chikoo milk shake", "tea", "Coffee", "Cold coffee", "Energy drink"};

    public static String[] food_drinks_cutlery_text =
            {"Bowl", "Plate", "Spoon", "Fork", "Knife", "Mug", "Cup", "Glass"};

    public static String[] food_drinks_add_ons_text =
            {"Butter", "Jam", "Salt", "Pepper", "Sugar", "Sauce", "Pickle", "Papad", "Masala"};

    public static String[] fun_indoor_games_text =
            {"Puzzle", "Board game", "Blocks", "Legos", "Chess", "Snakes and Ladders", "Scrabble", "Videogame",  "Doll", "Action figure", "Soft toy", "Car", "Truck", "Art and craft", "Play with me"};

    public static String[] fun_outdoor_games_text =
            {"Playground", "Park", "Swing", "Slide", "See-saw", "Merry-go-round", "Hide and seek", "Bat and ball",  "Statue", "Lock and key", "Catch-catch", "Kite", "Chor-police", "Marbles", "Walk", "Cycle",  "Run", "Swim"};

    public static String[] fun_sports_text =
            {"Cricket", "Badminton", "Tennis", "basketball", "Dodgeball", "Volleyball", "Kho-kho", "Football", "kabbbaddi", "gym nastics", "Swimming"};

    public static String[] fun_tv_text =
            {"Next channel", "Previous channel", "Volume up", "Volume down"};

    public static String[] fun_music_text =
            {"Change music", "Lets dance", "Volume up", "Volume down"};

    public static String[] fun_activities_text =
            {"Draw", "Paint", "Read", "Write", "Arts and crafts", "Drama", "Dance", "Make music"};

    public static String[] learning_animals_birds_text =
            {"Dog", "Cat", "Elephant", "Lion", "Parrot", "Rabbit", "Cow", "Duck", "Donkey", "Ant", "Tiger", "Monkey", "Pigeon", "Cockroach", "Crow", "Horse",  "Deer", "Owl", "Wolf", "Fox", "Bear", "Sheep", "Goat", "Pig",  "Fly", "Giraffe", "zebra", "Mosquito", "Buffalo", "Mouse", "Snake", "crocodile",  "Bee", "Hippopotamus", "Rhinoceros", "Fish", "Penguin", "Seal", "Dolphin", "Whale",  "Shark", "tortoyse", "Sparrow", "Eagle", "Hawk", "Vulture"};

    public static String[] learning_body_parts_text =
            {"Head", "हैर", "Eyes", "Nose", "Ears", "mouth", "Tongue", "Neck",  "Shoulder", "Elbow", "Wrist", "Hands", "Fingers", "Back", "Stomach", "Hip",  "knee", "Ankle", "Legs", "Toes"};

    public static String[] learning_books_text =
            {"Bed time story book", "Comic book", "Rhymes book", "Drawing book", "Story book", "Picture book", "Mystery book", "Adventure book", "School notebook", "Maths book", "Science book", "History book", "Geography book", "Social studies book", "English book", "Hindi book",  "Marathi book", "Textbook", "Favourites" };

    public static String[] learning_colours_text =
            {"Black", "Blue", "Brown", "Green", "Red", "Silver", "White", "Yellow",  "Golden", "Pink", "Orange", "Purple", "Gray"};

    public static String[] learning_shapes_text =
            {"Standing line", "Sleeping line", "Slanting line", "Circle", "Rectangle", "Square", "Triangle", " Star ", "Heart", "Trapezium", "Cube", "Rhombus", "Hexagon", "Oval", "Diamond", "Pentagon", " freeform "};

    public static String[] learning_stationary_text =
            {"Pencil", "Pen", "Scale", "Eraser", "Sharpener", "Crayon", "Blank paper", "Coloured paper",  "Scissors", "Pencil led", "Compass", "Divider", "stapler", " U-pin", "selo tape", "Compass box"};

    public static String[] learning_school_objects_text =
            {"Bag", "Lunch box", "Water bottle", "Compass box", "Homework", "School notebooks", "Textbooks", "Uniform",  "Shoes", "Socks", "Pencil", "Pen", "Scale", "Eraser", "Sharpener", "Chalk"};

    public static String[] learning_home_objects_text =
            {"Window", "Door", "Fan", "Lamp", "Desk", "cupboard", "Table", "Chair", "Toilet", "Kitchen", "Living room", "Bedroom", "Play room", "Bathroom", "Balcony", "Study room",  "bed", "Television", "Computer", "Sofa", "Fridge", "Microwave", "Washing machine", "Vacuum cleaner",  "Clock", " Tube light"};

    public static String[] learning_transportation_text =
            {"Bus", "School bus", "Car", "Bicycle", "Train", "Rickshaa", "Bike", "Aeroplane", "Ship"};

    public static String[] time_weather_time_text =
            {"What is the time?", "Today", "Yesterday", "Tomorrow", "Morning", "Afternoon", "Evening", "Night"};

    public static String[] time_weather_day_text =
            {"What is the day today?", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static String[] time_weather_month_text =
            {"What is the Current month?", "January", "February", "March", "April", "May", "June", "July",  "August", "September", "October", "November", "December", "This month", "Previous month", "Next month"};

    public static String[] time_weather_weather_text =
            {"What is Today’s weather?", "Sunny", "Rainy", "Cloudy", "Windy", "Foggy", "Snowy"};

    public static String[] time_weather_seasons_text =
            {"What is the Current season?", "Spring", "Summer", "Rainy", "Autumn", "Winter"};

    public static String[] time_weather_holidays_festivals_text =
            {"Diwali", "Ganesh chaturthi", "Christmas", "Dussehra", "मकर संक्रांति", "Holi", "Eid", "Good Friday",  "गुडी पाडवा", "Republic day", "Independence day", "New year"};

    public static String[] time_weather_brthdays_text =
            {"My birthday", "Mom’s birthday", "Father’s birthday", "Brother’s birthday", "Sister’s birthday", "Friend’s birthday", "Grandmother’s birthday", "Grandfather’s birthday",  "Uncle’s birthday", "Aunt’s birthday", "Cousin’s birthday", "Teacher’s birthday"};

    public static Integer[] count = new Integer[100];
    int[] sort = new int[100];
    int count_flag = 0;
    private SessionManager session;
    DataBaseHelper myDbHelper;
    float dpHeight;
    int sr, bw;

    String[] side = new String[100];
    String[] below = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setElevation(0);
        session = new SessionManager(getApplicationContext());
        more_count = 0;
        {
            String strSrBw = new AppPreferences(this).getShadowRadiusAndBorderWidth();
            sr = Integer.valueOf(strSrBw.split(",")[0]);
            bw = Integer.valueOf(strSrBw.split(",")[1]);
        }

        final String[] side_hindi = {"अच्छा लगता हैं", "सच में अच्छा लगता हैं", "हाँ", "सच में हाँ", "ज्यादा", "सच में ज्यादा", "अच्छा नहीं लगता हैं", "सच में अच्छा नहीं लगता हैं", "नहीं", "सच में नहीं", "कम", "सच में कम"};
        final String[] side_english = {"like", "really like", "yes", "really yes", "more", "really more", "dont like", "really dont like", "no", "really no", "less", "really less"};

        final String[] below_hindi = {"होम", "वापस", "कीबोर्ड"};
        final String[] below_english = {"Home", "back", "keyboard"};

        if (session.getLanguage() == 0 /*&& session.getAccent() == 0*/) {
            side = side_english;
            below = below_english;
        }
        if (session.getLanguage() == 0 /*&& session.getAccent() == 1*/) {
            side = side_english;
            below = below_english;
        }
        if (session.getLanguage() == 0 /*&& session.getAccent() == 2*/) {
            side = side_english;
            below = below_english;
        }
        myDbHelper = new DataBaseHelper(Layer3Activity.this);
        myDbHelper = new DataBaseHelper(this);
        session = new SessionManager(getApplicationContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setEngineByPackageName("com.google.android.mTts");
                    new LongOperation().execute("");
                }
            }
        });

        tts.setSpeechRate((float) session.getSpeed() / 50);
        tts.setPitch((float) session.getPitch() / 50);
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
        final Intent i = getIntent();
        layer_1_id = i.getExtras().getInt("layer_1_id");
        layer_2_id = i.getExtras().getInt("layer_2_id");
        System.out.println("LAYERSS" + layer_1_id + " " + layer_2_id);
        keyboard = (ImageView) findViewById(R.id.keyboard);
        et = (EditText) findViewById(R.id.et);
        et.setVisibility(View.INVISIBLE);
        ttsButton = (ImageView) findViewById(R.id.ttsbutton);
        ttsButton.setVisibility(View.INVISIBLE);
        originalKeyListener = et.getKeyListener();
        // Set it to null - this will make to the field non-editable
        et.setKeyListener(null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //imgad = new ImageAdapter(Layer3Activity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        String savedString = myDbHelper.getlevel(layer_1_id, layer_2_id);
        System.out.println("checkkk" + savedString);
        if (!savedString.equals("false")) {
            count_flag = 1;
            StringTokenizer st = new StringTokenizer(savedString, ",");
            System.out.println("Stringtokenizer " + st);
            count = new Integer[layer_3_speech[layer_1_id][layer_2_id].length];
            Log.d("counting ", count.length + "");
            for (int j = 0; j < layer_3_speech[layer_1_id][layer_2_id].length; j++) {
                count[j] = Integer.parseInt(st.nextToken());
                System.out.println("count " + j + " " + count[j]);
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
            myMusic_function(layer_1_id, layer_2_id);
            recyclerView.setAdapter(new Layer_three_Adapter(this, layer_1_id, layer_2_id, sort));
        } else if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
            myMusic_function(layer_1_id, layer_2_id);
            recyclerView.setAdapter(new Layer_three_Adapter(this, layer_1_id, layer_2_id, sort));
        } else {
            count_flag = 0;
            myMusic_function(layer_1_id, layer_2_id);
        }

        /*if (layer_1_id == 0) {
            if (layer_2_id == 0 || layer_2_id == 2 || layer_2_id == 3) {
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
        }*/
        final int[] prev_pos = {-1};
        final int[] cko = {-1};
        final View[] xx = {null};
        final int[] ix = {-1};
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(final View view, final int position) {
                        Log.d("pooooolooo", location + " " + locayy);
                        mLinearLayoutIconOne = (LinearLayout)view.findViewById(R.id.linearlayout_icon1);
                        mLinearLayoutIconTwo = (LinearLayout)view.findViewById(R.id.linearlayout_icon2);
                        mLinearLayoutIconThree = (LinearLayout)view.findViewById(R.id.linearlayout_icon3);

                        if (xx[0] != null && xx[0] != view) {
                            notifyDataSet(xx[0]);

                        }
                        if (session.getGridSize() == 0) {
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

                                    if (layer_1_id == 0) {
                                        if (layer_2_id == 0 || layer_2_id == 2 || layer_2_id == 3) {
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
                                    }

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
                                    System.out.println("sucess" + ix[0]);
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

                                    if (layer_1_id == 0) {
                                        if (layer_2_id == 0 || layer_2_id == 2 || layer_2_id == 3) {
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
                                    }

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

                                    if (layer_1_id == 0) {
                                        if (layer_2_id == 0 || layer_2_id == 2 || layer_2_id == 3) {
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
                                    }

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
                                    Log.d("checkloc", locayy + " location " + location);
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else
                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

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

                        }
                        if (session.getGridSize() == 1) {
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
                                    System.out.println("sucess" + ix[0]);
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
                                    Log.d("checkloc", locayy + " location " + location);
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else
                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);

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

                                    cko[0]++;
                                    locayy = position * 9 + 3;
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
                                    System.out.println("sucess" + ix[0]);
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

                                    im4.setBorderColor(-1);
                                    im4.setShadowColor(0);
                                    im4.setShadowRadius(sr);
                                    im4.setBorderWidth(0);

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
                                    System.out.println("sucess" + ix[0]);
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

                                    im5.setBorderColor(-1);
                                    im5.setShadowColor(0);
                                    im5.setShadowRadius(sr);
                                    im5.setBorderWidth(0);

                                    im4.setBorderColor(-1);
                                    im4.setShadowColor(0);
                                    im4.setShadowRadius(sr);
                                    im4.setBorderWidth(0);

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
                                    System.out.println("sucess" + ix[0]);
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else
                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);


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

                                    im8.setBorderColor(-1);
                                    im8.setShadowColor(0);
                                    im8.setShadowRadius(sr);
                                    im8.setBorderWidth(0);

                                    im9.setBorderColor(-1);
                                    im9.setShadowColor(0);
                                    im9.setShadowRadius(sr);
                                    im9.setBorderWidth(0);

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

                                    cko[0]++;
                                    locayy = position * 9 + 6;
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
                                    System.out.println("sucess" + ix[0]);
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

                                    im7.setBorderColor(-1);
                                    im7.setShadowColor(0);
                                    im7.setShadowRadius(sr);
                                    im7.setBorderWidth(0);

                                    im9.setBorderColor(-1);
                                    im9.setShadowColor(0);
                                    im9.setShadowRadius(sr);
                                    im9.setBorderWidth(0);

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
                                    System.out.println("sucess" + ix[0]);
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

                                    im8.setBorderColor(-1);
                                    im8.setShadowColor(0);
                                    im8.setShadowRadius(sr);
                                    im8.setBorderWidth(0);

                                    im7.setBorderColor(-1);
                                    im7.setShadowColor(0);
                                    im7.setShadowRadius(sr);
                                    im7.setBorderWidth(0);

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

                                    cko[0]++;
                                    locayy = position * 9 + 8;
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
                                    System.out.println("sucess" + ix[0]);
                                    if ((layer_1_id == 3 && (layer_2_id == 3 || layer_2_id == 4)) || (layer_1_id == 7 && (layer_2_id == 0 || layer_2_id == 1 || layer_2_id == 2 || layer_2_id == 3 || layer_2_id == 4))) {
                                        tts.speak(myMusic[locayy], TextToSpeech.QUEUE_FLUSH, null);
                                    } else
                                        tts.speak(myMusic[sort[locayy]], TextToSpeech.QUEUE_FLUSH, null);
                                    location = locayy;
                                    Log.d("checkloc", locayy + " location " + location);


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

                        }
                    }

                    private void unset() {
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

        //imgad.notifyDataSetChanged();

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

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tts.speak(below[2], TextToSpeech.QUEUE_FLUSH, null);

                if (flag_keyboard == 1) {
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

                } else {
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
                new View.OnClickListener() {
                    public void onClick(View view) {

                        tts.setSpeechRate((float) session.getSpeed() / 50);
                        tts.setPitch((float) session.getPitch() / 50);
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
                new View.OnClickListener() {
                    public void onClick(View view) {
                        tts.speak(below[1], TextToSpeech.QUEUE_FLUSH, null);
                        back.setImageResource(R.drawable.backpressed);
                        if (flag_keyboard == 1) {
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
                            more_count -= 1;
                            myMusic_function(layer_1_id, layer_2_id);
                            System.out.println("LAUNCHING " + layer_1_id);
                            recyclerView.setAdapter(new Layer_three_Adapter(Layer3Activity.this, layer_1_id, layer_2_id, sort));
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
                } else {
                    if (session.getGridSize()==0){
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[0]);
                        im1.setShadowColor(color[0]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[0]);
                        im2.setShadowColor(color[0]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
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
                        if (location % 9 == 0) {
                            im1.setBorderColor(color[0]);
                            im1.setShadowColor(color[0]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 9 == 1) {
                            im2.setBorderColor(color[0]);
                            im2.setShadowColor(color[0]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 9 == 2) {
                            im3.setBorderColor(color[0]);
                            im3.setShadowColor(color[0]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if (location % 9 == 3) {
                            im4.setBorderColor(color[0]);
                            im4.setShadowColor(color[0]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[0]);
                            im5.setShadowColor(color[0]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[0]);
                            im6.setShadowColor(color[0]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if (location % 9 == 6) {
                            im7.setBorderColor(color[0]);
                            im7.setShadowColor(color[0]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[0]);
                            im8.setShadowColor(color[0]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[1]);
                        im1.setShadowColor(color[1]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[1]);
                        im2.setShadowColor(color[1]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
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
                        if (location % 9 == 0) {
                            im1.setBorderColor(color[1]);
                            im1.setShadowColor(color[1]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 9 == 1) {
                            im2.setBorderColor(color[1]);
                            im2.setShadowColor(color[1]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 9 == 2) {
                            im3.setBorderColor(color[1]);
                            im3.setShadowColor(color[1]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }if (location % 9 == 3) {
                            im4.setBorderColor(color[1]);
                            im4.setShadowColor(color[1]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[1]);
                            im5.setShadowColor(color[1]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[1]);
                            im6.setShadowColor(color[1]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }if (location % 9 == 6) {
                            im7.setBorderColor(color[1]);
                            im7.setShadowColor(color[1]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[1]);
                            im8.setShadowColor(color[1]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
                clike = 0;
                cm = 0;
                cd = 0;
                cn = 0;
                cl = 0;
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
                } else {
                    if (session.getGridSize()==0){
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[2]);
                        im1.setShadowColor(color[2]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[2]);
                        im2.setShadowColor(color[2]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
                        im3.setBorderColor(color[2]);
                        im3.setShadowColor(color[2]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

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
                    if (session.getGridSize()==1){
                        if (location % 9 == 0) {
                            im1.setBorderColor(color[2]);
                            im1.setShadowColor(color[2]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 9 == 1) {
                            im2.setBorderColor(color[2]);
                            im2.setShadowColor(color[2]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 9 == 2) {
                            im3.setBorderColor(color[2]);
                            im3.setShadowColor(color[2]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if (location % 9 == 3) {
                            im4.setBorderColor(color[2]);
                            im4.setShadowColor(color[2]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[2]);
                            im5.setShadowColor(color[2]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[2]);
                            im6.setShadowColor(color[2]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if (location % 9 == 6) {
                            im7.setBorderColor(color[2]);
                            im7.setShadowColor(color[2]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[2]);
                            im8.setShadowColor(color[2]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
                clike = 0;
                cy = 0;
                cm = 0;
                cd = 0;
                cl = 0;
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
                } else {

                    if (session.getGridSize()==0){
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[3]);
                        im1.setShadowColor(color[3]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[3]);
                        im2.setShadowColor(color[3]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
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
                        if (location % 3 == 0) {
                            im1.setBorderColor(color[3]);
                            im1.setShadowColor(color[3]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 3 == 1) {
                            im2.setBorderColor(color[3]);
                            im2.setShadowColor(color[3]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 3 == 2) {
                            im3.setBorderColor(color[3]);
                            im3.setShadowColor(color[3]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if (location % 9 == 3) {
                            im4.setBorderColor(color[3]);
                            im4.setShadowColor(color[3]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[3]);
                            im5.setShadowColor(color[3]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[3]);
                            im6.setShadowColor(color[3]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if (location % 9 == 6) {
                            im7.setBorderColor(color[3]);
                            im7.setShadowColor(color[3]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[3]);
                            im8.setShadowColor(color[3]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
                clike = 0;
                cy = 0;
                cd = 0;
                cn = 0;
                cl = 0;
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
                } else {
                    if (session.getGridSize()==0){
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[4]);
                        im1.setShadowColor(color[4]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[4]);
                        im2.setShadowColor(color[4]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
                        im3.setBorderColor(color[4]);
                        im3.setShadowColor(color[4]);
                        im3.setShadowRadius(sr);
                        im3.setBorderWidth(bw);

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
                    if (session.getGridSize()==1){
                        if (location % 9 == 0) {
                            im1.setBorderColor(color[4]);
                            im1.setShadowColor(color[4]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 9 == 1) {
                            im2.setBorderColor(color[4]);
                            im2.setShadowColor(color[4]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 9 == 2) {
                            im3.setBorderColor(color[4]);
                            im3.setShadowColor(color[4]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if (location % 9 == 3) {
                            im4.setBorderColor(color[4]);
                            im4.setShadowColor(color[4]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[4]);
                            im5.setShadowColor(color[4]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[4]);
                            im6.setShadowColor(color[4]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if (location % 9 == 6) {
                            im7.setBorderColor(color[4]);
                            im7.setShadowColor(color[4]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[4]);
                            im8.setShadowColor(color[4]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
                clike = 0;
                cy = 0;
                cm = 0;
                cd = 0;
                cn = 0;
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
                } else {
                    if (session.getGridSize()==0){
                    if (location % 3 == 0) {
                        im1.setBorderColor(color[5]);
                        im1.setShadowColor(color[5]);
                        im1.setShadowRadius(sr);
                        im1.setBorderWidth(bw);

                    } else if (location % 3 == 1) {
                        im2.setBorderColor(color[5]);
                        im2.setShadowColor(color[5]);
                        im2.setShadowRadius(sr);
                        im2.setBorderWidth(bw);

                    } else if (location % 3 == 2) {
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
                        if (location % 9 == 0) {
                            im1.setBorderColor(color[5]);
                            im1.setShadowColor(color[5]);
                            im1.setShadowRadius(sr);
                            im1.setBorderWidth(bw);

                        } else if (location % 9 == 1) {
                            im2.setBorderColor(color[5]);
                            im2.setShadowColor(color[5]);
                            im2.setShadowRadius(sr);
                            im2.setBorderWidth(bw);

                        } else if (location % 9 == 2) {
                            im3.setBorderColor(color[5]);
                            im3.setShadowColor(color[5]);
                            im3.setShadowRadius(sr);
                            im3.setBorderWidth(bw);

                        }
                        if (location % 9 == 3) {
                            im4.setBorderColor(color[5]);
                            im4.setShadowColor(color[5]);
                            im4.setShadowRadius(sr);
                            im4.setBorderWidth(bw);

                        } else if (location % 9 == 4) {
                            im5.setBorderColor(color[5]);
                            im5.setShadowColor(color[5]);
                            im5.setShadowRadius(sr);
                            im5.setBorderWidth(bw);

                        } else if (location % 9 == 5) {
                            im6.setBorderColor(color[5]);
                            im6.setShadowColor(color[5]);
                            im6.setShadowRadius(sr);
                            im6.setBorderWidth(bw);

                        }
                        if (location % 9 == 6) {
                            im7.setBorderColor(color[5]);
                            im7.setShadowColor(color[5]);
                            im7.setShadowRadius(sr);
                            im7.setBorderWidth(bw);

                        } else if (location % 9 == 7) {
                            im8.setBorderColor(color[5]);
                            im8.setShadowColor(color[5]);
                            im8.setShadowRadius(sr);
                            im8.setBorderWidth(bw);

                        } else if (location % 9 == 8) {
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
    } private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                if (session.getLanguage() == 0 ) {
                    tts.setLanguage(new Locale("hin", "IND"));
                }
                if (session.getLanguage() == 1) {
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

    public void myMusic_function(int layer_1_id, int layer_2_id) {
        System.out.println("size"+greet_feel_greetings_text.length);
        if (layer_1_id == 0) {
            if (layer_2_id == 0) {
              //  for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = greet_feel_greetings_text;
            } else if (layer_2_id == 1) {
               // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = greet_feel_feelings_text;
            } else if (layer_2_id == 2) {
               // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = greet_feel_requests_text;
            } else if (layer_2_id == 3) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = greet_feel_questions_text;
            }
        } else if (layer_1_id == 1) {
            if (layer_2_id == 0) {
                myMusic = daily_activities_brushing_text;
            } else if (layer_2_id == 1) {
                myMusic = daily_activities_toilet_text;
            } else if (layer_2_id == 2) {
                myMusic = daily_activities_bathing_text;
            } else if (layer_2_id == 3) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = daily_activities_clothes_access_text;
            } else if (layer_2_id == 4) {
               // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = daily_activities_get_ready_text;
            } else if (layer_2_id == 5) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = daily_activities_sleep_text;
            } else if (layer_2_id == 6) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = daily_activities_therapy_text;
            } else if (layer_2_id == 7) {
                myMusic = daily_activities_morning_schedule_text;
            } else if (layer_2_id == 8) {
                myMusic = daily_activities_bedtime_schedule_text;
            }
        } else if (layer_1_id == 2) {
            if (layer_2_id == 0) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = foods_drinks_breakfast_text;
            } else if (layer_2_id == 1) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_lunch_dinner_text;
            } else if (layer_2_id == 2) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_sweets_text;
            } else if (layer_2_id == 3) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_snacks_text;
            } else if (layer_2_id == 4) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_fruits_text;
            } else if (layer_2_id == 5) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_drinks_text;
            } else if (layer_2_id == 6) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_cutlery_text;
            } else if (layer_2_id == 7) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = food_drinks_add_ons_text;
            }
        } else if (layer_1_id == 3) {
            if (layer_2_id == 0) {
               // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = fun_indoor_games_text;
            } else if (layer_2_id == 1) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = fun_outdoor_games_text;
            } else if (layer_2_id == 2) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = fun_sports_text;
            } else if (layer_2_id == 3) {
                myMusic = fun_tv_text;
            } else if (layer_2_id == 4) {
                myMusic = fun_music_text;
            } else if (layer_2_id == 5) {
             //   for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = fun_activities_text;
            }
        } else if (layer_1_id == 4) {
            if (layer_2_id == 0) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_animals_birds_text;
            } else if (layer_2_id == 1) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_body_parts_text;
            } else if (layer_2_id == 2) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_books_text;
            } else if (layer_2_id == 3) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_colours_text;
            } else if (layer_2_id == 4) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_shapes_text;
            } else if (layer_2_id == 5) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_stationary_text;
            } else if (layer_2_id == 6) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_school_objects_text;
            } else if (layer_2_id == 7) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_home_objects_text;
            } else if (layer_2_id == 8) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = learning_transportation_text;
            }
        } else if (layer_1_id == 7) {
            if (layer_2_id == 0) {
                myMusic = time_weather_time_text;
            } else if (layer_2_id == 1) {
                myMusic = time_weather_day_text;
            } else if (layer_2_id == 2) {
                myMusic = time_weather_month_text;
            } else if (layer_2_id == 3) {
                myMusic = time_weather_weather_text;
            } else if (layer_2_id == 4) {
                myMusic = time_weather_seasons_text;
            } else if (layer_2_id == 5) {
               // for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = time_weather_holidays_festivals_text;
            } else if (layer_2_id == 6) {
                //for (int j = more_count * 9; j < more_count * 9 + 9; j++)
                    myMusic = time_weather_brthdays_text;
            }
        }
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

    String[][][][] layer_3_speech = {{{{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},

            {{"I feel happy",
                    "I really feel happy",
                    "I want to be happy ",
                    "I really want to be happy",
                    "I feel very happy",
                    "I really feel very happy",
                    "I don’t feel happy",
                    "I really don’t feel happy",
                    "I don’t want to be happy",
                    "I really don’t want to be happy",
                    "I don’t feel very happy",
                    "I really don’t feel happy any more"

            }, {"I feel sad",
                    "I really feel sad",
                    "I want to be sad",
                    "I really want to be sad",
                    "I feel very sad",
                    "I really feel very sad",
                    "I don’t feel sad",
                    "I really don’t feel sad",
                    "I don’t want to be sad",
                    "I really don’t want to be sad",
                    "I don’t feel very sad",
                    "I really don’t feel sad any more"

            }, {"I feel angry",
                    "I really feel angry",
                    "I want to be angry",
                    "I really want to be angry",
                    "I feel very angry",
                    "I really feel very angry",
                    "I don’t feel angry",
                    "I really don’t feel angry",
                    "I don’t want to be angry",
                    "I really don’t want to be angry",
                    "I don’t feel very angry",
                    "I really don’t feel angry any more"

            }, {"I feel afraid",
                    "I really feel afraid",
                    "I want to be afraid",
                    "I really want to be afraid",
                    "I feel very afraid",
                    "I really feel very afraid",
                    "I don’t feel afraid",
                    "I really don’t feel afraid",
                    "I don’t want to be afraid",
                    "I really don’t want to be afraid",
                    "I don’t feel very afraid",
                    "I really don’t feel afraid any more"

            }, {"I am amazed",
                    "I am really amazed",
                    "I want to be amazed",
                    "I really want to be amazed",
                    "I am very amazed",
                    "I am really very amazed",
                    "I am not amazed",
                    "I am really not amazed",
                    "I don’t want to be amazed",
                    "I really don’t want to be amazed",
                    "I don’t feel very amazed",
                    "I am really not amazed at all"

            }, {"I feel irritated",
                    "I really feel irritated",
                    "I want to be irritated",
                    "I really want to be irritated",
                    "I feel very irritated",
                    "I really feel very irritated",
                    "I don’t feel irritated",
                    "I really don’t feel irritated",
                    "I don’t want to be irritated",
                    "I really don’t want to be irritated",
                    "I don’t feel very irritated",
                    "I really don’t feel irritated any more"

            }, {"I feel confused",
                    "I really feel confused",
                    "I want to be confused",
                    "I really want to be confused",
                    "I feel very confused",
                    "I really feel very confused",
                    "I don’t feel confused",
                    "I really don’t feel confused",
                    "I don’t want to be confused",
                    "I really don’t want to be confused",
                    "I don’t feel very confused",
                    "I really don’t feel confused any more"

            }, {"I feel ashamed",
                    "I really feel ashamed",
                    "I want to feel ashamed",
                    "I really want to feel ashamed",
                    "I feel very ashamed",
                    "I really feel very ashamed",
                    "I don’t feel ashamed",
                    "I really don’t feel ashamed",
                    "I don’t want to be ashamed",
                    "I really don’t want to be ashamed",
                    "I don’t feel very ashamed",
                    "I really don’t feel ashamed any more"

            }, {"I feel disappointed",
                    "I really feel disappointed",
                    "I want to be disappointed",
                    "I really want to be disappointed",
                    "I feel very disappointed",
                    "I really feel very disappointed",
                    "I don’t feel disappointed",
                    "I really don’t feel disappointed",
                    "I don’t want to be disappointed",
                    "I really don’t want to be disappointed",
                    "I don’t feel very disappointed",
                    "I really don’t feel disappointed any more"

            }, {"I feel bored",
                    "I really feel bored",
                    "I want to be bored",
                    "I really want to be bored",
                    "I feel very bored",
                    "I really feel very bored",
                    "I don’t feel bored",
                    "I really don’t feel bored",
                    "I don’t want to be bored",
                    "I really don’t want to be bored",
                    "I don’t feel very bored",
                    "I really don’t feel bored any more"

            }, {"I feel worried",
                    "I really feel worried",
                    "I want to be worried",
                    "I really want to be worried",
                    "I feel very worried",
                    "I really feel very worried",
                    "I don’t feel worried",
                    "I really don’t feel worried",
                    "I don’t want to be worried",
                    "I really don’t want to be worried",
                    "I don’t feel very worried",
                    "I really don’t feel worried any more"

            }, {"I feel stressed",
                    "I really feel stressed",
                    "I want to be stressed",
                    "I really want to be stressed",
                    "I feel very stressed",
                    "I really feel very stressed",
                    "I don’t feel stressed",
                    "I really don’t feel stressed",
                    "I don’t want to be stressed",
                    "I really don’t want to be stressed",
                    "I don’t feel very stressed",
                    "I really don’t feel stressed any more"

            }, {"I feel tired",
                    "I really feel tired",
                    "I want to be tired",
                    "I really want to be tired",
                    "I feel very tired",
                    "I really feel very tired",
                    "I don’t feel tired",
                    "I really don’t feel tired",
                    "I don’t want to be tired",
                    "I really don’t want to be tired",
                    "I don’t feel very tired",
                    "I really don’t feel tired any more"

            }, {"I feel hot",
                    "I really feel hot",
                    "I want to feel warm",
                    "I really want to feel warm",
                    "I want it to be more hot",
                    "I really want it to be hotter",
                    "I don’t feel hot",
                    "I really don’t feel hot",
                    "I don’t want to feel hot",
                    "I really don’t want to feel hot",
                    "I don’t want it to be more hot",
                    "I really don’t want it to be any hotter"


            }, {"I feel cold",
                    "I really feel cold",
                    "I want to feel cool",
                    "I really want to feel cool",
                    "I want it to be more cold",
                    "I really want it to be colder",
                    "I don’t feel cold",
                    "I really don’t feel cold",
                    "I don’t want to feel cold",
                    "I really don’t want to feel cold",
                    "I don’t want it to be more cold",
                    "I really don’t want it to be any colder",

            }, {"I feel sick",
                    "I really feel sick",
                    "I want to be sick",
                    "I really want to be sick",
                    "I feel very sick",
                    "I really feel very sick",
                    "I don’t feel sick",
                    "I really don’t feel sick",
                    "I don’t want to be sick",
                    "I really don’t want to be sick",
                    "I don’t feel very sick",
                    "I really don’t feel sick any more"

            }, {"I feel hurt",
                    "I really feel hurt",
                    "I want to be hurt",
                    "I really want to be hurt",
                    "I feel very hurt",
                    "I really feel very hurt",
                    "I don’t feel hurt",
                    "I really don’t feel hurt",
                    "I don’t want to be hurt",
                    "I really don’t want to be hurt",
                    "I don’t feel very hurt",
                    "I really don’t feel hurt any more"

            }}, {{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}}, {{}, {}, {}, {}, {}, {}, {}, {}, {

    }}}, {{{"I like to rinse my mouth",
            "I really like to rinse my mouth",
            "I want to rinse my mouth",
            "I really want to rinse my mouth",
            "I want to rinse my mouth some more",
            "I really want to rinse my mouth some more",
            "I don’t like to rinse my mouth",
            "I really don’t like to rinse my mouth",
            "I don’t want to rinse my mouth",
            "I really don’t want to rinse my mouth",
            "I don’t want to rinse my mouth more",
            "I really don’t want to rinse my mouth any more",

    }, {"I like to rinse my toothbrush",
            "I really like to rinse my toothbrush",
            "I want to rinse my toothbrush",
            "I really want to rinse my toothbrush",
            "I want to rinse my toothbrush some more",
            "I really want to rinse my toothbrush some more",
            "I don’t like to rinse my toothbrush",
            "I really don’t like to rinse my toothbrush",
            "I don’t want to rinse my toothbrush",
            "I really don’t want to rinse my toothbrush",
            "I don’t want to rinse my toothbrush more",
            "I really don’t want to rinse my toothbrush any more"

    }, {"I like to put toothpaste on my brush",
            "I really like to put toothpaste on my brush",
            "I want to put toothpaste on my brush",
            "I really want to put toothpaste on my brush",
            "I want to put some more toothpaste on my brush",
            "I really want to put some more toothpaste on my brush",
            "I don’t like to put toothpaste on my brush",
            "I really don’t like to put toothpaste on my brush",
            "I don’t want to put toothpaste on my brush",
            "I really don’t want to put toothpaste on my brush",
            "I don’t want to put more toothpaste on my brush",
            "I really don’t want to put any more toothpaste on my brush"

    }, {"I like to brush my front teeth",
            "I really like to brush my front teeth",
            "I want to brush my front teeth",
            "I really want to brush my front teeth",
            "I want to brush my front teeth some more",
            "I really want to brush my front teeth a bit longer",
            "I don’t like to brush my front teeth",
            "I really don’t like to brush my front teeth",
            "I don’t want to brush my front teeth",
            "I really don’t want to brush my front teeth",
            "I don’t want to brush my front teeth more",
            "I really don’t want to brush my front teeth any more"

    }, {"I like to brush my back teeth",
            "I really like to brush my back teeth",
            "I want to brush my back teeth",
            "I really want to brush my back teeth",
            "I want to brush my back teeth some more",
            "I really want to brush my back teeth a little longer",
            "I don’t like to brush my back teeth",
            "I really don’t like to brush my back teeth",
            "I don’t want to brush my back teeth",
            "I really don’t want to brush my back teeth",
            "I don’t want to brush my back teeth more",
            "I really don’t want to brush my back teeth any more"

    }, {"I like to brush my tongue",
            "I really like to brush my tongue",
            "I want to brush my tongue",
            "I really want to brush my tongue",
            "I want to brush my tongue some more",
            "I really want to brush my tongue a little longer",
            "I don’t like to brush my tongue",
            "I really don’t like to brush my tongue",
            "I don’t want to brush my tongue",
            "I really don’t want to brush my tongue",
            "I don’t want to brush my tongue more",
            "I really don’t want to brush my tongue any more",

    }, {"I like to rinse my mouth",
            "I really like to rinse my mouth",
            "I want to rinse my mouth",
            "I really want to rinse my mouth",
            "I want to rinse my mouth some more",
            "I really want to rinse my mouth some more",
            "I don’t like to rinse my mouth",
            "I really don’t like to rinse my mouth",
            "I don’t want to rinse my mouth",
            "I really don’t want to rinse my mouth",
            "I don’t want to rinse my mouth more",
            "I really don’t want to rinse my mouth any more"

    }, {"I like to be all done",
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

    }}, {{"I have to pull my pants down",
            "I really have to pull my pants down",
            "I want to pull my pants down",
            "I really want to pull my pants down",
            "I want to pull my pants down again",
            "I really want to pull my pants down again",
            "I don’t have to pull my pants down",
            "I really don’t have to pull my pants down",
            "I don’t want to pull my pants down",
            "I really don’t want to pull my pants down",
            "I don’t want to pull my pants down again",
            "I really don’t want to pull my pants down again"

    }, {"I have to sit on the toilet",
            "I really have to sit on the toilet",
            "I want to sit on the toilet",
            "I really want to sit on the toilet",
            "I want to sit on the toilet again",
            "I really want to sit on the toilet again",
            "I don’t have to sit on the toilet",
            "I really don’t have to sit on the toilet",
            "I don’t want to sit on the toilet",
            "I really don’t want to sit on the toilet",
            "I don’t want to sit on the toilet again",
            "I really don’t want to sit on the toilet again"

    }, {"I have to wash my bottom",
            "I really have to wash my bottom",
            "I want to wash my bottom",
            "I really want to wash my bottom",
            "I want to wash my bottom again",
            "I really want to wash my bottom again",
            "I don’t have to wash my bottom",
            "I really don’t have to wash my bottom",
            "I don’t want to wash my bottom",
            "I really don’t want to wash my bottom",
            "I don’t want to wash my bottom again",
            "I really don’t want to wash my bottom again"

    }, {"I have to flushh the toilet",
            "I really have to flushh the toilet",
            "I want to flushh the toilet",
            "I really want to flushh the toilet",
            "I want to flushh the toilet again",
            "I really want to flushh the toilet again",
            "I don’t have to flushh the toilet",
            "I really don’t have to flushh the toilet",
            "I don’t want to flushh the toilet",
            "I really don’t want to flushh the toilet",
            "I don’t want to flushh the toilet again",
            "I really don’t want to flushh the toilet again"

    }, {"I have to pull my pants up",
            "I really have to pull my pants up",
            "I want to pull my pants up",
            "I really want to pull my pants up",
            "I want to pull my pants up again",
            "I really want to pull my pants up again",
            "I don’t have to pull my pants up",
            "I really don’t have to pull my pants up",
            "I don’t want to pull my pants up",
            "I really don’t want to pull my pants up",
            "I don’t want to pull my pants up again",
            "I really don’t want to pull my pants up again",

    }, {"I have to wash my hands",
            "I really have to wash my hands",
            "I want to wash my hands",
            "I really want to wash my hands",
            "I want to wash my hands again",
            "I really want to wash my hands again",
            "I don’t have to wash my hands",
            "I really don’t have to wash my hands",
            "I don’t want to wash my hands",
            "I really don’t want to wash my hands",
            "I don’t want to wash my hands again",
            "I really don’t want to wash my hands again"

    }, {"I like to be all done",
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

    }}, {{"I have to remove my clothes",
            "I really have to remove my clothes",
            "I want to remove my clothes",
            "I really want to remove my clothes",
            "I want to remove my clothes again",
            "I really want to remove my clothes again",
            "I don’t have to remove my clothes",
            "I really don’t have to remove my clothes",
            "I don’t want to remove my clothes",
            "I really don’t want to remove my clothes",
            "I don’t want to remove my clothes again",
            "I really don’t want to remove my clothes again"

    }, {"I have to turn on the water",
            "I really have to turn on the water",
            "I want to turn on the water",
            "I really want to turn on the water",
            "I want to turn on the water again",
            "I really want to turn on the water again",
            "I don’t have to turn on the water",
            "I really don’t have to turn on the water",
            "I don’t want to turn on the water",
            "I really don’t want to turn on the water",
            "I don’t want to turn on the water again",
            "I really don’t want to turn on the water again"

    }, {"I have to get in the shower",
            "I really have to get in the shower",
            "I want to get in the shower",
            "I really want to get in the shower",
            "I want to get in the shower again",
            "I really want to get in the shower again",
            "I don’t have to get in the shower",
            "I really don’t have to get in the shower",
            "I don’t want to get in the shower",
            "I really don’t want to get in the shower",
            "I don’t want to get in the shower again",
            "I really don’t want to get in the shower again"

    }, {"I like to wet my body",
            "I really like to wet my body",
            "I want to wet my body",
            "I really want to wet my body",
            "I want to wet my body again",
            "I really want to wet my body again",
            "I don’t like to wet my body",
            "I really don’t like to wet my body",
            "I don’t want to wet my body",
            "I really don’t want to wet my body",
            "I don’t want to wet my body again",
            "I really don’t want to wet my body again"

    }, {"I like to put soap on my body",
            "I really like to put soap on my body",
            "I want to put soap on my body",
            "I really want to put soap on my body",
            "I want to put some more soap on my body",
            "I really want to put some more soap on my body",
            "I don’t like to put soap on my body",
            "I really don’t like to put soap on my body",
            "I don’t want to put soap on my body",
            "I really don’t want to put soap on my body",
            "I don’t want to put more soap on my body",
            "I really don’t want to put any more soap on my body"

    }, {"I like to shampoo my हैर",
            "I really like to shampoo my हैर",
            "I want to shampoo my हैर",
            "I really want to shampoo my हैर",
            "I want to shampoo my हैर again",
            "I really want to shampoo my हैर again",
            "I don’t like to shampoo my हैर",
            "I really don’t like to shampoo my हैर",
            "I don’t want to shampoo my हैर",
            "I really don’t want to shampoo my हैर",
            "I don’t want to shampoo my हैर again",
            "I really don’t want to shampoo my हैर again"

    }, {"I like to use face wash",
            "I really like to use face wash",
            "I want to use some face wash",
            "I really want to use some face wash",
            "I want to use face wash again",
            "I really want to use face wash again",
            "I don’t like to use face wash",
            "I really don’t like to use face wash",
            "I don’t want to use face wash",
            "I really don’t want to use face wash",
            "I don’t want to use face wash again",
            "I really don’t want to use face wash again"

    }, {"I like to wash my हैर",
            "I really like to wash my हैर",
            "I want to wash my हैर",
            "I really want to wash my हैर",
            "I want to wash my हैर again",
            "I really want to wash my हैर again",
            "I don’t like to wash my हैर",
            "I really don’t like to wash my हैर",
            "I don’t want to wash my हैर",
            "I really don’t want to wash my हैर",
            "I don’t want to wash my हैर again",
            "I really don’t want to wash my हैर again"

    }, {"I like to wash my body",
            "I really like to wash my body",
            "I want to wash my body",
            "I really want to wash my body",
            "I want to wash my body again",
            "I really want to wash my body again",
            "I don’t like to wash my body",
            "I really don’t like to wash my body",
            "I don’t want to wash my body",
            "I really don’t want to wash my body",
            "I don’t want to wash my body again",
            "I really don’t want to wash my body again"

    }, {"I have to turn off the water",
            "I really have to turn off the water",
            "I want to turn off the water",
            "I really want to turn off the water",
            "I want to turn off the water again",
            "I really want to turn off the water again",
            "I don’t have to turn off the water",
            "I really don’t have to turn off the water",
            "I don’t want to turn off the water",
            "I really don’t want to turn off the water",
            "I don’t want to turn off the water again",
            "I really don’t want to turn off the water again"

    }, {"I like to dry my हैर",
            "I really like to dry my हैर",
            "I want to dry my हैर",
            "I really want to dry my हैर",
            "I want to dry my हैर again",
            "I really want to dry my हैर again",
            "I don’t like to dry my हैर",
            "I really don’t like to dry my हैर",
            "I don’t want to dry my हैर",
            "I really don’t want to dry my हैर",
            "I don’t want to dry my हैर again",
            "I really don’t want to dry my हैर again"

    }, {"I like to dry my face",
            "I really like to dry my face",
            "I want to dry my face",
            "I really want to dry my face",
            "I want to dry my face again",
            "I really want to dry my face again",
            "I don’t like to dry my face",
            "I really don’t like to dry my face",
            "I don’t want to dry my face",
            "I really don’t want to dry my face",
            "I don’t want to dry my face again",
            "I really don’t want to dry my face again",

    }, {"I like to dry my body",
            "I really like to dry my body",
            "I want to dry my body",
            "I really want to dry my body",
            "I want to dry my body again",
            "I really want to dry my body again",
            "I don’t like to dry my body",
            "I really don’t like to dry my body",
            "I don’t want to dry my body",
            "I really don’t want to dry my body",
            "I don’t want to dry my body again",
            "I really don’t want to dry my body again"

    }, {"I have to put my clothes on",
            "I really have to put my clothes on",
            "I want to put my clothes on",
            "I really want to put my clothes on",
            "I want to put my clothes on again",
            "I really want to put my clothes on again",
            "I don’t have to put my clothes on",
            "I really don’t have to put my clothes on",
            "I don’t want to put my clothes on",
            "I really don’t want to put my clothes on",
            "I don’t want to put my clothes on again",
            "I really don’t want to put my clothes on again"

    }, {"I like to be all done",
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


    }}, {{"I like to change my t-shirt",
            "I really like to change my t-shirt",
            "I want to change my t-shirt",
            "I really want to change my t-shirt",
            "I want to change my t-shirt again",
            "I really want to change my t-shirt again",
            "I don’t like to change my t-shirt",
            "I really don’t like to change my t-shirt",
            "I don’t want to change my t-shirt",
            "I really don’t want to change my t-shirt",
            "I don’t want to change my t-shirt again",
            "I really don’t want to change my t-shirt again"

    }, {"I like to change my frock",
            "I really like to change my frock",
            "I want to change my frock",
            "I really want to change my frock",
            "I want to change my frock again",
            "I really want to change my frock again",
            "I don’t like to change my frock",
            "I really don’t like to change my frock",
            "I don’t want to change my frock",
            "I really don’t want to change my frock",
            "I don’t want to change my frock again",
            "I really don’t want to change my frock again"


    }, {"I like to change my skirt",
            "I really like to change my skirt",
            "I want to change my skirt",
            "I really want to change my skirt",
            "I want to change my skirt again",
            "I really want to change my skirt again",
            "I don’t like to change my skirt",
            "I really don’t like to change my skirt",
            "I don’t want to change my skirt",
            "I really don’t want to change my skirt",
            "I don’t want to change my skirt again",
            "I really don’t want to change my skirt again"

    }, {"I like to change my jeans",
            "I really like to change my jeans",
            "I want to change my jeans",
            "I really want to change my jeans",
            "I want to change my jeans again",
            "I really want to change my jeans again",
            "I don’t like to change my jeans",
            "I really don’t like to change my jeans",
            "I don’t want to change my jeans",
            "I really don’t want to change my jeans",
            "I don’t want to change my jeans again",
            "I really don’t want to change my jeans again"

    }, {"I like to change my pants",
            "I really like to change my pants",
            "I want to change my pants",
            "I really want to change my pants",
            "I want to change my pants again",
            "I really want to change my pants again",
            "I don’t like to change my pants",
            "I really don’t like to change my pants",
            "I don’t want to change my pants",
            "I really don’t want to change my pants",
            "I don’t want to change my pants again",
            "I really don’t want to change my pants again"

    }, {"I like to change my leggings",
            "I really like to change my leggings",
            "I want to change my leggings",
            "I really want to change my leggings",
            "I want to change my leggings again",
            "I really want to change my leggings again",
            "I don’t like to change my leggings",
            "I really don’t like to change my leggings",
            "I don’t want to change my leggings",
            "I really don’t want to change my leggings",
            "I don’t want to change my leggings again",
            "I really don’t want to change my leggings again"

    }, {"I like to change my slacks",
            "I really like to change my slacks",
            "I want to change my slacks",
            "I really want to change my slacks",
            "I want to change my slacks again",
            "I really want to change my slacks again",
            "I don’t like to change my slacks",
            "I really don’t like to change my slacks",
            "I don’t want to change my slacks",
            "I really don’t want to change my slacks",
            "I don’t want to change my slacks again",
            "I really don’t want to change my slacks again"

    }, {"I like to change my shorts",
            "I really like to change my shorts",
            "I want to change my shorts",
            "I really want to change my shorts",
            "I want to change my shorts again",
            "I really want to change my shorts again",
            "I don’t like to change my shorts",
            "I really don’t like to change my shorts",
            "I don’t want to change my shorts",
            "I really don’t want to change my shorts",
            "I don’t want to change my shorts again",
            "I really don’t want to change my shorts again"

    }, {"I like to change my inner Wear",
            "I really like to change my inner Wear",
            "I want to change my inner Wear",
            "I really want to change my inner Wear",
            "I want to change my inner Wear again",
            "I really want to change my inner Wear again",
            "I don’t like to change my inner Wear",
            "I really don’t like to change my inner Wear",
            "I don’t want to change my inner Wear",
            "I really don’t want to change my inner Wear",
            "I don’t want to change my inner Wear again",
            "I really don’t want to change my inner Wear again"

    }, {"I like to change my foot Wear",
            "I really like to change my foot Wear",
            "I want to change my foot Wear",
            "I really want to change my foot Wear",
            "I want to change my foot Wear again",
            "I really want to change my foot Wear again",
            "I don’t like to change my foot Wear",
            "I really don’t like to change my foot Wear",
            "I don’t want to change my foot Wear",
            "I really don’t want to change my foot Wear",
            "I don’t want to change my foot Wear again",
            "I really don’t want to change my foot Wear again",

    }, {"I like to change my shoes",
            "I really like to change my shoes",
            "I want to change my shoes",
            "I really want to change my shoes",
            "I want to change my shoes again",
            "I really want to change my shoes again",
            "I don’t like to change my shoes",
            "I really don’t like to change my shoes",
            "I don’t want to change my shoes",
            "I really don’t want to change my shoes",
            "I don’t want to change my shoes again",
            "I really don’t want to change my shoes again"

    }, {"I like to change my socks",
            "I really like to change my socks",
            "I want to change my socks",
            "I really want to change my socks",
            "I want to change my socks again",
            "I really want to change my socks again",
            "I don’t like to change my socks",
            "I really don’t like to change my socks",
            "I don’t want to change my socks",
            "I really don’t want to change my socks",
            "I don’t want to change my socks again",
            "I really don’t want to change my socks again"

    }, {"I like to Wear night clothes",
            "I really like to Wear night clothes",
            "I want to Wear night clothes",
            "I really want to Wear night clothes",
            "I want to Wear night clothes again",
            "I really want to Wear night clothes again",
            "I don’t like to Wear night clothes",
            "I really don’t like to Wear night clothes",
            "I don’t want to Wear night clothes",
            "I really don’t want to Wear night clothes",
            "I don’t want to Wear night clothes again",
            "I really don’t want to Wear night clothes again"

    }, {"I like to wear shirts",
            "I really like to wear shirts",
            "I want to wear a shirt",
            "I really want to wear a shirt",
            "I want to wear a shirt again",
            "I really want to wear a shirt again",
            "I don’t like to Wear shirts",
            "I really don’t like to Wear shirts",
            "I don’t want to wear a shirt",
            "I really don’t want to wear a shirt",
            "I don’t want to wear a shirt again",
            "I really don’t want to wear a shirt again"

    }, {"I like to wear t-shirts",
            "I really like to wear t-shirts",
            "I want to wear a t-shirt",
            "I really want to wear a t-shirt",
            "I want to wear a t-shirt again",
            "I really want to wear a t-shirt again",
            "I don’t like to wear t-shirts",
            "I really don’t like to wear t-shirts",
            "I don’t want to wear a t-shirt",
            "I really don’t want to wear a t-shirt",
            "I don’t want to wear a t-shirt again",
            "I really don’t want to wear a t-shirt again"

    }, {"I like to wear frocks",
            "I really like to wear frocks",
            "I want to wear a frock",
            "I really want to wear a frock",
            "I want to wear a frock again",
            "I really want to wear a frock again",
            "I don’t like to wear frocks",
            "I really don’t like to wear frocks",
            "I don’t want to wear a frock",
            "I really don’t want to wear a frock",
            "I don’t want to wear a frock again",
            "I really don’t want to wear a frock again"

    }, {"I like to Wear pants",
            "I really like to Wear pants",
            "I want to Wear pants",
            "I really want to Wear pants",
            "I want to Wear pants again",
            "I really want to Wear pants again",
            "I don’t like to Wear pants",
            "I really don’t like to Wear pants",
            "I don’t want to Wear pants",
            "I really don’t want to Wear pants",
            "I don’t want to Wear pants again",
            "I really don’t want to Wear pants again"

    }, {"I like to Wear slacks",
            "I really like to Wear slacks",
            "I want to Wear slacks",
            "I really want to Wear slacks",
            "I want to Wear slacks again",
            "I really want to Wear slacks again",
            "I don’t like to Wear slacks",
            "I really don’t like to Wear slacks",
            "I don’t want to Wear slacks",
            "I really don’t want to Wear slacks",
            "I don’t want to Wear slacks again",
            "I really don’t want to Wear slacks again"

    }, {"I like to Wear leggings",
            "I really like to Wear leggings",
            "I want to Wear leggings",
            "I really want to Wear leggings",
            "I want to Wear leggings again",
            "I really want to Wear leggings again",
            "I don’t like to Wear leggings",
            "I really don’t like to Wear leggings",
            "I don’t want to Wear leggings",
            "I really don’t want to Wear leggings",
            "I don’t want to Wear leggings again",
            "I really don’t want to Wear leggings again"

    }, {"I like to Wear shorts",
            "I really like to Wear shorts",
            "I want to Wear shorts",
            "I really want to Wear shorts",
            "I want to Wear shorts again",
            "I really want to Wear shorts again",
            "I don’t like to Wear shorts",
            "I really don’t like to Wear shorts",
            "I don’t want to Wear shorts",
            "I really don’t want to Wear shorts",
            "I don’t want to Wear shorts again",
            "I really don’t want to Wear shorts again"

    }, {"I like to Wear salwarkameez",
            "I really like to Wear salwarkameez",
            "I want to Wear salwarkameez",
            "I really want to Wear salwarkameez",
            "I want to Wear salwarkameez again",
            "I really want to Wear salwarkameez again",
            "I don’t like to Wear salwarkameez",
            "I really don’t like to Wear salwarkameez",
            "I don’t want to Wear salwarkameez",
            "I really don’t want to Wear salwarkameez",
            "I don’t want to Wear salwarkameez again",
            "I really don’t want to Wear salwarkameez again"

    }, {"I like to wear sweaters",
            "I really like to wear sweaters",
            "I want to wear a sweater",
            "I really want to wear a sweater",
            "I want to wear a sweater again",
            "I really want to wear a sweater again",
            "I don’t like to wear sweaters",
            "I really don’t like to wear sweaters",
            "I don’t want to wear a sweater",
            "I really don’t want to wear a sweater",
            "I don’t want to wear a sweater again",
            "I really don’t want to wear a sweater again"

    }, {"I like to wear jackets",
            "I really like to wear jackets",
            "I want to wear a jacket",
            "I really want to wear a jacket",
            "I want to wear a jacket again",
            "I really want to wear a jacket again",
            "I don’t like to wear jackets",
            "I really don’t like to wear jackets",
            "I don’t want to wear a jacket",
            "I really don’t want to wear a jacket",
            "I don’t want to wear a jacket again",
            "I really don’t want to wear a jacket again"

    }, {"I like to wear scarves",
            "I really like to wear scarves",
            "I want to wear a scarf",
            "I really want to wear a scarf",
            "I want to wear a scarf again",
            "I really want to wear a scarf again",
            "I don’t like to wear scarves",
            "I really don’t like to wear scarves",
            "I don’t want to wear a scarf",
            "I really don’t want to wear a scarf",
            "I don’t want to wear a scarf again",
            "I really don’t want to wear a scarf again"

    }, {"I like to Wear my cap",
            "I really like to Wear my cap",
            "I want to Wear my cap",
            "I really want to Wear my cap",
            "I want to Wear my cap again",
            "I really want to Wear my cap again",
            "I don’t like to Wear my cap",
            "I really don’t like to Wear my cap",
            "I don’t want to Wear my cap",
            "I really don’t want to Wear my cap",
            "I don’t want to Wear my cap again",
            "I really don’t want to Wear my cap again"

    }, {"I like to Wear my belt",
            "I really like to Wear my belt",
            "I want to Wear my belt",
            "I really want to Wear my belt",
            "I want to Wear my belt again",
            "I really want to Wear my belt again",
            "I don’t like to Wear my belt",
            "I really don’t like to Wear my belt",
            "I don’t want to Wear my belt",
            "I really don’t want to Wear my belt",
            "I don’t want to Wear my belt again",
            "I really don’t want to Wear my belt again"

    }, {"I like to Wear my raincoat",
            "I really like to Wear my raincoat",
            "I want to Wear my raincoat",
            "I really want to Wear my raincoat",
            "I want to Wear my raincoat again",
            "I really want to Wear my raincoat again",
            "I don’t like to Wear my raincoat",
            "I really don’t like to Wear my raincoat",
            "I don’t want to Wear my raincoat",
            "I really don’t want to Wear my raincoat",
            "I don’t want to Wear my raincoat again",
            "I really don’t want to Wear my raincoat again"

    }, {"I like to Wear my spectacles",
            "I really like to Wear my spectacles",
            "I want to Wear my spectacles",
            "I really want to Wear my spectacles",
            "I want to Wear my spectacles again",
            "I really want to Wear my spectacles again",
            "I don’t like to Wear my spectacles",
            "I really don’t like to Wear my spectacles",
            "I don’t want to Wear my spectacles",
            "I really don’t want to Wear my spectacles",
            "I don’t want to Wear my spectacles again",
            "I really don’t want to Wear my spectacles again"

    }, {"I like to Wear my wrist watch",
            "I really like to Wear my wrist watch",
            "I want to Wear my wrist watch",
            "I really want to Wear my wrist watch",
            "I want to Wear my wrist watch again",
            "I really want to Wear my wrist watch again",
            "I don’t like to Wear my wrist watch",
            "I really don’t like to Wear my wrist watch",
            "I don’t want to Wear my wrist watch",
            "I really don’t want to Wear my wrist watch",
            "I don’t want to Wear my wrist watch again",
            "I really don’t want to Wear my wrist watch again"

    }, {"I like to Wear earrings",
            "I really like to Wear earrings",
            "I want to Wear earrings",
            "I really want to Wear earrings",
            "I want to Wear earrings again",
            "I really want to Wear earrings again",
            "I don’t like to Wear earrings",
            "I really don’t like to Wear earrings",
            "I don’t want to Wear earrings",
            "I really don’t want to Wear earrings",
            "I don’t want to Wear earrings again",
            "I really don’t want to Wear earrings again"

    }, {"I like to Wear brace lets",
            "I really like to Wear brace lets",
            "I want to wear a brace let",
            "I really want to wear a brace let",
            "I want to wear a brace let again",
            "I really want to wear a brace let again",
            "I don’t like to Wear brace lets",
            "I really don’t like to Wear brace lets",
            "I don’t want to wear a brace let",
            "I really don’t want to wear a brace let",
            "I don’t want to wear a brace let again",
            "I really don’t want to wear a brace let again"

    }, {"I like to Wear necklaces",
            "I really like to Wear necklaces",
            "I want to Wear a necklace",
            "I really want to Wear a necklace",
            "I want to Wear a necklace again",
            "I really want to Wear a necklace again",
            "I don’t like to Wear necklaces",
            "I really don’t like to Wear necklaces",
            "I don’t want to Wear a necklace",
            "I really don’t want to Wear a necklace",
            "I don’t want to Wear a necklace again",
            "I really don’t want to Wear a necklace again"

    },  {"I like to wear a बिंदी",
            "I really like to wear a बिंदी",
            "I want to wear a बिंदी",
            "I really want to wear a बिंदी",
            "I want to wear a बिंदी again",
            "I really want to wear a बिंदी again",
            "I don’t like to wear a बिंदी",
            "I really don’t like to wear a बिंदी",
            "I don’t want to wear a बिंदी",
            "I really don’t want to wear a बिंदी",
            "I don’t want to wear a बिंदी again",
            "I really don’t want to wear a बिंदी again"

    }, {"I like to Wear chappals",
            "I really like to Wear chappals",
            "I want to Wear chappals",
            "I really want to Wear chappals",
            "I want to Wear chappals again",
            "I really want to Wear chappals again",
            "I don’t like to Wear chappals",
            "I really don’t like to Wear chappals",
            "I don’t want to Wear chappals",
            "I really don’t want to Wear chappals",
            "I don’t want to Wear chappals again",
            "I really don’t want to Wear chappals again"

    }, {}, {}, {}, {}}, {{"I like to comb my हैर",
            "I really like to comb my हैर",
            "I want to comb my हैर",
            "I really want to comb my हैर",
            "I want to comb my हैर again",
            "I really want to comb my हैर again",
            "I don’t like to comb my हैर",
            "I really don’t like to comb my हैर",
            "I don’t want to comb my हैर",
            "I really don’t want to comb my हैर",
            "I don’t want to comb my हैर again",
            "I really don’t want to comb my हैर again"

    }, {"I like to use face wash",
            "I really like to use face wash",
            "I want to use face wash",
            "I really want to use face wash",
            "I want to use face wash again",
            "I really want to use face wash again",
            "I don’t like to use face wash",
            "I really don’t like to use face wash",
            "I don’t want to use face wash",
            "I really don’t want to use face wash",
            "I don’t want to use face wash again",
            "I really don’t want to use face wash again"

    }, {"I like to cut my nails",
            "I really like to cut my nails",
            "I want to cut my nails",
            "I really want to cut my nails",
            "I want to cut my nails again",
            "I really want to cut my nails again",
            "I don’t like to cut my nails",
            "I really don’t like to cut my nails",
            "I don’t want to cut my nails",
            "I really don’t want to cut my nails",
            "I don’t want to cut my nails again",
            "I really don’t want to cut my nails again"

    }, {"I like to blow my nose",
            "I really like to blow my nose",
            "I want to blow my nose",
            "I really want to blow my nose",
            "I want to blow my nose again",
            "I really want to blow my nose again",
            "I don’t like to blow my nose",
            "I really don’t like to blow my nose",
            "I don’t want to blow my nose",
            "I really don’t want to blow my nose",
            "I don’t want to blow my nose again",
            "I really don’t want to blow my nose again"

    }, {"I like to use soap",
            "I really like to use soap",
            "I want a soap",
            "I really want a soap",
            "I want to use a soap again",
            "I really want to use a soap again",
            "I don’t like to use soap",
            "I really don’t like to use soap",
            "I don’t want a soap",
            "I really don’t want a soap",
            "I don’t want to use a soap again",
            "I really don’t want to use a soap again"

    }, {"I like to use shampoo",
            "I really like to use shampoo",
            "I want to use shampoo",
            "I really want to use shampoo",
            "I want more shampoo",
            "I really want some more shampoo",
            "I don’t like to use shampoo",
            "I really don’t like to use shampoo",
            "I don’t want to use shampoo",
            "I really don’t want to use shampoo",
            "I don’t want to use more shampoo",
            "I really don’t want to use any more shampoo"

    }}, {{"I like to use the door",
            "I really like to use the door",
            "I want to open the door",
            "I really want to open the door",
            "I want to open the door again",
            "I really want to open the door again",
            "I don’t like to use the door",
            "I really don’t like to use the door",
            "I want to close the door",
            "I really want to close the door",
            "I want to close the door again",
            "I really want to close the door again"

    }, {"I like to use the fan",
            "I really like to use the fan",
            "I want to switch on the fan",
            "I really want to switch on the fan",
            "I want to switch on the fan again",
            "I really want to switch on the fan again",
            "I don’t like to use the fan",
            "I really don’t like to use the fan",
            "I want to switch off the fan",
            "I really want to switch off the fan",
            "I want to switch off the fan again",
            "I really want to switch off the fan again"

    }, {"I like to use the light",
            "I really like to use the light",
            "I want to switch on the light",
            "I really want to switch on the light",
            "I need more light",
            "I really need more light",
            "I don’t like to use the light",
            "I really don’t like to use the light",
            "I want to switch off the light",
            "I really want to switch off the light",
            "I don’t need more light",
            "I really don’t need any more light"

    }, {"I like to use the window",
            "I really like to use the window",
            "I want to open the window",
            "I really want to open the window",
            "I want to open the window again",
            "I really want to open the window again",
            "I don’t like to use the window",
            "I really don’t like to use the window",
            "I want to close the window",
            "I really want to close the window",
            "I want to close the window again",
            "I really want to close the window again"

    }, {"I like to sleep on the bed",
            "I really like to sleep on the bed",
            "I want to sleep on the bed",
            "I really want to sleep on the bed",
            "I want to sleep some more on the bed",
            "I really want to sleep a bit longer on the bed",
            "I don’t like to sleep on the bed",
            "I really don’t like to sleep on the bed",
            "I don’t want to sleep on the bed",
            "I really don’t want to sleep on the bed",
            "I don’t want to sleep any more on the bed",
            "I really don’t want to sleep on the bed any more"

    }, {"I like to sleep on pillows",
            "I really like to sleep on pillows",
            "I want a pillow",
            "I really want a pillow",
            "I want more pillows",
            "I really want some more pillows",
            "I don’t like to sleep on pillows",
            "I really don’t like to sleep on pillows",
            "I don’t want a pillow",
            "I really don’t want a pillow",
            "I don’t want any pillows",
            "I really don’t want any more pillows"

    }, {"I like to use blankets",
            "I really like to use blankets",
            "I want to use a blanket",
            "I really want to use a blanket",
            "I want more blankets",
            "I really want some more blankets",
            "I don’t like to use blankets",
            "I really don’t like to use blankets",
            "I don’t want to use a blanket",
            "I really don’t want to use a blanket",
            "I don’t want more blankets",
            "I really don’t want any more blankets"

    }, {"I like to feel warm",
            "I really like to feel warm",
            "I want to feel warm",
            "I really want to feel warm",
            "I want to feel very warm",
            "I really want to feel very warm",
            "I don’t like to feel warm",
            "I really don’t like to feel warm",
            "I don’t want to feel warm",
            "I really don’t want to feel warm",
            "I don’t want to feel very warm",
            "I really don’t want to feel very warm"

    }, {"I like to feel cool",
            "I really like to feel cool",
            "I want to feel cool",
            "I really want to feel cool",
            "I want to feel very cool",
            "I really want to feel very cool",
            "I don’t like to feel cold",
            "I really don’t like to feel cold",
            "I don’t want to feel cold",
            "I really don’t want to feel cold",
            "I don’t want to feel very cold",
            "I really don’t want to feel very cold"

    }}, {{"I like to exercise",
            "I really like to exercise",
            "I want to do exercises",
            "I really want to do exercises",
            "I want to do some more exercises",
            "I really want to exercise for a bit longer",
            "I don’t like to exercise",
            "I really don’t like to exercise",
            "I don’t want to do exercises",
            "I really don’t want to do exercises",
            "I don’t want to do more exercises",
            "I really don’t want to exercise any longer"

    }, {"I like to use the swing",
            "I really like to use the swing",
            "I want to use the swing",
            "I really want to use the swing",
            "I want to use the swing some more",
            "I really want to use the swing for a bit longer",
            "I don’t like to use the swing",
            "I really don’t like to use the swing",
            "I don’t want to use the swing",
            "I really don’t want to use the swing",
            "I don’t want to use the swing more",
            "I really don’t want to use the swing any more"

    }, {"I like to use the trampoline",
            "I really like to use the trampoline",
            "I want to use the trampoline",
            "I really want to use the trampoline",
            "I want to use the trampoline some more",
            "I really want to use the trampoline for a bit longer",
            "I don’t like to use the trampoline",
            "I really don’t like to use the trampoline",
            "I don’t want to use the trampoline",
            "I really don’t want to use the trampoline",
            "I don’t want to use the trampoline any more",
            "I really don’t want to use the trampoline any longer"

    }, {"I like to use the swiss ball",
            "I really like to use the swiss ball",
            "I want to use the swiss ball",
            "I really want to use the swiss ball",
            "I want to use the swiss ball some more",
            "I really want to use the swiss ball for a bit longer",
            "I don’t like to use the swiss ball",
            "I really don’t like to use the swiss ball",
            "I don’t want to use the swiss ball",
            "I really don’t want to use the swiss ball",
            "I don’t want to use the swiss ball any more",
            "I really don’t want to use the swiss ball any longer"

    }, {"I like to use the blanket",
            "I really like to use the blanket",
            "I want to use the blanket",
            "I really want to use the blanket",
            "I want to use the blanket again",
            "I really want to use the blanket again",
            "I don’t like to use the blanket",
            "I really don’t like to use the blanket",
            "I don’t want to use the blanket",
            "I really don’t want to use the blanket",
            "I don’t want to use the blanket again",
            "I really don’t want to use the blanket again"

    }, {"I like to use the ball pit",
            "I really like to use the ball pit",
            "I want to use the ball pit",
            "I really want to use the ball pit",
            "I want to use the ball pit again",
            "I really want to use the ball pit again",
            "I don’t like to use the ball pit",
            "I really don’t like to use the ball pit",
            "I don’t want to use the ball pit",
            "I really don’t want to use the ball pit",
            "I don’t want to use the ball pit again",
            "I really don’t want to use the ball pit again"

    }, {"I like to do hand activities",
            "I really like to do hand activities",
            "I want to do hand activities",
            "I really want to do hand activities",
            "I want to do some more hand activities",
            "I really want to do some more hand activities",
            "I don’t like to do hand activities",
            "I really don’t like to do hand activities",
            "I don’t want to do hand activities",
            "I really don’t want to do hand activities",
            "I don’t want to do any more hand activities",
            "I really don’t want to do any more hand activities"

    }, {"I like to do leg exercises",
            "I really like to do leg exercises",
            "I want to do leg exercises",
            "I really want to do leg exercises",
            "I want to do some more leg exercises",
            "I really want to do some more leg exercises",
            "I don’t like to do leg exercises",
            "I really don’t like to do leg exercises",
            "I don’t want to do leg exercises",
            "I really don’t want to do leg exercises",
            "I don’t want to do any more leg exercises",
            "I really don’t want to do any more leg exercises"

    }, {"I like to Wear body vests",
            "I really like to Wear body vests",
            "I want to Wear body vests",
            "I really want to Wear body vests",
            "I want to Wear body vests again",
            "I really want to Wear body vests again",
            "I don’t like to Wear body vests",
            "I really don’t like to Wear body vests",
            "I don’t want to Wear body vests",
            "I really don’t want to Wear body vests",
            "I don’t want to Wear body vests again",
            "I really don’t want to Wear body vests again"


    }}, {{"I like to wake up",
            "I really like to wake up",
            "I want to wake up",
            "I really want to wake up",
            "I want to wake up again",
            "I really want to wake up again",
            "I don’t like to wake up",
            "I really don’t like to wake up",
            "I don’t want to wake up",
            "I really don’t want to wake up",
            "I don’t want to wake up again",
            "I really don’t want to wake up again"

    }, {"I like to wash my face",
            "I really like to wash my face",
            "I want to wash my face",
            "I really want to wash my face",
            "I want to wash my face again",
            "I really want to wash my face again",
            "I don’t like to wash my face",
            "I really don’t like to wash my face",
            "I don’t want to wash my face",
            "I really don’t want to wash my face",
            "I don’t want to wash my face again",
            "I really don’t want to wash my face again"

    }, {"I like to go to the bathroom",
            "I really like to go to the bathroom",
            "I want to go to the bathroom",
            "I really want to go to the bathroom",
            "I want to go to the bathroom again",
            "I really want to go to the bathroom again",
            "I don’t like to go to the bathroom",
            "I really don’t like to go to the bathroom",
            "I don’t want to go to the bathroom",
            "I really don’t want to go to the bathroom",
            "I don’t want to go to the bathroom again",
            "I really don’t want to go to the bathroom again"

    }, {"I like to brush my teeth",
            "I really like to brush my teeth",
            "I want to brush my teeth",
            "I really want to brush my teeth",
            "I want to brush my teeth again",
            "I really want to brush my teeth again",
            "I don’t like to brush my teeth",
            "I really don’t like to brush my teeth",
            "I don’t want to brush my teeth",
            "I really don’t want to brush my teeth",
            "I don’t want to brush my teeth again",
            "I really don’t want to brush my teeth again"

    }, {"I have to remove my clothes",
            "I really have to remove my clothes",
            "I want to remove my clothes",
            "I really want to remove my clothes",
            "I want to remove my clothes again",
            "I really want to remove my clothes again",
            "I don’t have to remove my clothes",
            "I really don’t have to remove my clothes",
            "I don’t want to remove my clothes",
            "I really don’t want to remove my clothes",
            "I don’t want to remove my clothes again",
            "I really don’t want to remove my clothes again"

    }, {"I like to have a bath",
            "I really like to have a bath",
            "I want to have a bath",
            "I really want to have a bath",
            "I want to have a bath again",
            "I really want to have a bath again",
            "I don’t like to have a bath",
            "I really don’t like to have a bath",
            "I don’t want to have a bath",
            "I really don’t want to have a bath",
            "I don’t want to have a bath again",
            "I really don’t want to have a bath again"

    }, {"I like to get dressed",
            "I really like to get dressed",
            "I want to get dressed",
            "I really want to get dressed",
            "I want to get dressed again",
            "I really want to get dressed again",
            "I don’t like to get dressed",
            "I really don’t like to get dressed",
            "I don’t want to get dressed",
            "I really don’t want to get dressed",
            "I don’t want to get dressed again",
            "I really don’t want to get dressed again"

    }, {"I like to comb my हैर",
            "I really like to comb my हैर",
            "I want to comb my हैर",
            "I really want to comb my हैर",
            "I want to comb my हैर again",
            "I really want to comb my हैर again",
            "I don’t like to comb my हैर",
            "I really don’t like to comb my हैर",
            "I don’t want to comb my हैर",
            "I really don’t want to comb my हैर",
            "I don’t want to comb my हैर again",
            "I really don’t want to comb my हैर again"

    },  {"I like to eat brekfust",
            "I really like to eat brekfust",
            "I want to eat brekfust",
            "I really want to eat brekfust",
            "I want to eat more brekfust",
            "I really want to eat some more brekfust",
            "I don’t like to eat brekfust",
            "I really don’t like to eat brekfust",
            "I don’t want to eat brekfust",
            "I really don’t want to eat brek fust",
            "I don’t want to eat more brek fust",
            "I really don’t want to eat any more brek fust"

    }, {"I like to pack my lunch box",
            "I really like to pack my lunch box",
            "I want to pack my lunch box",
            "I really want to pack my lunch box",
            "I want to pack my lunch box again",
            "I really want to pack my lunch box again",
            "I don’t like to pack my lunch box",
            "I really don’t like to pack my lunch box",
            "I don’t want to pack my lunch box",
            "I really don’t want to pack my lunch box",
            "I don’t want to pack my lunch box again",
            "I really don’t want to pack my lunch box again"

    }, {"I like to pack my school bag",
            "I really like to pack my school bag",
            "I want to pack my school bag",
            "I really want to pack my school bag",
            "I want to pack my school bag again",
            "I really want to pack my school bag again",
            "I don’t like to pack my school bag",
            "I really don’t like to pack my school bag",
            "I don’t want to pack my school bag",
            "I really don’t want to pack my school bag",
            "I don’t want to pack my school bag again",
            "I really don’t want to pack my school bag again"

    }, {"I like to go to school",
            "I really like to go to school",
            "I want to go to school",
            "I really want to go to school",
            "I want to go to school again",
            "I really want to go to school again",
            "I don’t like to go to school",
            "I really don’t like to go to school",
            "I don’t want to go to school",
            "I really don’t want to go to school",
            "I don’t want to go to school again",
            "I really don’t want to go to school again"

    }, {

    }}, {{"I like to eat dinner",
            "I really like to eat dinner",
            "I want to eat dinner",
            "I really want to eat dinner",
            "I want to eat more dinner",
            "I really want to eat some more dinner",
            "I don’t like to eat dinner",
            "I really don’t like to eat dinner",
            "I don’t want to eat dinner",
            "I really don’t want to eat dinner",
            "I don’t want to eat more dinner",
            "I really don’t want to eat any more dinner"

    }, {"I like to Wear night clothes",
            "I really like to Wear night clothes",
            "I want to Wear night clothes",
            "I really want to Wear night clothes",
            "I want to Wear night clothes again",
            "I really want to Wear night clothes again",
            "I don’t like to Wear night clothes",
            "I really don’t like to Wear night clothes",
            "I don’t want to Wear night clothes",
            "I really don’t want to Wear night clothes",
            "I don’t want to Wear night clothes again",
            "I really don’t want to Wear night clothes again"

    }, {"I like to brush my teeth",
            "I really like to brush my teeth",
            "I want to brush my teeth",
            "I really want to brush my teeth",
            "I want to brush my teeth again",
            "I really want to brush my teeth again",
            "I don’t like to brush my teeth",
            "I really don’t like to brush my teeth",
            "I don’t want to brush my teeth",
            "I really don’t want to brush my teeth",
            "I don’t want to brush my teeth again",
            "I really don’t want to brush my teeth again"

    }, {"I like to read stories",
            "I really like to read stories",
            "I want to read a story",
            "I really want to read a story",
            "I want more stories",
            "I want to read many more stories",
            "I don’t like to read stories",
            "I really don’t like to read stories",
            "I don’t want to read a story",
            "I really don’t want to read a story",
            "I don’t want to read more stories",
            "I really don’t want to read any more stories"

    }, {"I like to say goodnight to every1",
            "I really like to say goodnight to every1",
            "I want to say goodnight to every1",
            "I really want to say goodnight to every1",
            "I want to say goodnight to every1 again",
            "I really want to say goodnight to every1 again",
            "I don’t like to say goodnight to every1",
            "I really don’t like to say goodnight to every1",
            "I don’t want to say goodnight to every1",
            "I really don’t want to say goodnight to every1",
            "I don’t want to say goodnight to every1 again",
            "I really don’t want to say goodnight to every1 again"

    }, {"I like to say my prayers",
            "I really like to say my prayers",
            "I want to say my prayers",
            "I really want to say my prayers",
            "I want to say my prayers again",
            "I really want to say my prayers again",
            "I don’t like to say my prayers",
            "I really don’t like to say my prayers",
            "I don’t want to say my prayers",
            "I really don’t want to say my prayers",
            "I don’t want to say my prayers again",
            "I really don’t want to say my prayers again"

    }, {


    }}}, {{{"I like bread",
            "I really like bread",
            "I want some bread",
            "I really want some bread",
            "I want more bread",
            "I really want some more bread",
            "I don’t like bread",
            "I really don’t like bread",
            "I don’t want any bread",
            "I really don’t want any bread",
            "I don’t want more bread",
            "I really don’t want any more bread"

    }, {"I like cornflakes",
            "I really like cornflakes",
            "I want some cornflakes",
            "I really want some cornflakes",
            "I want more cornflakes",
            "I really want some more cornflakes",
            "I don’t like cornflakes",
            "I really don’t like cornflakes",
            "I don’t want any cornflakes",
            "I really don’t want any cornflakes",
            "I don’t want more cornflakes",
            "I really don’t want any more cornflakes"

    }, {"I like aaloo poori",
            "I really like aaloo poori",
            "I want some aaloo poori",
            "I really want some aaloo poori",
            "I want more aaloo poori",
            "I really want some more aaloo poori",
            "I don’t like aaloo poori",
            "I really don’t like aaloo poori",
            "I don’t want any aaloo poori",
            "I really don’t want any aaloo poori",
            "I don’t want more aaloo poori",
            "I really don’t want any more aaloo poori"

    }, {"I like eggs",
            "I really like eggs",
            "I want some eggs",
            "I really want some eggs",
            "I want more eggs",
            "I really want some more eggs",
            "I don’t like eggs",
            "I really don’t like eggs",
            "I don’t want any eggs",
            "I really don’t want any eggs",
            "I don’t want more eggs",
            "I really don’t want any more eggs"

    }, {"I like poha",
            "I really like poha",
            "I want some poha",
            "I really want some poha",
            "I want more poha",
            "I really want some more poha",
            "I don’t like poha",
            "I really don’t like poha",
            "I don’t want any poha",
            "I really don’t want any poha",
            "I don’t want more poha",
            "I really don’t want any more poha"

    }, {"I like upma",
            "I really like upma",
            "I want some upma",
            "I really want some upma",
            "I want more upma",
            "I really want some more upma",
            "I don’t like upma",
            "I really don’t like upma",
            "I don’t want any upma",
            "I really don’t want any upma",
            "I don’t want more upma",
            "I really don’t want any more upma"

    }, {"I like khichdi",
            "I really like khichdi",
            "I want some khichdi",
            "I really want some khichdi",
            "I want more khichdi",
            "I really want some more khichdi",
            "I don’t like khichdi",
            "I really don’t like khichdi",
            "I don’t want any khichdi",
            "I really don’t want any khichdi",
            "I don’t want more khichdi",
            "I really don’t want any more khichdi"

    }, {"I like idlys",
            "I really like idlys",
            "I want some idlys",
            "I really want some idlys",
            "I want more idlys",
            "I really want some more idlys",
            "I don’t like idlys",
            "I really don’t like idlys",
            "I don’t want idli",
            "I really don’t want idli",
            "I don’t want more idlys",
            "I really don’t want any more idlys"

    },  {"I like dosaaa",
            "I really like dosaaa",
            "I want a dosaaa",
            "I really want a dosaaa",
            "I want another dosaaa",
            "I really want another dosaaa",
            "I don’t like dosaaa",
            "I really don’t like dosaaa",
            "I don’t want a dosaaa",
            "I really don’t want a dosaaa",
            "I don’t want another dosaaa",
            "I really don’t want another dosaaa"

    }, {"I like paraathaas ",
            "I really like paraathaas",
            "I want a paraathaa",
            "I really want a paraathaa",
            "I want more paraathaas ",
            "I really want some more paraathaas ",
            "I don’t like paraathaas ",
            "I really don’t like paraathaas ",
            "I don’t want a paraathaa",
            "I really don’t want a paraathaa",
            "I don’t want more paraathaas ",
            "I really don’t want any more paraathaas"

    }, {"I like omlettes",
            "I really like omlettes",
            "I want an omlette",
            "I really want an omlette",
            "I want more omlettes",
            "I really want some more omlettes",
            "I don’t like omlettes",
            "I really don’t like omlettes",
            "I don’t want an omlette",
            "I really don’t want an omlette",
            "I don’t want more omlettes",
            "I really don’t want any more omlettes",

    }, {"I like मेदु वड़ा",
            "I really like मेदु वड़ा",
            "I want a मेदु वड़ा",
            "I really want a मेदु वड़ा",
            "I want another मेदु वड़ा",
            "I really want another मेदु वड़ा",
            "I don’t like मेदु वड़ा",
            "I really don’t like मेदु वड़ा",
            "I don’t want a मेदु वड़ा",
            "I really don’t want a मेदु वड़ा",
            "I don’t want another मेदु वड़ा",
            "I really don’t want another मेदु वड़ा"

    }, {"I like porridge",
            "I really like porridge",
            "I want some porridge",
            "I really want some porridge",
            "I want more porridge",
            "I really want some more porridge",
            "I don’t like porridge",
            "I really don’t like porridge",
            "I don’t want any porridge",
            "I really don’t want any porridge",
            "I don’t want more porridge",
            "I really don’t want any more porridge"

    }, {"I like sandwiches",
            "I really like sandwiches",
            "I want a sandwich",
            "I really want a sandwich",
            "I want more sandwiches",
            "I really want some more sandwiches",
            "I don’t like sandwiches",
            "I really don’t like sandwiches",
            "I don’t want a sandwich",
            "I really don’t want a sandwich",
            "I don’t want more sandwiches",
            "I really don’t want any more sandwiches"

    }, {"I like chutney",
            "I really like chutney",
            "I want some chutney",
            "I really want some chutney",
            "I want more chutney",
            "I really want some more chutney",
            "I don’t like chutney",
            "I really don’t like chutney",
            "I don’t want any chutney",
            "I really don’t want any chutney",
            "I don’t want more chutney",
            "I really don’t want any more chutney"

    }, {"I like saambaar",
            "I really like saambaar",
            "I want some saambaar",
            "I really want some saambaar",
            "I want more saambaar",
            "I really want some more saambaar",
            "I don’t like saambaar",
            "I really don’t like saambaar",
            "I don’t want any saambaar",
            "I really don’t want any saambaar",
            "I don’t want more saambaar",
            "I really don’t want any more saambaar"

    }, {"I like uutappam",
            "I really like uutappam",
            "I want an uutappam",
            "I really want an uutappam",
            "I want another uutappam",
            "I really want another uutappam",
            "I don’t like uutappam",
            "I really don’t like uutappam",
            "I don’t want an uutappam",
            "I really don’t want an uutappam",
            "I don’t want another uutappam",
            "I really don’t want another uutappam"

    }}, {{"I like roti",
            "I really like roti",
            "I want roti",
            "I really want roti",
            "I want more rotees",
            "I really want some more rotees",
            "I don’t like roti",
            "I really don’t like roti",
            "I don’t want roti",
            "I really don’t want roti",
            "I don’t want more rotees",
            "I really don’t want any more rotees"

    }, {"I like sabzi",
            "I really like sabzi",
            "I want some sabzi",
            "I really want some sabzi",
            "I want more sabzi",
            "I really want some more sabzi",
            "I don’t like sabzi",
            "I really don’t like sabzi",
            "I don’t want any sabzi",
            "I really don’t want any sabzi",
            "I don’t want more sabzi",
            "I really don’t want any more sabzi"

    }, {"I like rice",
            "I really like rice",
            "I want some rice",
            "I really want some rice",
            "I want more rice",
            "I really want some more rice",
            "I don’t like rice",
            "I really don’t like rice",
            "I don’t want any rice",
            "I really don’t want any rice",
            "I don’t want more rice",
            "I really don’t want any more rice"

    }, {"I like dal",
            "I really like dal",
            "I want some dal",
            "I really want some dal",
            "I want more dal",
            "I really want some more dal",
            "I don’t like dal",
            "I really don’t like dal",
            "I don’t want any dal",
            "I really don’t want any dal",
            "I don’t want more dal",
            "I really don’t want any more dal"

    }, {"I like dal khichdi",
            "I really like dal khichdi",
            "I want some dal khichdi",
            "I really want some dal khichdi",
            "I want more dal khichdi",
            "I really want some more dal khichdi",
            "I don’t like dal khichdi",
            "I really don’t like dal khichdi",
            "I don’t want any dal khichdi",
            "I really don’t want any dal khichdi",
            "I don’t want more dal khichdi",
            "I really don’t want any more dal khichdi"

    }, {"I like raaigh ta",
            "I really like raaigh ta",
            "I want some raaigh ta",
            "I really want some raaigh ta",
            "I want more raaigh ta",
            "I really want some more raaigh ta",
            "I don’t like raaigh ta",
            "I really don’t like raaigh ta",
            "I don’t want any raaigh ta",
            "I really don’t want any raaigh ta",
            "I don’t want more raaigh ta",
            "I really don’t want any more raaigh ta"

    }, {"I like paraathaas ",
            "I really like paraathaas",
            "I want a paraathaa",
            "I really want a paraathaa",
            "I want more paraathaas ",
            "I really want some more paraathaas ",
            "I don’t like paraathaas ",
            "I really don’t like paraathaas ",
            "I don’t want a paraathaa",
            "I really don’t want a paraathaa",
            "I don’t want more paraathaas ",
            "I really don’t want any more paraathaas"

    }, {"I like curd",
            "I really like curd",
            "I want some curd",
            "I really want some curd",
            "I want more curd",
            "I really want some more curd",
            "I don’t like curd",
            "I really don’t like curd",
            "I don’t want any curd",
            "I really don’t want any curd",
            "I don’t want more curd",
            "I really don’t want any more curd"

    },  {"I like fish",
            "I really like fish",
            "I want some fish",
            "I really want some fish",
            "I want more fish",
            "I really want some more fish",
            "I don’t like fish",
            "I really don’t like fish",
            "I don’t want any fish",
            "I really don’t want any fish",
            "I don’t want more fish",
            "I really don’t want any more fish"

    }, {"I like chicken",
            "I really like chicken",
            "I want some chicken",
            "I really want some chicken",
            "I want more chicken",
            "I really want some more chicken",
            "I don’t like chicken",
            "I really don’t like chicken",
            "I don’t want any chicken",
            "I really don’t want any chicken",
            "I don’t want more chicken",
            "I really don’t want any more chicken"

    }, {"I like pork",
            "I really like pork",
            "I want some pork",
            "I really want some pork",
            "I want more pork",
            "I really want some more pork",
            "I don’t like pork",
            "I really don’t like pork",
            "I don’t want any pork",
            "I really don’t want any pork",
            "I don’t want more pork",
            "I really don’t want any more pork"

    }, {"I like mutton",
            "I really like mutton",
            "I want some mutton",
            "I really want some mutton",
            "I want more mutton",
            "I really want some more mutton",
            "I don’t like mutton",
            "I really don’t like mutton",
            "I don’t want any mutton",
            "I really don’t want any mutton",
            "I don’t want more mutton",
            "I really don’t want any more mutton"

    }, {"I like crabbb meat",
            "I really like crabbb meat",
            "I want some crabbb meat",
            "I really want some crabbb meat",
            "I want more crabbb meat",
            "I really want some more crabbb meat",
            "I don’t like crabbb meat",
            "I really don’t like crabbb meat",
            "I don’t want any crabbb meat",
            "I really don’t want any crabbb meat",
            "I don’t want more crabbb meat",
            "I really don’t want any more crabbb meat"

    }, {"I like turkey",
            "I really like turkey",
            "I want some turkey",
            "I really want some turkey",
            "I want more turkey",
            "I really want some more turkey",
            "I don’t like turkey",
            "I really don’t like turkey",
            "I don’t want any turkey",
            "I really don’t want any turkey",
            "I don’t want more turkey",
            "I really don’t want any more turkey"

    }, {"I like pizza",
            "I really like pizza",
            "I want some pizza",
            "I really want some pizza",
            "I want more pizza",
            "I really want some more pizza",
            "I don’t like pizza",
            "I really don’t like pizza",
            "I don’t want pizza",
            "I really don’t want pizza",
            "I don’t want more pizza",
            "I really don’t want any more pizza"

    }, {"I like salad",
            "I really like salad",
            "I want some salad",
            "I really want some salad",
            "I want more salad",
            "I really want some more salad",
            "I don’t like salad",
            "I really don’t like salad",
            "I don’t want any salad",
            "I really don’t want any salad",
            "I don’t want more salad",
            "I really don’t want any more salad"

    },  {"I like soup",
            "I really like soup",
            "I want some soup",
            "I really want some soup",
            "I want more soup",
            "I really want some more soup",
            "I don’t like soup",
            "I really don’t like soup",
            "I don’t want any soup",
            "I really don’t want any soup",
            "I don’t want more soup",
            "I really don’t want any more soup"

    }, {"I like pastaa",
            "I really like pastaa",
            "I want some pastaa",
            "I really want some pastaa",
            "I want more pastaa",
            "I really want some more pastaa",
            "I don’t like pastaa",
            "I really don’t like pastaa",
            "I don’t want any pastaa",
            "I really don’t want any pastaa",
            "I don’t want more pastaa",
            "I really don’t want any more pastaa"

    }, {"I like noodles",
            "I really like noodles",
            "I want some noodles",
            "I really want some noodles",
            "I want more noodles",
            "I really want some more noodles",
            "I don’t like noodles",
            "I really don’t like noodles",
            "I don’t want any noodles",
            "I really don’t want any noodles",
            "I don’t want more noodles",
            "I really don’t want any more noodles"

    }, {"I like Italian food",
            "I really like Italian food",
            "I want some Italian food",
            "I really want some Italian food",
            "I want more Italian food",
            "I really want some more Italian food",
            "I don’t like Italian food",
            "I really don’t like Italian food",
            "I don’t want any Italian food",
            "I really don’t want any Italian food",
            "I don’t want more Italian food",
            "I really don’t want any more Italian food"

    }, {"I like pav bhaji",
            "I really like pav bhaji",
            "I want some pav bhaji",
            "I really want some pav bhaji",
            "I want more pav bhaji",
            "I really want some more pav bhaji",
            "I don’t like pav bhaji",
            "I really don’t like pav bhaji",
            "I don’t want any pav bhaji",
            "I really don’t want any pav bhaji",
            "I don’t want some more pav bhaji",
            "I really don’t want any more pav bhaji"

    }, {"I like bhakri",
            "I really like bhakri",
            "I want a bhakri",
            "I really want a bhakri",
            "I want another bhakri",
            "I really want another bhakri",
            "I don’t like bhakri",
            "I really don’t like bhakri",
            "I don’t want a bhakri",
            "I really don’t want a bhakri",
            "I don’t want another bhakri",
            "I really don’t want another bhakri"

    }}, {{"I like cakes",
            "I really like cakes",
            "I want a cake",
            "I really want a cake",
            "I want more cakes",
            "I really want some more cakes",
            "I don’t like cakes",
            "I really don’t like cakes",
            "I don’t want a cake",
            "I really don’t want a cake",
            "I don’t want more cakes",
            "I really don’t want any more cakes"

    }, {"I like ice-cream",
            "I really like ice-cream",
            "I want an ice-cream",
            "I really want an ice-cream",
            "I want more ice-cream",
            "I really want some more ice-cream",
            "I don’t like ice-cream",
            "I really don’t like ice-cream",
            "I don’t want an ice-cream",
            "I really don’t want an ice-cream",
            "I don’t want more ice-cream",
            "I really don’t want any more ice-cream"

    }, {"I like gajar halva",
            "I really like gajar halva",
            "I want some gajar halva",
            "I really want some gajar halva",
            "I want more gajar halva",
            "I really want some more gajar halva",
            "I don’t like gajar halva",
            "I really don’t like gajar halva",
            "I don’t want any gajar halva",
            "I really don’t want any gajar halva",
            "I don’t want more gajar halva",
            "I really don’t want any more gajar halva"

    }, {"I like gulab jamun",
            "I really like gulab jamun",
            "I want some gulab jamun",
            "I really want some gulab jamun",
            "I want more gulab jamun",
            "I really want some more gulab jamun",
            "I don’t like gulab jamun",
            "I really don’t like gulab jamun",
            "I don’t want any gulab jamun",
            "I really don’t want any gulab jamun",
            "I don’t want more gulab jamun",
            "I really don’t want any more gulab jamun"

    }, {"I like lud doos",
            "I really like lud doos",
            "I want a lud doo",
            "I really want a lud doo",
            "I want more lud doos",
            "I really want some more lud doos",
            "I don’t like lud doos",
            "I really don’t like lud doos",
            "I don’t want a lud doo",
            "I really don’t want a lud doo",
            "I don’t want more lud doos",
            "I really don’t want any more lud doos"

    }, {"I like burrfees",
            "I really like burrfees",
            "I want a barfi",
            "I really want a barfi",
            "I want more burrfees",
            "I really want some more burrfees",
            "I don’t like burrfees",
            "I really don’t like burrfees",
            "I don’t want a barfi",
            "I really don’t want a barfi",
            "I don’t want more burrfees",
            "I really don’t want any more burrfees"

    }, {"I like jalaebees",
            "I really like jalaebees",
            "I want a jalebi",
            "I really want a jalebi",
            "I want more jalaebees",
            "I really want some more jalaebees",
            "I don’t like jalaebees",
            "I really don’t like jalaebees",
            "I don’t want a jalebi",
            "I really don’t want a jalebi",
            "I don’t want more jalaebees",
            "I really don’t want any more jalaebees"

    }, {"I like fruit salad",
            "I really like fruit salad",
            "I want some fruit salad",
            "I really want some fruit salad",
            "I want more fruit salad",
            "I really want some more fruit salad",
            "I don’t like fruit salad",
            "I really don’t like fruit salad",
            "I don’t want any fruit salad",
            "I really don’t want any fruit salad",
            "I don’t want more fruit salad",
            "I really don’t want any more fruit salad"

    }, {"I like rass gullas",
            "I really like rass gullas",
            "I want a rass gulla",
            "I really want a rass gulla",
            "I want more rass gullas",
            "I really want some more rass gullas",
            "I don’t like rass gullas",
            "I really don’t like rass gullas",
            "I don’t want a rass gulla",
            "I really don’t want a rass gulla",
            "I don’t want more rass gullas",
            "I really don’t want any more rass gullas"

    }, {"I like sheeraa",
            "I really like sheeraa",
            "I want some sheeraa",
            "I really want some sheeraa",
            "I want more sheeraa",
            "I really want some more sheeraa",
            "I don’t like sheeraa",
            "I really don’t like sheeraa",
            "I don’t want any sheeraa",
            "I really don’t want any sheeraa",
            "I don’t want more sheeraa",
            "I really don’t want any more sheeraa"

    }}, {{"I like biscuits",
            "I really like biscuits",
            "I want a biscuit",
            "I really want a biscuit",
            "I want more biscuits",
            "I really want some more biscuits",
            "I don’t like biscuits",
            "I really don’t like biscuits",
            "I don’t want a biscuit",
            "I really don’t want a biscuit",
            "I don’t want more biscuits",
            "I really don’t want any more biscuits"

    }, {"I like chaat",
            "I really like chaat",
            "I want some chaat",
            "I really want some chaat",
            "I want more chaat",
            "I really want some more chaat",
            "I don’t like chaat",
            "I really don’t like chaat",
            "I don’t want any chaat",
            "I really don’t want any chaat",
            "I don’t want more chaat",
            "I really don’t want any more chaat"

    }, {"I like chocolates",
            "I really like chocolates",
            "I want a chocolate",
            "I really want a chocolate",
            "I want more chocolates",
            "I really want some more chocolates",
            "I don’t like chocolates",
            "I really don’t like chocolates",
            "I don’t want a chocolate",
            "I really don’t want a chocolate",
            "I don’t want more chocolates",
            "I really don’t want any more chocolates"

    }, {"I like wafers",
            "I really like wafers",
            "I want some wafers",
            "I really want some wafers",
            "I want more wafers",
            "I really want a some more wafers",
            "I don’t like wafers",
            "I really don’t like wafers",
            "I don’t want any wafers",
            "I really don’t want any wafers",
            "I don’t want more wafers",
            "I really don’t want any more wafers"

    }, {"I like sandwiches",
            "I really like sandwiches",
            "I want a sandwich",
            "I really want a sandwich",
            "I want more sandwiches",
            "I really want some more sandwiches",
            "I don’t like sandwiches",
            "I really don’t like sandwiches",
            "I don’t want a sandwich",
            "I really don’t want a sandwich",
            "I don’t want more sandwiches",
            "I really don’t want any more sandwiches"

    }, {"I like noodles",
            "I really like noodles",
            "I want some noodles",
            "I really want some noodles",
            "I want more noodles",
            "I really want some more noodles",
            "I don’t like noodles",
            "I really don’t like noodles",
            "I don’t want any noodles",
            "I really don’t want any noodles",
            "I don’t want more noodles",
            "I really don’t want any more noodles"

    }, {"I like cheese",
            "I really like cheese",
            "I want some cheese",
            "I really want some cheese",
            "I want more cheese",
            "I really want some more cheese",
            "I don’t like cheese",
            "I really don’t like cheese",
            "I don’t want any cheese",
            "I really don’t want any cheese",
            "I don’t want more cheese",
            "I really don’t want any more cheese"

    }, {"I like nuts",
            "I really like nuts",
            "I want some nuts",
            "I really want some nuts",
            "I want more nuts",
            "I really want some more nuts",
            "I don’t like nuts",
            "I really don’t like nuts",
            "I don’t want any nuts",
            "I really don’t want any nuts",
            "I don’t want more nuts",
            "I really don’t want any more nuts"

    }}, {{"I like apples",
            "I really like apples",
            "I want an apple",
            "I really want an apple",
            "I want more apples",
            "I really want some more apples",
            "I don’t like apples",
            "I really don’t like apples",
            "I don’t want an apple",
            "I really don’t want an apple",
            "I don’t want more apples",
            "I really don’t want any more apples"

    }, {"I like bananas",
            "I really like bananas",
            "I want a banana",
            "I really want a banana",
            "I want more bananas",
            "I really want some more bananas",
            "I don’t like bananas",
            "I really don’t like bananas",
            "I don’t want a banana",
            "I really don’t want a banana",
            "I don’t want more bananas",
            "I really don’t want any more bananas"

    }, {"I like grapes",
            "I really like grapes",
            "I want some grapes",
            "I really want some grapes",
            "I want more grapes",
            "I really want some more grapes",
            "I don’t like grapes",
            "I really don’t like grapes",
            "I don’t want any grapes",
            "I really don’t want any grapes",
            "I don’t want more grapes",
            "I really don’t want any more grapes"

    }, {"I like guavas",
            "I really like guavas",
            "I want a guava",
            "I really want a guava",
            "I want more guavas",
            "I really want some more guavas",
            "I don’t like guavas",
            "I really don’t like guavas",
            "I don’t want a guava",
            "I really don’t want a guava",
            "I don’t want more guavas",
            "I really don’t want any more guavas"

    }, {"I like mangoes",
            "I really like mangoes",
            "I want a mango",
            "I really want a mango",
            "I want more mangoes",
            "I really want some more mangoes",
            "I don’t like mangoes",
            "I really don’t like mangoes",
            "I don’t want a mango",
            "I really don’t want a mango",
            "I don’t want more mangoes",
            "I really don’t want any more mangoes"

    }, {"I like oranges",
            "I really like oranges",
            "I want an orange",
            "I really want an orange",
            "I want more oranges",
            "I really want some more oranges",
            "I don’t like oranges",
            "I really don’t like oranges",
            "I don’t want an orange",
            "I really don’t want an orange",
            "I don’t want more oranges",
            "I really don’t want any more oranges"

    }, {"I like pineapples",
            "I really like pineapples",
            "I want a pineapple",
            "I really want a pineapple",
            "I want more pineapples",
            "I really want some more pineapples",
            "I don’t like pineapples",
            "I really don’t like pineapples",
            "I don’t want a pineapple",
            "I really don’t want a pineapple",
            "I don’t want more pineapples",
            "I really don’t want any more pineapples"

    }, {"I like strawberries",
            "I really like strawberries",
            "I want some strawberries",
            "I really want some strawberries",
            "I want more strawberries",
            "I really want some more strawberries",
            "I don’t like strawberries",
            "I really don’t like strawberries",
            "I don’t want any strawberries",
            "I really don’t want any strawberries",
            "I don’t want more strawberries",
            "I really don’t want any more strawberries"

    }, {"I like blueberries",
            "I really like blueberries",
            "I want some blueberries",
            "I really want some blueberries",
            "I want more blueberries",
            "I really want some more blueberries",
            "I don’t like blueberries",
            "I really don’t like blueberries",
            "I don’t want any blueberries",
            "I really don’t want any blueberries",
            "I don’t want more blueberries",
            "I really don’t want any more blueberries"

    }, {"I like pomegranates",
            "I really like pomegranates",
            "I want a pomegranate",
            "I really want a pomegranate",
            "I want more pomegranates",
            "I really want some more pomegranates",
            "I don’t like pomegranates",
            "I really don’t like pomegranates",
            "I don’t want a pomegranate",
            "I really don’t want a pomegranate",
            "I don’t want more pomegranates",
            "I really don’t want any more pomegranates"

    }, {"I like watermelons",
            "I really like watermelons",
            "I want a watermelon",
            "I really want a watermelon",
            "I want more watermelons",
            "I really want some more watermelons",
            "I don’t like watermelons",
            "I really don’t like watermelons",
            "I don’t want a watermelon",
            "I really don’t want a watermelon",
            "I don’t want more watermelons",
            "I really don’t want any more watermelons"

    }, {"I like pears",
            "I really like pears",
            "I want a pear",
            "I really want a pear",
            "I want more pears",
            "I really want some more pears",
            "I don’t like pears",
            "I really don’t like pears",
            "I don’t want a pear",
            "I really don’t want a pear",
            "I don’t want more pears",
            "I really don’t want any more pears"

    }, {"I like papayas",
            "I really like papayas",
            "I want a papaya",
            "I really want a papaya",
            "I want more papayas",
            "I really want some more papayas",
            "I don’t like papayas",
            "I really don’t like papayas",
            "I don’t want a papaya",
            "I really don’t want a papaya",
            "I don’t want more papayas",
            "I really don’t want any more papayas"

    }, {"I like muskmelons",
            "I really like muskmelons",
            "I want a muskmelon",
            "I really want a muskmelon",
            "I want more muskmelons",
            "I really want some more muskmelons",
            "I don’t like muskmelons",
            "I really don’t like muskmelons",
            "I don’t want a muskmelon",
            "I really don’t want a muskmelon",
            "I don’t want more muskmelons",
            "I really don’t want any more muskmelons"

    }, {"I like chikoos",
            "I really like chikoos",
            "I want a chikoo",
            "I really want a chikoo",
            "I want more chikoos",
            "I really want some more chikoos",
            "I don’t like chikoos",
            "I really don’t like chikoos",
            "I don’t want a chikoo",
            "I really don’t want a chikoo",
            "I don’t want more chikoos",
            "I really don’t want any more chikoos"

    }, {"I like jackfruit",
            "I really like jackfruit",
            "I want some jackfruit",
            "I really want some jackfruit",
            "I want more jackfruit",
            "I really want some more jackfruit",
            "I don’t like jackfruit",
            "I really don’t like jackfruit",
            "I don’t want any jackfruit",
            "I really don’t want any jackfruit",
            "I don’t want more jackfruit",
            "I really don’t want any more jackfruit"

    }, {"I like cherries",
            "I really like cherries",
            "I want some cherries",
            "I really want some cherries",
            "I want more cherries",
            "I really want some more cherries",
            "I don’t like cherries",
            "I really don’t like cherries",
            "I don’t want any cherries",
            "I really don’t want any cherries",
            "I don’t want more cherries",
            "I really don’t want any more cherries"

    }}, {{"I like water",
            "I really like water",
            "I want some water",
            "I really want some water",
            "I want more water",
            "I really want some more water",
            "I don’t like water",
            "I really don’t like water",
            "I don’t want any water",
            "I really don’t want any water",
            "I don’t want more water",
            "I really don’t want any more water"

    }, {"I like milk",
            "I really like milk",
            "I want some milk",
            "I really want some milk",
            "I want more milk",
            "I really want some more milk",
            "I don’t like milk",
            "I really don’t like milk",
            "I don’t want any milk",
            "I really don’t want any milk",
            "I don’t want more milk",
            "I really don’t want any more milk"

    }, {"I like bournvita",
            "I really like bournvita",
            "I want some bournvita",
            "I really want some bournvita",
            "I want more bournvita",
            "I really want some more bournvita",
            "I don’t like bournvita",
            "I really don’t like bournvita",
            "I don’t want any bournvita",
            "I really don’t want any bournvita",
            "I don’t want more bournvita",
            "I really don’t want any more bournvita"

    }, {"I like mango juice",
            "I really like mango juice",
            "I want some mango juice",
            "I really want some mango juice",
            "I want more mango juice",
            "I really want some more mango juice",
            "I don’t like mango juice",
            "I really don’t like mango juice",
            "I don’t want any mango juice",
            "I really don’t want any mango juice",
            "I don’t want more mango juice",
            "I really don’t want any more mango juice"

    }, {"I like apple juice",
            "I really like apple juice",
            "I want some apple juice",
            "I really want some apple juice",
            "I want more apple juice",
            "I really want some more apple juice",
            "I don’t like apple juice",
            "I really don’t like apple juice",
            "I don’t want any apple juice",
            "I really don’t want any apple juice",
            "I don’t want more apple juice",
            "I really don’t want any more apple juice"

    }, {"I like orange juice",
            "I really like orange juice",
            "I want some orange juice",
            "I really want some orange juice",
            "I want more orange juice",
            "I really want some more orange juice",
            "I don’t like orange juice",
            "I really don’t like orange juice",
            "I don’t want any orange juice",
            "I really don’t want any orange juice",
            "I don’t want more orange juice",
            "I really don’t want any more  orange juice"

    }, {"I like lemon juice",
            "I really like lemon juice",
            "I want some lemon juice",
            "I really want some lemon juice",
            "I want more lemon juice",
            "I really want some more lemon juice",
            "I don’t like lemon juice",
            "I really don’t like lemon juice",
            "I don’t want any lemon juice",
            "I really don’t want any lemon juice",
            "I don’t want more lemon juice",
            "I really don’t want any more lemon juice"

    }, {"I like pineapple juice",
            "I really like pineapple juice",
            "I want some pineapple juice",
            "I really want some pineapple juice",
            "I want more pineapple juice",
            "I really want some more pineapple juice",
            "I don’t like pineapple juice",
            "I really don’t like pineapple juice",
            "I don’t want any pineapple juice",
            "I really don’t want any pineapple juice",
            "I don’t want more pineapple juice",
            "I really don’t want any more pineapple juice"

    },  {"I like pepsi",
            "I really like pepsi",
            "I want some pepsi",
            "I really want some pepsi",
            "I want more pepsi",
            "I really want some more pepsi",
            "I don’t like pepsi",
            "I really don’t like pepsi",
            "I don’t want any pepsi",
            "I really don’t want any pepsi",
            "I don’t want more pepsi",
            "I really don’t want any more pepsi"

    }, {"I like cocacola",
            "I really like cocacola",
            "I want some cocacola",
            "I really want some cocacola",
            "I want more cocacola",
            "I really want some more cocacola",
            "I don’t like cocacola",
            "I really don’t like cocacola",
            "I don’t want any cocacola",
            "I really don’t want any cocacola",
            "I don’t want more cocacola",
            "I really don’t want any more cocacola"

    }, {"I like mirinda",
            "I really like mirinda",
            "I want some mirinda",
            "I really want some mirinda",
            "I want more mirinda",
            "I really want some more mirinda",
            "I don’t like mirinda",
            "I really don’t like mirinda",
            "I don’t want any mirinda",
            "I really don’t want any mirinda",
            "I don’t want more mirinda",
            "I really don’t want any more mirinda"

    }, {"I like fanta",
            "I really like fanta",
            "I want some fanta",
            "I really want some fanta",
            "I want more fanta",
            "I really want some more fanta",
            "I don’t like fanta",
            "I really don’t like fanta",
            "I don’t want any fanta",
            "I really don’t want any fanta",
            "I don’t want more fanta",
            "I really don’t want any more fanta"

    }, {"I like maaza",
            "I really like maaza",
            "I want some maaza",
            "I really want some maaza",
            "I want more maaza",
            "I really want some more maaza",
            "I don’t like maaza",
            "I really don’t like maaza",
            "I don’t want any maaza",
            "I really don’t want any maaza",
            "I don’t want more maaza",
            "I really don’t want any more maaza"

    }, {"I like sprite",
            "I really like sprite",
            "I want some sprite",
            "I really want some sprite",
            "I want more sprite",
            "I really want some more sprite",
            "I don’t like sprite",
            "I really don’t like sprite",
            "I don’t want any sprite",
            "I really don’t want any sprite",
            "I don’t want more sprite",
            "I really don’t want any more sprite"

    }, {"I like mountain dew",
            "I really like mountain dew",
            "I want some mountain dew",
            "I really want some mountain dew",
            "I want more mountain dew",
            "I really want some more mountain dew",
            "I don’t like mountain dew",
            "I really don’t like mountain dew",
            "I don’t want any mountain dew",
            "I really don’t want any mountain dew",
            "I don’t want more mountain dew",
            "I really don’t want any more mountain dew"

    }, {"I like milk shakes",
            "I really like milk shakes",
            "I want a milk shake",
            "I really want a milk shake",
            "I want more milk shake",
            "I really want some more milk shake",
            "I don’t like milk shake",
            "I really don’t like milk shake",
            "I don’t want a milk shake",
            "I really don’t want a milk shake",
            "I don’t want more milk shake",
            "I really don’t want any more milk shake"
    },  {"I like chocolate milk shake",
            "I really like chocolate milk shake",
            "I want some chocolate milk shake",
            "I really want some chocolate milk shake",
            "I want more chocolate milk shake",
            "I really want some more chocolate milk shake",
            "I don’t like chocolate milk shake",
            "I really don’t like chocolate milk shake",
            "I don’t want any chocolate milk shake",
            "I really don’t want any chocolate milk shake",
            "I don’t want more chocolate milk shake",
            "I really don’t want any more chocolate milk shake"

    }, {"I like strawberry milk shake",
            "I really like strawberry milk shake",
            "I want some strawberry milk shake",
            "I really want some strawberry milk shake",
            "I want more strawberry milk shake",
            "I really want some more strawberry milk shake",
            "I don’t like strawberry milk shake",
            "I really don’t like strawberry milk shake",
            "I don’t want any strawberry milk shake",
            "I really don’t want any strawberry milk shake",
            "I don’t want more strawberry milk shake",
            "I really don’t want any more strawberry milk shake"

    }, {"I like banana milk shake",
            "I really like banana milk shake",
            "I want some banana milk shake",
            "I really want some banana milk shake",
            "I want more banana milk shake",
            "I really want some more banana milk shake",
            "I don’t like banana milk shake",
            "I really don’t like banana milk shake",
            "I don’t want any banana milk shake",
            "I really don’t want any banana milk shake",
            "I don’t want more banana milk shake",
            "I really don’t want any more banana milk shake"

    }, {"I like mango milk shake",
            "I really like mango milk shake",
            "I want some mango milk shake",
            "I really want some mango milk shake",
            "I want more mango milk shake",
            "I really want some more mango milk shake",
            "I don’t like mango milk shake",
            "I really don’t like mango milk shake",
            "I don’t want any mango milk shake",
            "I really don’t want any mango milk shake",
            "I don’t want more mango milk shake",
            "I really don’t want any more mango milk shake"

    }, {"I like chikoo milk shake",
            "I really like chikoo milk shake",
            "I want some chikoo milk shake",
            "I really want some chikoo milk shake",
            "I want more chikoo milk shake",
            "I really want some more chikoo milk shake",
            "I don’t like chikoo milk shake",
            "I really don’t like chikoo milk shake",
            "I don’t want any chikoo milk shake",
            "I really don’t want any chikoo milk shake",
            "I don’t want more chikoo milk shake",
            "I really don’t want any more chikoo milk shake"

    }, {"I like tea",
            "I really like tea",
            "I want some tea",
            "I really want some tea",
            "I want more tea",
            "I really want some more tea",
            "I don’t like tea",
            "I really don’t like tea",
            "I don’t want any tea",
            "I really don’t want any tea",
            "I don’t want more tea",
            "I really don’t want any more tea"

    }, {"I like coffee",
            "I really like coffee",
            "I want some coffee",
            "I really want some coffee",
            "I want more coffee",
            "I really want some more coffee",
            "I don’t like coffee",
            "I really don’t like coffee",
            "I don’t want any coffee",
            "I really don’t want any coffee",
            "I don’t want more coffee",
            "I really don’t want any more coffee"

    }, {"I like cold coffee",
            "I really like cold coffee",
            "I want some cold coffee",
            "I really want some cold coffee",
            "I want more cold coffee",
            "I really want some more cold coffee",
            "I don’t like cold coffee",
            "I really don’t like cold coffee",
            "I don’t want any cold coffee",
            "I really don’t want any cold coffee",
            "I don’t want more cold coffee",
            "I really don’t want any more cold coffee"

    }, {"I like energy drinks",
            "I really like energy drinks",
            "I want an energy drink",
            "I really want an energy drink",
            "I want more energy drinks",
            "I really want some more energy drinks",
            "I don’t like energy drinks",
            "I really don’t like energy drinks",
            "I don’t want an energy drink",
            "I really don’t want an energy drink",
            "I don’t want more energy drinks",
            "I really don’t want any more energy drinks"

    }}, {{"I like to use a bowl to eat",
            "I really like to use a bowl to eat",
            "I want a bowl",
            "I really want a bowl",
            "I want another bowl",
            "I really want another bowl",
            "I don’t like to use a bowl to eat",
            "I really don’t like to use a bowl to eat",
            "I don’t want a bowl",
            "I really don’t want a bowl",
            "I don’t want another bowl",
            "I really don’t want another bowl"

    }, {"I like to use a plate to eat",
            "I really like to use a plate to eat",
            "I want a plate",
            "I really want a plate",
            "I want another plate",
            "I really want another plate",
            "I don’t like to use a plate to eat",
            "I really don’t like to use a plate to eat",
            "I don’t want a plate",
            "I really don’t want a plate",
            "I don’t want another plate",
            "I really don’t want another plate"

    }, {"I like to use a spoon to eat",
            "I really like to use a spoon to eat",
            "I want a spoon",
            "I really want a spoon",
            "I want another spoon",
            "I really want another spoon",
            "I don’t like to use a spoon to eat",
            "I really don’t like to use a spoon to eat",
            "I don’t want a spoon",
            "I really don’t want a spoon",
            "I don’t want another spoon",
            "I really don’t want another spoon"

    }, {"I like to use a fork to eat",
            "I really like to use a fork to eat",
            "I want a fork",
            "I really want a fork",
            "I want another fork",
            "I really want another fork",
            "I don’t like to use a fork to eat",
            "I really don’t like to use a fork to eat",
            "I don’t want a fork",
            "I really don’t want a fork",
            "I don’t want another fork",
            "I really don’t want another fork"

    }, {"I like to use a knife to eat",
            "I really like to use a knife to eat",
            "I want a knife",
            "I really want a knife",
            "I want another knives",
            "I really want another knives",
            "I don’t like to use a knife to eat",
            "I really don’t like to use a knife to eat",
            "I don’t want a knife",
            "I really don’t want a knife",
            "I don’t want another knives",
            "I really don’t want another knives"

    }, {"I like to use a mug to drink",
            "I really like to use a mug to drink",
            "I want a mug",
            "I really want a mug",
            "I want another mug",
            "I really want another mug",
            "I don’t like to use a mug to drink",
            "I really don’t like to use a mug to drink",
            "I don’t want a mug",
            "I really don’t want a mug",
            "I don’t want another mug",
            "I really don’t want another mug"

    }, {"I like to use a cup to drink",
            "I really like to use a cup to drink",
            "I want a cup",
            "I really want a cup",
            "I want another cup",
            "I really want another cup",
            "I don’t like to use a cup to drink",
            "I really don’t like to use a cup to drink",
            "I don’t want a cup",
            "I really don’t want a cup",
            "I don’t want another cup",
            "I really don’t want another cup"

    }, {"I like to use a glass to drink",
            "I really like to use a glass to drink",
            "I want a glass",
            "I really want a glass",
            "I want another glass",
            "I really want another glass",
            "I don’t like to use a glass to drink",
            "I really don’t like to use a glass to drink",
            "I don’t want a glass",
            "I really don’t want a glass",
            "I don’t want another glass",
            "I really don’t want another glass"

    }}, {{"I like butter",
            "I really like butter",
            "I want some butter",
            "I really want some butter",
            "I want more butter",
            "I really want some more butter",
            "I don’t like butter",
            "I really don’t like butter",
            "I don’t want any butter",
            "I really don’t want any butter",
            "I don’t want more butter",
            "I really don’t want any more butter"

    }, {"I like jam",
            "I really like jam",
            "I want some jam",
            "I really want some jam",
            "I want more jam",
            "I really want some more jam",
            "I don’t like jam",
            "I really don’t like jam",
            "I don’t want any jam",
            "I really don’t want any jam",
            "I don’t want more jam",
            "I really don’t want any more jam"

    }, {"I like salt",
            "I really like salt",
            "I want some salt",
            "I really want some salt",
            "I want more salt",
            "I really want some more salt",
            "I don’t like salt",
            "I really don’t like salt",
            "I don’t want any salt",
            "I really don’t want any salt",
            "I don’t want more salt",
            "I really don’t want any more salt"

    }, {"I like pepper",
            "I really like pepper",
            "I want some pepper",
            "I really want some pepper",
            "I want more pepper",
            "I really want some more pepper",
            "I don’t like pepper",
            "I really don’t like pepper",
            "I don’t want any pepper",
            "I really don’t want any pepper",
            "I don’t want more pepper",
            "I really don’t want any more pepper"

    }, {"I like sugar",
            "I really like sugar",
            "I want some sugar",
            "I really want some sugar",
            "I want more sugar",
            "I really want some more sugar",
            "I don’t like sugar",
            "I really don’t like sugar",
            "I don’t want any sugar",
            "I really don’t want any sugar",
            "I don’t want more sugar",
            "I really don’t want any more sugar"

    }, {"I like sauce",
            "I really like sauce",
            "I want some sauce",
            "I really want some sauce",
            "I want more sauce",
            "I really want some more sauce",
            "I don’t like sauce",
            "I really don’t like sauce",
            "I don’t want any sauce",
            "I really don’t want any sauce",
            "I don’t want more sauce",
            "I really don’t want any more sauce"

    }, {"I like pickle",
            "I really like pickle",
            "I want some pickle",
            "I really want some pickle",
            "I want more pickle",
            "I really want some more pickle",
            "I don’t like pickle",
            "I really don’t like pickle",
            "I don’t want any pickle",
            "I really don’t want any pickle",
            "I don’t want more pickle",
            "I really don’t want any more pickle"

    }, {"I like papad",
            "I really like papad",
            "I want some papad",
            "I really want some papad",
            "I want more papad",
            "I really want more papad",
            "I don’t like papad",
            "I really don’t like papad",
            "I don’t want any papad",
            "I really don’t want any papad",
            "I don’t want more papad",
            "I really don’t want any more papad"

    }, {"I like masala",
            "I really like masala",
            "I want masala",
            "I really want masala",
            "I want more masala",
            "I really want some more masala",
            "I don’t like masala",
            "I really don’t like masala",
            "I don’t want masala",
            "I really don’t want masala",
            "I don’t want more masala",
            "I really don’t want any more masala"

    }}}, {{{"I like puzzles",
            "I really like puzzles",
            "I want to play puzzles",
            "I really want to play puzzles",
            "I want to play more puzzles",
            "I really want to play some more puzzles",
            "I don’t like puzzles",
            "I really don’t like puzzles",
            "I don’t want to play puzzles",
            "I really don’t want to play puzzles",
            "I don’t want to play more puzzles",
            "I really don’t want to play any more puzzles"

    }, {"I like board games",
            "I really like board games",
            "I want to play board games",
            "I really want to play board games",
            "I want to play more board games",
            "I really want to play some more board games",
            "I don’t like board games",
            "I really don’t like board games",
            "I don’t want to play board games",
            "I really don’t want to play board games",
            "I don’t want to play more board games",
            "I really don’t want to play any more board games"


    }, {"I like to play with blocks",
            "I really like to play with blocks",
            "I want to play with blocks",
            "I really want to play with blocks",
            "I want to play some more with blocks",
            "I really want to play with blocks for a bit longer",
            "I don’t like to play with blocks",
            "I really don’t like to play with blocks",
            "I don’t want to play with blocks",
            "I really don’t want to play with blocks",
            "I don’t want to play with blocks any more",
            "I really don’t want to play with blocks any longer"

    }, {"I like to play with legos",
            "I really like to play with legos",
            "I want to play with legos",
            "I really want to play with legos",
            "I want to play some more with legos",
            "I really want to play with legos for a bit longer",
            "I don’t like legos",
            "I really don’t like legos",
            "I don’t want to play with legos",
            "I really don’t want to play with legos",
            "I don’t want to play with legos any more",
            "I really don’t want to play with legos any longer"

    }, {"I like to play chess",
            "I really like to play chess",
            "I want to play chess",
            "I really want to play chess",
            "I want to play chess some more",
            "I really want to play chess for a bit longer",
            "I don’t like to play chess",
            "I really don’t like to play chess",
            "I don’t want to play chess",
            "I really don’t want to play chess",
            "I don’t want to play chess any more",
            "I really don’t want to play chess any longer"

    }, {"I like snakes and ladders",
            "I really like snakes and ladders",
            "I want to play snakes and ladders",
            "I really want to play snakes and ladders",
            "I want to play snakes and ladders some more",
            "I really want to play snakes and ladders for a bit longer",
            "I don’t like snakes and ladders",
            "I really don’t like snakes and ladders",
            "I don’t want to play snakes and ladders",
            "I really don’t want to play snakes and ladders",
            "I don’t want to play snakes and ladders any more ",
            "I really don’t want to play snakes and ladders any longer"

    }, {"I like scrabble",
            "I really like scrabble",
            "I want to play scrabble",
            "I really want to play scrabble",
            "I want to play scrabble some more",
            "I really want to play scrabble for a bit longer",
            "I don’t like scrabble",
            "I really don’t like scrabble",
            "I don’t want to play scrabble",
            "I really don’t want to play scrabble",
            "I don’t want to play scrabble any more",
            "I really don’t want to play scrabble any longer"

    }, {"I like videogames",
            "I really like videogames",
            "I want to play videogames",
            "I really want to play videogames",
            "I want to play some more videogames",
            "I really want to play some more videogames",
            "I don’t like videogames",
            "I really don’t like videogames",
            "I don’t want to play videogames",
            "I really don’t want to play videogames",
            "I don’t want to play any more videogames",
            "I really don’t want to play videogames any longer "

    }, {"I like to play with dolls",
            "I really like to play with dolls",
            "I want to play with dolls",
            "I really want to play with dolls",
            "I want to play with dolls for some more time",
            "I really want to play with dolls for a bit longer",
            "I don’t like to play with dolls",
            "I really don’t like to play with dolls",
            "I don’t want to play with dolls",
            "I really don’t want to play with dolls",
            "I don’t want to play with dolls any more",
            "I really don’t want to play with dolls any longer"

    }, {"I like to play with action figures",
            "I really like to play with action figures",
            "I want to play with action figures",
            "I really want to play with action figures",
            "I want to play with action figures for some more time",
            "I really want to play with action figures for a bit longer",
            "I don’t like to play with action figures",
            "I really don’t like to play with action figures",
            "I don’t want to play with action figures",
            "I really don’t want to play with action figures",
            "I don’t want to play with action figures any more",
            "I really don’t want to play with action figures any longer"

    }, {"I like to play with soft toys",
            "I really like to play with soft toys",
            "I want to play with soft toys",
            "I really want to play with soft toys",
            "I want to play with soft toys for some more time",
            "I really want to play with soft toys for a bit longer",
            "I don’t like to play with soft toys",
            "I really don’t like to play with soft toys",
            "I don’t want to play with soft toys",
            "I really don’t want to play with soft toys",
            "I don’t want to play with soft toys any more",
            "I really don’t want to play with soft toys any longer"

    }, {"I like to play with cars",
            "I really like to play with cars",
            "I want to play with cars",
            "I really want to play with cars",
            "I want to play with cars for some more time",
            "I really want to play with cars for a bit longer",
            "I don’t like to play with cars",
            "I really don’t like to play with cars",
            "I don’t want to play with cars",
            "I really don’t want to play with cars",
            "I don’t want to play with cars any more",
            "I really don’t want to play with cars any longer"

    }, {"I like to play with trucks",
            "I really like to play with trucks",
            "I want to play with trucks",
            "I really want to play with trucks",
            "I want to play with trucks for some more time",
            "I really want to play with trucks for a bit longer",
            "I don’t like to play with trucks",
            "I really don’t like to play with trucks",
            "I don’t want to play with trucks",
            "I really don’t want to play with trucks",
            "I don’t want to play with trucks any more",
            "I really don’t want to play with trucks any longer"

    }, {"I like to do art and craft activities",
            "I really like to do art and craft activities",
            "I want to do art and craft activities",
            "I really want to do art and craft activities",
            "I want to do art and craft activities for some more time",
            "I really want to do art and craft activities for a bit longer",
            "I don’t like to do art and craft activities",
            "I really don’t like to do art and craft activities",
            "I don’t want to do art and craft activities",
            "I really don’t want to do art and craft activities",
            "I don’t want to do art and craft activities any more",
            "I really don’t want to do art and craft activities any longer"

    }, {"I like to play with you",
            "I really like to play with you",
            "I want to play with you",
            "I really want to play with you",
            "I want to play with you for some more time",
            "I really want to play with you for a bit longer",
            "I don’t like to play with you",
            "I really don’t like to play with you",
            "I don’t want to play with you",
            "I really don’t want to play with you",
            "I don’t want to play with you any more",
            "I really don’t want to play with you any more",

    }}, {{"I like to go to the playground",
            "I really like to go to the playground",
            "I want to go to the playground",
            "I really want to go to the playground",
            "I want to go to the playground again ",
            "I really want to go to the playground again ",
            "I don’t like to go to the playground",
            "I really don’t like to go to the playground",
            "I don’t want to go to the playground",
            "I really don’t want to go to the playground",
            "I don’t want to go to the playground again ",
            "I really don’t want to go to the playground again "

    }, {"I like to play in the park",
            "I really like to play in the park",
            "I want to play in the park",
            "I really want to play in the park",
            "I want to play in the park for some more time",
            "I really want to play in the park for a bit longer",
            "I don’t like to play in the park",
            "I really don’t like to play in the park",
            "I don’t want to play in the park",
            "I really don’t want to play in the park",
            "I don’t want to play in the park any more",
            "I really don’t want to play in the park any longer"

    }, {"I like to play on the swing",
            "I really like to play on the swing",
            "I want to play on the swing",
            "I really want to play on the swing",
            "I want to play on the swing for some more time",
            "I really want to play on the swing for a bit longer ",
            "I don’t like to play on the swing",
            "I really don’t like to play on the swing",
            "I don’t want to play on the swing",
            "I really don’t want to play on the swing",
            "I don’t want to play on the swing any more",
            "I really don’t want to play on the swing any longer"

    }, {"I like to play on the slide",
            "I really like to play on the slide",
            "I want to play on the slide",
            "I really want to play on the slide",
            "I want to play on the slide for some more time",
            "I really want to play on the slide for a bit longer",
            "I don’t like to play on the slide",
            "I really don’t like to play on the slide",
            "I don’t want to play on the slide",
            "I really don’t want to play on the slide",
            "I don’t want to play on the slide any more",
            "I really don’t want to play on the slide any longer"

    }, {"I like to play on the see-saw",
            "I really like to play on the see-saw",
            "I want to play on the see-saw",
            "I really want to play on the see-saw",
            "I want to play on the see-saw for some more time",
            "I really want to play on the see-saw for a bit longer",
            "I don’t like to play on the see-saw",
            "I really don’t like to play on the see-saw",
            "I don’t want to play on the see-saw",
            "I really don’t want to play on the see-saw",
            "I don’t want to play on the see-saw any more ",
            "I really don’t want to play on the see-saw any longer"

    }, {"I like to play on the merry-go-round",
            "I really like to play on the merry-go-round",
            "I want to play on the merry-go-round",
            "I really want to play on the merry-go-round",
            "I want to play on the merry-go-round for some more time",
            "I really want to play on the merry-go-round for a bit longer",
            "I don’t like to play on the merry-go-round",
            "I really don’t like to play on the merry-go-round",
            "I don’t want to play on the merry-go-round",
            "I really don’t want to play on the merry-go-round",
            "I don’t want to play on the merry-go-round any more ",
            "I really don’t want to play on the merry-go-round any longer"

    }, {"I like to play hide and seek",
            "I really like to play hide and seek",
            "I want to play hide and seek",
            "I really want to play hide and seek",
            "I want to play hide and seek again",
            "I really want to play hide and seek once again",
            "I don’t like to play hide and seek",
            "I really don’t like to play hide and seek",
            "I don’t want to play hide and seek",
            "I really don’t want to play hide and seek",
            "I don’t want to play hide and seek again",
            "I really don’t want to play hide and seek once again"

    }, {"I like to play bat and ball",
            "I really like to play bat and ball",
            "I want to play bat and ball",
            "I really want to play bat and ball",
            "I want to play bat and ball again",
            "I really want to play bat and ball once again",
            "I don’t like to play bat and ball",
            "I really don’t like to play bat and ball",
            "I don’t want to play bat and ball",
            "I really don’t want to play bat and ball",
            "I don’t want to play bat and ball again",
            "I really don’t want to play bat and ball once again"

    }, {"I like to play statue",
            "I really like to play statue",
            "I want to play statue",
            "I really want to play statue",
            "I want to play statue again",
            "I really want to play statue once again",
            "I don’t like to play statue",
            "I really don’t like to play statue",
            "I don’t want to play statue",
            "I really don’t want to play statue",
            "I don’t want to play statue again",
            "I really don’t want to play statue once again"

    }, {"I like to play lock and key",
            "I really like to play lock and key",
            "I want to play lock and key",
            "I really want to play lock and key",
            "I want to play lock and key again",
            "I really want to play lock and key once again",
            "I don’t like to play lock and key",
            "I really don’t like to play lock and key",
            "I don’t want to play lock and key",
            "I really don’t want to play lock and key",
            "I don’t want to play lock and key again",
            "I really don’t want to play lock and key once again"

    }, {"I like to play catch-catch",
            "I really like to play catch-catch",
            "I want to play catch-catch",
            "I really want to play catch-catch",
            "I want to play catch-catch again",
            "I really want to play catch-catch once again",
            "I don’t like to play catch-catch",
            "I really don’t like to play catch-catch",
            "I don’t want to play catch-catch",
            "I really don’t want to play catch-catch",
            "I don’t want to play catch-catch again",
            "I really don’t want to play catch-catch once again"

    }, {"I like to fly kites",
            "I really like to fly kites",
            "I want to fly a kite",
            "I really want to fly a kite",
            "I want to fly a kite again",
            "I really want to fly a kite once again",
            "I don’t like to fly kites",
            "I really don’t like to fly kites",
            "I don’t want to fly a kite",
            "I really don’t want to fly a kite",
            "I don’t want to fly a kite again",
            "I really don’t want to fly a kite once again"

    }, {"I like to play chor-police",
            "I really like to play chor-police",
            "I want to play chor-police",
            "I really want to play chor-police",
            "I want to play chor-police again",
            "I really want to play chor-police once again",
            "I don’t like to play chor-police",
            "I really don’t like to play chor-police",
            "I don’t want to play chor-police",
            "I really don’t want to play chor-police",
            "I don’t want to play chor-police again",
            "I really don’t want to play chor-police once again"

    }, {"I like to play with marbles",
            "I really like to play with marbles",
            "I want to play with marbles",
            "I really want to play with marbles",
            "I want to play with marbles again",
            "I really want to play with marbles once again",
            "I don’t like to play with marbles",
            "I really don’t like to play with marbles",
            "I don’t want to play with marbles",
            "I really don’t want to play with marbles",
            "I don’t want to play with marbles again",
            "I really don’t want to play with marbles once again"

    }, {"I like to walk",
            "I really like to walk",
            "I want to walk",
            "I really want to walk",
            "I want to walk for some more time",
            "I really want to walk for a bit longer",
            "I don’t like to walk",
            "I really don’t like to walk",
            "I don’t want to walk",
            "I really don’t want to walk",
            "I don’t want to walk more",
            "I really don’t want to walk any more"

    }, {"I like to cycle",
            "I really like to cycle",
            "I want to cycle",
            "I really want to cycle",
            "I want to cycle for some more time",
            "I really want to cycle for a bit longer",
            "I don’t like to cycle",
            "I really don’t like to cycle",
            "I don’t want to cycle",
            "I really don’t want to cycle",
            "I don’t want to cycle more",
            "I really don’t want to cycle any more"

    }, {"I like to run",
            "I really like to run",
            "I want to run",
            "I really want to run",
            "I want to run for some more time",
            "I really want to run for a bit longer",
            "I don’t like to run",
            "I really don’t like to run",
            "I don’t want to run",
            "I really don’t want to run",
            "I don’t want to run more",
            "I really don’t want to run any more"

    }, {"I like to swim",
            "I really like to swim",
            "I want to go for a swim",
            "I really want to go for a swim",
            "I want to swim for some more time",
            "I really want to swim for a bit longer",
            "I don’t like to swim",
            "I really don’t like to swim",
            "I don’t want to go for a swim",
            "I really don’t want to go for a swim",
            "I don’t want to swim any more",
            "I really don’t want to swim any longer"

    }}, {{"I like to play cricket",
            "I really like to play cricket",
            "I want to play cricket",
            "I really want to play cricket",
            "I want to play cricket for some more time",
            "I really want to play cricket for a bit longer",
            "I don’t like to play cricket",
            "I really don’t like to play cricket",
            "I don’t want to play cricket",
            "I really don’t want to play cricket",
            "I don’t want to play cricket any more",
            "I really don’t want to play cricket any longer"

    }, {"I like to play badminton",
            "I really like to play badminton",
            "I want to play badminton",
            "I really want to play badminton",
            "I want to play badminton for some more time",
            "I really want to play badminton for a bit longer",
            "I don’t like to play badminton",
            "I really don’t like to play badminton",
            "I don’t want to play badminton",
            "I really don’t want to play badminton",
            "I don’t want to play badminton any more",
            "I really don’t want to play badminton any longer"

    }, {"I like to play tennis",
            "I really like to play tennis",
            "I want to play tennis",
            "I really want to play tennis",
            "I want to play tennis for some more time",
            "I really want to play tennis for a bit longer",
            "I don’t like to play tennis",
            "I really don’t like to play tennis",
            "I don’t want to play tennis",
            "I really don’t want to play tennis",
            "I don’t want to play tennis any more",
            "I really don’t want to play tennis any longer"

    }, {"I like to play basketball",
            "I really like to play basketball",
            "I want to play basketball",
            "I really want to play basketball",
            "I want to play basketball for some more time",
            "I really want to play basketball for a bit longer",
            "I don’t like to play basketball",
            "I really don’t like to play basketball",
            "I don’t want to play basketball",
            "I really don’t want to play basketball",
            "I don’t want to play basketball any more",
            "I really don’t want to play basketball any longer"

    }, {"I like to play dodgeball",
            "I really like to play dodgeball",
            "I want to play dodgeball",
            "I really want to play dodgeball",
            "I want to play dodgeball for some more time",
            "I really want to play dodgeball for a bit longer",
            "I don’t like to play dodgeball",
            "I really don’t like to play dodgeball",
            "I don’t want to play dodgeball",
            "I really don’t want to play dodgeball",
            "I don’t want to play dodgeball any more",
            "I really don’t want to play dodgeball any longer"

    }, {"I like to play volleyball",
            "I really like to play volleyball",
            "I want to play volleyball",
            "I really want to play volleyball",
            "I want to play volleyball for some more time",
            "I really want to play volleyball for a bit longer",
            "I don’t like to play volleyball",
            "I really don’t like to play volleyball",
            "I don’t want to play volleyball",
            "I really don’t want to play volleyball",
            "I don’t want to play volleyball any more",
            "I really don’t want to play volleyball any longer"

    }, {"I like to play kho-kho",
            "I really like to play kho-kho",
            "I want to play kho-kho",
            "I really want to play kho-kho",
            "I want to play kho-kho for some more time",
            "I really want to play kho-kho for a bit longer",
            "I don’t like to play kho-kho",
            "I really don’t like to play kho-kho",
            "I don’t want to play kho-kho",
            "I really don’t want to play kho-kho",
            "I don’t want to play kho-kho any more",
            "I really don’t want to play kho-kho any longer"

    }, {"I like to play football",
            "I really like to play football",
            "I want to play football",
            "I really want to play football",
            "I want to play football for some more time",
            "I really want to play football for a bit longer",
            "I don’t like to play football",
            "I really don’t like to play football",
            "I don’t want to play football",
            "I really don’t want to play football",
            "I don’t want to play football any more",
            "I really don’t want to play football any longer"

    },  {"I like to play kabbbaddi",
            "I really like to play kabbbaddi",
            "I want to play kabbbaddi",
            "I really want to play kabbbaddi",
            "I want to play kabbbaddi for some more time",
            "I really want to play kabbbaddi for a bit longer",
            "I don’t like to play kabbbaddi",
            "I really don’t like to play kabbbaddi",
            "I don’t want to play kabbbaddi",
            "I really don’t want to play kabbbaddi",
            "I don’t want to play kabbbaddi any more",
            "I really don’t want to play kabbbaddi any longer"

    }, {"I like to do gym nastics",
            "I really like to do gym nastics",
            "I want to do gym nastics",
            "I really want to do gym nastics",
            "I want to do gym nastics for some more time",
            "I really want to do gym nastics for a bit longer",
            "I don’t like to do gym nastics",
            "I really don’t like to do gym nastics",
            "I don’t want to do gym nastics",
            "I really don’t want to do gym nastics",
            "I don’t want to do gym nastics any more",
            "I really don’t want to do gym nastics any longer"

    }, {"I like to swim",
            "I really like to swim",
            "I want to go for a swim",
            "I really want to go for a swim",
            "I want to swim for some more time",
            "I really want to swim for a bit longer",
            "I don’t like to go for a swim",
            "I really don’t like to go for a swim",
            "I don’t want to go for a swim",
            "I really don’t want to go for a swim",
            "I don’t want to swim any more",
            "I really don’t want to swim any longer"

    }}, {{"I like the next channel",
            "I really like the next channel",
            "I want to watch the next channel",
            "I really want to watch the next channel",
            "I want to watch the next channel again",
            "I really want to watch the next channel again",
            "I don’t like the next channel",
            "I really don’t like the next channel",
            "I don’t want to watch the next channel",
            "I really don’t want to watch the next channel",
            "I don’t want to watch the next channel again",
            "I really don’t want to watch the next channel again"

    }, {"I like the previous channel",
            "I really like the previous channel",
            "I want to watch the previous channel",
            "I really want to watch the previous channel",
            "I want to watch the previous channel again",
            "I really want to watch the previous channel again",
            "I don’t like the previous channel",
            "I really don’t like the previous channel",
            "I don’t want to watch the previous channel",
            "I really don’t want to watch the previous channel",
            "I don’t want to watch the previous channel again",
            "I really don’t want to watch the previous channel again"

    }, {"I like the volume to be turned up",
            "I really like the volume to be turned up",
            "I want the volume to be turned up",
            "I really want the volume to be turned up",
            "I want the volume to be turned up more",
            "I really want the volume to be turned up some more",
            "I don’t like the volume to be turned up",
            "I really don’t like the volume to be turned up",
            "I don’t want the volume to be turned up",
            "I really don’t want the volume to be turned up",
            "I don’t want the volume to be turned up more",
            "I really don’t want the volume to be turned up any more"

    }, {"I like the volume to be turned down",
            "I really like the volume to be turned down",
            "I want the volume to be turned down",
            "I really want the volume to be turned down",
            "I want the volume to be turned down more",
            "I really want the volume to be turned down some more",
            "I don’t like the volume to be turned down",
            "I really don’t like the volume to be turned down",
            "I don’t want the volume to be turned down",
            "I really don’t want the volume to be turned down",
            "I don’t want the volume to be turned down more",
            "I really don’t want the volume to be turned down any more"

    }}, {{"I like to change the music",
            "I really like to change the music",
            "I want to change the music",
            "I really want to change the music",
            "I want to change the music again",
            "I really want to change the music again",
            "I don’t like to change the music",
            "I really don’t like to change the music",
            "I don’t want to change the music",
            "I really don’t want to change the music",
            "I don’t want to change the music again",
            "I really don’t want to change the music again"

    }, {"I like to dance",
            "I really like to dance",
            "I want to dance",
            "I really want to dance",
            "I want to dance more",
            "I really want to dance some more",
            "I don’t like to dance",
            "I really don’t like to dance",
            "I don’t want to dance",
            "I really don’t want to dance",
            "I don’t want to dance more",
            "I really don’t want to dance any more"

    }, {"I like the volume to be turned up",
            "I really like the volume to be turned up",
            "I want the volume to be turned up",
            "I really want the volume to be turned up",
            "I want the volume to be turned up more",
            "I really want the volume to be turned up some more",
            "I don’t like the volume to be turned up",
            "I really don’t like the volume to be turned up",
            "I don’t want the volume to be turned up",
            "I really don’t want the volume to be turned up",
            "I don’t want the volume to be turned up more",
            "I really don’t want the volume to be turned up any more"

    }, {"I like the volume to be turned down",
            "I really like the volume to be turned down",
            "I want the volume to be turned down",
            "I really want the volume to be turned down",
            "I want the volume to be turned down more",
            "I really want the volume to be turned down some more",
            "I don’t like the volume to be turned down",
            "I really don’t like the volume to be turned down",
            "I don’t want the volume to be turned down",
            "I really don’t want the volume to be turned down",
            "I don’t want the volume to be turned down more",
            "I really don’t want the volume to be turned down any more"

    }}, {{"I like to draw",
            "I really like to draw",
            "I want to draw",
            "I really want to draw",
            "I want to draw some more",
            "I really want to draw once again",
            "I don’t like to draw",
            "I really don’t like to draw",
            "I don’t want to draw",
            "I really don’t want to draw",
            "I don’t want to draw more",
            "I really don’t want to draw any more"

    }, {"I like to paint",
            "I really like to paint",
            "I want to paint",
            "I really want to paint",
            "I want to paint some more",
            "I really want to paint once again",
            "I don’t like to paint",
            "I really don’t like to paint",
            "I don’t want to paint",
            "I really don’t want to paint",
            "I don’t want to paint more",
            "I really don’t want to paint any more"

    }, {"I like to read",
            "I really like to read",
            "I want to read",
            "I really want to read",
            "I want to read some more",
            "I really want to read for a bit longer",
            "I don’t like to read",
            "I really don’t like to read",
            "I don’t want to read",
            "I really don’t want to read",
            "I don’t want to read more",
            "I really don’t want to read any more"

    }, {"I like to write",
            "I really like to write",
            "I want to write",
            "I really want to write",
            "I want to write some more",
            "I really want to write for a bit longer",
            "I don’t like to write",
            "I really don’t like to write",
            "I don’t want to write",
            "I really don’t want to write",
            "I don’t want to write more",
            "I really don’t want to write any more"

    }, {"I like to do art and craft activities",
            "I really like to do art and craft activities",
            "I want to do art and craft activities",
            "I really want to do art and craft activities",
            "I want to do more art and craft activities",
            "I really want to do art and craft activities some more",
            "I don’t like to do art and craft activities",
            "I really don’t like to do art and craft activities",
            "I don’t want to do art and craft activities",
            "I really don’t want to do art and craft activities",
            "I don’t want to do more art and craft activities",
            "I really don’t want to do art and craft activities any more"

    }, {"I like to paarticipate in drama",
            "I really like to paarticipate in drama",
            "I want to paarticipate in drama",
            "I really want to paarticipate in drama",
            "I want to paarticipate in drama again",
            "I really want to paarticipate in drama once again",
            "I don’t like to paarticipate in drama",
            "I really don’t like to paarticipate in drama",
            "I don’t want to paarticipate in drama",
            "I really don’t want to paarticipate in drama",
            "I don’t want to paarticipate in drama again",
            "I really don’t want to paarticipate in drama again"

    }, {"I like to dance",
            "I really like to dance",
            "I want to dance",
            "I really want to dance",
            "I want to dance some more",
            "I really want to dance some more",
            "I don’t like to dance",
            "I really don’t like to dance",
            "I don’t want to dance",
            "I really don’t want to dance",
            "I don’t want to dance more",
            "I really don’t want to dance any more"

    }, {"I like to make music",
            "I really like to make music",
            "I want to make music",
            "I really want to make music",
            "I want to make some more music",
            "I really want to make music for a bit longer",
            "I don’t like to make music",
            "I really don’t like to make music",
            "I don’t want to make music",
            "I really don’t want to make music",
            "I don’t want to make more music",
            "I really don’t want to make music any more"

    }}}, {{{"I like dogs",
            "I really like dogs",
            "I want to learn about dogs",
            "I really want to learn about dogs",
            "I want to learn more about dogs",
            "I really want to learn some more about dogs",
            "I don’t like dogs",
            "I really don’t like dogs",
            "I don’t want to learn about dogs",
            "I really don’t want to learn about dogs",
            "I don’t want to learn more about dogs",
            "I really don’t want to learn any more about dogs",

    }, {"I like cats",
            "I really like cats",
            "I want to learn about cats",
            "I really want to learn about cats",
            "I want to learn more about cats",
            "I really want to learn some more about cats",
            "I don’t like cats",
            "I really don’t like cats",
            "I don’t want to learn about cats",
            "I really don’t want to learn about cats",
            "I don’t want to learn more about cats",
            "I really don’t want to learn any more about cats"

    }, {"I like elephants",
            "I really like elephants",
            "I want to learn about elephants",
            "I really want to learn about elephants",
            "I want to learn more about elephants",
            "I really want to learn some more about elephants",
            "I don’t like elephants",
            "I really don’t like elephants",
            "I don’t want to learn about elephants",
            "I really don’t want to learn about elephants",
            "I don’t want to learn more about elephants",
            "I really don’t want to learn any more about elephants"

    }, {"I like lions",
            "I really like lions",
            "I want to learn about lions",
            "I really want to learn about lions",
            "I want to learn more about lions",
            "I really want to learn some more about lions",
            "I don’t like lions",
            "I really don’t like lions",
            "I don’t want to learn about lions",
            "I really don’t want to learn about lions",
            "I don’t want to learn more about lions",
            "I really don’t want to learn any more about lions"

    }, {"I like parrots",
            "I really like parrots",
            "I want to learn about parrots",
            "I really want to learn about parrots",
            "I want to learn more about parrots",
            "I really want to learn some more about parrots",
            "I don’t like parrots",
            "I really don’t like parrots",
            "I don’t want to learn about parrots",
            "I really don’t want to learn about parrots",
            "I don’t want to learn more about parrots",
            "I really don’t want to learn any more about parrots"

    }, {"I like rabbits",
            "I really like rabbits",
            "I want to learn about rabbits",
            "I really want to learn about rabbits",
            "I want to learn more about rabbits",
            "I really want to learn some more about rabbits",
            "I don’t like rabbits",
            "I really don’t like rabbits",
            "I don’t want to learn about rabbits",
            "I really don’t want to learn about rabbits",
            "I don’t want to learn more about rabbits",
            "I really don’t want to learn any more about rabbits"

    }, {
            "I like cows",
            "I really like cows",
            "I want to learn about cows",
            "I really want to learn about cows",
            "I want to learn more about cows",
            "I really want to learn some more about cows",
            "I don’t like cows",
            "I really don’t like cows",
            "I don’t want to learn about cows",
            "I really don’t want to learn about cows",
            "I don’t want to learn more about cows",
            "I really don’t want to learn any more about cows"

    }, {"I like ducks",
            "I really like ducks",
            "I want to learn about ducks",
            "I really want to learn about ducks",
            "I want to learn more about ducks",
            "I really want to learn some more about ducks",
            "I don’t like ducks",
            "I really don’t like ducks",
            "I don’t want to learn about ducks",
            "I really don’t want to learn about ducks",
            "I don’t want to learn more about ducks",
            "I really don’t want to learn any more about ducks"

    },  {"I like donkeys",
            "I really like donkeys",
            "I want to learn about donkeys",
            "I really want to learn about donkeys",
            "I want to learn more about donkeys",
            "I really want to learn some more about donkeys",
            "I don’t like donkeys",
            "I really don’t like donkeys",
            "I don’t want to learn about donkeys",
            "I really don’t want to learn about donkeys",
            "I don’t want to learn more about donkeys",
            "I really don’t want to learn any more about donkeys"

    }, {
            "I like ants",
            "I really like ants",
            "I want to learn about ants",
            "I really want to learn about ants",
            "I want to learn more about ants",
            "I really want to learn some more about ants",
            "I don’t like ants",
            "I really don’t like ants",
            "I don’t want to learn about ants",
            "I really don’t want to learn about ants",
            "I don’t want to learn more about ants",
            "I really don’t want to learn any more about ants"

    }, {"I like tigers",
            "I really like tigers",
            "I want to learn about tigers",
            "I really want to learn about tigers",
            "I want to learn more about tigers",
            "I really want to learn some more about tigers",
            "I don’t like tigers",
            "I really don’t like tigers",
            "I don’t want to learn about tigers",
            "I really don’t want to learn about tigers",
            "I don’t want to learn more about tigers",
            "I really don’t want to learn any more about tigers"

    }, {"I like monkeys",
            "I really like monkeys",
            "I want to learn about monkeys",
            "I really want to learn about monkeys",
            "I want to learn more about monkeys",
            "I really want to learn some more about monkeys",
            "I don’t like monkeys",
            "I really don’t like monkeys",
            "I don’t want to learn about monkeys",
            "I really don’t want to learn about monkeys",
            "I don’t want to learn more about monkeys",
            "I really don’t want to learn any more about monkeys"

    }, {"I like pigeons",
            "I really like pigeons",
            "I want to learn about pigeons",
            "I really want to learn about pigeons",
            "I want to learn more about pigeons",
            "I really want to learn some more about pigeons",
            "I don’t like pigeons",
            "I really don’t like pigeons",
            "I don’t want to learn about pigeons",
            "I really don’t want to learn about pigeons",
            "I don’t want to learn more about pigeons",
            "I really don’t want to learn any more about pigeons"

    }, {
            "I like cockroaches",
            "I really like cockroaches",
            "I want to learn about cockroaches",
            "I really want to learn about cockroaches",
            "I want to learn more about cockroaches",
            "I really want to learn some more about cockroaches",
            "I don’t like cockroaches",
            "I really don’t like cockroaches",
            "I don’t want to learn about cockroaches",
            "I really don’t want to learn about cockroaches",
            "I don’t want to learn more about cockroaches",
            "I really don’t want to learn any more about cockroaches"


    }, {"I like crows",
            "I really like crows",
            "I want to learn about crows",
            "I really want to learn about crows",
            "I want to learn more about crows",
            "I really want to learn some more about crows",
            "I don’t like crows",
            "I really don’t like crows",
            "I don’t want to learn about crows",
            "I really don’t want to learn about crows",
            "I don’t want to learn more about crows",
            "I really don’t want to learn any more about crows"

    }, {"I like horses",
            "I really like horses",
            "I want to learn about horses",
            "I really want to learn about horses",
            "I want to learn more about horses",
            "I really want to learn some more about horses",
            "I don’t like horses",
            "I really don’t like horses",
            "I don’t want to learn about horses",
            "I really don’t want to learn about horses",
            "I don’t want to learn more about horses",
            "I really don’t want to learn any more about horses"

    },  {"I like deer",
            "I really like deer",
            "I want to learn about deer",
            "I really want to learn about deer",
            "I want to learn more about deer",
            "I really want to learn some more about deer",
            "I don’t like deer",
            "I really don’t like deer",
            "I don’t want to learn about deer",
            "I really don’t want to learn about deer",
            "I don’t want to learn more about deer",
            "I really don’t want to learn any more about deer"

    }, {"I like owls",
            "I really like owls",
            "I want to learn about owls",
            "I really want to learn about owls",
            "I want to learn more about owls",
            "I really want to learn some more about owls",
            "I don’t like owls",
            "I really don’t like owls",
            "I don’t want to learn about owls",
            "I really don’t want to learn about owls",
            "I don’t want to learn more about owls",
            "I really don’t want to learn any more about owls"

    }, {"I like wolves",
            "I really like wolves",
            "I want to learn about wolves",
            "I really want to learn about wolves",
            "I want to learn more about wolves",
            "I really want to learn some more about wolves",
            "I don’t like wolves",
            "I really don’t like wolves",
            "I don’t want to learn about wolves",
            "I really don’t want to learn about wolves",
            "I don’t want to learn more about wolves",
            "I really don’t want to learn any more about wolves"

    }, {"I like foxes",
            "I really like foxes",
            "I want to learn about foxes",
            "I really want to learn about foxes",
            "I want to learn more about foxes",
            "I really want to learn some more about foxes",
            "I don’t like foxes",
            "I really don’t like foxes",
            "I don’t want to learn about foxes",
            "I really don’t want to learn about foxes",
            "I don’t want to learn more about foxes",
            "I really don’t want to learn any more about foxes"

    }, {"I like bears",
            "I really like bears",
            "I want to learn about bears",
            "I really want to learn about bears",
            "I want to learn more about bears",
            "I really want to learn some more about bears",
            "I don’t like bears",
            "I really don’t like bears",
            "I don’t want to learn about bears",
            "I really don’t want to learn about bears",
            "I don’t want to learn more about bears",
            "I really don’t want to learn any more about bears"

    }, {"I like sheep",
            "I really like sheep",
            "I want to learn about sheep",
            "I really want to learn about sheep",
            "I want to learn more about sheep",
            "I really want to learn some more about sheep",
            "I don’t like sheep",
            "I really don’t like sheep",
            "I don’t want to learn about sheep",
            "I really don’t want to learn about sheep",
            "I don’t want to learn more about sheep",
            "I really don’t want to learn any more about sheep"

    }, {"I like goats",
            "I really like goats",
            "I want to learn about goats",
            "I really want to learn about goats",
            "I want to learn more about goats",
            "I really want to learn some more about goats",
            "I don’t like goats",
            "I really don’t like goats",
            "I don’t want to learn about goats",
            "I really don’t want to learn about goats",
            "I don’t want to learn more about goats",
            "I really don’t want to learn any more about goats"

    }, {"I like pigs",
            "I really like pigs",
            "I want to learn about pigs",
            "I really want to learn about pigs",
            "I want to learn more about pigs",
            "I really want to learn some more about pigs",
            "I don’t like pigs",
            "I really don’t like pigs",
            "I don’t want to learn about pigs",
            "I really don’t want to learn about pigs",
            "I don’t want to learn more about pigs",
            "I really don’t want to learn any more about pigs"

    },  {
            "I like flies",
            "I really like flies",
            "I want to learn about flies",
            "I really want to learn about flies",
            "I want to learn more about flies",
            "I really want to learn some more about flies",
            "I don’t like flies",
            "I really don’t like flies",
            "I don’t want to learn about flies",
            "I really don’t want to learn about flies",
            "I don’t want to learn more about flies",
            "I really don’t want to learn any more about flies"


    }, {"I like giraffes",
            "I really like giraffes",
            "I want to learn about giraffes",
            "I really want to learn about giraffes",
            "I want to learn more about giraffes",
            "I really want to learn some more about giraffes",
            "I don’t like giraffes",
            "I really don’t like giraffes",
            "I don’t want to learn about giraffes",
            "I really don’t want to learn about giraffes",
            "I don’t want to learn more about giraffes",
            "I really don’t want to learn any more about giraffes"

    }, {"I like zebras",
            "I really like zebras",
            "I want to learn about zebras",
            "I really want to learn about zebras",
            "I want to learn more about zebras",
            "I really want to learn some more about zebras",
            "I don’t like zebras",
            "I really don’t like zebras",
            "I don’t want to learn about zebras",
            "I really don’t want to learn about zebras",
            "I don’t want to learn more about zebras",
            "I really don’t want to learn any more about zebras"

    }, {
            "I like mosquitoes",
            "I really like mosquitoes",
            "I want to learn about mosquitoes",
            "I really want to learn about mosquitoes",
            "I want to learn more about mosquitoes",
            "I really want to learn some more about mosquitoes",
            "I don’t like mosquitoes",
            "I really don’t like mosquitoes",
            "I don’t want to learn about mosquitoes",
            "I really don’t want to learn about mosquitoes",
            "I don’t want to learn more about mosquitoes",
            "I really don’t want to learn any more about mosquitoes"


    }, {"I like buffaloes",
            "I really like buffaloes",
            "I want to learn about buffaloes",
            "I really want to learn about buffaloes",
            "I want to learn more about buffaloes",
            "I really want to learn some more about buffaloes",
            "I don’t like buffaloes",
            "I really don’t like buffaloes",
            "I don’t want to learn about buffaloes",
            "I really don’t want to learn about buffaloes",
            "I don’t want to learn more about buffaloes",
            "I really don’t want to learn any more about buffaloes"

    }, {"I like mice",
            "I really like mice",
            "I want to learn about mice",
            "I really want to learn about mice",
            "I want to learn more about mice",
            "I really want to learn some more about mice",
            "I don’t like mice",
            "I really don’t like mice",
            "I don’t want to learn about mice",
            "I really don’t want to learn about mice",
            "I don’t want to learn more about mice",
            "I really don’t want to learn any more about mice"

    }, {"I like snakes",
            "I really like snakes",
            "I want to learn about snakes",
            "I really want to learn about snakes",
            "I want to learn more about snakes",
            "I really want to learn some more about snakes",
            "I don’t like snakes",
            "I really don’t like snakes",
            "I don’t want to learn about snakes",
            "I really don’t want to learn about snakes",
            "I don’t want to learn more about snakes",
            "I really don’t want to learn any more about snakes"

    }, {"I like crocodiles",
            "I really like crocodiles",
            "I want to learn about crocodiles",
            "I really want to learn about crocodiles",
            "I want to learn more about crocodiles",
            "I really want to learn some more about crocodiles",
            "I don’t like crocodiles",
            "I really don’t like crocodiles",
            "I don’t want to learn about crocodiles",
            "I really don’t want to learn about crocodiles",
            "I don’t want to learn more about crocodiles",
            "I really don’t want to learn any more about crocodiles"

    },  {"I like bees",
            "I really like bees",
            "I want to learn about bees",
            "I really want to learn about bees",
            "I want to learn more about bees",
            "I really want to learn some more about bees",
            "I don’t like bees",
            "I really don’t like bees",
            "I don’t want to learn about bees",
            "I really don’t want to learn about bees",
            "I don’t want to learn more about bees",
            "I really don’t want to learn any more about bees"

    }, {"I like hippos",
            "I really like hippos",
            "I want to learn about hippos",
            "I really want to learn about hippos",
            "I want to learn more about hippos",
            "I really want to learn some more about hippos",
            "I don’t like hippos",
            "I really don’t like hippos",
            "I don’t want to learn about hippos",
            "I really don’t want to learn about hippos",
            "I don’t want to learn more about hippos",
            "I really don’t want to learn any more about hippos"

    }, {"I like rhinos",
            "I really like rhinos",
            "I want to learn about rhinos",
            "I really want to learn about rhinos",
            "I want to learn more about rhinos",
            "I really want to learn some more about rhinos",
            "I don’t like rhinos",
            "I really don’t like rhinos",
            "I don’t want to learn about rhinos",
            "I really don’t want to learn about rhinos",
            "I don’t want to learn more about rhinos",
            "I really don’t want to learn any more about rhinos"

    }, {"I like fishes",
            "I really like fishes",
            "I want to learn about fishes",
            "I really want to learn about fishes",
            "I want to learn more about fishes",
            "I really want to learn some more about fishes",
            "I don’t like fishes",
            "I really don’t like fishes",
            "I don’t want to learn about fishes",
            "I really don’t want to learn about fishes",
            "I don’t want to learn more about fishes",
            "I really don’t want to learn any more about fishes"

    }, {"I like penguins",
            "I really like penguins",
            "I want to learn about penguins",
            "I really want to learn about penguins",
            "I want to learn more about penguins",
            "I really want to learn some more about penguins",
            "I don’t like penguins",
            "I really don’t like penguins",
            "I don’t want to learn about penguins",
            "I really don’t want to learn about penguins",
            "I don’t want to learn more about penguins",
            "I really don’t want to learn any more about penguins"

    }, {"I like seals",
            "I really like seals",
            "I want to learn about seals",
            "I really want to learn about seals",
            "I want to learn more about seals",
            "I really want to learn some more about seals",
            "I don’t like seals",
            "I really don’t like seals",
            "I don’t want to learn about seals",
            "I really don’t want to learn about seals",
            "I don’t want to learn more about seals",
            "I really don’t want to learn any more about seals"

    }, {"I like dolphins",
            "I really like dolphins",
            "I want to learn about dolphins",
            "I really want to learn about dolphins",
            "I want to learn more about dolphins",
            "I really want to learn some more about dolphins",
            "I don’t like dolphins",
            "I really don’t like dolphins",
            "I don’t want to learn about dolphins",
            "I really don’t want to learn about dolphins",
            "I don’t want to learn more about dolphins",
            "I really don’t want to learn any more about dolphins"

    }, {"I like whales",
            "I really like whales",
            "I want to learn about whales",
            "I really want to learn about whales",
            "I want to learn more about whales",
            "I really want to learn some more about whales",
            "I don’t like whales",
            "I really don’t like whales",
            "I don’t want to learn about whales",
            "I really don’t want to learn about whales",
            "I don’t want to learn more about whales",
            "I really don’t want to learn any more about whales"

    },  {"I like sharks",
            "I really like sharks",
            "I want to learn about sharks",
            "I really want to learn about sharks",
            "I want to learn more about sharks",
            "I really want to learn some more about sharks",
            "I don’t like sharks",
            "I really don’t like sharks",
            "I don’t want to learn about sharks",
            "I really don’t want to learn about sharks",
            "I don’t want to learn more about sharks",
            "I really don’t want to learn any more about sharks"

    }, {"I like tortoyses",
            "I really like tortoyses",
            "I want to learn about tortoyses",
            "I really want to learn about tortoyses",
            "I want to learn more about tortoyses",
            "I really want to learn some more about tortoyses",
            "I don’t like tortoyses",
            "I really don’t like tortoyses",
            "I don’t want to learn about tortoyses",
            "I really don’t want to learn about tortoyses",
            "I don’t want to learn more about tortoyses",
            "I really don’t want to learn any more about tortoyses"

    }, {"I like sparrows",
            "I really like sparrows",
            "I want to learn about sparrows",
            "I really want to learn about sparrows",
            "I want to learn more about sparrows",
            "I really want to learn some more about sparrows",
            "I don’t like sparrows",
            "I really don’t like sparrows",
            "I don’t want to learn about sparrows",
            "I really don’t want to learn about sparrows",
            "I don’t want to learn more about sparrows",
            "I really don’t want to learn any more about sparrows"

    }, {"I like eagles",
            "I really like eagles",
            "I want to learn about eagles",
            "I really want to learn about eagles",
            "I want to learn more about eagles",
            "I really want to learn some more about eagles",
            "I don’t like eagles",
            "I really don’t like eagles",
            "I don’t want to learn about eagles",
            "I really don’t want to learn about eagles",
            "I don’t want to learn more about eagles",
            "I really don’t want to learn any more about eagles"

    }, {"I like hawks",
            "I really like hawks",
            "I want to learn about hawks",
            "I really want to learn about hawks",
            "I want to learn more about hawks",
            "I really want to learn some more about hawks",
            "I don’t like hawks",
            "I really don’t like hawks",
            "I don’t want to learn about hawks",
            "I really don’t want to learn about hawks",
            "I don’t want to learn more about hawks",
            "I really don’t want to learn any more about hawks"

    }, {"I like vultures",
            "I really like vultures",
            "I want to learn about vultures",
            "I really want to learn about vultures",
            "I want to learn more about vultures",
            "I really want to learn some more about vultures",
            "I don’t like vultures",
            "I really don’t like vultures",
            "I don’t want to learn about vultures",
            "I really don’t want to learn about vultures",
            "I don’t want to learn more about vultures",
            "I really don’t want to learn any more about vultures"

    }}, {{"My head feels fine and I can think well",
            "My head really feels fine",
            "I want to think",
            "I really want to think",
            "I want to think more",
            "I really want to think some more",
            "My head hurts and I can’t think well",
            "My head really hurts a lot",
            "I don’t want to think",
            "I really don’t want to think",
            "I don’t want to think more",
            "I really don’t want to think any more"

    }, {"My हैर looks good",
            "My हैर really looks good",
            "I want to touch my हैर",
            "I really want to touch my हैर",
            "I want to touch my हैर again",
            "I really want to touch my हैर again",
            "My हैर doesn’t look good",
            "My हैर really doesn’t look good",
            "I don’t want to touch my हैर",
            "I really don’t want to touch my हैर",
            "I want to touch my हैर again",
            "I really want to touch my हैर again"

    }, {"My eyes feel fine and I can see well",
            "My eyes really feel fine",
            "I want to see",
            "I really want to see",
            "I want to see better",
            "I really want to see much better",
            "My eyes hurt and I can’t see well",
            "My eyes really hurt a lot",
            "I don’t want to see",
            "I really don’t want to see",
            "I don’t want to see more",
            "I really don’t want to see any more"

    }, {"My nose feels fine and I can smell well",
            "My nose really feels fine",
            "I want to smell",
            "I really want to smell",
            "I want to smell that again",
            "I really want to smell that again",
            "My nose hurts and I can’t smell well",
            "My nose really hurts a lot",
            "I don’t want to smell",
            "I really don’t want to smell",
            "I don’t want to smell that again",
            "I really don’t want to smell that any more"

    }, {"My ears feel fine and I can hear well",
            "My ears really feel fine",
            "I want to hear",
            "I really want to hear",
            "I want to hear better",
            "I really want to hear much better",
            "My ears hurt and I can’t hear well",
            "My ears really hurt a lot",
            "I don’t want to hear",
            "I really don’t want to hear",
            "I don’t want to hear more",
            "I really don’t want to hear any more"

    }, {"My mouth feels fine and I can speak well",
            "My mouth really feels fine",
            "I want to speak",
            "I really want to speak",
            "I want to speak more",
            "I really want to speak some more",
            "My mouth hurts and I can’t speak well",
            "My mouth really hurts a lot",
            "I don’t want to speak",
            "I really don’t want to speak",
            "I don’t want to speak more",
            "I really don’t want to speak any more"

    }, {"My tongue feels fine and I can taste well",
            "My tongue really feels fine",
            "I want to taste",
            "I really want to taste",
            "I want to taste more",
            "I really want to taste some more",
            "My tongue hurts and I can’t taste well",
            "My tongue really hurts a lot",
            "I don’t want to taste",
            "I really don’t want to taste",
            "I don’t want to taste more",
            "I really don’t want to taste any more"

    }, {"My neck feels fine and I can move it well",
            "My neck really feels fine",
            "I want to move my neck",
            "I really want to move my neck",
            "I want to move my neck some more",
            "I really want to move my neck some more",
            "My neck hurts and I can’t move it well",
            "My neck really hurts a lot",
            "I don’t want to move my neck",
            "I really don’t want to move my neck",
            "I don’t want to move my neck more",
            "I really don’t want to move my neck any more"

    },  {"My shoulder feels fine and I can move it well",
            "My shoulder really feels fine",
            "I want to move my shoulder",
            "I really want to move my shoulder",
            "I want to move my shoulder some more",
            "I really want to move my shoulder some more",
            "My shoulder hurts and I can’t move it well",
            "My shoulder really hurts a lot",
            "I don’t want to move my shoulder",
            "I really don’t want to move my shoulder",
            "I don’t want to move my shoulder more",
            "I really don’t want to move my shoulder any more"

    }, {"My elbow feels fine and I can move it well",
            "My elbow really feels fine",
            "I want to move my elbow",
            "I really want to move my elbow",
            "I want to move my elbow some more",
            "I really want to move my elbow some more",
            "My elbow hurts and I can’t move it well",
            "My elbow really hurts a lot",
            "I don’t want to move my elbow",
            "I really don’t want to move my elbow",
            "I don’t want to move my elbow more",
            "I really don’t want to move my elbow any more"

    }, {"My wrist feels fine and I can move it well",
            "My wrist really feels fine",
            "I want to move my wrist",
            "I really want to move my wrist",
            "I want to move my wrist some more",
            "I really want to move my wrist some more",
            "My wrist hurts and I can’t move it well",
            "My wrist really hurts a lot",
            "I don’t want to move my wrist",
            "I really don’t want to move my wrist",
            "I don’t want to move my wrist more",
            "I really don’t want to move my wrist any more"

    }, {"My hands feel fine and I can work",
            "My hands really feel fine",
            "I want to work",
            "I really want to work",
            "I want to work more",
            "I really want to work some more",
            "My hands hurt and I can’t work",
            "My hands really hurt a lot",
            "I don’t want to work",
            "I really don’t want to work",
            "I don’t want to work more",
            "I really don’t want to work any more"

    }, {"My fingers are fine and I can move them well",
            "My fingers really feel fine",
            "I want to move my fingers",
            "I really want to move my fingers",
            "I want to move my fingers again",
            "I really want to move my fingers again",
            "My fingers hurt and I can’t move them well",
            "My fingers really hurt a lot",
            "I don’t want to move my fingers",
            "I really don’t want to move my fingers",
            "I don’t want to move my fingers again",
            "I really don’t want to move my fingers anymore"

    }, {"My back feels fine and I can move it well",
            "My back really feels fine",
            "I want to move my back",
            "I really want to move my back",
            "I want to move my back some more",
            "I really want to move my back some more",
            "My back hurts and I can’t move it well",
            "My back really hurts a lot",
            "I don’t want to move my back",
            "I really don’t want to move my back",
            "I don’t want to move my back more",
            "I really don’t want to move my back any more",

    }, {"My stomach feels fine and I can eat well",
            "My stomach really feels fine",
            "I want to eat",
            "I really want to eat",
            "I want to eat more",
            "I really want to eat some more",
            "My stomach hurts and I can’t eat well",
            "My stomach really hurts a lot",
            "I don’t want to eat",
            "I really don’t want to eat",
            "I don’t want to eat more",
            "I really don’t want to eat any more"

    }, {"My hip feels fine and I can move it well",
            "My hip really feels fine",
            "I want to move my hip",
            "I really want to move my hip",
            "I want to move my hip some more",
            "I really want to move my hip some more",
            "My hip hurts and I can’t move it well",
            "My hip really hurts a lot",
            "I don’t want to move my hip",
            "I really don’t want to move my hip",
            "I don’t want to move my hip more",
            "I really don’t want to move my hip any more"

    },  {"My knee feels fine and I can move it well",
            "My knee really feels fine",
            "I want to move my knee",
            "I really want to move my knee",
            "I want to move my knee some more",
            "I really want to move my knee some more",
            "My knee hurts and I can’t move it well",
            "My knee really hurts a lot",
            "I don’t want to move my knee",
            "I really don’t want to move my knee",
            "I don’t want to move my knee more",
            "I really don’t want to move my knee any more"

    }, {"My ankle feels fine and I can move it well",
            "My ankle really feels fine",
            "I want to move my ankle",
            "I really want to move my ankle",
            "I want to move my ankle some more",
            "I really want to move my ankle some more",
            "My ankle hurts and I can’t move it well",
            "My ankle really hurts a lot",
            "I don’t want to move my ankle",
            "I really don’t want to move my ankle",
            "I don’t want to move my ankle more",
            "I really don’t want to move my ankle any more"

    }, {"My legs feel fine and I can walk well",
            "My legs really feel fine",
            "I want to walk",
            "I really want to walk",
            "I want to walk some more",
            "I really want to walk some more",
            "My legs hurt and I can’t walk well",
            "My legs really hurt a lot",
            "I don’t want to walk",
            "I really don’t want to walk",
            "I don’t want to walk more",
            "I really don’t want to walk any more",

    }, {"My toes feel fine and I can move them well",
            "My toes really feel fine",
            "I want to move my toes",
            "I really want to move my toes",
            "I want to move my toes some more",
            "I really want to move my toes some more",
            "My toes hurt and I can’t move them well",
            "My toes really hurt a lot",
            "I don’t want to move my toes",
            "I really don’t want to move my toes",
            "I don’t want to move my toes more",
            "I really don’t want to move my toes any more"

    }}, {{"I like to read bed time stories",
            "I really like to read bed time stories",
            "I want to read bed time stories",
            "I really want to read bed time stories",
            "I want to read some more bed time stories",
            "I really want to read some more bed time stories",
            "I don’t like to read bed time stories",
            "I really don’t like to read bed time stories",
            "I don’t want to read bed time stories",
            "I really don’t want to read bed time stories",
            "I don’t want to read more bed time stories",
            "I really don’t want to read any more bed time stories"

    }, {"I like to read comic books",
            "I really like to read comic books",
            "I want to read a comic book",
            "I really want to read a comic book",
            "I want to read some more comic books",
            "I really want to read some more comic books",
            "I don’t like to read comic books",
            "I really don’t like to read comic books",
            "I don’t want to read a comic book",
            "I really don’t want to read a comic book",
            "I don’t want to read more comic books",
            "I really don’t want to read any more comic books"

    }, {"I like to read rhymes books",
            "I really like to read rhymes books",
            "I want to read a rhymes book",
            "I really want to read a rhymes book",
            "I want to read some more rhymes books",
            "I really want to read some more rhymes books",
            "I don’t like to read rhymes books",
            "I really don’t like to read rhymes books",
            "I don’t want to read a rhymes book",
            "I really don’t want to read a rhymes book",
            "I don’t want to read more rhymes books",
            "I really don’t want to read any more rhymes books"

    }, {"I like drawing books",
            "I really like drawing books",
            "I want a drawing book",
            "I really want a drawing book",
            "I want more drawing books",
            "I really want some more drawing books",
            "I don’t like drawing books",
            "I really don’t like drawing books",
            "I don’t want a drawing book",
            "I really don’t want a drawing book",
            "I don’t want more drawing books",
            "I really don’t want any more drawing books"

    }, {"I like to read story books",
            "I really like to read story books",
            "I want to read a story book",
            "I really want to read a story book",
            "I want to read some more story books",
            "I really want to read some more story books",
            "I don’t like to read story books",
            "I really don’t like to read story books",
            "I don’t want to read a story book",
            "I really don’t want to read a story book",
            "I don’t want to read more story books",
            "I really don’t want to read any more story books"

    }, {"I like to see picture books",
            "I really like to see picture books",
            "I want to see a picture book",
            "I really want to see a picture book",
            "I want to see some more picture books",
            "I really want to see some more picture books",
            "I don’t like to see picture books",
            "I really don’t like to see picture books",
            "I don’t want to see a picture book",
            "I really don’t want to see a picture book",
            "I don’t want to see more picture books",
            "I really don’t want to see any more picture books"

    }, {"I like to read mystery books",
            "I really like to read mystery books",
            "I want to read a mystery book",
            "I really want to read a mystery book",
            "I want to read some more mystery books",
            "I really want to read some more mystery books",
            "I don’t like to read mystery books",
            "I really don’t like to read mystery books",
            "I don’t want to read a mystery book",
            "I really don’t want to read a mystery book",
            "I don’t want to read more mystery books",
            "I really don’t want to read any more mystery books"

    }, {"I like to read adventure books",
            "I really like to read adventure books",
            "I want to read an adventure book",
            "I really want to read an adventure book",
            "I want to read some more adventure books",
            "I really want to read some more adventure books",
            "I don’t like to read adventure books",
            "I really don’t like to read adventure books",
            "I don’t want to read an adventure book",
            "I really don’t want to read an adventure book",
            "I don’t want to read more adventure books",
            "I really don’t want to read any more adventure books"

    },  {"I like to use my school notebook",
            "I really like to use my school notebook",
            "I want my school notebook",
            "I really want to use my school notebook",
            "I want more school notebooks",
            "I really want some more school notebooks",
            "I don’t like to use my school notebook",
            "I really don’t like to use my school notebook",
            "I don’t want my school notebook",
            "I really don’t want to use my school notebook",
            "I don’t want more school notebooks",
            "I really don’t want any more school notebooks"

    }, {"I like to learn maths",
            "I really like to learn maths",
            "I want my maths book",
            "I really want my maths book",
            "I want to learn some more maths",
            "I really want some more maths books",
            "I don’t like maths",
            "I really don’t like maths",
            "I don’t want my maths book",
            "I really don’t want my maths book",
            "I don’t want to learn more maths",
            "I really don’t want any more maths books"

    }, {"I like to learn science",
            "I really like to learn science",
            "I want my science book",
            "I really want my science book",
            "I want to learn some more science",
            "I really want some more science books",
            "I don’t like science",
            "I really don’t like science",
            "I don’t want my science book",
            "I really don’t want my science book",
            "I don’t want to learn more science",
            "I really don’t want any more science books"

    }, {"I like to learn history",
            "I really like to learn history",
            "I want my history book",
            "I really want my history book",
            "I want to learn some more history",
            "I really want some more history books",
            "I don’t like history",
            "I really don’t like history",
            "I don’t want my history book",
            "I really don’t want my history book",
            "I don’t want to learn more history",
            "I really don’t want any more history books"

    }, {"I like to learn geography ",
            "I really like to learn geography ",
            "I want my geography book",
            "I really want my geography book",
            "I want to learn some more jawgrephy",
            "I really want some more geography books",
            "I don’t like geography ",
            "I really don’t like geography ",
            "I don’t want my geography book",
            "I really don’t want my geography book",
            "I don’t want to learn more geography ",
            "I really don’t want any more geography books"

    }, {"I like to learn social studies",
            "I really like to learn social studies",
            "I want my social studies book",
            "I really want my social studies book",
            "I want to learn some more social studies",
            "I really want some more social studies books",
            "I don’t like social studies",
            "I really don’t like social studies",
            "I don’t want my social studies book",
            "I really don’t want my social studies book",
            "I don’t want to learn more social studies",
            "I really don’t want any more social studies books"

    }, {"I like to learn english",
            "I really like to learn english",
            "I want my english book",
            "I really want my english book",
            "I want to learn some more english",
            "I really want some more english books",
            "I don’t like english",
            "I really don’t like english",
            "I don’t want my english book",
            "I really don’t want my english book",
            "I don’t want to learn more english",
            "I really don’t want any more english books"

    }, {"I like to learn hindi",
            "I really like to learn hindi",
            "I want my hindi book",
            "I really want my hindi book",
            "I want to learn some more hindi",
            "I really want some more hindi books",
            "I don’t like hindi",
            "I really don’t like hindi",
            "I don’t want my hindi book",
            "I really don’t want my hindi book",
            "I don’t want to learn more hindi",
            "I really don’t want any more hindi books"},

            {"I like to learn marathi",
                    "I really like to learn marathi",
                    "I want my marathi book",
                    "I really want my marathi book",
                    "I want to learn some more marathi",
                    "I really want some more marathi books",
                    "I don’t like marathi",
                    "I really don’t like marathi",
                    "I don’t want my marathi book",
                    "I really don’t want my marathi book",
                    "I don’t want to learn more marathi",
                    "I really don’t want any more marathi books"

            },  {"I like to read my textbooks",
            "I really like to read my textbooks",
            "I want to read my textbook",
            "I really want to read my textbook",
            "I want to read more textbooks",
            "I really want to read some more textbooks",
            "I don’t like to read my textbooks",
            "I really don’t like to read my textbooks",
            "I don’t want to read my textbook",
            "I really don’t want to read my textbook",
            "I don’t want to read more textbooks",
            "I really don’t want to read any more textbooks"

    },{"I like to read my favorite book",
            "I really like to read my favorite book",
            "I want to read my favorite book",
            "I really want to read my favorite book",
            "I want to read my favorite book again",
            "I really want to read my favorite book again",
            "I don’t like to read my favorite book",
            "I really don’t like to read my favorite book",
            "I don’t want to read my favorite book",
            "I really don’t want to read my favorite book",
            "I don’t want to read my favorite book again",
            "I really don’t want to read my favorite book again"

    }}, {{"I like black color",
            "I really like black color",
            "I want black color",
            "I really want black color",
            "I want more black color",
            "I really want some more black color",
            "I don’t like black color",
            "I really don’t like black color",
            "I don’t want black color",
            "I really don’t want black color",
            "I don’t want more black color",
            "I really don’t want any more black color"

    }, {"I like blue color",
            "I really like blue color",
            "I want blue color color",
            "I really want blue color",
            "I want more blue color",
            "I really want some more blue color",
            "I don’t like blue color",
            "I really don’t like blue color",
            "I don’t want blue color",
            "I really don’t want blue color",
            "I don’t want more blue color",
            "I really don’t want any more blue color"

    }, {"I like brown color",
            "I really like brown color",
            "I want brown color",
            "I really want brown color",
            "I want more brown color",
            "I really want some more brown color",
            "I don’t like brown color",
            "I really don’t like brown color",
            "I don’t want brown color",
            "I really don’t want brown color",
            "I don’t want more brown color",
            "I really don’t want any more brown color"

    }, {"I like green color",
            "I really like green color",
            "I want green color",
            "I really want green color",
            "I want more green color",
            "I really want some more green color",
            "I don’t like green",
            "I really don’t like green",
            "I don’t want green color",
            "I really don’t want green color",
            "I don’t want more green color",
            "I really don’t want any more green color"

    }, {"I like red color",
            "I really like red color",
            "I want red color",
            "I really want red color",
            "I want more red color",
            "I really want some more red color",
            "I don’t like red color",
            "I really don’t like red color",
            "I don’t want red color",
            "I really don’t want red color",
            "I don’t want more red color",
            "I really don’t want any more red color"

    }, {"I like silver color",
            "I really like silver color",
            "I want silver color",
            "I really want silver color",
            "I want more silver color",
            "I really want some more silver color",
            "I don’t like silver color",
            "I really don’t like silver color",
            "I don’t want silver color",
            "I really don’t want silver color",
            "I don’t want more silver color",
            "I really don’t want any more silver color"

    }, {"I like white color",
            "I really like white color",
            "I want white color",
            "I really want white color",
            "I want more white color",
            "I really want much more white color",
            "I don’t like white color",
            "I really don’t like white color",
            "I don’t want white color",
            "I really don’t want white color",
            "I don’t want more white color",
            "I really don’t want any more white color"

    }, {"I like yellow color",
            "I really like yellow color",
            "I want yellow color",
            "I really want yellow color",
            "I want more yellow color",
            "I really want some more yellow color",
            "I don’t like yellow color",
            "I really don’t like yellow_ color",
            "I don’t want yellow color",
            "I really don’t want yellow color",
            "I don’t want more yellow color",
            "I really don’t want any more yellow color"

    },  {"I like golden color",
            "I really like golden color",
            "I want golden color",
            "I really want golden color",
            "I want more golden color",
            "I really want some more golden color",
            "I don’t like golden color",
            "I really don’t like golden color",
            "I don’t want golden color",
            "I really don’t want golden color",
            "I don’t want more golden color",
            "I really don’t want any more golden color"

    }, {"I like pink color",
            "I really like pink color",
            "I want pink color",
            "I really want pink color",
            "I want more pink color",
            "I really want some more pink color",
            "I don’t like pink color",
            "I really don’t like pink color",
            "I don’t want pink color",
            "I really don’t want pink color",
            "I don’t want more pink color",
            "I really don’t want any more pink color"

    }, {"I like orange color",
            "I really like orange color",
            "I want orange color",
            "I really want orange color",
            "I want more orange color",
            "I really want some more orange color",
            "I don’t like orange color",
            "I really don’t like orange color",
            "I don’t want orange color",
            "I really don’t want orange color",
            "I don’t want more orange color",
            "I really don’t want any more orange color"

    }, {"I like purple color",
            "I really like purple color",
            "I want purple color",
            "I really want purple color",
            "I want more purple color",
            "I really want some more purple color",
            "I don’t like purple color",
            "I really don’t like purple color",
            "I don’t want purple color",
            "I really don’t want purple color",
            "I don’t want more purple color",
            "I really don’t want any more purple color"

    }, {"I like gray color",
            "I really like gray color",
            "I want gray color",
            "I really want gray color",
            "I want more gray color",
            "I really want some more gray color",
            "I don’t like gray color",
            "I really don’t like gray color",
            "I don’t want gray color",
            "I really don’t want gray color",
            "I don’t want more gray color",
            "I really don’t want any more gray color"

    }}, {{"I like to draw standing lines",
            "I really like to draw standing lines",
            "I want to draw a standing line",
            "I really want to draw a standing line",
            "I want to draw more standing lines",
            "I really want to draw some more standing lines",
            "I don’t like to draw standing lines",
            "I really don’t like to draw standing lines",
            "I don’t want to draw a standing line",
            "I really don’t want to draw a standing line",
            "I don’t want to draw more standing lines",
            "I really don’t want to draw any more standing lines"

    }, {"I like to draw sleeping lines",
            "I really like to draw sleeping lines",
            "I want to draw a sleeping line",
            "I really want to draw a sleeping line",
            "I want to draw more sleeping lines",
            "I really want to draw some more sleeping lines",
            "I don’t like to draw sleeping lines",
            "I really don’t like to draw sleeping lines",
            "I don’t want to draw a sleeping line",
            "I really don’t want to draw a sleeping line",
            "I don’t want to draw more sleeping lines",
            "I really don’t want to draw any more sleeping lines"

    }, {"I like to draw slanting lines",
            "I really like to draw slanting lines",
            "I want to draw a slanting line",
            "I really want to draw a slanting line",
            "I want to draw more slanting lines",
            "I really want to draw some more slanting lines",
            "I don’t like to draw slanting lines",
            "I really don’t like to draw slanting lines",
            "I don’t want to draw a slanting line",
            "I really don’t want to draw a slanting line",
            "I don’t want to draw more slanting lines",
            "I really don’t want to draw any more slanting lines",

    }, {"I like to draw circles",
            "I really like to draw circles",
            "I want to draw a circle",
            "I really want to draw a circle",
            "I want to draw more circles",
            "I really want to draw some more circles",
            "I don’t like to draw circles",
            "I really don’t like to draw circles",
            "I don’t want to draw a circle",
            "I really don’t want to draw a circle",
            "I don’t want to draw more circles",
            "I really don’t want to draw any more circles"

    }, {"I like to draw rectangles",
            "I really like to draw rectangles",
            "I want to draw a rectangle",
            "I really want to draw a rectangle",
            "I want to draw more rectangles",
            "I really want to draw some more rectangles",
            "I don’t like to draw rectangles",
            "I really don’t like to draw rectangles",
            "I don’t want to draw a rectangle",
            "I really don’t want to draw a rectangle",
            "I don’t want to draw more rectangles",
            "I really don’t want to draw any more rectangles"

    }, {"I like to draw squares",
            "I really like to draw squares",
            "I want to draw a square",
            "I really want to draw a square",
            "I want to draw more squares",
            "I really want to draw some more squares",
            "I don’t like to draw squares",
            "I really don’t like to draw squares",
            "I don’t want to draw a square",
            "I really don’t want to draw a square",
            "I don’t want to draw more squares",
            "I really don’t want to draw any more squares"

    }, {"I like to draw Triangles",
            "I really like to draw Triangles",
            "I want to draw a Triangle",
            "I really want to draw a Triangle",
            "I want to draw more Triangles",
            "I really want to draw some more Triangles",
            "I don’t like to draw Triangles",
            "I really don’t like to draw Triangles",
            "I don’t want to draw a Triangle",
            "I really don’t want to draw a Triangle",
            "I don’t want to draw more Triangles",
            "I really don’t want to draw any more Triangles"

    }, {"I like to draw stars",
            "I really like to draw stars",
            "I want to draw a star",
            "I really want a star",
            "I want to draw more stars",
            "I really want to draw some more stars",
            "I don’t like to draw stars",
            "I really don’t like to draw stars",
            "I don’t want to draw a star",
            "I really don’t want to draw a star",
            "I don’t want to draw more stars",
            "I really don’t want to draw any more stars"

    }, {"I like to draw hearts",
            "I really like to draw hearts",
            "I want to draw a heart",
            "I really want to draw a heart",
            "I want to draw more hearts",
            "I really want to draw some more hearts",
            "I don’t like to draw hearts",
            "I really don’t like to draw hearts",
            "I don’t want to draw a heart",
            "I really don’t want to draw a heart",
            "I don’t want to draw more hearts",
            "I really don’t want to draw any more hearts"

    }, {"I like to draw trapeziums",
            "I really like to draw trapeziums",
            "I want to draw a trapezium",
            "I really want to draw a trapezium",
            "I want to draw more trapeziums",
            "I really want to draw some more trapeziums",
            "I don’t like to draw trapeziums",
            "I really don’t like to draw trapeziums",
            "I don’t want to draw a trapezium",
            "I really don’t want to draw a trapezium",
            "I don’t want to draw more trapeziums",
            "I really don’t want to draw any more trapeziums"

    }, {"I like to draw cubes",
            "I really like to draw cubes",
            "I want to draw a cube",
            "I really want to draw a cube",
            "I want to draw more cubes",
            "I really want to draw some more cubes",
            "I don’t like to draw cubes",
            "I really don’t like to draw cubes",
            "I don’t want to draw a cube",
            "I really don’t want to draw a cube",
            "I don’t want to draw more cubes",
            "I really don’t want to draw any more cubes"

    }, {"I like to draw rhombuses",
            "I really like to draw rhombuses",
            "I want to draw a rhombus",
            "I really want to draw a rhombus",
            "I want to draw more rhombuses",
            "I really want to draw some more rhombuses",
            "I don’t like to draw rhombuses",
            "I really don’t like to draw rhombuses",
            "I don’t want to draw a rhombus",
            "I really don’t want to draw a rhombus",
            "I don’t want to draw more rhombuses",
            "I really don’t want to draw any more rhombuses"

    }, {"I like to draw hexagons",
            "I really like to draw hexagons",
            "I want to draw a hexagon",
            "I really want to draw a hexagon",
            "I want to draw more hexagons",
            "I really want to draw some more hexagons",
            "I don’t like to draw hexagons",
            "I really don’t like to draw hexagons",
            "I don’t want to draw a hexagon",
            "I really don’t want to draw a hexagon",
            "I don’t want to draw more hexagons",
            "I really don’t want to draw any more hexagons"

    }, {"I like to draw ovals",
            "I really like to draw ovals",
            "I want to draw a oval",
            "I really want to draw a oval",
            "I want to draw more ovals",
            "I really want to draw some more ovals",
            "I don’t like to draw ovals",
            "I really don’t like to draw ovals",
            "I don’t want to draw a oval",
            "I really don’t want to draw a oval",
            "I don’t want to draw more ovals",
            "I really don’t want to draw any more ovals"

    }, {"I like to draw diamonds",
            "I really like to draw diamonds",
            "I want to draw a diamond",
            "I really want to draw a diamond",
            "I want to draw more diamonds",
            "I really want to draw some more diamonds",
            "I don’t like to draw diamonds",
            "I really don’t like to draw diamonds",
            "I don’t want to draw a diamond",
            "I really don’t want to draw a diamond",
            "I don’t want to draw more diamonds",
            "I really don’t want to draw any more diamonds"

    }, {"I like to draw pentagons",
            "I really like to draw pentagons",
            "I want to draw a pentagon",
            "I really want to to draw a pentagon",
            "I want to draw more pentagons",
            "I really want to draw some more pentagons",
            "I don’t like to draw pentagons",
            "I really don’t like to draw pentagons",
            "I don’t want to draw a pentagon",
            "I really don’t want to draw a pentagon",
            "I don’t want to draw more pentagons",
            "I really don’t want to draw any more pentagons"

    }, {"I like to draw freeform",
            "I really like to draw freeform",
            "I want to draw  freeform",
            "I really want to draw  freeform",
            "I want to draw more freeform",
            "I really want to draw more freeform",
            "I don’t like to draw freeform",
            "I really don’t like to draw freeform",
            "I don’t want to draw  freeform",
            "I really don’t want to draw freeform",
            "I don’t want to draw more freeform",
            "I really don’t want to draw more freeform"

    }}, {{"I like to use pencils",
            "I really like to use pencils",
            "I want a pencil",
            "I really want a pencil",
            "I want more pencils",
            "I really want some more pencils",
            "I don’t like to use pencils",
            "I really don’t like to use pencils",
            "I don’t want a pencil",
            "I really don’t want a pencil",
            "I don’t want more pencils",
            "I really don’t want any more pencils"

    }, {"I like to use pens",
            "I really like to use pens",
            "I want a pen",
            "I really want a pen",
            "I want more pens",
            "I really want some more pens",
            "I don’t like to use pens",
            "I really don’t like to use pens",
            "I don’t want a pen",
            "I really don’t want a pen",
            "I don’t want more pens",
            "I really don’t want any more pens"

    }, {"I like to use scales",
            "I really like to use scales",
            "I want a scale",
            "I really want a scale",
            "I want to use the scale again",
            "I really want another scale",
            "I don’t like to use scales",
            "I really don’t like to use scales",
            "I don’t want a scale",
            "I really don’t want a scale",
            "I don’t want to use the scale again",
            "I really don’t want another scale"

    }, {"I like to use erasers",
            "I really like to use erasers",
            "I want an eraser",
            "I really want an eraser",
            "I want to use an eraser again",
            "I really want another eraser",
            "I don’t like to use erasers",
            "I really don’t like to use erasers",
            "I don’t want an eraser",
            "I really don’t want an eraser",
            "I don’t want to use an eraser again",
            "I really don’t want another eraser"

    }, {"I like to use sharpeners",
            "I really like to use sharpeners",
            "I want a sharpener",
            "I really want a sharpener",
            "I want to use the sharpener again",
            "I really want another sharpener",
            "I don’t like to use sharpeners",
            "I really don’t like to use sharpeners",
            "I don’t want a sharpener",
            "I really don’t want a sharpener",
            "I don’t want to use the sharpener again",
            "I really don’t want another sharpener"

    }, {"I like to use crayons",
            "I really like to use crayons",
            "I want crayons",
            "I really want crayons",
            "I want some crayons",
            "I really want some more crayons",
            "I don’t like to use crayons",
            "I really don’t like to use crayons",
            "I don’t want crayons",
            "I really don’t want crayons",
            "I don’t want more crayons",
            "I really don’t want any more crayons"

    }, {"I like to use blank papers",
            "I really like to use blank papers",
            "I want some blank papers",
            "I really want some blank papers",
            "I want more blank papers",
            "I really want some more blank papers",
            "I don’t like to use blank papers",
            "I really don’t like to use blank papers",
            "I don’t want any blank papers",
            "I really don’t want any blank papers",
            "I don’t want more blank papers",
            "I really don’t want any more blank papers"

    }, {"I like to use coloured papers",
            "I really like to use coloured papers",
            "I want some coloured papers",
            "I really want some coloured papers",
            "I want more coloured papers",
            "I really want some more coloured papers",
            "I don’t like to use coloured papers",
            "I really don’t like to use coloured papers",
            "I don’t want any coloured papers",
            "I really don’t want any coloured papers",
            "I don’t want more coloured papers",
            "I really don’t want any more coloured papers"

    }, {"I like to use scissors",
            "I really like to use scissors",
            "I want a pair of scissors",
            "I really want a pair of scissors",
            "I want to use the scissors again",
            "I really want another pair of scissors",
            "I don’t like to use scissors",
            "I really don’t like to use scissors",
            "I don’t want a pair of scissors",
            "I really don’t want a pair of scissors",
            "I don’t want to use the scissors again",
            "I really don’t want another pair of scissors"

    }, {"I like to use pencil led",
            "I really like to use pencil led",
            "I want some pencil led",
            "I really want some pencil led",
            "I want some more pencil led",
            "I really want some more pencil led",
            "I don’t like to use pencil led",
            "I really don’t like to use pencil led",
            "I don’t want any pencil led",
            "I really don’t want any pencil led",
            "I don’t want more pencil led",
            "I really don’t want any more pencil led"

    }, {"I like to use a compass",
            "I really like to use a compass",
            "I want a compass",
            "I really want a compass",
            "I want to use the compass again",
            "I really want another compass",
            "I don’t like to use a compass",
            "I really don’t like to use a compass",
            "I don’t want a compass",
            "I really don’t want a compass",
            "I don’t want to use the compass again",
            "I really don’t want another compass"

    }, {"I like to use a divider",
            "I really like to use a divider",
            "I want a divider",
            "I really want a divider",
            "I want to use the divider again",
            "I really want another divider",
            "I don’t like to use a divider",
            "I really don’t like to use a divider",
            "I don’t want a divider",
            "I really don’t want a divider",
            "I don’t want to use the divider again",
            "I really don’t want another divider"

    }, {"I like to use a stapler",
            "I really like to use a stapler",
            "I want a stapler",
            "I really want a stapler",
            "I want to use the stapler again",
            "I really want another stapler",
            "I don’t like to use a stapler",
            "I really don’t like to use a stapler",
            "I don’t want a stapler",
            "I really don’t want a stapler",
            "I don’t want to use the stapler again",
            "I really don’t want another stapler"

    }, {"I like to use यूपिंस",
            "I really like to use यूपिंस",
            "I want a U-pin",
            "I really want a U-pin",
            "I want more यूपिंस",
            "I really want some more यूपिंस",
            "I don’t like to use यूपिंस",
            "I really don’t like to use यूपिंस",
            "I don’t want a U-pin",
            "I really don’t want a U-pin",
            "I don’t want more यूपिंस",
            "I really don’t want any more यूपिंस"

    }, {"I like to use selo tape",
            "I really like to use selo tape",
            "I want some selo tape",
            "I really want some selo tape",
            "I want more selo tape",
            "I really want some more selo tape",
            "I don’t like to use selo tape",
            "I really don’t like to use selo tape",
            "I don’t want any selo tape",
            "I really don’t want any selo tape",
            "I don’t want more selo tape",
            "I really don’t want any more selo tape"

    }, {"I like to use a compass box",
            "I really like to use a compass box",
            "I want my compass box",
            "I really want my compass box",
            "I want to use my compass box again",
            "I really want another compass box",
            "I don’t like to use a compass box",
            "I really don’t like to use a compass box",
            "I don’t want my compass box",
            "I really don’t want my compass box",
            "I don’t want to use my compass box again",
            "I really don’t want another compass box"

    }}, {{"I like my school bag",
            "I really like my school bag",
            "I want my school bag",
            "I really want my school bag",
            "I want my school bag again",
            "I really want my school bag again",
            "I don’t like my school bag",
            "I really don’t like my school bag",
            "I don’t want my school bag",
            "I really don’t want my school bag",
            "I don’t want my school bag again",
            "I really don’t want my school bag again"

    }, {"I like my lunch box",
            "I really like my lunch box",
            "I want my lunch box",
            "I really want my lunch box",
            "I want my lunch box again",
            "I really want my lunch box again",
            "I don’t like my lunch box",
            "I really don’t like my lunch box",
            "I don’t want my lunch box",
            "I really don’t want my lunch box",
            "I don’t want my lunch box again",
            "I really don’t want my lunch box again"

    }, {"I like my water bottle",
            "I really like my water bottle",
            "I want my water bottle",
            "I really want my water bottle",
            "I want my water bottle again",
            "I really want my water bottle again",
            "I don’t like my water bottle",
            "I really don’t like my water bottle",
            "I don’t want my water bottle",
            "I really don’t want my water bottle",
            "I don’t want my water bottle again",
            "I really don’t want my water bottle again"

    }, {"I like to use a compass box",
            "I really like to use a compass box",
            "I want my compass box",
            "I really want my compass box",
            "I want to use my compass box again",
            "I really want another compass box",
            "I don’t like to use a compass box",
            "I really don’t like to use a compass box",
            "I don’t want my compass box",
            "I really don’t want my compass box",
            "I don’t want to use my compass box again",
            "I really don’t want another compass box"

    }, {"I like to do homework",
            "I really like to do homework",
            "I want to do my homework",
            "I really want to do my homework",
            "I want to do some more homework",
            "I really want to do some more homework",
            "I don’t like to do homework",
            "I really don’t like to do homework",
            "I don’t want to do my homework",
            "I really don’t want to do my homework",
            "I don’t want to do more homework",
            "I really don’t want to do any more homework"

    }, {"I like to use my school notebooks",
            "I really like to use my school notebooks",
            "I want my school notebooks",
            "I really want my school notebooks",
            "I want some more school notebooks",
            "I really want some more school notebooks",
            "I don’t like to use my school notebooks",
            "I really don’t like to use my school notebooks",
            "I don’t want my school notebooks",
            "I really don’t want my school notebooks",
            "I don’t want more school notebooks",
            "I really don’t want any more school notebooks"

    }, {"I like to read my school textbooks",
            "I really like to read my school textbooks",
            "I want my school textbooks",
            "I really want my school textbooks",
            "I want more school textbooks",
            "I really want some more school textbooks",
            "I don’t like to read my school textbooks",
            "I really don’t like to read my school textbooks",
            "I don’t want my school textbooks",
            "I really don’t want my school textbooks",
            "I don’t want more school textbooks",
            "I really don’t want any more school textbooks"

    }, {"I like my school uniform",
            "I really like my school uniform",
            "I want to wear my school uniform",
            "I really want to wear my school uniform",
            "I want to Wear my school uniform again",
            "I really want to Wear my school uniform again",
            "I don’t like my school uniform",
            "I really don’t like my school uniform",
            "I don’t want to wear my school uniform",
            "I really don’t want to wear my school uniform",
            "I don’t want to Wear my school uniform again",
            "I really don’t want to Wear my school uniform again"

    },  {"I like my school shoes",
            "I really like my school shoes",
            "I want to wear my school shoes",
            "I really want to wear my school shoes",
            "I want to Wear my school shoes again",
            "I really want to Wear my school shoes again",
            "I don’t like my school shoes",
            "I really don’t like my school shoes",
            "I don’t want to wear my school shoes",
            "I really don’t want to wear my school shoes",
            "I don’t want to Wear my school shoes again",
            "I really don’t want to Wear my school shoes again"

    }, {"I like my school socks",
            "I really like my school socks",
            "I want to wear my school socks",
            "I really want to wear my school socks",
            "I want to Wear my school socks again",
            "I really want to Wear my school socks again",
            "I don’t like my school socks",
            "I really don’t like my school socks",
            "I don’t want to wear my school socks",
            "I really don’t want to wear my school socks",
            "I don’t want to Wear my school socks again",
            "I really don’t want to Wear my school socks again"

    }, {"I like to use pencils",
            "I really like to use pencils",
            "I want a pencil",
            "I really want a pencil",
            "I want more pencils",
            "I really want some more pencils",
            "I don’t like to use pencils",
            "I really don’t like to use pencils",
            "I don’t want a pencil",
            "I really don’t want a pencil",
            "I don’t want more pencils",
            "I really don’t want any more pencils"

    }, {"I like to use pens",
            "I really like to use pens",
            "I want a pen",
            "I really want a pen",
            "I want more pens",
            "I really want some more pens",
            "I don’t like to use pens",
            "I really don’t like to use pens",
            "I don’t want a pen",
            "I really don’t want a pen",
            "I don’t want more pens",
            "I really don’t want any more pens"

    }, {"I like to use scales",
            "I really like to use scales",
            "I want a scale",
            "I really want a scale",
            "I want to use the scale again",
            "I really want another scale",
            "I don’t like to use scales",
            "I really don’t like to use scales",
            "I don’t want a scale",
            "I really don’t want a scale",
            "I don’t want to use the scale again",
            "I really don’t want another scale"

    }, {"I like to use erasers",
            "I really like to use erasers",
            "I want an eraser",
            "I really want an eraser",
            "I want to use an eraser again",
            "I really want another eraser",
            "I don’t like to use erasers",
            "I really don’t like to use erasers",
            "I don’t want an eraser",
            "I really don’t want an eraser",
            "I don’t want to use an eraser again",
            "I really don’t want another eraser"

    }, {"I like to use sharpeners",
            "I really like to use sharpeners",
            "I want a sharpener",
            "I really want a sharpener",
            "I want to use the sharpener again",
            "I really want another sharpener",
            "I don’t like to use sharpeners",
            "I really don’t like to use sharpeners",
            "I don’t want a sharpener",
            "I really don’t want a sharpener",
            "I don’t want to use the sharpener again",
            "I really don’t want another sharpener"

    }, {"I like to write on the board with chalk",
            "I really like to write on the board with chalk",
            "I want some chalk",
            "I really want some chalk",
            "I want more chalk",
            "I really want some more chalk",
            "I don’t like to write on the board with chalk",
            "I really don’t like to write on the board with chalk",
            "I don’t want any chalk",
            "I really don’t want any chalk",
            "I don’t want more chalk",
            "I really don’t want any more chalk"

    }}, {{"I like to use the window",
            "I really like to use the window",
            "I want to open the window",
            "I really want to open the window",
            "I want to open the window again",
            "I really want to open the window again",
            "I don’t like to use the window",
            "I really don’t like to use the window",
            "I want to close the window",
            "I really want to close the window",
            "I want to close the window again",
            "I really want to close the window again",

    }, {"I like to use the door",
            "I really like to use the door",
            "I want to open the door",
            "I really want to open the door",
            "I want to open the door again",
            "I really want to open the door again",
            "I don’t like to use the door",
            "I really don’t like to use the door",
            "I want to close the door",
            "I really want to close the door",
            "I want to close the door again",
            "I really want to close the door again"

    }, {"I like to use the fan",
            "I really like to use the fan",
            "I want to switch on the fan",
            "I really want to switch on the fan",
            "I want to switch on the fan again",
            "I really want to switch on the fan again",
            "I don’t like to use the fan",
            "I really don’t like to use the fan",
            "I want to switch off the fan",
            "I really want to switch off the fan",
            "I want to switch off the fan again",
            "I really want to switch off the fan again"

    }, {"I like to use the lamp",
            "I really like to use the lamp",
            "I want to switch on the lamp",
            "I really want to switch on the lamp",
            "I want to switch on the lamp again",
            "I really want to switch on the lamp again",
            "I don’t like to use the lamp",
            "I really don’t like to use the lamp",
            "I want to switch off the lamp",
            "I really want to switch off the lamp",
            "I want to switch off the lamp again",
            "I really want to switch off the lamp again"

    }, {"I like to use the desk",
            "I really like to use the desk",
            "I want to use the desk",
            "I really want to use the desk",
            "I want to use the desk again",
            "I really want to use the desk again",
            "I don’t like to use the desk",
            "I really don’t like to use the desk",
            "I don't want to use the desk",
            "I really don't want to use the desk",
            "I don't want to use the desk again",
            "I really don't want to use the desk again"

    }, {"I like to use the cupboard",
            "I really like to use the cupboard",
            "I want to open the cupboard",
            "I really want to open the cupboard",
            "I want to use the cupboard again",
            "I really want to use the cupboard again",
            "I don’t like to use the cupboard",
            "I really don’t like to use the cupboard",
            "I want to close the cupboard",
            "I really want to close the cupboard",
            "I don’t want to use the cupboard again",
            "I really don’t want to use the cupboard again"

    }, {"I like to use the table",
            "I really like to use the table",
            "I want to use the table",
            "I really want to use the table",
            "I want to use the table again",
            "I really want to use the table again",
            "I don’t like to use the table",
            "I really don’t like to use the table",
            "I don’t want to use the table",
            "I really don’t want to use the table",
            "I don’t want to use the table again",
            "I really don’t want to use the table again"

    }, {"I like to sit on a chair ",
            "I really like to sit on a chair",
            "I want to sit on a chair",
            "I really want to sit on a chair",
            "I want to sit on the chair for some more time",
            "I really want to sit on the chair for a bit longer",
            "I don’t like to sit on a chair",
            "I really don’t like to sit on a chair",
            "I don’t want to sit on a chair",
            "I really don’t want to sit on a chair",
            "I don’t want to sit on the chair any more ",
            "I really don’t want to sit on the chair any longer"

    },  {"I like to go to the toilet",
            "I really like to go to the toilet",
            "I want to go to the toilet",
            "I really want to go to the toilet",
            "I want to go to the toilet again",
            "I really want to go to the toilet again",
            "I don’t like to go to the toilet",
            "I really don’t like to go to the toilet",
            "I don’t want to go to the toilet",
            "I really don’t want to go to the toilet",
            "I don’t want to go to the toilet again ",
            "I really don’t want to go to the toilet again "

    }, {"I like to go to the kitchen",
            "I really like to go to the kitchen",
            "I want to go to the kitchen",
            "I really want to go to the kitchen",
            "I want to go to the kitchen again",
            "I really want to go to the kitchen again",
            "I don’t like to go to the kitchen",
            "I really don’t like to go to the kitchen",
            "I don’t want to go to the kitchen",
            "I really don’t want to go to the kitchen",
            "I don’t want to go to the kitchen again",
            "I really don’t want to go to the kitchen again"

    }, {"I like to go to the living room",
            "I really like to go to the living room",
            "I want to go to the living room",
            "I really want to go to the living room",
            "I want to go to the living room again",
            "I really want to go to the living room again",
            "I don’t like to go to the living room",
            "I really don’t like to go to the living room",
            "I don’t want to go to the living room",
            "I really don’t want to go to the living room",
            "I don’t want to go to the living room again",
            "I really don’t want to go to the living room again"

    }, {"I like to go to the bedroom",
            "I really like to go to the bedroom",
            "I want to go to the bedroom",
            "I really want to go to the bedroom",
            "I want to go to the bedroom again",
            "I really want to go to the bedroom again",
            "I don’t like to go to the bedroom",
            "I really don’t like to go to the bedroom",
            "I don’t want to go to the bedroom",
            "I really don’t want to go to the bedroom",
            "I don’t want to go to the bedroom again",
            "I really don’t want to go to the bedroom again"

    }, {"I like to go to the play room",
            "I really like to go to the play room",
            "I want to go to the play room",
            "I really want to go to the play room",
            "I want to go to the play room again",
            "I really want to go to the play room again",
            "I don’t like to go to the play room",
            "I really don’t like to go to the play room",
            "I don’t want to go to the play room",
            "I really don’t want to go to the play room",
            "I don’t want to go to the play room again",
            "I really don’t want to go to the play room again"

    }, {"I like to go to the bathroom",
            "I really like to go to the bathroom",
            "I want to go to the bathroom",
            "I really want to go to the bathroom",
            "I want to go to the bathroom again",
            "I really want to go to the bathroom again",
            "I don’t like to go to the bathroom",
            "I really don’t like to go to the bathroom",
            "I don’t want to go to the bathroom",
            "I really don’t want to go to the bathroom",
            "I don’t want to go to the bathroom again",
            "I really don’t want to go to the bathroom again"

    }, {"I like to go to the balcony",
            "I really like to go to the balcony",
            "I want to go to the balcony",
            "I really want to go to the balcony",
            "I want to go to the balcony again",
            "I really want to go to the balcony again",
            "I don’t like to go to the balcony",
            "I really don’t like to go to the balcony",
            "I don’t want to go to the balcony",
            "I really don’t want to go to the balcony",
            "I don’t want to go to the balcony again",
            "I really don’t want to go to the balcony again"

    }, {"I like to go to the study room",
            "I really like to go to the study room",
            "I want to go to the study room",
            "I really want to go to the study room",
            "I want to go to the study room again",
            "I really want to go to the study room again",
            "I don’t like to go to the study room",
            "I really don’t like to go to the study room",
            "I don’t want to go to the study room",
            "I really don’t want to go to the study room",
            "I don’t want to go to the study room again",
            "I really don’t want to go to the study room again"

    },  {"I like to use the bed",
            "I really like to use the bed",
            "I want to lie on the bed",
            "I really want to lie on the bed",
            "I want to lie on the bed again",
            "I really want to lie on the bed again",
            "I don’t like to use the bed",
            "I really don’t like to use the bed",
            "I don’t want to lie on the bed",
            "I really don’t want to lie on the bed",
            "I don’t want to lie on the bed again",
            "I really don’t want to lie on the bed again"

    }, {"I like to watch TV",
            "I really like to watch TV",
            "I want to watch TV",
            "I really want to watch TV",
            "I want to watch TV for some more time",
            "I really want to watch TV for a bit longer",
            "I don’t like to watch TV",
            "I really don’t like to watch TV",
            "I don’t want to watch TV",
            "I really don’t want to watch TV",
            "I don’t want to watch TV for more time",
            "I really don’t want to watch TV any more"

    }, {"I like to use the computer",
            "I really like to use the computer",
            "I want to use the computer",
            "I really want to use the computer",
            "I want to use the computer for some more time",
            "I really want to use the computer for a bit longer",
            "I don’t like to use the computer",
            "I really don’t like to use the computer",
            "I don’t want to use the computer",
            "I really don’t want to use the computer",
            "I don’t want to use the computer for more time",
            "I really don’t want to use the computer any more"

    }, {"I like to use the sofa",
            "I really like to use the sofa",
            "I want to sit on the sofa",
            "I really want to sit on the sofa",
            "I want to sit on the sofa for some more time",
            "I really want to sit on the sofa for a bit longer",
            "I don’t like to use the sofa",
            "I really don’t like to use the sofa",
            "I don’t want to sit on the sofa",
            "I really don’t want to sit on the sofa",
            "I don’t want to sit on the sofa for more time",
            "I really don’t want to sit on the sofa any longer"

    }, {"I like to use the fridge",
            "I really like to use the fridge",
            "I want to open the fridge",
            "I really want to open the fridge",
            "I want to use the fridge again",
            "I really want to use the fridge once again",
            "I don’t like to use the fridge",
            "I really don’t like to use the fridge",
            "I want to close the fridge",
            "I really want to close the fridge",
            "I don’t want to use the fridge again",
            "I really don’t want to use the fridge once again"

    }, {"I like to use the microwave",
            "I really like to use the microwave",
            "I want to use the microwave",
            "I really want to use the microwave",
            "I want to use the microwave again",
            "I really want to use the microwave again",
            "I don’t like to use the microwave",
            "I really don’t like to use the microwave",
            "I don’t want to use the microwave",
            "I really don’t want to use the microwave",
            "I don’t want to use the microwave again",
            "I really don’t want to use the microwave again"

    }, {"I like to use the washing machine",
            "I really like to use the washing machine",
            "I want to use the washing machine",
            "I really want to use the washing machine",
            "I want to use the washing machine again",
            "I really want to use the washing machine again",
            "I don’t like to use the washing machine",
            "I really don’t like to use the washing machine",
            "I don’t want to use the washing machine",
            "I really don’t want to use the washing machine",
            "I don’t want to use the washing machine again",
            "I really don’t want to use the washing machine again"

    }, {"I like to use the vacuum cleaner",
            "I really like to use the vacuum cleaner",
            "I want to use the vacuum cleaner",
            "I really want to use the vacuum cleaner",
            "I want to use the vacuum cleaner again",
            "I really want to use the vacuum cleaner again",
            "I don’t like to use the vacuum cleaner",
            "I really don’t like to use the vacuum cleaner",
            "I don’t want to use the vacuum cleaner",
            "I really don’t want to use the vacuum cleaner",
            "I don’t want to use the vacuum cleaner again",
            "I really don’t want to use the vacuum cleaner again"

    },  {"I like to use the clock",
            "I really like to use the clock",
            "I want to see the clock",
            "I really want to see the clock",
            "I want to see the clock again",
            "I really want to see the clock again",
            "I don’t like to use the clock",
            "I really don’t like to use the clock",
            "I don’t want to see the clock",
            "I really don’t want to see the clock",
            "I don’t want to see the clock again",
            "I really don’t want to see the clock again"

    }, {"I like to use the tube light",
            "I really like to use the tube light",
            "I want to switch on the tube light",
            "I really want to switch on the tube light",
            "I want to switch on the tube light again",
            "I really want to switch on the tube light again",
            "I don’t like to use the tube light",
            "I really don’t like to use the tube light",
            "I want to switch off the tube light",
            "I really want to switch off the tube light",
            "I want to switch off the tube light again",
            "I really want to switch off the tube light again"

    }}, {{"I like to ride in a bus",
            "I really like ride in a bus",
            "I want to ride in a bus",
            "I really want ride in a bus",
            "I want to ride in a bus again ",
            "I really want to ride in a bus again ",
            "I don’t like to ride in a bus",
            "I really don’t like to ride in a bus",
            "I don’t want to ride in a bus",
            "I really don’t want to ride in a bus",
            "I don’t want to ride in a bus again ",
            "I really don’t want to ride in a bus again"

    }, {"I like to ride in the school bus",
            "I really like to ride in the school bus",
            "I want to ride in the school bus",
            "I really want to ride in the school bus",
            "I want to ride in the school bus again",
            "I really want to ride in the school bus again",
            "I don’t like to ride in the school bus",
            "I really don’t like to ride in the school bus",
            "I don’t want to ride in the school bus",
            "I really don’t want to ride in the school bus",
            "I don’t want to ride in the school bus again",
            "I really don’t want to ride in the school bus again"

    }, {"I like to ride in a car",
            "I really like to ride in a car",
            "I want to ride in a car",
            "I really want to ride in a car",
            "I want to ride in a car again",
            "I really want to ride in a car again",
            "I don’t like to ride in a car",
            "I really don’t like to ride in a car",
            "I don’t want to ride in a car",
            "I really don’t want to ride in a car",
            "I don’t want to ride in a car again",
            "I really don’t want to ride in a car again"

    }, {"I like to ride my bicycle",
            "I really like to ride my bicycle",
            "I want to ride my bicycle",
            "I really want to ride my bicycle",
            "I want to ride my bicycle again",
            "I really want to ride my bicycle again",
            "I don’t like to ride my bicycle",
            "I really don’t like to ride my bicycle",
            "I don’t want to ride my bicycle",
            "I really don’t want to ride my bicycle",
            "I don't want to ride my bicycle again",
            "I really don’t want to ride my bicycle again "

    }, {"I like to ride in a train",
            "I really like to ride in a train",
            "I want to ride in a train",
            "I really want to ride in a train",
            "I want to ride in a train again",
            "I really want to ride in a train again",
            "I don’t like to ride in a train",
            "I really don’t like to ride in a train",
            "I don’t want to ride in a train",
            "I really don’t want to ride in a train",
            "I don’t want to ride in a train again",
            "I really don’t want to ride in a train again"

    }, {"I like to ride in a rickshaa",
            "I really like to ride in a rickshaa",
            "I want to ride in a rickshaa",
            "I really want to ride in a rickshaa",
            "I want to ride in a rickshaa again",
            "I really want to ride in a rickshaa again",
            "I don’t like to ride in a rickshaa",
            "I really don’t like to ride in a rickshaa",
            "I don’t want to ride in a rickshaa",
            "I really don’t want to ride in a rickshaa",
            "I don’t want to ride in a rickshaa again",
            "I really don’t want to ride in a rickshaa again"

    }, {"I like to ride on a bike",
            "I really like to ride on a bike",
            "I want to ride on a bike",
            "I really want to ride on a bike",
            "I want to ride on a bike again",
            "I really want to ride on a bike again",
            "I don’t like to ride on a bike",
            "I really don’t like to ride on a bike",
            "I don’t want to ride on a bike",
            "I really don’t want to ride on a bike",
            "I don’t want to ride on a bike again",
            "I really don’t want to ride on a bike again"

    }, {"I like to fly on an aeroplane",
            "I really like to fly on an aeroplane",
            "I want to fly on an aeroplane",
            "I really want to fly on an aeroplane",
            "I want to fly on an aeroplane again",
            "I really want to fly on an aeroplane again",
            "I don’t like to fly on an aeroplane",
            "I really don’t like to fly on an aeroplane",
            "I don’t want to fly on an aeroplane",
            "I really don’t want to fly on an aeroplane",
            "I don’t want to fly on an aeroplane again",
            "I really don’t want to fly on an aeroplane again"

    }, {"I like to sail on a ship",
            "I really like to sail on a ship",
            "I want to sail on a ship",
            "I really want to sail on a ship",
            "I want to sail on a ship again",
            "I really want to sail on a ship again",
            "I don’t like to sail on a ship",
            "I really don’t like to sail on a ship",
            "I don’t want to sail on a ship",
            "I really don’t want to sail on a ship",
            "I don’t want to sail on a ship again",
            "I really don’t want to sail on a ship again"

    }}}, {{{}}}, {{{}}}, {{{}, {"I am having a good time today",
            "I really am having a good time today",
            "I want to have fun today",
            "I really want to have fun today",
            "I want to go out again today",
            "I really want to go out again today",
            "I am not having a good time today",
            "I really am not having a good time today",
            "I don’t want to have fun today",
            "I really don’t want to have fun today",
            "I don’t want to go out again today",
            "I really don’t want to go out again today"

    }, {"I had a good time yesterday",
            "I had a really good time yesterday",
            "I had fun yesterday",
            "I really had fun yesterday",
            "I went out yesterday",
            "I really went out yesterday",
            "I did not have a good time yesterday",
            "I really did not have a good time yesterday",
            "I did not have fun yesterday",
            "I really did not have fun yesterday",
            "I did not go out yesterday",
            "I really did not go out yesterday"

    }, {"I am looking forward to tomorrow",
            "I am really looking forward to tomorrow",
            "I want to have fun tomorrow",
            "I really want to have fun tomorrow",
            "I want to go out again tomorrow",
            "I really want to go out again tomorrow",
            "I am not looking forward to tomorrow",
            "I am really not looking forward to tomorrow",
            "I don’t want to have fun tomorrow",
            "I really don’t want to have fun tomorrow",
            "I don’t want to go out again tomorrow",
            "I really don’t want to go out again tomorrow"

    }, {"I like the morning time",
            "I really like the morning time",
            "I want to go in the morning",
            "I really want to go in the morning",
            "I want to go again in the morning",
            "I really want to go again in the morning",
            "I don’t like the morning time",
            "I really don’t like the morning time",
            "I don’t want to go in the morning",
            "I really don’t want to go in the morning",
            "I don’t want to go again in the morning",
            "I really don’t want to go again in the morning"

    }, {"I like the afternoon time",
            "I really like the afternoon time",
            "I want to go in the afternoon",
            "I really want to go in the afternoon",
            "I want to go again in the afternoon",
            "I really want to go again in the afternoon",
            "I don’t like the afternoon time",
            "I really don’t like the afternoon time",
            "I don’t want to go in the afternoon",
            "I really don’t want to go in the afternoon",
            "I don’t want to go again in the afternoon",
            "I really don’t want to go again in the afternoon"

    }, {"I like the evening time",
            "I really like the evening time",
            "I want to go in the evening",
            "I really want to go in the evening",
            "I want to go again in the evening",
            "I really want to go again in the evening",
            "I don’t like the evening time",
            "I really don’t like the evening time",
            "I don’t want to go in the evening",
            "I really don’t want to go in the evening",
            "I don’t want to go again in the evening",
            "I really don’t want to go again in the evening"

    }, {"I like the night time",
            "I really like the night time",
            "I want to go at night",
            "I really want to go at night",
            "I want to go again at night",
            "I really want to go again at night",
            "I don’t like the night time",
            "I really don’t like the night time",
            "I don’t want to go at night",
            "I really don’t want to go at night",
            "I don’t want to go again at night",
            "I really don’t want to go again at night"

    }}, {{}, {"I like Mondays",
            "I really like Mondays",
            "I want to go on Monday",
            "I really want to go on Monday",
            "I want to go again on Monday",
            "I really want to go again on Monday",
            "I don’t like Mondays",
            "I really don’t like Mondays",
            "I don’t want to go on Monday",
            "I really don’t want to go on Monday",
            "I don’t want to go again on Monday",
            "I really don’t want to go again on Monday"

    }, {"I like Tuesdays",
            "I really like Tuesdays",
            "I want to go on Tuesday",
            "I really want to go on Tuesday",
            "I want to go again on Tuesday",
            "I really want to go again on Tuesday",
            "I don’t like Tuesdays",
            "I really don’t like Tuesdays",
            "I don’t want to go on Tuesday",
            "I really don’t want to go on Tuesday",
            "I don’t want to go again on Tuesday",
            "I really don’t want to go again on Tuesday"

    }, {"I like Wednesdays",
            "I really like Wednesdays",
            "I want to go on Wednesday",
            "I really want to go on Wednesday",
            "I want to go again on Wednesday",
            "I really want to go again on Wednesday",
            "I don’t like Wednesdays",
            "I really don’t like Wednesdays",
            "I don’t want to go on Wednesday",
            "I really don’t want to go on Wednesday",
            "I don’t want to go again on Wednesday",
            "I really don’t want to go again on Wednesday"

    }, {"I like Thursdays",
            "I really like Thursdays",
            "I want to go on Thursday",
            "I really want to go on Thursday",
            "I want to go again on Thursday",
            "I really want to go again on Thursday",
            "I don’t like Thursdays",
            "I really don’t like Thursdays",
            "I don’t want to go on Thursday",
            "I really don’t want to go on Thursday",
            "I don’t want to go again on Thursday",
            "I really don’t want to go again on Thursday"

    }, {"I like Fridays",
            "I really like Fridays",
            "I want to go on Friday",
            "I really want to go on Friday",
            "I want to go again on Friday",
            "I really want to go again on Friday",
            "I don’t like Fridays",
            "I really don’t like Fridays",
            "I don’t want to go on Friday",
            "I really don’t want to go on Friday",
            "I don’t want to go again on Friday",
            "I really don’t want to go again on Friday"

    }, {"I like Saturdays",
            "I really like Saturdays",
            "I want to go on Saturday",
            "I really want to go on Saturday",
            "I want to go again on Saturday",
            "I really want to go again on Saturday",
            "I don’t like Saturdays",
            "I really don’t like Saturdays",
            "I don’t want to go on Saturday",
            "I really don’t want to go on Saturday",
            "I don’t want to go again on Saturday",
            "I really don’t want to go again on Saturday"

    }, {"I like Sundays",
            "I really like Sundays",
            "I want to go on Sunday",
            "I really want to go on Sunday",
            "I want to go again on Sunday",
            "I really want to go again on Sunday",
            "I don’t like Sundays",
            "I really don’t like Sundays",
            "I don’t want to go on Sunday",
            "I really don’t want to go on Sunday",
            "I don’t want to go again on Sunday",
            "I really don’t want to go again on Sunday"

    }}, {{}, {"I like the month of January",
            "I really like the month of January",
            "I want to go in January",
            "I really want to go in January",
            "I want to go again in January",
            "I really want to go again in January",
            "I don’t like the month of January",
            "I really don’t like the month of January",
            "I don’t want to go in January",
            "I really don’t want to go in January",
            "I don’t want to go again in January",
            "I really don’t want to go again in January"

    }, {"I like the month of February",
            "I really like the month of February",
            "I want to go in February",
            "I really want to go in February",
            "I want to go again in February",
            "I really want to go again in February",
            "I don’t like the month of February",
            "I really don’t like the month of February",
            "I don’t want to go in February",
            "I really don’t want to go in February",
            "I don’t want to go again in February",
            "I really don’t want to go again in February"

    }, {"I like the month of March",
            "I really like the month of March",
            "I want to go in March",
            "I really want to go in March",
            "I want to go again in March",
            "I really want to go again in March",
            "I don’t like the month of March",
            "I really don’t like the month of March",
            "I don’t want to go in March",
            "I really don’t want to go in March",
            "I don’t want to go again in March",
            "I really don’t want to go again in March"

    }, {"I like the month of April",
            "I really like the month of April",
            "I want to go in April",
            "I really want to go in April",
            "I want to go again in April",
            "I really want to go again in April",
            "I don’t like the month of April",
            "I really don’t like the month of April",
            "I don’t want to go in April",
            "I really don’t want to go in April",
            "I don’t want to go again in April",
            "I really don’t want to go again in April"

    }, {"I like the month of May",
            "I really like the month of May",
            "I want to go in May",
            "I really want to go in May",
            "I want to go again in May",
            "I really want to go again in May",
            "I don’t like the month of May",
            "I really don’t like the month of May",
            "I don’t want to go in May",
            "I really don’t want to go in May",
            "I don’t want to go again in May",
            "I really don’t want to go again in May"

    }, {"I like the month of June",
            "I really like the month of June",
            "I want to go in June",
            "I really want to go in June",
            "I want to go again in June",
            "I really want to go again in June",
            "I don’t like the month of June",
            "I really don’t like the month of June",
            "I don’t want to go in June",
            "I really don’t want to go in June",
            "I don’t want to go again in June",
            "I really don’t want to go again in June"

    }, {"I like the month of July",
            "I really like the month of July",
            "I want to go in July",
            "I really want to go in July",
            "I want to go again in July",
            "I really want to go again in July",
            "I don’t like the month of July",
            "I really don’t like the month of July",
            "I don’t want to go in July",
            "I really don’t want to go in July",
            "I don’t want to go again in July",
            "I really don’t want to go again in July"

    },  {"I like the month of August",
            "I really like the month of August",
            "I want to go in August",
            "I really want to go in August",
            "I want to go again in August",
            "I really want to go again in August",
            "I don’t like the month of August",
            "I really don’t like the month of August",
            "I don’t want to go in August",
            "I really don’t want to go in August",
            "I don’t want to go again in August",
            "I really don’t want to go again in August"

    }, {"I like the month of September",
            "I really like the month of September",
            "I want to go in September",
            "I really want to go in September",
            "I want to go again in September",
            "I really want to go again in September",
            "I don’t like the month of September",
            "I really don’t like the month of September",
            "I don’t want to go in September",
            "I really don’t want to go in September",
            "I don’t want to go again in September",
            "I really don’t want to go again in September"

    }, {"I like the month of October",
            "I really like the month of October",
            "I want to go in October",
            "I really want to go in October",
            "I want to go again in October",
            "I really want to go again in October",
            "I don’t like the month of October",
            "I really don’t like the month of October",
            "I don’t want to go in October",
            "I really don’t want to go in October",
            "I don’t want to go again in October",
            "I really don’t want to go again in October"

    }, {"I like the month of November",
            "I really like the month of November",
            "I want to go in November",
            "I really want to go in November",
            "I want to go again in November",
            "I really want to go again in November",
            "I don’t like the month of November",
            "I really don’t like the month of November",
            "I don’t want to go in November",
            "I really don’t want to go in November",
            "I don’t want to go again in November",
            "I really don’t want to go again in November"

    }, {"I like the month of December",
            "I really like the month of December",
            "I want to go in December",
            "I really want to go in December",
            "I want to go again in December",
            "I really want to go again in December",
            "I don’t like the month of December",
            "I really don’t like the month of December",
            "I don’t want to go in December",
            "I really don’t want to go in December",
            "I don’t want to go again in December",
            "I really don’t want to go again in December"

    }, {"I am having a good time this month",
            "I really am having a good time this month",
            "I want to have fun this month",
            "I really want to have fun this month",
            "I want to go out again this month",
            "I really want to go out again this month",
            "I am not having a good time this month",
            "I really am not having a good time this month",
            "I don’t want to have fun this month",
            "I really don’t want to have fun this month",
            "I don’t want to go out again this month",
            "I really don’t want to go out again this month"

    }, {"I had a good time last month",
            "I had a really good time last month",
            "I had fun last month",
            "I really had fun last month",
            "I went out last month",
            "I really went out last month",
            "I did not have a good time last month",
            "I really did not have a good time last month",
            "I did not have fun last month",
            "I really did not have fun last month",
            "I did not go out  last month",
            "I really did not go out last month"

    }, {"I am looking forward to next month",
            "I am really looking forward to next month",
            "I want to have fun next month",
            "I really want to have fun next month",
            "I want to go out again next month",
            "I really want to go out again next month",
            "I am not looking forward to next month",
            "I am really not looking forward to next month",
            "I don’t want to have fun next month",
            "I really don’t want to have fun next month",
            "I don’t want to go out again next month",
            "I really don’t want to go out again next month"

    }}, {{}, {"I like the sunny weather",
            "I really like the sunny weather",
            "I want it to be sunny",
            "I really want it to be sunny",
            "I want it to be more sunny",
            "I really want it to be sunny again",
            "I don’t like the sunny weather",
            "I really don’t like the sunny weather",
            "I don’t want it to be sunny",
            "I really don’t want it to be sunny",
            "I don’t want it to be sunny any more",
            "I really don’t want it to be sunny again"

    }, {"I like the rainy weather",
            "I really like the rainy weather",
            "I want it to rain",
            "I really want it to rain",
            "I want it to be more rainy",
            "I really want it to be rainy again",
            "I don’t like the rainy weather",
            "I really don’t like the rainy weather",
            "I don’t want it to rain",
            "I really don’t want it to rain",
            "I don’t want it to be rainy any more",
            "I really don’t want it to be rainy again"

    }, {"I like the cloudy weather",
            "I really like the cloudy weather",
            "I want it to be cloudy",
            "I really want it to be cloudy",
            "I want it to be more cloudy",
            "I really want it to be cloudy again",
            "I don’t like the cloudy weather",
            "I really don’t like the cloudy weather",
            "I don’t want it to be cloudy",
            "I really don’t want it to be cloudy",
            "I don’t want it to be cloudy any more",
            "I really don’t want it to be cloudy again"

    }, {"I like the windy weather",
            "I really like the windy weather",
            "I want it to be windy",
            "I really want it to be windy",
            "I want it to be more windy",
            "I really want it to be windy again",
            "I don’t like the windy weather",
            "I really don’t like the windy weather",
            "I don’t want it to be windy",
            "I really don’t want it to be windy",
            "I don’t want it to be windy any more",
            "I really don’t want it to be windy again"

    }, {"I like the foggy weather",
            "I really like the foggy weather",
            "I want it to be foggy",
            "I really want it to be foggy",
            "I want it to be more foggy",
            "I really want it to be foggy again",
            "I don’t like the foggy weather",
            "I really don’t like the foggy weather",
            "I don’t want it to be foggy",
            "I really don’t want it to be foggy",
            "I don’t want it to be foggy any more",
            "I really don’t want it to be foggy again"

    },  {"I like the snow",
            "I really like the snow",
            "I want it to snow",
            "I really want it to snow",
            "I want it to snow more",
            "I really want it to snow again",
            "I don’t like the snow",
            "I really don’t like the snow",
            "I don’t want it to snow",
            "I really don’t want it to snow",
            "I don’t want it to snow more",
            "I really don’t want it to snow again"

    }}, {{}, {"I like the spring season",
            "I really like the spring season",
            "I want the spring season",
            "I really want the spring season",
            "I want the spring season to last longer",
            "I really want the spring season to last a bit longer",
            "I don’t like the spring season",
            "I really don’t like the spring season",
            "I don’t want the spring season",
            "I really don’t want the spring season",
            "I don’t want the spring season to last longer",
            "I really don’t want the spring season to last any longer"

    }, {"I like the summer season",
            "I really like the summer season",
            "I want the summer season",
            "I really want the summer season",
            "I want it to get warmer",
            "I really want it to get a bit warmer",
            "I don’t like the summer season",
            "I really don’t like the summer season",
            "I don’t want the summer season",
            "I really don’t want the summer season",
            "I don’t want it to get hotter",
            "I really don’t want it to get any hotter"

    }, {"I like the rainy season",
            "I really like the rainy season",
            "I want the rainy season",
            "I really want the rainy season",
            "I want it to rain more",
            "I really want it to rain some more",
            "I don’t like the rainy season",
            "I really don’t like the rainy season",
            "I don’t want the rainy season",
            "I really don’t want the rainy season",
            "I don’t want it to rain more",
            "I really don’t want it to rain any more"

    }, {"I like the autumn season",
            "I really like the autumn season",
            "I want the autumn season",
            "I really want the autumn season",
            "I want the autumn season to last longer",
            "I really want the autumn season to last a bit longer",
            "I don’t like the autumn season",
            "I really don’t like the autumn season",
            "I don’t want the autumn season",
            "I really don’t want the autumn season",
            "I don’t want the autumn season to last longer",
            "I really don’t want the autumn season to last any longer"

    }, {"I like the winter season",
            "I really like the winter season",
            "I want the winter season",
            "I really want the winter season",
            "I want it to get colder",
            "I really want it to get a bit colder",
            "I don’t like the winter season",
            "I really don’t like the winter season",
            "I don’t want the winter season",
            "I really don’t want the winter season",
            "I don’t want it to get colder",
            "I really don’t want it to get any colder"

    }}, {{"I love the Diwali festival",
            "I really like celebrating the Diwali festival",
            "I want to have fun during Diwali",
            "I really want to have fun during Diwali",
            "I want to have more fun during Diwali",
            "I really want to have some more fun during Diwali",
            "I don’t like the Diwali festival",
            "I really don’t like celebrating the Diwali festival",
            "I don’t want to have fun during Diwali",
            "I really don’t want to have fun during Diwali",
            "I don’t want to have more fun during Diwali",
            "I really don’t want to have any more fun during Diwali"

    }, {"I love the Ganesh chaturthi festival",
            "I really like celebrating the Ganesh chaturthi festival",
            "I want to have fun during Ganesh chaturthi",
            "I really want to have fun during Ganesh chaturthi",
            "I want to have more fun during Ganesh chaturthi",
            "I really want to have some more fun during Ganesh chaturthi",
            "I don’t like the Ganesh chaturthi festival",
            "I really don’t like celebrating the Ganesh chaturthi festival",
            "I don’t want to have fun during Ganesh chaturthi",
            "I really don’t want to have fun during Ganesh chaturthi",
            "I don’t want to have more fun during Ganesh chaturthi",
            "I really don’t want to have any more fun during Ganesh chaturthi"

    }, {"I love the Christmas festival",
            "I really like celebrating the Christmas festival",
            "I want to have fun during Christmas",
            "I really want to have fun during Christmas",
            "I want to have more fun during Christmas",
            "I really want to have some more fun during Christmas",
            "I don’t like the Christmas festival",
            "I really don’t like celebrating the Christmas festival",
            "I don’t want to have fun during Christmas",
            "I really don’t want to have fun during Christmas",
            "I don’t want to have more fun during Christmas",
            "I really don’t want to have any more fun during Christmas"

    }, {"I love the Dussehra festival",
            "I really like celebrating the Dussehra festival",
            "I want to have fun during Dussehra",
            "I really want to have fun during Dussehra",
            "I want to have more fun during Dussehra",
            "I really want to have some more fun during Dussehra",
            "I don’t like the Dussehra festival",
            "I really don’t like celebrating the Dussehra festival",
            "I don’t want to have fun during Dussehra",
            "I really don’t want to have fun during Dussehra",
            "I don’t want to have more fun during Dussehra",
            "I really don’t want to have any more fun during Dussehra"

    }, {"I love the मकर संक्रांति festival",
            "I really like celebrating the मकर संक्रांति festival",
            "I want to have fun during मकर संक्रांति",
            "I really want to have fun during मकर संक्रांति",
            "I want to have more fun during मकर संक्रांति",
            "I really want to have some more fun during मकर संक्रांति",
            "I don’t like the मकर संक्रांति festival",
            "I really don’t like celebrating the मकर संक्रांति festival",
            "I don’t want to have fun during मकर संक्रांति",
            "I really don’t want to have fun during मकर संक्रांति",
            "I don’t want to have more fun during मकर संक्रांति",
            "I really don’t want to have any more fun during मकर संक्रांति"

    }, {"I love the Holi festival",
            "I really like celebrating the Holi festival",
            "I want to have fun during Holi",
            "I really want to have fun during Holi",
            "I want to have more fun during Holi",
            "I really want to have some more fun during Holi",
            "I don’t like the Holi festival",
            "I really don’t like celebrating the Holi festival",
            "I don’t want to have fun during Holi",
            "I really don’t want to have fun during Holi",
            "I don’t want to have more fun during Holi",
            "I really don’t want to have any more fun during Holi"

    }, {"I love the Eid festival",
            "I really like celebrating the Eid festival",
            "I want to have fun during Eid",
            "I really want to have fun during Eid",
            "I want to have more fun during Eid",
            "I really want to have some more fun during Eid",
            "I don’t like the Eid festival",
            "I really don’t like celebrating the Eid festival",
            "I don’t want to have fun during Eid",
            "I really don’t want to have fun during Eid",
            "I don’t want to have more fun during Eid",
            "I really don’t want to have any more fun during Eid"

    }, {"I love the Good Friday festival",
            "I really like celebrating the Good Friday festival",
            "I want to have fun during Good Friday",
            "I really want to have fun during Good Friday",
            "I want to have more fun during Good Friday",
            "I really want to have some more fun during Good Friday",
            "I don’t like the Good Friday festival",
            "I really don’t like celebrating the Good Friday festival",
            "I don’t want to have fun during Good Friday",
            "I really don’t want to have fun during Good Friday",
            "I don’t want to have more fun during Good Friday",
            "I really don’t want to have any more fun during Good Friday"

    }, {"I love the गुडी पाडवा festival",
            "I really like celebrating the गुडी पाडवा festival",
            "I want to have fun during गुडी पाडवा",
            "I really want to have fun during गुडी पाडवा",
            "I want to have more fun during गुडी पाडवा",
            "I really want to have some more fun during गुडी पाडवा",
            "I don’t like the गुडी पाडवा festival",
            "I really don’t like celebrating the गुडी पाडवा festival",
            "I don’t want to have fun during गुडी पाडवा",
            "I really don’t want to have fun during गुडी पाडवा",
            "I don’t want to have more fun during गुडी पाडवा",
            "I really don’t want to have any more fun during गुडी पाडवा"

    }, {"I like the Republic day holiday",
            "I really like celebrating the Republic day holiday",
            "I want to enjoy the Republic day holiday",
            "I really want to enjoy the Republic day holiday",
            "I want to have more fun during the Republic day holiday",
            "I really want to have some more fun during the Republic day holiday",
            "I don’t like the Republic day holiday",
            "I really don’t like celebrating the Republic day holiday",
            "I don’t want to enjoy the Republic day holiday",
            "I really don’t want to enjoy the Republic day holiday",
            "I don’t want to have more fun during the Republic day holiday",
            "I really don't want to have any more fun during the Republic day holiday"

    }, {"I like the Independence day holiday",
            "I really like celebrating the Independence day holiday",
            "I want to enjoy the Independence day holiday",
            "I really want to enjoy the Independence day holiday",
            "I want to have more fun during the Independence day holiday",
            "I really want to have some more fun during the Independence day holiday",
            "I don’t like the Independence day holiday",
            "I really don’t like celebrating the Independence day holiday",
            "I don’t want to enjoy the Independence day holiday",
            "I really don’t want to enjoy the Independence day holiday",
            "I don’t want to have more fun during the Independence day holiday",
            "I really don't want to have any more fun during the Independence day holiday"
    }, {"I love New years",
            "I really like celebrating New years",
            "I want to have fun during New years",
            "I really want to have fun during New years",
            "I want to have more fun during New years",
            "I really want to have some more fun during New years",
            "I don’t like New years",
            "I really don’t like celebrating New years",
            "I don’t want to have fun during New years",
            "I really don’t want to have fun during New years",
            "I don’t want to have more fun during New years",
            "I really don’t want to have any more fun during New years"
    }}, {{"I like to celebrate my birthday",
            "I really like to celebrate my birthday",
            "I want to have fun on my birthday",
            "I really want to have fun on my birthday",
            "I want to have more fun on my birthday",
            "I really want to have some more fun on my birthday",
            "I don’t like to celebrate my birthday",
            "I really don’t like to celebrate my birthday",
            "I don’t want to have fun on my birthday",
            "I really don’t want to have fun on my birthday",
            "I don’t want to have more fun on my birthday",
            "I really don’t want to have any more fun on my birthday"
    }, {"I like to celebrate my mom’s birthday",
            "I really like to celebrate my mom’s birthday",
            "I want to have fun on my mom’s birthday",
            "I really want to have fun on my mom’s birthday",
            "I want to have more fun on my mom’s birthday",
            "I really want to have some more fun on my mom’s birthday",
            "I don’t like to celebrate my mom’s birthday",
            "I really don’t like to celebrate my mom’s birthday",
            "I don’t want to have fun on my mom’s birthday",
            "I really don’t want to have fun on my mom’s birthday",
            "I don’t want to have more fun on my mom’s birthday",
            "I really don’t want to have any more fun on my mom’s birthday"
    }, {"I like to celebrate my father’s birthday",
            "I really like to celebrate my father’s birthday",
            "I want to have fun on my father’s birthday",
            "I really want to have fun on my father’s birthday",
            "I want to have more fun on my father’s birthday",
            "I really want to have some more fun on my father’s birthday",
            "I don’t like to celebrate my father’s birthday",
            "I really don’t like to celebrate my father’s birthday",
            "I don’t want to have fun on my father’s birthday",
            "I really don’t want to have fun on my father’s birthday",
            "I don’t want to have more fun on my father’s birthday",
            "I really don’t want to have any more fun on my father’s birthday"
    }, {"I like to celebrate my brother’s birthday",
            "I really like to celebrate my brother’s birthday",
            "I want to have fun on my brother’s birthday",
            "I really want to have fun on my brother’s birthday",
            "I want to have more fun on my brother’s birthday",
            "I really want to have some more fun on my brother’s birthday",
            "I don’t like to celebrate my brother’s birthday",
            "I really don’t like to celebrate my brother’s birthday",
            "I don’t want to have fun on my brother’s birthday",
            "I really don’t want to have fun on my brother’s birthday",
            "I don’t want to have more fun on my brother’s birthday",
            "I really don’t want to have any more fun on my brother’s birthday"
    }, {"I like to celebrate my sister’s birthday",
            "I really like to celebrate my sister’s birthday",
            "I want to have fun on my sister’s birthday",
            "I really want to have fun on my sister’s birthday",
            "I want to have more fun on my sister’s birthday",
            "I really want to have some more fun on my sister’s birthday",
            "I don’t like to celebrate my sister’s birthday",
            "I really don’t like to celebrate my sister’s birthday",
            "I don’t want to have fun on my sister’s birthday",
            "I really don’t want to have fun on my sister’s birthday",
            "I don’t want to have more fun on my sister’s birthday",
            "I really don’t want to have any more fun on my sister’s birthday"
    }, {"I like to celebrate my friend’s birthday",
            "I really like to celebrate my friend’s birthday",
            "I want to have fun on my friend’s birthday",
            "I really want to have fun on my friend’s birthday",
            "I want to have more fun on my friend’s birthday",
            "I really want to have some more fun on my friend’s birthday",
            "I don’t like to celebrate my friend’s birthday",
            "I really don’t like to celebrate my friend’s birthday",
            "I don’t want to have fun on my friend’s birthday",
            "I really don’t want to have fun on my friend’s birthday",
            "I don’t want to have more fun on my friend’s birthday",
            "I really don’t want to have any more fun on my friend’s birthday"
    }, {"I like to celebrate my grandmother’s birthday",
            "I really like to celebrate my grandmother’s birthday",
            "I want to have fun on my grandmother’s birthday",
            "I really want to have fun on my grandmother’s birthday",
            "I want to have more fun on my grandmother’s birthday",
            "I really want to have some more fun on my grandmother’s birthday",
            "I don’t like to celebrate my grandmother’s birthday",
            "I really don’t like to celebrate my grandmother’s birthday",
            "I don’t want to have fun on my grandmother’s birthday",
            "I really don’t want to have fun on my grandmother’s birthday",
            "I don’t want to have more fun on my grandmother’s birthday",
            "I really don’t want to have any more fun on my grandmother’s birthday"
    }, {"I like to celebrate my grandfather’s birthday",
            "I really like to celebrate my grandfather’s birthday",
            "I want to have fun on my grandfather’s birthday",
            "I really want to have fun on my grandfather’s birthday",
            "I want to have more fun on my grandfather’s birthday",
            "I really want to have some more fun on my grandfather’s birthday",
            "I don’t like to celebrate my grandfather’s birthday",
            "I really don’t like to celebrate my grandfather’s birthday",
            "I don’t want to have fun on my grandfather’s birthday",
            "I really don’t want to have fun on my grandfather’s birthday",
            "I don’t want to have more fun on my grandfather’s birthday",
            "I really don’t want to have any more fun on my grandfather’s birthday"
    },  {"I like to celebrate my uncle’s birthday",
            "I really like to celebrate my uncle’s birthday",
            "I want to have fun on my uncle’s birthday",
            "I really want to have fun on my uncle’s birthday",
            "I want to have more fun on my uncle’s birthday",
            "I really want to have some more fun on my uncle’s birthday",
            "I don’t like to celebrate my uncle’s birthday",
            "I really don’t like to celebrate my uncle’s birthday",
            "I don’t want to have fun on my uncle’s birthday",
            "I really don’t want to have fun on my uncle’s birthday",
            "I don’t want to have more fun on my uncle’s birthday",
            "I really don’t want to have any more fun on my uncle’s birthday"
    }, {"I like to celebrate my aunt’s birthday",
            "I really like to celebrate my aunt’s birthday",
            "I want to have fun on my aunt’s birthday",
            "I really want to have fun on my aunt’s birthday",
            "I want to have more fun on my aunt’s birthday",
            "I really want to have some more fun on my aunt’s birthday",
            "I don’t like to celebrate my aunt’s birthday",
            "I really don’t like to celebrate my aunt’s birthday",
            "I don’t want to have fun on my aunt’s birthday",
            "I really don’t want to have fun on my aunt’s birthday",
            "I don’t want to have more fun on my aunt’s birthday",
            "I really don’t want to have any more fun on my aunt’s birthday"
    }, {"I like to celebrate my cousin’s birthday",
            "I really like to celebrate my cousin’s birthday",
            "I want to have fun on my cousin’s birthday",
            "I really want to have fun on my cousin’s birthday",
            "I want to have more fun on my cousin’s birthday",
            "I really want to have some more fun on my cousin’s birthday",
            "I don’t like to celebrate my cousin’s birthday",
            "I really don’t like to celebrate my cousin’s birthday",
            "I don’t want to have fun on my cousin’s birthday",
            "I really don’t want to have fun on my cousin’s birthday",
            "I don’t want to have more fun on my cousin’s birthday",
            "I really don’t want to have any more fun on my cousin’s birthday"
    }, {"I like to celebrate my teacher’s birthday",
            "I really like to celebrate my teacher’s birthday",
            "I want to have fun on my teacher’s birthday",
            "I really want to have fun on my teacher’s birthday",
            "I want to have more fun on my teacher’s birthday",
            "I really want to have some more fun on my teacher’s birthday",
            "I don’t like to celebrate my teacher’s birthday",
            "I really don’t like to celebrate my teacher’s birthday",
            "I don’t want to have fun on my teacher’s birthday",
            "I really don’t want to have fun on my teacher’s birthday",
            "I don’t want to have more fun on my teacher’s birthday",
            "I really don’t want to have any more fun on my teacher’s birthday"
    }}}, {{{}}}};

    public class ArrayIndexComparator implements Comparator<Integer> {
        private final Integer[] array;

        public ArrayIndexComparator(Integer[] array) {
            this.array = array;
        }

        public Integer[] createIndexArray() {
            Integer[] indexes = new Integer[array.length];

            for (int i = 0; i < array.length; i++) {
                indexes[i] = i; // Autoboxing
            }
            return indexes;
        }

        @Override
        public int compare(Integer index1, Integer index2) {
            // Autounbox from Integer to int to use as array indexes

            System.out.println("index1 " + index1);
            System.out.println("index2 " + index2);
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
            blowUp.inflate(R.menu.menu_1, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Layer3Activity.this, Setting.class);
                startActivity(intent);
                break;
            case R.id.info:
                Intent i = new Intent(Layer3Activity.this, About_Jellow.class);
                startActivity(i);
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Layer3Activity.this, Profile_form.class);
                startActivity(intent1);
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Layer3Activity.this, Feedback.class);
                startActivity(intent2);
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Layer3Activity.this, Tutorial.class);
                startActivity(intent3);
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Layer3Activity.this, Reset__preferences.class);
                startActivity(intent4);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Layer3Activity.this, Keyboard_Input.class);
                startActivity(intent6);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Layer3Activity.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}



