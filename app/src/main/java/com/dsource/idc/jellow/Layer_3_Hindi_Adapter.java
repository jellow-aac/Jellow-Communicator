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
import com.dsource.idc.jellow.Utility.SessionManager;

import java.util.Arrays;

/**
 * Created by ekalpa on 7/14/2016.
 */
class Layer_3_Hindi_Adapter extends android.support.v7.widget.RecyclerView.Adapter<Layer_3_Hindi_Adapter.MyViewHolder> {
    private Context mContext;
    private SessionManager mSession;
    private EvaluateDisplayMetricsUtils mMetricsUtils;
    private Integer[] mThumbIds = new Integer[100];
    private String[] belowText = new String[100];

    Layer_3_Hindi_Adapter(Context context, int levelOneITemPos, int levelTwoItemPos, int sort[]) {
        mContext = context;
        mSession = new SessionManager(mContext);
        mMetricsUtils = new EvaluateDisplayMetricsUtils(mContext);
        int store = 0;
        if(levelOneITemPos == 0) {
            if (levelTwoItemPos == 0) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_greetings.length; j++) {
                    temp1[j] = greet_feel_greetings[sort[j]];
                    temp2[j] = greet_feel_greetings_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 1) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_feelings.length; j++) {
                    temp1[j] = greet_feel_feelings[sort[j]];
                    temp2[j] = greet_feel_feelings_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_requests.length; j++) {
                    temp1[j] = greet_feel_requests[sort[j]];
                    temp2[j] = greet_feel_requests_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< greet_feel_questions.length; j++) {
                    temp1[j] = greet_feel_questions[sort[j]];
                    temp2[j] = greet_feel_questions_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }else if(levelOneITemPos == 1) {
            if (levelTwoItemPos == 0) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_brushing.length; j++) {
                    temp1[j] = daily_activities_brushing[sort[j]];
                    temp2[j] = daily_activities_brushing_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 1) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_toilet.length; j++) {
                    temp1[j] = daily_activities_toilet[sort[j]];
                    temp2[j] = daily_activities_toilet_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_bathing.length; j++) {
                    temp1[j] = daily_activities_bathing[sort[j]];
                    temp2[j] = daily_activities_bathing_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_clothes_access.length; j++) {
                    temp1[j] = daily_activities_clothes_access[sort[j]];
                    temp2[j] = daily_activities_clothes_access_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 4) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_get_ready.length; j++) {
                    temp1[j] = daily_activities_get_ready[sort[j]];
                    temp2[j] = daily_activities_get_ready_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 5) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_sleep.length; j++) {
                    temp1[j] = daily_activities_sleep[sort[j]];
                    temp2[j] = daily_activities_sleep_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 6) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_therapy.length; j++) {
                    temp1[j] = daily_activities_therapy[sort[j]];
                    temp2[j] = daily_activities_therapy_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if (levelTwoItemPos == 7) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_morning_schedule.length; j++) {
                    temp1[j] = daily_activities_morning_schedule[sort[j]];
                    temp2[j] = daily_activities_morning_schedule_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if (levelTwoItemPos == 8) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< daily_activities_bedtime_schedule.length; j++) {
                    temp1[j] = daily_activities_bedtime_schedule[sort[j]];
                    temp2[j] = daily_activities_bedtime_schedule_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }else if(levelOneITemPos == 2){
            if(levelTwoItemPos == 0){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_foods_drinks_breakfast.length; j++) {
                    temp1[j] = eating_foods_drinks_breakfast[sort[j]];
                    temp2[j] = foods_drinks_breakfast_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 1){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_lunch_dinner.length; j++) {
                    temp1[j] = eating_food_drinks_lunch_dinner[sort[j]];
                    temp2[j] = food_drinks_lunch_dinner_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            } else if(levelTwoItemPos == 2){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_sweets.length; j++) {
                    temp1[j] = eating_food_drinks_sweets[sort[j]];
                    temp2[j] = food_drinks_sweets_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 3){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_snacks.length; j++) {
                    temp1[j] = eating_food_drinks_snacks[sort[j]];
                    temp2[j] = food_drinks_snacks_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 4){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_fruits.length; j++) {
                    temp1[j] = eating_food_drinks_fruits[sort[j]];
                    temp2[j] = food_drinks_fruits_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 5){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_drinks.length; j++) {
                    temp1[j] = eating_food_drinks_drinks[sort[j]];
                    temp2[j] = food_drinks_drinks_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 6){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_cutlery.length; j++) {
                    temp1[j] = eating_food_drinks_cutlery[sort[j]];
                    temp2[j] = food_drinks_cutlery_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 7){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< eating_food_drinks_add_ons.length; j++) {
                    temp1[j] = eating_food_drinks_add_ons[sort[j]];
                    temp2[j] = food_drinks_add_ons_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }else if(levelOneITemPos == 3){
            if(levelTwoItemPos == 0){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_indoor_games.length; j++) {
                    temp1[j] = fun_indoor_games[sort[j]];
                    temp2[j] = fun_indoor_games_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 1){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_outdoor_games.length; j++) {
                    temp1[j] = fun_outdoor_games[sort[j]];
                    temp2[j] = fun_outdoor_games_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 2){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_sports.length; j++) {
                    temp1[j] = fun_sports[sort[j]];
                    temp2[j] = fun_sports_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 3){
                mThumbIds = fun_tv;
                belowText = fun_tv_text;
            }else if(levelTwoItemPos == 4){
                mThumbIds = fun_music;
                belowText = fun_music_text;
            }else if(levelTwoItemPos == 5){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_activities.length; j++) {
                    temp1[j] = fun_activities[sort[j]];
                    temp2[j] = fun_activities_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }else if(levelOneITemPos ==4){
            if(levelTwoItemPos == 0){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_animals_birds.length; j++) {
                    temp1[j] = learning_animals_birds[sort[j]];
                    temp2[j] = learning_animals_birds_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 1){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_body_parts.length; j++) {
                    temp1[j] = learning_body_parts[sort[j]];
                    temp2[j] = learning_body_parts_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 2) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_books.length; j++) {
                    temp1[j] = learning_books[sort[j]];
                    temp2[j] = learning_books_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 3) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_colours.length; j++) {
                    temp1[j] = learning_colours[sort[j]];
                    temp2[j] = learning_colours_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 4) {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_shapes.length; j++) {
                    temp1[j] = learning_shapes[sort[j]];
                    temp2[j] = learning_shapes_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 5){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_stationary.length; j++) {
                    temp1[j] = learning_stationary[sort[j]];
                    temp2[j] = learning_stationary_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 6){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_school_objects.length; j++) {
                    temp1[j] = learning_school_objects[sort[j]];
                    temp2[j] = learning_school_objects_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 7){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_home_objects.length; j++) {
                    temp1[j] = learning_home_objects[sort[j]];
                    temp2[j] = learning_home_objects_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 8){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_transportation.length; j++) {
                    temp1[j] = learning_transportation[sort[j]];
                    temp2[j] = learning_transportation_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }else if(levelOneITemPos == 7){
            if(levelTwoItemPos == 0){
                mThumbIds = time_weather_time;
                belowText = time_weather_time_text;
            }else if(levelTwoItemPos == 1){
                mThumbIds = time_weather_day;
                belowText = time_weather_day_text;
            }else if(levelTwoItemPos == 2){
                mThumbIds = time_weather_month;
                belowText = time_weather_month_text;
            }else if(levelTwoItemPos == 3){
                mThumbIds = time_weather_weather;
                belowText = time_weather_weather_text;
            }else if(levelTwoItemPos == 4){
                mThumbIds = time_weather_seasons;
                belowText = time_weather_seasons_text;
            }else if(levelTwoItemPos == 5){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< time_weather_holidays_festivals.length; j++) {
                    temp1[j] = time_weather_holidays_festivals[sort[j]];
                    temp2[j] = time_weather_holidays_festivals_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else if(levelTwoItemPos == 6){
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< time_weather_birthdays.length; j++) {
                    temp1[j] = time_weather_birthdays[sort[j]];
                    temp2[j] = time_weather_brthdays_text[sort[j]];
                    store = j;
                }
                store = store +1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
        }
    }

    @Override
    public Layer_3_Hindi_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        return new Layer_3_Hindi_Adapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(Layer_3_Hindi_Adapter.MyViewHolder holder, int position) {
        final int LANG_HINDI = 1, GRID_1BY3 = 0, GRID_3BY3 = 1, MODE_PICTURE_ONLY = 1;
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mThumbIds.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemBelowText;
        private CircularImageView menuItemImage;
        private LinearLayout menuItemLinearLayout;

        MyViewHolder(final View view) {
            super(view);
            menuItemImage = (CircularImageView) view.findViewById(R.id.icon1);
            menuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = (TextView) view.findViewById(R.id.te1);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mukta-Regular.ttf");
            menuItemBelowText.setTypeface(font, Typeface.BOLD);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
        }
    }

    private final Integer[] greet_feel_greetings = {
            R.drawable.thankyou, R.drawable.thankyou,
            R.drawable.bye, R.drawable.goodmorning,
            R.drawable.goodafternoon, R.drawable.goodevening, R.drawable.goodnight, R.drawable.highfive,
            R.drawable.nicetomeetyou, R.drawable.howareyou, R.drawable.howwasyourday, R.drawable.howareyou
    };

    private final Integer[] greet_feel_feelings = {
            R.drawable.happy, R.drawable.sad,
            R.drawable.angry, R.drawable.scared,
            R.drawable.surprised, R.drawable.irritated, R.drawable.confused, R.drawable.ashamed,
            R.drawable.disappointed, R.drawable.bored,
            R.drawable.worried, R.drawable.stressed,
            R.drawable.tired, R.drawable.hot, R.drawable.cold, R.drawable.sick, R.drawable.hurt
    };

    private final Integer[] greet_feel_requests = {
            R.drawable.please, R.drawable.thankyou,
            R.drawable.welcome, R.drawable.pleasegiveme,
            R.drawable.pleasetellmeagain, R.drawable.pleaseshowme, R.drawable.ineedabreak, R.drawable.iamalldone,
            R.drawable.excuseme,
            R.drawable.iamsorry, R.drawable.idontunderstand,
            R.drawable.pleaseshare, R.drawable.pleaseslowdown, R.drawable.ineedhelp, R.drawable.comehere, R.drawable.takeme
    };

    private final Integer[] greet_feel_questions = {
            R.drawable.how, R.drawable.when,
            R.drawable.where, R.drawable.why,
            R.drawable.what, R.drawable.who, R.drawable.howmuch, R.drawable.howmany, R.drawable.howlong
    };

    private final Integer[] daily_activities_brushing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    private final String[] daily_activities_brushing_text =
            {"Rinse mouth", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush back teeth", "Brush tongue", "Rinse mouth", "All done"};

    private final Integer[] daily_activities_toilet = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    private final String[] daily_activities_toilet_text =
            {"Pull pants down", "Sit on toilet", "Wash bottom", "Flush toilet", "Pull pants up", "Wash hands", "All done"};

    private final Integer[] daily_activities_bathing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    private final static String[] daily_activities_bathing_text =
            {"Remove clothes", "Turn on water", "Get in the shower", "Wet body", "Put soap", "Shampoo hair", "Put face wash", "Wash hair",
                    "Wash body", "Turn off water", "Dry hair", "Dry face", "Dry body", "Put on clothes", "All done"};

    private final Integer[] daily_activities_clothes_access = {
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

    private final Integer[] daily_activities_get_ready = {
            R.drawable.combhair, R.drawable.facewash,
            R.drawable.cuttingnails, R.drawable.blowingnose,
            R.drawable.soap, R.drawable.shampoo
    };

    private final Integer[] daily_activities_sleep = {
            R.drawable.door_homeobjects, R.drawable.fan,
            R.drawable.light, R.drawable.window,
            R.drawable.bed, R.drawable.pilllow, R.drawable.blanket, R.drawable.feelinghot, R.drawable.feelingcold
    };

    private final Integer[] daily_activities_therapy = {
            R.drawable.exercise, R.drawable.swing,
            R.drawable.trampoline, R.drawable.swissball,
            R.drawable.blanket, R.drawable.ballpit, R.drawable.handmovements, R.drawable.legexercises, R.drawable.bodyvest

    };

    private final Integer[] daily_activities_morning_schedule = {
            R.drawable.wakeup, R.drawable.washface,
            R.drawable.gototoilet, R.drawable.brushteeth,
            R.drawable.removeclothes, R.drawable.haveabath, R.drawable.getdressed, R.drawable.combhair,
            R.drawable.eatbreakfast, R.drawable.packlunch, R.drawable.packbagpack, R.drawable.gradient, R.drawable.haveagreatday
    };

    private final String[] daily_activities_morning_schedule_text =
            {"Wake up", "Wash face", "Go to bathroom", "Brush teeth", "Remove clothes", "Have a bath", "Get dressed", "Comb hair",
                    "Eat breakfast", "Pack lunchbox", "Pack school bag", "Go to school", "Have a great day!"};

    private final Integer[] daily_activities_bedtime_schedule = {
            R.drawable.eatdinner, R.drawable.wearnightsuit,
            R.drawable.brushteeth, R.drawable.readstory,
            R.drawable.saygoodnight, R.drawable.sayprayres, R.drawable.sweetdreams
    };

    private final String[] daily_activities_bedtime_schedule_text =
            {"Eat dinner", "Wear night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"};

    private final Integer[] eating_foods_drinks_breakfast = {
            R.drawable.bread, R.drawable.cornflakes,
            R.drawable.aloopuri, R.drawable.eggs,
            R.drawable.poha, R.drawable.upma, R.drawable.khichdi, R.drawable.idli,
            R.drawable.dosa, R.drawable.paratha,
            R.drawable.omelette, R.drawable.meduvada,
            R.drawable.porriage, R.drawable.sandwich, R.drawable.chutney, R.drawable.sambar, R.drawable.uttapam
    };

    private final Integer[] eating_food_drinks_lunch_dinner = {
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

    private final Integer[] eating_food_drinks_sweets = {
            R.drawable.cake, R.drawable.icecream,
            R.drawable.gajarhalwa, R.drawable.gulabjamun,
            R.drawable.ladoo, R.drawable.barfi,
            R.drawable.jalebi, R.drawable.fruitsalad,
            R.drawable.rasgulla, R.drawable.sheera
    };

    private final Integer[] eating_food_drinks_snacks = {
            R.drawable.biscuits, R.drawable.chaat,
            R.drawable.chocolate, R.drawable.chips,
            R.drawable.sandwich, R.drawable.noodles,
            R.drawable.cheese, R.drawable.peanuts
    };

    private final Integer[] eating_food_drinks_fruits = {
            R.drawable.apple, R.drawable.banana,
            R.drawable.grapes, R.drawable.guaua,
            R.drawable.mango, R.drawable.orange,
            R.drawable.pineapple, R.drawable.strawberry,
            R.drawable.blueberry, R.drawable.pomogranate,
            R.drawable.watermelon, R.drawable.pear,
            R.drawable.papaya, R.drawable.muskmelon,
            R.drawable.chikoo, R.drawable.jackfruit, R.drawable.cherry
    };

    private final Integer[] eating_food_drinks_drinks = {
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

    private final Integer[] eating_food_drinks_cutlery = {
            R.drawable.bowl_cutlery, R.drawable.plate_cutlery,
            R.drawable.spoon_cutlery, R.drawable.fork_cutlery,
            R.drawable.knife_cutlery, R.drawable.mug_cutlery, R.drawable.cup_cutlery, R.drawable.glass_cutlery
    };

    private final Integer[] eating_food_drinks_add_ons = {
            R.drawable.butter_addons, R.drawable.jam_addons,
            R.drawable.salt_addons, R.drawable.pepper_addons,
            R.drawable.sugar_addons, R.drawable.ketchup_addons,
            R.drawable.pickle, R.drawable.papad_addons, R.drawable.masala_addons
    };

    private final Integer[] fun_indoor_games = {
            R.drawable.puzzles, R.drawable.boradgames,
            R.drawable.blocks, R.drawable.lego,
            R.drawable.chess, R.drawable.snakesandladders, R.drawable.scrabble, R.drawable.videogames,
            R.drawable.doll, R.drawable.actionfigure,
            R.drawable.teddy, R.drawable.cars,
            R.drawable.truck, R.drawable.artandcraft_activities, R.drawable.playwithme
    };

    private final Integer[] fun_outdoor_games = {
            R.drawable.level2_places_playground, R.drawable.level2_places_park, R.drawable.swing,
            R.drawable.slide, R.drawable.seesaw, R.drawable.merrygoround, R.drawable.hideandseek, R.drawable.batandball,
            R.drawable.statue,
            R.drawable.lockandkey, R.drawable.catchcatch,
            R.drawable.kite, R.drawable.choorpolice, R.drawable.marbles, R.drawable.walking, R.drawable.cycle_transport,
            R.drawable.running, R.drawable.swimming

    };

    private final Integer[] fun_sports = {
            R.drawable.cricket_activities, R.drawable.badminton_activities,
            R.drawable.tennis_activities, R.drawable.basketball_activities,
            R.drawable.dodgeball_activities, R.drawable.vollyball_activities, R.drawable.khokho, R.drawable.football_activities, R.drawable.kabaddi, R.drawable.gymnastics, R.drawable.swimming
    };

    private final Integer[] fun_tv = {
            R.drawable.nextchannel, R.drawable.previouschannel,
            R.drawable.volumeup, R.drawable.volumedown
    };

    private final Integer[] fun_music = {
            R.drawable.changemusic, R.drawable.dancing,
            R.drawable.volumeup, R.drawable.volumedown
    };

    private final Integer[] fun_activities = {
            R.drawable.draw_activities, R.drawable.painting_activities,
            R.drawable.read_activities, R.drawable.write_activities,
            R.drawable.artandcraft_activities, R.drawable.drama_activities, R.drawable.dancing, R.drawable.musicalinstrument_activities
    };

    private final Integer[] learning_animals_birds = {
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

    private final Integer[] learning_body_parts = {
            R.drawable.head, R.drawable.hair_bodyparts,
            R.drawable.eye_bodyparts, R.drawable.nose_bodyparts,
            R.drawable.ear_bodyparts, R.drawable.mouth_bodyparts, R.drawable.tongue, R.drawable.neck, R.drawable.shoulder, R.drawable.elbow,
            R.drawable.wrist, R.drawable.hand_bodyparts,
            R.drawable.fingers, R.drawable.back, R.drawable.stomach_bodyparts, R.drawable.hips, R.drawable.knee_bodyparts, R.drawable.ankle, R.drawable.legs, R.drawable.toes
    };

    private final Integer[] learning_books = {
            R.drawable.bedtimebook, R.drawable.comics_books,
            R.drawable.rhymebook, R.drawable.drawingbook_books,
            R.drawable.storybook, R.drawable.picturebook, R.drawable.mysterybook, R.drawable.adventurebook,  R.drawable.notebook_books, R.drawable.maths,
            R.drawable.science_books, R.drawable.history,
            R.drawable.geography_books, R.drawable.socialscience, R.drawable.english, R.drawable.hindi_books,  R.drawable.marathi_books, R.drawable.wordbook, R.drawable.favouritebook
    };

    private final Integer[] learning_colours = {
            R.drawable.black_colour, R.drawable.blue_colour,
            R.drawable.brown_colour, R.drawable.greem_colour,
            R.drawable.redcolour, R.drawable.silvercolour,
            R.drawable.white_colour, R.drawable.yellow_colour,
            R.drawable.golden_colour, R.drawable.pink_colour, R.drawable.orange_colour, R.drawable.purple_colour, R.drawable.grey_colour
    };

    private final Integer[] learning_shapes = {
            R.drawable.standingline_shapes, R.drawable.sleepingline_shapes,
            R.drawable.slantingline_shapes, R.drawable.circle_shapes,
            R.drawable.rectangle_shapes, R.drawable.square_shapes, R.drawable.triangle_shapes, R.drawable.star_shapes, R.drawable.heart_shape, R.drawable.trapezium_shapes,
            R.drawable.cube_shapes, R.drawable.rhombus_shapes,
            R.drawable.hexagon_shapes, R.drawable.oval_shapes, R.drawable.diamond_shapes, R.drawable.pentagon_shapes, R.drawable.freeform_shapes
    };

    private final Integer[] learning_stationary = {
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary, R.drawable.crayon_stationary,
            R.drawable.blankpaper_stationary, R.drawable.colouredpaper_stationary, R.drawable.sissors, R.drawable.pencillead,
            R.drawable.compass, R.drawable.divider,
            R.drawable.stapler, R.drawable.upin,
            R.drawable.cellotape, R.drawable.pencilbox
    };

    private final Integer[] learning_school_objects = {
            R.drawable.bag_school, R.drawable.lunchbox,
            R.drawable.waterbottle_school, R.drawable.pencilbox,
            R.drawable.homework_school, R.drawable.notebook_books,
            R.drawable.textbooks_school, R.drawable.uniform_school,  R.drawable.shoes_school, R.drawable.socks_school,
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary, R.drawable.chalk
    };

    private final Integer[] learning_home_objects = {
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

    private final Integer[] learning_transportation = {
            R.drawable.bus_transport, R.drawable.schoolbus_transport,
            R.drawable.car_transport, R.drawable.cycle_transport,
            R.drawable.train_transport, R.drawable.rickshaw_transport,
            R.drawable.motorbike_transport, R.drawable.plane_transport, R.drawable.shop_transport
    };

    private final Integer[] time_weather_time = {
            R.drawable.currenttime, R.drawable.today,
            R.drawable.yesterday, R.drawable.tomorrow,
            R.drawable.morning, R.drawable.afternoon,
            R.drawable.goodevening, R.drawable.night
    };

    private final Integer[] time_weather_day = {
            R.drawable.currentday, R.drawable.somvaar,
            R.drawable.mangalvaaar, R.drawable.budhvaar,
            R.drawable.guruvaar, R.drawable.sukravaar,
            R.drawable.shanivaar, R.drawable.ravivaar
    };

    private final Integer[] time_weather_month = {
            R.drawable.whatisthemonth, R.drawable.januaryhindi,
            R.drawable.februaryhjindi, R.drawable.marchhindi,
            R.drawable.aprilhindi, R.drawable.mayhindi,
            R.drawable.junehindi, R.drawable.julyhindi,  R.drawable.augusthindi, R.drawable.septemberhindi,
            R.drawable.octoberhindi, R.drawable.novemberhindi,
            R.drawable.decemberhindi, R.drawable.currentmonthhindi,
            R.drawable.previousmonthhindi, R.drawable.nextmonthhindi
    };

    private final Integer[] time_weather_weather = {
            R.drawable.todaysweather, R.drawable.afternoon,
            R.drawable.rainy, R.drawable.cloudy,
            R.drawable.windy, R.drawable.foggy,
            R.drawable.snowy
    };

    private final Integer[] time_weather_seasons = {
            R.drawable.currentseason, R.drawable.spring,
            R.drawable.afternoon, R.drawable.rainy,
            R.drawable.autumn, R.drawable.winter
    };

    private final Integer[] time_weather_holidays_festivals = {
            R.drawable.diwali, R.drawable.ganeshchaturthi,
            R.drawable.christmas, R.drawable.duserra,
            R.drawable.mararsankranti, R.drawable.holi,
            R.drawable.eid, R.drawable.goodfriday,R.drawable.gudipadwa,
            R.drawable.republicday, R.drawable.independance, R.drawable.newyear
    };

    private final Integer[] time_weather_birthdays = {
            R.drawable.mybirthday, R.drawable.momsbirthday,
            R.drawable.dadsbirthday, R.drawable.brothersbirthday,
            R.drawable.sistersbirthday, R.drawable.badepapa,
            R.drawable.badimummy, R.drawable.grandfathersbirthday,  R.drawable.grandmomsbirthday, R.drawable.nanaji,
            R.drawable.naniji, R.drawable.chacha,
            R.drawable.chachi, R.drawable.mama, R.drawable.mami,
            R.drawable.bua,  R.drawable.fufaji, R.drawable.mausi, R.drawable.mausa,
            R.drawable.friendsbirthday, R.drawable.teachersbirthday
    };


    private final String[] greet_feel_greetings_text =
            {"नमस्ते!", "नमस्कार", "अलविदा", "शुभ प्रभात", "शुभ दिन", "शुभ संध्या", "शुभ रात्रि", "ताली दो",  "आपसे मिलकर अच्छा लगा", "आप कैसे हैं?", "आपका दिन कैसा था?", "आपके क्या हाल हैं?"};
    private final String[] greet_feel_feelings_text =
            {"खुश", "उदास", "गुस्सा", "डर", "हैरान", "चिढ़ा हुआ", "उलझन", "शर्मिंदा", "निराश", "बोर", "चिंता", "तनावग्रस्त", "थका हुआ", "गरम", "ठंडा", "बीमार", "दुःखी"};
    private final String[] greet_feel_requests_text =
            {"कृपया", "धन्यवाद", "आपका स्वागत हैं", "कृपया दिजीए", "कृपया फिर से बताईए", "कृपया दिखाईए", "मुझे एक ब्रेक चाहिए", "मैंने खत्म कर दिया",  "क्षमा कीजिये!", "मुझे माफ करें", "मुझे समझ में नहीं आया", "मेरे साथ बाँटे", "कृपया थोड़ा धीरे जाइए", "मुझे मदद की ज़रूरत हैं", "कृपया यहाँ आइये", "कृपया मुझे लेके जाईए"};
    private final String[] greet_feel_questions_text =
            {"कैसे?", "कब?", "कहॉं पे?", "क्यूं?", "क्या?", "कौन?", "कितने?", "कितना लंबा?", "कितनी देर?"};
    private final String[] daily_activities_clothes_access_text =
            {"टी-शर्ट बदलना ", "फ्रॉक बदलना", "स्कर्ट बदलना", "जीन्स बदलना", "पैंट बदलना", "लेगिंग बदलना", "स्लैक्स बदलना", "शॉर्ट्स बदलना", "इनरवियर बदलना", "जूते बदलना", "बूट बदलना", "मोज़े बदलना", "रात के कपड़े पहनना", "शर्ट ", "टी-शर्ट ", "फ्रॉक ",  "पैंट ", "स्लैक्स ", "लेगिंग ", "शॉर्ट्स ", "सलवार कमीज़ ", "स्वेटर ", "जैकेट ", "दुपट्टा ",  "टोपी ", "बेल्ट ", "रेनकोट ", "चश्मा ", "घड़ी ", "कान की बाली ", "कंगन", "हार ",  "बिंदी ", "चप्पल ", "मेरे कपड़े टाइट हैं", "मेरे कपड़े ढीले हैं", "कपड़े निकालने में मदद", "कपड़े पैहनने में मदद"};
    private final String[] daily_activities_get_ready_text =
            {"कंघी करना", "फेस वॉश", "नाखून काटना", "नाक साफ करना", "साबुन", "शैम्पू"};
    private final String[] daily_activities_sleep_text =
            {"दरवाज़ा", "पंखा", "लाईट", "खिड़की", "बिस्तर", "तकिया", "कंबल", "गर्मी", "ठंडक"};
    private final String[] daily_activities_therapy_text =
            {"कसरत", "झूला", "ट्रेम्पोलिन", "स्विस बॉल", "कंबल", "बॉल पिट", "हातों की कसरत", "पैरों की कसरत", "बॉडी वेस्ट"};
    private final String[] foods_drinks_breakfast_text =
            {"ब्रेड", "कॉर्नफ़्लेक्स", "आलू पूरी", "अंडे", "पोहा", "उपमा", "खिचड़ी", "इड़ली ",  "डोसा", "पराठा", "आमलेट", "मेदु वड़ा", "दलिया", "सैंडविच", "चटनी", "सांबर", "उत्तपा"};
    private final String[] food_drinks_lunch_dinner_text =
            {"रोटी", "सब्ज़ी", "चावल", "दाल", "दालखिचड़ी", "रायता", "पराठा", "दही",  "मछली", "चिकन", "पोर्क", "मटन", "केकड़े का मांस", "टर्की", "पिज्जा", "सलाद ",  "सूप ", "पास्ता ", "नूडल्स", "इटालियन खाना", "पाव भाजी", "भाकरी"};
    private final String[] food_drinks_sweets_text =
            {"केक", "आइसक्रीम", "गाजर का हलवा", "गुलाब जामुन", "लड्डू", "बर्फी", "जलेबी", "फलों का सलाद",  "रसगुल्ला", "शीरा"};
    private final String[] food_drinks_snacks_text =
            {"बिस्कुट", "चाट", "चॉकलेट", "वेफर्स", "सैंडविच", "नूडल्स", "चीज़", "नट्स"};
    private final String[] food_drinks_fruits_text =
            {"सेब", "केला", "अंगूर", "अमरूद", "आम", "संतरा", "अनानास", "स्ट्रॉबेरी",  "बेर", "अनार", "तरबूज", "पेर", "पपीता", "खरबूजा", "चिकू", "पनस", "चेरी"};
    private final String[] food_drinks_drinks_text =
            {"पानी", "दूध", "बॉर्नविटा", "आम का ज्यूस", "सेब का ज्यूस", "संतरे का ज्यूस", "नींबू का ज्यूस", "अनानास का ज्यूस",  "पेप्सी", "कोका कोला", "मिरिंडा", "फैंटा", "माझा", "स्प्राइट ", "माउंटेन ड्यू", "मिल्कशेक",  "चॉकलेट मिल्कशेक", "स्ट्रॉबेरी मिल्कशेक ", "केला मिल्कशेक ", "आम मिल्कशेक ", "चिकू मिल्कशेक", "चाय ", "कॉफी ", "कोल्ड कॉफी", "एनर्जी ड्रिंक्स"};
    private final String[] food_drinks_cutlery_text =
            {"कटोरा", "प्लेट", "चम्मच", "काँटे का चम्मच", "चाकू", "मग", "कप", "ग्लास"};
    private final String[] food_drinks_add_ons_text =
            {"मक्खन", "जाम", "नमक", "काली मिर्च", "चीनी", "सॉस", "आचार", "पापड़", "मसाला"};
    private final String[] fun_indoor_games_text =
            {"पज़ल्स", "बोर्ड खेल", "ब्लॉक्स", "लेगो", "शतरंज", "सांप और सीढ़ी", "स्क्रैबल", "विडियो गेम",  "गुड़ियाँ", "एक्शन फिगर्स", "सॉफ्ट टॉयज़", "कार", "ट्रक", "आर्ट-क्राफ्ट", "मेरे साथ खेलो"};
    private final String[] fun_outdoor_games_text =
            {"खेल का मैदान", "पार्क", "झूला", "स्लाईड ", "सी-सॉ", "मेरी-गो-राउंड", "लुकाछिपी", "बल्ला और गेंद", "स्टैचू", "ताला और चाबी", "पकड़ा-पकड़ी", "पतंग", "चोर-पुलिस", "कंचे", "चलना", "साइकिल", "दौड़ना", "तैरना"};
    private final String[] fun_sports_text =
            {"क्रिकेट ", "बैडमिंटन", "टेनिस ", "बास्केटबॉल", "डॉजबॉल", "वॉलीबॉल", "खो-खो", "फुटबॉल ",  "कबड्डी", "जिमनास्टिक", "तैरना"};
    private final String[] fun_tv_text =
            {"अगला चैनल", "पिछला चैनल", "उँची आवाज़", "धीमी आवाज़"};
    private final String[] fun_music_text =
            {"संगीत बदलना", "नाचना", "उँची आवाज़", "धीमी आवाज़"};
    private final String[] fun_activities_text =
            {"चित्र बनाना", "रंग भरना", "पढ़ना", "लिखना", "आर्ट-क्राफ्ट", "नाटक", "नाचना", "संगीत बजाना"};
    private final String[] learning_animals_birds_text =
            {"कुत्ता", "बिल्ली", "हाथी", "शेर ", "तोता ", "खरगोश ", "गाय", "बत्तख", "गधा ", "चींटी", "बाघ", "बंदर", "कबूतर ", "तिलचट्टा", "कौआ ", "घोड़ा ",  "हिरण ", "उल्लू", "भेड़िया ", "लोमड़ी", "भालू ", "भेड़ ", "बकरी ", "सुअर ", "मक्खी", "जिराफ़", "ज़ेब्रा", "मच्छर", "भैंस", "चूहा", "साँप", "मगरमच्छ ",  "मधुमक्खी ", "दरियाई घोड़ा ", "गेंडा", "मछली ", "पेंगविन ", "सील ", "डॉल्फिन ", "व्हेल ", "शार्क ", "कछुआ ", "चिडिया", "गरुड़", "हॉक", "गिद्ध"};
    private final String[] learning_body_parts_text =
            {"सिर", "बाल", "आँखें", "नाक", "कान", "मुँह", "जीभ", "गर्दन", "कंधा", "कोहनी", "कलाई", "हाथ", "उंगलियाँ", "पीठ", "पेट", "कूल्हे का जोड़", "घुटना", "घुटिका", "पैर", "पैर की उंगलियाँ"};
    private final String[] learning_books_text =
            {"सोने के समय की कहानीयाँ", "हास्यमय किताबें", "काव्यमय किताबें", "चित्रकला की किताबें", "कहानियों की किताबें", "चित्रों की किताबें", "जासूसी किताबें", "साहसी किताबें",  "पाठशाला की नोटबुक", "गणित किताब", "विज्ञान किताब", "इतिहास किताब", "भूगोल किताब", "सामाजिक अध्ययन किताब ", "अंग्रेजी किताब", "हिंदी किताब",  "मराठी किताब", "पाठ्यपुस्तकें", "पसंदीदा किताब"};
    private final String[] learning_colours_text =
            {"काला", "नीला", "भूरा", "हरा", "लाल", "चाँदी", "सफेद", "पीला",  "सुनहरा", "गुलाबी", "नारंगी ", "जामुनी", "ग्रे "};
    private final String[] learning_shapes_text =
            {"सीधी रेखा ", "आड़ी रेखा", "तिरछी रेखा", "गोल", "आयत", "चौकोर", "त्रिकोण", "तारा",  "दिल", "असमांतरभुज कोण", "घनाकार", "समचतुर्भुज कोण", "षट्कोण", "अंडाकार", "ईंट", "पंचकोण", "मुक्ताकार"};
    private final String[] learning_stationary_text =
            {"पेंसिल", "पेन", "स्केल", "रबर", "शार्पनर", "क्रेयान", "कोरा कागज", "रंगीन कागज",  "कैंची", "सीसा", "कम्पास", "विभाजक", "स्टेप्लर", "यू-पिन", "सेलो टेप", "कम्पास बॉक्स"};
    private final String[] learning_school_objects_text =
            {"बैग", "खाने का डिब्बा", "पानी की बोतल", "कम्पास बॉक्स", "गृहपाठ", "कापी", "पाठ्यपुस्तकें", "यूनिफार्म",  "जूते ", "मोज़े ", "पेंसिल", "पेन", "स्केल", "रबर", "शार्पनर", "चॉक"};
    private final String[] learning_home_objects_text =
            {"खिड़की", "दरवाज़ा", "पंखा", "लैंप", "डेस्क", "अलमारी", "टेबल", "कुर्सी","शौचालय", "रसोईघर", "हॉल", "बेडरूम", "खेलने का कमरा", "बाथरूम", "बालकनी", "पढ़ाई का कमरा", "बिस्तर", "टीवी", "संगणक", "सोफ़ा", "फ्रिज़", "माइक्रोवेव", "वॉशिंग मशीन", "वैक्यूम क्लीनर",  "घड़ी", "ट्यूब लाइट"};
    private final String[] learning_transportation_text =
            {"बस", "स्कूल  बस", "कार", "साइकिल", "रेलगाड़ी", "रिक्शा", "मोटर साइकिल ", "हवाई जहाज़", "जहाज़"};
    private final String[] time_weather_time_text =
            {"समय क्या हुआ हैं?", "आज़", "कल", "कल", "सुबह", "दोपहर", "शाम", "रात"};
    private final String[] time_weather_day_text =
            {"आज कौनसा दिन है?", "सोमवार", "मंगलवार", "बुधवार", "गुरूवार", "शुक्रवार", "शनिवार", "रविवार"};
    private final String[] time_weather_month_text =
            {"वर्तमान महीना कौनसा हैं ?", "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून", "जुलाई",  "अगस्त", "सितंबर ", "अक्टूबर", "नवंबर", "दिसंबर", "यह महीना", "पिछला महीना", "अगला महीना"};
    private final String[] time_weather_weather_text =
            {"आज का मौसम क्या हैं? ", "गरम", "बरसात", "धुंधला", "तूफ़ानी", "घना", "बर्फीला"};
    private final String[] time_weather_seasons_text =
            {"वर्तमान ऋतु कौनसा हैं? ", "वसंत ऋतु", "ग्रीष्म ऋतु", "वर्षा ऋतु", "शरद ऋतु", "शीत ऋतु"};
    private final String[] time_weather_holidays_festivals_text =
            {"दिवाली", "गणेश चतुर्थी", "क्रिसमस ", "दशहरा", "मकर संक्रांति", "होली", "ईद", "गुड फ्राइडे", "गुड़ी पाड़वा", "गणतंत्र दिवस", "स्वतंत्रता दिवस", "नया साल"};
    private final String[] time_weather_brthdays_text =
            {"मेरा जन्मदिन", "माँ का जन्मदिन", "पिताजी का जन्मदिन", "भाई का जन्मदिन", "बहन का जन्मदिन", "बड़े पापा का जन्मदिन", "बड़ी मम्मी का जन्मदिन", "दादाज़ी का जन्मदिन",
                    "दादी माँ का जन्मदिन", "नानाज़ी का जन्मदिन", "नानी माँ का जन्मदिन", "चाचा का जन्मदिन", "चाची का जन्मदिन", "मामा का जन्मदिन", "मामी का जन्मदिन", "बुआ का जन्मदिन",
                    "फ़ुफ़ा का जन्मदिन", "मौसी का जन्मदिन", "मौसा का जन्मदिन", "मित्र का जन्मदिन", "शिक्षक का जन्मदिन"};

}