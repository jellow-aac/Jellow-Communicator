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
 * Created by Sumeet on 19-04-2016.
 */
class Layer_three_Adapter extends android.support.v7.widget.RecyclerView.Adapter<Layer_three_Adapter.MyViewHolder>{
    private Context mContext;
    private SessionManager mSession;
    private EvaluateDisplayMetricsUtils mMetricsUtils;
    private Integer[] mThumbIds = new Integer[100];
    private String[] belowText = new String[100];

    Layer_three_Adapter(Context context, int levelOneITemPos, int levelTwoItemPos, int sort[]){
        mContext = context;
        mSession = new SessionManager(mContext);
        mMetricsUtils = new EvaluateDisplayMetricsUtils(mContext);
            if (levelOneITemPos == 0) {
                if (levelTwoItemPos == 0) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelGreetingIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 1) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelFeelingIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 2) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelRequestsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 3) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeGreetFeelQuestionsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechTextEnglish), sort);
                }
            } else if (levelOneITemPos == 1) {
                if (levelTwoItemPos == 0) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBrushingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBrushingAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 1) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActToiletIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActToiletAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 2) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBathingIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBathingAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 3) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActClothesIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 4) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActGetReadyIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 5) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActSleepIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 6) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActTherapyIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 7) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActMorningScheIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActMorningScheSpeechTextEnglish), sort);
                } else if (levelTwoItemPos == 8) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeDailyActBedTimeScheIconAdapter),
                        mContext.getResources().getStringArray(R.array.arrLevelThreeDailyActBedTimeScheSpeechTextEnglish), sort);
                }
            } else if (levelOneITemPos == 2) {
                if (levelTwoItemPos == 0) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksBreakfastIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 1) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksLunchDinIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 2) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSweetsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 3) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksSnacksIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 4) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksFruitsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 5) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksDrinksIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 6) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksCutleryIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 7) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFoodDrinksAddonsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonAdapterTextEnglish), sort);
                }
            } else if (levelOneITemPos == 3) {
                if (levelTwoItemPos == 0) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunInDGamesIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 1) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunOutDGamesIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 2) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunSportsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunSportsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 3) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunTvIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeFunTvAdapterTextEnglish);
                } else if (levelTwoItemPos == 4) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunMusicIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeFunMusicAdapterTextEnglish);
                } else if (levelTwoItemPos == 5) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeFunActivitiesIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesAdapterTextEnglish), sort);
                }
            } else if (levelOneITemPos == 4) {
                if (levelTwoItemPos == 0) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningAnimBirdsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 1) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBodyPartsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 2) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningBooksIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningBooksAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 3) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningColorsIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningColorsAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 4) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningShapesIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningShapesAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 5) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningStationaryIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 6) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningSchoolIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 7) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningHomeIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 8) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeLearningTransportationIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeLearningTransportAdapterTextEnglish), sort);
                }
            } else if (levelOneITemPos == 7) {
                if (levelTwoItemPos == 0) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaTimeIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeAdapterTextEnglish);
                } else if (levelTwoItemPos == 1) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaDayIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayAdapterTextEnglish);
                } else if (levelTwoItemPos == 2) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaMonthIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthAdapterTextEnglish);
                } else if (levelTwoItemPos == 3) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaWeatherIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherAdapterTextEnglish);
                } else if (levelTwoItemPos == 4) {
                    mThumbIds = getIconArray(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaSeasonsIconAdapter));
                    belowText = mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsAdapterTextEnglish);
                } else if (levelTwoItemPos == 5) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaHoliFestIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestAdapterTextEnglish), sort);
                } else if (levelTwoItemPos == 6) {
                    loadAdapterMenuTextIcons(mContext.getResources().obtainTypedArray(R.array.arrLevelThreeTimeWeaBirthdaysIconAdapter),
                            mContext.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysAdapterTextEnglish), sort);
                }
            }
    }

    private Integer[] getIconArray(TypedArray typedArray) {
        Integer[] tempIconArr = new Integer[typedArray.length()];
        for (int i=0; i< typedArray.length(); ++i) {
            tempIconArr[i] = typedArray.getResourceId(i, -1);
        }
        return tempIconArr;
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

    @Override
    public Layer_three_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        return new Layer_three_Adapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

    class MyViewHolder extends RecyclerView.ViewHolder  {
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