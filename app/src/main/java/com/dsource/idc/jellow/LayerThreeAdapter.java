package com.dsource.idc.jellow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.SessionManager;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sumeet on 19-04-2016.
 */
class LayerThreeAdapter extends android.support.v7.widget.RecyclerView.Adapter<LayerThreeAdapter.MyViewHolder>{
    private Context mContext;
    private SessionManager mSession;
    private Integer[] mThumbIds = new Integer[100];
    private String[] belowText = new String[100];

    LayerThreeAdapter(Context context, int levelOneItemPos, int levelTwoItemPos, int sort[]){
        mContext = context;
        mSession = new SessionManager(mContext);
        loadArraysFromResources(levelOneItemPos, levelTwoItemPos, sort);
    }

    @Override
    public LayerThreeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY3 = 0;
        View rowView;
        if (mSession.getGridSize() == GRID_1BY3)
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        else
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        return new LayerThreeAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final int MODE_PICTURE_ONLY = 1;
        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemBelowText.setText(belowText[position]);

        holder.menuItemImage.setImageResource(mThumbIds[position]);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((LevelThreeActivity)mContext).tappedGridItemEvent(holder.menuItemLinearLayout, v, position);
            }
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

    private void loadArraysFromResources(int levelOneItemPos, int levelTwoItemPos, int[] sort) {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelGreetingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingAdapterText), sort);
                    break;
                case 1: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelFeelingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsAdapterText), sort);
                    break;
                case 2: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelRequestsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsAdapterText), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelQuestionsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsAdapterText), sort);
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBrushingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBrushingAdapterText), sort);
                    break;
                case 1: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActToiletIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActToiletAdapterText), sort);
                    break;
                case 2: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBathingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBathingAdapterText), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActClothesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccAdapterText), sort);
                    break;
                case 4: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActGetReadyIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyAdapterText), sort);
                    break;
                case 5: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActSleepIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepAdapterText), sort);
                    break;
                case 6: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActTherapyIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyAdapterText), sort);
                    break;
                case 7: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActMorningScheIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActMorningScheAdapterText), sort);
                    break;
                case 8: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBedTimeScheIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActMorningBedTimScheAdapterText), sort);
                    break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksBreakfastIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastAdapterText), sort);
                    break;
                case 1: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksLunchDinIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerAdapterText), sort);
                    break;
                case 2: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSweetsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsAdapterText), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSnacksIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksAdapterText), sort);
                    break;
                case 4: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksFruitsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsAdapterText), sort);
                    break;
                case 5: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksDrinksIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksAdapterText), sort);
                    break;
                case 6: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksCutleryIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryAdapterText), sort);
                    break;
                case 7: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksAddonsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonAdapterText), sort);
                    break;
            }
        } else if (levelOneItemPos == 3) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunInDGamesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesAdapterText), sort);
                    break;
                case 1: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunOutDGamesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesAdapterText), sort);
                    break;
                case 2: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunSportsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunSportsAdapterText), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunTvIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunTvAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunMusicIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunMusicAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunActivitiesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesAdapterText), sort);
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningAnimBirdsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsAdapterText), sort);
                    break;
                case 1: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBodyPartsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsAdapterText), sort);
                    break;
                case 2: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBooksIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBooksAdapterText), sort);
                    break;
                case 3: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningColorsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningColorsAdapterText), sort);
                    break;
                case 4: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningShapesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningShapesAdapterText), sort);
                    break;
                case 5: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningStationaryIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryAdapterText), sort);
                    break;
                case 6: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningSchoolIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjAdapterText), sort);
                    break;
                case 7: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningHomeIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjAdapterText), sort);
                    break;
                case 8: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningTransportationIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningTransportAdapterText), sort);
                    break;
                case 9: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningMoneyIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeLearningMoneyAdapterText));
                    break;
            }
        } else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaTimeIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeAdapterText));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaDayIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayAdapterText));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaMonthIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthAdapterText));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaWeatherIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaSeasonsIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaHoliFestIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestAdapterText), sort);
                    break;
                case 6: loadAdapterMenuTextIconsWithSort(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaBirthdaysIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysAdapterText), sort);
                    break;
            }
        }
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

    private void loadAdapterMenuTextIconsWithSort(TypedArray typeIconArray, String[] stringBelowTextArray, int[] sort) {
        Integer[] tempIconArr = new Integer[sort.length];
        String[] tempBelowTextArr = new String[sort.length];

        for (int j = 0; j < typeIconArray.length(); j++) {
            tempIconArr[j] = typeIconArray.getResourceId(sort[j], -1);
            tempBelowTextArr[j] = stringBelowTextArray[sort[j]];
        }
        mThumbIds = Arrays.copyOfRange(tempIconArr, 0, typeIconArray.length());
        belowText = Arrays.copyOfRange(tempBelowTextArr, 0, typeIconArray.length());
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {
        private LinearLayout menuItemLinearLayout;
        private CircleImageView menuItemImage;
        private TextView menuItemBelowText;

        MyViewHolder(final View view) {
            super(view);
            menuItemImage = (CircleImageView) view.findViewById(R.id.icon1);
            menuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = (TextView) view.findViewById(R.id.te1);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mukta-Regular.ttf");
            menuItemBelowText.setTypeface(font);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
        }
    }
}