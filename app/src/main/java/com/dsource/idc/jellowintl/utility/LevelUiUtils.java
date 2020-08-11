package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.Presentor.PreferencesHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.CategoryPreference;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;

import static com.dsource.idc.jellowintl.factories.IconFactory.getIconCode;
import static com.dsource.idc.jellowintl.models.GlobalConstants.NA;

public class LevelUiUtils {

    public static void disableAllExpressiveIcon(ImageView[] btn){
        for (ImageView expr : btn) {
            expr.setEnabled(false);
            expr.setAlpha(GlobalConstants.DISABLE_ALPHA);
        }
    }

    public static void enableAllExpressiveIcon(ImageView[] btn){
        for (ImageView expr : btn) {
            expr.setEnabled(true);
            expr.setAlpha(GlobalConstants.ENABLE_ALPHA);
        }
    }

    public static void setExpressiveIconConditionally(ImageView[] btn, Icon icon) {
        String[] expr = {icon.getL(), icon.getY(), icon.getM(), icon.getD(), icon.getN(), icon.getS()};
        for (int i = 0; i < btn.length; i++) {
            if (expr[i].equals(NA)){
                btn[i].setAlpha(GlobalConstants.DISABLE_ALPHA);
                btn[i].setEnabled(false);
            }else{
                btn[i].setAlpha(GlobalConstants.ENABLE_ALPHA);
                btn[i].setEnabled(true);
            }
        }
    }

    public static void setExpressiveIconPressedState(ImageView[] btn, int expression){
        // clear previously selected any expressive button or home button
        btn[GlobalConstants.LIKE].setImageResource(R.drawable.like);
        btn[GlobalConstants.YES].setImageResource(R.drawable.yes);
        btn[GlobalConstants.MORE].setImageResource(R.drawable.more);
        btn[GlobalConstants.DONT_LIKE].setImageResource(R.drawable.dontlike);
        btn[GlobalConstants.NO].setImageResource(R.drawable.no);
        btn[GlobalConstants.LESS].setImageResource(R.drawable.less);
        // set expressive button or home button to pressed state
        switch (expression){
            case GlobalConstants.LIKE:
                btn[GlobalConstants.LIKE].setImageResource(R.drawable.like_pressed);
                break;
            case GlobalConstants.YES:
                btn[GlobalConstants.YES].setImageResource(R.drawable.yes_pressed);
                break;
            case GlobalConstants.MORE:
                btn[GlobalConstants.MORE].setImageResource(R.drawable.more_pressed);
                break;
            case GlobalConstants.DONT_LIKE:
                btn[GlobalConstants.DONT_LIKE].setImageResource(R.drawable.dontlike_pressed);
                break;
            case GlobalConstants.NO:
                btn[GlobalConstants.NO].setImageResource(R.drawable.no_pressed);
                break;
            case GlobalConstants.LESS:
                btn[GlobalConstants.LESS].setImageResource(R.drawable.less_pressed);
                break;
            default: break;
        }
    }

    /**
     * <p>This function set the border to category icons. This function first extracts the view to
     * which border should applied then apply the border.
     * {@param recyclerChildView} is a parent view extracted from recycler view when category icon is tapped.
     * {@param setBorder} is set if category icon tapped and a color border should appear on the view other wise
     *  transparent color is set to border.
     * </p>
     * */
    public static void setBorderToCategoryIcon(Context context, View recyclerChildView,
                                   boolean setBorder, int actionBtnClickCount, int expression) {
        GradientDrawable gd = (GradientDrawable) recyclerChildView.findViewById(R.id.borderView).getBackground();
        if(setBorder){
            // mActionBtnClickCount = 0, brown color border is set.
            if (actionBtnClickCount > 0) {
                // mFlgImage define color of border.
                switch (expression) {
                    case GlobalConstants.LIKE: gd.setColor(ContextCompat.getColor(context,R.color.colorLike)); break;
                    case GlobalConstants.YES: gd.setColor(ContextCompat.getColor(context,R.color.colorYes)); break;
                    case GlobalConstants.MORE: gd.setColor(ContextCompat.getColor(context,R.color.colorMore)); break;
                    case GlobalConstants.DONT_LIKE: gd.setColor(ContextCompat.getColor(context,R.color.colorDontLike)); break;
                    case GlobalConstants.NO: gd.setColor(ContextCompat.getColor(context,R.color.colorNo)); break;
                    case GlobalConstants.LESS: gd.setColor(ContextCompat.getColor(context,R.color.colorLess)); break;
                }
            } else
                gd.setColor(ContextCompat.getColor(context,R.color.colorSelect));
        } else
            gd.setColor(ContextCompat.getColor(context,android.R.color.transparent));
    }

    public static void resetRecyclerAllItems(Context context, RecyclerView recyclerView,
                                             int actionBtnClickCount, int expression){
        for(int i = 0; i< recyclerView.getChildCount(); i++)
            setBorderToCategoryIcon(context, recyclerView.getChildAt(i),
                    false, actionBtnClickCount, expression);
    }

     /**
      * if Daily Activities category is selected in level one and
         if category icon selected in level two is
         Daily Activities ->Brushing  (position = 0)or
         Daily Activities ->Toilet (position = 1) or
         Daily Activities ->Bathing (position = 2) or
         Daily Activities ->Morning routine (position = 7) or
         Daily Activities ->Bedtime routine (position = 8)
         then change intent to open sequence activity.
      *
      **/
    public static boolean isSequencePosition(int position){
        return position == 0 || position == 1 || position == 2 ||
                position == 7 || position == 8;
    }

    /**
     * <p>This function places the call to child's caregiver number provided
     * in User profile.
     * If device is Lollipop or below version then this function is directly called.
     * If device is MarshMellow or above then after getting the user permission this function
     * is called.
     * @param contact has the contact number of child's caregiver.</p>
     * */
    public static void startCall(Context context, String contact){
        //removing extra digits (these digits are added to make mobile number unique)
        // stored during registration.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(contact));
        context.startActivity(callIntent);
    }

    /**
     * <p>This function increment the touch count for the selected category icon.
     *  */
    public static void incrementTouchCountOfItem(Integer[] currentPrefArray, Integer[] sortArray,
                             int tappedIconPos, int l1CatPos, int l2CatPos,
                             String languageCode, AppDatabase appDatabase) {
        // increment tap count of selected category icon
        currentPrefArray[sortArray[tappedIconPos]] += 1;

        StringBuilder stringBuilder = new StringBuilder();
        // create preference string.
        for (Integer count : currentPrefArray)
            stringBuilder.append(count).append(",");

        // store preference string in session preferences.
        CategoryPreference categoryPref = new CategoryPreference();
        categoryPref.setCategoryPosition(getIconCode(languageCode, l1CatPos, l2CatPos));
        categoryPref.setPrefString(stringBuilder.toString());
        PreferencesHelper.setPrefString(appDatabase, categoryPref);
    }
}