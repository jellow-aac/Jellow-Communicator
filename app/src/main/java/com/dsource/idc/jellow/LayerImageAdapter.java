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

/**
 * Created by Sumeet on 19-04-2016.
 */

public class LayerImageAdapter extends android.support.v7.widget.RecyclerView.Adapter<LayerImageAdapter.MyViewHolder> {
    private Context mContext;
    private int pos;
    public static Integer[] mThumbIds = new Integer[100];

    public static String[] belowText = new String[100];

    /*public static int people_more = 0;
    public static int places_more = 0;*/
    int j=0;
    private SessionManager session;
    TextView tv1,tv2,tv3, tv4,tv5,tv6, tv7,tv8,tv9;
    CircularImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;
    // Keep all Images in array
    public Integer[] learning = {
            R.drawable.level2_learning_animal, R.drawable.level2_learn_bodypart,
            R.drawable.level2_learn_books, R.drawable.level2_learn_colors,
            R.drawable.level2_learn_shapes,R.drawable.level2_learn_stationery, R.drawable.level2_learn_schoolobject2,
            R.drawable.level2_learn_homeobjects, R.drawable.level2_learn_transport,
    };

    public Integer[] eating = {
            R.drawable.level2_eat_breakfast, R.drawable.level2_eat_lunch,R.drawable.level2_eat_sweets,
            R.drawable.level2_eat_snacks, R.drawable.level2_eat_fruits,
            R.drawable.level2_eat_beverages,R.drawable.level2_eat_cutlery, R.drawable.level2_eat_addons
    };

    public Integer[] fun = {
            R.drawable.level2_fun_indoor_games1, R.drawable.level2_fun_outdoor_games,
            R.drawable.level2_fun_sports1, R.drawable.level2_fun_tv,
            R.drawable.level2_fun_music,R.drawable.level2_fun_activities
    };

    public Integer[] help = {
            R.drawable.level2_people_aboutme, R.drawable.level2_help_feelhurt,
            R.drawable.level2_help_feelsick, R.drawable.level2_help_feeltired,
            R.drawable.level2_help_helpmedo1,R.drawable.level2_help_medicine, R.drawable.level2_help_bandaid, R.drawable.water
    };

    public Integer[] time_weather = {
            R.drawable.level2_time_time, R.drawable.level2_time_day,
            R.drawable.level2_time_month, R.drawable.level2_time_weather,
            R.drawable.level2_time_season,R.drawable.level2_time_festivals, R.drawable.level2_time_birthday
    };

    public Integer[] time_weather_hindi = {
            R.drawable.level2_time_time, R.drawable.level2_time_day,
            R.drawable.month_level2_hindi, R.drawable.level2_time_weather,
            R.drawable.level2_time_season,R.drawable.level2_time_festivals, R.drawable.level2_time_birthday
    };

    public Integer[] greet_feel = {
            R.drawable.level2_time_greetings, R.drawable.level2_time_feelings,
            R.drawable.level2_time_request, R.drawable.level2_time_questions

    };

    public Integer[] daily_activities = {
            R.drawable.level2_dailyact_brush, R.drawable.level2_dailyact_toilet1,
            R.drawable.level2_dailyact_bathing, R.drawable.level2_dailyact_clothing1,
            R.drawable.level2_dailyact_getready,R.drawable.level2_dailyact_sleep, R.drawable.level2_dailyact_therapy,
            R.drawable.level2_dailyact_morning1,
            R.drawable.level2_dailyact_evening
    };
    /*public Integer[] people_english = {
            R.drawable.level2_people_mom, R.drawable.level2_people_dad,
            R.drawable.level2_people_brother, R.drawable.level2_people_sister,
            R.drawable.level2_people_grandfather,R.drawable.level2_people_grandmother, R.drawable.level2_people_uncle, R.drawable.level2_people_aunt, R.drawable.more,
            R.drawable.level2_people_cousin, R.drawable.level2_people_baby,
            R.drawable.level2_people_friend, R.drawable.level2_people_teacher,
            R.drawable.level2_people_doctor,R.drawable.level2_people_nurse, R.drawable.level2_people_caregiver1,
            R.drawable.level2_people_stranger, R.drawable.level2_people_aboutme
    };

    public Integer[] people_hindi = {
            R.drawable.level2_people_mom, R.drawable.level2_people_dad,
            R.drawable.level2_people_brother, R.drawable.level2_people_sister,R.drawable.level2_people_badepapa,R.drawable.level2_people_badimom,
            R.drawable.level2_people_grandfather,R.drawable.level2_people_grandmother, R.drawable.more, R.drawable.level2_people_nanaji, R.drawable.level2_people_nanima,R.drawable.level2_people_uncle, R.drawable.level2_people_aunt,
            R.drawable.level2_people_mama, R.drawable.level2_people_mami, R.drawable.level2_people_bua, R.drawable.level2_people_fufa, R.drawable.more, R.drawable.level2_people_mausi, R.drawable.level2_people_mausa,
            R.drawable.level2_people_baby,
            R.drawable.level2_people_friend, R.drawable.level2_people_teacher,
            R.drawable.level2_people_doctor,  R.drawable.level2_people_nurse, R.drawable.level2_people_caregiver1,R.drawable.more,
            R.drawable.level2_people_stranger, R.drawable.level2_people_aboutme
    };*/

    public static String[] learning_text =
            {"Animals & Birds...", "Body...", "Books...", "Colors...", "Shapes...", "Stationery...", "School Objects...", "Home Objects...", "Transport Modes..."};

    public static String[] eat_text =
            {"Breakfast...", "Lunch/Dinner...", "Sweets...", "Snacks...", "Fruits...", "Drinks...", "Cutlery...", "Add-ons..."};

    public static String[] fun_text =
            {"Indoor Games...", "Outdoor Games...", "Sports...", "TV...", "Music...", "Activities..."};

   /*public static String[] people_text =
            {"Mom", "Dad", "Brother", "Sister", "Grandfather", "Grandmother", "Uncle", "Aunt", "More", "Cousin", "Baby", "Friends", "Teacher", "Doctor", "Nurse", "Caregiver", "Stranger", "About Me"};

    public static String[] places_text =
            {"My House", "School", "Mall", "Museum", "Hotel", "Theater", "Playground", "Garden", "More", "Store", "Friend's House", "Relative's House", "Hospital", "Clinic", "Library", "Terrace"};
*/
    public static String[] time_weather_text =
            {"Time...", "Day...", "Month...", "Weather...", "Seasons...", "Festivals & Holidays...", "Birthdays..."};

    public static String[] greet_text =
            {"Greetings...", "Feelings...", "Requests...", "Questions..."};

    public static String[] daily_text =
            {"Brushing...", "Toilet...", "Bathing...", "Clothes & More...", "Getting Ready...", "Sleep...", "Therapy...", "Morning Routine...", "Bedtime Routine..."};

    public static String[] help_text =
            {"About Me", "I am hurt", "I feel sick", "I feel tired", "Help me do this", "Medicine", "Bandage", "Water"};

    public static String[] greet_text_hindi =
            {"शुभकामनाएं...", "भावना...", "बिनती...", "सवाल..."};
    public static String[] daily_text_hindi =
            {"दांत साफ़ करना...", "शौचालय...", "नहाना...", "कपड़े और सहायक चीज़ें...", "तैयार होना...", "नींद...", "उपचार...", "सुबह के नियमित कार्य...", "रात के नियमित कार्य..."};
    public static String[] eat_text_hindi =
            {"सुबह का नाश्ता...", "दोपहर/रात का भोजन...", "मिठाइयाँ...", "स्नैक्स...", "फल...", "ड्रिंक्स...", "कटलरी...", "ऍड-ऑन्स..."};
    public static String[] fun_text_hindi =
            {"घर के खेल...", "बाहरी खेल...", "खेलकूद...", "टीवी...", "संगीत...", "कार्य..."};
    public static String[] learning_text_hindi =
            {"पशु और पक्षी...", "शरीर...", "किताबें...", "रंग...", "आकार...", "स्टेशनरी...", "पाठशाला की वस्तुएं...", "घरेलु वस्तुएं...", "यात्रा के साधन..."};
    /*public static String[] people_text_hindi =
            {"माँ", "पिताजी", "भाई", "बहन", "बड़े पापा", "बड़ी मम्मी","दादाज़ी", "दादी माँ", "अधिक", "नानाज़ी","नानी माँ","चाचा", "चाची", "मामा","मामी" , "बुआ", "फ़ुफ़ा", "अधिक","मौसी", "मौसा", "नन्हा बच्चा", "दोस्त", "शिक्षक", "डॉक्टर","नर्स", "देख-रेख करने वाली मौसी", "अधिक","अजनबी", "मेरे बारे में"};
    public static String[] places_text_hindi =
            {"मेरा घर", "पाठशाला", "मॉल", "संग्रहालय", "होटल", "थिएटर", "खेल का मैदान", "बगीचा", "अधिक", "दुकान", "दोस्तों का घर", "रिश्तेदारों का घर", "अस्पताल", "क्लिनिक", "वाचनालय", "छत पर"};*/
    public static String[] time_weather_text_hindi =
            {"समय...", "दिन...", "महीना...", "मौसम...", "ऋतु...", "त्योहार और छुट्टी...", "जन्मदिन..."};
    public static String[] help_text_hindi =
            {"मेरे बारे में", "मैं घायल हूँ", "मेरी तबियत ठीक नहीं हैं", "मुझे थकावट लग रही हैं", "मुझे मदद करें", "दवाई", "बैंडेज", "पानी"};

int k=0,l=0,a=0,x=0;
    // Constructor
    public LayerImageAdapter(Context c, int posi, String[] temp, Integer[] image_temp){
        mContext = c;
        this.pos = posi;
        session = new SessionManager(mContext);
Log.d("possssiii",pos+"");
        if (pos == 0){
            if (session.getLanguage() == 0){
            mThumbIds = greet_feel;
            belowText = greet_text;
            } else {
                mThumbIds = greet_feel;
                belowText = greet_text_hindi;
            }
        } else if (pos == 1) {
            if (session.getLanguage() == 0){
            mThumbIds = daily_activities;
            belowText = daily_text;
            } else {
                mThumbIds = daily_activities;
                belowText = daily_text_hindi;
            }
        } else if (pos == 2) {
            if (session.getLanguage() == 0) {
                mThumbIds = eating;
                belowText = eat_text;
            } else {
                mThumbIds = eating;
                belowText = eat_text_hindi;
            }
        } else if (pos == 3) {
            if (session.getLanguage() == 0) {
                mThumbIds = fun;
                belowText = fun_text;
            }else {
                mThumbIds = fun;
                belowText = fun_text_hindi;
            }
        } else if (pos == 4) {
            if (session.getLanguage() == 0) {
            mThumbIds = learning;
            belowText = learning_text;
            }else {
                mThumbIds = learning;
                belowText = learning_text_hindi;
            }
        } /*else if (pos == 5) {
            if (session.getLanguage() == 0) {
                mThumbIds = image_temp;
                belowText = temp;

            }else {

                mThumbIds = image_temp;
                belowText = temp;
            }
            }

        else if (pos == 6) {
            System.out.println("eerr " + image_temp.length);
            if (places_more == 0) {
                mThumbIds = image_temp;
                belowText = temp;
            }
            else {
                mThumbIds = image_temp;
                belowText = temp;
            }
        }*/
        else if (pos == 7) {
            if (session.getLanguage() == 0)
            mThumbIds = time_weather;
            else
            mThumbIds = time_weather_hindi;
            if (session.getLanguage() == 0)
            belowText = time_weather_text;
            else
                belowText = time_weather_text_hindi;
        } else if (pos == 8) {
            mThumbIds = help;
            if (session.getLanguage() == 0)
            belowText = help_text;
            else
                belowText = help_text_hindi;
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Mukta-Regular.ttf");

        public MyViewHolder(final View view) {

            super(view);
if (session.getGridSize()==0) {
    img1 = (CircularImageView) view.findViewById(R.id.icon1);
    img2 = (CircularImageView) view.findViewById(R.id.icon2);
    img3 = (CircularImageView) view.findViewById(R.id.icon3);


    tv1 = (TextView) view.findViewById(R.id.te1);
    tv2 = (TextView) view.findViewById(R.id.te2);
    tv3 = (TextView) view.findViewById(R.id.te3);

    tv1.setTypeface(custom_font);
    tv1.setTextColor(Color.rgb(64, 64, 64));
    tv2.setTypeface(custom_font);
    tv2.setTextColor(Color.rgb(64, 64, 64));
    tv3.setTypeface(custom_font);
    tv3.setTextColor(Color.rgb(64, 64, 64));
}
if (session.getGridSize()==1)
{
    img1 = (CircularImageView) view.findViewById(R.id.icon1);
    img2 = (CircularImageView) view.findViewById(R.id.icon2);
    img3 = (CircularImageView) view.findViewById(R.id.icon3);
    img4 = (CircularImageView) view.findViewById(R.id.icon4);
    img5 = (CircularImageView) view.findViewById(R.id.icon5);
    img6 = (CircularImageView) view.findViewById(R.id.icon6);
    img7 = (CircularImageView) view.findViewById(R.id.icon7);
    img8 = (CircularImageView) view.findViewById(R.id.icon8);
    img9 = (CircularImageView) view.findViewById(R.id.icon9);

    tv1 = (TextView) view.findViewById(R.id.te1);
    tv2 = (TextView) view.findViewById(R.id.te2);
    tv3 = (TextView) view.findViewById(R.id.te3);
    tv4 = (TextView) view.findViewById(R.id.te4);
    tv5 = (TextView) view.findViewById(R.id.te5);
    tv6 = (TextView) view.findViewById(R.id.te6);
    tv7 = (TextView) view.findViewById(R.id.te7);
    tv8 = (TextView) view.findViewById(R.id.te8);
    tv9 = (TextView) view.findViewById(R.id.te9);

    tv1.setTypeface(custom_font);
    tv1.setTextColor(Color.rgb(64, 64, 64));
    tv2.setTypeface(custom_font);
    tv2.setTextColor(Color.rgb(64, 64, 64));
    tv3.setTypeface(custom_font);
    tv3.setTextColor(Color.rgb(64, 64, 64));
    tv4.setTypeface(custom_font);
    tv4.setTextColor(Color.rgb(64, 64, 64));
    tv5.setTypeface(custom_font);
    tv5.setTextColor(Color.rgb(64, 64, 64));
    tv6.setTypeface(custom_font);
    tv6.setTextColor(Color.rgb(64, 64, 64));
    tv7.setTypeface(custom_font);
    tv7.setTextColor(Color.rgb(64, 64, 64));
    tv8.setTypeface(custom_font);
    tv8.setTextColor(Color.rgb(64, 64, 64));
    tv9.setTypeface(custom_font);
    tv9.setTextColor(Color.rgb(64, 64, 64));

}
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        return new LayerImageAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        j=mThumbIds.length;
        System.out.println("COUNTER "+position);
        int h = 1, q = 0;
if (session.getGridSize()==0) {
    if (position == 0) {
        tv1.setText(belowText[0]);
        tv2.setText(belowText[1]);
        tv3.setText(belowText[2]);
        img1.setImageResource(mThumbIds[0]);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 0;
                handleItemClick(belowText[0], 0);
                putpos(belowText[0]);
            }
        });
        img2.setImageResource(mThumbIds[1]);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 1;
                handleItemClick(belowText[1], 1);
                putpos(belowText[1]);
            }
        });

        img3.setImageResource(mThumbIds[2]);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 1;
                handleItemClick(belowText[2], 2);
                putpos(belowText[2]);
            }
        });

        System.out.print("ssssssssssss" + j);
    }
    j = j - 3;
    System.out.print("cccccccccc" + j);
    if (position == 1) {
        {
            System.out.print("YYYYY" + j);
            if (j > 0) {
                System.out.print("zzzzzz" + j);
                tv1.setText(belowText[3]);
                img1.setImageResource(mThumbIds[3]);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        x = 3;
                        handleItemClick(belowText[3], 3);
                        putpos(belowText[3]);
                    }
                });
                j = j - 1;
                System.out.print("aaaaaaaaaaa" + j);
            }
        }
        if (j > 0) {
            tv2.setText(belowText[4]);
            img2.setImageResource(mThumbIds[4]);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x = 4;
                    handleItemClick(belowText[4], 4);
                    putpos(belowText[4]);
                }
            });

            j = j - 1;
        } else {

            tv2.setText(" ");
            img2.setImageResource(R.drawable.background_circle);
            //    img2.setAlpha(0.5f);
            img2.setEnabled(false);
        }
        if (j > 0) {
            tv3.setText(belowText[5]);
            img3.setImageResource(mThumbIds[5]);
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x = 5;
                    handleItemClick(belowText[5], 5);
                    putpos(belowText[5]);
                }
            });

            j = j - 1;
        } else {

            tv3.setText(" ");
            img3.setImageResource(R.drawable.background_circle);
            //   img3.setAlpha(0.5f);
            img3.setEnabled(false);
        }
    }

    j = j - 3;
    if (position == 2) {
        k = 6;
        System.out.print("jjjjjjjjj" + j);
        if (j > 0) {
            tv1.setText(belowText[6]);
            img1.setImageResource(mThumbIds[6]);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x = 6;
                    handleItemClick(belowText[6], 6);
                    putpos(belowText[6]);
                }
            });

            j = j - 1;
            System.out.print("DDDDD" + j);
        }
        if (j > 0) {
            tv2.setText(belowText[7]);
            img2.setImageResource(mThumbIds[7]);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x = 7;
                    handleItemClick(belowText[7], 7);
                    putpos(belowText[7]);
                }
            });

            j = j - 1;
            System.out.print("QQQQQ" + j);
        } else {

            tv2.setText("");
            img2.setImageResource(R.drawable.background_circle);
            img2.setEnabled(false);
        }

        if (j > 0) {
            tv3.setText(belowText[8]);
            img3.setImageResource(mThumbIds[8]);
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    x = 8;
                    handleItemClick(belowText[8], 8);
                    putpos(belowText[8]);
                }
            });

        } else {
            tv3.setText(" ");
            img3.setImageResource(R.drawable.background_circle);
            img3.setEnabled(false);
        }

    }
}
if (session.getGridSize()==1)
{
    j=mThumbIds.length;
    img1.setImageResource(mThumbIds[0]);
    img2.setImageResource(mThumbIds[1]);
    img3.setImageResource(mThumbIds[2]);
    tv1.setText(belowText[0]);
    tv2.setText(belowText[1]);
    tv3.setText(belowText[2]);
    j=j-3;

    if(j>0) {
        img4.setImageResource(mThumbIds[3]);
        tv4.setText(belowText[3]);
        j=j-1;
    }
    else
    {
        img4.setImageResource(R.drawable.background_circle);
        tv4.setText("");

    }

    if(j>0) {
        img5.setImageResource(mThumbIds[4]);
        tv5.setText(belowText[4]);
        j=j-1;
    }
    else
    {
        img5.setImageResource(R.drawable.background_circle);
        tv5.setText("");

    }
    if(j>0) {
        img6.setImageResource(mThumbIds[5]);
        tv6.setText(belowText[5]);
        j=j-1;
    }
    else
    {
        img6.setImageResource(R.drawable.background_circle);
        tv6.setText("");

    }
    if(j>0) {
        img7.setImageResource(mThumbIds[6]);
        tv7.setText(belowText[6]);
        j=j-1;
    }
    else
    {
        img7.setImageResource(R.drawable.background_circle);
        tv7.setText("");

    }
    if(j>0) {
        img8.setImageResource(mThumbIds[7]);
        tv8.setText(belowText[7]);
        j=j-1;
    }
    else
    {
        img8.setImageResource(R.drawable.background_circle);
        tv8.setText("");

    }
    if(j>0) {
        img9.setImageResource(mThumbIds[8]);
        tv9.setText(belowText[8]);
        j=j-1;
    }
    else
    {
        img9.setImageResource(R.drawable.background_circle);
        tv9.setText("");

    }


}




        Log.d("trueee",x+"");


/*        if (session.getGridSize()==1){
            if (position == 0) {
                tv1.setText(belowText[0]);
                tv2.setText(belowText[1]);
                tv3.setText(belowText[2]);
                tv4.setText(belowText[3]);
                tv5.setText(belowText[4]);
                tv6.setText(belowText[5]);
                tv7.setText(belowText[6]);
                tv8.setText(belowText[7]);
                tv9.setText(belowText[8]);
                img1.setImageResource(mThumbIds[0]);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        x=0;
                        handleItemClick(belowText[0], 0);
                        putpos(belowText[0]);
                    }
                });
                img2.setImageResource(mThumbIds[1]);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        x=1;
                        handleItemClick(belowText[1], 1);
                        putpos(belowText[1]);
                    }
                });

                img3.setImageResource(mThumbIds[2]);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        x=1;
                        handleItemClick(belowText[2], 2);
                        putpos(belowText[2]);
                    }
                });

                System.out.print("ssssssssssss"+j);
            }
            j=j-3;
            System.out.print("cccccccccc"+j);
*//*            if (position == 1) {
                {
                    System.out.print("YYYYY"+j);
                    if(j>0) {
                        System.out.print("zzzzzz"+j);
                        tv1.setText(belowText[3]);
                        img1.setImageResource(mThumbIds[3]);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                x=3;
                                handleItemClick(belowText[3], 3);
                                putpos(belowText[3]);
                            }
                        });
                        j = j - 1;
                        System.out.print("aaaaaaaaaaa" + j);
                    }
                }
                if (j > 0) {
                    tv2.setText(belowText[4]);
                    img2.setImageResource(mThumbIds[4]);
                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            x=4;
                            handleItemClick(belowText[4], 4);
                            putpos(belowText[4]);
                        }
                    });

                    j=j-1;
                }
                else
                {

                    tv2.setText(" ");
                    img2.setImageResource(R.drawable.background_circle);
                    img2.setAlpha(0.5f);
                    img2.setEnabled(false);
                }
                if (j > 0) {
                    tv3.setText(belowText[5]);
                    img3.setImageResource(mThumbIds[5]);
                    img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            x=5;
                            handleItemClick(belowText[5], 5);
                            putpos(belowText[5]);
                        }
                    });

                    j=j-1;
                }
                else
                {

                    tv3.setText(" ");
                    img3.setImageResource(R.drawable.background_circle);
                    img3.setAlpha(0.5f);
                    img3.setEnabled(false);
                }
            }

            j=j-3;
            if (position == 2) {
                k = 6;
                System.out.print("jjjjjjjjj"+j);
                if(j>0) {
                    tv1.setText(belowText[6]);
                    img1.setImageResource(mThumbIds[6]);
                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            x=6;
                            handleItemClick(belowText[6], 6);
                            putpos(belowText[6]);
                        }
                    });

                    j = j - 1;
                    System.out.print("DDDDD"+j);
                }
                if(j>0) {
                    tv2.setText(belowText[7]);
                    img2.setImageResource(mThumbIds[7]);
                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            x=7;
                            handleItemClick(belowText[7], 7);
                            putpos(belowText[7]);
                        }
                    });

                    j=j-1;
                    System.out.print("QQQQQ"+j);
                }
                else
                {

                    tv2.setText(null);
                    img2.setImageResource(0);
                }

                if(j>0) {
                    tv3.setText(belowText[8]);
                    img3.setImageResource(mThumbIds[8]);
                    img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            x=8;
                            handleItemClick(belowText[8], 8);
                            putpos(belowText[8]);
                        }
                    });

                }
                else
                {
                    tv3.setText(" ");
                    img3.setImageResource(R.drawable.background_circle);
                    img3.setEnabled(false);
                }

            }*//*
            Log.d("trueee",x+"");
        }*/
    }

    @Override
    public int getItemCount() {
        System.out.println("MTHUMBSID"+(mThumbIds.length));
        if (session.getGridSize()==0) {
            k = (mThumbIds.length % 3);
            System.out.println("kkkkkkkk" + k);
            l = (mThumbIds.length / 3);
            System.out.println("llllll" + l);
            if (k > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);
        }
        if (session.getGridSize()==1) {
            k = (mThumbIds.length % 9);
            System.out.println("kkkkkkkk" + k);
            l = (mThumbIds.length / 9);
            System.out.println("llllll" + l);
            if (k > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);
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