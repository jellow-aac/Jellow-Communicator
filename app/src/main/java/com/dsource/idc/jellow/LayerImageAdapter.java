package com.dsource.idc.jellow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;

/**
 * Created by Sumeet on 19-04-2016.
 */
public class LayerImageAdapter extends android.support.v7.widget.RecyclerView.Adapter<LayerImageAdapter.MyViewHolder> {
    private static final int LANG_ENG = 0, LANG_HINDI = 1, GRID_1BY3 = 0, GRID_3BY3 = 1, MODE_PICTURE_ONLY = 1;
    private Context mContext;
    private SessionManager mSession;
    private EvaluateDisplayMetricsUtils mMetricsUtils;

    private Integer[] mThumbIds = new Integer[100];
    private String[] belowText = new String[100];

    // Keep all Images in array
    private final Integer[] learning = {
            R.drawable.level2_learning_animal, R.drawable.level2_learn_bodypart,
            R.drawable.level2_learn_books, R.drawable.level2_learn_colors,
            R.drawable.level2_learn_shapes,R.drawable.level2_learn_stationery, R.drawable.level2_learn_schoolobject2,
            R.drawable.level2_learn_homeobjects, R.drawable.level2_learn_transport,
    };

    private final Integer[] eating = {
            R.drawable.level2_eat_breakfast, R.drawable.level2_eat_lunch,R.drawable.level2_eat_sweets,
            R.drawable.level2_eat_snacks, R.drawable.level2_eat_fruits,
            R.drawable.level2_eat_beverages,R.drawable.level2_eat_cutlery, R.drawable.level2_eat_addons
    };

    private final Integer[] fun = {
            R.drawable.level2_fun_indoor_games1, R.drawable.level2_fun_outdoor_games,
            R.drawable.level2_fun_sports1, R.drawable.level2_fun_tv,
            R.drawable.level2_fun_music,R.drawable.level2_fun_activities
    };

    private final Integer[] help = {
            R.drawable.level2_people_aboutme, R.drawable.level2_help_feelhurt,
            R.drawable.level2_help_feelsick, R.drawable.level2_help_feeltired,
            R.drawable.level2_help_helpmedo1,R.drawable.level2_help_medicine, R.drawable.level2_help_bandaid, R.drawable.water
    };

    private final Integer[] time_weather = {
            R.drawable.level2_time_time, R.drawable.level2_time_day,
            R.drawable.level2_time_month, R.drawable.level2_time_weather,
            R.drawable.level2_time_season,R.drawable.level2_time_festivals, R.drawable.level2_time_birthday
    };

    private final Integer[] time_weather_hindi = {
            R.drawable.level2_time_time, R.drawable.level2_time_day,
            R.drawable.month_level2_hindi, R.drawable.level2_time_weather,
            R.drawable.level2_time_season,R.drawable.level2_time_festivals, R.drawable.level2_time_birthday
    };

    private final Integer[] greet_feel = {
            R.drawable.level2_time_greetings, R.drawable.level2_time_feelings,
            R.drawable.level2_time_request, R.drawable.level2_time_questions

    };

    private final Integer[] daily_activities = {
            R.drawable.level2_dailyact_brush, R.drawable.level2_dailyact_toilet1,
            R.drawable.level2_dailyact_bathing, R.drawable.level2_dailyact_clothing1,
            R.drawable.level2_dailyact_getready,R.drawable.level2_dailyact_sleep, R.drawable.level2_dailyact_therapy,
            R.drawable.level2_dailyact_morning1,
            R.drawable.level2_dailyact_evening
    };

    private final String[] learning_text =
            {"Animals & Birds...", "Body...", "Books...", "Colors...", "Shapes...", "Stationery...", "School Objects...", "Home Objects...", "Transport Modes..."};

    private final String[] eat_text =
            {"Breakfast...", "Lunch/Dinner...", "Sweets...", "Snacks...", "Fruits...", "Drinks...", "Cutlery...", "Add-ons..."};

    private final String[] fun_text =
            {"Indoor Games...", "Outdoor Games...", "Sports...", "TV...", "Music...", "Activities..."};

    private final String[] time_weather_text =
            {"Time...", "Day...", "Month...", "Weather...", "Seasons...", "Festivals & Holidays...", "Birthdays..."};

    private final String[] greet_text =
            {"Greetings...", "Feelings...", "Requests...", "Questions..."};

    private final String[] daily_text =
            {"Brushing...", "Toilet...", "Bathing...", "Clothes & More...", "Getting Ready...", "Sleep...", "Therapy...", "Morning Routine...", "Bedtime Routine..."};

    private final String[] help_text =
            {"About Me", "I am hurt", "I feel sick", "I feel tired", "Help me do this", "Medicine", "Bandage", "Water"};

    private final String[] greet_text_hindi =
            {"शुभकामनाएं...", "भावना...", "बिनती...", "सवाल..."};
    private final String[] daily_text_hindi =
            {"दांत साफ़ करना...", "शौचालय...", "नहाना...", "कपड़े और सहायक चीज़ें...", "तैयार होना...", "नींद...", "उपचार...", "सुबह के नियमित कार्य...", "रात के नियमित कार्य..."};
    private final String[] eat_text_hindi =
            {"सुबह का नाश्ता...", "दोपहर/रात का भोजन...", "मिठाइयाँ...", "स्नैक्स...", "फल...", "ड्रिंक्स...", "कटलरी...", "ऐड-ऑन्स..."};
    private final String[] fun_text_hindi =
            {"घर के खेल...", "बाहरी खेल...", "खेलकूद...", "टीवी...", "संगीत...", "कार्य..."};
    private final String[] learning_text_hindi =
            {"पशु और पक्षी...", "शरीर...", "किताबें...", "रंग...", "आकार...", "स्टेशनरी...", "पाठशाला की वस्तुएं...", "घरेलु वस्तुएं...", "यात्रा के साधन..."};
    private final String[] time_weather_text_hindi =
            {"समय...", "दिन...", "महीना...", "मौसम...", "ऋतु...", "त्योहार और छुट्टी...", "जन्मदिन..."};
    private final String[] help_text_hindi =
            {"मेरे बारे में", "मैं घायल हूँ", "मेरी तबियत ठीक नहीं हैं", "मुझे थकावट लग रही हैं", "मुझे मदद करें", "दवाई", "बैंडेज", "पानी"};

    public LayerImageAdapter(Context context, int levelTwoItemPos){
        this.mContext = context;
        this.mSession = new SessionManager(mContext);
        this.mMetricsUtils = new EvaluateDisplayMetricsUtils(mContext);

        if (levelTwoItemPos == 0){
            if (mSession.getLanguage() == LANG_ENG){
                mThumbIds = greet_feel;
                belowText = greet_text;
            } else {
                mThumbIds = greet_feel;
                belowText = greet_text_hindi;
            }
        } else if (levelTwoItemPos == 1) {
            if (mSession.getLanguage() == LANG_ENG){
                mThumbIds = daily_activities;
                belowText = daily_text;
            } else {
                mThumbIds = daily_activities;
                belowText = daily_text_hindi;
            }
        } else if (levelTwoItemPos == 2) {
            if (mSession.getLanguage() == LANG_ENG) {
                mThumbIds = eating;
                belowText = eat_text;
            } else {
                mThumbIds = eating;
                belowText = eat_text_hindi;
            }
        } else if (levelTwoItemPos == 3) {
            if (mSession.getLanguage() == LANG_ENG) {
                mThumbIds = fun;
                belowText = fun_text;
            }else {
                mThumbIds = fun;
                belowText = fun_text_hindi;
            }
        } else if (levelTwoItemPos == 4) {
            if (mSession.getLanguage() == LANG_ENG) {
                mThumbIds = learning;
                belowText = learning_text;
            }else {
                mThumbIds = learning;
                belowText = learning_text_hindi;
            }
        }
        else if (levelTwoItemPos == 7) {
            if (mSession.getLanguage() == LANG_ENG)
                mThumbIds = time_weather;
            else
                mThumbIds = time_weather_hindi;
            if (mSession.getLanguage() == LANG_ENG)
                belowText = time_weather_text;
            else
                belowText = time_weather_text_hindi;
        } else if (levelTwoItemPos == 8) {
                mThumbIds = help;
            if (mSession.getLanguage() == LANG_ENG)
                belowText = help_text;
            else
                belowText = help_text_hindi;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemBelowText;
        private CircularImageView menuItemImage;
        private LinearLayout menuItemLinearLayout;

        public MyViewHolder(final View view) {
            super(view);
            menuItemImage = (CircularImageView) view.findViewById(R.id.icon1);
            menuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = (TextView) view.findViewById(R.id.te1);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mukta-Regular.ttf");
            menuItemBelowText.setTypeface(font,  Typeface.BOLD);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        return new LayerImageAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mSession.getGridSize() == GRID_1BY3) {
            if (mSession.getScreenHeight() >= 720) {
                params.setMargins(mMetricsUtils.getPixelsFromDpVal(36), mMetricsUtils.getPixelsFromDpVal(180), 0, mMetricsUtils.getPixelsFromDpVal(180));
                holder.menuItemBelowText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if(mSession.getLanguage() == LANG_HINDI) holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                else    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            }else if (mSession.getScreenWidth() > 640 && mSession.getScreenWidth() <= 1024) {
                params.setMargins(mMetricsUtils.getPixelsFromDpVal(20), mMetricsUtils.getPixelsFromDpVal(124), 0, mMetricsUtils.getPixelsFromDpVal(124));
                holder.menuItemBelowText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if(mSession.getLanguage() == LANG_HINDI) holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                else    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            }else {
                params.setMargins(mMetricsUtils.getPixelsFromDpVal(12), mMetricsUtils.getPixelsFromDpVal(92), 0, mMetricsUtils.getPixelsFromDpVal(92));
                holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            }
        }else if(mSession.getGridSize() == GRID_3BY3){
            if (mSession.getScreenHeight() >= 720) {
                holder.menuItemImage.setLayoutParams(new LinearLayout.LayoutParams(mMetricsUtils.getPixelsFromDpVal(124), mMetricsUtils.getPixelsFromDpVal(124)));
                if(mSession.getLanguage() == LANG_HINDI) {
                    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(16), mMetricsUtils.getPixelsFromDpVal(16), 0, mMetricsUtils.getPixelsFromDpVal(-8));
                }else{
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(36), mMetricsUtils.getPixelsFromDpVal(16), 0, mMetricsUtils.getPixelsFromDpVal(-7));
                }
            }else if (mSession.getScreenWidth() > 640 && mSession.getScreenWidth() <= 1024) {
                holder.menuItemImage.setLayoutParams(new LinearLayout.LayoutParams(mMetricsUtils.getPixelsFromDpVal(86), mMetricsUtils.getPixelsFromDpVal(86)));
                if(mSession.getLanguage() == LANG_HINDI) {
                    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(16), mMetricsUtils.getPixelsFromDpVal(1), 0, mMetricsUtils.getPixelsFromDpVal(8));
                }else{
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(16), mMetricsUtils.getPixelsFromDpVal(1), 0, mMetricsUtils.getPixelsFromDpVal(16));
                }
            }else {
                if(mSession.getLanguage() == LANG_HINDI) {
                    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(16), mMetricsUtils.getPixelsFromDpVal(0), 0, mMetricsUtils.getPixelsFromDpVal(-3));
                }else {
                    params.setMargins(mMetricsUtils.getPixelsFromDpVal(12), mMetricsUtils.getPixelsFromDpVal(0), 0, mMetricsUtils.getPixelsFromDpVal(0));
                    holder.menuItemBelowText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                }
                if (position == mThumbIds.length-1)  holder.menuItemLinearLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //Resolve black circular imageview issue.
            }
        }

        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemLinearLayout.setLayoutParams(params);
            holder.menuItemBelowText.setText(belowText[position]);

        holder.menuItemImage.setImageResource(mThumbIds[position]);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {}
        });
    }

    @Override
    public int getItemCount() {
        return mThumbIds.length;
    }
}