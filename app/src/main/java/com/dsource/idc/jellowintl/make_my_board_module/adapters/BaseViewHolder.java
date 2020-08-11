package com.dsource.idc.jellowintl.make_my_board_module.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;

import static android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO;
import static android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES;
import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    private final Context context;

    private View convertView;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public void setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
    }

    public View setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return view;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public void setItemViewBackgroundColor(int colorResId) {
        itemView.setBackgroundColor(context.getResources().getColor(colorResId));
    }

    public void setVisibility(View view, int visibility){
        view.setVisibility(visibility);
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public void setImageFromBoard(int viewId, String imageURL) {
        ImageView imageView = getView(viewId);
        File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(context)
                .load(path + "/" + imageURL + ".png")
                .transform(new CircleCrop())
                .error(R.drawable.ic_board_person)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }

    public void setImageFromBoard(int viewId, String imageURL,int placeHolderResId) {
        ImageView imageView = getView(viewId);
        File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(context)
                .load(path + "/" + imageURL + ".png")
                .transform(new CircleCrop())
                .error(placeHolderResId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }


    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
    }

    public void setBackgroundDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setBackground(drawable);
    }

    public void setImageFromLibrary(int viewId, String drawableId) {
        ImageView imageView = getView(viewId);
        GlideApp.with(context).load(getIconPath(context, drawableId + EXTENSION))
                .into(imageView);
    }

    public void setMenuImageBorder(int gradientResId, boolean setBorder, int pos) {

        GradientDrawable gd = (GradientDrawable) getView(gradientResId).getBackground();

        if (gd == null)
            return;

        if (setBorder) {
            switch (pos) {
                case -1:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorSelect));
                    break;
                case 0:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorLike));
                    break;
                case 1:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorYes));
                    break;
                case 2:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorMore));
                    break;
                case 3:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorDontLike));
                    break;
                case 4:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorNo));
                    break;
                case 5:
                    gd.setColor(ContextCompat.getColor(context, R.color.colorLess));
                    break;
            }
        } else
            gd.setColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    public void setSetAccessibility(int viewId, boolean enable) {
        if (!enable){
            getView(viewId).setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
        }else{
            getView(viewId).setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    public void setDescriptionToIcon(int edit_icons, String description) {
        getView(edit_icons).setContentDescription(description);
    }
}
