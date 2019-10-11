package com.dsource.idc.jellowintl.makemyboard.iAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

public class BaseViewHolder extends RecyclerView.ViewHolder  {

    private final SparseArray<View> views;

    private final Context context;

    public View convertView;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    public View getConvertView() {
        return convertView;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public View setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return view;
    }

    public View setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return view;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return this;
    }

    public BaseViewHolder setImageUrl(int viewId, String imageUrl, int defResourceId) {
        ImageView view = getView(viewId);
        Glide.with(context).load(imageUrl).placeholder(defResourceId).into(view);
        return this;
    }

    public BaseViewHolder setImageUrl(int viewId, String imageUrl, int defResourceId, BitmapTransformation... transformations) {
        ImageView view = getView(viewId);
        Glide.with(context).load(imageUrl).placeholder(defResourceId).transform(transformations).into(view);
        return this;
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


    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    public void setItemClickListener(int resId, final OnItemClickListener listener) {
        View view = getView(resId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getAdapterPosition());
            }
        });
    }

    public void setImageFromBoard(int viewId, String imageURL) {
        ImageView imageView = getView(viewId);
        File en_dir =context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(context)
                .load(path+"/"+imageURL+".png")
                .transform(new CircleCrop())
                .error(R.drawable.ic_board_person)
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

    public void setImageFromLibrary(int viewId,String drawableId){
        ImageView imageView = getView(viewId);
        GlideApp.with(context).load(getIconPath(context, drawableId+EXTENSION))
                .into(imageView);
    }
}
