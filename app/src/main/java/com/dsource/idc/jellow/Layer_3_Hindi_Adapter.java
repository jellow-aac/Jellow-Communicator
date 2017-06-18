package com.dsource.idc.jellow;

import android.content.Context;
import android.content.res.TypedArray;
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

    Layer_3_Hindi_Adapter(Context context, int levelOneItemPos, int levelTwoItemPos, int sort[]) {
        mContext = context;
        mSession = new SessionManager(mContext);
        mMetricsUtils = new EvaluateDisplayMetricsUtils(mContext);
        int store = 0;
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelGreetingIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingAdapterTextHindi), sort);
                    break;
                case 1: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelFeelingIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsAdapterTextHindi), sort);
                    break;
                case 2: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelRequestsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsAdapterTextHindi), sort);
                    break;
                case 3: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelQuestionsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechTextHindi), sort);
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBrushingIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBrushingAdapterTextHindi), sort);
                    break;
                case 1: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActToiletIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActToiletAdapterTextHindi), sort);
                    break;
                case 2: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBathingIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBathingAdapterTextHindi), sort);
                    break;
                case 3: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActClothesIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccAdapterTextHindi), sort);
                    break;
                case 4: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActGetReadyIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyAdapterTextHindi), sort);
                    break;
                case 5: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActSleepIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepAdapterTextHindi), sort);
                    break;
                case 6: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActTherapyIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyAdapterTextHindi), sort);
                    break;
                case 7: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActMorningScheIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActMorningScheSpeechTextHindi), sort);
                    break;
                case 8: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBedTimeScheIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBedTimeScheSpeechTextHindi), sort);
                    break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksBreakfastIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastAdapterTextHindi), sort);
                    break;
                case 1: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksLunchDinIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerAdapterTextHindi), sort);
                    break;
                case 2: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSweetsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsAdapterTextHindi), sort);
                    break;
                case 3: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSnacksIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksAdapterTextHindi), sort);
                    break;
                case 4: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksFruitsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsAdapterTextHindi), sort);
                    break;
                case 5: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksDrinksIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksAdapterTextHindi), sort);
                    break;
                case 6: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksCutleryIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryAdapterTextHindi), sort);
                    break;
                case 7: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksAddonsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonAdapterTextHindi), sort);
                    break;
            }
        } else if (levelOneItemPos == 3) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunInDGamesIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesAdapterTextHindi), sort);
                    break;
                case 1: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunOutDGamesIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesAdapterTextHindi), sort);
                    break;
                case 2:
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunSportsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunSportsAdapterTextHindi), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunTvIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunTvAdapterTextHindi));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunMusicIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunMusicAdapterTextHindi));
                    break;
                case 5: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunActivitiesIconAdapterHindi),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesAdapterTextHindi), sort);
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningAnimBirdsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsAdapterTextHindi), sort);
                    break;
                case 1: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBodyPartsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsAdapterTextHindi), sort);
                    break;
                case 2: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBooksIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBooksAdapterTextHindi), sort);
                    break;
                case 3: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningColorsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningColorsAdapterTextHindi), sort);
                    break;
                case 4: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningShapesIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningShapesAdapterTextHindi), sort);
                    break;
                case 5: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningStationaryIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryAdapterTextHindi), sort);
                    break;
                case 6: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningSchoolIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjAdapterTextHindi), sort);
                    break;
                case 7: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningHomeIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjAdapterTextHindi), sort);
                    break;
                case 8: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningTransportationIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningTransportAdapterTextHindi), sort);
                    break;
            }
        } else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaTimeIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeAdapterTextHindi));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaDayIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayAdapterTextHindi));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaMonthIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthAdapterTextHindi));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaWeatherIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherAdapterTextHindi));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaSeasonsIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsAdapterTextHindi));
                    break;
                case 5: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaHoliFestIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestAdapterTextHindi), sort);
                    break;
                case 6: loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaBirthdaysIconAdapterHindi),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysAdapterTextHindi), sort);
                    break;
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

    private void loadAdapterMenuTextIconsWithoutSort(TypedArray typeIconArray, String[] stringBelowTextArray) {
        Integer[] tempIconArr = new Integer[stringBelowTextArray.length];
        String[] tempBelowTextArr = new String[stringBelowTextArray.length];

        for (int j = 0; j < typeIconArray.length(); j++) {
            tempIconArr[j] = typeIconArray.getResourceId(j, -1);
            tempBelowTextArr[j] = stringBelowTextArray[j];
        }
        mThumbIds = Arrays.copyOfRange(tempIconArr, 0, typeIconArray.length());
        belowText = Arrays.copyOfRange(tempBelowTextArr, 0, typeIconArray.length());
    }

    private void loadAdapterMenuTextIcons(TypedArray typeIconArray, String[] stringBelowTextArray, int[] sort) {
        Integer[] tempIconArr = new Integer[sort.length];
        String[] tempBelowTextArr = new String[sort.length];

        for (int j = 0; j < typeIconArray.length(); j++) {
            tempIconArr[j] = typeIconArray.getResourceId(sort[j], -1);
            tempBelowTextArr[j] = stringBelowTextArray[sort[j]];
        }
        mThumbIds = Arrays.copyOfRange(tempIconArr, 0, typeIconArray.length());
        belowText = Arrays.copyOfRange(tempBelowTextArr, 0, typeIconArray.length());
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
}